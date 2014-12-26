package com.wmt.opengl;

public class FloatUtils {
    private static final float ANIMATION_SPEED = 4.0f;

    public static final float animate(float prevVal, float targetVal, float timeElapsed) {
        return animateAfterFactoringSpeed(prevVal, targetVal, timeElapsed * 4.0f);
    }

    private static final float animateAfterFactoringSpeed(float prevVal, float targetVal, float timeElapsed) {
        if (prevVal == targetVal) {
            return targetVal;
        }
        float newVal = prevVal + (targetVal - prevVal) * timeElapsed;
        if (Math.abs(newVal - prevVal) < 1.0E-5f || newVal == prevVal) {
            return targetVal;
        }
        if (prevVal <= targetVal || newVal >= targetVal) {
            return (prevVal >= targetVal || newVal <= targetVal) ? newVal : targetVal;
        } else {
            return targetVal;
        }
    }

    public static final float animateWithMaxSpeed(float prevVal, float targetVal, float timeElapsed, float maxSpeed) {
        float newTargetVal = targetVal;
        float delta = newTargetVal - prevVal;
        if (Math.abs(delta) > maxSpeed) {
            newTargetVal = prevVal + Math.signum(delta) * maxSpeed;
        }
        return animateAfterFactoringSpeed(prevVal, newTargetVal, timeElapsed * 4.0f);
    }

    public static final boolean boundsContainsPoint(float left, float right, float top, float bottom, float posX, float posY) {
        return posX >= left && posX <= right && posY >= top && posY <= bottom;
    }

    public static final float clamp(float val, float minVal, float maxVal) {
        if (val < minVal) {
            return minVal;
        }
        return val > maxVal ? maxVal : val;
    }

    public static final int clamp(int val, int minVal, int maxVal) {
        if (val < minVal) {
            return minVal;
        }
        return val > maxVal ? maxVal : val;
    }

    public static final float clampMax(float val, float maxVal) {
        return val > maxVal ? maxVal : val;
    }

    public static final float clampMin(float val, float minVal) {
        return val < minVal ? minVal : val;
    }

    public static final float max(float scaleX, float scaleY) {
        return scaleX > scaleY ? scaleX : scaleY;
    }
}