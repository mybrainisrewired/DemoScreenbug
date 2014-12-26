package com.google.android.gms.internal;

public class km {
    private final byte[] adH;
    private int adI;
    private int adJ;

    public km(byte[] bArr) {
        this.adH = new byte[256];
        int i = 0;
        while (i < 256) {
            this.adH[i] = (byte) i;
            i++;
        }
        i = 0;
        int i2 = 0;
        while (i2 < 256) {
            i = ((i + this.adH[i2]) + bArr[i2 % bArr.length]) & 255;
            byte b = this.adH[i2];
            this.adH[i2] = this.adH[i];
            this.adH[i] = b;
            i2++;
        }
        this.adI = 0;
        this.adJ = 0;
    }

    public void m(byte[] bArr) {
        int i = this.adI;
        int i2 = this.adJ;
        int i3 = 0;
        while (i3 < bArr.length) {
            i = (i + 1) & 255;
            i2 = (i2 + this.adH[i]) & 255;
            byte b = this.adH[i];
            this.adH[i] = this.adH[i2];
            this.adH[i2] = b;
            bArr[i3] = (byte) (bArr[i3] ^ this.adH[(this.adH[i] + this.adH[i2]) & 255]);
            i3++;
        }
        this.adI = i;
        this.adJ = i2;
    }
}