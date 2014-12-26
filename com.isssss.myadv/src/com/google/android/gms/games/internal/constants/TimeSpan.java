package com.google.android.gms.games.internal.constants;

import com.millennialmedia.android.MMAdView;

public final class TimeSpan {
    private TimeSpan() {
        throw new AssertionError("Uninstantiable");
    }

    public static String bd(int i) {
        switch (i) {
            case MMAdView.TRANSITION_NONE:
                return "DAILY";
            case MMAdView.TRANSITION_FADE:
                return "WEEKLY";
            case MMAdView.TRANSITION_UP:
                return "ALL_TIME";
            default:
                throw new IllegalArgumentException("Unknown time span " + i);
        }
    }
}