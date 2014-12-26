package com.wmt.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import com.wmt.MusicPlayer.MusicUtils;
import com.wmt.data.utils.BitmapUtils;
import com.wmt.data.utils.ReverseGeocoder;
import com.wmt.data.utils.UpdateHelper;
import com.wmt.util.Utils;
import java.io.File;
import java.io.IOException;

public class LocalImage extends LocalMediaItem {
    private static final String[] EMULATOR_PROJECTION;
    private static final int INDEX_BUCKET_ID = 10;
    private static final int INDEX_CAPTION = 1;
    private static final int INDEX_COLOR_MAJOR_INDEX = 16;
    private static final int INDEX_DATA = 8;
    private static final int INDEX_DATE_ADDED = 6;
    private static final int INDEX_DATE_MODIFIED = 7;
    private static final int INDEX_DATE_TAKEN = 5;
    private static final int INDEX_DISPLAYNAME = 14;
    private static final int INDEX_HEIGHT = 13;
    private static final int INDEX_ID = 0;
    private static final int INDEX_LATITUDE = 3;
    private static final int INDEX_LONGITUDE = 4;
    private static final int INDEX_MIME_TYPE = 2;
    private static final int INDEX_ORIENTATION = 9;
    private static final int INDEX_SIZE_ID = 11;
    private static final int INDEX_THUMBDATA = 15;
    private static final int INDEX_WIDTH = 12;
    static final Path LOCAL_ITEM_PATH;
    private static final String[] PROJECTION;
    static final Path SD_ITEM_PATH;
    private static final String TAG = "LocalImage";
    static final Path USB_ITEM_PATH;
    public int colorIndex;
    public int height;
    final Uri mBaseUri;
    final ContentResolver mContentResolver;
    public int rotation;
    public byte[] thumbData;
    public int width;

    static {
        LOCAL_ITEM_PATH = Path.fromString("/local/image/item");
        SD_ITEM_PATH = Path.fromString("/sd/image/item");
        USB_ITEM_PATH = Path.fromString("/usb/image/item");
        EMULATOR_PROJECTION = new String[]{"_id", "title", "mime_type", "latitude", "longitude", "datetaken", "date_added", "date_modified", "_data", "orientation", "bucket_id", "_size", "width", "height", "_display_name"};
        PROJECTION = new String[]{"_id", "title", "mime_type", "latitude", "longitude", "datetaken", "date_added", "date_modified", "_data", "orientation", "bucket_id", "_size", "width", "height", "_display_name", "thumbdata", "color_major_index"};
    }

    public LocalImage(Path path, ContentResolver resolver, int id) {
        super(path, nextVersionNumber());
        this.mBaseUri = getBaseUri(path);
        Cursor cursor = LocalAlbum.getItemCursor(resolver, this.mBaseUri, getProjection(), id);
        if (cursor == null) {
            throw new RuntimeException("cannot get cursor for: " + path);
        } else if (cursor.moveToNext()) {
            loadFromCursor(cursor);
            cursor.close();
            this.mContentResolver = resolver;
        } else {
            throw new RuntimeException("cannot find data for: " + path);
        }
    }

    public LocalImage(Path path, ContentResolver resolver, Cursor cursor) {
        super(path, nextVersionNumber());
        loadFromCursor(cursor);
        this.mContentResolver = resolver;
        this.mBaseUri = getBaseUri(path);
    }

    private static final Uri getBaseUri(Path path) {
        return Media.getContentUri(DataManager.getDeviceString(DataManager.getDeviceFromPath(path)));
    }

