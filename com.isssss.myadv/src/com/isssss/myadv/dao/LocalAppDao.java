package com.isssss.myadv.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.isssss.myadv.model.LocalApp;

public class LocalAppDao {
    private static LocalAppDao instance;
    private SQLiteDatabase db;
    private DataBaseHelper helper;

    private LocalAppDao(Context context) {
        this.helper = new DataBaseHelper(context);
    }

    public static LocalAppDao getInstance(Context context) {
        if (instance == null) {
            instance = new LocalAppDao(context);
        }
        return instance;
    }

    public void clearLocalAppData() {
        LocalAppTable.dropTable(this.db);
        LocalAppTable.createTable(this.db);
    }

    public LocalApp getLocalApp() {
        LocalApp localApp = new LocalApp();
        this.db = this.helper.getReadableDatabase();
        Cursor c = this.db.query(LocalAppTable.TABLE_NAME, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            localApp.setShowCount(c.getInt(c.getColumnIndex(LocalAppTable.COLUMN_SHOW_COUNT)));
            localApp.setFirstShowTime(c.getLong(c.getColumnIndex(LocalAppTable.COLUMN_FIRST_SHOW_TIME)));
        }
        c.close();
        return localApp;
    }

    public void insertLocalAppData(Context context) {
        LocalApp localApp = getInstance(context).getLocalApp();
        localApp.setShowCount(localApp.getShowCount() + 1);
        if (0 == localApp.getFirstShowTime()) {
            localApp.setFirstShowTime(System.currentTimeMillis());
        }
        this.db = this.helper.getWritableDatabase();
        this.db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(LocalAppTable.COLUMN_SHOW_COUNT, Integer.valueOf(localApp.getShowCount()));
            values.put(LocalAppTable.COLUMN_FIRST_SHOW_TIME, Long.valueOf(localApp.getFirstShowTime()));
            this.db.insert(LocalAppTable.TABLE_NAME, null, values);
            this.db.setTransactionSuccessful();
            this.db.endTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            this.db.endTransaction();
        }
    }

    public void updateLocalAppData(int showCount) {
        this.db = this.helper.getWritableDatabase();
        this.db.execSQL("UPDATE localappdata SET showcount = ? ", new Object[]{Integer.valueOf(showCount)});
    }
}