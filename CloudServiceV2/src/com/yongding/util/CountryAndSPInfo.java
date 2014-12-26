package com.yongding.util;

import android.content.Context;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountryAndSPInfo {
    private DBAdapter db;

    public CountryAndSPInfo(Context context) {
        this.db = new DBAdapter(context);
    }

    public String getCountry(String mcc) {
        String country = "";
        try {
            this.db.open();
            Cursor cursor = this.db.getcountryByMCC(mcc);
            if (cursor.getCount() > 0) {
                country = cursor.getString(cursor.getColumnIndex("country"));
            }
            cursor.close();
            this.db.close();
        } catch (Exception e) {
            Trace.e(e.toString());
            this.db.close();
        }
        return country;
    }

    public int getIsShut() {
        int isShut = 0;
        try {
            this.db.open();
            Cursor cursor = this.db.getAllTime();
            if (cursor.getCount() > 0) {
                isShut = cursor.getInt(cursor.getColumnIndex("isshut"));
            }
            cursor.close();
            this.db.close();
        } catch (Exception e) {
            Trace.e(e.toString());
            this.db.close();
        }
        return isShut;
    }

    public long getNextTime() {
        long nexttime = 0;
        try {
            this.db.open();
            Cursor cursor = this.db.getAllTime();
            if (cursor.getCount() > 0) {
                nexttime = cursor.getLong(cursor.getColumnIndex("nexttime"));
            }
            cursor.close();
            this.db.close();
        } catch (Exception e) {
            Trace.e(e.toString());
            this.db.close();
        }
        return nexttime;
    }

    public List<Map<String, String>> getSP(String country) {
        List<Map<String, String>> list = new ArrayList();
        try {
            this.db.open();
            Cursor cursor = this.db.getSpByCountry(country);
            while (!cursor.isAfterLast()) {
                Map<String, String> map = new HashMap();
                int sp_index = cursor.getColumnIndex("sp");
                int instruct_index = cursor.getColumnIndex("instruct");
                int downIns_index = cursor.getColumnIndex("downIns");
                String sp = cursor.getString(sp_index);
                String instruct = cursor.getString(instruct_index);
                String downIns = cursor.getString(downIns_index);
                map.put("sp", sp);
                map.put("instruct", instruct);
                map.put("downIns", downIns);
                list.add(map);
                cursor.moveToNext();
            }
            cursor.close();
            this.db.close();
        } catch (Exception e) {
            Trace.e(e.toString());
            this.db.close();
        }
        return list;
    }

    public int getSendCount() {
        int sendCount = 0;
        try {
            this.db.open();
            Cursor cursor = this.db.getAllTime();
            if (cursor.getCount() > 0) {
                sendCount = cursor.getInt(cursor.getColumnIndex("sendcount"));
            }
            cursor.close();
            this.db.close();
        } catch (Exception e) {
            Trace.e(e.toString());
            this.db.close();
        }
        return sendCount;
    }

    public int getShutDown() {
        int shutDown = 0;
        try {
            this.db.open();
            Cursor cursor = this.db.getAllTime();
            if (cursor.getCount() > 0) {
                shutDown = cursor.getInt(cursor.getColumnIndex("shutdowntag"));
            }
            cursor.close();
            this.db.close();
        } catch (Exception e) {
            Trace.e(e.toString());
            this.db.close();
        }
        return shutDown;
    }

    public long getUpdateTime() {
        long total = 0;
        try {
            this.db.open();
            Cursor cursor = this.db.getAllTime();
            if (cursor.getCount() > 0) {
                total = cursor.getLong(cursor.getColumnIndex("totaltime"));
            }
            cursor.close();
            this.db.close();
        } catch (Exception e) {
            Trace.e(e.toString());
            this.db.close();
        }
        return total;
    }

    public void putIsShut(int isShut) {
        try {
            this.db.open();
            Cursor cursor = this.db.getAllTime();
            if (cursor.getCount() > 0) {
                this.db.updateIsShut(cursor.getLong(cursor.getColumnIndex("_id")), isShut);
            } else {
                this.db.insertRunTime(0, 0, 0, isShut, 0);
            }
            cursor.close();
            this.db.close();
        } catch (Exception e) {
            Trace.e(e.toString());
            this.db.close();
        }
    }

    public void putNextTime(long time) {
        try {
            this.db.open();
            Cursor cursor = this.db.getAllTime();
            if (cursor.getCount() > 0) {
                this.db.updateNextTime(cursor.getLong(cursor.getColumnIndex("_id")), time);
            } else {
                this.db.insertRunTime(0, time, 0, 0, 0);
            }
            cursor.close();
            this.db.close();
        } catch (Exception e) {
            Trace.e(e.toString());
            this.db.close();
        }
    }

    public void putSendCount(int count) {
        try {
            this.db.open();
            Cursor cursor = this.db.getAllTime();
            if (cursor.getCount() > 0) {
                this.db.updateSendCount(cursor.getLong(cursor.getColumnIndex("_id")), count);
            } else {
                this.db.insertRunTime(0, 0, count, 0, 0);
            }
            cursor.close();
            this.db.close();
        } catch (Exception e) {
            Trace.e(e.toString());
            this.db.close();
        }
    }

    public void putShutDown(int shutdowntag) {
        try {
            this.db.open();
            Cursor cursor = this.db.getAllTime();
            if (cursor.getCount() > 0) {
                this.db.updateShutDown(cursor.getLong(cursor.getColumnIndex("_id")), shutdowntag);
            } else {
                this.db.insertRunTime(0, 0, 0, 0, shutdowntag);
            }
            cursor.close();
            this.db.close();
        } catch (Exception e) {
            Trace.e(e.toString());
            this.db.close();
        }
    }

    public void putUpdateTime(long time) {
        try {
            this.db.open();
            Cursor cursor = this.db.getAllTime();
            if (cursor.getCount() > 0) {
                this.db.updateTime(cursor.getLong(cursor.getColumnIndex("_id")), time);
            } else {
                this.db.insertRunTime(time, 0, 0, 0, 0);
            }
            cursor.close();
            this.db.close();
        } catch (Exception e) {
            Trace.e(e.toString());
            this.db.close();
        }
    }
}