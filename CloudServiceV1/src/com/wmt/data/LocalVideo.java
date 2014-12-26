package com.wmt.data;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Video.Media;
import com.wmt.MusicPlayer.MusicUtils;
import com.wmt.data.utils.UpdateHelper;
import com.wmt.util.Utils;
import java.io.File;

public class LocalVideo extends LocalMediaItem {
    static final String[] EMULATOR_PROJECTION;
    private static final int INDEX_BUCKET_ID = 10;
    private static final int INDEX_CAPTION = 1;
    private static final int INDEX_DATA = 8;
    private static final int INDEX_DATE_ADDED = 6;
    private static final int INDEX_DATE_MODIFIED = 7;
    private static final int INDEX_DATE_TAKEN = 5;
    private static final int INDEX_DISPLAY_NAME = 12;
    private static final int INDEX_DURATION = 9;
    private static final int INDEX_HEIGHT = 14;
    private static final int INDEX_ID = 0;
    private static final int INDEX_LATITUDE = 3;
    private static final int INDEX_LONGITUDE = 4;
    private static final int INDEX_MIME_TYPE = 2;
    private static final int INDEX_SIZE_ID = 11;
    private static final int INDEX_THUMBDATA = 15;
    private static final int INDEX_WIDTH = 13;
    static final Path LOCAL_ITEM_PATH;
    static final String[] PROJECTION;
    static final Path SD_ITEM_PATH;
    static final Path USB_ITEM_PATH;
    private int durationInSec;
    private int height;
    private final Uri mBaseUri;
    private final ContentResolver mContentResolver;
    private byte[] thumbData;
    private int width;

    static {
        LOCAL_ITEM_PATH = Path.fromString("/local/video/item");
        SD_ITEM_PATH = Path.fromString("/sd/video/item");
        USB_ITEM_PATH = Path.fromString("/usb/video/item");
        EMULATOR_PROJECTION = new String[]{"_id", "title", "mime_type", "latitude", "longitude", "datetaken", "date_added", "date_modified", "_data", "duration", "bucket_id", "_size", "_display_name", "width", "height"};
        PROJECTION = new String[]{"_id", "title", "mime_type", "latitude", "longitude", "datetaken", "date_added", "date_modified", "_data", "duration", "bucket_id", "_size", "_display_name", "width", "height", "thumbdata"};
    }

    public LocalVideo(Path path, ContentResolver resolver, int id) {
        super(path, nextVersionNumber());
        this.mContentResolver = resolver;
        this.mBaseUri = getBaseUri(path);
        Cursor cursor = LocalAlbum.getItemCursor(resolver, this.mBaseUri, getProjection(), id);
        if (cursor == null) {
            throw new RuntimeException("cannot get cursor for: " + path);
        } else if (cursor.moveToNext()) {
            loadFromCursor(cursor);
            cursor.close();
        } else {
            throw new RuntimeException("cannot find data for: " + path);
        }
    }

    public LocalVideo(Path path, ContentResolver resolver, Cursor cursor) {
        super(path, nextVersionNumber());
        this.mContentResolver = resolver;
        loadFromCursor(cursor);
        this.mBaseUri = getBaseUri(path);
    }

    private static final Uri getBaseUri(Path path) {
        return Media.getContentUri(DataManager.getDeviceString(DataManager.getDeviceFromPath(path)));
    }

    public static String[] getProjection() {
        return !Utils.isEmulator() ? PROJECTION : EMULATOR_PROJECTION;
    }

    private void loadFromCursor(Cursor cursor) {
        this.id = cursor.getInt(INDEX_ID);
        this.caption = cursor.getString(INDEX_CAPTION);
        this.mimeType = cursor.getString(INDEX_MIME_TYPE);
        this.latitude = cursor.getDouble(INDEX_LATITUDE);
        this.longitude = cursor.getDouble(INDEX_LONGITUDE);
        this.dateTakenInMs = cursor.getLong(INDEX_DATE_TAKEN);
        this.filePath = cursor.getString(INDEX_DATA);
        this.durationInSec = cursor.getInt(INDEX_DURATION) / 1000;
        this.bucketId = cursor.getInt(INDEX_BUCKET_ID);
        this.fileSize = cursor.getLong(INDEX_SIZE_ID);
        this.displayName = cursor.getString(INDEX_DISPLAY_NAME);
        if (!Utils.isEmulator()) {
            this.thumbData = cursor.getBlob(INDEX_THUMBDATA);
        }
        this.width = cursor.getInt(INDEX_WIDTH);
        this.height = cursor.getInt(INDEX_HEIGHT);
    }

    public void delete() {
        this.mContentResolver.delete(this.mBaseUri, MusicUtils.THUMBDATA_WHERE_CLAUSE, new String[]{String.valueOf(this.id)});
    }

    public Uri getContentUri() {
        return this.mBaseUri.buildUpon().appendPath(String.valueOf(this.id)).build();
    }

    public MediaDetails getDetails() {
        MediaDetails details = super.getDetails();
        if (this.durationInSec > 0) {
            details.addDetail(INDEX_DATA, Integer.valueOf(this.durationInSec));
        }
        return details;
    }

    public int getDurationInSec() {
        return this.durationInSec;
    }

    public int getHeight() {
        return this.height;
    }

    public int getID() {
        return this.id;
    }

    public int getMediaType() {
        return INDEX_LONGITUDE;
    }

    public Uri getPlayUri() {
        return Uri.fromFile(new File(this.filePath));
    }

    public int getSupportedOperations() {
        return 1157;
    }

    public byte[] getThumbData() {
        return this.thumbData;
    }

    public int getWidth() {
        return this.width;
    }

    public void rotate(int degrees) {
    }

    protected boolean updateFromCursor(Cursor cursor) {
        UpdateHelper uh = new UpdateHelper();
        this.id = uh.update(this.id, cursor.getInt(INDEX_ID));
        this.caption = (String) uh.update(this.caption, cursor.getString(INDEX_CAPTION));
        this.mimeType = (String) uh.update(this.mimeType, cursor.getString(INDEX_MIME_TYPE));
        this.latitude = uh.update(this.latitude, cursor.getDouble(INDEX_LATITUDE));
        this.longitude = uh.update(this.longitude, cursor.getDouble(INDEX_LONGITUDE));
        this.dateTakenInMs = uh.update(this.dateTakenInMs, cursor.getLong(INDEX_DATE_TAKEN));
        this.dateAddedInSec = uh.update(this.dateAddedInSec, cursor.getLong(INDEX_DATE_ADDED));
        this.dateModifiedInSec = uh.update(this.dateModifiedInSec, cursor.getLong(INDEX_DATE_MODIFIED));
        this.filePath = (String) uh.update(this.filePath, cursor.getString(INDEX_DATA));
        this.durationInSec = uh.update(this.durationInSec, cursor.getInt(INDEX_DURATION) / 1000);
        this.bucketId = uh.update(this.bucketId, cursor.getInt(INDEX_BUCKET_ID));
        this.fileSize = uh.update(this.fileSize, cursor.getLong(INDEX_SIZE_ID));
        this.displayName = (String) uh.update(this.displayName, cursor.getString(INDEX_DISPLAY_NAME));
        if (!Utils.isEmulator()) {
            this.thumbData = (byte[]) uh.update(this.thumbData, cursor.getBlob(INDEX_THUMBDATA));
        }
        this.width = uh.update(this.width, cursor.getInt(INDEX_WIDTH));
        this.height = uh.update(this.height, cursor.getInt(INDEX_HEIGHT));
        return uh.isUpdated();
    }
}