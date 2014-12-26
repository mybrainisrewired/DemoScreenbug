package com.isssss.myadv.dao;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class AdvConfigTable implements BaseColumns {
    public static final String COLUMN_ADV_COMPANY = "advcompany";
    public static final String COLUMN_ADV_ID = "advid";
    public static final String COLUMN_CUSTOM_IP = "customip";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SHIPMENT_TIME = "shipmenttime";
    public static final String COLUMN_SHOW = "show";
    public static final String COLUMN_TIMELY = "timely";
    public static final String COLUMN_WHITE_LIST = "whitelist";
    public static final String TABLE_NAME = "advconfig";

    public static final void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE advconfig (_id INTEGER NOT NULL PRIMARY KEY ,timely INTEGER NOT NULL ,show INTEGER NOT NULL ,whitelist TEXT NOT NULL ,advcompany INTEGER NOT NULL ,advid TEXT NOT NULL ,customip TEXT NOT NULL , shipmenttime LONG NO NULL  ); ");
    }

    public static final void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS advconfig");
    }
}