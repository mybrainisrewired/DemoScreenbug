package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.b;
import com.google.android.gms.internal.d.a;
import java.util.Iterator;
import java.util.Map;

abstract class cc extends aj {
    private static final String XQ;
    private static final String YN;

    static {
        XQ = b.bi.toString();
        YN = b.bj.toString();
    }

    public cc(String str) {
        super(str, new String[]{XQ, YN});
    }

    protected abstract boolean a(a aVar, a aVar2, Map<String, a> map);

    public boolean jX() {
        return true;
    }

    public a x(Map<String, a> map) {
        Iterator it = map.values().iterator();
        while (it.hasNext()) {
            if (((a) it.next()) == dh.lT()) {
                return dh.r(Boolean.valueOf(false));
            }
        }
        a aVar = (a) map.get(XQ);
        a aVar2 = (a) map.get(YN);
        boolean a = (aVar == null || aVar2 == null) ? false : a(aVar, aVar2, map);
        return dh.r(Boolean.valueOf(a));
    }
}