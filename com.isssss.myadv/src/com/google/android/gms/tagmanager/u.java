package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.a;
import com.google.android.gms.internal.b;
import com.google.android.gms.internal.d;
import java.util.Map;

class u extends aj {
    private static final String ID;
    private static final String NAME;
    private static final String XA;
    private final DataLayer WK;

    static {
        ID = a.C.toString();
        NAME = b.dc.toString();
        XA = b.cb.toString();
    }

    public u(DataLayer dataLayer) {
        super(ID, new String[]{NAME});
        this.WK = dataLayer;
    }

    public boolean jX() {
        return false;
    }

    public d.a x(Map<String, d.a> map) {
        Object obj = this.WK.get(dh.j((d.a) map.get(NAME)));
        if (obj != null) {
            return dh.r(obj);
        }
        d.a aVar = (d.a) map.get(XA);
        return aVar != null ? aVar : dh.lT();
    }
}