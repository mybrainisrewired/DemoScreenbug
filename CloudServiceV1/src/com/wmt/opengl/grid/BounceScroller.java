package com.wmt.opengl.grid;

import android.content.Context;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

public class BounceScroller {
    private static final int BOUNCE_MODE = 2;
    private static final int DEFAULT_DURATION = 250;
    private static final int FLING_MODE = 1;
    private static final int SCROLL_MODE = 0;
    private static final String TAG = "BounceScroller";
    private float mCoeffX;
    private float mCoeffY;
    private int mCurrX;
    private int mCurrY;
    private final float mDeceleration;
    private float mDeltaX;
    private float mDeltaY;
    private int mDuration;
    private float mDurationReciprocal;
    private int mFinalX;
    private int mFinalY;
    private boolean mFinished;
    private Interpolator mInterpolator;
    private int mMaxX;
    private int mMaxY;
    private int mMinX;
    private int mMinY;
    private int mMode;
    private ScrollerListener mScrollerListen;
    private long mStartTime;
    private int mStartX;
    private int mStartY;
    private float mVelocity;
    private float mViscousFluidNormalize;
    private float mViscousFluidScale;

    public static interface ScrollerListener {
        void onFlingBegin();

        void onFlingEnd();

        void onScrollEnd();
    }

    public BounceScroller(Context context) {
        this(context, null);
    }

    public BounceScroller(Context context, float gravity) {
        this.mCoeffX = 0.0f;
        this.mCoeffY = 1.0f;
        forceFinished(true);
        this.mInterpolator = null;
        this.mDeceleration = ((39.37f * gravity) * (context.getResources().getDisplayMetrics().density * 160.0f)) * ViewConfiguration.getScrollFriction();
    }

    public BounceScroller(Context context, Interpolator interpolator) {
        this.mCoeffX = 0.0f;
        this.mCoeffY = 1.0f;
        forceFinished(true);
        this.mInterpolator = interpolator;
        this.mDeceleration = (386.0878f * (context.getResources().getDisplayMetrics().density * 160.0f)) * ViewConfiguration.getScrollFriction();
    }

    private float viscousFluid(float x) {
        x *= this.mViscousFluidScale;
        if (x < 1.0f) {
            x -= 1.0f - ((float) Math.exp((double) (-x)));
        } else {
            x = 0.36787945f + (1.0f - 0.36787945f) * (1.0f - ((float) Math.exp((double) (1.0f - x))));
        }
        return x * this.mViscousFluidNormalize;
    }

    public void abortAnimation() {
        this.mCurrX = this.mFinalX;
        this.mCurrY = this.mFinalY;
        forceFinished(true);
    }

    public void bounce(float totalDistanceX, float startX, float totalDistanceY, float startY) {
        bounce(totalDistanceX, (int) startX, totalDistanceY, (int) startY);
    }

