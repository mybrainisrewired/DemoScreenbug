package org.codehaus.jackson.io;

import org.codehaus.jackson.org.objectweb.asm.Type;

public final class NumberOutput {
    private static int BILLION;
    static final char[] FULL_TRIPLETS;
    static final byte[] FULL_TRIPLETS_B;
    static final char[] LEADING_TRIPLETS;
    private static long MAX_INT_AS_LONG;
    private static int MILLION;
    private static long MIN_INT_AS_LONG;
    private static final char NULL_CHAR = '\u0000';
    static final String SMALLEST_LONG;
    private static long TEN_BILLION_L;
    private static long THOUSAND_L;
    static final String[] sSmallIntStrs;
    static final String[] sSmallIntStrs2;

    static {
        MILLION = 1000000;
        BILLION = 1000000000;
        TEN_BILLION_L = 10000000000L;
        THOUSAND_L = 1000;
        MIN_INT_AS_LONG = -2147483648L;
        MAX_INT_AS_LONG = 2147483647L;
        SMALLEST_LONG = String.valueOf(Long.MIN_VALUE);
        LEADING_TRIPLETS = new char[4000];
        FULL_TRIPLETS = new char[4000];
        int i = 0;
        int i1 = 0;
        while (i1 < 10) {
            char l1;
            char f1 = (char) (i1 + 48);
            if (i1 == 0) {
                l1 = '\u0000';
            } else {
                l1 = f1;
            }
            int i2 = 0;
            while (i2 < 10) {
                char l2;
                char f2 = (char) (i2 + 48);
                if (i1 == 0 && i2 == 0) {
                    l2 = '\u0000';
                } else {
                    l2 = f2;
                }
                int i3 = 0;
                while (i3 < 10) {
                    char f3 = (char) (i3 + 48);
                    LEADING_TRIPLETS[i] = l1;
                    LEADING_TRIPLETS[i + 1] = l2;
                    LEADING_TRIPLETS[i + 2] = f3;
                    FULL_TRIPLETS[i] = f1;
                    FULL_TRIPLETS[i + 1] = f2;
                    FULL_TRIPLETS[i + 2] = f3;
                    i += 4;
                    i3++;
                }
                i2++;
            }
            i1++;
        }
        FULL_TRIPLETS_B = new byte[4000];
        int i4 = 0;
        while (i4 < 4000) {
            FULL_TRIPLETS_B[i4] = (byte) FULL_TRIPLETS[i4];
            i4++;
        }
        sSmallIntStrs = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        sSmallIntStrs2 = new String[]{"-1", "-2", "-3", "-4", "-5", "-6", "-7", "-8", "-9", "-10"};
    }

    private static int calcLongStrLength(long posValue) {
        int len = Type.OBJECT;
        long comp = TEN_BILLION_L;
        while (posValue >= comp && len != 19) {
            len++;
            comp = comp << 3 + comp << 1;
        }
        return len;
    }

    private static int outputFullTriplet(int triplet, byte[] buffer, int offset) {
        int digitOffset = triplet << 2;
        int offset2 = offset + 1;
        int digitOffset2 = digitOffset + 1;
        buffer[offset] = FULL_TRIPLETS_B[digitOffset];
        offset = offset2 + 1;
        digitOffset = digitOffset2 + 1;
        buffer[offset2] = FULL_TRIPLETS_B[digitOffset2];
        offset2 = offset + 1;
        buffer[offset] = FULL_TRIPLETS_B[digitOffset];
        return offset2;
    }

    private static int outputFullTriplet(int triplet, char[] buffer, int offset) {
        int digitOffset = triplet << 2;
        int offset2 = offset + 1;
        int digitOffset2 = digitOffset + 1;
        buffer[offset] = FULL_TRIPLETS[digitOffset];
        offset = offset2 + 1;
        digitOffset = digitOffset2 + 1;
        buffer[offset2] = FULL_TRIPLETS[digitOffset2];
        offset2 = offset + 1;
        buffer[offset] = FULL_TRIPLETS[digitOffset];
        return offset2;
    }

