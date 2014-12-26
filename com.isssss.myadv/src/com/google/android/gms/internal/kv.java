package com.google.android.gms.internal;

import java.util.Arrays;

public final class kv {
    final byte[] adZ;
    final int tag;

    kv(int i, byte[] bArr) {
        this.tag = i;
        this.adZ = bArr;
    }

    public boolean equals(kv o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof kv)) {
            return false;
        }
        o = o;
        return this.tag == o.tag && Arrays.equals(this.adZ, o.adZ);
    }

    public int hashCode() {
        return (this.tag + 527) * 31 + Arrays.hashCode(this.adZ);
    }
}