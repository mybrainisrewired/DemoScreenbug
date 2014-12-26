package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Prefs {
    public static final boolean DO_NOT_DISTURB_DEFAULT = false;
    public static final String DO_NOT_DISTURB_PREF = "do_not_disturb";
    private static final String SHARED_PREFS_NAME = "status_bar";
    public static final String SHOWN_COMPAT_MODE_HELP = "shown_compat_mode_help";

    public static Editor edit(Context context) {
        return context.getSharedPreferences(SHARED_PREFS_NAME, 0).edit();
    }

    public static SharedPreferences read(Context context) {
        return context.getSharedPreferences(SHARED_PREFS_NAME, 0);
    }
}