package com.google.android.gms.internal;

import java.io.IOException;

class q implements o {
    private ko kk;
    private byte[] kl;
    private final int km;

    public q(int i) {
        this.km = i;
        reset();
    }

    public void b(int i, long j) throws IOException {
        this.kk.b(i, j);
    }

    public void b(int i, String str) throws IOException {
        this.kk.b(i, str);
    }

    public void reset() {
        this.kl = new byte[this.km];
        this.kk = ko.o(this.kl);
    }

    public byte[] z() throws IOException {
        int mv = this.kk.mv();
        if (mv < 0) {
            throw new IOException();
        } else if (mv == 0) {
            return this.kl;
        } else {
            Object obj = new Object[(this.kl.length - mv)];
            System.arraycopy(this.kl, 0, obj, 0, obj.length);
            return obj;
        }
    }
}