package com.google.android.gms.internal;

import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.io.IOException;

public final class kn {
    private int adK;
    private int adL;
    private int adM;
    private int adN;
    private int adO;
    private int adP;
    private int adQ;
    private int adR;
    private int adS;
    private final byte[] buffer;

    private kn(byte[] bArr, int i, int i2) {
        this.adP = Integer.MAX_VALUE;
        this.adR = 64;
        this.adS = 67108864;
        this.buffer = bArr;
        this.adK = i;
        this.adL = i + i2;
        this.adN = i;
    }

    public static kn a(byte[] bArr, int i, int i2) {
        return new kn(bArr, i, i2);
    }

    private void mr() {
        this.adL += this.adM;
        int i = this.adL;
        if (i > this.adP) {
            this.adM = i - this.adP;
            this.adL -= this.adM;
        } else {
            this.adM = 0;
        }
    }

    public static kn n(byte[] bArr) {
        return a(bArr, 0, bArr.length);
    }

    public static long x(long j) {
        return (j >>> 1) ^ (-(1 & j));
    }

    public void a(kt ktVar) throws IOException {
        int mn = mn();
        if (this.adQ >= this.adR) {
            throw ks.mE();
        }
        mn = cR(mn);
        this.adQ++;
        ktVar.b(this);
        cP(0);
        this.adQ--;
        cS(mn);
    }

    public void a(kt ktVar, int i) throws IOException {
        if (this.adQ >= this.adR) {
            throw ks.mE();
        }
        this.adQ++;
        ktVar.b(this);
        cP(kw.l(i, MMAdView.TRANSITION_RANDOM));
        this.adQ--;
    }

    public void cP(int i) throws ks {
        if (this.adO != i) {
            throw ks.mC();
        }
    }

    public boolean cQ(int i) throws IOException {
        switch (kw.de(i)) {
            case MMAdView.TRANSITION_NONE:
                mk();
                return true;
            case MMAdView.TRANSITION_FADE:
                mq();
                return true;
            case MMAdView.TRANSITION_UP:
                cV(mn());
                return true;
            case MMAdView.TRANSITION_DOWN:
                mi();
                cP(kw.l(kw.df(i), MMAdView.TRANSITION_RANDOM));
                return true;
            case MMAdView.TRANSITION_RANDOM:
                return false;
            case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                mp();
                return true;
            default:
                throw ks.mD();
        }
    }

    public int cR(int i) throws ks {
        if (i < 0) {
            throw ks.mz();
        }
        int i2 = this.adN + i;
        int i3 = this.adP;
        if (i2 > i3) {
            throw ks.my();
        }
        this.adP = i2;
        mr();
        return i3;
    }

    public void cS(int i) {
        this.adP = i;
        mr();
    }

    public void cT(int i) {
        if (i > this.adN - this.adK) {
            throw new IllegalArgumentException("Position " + i + " is beyond current " + (this.adN - this.adK));
        } else if (i < 0) {
            throw new IllegalArgumentException("Bad position " + i);
        } else {
            this.adN = this.adK + i;
        }
    }

    public byte[] cU(int i) throws IOException {
        if (i < 0) {
            throw ks.mz();
        } else if (this.adN + i > this.adP) {
            cV(this.adP - this.adN);
            throw ks.my();
        } else if (i <= this.adL - this.adN) {
            Object obj = new Object[i];
            System.arraycopy(this.buffer, this.adN, obj, 0, i);
            this.adN += i;
            return obj;
        } else {
            throw ks.my();
        }
    }

    public void cV(int i) throws IOException {
        if (i < 0) {
            throw ks.mz();
        } else if (this.adN + i > this.adP) {
            cV(this.adP - this.adN);
            throw ks.my();
        } else if (i <= this.adL - this.adN) {
            this.adN += i;
        } else {
            throw ks.my();
        }
    }

    public int getPosition() {
        return this.adN - this.adK;
    }

    public byte[] h(int i, int i2) {
        if (i2 == 0) {
            return kw.aeh;
        }
        Object obj = new Object[i2];
        System.arraycopy(this.buffer, this.adK + i, obj, 0, i2);
        return obj;
    }

    public int mh() throws IOException {
        if (mt()) {
            this.adO = 0;
            return 0;
        } else {
            this.adO = mn();
            if (this.adO != 0) {
                return this.adO;
            }
            throw ks.mB();
        }
    }

    public void mi() throws IOException {
        int mh;
        do {
            mh = mh();
            if (mh == 0) {
                return;
            }
        } while (cQ(mh));
    }

    public long mj() throws IOException {
        return mo();
    }

    public int mk() throws IOException {
        return mn();
    }

    public boolean ml() throws IOException {
        return mn() != 0;
    }

    public long mm() throws IOException {
        return x(mo());
    }

    public int mn() throws IOException {
        byte mu = mu();
        if (mu >= 0) {
            return mu;
        }
        int i = mu & 127;
        byte mu2 = mu();
        if (mu2 >= 0) {
            return i | (mu2 << 7);
        }
        i |= (mu2 & 127) << 7;
        mu2 = mu();
        if (mu2 >= 0) {
            return i | (mu2 << 14);
        }
        i |= (mu2 & 127) << 14;
        mu2 = mu();
        if (mu2 >= 0) {
            return i | (mu2 << 21);
        }
        i |= (mu2 & 127) << 21;
        mu2 = mu();
        i |= mu2 << 28;
        if (mu2 >= 0) {
            return i;
        }
        int i2 = 0;
        while (i2 < 5) {
            if (mu() >= 0) {
                return i;
            }
            i2++;
        }
        throw ks.mA();
    }

    public long mo() throws IOException {
        int i = 0;
        long j = 0;
        while (i < 64) {
            byte mu = mu();
            j |= ((long) (mu & 127)) << i;
            if ((mu & 128) == 0) {
                return j;
            }
            i += 7;
        }
        throw ks.mA();
    }

    public int mp() throws IOException {
        return (((mu() & 255) | ((mu() & 255) << 8)) | ((mu() & 255) << 16)) | ((mu() & 255) << 24);
    }

    public long mq() throws IOException {
        byte mu = mu();
        byte mu2 = mu();
        return ((((((((((long) mu2) & 255) << 8) | (((long) mu) & 255)) | ((((long) mu()) & 255) << 16)) | ((((long) mu()) & 255) << 24)) | ((((long) mu()) & 255) << 32)) | ((((long) mu()) & 255) << 40)) | ((((long) mu()) & 255) << 48)) | ((((long) mu()) & 255) << 56);
    }

    public int ms() {
        if (this.adP == Integer.MAX_VALUE) {
            return -1;
        }
        return this.adP - this.adN;
    }

    public boolean mt() {
        return this.adN == this.adL;
    }

    public byte mu() throws IOException {
        if (this.adN == this.adL) {
            throw ks.my();
        }
        byte[] bArr = this.buffer;
        int i = this.adN;
        this.adN = i + 1;
        return bArr[i];
    }

    public float readFloat() throws IOException {
        return Float.intBitsToFloat(mp());
    }

    public String readString() throws IOException {
        int mn = mn();
        if (mn > this.adL - this.adN || mn <= 0) {
            return new String(cU(mn), "UTF-8");
        }
        String str = new String(this.buffer, this.adN, mn, "UTF-8");
        this.adN = mn + this.adN;
        return str;
    }
}