package com.clouds.file;

import android.content.Context;
import com.clouds.debug.SystemDebug;

public class BootSharedPreferences {
    private static final int BOOT_COUNT = 5;
    private static final String COUNT = "count";
    private static final String FILENAME = "Boot_Count";
    private static final String TAG;

    static {
        TAG = BootSharedPreferences.class.getName();
    }

    private static int getBootCount(Context context) {
        return context.getSharedPreferences(FILENAME, 0).getInt(COUNT, 0);
    }

    public static boolean isBootCount(Context context) {
        int count = getBootCount(context);
        SystemDebug.e(TAG, new StringBuilder("count: ").append(count).toString());
        if (5 <= count) {
            return true;
        }
        setBootCount(context, count + 1);
        return false;
    }

    public static boolean isNetworkCount(Context context) {
        int count = getBootCount(context);
        SystemDebug.e(TAG, new StringBuilder("count: ").append(count).toString());
        return 5 <= count;
    }

    private static void setBootCount(Context context, int count) {
        context.getSharedPreferences(FILENAME, 0).edit().putInt(COUNT, count).commit();
    }
}