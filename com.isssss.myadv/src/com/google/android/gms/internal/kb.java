package com.google.android.gms.internal;

import com.google.android.gms.wearable.a;
import com.google.android.gms.wearable.c;

public class kb implements a {
    private int LF;
    private c adC;

    public kb(a aVar) {
        this.LF = aVar.getType();
        this.adC = (c) aVar.lZ().freeze();
    }

    public /* synthetic */ Object freeze() {
        return me();
    }

    public int getType() {
        return this.LF;
    }

    public boolean isDataValid() {
        return true;
    }

    public c lZ() {
        return this.adC;
    }

    public a me() {
        return this;
    }
}