    public void bounce(float totalDistanceX, int startX, float totalDistanceY, int startY) {
        this.mMode = 2;
        forceFinished(false);
        float distanceX = Math.abs(totalDistanceX);
        float distanceY = Math.abs(totalDistanceY);
        this.mDuration = (int) (((float) Math.sqrt((double) ((2.0f * Math.max(distanceX, distanceY)) / this.mDeceleration))) * 1000.0f);
        this.mVelocity = (this.mDeceleration * ((float) this.mDuration)) / 1000.0f;
        this.mStartX = startX;
        this.mStartY = startY;
        if (distanceX != 0.0f) {
            this.mCoeffX = totalDistanceX / distanceX;
        } else {
            this.mCoeffX = 0.0f;
        }
        if (distanceY != 0.0f) {
            this.mCoeffY = totalDistanceY / distanceY;
        } else {
            this.mCoeffY = 0.0f;
        }
        this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
        this.mFinalX = Math.round(((float) startX) + this.mCoeffX * distanceX);
        this.mFinalY = Math.round(((float) startY) + this.mCoeffY * distanceY);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean computeScrollOffset() {
        throw new UnsupportedOperationException("Method not decompiled: com.wmt.opengl.grid.BounceScroller.computeScrollOffset():boolean");
        /*
        r13 = this;
        r12 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r11 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r6 = 1;
        r7 = r13.mFinished;
        if (r7 == 0) goto L_0x000b;
    L_0x0009:
        r6 = 0;
    L_0x000a:
        return r6;
    L_0x000b:
        r7 = android.view.animation.AnimationUtils.currentAnimationTimeMillis();
        r9 = r13.mStartTime;
        r7 = r7 - r9;
        r2 = (int) r7;
        r7 = r13.mDuration;
        if (r2 >= r7) goto L_0x00ff;
    L_0x0017:
        r7 = r13.mMode;
        switch(r7) {
            case 0: goto L_0x001d;
            case 1: goto L_0x0062;
            case 2: goto L_0x00c9;
            default: goto L_0x001c;
        };
    L_0x001c:
        goto L_0x000a;
    L_0x001d:
        r7 = (float) r2;
        r8 = r13.mDurationReciprocal;
        r5 = r7 * r8;
        r7 = r13.mInterpolator;
        if (r7 != 0) goto L_0x005b;
    L_0x0026:
        r5 = r13.viscousFluid(r5);
    L_0x002a:
        r7 = r13.mStartX;
        r8 = r13.mDeltaX;
        r8 = r8 * r5;
        r8 = java.lang.Math.round(r8);
        r7 = r7 + r8;
        r13.mCurrX = r7;
        r7 = r13.mStartY;
        r8 = r13.mDeltaY;
        r8 = r8 * r5;
        r8 = java.lang.Math.round(r8);
        r7 = r7 + r8;
        r13.mCurrY = r7;
        r7 = r13.mCurrX;
        r8 = r13.mFinalX;
        if (r7 != r8) goto L_0x000a;
    L_0x0048:
        r7 = r13.mCurrY;
        r8 = r13.mFinalY;
        if (r7 != r8) goto L_0x000a;
    L_0x004e:
        r13.forceFinished(r6);
        r7 = r13.mScrollerListen;
        if (r7 == 0) goto L_0x000a;
    L_0x0055:
        r7 = r13.mScrollerListen;
        r7.onScrollEnd();
        goto L_0x000a;
    L_0x005b:
        r7 = r13.mInterpolator;
        r5 = r7.getInterpolation(r5);
        goto L_0x002a;
    L_0x0062:
        r7 = (float) r2;
        r3 = r7 / r12;
        r7 = r13.mVelocity;
        r7 = r7 * r3;
        r8 = r13.mDeceleration;
        r8 = r8 * r3;
        r8 = r8 * r3;
        r8 = r8 / r11;
        r0 = r7 - r8;
        r7 = r13.mStartX;
        r8 = r13.mCoeffX;
        r8 = r8 * r0;
        r8 = java.lang.Math.round(r8);
        r7 = r7 + r8;
        r13.mCurrX = r7;
        r7 = r13.mCurrX;
        r8 = r13.mMaxX;
        r7 = java.lang.Math.min(r7, r8);
        r13.mCurrX = r7;
        r7 = r13.mCurrX;
        r8 = r13.mMinX;
        r7 = java.lang.Math.max(r7, r8);
        r13.mCurrX = r7;
        r7 = r13.mStartY;
        r8 = r13.mCoeffY;
        r8 = r8 * r0;
        r8 = java.lang.Math.round(r8);
        r7 = r7 + r8;
        r13.mCurrY = r7;
        r7 = r13.mCurrY;
        r8 = r13.mMaxY;
        r7 = java.lang.Math.min(r7, r8);
        r13.mCurrY = r7;
        r7 = r13.mCurrY;
        r8 = r13.mMinY;
        r7 = java.lang.Math.max(r7, r8);
        r13.mCurrY = r7;
        r7 = r13.mCurrX;
        r8 = r13.mFinalX;
        if (r7 != r8) goto L_0x000a;
    L_0x00b5:
        r7 = r13.mCurrY;
        r8 = r13.mFinalY;
        if (r7 != r8) goto L_0x000a;
    L_0x00bb:
        r13.forceFinished(r6);
        r7 = r13.mScrollerListen;
        if (r7 == 0) goto L_0x000a;
    L_0x00c2:
        r7 = r13.mScrollerListen;
        r7.onFlingEnd();
        goto L_0x000a;
    L_0x00c9:
        r7 = (float) r2;
        r4 = r7 / r12;
        r7 = r13.mVelocity;
        r7 = r7 * r4;
        r8 = r13.mDeceleration;
        r8 = r8 * r4;
        r8 = r8 * r4;
        r8 = r8 / r11;
        r1 = r7 - r8;
        r7 = r13.mStartX;
        r8 = r13.mCoeffX;
        r8 = r8 * r1;
        r8 = java.lang.Math.round(r8);
        r7 = r7 + r8;
        r13.mCurrX = r7;
        r7 = r13.mStartY;
        r8 = r13.mCoeffY;
        r8 = r8 * r1;
        r8 = java.lang.Math.round(r8);
        r7 = r7 + r8;
        r13.mCurrY = r7;
        r7 = r13.mCurrX;
        r8 = r13.mFinalX;
        if (r7 != r8) goto L_0x000a;
    L_0x00f4:
        r7 = r13.mCurrY;
        r8 = r13.mFinalY;
        if (r7 != r8) goto L_0x000a;
    L_0x00fa:
        r13.forceFinished(r6);
        goto L_0x000a;
    L_0x00ff:
        r7 = r13.mFinalX;
        r13.mCurrX = r7;
        r7 = r13.mFinalY;
        r13.mCurrY = r7;
        r13.forceFinished(r6);
        r7 = r13.mMode;
        if (r7 != 0) goto L_0x0119;
    L_0x010e:
        r7 = r13.mScrollerListen;
        if (r7 == 0) goto L_0x000a;
    L_0x0112:
        r7 = r13.mScrollerListen;
        r7.onScrollEnd();
        goto L_0x000a;
    L_0x0119:
        r7 = r13.mMode;
        if (r7 != r6) goto L_0x000a;
    L_0x011d:
        r7 = r13.mScrollerListen;
        if (r7 == 0) goto L_0x000a;
    L_0x0121:
        r7 = r13.mScrollerListen;
        r7.onFlingEnd();
        goto L_0x000a;
        */
    }

    public void extendDuration(int extend) {
        this.mDuration = timePassed() + extend;
        this.mDurationReciprocal = 1.0f / ((float) this.mDuration);
        forceFinished(false);
    }

    public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY) {
        this.mMode = 1;
        forceFinished(false);
        float velocity = (float) Math.hypot((double) velocityX, (double) velocityY);
        this.mVelocity = velocity;
        this.mDuration = (int) ((1000.0f * velocity) / this.mDeceleration);
        this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
        this.mStartX = startX;
        this.mStartY = startY;
        this.mCoeffX = velocity == 0.0f ? 1.0f : ((float) velocityX) / velocity;
        this.mCoeffY = velocity == 0.0f ? 1.0f : ((float) velocityY) / velocity;
        int totalDistance = (int) ((velocity * velocity) / (2.0f * this.mDeceleration));
        this.mMinX = minX;
        this.mMaxX = maxX;
        this.mMinY = minY;
        this.mMaxY = maxY;
        this.mFinalX = Math.round(((float) totalDistance) * this.mCoeffX) + startX;
        this.mFinalX = Math.min(this.mFinalX, this.mMaxX);
        this.mFinalX = Math.max(this.mFinalX, this.mMinX);
        this.mFinalY = Math.round(((float) totalDistance) * this.mCoeffY) + startY;
        this.mFinalY = Math.min(this.mFinalY, this.mMaxY);
        this.mFinalY = Math.max(this.mFinalY, this.mMinY);
    }

