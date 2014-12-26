package com.wmt.data;

import android.util.Log;

public class ThreadAsserter {
    private static final String TAG = "ThreadAsserter";
    private static volatile Thread sCurrentThread;
    private static volatile boolean sWarned;

    public static void assertNotInRenderThread() {
        if (!sWarned && Thread.currentThread() == sCurrentThread) {
            sWarned = true;
            Log.w(TAG, new Throwable("Should not do this in render thread"));
        }
    }

    public static void setRenderThread() {
        sCurrentThread = Thread.currentThread();
    }
}