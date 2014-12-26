package com.isssss.myadv.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.isssss.myadv.model.AdvConfig;

public class AdvConfigDao {
    private static AdvConfigDao instance;
    private SQLiteDatabase db;
    private DataBaseHelper helper;

    private AdvConfigDao(Context context) {
        this.helper = new DataBaseHelper(context);
    }

    public static AdvConfigDao getInstance(Context context) {
        if (instance == null) {
            instance = new AdvConfigDao(context);
        }
        return instance;
    }

    public int clearConfig() {
        this.db = this.helper.getWritableDatabase();
        return this.db.delete(AdvConfigTable.TABLE_NAME, null, null);
    }

    public AdvConfig getConfig() {
        AdvConfig config = new AdvConfig();
        this.db = this.helper.getReadableDatabase();
        Cursor c = this.db.query(AdvConfigTable.TABLE_NAME, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            config.setShow(c.getInt(c.getColumnIndex(AdvConfigTable.COLUMN_SHOW)));
            config.setTimely(c.getInt(c.getColumnIndex(AdvConfigTable.COLUMN_TIMELY)));
            config.setWhitelist(c.getString(c.getColumnIndex(BannerConfigTable.COLUMN_WHITE_LIST)));
            config.setAdvCompany(c.getInt(c.getColumnIndex(AdvConfigTable.COLUMN_ADV_COMPANY)));
            config.setAdvid(c.getString(c.getColumnIndex(AdvConfigTable.COLUMN_ADV_ID)));
            config.setIp(c.getString(c.getColumnIndex(AdvConfigTable.COLUMN_CUSTOM_IP)));
            config.setShipmentTime(c.getLong(c.getColumnIndex(BannerConfigTable.COLUMN_SHIPMENT_TIME)));
        }
        c.close();
        return config;
    }

    public void insertConfig(AdvConfig config) {
        clearConfig();
        this.db = this.helper.getWritableDatabase();
        this.db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(AdvConfigTable.COLUMN_SHOW, Integer.valueOf(config.getShow()));
            values.put(AdvConfigTable.COLUMN_TIMELY, Integer.valueOf(config.getTimely()));
            values.put(BannerConfigTable.COLUMN_WHITE_LIST, config.getWhitelist());
            values.put(AdvConfigTable.COLUMN_ADV_COMPANY, Integer.valueOf(config.getAdvCompany()));
            values.put(AdvConfigTable.COLUMN_ADV_ID, config.getAdvid());
            values.put(AdvConfigTable.COLUMN_CUSTOM_IP, config.getIp());
            values.put(BannerConfigTable.COLUMN_SHIPMENT_TIME, Long.valueOf(config.getShipmentTime()));
            this.db.insert(AdvConfigTable.TABLE_NAME, null, values);
            this.db.setTransactionSuccessful();
            this.db.endTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            this.db.endTransaction();
        }
    }
}