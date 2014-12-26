package com.wmt.data;

import android.net.Uri;

public abstract class MediaObject {
    public static final int CACHE_FLAG_FULL = 2;
    public static final int CACHE_FLAG_NO = 0;
    public static final int CACHE_FLAG_SCREENNAIL = 1;
    public static final int CACHE_STATUS_CACHED_FULL = 3;
    public static final int CACHE_STATUS_CACHED_SCREENNAIL = 2;
    public static final int CACHE_STATUS_CACHING = 1;
    public static final int CACHE_STATUS_NOT_CACHED = 0;
    public static final long INVALID_DATA_VERSION = -1;
    public static final int MEDIA_TYPE_ALL = 6;
    public static final int MEDIA_TYPE_AUDIO = 8;
    public static final int MEDIA_TYPE_IMAGE = 2;
    public static final int MEDIA_TYPE_UNKNOWN = 1;
    public static final int MEDIA_TYPE_VIDEO = 4;
    public static final int SUPPORT_ALL = -1;
    public static final int SUPPORT_CACHE = 256;
    public static final int SUPPORT_CROP = 8;
    public static final int SUPPORT_DELETE = 1;
    public static final int SUPPORT_EDIT = 512;
    public static final int SUPPORT_FULL_IMAGE = 64;
    public static final int SUPPORT_IMPORT = 2048;
    public static final int SUPPORT_INFO = 1024;
    public static final int SUPPORT_PLAY = 128;
    public static final int SUPPORT_ROTATE = 2;
    public static final int SUPPORT_SETAS = 32;
    public static final int SUPPORT_SHARE = 4;
    public static final int SUPPORT_SHOW_ON_MAP = 16;
    private static final String TAG = "MediaObject";
    private static long sVersionSerial;
    protected long mDataVersion;
    protected final Path mPath;

    static {
        sVersionSerial = 0;
    }

    public MediaObject(Path path, long version) {
        path.setObject(this);
        this.mPath = path;
        this.mDataVersion = version;
    }

    public static synchronized long nextVersionNumber() {
        long j;
        synchronized (MediaObject.class) {
            j = sVersionSerial + 1;
            sVersionSerial = j;
        }
        return j;
    }

    public boolean Import() {
        throw new UnsupportedOperationException();
    }

    public void cache(int flag) {
        throw new UnsupportedOperationException();
    }

    public void delete() {
        throw new UnsupportedOperationException();
    }

    public int getCacheFlag() {
        return CACHE_STATUS_NOT_CACHED;
    }

    public long getCacheSize() {
        throw new UnsupportedOperationException();
    }

    public int getCacheStatus() {
        throw new UnsupportedOperationException();
    }

    public Uri getContentUri() {
        throw new UnsupportedOperationException();
    }

    public long getDataVersion() {
        return this.mDataVersion;
    }

    public MediaDetails getDetails() {
        return new MediaDetails();
    }

    public int getID() {
        throw new UnsupportedOperationException();
    }

    public int getMediaType() {
        return SUPPORT_DELETE;
    }

    public Path getPath() {
        return this.mPath;
    }

    public Uri getPlayUri() {
        throw new UnsupportedOperationException();
    }

    public int getSupportedOperations() {
        return CACHE_STATUS_NOT_CACHED;
    }

    public void rotate(int degrees) {
        throw new UnsupportedOperationException();
    }
}