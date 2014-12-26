package com.inmobi.monetization.internal;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

// compiled from: BannerAd.java
class e implements AnimationListener {
    final /* synthetic */ BannerAd a;

    e(BannerAd bannerAd) {
        this.a = bannerAd;
    }

    public void onAnimationEnd(Animation animation) {
        BannerAd.g(this.a);
    }

    public void onAnimationRepeat(Animation animation) {
    }

    public void onAnimationStart(Animation animation) {
    }
}