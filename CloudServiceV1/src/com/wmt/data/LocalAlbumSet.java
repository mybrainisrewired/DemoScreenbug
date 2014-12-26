package com.wmt.data;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Files;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Video;
import android.util.Log;
import com.wmt.data.utils.MediaSetUtils;
import com.wmt.opengl.grid.ItemAnimation;
import com.wmt.util.Utils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

public class LocalAlbumSet extends MediaSet {
    private static final String BUCKET_GROUP_BY = ") GROUP BY 1, (2";
    private static final String BUCKET_ORDER_BY = "MAX(datetaken) DESC";
    private static final String[] COUNT_PROJECTION;
    private static final int INDEX_BUCKET_ID = 0;
    private static final int INDEX_BUCKET_NAME = 2;
    private static final int INDEX_MEDIA_TYPE = 1;
    public static final Path LOCAL_PATH_ALL;
    public static final Path LOCAL_PATH_IMAGE;
    public static final Path LOCAL_PATH_VIDEO;
    private static final String[] PROJECTION_BUCKET;
    public static final Path SD_PATH_ALL;
    public static final Path SD_PATH_IMAGE;
    public static final Path SD_PATH_VIDEO;
    private static final String TAG = "LocalAlbumSet";
    public static final Path USB_PATH_ALL;
    public static final Path USB_PATH_IMAGE;
    public static final Path USB_PATH_VIDEO;
    private ArrayList<MediaSet> mAlbums;
    private final Uri mBaseUri;
    private final ContentResolver mContentResolver;
    private final DataManager mDataManager;
    private int mFilterSize;
    private final String mName;
    private final ChangeNotifier mNotifierImage;
    private final ChangeNotifier mNotifierVideo;
    private boolean mReloadRequested;
    private final int mType;
    private final Uri mWatchUriImage;
    private final Uri mWatchUriVideo;

    private static class BucketEntry {
        public int bucketId;
        public String bucketName;

        public BucketEntry(int id, String name) {
            this.bucketId = id;
            this.bucketName = Utils.ensureNotNull(name);
        }

        public boolean equals(Object object) {
            if (!(object instanceof BucketEntry)) {
                return false;
            }
            BucketEntry entry = (BucketEntry) object;
            return this.bucketId == entry.bucketId;
        }

        public int hashCode() {
            return this.bucketId;
        }
    }

    static {
        LOCAL_PATH_ALL = Path.fromString("/local/all");
        LOCAL_PATH_IMAGE = Path.fromString("/local/image");
        LOCAL_PATH_VIDEO = Path.fromString("/local/video");
        SD_PATH_ALL = Path.fromString("/sd/all");
        SD_PATH_IMAGE = Path.fromString("/sd/image");
        SD_PATH_VIDEO = Path.fromString("/sd/video");
        USB_PATH_ALL = Path.fromString("/usb/all");
        USB_PATH_IMAGE = Path.fromString("/usb/image");
        USB_PATH_VIDEO = Path.fromString("/usb/video");
        PROJECTION_BUCKET = new String[]{"bucket_id", "media_type", "bucket_display_name"};
        COUNT_PROJECTION = new String[]{"count(*)"};
    }

    public LocalAlbumSet(DataManager dataManager, Path path, ContentResolver resolver, String setName) {
        super(path, nextVersionNumber());
        this.mFilterSize = 0;
        this.mAlbums = new ArrayList();
        this.mReloadRequested = false;
        this.mContentResolver = resolver;
        this.mType = getTypeFromPath(path);
        this.mName = setName;
        this.mDataManager = dataManager;
        String deviceStr = DataManager.getDeviceString(DataManager.getDeviceFromPath(path));
        this.mBaseUri = Files.getContentUri(deviceStr);
        this.mWatchUriImage = Media.getContentUri(deviceStr);
        this.mWatchUriVideo = Video.Media.getContentUri(deviceStr);
        this.mNotifierImage = new ChangeNotifier(this, this.mWatchUriImage, dataManager);
        this.mNotifierVideo = new ChangeNotifier(this, this.mWatchUriVideo, dataManager);
    }

