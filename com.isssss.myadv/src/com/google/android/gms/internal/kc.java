package com.google.android.gms.internal;

import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.common.data.b;
import com.google.android.gms.wearable.a;
import com.google.android.gms.wearable.c;

public final class kc extends b implements a {
    private final int LE;

    public kc(DataHolder dataHolder, int i, int i2) {
        super(dataHolder, i);
        this.LE = i2;
    }

    public /* synthetic */ Object freeze() {
        return me();
    }

    public int getType() {
        return getInteger("event_type");
    }

    public c lZ() {
        return new kg(this.BB, this.BD, this.LE);
    }

    public a me() {
        return new kb(this);
    }
}