package com.mopub.mobileads.factories;

import android.content.Context;
import com.mopub.mobileads.MoPubView;

public class MoPubViewFactory {
    protected static MoPubViewFactory instance;

    static {
        instance = new MoPubViewFactory();
    }

    public static MoPubView create(Context context) {
        return instance.internalCreate(context);
    }

    @Deprecated
    public static void setInstance(MoPubViewFactory factory) {
        instance = factory;
    }

    protected MoPubView internalCreate(Context context) {
        return new MoPubView(context);
    }
}