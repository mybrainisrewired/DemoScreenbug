package com.clouds.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.wmt.opengl.grid.ItemAnimation;
import java.util.ArrayList;
import java.util.List;

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
                        info2 = new DownloadInfo(cursor.getInt(0), cursor.getInt(1), cursor.getInt(ItemAnimation.CUR_Z), cursor.getInt(ItemAnimation.CUR_ALPHA), cursor.getString(ItemAnimation.CUR_ARC));
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
            Cursor cursor = database.rawQuery("select clientver, logover, bhurlver, wallppver,screenshotsver, appsver from system_info where sysInfo=?", new String[]{sysInfo});
            while (cursor.moveToNext()) {
                list.add(Integer.valueOf(cursor.getInt(0)));
                list.add(Integer.valueOf(cursor.getInt(1)));
                list.add(Integer.valueOf(cursor.getInt(ItemAnimation.CUR_Z)));
                list.add(Integer.valueOf(cursor.getInt(ItemAnimation.CUR_ALPHA)));
                list.add(Integer.valueOf(cursor.getInt(ItemAnimation.CUR_ARC)));
                list.add(Integer.valueOf(cursor.getInt(FragmentManagerImpl.ANIM_STYLE_FADE_ENTER)));
            }
            Log.d(TAG, new StringBuilder("getSysInfos (").append(sysInfo).append(") = ").append(list.toString()).toString());
            cursor.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public synchronized void insertSysInfos(int logover, int bhurlver, int wallppver, int appsver) {
        SQLiteDatabase database = getDbHelper().getWritableDatabase();
        try {
            database.execSQL("insert into system_info(1, logover, bhurlver, wallppver, appsver) values (?,?,?,?)", new Object[]{Integer.valueOf(logover), Integer.valueOf(bhurlver), Integer.valueOf(wallppver), Integer.valueOf(appsver)});
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