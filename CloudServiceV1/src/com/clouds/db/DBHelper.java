package com.clouds.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper dBHelper = null;
    private static final int version = 3;

    static {
        dBHelper = null;
    }

    public DBHelper(Context context) {
        super(context, "download.db", null, 3);
    }

    public static DBHelper getDBHelperInstanca(Context context) {
        if (dBHelper == null) {
            dBHelper = new DBHelper(context);
        }
        return dBHelper;
    }

    public synchronized void close() {
        super.close();
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table download_info(_id integer PRIMARY KEY AUTOINCREMENT, thread_id integer, start_pos integer, end_pos integer, compelete_size integer,url char)");
        db.execSQL("create table system_info(clientver integer,logover integer, bhurlver integer, wallppver integer,screenshotsver integer, appsver integer,sysInfo char)");
        db.execSQL("insert into system_info(clientver,logover, bhurlver, wallppver,screenshotsver, appsver, sysInfo) values (?,?,?,?,?,?,?)", new Object[]{Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), "sysInfo"});
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS  download_info");
            db.execSQL("DROP TABLE IF EXISTS  system_info");
        }
        onCreate(db);
    }
}