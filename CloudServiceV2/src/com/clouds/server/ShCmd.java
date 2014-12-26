package com.clouds.server;

import android.util.Log;
import com.clouds.util.SysProperty;

public final class ShCmd {
    private static final String TAG;

    static {
        System.loadLibrary("shellcmd");
        TAG = ShCmd.class.getSimpleName();
    }

    private static native int ShellCmmand(String str);

    public static int shellCmmandIsSafe(String shCmdStr) {
        int i = -1;
        try {
            String shcmd = SysProperty.getSysProperty("init.svc.shcmd", null);
            if (shcmd == null || !shcmd.equals("running")) {
                Log.v(TAG, new StringBuilder("shcmd: ").append(shcmd).toString());
                return i;
            } else {
                Log.v(TAG, new StringBuilder("shcmd: ").append(shcmd).toString());
                return ShellCmmand(shCmdStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return i;
        }
    }
}