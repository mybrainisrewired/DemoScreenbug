package com.isssss.myadv.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

public class PushIdsDao {
    private static PushIdsDao instance;
    private SQLiteDatabase db;
    private DataBaseHelper helper;

    private PushIdsDao(Context context) {
        this.helper = new DataBaseHelper(context);
    }

    public static PushIdsDao getInstance(Context context) {
        if (instance == null) {
            instance = new PushIdsDao(context);
        }
        return instance;
    }

    public int clearHistory() {
        this.db = this.helper.getWritableDatabase();
        return this.db.delete(PushIdsTable.TABLE_NAME, null, null);
    }

    public ArrayList<Integer> getIDS() {
        this.db = this.helper.getReadableDatabase();
        ArrayList<Integer> list = new ArrayList();
        Cursor c = this.db.query(PushIdsTable.TABLE_NAME, null, null, null, null, null, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            list.add(Integer.valueOf(c.getInt(c.getColumnIndex(PushIdsTable.PUSH_ID))));
            c.moveToNext();
        }
        c.close();
        return list;
    }

    public void insertIds(ArrayList<Integer> list) {
        clearHistory();
        this.db = this.helper.getWritableDatabase();
        this.db.beginTransaction();
        int i = 0;
        while (i < list.size()) {
            try {
                ContentValues values = new ContentValues();
                values.put(PushIdsTable.PUSH_ID, (Integer) list.get(i));
                this.db.insert(PushIdsTable.TABLE_NAME, null, values);
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                this.db.endTransaction();
            }
        }
        this.db.setTransactionSuccessful();
        this.db.endTransaction();
    }
}