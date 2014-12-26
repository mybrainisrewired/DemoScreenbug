package com.mopub.mobileads.util;

import java.io.UnsupportedEncodingException;

public class Base64 {
    static final /* synthetic */ boolean $assertionsDisabled;
    public static final int CRLF = 4;
    public static final int DEFAULT = 0;
    public static final int NO_CLOSE = 16;
    public static final int NO_PADDING = 1;
    public static final int NO_WRAP = 2;
    public static final int URL_SAFE = 8;

    static abstract class Coder {
        public int op;
        public byte[] output;

        Coder() {
        }

        public abstract int maxOutputSize(int i);

        public abstract boolean process(byte[] bArr, int i, int i2, boolean z);
    }

    static class Decoder extends Coder {
        private static final int[] DECODE;
        private static final int[] DECODE_WEBSAFE;
        private static final int EQUALS = -2;
        private static final int SKIP = -1;
        private final int[] alphabet;
        private int state;
        private int value;

        static {
            int[] iArr = new int[256];
            iArr[0] = -1;
            iArr[1] = -1;
            iArr[2] = -1;
            iArr[3] = -1;
            iArr[4] = -1;
            iArr[5] = -1;
            iArr[6] = -1;
            iArr[7] = -1;
            iArr[8] = -1;
            iArr[9] = -1;
            iArr[10] = -1;
            iArr[11] = -1;
            iArr[12] = -1;
            iArr[13] = -1;
            iArr[14] = -1;
            iArr[15] = -1;
            iArr[16] = -1;
            iArr[17] = -1;
            iArr[18] = -1;
            iArr[19] = -1;
            iArr[20] = -1;
            iArr[21] = -1;
            iArr[22] = -1;
            iArr[23] = -1;
            iArr[24] = -1;
            iArr[25] = -1;
            iArr[26] = -1;
            iArr[27] = -1;
            iArr[28] = -1;
            iArr[29] = -1;
            iArr[30] = -1;
            iArr[31] = -1;
            iArr[32] = -1;
            iArr[33] = -1;
            iArr[34] = -1;
            iArr[35] = -1;
            iArr[36] = -1;
            iArr[37] = -1;
            iArr[38] = -1;
            iArr[39] = -1;
            iArr[40] = -1;
            iArr[41] = -1;
            iArr[42] = -1;
            iArr[43] = 62;
            iArr[44] = -1;
            iArr[45] = -1;
            iArr[46] = -1;
            iArr[47] = 63;
            iArr[48] = 52;
            iArr[49] = 53;
            iArr[50] = 54;
            iArr[51] = 55;
            iArr[52] = 56;
            iArr[53] = 57;
            iArr[54] = 58;
            iArr[55] = 59;
            iArr[56] = 60;
            iArr[57] = 61;
            iArr[58] = -1;
            iArr[59] = -1;
            iArr[60] = -1;
            iArr[61] = -2;
            iArr[62] = -1;
            iArr[63] = -1;
            iArr[64] = -1;
            iArr[66] = 1;
            iArr[67] = 2;
            iArr[68] = 3;
            iArr[69] = 4;
            iArr[70] = 5;
            iArr[71] = 6;
            iArr[72] = 7;
            iArr[73] = 8;
            iArr[74] = 9;
            iArr[75] = 10;
            iArr[76] = 11;
            iArr[77] = 12;
            iArr[78] = 13;
            iArr[79] = 14;
            iArr[80] = 15;
            iArr[81] = 16;
            iArr[82] = 17;
            iArr[83] = 18;
            iArr[84] = 19;
            iArr[85] = 20;
            iArr[86] = 21;
            iArr[87] = 22;
            iArr[88] = 23;
            iArr[89] = 24;
            iArr[90] = 25;
            iArr[91] = -1;
            iArr[92] = -1;
            iArr[93] = -1;
            iArr[94] = -1;
            iArr[95] = -1;
            iArr[96] = -1;
            iArr[97] = 26;
            iArr[98] = 27;
            iArr[99] = 28;
            iArr[100] = 29;
            iArr[101] = 30;
            iArr[102] = 31;
            iArr[103] = 32;
            iArr[104] = 33;
            iArr[105] = 34;
            iArr[106] = 35;
            iArr[107] = 36;
            iArr[108] = 37;
            iArr[109] = 38;
            iArr[110] = 39;
            iArr[111] = 40;
            iArr[112] = 41;
            iArr[113] = 42;
            iArr[114] = 43;
            iArr[115] = 44;
            iArr[116] = 45;
            iArr[117] = 46;
            iArr[118] = 47;
            iArr[119] = 48;
            iArr[120] = 49;
            iArr[121] = 50;
            iArr[122] = 51;
            iArr[123] = -1;
            iArr[124] = -1;
            iArr[125] = -1;
            iArr[126] = -1;
            iArr[127] = -1;
            iArr[128] = -1;
            iArr[129] = -1;
            iArr[130] = -1;
            iArr[131] = -1;
            iArr[132] = -1;
            iArr[133] = -1;
            iArr[134] = -1;
            iArr[135] = -1;
            iArr[136] = -1;
            iArr[137] = -1;
            iArr[138] = -1;
            iArr[139] = -1;
            iArr[140] = -1;
            iArr[141] = -1;
            iArr[142] = -1;
            iArr[143] = -1;
            iArr[144] = -1;
            iArr[145] = -1;
            iArr[146] = -1;
            iArr[147] = -1;
            iArr[148] = -1;
            iArr[149] = -1;
            iArr[150] = -1;
            iArr[151] = -1;
            iArr[152] = -1;
            iArr[153] = -1;
            iArr[154] = -1;
            iArr[155] = -1;
            iArr[156] = -1;
            iArr[157] = -1;
            iArr[158] = -1;
            iArr[159] = -1;
            iArr[160] = -1;
            iArr[161] = -1;
            iArr[162] = -1;
            iArr[163] = -1;
            iArr[164] = -1;
            iArr[165] = -1;
            iArr[166] = -1;
            iArr[167] = -1;
            iArr[168] = -1;
            iArr[169] = -1;
            iArr[170] = -1;
            iArr[171] = -1;
            iArr[172] = -1;
            iArr[173] = -1;
            iArr[174] = -1;
            iArr[175] = -1;
            iArr[176] = -1;
            iArr[177] = -1;
            iArr[178] = -1;
            iArr[179] = -1;
            iArr[180] = -1;
            iArr[181] = -1;
            iArr[182] = -1;
            iArr[183] = -1;
            iArr[184] = -1;
            iArr[185] = -1;
            iArr[186] = -1;
            iArr[187] = -1;
            iArr[188] = -1;
            iArr[189] = -1;
            iArr[190] = -1;
            iArr[191] = -1;
            iArr[192] = -1;
            iArr[193] = -1;
            iArr[194] = -1;
            iArr[195] = -1;
            iArr[196] = -1;
            iArr[197] = -1;
            iArr[198] = -1;
            iArr[199] = -1;
            iArr[200] = -1;
            iArr[201] = -1;
            iArr[202] = -1;
            iArr[203] = -1;
            iArr[204] = -1;
            iArr[205] = -1;
            iArr[206] = -1;
            iArr[207] = -1;
            iArr[208] = -1;
            iArr[209] = -1;
            iArr[210] = -1;
            iArr[211] = -1;
            iArr[212] = -1;
            iArr[213] = -1;
            iArr[214] = -1;
            iArr[215] = -1;
            iArr[216] = -1;
            iArr[217] = -1;
            iArr[218] = -1;
            iArr[219] = -1;
            iArr[220] = -1;
            iArr[221] = -1;
            iArr[222] = -1;
            iArr[223] = -1;
            iArr[224] = -1;
            iArr[225] = -1;
            iArr[226] = -1;
            iArr[227] = -1;
            iArr[228] = -1;
            iArr[229] = -1;
            iArr[230] = -1;
            iArr[231] = -1;
            iArr[232] = -1;
            iArr[233] = -1;
            iArr[234] = -1;
            iArr[235] = -1;
            iArr[236] = -1;
            iArr[237] = -1;
            iArr[238] = -1;
            iArr[239] = -1;
            iArr[240] = -1;
            iArr[241] = -1;
            iArr[242] = -1;
            iArr[243] = -1;
            iArr[244] = -1;
            iArr[245] = -1;
            iArr[246] = -1;
            iArr[247] = -1;
            iArr[248] = -1;
            iArr[249] = -1;
            iArr[250] = -1;
            iArr[251] = -1;
            iArr[252] = -1;
            iArr[253] = -1;
            iArr[254] = -1;
            iArr[255] = -1;
            DECODE = iArr;
            iArr = new int[256];
            iArr[0] = -1;
            iArr[1] = -1;
            iArr[2] = -1;
            iArr[3] = -1;
            iArr[4] = -1;
            iArr[5] = -1;
            iArr[6] = -1;
            iArr[7] = -1;
            iArr[8] = -1;
            iArr[9] = -1;
            iArr[10] = -1;
            iArr[11] = -1;
            iArr[12] = -1;
            iArr[13] = -1;
            iArr[14] = -1;
            iArr[15] = -1;
            iArr[16] = -1;
            iArr[17] = -1;
            iArr[18] = -1;
            iArr[19] = -1;
            iArr[20] = -1;
            iArr[21] = -1;
            iArr[22] = -1;
            iArr[23] = -1;
            iArr[24] = -1;
            iArr[25] = -1;
            iArr[26] = -1;
            iArr[27] = -1;
            iArr[28] = -1;
            iArr[29] = -1;
            iArr[30] = -1;
            iArr[31] = -1;
            iArr[32] = -1;
            iArr[33] = -1;
            iArr[34] = -1;
            iArr[35] = -1;
            iArr[36] = -1;
            iArr[37] = -1;
            iArr[38] = -1;
            iArr[39] = -1;
            iArr[40] = -1;
            iArr[41] = -1;
            iArr[42] = -1;
            iArr[43] = -1;
            iArr[44] = -1;
            iArr[45] = 62;
            iArr[46] = -1;
            iArr[47] = -1;
            iArr[48] = 52;
            iArr[49] = 53;
            iArr[50] = 54;
            iArr[51] = 55;
            iArr[52] = 56;
            iArr[53] = 57;
            iArr[54] = 58;
            iArr[55] = 59;
            iArr[56] = 60;
            iArr[57] = 61;
            iArr[58] = -1;
            iArr[59] = -1;
            iArr[60] = -1;
            iArr[61] = -2;
            iArr[62] = -1;
            iArr[63] = -1;
            iArr[64] = -1;
            iArr[66] = 1;
            iArr[67] = 2;
            iArr[68] = 3;
            iArr[69] = 4;
            iArr[70] = 5;
            iArr[71] = 6;
            iArr[72] = 7;
            iArr[73] = 8;
            iArr[74] = 9;
            iArr[75] = 10;
            iArr[76] = 11;
            iArr[77] = 12;
            iArr[78] = 13;
            iArr[79] = 14;
            iArr[80] = 15;
            iArr[81] = 16;
            iArr[82] = 17;
            iArr[83] = 18;
            iArr[84] = 19;
            iArr[85] = 20;
            iArr[86] = 21;
            iArr[87] = 22;
            iArr[88] = 23;
            iArr[89] = 24;
            iArr[90] = 25;
            iArr[91] = -1;
            iArr[92] = -1;
            iArr[93] = -1;
            iArr[94] = -1;
            iArr[95] = 63;
            iArr[96] = -1;
            iArr[97] = 26;
            iArr[98] = 27;
            iArr[99] = 28;
            iArr[100] = 29;
            iArr[101] = 30;
            iArr[102] = 31;
            iArr[103] = 32;
            iArr[104] = 33;
            iArr[105] = 34;
            iArr[106] = 35;
            iArr[107] = 36;
            iArr[108] = 37;
            iArr[109] = 38;
            iArr[110] = 39;
            iArr[111] = 40;
            iArr[112] = 41;
            iArr[113] = 42;
            iArr[114] = 43;
            iArr[115] = 44;
            iArr[116] = 45;
            iArr[117] = 46;
            iArr[118] = 47;
            iArr[119] = 48;
            iArr[120] = 49;
            iArr[121] = 50;
            iArr[122] = 51;
            iArr[123] = -1;
            iArr[124] = -1;
            iArr[125] = -1;
            iArr[126] = -1;
            iArr[127] = -1;
            iArr[128] = -1;
            iArr[129] = -1;
            iArr[130] = -1;
            iArr[131] = -1;
            iArr[132] = -1;
            iArr[133] = -1;
            iArr[134] = -1;
            iArr[135] = -1;
            iArr[136] = -1;
            iArr[137] = -1;
            iArr[138] = -1;
            iArr[139] = -1;
            iArr[140] = -1;
            iArr[141] = -1;
            iArr[142] = -1;
            iArr[143] = -1;
            iArr[144] = -1;
            iArr[145] = -1;
            iArr[146] = -1;
            iArr[147] = -1;
            iArr[148] = -1;
            iArr[149] = -1;
            iArr[150] = -1;
            iArr[151] = -1;
            iArr[152] = -1;
            iArr[153] = -1;
            iArr[154] = -1;
            iArr[155] = -1;
            iArr[156] = -1;
            iArr[157] = -1;
            iArr[158] = -1;
            iArr[159] = -1;
            iArr[160] = -1;
            iArr[161] = -1;
            iArr[162] = -1;
            iArr[163] = -1;
            iArr[164] = -1;
            iArr[165] = -1;
            iArr[166] = -1;
            iArr[167] = -1;
            iArr[168] = -1;
            iArr[169] = -1;
            iArr[170] = -1;
            iArr[171] = -1;
            iArr[172] = -1;
            iArr[173] = -1;
            iArr[174] = -1;
            iArr[175] = -1;
            iArr[176] = -1;
            iArr[177] = -1;
            iArr[178] = -1;
            iArr[179] = -1;
            iArr[180] = -1;
            iArr[181] = -1;
            iArr[182] = -1;
            iArr[183] = -1;
            iArr[184] = -1;
            iArr[185] = -1;
            iArr[186] = -1;
            iArr[187] = -1;
            iArr[188] = -1;
            iArr[189] = -1;
            iArr[190] = -1;
            iArr[191] = -1;
            iArr[192] = -1;
            iArr[193] = -1;
            iArr[194] = -1;
            iArr[195] = -1;
            iArr[196] = -1;
            iArr[197] = -1;
            iArr[198] = -1;
            iArr[199] = -1;
            iArr[200] = -1;
            iArr[201] = -1;
            iArr[202] = -1;
            iArr[203] = -1;
            iArr[204] = -1;
            iArr[205] = -1;
            iArr[206] = -1;
            iArr[207] = -1;
            iArr[208] = -1;
            iArr[209] = -1;
            iArr[210] = -1;
            iArr[211] = -1;
            iArr[212] = -1;
            iArr[213] = -1;
            iArr[214] = -1;
            iArr[215] = -1;
            iArr[216] = -1;
            iArr[217] = -1;
            iArr[218] = -1;
            iArr[219] = -1;
            iArr[220] = -1;
            iArr[221] = -1;
            iArr[222] = -1;
            iArr[223] = -1;
            iArr[224] = -1;
            iArr[225] = -1;
            iArr[226] = -1;
            iArr[227] = -1;
            iArr[228] = -1;
            iArr[229] = -1;
            iArr[230] = -1;
            iArr[231] = -1;
            iArr[232] = -1;
            iArr[233] = -1;
            iArr[234] = -1;
            iArr[235] = -1;
            iArr[236] = -1;
            iArr[237] = -1;
            iArr[238] = -1;
            iArr[239] = -1;
            iArr[240] = -1;
            iArr[241] = -1;
            iArr[242] = -1;
            iArr[243] = -1;
            iArr[244] = -1;
            iArr[245] = -1;
            iArr[246] = -1;
            iArr[247] = -1;
            iArr[248] = -1;
            iArr[249] = -1;
            iArr[250] = -1;
            iArr[251] = -1;
            iArr[252] = -1;
            iArr[253] = -1;
            iArr[254] = -1;
            iArr[255] = -1;
            DECODE_WEBSAFE = iArr;
        }

