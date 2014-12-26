package com.wmt.opengl.grid;

import com.wmt.opengl.GLCanvas;
import com.wmt.opengl.TextureManager;
import javax.microedition.khronos.opengles.GL11;

public final class GridDrawArgs {
    public float[] arcVertex;
    public GLCanvas canvas;
    public FocusAnimation focusAnimation;
    public GL11 gl;
    public boolean isChecked;
    public boolean isFastScroll;
    public boolean isFocused;
    public boolean isTouchDowm;
    public float itemCenterRoateY;
    public float itemCenterX;
    public float itemCenterY;
    public float itemCenterZ;
    public float itemHeight;
    public int itemIndex;
    public float itemLeft;
    public float itemLeftZ;
    public int itemPartIndex;
    public float itemTop;
    public float itemWidth;
    public float oldFocusGLPosX;
    public float oldFocusGLPosY;
    public TextureManager tm;
    public float z;

    public GridDrawArgs() {
        this.focusAnimation = new FocusAnimation(-2.0f);
    }

    public boolean isFocusAnimation() {
        return this.focusAnimation.isValid();
    }

    public void moveFocus2CurPos(float curGLlX, float curGLlY) {
        this.focusAnimation.setAnimationValue(curGLlX - this.oldFocusGLPosX, 0.0f, this.oldFocusGLPosY - curGLlY, 0.0f);
    }

    public void recodeOldFocusGLPos(float glX, float glY) {
        this.oldFocusGLPosX = glX;
        this.oldFocusGLPosY = glY;
    }

    public void setZ(float az) {
        this.focusAnimation.setZ(az);
        this.z = az;
    }
}