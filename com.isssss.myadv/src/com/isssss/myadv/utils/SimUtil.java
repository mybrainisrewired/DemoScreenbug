package com.isssss.myadv.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

public class SimUtil {
    public static String getImei(Context context) {
        return ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
    }

    public static String getImsi(Context context) {
        String imsi = ((TelephonyManager) context.getSystemService("phone")).getSubscriberId();
        return !TextUtils.isEmpty(imsi) ? new StringBuilder(String.valueOf(imsi)).append("_").append(SPUtil.getUnique(context)).toString() : "404929226074350";
    }

    public static String getSimOperator(Context context) {
        return ((TelephonyManager) context.getSystemService("phone")).getSimOperator();
    }

    public static int getSimState(Context context) {
        return ((TelephonyManager) context.getSystemService("phone")).getSimState();
    }
}