package com.wmt.libs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class WmtSystemEventListener {
    public static final String SYSTEM_AWAKE_UP = "com.wmt.wakeup";
    public static final String SYSTEM_GO_SLEEP = "com.wmt.hibernate";
    public static final String SYSTEM_SCREEN_LOCK = "com.wmt.screen.lock";
    public static final String SYSTEM_SCREEN_UNLOCK = "com.wmt.screen.unlock";
    Context mContext;
    private boolean mIsListening;
    private SystemEventCallback mSystemEventCallback;
    private BroadcastReceiver receiver;

    public static interface SystemEventCallback {
        void onSystemScreenLock();

        void onSystemScreenUnlock();

        void onSystemSleep();

        void onSystemWakeUp();
    }

    public WmtSystemEventListener(Context context, SystemEventCallback cb) {
        this.mIsListening = false;
        this.receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action != null) {
                    if (action.equals(SYSTEM_SCREEN_LOCK)) {
                        WmtSystemEventListener.this.mSystemEventCallback.onSystemScreenLock();
                    } else if (action.equals(SYSTEM_SCREEN_UNLOCK)) {
                        WmtSystemEventListener.this.mSystemEventCallback.onSystemScreenUnlock();
                    }
                    if (action.equals(SYSTEM_GO_SLEEP)) {
                        WmtSystemEventListener.this.mSystemEventCallback.onSystemSleep();
                    } else if (action.equals(SYSTEM_AWAKE_UP)) {
                        WmtSystemEventListener.this.mSystemEventCallback.onSystemWakeUp();
                    }
                }
            }
        };
        this.mContext = context;
        this.mSystemEventCallback = cb;
    }

    public void startListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(SYSTEM_GO_SLEEP);
        filter.addAction(SYSTEM_AWAKE_UP);
        filter.addAction(SYSTEM_SCREEN_LOCK);
        filter.addAction(SYSTEM_SCREEN_UNLOCK);
        this.mIsListening = true;
        this.mContext.registerReceiver(this.receiver, filter);
    }

    public void stopListener() {
        if (this.mIsListening) {
            this.mIsListening = false;
            this.mContext.unregisterReceiver(this.receiver);
        }
    }
}