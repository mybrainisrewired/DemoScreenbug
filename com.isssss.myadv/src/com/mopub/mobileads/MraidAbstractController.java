package com.mopub.mobileads;

class MraidAbstractController {
    private final MraidView mMraidView;

    MraidAbstractController(MraidView view) {
        this.mMraidView = view;
    }

    public MraidView getMraidView() {
        return this.mMraidView;
    }
}