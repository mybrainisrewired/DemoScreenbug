package com.isssss.myadv.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "advshowcount.db";
    private static final int DB_VERSION = 4;

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 4);
    }

    public void onCreate(SQLiteDatabase db) {
        Log.d("DB helper", "onCreate");
        LocalAppTable.createTable(db);
        AdvConfigTable.createTable(db);
        PushIdsTable.createTable(db);
        BannerConfigTable.createTable(db);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DB helper", "onUpgrade");
        LocalAppTable.dropTable(db);
        AdvConfigTable.dropTable(db);
        PushIdsTable.dropTable(db);
        BannerConfigTable.dropTable(db);
        onCreate(db);
    }
}