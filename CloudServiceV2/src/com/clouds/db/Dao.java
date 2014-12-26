package com.clouds.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;

public class Dao {
    private static final String TAG;
    private static Dao dao;
    private Context context;

    static {
        TAG = Dao.class.getSimpleName();
        dao = null;
    }

    public Dao(Context context) {
        this.context = context;
    }

    public static Dao getDaoInstanca(Context context) {
        if (dao == null) {
            dao = new Dao(context);
        }
        return dao;
    }

    public void closeDb() {
    }

    public synchronized void delete(String url) {
        if (url != null) {
            try {
                SQLiteDatabase database = getDbHelper().getReadableDatabase();
                Log.d("DAO", new StringBuilder("issuccess").append(database.delete("download_info", "url=?", new String[]{url})).toString());
                database.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("DAO", "database is delete success");
        } else {
            Log.d("DAO", "database is delete fail");
        }
    }

    public synchronized void deleteSMSDBHelper() {
        try {
            SQLiteDatabase database = getDbHelper().getReadableDatabase();
            Log.d("DAO", new StringBuilder("issuccess rows").append(database.delete(DBHelper.smsDBName, "smstag=?", new String[]{"sms"})).toString());
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("DAO", "database is delete fail!");
        }
    }

    public DBHelper getDbHelper() {
        return DBHelper.getDBHelperInstanca(this.context);
    }

    public synchronized DownloadInfo getInfos(String urlstr) {
        DownloadInfo info;
        info = null;
        try {
            SQLiteDatabase database = getDbHelper().getReadableDatabase();
            try {
                Cursor cursor = database.rawQuery("select thread_id, start_pos, end_pos,compelete_size,url from download_info where url=?", new String[]{urlstr});
                DownloadInfo info2 = null;
                while (cursor.moveToNext()) {
                    try {
                        info2 = new DownloadInfo(cursor.getInt(0), cursor.getInt(1), cursor.getInt(ClassWriter.COMPUTE_FRAMES), cursor.getInt(JsonWriteContext.STATUS_OK_AFTER_SPACE), cursor.getString(JsonWriteContext.STATUS_EXPECT_VALUE));
                    } catch (Exception e) {
                        e = e;
                        info = info2;
                    } catch (Throwable th) {
                        th = th;
                    }
                }
                cursor.close();
                database.close();
                info = info2;
            } catch (Exception e2) {
                Exception e3 = e2;
                e3.printStackTrace();
                return info;
            }
        } catch (Throwable th2) {
            Throwable th3 = th2;
            throw th3;
        }
        return info;
    }

    public synchronized List<Integer> getSysInfos(String sysInfo) {
        List<Integer> list;
        list = new ArrayList();
        SQLiteDatabase database = getDbHelper().getReadableDatabase();
        try {
            Cursor cursor = database.rawQuery("select clientver, logover, bhurlver, wallppver,screenshotsver, appsver ,browerjssver from system_info where sysInfo=?", new String[]{sysInfo});
            while (cursor.moveToNext()) {
                list.add(Integer.valueOf(cursor.getInt(0)));
                list.add(Integer.valueOf(cursor.getInt(1)));
                list.add(Integer.valueOf(cursor.getInt(ClassWriter.COMPUTE_FRAMES)));
                list.add(Integer.valueOf(cursor.getInt(JsonWriteContext.STATUS_OK_AFTER_SPACE)));
                list.add(Integer.valueOf(cursor.getInt(JsonWriteContext.STATUS_EXPECT_VALUE)));
                list.add(Integer.valueOf(cursor.getInt(JsonWriteContext.STATUS_EXPECT_NAME)));
                list.add(Integer.valueOf(cursor.getInt(FragmentManagerImpl.ANIM_STYLE_FADE_EXIT)));
            }
            Log.d(TAG, new StringBuilder("getSysInfos (").append(sysInfo).append(") = ").append(list.toString()).toString());
            cursor.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public synchronized void insertSmsMessageInfo(String smsinfo) {
        SQLiteDatabase database = getDbHelper().getWritableDatabase();
        try {
            database.execSQL("insert into sms_Info (smsinfo,smstag) values (?,?)", new Object[]{smsinfo, "sms"});
            database.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized void insertSysInfos(int logover, int bhurlver, int wallppver, int appsver, int browerjssver) {
        SQLiteDatabase database = getDbHelper().getWritableDatabase();
        try {
            database.execSQL("insert into system_info(1, logover, bhurlver, wallppver, appsver,browerjssver) values (?,?,?,?)", new Object[]{Integer.valueOf(logover), Integer.valueOf(bhurlver), Integer.valueOf(wallppver), Integer.valueOf(appsver), Integer.valueOf(browerjssver)});
            database.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isHasInfors(String urlstr) {
        Cursor cursor = getDbHelper().getReadableDatabase().rawQuery("select count(*)  from download_info where url=?", new String[]{urlstr});
        int count = 0;
        if (cursor != null) {
            cursor.moveToFirst();
            count = cursor.getInt(0);
            cursor.close();
        }
        return count == 0;
    }

    public synchronized List<String> querySmsMessageInfo() {
        List<String> list;
        Exception e;
        list = null;
        try {
            SQLiteDatabase database = getDbHelper().getReadableDatabase();
            try {
                Cursor cursor = database.rawQuery("select * from sms_Info where smstag=?", new String[]{"sms"});
                if (cursor != null && cursor.getCount() > 0) {
                    List<String> list2 = new ArrayList();
                    while (cursor.moveToNext()) {
                        try {
                            int ID = cursor.getInt(0);
                            String tag = cursor.getString(1);
                            String smsinfo = cursor.getString(ClassWriter.COMPUTE_FRAMES);
                            list2.add(smsinfo);
                            Log.d(TAG, new StringBuilder("smsinfo (").append(smsinfo).append(")").append("tag: ").append(tag).append(" id: ").append(ID).toString());
                        } catch (Exception e2) {
                            e = e2;
                            list = list2;
                        } catch (Throwable th) {
                            th = th;
                        }
                    }
                    cursor.close();
                    database.close();
                    list = list2;
                }
            } catch (Exception e3) {
                e = e3;
                e.printStackTrace();
                database.close();
                return list;
            }
        } catch (Throwable th2) {
            Throwable th3;
            th3 = th2;
            throw th3;
        }
        return list;
    }

    public synchronized void saveInfos(DownloadInfo info) {
        if (info != null) {
            SQLiteDatabase database = getDbHelper().getWritableDatabase();
            try {
                database.execSQL("insert into download_info(thread_id,start_pos, end_pos,compelete_size,url) values (?,?,?,?,?)", new Object[]{Integer.valueOf(info.getThreadId()), Integer.valueOf(info.getStartPos()), Integer.valueOf(info.getEndPos()), Integer.valueOf(info.getCompeleteSize()), info.getUrl()});
                database.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void updataInfos(int threadId, int compeleteSize, String urlstr) {
        try {
            SQLiteDatabase database = getDbHelper().getReadableDatabase();
            database.execSQL("update download_info set compelete_size=? where thread_id=? and url=?", new Object[]{Integer.valueOf(compeleteSize), Integer.valueOf(threadId), urlstr});
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void updataSysInfos(String key, int value) {
        try {
            SQLiteDatabase db = getDbHelper().getReadableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(key, Integer.valueOf(value));
            db.update("system_info", cv, "sysInfo = ?", new String[]{"sysInfo"});
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}