    public static int outputInt(int value, byte[] buffer, int offset) {
        if (value < 0 && value == Integer.MIN_VALUE) {
            return outputLong((long) value, buffer, offset);
        }
        int offset2 = offset + 1;
        buffer[offset] = (byte) 45;
        value = -value;
        offset = offset2;
        int thousands;
        if (value < MILLION) {
            if (value >= 1000) {
                thousands = value / 1000;
                offset = outputFullTriplet(value - thousands * 1000, buffer, outputLeadingTriplet(thousands, buffer, offset));
            } else if (value < 10) {
                offset2 = offset + 1;
                buffer[offset] = (byte) (value + 48);
                offset = offset2;
            } else {
                offset = outputLeadingTriplet(value, buffer, offset);
            }
            return offset;
        } else {
            boolean hasBillions = value >= BILLION;
            if (hasBillions) {
                value -= BILLION;
                if (value >= BILLION) {
                    value -= BILLION;
                    offset2 = offset + 1;
                    buffer[offset] = (byte) 50;
                    offset = offset2;
                } else {
                    offset2 = offset + 1;
                    buffer[offset] = (byte) 49;
                    offset = offset2;
                }
            }
            int newValue = value / 1000;
            int ones = value - newValue * 1000;
            value = newValue;
            newValue /= 1000;
            thousands = value - newValue * 1000;
            if (hasBillions) {
                offset = outputFullTriplet(newValue, buffer, offset);
            } else {
                offset = outputLeadingTriplet(newValue, buffer, offset);
            }
            return outputFullTriplet(ones, buffer, outputFullTriplet(thousands, buffer, offset));
        }
    }

    public static int outputInt(int value, char[] buffer, int offset) {
        if (value < 0 && value == Integer.MIN_VALUE) {
            return outputLong((long) value, buffer, offset);
        }
        int offset2 = offset + 1;
        buffer[offset] = '-';
        value = -value;
        offset = offset2;
        int thousands;
        if (value < MILLION) {
            if (value >= 1000) {
                thousands = value / 1000;
                offset = outputFullTriplet(value - thousands * 1000, buffer, outputLeadingTriplet(thousands, buffer, offset));
            } else if (value < 10) {
                offset2 = offset + 1;
                buffer[offset] = (char) (value + 48);
                offset = offset2;
            } else {
                offset = outputLeadingTriplet(value, buffer, offset);
            }
            return offset;
        } else {
            boolean hasBillions = value >= BILLION;
            if (hasBillions) {
                value -= BILLION;
                if (value >= BILLION) {
                    value -= BILLION;
                    offset2 = offset + 1;
                    buffer[offset] = '2';
                    offset = offset2;
                } else {
                    offset2 = offset + 1;
                    buffer[offset] = '1';
                    offset = offset2;
                }
            }
            int newValue = value / 1000;
            int ones = value - newValue * 1000;
            value = newValue;
            newValue /= 1000;
            thousands = value - newValue * 1000;
            if (hasBillions) {
                offset = outputFullTriplet(newValue, buffer, offset);
            } else {
                offset = outputLeadingTriplet(newValue, buffer, offset);
            }
            return outputFullTriplet(ones, buffer, outputFullTriplet(thousands, buffer, offset));
        }
    }

    private static int outputLeadingTriplet(int triplet, byte[] buffer, int offset) {
        int offset2;
        int digitOffset = triplet << 2;
        int digitOffset2 = digitOffset + 1;
        char c = LEADING_TRIPLETS[digitOffset];
        if (c != '\u0000') {
            offset2 = offset + 1;
            buffer[offset] = (byte) c;
            offset = offset2;
        }
        digitOffset = digitOffset2 + 1;
        c = LEADING_TRIPLETS[digitOffset2];
        if (c != '\u0000') {
            offset2 = offset + 1;
            buffer[offset] = (byte) c;
            offset = offset2;
        }
        offset2 = offset + 1;
        buffer[offset] = (byte) LEADING_TRIPLETS[digitOffset];
        return offset2;
    }

