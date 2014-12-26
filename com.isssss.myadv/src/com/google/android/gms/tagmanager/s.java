package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.b;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

class s extends aj {
    private static final String ID;
    private static final String WC;
    private static final String Xn;
    private final a Xo;

    public static interface a {
        Object b(String str, Map<String, Object> map);
    }

    static {
        ID = com.google.android.gms.internal.a.J.toString();
        Xn = b.cy.toString();
        WC = b.aX.toString();
    }

    public s(a aVar) {
        super(ID, new String[]{Xn});
        this.Xo = aVar;
    }

    public boolean jX() {
        return false;
    }

    public com.google.android.gms.internal.d.a x(Map<String, com.google.android.gms.internal.d.a> map) {
        String j = dh.j((com.google.android.gms.internal.d.a) map.get(Xn));
        Map hashMap = new HashMap();
        com.google.android.gms.internal.d.a aVar = (com.google.android.gms.internal.d.a) map.get(WC);
        if (aVar != null) {
            Object o = dh.o(aVar);
            if (o instanceof Map) {
                Iterator it = ((Map) o).entrySet().iterator();
                while (it.hasNext()) {
                    Entry entry = (Entry) it.next();
                    hashMap.put(entry.getKey().toString(), entry.getValue());
                }
            } else {
                bh.z("FunctionCallMacro: expected ADDITIONAL_PARAMS to be a map.");
                return dh.lT();
            }
        }
        try {
            return dh.r(this.Xo.b(j, hashMap));
        } catch (Exception e) {
            bh.z("Custom macro/tag " + j + " threw exception " + e.getMessage());
            return dh.lT();
        }
    }
}