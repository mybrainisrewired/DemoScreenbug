package com.google.android.gms.tagmanager;

import android.content.Context;
import com.google.android.gms.internal.a;
import com.google.android.gms.internal.d;
import java.util.Map;

class c extends aj {
    private static final String ID;
    private final a Wz;

    static {
        ID = a.v.toString();
    }

    public c(Context context) {
        this(a.E(context));
    }

    c(a aVar) {
        super(ID, new String[0]);
        this.Wz = aVar;
    }

    public boolean jX() {
        return false;
    }

    public d.a x(Map<String, d.a> map) {
        return dh.r(Boolean.valueOf(!this.Wz.isLimitAdTrackingEnabled()));
    }
}