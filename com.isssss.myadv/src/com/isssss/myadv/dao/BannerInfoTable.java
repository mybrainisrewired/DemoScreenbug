package com.isssss.myadv.dao;

import android.database.sqlite.SQLiteDatabase;

public class BannerInfoTable {
    public static final String COLUMN_APK_PATH = "apkpath";
    public static final String COLUMN_APK_URL = "apkurl";
    public static final String COLUMN_APPID = "appID";
    public static final String COLUMN_BACKGROUND_PATH = "backgroundpath";
    public static final String COLUMN_BACKGROUND_URL = "backgroundurl";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DOWNLOADED = "downloaded";
    public static final String COLUMN_DOWNTYPE = "downtype";
    public static final String COLUMN_ICON_PATH = "iconpath";
    public static final String COLUMN_ICON_URL = "iconurl";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String TABLE_NAME = "bannerinfo";

    public static final void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE bannerinfo ( _id INTEGER NOT NULL ,appID INTEGER NOT NULL ,title TEXT NOT NULL ,description TEXT NOT NULL ,iconurl TEXT NOT NULL ,iconpath TEXT NOT NULL ,apkpath TEXT NOT NULL ,apkurl TEXT NOT NULL ,backgroundpath TEXT NOT NULL ,backgroundurl TEXT NOT NULL ,downloaded INTEGER NOT NULL ,downtype INTEGER NOT NULL );");
    }

    public static final void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS bannerinfo");
    }
}