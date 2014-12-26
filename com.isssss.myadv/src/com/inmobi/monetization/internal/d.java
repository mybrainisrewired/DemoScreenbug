package com.inmobi.monetization.internal;

import com.inmobi.re.container.IMWebView.IMWebViewListener;
import com.inmobi.re.container.mraidimpl.ResizeDimensions;
import java.util.Map;

// compiled from: BannerAd.java
class d implements IMWebViewListener {
    final /* synthetic */ BannerAd a;

    d(BannerAd bannerAd) {
        this.a = bannerAd;
    }

    public void onDismissAdScreen() {
        if (this.a.mAdListener != null) {
            this.a.mAdListener.onDismissAdScreen();
        }
        this.a.q.set(false);
    }

    public void onDisplayFailed() {
    }

    public void onError() {
        if (this.a.mAdListener != null) {
            this.a.mAdListener.onAdRequestFailed(AdErrorCode.INTERNAL_ERROR);
        }
    }

    public void onExpand() {
        if (this.a.mAdListener != null) {
            this.a.mAdListener.onShowAdScreen();
        }
    }

    public void onExpandClose() {
        if (this.a.mAdListener != null) {
            this.a.mAdListener.onDismissAdScreen();
        }
    }

    public void onIncentCompleted(Map<Object, Object> map) {
    }

    public void onLeaveApplication() {
        if (this.a.mAdListener != null) {
            this.a.mAdListener.onLeaveApplication();
        }
    }

    public void onResize(ResizeDimensions resizeDimensions) {
        if (this.a.mAdListener != null) {
            this.a.mAdListener.onShowAdScreen();
        }
    }

    public void onResizeClose() {
        if (this.a.mAdListener != null) {
            this.a.mAdListener.onDismissAdScreen();
        }
    }

    public void onShowAdScreen() {
        if (this.a.mAdListener != null) {
            this.a.mAdListener.onShowAdScreen();
        }
        this.a.q.set(true);
    }

    public void onUserInteraction(Map<String, String> map) {
        if (this.a.mAdListener != null) {
            this.a.mAdListener.onAdInteraction(map);
        }
    }
}