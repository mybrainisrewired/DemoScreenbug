package com.google.android.gms.games.internal.constants;

import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public final class MatchResult {
    public static boolean isValid(int result) {
        switch (result) {
            case MMAdView.TRANSITION_NONE:
            case MMAdView.TRANSITION_FADE:
            case MMAdView.TRANSITION_UP:
            case MMAdView.TRANSITION_DOWN:
            case MMAdView.TRANSITION_RANDOM:
            case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                return true;
            default:
                return false;
        }
    }
}