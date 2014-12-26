package com.android.volley.toolbox;

import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;

public class ScaleAnimationExt {
    private static OvershootInterpolator mInterpolator;
    private static ScaleAnimation mScaleAnimation;

    static {
        mScaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, 1, 0.5f, 1, 0.5f);
        mInterpolator = new OvershootInterpolator();
    }

    public static ScaleAnimation getmScaleAnimation() {
        mScaleAnimation.setDuration(300);
        mScaleAnimation.setInterpolator(mInterpolator);
        return mScaleAnimation;
    }
}