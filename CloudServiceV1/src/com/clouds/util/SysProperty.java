package com.clouds.util;

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SysProperty {
    private static final String TAG;

    static {
        TAG = SysProperty.class.getSimpleName();
    }

    public static void execShell(String command) {
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String execShellCommand(String command) {
        StringBuilder sb = new StringBuilder("");
        try {
            Process proc = Runtime.getRuntime().exec(command);
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            while (true) {
                String line = bufferedreader.readLine();
                if (line == null) {
                    try {
                        if (proc.waitFor() != 0) {
                            System.err.println(new StringBuilder(String.valueOf(TAG)).append("exit value = ").append(proc.exitValue()).toString());
                            Log.e(TAG, new StringBuilder(String.valueOf(command)).append("exit value=").append(proc.exitValue()).toString());
                        }
                    } catch (InterruptedException e) {
                        System.err.println(e);
                    }
                    return sb.toString();
                } else {
                    sb.append(line);
                    sb.append('\n');
                }
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    private static String getKeyValue(String str) {
        return str.replaceAll("\n", "").replaceAll(" ", "");
    }

    public static String getMACAddress() {
        String str = "";
        str = execShellCommand("busybox ifconfig");
        if (str == null) {
            return "network err,plz check network!";
        }
        if (str.length() <= 0 || !str.contains("HWaddr")) {
            return "";
        }
        int index = str.indexOf("HWaddr");
        str = str.substring(index + 7, index + 24);
        Log.v(TAG, new StringBuilder("MAC address:").append(str).toString());
        return str;
    }

    public static String getSysProperty(String key) {
        return getKeyValue(execShellCommand(new StringBuilder("getprop ").append(key).toString()));
    }

    public static String getSysProperty(String key, String def) {
        return getKeyValue(execShellCommand(new StringBuilder("getprop ").append(key).append(" ").append(def).toString()));
    }
}