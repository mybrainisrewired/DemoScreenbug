package com.android.volley.toolbox;

import android.os.SystemClock;
import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.VolleyLog;
import com.mopub.common.Preconditions;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class DiskBasedCache implements Cache {
    private static final int CACHE_VERSION = 2;
    private static final int DEFAULT_DISK_USAGE_BYTES = 5242880;
    private static final float HYSTERESIS_FACTOR = 0.9f;
    private final Map<String, CacheHeader> mEntries;
    private final int mMaxCacheSizeInBytes;
    private final File mRootDirectory;
    private long mTotalSize;

    private static class CacheHeader {
        public String etag;
        public String key;
        public Map<String, String> responseHeaders;
        public long serverDate;
        public long size;
        public long softTtl;
        public long ttl;

        private CacheHeader() {
        }

        public CacheHeader(String key, Entry entry) {
            this.key = key;
            this.size = (long) entry.data.length;
            this.etag = entry.etag;
            this.serverDate = entry.serverDate;
            this.ttl = entry.ttl;
            this.softTtl = entry.softTtl;
            this.responseHeaders = entry.responseHeaders;
        }

        public static CacheHeader readHeader(InputStream is) throws IOException {
            CacheHeader entry = new CacheHeader();
            ObjectInputStream ois = new ObjectInputStream(is);
            if (ois.readByte() != 2) {
                throw new IOException();
            }
            entry.key = ois.readUTF();
            entry.etag = ois.readUTF();
            if (entry.etag.equals(Preconditions.EMPTY_ARGUMENTS)) {
                entry.etag = null;
            }
            entry.serverDate = ois.readLong();
            entry.ttl = ois.readLong();
            entry.softTtl = ois.readLong();
            entry.responseHeaders = readStringStringMap(ois);
            return entry;
        }

        private static Map<String, String> readStringStringMap(ObjectInputStream ois) throws IOException {
            Map<String, String> result;
            int size = ois.readInt();
            if (size == 0) {
                result = Collections.emptyMap();
            } else {
                result = new HashMap(size);
            }
            int i = 0;
            while (i < size) {
                result.put(ois.readUTF().intern(), ois.readUTF().intern());
                i++;
            }
            return result;
        }

        private static void writeStringStringMap(Map<String, String> map, ObjectOutputStream oos) throws IOException {
            if (map != null) {
                oos.writeInt(map.size());
                Iterator it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry) it.next();
                    oos.writeUTF((String) entry.getKey());
                    oos.writeUTF((String) entry.getValue());
                }
            } else {
                oos.writeInt(0);
            }
        }

        public Entry toCacheEntry(byte[] data) {
            Entry e = new Entry();
            e.data = data;
            e.etag = this.etag;
            e.serverDate = this.serverDate;
            e.ttl = this.ttl;
            e.softTtl = this.softTtl;
            e.responseHeaders = this.responseHeaders;
            return e;
        }

        public boolean writeHeader(OutputStream os) {
            boolean z = true;
            boolean z2 = false;
            try {
                ObjectOutputStream oos = new ObjectOutputStream(os);
                oos.writeByte(CACHE_VERSION);
                oos.writeUTF(this.key);
                oos.writeUTF(this.etag == null ? Preconditions.EMPTY_ARGUMENTS : this.etag);
                oos.writeLong(this.serverDate);
                oos.writeLong(this.ttl);
                oos.writeLong(this.softTtl);
                writeStringStringMap(this.responseHeaders, oos);
                oos.flush();
                return z;
            } catch (IOException e) {
                String str = "%s";
                Object[] objArr = new Object[z];
                objArr[z2] = e.toString();
                VolleyLog.d(str, objArr);
                return z2;
            }
        }
    }

    private static class CountingInputStream extends FilterInputStream {
        private int bytesRead;

        private CountingInputStream(InputStream in) {
            super(in);
            this.bytesRead = 0;
        }

        public int read() throws IOException {
            int result = super.read();
            if (result != -1) {
                this.bytesRead++;
            }
            return result;
        }

        public int read(byte[] buffer, int offset, int count) throws IOException {
            int result = super.read(buffer, offset, count);
            if (result != -1) {
                this.bytesRead += result;
            }
            return result;
        }
    }

    public DiskBasedCache(File rootDirectory) {
        this(rootDirectory, 5242880);
    }

    public DiskBasedCache(File rootDirectory, int maxCacheSizeInBytes) {
        this.mEntries = new LinkedHashMap(16, 0.75f, true);
        this.mTotalSize = 0;
        this.mRootDirectory = rootDirectory;
        this.mMaxCacheSizeInBytes = maxCacheSizeInBytes;
    }

    private String getFilenameForKey(String key) {
        int firstHalfLength = key.length() / 2;
        return new StringBuilder(String.valueOf(String.valueOf(key.substring(0, firstHalfLength).hashCode()))).append(String.valueOf(key.substring(firstHalfLength).hashCode())).toString();
    }

    private void pruneIfNeeded(int neededSpace) {
        if (this.mTotalSize + ((long) neededSpace) >= ((long) this.mMaxCacheSizeInBytes)) {
            if (VolleyLog.DEBUG) {
                VolleyLog.v("Pruning old cache entries.", new Object[0]);
            }
            long before = this.mTotalSize;
            int prunedFiles = 0;
            long startTime = SystemClock.elapsedRealtime();
            Iterator<Map.Entry<String, CacheHeader>> iterator = this.mEntries.entrySet().iterator();
            while (iterator.hasNext()) {
                CacheHeader e = (CacheHeader) ((Map.Entry) iterator.next()).getValue();
                if (getFileForKey(e.key).delete()) {
                    this.mTotalSize -= e.size;
                } else {
                    VolleyLog.d("Could not delete cache entry for key=%s, filename=%s", new Object[]{e.key, getFilenameForKey(e.key)});
                }
                iterator.remove();
                prunedFiles++;
                if (((float) (this.mTotalSize + ((long) neededSpace))) < ((float) this.mMaxCacheSizeInBytes) * 0.9f) {
                    break;
                }
            }
            if (VolleyLog.DEBUG) {
                VolleyLog.v("pruned %d files, %d bytes, %d ms", new Object[]{Integer.valueOf(prunedFiles), Long.valueOf(this.mTotalSize - before), Long.valueOf(SystemClock.elapsedRealtime() - startTime)});
            }
        }
    }

    private void putEntry(String key, CacheHeader entry) {
        if (this.mEntries.containsKey(key)) {
            this.mTotalSize += entry.size - ((CacheHeader) this.mEntries.get(key)).size;
        } else {
            this.mTotalSize += entry.size;
        }
        this.mEntries.put(key, entry);
    }

    private void removeEntry(String key) {
        CacheHeader entry = (CacheHeader) this.mEntries.get(key);
        if (entry != null) {
            this.mTotalSize -= entry.size;
            this.mEntries.remove(key);
        }
    }

    private static byte[] streamToBytes(InputStream in, int length) throws IOException {
        byte[] bytes = new byte[length];
        int pos = 0;
        while (pos < length) {
            int count = in.read(bytes, pos, length - pos);
            if (count == -1) {
                break;
            }
            pos += count;
        }
        if (pos == length) {
            return bytes;
        }
        throw new IOException(new StringBuilder("Expected ").append(length).append(" bytes, read ").append(pos).append(" bytes").toString());
    }

    public synchronized void clear() {
        int i = 0;
        synchronized (this) {
            File[] files = this.mRootDirectory.listFiles();
            if (files != null) {
                int length = files.length;
                while (i < length) {
                    files[i].delete();
                    i++;
                }
            }
            this.mEntries.clear();
            this.mTotalSize = 0;
            VolleyLog.d("Cache cleared.", new Object[0]);
        }
    }

    public synchronized Entry get(String key) {
        IOException e;
        Throwable th;
        Entry entry = null;
        synchronized (this) {
            CacheHeader entry2 = (CacheHeader) this.mEntries.get(key);
            if (entry2 != null) {
                File file = getFileForKey(key);
                CountingInputStream cis = null;
                try {
                    CountingInputStream cis2 = new CountingInputStream(null);
                    try {
                        CacheHeader.readHeader(cis2);
                        Entry toCacheEntry = entry2.toCacheEntry(streamToBytes(cis2, (int) (file.length() - ((long) cis2.bytesRead))));
                        if (cis2 != null) {
                            try {
                                cis2.close();
                            } catch (IOException e2) {
                            }
                        }
                        entry = toCacheEntry;
                    } catch (IOException e3) {
                        e = e3;
                        cis = cis2;
                        VolleyLog.d("%s: %s", new Object[]{file.getAbsolutePath(), e.toString()});
                        remove(key);
                        if (cis != null) {
                            cis.close();
                        }
                        return entry;
                    } catch (Throwable th2) {
                        th = th2;
                        cis = cis2;
                        if (cis != null) {
                            cis.close();
                        }
                        throw th;
                    }
                } catch (IOException e4) {
                    e = e4;
                    try {
                        VolleyLog.d("%s: %s", new Object[]{file.getAbsolutePath(), e.toString()});
                        remove(key);
                        if (cis != null) {
                            try {
                                cis.close();
                            } catch (IOException e5) {
                            }
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        if (cis != null) {
                            try {
                                cis.close();
                            } catch (IOException e6) {
                            }
                        }
                        throw th;
                    }
                    return entry;
                }
            }
        }
        return entry;
    }

    public File getFileForKey(String key) {
        return new File(this.mRootDirectory, getFilenameForKey(key));
    }

    public synchronized void initialize() {
        throw new UnsupportedOperationException("Method not decompiled: com.android.volley.toolbox.DiskBasedCache.initialize():void");
        /* JADX: method processing error */
/*
        Error: jadx.core.utils.exceptions.JadxRuntimeException: Try/catch wrap count limit reached in com.android.volley.toolbox.DiskBasedCache.initialize():void
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:52)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:40)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:16)
	at jadx.core.ProcessClass.process(ProcessClass.java:22)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:209)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:133)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1145)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615)
	at java.lang.Thread.run(Thread.java:745)
*/
        /*
        r10 = this;
        r6 = 0;
        monitor-enter(r10);
        r7 = r10.mRootDirectory;	 Catch:{ all -> 0x0065 }
        r7 = r7.exists();	 Catch:{ all -> 0x0065 }
        if (r7 != 0) goto L_0x0025;
    L_0x000a:
        r6 = r10.mRootDirectory;	 Catch:{ all -> 0x0065 }
        r6 = r6.mkdirs();	 Catch:{ all -> 0x0065 }
        if (r6 != 0) goto L_0x0023;
    L_0x0012:
        r6 = "Unable to create cache dir %s";
        r7 = 1;
        r7 = new java.lang.Object[r7];	 Catch:{ all -> 0x0065 }
        r8 = 0;
        r9 = r10.mRootDirectory;	 Catch:{ all -> 0x0065 }
        r9 = r9.getAbsolutePath();	 Catch:{ all -> 0x0065 }
        r7[r8] = r9;	 Catch:{ all -> 0x0065 }
        com.android.volley.VolleyLog.e(r6, r7);	 Catch:{ all -> 0x0065 }
    L_0x0023:
        monitor-exit(r10);
        return;
    L_0x0025:
        r7 = r10.mRootDirectory;	 Catch:{ all -> 0x0065 }
        r3 = r7.listFiles();	 Catch:{ all -> 0x0065 }
        if (r3 == 0) goto L_0x0023;
    L_0x002d:
        r7 = r3.length;	 Catch:{ all -> 0x0065 }
    L_0x002e:
        if (r6 >= r7) goto L_0x0023;
    L_0x0030:
        r2 = r3[r6];	 Catch:{ all -> 0x0065 }
        r4 = 0;
        r5 = new java.io.FileInputStream;	 Catch:{ IOException -> 0x0050 }
        r5.<init>(r2);	 Catch:{ IOException -> 0x0050 }
        r1 = com.android.volley.toolbox.DiskBasedCache.CacheHeader.readHeader(r5);	 Catch:{ IOException -> 0x0070, all -> 0x006d }
        r8 = r2.length();	 Catch:{ IOException -> 0x0070, all -> 0x006d }
        r1.size = r8;	 Catch:{ IOException -> 0x0070, all -> 0x006d }
        r8 = r1.key;	 Catch:{ IOException -> 0x0070, all -> 0x006d }
        r10.putEntry(r8, r1);	 Catch:{ IOException -> 0x0070, all -> 0x006d }
        if (r5 == 0) goto L_0x0073;
    L_0x0049:
        r5.close();	 Catch:{ IOException -> 0x0068 }
        r4 = r5;
    L_0x004d:
        r6 = r6 + 1;
        goto L_0x002e;
    L_0x0050:
        r0 = move-exception;
    L_0x0051:
        if (r2 == 0) goto L_0x0056;
    L_0x0053:
        r2.delete();	 Catch:{ all -> 0x005e }
    L_0x0056:
        if (r4 == 0) goto L_0x004d;
    L_0x0058:
        r4.close();	 Catch:{ IOException -> 0x005c }
        goto L_0x004d;
    L_0x005c:
        r8 = move-exception;
        goto L_0x004d;
    L_0x005e:
        r6 = move-exception;
    L_0x005f:
        if (r4 == 0) goto L_0x0064;
    L_0x0061:
        r4.close();	 Catch:{ IOException -> 0x006b }
    L_0x0064:
        throw r6;	 Catch:{ all -> 0x0065 }
    L_0x0065:
        r6 = move-exception;
        monitor-exit(r10);
        throw r6;
    L_0x0068:
        r8 = move-exception;
        r4 = r5;
        goto L_0x004d;
    L_0x006b:
        r7 = move-exception;
        goto L_0x0064;
    L_0x006d:
        r6 = move-exception;
        r4 = r5;
        goto L_0x005f;
    L_0x0070:
        r0 = move-exception;
        r4 = r5;
        goto L_0x0051;
    L_0x0073:
        r4 = r5;
        goto L_0x004d;
        */
    }

    public synchronized void invalidate(String key, boolean fullExpire) {
        Entry entry = get(key);
        if (entry != null) {
            entry.softTtl = 0;
            if (fullExpire) {
                entry.ttl = 0;
            }
            put(key, entry);
        }
    }

    public synchronized void put(String key, Entry entry) {
        pruneIfNeeded(entry.data.length);
        File file = getFileForKey(key);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            CacheHeader e = new CacheHeader(key, entry);
            e.writeHeader(fos);
            fos.write(entry.data);
            fos.close();
            putEntry(key, e);
        } catch (IOException e2) {
            if (!file.delete()) {
                VolleyLog.d("Could not clean up file %s", new Object[]{file.getAbsolutePath()});
            }
        }
    }

    public synchronized void remove(String key) {
        boolean deleted = getFileForKey(key).delete();
        removeEntry(key);
        if (!deleted) {
            VolleyLog.d("Could not delete cache entry for key=%s, filename=%s", new Object[]{key, getFilenameForKey(key)});
        }
    }
}