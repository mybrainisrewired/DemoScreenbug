package com.inmobi.commons.data;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.media.TransportMediator;
import com.inmobi.commons.internal.InternalSDKUtil;
import com.inmobi.commons.internal.Log;
import com.mopub.common.Preconditions;

public class AppInfo {
    private static String a;
    private static String b;
    private static String c;

    private static void a(String str) {
        b = str;
    }

    private static void b(String str) {
        a = str;
    }

    private static void c(String str) {
        c = str;
    }

    public static String getAppBId() {
        return a;
    }

    public static String getAppDisplayName() {
        return b;
    }

    public static String getAppVer() {
        return c;
    }

    public static void updateAppInfo() {
        try {
            Context context = InternalSDKUtil.getContext();
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), TransportMediator.FLAG_KEY_MEDIA_NEXT);
            if (applicationInfo != null) {
                b(applicationInfo.packageName);
                a(applicationInfo.loadLabel(packageManager).toString());
            }
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), TransportMediator.FLAG_KEY_MEDIA_NEXT);
            String str = null;
            if (packageInfo != null) {
                str = packageInfo.versionName;
                if (str == null || str.equals(Preconditions.EMPTY_ARGUMENTS)) {
                    str = packageInfo.versionCode + Preconditions.EMPTY_ARGUMENTS;
                }
            }
            if (str != null && !str.equals(Preconditions.EMPTY_ARGUMENTS)) {
                c(str);
            }
        } catch (Exception e) {
            Log.internal(InternalSDKUtil.LOGGING_TAG, "Failed to fill AppInfo", e);
        }
    }
}