package com.google.android.gms.internal;

import android.util.Log;
import com.google.ads.AdRequest;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public final class dw {
    public static void a(String str, Throwable th) {
        if (n(MMAdView.TRANSITION_DOWN)) {
            Log.d(AdRequest.LOGTAG, str, th);
        }
    }

    public static void b(String str, Throwable th) {
        if (n(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES)) {
            Log.e(AdRequest.LOGTAG, str, th);
        }
    }

    public static void c(String str, Throwable th) {
        if (n(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES)) {
            Log.w(AdRequest.LOGTAG, str, th);
        }
    }

    public static boolean n(int i) {
        return (i >= 5 || Log.isLoggable(AdRequest.LOGTAG, i)) && i != 2;
    }

    public static void v(String str) {
        if (n(MMAdView.TRANSITION_DOWN)) {
            Log.d(AdRequest.LOGTAG, str);
        }
    }

    public static void w(String str) {
        if (n(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES)) {
            Log.e(AdRequest.LOGTAG, str);
        }
    }

    public static void x(String str) {
        if (n(MMAdView.TRANSITION_RANDOM)) {
            Log.i(AdRequest.LOGTAG, str);
        }
    }

    public static void y(String str) {
        if (n(MMAdView.TRANSITION_UP)) {
            Log.v(AdRequest.LOGTAG, str);
        }
    }

    public static void z(String str) {
        if (n(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES)) {
            Log.w(AdRequest.LOGTAG, str);
        }
    }
}