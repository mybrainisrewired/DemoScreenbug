package com.google.android.gms.tagmanager;

import android.os.Build.VERSION;
import com.google.android.gms.internal.a;
import com.google.android.gms.internal.d;
import java.util.Map;

class cu extends aj {
    private static final String ID;

    static {
        ID = a.S.toString();
    }

    public cu() {
        super(ID, new String[0]);
    }

    public boolean jX() {
        return true;
    }

    public d.a x(Map<String, d.a> map) {
        return dh.r(Integer.valueOf(VERSION.SDK_INT));
    }
}