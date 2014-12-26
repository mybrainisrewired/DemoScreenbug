package com.wmt.data;

public abstract class MediaItem extends MediaObject {
    public static final int IMAGE_ERROR = -1;
    public static final int IMAGE_READY = 0;
    public static final int IMAGE_WAIT = 1;
    public static final double INVALID_LATLNG = 0.0d;
    public static final int TYPE_MICROTHUMBNAIL = 2;
    public static final int TYPE_THUMBNAIL = 1;
    public boolean mIsDeleted;

    public MediaItem(Path path, long version) {
        super(path, version);
        this.mIsDeleted = false;
    }

    public String getAlbum() {
        return null;
    }

    public int getAlbumID() {
        return IMAGE_ERROR;
    }

    public String getArtist() {
        return null;
    }

    public int getArtistID() {
        return IMAGE_ERROR;
    }

    public int getColorMajorIndex() {
        return IMAGE_READY;
    }

    public long getDataAdded() {
        return -1;
    }

    public long getDataAddedMsInDays() {
        return -1;
    }

    public long getDateInMs() {
        return 0;
    }

    public String getDisplayName() {
        return null;
    }

    public int getDurationInSec() {
        return IMAGE_READY;
    }

    public Face[] getFaces() {
        return null;
    }

    public abstract String getFilePath();

    public int getFullImageRotation() {
        return getRotation();
    }

    public abstract int getHeight();

    public boolean getIsDeleted() {
        return this.mIsDeleted;
    }

    public void getLatLong(double[] latLong) {
        latLong[0] = 0.0d;
        latLong[1] = 0.0d;
    }

    public byte[] getMicroThumbData() {
        throw new UnsupportedOperationException();
    }

    public abstract String getMimeType();

    public String getName() {
        return null;
    }

    public int getRotation() {
        return IMAGE_READY;
    }

    public long getSize() {
        return 0;
    }

    public String[] getTags() {
        return null;
    }

    public abstract byte[] getThumbData();

    public String getTitle() {
        return null;
    }

    public String getTitleKey() {
        return null;
    }

    public abstract int getWidth();

    public int getYear() {
        return IMAGE_READY;
    }

    public void setIsDeleted(boolean delected) {
        this.mIsDeleted = delected;
    }
}