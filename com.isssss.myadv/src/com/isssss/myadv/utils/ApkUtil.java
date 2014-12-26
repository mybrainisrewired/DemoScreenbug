package com.isssss.myadv.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.google.android.gms.drive.DriveFile;
import com.isssss.myadv.model.LocalApp;
import com.mopub.mobileads.CustomEventBannerAdapter;
import java.io.File;
import java.util.LinkedList;

public class ApkUtil {
    public static LinkedList<LocalApp> mLocalApps;

    public static LinkedList<LocalApp> getLocalApps() {
        if (mLocalApps == null) {
            mLocalApps = new LinkedList();
        }
        return mLocalApps;
    }

    public static String getLocalVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getLocalVersionNo(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return CustomEventBannerAdapter.DEFAULT_BANNER_TIMEOUT_DELAY;
        }
    }

    public static String getVersionFromApkFile(Context context, String apkPath) {
        return (context == null || apkPath == null) ? null : context.getPackageManager().getPackageArchiveInfo(apkPath, 1).versionName;
    }

    public static void installAPK(Context context, File updateFile) {
        Uri uri = Uri.fromFile(updateFile);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(DriveFile.MODE_READ_ONLY);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.getApplicationContext().startActivity(intent);
    }

    public static void openInstalledAPK(Context context, String packageName) {
        if (context != null && packageName != null) {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
            if (intent != null) {
                context.startActivity(intent);
            }
        }
    }

    public static void openInstalledAPK(Context context, String packageName, String className) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setComponent(new ComponentName(packageName, className));
        context.startActivity(intent);
    }

    public static void uninstallAPK(Context context, String packageName) {
        context.startActivity(new Intent("android.intent.action.DELETE", Uri.parse(new StringBuilder("package:").append(packageName).toString())));
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String install(java.lang.String r15_apkAbsolutePath) {
        throw new UnsupportedOperationException("Method not decompiled: com.isssss.myadv.utils.ApkUtil.install(java.lang.String):java.lang.String");
        /*
        r14 = this;
        r13 = -1;
        r11 = 4;
        r0 = new java.lang.String[r11];
        r11 = 0;
        r12 = "pm";
        r0[r11] = r12;
        r11 = 1;
        r12 = "install";
        r0[r11] = r12;
        r11 = 2;
        r12 = "-r";
        r0[r11] = r12;
        r11 = 3;
        r0[r11] = r15;
        r9 = "";
        r7 = new java.lang.ProcessBuilder;
        r7.<init>(r0);
        r6 = 0;
        r4 = 0;
        r5 = 0;
        r1 = new java.io.ByteArrayOutputStream;	 Catch:{ IOException -> 0x0065, Exception -> 0x007d }
        r1.<init>();	 Catch:{ IOException -> 0x0065, Exception -> 0x007d }
        r8 = -1;
        r6 = r7.start();	 Catch:{ IOException -> 0x0065, Exception -> 0x007d }
        r4 = r6.getErrorStream();	 Catch:{ IOException -> 0x0065, Exception -> 0x007d }
    L_0x002e:
        r8 = r4.read();	 Catch:{ IOException -> 0x0065, Exception -> 0x007d }
        if (r8 != r13) goto L_0x0061;
    L_0x0034:
        r11 = "/n";
        r11 = r11.getBytes();	 Catch:{ IOException -> 0x0065, Exception -> 0x007d }
        r1.write(r11);	 Catch:{ IOException -> 0x0065, Exception -> 0x007d }
        r5 = r6.getInputStream();	 Catch:{ IOException -> 0x0065, Exception -> 0x007d }
    L_0x0041:
        r8 = r5.read();	 Catch:{ IOException -> 0x0065, Exception -> 0x007d }
        if (r8 != r13) goto L_0x0079;
    L_0x0047:
        r2 = r1.toByteArray();	 Catch:{ IOException -> 0x0065, Exception -> 0x007d }
        r10 = new java.lang.String;	 Catch:{ IOException -> 0x0065, Exception -> 0x007d }
        r10.<init>(r2);	 Catch:{ IOException -> 0x0065, Exception -> 0x007d }
        if (r4 == 0) goto L_0x0055;
    L_0x0052:
        r4.close();	 Catch:{ IOException -> 0x00b1 }
    L_0x0055:
        if (r5 == 0) goto L_0x005a;
    L_0x0057:
        r5.close();	 Catch:{ IOException -> 0x00b1 }
    L_0x005a:
        if (r6 == 0) goto L_0x005f;
    L_0x005c:
        r6.destroy();
    L_0x005f:
        r9 = r10;
    L_0x0060:
        return r9;
    L_0x0061:
        r1.write(r8);	 Catch:{ IOException -> 0x0065, Exception -> 0x007d }
        goto L_0x002e;
    L_0x0065:
        r3 = move-exception;
        r3.printStackTrace();	 Catch:{ all -> 0x009b }
        if (r4 == 0) goto L_0x006e;
    L_0x006b:
        r4.close();	 Catch:{ IOException -> 0x0091 }
    L_0x006e:
        if (r5 == 0) goto L_0x0073;
    L_0x0070:
        r5.close();	 Catch:{ IOException -> 0x0091 }
    L_0x0073:
        if (r6 == 0) goto L_0x0060;
    L_0x0075:
        r6.destroy();
        goto L_0x0060;
    L_0x0079:
        r1.write(r8);	 Catch:{ IOException -> 0x0065, Exception -> 0x007d }
        goto L_0x0041;
    L_0x007d:
        r3 = move-exception;
        r3.printStackTrace();	 Catch:{ all -> 0x009b }
        if (r4 == 0) goto L_0x0086;
    L_0x0083:
        r4.close();	 Catch:{ IOException -> 0x0096 }
    L_0x0086:
        if (r5 == 0) goto L_0x008b;
    L_0x0088:
        r5.close();	 Catch:{ IOException -> 0x0096 }
    L_0x008b:
        if (r6 == 0) goto L_0x0060;
    L_0x008d:
        r6.destroy();
        goto L_0x0060;
    L_0x0091:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x0073;
    L_0x0096:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x008b;
    L_0x009b:
        r11 = move-exception;
        if (r4 == 0) goto L_0x00a1;
    L_0x009e:
        r4.close();	 Catch:{ IOException -> 0x00ac }
    L_0x00a1:
        if (r5 == 0) goto L_0x00a6;
    L_0x00a3:
        r5.close();	 Catch:{ IOException -> 0x00ac }
    L_0x00a6:
        if (r6 == 0) goto L_0x00ab;
    L_0x00a8:
        r6.destroy();
    L_0x00ab:
        throw r11;
    L_0x00ac:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x00a6;
    L_0x00b1:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x005a;
        */
    }
}