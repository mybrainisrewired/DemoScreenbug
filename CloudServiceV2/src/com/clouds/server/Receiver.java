package com.clouds.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.clmobile.utils.Utils;
import com.clouds.constant.ReceiverActionConstant;
import com.clouds.debug.SystemDebug;
import com.clouds.file.BootSharedPreferences;

public class Receiver extends BroadcastReceiver {
    private static final String ACTION_SCREEN_ON = "android.intent.action.SCREEN_ON";
    private static final String BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
    private static final String CONNECT_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    private static final String TAG;

    static {
        TAG = Receiver.class.getSimpleName();
    }

    private void checkService(Context context, String action) {
        Intent serviceIntent = new Intent();
        serviceIntent.setClass(context, CloudService.class);
        serviceIntent.setAction(action);
        context.startService(serviceIntent);
    }

    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, new StringBuilder("action: ").append(intent.getAction()).toString());
        boolean isBootCount;
        if (intent.getAction().equals(BOOT_COMPLETED)) {
            isBootCount = BootSharedPreferences.isBootCount(context);
            checkService(context, ReceiverActionConstant.ACTION_NETWORK_IS_ONLINE);
            Utils.startPluginService(context);
            Log.i(TAG, "start server on boot completed!");
        } else if (CONNECT_CHANGE.equals(intent.getAction())) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            NetworkInfo gprs = connectivityManager.getNetworkInfo(0);
            NetworkInfo wifi = connectivityManager.getNetworkInfo(1);
            isBootCount = BootSharedPreferences.isNetworkCount(context);
            if (gprs.isConnected() && wifi.isConnected()) {
                Log.i(TAG, new StringBuilder("network is not  connected").append(isBootCount).toString());
            } else {
                SystemDebug.e(TAG, new StringBuilder("network is not connected isBootCount :  ").append(isBootCount).toString());
            }
        } else if (ACTION_SCREEN_ON.equals(intent.getAction())) {
            SystemDebug.e(TAG, "ACTION_SCREEN_ON: android.intent.action.SCREEN_ON");
        }
    }
}