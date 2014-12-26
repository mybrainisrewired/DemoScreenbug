package com.wmt.opengl.grid;

import com.wmt.opengl.Animation;
import com.wmt.opengl.FloatAnim;
import com.wmt.opengl.GLCanvas;
import com.wmt.remotectrl.EventPacket;
import javax.microedition.khronos.opengles.GL11;

public class FocusAnimation implements Animation {
    private final int FOCUS_ANIMATION_DURATION;
    private float mEndPosX;
    private float mEndPosY;
    public FloatAnim mFloatAnimX;
    public FloatAnim mFloatAnimY;
    private float mStartPosX;
    private float mStartPosY;
    private float zValue;

    public FocusAnimation(float z) {
        this.FOCUS_ANIMATION_DURATION = 200;
        this.mFloatAnimX = new FloatAnim();
        this.mFloatAnimY = new FloatAnim();
        this.zValue = z;
    }

    public float getEndPosX() {
        return this.mEndPosX;
    }

    public float getEndPosY() {
        return this.mEndPosY;
    }

    public float getStartPosX() {
        return this.mStartPosX;
    }

    public float getStartPosY() {
        return this.mStartPosY;
    }

    public boolean isValid() {
        return this.mFloatAnimX.isAnimating() || this.mFloatAnimY.isAnimating();
    }

    public void setAnimationValue(float mStartPosX, float mEndPosX, float mStartPosY, float mEndPosY) {
        this.mStartPosX = mStartPosX;
        this.mStartPosY = mStartPosY;
        this.mEndPosX = mEndPosX;
        this.mEndPosY = mEndPosY;
        this.mFloatAnimX.animateValue(mStartPosX, mEndPosX, EventPacket.SHOW_IME);
        this.mFloatAnimY.animateValue(mStartPosY, mEndPosY, EventPacket.SHOW_IME);
    }

    public void setZ(float z) {
        this.zValue = z;
    }

    public void updateApply(GLCanvas glCanvas, long time) {
        GL11 gl = glCanvas.getGL();
        float pixel2gl = glCanvas.getPixel2GL(this.zValue);
        gl.glTranslatef((this.mEndPosX - this.mFloatAnimX.getCurValue()) * pixel2gl, (this.mEndPosY - this.mFloatAnimY.getCurValue()) * pixel2gl, 0.0f);
    }
}