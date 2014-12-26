package com.mopub.mobileads.factories;

import android.content.Context;
import com.mopub.mobileads.util.vast.VastManager;

public class VastManagerFactory {
    protected static VastManagerFactory instance;

    static {
        instance = new VastManagerFactory();
    }

    public static VastManager create(Context context) {
        return instance.internalCreate(context);
    }

    @Deprecated
    public static void setInstance(VastManagerFactory factory) {
        instance = factory;
    }

    public VastManager internalCreate(Context context) {
        return new VastManager(context);
    }
}