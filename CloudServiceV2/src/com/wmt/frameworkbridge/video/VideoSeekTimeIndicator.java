package com.wmt.frameworkbridge.video;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.IBinder;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.WindowManagerImpl;
import android.widget.TextView;

public class VideoSeekTimeIndicator extends TextView {
    private static final int DEFAULT_ANIM_DURATION = 200;
    private boolean mAnimation;
    private int mDuration;
    private ObjectAnimator mFadeInAnimator;
    private ObjectAnimator mFadeOutAnimator;
    private LayoutParams mLayoutParams;
    private boolean mShowing;
    private WindowManager mWindowManager;

    public VideoSeekTimeIndicator(Context context) {
        this(context, null);
    }

    public VideoSeekTimeIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoSeekTimeIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mDuration = 200;
        this.mAnimation = true;
        this.mWindowManager = WindowManagerImpl.getDefault();
    }

    private void cancelAnimation() {
        if (this.mFadeInAnimator != null) {
            this.mFadeInAnimator.cancel();
        }
        if (this.mFadeOutAnimator != null) {
            this.mFadeOutAnimator.cancel();
        }
    }

    private void dismissAnimation() {
        cancelAnimation();
        if (this.mFadeOutAnimator == null) {
            this.mFadeOutAnimator = ObjectAnimator.ofFloat(this, "alpha", new float[]{1.0f, 0.0f});
            this.mFadeOutAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator arg0) {
                    super.onAnimationEnd(arg0);
                    try {
                        if (VideoSeekTimeIndicator.this.mShowing) {
                            VideoSeekTimeIndicator.this.mWindowManager.removeView(VideoSeekTimeIndicator.this);
                            VideoSeekTimeIndicator.this.mShowing = false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        this.mFadeOutAnimator.setDuration((long) this.mDuration);
        this.mFadeOutAnimator.start();
    }

    private void showAnimation() {
        cancelAnimation();
        if (this.mFadeInAnimator == null) {
            this.mFadeInAnimator = ObjectAnimator.ofFloat(this, "alpha", new float[]{0.0f, 1.0f});
        }
        this.mFadeInAnimator.setDuration((long) this.mDuration);
        this.mFadeInAnimator.start();
    }

    public void dismiss() {
        if (this.mAnimation) {
            dismissAnimation();
        } else {
            try {
                if (this.mShowing) {
                    this.mWindowManager.removeView(this);
                    this.mShowing = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void enableAnimation(boolean enable) {
        this.mAnimation = enable;
    }

    public boolean isShowing() {
        return this.mShowing;
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    public void show(IBinder windowToken, int touchX, int touchY) {
        setAlpha(0.0f);
        LayoutParams lp = new LayoutParams(-2, -2, touchX, touchY, 1004, 768, -3);
        lp.gravity = 51;
        lp.token = windowToken;
        lp.setTitle("MovieSeekTimeIndicator");
        this.mLayoutParams = lp;
        this.mWindowManager.addView(this, lp);
        this.mShowing = true;
        if (this.mAnimation) {
            showAnimation();
        } else {
            setAlpha(1.0f);
        }
    }

    public void updatePosition(int touchX, int touchY) {
        if (this.mShowing) {
            LayoutParams lp = this.mLayoutParams;
            lp.x = touchX;
            lp.y = touchY;
            this.mWindowManager.updateViewLayout(this, lp);
        }
    }
}