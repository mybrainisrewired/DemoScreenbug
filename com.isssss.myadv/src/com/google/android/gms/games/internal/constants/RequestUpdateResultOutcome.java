package com.google.android.gms.games.internal.constants;

import com.millennialmedia.android.MMAdView;

public final class RequestUpdateResultOutcome {
    private RequestUpdateResultOutcome() {
    }

    public static boolean isValid(int outcome) {
        switch (outcome) {
            case MMAdView.TRANSITION_NONE:
            case MMAdView.TRANSITION_FADE:
            case MMAdView.TRANSITION_UP:
            case MMAdView.TRANSITION_DOWN:
                return true;
            default:
                return false;
        }
    }
}