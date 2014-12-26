package com.isssss.myadv.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.isssss.myadv.model.BannerInfo;
import java.util.ArrayList;

public class BannerInfoDao {
    private static BannerInfoDao instance;
    private SQLiteDatabase db;
    private DataBaseHelper helper;

    private BannerInfoDao(Context context) {
        this.helper = new DataBaseHelper(context);
    }

    public static void deleteOldItem(int appID) {
    }

    public static BannerInfoDao getInstance(Context context) {
        if (instance == null) {
            instance = new BannerInfoDao(context);
        }
        return instance;
    }

    public static ArrayList<BannerInfo> getList() {
        return null;
    }

    public static void insertList(ArrayList<BannerInfo> arrayList) {
    }

    public static void insertNewItem(BannerInfo info) {
    }

    public static BannerInfo queryItem(int appID) {
        return null;
    }

    public static void updateItemStatus(int appID) {
    }
}