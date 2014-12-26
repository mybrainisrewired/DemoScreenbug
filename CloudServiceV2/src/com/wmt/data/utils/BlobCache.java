package com.wmt.data.utils;

import android.util.Log;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.zip.Adler32;
import org.codehaus.jackson.smile.SmileConstants;

public class BlobCache {
    private static final int BH_CHECKSUM = 8;
    private static final int BH_KEY = 0;
    private static final int BH_LENGTH = 16;
    private static final int BH_OFFSET = 12;
    private static final int BLOB_HEADER_SIZE = 20;
    private static final int DATA_HEADER_SIZE = 4;
    private static final int IH_ACTIVE_BYTES = 20;
    private static final int IH_ACTIVE_ENTRIES = 16;
    private static final int IH_ACTIVE_REGION = 12;
    private static final int IH_CHECKSUM = 28;
    private static final int IH_MAGIC = 0;
    private static final int IH_MAX_BYTES = 8;
    private static final int IH_MAX_ENTRIES = 4;
    private static final int IH_VERSION = 24;
    private static final int INDEX_HEADER_SIZE = 32;
    private static final int MAGIC_DATA_FILE = -1121680112;
    private static final int MAGIC_INDEX_FILE = -1289277392;
    private static final String TAG = "BlobCache";
    private int mActiveBytes;
    private RandomAccessFile mActiveDataFile;
    private int mActiveEntries;
    private int mActiveHashStart;
    private int mActiveRegion;
    private Adler32 mAdler32;
    private byte[] mBlobHeader;
    private RandomAccessFile mDataFile0;
    private RandomAccessFile mDataFile1;
    private int mFileOffset;
    private RandomAccessFile mInactiveDataFile;
    private int mInactiveHashStart;
    private MappedByteBuffer mIndexBuffer;
    private FileChannel mIndexChannel;
    private RandomAccessFile mIndexFile;
    private byte[] mIndexHeader;
    private LookupRequest mLookupRequest;
    private int mMaxBytes;
    private int mMaxEntries;
    private int mSlotOffset;
    private int mVersion;

    public static class LookupRequest {
        public byte[] buffer;
        public long key;
        public int length;
    }

    public BlobCache(String path, int maxEntries, int maxBytes, boolean reset) throws IOException {
        this(path, maxEntries, maxBytes, reset, 0);
    }

    public BlobCache(String path, int maxEntries, int maxBytes, boolean reset, int version) throws IOException {
        this.mIndexHeader = new byte[32];
        this.mBlobHeader = new byte[20];
        this.mAdler32 = new Adler32();
        this.mLookupRequest = new LookupRequest();
        this.mIndexFile = new RandomAccessFile(path + ".idx", "rw");
        this.mDataFile0 = new RandomAccessFile(path + ".0", "rw");
        this.mDataFile1 = new RandomAccessFile(path + ".1", "rw");
        this.mVersion = version;
        if (reset || !loadIndex()) {
            resetCache(maxEntries, maxBytes);
            if (!loadIndex()) {
                closeAll();
                throw new IOException("unable to load index");
            }
        }
    }

    private void clearHash(int hashStart) {
        byte[] zero = new byte[1024];
        this.mIndexBuffer.position(hashStart);
        int count = this.mMaxEntries * 12;
        while (count > 0) {
            int todo = Math.min(count, SmileConstants.MAX_SHARED_STRING_VALUES);
            this.mIndexBuffer.put(zero, IH_MAGIC, todo);
            count -= todo;
        }
    }

    private void closeAll() {
        closeSilently(this.mIndexChannel);
        closeSilently(this.mIndexFile);
        closeSilently(this.mDataFile0);
        closeSilently(this.mDataFile1);
    }

