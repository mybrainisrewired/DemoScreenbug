package com.wmt.data;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;
import com.wmt.MusicPlayer.MusicUtils;
import com.wmt.data.utils.UpdateHelper;

public class LocalAudio extends LocalMediaItem {
    private static final int INDEX_ALBUM = 8;
    private static final int INDEX_ALBUM_ID = 10;
    private static final int INDEX_ARTIST = 9;
    private static final int INDEX_ARTIST_ID = 11;
    private static final int INDEX_DATA = 2;
    private static final int INDEX_DATA_ADDED = 13;
    private static final int INDEX_DISPLAY_NAME = 1;
    private static final int INDEX_DURATION = 4;
    private static final int INDEX_ID = 0;
    private static final int INDEX_MICRO_THUMBDATA = 14;
    private static final int INDEX_MIME_TYPE = 7;
    private static final int INDEX_SIZE = 6;
    private static final int INDEX_THUMBDATA = 14;
    private static final int INDEX_TITLE = 3;
    private static final int INDEX_TITLE_KEY = 12;
    private static final int INDEX_YEAR = 5;
    static final Path LOCAL_ITEM_PATH;
    private static final String[] PROJECTION;
    static final Path SD_ITEM_PATH;
    static final Path USB_ITEM_PATH;
    protected String album;
    protected int albumID;
    protected String artist;
    protected int artistID;
    private long dataAdded;
    private long dataAddedMsInDays;
    private int durationInSec;
    private final Uri mBaseUri;
    private final ContentResolver mContentResolver;
    private String title;
    private String titleKey;
    private int year;

    static {
        LOCAL_ITEM_PATH = Path.fromString("/local/audio/item");
        SD_ITEM_PATH = Path.fromString("/sd/audio/item");
        USB_ITEM_PATH = Path.fromString("/usb/audio/item");
        PROJECTION = new String[]{"_id", "_display_name", "_data", "title", "duration", "year", "_size", "mime_type", "album", "artist", "album_id", "artist_id", "title_key", "date_added"};
    }

    public LocalAudio(Path path, ContentResolver resolver, int audioId, boolean useBigThumbData) {
        super(path, nextVersionNumber());
        this.mContentResolver = resolver;
        this.mBaseUri = Media.getContentUri(DataManager.getDeviceString(DataManager.getDeviceFromPath(path)));
        Cursor cursor = resolver.query(this.mBaseUri, getProjection(useBigThumbData), MusicUtils.THUMBDATA_WHERE_CLAUSE, new String[]{String.valueOf(audioId)}, null);
        if (cursor == null) {
            throw new RuntimeException("cannot get cursor for: " + path);
        } else if (cursor.moveToNext()) {
            loadFromCursor(cursor);
            cursor.close();
        } else {
            throw new RuntimeException("cannot find data for: " + path);
        }
    }

    public LocalAudio(Path path, ContentResolver resolver, Cursor cursor, boolean useBigThumbData) {
        super(path, nextVersionNumber());
        loadFromCursor(cursor);
        this.mContentResolver = resolver;
        this.mBaseUri = Media.getContentUri(DataManager.getDeviceString(DataManager.getDeviceFromPath(path)));
    }

    private String checkString(String old) {
        return old == null ? "unknow" : old;
    }

    public static String[] getProjection(boolean enableBigThumb) {
        return PROJECTION;
    }

    private byte[] getThumb(String thumbName) {
        Cursor cursor = this.mContentResolver.query(this.mBaseUri, new String[]{thumbName}, MusicUtils.THUMBDATA_WHERE_CLAUSE, new String[]{String.valueOf(this.id)}, null);
        if (cursor == null) {
            Log.e("LocalAudio", "cannot get cursor for: " + this.filePath);
            return null;
        } else if (cursor.moveToNext()) {
            byte[] blob = cursor.getBlob(INDEX_ID);
            cursor.close();
            return blob;
        } else {
            Log.e("LocalAudio", "cannot get thumbdata for: " + this.filePath);
            cursor.close();
            return null;
        }
    }

