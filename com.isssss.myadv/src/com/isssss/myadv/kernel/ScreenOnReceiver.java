package com.isssss.myadv.kernel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.isssss.myadv.service.ScanService;
import com.isssss.myadv.service.SystemService;
import com.isssss.myadv.utils.NetUtil;

public class ScreenOnReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if ("android.intent.action.PACKAGE_RESTARTED".equals(action) || "android.intent.action.USER_PRESENT".equals(action) || "android.intent.action.BOOT_COMPLETED".equals(action)) {
            Log.d("ScreenOnReceiver", "screen is on...");
            if (NetUtil.isNetworkConnected(context)) {
                context.startService(new Intent(context, SystemService.class));
                context.startService(new Intent(context, ScanService.class));
            }
            MyAlarm.startAlarm(context);
        }
        if ("android.intent.action.PACKAGE_REMOVED".equals(action) || "android.intent.action.PACKAGE_ADDED".equals(action)) {
            Log.e("ScreenOnReceiver", "PACKAGE REMOVED OR ADDED");
            ScanService.getNonSystemInstalledApps(context);
        }
    }
}