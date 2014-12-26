package com.mopub.common.util;

public class Visibility {
    private Visibility() {
    }

    public static boolean hasScreenVisibilityChanged(int oldVisibility, int newVisibility) {
        return isScreenVisible(oldVisibility) ^ isScreenVisible(newVisibility);
    }

    public static boolean isScreenVisible(int visibility) {
        return visibility == 0;
    }
}