    public static String getExifOrientation(int orientation) {
        switch (orientation) {
            case INDEX_ID:
                return String.valueOf(INDEX_CAPTION);
            case ReverseGeocoder.LAT_MAX:
                return String.valueOf(INDEX_DATE_ADDED);
            case ReverseGeocoder.LON_MAX:
                return String.valueOf(INDEX_LATITUDE);
            case 270:
                return String.valueOf(INDEX_DATA);
            default:
                throw new AssertionError("invalid: " + orientation);
        }
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
        this.rotation = cursor.getInt(INDEX_ORIENTATION);
        this.bucketId = cursor.getInt(INDEX_BUCKET_ID);
        this.fileSize = cursor.getLong(INDEX_SIZE_ID);
        this.width = cursor.getInt(INDEX_WIDTH);
        this.height = cursor.getInt(INDEX_HEIGHT);
        this.displayName = cursor.getString(INDEX_DISPLAYNAME);
        if (!Utils.isEmulator()) {
            this.thumbData = cursor.getBlob(INDEX_THUMBDATA);
            this.colorIndex = cursor.getInt(INDEX_COLOR_MAJOR_INDEX);
        }
    }

    public void delete() {
        this.mContentResolver.delete(this.mBaseUri, MusicUtils.THUMBDATA_WHERE_CLAUSE, new String[]{String.valueOf(this.id)});
    }

    public int getColorMajorIndex() {
        return this.colorIndex;
    }

    public Uri getContentUri() {
        return this.mBaseUri.buildUpon().appendPath(String.valueOf(this.id)).build();
    }

    public MediaDetails getDetails() {
        MediaDetails details = super.getDetails();
        details.addDetail(INDEX_DATE_MODIFIED, Integer.valueOf(this.rotation));
        MediaDetails.extractExifInfo(details, this.filePath);
        return details;
    }

    public int getHeight() {
        return this.height;
    }

    public int getID() {
        return this.id;
    }

    public int getMediaType() {
        return INDEX_MIME_TYPE;
    }

    public int getRotation() {
        return this.rotation;
    }

    public int getSupportedOperations() {
        int operation = 1581;
        if (BitmapUtils.isSupportedByRegionDecoder(this.mimeType)) {
            operation = 1581 | 64;
        }
        if (BitmapUtils.isRotationSupported(this.mimeType)) {
            operation |= 2;
        }
        return Utils.isValidLocation(this.latitude, this.longitude) ? operation | 16 : operation;
    }

    public byte[] getThumbData() {
        return this.thumbData;
    }

    public int getWidth() {
        return this.width;
    }

    public void rotate(int degrees) {
        ContentValues values = new ContentValues();
        int rotation = (this.rotation + degrees) % 360;
        if (rotation < 0) {
            rotation += 360;
        }
        if (this.mimeType.equalsIgnoreCase("image/jpeg")) {
            try {
                ExifInterface exif = new ExifInterface(this.filePath);
                exif.setAttribute("Orientation", getExifOrientation(rotation));
                exif.saveAttributes();
            } catch (IOException e) {
                Log.w(TAG, "cannot set exif data: " + this.filePath);
            }
            this.fileSize = new File(this.filePath).length();
            values.put("_size", Long.valueOf(this.fileSize));
        }
        values.put("orientation", Integer.valueOf(rotation));
        this.mContentResolver.update(this.mBaseUri, values, MusicUtils.THUMBDATA_WHERE_CLAUSE, new String[]{String.valueOf(this.id)});
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
        this.rotation = uh.update(this.rotation, cursor.getInt(INDEX_ORIENTATION));
        this.bucketId = uh.update(this.bucketId, cursor.getInt(INDEX_BUCKET_ID));
        this.fileSize = uh.update(this.fileSize, cursor.getLong(INDEX_SIZE_ID));
        this.width = uh.update(this.width, cursor.getInt(INDEX_WIDTH));
        this.height = uh.update(this.height, cursor.getInt(INDEX_HEIGHT));
        this.displayName = (String) uh.update(this.displayName, cursor.getString(INDEX_DISPLAYNAME));
        if (!Utils.isEmulator()) {
            this.thumbData = (byte[]) uh.update(this.thumbData, cursor.getBlob(INDEX_THUMBDATA));
            this.colorIndex = uh.update(this.colorIndex, (int)INDEX_COLOR_MAJOR_INDEX);
        }
        return uh.isUpdated();
    }
}