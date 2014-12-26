package com.google.android.gms.tagmanager;

import android.content.Context;
import com.google.android.gms.internal.a;
import com.google.android.gms.internal.b;
import com.google.android.gms.internal.d;
import java.util.Map;

class ax extends aj {
    private static final String ID;
    private static final String WA;
    private final Context kI;

    static {
        ID = a.ab.toString();
        WA = b.bH.toString();
    }

    public ax(Context context) {
        super(ID, new String[0]);
        this.kI = context;
    }

    public boolean jX() {
        return true;
    }

    public d.a x(Map<String, d.a> map) {
        String d = ay.d(this.kI, ((d.a) map.get(WA)) != null ? dh.j((d.a) map.get(WA)) : null);
        return d != null ? dh.r(d) : dh.lT();
    }
}