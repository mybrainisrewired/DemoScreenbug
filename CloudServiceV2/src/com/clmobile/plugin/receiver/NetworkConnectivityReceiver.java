package com.clmobile.plugin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.clmobile.utils.Utils;

public class NetworkConnectivityReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        NetworkInfo activeNetwork = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            Utils.startPluginService(context);
        }
    }
}