    private static <T> void circularShiftRight(T[] array, int i, int j) {
        T temp = array[j];
        int k = j;
        while (k > i) {
            array[k] = array[k - 1];
            k--;
        }
        array[i] = temp;
    }

    private static int findBucket(BucketEntry[] entries, int bucketId) {
        int i = INDEX_BUCKET_ID;
        int n = entries.length;
        while (i < n) {
            if (entries[i].bucketId == bucketId) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static String getBucketName(Path path, ContentResolver resolver, int bucketId) {
        Uri uri = Files.getContentUri(DataManager.getDeviceString(DataManager.getDeviceFromPath(path))).buildUpon().appendQueryParameter("limit", "1").build();
        Cursor cursor = resolver.query(uri, PROJECTION_BUCKET, "bucket_id = ?", new String[]{String.valueOf(bucketId)}, null);
        if (cursor == null) {
            Log.w(TAG, "query fail: " + uri);
            return "";
        } else {
            String string = cursor.moveToNext() ? cursor.getString(INDEX_BUCKET_NAME) : "";
            cursor.close();
            return string;
        }
    }

    private MediaSet getLocalAlbum(int type, Path parent, int id, String name) {
        Path path = parent.getChild(id);
        MediaObject object = path.getObject();
        if (object != null) {
            return (MediaSet) object;
        }
        switch (type) {
            case INDEX_BUCKET_NAME:
                return new LocalAlbum(this.mDataManager, path, this.mContentResolver, id, true, name);
            case ItemAnimation.CUR_ARC:
                return new LocalAlbum(this.mDataManager, path, this.mContentResolver, id, false, name);
            case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                Comparator<MediaItem> comp = DataManager.sDateTakenComparator;
                Path path2 = SD_PATH_IMAGE;
                Path path3 = SD_PATH_VIDEO;
                int device = DataManager.getDeviceFromPath(parent);
                if (device == 0) {
                    path2 = LOCAL_PATH_IMAGE;
                    path3 = LOCAL_PATH_VIDEO;
                } else if (device == 1) {
                    path2 = SD_PATH_IMAGE;
                    path3 = SD_PATH_VIDEO;
                } else {
                    path2 = USB_PATH_IMAGE;
                    path3 = USB_PATH_VIDEO;
                }
                return new LocalMergeAlbum(path, comp, new MediaSet[]{getLocalAlbum(INDEX_BUCKET_NAME, path2, id, name), getLocalAlbum(ItemAnimation.CUR_ARC, path3, id, name)});
            default:
                throw new IllegalArgumentException(String.valueOf(type));
        }
    }

    private static int getTypeFromPath(Path path) {
        String[] name = path.split();
        if (name.length < 2) {
            throw new IllegalArgumentException(path.toString());
        } else if ("all".equals(name[1])) {
            return FragmentManagerImpl.ANIM_STYLE_FADE_EXIT;
        } else {
            if ("image".equals(name[1])) {
                return INDEX_BUCKET_NAME;
            }
            if ("video".equals(name[1])) {
                return ItemAnimation.CUR_ARC;
            }
            throw new IllegalArgumentException(path.toString());
        }
    }

    private BucketEntry[] loadBucketEntries(Cursor cursor) {
        ArrayList<BucketEntry> buffer = new ArrayList();
        int typeBits = INDEX_BUCKET_ID;
        if ((this.mType & 2) != 0) {
            typeBits = 0 | 2;
        }
        if ((this.mType & 4) != 0) {
            typeBits |= 8;
        }
        while (cursor.moveToNext()) {
            if (((1 << cursor.getInt(INDEX_MEDIA_TYPE)) & typeBits) != 0) {
                BucketEntry entry = new BucketEntry(cursor.getInt(INDEX_BUCKET_ID), cursor.getString(INDEX_BUCKET_NAME));
                if (!buffer.contains(entry)) {
                    buffer.add(entry);
                }
            }
        }
        cursor.close();
        return (BucketEntry[]) buffer.toArray(new BucketEntry[buffer.size()]);
    }

    private void loadDataIfNecessary() {
        if (this.mNotifierImage == null) {
            return;
        }
        if ((this.mNotifierImage.isDirty() | this.mNotifierVideo.isDirty()) != 0 || this.mReloadRequested) {
            this.mDataVersion = nextVersionNumber();
            this.mAlbums = loadSubMediaSets();
            this.mReloadRequested = false;
        }
    }

    void fakeChange() {
        this.mNotifierImage.fakeChange();
        this.mNotifierVideo.fakeChange();
    }

    public String getName() {
        return this.mName;
    }

    public synchronized MediaSet getSubMediaSet(int index) {
        MediaSet mediaSet;
        loadDataIfNecessary();
        if (index < this.mAlbums.size()) {
            mediaSet = (MediaSet) this.mAlbums.get(index);
        } else {
            mediaSet = null;
        }
        return mediaSet;
    }

    public synchronized int getSubMediaSetCount() {
        loadDataIfNecessary();
        return this.mAlbums.size();
    }

    protected ArrayList<MediaSet> loadSubMediaSets() {
        String where;
        Uri uri = this.mBaseUri;
        if (this.mType == 2) {
            where = "_size > ? and mime_type LIKE \"image%\"";
        } else if (this.mType == 4) {
            where = "_size > ? and mime_type LIKE \"video%\"";
        } else {
            where = "_size > ?";
        }
        Cursor cursor = this.mContentResolver.query(uri, PROJECTION_BUCKET, where + BUCKET_GROUP_BY, new String[]{String.valueOf(this.mFilterSize)}, BUCKET_ORDER_BY);
        if (cursor == null) {
            Log.w(TAG, "cannot open local database: " + uri);
            return new ArrayList();
        } else {
            int offset;
            BucketEntry[] entries = loadBucketEntries(cursor);
            int offset2 = INDEX_BUCKET_ID;
            int index = findBucket(entries, MediaSetUtils.CAMERA_BUCKET_ID);
            if (index != -1) {
                offset = 0 + 1;
                circularShiftRight(entries, 0, index);
                offset2 = offset;
            }
            index = findBucket(entries, MediaSetUtils.DOWNLOAD_BUCKET_ID);
            if (index != -1) {
                offset = offset2 + 1;
                circularShiftRight(entries, offset2, index);
            }
            ArrayList<MediaSet> albums = new ArrayList();
            BucketEntry[] arr$ = entries;
            int len$ = arr$.length;
            int i$ = INDEX_BUCKET_ID;
            while (i$ < len$) {
                BucketEntry entry = arr$[i$];
                MediaSet set = getLocalAlbum(this.mType, this.mPath, entry.bucketId, entry.bucketName);
                set.setFilterSize(this.mFilterSize);
                albums.add(set);
                i$++;
            }
            int i = INDEX_BUCKET_ID;
            int n = albums.size();
            while (i < n) {
                ((MediaSet) albums.get(i)).reload();
                i++;
            }
            return albums;
        }
    }

    public synchronized long reload() {
        loadDataIfNecessary();
        return this.mDataVersion;
    }

    public void setFilterSize(int sizeInByte) {
        if (this.mFilterSize != sizeInByte) {
            this.mFilterSize = sizeInByte;
            this.mReloadRequested = true;
        }
    }

    public synchronized void setOrderBy(String fieldName) {
        Iterator i$ = this.mAlbums.iterator();
        while (i$.hasNext()) {
            ((MediaSet) i$.next()).setOrderBy(fieldName);
        }
    }
}