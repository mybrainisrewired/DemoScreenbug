package com.wmt.opengl;

import android.os.SystemClock;
import android.util.FloatMath;

public final class FloatAnim {
    private float mDelta;
    private int mDuration;
    private float mFinalValue;
    private long mStartTime;

    public static float getInterpolatedValue(float value, float delta, long duration, long interval) {
        float ratio = ((float) interval) / ((float) duration);
        return ratio >= 1.0f ? value : value + (1.0f - (0.5f - (FloatMath.cos(3.1415927f * ratio) * 0.5f))) * delta;
    }

    private float getInterpolatedValue(long currentTime) {
        float ratio = ((float) (currentTime - this.mStartTime)) / ((float) this.mDuration);
        if (ratio >= 1.0f) {
            this.mStartTime = 0;
            return this.mFinalValue;
        } else {
            return this.mFinalValue - (1.0f - (0.5f - (FloatMath.cos(3.1415927f * ratio) * 0.5f))) * this.mDelta;
        }
    }

    public void animateValue(float startValue, float finalValue, int durationMS) {
        this.mFinalValue = finalValue;
        this.mDelta = finalValue - startValue;
        this.mDuration = durationMS;
        this.mStartTime = SystemClock.uptimeMillis();
    }

    public float getCurValue() {
        return this.mStartTime == 0 ? this.mFinalValue : getInterpolatedValue(SystemClock.uptimeMillis());
    }

    public float getFinalValue() {
        return this.mFinalValue;
    }

    public int getTimeRemaining() {
        int duration = (int) (SystemClock.uptimeMillis() - this.mStartTime);
        return this.mDuration > duration ? this.mDuration - duration : 0;
    }

    public boolean isAnimating() {
        return this.mStartTime != 0;
    }

    public void stop() {
        this.mStartTime = 0;
    }
}