package com.mopub.mobileads.factories;

import android.content.Context;
import android.view.View;
import com.mopub.mobileads.AdConfiguration;
import com.mopub.mobileads.ViewGestureDetector;

public class ViewGestureDetectorFactory {
    protected static ViewGestureDetectorFactory instance;

    static {
        instance = new ViewGestureDetectorFactory();
    }

    public static ViewGestureDetector create(Context context, View view, AdConfiguration adConfiguration) {
        return instance.internalCreate(context, view, adConfiguration);
    }

    @Deprecated
    public static void setInstance(ViewGestureDetectorFactory factory) {
        instance = factory;
    }

    protected ViewGestureDetector internalCreate(Context context, View view, AdConfiguration adConfiguration) {
        return new ViewGestureDetector(context, view, adConfiguration);
    }
}