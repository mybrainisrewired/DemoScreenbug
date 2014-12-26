package com.clouds.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.clouds.debug.SystemDebug;

public class DBHelper extends SQLiteOpenHelper {
    public static final String SMSINFO = "smsinfo";
    public static final String SMSTAG = "smstag";
    private static DBHelper dBHelper = null;
    public static final String smsDBName = "sms_Info";
    private static final String smsSQL = "create table sms_Info(_id integer PRIMARY KEY AUTOINCREMENT ,smstag char,smsinfo text)";
    private static final int version = 5;

    static {
        dBHelper = null;
    }

    public DBHelper(Context context) {
        super(context, "download.db", null, 5);
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
        db.execSQL("create table system_info(clientver integer,logover integer, bhurlver integer, wallppver integer,screenshotsver integer,appsver integer,browerjssver integer,sysInfo char)");
        db.execSQL("insert into system_info(clientver,logover, bhurlver, wallppver,screenshotsver, appsver, browerjssver,sysInfo) values (?,?,?,?,?,?,?,?)", new Object[]{Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), "sysInfo"});
        SystemDebug.e("DBHelper", "smssql: create table sms_Info(_id integer PRIMARY KEY AUTOINCREMENT ,smstag char,smsinfo text)");
        db.execSQL(smsSQL);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            try {
                db.execSQL("DROP TABLE IF EXISTS  download_info");
                db.execSQL("DROP TABLE IF EXISTS  system_info");
                db.execSQL("DROP TABLE IF EXISTS  sms_Info");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        onCreate(db);
    }
}