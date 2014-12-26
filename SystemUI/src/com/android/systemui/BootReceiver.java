package com.android.systemui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings.System;
import android.util.Slog;

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "SystemUIBootReceiver";

    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, SoftwinnerService.class));
        try {
            if (System.getInt(context.getContentResolver(), "show_processes", 0) != 0) {
                context.startService(new Intent(context, LoadAverageService.class));
            }
        } catch (Exception e) {
            Slog.e(TAG, "Can't start load average service", e);
        }
    }
}