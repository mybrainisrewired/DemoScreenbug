package com.isssss.myadv.dao;

import android.database.sqlite.SQLiteDatabase;

public class PushIdsTable {
    public static final String COLUMN_ID = "_id";
    public static final String PUSH_ID = "pushID";
    public static final String TABLE_NAME = "pushHistory";

    public static final void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE pushHistory (_id INTEGER NOT NULL PRIMARY KEY ,pushID INTEGER NOT NULL );");
    }

    public static final void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS pushHistory");
    }
}