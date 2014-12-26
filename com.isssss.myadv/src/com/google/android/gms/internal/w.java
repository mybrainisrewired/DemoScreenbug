package com.google.android.gms.internal;

import java.util.HashMap;
import java.util.Map;

class w implements y {
    private dz kU;

    public w(dz dzVar) {
        this.kU = dzVar;
    }

    public void a(ab abVar, boolean z) {
        Map hashMap = new HashMap();
        hashMap.put("isVisible", z ? "1" : "0");
        this.kU.a("onAdVisibilityChanged", hashMap);
    }
}