    static void closeSilently(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (Throwable th) {
            }
        }
    }

    private static void deleteFileSilently(String path) {
        try {
            new File(path).delete();
        } catch (Throwable th) {
        }
    }

    public static void deleteFiles(String path) {
        deleteFileSilently(path + ".idx");
        deleteFileSilently(path + ".0");
        deleteFileSilently(path + ".1");
    }

    private void flipRegion() throws IOException {
        this.mActiveRegion = 1 - this.mActiveRegion;
        this.mActiveEntries = 0;
        this.mActiveBytes = 4;
        writeInt(this.mIndexHeader, IH_ACTIVE_REGION, this.mActiveRegion);
        writeInt(this.mIndexHeader, IH_ACTIVE_ENTRIES, this.mActiveEntries);
        writeInt(this.mIndexHeader, IH_ACTIVE_BYTES, this.mActiveBytes);
        updateIndexHeader();
        setActiveVariables();
        clearHash(this.mActiveHashStart);
        syncIndex();
    }

    private boolean getBlob(RandomAccessFile file, int offset, LookupRequest req) throws IOException {
        byte[] header = this.mBlobHeader;
        long oldPosition = file.getFilePointer();
        try {
            file.seek((long) offset);
            if (file.read(header) != 20) {
                Log.w(TAG, "cannot read blob header");
                file.seek(oldPosition);
                return false;
            } else {
                long blobKey = readLong(header, IH_MAGIC);
                if (blobKey != req.key) {
                    Log.w(TAG, "blob key does not match: " + blobKey);
                    file.seek(oldPosition);
                    return false;
                } else {
                    int sum = readInt(header, IH_MAX_BYTES);
                    int blobOffset = readInt(header, IH_ACTIVE_REGION);
                    if (blobOffset != offset) {
                        Log.w(TAG, "blob offset does not match: " + blobOffset);
                        file.seek(oldPosition);
                        return false;
                    } else {
                        int length = readInt(header, IH_ACTIVE_ENTRIES);
                        if (length < 0 || length > this.mMaxBytes - offset - 20) {
                            Log.w(TAG, "invalid blob length: " + length);
                            file.seek(oldPosition);
                            return false;
                        } else {
                            if (req.buffer == null || req.buffer.length < length) {
                                req.buffer = new byte[length];
                            }
                            byte[] blob = req.buffer;
                            req.length = length;
                            if (file.read(blob, IH_MAGIC, length) != length) {
                                Log.w(TAG, "cannot read blob data");
                                file.seek(oldPosition);
                                return false;
                            } else if (checkSum(blob, IH_MAGIC, length) != sum) {
                                Log.w(TAG, "blob checksum does not match: " + sum);
                                file.seek(oldPosition);
                                return false;
                            } else {
                                file.seek(oldPosition);
                                return true;
                            }
                        }
                    }
                }
            }
        } catch (Throwable th) {
            Log.e(TAG, "getBlob failed.", th);
            file.seek(oldPosition);
            return false;
        }
    }

    private void insertInternal(long key, byte[] data, int length) throws IOException {
        byte[] header = this.mBlobHeader;
        int sum = checkSum(data);
        writeLong(header, IH_MAGIC, key);
        writeInt(header, IH_MAX_BYTES, sum);
        writeInt(header, IH_ACTIVE_REGION, this.mActiveBytes);
        writeInt(header, IH_ACTIVE_ENTRIES, length);
        this.mActiveDataFile.write(header);
        this.mActiveDataFile.write(data, IH_MAGIC, length);
        this.mIndexBuffer.putLong(this.mSlotOffset, key);
        this.mIndexBuffer.putInt(this.mSlotOffset + 8, this.mActiveBytes);
        this.mActiveBytes += length + 20;
        writeInt(this.mIndexHeader, IH_ACTIVE_BYTES, this.mActiveBytes);
    }

    private boolean loadIndex() {
        boolean z = false;
        try {
            this.mIndexFile.seek(0);
            this.mDataFile0.seek(0);
            this.mDataFile1.seek(0);
            byte[] buf = this.mIndexHeader;
            if (this.mIndexFile.read(buf) != 32) {
                Log.w(TAG, "cannot read header");
                return z;
            } else if (readInt(buf, IH_MAGIC) != -1289277392) {
                Log.w(TAG, "cannot read header magic");
                return z;
            } else if (readInt(buf, IH_VERSION) != this.mVersion) {
                Log.w(TAG, "version mismatch");
                return z;
            } else {
                this.mMaxEntries = readInt(buf, IH_MAX_ENTRIES);
                this.mMaxBytes = readInt(buf, IH_MAX_BYTES);
                this.mActiveRegion = readInt(buf, IH_ACTIVE_REGION);
                this.mActiveEntries = readInt(buf, IH_ACTIVE_ENTRIES);
                this.mActiveBytes = readInt(buf, IH_ACTIVE_BYTES);
                if (checkSum(buf, IH_MAGIC, IH_CHECKSUM) != readInt(buf, IH_CHECKSUM)) {
                    Log.w(TAG, "header checksum does not match");
                    return z;
                } else if (this.mMaxEntries <= 0) {
                    Log.w(TAG, "invalid max entries");
                    return z;
                } else if (this.mMaxBytes <= 0) {
                    Log.w(TAG, "invalid max bytes");
                    return z;
                } else if (this.mActiveRegion != 0 && this.mActiveRegion != 1) {
                    Log.w(TAG, "invalid active region");
                    return z;
                } else if (this.mActiveEntries < 0 || this.mActiveEntries > this.mMaxEntries) {
                    Log.w(TAG, "invalid active entries");
                    return z;
                } else if (this.mActiveBytes < 4 || this.mActiveBytes > this.mMaxBytes) {
                    Log.w(TAG, "invalid active bytes");
                    return z;
                } else if (this.mIndexFile.length() != ((long) ((this.mMaxEntries * 12) * 2 + 32))) {
                    Log.w(TAG, "invalid index file length");
                    return z;
                } else {
                    byte[] magic = new byte[4];
                    if (this.mDataFile0.read(magic) != 4) {
                        Log.w(TAG, "cannot read data file magic");
                        return z;
                    } else if (readInt(magic, IH_MAGIC) != -1121680112) {
                        Log.w(TAG, "invalid data file magic");
                        return z;
                    } else if (this.mDataFile1.read(magic) != 4) {
                        Log.w(TAG, "cannot read data file magic");
                        return z;
                    } else if (readInt(magic, IH_MAGIC) != -1121680112) {
                        Log.w(TAG, "invalid data file magic");
                        return z;
                    } else {
                        this.mIndexChannel = this.mIndexFile.getChannel();
                        this.mIndexBuffer = this.mIndexChannel.map(MapMode.READ_WRITE, 0, this.mIndexFile.length());
                        this.mIndexBuffer.order(ByteOrder.LITTLE_ENDIAN);
                        setActiveVariables();
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "loadIndex failed.", e);
            return z;
        }
    }

    private boolean lookupInternal(long key, int hashStart) {
        int slot = (int) (key % ((long) this.mMaxEntries));
        if (slot < 0) {
            slot += this.mMaxEntries;
        }
        int slotBegin = slot;
        while (true) {
            int offset = hashStart + slot * 12;
            long candidateKey = this.mIndexBuffer.getLong(offset);
            int candidateOffset = this.mIndexBuffer.getInt(offset + 8);
            if (candidateOffset == 0) {
                this.mSlotOffset = offset;
                return false;
            } else if (candidateKey == key) {
                this.mSlotOffset = offset;
                this.mFileOffset = candidateOffset;
                return true;
            } else {
                slot++;
                if (slot >= this.mMaxEntries) {
                    slot = IH_MAGIC;
                }
                if (slot == slotBegin) {
                    Log.w(TAG, "corrupted index: clear the slot.");
                    this.mIndexBuffer.putInt(slot * 12 + hashStart + 8, IH_MAGIC);
                }
            }
        }
    }

    static int readInt(byte[] buf, int offset) {
        return (((buf[offset] & 255) | ((buf[offset + 1] & 255) << 8)) | ((buf[offset + 2] & 255) << 16)) | ((buf[offset + 3] & 255) << 24);
    }

    static long readLong(byte[] buf, int offset) {
        long result = (long) (buf[offset + 7] & 255);
        int i = FragmentManagerImpl.ANIM_STYLE_FADE_EXIT;
        while (i >= 0) {
            result = (result << 8) | ((long) (buf[offset + i] & 255));
            i--;
        }
        return result;
    }

    private void resetCache(int maxEntries, int maxBytes) throws IOException {
        this.mIndexFile.setLength(0);
        this.mIndexFile.setLength((long) ((maxEntries * 12) * 2 + 32));
        this.mIndexFile.seek(0);
        byte[] buf = this.mIndexHeader;
        writeInt(buf, IH_MAGIC, MAGIC_INDEX_FILE);
        writeInt(buf, IH_MAX_ENTRIES, maxEntries);
        writeInt(buf, IH_MAX_BYTES, maxBytes);
        writeInt(buf, IH_ACTIVE_REGION, IH_MAGIC);
        writeInt(buf, IH_ACTIVE_ENTRIES, IH_MAGIC);
        writeInt(buf, IH_ACTIVE_BYTES, IH_MAX_ENTRIES);
        writeInt(buf, IH_VERSION, this.mVersion);
        writeInt(buf, IH_CHECKSUM, checkSum(buf, IH_MAGIC, IH_CHECKSUM));
        this.mIndexFile.write(buf);
        this.mDataFile0.setLength(0);
        this.mDataFile1.setLength(0);
        this.mDataFile0.seek(0);
        this.mDataFile1.seek(0);
        writeInt(buf, IH_MAGIC, MAGIC_DATA_FILE);
        this.mDataFile0.write(buf, IH_MAGIC, IH_MAX_ENTRIES);
        this.mDataFile1.write(buf, IH_MAGIC, IH_MAX_ENTRIES);
    }

    private void setActiveVariables() throws IOException {
        this.mActiveDataFile = this.mActiveRegion == 0 ? this.mDataFile0 : this.mDataFile1;
        this.mInactiveDataFile = this.mActiveRegion == 1 ? this.mDataFile0 : this.mDataFile1;
        this.mActiveDataFile.setLength((long) this.mActiveBytes);
        this.mActiveDataFile.seek((long) this.mActiveBytes);
        this.mActiveHashStart = 32;
        this.mInactiveHashStart = 32;
        if (this.mActiveRegion == 0) {
            this.mInactiveHashStart += this.mMaxEntries * 12;
        } else {
            this.mActiveHashStart += this.mMaxEntries * 12;
        }
    }

    private void updateIndexHeader() {
        writeInt(this.mIndexHeader, IH_CHECKSUM, checkSum(this.mIndexHeader, IH_MAGIC, IH_CHECKSUM));
        this.mIndexBuffer.position(IH_MAGIC);
        this.mIndexBuffer.put(this.mIndexHeader);
    }

    static void writeInt(byte[] buf, int offset, int value) {
        int i = IH_MAGIC;
        while (i < 4) {
            buf[offset + i] = (byte) (value & 255);
            value >>= 8;
            i++;
        }
    }

    static void writeLong(byte[] buf, int offset, long value) {
        int i = IH_MAGIC;
        while (i < 8) {
            buf[offset + i] = (byte) ((int) (255 & value));
            value >>= 8;
            i++;
        }
    }

    int checkSum(byte[] data) {
        this.mAdler32.reset();
        this.mAdler32.update(data);
        return (int) this.mAdler32.getValue();
    }

    int checkSum(byte[] data, int offset, int nbytes) {
        this.mAdler32.reset();
        this.mAdler32.update(data, offset, nbytes);
        return (int) this.mAdler32.getValue();
    }

    public void close() {
        syncAll();
        closeAll();
    }

    int getActiveCount() {
        int count = IH_MAGIC;
        int i = IH_MAGIC;
        while (i < this.mMaxEntries) {
            int offset = this.mActiveHashStart + i * 12;
            long candidateKey = this.mIndexBuffer.getLong(offset);
            if (this.mIndexBuffer.getInt(offset + 8) != 0) {
                count++;
            }
            i++;
        }
        if (count == this.mActiveEntries) {
            return count;
        }
        Log.e(TAG, "wrong active count: " + this.mActiveEntries + " vs " + count);
        return -1;
    }

    public void insert(long key, byte[] data) throws IOException {
        if (data.length + 24 > this.mMaxBytes) {
            throw new RuntimeException("blob is too large!");
        }
        if (this.mActiveBytes + 20 + data.length > this.mMaxBytes || this.mActiveEntries * 2 >= this.mMaxEntries) {
            flipRegion();
        }
        if (!lookupInternal(key, this.mActiveHashStart)) {
            this.mActiveEntries++;
            writeInt(this.mIndexHeader, IH_ACTIVE_ENTRIES, this.mActiveEntries);
        }
        insertInternal(key, data, data.length);
        updateIndexHeader();
    }

    public boolean lookup(LookupRequest req) throws IOException {
        if (lookupInternal(req.key, this.mActiveHashStart) && getBlob(this.mActiveDataFile, this.mFileOffset, req)) {
            return true;
        }
        int insertOffset = this.mSlotOffset;
        if (!lookupInternal(req.key, this.mInactiveHashStart) || !getBlob(this.mInactiveDataFile, this.mFileOffset, req)) {
            return false;
        }
        if (this.mActiveBytes + 20 + req.length > this.mMaxBytes || this.mActiveEntries * 2 >= this.mMaxEntries) {
            return true;
        }
        this.mSlotOffset = insertOffset;
        try {
            insertInternal(req.key, req.buffer, req.length);
            this.mActiveEntries++;
            writeInt(this.mIndexHeader, IH_ACTIVE_ENTRIES, this.mActiveEntries);
            updateIndexHeader();
            return true;
        } catch (Throwable th) {
            Log.e(TAG, "cannot copy over");
            return true;
        }
    }

    public byte[] lookup(long key) throws IOException {
        this.mLookupRequest.key = key;
        this.mLookupRequest.buffer = null;
        return lookup(this.mLookupRequest) ? this.mLookupRequest.buffer : null;
    }

    public void syncAll() {
        syncIndex();
        try {
            this.mDataFile0.getFD().sync();
        } catch (Throwable th) {
            Log.w(TAG, "sync data file 0 failed", th);
        }
        try {
            this.mDataFile1.getFD().sync();
        } catch (Throwable th2) {
            Log.w(TAG, "sync data file 1 failed", th2);
        }
    }

    public void syncIndex() {
        try {
            this.mIndexBuffer.force();
        } catch (Throwable th) {
            Log.w(TAG, "sync index failed", th);
        }
    }
}