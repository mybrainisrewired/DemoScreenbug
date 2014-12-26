package com.google.android.gms.games.internal.constants;

import com.millennialmedia.android.MMAdView;

public final class LeaderboardCollection {
    private LeaderboardCollection() {
    }

    public static String bd(int i) {
        switch (i) {
            case MMAdView.TRANSITION_NONE:
                return "PUBLIC";
            case MMAdView.TRANSITION_FADE:
                return "SOCIAL";
            default:
                throw new IllegalArgumentException("Unknown leaderboard collection: " + i);
        }
    }
}