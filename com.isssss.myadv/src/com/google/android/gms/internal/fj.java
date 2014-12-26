package com.google.android.gms.internal;

import android.util.Log;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public final class fj {
    private final String DH;

    public fj(String str) {
        this.DH = (String) fq.f(str);
    }

    public boolean P(int i) {
        return Log.isLoggable(this.DH, i);
    }

    public void a(String str, String str2, Throwable th) {
        if (P(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES)) {
            Log.e(str, str2, th);
        }
    }

    public void f(String str, String str2) {
        if (P(MMAdView.TRANSITION_UP)) {
            Log.v(str, str2);
        }
    }

    public void g(String str, String str2) {
        if (P(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES)) {
            Log.w(str, str2);
        }
    }

    public void h(String str, String str2) {
        if (P(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES)) {
            Log.e(str, str2);
        }
    }
}