package com.google.android.gms.internal;

import android.content.Context;

public final class gk {
    public static boolean y(Context context) {
        return context.getPackageManager().hasSystemFeature("android.hardware.type.watch");
    }
}