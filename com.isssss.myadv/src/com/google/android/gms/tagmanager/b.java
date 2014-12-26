package com.google.android.gms.tagmanager;

import android.content.Context;
import com.google.android.gms.internal.a;
import com.google.android.gms.internal.d;
import java.util.Map;

class b extends aj {
    private static final String ID;
    private final a Wz;

    static {
        ID = a.u.toString();
    }

    public b(Context context) {
        this(a.E(context));
    }

    b(a aVar) {
        super(ID, new String[0]);
        this.Wz = aVar;
    }

    public boolean jX() {
        return false;
    }

    public d.a x(Map<String, d.a> map) {
        String jT = this.Wz.jT();
        return jT == null ? dh.lT() : dh.r(jT);
    }
}