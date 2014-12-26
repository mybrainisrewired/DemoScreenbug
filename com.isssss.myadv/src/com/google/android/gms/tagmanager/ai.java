package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.c.c;
import com.google.android.gms.internal.c.d;
import com.google.android.gms.internal.c.i;
import com.google.android.gms.internal.d.a;
import java.util.Map;

class ai {
    private static void a(DataLayer dataLayer, d dVar) {
        a[] aVarArr = dVar.eS;
        int length = aVarArr.length;
        int i = 0;
        while (i < length) {
            dataLayer.bv(dh.j(aVarArr[i]));
            i++;
        }
    }

    public static void a(DataLayer dataLayer, i iVar) {
        if (iVar.fI == null) {
            bh.z("supplemental missing experimentSupplemental");
        } else {
            a(dataLayer, iVar.fI);
            b(dataLayer, iVar.fI);
            c(dataLayer, iVar.fI);
        }
    }

    private static void b(DataLayer dataLayer, d dVar) {
        a[] aVarArr = dVar.eR;
        int length = aVarArr.length;
        int i = 0;
        while (i < length) {
            Map c = c(aVarArr[i]);
            if (c != null) {
                dataLayer.push(c);
            }
            i++;
        }
    }

    private static Map<String, Object> c(a aVar) {
        Object o = dh.o(aVar);
        if (o instanceof Map) {
            return (Map) o;
        }
        bh.z("value: " + o + " is not a map value, ignored.");
        return null;
    }

    private static void c(DataLayer dataLayer, d dVar) {
        c[] cVarArr = dVar.eT;
        int length = cVarArr.length;
        int i = 0;
        while (i < length) {
            c cVar = cVarArr[i];
            if (cVar.eM == null) {
                bh.z("GaExperimentRandom: No key");
            } else {
                Object obj = dataLayer.get(cVar.eM);
                Long valueOf = !(obj instanceof Number) ? null : Long.valueOf(((Number) obj).longValue());
                long j = cVar.eN;
                long j2 = cVar.eO;
                if ((!cVar.eP || valueOf == null || valueOf.longValue() < j || valueOf.longValue() > j2) && j > j2) {
                    bh.z("GaExperimentRandom: random range invalid");
                } else {
                    obj = Long.valueOf(Math.round(Math.random() * ((double) (j2 - j)) + ((double) j)));
                    dataLayer.bv(cVar.eM);
                    Map c = dataLayer.c(cVar.eM, obj);
                    if (cVar.eQ > 0) {
                        if (c.containsKey("gtm")) {
                            Object obj2 = c.get("gtm");
                            if (obj2 instanceof Map) {
                                ((Map) obj2).put("lifetime", Long.valueOf(cVar.eQ));
                            } else {
                                bh.z("GaExperimentRandom: gtm not a map");
                            }
                        } else {
                            c.put("gtm", DataLayer.mapOf(new Object[]{"lifetime", Long.valueOf(cVar.eQ)}));
                        }
                    }
                    dataLayer.push(c);
                }
            }
            i++;
        }
    }
}