package com.mopub.mobileads.factories;

import com.mopub.mobileads.CustomEventInterstitialAdapter;
import com.mopub.mobileads.MoPubInterstitial;

public class CustomEventInterstitialAdapterFactory {
    protected static CustomEventInterstitialAdapterFactory instance;

    static {
        instance = new CustomEventInterstitialAdapterFactory();
    }

    public static CustomEventInterstitialAdapter create(MoPubInterstitial moPubInterstitial, String className, String classData) {
        return instance.internalCreate(moPubInterstitial, className, classData);
    }

    @Deprecated
    public static void setInstance(CustomEventInterstitialAdapterFactory factory) {
        instance = factory;
    }

    protected CustomEventInterstitialAdapter internalCreate(MoPubInterstitial moPubInterstitial, String className, String classData) {
        return new CustomEventInterstitialAdapter(moPubInterstitial, className, classData);
    }
}