package com.inmobi.monetization.internal;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import com.facebook.ads.internal.AdWebViewUtils;
import com.inmobi.commons.AnimationType;
import com.inmobi.monetization.internal.anim.Rotate3dAnimation;
import com.inmobi.monetization.internal.anim.Rotate3dAnimationVert;

// compiled from: AnimationController.java
class a {
    private BannerAd a;
    private AnimationListener b;

    public a(BannerAd bannerAd, AnimationListener animationListener) {
        this.a = bannerAd;
        this.b = animationListener;
    }

    public void a(AnimationType animationType) {
        Animation alphaAnimation;
        Animation alphaAnimation2;
        if (animationType == AnimationType.ANIMATION_ALPHA) {
            alphaAnimation = new AlphaAnimation(0.0f, 0.5f);
            alphaAnimation2 = new AlphaAnimation(0.5f, 1.0f);
            alphaAnimation.setDuration(AdWebViewUtils.DEFAULT_IMPRESSION_DELAY_MS);
            alphaAnimation.setFillAfter(false);
            alphaAnimation.setAnimationListener(this.b);
            alphaAnimation.setInterpolator(new DecelerateInterpolator());
            alphaAnimation2.setDuration(500);
            alphaAnimation2.setFillAfter(false);
            alphaAnimation2.setAnimationListener(this.b);
            alphaAnimation2.setInterpolator(new DecelerateInterpolator());
            this.a.a(alphaAnimation);
            this.a.b(alphaAnimation2);
        } else if (animationType == AnimationType.ROTATE_HORIZONTAL_AXIS) {
            alphaAnimation = new Rotate3dAnimation(0.0f, 90.0f, ((float) this.a.b()) / 2.0f, ((float) this.a.c()) / 2.0f, 0.0f, true);
            alphaAnimation2 = new Rotate3dAnimation(270.0f, 360.0f, ((float) this.a.b()) / 2.0f, ((float) this.a.c()) / 2.0f, 0.0f, true);
            alphaAnimation.setDuration(500);
            alphaAnimation.setFillAfter(false);
            alphaAnimation.setAnimationListener(this.b);
            alphaAnimation.setInterpolator(new AccelerateInterpolator());
            alphaAnimation2.setDuration(500);
            alphaAnimation2.setFillAfter(false);
            alphaAnimation2.setAnimationListener(this.b);
            alphaAnimation2.setInterpolator(new DecelerateInterpolator());
            this.a.a(alphaAnimation);
            this.a.b(alphaAnimation2);
        } else if (animationType == AnimationType.ROTATE_VERTICAL_AXIS) {
            alphaAnimation = new Rotate3dAnimationVert(0.0f, 90.0f, ((float) this.a.b()) / 2.0f, ((float) this.a.c()) / 2.0f, 0.0f, true);
            alphaAnimation2 = new Rotate3dAnimationVert(270.0f, 360.0f, ((float) this.a.b()) / 2.0f, ((float) this.a.c()) / 2.0f, 0.0f, true);
            alphaAnimation.setDuration(500);
            alphaAnimation.setFillAfter(false);
            alphaAnimation.setAnimationListener(this.b);
            alphaAnimation.setInterpolator(new AccelerateInterpolator());
            alphaAnimation2.setDuration(500);
            alphaAnimation2.setFillAfter(false);
            alphaAnimation2.setAnimationListener(this.b);
            alphaAnimation2.setInterpolator(new DecelerateInterpolator());
            this.a.a(alphaAnimation);
            this.a.b(alphaAnimation2);
        }
        this.a.c(this.a.a());
    }
}