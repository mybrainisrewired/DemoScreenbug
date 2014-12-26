package com.mopub.common.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.ConnectivityManager;
import android.os.StatFs;
import android.provider.Settings.Secure;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.Reflection.MethodBuilder;
import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Iterator;
import org.apache.http.conn.util.InetAddressUtils;

public class DeviceUtils {
    private static final int MAX_DISK_CACHE_SIZE = 104857600;
    private static final int MAX_MEMORY_CACHE_SIZE = 31457280;
    private static final int MIN_DISK_CACHE_SIZE = 31457280;

    public enum IP {
        IPv4,
        IPv6;

        static {
            IPv4 = new com.mopub.common.util.DeviceUtils.IP("IPv4", 0);
            IPv6 = new com.mopub.common.util.DeviceUtils.IP("IPv6", 1);
            ENUM$VALUES = new com.mopub.common.util.DeviceUtils.IP[]{IPv4, IPv6};
        }

        private boolean matches(String address) {
            switch ($SWITCH_TABLE$com$mopub$common$util$DeviceUtils$IP()[ordinal()]) {
                case MMAdView.TRANSITION_FADE:
                    return InetAddressUtils.isIPv4Address(address);
                case MMAdView.TRANSITION_UP:
                    return InetAddressUtils.isIPv6Address(address);
                default:
                    return false;
            }
        }

        private String toString(String address) {
            switch ($SWITCH_TABLE$com$mopub$common$util$DeviceUtils$IP()[ordinal()]) {
                case MMAdView.TRANSITION_FADE:
                    return address;
                case MMAdView.TRANSITION_UP:
                    return address.split("%")[0];
                default:
                    return null;
            }
        }
    }

    private DeviceUtils() {
    }

    public static long diskCacheSizeBytes(File dir) {
        long size = 31457280;
        try {
            StatFs statFs = new StatFs(dir.getAbsolutePath());
            size = (((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize())) / 50;
        } catch (IllegalArgumentException e) {
            MoPubLog.d("Unable to calculate 2% of available disk space, defaulting to minimum");
        }
        return Math.max(Math.min(size, 104857600), 31457280);
    }

    public static String getHashedUdid(Context context) {
        return context == null ? null : Utils.sha1(Secure.getString(context.getContentResolver(), "android_id"));
    }

    public static String getIpAddress(IP ip) throws SocketException {
        Iterator it = Collections.list(NetworkInterface.getNetworkInterfaces()).iterator();
        while (it.hasNext()) {
            Iterator it2 = Collections.list(((NetworkInterface) it.next()).getInetAddresses()).iterator();
            while (it2.hasNext()) {
                InetAddress address = (InetAddress) it2.next();
                if (!address.isLoopbackAddress()) {
                    String hostAddress = address.getHostAddress().toUpperCase();
                    if (IP.access$3(ip, hostAddress)) {
                        return IP.access$4(ip, hostAddress);
                    }
                }
            }
        }
        return null;
    }

    public static String getUserAgent() {
        return System.getProperty("http.agent");
    }

    public static boolean isNetworkAvailable(Context context) {
        if (context == null || context.checkCallingOrSelfPermission("android.permission.INTERNET") == -1) {
            return false;
        }
        if (context.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") == -1) {
            return true;
        }
        try {
            return ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo().isConnected();
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static int memoryCacheSizeBytes(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        long memoryClass = (long) activityManager.getMemoryClass();
        if (VersionCode.currentApiLevel().isAtLeast(VersionCode.HONEYCOMB)) {
            try {
                if (Utils.bitMaskContainsFlag(context.getApplicationInfo().flags, ApplicationInfo.class.getDeclaredField("FLAG_LARGE_HEAP").getInt(null))) {
                    memoryClass = (long) ((Integer) new MethodBuilder(activityManager, "getLargeMemoryClass").execute()).intValue();
                }
            } catch (Exception e) {
                MoPubLog.d("Unable to reflectively determine large heap size on Honeycomb and above.");
            }
        }
        return (int) Math.min(31457280, ((memoryClass / 8) * 1024) * 1024);
    }
}