        public Decoder(int flags, byte[] output) {
            this.output = output;
            this.alphabet = (flags & 8) == 0 ? DECODE : DECODE_WEBSAFE;
            this.state = 0;
            this.value = 0;
        }

        public int maxOutputSize(int len) {
            return (len * 3) / 4 + 10;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean process(byte[] r12_input, int r13_offset, int r14_len, boolean r15_finish) {
            throw new UnsupportedOperationException("Method not decompiled: com.mopub.mobileads.util.Base64.Decoder.process(byte[], int, int, boolean):boolean");
            /*
            r11 = this;
            r9 = r11.state;
            r10 = 6;
            if (r9 != r10) goto L_0x0007;
        L_0x0005:
            r9 = 0;
        L_0x0006:
            return r9;
        L_0x0007:
            r5 = r13;
            r14 = r14 + r13;
            r7 = r11.state;
            r8 = r11.value;
            r2 = 0;
            r4 = r11.output;
            r0 = r11.alphabet;
        L_0x0012:
            if (r5 < r14) goto L_0x001f;
        L_0x0014:
            r3 = r2;
        L_0x0015:
            if (r15 != 0) goto L_0x0110;
        L_0x0017:
            r11.state = r7;
            r11.value = r8;
            r11.op = r3;
            r9 = 1;
            goto L_0x0006;
        L_0x001f:
            if (r7 != 0) goto L_0x0069;
        L_0x0021:
            r9 = r5 + 4;
            if (r9 > r14) goto L_0x004f;
        L_0x0025:
            r9 = r12[r5];
            r9 = r9 & 255;
            r9 = r0[r9];
            r9 = r9 << 18;
            r10 = r5 + 1;
            r10 = r12[r10];
            r10 = r10 & 255;
            r10 = r0[r10];
            r10 = r10 << 12;
            r9 = r9 | r10;
            r10 = r5 + 2;
            r10 = r12[r10];
            r10 = r10 & 255;
            r10 = r0[r10];
            r10 = r10 << 6;
            r9 = r9 | r10;
            r10 = r5 + 3;
            r10 = r12[r10];
            r10 = r10 & 255;
            r10 = r0[r10];
            r8 = r9 | r10;
            if (r8 >= 0) goto L_0x0053;
        L_0x004f:
            if (r5 < r14) goto L_0x0069;
        L_0x0051:
            r3 = r2;
            goto L_0x0015;
        L_0x0053:
            r9 = r2 + 2;
            r10 = (byte) r8;
            r4[r9] = r10;
            r9 = r2 + 1;
            r10 = r8 >> 8;
            r10 = (byte) r10;
            r4[r9] = r10;
            r9 = r8 >> 16;
            r9 = (byte) r9;
            r4[r2] = r9;
            r2 = r2 + 3;
            r5 = r5 + 4;
            goto L_0x0021;
        L_0x0069:
            r6 = r5 + 1;
            r9 = r12[r5];
            r9 = r9 & 255;
            r1 = r0[r9];
            switch(r7) {
                case 0: goto L_0x0076;
                case 1: goto L_0x0085;
                case 2: goto L_0x0098;
                case 3: goto L_0x00bb;
                case 4: goto L_0x00f6;
                case 5: goto L_0x0107;
                default: goto L_0x0074;
            };
        L_0x0074:
            r5 = r6;
            goto L_0x0012;
        L_0x0076:
            if (r1 < 0) goto L_0x007d;
        L_0x0078:
            r8 = r1;
            r7 = r7 + 1;
            r5 = r6;
            goto L_0x0012;
        L_0x007d:
            r9 = -1;
            if (r1 == r9) goto L_0x0074;
        L_0x0080:
            r9 = 6;
            r11.state = r9;
            r9 = 0;
            goto L_0x0006;
        L_0x0085:
            if (r1 < 0) goto L_0x008f;
        L_0x0087:
            r9 = r8 << 6;
            r8 = r9 | r1;
            r7 = r7 + 1;
            r5 = r6;
            goto L_0x0012;
        L_0x008f:
            r9 = -1;
            if (r1 == r9) goto L_0x0074;
        L_0x0092:
            r9 = 6;
            r11.state = r9;
            r9 = 0;
            goto L_0x0006;
        L_0x0098:
            if (r1 < 0) goto L_0x00a3;
        L_0x009a:
            r9 = r8 << 6;
            r8 = r9 | r1;
            r7 = r7 + 1;
            r5 = r6;
            goto L_0x0012;
        L_0x00a3:
            r9 = -2;
            if (r1 != r9) goto L_0x00b2;
        L_0x00a6:
            r3 = r2 + 1;
            r9 = r8 >> 4;
            r9 = (byte) r9;
            r4[r2] = r9;
            r7 = 4;
            r2 = r3;
            r5 = r6;
            goto L_0x0012;
        L_0x00b2:
            r9 = -1;
            if (r1 == r9) goto L_0x0074;
        L_0x00b5:
            r9 = 6;
            r11.state = r9;
            r9 = 0;
            goto L_0x0006;
        L_0x00bb:
            if (r1 < 0) goto L_0x00d8;
        L_0x00bd:
            r9 = r8 << 6;
            r8 = r9 | r1;
            r9 = r2 + 2;
            r10 = (byte) r8;
            r4[r9] = r10;
            r9 = r2 + 1;
            r10 = r8 >> 8;
            r10 = (byte) r10;
            r4[r9] = r10;
            r9 = r8 >> 16;
            r9 = (byte) r9;
            r4[r2] = r9;
            r2 = r2 + 3;
            r7 = 0;
            r5 = r6;
            goto L_0x0012;
        L_0x00d8:
            r9 = -2;
            if (r1 != r9) goto L_0x00ed;
        L_0x00db:
            r9 = r2 + 1;
            r10 = r8 >> 2;
            r10 = (byte) r10;
            r4[r9] = r10;
            r9 = r8 >> 10;
            r9 = (byte) r9;
            r4[r2] = r9;
            r2 = r2 + 2;
            r7 = 5;
            r5 = r6;
            goto L_0x0012;
        L_0x00ed:
            r9 = -1;
            if (r1 == r9) goto L_0x0074;
        L_0x00f0:
            r9 = 6;
            r11.state = r9;
            r9 = 0;
            goto L_0x0006;
        L_0x00f6:
            r9 = -2;
            if (r1 != r9) goto L_0x00fe;
        L_0x00f9:
            r7 = r7 + 1;
            r5 = r6;
            goto L_0x0012;
        L_0x00fe:
            r9 = -1;
            if (r1 == r9) goto L_0x0074;
        L_0x0101:
            r9 = 6;
            r11.state = r9;
            r9 = 0;
            goto L_0x0006;
        L_0x0107:
            r9 = -1;
            if (r1 == r9) goto L_0x0074;
        L_0x010a:
            r9 = 6;
            r11.state = r9;
            r9 = 0;
            goto L_0x0006;
        L_0x0110:
            switch(r7) {
                case 0: goto L_0x011b;
                case 1: goto L_0x011d;
                case 2: goto L_0x0123;
                case 3: goto L_0x012b;
                case 4: goto L_0x013b;
                default: goto L_0x0113;
            };
        L_0x0113:
            r2 = r3;
        L_0x0114:
            r11.state = r7;
            r11.op = r2;
            r9 = 1;
            goto L_0x0006;
        L_0x011b:
            r2 = r3;
            goto L_0x0114;
        L_0x011d:
            r9 = 6;
            r11.state = r9;
            r9 = 0;
            goto L_0x0006;
        L_0x0123:
            r2 = r3 + 1;
            r9 = r8 >> 4;
            r9 = (byte) r9;
            r4[r3] = r9;
            goto L_0x0114;
        L_0x012b:
            r2 = r3 + 1;
            r9 = r8 >> 10;
            r9 = (byte) r9;
            r4[r3] = r9;
            r3 = r2 + 1;
            r9 = r8 >> 2;
            r9 = (byte) r9;
            r4[r2] = r9;
            r2 = r3;
            goto L_0x0114;
        L_0x013b:
            r9 = 6;
            r11.state = r9;
            r9 = 0;
            goto L_0x0006;
            */
        }
    }

