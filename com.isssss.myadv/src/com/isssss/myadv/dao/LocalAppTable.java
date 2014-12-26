package com.isssss.myadv.dao;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class LocalAppTable implements BaseColumns {
    public static final String COLUMN_FIRST_SHOW_TIME = "firstshowtime";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SHOW_COUNT = "showcount";
    public static final String TABLE_NAME = "localappdata";

    public static final void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE localappdata (_id INTEGER NOT NULL PRIMARY KEY ,showcount INTEGER NOT NULL ,firstshowtime LONG NOT NULL );");
    }

    public static final void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS localappdata");
    }
}