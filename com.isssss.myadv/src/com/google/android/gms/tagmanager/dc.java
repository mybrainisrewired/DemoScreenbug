package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.d.a;
import java.util.Map;

abstract class dc extends cc {
    public dc(String str) {
        super(str);
    }

    protected boolean a(a aVar, a aVar2, Map map) {
        String j = dh.j(aVar);
        String j2 = dh.j(aVar2);
        return (j == dh.lS() || j2 == dh.lS()) ? false : a(j, j2, map);
    }

    protected abstract boolean a(String str, String str2, Map<String, a> map);
}