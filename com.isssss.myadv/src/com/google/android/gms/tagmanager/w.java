package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.a;
import com.google.android.gms.internal.b;
import com.google.android.gms.internal.d;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class w extends df {
    private static final String ID;
    private static final String VALUE;
    private static final String XL;
    private final DataLayer WK;

    static {
        ID = a.af.toString();
        VALUE = b.ew.toString();
        XL = b.bD.toString();
    }

    public w(DataLayer dataLayer) {
        super(ID, new String[]{VALUE});
        this.WK = dataLayer;
    }

    private void a(d.a aVar) {
        if (aVar != null && aVar != dh.lN()) {
            String j = dh.j(aVar);
            if (j != dh.lS()) {
                this.WK.bv(j);
            }
        }
    }

    private void b(d.a aVar) {
        if (aVar != null && aVar != dh.lN()) {
            Object o = dh.o(aVar);
            if (o instanceof List) {
                Iterator it = ((List) o).iterator();
                while (it.hasNext()) {
                    o = it.next();
                    if (o instanceof Map) {
                        this.WK.push((Map) o);
                    }
                }
            }
        }
    }

    public void z(Map<String, d.a> map) {
        b((d.a) map.get(VALUE));
        a((d.a) map.get(XL));
    }
}