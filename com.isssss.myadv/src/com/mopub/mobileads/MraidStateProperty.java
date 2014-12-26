package com.mopub.mobileads;

import com.mopub.mobileads.MraidView.ViewState;

// compiled from: MraidProperty.java
class MraidStateProperty extends MraidProperty {
    private final ViewState mViewState;

    MraidStateProperty(ViewState viewState) {
        this.mViewState = viewState;
    }

    public static MraidStateProperty createWithViewState(ViewState viewState) {
        return new MraidStateProperty(viewState);
    }

    public String toJsonPair() {
        return new StringBuilder("state: '").append(this.mViewState.toString().toLowerCase()).append("'").toString();
    }
}