package com.inmobi.monetization.internal;

import com.inmobi.commons.internal.Log;
import com.inmobi.re.container.IMWebView.IMWebViewListener;
import com.inmobi.re.container.mraidimpl.ResizeDimensions;
import java.util.Map;

// compiled from: InterstitialAd.java
class c implements IMWebViewListener {
    final /* synthetic */ InterstitialAd a;

    c(InterstitialAd interstitialAd) {
        this.a = interstitialAd;
    }

    public void onDismissAdScreen() {
        if (this.a.mAdListener != null) {
            this.a.mAdListener.onDismissAdScreen();
        }
    }

    public void onDisplayFailed() {
        this.a.b();
    }

    public void onError() {
        Log.debug(Constants.LOG_TAG, "Error loading the interstitial ad ");
        this.a.k.removeMessages(301);
        this.a.f = null;
        if (this.a.mAdListener != null) {
            this.a.mAdListener.onAdRequestFailed(AdErrorCode.INTERNAL_ERROR);
        }
    }

    public void onExpand() {
    }

    public void onExpandClose() {
    }

    public void onIncentCompleted(Map<Object, Object> map) {
        if (this.a.mAdListener != null) {
            this.a.mAdListener.onIncentCompleted(map);
        }
    }

    public void onLeaveApplication() {
        if (this.a.mAdListener != null) {
            this.a.mAdListener.onLeaveApplication();
        }
    }

    public void onResize(ResizeDimensions resizeDimensions) {
    }

    public void onResizeClose() {
    }

    public void onShowAdScreen() {
        if (this.a.mAdListener != null) {
            this.a.mAdListener.onShowAdScreen();
        }
    }

    public void onUserInteraction(Map<String, String> map) {
        if (this.a.mAdListener != null) {
            this.a.mAdListener.onAdInteraction(map);
        }
    }
}