package com.google.android.gms.internal;

import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public final class ko {
    private final int adT;
    private final byte[] buffer;
    private int position;

    public static class a extends IOException {
        a(int i, int i2) {
            super("CodedOutputStream was writing to a flat byte array and ran out of space (pos " + i + " limit " + i2 + ").");
        }
    }

    private ko(byte[] bArr, int i, int i2) {
        this.buffer = bArr;
        this.position = i;
        this.adT = i + i2;
    }

    public static int A(long j) {
        return D(j);
    }

    public static int B(long j) {
        return D(E(j));
    }

    public static int D(long j) {
        return (-128 & j) == 0 ? 1 : (-16384 & j) == 0 ? MMAdView.TRANSITION_UP : (-2097152 & j) == 0 ? MMAdView.TRANSITION_DOWN : (-268435456 & j) == 0 ? MMAdView.TRANSITION_RANDOM : (-34359738368L & j) == 0 ? ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES : (-4398046511104L & j) == 0 ? ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES : (-562949953421312L & j) == 0 ? ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES : (-72057594037927936L & j) == 0 ? ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES : (Long.MIN_VALUE & j) == 0 ? ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES : ApiEventType.API_MRAID_USE_CUSTOM_CLOSE;
    }

    public static int E(boolean z) {
        return 1;
    }

    public static long E(long j) {
        return (j << 1) ^ (j >> 63);
    }

    public static int b(int i, kt ktVar) {
        return cZ(i) + c(ktVar);
    }

    public static int b(int i, boolean z) {
        return cZ(i) + E(z);
    }

    public static ko b(byte[] bArr, int i, int i2) {
        return new ko(bArr, i, i2);
    }

    public static int c(int i, float f) {
        return cZ(i) + e(f);
    }

    public static int c(kt ktVar) {
        int c = ktVar.c();
        return c + db(c);
    }

    public static int cX(int i) {
        return i >= 0 ? db(i) : ApiEventType.API_MRAID_USE_CUSTOM_CLOSE;
    }

    public static int cZ(int i) {
        return db(kw.l(i, 0));
    }

    public static int cf(String str) {
        try {
            byte[] bytes = str.getBytes("UTF-8");
            return bytes.length + db(bytes.length);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 not supported.");
        }
    }

    public static int d(int i, long j) {
        return cZ(i) + A(j);
    }

    public static int db(int i) {
        return (i & -128) == 0 ? 1 : (i & -16384) == 0 ? MMAdView.TRANSITION_UP : (-2097152 & i) == 0 ? MMAdView.TRANSITION_DOWN : (-268435456 & i) == 0 ? MMAdView.TRANSITION_RANDOM : ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES;
    }

    public static int e(float f) {
        return MMAdView.TRANSITION_RANDOM;
    }

    public static int e(int i, long j) {
        return cZ(i) + B(j);
    }

    public static int g(int i, String str) {
        return cZ(i) + cf(str);
    }

    public static int j(int i, int i2) {
        return cZ(i) + cX(i2);
    }

    public static ko o(byte[] bArr) {
        return b(bArr, 0, bArr.length);
    }

    public void C(long j) throws IOException {
        while ((-128 & j) != 0) {
            cY((((int) j) & 127) | 128);
            j >>>= 7;
        }
        cY((int) j);
    }

    public void D(boolean z) throws IOException {
        cY(z ? 1 : 0);
    }

    public void a(int i, kt ktVar) throws IOException {
        k(i, MMAdView.TRANSITION_UP);
        b(ktVar);
    }

    public void a(int i, boolean z) throws IOException {
        k(i, 0);
        D(z);
    }

    public void b(byte b) throws IOException {
        if (this.position == this.adT) {
            throw new a(this.position, this.adT);
        }
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        bArr[i] = b;
    }

    public void b(int i, float f) throws IOException {
        k(i, ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES);
        d(f);
    }

    public void b(int i, long j) throws IOException {
        k(i, 0);
        y(j);
    }

    public void b(int i, String str) throws IOException {
        k(i, MMAdView.TRANSITION_UP);
        ce(str);
    }

    public void b(kt ktVar) throws IOException {
        da(ktVar.mF());
        ktVar.a(this);
    }

    public void c(int i, long j) throws IOException {
        k(i, 0);
        z(j);
    }

    public void c(byte[] bArr, int i, int i2) throws IOException {
        if (this.adT - this.position >= i2) {
            System.arraycopy(bArr, i, this.buffer, this.position, i2);
            this.position += i2;
        } else {
            throw new a(this.position, this.adT);
        }
    }

    public void cW(int i) throws IOException {
        if (i >= 0) {
            da(i);
        } else {
            C((long) i);
        }
    }

    public void cY(int i) throws IOException {
        b((byte) i);
    }

    public void ce(String str) throws IOException {
        byte[] bytes = str.getBytes("UTF-8");
        da(bytes.length);
        p(bytes);
    }

    public void d(float f) throws IOException {
        dc(Float.floatToIntBits(f));
    }

    public void da(int i) throws IOException {
        while ((i & -128) != 0) {
            cY((i & 127) | 128);
            i >>>= 7;
        }
        cY(i);
    }

    public void dc(int i) throws IOException {
        cY(i & 255);
        cY((i >> 8) & 255);
        cY((i >> 16) & 255);
        cY((i >> 24) & 255);
    }

    public void i(int i, int i2) throws IOException {
        k(i, 0);
        cW(i2);
    }

    public void k(int i, int i2) throws IOException {
        da(kw.l(i, i2));
    }

    public int mv() {
        return this.adT - this.position;
    }

    public void mw() {
        if (mv() != 0) {
            throw new IllegalStateException("Did not write as much data as expected.");
        }
    }

    public void p(byte[] bArr) throws IOException {
        c(bArr, 0, bArr.length);
    }

    public void y(long j) throws IOException {
        C(j);
    }

    public void z(long j) throws IOException {
        C(E(j));
    }
}