    static class Encoder extends Coder {
        static final /* synthetic */ boolean $assertionsDisabled;
        private static final byte[] ENCODE;
        private static final byte[] ENCODE_WEBSAFE;
        public static final int LINE_GROUPS = 19;
        private final byte[] alphabet;
        private int count;
        public final boolean do_cr;
        public final boolean do_newline;
        public final boolean do_padding;
        private final byte[] tail;
        int tailLen;

        static {
            $assertionsDisabled = !Base64.class.desiredAssertionStatus() ? true : $assertionsDisabled;
            ENCODE = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, (byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 43, (byte) 47};
            ENCODE_WEBSAFE = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, (byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 45, (byte) 95};
        }

        public Encoder(int flags, byte[] output) {
            boolean z;
            boolean z2 = true;
            this.output = output;
            this.do_padding = (flags & 1) == 0;
            if ((flags & 2) == 0) {
                z = true;
            } else {
                z = false;
            }
            this.do_newline = z;
            if ((flags & 4) == 0) {
                z2 = false;
            }
            this.do_cr = z2;
            this.alphabet = (flags & 8) == 0 ? ENCODE : ENCODE_WEBSAFE;
            this.tail = new byte[2];
            this.tailLen = 0;
            this.count = this.do_newline ? LINE_GROUPS : -1;
        }

