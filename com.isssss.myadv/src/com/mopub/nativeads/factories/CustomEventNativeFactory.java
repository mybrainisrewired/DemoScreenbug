package com.mopub.nativeads.factories;

import com.mopub.nativeads.CustomEventNative;
import com.mopub.nativeads.MoPubCustomEventNative;
import java.lang.reflect.Constructor;

public class CustomEventNativeFactory {
    protected static CustomEventNativeFactory instance;

    static {
        instance = new CustomEventNativeFactory();
    }

    public static CustomEventNative create(String className) throws Exception {
        if (className == null) {
            return new MoPubCustomEventNative();
        }
        return instance.internalCreate(Class.forName(className).asSubclass(CustomEventNative.class));
    }

    @Deprecated
    public static void setInstance(CustomEventNativeFactory customEventNativeFactory) {
        instance = customEventNativeFactory;
    }

    protected CustomEventNative internalCreate(Class<? extends CustomEventNative> nativeClass) throws Exception {
        Constructor<?> nativeConstructor = nativeClass.getDeclaredConstructor(null);
        nativeConstructor.setAccessible(true);
        return (CustomEventNative) nativeConstructor.newInstance(new Object[0]);
    }
}