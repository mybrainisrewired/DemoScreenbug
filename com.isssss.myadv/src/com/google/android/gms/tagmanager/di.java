package com.google.android.gms.tagmanager;

import android.content.Context;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.internal.a;
import com.google.android.gms.internal.b;
import com.google.android.gms.internal.d;
import com.inmobi.commons.analytics.db.AnalyticsSQLiteHelper;
import com.mopub.common.Preconditions;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

class di extends df {
    private static final String ID;
    private static final String aaO;
    private static final String aaP;
    private static final String aaQ;
    private static final String aaR;
    private static final String aaS;
    private static final String aaT;
    private static Map<String, String> aaU;
    private static Map<String, String> aaV;
    private final DataLayer WK;
    private final Set<String> aaW;
    private final de aaX;

    static {
        ID = a.aF.toString();
        aaO = b.aV.toString();
        aaP = b.be.toString();
        aaQ = b.bd.toString();
        aaR = b.eg.toString();
        aaS = b.ei.toString();
        aaT = b.ek.toString();
    }

    public di(Context context, DataLayer dataLayer) {
        this(context, dataLayer, new de(context));
    }

    di(Context context, DataLayer dataLayer, de deVar) {
        super(ID, new String[0]);
        this.WK = dataLayer;
        this.aaX = deVar;
        this.aaW = new HashSet();
        this.aaW.add(Preconditions.EMPTY_ARGUMENTS);
        this.aaW.add("0");
        this.aaW.add("false");
    }

    private Map<String, String> H(Map<String, d.a> map) {
        d.a aVar = (d.a) map.get(aaS);
        if (aVar != null) {
            return c(aVar);
        }
        if (aaU == null) {
            Map hashMap = new HashMap();
            hashMap.put(AnalyticsSQLiteHelper.TRANSACTION_ID, "&ti");
            hashMap.put("transactionAffiliation", "&ta");
            hashMap.put("transactionTax", "&tt");
            hashMap.put("transactionShipping", "&ts");
            hashMap.put("transactionTotal", "&tr");
            hashMap.put("transactionCurrency", "&cu");
            aaU = hashMap;
        }
        return aaU;
    }

    private Map<String, String> I(Map<String, d.a> map) {
        d.a aVar = (d.a) map.get(aaT);
        if (aVar != null) {
            return c(aVar);
        }
        if (aaV == null) {
            Map hashMap = new HashMap();
            hashMap.put("name", "&in");
            hashMap.put("sku", "&ic");
            hashMap.put("category", "&iv");
            hashMap.put("price", "&ip");
            hashMap.put("quantity", "&iq");
            hashMap.put("currency", "&cu");
            aaV = hashMap;
        }
        return aaV;
    }

    private void a(Tracker tracker, Map<String, d.a> map) {
        String cc = cc(AnalyticsSQLiteHelper.TRANSACTION_ID);
        if (cc == null) {
            bh.w("Cannot find transactionId in data layer.");
        } else {
            List linkedList = new LinkedList();
            try {
                Map p = p((d.a) map.get(aaQ));
                p.put("&t", "transaction");
                Iterator it = H(map).entrySet().iterator();
                while (it.hasNext()) {
                    Entry entry = (Entry) it.next();
                    b(p, (String) entry.getValue(), cc((String) entry.getKey()));
                }
                linkedList.add(p);
                List lU = lU();
                if (lU != null) {
                    it = lU.iterator();
                    while (it.hasNext()) {
                        Map map2 = (Map) it.next();
                        if (map2.get("name") == null) {
                            bh.w("Unable to send transaction item hit due to missing 'name' field.");
                            return;
                        } else {
                            Map p2 = p((d.a) map.get(aaQ));
                            p2.put("&t", "item");
                            p2.put("&ti", cc);
                            Iterator it2 = I(map).entrySet().iterator();
                            while (it2.hasNext()) {
                                Entry entry2 = (Entry) it2.next();
                                b(p2, (String) entry2.getValue(), (String) map2.get(entry2.getKey()));
                            }
                            linkedList.add(p2);
                        }
                    }
                }
                Iterator it3 = linkedList.iterator();
                while (it3.hasNext()) {
                    tracker.send((Map) it3.next());
                }
            } catch (IllegalArgumentException e) {
                bh.b("Unable to send transaction", e);
            }
        }
    }

    private void b(Map<String, String> map, String str, String str2) {
        if (str2 != null) {
            map.put(str, str2);
        }
    }

    private Map<String, String> c(d.a aVar) {
        Object o = dh.o(aVar);
        if (!(o instanceof Map)) {
            return null;
        }
        Map map = (Map) o;
        Map<String, String> linkedHashMap = new LinkedHashMap();
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            linkedHashMap.put(entry.getKey().toString(), entry.getValue().toString());
        }
        return linkedHashMap;
    }

    private String cc(String str) {
        Object obj = this.WK.get(str);
        return obj == null ? null : obj.toString();
    }

    private boolean e(Map<String, d.a> map, String str) {
        d.a aVar = (d.a) map.get(str);
        return aVar == null ? false : dh.n(aVar).booleanValue();
    }

    private List<Map<String, String>> lU() {
        Object obj = this.WK.get("transactionProducts");
        if (obj == null) {
            return null;
        }
        if (obj instanceof List) {
            Iterator it = ((List) obj).iterator();
            while (it.hasNext()) {
                if (!it.next() instanceof Map) {
                    throw new IllegalArgumentException("Each element of transactionProducts should be of type Map.");
                }
            }
            return (List) obj;
        } else {
            throw new IllegalArgumentException("transactionProducts should be of type List.");
        }
    }

    private Map<String, String> p(d.a aVar) {
        if (aVar == null) {
            return new HashMap();
        }
        Map<String, String> c = c(aVar);
        if (c == null) {
            return new HashMap();
        }
        String str = (String) c.get("&aip");
        if (str != null && this.aaW.contains(str.toLowerCase())) {
            c.remove("&aip");
        }
        return c;
    }

    public void z(Map<String, d.a> map) {
        Tracker bU = this.aaX.bU("_GTM_DEFAULT_TRACKER_");
        if (e(map, aaP)) {
            bU.send(p((d.a) map.get(aaQ)));
        } else if (e(map, aaR)) {
            a(bU, map);
        } else {
            bh.z("Ignoring unknown tag.");
        }
    }
}