        public int maxOutputSize(int len) {
            return (len * 8) / 5 + 10;
        }

        public boolean process(byte[] r15_input, int r16_offset, int r17_len, boolean r18_finish) {
            throw new UnsupportedOperationException("Method not decompiled: com.mopub.mobileads.util.Base64.Encoder.process(byte[], int, int, boolean):boolean");
            /* JADX: method processing error */
/*
            Error: jadx.core.utils.exceptions.JadxOverflowException: Regions stack size limit reached
	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:42)
	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:62)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:29)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:16)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
	at jadx.core.ProcessClass.process(ProcessClass.java:22)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:209)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:133)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1145)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615)
	at java.lang.Thread.run(Thread.java:745)
*/
            /*
            r14 = this;
            r1 = r14.alphabet;
            r5 = r14.output;
            r3 = 0;
            r2 = r14.count;
            r6 = r16;
            r17 = r17 + r16;
            r10 = -1;
            r11 = r14.tailLen;
            switch(r11) {
                case 0: goto L_0x0011;
                case 1: goto L_0x00bf;
                case 2: goto L_0x00e4;
                default: goto L_0x0011;
            };
        L_0x0011:
            r11 = -1;
            if (r10 == r11) goto L_0x024b;
        L_0x0014:
            r4 = r3 + 1;
            r11 = r10 >> 18;
            r11 = r11 & 63;
            r11 = r1[r11];
            r5[r3] = r11;
            r3 = r4 + 1;
            r11 = r10 >> 12;
            r11 = r11 & 63;
            r11 = r1[r11];
            r5[r4] = r11;
            r4 = r3 + 1;
            r11 = r10 >> 6;
            r11 = r11 & 63;
            r11 = r1[r11];
            r5[r3] = r11;
            r3 = r4 + 1;
            r11 = r10 & 63;
            r11 = r1[r11];
            r5[r4] = r11;
            r2 = r2 + -1;
            if (r2 != 0) goto L_0x024b;
        L_0x003e:
            r11 = r14.do_cr;
            if (r11 == 0) goto L_0x0049;
        L_0x0042:
            r4 = r3 + 1;
            r11 = 13;
            r5[r3] = r11;
            r3 = r4;
        L_0x0049:
            r4 = r3 + 1;
            r11 = 10;
            r5[r3] = r11;
            r2 = 19;
            r7 = r6;
        L_0x0052:
            r11 = r7 + 3;
            r0 = r17;
            if (r11 <= r0) goto L_0x010b;
        L_0x0058:
            if (r18 == 0) goto L_0x0210;
        L_0x005a:
            r11 = r14.tailLen;
            r11 = r7 - r11;
            r12 = r17 + -1;
            if (r11 != r12) goto L_0x016a;
        L_0x0062:
            r8 = 0;
            r11 = r14.tailLen;
            if (r11 <= 0) goto L_0x0164;
        L_0x0067:
            r11 = r14.tail;
            r9 = r8 + 1;
            r11 = r11[r8];
            r8 = r9;
            r6 = r7;
        L_0x006f:
            r11 = r11 & 255;
            r10 = r11 << 4;
            r11 = r14.tailLen;
            r11 = r11 - r8;
            r14.tailLen = r11;
            r3 = r4 + 1;
            r11 = r10 >> 6;
            r11 = r11 & 63;
            r11 = r1[r11];
            r5[r4] = r11;
            r4 = r3 + 1;
            r11 = r10 & 63;
            r11 = r1[r11];
            r5[r3] = r11;
            r11 = r14.do_padding;
            if (r11 == 0) goto L_0x009a;
        L_0x008e:
            r3 = r4 + 1;
            r11 = 61;
            r5[r4] = r11;
            r4 = r3 + 1;
            r11 = 61;
            r5[r3] = r11;
        L_0x009a:
            r3 = r4;
            r11 = r14.do_newline;
            if (r11 == 0) goto L_0x00b1;
        L_0x009f:
            r11 = r14.do_cr;
            if (r11 == 0) goto L_0x00aa;
        L_0x00a3:
            r4 = r3 + 1;
            r11 = 13;
            r5[r3] = r11;
            r3 = r4;
        L_0x00aa:
            r4 = r3 + 1;
            r11 = 10;
            r5[r3] = r11;
            r3 = r4;
        L_0x00b1:
            r11 = $assertionsDisabled;
            if (r11 != 0) goto L_0x0202;
        L_0x00b5:
            r11 = r14.tailLen;
            if (r11 == 0) goto L_0x0202;
        L_0x00b9:
            r11 = new java.lang.AssertionError;
            r11.<init>();
            throw r11;
        L_0x00bf:
            r11 = r6 + 2;
            r0 = r17;
            if (r11 > r0) goto L_0x0011;
        L_0x00c5:
            r11 = r14.tail;
            r12 = 0;
            r11 = r11[r12];
            r11 = r11 & 255;
            r11 = r11 << 16;
            r7 = r6 + 1;
            r12 = r15[r6];
            r12 = r12 & 255;
            r12 = r12 << 8;
            r11 = r11 | r12;
            r6 = r7 + 1;
            r12 = r15[r7];
            r12 = r12 & 255;
            r10 = r11 | r12;
            r11 = 0;
            r14.tailLen = r11;
            goto L_0x0011;
        L_0x00e4:
            r11 = r6 + 1;
            r0 = r17;
            if (r11 > r0) goto L_0x0011;
        L_0x00ea:
            r11 = r14.tail;
            r12 = 0;
            r11 = r11[r12];
            r11 = r11 & 255;
            r11 = r11 << 16;
            r12 = r14.tail;
            r13 = 1;
            r12 = r12[r13];
            r12 = r12 & 255;
            r12 = r12 << 8;
            r11 = r11 | r12;
            r7 = r6 + 1;
            r12 = r15[r6];
            r12 = r12 & 255;
            r10 = r11 | r12;
            r11 = 0;
            r14.tailLen = r11;
            r6 = r7;
            goto L_0x0011;
        L_0x010b:
            r11 = r15[r7];
            r11 = r11 & 255;
            r11 = r11 << 16;
            r12 = r7 + 1;
            r12 = r15[r12];
            r12 = r12 & 255;
            r12 = r12 << 8;
            r11 = r11 | r12;
            r12 = r7 + 2;
            r12 = r15[r12];
            r12 = r12 & 255;
            r10 = r11 | r12;
            r11 = r10 >> 18;
            r11 = r11 & 63;
            r11 = r1[r11];
            r5[r4] = r11;
            r11 = r4 + 1;
            r12 = r10 >> 12;
            r12 = r12 & 63;
            r12 = r1[r12];
            r5[r11] = r12;
            r11 = r4 + 2;
            r12 = r10 >> 6;
            r12 = r12 & 63;
            r12 = r1[r12];
            r5[r11] = r12;
            r11 = r4 + 3;
            r12 = r10 & 63;
            r12 = r1[r12];
            r5[r11] = r12;
            r6 = r7 + 3;
            r3 = r4 + 4;
            r2 = r2 + -1;
            if (r2 != 0) goto L_0x024b;
        L_0x014e:
            r11 = r14.do_cr;
            if (r11 == 0) goto L_0x0159;
        L_0x0152:
            r4 = r3 + 1;
            r11 = 13;
            r5[r3] = r11;
            r3 = r4;
        L_0x0159:
            r4 = r3 + 1;
            r11 = 10;
            r5[r3] = r11;
            r2 = 19;
            r7 = r6;
            goto L_0x0052;
        L_0x0164:
            r6 = r7 + 1;
            r11 = r15[r7];
            goto L_0x006f;
        L_0x016a:
            r11 = r14.tailLen;
            r11 = r7 - r11;
            r12 = r17 + -2;
            if (r11 != r12) goto L_0x01e4;
        L_0x0172:
            r8 = 0;
            r11 = r14.tailLen;
            r12 = 1;
            if (r11 <= r12) goto L_0x01d9;
        L_0x0178:
            r11 = r14.tail;
            r9 = r8 + 1;
            r11 = r11[r8];
            r8 = r9;
            r6 = r7;
        L_0x0180:
            r11 = r11 & 255;
            r12 = r11 << 10;
            r11 = r14.tailLen;
            if (r11 <= 0) goto L_0x01de;
        L_0x0188:
            r11 = r14.tail;
            r9 = r8 + 1;
            r11 = r11[r8];
            r8 = r9;
        L_0x018f:
            r11 = r11 & 255;
            r11 = r11 << 2;
            r10 = r12 | r11;
            r11 = r14.tailLen;
            r11 = r11 - r8;
            r14.tailLen = r11;
            r3 = r4 + 1;
            r11 = r10 >> 12;
            r11 = r11 & 63;
            r11 = r1[r11];
            r5[r4] = r11;
            r4 = r3 + 1;
            r11 = r10 >> 6;
            r11 = r11 & 63;
            r11 = r1[r11];
            r5[r3] = r11;
            r3 = r4 + 1;
            r11 = r10 & 63;
            r11 = r1[r11];
            r5[r4] = r11;
            r11 = r14.do_padding;
            if (r11 == 0) goto L_0x01c1;
        L_0x01ba:
            r4 = r3 + 1;
            r11 = 61;
            r5[r3] = r11;
            r3 = r4;
        L_0x01c1:
            r11 = r14.do_newline;
            if (r11 == 0) goto L_0x00b1;
        L_0x01c5:
            r11 = r14.do_cr;
            if (r11 == 0) goto L_0x01d0;
        L_0x01c9:
            r4 = r3 + 1;
            r11 = 13;
            r5[r3] = r11;
            r3 = r4;
        L_0x01d0:
            r4 = r3 + 1;
            r11 = 10;
            r5[r3] = r11;
            r3 = r4;
            goto L_0x00b1;
        L_0x01d9:
            r6 = r7 + 1;
            r11 = r15[r7];
            goto L_0x0180;
        L_0x01de:
            r7 = r6 + 1;
            r11 = r15[r6];
            r6 = r7;
            goto L_0x018f;
        L_0x01e4:
            r11 = r14.do_newline;
            if (r11 == 0) goto L_0x01fe;
        L_0x01e8:
            if (r4 <= 0) goto L_0x01fe;
        L_0x01ea:
            r11 = 19;
            if (r2 == r11) goto L_0x01fe;
        L_0x01ee:
            r11 = r14.do_cr;
            if (r11 == 0) goto L_0x0249;
        L_0x01f2:
            r3 = r4 + 1;
            r11 = 13;
            r5[r4] = r11;
        L_0x01f8:
            r4 = r3 + 1;
            r11 = 10;
            r5[r3] = r11;
        L_0x01fe:
            r6 = r7;
            r3 = r4;
            goto L_0x00b1;
        L_0x0202:
            r11 = $assertionsDisabled;
            if (r11 != 0) goto L_0x0222;
        L_0x0206:
            r0 = r17;
            if (r6 == r0) goto L_0x0222;
        L_0x020a:
            r11 = new java.lang.AssertionError;
            r11.<init>();
            throw r11;
        L_0x0210:
            r11 = r17 + -1;
            if (r7 != r11) goto L_0x0228;
        L_0x0214:
            r11 = r14.tail;
            r12 = r14.tailLen;
            r13 = r12 + 1;
            r14.tailLen = r13;
            r13 = r15[r7];
            r11[r12] = r13;
            r6 = r7;
            r3 = r4;
        L_0x0222:
            r14.op = r3;
            r14.count = r2;
            r11 = 1;
            return r11;
        L_0x0228:
            r11 = r17 + -2;
            if (r7 != r11) goto L_0x0246;
        L_0x022c:
            r11 = r14.tail;
            r12 = r14.tailLen;
            r13 = r12 + 1;
            r14.tailLen = r13;
            r13 = r15[r7];
            r11[r12] = r13;
            r11 = r14.tail;
            r12 = r14.tailLen;
            r13 = r12 + 1;
            r14.tailLen = r13;
            r13 = r7 + 1;
            r13 = r15[r13];
            r11[r12] = r13;
        L_0x0246:
            r6 = r7;
            r3 = r4;
            goto L_0x0222;
        L_0x0249:
            r3 = r4;
            goto L_0x01f8;
        L_0x024b:
            r7 = r6;
            r4 = r3;
            goto L_0x0052;
            */
        }
    }

