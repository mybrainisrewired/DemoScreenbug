package com.isssss.myadv.kernel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.util.Log;
import java.lang.reflect.Method;

public class ScreenObserver {
    private static String TAG;
    private static Method mReflectScreenState;
    private Context mContext;
    private ScreenBroadcastReceiver mScreenReceiver;
    private ScreenStateListener mScreenStateListener;

    private class ScreenBroadcastReceiver extends BroadcastReceiver {
        private String action;

        private ScreenBroadcastReceiver() {
            this.action = null;
        }

        public void onReceive(Context context, Intent intent) {
            this.action = intent.getAction();
            if ("android.intent.action.SCREEN_ON".equals(this.action)) {
                ScreenObserver.this.mScreenStateListener.onScreenOn();
            } else if ("android.intent.action.SCREEN_OFF".equals(this.action)) {
                ScreenObserver.this.mScreenStateListener.onScreenOff();
            }
        }
    }

    public static interface ScreenStateListener {
        void onScreenOff();

        void onScreenOn();
    }

    static {
        TAG = "ScreenObserver";
    }

    public ScreenObserver(Context context) {
        this.mContext = context;
        this.mScreenReceiver = new ScreenBroadcastReceiver(null);
        try {
            mReflectScreenState = PowerManager.class.getMethod("isScreenOn", new Class[0]);
        } catch (NoSuchMethodException e) {
            Log.d(TAG, new StringBuilder("API < 7,").append(e).toString());
        }
    }

    private void firstGetScreenState() {
        if (isScreenOn((PowerManager) this.mContext.getSystemService("power"))) {
            if (this.mScreenStateListener != null) {
                this.mScreenStateListener.onScreenOn();
            }
        } else if (this.mScreenStateListener != null) {
            this.mScreenStateListener.onScreenOff();
        }
    }

    private static boolean isScreenOn(PowerManager pm) {
        try {
            return ((Boolean) mReflectScreenState.invoke(pm, new Object[0])).booleanValue();
        } catch (Exception e) {
            return false;
        }
    }

    private void startScreenBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.SCREEN_OFF");
        this.mContext.registerReceiver(this.mScreenReceiver, filter);
    }

    public void requestScreenStateUpdate(ScreenStateListener listener) {
        this.mScreenStateListener = listener;
        startScreenBroadcastReceiver();
        firstGetScreenState();
    }

    public void stopScreenStateUpdate() {
        this.mContext.unregisterReceiver(this.mScreenReceiver);
    }
}