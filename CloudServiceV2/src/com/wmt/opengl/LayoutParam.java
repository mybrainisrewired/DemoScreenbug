package com.wmt.opengl;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

public final class LayoutParam {
    public static final int UNIT_DP = 1;
    public static final int UNIT_PERCENT = 3;
    public static final int UNIT_PX = 0;
    public static final int UNIT_SP = 2;
    private static final float sDensity;
    private static final float sScaledScale;
    public int bottomUnit;
    public int bottomValue;
    public int leftUnit;
    public int leftValue;
    public int rightUnit;
    public int rightValue;
    public int topUnit;
    public int topValue;

    static {
        DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
        sDensity = dm.density;
        sScaledScale = dm.scaledDensity;
    }

    public static int calcLayoutParamValue(GLCanvas canvas, int unit, int value, boolean horz) {
        switch (unit) {
            case UNIT_PX:
                return value;
            case UNIT_DP:
                return (int) (((float) value) * sDensity + 0.5f);
            case UNIT_SP:
                return (int) (((float) value) * sScaledScale + 0.5f);
            case UNIT_PERCENT:
                if (horz) {
                    value = Math.round(((float) canvas.getSurfaceWidth()) * (((float) value) / 100.0f));
                } else {
                    value = Math.round(((float) canvas.getSurfaceHeight()) * (((float) value) / 100.0f));
                }
                return value;
            default:
                throw new RuntimeException("Unknown unit:" + unit);
        }
    }

    public void convertToPixel(GLCanvas canvas) {
        this.leftValue = calcLayoutParamValue(canvas, this.leftUnit, this.leftValue, true);
        this.topValue = calcLayoutParamValue(canvas, this.topUnit, this.topValue, false);
        this.rightValue = calcLayoutParamValue(canvas, this.rightUnit, this.rightValue, true);
        this.bottomValue = calcLayoutParamValue(canvas, this.bottomUnit, this.bottomValue, false);
        setUnit(UNIT_PX);
    }

    public void setFullSize() {
        setUnit(UNIT_PERCENT);
        setValue(UNIT_PX, UNIT_PX, Opcodes.ISUB, Opcodes.ISUB);
    }

    public void setUnit(int unit) {
        this.bottomUnit = unit;
        this.topUnit = unit;
        this.rightUnit = unit;
        this.leftUnit = unit;
    }

    public void setValue(int left, int top, int right, int bottom) {
        this.leftValue = left;
        this.topValue = top;
        this.rightValue = right;
        this.bottomValue = bottom;
    }
}