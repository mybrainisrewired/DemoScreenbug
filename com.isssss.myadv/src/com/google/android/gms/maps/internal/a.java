package com.google.android.gms.maps.internal;

import com.millennialmedia.android.MMAdView;

public final class a {
    public static Boolean a(byte b) {
        switch (b) {
            case MMAdView.TRANSITION_NONE:
                return Boolean.FALSE;
            case MMAdView.TRANSITION_FADE:
                return Boolean.TRUE;
            default:
                return null;
        }
    }

    public static byte c(Boolean bool) {
        return bool != null ? bool.booleanValue() ? (byte) 1 : (byte) 0 : (byte) -1;
    }
}