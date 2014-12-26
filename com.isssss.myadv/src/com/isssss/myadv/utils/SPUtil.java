package com.isssss.myadv.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import com.isssss.myadv.constant.ParamConst;
import com.mopub.common.Preconditions;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.UUID;

public class SPUtil {
    protected static String BANNER_SHOW_TIMES = null;
    protected static String BANNER_SHOW_TIMES_XML = null;
    private static String COUNTRY_CODE = null;
    public static String COUNTRY_DEFAULT = null;
    public static String DEFUALT_UID = null;
    private static String FIRST_LAUNCHER_TIME = null;
    private static String FIRST_LAUNCHER_XML = null;
    private static final String FIRST_POWER_ON_TIME = "first_power_on_time";
    private static final String FIRST_POWER_ON_XML = "first_power_on_xml";
    private static final long MINUTE = 60000;
    private static final long ONE_HOUR = 3600000;
    private static String PUSH_INTERVAL_XML = null;
    private static String PUSH_LATEST_REQUEST_TIME = null;
    private static String PUSH_REQUEST_INTERVAL = null;
    private static String PUSH_REQUEST_XML = null;
    private static String SA = null;
    private static String STATISTICS_XML = null;
    private static final long THREE_HOURS = 10800000;
    protected static String UNIQUE_ID;
    protected static String UNIQUE_ID_XML;
    private static String country_code_xml;

    static {
        FIRST_LAUNCHER_XML = "first_launcher_xml";
        FIRST_LAUNCHER_TIME = "first_launcher_time";
        STATISTICS_XML = "statistics_xml";
        SA = "StatisticsAgreement";
        country_code_xml = "country_code_xml";
        COUNTRY_CODE = ParamConst.COUNTRY_CODE;
        COUNTRY_DEFAULT = "IANA";
        PUSH_REQUEST_XML = "push_request_xml";
        PUSH_INTERVAL_XML = "push_interval_xml";
        PUSH_LATEST_REQUEST_TIME = "push_latest_request_time";
        PUSH_REQUEST_INTERVAL = "PUSH_REQUEST_INTERVAL";
        UNIQUE_ID = "unique_id";
        UNIQUE_ID_XML = "unique_id_xml";
        DEFUALT_UID = "XXXX_XXXX";
        BANNER_SHOW_TIMES_XML = "banner_show_times";
        BANNER_SHOW_TIMES = "banner_show_times_daily";
    }

    public static String getCountryCode(Context context) {
        return context.getSharedPreferences(country_code_xml, 0).getString(COUNTRY_CODE, COUNTRY_DEFAULT);
    }

    public static long getFirstLauncherTime(Context context) {
        return context.getSharedPreferences(FIRST_LAUNCHER_XML, 0).getLong(FIRST_LAUNCHER_TIME, 0);
    }

    public static long getFirstPowerOnTime(Context context) {
        return context.getSharedPreferences(FIRST_POWER_ON_XML, 0).getLong(FIRST_POWER_ON_TIME, -1);
    }

    public static long getLatestPushRQ(Context context) {
        return context.getSharedPreferences(PUSH_REQUEST_XML, 0).getLong(PUSH_LATEST_REQUEST_TIME, 0);
    }

    public static String getMac() {
        String macSerial = null;
        String str = Preconditions.EMPTY_ARGUMENTS;
        try {
            LineNumberReader input = new LineNumberReader(new InputStreamReader(Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ").getInputStream()));
            while (str != null) {
                str = input.readLine();
                if (str != null) {
                    return str.trim();
                }
            }
            return macSerial;
        } catch (IOException e) {
            e.printStackTrace();
            return macSerial;
        }
    }

    public static long getPushRequestInterval(Context context) {
        return context.getSharedPreferences(PUSH_INTERVAL_XML, 0).getLong(PUSH_REQUEST_INTERVAL, THREE_HOURS);
    }

    public static int getShowTimes(Context context) {
        return context.getSharedPreferences(BANNER_SHOW_TIMES_XML, 0).getInt(BANNER_SHOW_TIMES, 0);
    }

    public static boolean getStatisticsAgreement(Context context) {
        return context.getSharedPreferences(STATISTICS_XML, 0).getBoolean(SA, false);
    }

    public static String getUnique(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
        return new UUID((long) String.valueOf(Secure.getString(context.getContentResolver(), "android_id")).hashCode(), (((long) String.valueOf(tm.getDeviceId()).hashCode()) << 32) | ((long) String.valueOf(tm.getSimSerialNumber()).hashCode())).toString();
    }

    public static String getUniqueID(Context context) {
        return context.getSharedPreferences(UNIQUE_ID_XML, 0).getString(UNIQUE_ID, DEFUALT_UID);
    }

    public static void setCountryCode(Context context, String countryCode) {
        context.getSharedPreferences(country_code_xml, 0).edit().putString(COUNTRY_CODE, countryCode).commit();
    }

    public static void setLatestPushRQ(Context context, long latestRQ) {
        context.getSharedPreferences(PUSH_REQUEST_XML, 0).edit().putLong(PUSH_LATEST_REQUEST_TIME, latestRQ).commit();
    }

    public static void setPushRequestInterval(Context context, long interval) {
        context.getSharedPreferences(PUSH_INTERVAL_XML, 0).edit().putLong(PUSH_REQUEST_INTERVAL, interval).commit();
    }

    public static void setShowTimes(Context context, int times) {
        context.getSharedPreferences(BANNER_SHOW_TIMES_XML, 0).edit().putInt(BANNER_SHOW_TIMES, times).commit();
    }

    public static void setStatisticsResult(Context context, boolean result) {
        context.getSharedPreferences(STATISTICS_XML, 0).edit().putBoolean(SA, result).commit();
    }

    public static void setUniqueID(Context context, String uid) {
        context.getSharedPreferences(UNIQUE_ID_XML, 0).edit().putString(UNIQUE_ID, uid).commit();
    }

    public static void storeFirstLauncherTime(Context context, long time) {
        context.getSharedPreferences(FIRST_LAUNCHER_XML, 0).edit().putLong(FIRST_LAUNCHER_TIME, time).commit();
    }

    public static void storeFirstPowerOnTime(Context context, long time) {
        SharedPreferences sp = context.getSharedPreferences(FIRST_POWER_ON_XML, 0);
        if (-1 == sp.getLong(FIRST_POWER_ON_TIME, -1)) {
            sp.edit().putLong(FIRST_POWER_ON_TIME, time).commit();
        }
    }
}