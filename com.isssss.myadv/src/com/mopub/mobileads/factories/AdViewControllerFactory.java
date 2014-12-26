package com.mopub.mobileads.factories;

import android.content.Context;
import com.mopub.mobileads.AdViewController;
import com.mopub.mobileads.MoPubView;

public class AdViewControllerFactory {
    protected static AdViewControllerFactory instance;

    static {
        instance = new AdViewControllerFactory();
    }

    public static AdViewController create(Context context, MoPubView moPubView) {
        return instance.internalCreate(context, moPubView);
    }

    @Deprecated
    public static void setInstance(AdViewControllerFactory factory) {
        instance = factory;
    }

    protected AdViewController internalCreate(Context context, MoPubView moPubView) {
        return new AdViewController(context, moPubView);
    }
}