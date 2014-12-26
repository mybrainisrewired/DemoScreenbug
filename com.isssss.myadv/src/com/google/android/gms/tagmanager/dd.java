package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.a;
import com.google.android.gms.internal.d;
import java.util.Map;

class dd extends aj {
    private static final String ID;

    static {
        ID = a.U.toString();
    }

    public dd() {
        super(ID, new String[0]);
    }

    public boolean jX() {
        return false;
    }

    public d.a x(Map<String, d.a> map) {
        return dh.r(Long.valueOf(System.currentTimeMillis()));
    }
}