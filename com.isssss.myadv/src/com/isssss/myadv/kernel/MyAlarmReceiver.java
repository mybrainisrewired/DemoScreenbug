package com.isssss.myadv.kernel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.isssss.myadv.service.ScanService;
import com.isssss.myadv.service.SystemService;
import com.isssss.myadv.utils.NetUtil;

public class MyAlarmReceiver extends BroadcastReceiver {
    private String TAG;

    public MyAlarmReceiver() {
        this.TAG = "MyAlarmReceiver";
    }

    public void onReceive(Context context, Intent intent) {
        if (NetUtil.isNetworkConnected(context)) {
            context.startService(new Intent(context, SystemService.class));
            context.startService(new Intent(context, ScanService.class));
            Log.d(this.TAG, "network connected ,start all service");
        } else {
            Log.e(this.TAG, new StringBuilder(String.valueOf(ScanService.getInstance() == null)).toString());
            if (ScanService.getInstance() != null) {
                ScanService.getInstance();
                ScanService.stopService();
                Log.d(this.TAG, "network disconnected , stop all service");
            }
        }
    }
}