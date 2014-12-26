package com.wmt.util;

import android.content.Context;
import android.os.SystemProperties;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import com.wmt.data.LocalAudioAll;
import org.codehaus.jackson.org.objectweb.asm.Type;

public class WmtScroller {
    private static final int BOUNCE_MODE = 2;
    private static final int DEFAULT_DURATION = 250;
    private static final int FLING_MODE = 1;
    private static final int SCROLL_MODE = 0;
    private final int mBounceFactor;
    private float mCoeffX;
    private float mCoeffY;
    private int mCurrX;
    private int mCurrY;
    private float mDeceleration;
    private final float mDecelerationOrig;
    private float mDeltaX;
    private float mDeltaY;
    private int mDuration;
    private float mDurationReciprocal;
    private int mFinalX;
    private int mFinalY;
    private boolean mFinished;
    private final int mFlingFactor;
    private Interpolator mInterpolator;
    private int mMaxX;
    private int mMaxY;
    private int mMinX;
    private int mMinY;
    private int mMode;
    private WmtScrollerListener mScrollerListener;
    private long mStartTime;
    private int mStartX;
    private int mStartY;
    private float mVelocity;
    private float mViscousFluidNormalize;
    private float mViscousFluidScale;

    public WmtScroller(Context context) {
        this(context, null, 0.0f);
    }

    public WmtScroller(Context context, float gravity) {
        this(context, null, gravity);
    }

    public WmtScroller(Context context, Interpolator interpolator, float gravity) {
        this.mCoeffX = 0.0f;
        this.mCoeffY = 1.0f;
        this.mFinished = true;
        this.mInterpolator = interpolator;
        float ppi = context.getResources().getDisplayMetrics().density * 160.0f;
        if (gravity == 0.0f) {
            gravity = 9.80665f;
        }
        this.mDecelerationOrig = ((39.37f * gravity) * ppi) * ViewConfiguration.getScrollFriction();
        this.mDeceleration = this.mDecelerationOrig;
        this.mFlingFactor = SystemProperties.getInt("ro.wmt.ui.flingfactor", Type.OBJECT);
        this.mBounceFactor = SystemProperties.getInt("ro.wmt.ui.bouncefactor", BOUNCE_MODE);
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
        this.mFinished = true;
    }

    public void bounce(float totalDistanceX, int startX, float totalDistanceY, int startY) {
        this.mDeceleration = this.mDecelerationOrig + this.mDecelerationOrig * ((float) this.mBounceFactor);
        this.mMode = 2;
        forceFinished(false);
        float distance = (float) Math.hypot((double) totalDistanceX, (double) totalDistanceY);
        this.mDuration = (int) (((float) Math.sqrt((double) ((2.0f * distance) / this.mDeceleration))) * 1000.0f);
        this.mVelocity = (this.mDeceleration * ((float) this.mDuration)) / 1000.0f;
        this.mStartX = startX;
        this.mStartY = startY;
        this.mCoeffX = totalDistanceX / distance;
        this.mCoeffY = totalDistanceY / distance;
        this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
        this.mFinalX = Math.round(((float) startX) + this.mCoeffX * distance);
        this.mFinalY = Math.round(((float) startY) + this.mCoeffY * distance);
    }