    private static int outputLeadingTriplet(int triplet, char[] buffer, int offset) {
        int offset2;
        int digitOffset = triplet << 2;
        int digitOffset2 = digitOffset + 1;
        char c = LEADING_TRIPLETS[digitOffset];
        if (c != '\u0000') {
            offset2 = offset + 1;
            buffer[offset] = c;
            offset = offset2;
        }
        digitOffset = digitOffset2 + 1;
        c = LEADING_TRIPLETS[digitOffset2];
        if (c != '\u0000') {
            offset2 = offset + 1;
            buffer[offset] = c;
            offset = offset2;
        }
        offset2 = offset + 1;
        buffer[offset] = LEADING_TRIPLETS[digitOffset];
        return offset2;
    }

    public static int outputLong(long value, byte[] buffer, int offset) {
        if (value < 0) {
            if (value > MIN_INT_AS_LONG) {
                return outputInt((int) value, buffer, offset);
            }
            int offset2;
            if (value == Long.MIN_VALUE) {
                int len = SMALLEST_LONG.length();
                int i = 0;
                offset2 = offset;
                while (i < len) {
                    offset = offset2 + 1;
                    buffer[offset2] = (byte) SMALLEST_LONG.charAt(i);
                    i++;
                    offset2 = offset;
                }
                return offset2;
            } else {
                offset2 = offset + 1;
                buffer[offset] = (byte) 45;
                value = -value;
                offset = offset2;
            }
        } else if (value <= MAX_INT_AS_LONG) {
            return outputInt((int) value, buffer, offset);
        }
        int origOffset = offset;
        offset += calcLongStrLength(value);
        int ptr = offset;
        while (value > MAX_INT_AS_LONG) {
            ptr -= 3;
            long newValue = value / THOUSAND_L;
            outputFullTriplet((int) (value - THOUSAND_L * newValue), buffer, ptr);
            value = newValue;
        }
        int ivalue = (int) value;
        while (ivalue >= 1000) {
            ptr -= 3;
            int newValue2 = ivalue / 1000;
            outputFullTriplet(ivalue - newValue2 * 1000, buffer, ptr);
            ivalue = newValue2;
        }
        outputLeadingTriplet(ivalue, buffer, origOffset);
        return offset;
    }

    public static int outputLong(long value, char[] buffer, int offset) {
        if (value < 0) {
            if (value > MIN_INT_AS_LONG) {
                return outputInt((int) value, buffer, offset);
            }
            if (value == Long.MIN_VALUE) {
                int len = SMALLEST_LONG.length();
                SMALLEST_LONG.getChars(0, len, buffer, offset);
                return offset + len;
            } else {
                int offset2 = offset + 1;
                buffer[offset] = '-';
                value = -value;
                offset = offset2;
            }
        } else if (value <= MAX_INT_AS_LONG) {
            return outputInt((int) value, buffer, offset);
        }
        int origOffset = offset;
        offset += calcLongStrLength(value);
        int ptr = offset;
        while (value > MAX_INT_AS_LONG) {
            ptr -= 3;
            long newValue = value / THOUSAND_L;
            outputFullTriplet((int) (value - THOUSAND_L * newValue), buffer, ptr);
            value = newValue;
        }
        int ivalue = (int) value;
        while (ivalue >= 1000) {
            ptr -= 3;
            int newValue2 = ivalue / 1000;
            outputFullTriplet(ivalue - newValue2 * 1000, buffer, ptr);
            ivalue = newValue2;
        }
        outputLeadingTriplet(ivalue, buffer, origOffset);
        return offset;
    }

    public static String toString(double value) {
        return Double.toString(value);
    }

    public static String toString(int value) {
        if (value < sSmallIntStrs.length && value >= 0) {
            return sSmallIntStrs[value];
        }
        int v2 = (-value) - 1;
        if (v2 < sSmallIntStrs2.length) {
            return sSmallIntStrs2[v2];
        }
        return Integer.toString(value);
    }

    public static String toString(long value) {
        return (value > 2147483647L || value < -2147483648L) ? Long.toString(value) : toString((int) value);
    }
}