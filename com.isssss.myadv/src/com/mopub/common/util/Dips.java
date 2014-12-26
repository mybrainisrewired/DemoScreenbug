package com.mopub.common.util;

import android.content.Context;
import android.util.TypedValue;

public class Dips {
    public static float asFloatPixels(float dips, Context context) {
        return TypedValue.applyDimension(1, dips, context.getResources().getDisplayMetrics());
    }

    public static int asIntPixels(float dips, Context context) {
        return (int) (asFloatPixels(dips, context) + 0.5f);
    }

    public static float dipsToFloatPixels(float dips, Context context) {
        return getDensity(context) * dips;
    }

    public static int dipsToIntPixels(float dips, Context context) {
        return (int) (dipsToFloatPixels(dips, context) + 0.5f);
    }

    private static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static float pixelsToFloatDips(float pixels, Context context) {
        return pixels / getDensity(context);
    }

    public static int pixelsToIntDips(float pixels, Context context) {
        return (int) (pixelsToFloatDips(pixels, context) + 0.5f);
    }
}