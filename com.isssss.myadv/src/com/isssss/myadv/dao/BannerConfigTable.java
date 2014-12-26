package com.isssss.myadv.dao;

import android.database.sqlite.SQLiteDatabase;

public class BannerConfigTable {
    public static final String COLUMN_ADSIGN = "advsign";
    public static final String COLUMN_ADV_ID = "advId";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_POSITION = "position";
    public static final String COLUMN_SHIPMENT_TIME = "shipmenttime";
    public static final String COLUMN_SHOWS = "shows";
    public static final String COLUMN_TIME_DAILY = "timedaily";
    public static final String COLUMN_WHITE_LIST = "whitelist";
    public static final String TABLE_NAME = "banner_config";

    public static final void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE banner_config ( _id INTEGER NOT NULL PRIMARY KEY ,shipmenttime LONG NOT NULL ,shows INTEGER NOT NULL ,timedaily INTEGER NOT NULL ,advsign INTEGER NOT NULL ,position INTEGER NOT NULL ,advId INTEGER NOT NULL ,whitelist TEXT NOT NULL );");
    }

    public static final void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS banner_config");
    }
}