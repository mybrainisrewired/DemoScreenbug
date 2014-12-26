package com.google.android.gms.games.internal.constants;

import com.google.android.gms.games.internal.GamesLog;
import com.millennialmedia.android.MMAdView;

public final class RequestType {
    private RequestType() {
    }

    public static String bd(int i) {
        switch (i) {
            case MMAdView.TRANSITION_FADE:
                return "GIFT";
            case MMAdView.TRANSITION_UP:
                return "WISH";
            default:
                GamesLog.h("RequestType", "Unknown request type: " + i);
                return "UNKNOWN_TYPE";
        }
    }
}