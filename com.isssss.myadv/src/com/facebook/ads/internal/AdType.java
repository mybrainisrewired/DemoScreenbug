package com.facebook.ads.internal;

public enum AdType {
    HTML(0),
    NATIVE(1);
    private final int value;

    private AdType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}