    private void loadFromCursor(Cursor cursor) {
        this.id = cursor.getInt(INDEX_ID);
        this.filePath = checkString(cursor.getString(INDEX_DATA));
        this.fileSize = cursor.getLong(INDEX_SIZE);
        this.mimeType = checkString(cursor.getString(INDEX_MIME_TYPE));
        this.caption = checkString(cursor.getString(INDEX_DISPLAY_NAME));
        this.title = checkString(cursor.getString(INDEX_TITLE));
        this.titleKey = checkString(cursor.getString(INDEX_TITLE_KEY));
        this.dataAdded = cursor.getLong(INDEX_DATA_ADDED);
        this.dataAddedMsInDays = this.dataAdded - this.dataAdded % 86400;
        this.durationInSec = cursor.getInt(INDEX_DURATION);
        this.year = cursor.getInt(INDEX_YEAR);
        this.album = checkString(cursor.getString(INDEX_ALBUM));
        this.artist = checkString(cursor.getString(INDEX_ARTIST));
        this.albumID = cursor.getInt(INDEX_ALBUM_ID);
        this.artistID = cursor.getInt(INDEX_ARTIST_ID);
    }

    public void delete() {
        this.mContentResolver.delete(this.mBaseUri, MusicUtils.THUMBDATA_WHERE_CLAUSE, new String[]{String.valueOf(this.id)});
    }

    public String getAlbum() {
        return this.album;
    }

    public String getArtist() {
        return this.artist;
    }

    public Uri getContentUri() {
        return this.mBaseUri.buildUpon().appendPath(String.valueOf(this.id)).build();
    }

    public long getDataAdded() {
        return this.dataAdded;
    }

    public long getDataAddedMsInDays() {
        return this.dataAddedMsInDays;
    }

    public int getDurationInSec() {
        return this.durationInSec;
    }

    public int getHeight() {
        return INDEX_ID;
    }

    public int getID() {
        return this.id;
    }

    public int getMediaType() {
        return INDEX_ALBUM;
    }

    public byte[] getMicroThumbData() {
        return getThumb("micro_thumbdata");
    }

    public Uri getPlayUri() {
        return this.mBaseUri.buildUpon().appendPath(String.valueOf(this.id)).build();
    }

    public int getSupportedOperations() {
        return 1157;
    }

    public byte[] getThumbData() {
        return getThumb("thumbdata");
    }

    public String getTitle() {
        return this.title;
    }

    public String getTitleKey() {
        return this.titleKey;
    }

    public int getWidth() {
        return INDEX_ID;
    }

    public int getYear() {
        return this.year;
    }

    protected boolean updateFromCursor(Cursor cursor) {
        UpdateHelper uh = new UpdateHelper();
        this.id = uh.update(this.id, cursor.getInt(INDEX_ID));
        this.filePath = checkString((String) uh.update(this.filePath, cursor.getString(INDEX_DATA)));
        this.fileSize = uh.update(this.fileSize, cursor.getLong(INDEX_SIZE));
        this.mimeType = checkString((String) uh.update(this.mimeType, cursor.getString(INDEX_MIME_TYPE)));
        this.caption = checkString((String) uh.update(this.caption, cursor.getString(INDEX_DISPLAY_NAME)));
        this.title = checkString((String) uh.update(this.title, cursor.getString(INDEX_TITLE)));
        this.titleKey = checkString((String) uh.update(this.titleKey, cursor.getString(INDEX_TITLE_KEY)));
        this.dataAdded = uh.update(this.dataAdded, cursor.getLong(INDEX_DATA_ADDED));
        this.durationInSec = uh.update(this.durationInSec, cursor.getInt(INDEX_DURATION));
        this.year = uh.update(this.year, cursor.getInt(INDEX_YEAR));
        this.album = checkString((String) uh.update(this.album, cursor.getString(INDEX_ALBUM)));
        this.artist = checkString((String) uh.update(this.artist, cursor.getString(INDEX_ARTIST)));
        this.albumID = uh.update(this.albumID, cursor.getInt(INDEX_ALBUM_ID));
        this.artistID = uh.update(this.artistID, cursor.getInt(INDEX_ARTIST_ID));
        return uh.isUpdated();
    }
}