package com.google.android.gms.internal;

import android.os.Build.VERSION;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;

public final class gr {
    private static boolean ab(int i) {
        return VERSION.SDK_INT >= i;
    }

    public static boolean fu() {
        return ab(ApiEventType.API_MRAID_EXPAND);
    }

    public static boolean fv() {
        return ab(ApiEventType.API_MRAID_RESIZE);
    }

    public static boolean fw() {
        return ab(ApiEventType.API_MRAID_CLOSE);
    }

    public static boolean fx() {
        return ab(ApiEventType.API_MRAID_IS_VIEWABLE);
    }

    public static boolean fy() {
        return ab(ApiEventType.API_MRAID_GET_ORIENTATION);
    }

    public static boolean fz() {
        return ab(ApiEventType.API_MRAID_GET_SCREEN_SIZE);
    }
}