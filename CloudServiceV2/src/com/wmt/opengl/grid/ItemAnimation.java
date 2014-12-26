package com.wmt.opengl.grid;

import com.wmt.opengl.Animation;
import com.wmt.opengl.FloatUtils;
import com.wmt.opengl.GLCanvas;
import javax.microedition.khronos.opengles.GL11;

public class ItemAnimation implements Animation {
    public static final int CUR_ALPHA = 3;
    public static final int CUR_ARC = 4;
    public static final int CUR_X = 0;
    public static final int CUR_Y = 1;
    public static final int CUR_Z = 2;
    public float mAnimatePosX;
    public float mAnimatePosY;
    public float mAnimatePosZ;
    public float mCurAlpha;
    public float mCurArc;
    public float mCurPosX;
    public float mCurPosY;
    public float mCurPosZ;
    public float mEndAlpha;
    public float mEndArc;
    public float mEndPosX;
    public float mEndPosY;
    public float mEndPosZ;
    private long mLastTime;
    public float mStartAlpha;
    public float mStartArc;
    public float mStartPosX;
    public float mStartPosY;
    public float mStartPosZ;
    public float zValue;

    public ItemAnimation(float mStartPosX, float mEndPosX, float mStartPosY, float mEndPosY, float z) {
        this.mStartArc = 0.0f;
        this.mStartAlpha = 1.0f;
        this.mEndArc = 0.0f;
        this.mEndAlpha = 1.0f;
        this.mCurArc = 0.0f;
        this.mCurAlpha = 1.0f;
        this.mAnimatePosY = 0.0f;
        this.mAnimatePosX = 0.0f;
        this.mAnimatePosZ = 0.0f;
        this.mLastTime = 0;
        this.mStartPosX = mStartPosX;
        this.mCurPosX = mStartPosX;
        this.mEndPosX = mEndPosX;
        this.mStartPosY = mStartPosY;
        this.mCurPosY = mStartPosY;
        this.mEndPosY = mEndPosY;
        this.mStartPosZ = 0.0f;
        this.mCurPosZ = 0.0f;
        this.mEndPosZ = 0.0f;
        this.zValue = z;
    }

    public ItemAnimation(float mStartPosX, float mEndPosX, float mStartPosY, float mEndPosY, float mStartPosZ, float mEndPosZ, float z) {
        this.mStartArc = 0.0f;
        this.mStartAlpha = 1.0f;
        this.mEndArc = 0.0f;
        this.mEndAlpha = 1.0f;
        this.mCurArc = 0.0f;
        this.mCurAlpha = 1.0f;
        this.mAnimatePosY = 0.0f;
        this.mAnimatePosX = 0.0f;
        this.mAnimatePosZ = 0.0f;
        this.mLastTime = 0;
        this.mStartPosX = mStartPosX;
        this.mCurPosX = mStartPosX;
        this.mEndPosX = mEndPosX;
        this.mStartPosY = mStartPosY;
        this.mCurPosY = mStartPosY;
        this.mEndPosY = mEndPosY;
        this.mStartPosZ = mStartPosZ;
        this.mCurPosZ = mStartPosZ;
        this.mEndPosZ = mEndPosZ;
        this.zValue = z;
    }

    public ItemAnimation(float mStartPosX, float mEndPosX, float mStartPosY, float mEndPosY, float mStartPosZ, float mEndPosZ, float z, float mStartAlpha, float mEndAlpha) {
        this.mStartArc = 0.0f;
        this.mStartAlpha = 1.0f;
        this.mEndArc = 0.0f;
        this.mEndAlpha = 1.0f;
        this.mCurArc = 0.0f;
        this.mCurAlpha = 1.0f;
        this.mAnimatePosY = 0.0f;
        this.mAnimatePosX = 0.0f;
        this.mAnimatePosZ = 0.0f;
        this.mLastTime = 0;
        this.mStartPosX = mStartPosX;
        this.mCurPosX = mStartPosX;
        this.mEndPosX = mEndPosX;
        this.mStartPosY = mStartPosY;
        this.mCurPosY = mStartPosY;
        this.mEndPosY = mEndPosY;
        this.mStartPosZ = mStartPosZ;
        this.mCurPosZ = mStartPosZ;
        this.mEndPosZ = mEndPosZ;
        this.mStartAlpha = mStartAlpha;
        this.mEndAlpha = mEndAlpha;
        this.mCurAlpha = mStartAlpha;
        this.zValue = z;
    }

    public void computeOffset() {
        float timepast = 0.03f;
        if (this.mLastTime == 0) {
            this.mLastTime = System.currentTimeMillis();
        } else {
            timepast = ((float) (System.currentTimeMillis() - this.mLastTime)) / 1000.0f;
            this.mLastTime = System.currentTimeMillis();
        }
        this.mCurPosX = FloatUtils.animate(this.mCurPosX, this.mEndPosX, timepast);
        this.mCurPosY = FloatUtils.animate(this.mCurPosY, this.mEndPosY, timepast);
        this.mCurPosZ = FloatUtils.animate(this.mCurPosZ, this.mEndPosZ, timepast);
        this.mCurArc = FloatUtils.animate(this.mCurArc, this.mEndArc, timepast);
        this.mCurAlpha = FloatUtils.animate(this.mCurAlpha, this.mEndAlpha, timepast);
        this.mAnimatePosX = this.mEndPosX - this.mCurPosX;
        this.mAnimatePosY = this.mEndPosY - this.mCurPosY;
        this.mAnimatePosZ = this.mEndPosZ - this.mCurPosZ;
    }

    public float getCurrentArgs(int type) {
        switch (type) {
            case CUR_X:
                return this.mCurPosX;
            case CUR_Y:
                return this.mCurPosY;
            case CUR_Z:
                return this.mCurPosZ;
            case CUR_ALPHA:
                return this.mCurAlpha;
            case CUR_ARC:
                return this.mCurArc;
            default:
                return -1.0f;
        }
    }

    public boolean isValid() {
        return (this.mCurPosX == this.mEndPosX && this.mCurPosY == this.mEndPosY && this.mCurPosZ == this.mEndPosZ && this.mCurAlpha == this.mEndAlpha && this.mCurArc == this.mEndArc) ? false : true;
    }

    public void restoreGL(GLCanvas glCanvas) {
        GL11 gl = glCanvas.getGL();
        float pixel2gl = glCanvas.getPixel2GL(this.zValue);
        float x = this.mAnimatePosX * pixel2gl;
        float y = this.mAnimatePosY * pixel2gl;
        float z = this.mAnimatePosZ;
        gl.glRotatef(-this.mCurArc, 0.0f, 0.0f, 1.0f);
        gl.glTranslatef(-x, -y, -z);
    }

    public void setRoatef(float mStartArc, float mEndArc) {
        this.mCurArc = mStartArc;
        this.mStartArc = mStartArc;
        this.mEndArc = mEndArc;
    }

    public void updateApply(GLCanvas glCanvas, long curTime) {
        GL11 gl = glCanvas.getGL();
        float pixel2gl = glCanvas.getPixel2GL(this.zValue);
        gl.glTranslatef(this.mAnimatePosX * pixel2gl, this.mAnimatePosY * pixel2gl, this.mAnimatePosZ);
        gl.glRotatef(this.mCurArc, 0.0f, 0.0f, 1.0f);
        gl.glColor4f(this.mCurAlpha, this.mCurAlpha, this.mCurAlpha, this.mCurAlpha);
    }
}