    public boolean computeScrollOffset() {
        if (this.mFinished) {
            return false;
        }
        int timePassed = (int) (AnimationUtils.currentAnimationTimeMillis() - this.mStartTime);
        if (timePassed < this.mDuration) {
            switch (this.mMode) {
                case LocalAudioAll.SORT_BY_TITLE:
                    float x = ((float) timePassed) * this.mDurationReciprocal;
                    if (this.mInterpolator == null) {
                        x = viscousFluid(x);
                    } else {
                        x = this.mInterpolator.getInterpolation(x);
                    }
                    this.mCurrX = this.mStartX + Math.round(this.mDeltaX * x);
                    this.mCurrY = this.mStartY + Math.round(this.mDeltaY * x);
                    if (this.mCurrX != this.mFinalX || this.mCurrY != this.mFinalY) {
                        return true;
                    }
                    this.mFinished = true;
                    return true;
                case FLING_MODE:
                    float timePassedSeconds = ((float) timePassed) / 1000.0f;
                    float distance = this.mVelocity * timePassedSeconds - ((this.mDeceleration * timePassedSeconds) * timePassedSeconds) / 2.0f;
                    this.mCurrX = this.mStartX + Math.round(this.mCoeffX * distance);
                    this.mCurrX = Math.min(this.mCurrX, this.mMaxX);
                    this.mCurrX = Math.max(this.mCurrX, this.mMinX);
                    this.mCurrY = this.mStartY + Math.round(this.mCoeffY * distance);
                    this.mCurrY = Math.min(this.mCurrY, this.mMaxY);
                    this.mCurrY = Math.max(this.mCurrY, this.mMinY);
                    if (this.mCurrX != this.mFinalX || this.mCurrY != this.mFinalY) {
                        return true;
                    }
                    this.mFinished = true;
                    if (this.mScrollerListener == null) {
                        return true;
                    }
                    this.mScrollerListener.onFlingEnd();
                    return true;
                case BOUNCE_MODE:
                    float timePassedSeconds1 = ((float) timePassed) / 1000.0f;
                    float distance1 = this.mVelocity * timePassedSeconds1 - ((this.mDeceleration * timePassedSeconds1) * timePassedSeconds1) / 2.0f;
                    this.mCurrX = this.mStartX + Math.round(this.mCoeffX * distance1);
                    this.mCurrY = this.mStartY + Math.round(this.mCoeffY * distance1);
                    if (this.mCurrX != this.mFinalX || this.mCurrY != this.mFinalY) {
                        return true;
                    }
                    this.mFinished = true;
                    if (this.mScrollerListener == null) {
                        return true;
                    }
                    this.mScrollerListener.onBounceEnd();
                    return true;
                default:
                    return true;
            }
        } else {
            this.mCurrX = this.mFinalX;
            this.mCurrY = this.mFinalY;
            this.mFinished = true;
            if (this.mScrollerListener == null) {
                return true;
            }
            if (this.mMode == 1) {
                this.mScrollerListener.onFlingEnd();
                return true;
            } else if (this.mMode != 2) {
                return true;
            } else {
                this.mScrollerListener.onBounceEnd();
                return true;
            }
        }
    }

    public void crossBorder() {
        if (this.mMode == 1 && this.mDeceleration != this.mDecelerationOrig + this.mDecelerationOrig * ((float) this.mFlingFactor)) {
            this.mMode = 1;
            this.mFinished = false;
            this.mStartX = getCurrX();
            this.mStartY = getCurrY();
            this.mVelocity = getCurrVelocity();
            this.mDeceleration = this.mDecelerationOrig + this.mDecelerationOrig * ((float) this.mFlingFactor);
            this.mDuration = (int) ((1000.0f * this.mVelocity) / this.mDeceleration);
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            int totalDistance = (int) ((this.mVelocity * this.mVelocity) / (2.0f * this.mDeceleration));
            this.mFinalX = this.mStartX + Math.round(((float) totalDistance) * this.mCoeffX);
            this.mFinalX = Math.min(this.mFinalX, this.mMaxX);
            this.mFinalX = Math.max(this.mFinalX, this.mMinX);
            this.mFinalY = this.mStartY + Math.round(((float) totalDistance) * this.mCoeffY);
            this.mFinalY = Math.min(this.mFinalY, this.mMaxY);
            this.mFinalY = Math.max(this.mFinalY, this.mMinY);
        }
    }

    public void extendDuration(int extend) {
        this.mDuration = timePassed() + extend;
        this.mDurationReciprocal = 1.0f / ((float) this.mDuration);
        this.mFinished = false;
    }

    public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY) {
        this.mDeceleration = this.mDecelerationOrig;
        this.mMode = 1;
        this.mFinished = false;
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
        this.mFinished = false;
    }

    public void setFinalY(int newY) {
        this.mFinalY = newY;
        this.mDeltaY = (float) (this.mFinalY - this.mStartY);
        this.mFinished = false;
    }

    public void setScrollerListener(WmtScrollerListener sl) {
        this.mScrollerListener = sl;
    }

    public void startScroll(int startX, int startY, int dx, int dy) {
        startScroll(startX, startY, dx, dy, DEFAULT_DURATION);
    }

    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        this.mMode = 0;
        this.mFinished = false;
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