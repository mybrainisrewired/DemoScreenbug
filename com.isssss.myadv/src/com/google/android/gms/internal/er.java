package com.google.android.gms.internal;

import android.text.TextUtils;
import android.util.Log;

public class er {
    private static boolean zC;
    private final String mTag;
    private boolean zD;
    private boolean zE;
    private String zF;

    static {
        zC = false;
    }

    public er(String str) {
        this(str, dR());
    }

    public er(String str, boolean z) {
        this.mTag = str;
        this.zD = z;
        this.zE = false;
    }

    public static boolean dR() {
        return zC;
    }

    private String e(String str, Object... objArr) {
        String format = String.format(str, objArr);
        return !TextUtils.isEmpty(this.zF) ? this.zF + format : format;
    }

    public void a(String str, Object... objArr) {
        if (dQ()) {
            Log.v(this.mTag, e(str, objArr));
        }
    }

    public void a(Throwable th, String str, Object... objArr) {
        if (dP() || zC) {
            Log.d(this.mTag, e(str, objArr), th);
        }
    }

    public void ab(String str) {
        String str2;
        if (TextUtils.isEmpty(str)) {
            str2 = null;
        } else {
            str2 = String.format("[%s] ", new Object[]{str});
        }
        this.zF = str2;
    }

    public void b(String str, Object... objArr) {
        if (dP() || zC) {
            Log.d(this.mTag, e(str, objArr));
        }
    }

    public void c(String str, Object... objArr) {
        Log.i(this.mTag, e(str, objArr));
    }

    public void d(String str, Object... objArr) {
        Log.w(this.mTag, e(str, objArr));
    }

    public boolean dP() {
        return this.zD;
    }

    public boolean dQ() {
        return this.zE;
    }
}