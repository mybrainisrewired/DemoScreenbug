package com.wmt.opengl.grid;

import android.os.SystemClock;
import com.wmt.opengl.GLCanvas;

public final class SlDownAnimation extends ItemAnimation {
    public static final String TAG = "slDownAnimation";
    private static long mAllAnimateReferenceTime;
    public static boolean mAnimateOnce;
    private double mCoefficientA;
    private double mCoefficientB;
    private float mDeltaY;
    private long mDuration;
    private double mMaxDeltaY;
    private long mStartAnimateTime;

    static {
        mAnimateOnce = true;
    }

    public SlDownAnimation(float mStartPosX, float mEndPosX, float mStartPosY, float mEndPosY, float z, long startAnimateTime, long duration) {
        super(mStartPosX, mEndPosX, mStartPosY, mEndPosY, z);
        this.mMaxDeltaY = 0.0d;
        this.mStartAnimateTime = startAnimateTime;
        this.mDuration = duration;
        if (mAnimateOnce) {
            mAnimateOnce = false;
            mAllAnimateReferenceTime = SystemClock.uptimeMillis() + 200;
        }
        this.mDeltaY = mEndPosY - mStartPosY;
        double verticalInit = ((3.0d * ((double) this.mDeltaY)) / ((double) this.mDuration)) * 1000.0d;
        this.mCoefficientA = (3.0d * ((double) this.mDeltaY)) / ((verticalInit * verticalInit) * verticalInit);
        this.mCoefficientB = (-6.0d * ((double) this.mDeltaY)) / (verticalInit * verticalInit);
        this.mMaxDeltaY = 0.0d;
    }

    public void updateApply(GLCanvas glCanvas, long curTime) {
        if (this.mDuration >= 0) {
            double animateTime = (double) (SystemClock.uptimeMillis() - mAllAnimateReferenceTime - this.mStartAnimateTime);
            if (animateTime > 0.0d && this.mCurPosY != this.mEndPosY) {
                double animateDelta = (((Math.sqrt(animateTime / 1000.0d) * animateTime) / 1000.0d) * this.mCoefficientB) / ((3.0d * this.mCoefficientA) * Math.sqrt(((double) this.mDuration) / 1000.0d)) - ((this.mCoefficientB / (2.0d * this.mCoefficientA)) * animateTime) / 1000.0d;
                if (Math.abs(animateDelta) - Math.abs(this.mMaxDeltaY) > 1.0E-6d) {
                    this.mMaxDeltaY = Math.abs(animateDelta);
                }
                if (this.mMaxDeltaY - Math.abs(animateDelta) > 1.0E-6d || this.mMaxDeltaY > ((double) Math.abs(this.mDeltaY))) {
                    this.mCurPosY = this.mEndPosY;
                } else {
                    this.mCurPosY = (float) (((double) this.mStartPosY) + animateDelta);
                }
            }
            this.mAnimatePosX = this.mEndPosX - this.mCurPosX;
            this.mAnimatePosY = this.mEndPosY - this.mCurPosY;
            this.mAnimatePosZ = this.mEndPosZ - this.mCurPosZ;
            float pixel2gl = glCanvas.getPixel2GL(this.zValue);
            glCanvas.getGL().glTranslatef(this.mAnimatePosX * pixel2gl, this.mAnimatePosY * pixel2gl, this.mAnimatePosZ * pixel2gl);
        }
    }
}