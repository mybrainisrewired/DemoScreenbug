package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.d.a;
import java.util.Map;

abstract class df extends aj {
    public df(String str, String... strArr) {
        super(str, strArr);
    }

    public boolean jX() {
        return false;
    }

    public a x(Map<String, a> map) {
        z(map);
        return dh.lT();
    }

    public abstract void z(Map<String, a> map);
}