    public final void forceFinished(boolean finished) {
        this.mFinished = finished;
    }

    public float getCurrVelocity() {
        return this.mVelocity - (this.mDeceleration * ((float) timePassed())) / 2000.0f;
    }

    public final int getCurrX() {
        return this.mCurrX;
    }

    public final int getCurrY() {
        return this.mCurrY;
    }

    public final int getDuration() {
        return this.mDuration;
    }

    public final int getFinalX() {
        return this.mFinalX;
    }

    public final int getFinalY() {
        return this.mFinalY;
    }

    public final int getStartX() {
        return this.mStartX;
    }

    public final int getStartY() {
        return this.mStartY;
    }

    public final boolean isFinished() {
        return this.mFinished;
    }

    public void setFinalX(int newX) {
        this.mFinalX = newX;
        this.mDeltaX = (float) (this.mFinalX - this.mStartX);
        forceFinished(false);
    }

    public void setFinalY(int newY) {
        this.mFinalY = newY;
        this.mDeltaY = (float) (this.mFinalY - this.mStartY);
        forceFinished(false);
    }

    public void setScrollerListener(ScrollerListener sl) {
        this.mScrollerListen = sl;
    }

    public void startScroll(float startX, float startY, float dx, float dy) {
        startScroll((int) startX, (int) startY, (int) dx, (int) dy, DEFAULT_DURATION);
    }

    public void startScroll(int startX, int startY, int dx, int dy) {
        startScroll(startX, startY, dx, dy, DEFAULT_DURATION);
    }

    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        this.mMode = 0;
        forceFinished(false);
        this.mDuration = duration;
        this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
        this.mStartX = startX;
        this.mStartY = startY;
        this.mFinalX = startX + dx;
        this.mFinalY = startY + dy;
        this.mDeltaX = (float) dx;
        this.mDeltaY = (float) dy;
        this.mDurationReciprocal = 1.0f / ((float) this.mDuration);
        this.mViscousFluidScale = 8.0f;
        this.mViscousFluidNormalize = 1.0f;
        this.mViscousFluidNormalize = 1.0f / viscousFluid(1.0f);
    }

    public int timePassed() {
        return (int) (AnimationUtils.currentAnimationTimeMillis() - this.mStartTime);
    }
}