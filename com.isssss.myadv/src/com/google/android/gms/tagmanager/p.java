package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.a;
import com.google.android.gms.internal.d;
import java.util.Map;

class p extends aj {
    private static final String ID;
    private final String Xl;

    static {
        ID = a.D.toString();
    }

    public p(String str) {
        super(ID, new String[0]);
        this.Xl = str;
    }

    public boolean jX() {
        return true;
    }

    public d.a x(Map<String, d.a> map) {
        return this.Xl == null ? dh.lT() : dh.r(this.Xl);
    }
}