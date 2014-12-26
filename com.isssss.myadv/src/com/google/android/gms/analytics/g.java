package com.google.android.gms.analytics;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

class g implements m {
    private static Object sf;
    private static g ss;
    protected String so;
    protected String sp;
    protected String sq;
    protected String sr;

    static {
        sf = new Object();
    }

    protected g() {
    }

    private g(Context context) {
        PackageManager packageManager = context.getPackageManager();
        this.sq = context.getPackageName();
        this.sr = packageManager.getInstallerPackageName(this.sq);
        String str = this.sq;
        String str2 = null;
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            if (packageInfo != null) {
                str = packageManager.getApplicationLabel(packageInfo.applicationInfo).toString();
                str2 = packageInfo.versionName;
            }
        } catch (NameNotFoundException e) {
            aa.w("Error retrieving package info: appName set to " + str);
        }
        this.so = str;
        this.sp = str2;
    }

    public static g ca() {
        return ss;
    }

    public static void n(Context context) {
        synchronized (sf) {
            if (ss == null) {
                ss = new g(context);
            }
        }
    }

    public boolean C(String str) {
        return "&an".equals(str) || "&av".equals(str) || "&aid".equals(str) || "&aiid".equals(str);
    }

    public String getValue(String field) {
        if (field == null) {
            return null;
        }
        if (field.equals("&an")) {
            return this.so;
        }
        if (field.equals("&av")) {
            return this.sp;
        }
        if (field.equals("&aid")) {
            return this.sq;
        }
        return field.equals("&aiid") ? this.sr : null;
    }
}