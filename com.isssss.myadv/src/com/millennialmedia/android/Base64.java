package com.millennialmedia.android;

import java.util.Arrays;

class Base64 {
    private static final char[] CA;
    private static final int[] IA;

    static {
        CA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
        IA = new int[256];
        Arrays.fill(IA, -1);
        int i = 0;
        int iS = CA.length;
        while (i < iS) {
            IA[CA[i]] = i;
            i++;
        }
        IA[61] = 0;
    }

    Base64() {
    }

    public static final byte[] decode(String str) {
        int sLen;
        if (str != null) {
            sLen = str.length();
        } else {
            sLen = 0;
        }
        if (sLen == 0) {
            return new byte[0];
        }
        int sepCnt = 0;
        int i = 0;
        while (i < sLen) {
            if (IA[str.charAt(i)] < 0) {
                sepCnt++;
            }
            i++;
        }
        if ((sLen - sepCnt) % 4 != 0) {
            return null;
        }
        int pad = 0;
        i = sLen;
        while (i > 1) {
            i--;
            if (IA[str.charAt(i)] > 0) {
                break;
            } else if (str.charAt(i) == '=') {
                pad++;
            }
        }
        int len = ((sLen - sepCnt) * 6) >> 3 - pad;
        byte[] dArr = new byte[len];
        int s = 0;
        int d = 0;
        while (d < len) {
            i = 0;
            int j = 0;
            int s2 = s;
            while (j < 4) {
                s = s2 + 1;
                int c = IA[str.charAt(s2)];
                if (c >= 0) {
                    i |= c << (18 - (j * 6));
                } else {
                    j--;
                }
                j++;
                s2 = s;
            }
            int d2 = d + 1;
            dArr[d] = (byte) (i >> 16);
            if (d2 < len) {
                d = d2 + 1;
                dArr[d2] = (byte) (i >> 8);
                if (d < len) {
                    d2 = d + 1;
                    dArr[d] = (byte) i;
                } else {
                    d2 = d;
                }
            }
            d = d2;
            s = s2;
        }
        return dArr;
    }

    public static final byte[] decode(byte[] sArr) {
        int sLen = sArr.length;
        int sepCnt = 0;
        int i = 0;
        while (i < sLen) {
            if (IA[sArr[i] & 255] < 0) {
                sepCnt++;
            }
            i++;
        }
        if ((sLen - sepCnt) % 4 != 0) {
            return null;
        }
        int pad = 0;
        i = sLen;
        while (i > 1) {
            i--;
            if (IA[sArr[i] & 255] > 0) {
                break;
            } else if (sArr[i] == (byte) 61) {
                pad++;
            }
        }
        int len = ((sLen - sepCnt) * 6) >> 3 - pad;
        byte[] dArr = new byte[len];
        int s = 0;
        int d = 0;
        while (d < len) {
            i = 0;
            int j = 0;
            int s2 = s;
            while (j < 4) {
                s = s2 + 1;
                int c = IA[sArr[s2] & 255];
                if (c >= 0) {
                    i |= c << (18 - (j * 6));
                } else {
                    j--;
                }
                j++;
                s2 = s;
            }
            int d2 = d + 1;
            dArr[d] = (byte) (i >> 16);
            if (d2 < len) {
                d = d2 + 1;
                dArr[d2] = (byte) (i >> 8);
                if (d < len) {
                    d2 = d + 1;
                    dArr[d] = (byte) i;
                } else {
                    d2 = d;
                }
            }
            d = d2;
            s = s2;
        }
        return dArr;
    }

    public static final char[] encodeToChar(byte[] sArr, boolean lineSep) {
        int sLen = sArr != null ? sArr.length : 0;
        if (sLen == 0) {
            return new char[0];
        }
        int eLen = (sLen / 3) * 3;
        int cCnt = (((sLen - 1) / 3) + 1) << 2;
        int dLen = cCnt + (lineSep ? ((cCnt - 1) / 76) << 1 : 0);
        char[] dArr = new char[dLen];
        int cc = 0;
        int d = 0;
        int s = 0;
        while (s < eLen) {
            int s2 = s + 1;
            s = s2 + 1;
            s2 = s + 1;
            int i = (((sArr[s] & 255) << 16) | ((sArr[s2] & 255) << 8)) | (sArr[s] & 255);
            int d2 = d + 1;
            dArr[d] = CA[(i >>> 18) & 63];
            d = d2 + 1;
            dArr[d2] = CA[(i >>> 12) & 63];
            d2 = d + 1;
            dArr[d] = CA[(i >>> 6) & 63];
            d = d2 + 1;
            dArr[d2] = CA[i & 63];
            if (lineSep) {
                cc++;
                if (cc == 19 && d < dLen - 2) {
                    d2 = d + 1;
                    dArr[d] = '\r';
                    d = d2 + 1;
                    dArr[d2] = '\n';
                    cc = 0;
                    d2 = d;
                    d = d2;
                    s = s2;
                }
            }
            d2 = d;
            d = d2;
            s = s2;
        }
        int left = sLen - eLen;
        if (left <= 0) {
            return dArr;
        }
        i = ((sArr[eLen] & 255) << 10) | (left == 2 ? (sArr[sLen - 1] & 255) << 2 : 0);
        dArr[dLen - 4] = CA[i >> 12];
        dArr[dLen - 3] = CA[(i >>> 6) & 63];
        dArr[dLen - 2] = left == 2 ? CA[i & 63] : '=';
        dArr[dLen - 1] = '=';
        return dArr;
    }

    public static final String encodeToString(byte[] sArr, boolean lineSep) {
        return new String(encodeToChar(sArr, lineSep));
    }
}