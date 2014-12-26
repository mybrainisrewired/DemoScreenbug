package com.wmt.libs;

import android.graphics.Bitmap;

public final class Native {
    static native Bitmap bmpSnapshot(String str);

    static native String getEnv(String str);

    static native int getSettingProbeInfo(String str) throws IllegalArgumentException;

    static native int setEnv(String str, String str2);

    static native byte[] snapshot();

    static native boolean verify();
}