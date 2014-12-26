package com.google.android.gms.internal;

import java.util.Arrays;

public final class kr {
    public static final Object adX;

    static {
        adX = new Object();
    }

    public static boolean equals(int[] field1, int[] field2) {
        if (field1 == null || field1.length == 0) {
            return field2 == null || field2.length == 0;
        } else {
            return Arrays.equals(field1, field2);
        }
    }

    public static boolean equals(Object[] field1, Object[] field2) {
        if (field1 == null) {
            boolean z = false;
        } else {
            int i = field1.length;
        }
        int length = field2 == null ? 0 : field2.length;
        int i2 = 0;
        int i3 = 0;
        while (true) {
            if (i3 >= i || field1[i3] != null) {
                int i4 = i2;
                while (i4 < length && field2[i4] == null) {
                    i4++;
                }
                int i5 = i3 >= i;
                i2 = i4 >= length;
                if (i5 != 0 && i2 != 0) {
                    return true;
                }
                if (i5 != i2 || !field1[i3].equals(field2[i4])) {
                    return false;
                }
                i2 = i4 + 1;
                i3++;
            } else {
                i3++;
            }
        }
    }

    public static int hashCode(int[] field) {
        return (field == null || field.length == 0) ? 0 : Arrays.hashCode(field);
    }

    public static int hashCode(Object[] field) {
        int i = 0;
        int length = field == null ? 0 : field.length;
        int i2 = 0;
        while (i2 < length) {
            Object obj = field[i2];
            if (obj != null) {
                i = i * 31 + obj.hashCode();
            }
            i2++;
        }
        return i;
    }
}