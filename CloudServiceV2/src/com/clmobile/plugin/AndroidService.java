package com.clmobile.plugin;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import com.clmobile.app.MandatoryInfo;
import com.clmobile.plugin.receiver.NetworkConnectivityReceiver;
import com.clmobile.plugin.task.PluginTaskManager;

public class AndroidService extends Service {
    private NetworkConnectivityReceiver receiver;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        MandatoryInfo.initContext(getApplicationContext());
        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        this.receiver = new NetworkConnectivityReceiver();
        registerReceiver(this.receiver, filter);
    }

    public void onDestroy() {
        unregisterReceiver(this.receiver);
        super.onDestroy();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(PluginTaskManager.getInstance(getApplicationContext())).start();
        return 1;
    }
}