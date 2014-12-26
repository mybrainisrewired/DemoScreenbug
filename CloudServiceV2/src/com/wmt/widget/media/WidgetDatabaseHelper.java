package com.wmt.widget.media;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import com.wmt.util.Utils;
import java.util.ArrayList;
import java.util.Iterator;

public class WidgetDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "launcher.db";
    private static final int DATABASE_VERSION = 5;
    private static final String FIELD_ALBUM_PATH = "albumPath";
    private static final String FIELD_APPWIDGET_ID = "appWidgetId";
    private static final String FIELD_IMAGE_URI = "imageUri";
    private static final String FIELD_MEDIA_TYPE = "mediaType";
    private static final String FIELD_PHOTO_BLOB = "photoBlob";
    private static final String FIELD_WIDGET_TYPE = "widgetType";
    private static final int INDEX_ALBUM_PATH = 3;
    private static final int INDEX_IMAGE_URI = 1;
    private static final int INDEX_MEDIA_TYPE = 4;
    private static final int INDEX_PHOTO_BLOB = 2;
    private static final int INDEX_WIDGET_TYPE = 0;
    public static final int PHOTO_MEDIA_TYPE = 1;
    private static final String[] PROJECTION;
    private static final String TABLE_WIDGETS = "widgets";
    private static final String TAG = "WidgetDatabaseHelper";
    public static final int TYPE_ALBUM = 1;
    public static final int TYPE_SINGLE_PHOTO = 0;
    public static final int TYPE_STORAGE = 2;
    public static final int VIDEO_MEDIA_TYPE = 2;
    private static final String WHERE_CLAUSE = "appWidgetId = ? AND mediaType = ?";

    public static class Entry {
        public String albumPath;
        public byte[] imageData;
        public String imageUri;
        public int mediaType;
        public int type;
        public int widgetId;

        private Entry() {
        }

        private Entry(int id, Cursor cursor) {
            this.widgetId = id;
            this.type = cursor.getInt(TYPE_SINGLE_PHOTO);
            if (this.type == 0) {
                this.imageUri = cursor.getString(TYPE_ALBUM);
                this.imageData = cursor.getBlob(VIDEO_MEDIA_TYPE);
            } else if (this.type == 1 || this.type == 2) {
                this.albumPath = cursor.getString(INDEX_ALBUM_PATH);
            }
            this.mediaType = cursor.getInt(INDEX_MEDIA_TYPE);
        }
    }

    static {
        PROJECTION = new String[]{FIELD_WIDGET_TYPE, FIELD_IMAGE_URI, FIELD_PHOTO_BLOB, FIELD_ALBUM_PATH, FIELD_MEDIA_TYPE};
    }

    public WidgetDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 5);
    }

    private void restoreData(SQLiteDatabase db, ArrayList<Entry> data) {
        db.beginTransaction();
        Iterator i$ = data.iterator();
        while (i$.hasNext()) {
            Entry entry = (Entry) i$.next();
            ContentValues values = new ContentValues();
            values.put(FIELD_APPWIDGET_ID, Integer.valueOf(entry.widgetId));
            values.put(FIELD_WIDGET_TYPE, Integer.valueOf(entry.type));
            values.put(FIELD_IMAGE_URI, entry.imageUri);
            values.put(FIELD_PHOTO_BLOB, entry.imageData);
            values.put(FIELD_ALBUM_PATH, entry.albumPath);
            db.insert(TABLE_WIDGETS, null, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    private void saveData(SQLiteDatabase db, int oldVersion, ArrayList<Entry> data) {
        Cursor cursor;
        Entry entry;
        if (oldVersion <= 2) {
            cursor = db.query("photos", new String[]{FIELD_APPWIDGET_ID, FIELD_PHOTO_BLOB}, null, null, null, null, null);
            if (cursor == null) {
                return;
            }
            while (cursor.moveToNext()) {
                entry = new Entry();
                entry.type = 0;
                entry.widgetId = cursor.getInt(TYPE_SINGLE_PHOTO);
                entry.imageData = cursor.getBlob(TYPE_ALBUM);
                data.add(entry);
            }
            cursor.close();
        } else if (oldVersion == 3) {
            cursor = db.query("photos", new String[]{FIELD_APPWIDGET_ID, FIELD_PHOTO_BLOB, FIELD_IMAGE_URI}, null, null, null, null, null);
            if (cursor == null) {
                return;
            }
            while (cursor.moveToNext()) {
                entry = new Entry();
                entry.type = 0;
                entry.widgetId = cursor.getInt(TYPE_SINGLE_PHOTO);
                entry.imageData = cursor.getBlob(TYPE_ALBUM);
                entry.imageUri = cursor.getString(VIDEO_MEDIA_TYPE);
                data.add(entry);
            }
            cursor.close();
        }
    }

    public void deleteEntry(int appWidgetId, int mediaType) {
        try {
            getWritableDatabase().delete(TABLE_WIDGETS, WHERE_CLAUSE, new String[]{String.valueOf(appWidgetId), String.valueOf(mediaType)});
        } catch (SQLiteException e) {
            Log.e(TAG, "Could not delete photo from database", e);
        }
    }

    public Entry getEntry(int appWidgetId, int mediaType) {
        Entry entry = null;
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(TABLE_WIDGETS, PROJECTION, WHERE_CLAUSE, new String[]{String.valueOf(appWidgetId), String.valueOf(mediaType)}, null, null, null);
            if (cursor == null || !cursor.moveToNext()) {
                Log.e(TAG, "query fail: empty cursor: " + cursor);
                Utils.closeSilently(cursor);
                return entry;
            } else {
                Entry entry2 = new Entry(cursor, null);
                Utils.closeSilently(cursor);
                return entry2;
            }
        } catch (Throwable th) {
            Log.e(TAG, "Could not load photo from database", th);
            Utils.closeSilently(cursor);
            return entry;
        }
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE widgets (appWidgetId INTEGER PRIMARY KEY, mediaType INTEGER DEFAULT 0, widgetType INTEGER DEFAULT 0, imageUri TEXT, albumPath TEXT, photoBlob BLOB)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != 5) {
            ArrayList<Entry> data = new ArrayList();
            saveData(db, oldVersion, data);
            Log.w(TAG, "destroying all old data.");
            db.execSQL("DROP TABLE IF EXISTS photos");
            db.execSQL("DROP TABLE IF EXISTS widgets");
            onCreate(db);
            restoreData(db, data);
        }
    }

    public boolean setPhoto(int appWidgetId, Uri imageUri, Bitmap bitmap, int mediaType) {
        boolean z = false;
        try {
            ContentValues values = new ContentValues();
            values.put(FIELD_APPWIDGET_ID, Integer.valueOf(appWidgetId));
            values.put(FIELD_MEDIA_TYPE, Integer.valueOf(mediaType));
            values.put(FIELD_WIDGET_TYPE, Integer.valueOf(TYPE_SINGLE_PHOTO));
            values.put(FIELD_IMAGE_URI, imageUri.toString());
            getWritableDatabase().replaceOrThrow(TABLE_WIDGETS, null, values);
            return true;
        } catch (Throwable th) {
            Log.e(TAG, "set widget photo fail", th);
            return z;
        }
    }

    public boolean setWidget(int id, int type, String albumPath, int mediaType) {
        try {
            ContentValues values = new ContentValues();
            values.put(FIELD_APPWIDGET_ID, Integer.valueOf(id));
            values.put(FIELD_MEDIA_TYPE, Integer.valueOf(mediaType));
            values.put(FIELD_WIDGET_TYPE, Integer.valueOf(type));
            values.put(FIELD_ALBUM_PATH, Utils.ensureNotNull(albumPath));
            getWritableDatabase().replaceOrThrow(TABLE_WIDGETS, null, values);
            return true;
        } catch (Throwable th) {
            Log.e(TAG, "set widget fail", th);
            return false;
        }
    }
}