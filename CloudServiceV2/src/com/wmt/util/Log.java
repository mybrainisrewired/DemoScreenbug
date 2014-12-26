package com.wmt.util;

public class Log {
    private static boolean DEBUG;

    static {
        DEBUG = true;
    }

    public static void d(String tag, String msg) {
        if (DEBUG) {
            android.util.Log.d(tag, "(" + Thread.currentThread().getName() + "): " + msg);
        }
    }

    public static void e(String tag, String msg) {
        android.util.Log.e(tag, "(" + Thread.currentThread().getName() + "): " + msg);
    }

    public static void e(String tag, String msg, Throwable thr) {
        android.util.Log.e(tag, "(" + Thread.currentThread().getName() + "): " + msg, thr);
    }

    public static void i(String tag, String msg) {
        if (DEBUG) {
            android.util.Log.i(tag, "(" + Thread.currentThread().getName() + "): " + msg);
        }
    }

    public static void v(String tag, String msg) {
        if (DEBUG) {
            android.util.Log.v(tag, "(" + Thread.currentThread().getName() + "): " + msg);
        }
    }

    public static void w(String tag, String msg) {
        android.util.Log.w(tag, "(" + Thread.currentThread().getName() + "): " + msg);
    }

    public static void w(String tag, String msg, Throwable thr) {
        android.util.Log.w(tag, "(" + Thread.currentThread().getName() + "): " + msg, thr);
    }
}