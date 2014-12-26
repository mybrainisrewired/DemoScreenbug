package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.a;
import com.google.android.gms.internal.b;
import com.google.android.gms.internal.d;
import java.util.Map;

class ce extends aj {
    private static final String ID;
    private static final String YX;
    private static final String YY;

    static {
        ID = a.O.toString();
        YX = b.da.toString();
        YY = b.cZ.toString();
    }

    public ce() {
        super(ID, new String[0]);
    }

    public boolean jX() {
        return false;
    }

    public d.a x(Map<String, d.a> map) {
        double doubleValue;
        double d;
        d.a aVar = (d.a) map.get(YX);
        d.a aVar2 = (d.a) map.get(YY);
        if (!(aVar == null || aVar == dh.lT() || aVar2 == null || aVar2 == dh.lT())) {
            dg k = dh.k(aVar);
            dg k2 = dh.k(aVar2);
            if (!(k == dh.lR() || k2 == dh.lR())) {
                double doubleValue2 = k.doubleValue();
                doubleValue = k2.doubleValue();
                if (doubleValue2 <= doubleValue) {
                    d = doubleValue2;
                    return dh.r(Long.valueOf(Math.round((doubleValue - d) * Math.random() + d)));
                }
            }
        }
        doubleValue = 2.147483647E9d;
        d = 0.0d;
        return dh.r(Long.valueOf(Math.round((doubleValue - d) * Math.random() + d)));
    }
}