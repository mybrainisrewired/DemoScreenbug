package com.clouds.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import com.clouds.debug.SystemDebug;
import com.clouds.widget.DeviceApplication;

public class DistinguishDevice {
    private static final String TAG;
    private static final String rk3066_hardware = "rk30board";
    private static final String wm8850 = "wmt";

    static {
        TAG = DistinguishDevice.class.getSimpleName();
    }

    public static String getReleaseVersion() {
        return DeviceApplication.getRelease();
    }

    public static int getVersionCode(Context context) {
        int versionCode = 1;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String versionName = info.versionName;
            versionCode = info.versionCode;
            String str = info.packageName;
            return versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return versionCode;
        }
    }

    public static boolean isRKDevice() {
        String platform = DeviceApplication.getPlatform();
        String hardware = DeviceApplication.getHardware();
        SystemDebug.e(TAG, new StringBuilder("platform: ").append(platform).append(" hardware: ").append(hardware).toString());
        return (hardware == null || hardware.equals("") || hardware.contains(rk3066_hardware)) ? true : true;
    }

    public static boolean isWMTDevice() {
        String platform = DeviceApplication.getPlatform();
        String hardware = DeviceApplication.getHardware();
        SystemDebug.e(TAG, new StringBuilder("platform: ").append(platform).append(" hardware: ").append(hardware).toString());
        return (hardware == null || hardware.equals("") || !hardware.contains(wm8850)) ? false : true;
    }
}