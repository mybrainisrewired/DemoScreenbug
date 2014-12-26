package org.codehaus.jackson.io;

import org.codehaus.jackson.org.objectweb.asm.Type;

public final class NumberInput {
    static final long L_BILLION = 1000000000;
    static final String MAX_LONG_STR;
    static final String MIN_LONG_STR_NO_SIGN;
    public static final String NASTY_SMALL_DOUBLE = "2.2250738585072012e-308";

    static {
        MIN_LONG_STR_NO_SIGN = String.valueOf(Long.MIN_VALUE).substring(1);
        MAX_LONG_STR = String.valueOf(Long.MAX_VALUE);
    }

    public static final boolean inLongRange(String numberStr, boolean negative) {
        String cmpStr = negative ? MIN_LONG_STR_NO_SIGN : MAX_LONG_STR;
        int cmpLen = cmpStr.length();
        int actualLen = numberStr.length();
        if (actualLen < cmpLen) {
            return true;
        }
        if (actualLen > cmpLen) {
            return false;
        }
        int i = 0;
        while (i < cmpLen) {
            int diff = numberStr.charAt(i) - cmpStr.charAt(i);
            if (diff != 0) {
                return diff < 0;
            } else {
                i++;
            }
        }
        return true;
    }

    public static final boolean inLongRange(char[] digitChars, int offset, int len, boolean negative) {
        String cmpStr = negative ? MIN_LONG_STR_NO_SIGN : MAX_LONG_STR;
        int cmpLen = cmpStr.length();
        if (len < cmpLen) {
            return true;
        }
        if (len > cmpLen) {
            return false;
        }
        int i = 0;
        while (i < cmpLen) {
            int diff = digitChars[offset + i] - cmpStr.charAt(i);
            if (diff != 0) {
                return diff < 0;
            } else {
                i++;
            }
        }
        return true;
    }

    public static double parseAsDouble(String input, double defaultValue) {
        if (input == null) {
            return defaultValue;
        }
        input = input.trim();
        if (input.length() == 0) {
            return defaultValue;
        }
        try {
            return parseDouble(input);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static int parseAsInt(String input, int defaultValue) {
        if (input == null) {
            return defaultValue;
        }
        input = input.trim();
        int len = input.length();
        if (len == 0) {
            return defaultValue;
        }
        char c;
        int i = 0;
        if (0 < len) {
            c = input.charAt(0);
            if (c == '+') {
                input = input.substring(1);
                len = input.length();
            } else if (c == '-') {
                i = 0 + 1;
            }
        }
        while (i < len) {
            c = input.charAt(i);
            if (c > '9' || c < '0') {
                try {
                    return (int) parseDouble(input);
                } catch (NumberFormatException e) {
                    return defaultValue;
                }
            }
            i++;
        }
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e2) {
            return defaultValue;
        }
    }

    public static long parseAsLong(String input, long defaultValue) {
        if (input == null) {
            return defaultValue;
        }
        input = input.trim();
        int len = input.length();
        if (len == 0) {
            return defaultValue;
        }
        char c;
        int i = 0;
        if (0 < len) {
            c = input.charAt(0);
            if (c == '+') {
                input = input.substring(1);
                len = input.length();
            } else if (c == '-') {
                i = 0 + 1;
            }
        }
        while (i < len) {
            c = input.charAt(i);
            if (c > '9' || c < '0') {
                try {
                    return (long) parseDouble(input);
                } catch (NumberFormatException e) {
                    return defaultValue;
                }
            }
            i++;
        }
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException e2) {
            return defaultValue;
        }
    }

