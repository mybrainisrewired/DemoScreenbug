package com.isssss.myadv.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.isssss.myadv.model.BannerConfig;

public class BannerConfigDao {
    private static BannerConfigDao instance;
    private SQLiteDatabase db;
    private DataBaseHelper helper;

    private BannerConfigDao(Context context) {
        this.helper = new DataBaseHelper(context);
    }

    private void clearConfig() {
        this.db = this.helper.getWritableDatabase();
        this.db.delete(BannerConfigTable.TABLE_NAME, null, null);
    }

    public static BannerConfigDao getInstance(Context context) {
        if (instance == null) {
            instance = new BannerConfigDao(context);
        }
        return instance;
    }

    public BannerConfig getConfig() {
        BannerConfig config = new BannerConfig();
        this.db = this.helper.getReadableDatabase();
        Cursor c = this.db.query(BannerConfigTable.TABLE_NAME, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            config.setShows(c.getInt(c.getColumnIndex(BannerConfigTable.COLUMN_SHOWS)));
            config.setShipmentTime((long) c.getInt(c.getColumnIndex(BannerConfigTable.COLUMN_SHIPMENT_TIME)));
            config.setTimeDaily(c.getInt(c.getColumnIndex(BannerConfigTable.COLUMN_TIME_DAILY)));
            config.setWhitelist(c.getString(c.getColumnIndex(BannerConfigTable.COLUMN_WHITE_LIST)));
            config.setAdvId(c.getString(c.getColumnIndex(BannerConfigTable.COLUMN_ADV_ID)));
            config.setAdvsign(c.getInt(c.getColumnIndex(BannerConfigTable.COLUMN_ADSIGN)));
            config.setPosition(c.getInt(c.getColumnIndex(BannerConfigTable.COLUMN_POSITION)));
        }
        c.close();
        return config;
    }

    public void insertConfig(BannerConfig config) {
        clearConfig();
        this.db = this.helper.getWritableDatabase();
        this.db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(BannerConfigTable.COLUMN_SHIPMENT_TIME, Long.valueOf(config.getShipmentTime()));
            values.put(BannerConfigTable.COLUMN_SHOWS, Integer.valueOf(config.getShows()));
            values.put(BannerConfigTable.COLUMN_TIME_DAILY, Integer.valueOf(config.getTimeDaily()));
            values.put(BannerConfigTable.COLUMN_WHITE_LIST, config.getWhitelist());
            values.put(BannerConfigTable.COLUMN_ADSIGN, Integer.valueOf(config.getAdvsign()));
            values.put(BannerConfigTable.COLUMN_ADV_ID, config.getAdvId());
            values.put(BannerConfigTable.COLUMN_POSITION, Integer.valueOf(config.getPosition()));
            this.db.insert(BannerConfigTable.TABLE_NAME, null, values);
            this.db.setTransactionSuccessful();
            this.db.endTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            this.db.endTransaction();
        }
    }
}