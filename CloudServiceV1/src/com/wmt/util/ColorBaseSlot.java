package com.wmt.util;

import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;

public class ColorBaseSlot {
    public static final int CB_HUE_COUNT = 12;
    public static final int CB_INDEX_BLACK = 12;
    public static final int CB_INDEX_GRAY = 13;
    public static final int CB_INDEX_INVALID = 65535;
    public static final int CB_INDEX_WHITE = 14;
    public static final int CB_TOTAL_COUNT = 15;
    public static final int INVALID_COLOR = -16777216;
    private static int[] sColors;
    public int[] color;
    public int majorIndex;

    public ColorBaseSlot() {
        this.color = new int[15];
    }

    public static ColorBaseSlot fromByteArray(byte[] data) {
        ColorBaseSlot colorBaseSlot = null;
        if (data != null && data.length == 64) {
            int byteIndex;
            colorBaseSlot = new ColorBaseSlot();
            int i = 0;
            int byteIndex2 = 0;
            while (i < 15) {
                byteIndex = byteIndex2 + 1;
                byteIndex2 = byteIndex + 1;
                byteIndex = byteIndex2 + 1;
                byteIndex2 = byteIndex + 1;
                colorBaseSlot.color[i] = ((unsignedByteToInt(data[byteIndex2]) | (unsignedByteToInt(data[byteIndex]) << 8)) | (unsignedByteToInt(data[byteIndex2]) << 16)) | (unsignedByteToInt(data[byteIndex]) << 24);
                i++;
            }
            byteIndex = byteIndex2 + 1;
            byteIndex2 = byteIndex + 1;
            byteIndex = byteIndex2 + 1;
            byteIndex2 = byteIndex + 1;
            colorBaseSlot.majorIndex = ((unsignedByteToInt(data[byteIndex2]) | (unsignedByteToInt(data[byteIndex]) << 8)) | (unsignedByteToInt(data[byteIndex2]) << 16)) | (unsignedByteToInt(data[byteIndex]) << 24);
        }
        return colorBaseSlot;
    }

    public static int getCBColor(int index) {
        if (index < 0 || index >= 15) {
            return INVALID_COLOR;
        }
        if (sColors == null) {
            sColors = new int[15];
            float[] hsv = new float[3];
            int i = 0;
            while (i < 12) {
                hsv[0] = (float) ((i * 360) / 12 + 15);
                hsv[1] = 0.7f;
                hsv[2] = 180.0f;
                sColors[i] = Color.HSVToColor(hsv);
                i++;
            }
            sColors[14] = Color.rgb(MotionEventCompat.ACTION_MASK, MotionEventCompat.ACTION_MASK, MotionEventCompat.ACTION_MASK);
            sColors[13] = Color.rgb(150, 150, 150);
            sColors[12] = Color.rgb(0, 0, 0);
        }
        return sColors[index];
    }

    public static int unsignedByteToInt(byte b) {
        return b & 255;
    }
}