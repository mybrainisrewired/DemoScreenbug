package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.a;
import com.google.android.gms.internal.d;
import java.util.Map;

class al extends bx {
    private static final String ID;

    static {
        ID = a.ao.toString();
    }

    public al() {
        super(ID);
    }

    protected boolean a(dg dgVar, dg dgVar2, Map<String, d.a> map) {
        return dgVar.a(dgVar2) >= 0;
    }
}