package com.isssss.myadv.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.facebook.ads.internal.AdWebViewUtils;
import com.isssss.myadv.dao.DataBaseHelper;
import com.isssss.myadv.kernel.GetConfig;
import com.isssss.myadv.kernel.PushAppTimer;
import com.isssss.myadv.kernel.RequestManager;
import java.util.Timer;

public class SystemService extends Service {
    public static final long MINUTE = 60000;
    public static final long ONE_HOUR = 3600000;
    public static final long THREE_HOUR = 10800000;
    private static Timer mGetAdvConfigTimer;
    private static Timer mPushAppTimer;

    private void initDB(Context context) {
        DataBaseHelper dbHelper = new DataBaseHelper(context);
        dbHelper.getWritableDatabase();
        dbHelper.close();
    }

    private void startTask() {
        if (mGetAdvConfigTimer == null) {
            mGetAdvConfigTimer = new Timer();
            mGetAdvConfigTimer.schedule(new GetConfig(getApplicationContext()), AdWebViewUtils.DEFAULT_IMPRESSION_DELAY_MS, THREE_HOUR);
        }
        if (mPushAppTimer == null) {
            mPushAppTimer = new Timer();
            mPushAppTimer.schedule(new PushAppTimer(getApplicationContext()), AdWebViewUtils.DEFAULT_IMPRESSION_DELAY_MS, ONE_HOUR);
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        Log.d("SystemService", "init");
        RequestManager.init(getApplicationContext());
        initDB(getApplicationContext());
    }

    public void onDestroy() {
        if (mGetAdvConfigTimer != null) {
            mGetAdvConfigTimer.cancel();
            mGetAdvConfigTimer = null;
        }
        if (mPushAppTimer != null) {
            mPushAppTimer.cancel();
            mPushAppTimer = null;
        }
        super.onDestroy();
        Log.d("SystemService", "destroyed");
    }

    @SuppressLint({"NewApi"})
    public int onStartCommand(Intent intent, int flags, int startId) {
        startTask();
        return super.onStartCommand(intent, flags, startId);
    }
}