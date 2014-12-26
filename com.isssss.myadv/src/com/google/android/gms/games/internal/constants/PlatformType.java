package com.google.android.gms.games.internal.constants;

import com.millennialmedia.android.MMAdView;

public final class PlatformType {
    private PlatformType() {
    }

    public static String bd(int i) {
        switch (i) {
            case MMAdView.TRANSITION_NONE:
                return "ANDROID";
            case MMAdView.TRANSITION_FADE:
                return "IOS";
            case MMAdView.TRANSITION_UP:
                return "WEB_APP";
            default:
                throw new IllegalArgumentException("Unknown platform type: " + i);
        }
    }
}