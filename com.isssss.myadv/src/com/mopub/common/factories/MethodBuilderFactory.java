package com.mopub.common.factories;

import com.mopub.common.util.Reflection.MethodBuilder;

public class MethodBuilderFactory {
    protected static MethodBuilderFactory instance;

    static {
        instance = new MethodBuilderFactory();
    }

    public static MethodBuilder create(Object object, String methodName) {
        return instance.internalCreate(object, methodName);
    }

    @Deprecated
    public static void setInstance(MethodBuilderFactory factory) {
        instance = factory;
    }

    protected MethodBuilder internalCreate(Object object, String methodName) {
        return new MethodBuilder(object, methodName);
    }
}