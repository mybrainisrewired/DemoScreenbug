package com.yongding.util;

import android.util.Log;
import java.io.File;

public final class Trace {
    private static final String LOG_TAG = "YDLOG";
    private static File dir;
    private static boolean isInit;
    private static boolean isLogMode;

    static {
        dir = null;
        isLogMode = false;
        isInit = false;
    }

    public static void e(String msg) {
        if (!isInit) {
            init(false);
        }
        if (isLogMode) {
            writeFile(msg);
            Log.e(LOG_TAG, msg);
        }
    }

    public static void i(String msg) {
        if (!isInit) {
            init(false);
        }
        if (isLogMode) {
            writeFile(msg);
            Log.i(LOG_TAG, msg);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void init(boolean r5_bCreated) {
        throw new UnsupportedOperationException("Method not decompiled: com.yongding.util.Trace.init(boolean):void");
        /*
        r4 = 1;
        r2 = android.os.Environment.getExternalStorageState();
        r3 = "mounted";
        r1 = r2.equals(r3);
        isInit = r4;
        if (r1 == 0) goto L_0x0056;
    L_0x000f:
        r2 = new java.io.File;	 Catch:{ Exception -> 0x004e }
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x004e }
        r4 = android.os.Environment.getExternalStorageDirectory();	 Catch:{ Exception -> 0x004e }
        r4 = r4.toString();	 Catch:{ Exception -> 0x004e }
        r4 = java.lang.String.valueOf(r4);	 Catch:{ Exception -> 0x004e }
        r3.<init>(r4);	 Catch:{ Exception -> 0x004e }
        r4 = java.io.File.separator;	 Catch:{ Exception -> 0x004e }
        r3 = r3.append(r4);	 Catch:{ Exception -> 0x004e }
        r4 = "yd.log";
        r3 = r3.append(r4);	 Catch:{ Exception -> 0x004e }
        r3 = r3.toString();	 Catch:{ Exception -> 0x004e }
        r2.<init>(r3);	 Catch:{ Exception -> 0x004e }
        dir = r2;	 Catch:{ Exception -> 0x004e }
        r2 = dir;	 Catch:{ Exception -> 0x004e }
        r2 = r2.exists();	 Catch:{ Exception -> 0x004e }
        if (r2 != 0) goto L_0x0053;
    L_0x003f:
        if (r5 == 0) goto L_0x004a;
    L_0x0041:
        r2 = dir;	 Catch:{ Exception -> 0x004e }
        r2.createNewFile();	 Catch:{ Exception -> 0x004e }
        r2 = 1;
        isLogMode = r2;	 Catch:{ Exception -> 0x004e }
    L_0x0049:
        return;
    L_0x004a:
        r2 = 0;
        isLogMode = r2;	 Catch:{ Exception -> 0x004e }
        goto L_0x0049;
    L_0x004e:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0049;
    L_0x0053:
        isLogMode = r5;	 Catch:{ Exception -> 0x004e }
        goto L_0x0049;
    L_0x0056:
        if (r5 == 0) goto L_0x0049;
    L_0x0058:
        r2 = 1;
        isLogMode = r2;	 Catch:{ Exception -> 0x004e }
        goto L_0x0049;
        */
    }

    public static void w(String msg) {
        if (!isInit) {
            init(false);
        }
        if (isLogMode) {
            writeFile(msg);
            Log.w(LOG_TAG, msg);
        }
    }

    public static void writeFile(String msg) {
    }
}