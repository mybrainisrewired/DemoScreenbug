package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.a;
import com.google.android.gms.internal.d;
import java.util.Map;

class ah extends aj {
    private static final String ID;
    private final cs WL;

    static {
        ID = a.I.toString();
    }

    public ah(cs csVar) {
        super(ID, new String[0]);
        this.WL = csVar;
    }

    public boolean jX() {
        return false;
    }

    public d.a x(Map<String, d.a> map) {
        String lx = this.WL.lx();
        return lx == null ? dh.lT() : dh.r(lx);
    }
}