    public static final double parseDouble(String numStr) throws NumberFormatException {
        return NASTY_SMALL_DOUBLE.equals(numStr) ? Double.MIN_NORMAL : Double.parseDouble(numStr);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final int parseInt(java.lang.String r10_str) {
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.io.NumberInput.parseInt(java.lang.String):int");
        /*
        r6 = 1;
        r2 = 0;
        r9 = 57;
        r8 = 48;
        r0 = r10.charAt(r2);
        r1 = r10.length();
        r7 = 45;
        if (r0 != r7) goto L_0x0013;
    L_0x0012:
        r2 = r6;
    L_0x0013:
        r4 = 1;
        if (r2 == 0) goto L_0x0031;
    L_0x0016:
        if (r1 == r6) goto L_0x001c;
    L_0x0018:
        r6 = 10;
        if (r1 <= r6) goto L_0x0021;
    L_0x001c:
        r3 = java.lang.Integer.parseInt(r10);
    L_0x0020:
        return r3;
    L_0x0021:
        r5 = r4 + 1;
        r0 = r10.charAt(r4);
    L_0x0027:
        if (r0 > r9) goto L_0x002b;
    L_0x0029:
        if (r0 >= r8) goto L_0x003a;
    L_0x002b:
        r3 = java.lang.Integer.parseInt(r10);
        r4 = r5;
        goto L_0x0020;
    L_0x0031:
        r6 = 9;
        if (r1 <= r6) goto L_0x008b;
    L_0x0035:
        r3 = java.lang.Integer.parseInt(r10);
        goto L_0x0020;
    L_0x003a:
        r3 = r0 + -48;
        if (r5 >= r1) goto L_0x0086;
    L_0x003e:
        r4 = r5 + 1;
        r0 = r10.charAt(r5);
        if (r0 > r9) goto L_0x0048;
    L_0x0046:
        if (r0 >= r8) goto L_0x004d;
    L_0x0048:
        r3 = java.lang.Integer.parseInt(r10);
        goto L_0x0020;
    L_0x004d:
        r6 = r3 * 10;
        r7 = r0 + -48;
        r3 = r6 + r7;
        if (r4 >= r1) goto L_0x0087;
    L_0x0055:
        r5 = r4 + 1;
        r0 = r10.charAt(r4);
        if (r0 > r9) goto L_0x005f;
    L_0x005d:
        if (r0 >= r8) goto L_0x0065;
    L_0x005f:
        r3 = java.lang.Integer.parseInt(r10);
        r4 = r5;
        goto L_0x0020;
    L_0x0065:
        r6 = r3 * 10;
        r7 = r0 + -48;
        r3 = r6 + r7;
        if (r5 >= r1) goto L_0x0086;
    L_0x006d:
        r4 = r5;
        r5 = r4 + 1;
        r0 = r10.charAt(r4);
        if (r0 > r9) goto L_0x0078;
    L_0x0076:
        if (r0 >= r8) goto L_0x007e;
    L_0x0078:
        r3 = java.lang.Integer.parseInt(r10);
        r4 = r5;
        goto L_0x0020;
    L_0x007e:
        r6 = r3 * 10;
        r7 = r0 + -48;
        r3 = r6 + r7;
        if (r5 < r1) goto L_0x006d;
    L_0x0086:
        r4 = r5;
    L_0x0087:
        if (r2 == 0) goto L_0x0020;
    L_0x0089:
        r3 = -r3;
        goto L_0x0020;
    L_0x008b:
        r5 = r4;
        goto L_0x0027;
        */
    }

    public static final int parseInt(char[] digitChars, int offset, int len) {
        int num = digitChars[offset] - 48;
        len += offset;
        offset++;
        if (offset >= len) {
            return num;
        }
        num = num * 10 + digitChars[offset] - 48;
        offset++;
        if (offset >= len) {
            return num;
        }
        num = num * 10 + digitChars[offset] - 48;
        offset++;
        if (offset >= len) {
            return num;
        }
        num = num * 10 + digitChars[offset] - 48;
        offset++;
        if (offset >= len) {
            return num;
        }
        num = num * 10 + digitChars[offset] - 48;
        offset++;
        if (offset >= len) {
            return num;
        }
        num = num * 10 + digitChars[offset] - 48;
        offset++;
        if (offset >= len) {
            return num;
        }
        num = num * 10 + digitChars[offset] - 48;
        offset++;
        if (offset >= len) {
            return num;
        }
        num = num * 10 + digitChars[offset] - 48;
        offset++;
        return offset < len ? num * 10 + digitChars[offset] - 48 : num;
    }

    public static final long parseLong(String str) {
        return str.length() <= 9 ? (long) parseInt(str) : Long.parseLong(str);
    }

    public static final long parseLong(char[] digitChars, int offset, int len) {
        int len1 = len - 9;
        return ((long) parseInt(digitChars, offset + len1, Type.ARRAY)) + ((long) parseInt(digitChars, offset, len1)) * 1000000000;
    }
}