package com.wmt.opengl;

public final class Draw3DCalculator {
    private float mPixel2GLScale;
    private float mShowPlaneX_half;
    private float mShowPlaneY_half;
    private float mShowPlaneZ;

    public static float getPixel2GLScale(float fovy, float z, int winHeight) {
        return ((((float) Math.tan(((double) ((fovy / 2.0f) / 180.0f)) * 3.141592653589793d)) * Math.abs(z)) * 2.0f) / ((float) winHeight);
    }

    public float getPixel2GLScale() {
        return this.mPixel2GLScale;
    }

    public float getPlaneZ() {
        return this.mShowPlaneZ;
    }

    public void init(float fovy, float showPlaneZ, int winWidth, int winHeight) {
        this.mShowPlaneZ = showPlaneZ;
        this.mShowPlaneY_half = Math.abs(showPlaneZ) * ((float) Math.tan(((double) ((fovy / 2.0f) / 180.0f)) * 3.141592653589793d));
        this.mShowPlaneX_half = (this.mShowPlaneY_half * ((float) winWidth)) / ((float) winHeight);
        this.mPixel2GLScale = this.mShowPlaneY_half / ((float) (winHeight / 2));
    }

    public float pixel2GL(float value) {
        return this.mPixel2GLScale * value;
    }

    public float xPos2GL(float x) {
        return this.mPixel2GLScale * x - this.mShowPlaneX_half;
    }

    public float yPos2GL(float y) {
        return this.mShowPlaneY_half - this.mPixel2GLScale * y;
    }
}