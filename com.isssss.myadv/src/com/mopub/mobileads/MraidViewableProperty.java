package com.mopub.mobileads;

// compiled from: MraidProperty.java
class MraidViewableProperty extends MraidProperty {
    private final boolean mViewable;

    MraidViewableProperty(boolean viewable) {
        this.mViewable = viewable;
    }

    public static MraidViewableProperty createWithViewable(boolean viewable) {
        return new MraidViewableProperty(viewable);
    }

    public String toJsonPair() {
        return new StringBuilder("viewable: ").append(this.mViewable ? "true" : "false").toString();
    }
}