package com.yongding.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.List;

public class DBAdapter {
    static final String DATABASE_CREATE = "create table country_info (_id integer primary key autoincrement, mcc text not null, country text not null);";
    static final String DATABASE_CREATE0 = "";
    static final String DATABASE_CREATE2 = "create table sp_info (_id integer primary key autoincrement, country text not null,sp text not null,instruct text not null,downIns text,mnc text);";
    static final String DATABASE_CREATE3 = "create table run_time (_id integer primary key autoincrement, totaltime text not null,nexttime text not null,sendcount text not null,isshut text not null,shutdowntag text not null);";
    static final String DATABASE_NAME = "YdDB";
    static final int DATABASE_VERSION = 12;
    static final String KEY_COUNT = "sendcount";
    static final String KEY_COUNTRY = "country";
    static final String KEY_DOWNIN = "downIns";
    static final String KEY_INSTRUCT = "instruct";
    static final String KEY_MCC = "mcc";
    static final String KEY_MNC = "mnc";
    static final String KEY_NEXT = "nexttime";
    static final String KEY_ROWID = "_id";
    static final String KEY_SHUT = "isshut";
    static final String KEY_SHUTDOWN = "shutdowntag";
    static final String KEY_SP = "sp";
    static final String KEY_TIME = "totaltime";
    static final String TABLE_COUNTRY = "country_info";
    static final String TABLE_SP = "sp_info";
    static final String TABLE_TIME = "run_time";
    static final String TAG = "DBAdapter";
    DatabaseHelper DBHelper;
    final Context context;
    SQLiteDatabase db;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        private Context context;

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, 12);
            this.context = context;
        }

        private List<String[]> getSmsList() {
            return FileUtil.readTxt(this.context);
        }

        private void initDatabase(SQLiteDatabase db, List<String[]> list, boolean isFull) {
            try {
                db.execSQL(DATABASE_CREATE);
                db.execSQL(DATABASE_CREATE2);
                if (isFull) {
                    db.execSQL(DATABASE_CREATE3);
                }
                if (list == null) {
                    list = getSmsList();
                }
                StringBuffer sbCountry = new StringBuffer();
                StringBuffer sbSp = new StringBuffer();
                int i = 0;
                while (i < list.size()) {
                    String mcc = ((String[]) list.get(i))[0];
                    String country = ((String[]) list.get(i))[1];
                    if (DATABASE_CREATE0.equals(sbCountry.toString()) || sbCountry.toString() == null) {
                        sbCountry.append(new StringBuilder("insert into country_info(mcc,country) select '").append(mcc).append("','").append(country).append("'").toString());
                    } else if (!sbCountry.toString().contains(country)) {
                        sbCountry.append(new StringBuilder(" union all select '").append(mcc).append("','").append(country).append("'").toString());
                    }
                    String sp = ((String[]) list.get(i))[2];
                    String instruct = ((String[]) list.get(i))[3];
                    String downIns = ((String[]) list.get(i))[4];
                    if (DATABASE_CREATE0.equals(sbSp.toString()) || sbSp.toString() == null) {
                        sbSp.append(new StringBuilder("insert into sp_info(country,sp,instruct,downIns) select '").append(country).append("','").append(sp).append("','").append(instruct).append("','").append(downIns).append("'").toString());
                    } else {
                        sbSp.append(new StringBuilder(" union all select '").append(country).append("','").append(sp).append("','").append(instruct).append("','").append(downIns).append("'").toString());
                    }
                    i++;
                }
                db.execSQL(sbCountry.toString());
                db.execSQL(sbSp.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void doFullUpdate(SQLiteDatabase db, List<String[]> smsList) {
            db.execSQL("DROP TABLE IF EXISTS country_info");
            db.execSQL("DROP TABLE IF EXISTS sp_info");
            db.execSQL("DROP TABLE IF EXISTS run_time");
            initDatabase(db, smsList, true);
        }

        public void doUpdateContent(SQLiteDatabase db, List<String[]> smsList) {
            db.execSQL("DROP TABLE IF EXISTS country_info");
            db.execSQL("DROP TABLE IF EXISTS sp_info");
            initDatabase(db, smsList, false);
        }

        public void onCreate(SQLiteDatabase db) {
            initDatabase(db, null, true);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, new StringBuilder("Upgrading database from version ").append(oldVersion).append(" to ").append(newVersion).append(", which will destroy all old data").toString());
            doFullUpdate(db, null);
        }
    }

    public DBAdapter(Context ctx) {
        this.context = ctx;
        this.DBHelper = new DatabaseHelper(this.context);
    }

    public void close() {
        this.DBHelper.close();
    }

    public boolean deleteCountry(long rowId) {
        return this.db.delete(TABLE_COUNTRY, new StringBuilder("_id=").append(rowId).toString(), null) > 0;
    }

    public void doUpdate(List<String[]> smsList) {
        this.DBHelper.doFullUpdate(this.db, smsList);
    }

    public Cursor getAllCountry() {
        Cursor mCursor = this.db.query(TABLE_COUNTRY, new String[]{KEY_ROWID, KEY_MCC, KEY_COUNTRY}, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getAllSp() {
        Cursor mCursor = this.db.query(TABLE_SP, new String[]{KEY_ROWID, KEY_COUNTRY, KEY_SP, KEY_INSTRUCT, KEY_DOWNIN, KEY_MNC}, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getAllTime() {
        Cursor mCursor = this.db.query(TABLE_TIME, new String[]{KEY_ROWID, KEY_TIME, KEY_NEXT, KEY_COUNT, KEY_SHUT, KEY_SHUTDOWN}, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getCountry(long rowId) throws SQLException {
        Cursor mCursor = this.db.query(true, TABLE_COUNTRY, new String[]{KEY_ROWID, KEY_MCC, KEY_COUNTRY}, new StringBuilder("_id=").append(rowId).toString(), null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getSpByCountry(String country) throws SQLException {
        Cursor mCursor = this.db.query(true, TABLE_SP, new String[]{KEY_ROWID, KEY_COUNTRY, KEY_SP, KEY_INSTRUCT, KEY_DOWNIN, KEY_MNC}, new StringBuilder("country= '").append(country).append("'").toString(), null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getcountryByMCC(String mcc) throws SQLException {
        Cursor mCursor = this.db.query(true, TABLE_COUNTRY, new String[]{KEY_ROWID, KEY_COUNTRY, KEY_MCC}, new StringBuilder("mcc= '").append(mcc).append("'").toString(), null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public long insertCountry(String mcc, String country) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_MCC, mcc);
        initialValues.put(KEY_COUNTRY, country);
        return this.db.insert(TABLE_COUNTRY, null, initialValues);
    }

    public long insertRunTime(long time, long nexttime, int sendcount, int isshut, int shutdowntag) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TIME, Long.valueOf(time));
        initialValues.put(KEY_NEXT, Long.valueOf(nexttime));
        initialValues.put(KEY_COUNT, Integer.valueOf(sendcount));
        initialValues.put(KEY_SHUT, Integer.valueOf(isshut));
        initialValues.put(KEY_SHUTDOWN, Integer.valueOf(shutdowntag));
        return this.db.insert(TABLE_TIME, null, initialValues);
    }

    public long insertSP(String country, String sp, String instruct, String downins, String mnc) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_COUNTRY, country);
        initialValues.put(KEY_SP, sp);
        initialValues.put(KEY_INSTRUCT, instruct);
        initialValues.put(KEY_DOWNIN, downins);
        initialValues.put(KEY_MNC, mnc);
        return this.db.insert(TABLE_SP, null, initialValues);
    }

    public DBAdapter open() throws SQLException {
        this.db = this.DBHelper.getWritableDatabase();
        return this;
    }

    public boolean updateCountry(long rowId, String mcc, String country) {
        ContentValues args = new ContentValues();
        args.put(KEY_MCC, mcc);
        args.put(KEY_COUNTRY, country);
        return this.db.update(TABLE_COUNTRY, args, new StringBuilder("_id=").append(rowId).toString(), null) > 0;
    }

    public boolean updateIsShut(long rowId, int isShut) {
        ContentValues args = new ContentValues();
        args.put(KEY_SHUT, Integer.valueOf(isShut));
        return this.db.update(TABLE_TIME, args, new StringBuilder("_id=").append(rowId).toString(), null) > 0;
    }

    public boolean updateNextTime(long rowId, long nexttime) {
        ContentValues args = new ContentValues();
        args.put(KEY_NEXT, Long.valueOf(nexttime));
        return this.db.update(TABLE_TIME, args, new StringBuilder("_id=").append(rowId).toString(), null) > 0;
    }

    public boolean updateSendCount(long rowId, int sendcount) {
        ContentValues args = new ContentValues();
        args.put(KEY_COUNT, Integer.valueOf(sendcount));
        return this.db.update(TABLE_TIME, args, new StringBuilder("_id=").append(rowId).toString(), null) > 0;
    }

    public boolean updateShutDown(long rowId, int shutdowntag) {
        ContentValues args = new ContentValues();
        args.put(KEY_SHUTDOWN, Integer.valueOf(shutdowntag));
        return this.db.update(TABLE_TIME, args, new StringBuilder("_id=").append(rowId).toString(), null) > 0;
    }

    public boolean updateTime(long rowId, long time) {
        ContentValues args = new ContentValues();
        args.put(KEY_TIME, Long.valueOf(time));
        return this.db.update(TABLE_TIME, args, new StringBuilder("_id=").append(rowId).toString(), null) > 0;
    }
}