package com.isssss.myadv.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;

public class NetUtil {
    public static String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                Enumeration<InetAddress> enumIpAddr = ((NetworkInterface) en.nextElement()).getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
            Log.e("WifiPreference IpAddress", e.toString());
        }
        return null;
    }

    public static String getPhoneIp(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
        return wifiManager.isWifiEnabled() ? intToIp(wifiManager.getConnectionInfo().getIpAddress()) : getLocalIpAddress();
    }

    public static <T extends InetAddress> T getWifiInetAddress(Context context, Class<T> inetClass) {
        Enumeration<InetAddress> e = getWifiInetAddresses(context);
        while (e.hasMoreElements()) {
            InetAddress inetAddress = (InetAddress) e.nextElement();
            if (inetAddress.getClass() == inetClass) {
                return inetAddress;
            }
        }
        return null;
    }

    @SuppressLint({"NewApi"})
    public static Enumeration<InetAddress> getWifiInetAddresses(Context context) {
        String[] macParts = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getMacAddress().split(":");
        byte[] macBytes = new byte[macParts.length];
        int i = 0;
        while (i < macParts.length) {
            macBytes[i] = (byte) Integer.parseInt(macParts[i], ApiEventType.API_MRAID_GET_ORIENTATION);
            i++;
        }
        try {
            Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface) e.nextElement();
                if (Arrays.equals(networkInterface.getHardwareAddress(), macBytes)) {
                    return networkInterface.getInetAddresses();
                }
            }
        } catch (SocketException e2) {
            Log.wtf("WIFIIP", "Unable to NetworkInterface.getNetworkInterfaces()");
        }
        return null;
    }

    private static String intToIp(int i) {
        return new StringBuilder(String.valueOf(i & 255)).append(".").append((i >> 8) & 255).append(".").append((i >> 16) & 255).append(".").append((i >> 24) & 255).toString();
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            NetworkInfo mNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            NetworkInfo mWiFiNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getNetworkInfo(1);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isConnected();
            }
        }
        return false;
    }
}