package com.mopub.mobileads.factories;

import com.mopub.mobileads.AdFetcher;
import com.mopub.mobileads.AdViewController;

public class AdFetcherFactory {
    protected static AdFetcherFactory instance;

    static {
        instance = new AdFetcherFactory();
    }

    public static AdFetcher create(AdViewController adViewController, String userAgent) {
        return instance.internalCreate(adViewController, userAgent);
    }

    @Deprecated
    public static void setInstance(AdFetcherFactory factory) {
        instance = factory;
    }

    protected AdFetcher internalCreate(AdViewController adViewController, String userAgent) {
        return new AdFetcher(adViewController, userAgent);
    }
}