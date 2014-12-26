package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.d.a;
import java.util.Map;

abstract class bx extends cc {
    public bx(String str) {
        super(str);
    }

    protected boolean a(a aVar, a aVar2, Map map) {
        dg k = dh.k(aVar);
        dg k2 = dh.k(aVar2);
        return (k == dh.lR() || k2 == dh.lR()) ? false : a(k, k2, map);
    }

    protected abstract boolean a(dg dgVar, dg dgVar2, Map<String, a> map);
}