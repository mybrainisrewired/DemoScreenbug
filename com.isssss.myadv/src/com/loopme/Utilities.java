package com.loopme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.unity3d.player.UnityPlayer;

class Utilities {
    private static final String LOG_TAG;
    private static Context mContext = null;
    private static final boolean mDevelopLog = true;
    private static final boolean mEnableAutoTestsEvents = false;
    private static final boolean mSendLogsBroadcasts = false;

    static {
        LOG_TAG = Utilities.class.getSimpleName();
    }

    Utilities() {
    }

    public static int convertDpToPixel(float dp, Context context) {
        return (int) (dp * (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f));
    }

    public static int getEndColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] = hsv[2] * 0.65f;
        return Color.HSVToColor(hsv);
    }

    static void init(Context context) {
        mContext = context;
    }

    public static boolean isAutoTestsEventsEnabled() {
        return false;
    }

    public static boolean isOnline(Context context) {
        try {
            NetworkInfo activeNetwork = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            return (activeNetwork != null && activeNetwork.isConnected() && activeNetwork.isAvailable()) ? mDevelopLog : false;
        } catch (Exception e) {
            log(LOG_TAG, e.toString(), LogLevel.ERROR);
            return false;
        }
    }

    public static boolean isUnityProject() {
        try {
            Activity activity = UnityPlayer.currentActivity;
            return mDevelopLog;
        } catch (NoClassDefFoundError e) {
            log(LOG_TAG, e.toString(), LogLevel.DEBUG);
            return false;
        }
    }

    public static void log(String tag, String text, LogLevel logLevel) {
        Log.d(new StringBuilder("Debug.LoopMe.").append(tag).toString(), text);
    }

    private static void sendBroadcast(String tag, String text, LogLevel level) {
        Intent intent = new Intent("com.loopme.logLevel");
        intent.putExtra("tag", tag);
        intent.putExtra("log", text);
        intent.putExtra("logLevel", level.toString());
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }
}