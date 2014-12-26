package com.loopme;

enum AdFormat {
    BANNER,
    INTERSTITIAL;

    static {
        BANNER = new AdFormat("BANNER", 0);
        INTERSTITIAL = new AdFormat("INTERSTITIAL", 1);
        ENUM$VALUES = new AdFormat[]{BANNER, INTERSTITIAL};
    }
}