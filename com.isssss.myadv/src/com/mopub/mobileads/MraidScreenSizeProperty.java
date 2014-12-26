package com.mopub.mobileads;

// compiled from: MraidProperty.java
class MraidScreenSizeProperty extends MraidProperty {
    private final int mScreenHeight;
    private final int mScreenWidth;

    MraidScreenSizeProperty(int width, int height) {
        this.mScreenWidth = width;
        this.mScreenHeight = height;
    }

    public static MraidScreenSizeProperty createWithSize(int width, int height) {
        return new MraidScreenSizeProperty(width, height);
    }

    public String toJsonPair() {
        return new StringBuilder("screenSize: { width: ").append(this.mScreenWidth).append(", height: ").append(this.mScreenHeight).append(" }").toString();
    }
}