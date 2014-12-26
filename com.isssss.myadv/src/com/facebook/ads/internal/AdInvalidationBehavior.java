package com.facebook.ads.internal;

import java.util.Locale;

public enum AdInvalidationBehavior {
    NONE,
    INSTALLED,
    NOT_INSTALLED;

    public static AdInvalidationBehavior fromString(String invalidationBehaviorStr) {
        if (StringUtils.isNullOrEmpty(invalidationBehaviorStr)) {
            return NONE;
        }
        try {
            return valueOf(invalidationBehaviorStr.toUpperCase(Locale.US));
        } catch (IllegalArgumentException e) {
            return NONE;
        }
    }
}