    static {
        $assertionsDisabled = !Base64.class.desiredAssertionStatus() ? true : $assertionsDisabled;
    }

    private Base64() {
    }

    public static byte[] decode(String str, int flags) {
        return decode(str.getBytes(), flags);
    }

    public static byte[] decode(byte[] input, int flags) {
        return decode(input, DEFAULT, input.length, flags);
    }

    public static byte[] decode(byte[] input, int offset, int len, int flags) {
        Decoder decoder = new Decoder(flags, new byte[((len * 3) / 4)]);
        if (!decoder.process(input, offset, len, true)) {
            throw new IllegalArgumentException("bad base-64");
        } else if (decoder.op == decoder.output.length) {
            return decoder.output;
        } else {
            byte[] temp = new byte[decoder.op];
            System.arraycopy(decoder.output, DEFAULT, temp, DEFAULT, decoder.op);
            return temp;
        }
    }

    public static byte[] encode(byte[] input, int flags) {
        return encode(input, DEFAULT, input.length, flags);
    }

    public static byte[] encode(byte[] input, int offset, int len, int flags) {
        Encoder encoder = new Encoder(flags, null);
        int output_len = (len / 3) * 4;
        if (!encoder.do_padding) {
            switch (len % 3) {
                case DEFAULT:
                    break;
                case NO_PADDING:
                    output_len += 2;
                    break;
                case NO_WRAP:
                    output_len += 3;
                    break;
                default:
                    break;
            }
        } else if (len % 3 > 0) {
            output_len += 4;
        }
        if (encoder.do_newline && len > 0) {
            output_len += (encoder.do_cr ? NO_WRAP : 1) * (((len - 1) / 57) + 1);
        }
        encoder.output = new byte[output_len];
        encoder.process(input, offset, len, true);
        if ($assertionsDisabled || encoder.op == output_len) {
            return encoder.output;
        }
        throw new AssertionError();
    }

    public static String encodeToString(byte[] input, int flags) {
        try {
            return new String(encode(input, flags), "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    public static String encodeToString(byte[] input, int offset, int len, int flags) {
        try {
            return new String(encode(input, offset, len, flags), "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }
}