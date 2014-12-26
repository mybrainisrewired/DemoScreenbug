package com.google.android.gms.wallet.fragment;

import android.support.v4.media.TransportMediator;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;

public class Dimension {
    public static final int MATCH_PARENT = -1;
    public static final int UNIT_DIP = 1;
    public static final int UNIT_IN = 4;
    public static final int UNIT_MM = 5;
    public static final int UNIT_PT = 3;
    public static final int UNIT_PX = 0;
    public static final int UNIT_SP = 2;
    public static final int WRAP_CONTENT = -2;

    private Dimension() {
    }

    public static int a(long j, DisplayMetrics displayMetrics) {
        int i = (int) (j >>> 32);
        int i2 = (int) j;
        switch (i) {
            case UNIT_PX:
                i = UNIT_PX;
                break;
            case UNIT_DIP:
                i = UNIT_DIP;
                break;
            case UNIT_SP:
                i = UNIT_SP;
                break;
            case UNIT_PT:
                i = UNIT_PT;
                break;
            case UNIT_IN:
                i = UNIT_IN;
                break;
            case UNIT_MM:
                i = UNIT_MM;
                break;
            case TransportMediator.FLAG_KEY_MEDIA_NEXT:
                return TypedValue.complexToDimensionPixelSize(i2, displayMetrics);
            case 129:
                return i2;
            default:
                throw new IllegalStateException("Unexpected unit or type: " + i);
        }
        return Math.round(TypedValue.applyDimension(i, Float.intBitsToFloat(i2), displayMetrics));
    }

    public static long a(int i, float f) {
        switch (i) {
            case UNIT_PX:
            case UNIT_DIP:
            case UNIT_SP:
            case UNIT_PT:
            case UNIT_IN:
            case UNIT_MM:
                return f(i, Float.floatToIntBits(f));
            default:
                throw new IllegalArgumentException("Unrecognized unit: " + i);
        }
    }

    public static long a(TypedValue typedValue) {
        switch (typedValue.type) {
            case UNIT_MM:
                return f(TransportMediator.FLAG_KEY_MEDIA_NEXT, typedValue.data);
            case ApiEventType.API_MRAID_GET_ORIENTATION:
                return cz(typedValue.data);
            default:
                throw new IllegalArgumentException("Unexpected dimension type: " + typedValue.type);
        }
    }

    public static long cz(int i) {
        if (i >= 0) {
            return a((int)UNIT_PX, (float) i);
        }
        if (i == -1 || i == -2) {
            return f(129, i);
        }
        throw new IllegalArgumentException("Unexpected dimension value: " + i);
    }

    private static long f(int i, int i2) {
        return (((long) i) << 32) | (((long) i2) & 4294967295L);
    }
}