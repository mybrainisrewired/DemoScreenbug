package com.millennialmedia.google.gson.internal;

// compiled from: $Gson$Preconditions.java
public final class _$Gson$Preconditions {
    public static void checkArgument(boolean condition) {
        if (!condition) {
            throw new IllegalArgumentException();
        }
    }

    public static <T> T checkNotNull(T obj) {
        if (obj != null) {
            return obj;
        }
        throw new NullPointerException();
    }
}