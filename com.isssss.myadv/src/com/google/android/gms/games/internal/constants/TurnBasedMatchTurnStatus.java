package com.google.android.gms.games.internal.constants;

import com.google.android.gms.games.internal.GamesLog;
import com.millennialmedia.android.MMAdView;

public final class TurnBasedMatchTurnStatus {
    public static String bd(int i) {
        switch (i) {
            case MMAdView.TRANSITION_NONE:
                return "TURN_STATUS_INVITED";
            case MMAdView.TRANSITION_FADE:
                return "TURN_STATUS_MY_TURN";
            case MMAdView.TRANSITION_UP:
                return "TURN_STATUS_THEIR_TURN";
            case MMAdView.TRANSITION_DOWN:
                return "TURN_STATUS_COMPLETE";
            default:
                GamesLog.h("MatchTurnStatus", "Unknown match turn status: " + i);
                return "TURN_STATUS_UNKNOWN";
        }
    }
}