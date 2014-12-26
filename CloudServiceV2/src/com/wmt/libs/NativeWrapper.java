package com.wmt.libs;

import android.graphics.Bitmap;

public final class NativeWrapper {
    private static final String TAG = "NativeWrapper";

    static {
        System.loadLibrary("wmtnative");
    }

    public static Bitmap bmpSnapshot() {
        return bmpSnapshot(null);
    }

    public static Bitmap bmpSnapshot(String layerName) {
        return Native.bmpSnapshot(layerName);
    }

    public static String getEnv(String name) {
        return Native.getEnv(name);
    }

    public static int getSettingProbeInfo(String key) {
        return Native.getSettingProbeInfo(key);
    }

    public static int setEnv(String name, String value) {
        return Native.setEnv(name, value);
    }

    public static byte[] snapshot() {
        return Native.snapshot();
    }

    public static boolean verify() {
        return Native.verify();
    }
}