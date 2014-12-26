package com.wmt.util;

import android.content.Context;
import android.os.SystemProperties;
import android.provider.Settings.System;

public class Network3GService {
    private static final String SOCKET_NAME_WITH_DRIVER = "3g-wmt";
    private static final String TAG = "Network3GService";
    private Context mContext;

    public void init(Context context) {
        Log.i(TAG, TAG);
        this.mContext = context;
        if (System.getInt(context.getContentResolver(), "ctl_3g_server_enable", 1) == 1) {
            SystemProperties.set("ctl.start", "3g_server");
        } else {
            SystemProperties.set("ctl.stop", "3g_server");
        }
        Context context2 = this.mContext;
    }
}