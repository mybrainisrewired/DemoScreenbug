package com.google.android.gms.internal;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.internal.ao.a;

public final class af extends a {
    private final AdListener lF;

    public af(AdListener adListener) {
        this.lF = adListener;
    }

    public void onAdClosed() {
        this.lF.onAdClosed();
    }

    public void onAdFailedToLoad(int errorCode) {
        this.lF.onAdFailedToLoad(errorCode);
    }

    public void onAdLeftApplication() {
        this.lF.onAdLeftApplication();
    }

    public void onAdLoaded() {
        this.lF.onAdLoaded();
    }

    public void onAdOpened() {
        this.lF.onAdOpened();
    }
}