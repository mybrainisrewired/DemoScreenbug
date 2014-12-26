package com.mopub.mobileads.factories;

import com.mopub.mobileads.CustomEventBannerAdapter;
import com.mopub.mobileads.MoPubView;

public class CustomEventBannerAdapterFactory {
    protected static CustomEventBannerAdapterFactory instance;

    static {
        instance = new CustomEventBannerAdapterFactory();
    }

    public static CustomEventBannerAdapter create(MoPubView moPubView, String className, String classData) {
        return instance.internalCreate(moPubView, className, classData);
    }

    @Deprecated
    public static void setInstance(CustomEventBannerAdapterFactory factory) {
        instance = factory;
    }

    protected CustomEventBannerAdapter internalCreate(MoPubView moPubView, String className, String classData) {
        return new CustomEventBannerAdapter(moPubView, className, classData);
    }
}