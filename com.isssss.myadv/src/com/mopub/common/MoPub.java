package com.mopub.common;

public class MoPub {
    private static final int DEFAULT_LOCATION_PRECISION = 6;
    public static final String SDK_VERSION = "3.1.0";
    private static volatile LocationAwareness sLocationLocationAwareness;
    private static volatile int sLocationPrecision;

    public enum LocationAwareness {
        NORMAL,
        TRUNCATED,
        DISABLED;

        static {
            NORMAL = new com.mopub.common.MoPub.LocationAwareness("NORMAL", 0);
            TRUNCATED = new com.mopub.common.MoPub.LocationAwareness("TRUNCATED", 1);
            DISABLED = new com.mopub.common.MoPub.LocationAwareness("DISABLED", 2);
            ENUM$VALUES = new com.mopub.common.MoPub.LocationAwareness[]{NORMAL, TRUNCATED, DISABLED};
        }
    }

    static {
        sLocationLocationAwareness = LocationAwareness.NORMAL;
        sLocationPrecision = 6;
    }

    public static LocationAwareness getLocationAwareness() {
        return sLocationLocationAwareness;
    }

    public static int getLocationPrecision() {
        return sLocationPrecision;
    }

    public static void setLocationAwareness(LocationAwareness locationAwareness) {
        sLocationLocationAwareness = locationAwareness;
    }

    public static void setLocationPrecision(int precision) {
        sLocationPrecision = Math.min(Math.max(0, precision), DEFAULT_LOCATION_PRECISION);
    }
}