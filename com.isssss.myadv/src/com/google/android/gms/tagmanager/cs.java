package com.google.android.gms.tagmanager;

import android.content.Context;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import com.google.android.gms.internal.c.i;
import com.google.android.gms.tagmanager.cq.e;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

class cs {
    private static final by<com.google.android.gms.internal.d.a> ZE;
    private final DataLayer WK;
    private final com.google.android.gms.tagmanager.cq.c ZF;
    private final ag ZG;
    private final Map<String, aj> ZH;
    private final Map<String, aj> ZI;
    private final Map<String, aj> ZJ;
    private final k<com.google.android.gms.tagmanager.cq.a, by<com.google.android.gms.internal.d.a>> ZK;
    private final k<String, b> ZL;
    private final Set<e> ZM;
    private final Map<String, c> ZN;
    private volatile String ZO;
    private int ZP;

    static interface a {
        void a(e eVar, Set<com.google.android.gms.tagmanager.cq.a> set, Set<com.google.android.gms.tagmanager.cq.a> set2, cm cmVar);
    }

    private static class b {
        private by<com.google.android.gms.internal.d.a> ZV;
        private com.google.android.gms.internal.d.a Zq;

        public b(by<com.google.android.gms.internal.d.a> byVar, com.google.android.gms.internal.d.a aVar) {
            this.ZV = byVar;
            this.Zq = aVar;
        }

        public int getSize() {
            return (this.Zq == null ? 0 : this.Zq.mF()) + ((com.google.android.gms.internal.d.a) this.ZV.getObject()).mF();
        }

        public com.google.android.gms.internal.d.a lf() {
            return this.Zq;
        }

        public by<com.google.android.gms.internal.d.a> lz() {
            return this.ZV;
        }
    }

    private static class c {
        private final Set<e> ZM;
        private final Map<e, List<com.google.android.gms.tagmanager.cq.a>> ZW;
        private final Map<e, List<com.google.android.gms.tagmanager.cq.a>> ZX;
        private final Map<e, List<String>> ZY;
        private final Map<e, List<String>> ZZ;
        private com.google.android.gms.tagmanager.cq.a aaa;

        public c() {
            this.ZM = new HashSet();
            this.ZW = new HashMap();
            this.ZY = new HashMap();
            this.ZX = new HashMap();
            this.ZZ = new HashMap();
        }

        public void a(e eVar, com.google.android.gms.tagmanager.cq.a aVar) {
            List list = (List) this.ZW.get(eVar);
            if (list == null) {
                list = new ArrayList();
                this.ZW.put(eVar, list);
            }
            list.add(aVar);
        }

        public void a(e eVar, String str) {
            List list = (List) this.ZY.get(eVar);
            if (list == null) {
                list = new ArrayList();
                this.ZY.put(eVar, list);
            }
            list.add(str);
        }

        public void b(e eVar) {
            this.ZM.add(eVar);
        }

        public void b(e eVar, com.google.android.gms.tagmanager.cq.a aVar) {
            List list = (List) this.ZX.get(eVar);
            if (list == null) {
                list = new ArrayList();
                this.ZX.put(eVar, list);
            }
            list.add(aVar);
        }

        public void b(e eVar, String str) {
            List list = (List) this.ZZ.get(eVar);
            if (list == null) {
                list = new ArrayList();
                this.ZZ.put(eVar, list);
            }
            list.add(str);
        }

        public void i(com.google.android.gms.tagmanager.cq.a aVar) {
            this.aaa = aVar;
        }

        public Set<e> lA() {
            return this.ZM;
        }

        public Map<e, List<com.google.android.gms.tagmanager.cq.a>> lB() {
            return this.ZW;
        }

        public Map<e, List<String>> lC() {
            return this.ZY;
        }

        public Map<e, List<String>> lD() {
            return this.ZZ;
        }

        public Map<e, List<com.google.android.gms.tagmanager.cq.a>> lE() {
            return this.ZX;
        }

        public com.google.android.gms.tagmanager.cq.a lF() {
            return this.aaa;
        }
    }

    class AnonymousClass_3 implements a {
        final /* synthetic */ Map ZR;
        final /* synthetic */ Map ZS;
        final /* synthetic */ Map ZT;
        final /* synthetic */ Map ZU;

        AnonymousClass_3(Map map, Map map2, Map map3, Map map4) {
            this.ZR = map;
            this.ZS = map2;
            this.ZT = map3;
            this.ZU = map4;
        }

        public void a(e eVar, Set<com.google.android.gms.tagmanager.cq.a> set, Set<com.google.android.gms.tagmanager.cq.a> set2, cm cmVar) {
            List list = (List) this.ZR.get(eVar);
            List list2 = (List) this.ZS.get(eVar);
            if (list != null) {
                set.addAll(list);
                cmVar.kK().b(list, list2);
            }
            list = (List) this.ZT.get(eVar);
            list2 = (List) this.ZU.get(eVar);
            if (list != null) {
                set2.addAll(list);
                cmVar.kL().b(list, list2);
            }
        }
    }

    static {
        ZE = new by(dh.lT(), true);
    }

    public cs(Context context, com.google.android.gms.tagmanager.cq.c cVar, DataLayer dataLayer, com.google.android.gms.tagmanager.s.a aVar, com.google.android.gms.tagmanager.s.a aVar2, ag agVar) {
        if (cVar == null) {
            throw new NullPointerException("resource cannot be null");
        }
        this.ZF = cVar;
        this.ZM = new HashSet(cVar.li());
        this.WK = dataLayer;
        this.ZG = agVar;
        this.ZK = new l().a(AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_START, new com.google.android.gms.tagmanager.l.a<com.google.android.gms.tagmanager.cq.a, by<com.google.android.gms.internal.d.a>>() {
            public int a(com.google.android.gms.tagmanager.cq.a aVar, by<com.google.android.gms.internal.d.a> byVar) {
                return ((com.google.android.gms.internal.d.a) byVar.getObject()).mF();
            }

            public /* synthetic */ int sizeOf(Object x0, Object x1) {
                return a((com.google.android.gms.tagmanager.cq.a) x0, (by) x1);
            }
        });
        this.ZL = new l().a(AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_START, new com.google.android.gms.tagmanager.l.a<String, b>() {
            public int a(String str, b bVar) {
                return str.length() + bVar.getSize();
            }

            public /* synthetic */ int sizeOf(Object x0, Object x1) {
                return a((String) x0, (b) x1);
            }
        });
        this.ZH = new HashMap();
        b(new i(context));
        b(new s(aVar2));
        b(new w(dataLayer));
        b(new di(context, dataLayer));
        this.ZI = new HashMap();
        c(new q());
        c(new ad());
        c(new ae());
        c(new al());
        c(new am());
        c(new bd());
        c(new be());
        c(new ch());
        c(new db());
        this.ZJ = new HashMap();
        a(new b(context));
        a(new c(context));
        a(new e(context));
        a(new f(context));
        a(new g(context));
        a(new h(context));
        a(new m());
        a(new p(this.ZF.getVersion()));
        a(new s(aVar));
        a(new u(dataLayer));
        a(new z(context));
        a(new aa());
        a(new ac());
        a(new ah(this));
        a(new an());
        a(new ao());
        a(new ax(context));
        a(new az());
        a(new bc());
        a(new bk(context));
        a(new bz());
        a(new cb());
        a(new ce());
        a(new cg());
        a(new ci(context));
        a(new ct());
        a(new cu());
        a(new dd());
        this.ZN = new HashMap();
        Iterator it = this.ZM.iterator();
        while (it.hasNext()) {
            e eVar = (e) it.next();
            if (agVar.kA()) {
                a(eVar.lq(), eVar.lr(), "add macro");
                a(eVar.lv(), eVar.ls(), "remove macro");
                a(eVar.lo(), eVar.lt(), "add tag");
                a(eVar.lp(), eVar.lu(), "remove tag");
            }
            int i = 0;
            while (i < eVar.lq().size()) {
                com.google.android.gms.tagmanager.cq.a aVar3 = (com.google.android.gms.tagmanager.cq.a) eVar.lq().get(i);
                String str = "Unknown";
                if (agVar.kA() && i < eVar.lr().size()) {
                    str = (String) eVar.lr().get(i);
                }
                c d = d(this.ZN, h(aVar3));
                d.b(eVar);
                d.a(eVar, aVar3);
                d.a(eVar, str);
                i++;
            }
            i = 0;
            while (i < eVar.lv().size()) {
                aVar3 = (com.google.android.gms.tagmanager.cq.a) eVar.lv().get(i);
                str = "Unknown";
                if (agVar.kA() && i < eVar.ls().size()) {
                    str = (String) eVar.ls().get(i);
                }
                d = d(this.ZN, h(aVar3));
                d.b(eVar);
                d.b(eVar, aVar3);
                d.b(eVar, str);
                i++;
            }
        }
        Iterator it2 = this.ZF.lj().entrySet().iterator();
        while (it2.hasNext()) {
            Entry entry = (Entry) it2.next();
            Iterator it3 = ((List) entry.getValue()).iterator();
            while (it3.hasNext()) {
                aVar3 = (com.google.android.gms.tagmanager.cq.a) it3.next();
                if (!dh.n((com.google.android.gms.internal.d.a) aVar3.le().get(com.google.android.gms.internal.b.dh.toString())).booleanValue()) {
                    d(this.ZN, (String) entry.getKey()).i(aVar3);
                }
            }
        }
    }

    private by<com.google.android.gms.internal.d.a> a(com.google.android.gms.internal.d.a aVar, Set set, dj djVar) {
        if (!aVar.fX) {
            return new by(aVar, true);
        }
        com.google.android.gms.internal.d.a g;
        int i;
        by a;
        switch (aVar.type) {
            case MMAdView.TRANSITION_UP:
                g = cq.g(aVar);
                g.fO = new com.google.android.gms.internal.d.a[aVar.fO.length];
                i = 0;
                while (i < aVar.fO.length) {
                    a = a(aVar.fO[i], set, djVar.cd(i));
                    if (a == ZE) {
                        return ZE;
                    }
                    g.fO[i] = (com.google.android.gms.internal.d.a) a.getObject();
                    i++;
                }
                return new by(g, false);
            case MMAdView.TRANSITION_DOWN:
                g = cq.g(aVar);
                if (aVar.fP.length != aVar.fQ.length) {
                    bh.w("Invalid serving value: " + aVar.toString());
                    return ZE;
                } else {
                    g.fP = new com.google.android.gms.internal.d.a[aVar.fP.length];
                    g.fQ = new com.google.android.gms.internal.d.a[aVar.fP.length];
                    i = 0;
                    while (i < aVar.fP.length) {
                        a = a(aVar.fP[i], set, djVar.ce(i));
                        by a2 = a(aVar.fQ[i], set, djVar.cf(i));
                        if (a != ZE && a2 != ZE) {
                            g.fP[i] = (com.google.android.gms.internal.d.a) a.getObject();
                            g.fQ[i] = (com.google.android.gms.internal.d.a) a2.getObject();
                            i++;
                        }
                        return ZE;
                    }
                    return new by(g, false);
                }
            case MMAdView.TRANSITION_RANDOM:
                if (set.contains(aVar.fR)) {
                    bh.w("Macro cycle detected.  Current macro reference: " + aVar.fR + "." + "  Previous macro references: " + set.toString() + ".");
                    return ZE;
                } else {
                    set.add(aVar.fR);
                    by<com.google.android.gms.internal.d.a> a3 = dk.a(a(aVar.fR, set, djVar.kP()), aVar.fW);
                    set.remove(aVar.fR);
                    return a3;
                }
            case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                g = cq.g(aVar);
                g.fV = new com.google.android.gms.internal.d.a[aVar.fV.length];
                i = 0;
                while (i < aVar.fV.length) {
                    a = a(aVar.fV[i], set, djVar.cg(i));
                    if (a == ZE) {
                        return ZE;
                    }
                    g.fV[i] = (com.google.android.gms.internal.d.a) a.getObject();
                    i++;
                }
                return new by(g, false);
            default:
                bh.w("Unknown type: " + aVar.type);
                return ZE;
        }
    }

    private by<com.google.android.gms.internal.d.a> a(String str, Set set, bj bjVar) {
        this.ZP++;
        b bVar = (b) this.ZL.get(str);
        if (bVar == null || this.ZG.kA()) {
            c cVar = (c) this.ZN.get(str);
            if (cVar == null) {
                bh.w(ly() + "Invalid macro: " + str);
                this.ZP--;
                return ZE;
            } else {
                com.google.android.gms.tagmanager.cq.a aVar;
                by a = a(str, cVar.lA(), cVar.lB(), cVar.lC(), cVar.lE(), cVar.lD(), set, bjVar.kr());
                if (((Set) a.getObject()).isEmpty()) {
                    aVar = cVar.lF();
                } else {
                    if (((Set) a.getObject()).size() > 1) {
                        bh.z(ly() + "Multiple macros active for macroName " + str);
                    }
                    aVar = (com.google.android.gms.tagmanager.cq.a) ((Set) a.getObject()).iterator().next();
                }
                if (aVar == null) {
                    this.ZP--;
                    return ZE;
                } else {
                    by a2 = a(this.ZJ, aVar, set, bjVar.kG());
                    boolean z = a.kQ() && a2.kQ();
                    by<com.google.android.gms.internal.d.a> byVar = a2 == ZE ? ZE : new by(a2.getObject(), z);
                    com.google.android.gms.internal.d.a lf = aVar.lf();
                    if (byVar.kQ()) {
                        this.ZL.e(str, new b(byVar, lf));
                    }
                    a(lf, set);
                    this.ZP--;
                    return byVar;
                }
            }
        } else {
            a(bVar.lf(), set);
            this.ZP--;
            return bVar.lz();
        }
    }

    private by<com.google.android.gms.internal.d.a> a(Map<String, aj> map, com.google.android.gms.tagmanager.cq.a aVar, Set set, cj cjVar) {
        boolean z = true;
        com.google.android.gms.internal.d.a aVar2 = (com.google.android.gms.internal.d.a) aVar.le().get(com.google.android.gms.internal.b.cx.toString());
        if (aVar2 == null) {
            bh.w("No function id in properties");
            return ZE;
        } else {
            String str = aVar2.fS;
            aj ajVar = (aj) map.get(str);
            if (ajVar == null) {
                bh.w(str + " has no backing implementation.");
                return ZE;
            } else {
                by<com.google.android.gms.internal.d.a> byVar = (by) this.ZK.get(aVar);
                if (byVar != null && !this.ZG.kA()) {
                    return byVar;
                }
                Map hashMap = new HashMap();
                Iterator it = aVar.le().entrySet().iterator();
                boolean z2 = true;
                while (it.hasNext()) {
                    Entry entry = (Entry) it.next();
                    by a = a((com.google.android.gms.internal.d.a) entry.getValue(), set, cjVar.bH((String) entry.getKey()).e((com.google.android.gms.internal.d.a) entry.getValue()));
                    if (a == ZE) {
                        return ZE;
                    }
                    boolean z3;
                    if (a.kQ()) {
                        aVar.a((String) entry.getKey(), (com.google.android.gms.internal.d.a) a.getObject());
                        int i = i;
                    } else {
                        z3 = false;
                    }
                    hashMap.put(entry.getKey(), a.getObject());
                    z2 = z3;
                }
                if (ajVar.a(hashMap.keySet())) {
                    if (i == 0 || !ajVar.jX()) {
                        z = false;
                    }
                    byVar = new by(ajVar.x(hashMap), z);
                    if (z) {
                        this.ZK.e(aVar, byVar);
                    }
                    cjVar.d((com.google.android.gms.internal.d.a) byVar.getObject());
                    return byVar;
                } else {
                    bh.w("Incorrect keys for function " + str + " required " + ajVar.kC() + " had " + hashMap.keySet());
                    return ZE;
                }
            }
        }
    }

    private by<Set<com.google.android.gms.tagmanager.cq.a>> a(Set<e> set, Set set2, a aVar, cr crVar) {
        Set hashSet = new HashSet();
        Collection hashSet2 = new HashSet();
        Iterator it = set.iterator();
        boolean z = true;
        while (it.hasNext()) {
            e eVar = (e) it.next();
            cm kO = crVar.kO();
            by a = a(eVar, set2, kO);
            if (((Boolean) a.getObject()).booleanValue()) {
                aVar.a(eVar, hashSet, hashSet2, kO);
            }
            boolean z2 = (z && a.kQ()) ? true : 0;
            z = z2;
        }
        hashSet.removeAll(hashSet2);
        crVar.b(hashSet);
        return new by(hashSet, z);
    }

    private void a(com.google.android.gms.internal.d.a aVar, Set set) {
        if (aVar != null) {
            by a = a(aVar, set, new bw());
            if (a != ZE) {
                Object o = dh.o((com.google.android.gms.internal.d.a) a.getObject());
                if (o instanceof Map) {
                    this.WK.push((Map) o);
                } else if (o instanceof List) {
                    Iterator it = ((List) o).iterator();
                    while (it.hasNext()) {
                        o = it.next();
                        if (o instanceof Map) {
                            this.WK.push((Map) o);
                        } else {
                            bh.z("pushAfterEvaluate: value not a Map");
                        }
                    }
                } else {
                    bh.z("pushAfterEvaluate: value not a Map or List");
                }
            }
        }
    }

    private static void a(List<com.google.android.gms.tagmanager.cq.a> list, List<String> list2, String str) {
        if (list.size() != list2.size()) {
            bh.x("Invalid resource: imbalance of rule names of functions for " + str + " operation. Using default rule name instead");
        }
    }

    private static void a(Map<String, aj> map, aj ajVar) {
        if (map.containsKey(ajVar.kB())) {
            throw new IllegalArgumentException("Duplicate function type name: " + ajVar.kB());
        }
        map.put(ajVar.kB(), ajVar);
    }

    private static c d(Map<String, c> map, String str) {
        c cVar = (c) map.get(str);
        if (cVar != null) {
            return cVar;
        }
        cVar = new c();
        map.put(str, cVar);
        return cVar;
    }

    private static String h(com.google.android.gms.tagmanager.cq.a aVar) {
        return dh.j((com.google.android.gms.internal.d.a) aVar.le().get(com.google.android.gms.internal.b.cI.toString()));
    }

    private String ly() {
        if (this.ZP <= 1) {
            return Preconditions.EMPTY_ARGUMENTS;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Integer.toString(this.ZP));
        int i = MMAdView.TRANSITION_UP;
        while (i < this.ZP) {
            stringBuilder.append(' ');
            i++;
        }
        stringBuilder.append(": ");
        return stringBuilder.toString();
    }

    by<Boolean> a(com.google.android.gms.tagmanager.cq.a aVar, Set set, cj cjVar) {
        by a = a(this.ZI, aVar, set, cjVar);
        Boolean n = dh.n((com.google.android.gms.internal.d.a) a.getObject());
        cjVar.d(dh.r(n));
        return new by(n, a.kQ());
    }

    by<Boolean> a(e eVar, Set set, cm cmVar) {
        Iterator it = eVar.ln().iterator();
        boolean z = true;
        while (it.hasNext()) {
            by a = a((com.google.android.gms.tagmanager.cq.a) it.next(), set, cmVar.kI());
            if (((Boolean) a.getObject()).booleanValue()) {
                cmVar.f(dh.r(Boolean.valueOf(false)));
                return new by(Boolean.valueOf(false), a.kQ());
            } else {
                boolean z2 = z && a.kQ();
                z = z2;
            }
        }
        it = eVar.lm().iterator();
        while (it.hasNext()) {
            a = a((com.google.android.gms.tagmanager.cq.a) it.next(), set, cmVar.kJ());
            if (((Boolean) a.getObject()).booleanValue()) {
                z = z && a.kQ();
            } else {
                cmVar.f(dh.r(Boolean.valueOf(false)));
                return new by(Boolean.valueOf(false), a.kQ());
            }
        }
        cmVar.f(dh.r(Boolean.valueOf(true)));
        return new by(Boolean.valueOf(true), z);
    }

    by<Set<com.google.android.gms.tagmanager.cq.a>> a(String str, Set set, Map<e, List<com.google.android.gms.tagmanager.cq.a>> map, Map<e, List<String>> map2, Map<e, List<com.google.android.gms.tagmanager.cq.a>> map3, Map<e, List<String>> map4, Set set2, cr crVar) {
        return a(set, set2, new AnonymousClass_3(map, map2, map3, map4), crVar);
    }

    by<Set<com.google.android.gms.tagmanager.cq.a>> a(Set set, cr crVar) {
        return a(set, new HashSet(), new a() {
            public void a(e eVar, Set<com.google.android.gms.tagmanager.cq.a> set, Set<com.google.android.gms.tagmanager.cq.a> set2, cm cmVar) {
                set.addAll(eVar.lo());
                set2.addAll(eVar.lp());
                cmVar.kM().b(eVar.lo(), eVar.lt());
                cmVar.kN().b(eVar.lp(), eVar.lu());
            }
        }, crVar);
    }

    void a(aj ajVar) {
        a(this.ZJ, ajVar);
    }

    void b(aj ajVar) {
        a(this.ZH, ajVar);
    }

    public by<com.google.android.gms.internal.d.a> bR(String str) {
        this.ZP = 0;
        af bA = this.ZG.bA(str);
        by<com.google.android.gms.internal.d.a> a = a(str, new HashSet(), bA.kx());
        bA.kz();
        return a;
    }

    synchronized void bS(String str) {
        this.ZO = str;
    }

    public synchronized void bp(String str) {
        bS(str);
        af bB = this.ZG.bB(str);
        t ky = bB.ky();
        Iterator it = ((Set) a(this.ZM, ky.kr()).getObject()).iterator();
        while (it.hasNext()) {
            a(this.ZH, (com.google.android.gms.tagmanager.cq.a) it.next(), new HashSet(), ky.kq());
        }
        bB.kz();
        bS(null);
    }

    void c(aj ajVar) {
        a(this.ZI, ajVar);
    }

    public synchronized void e(List<i> list) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            i iVar = (i) it.next();
            if (iVar.name == null || !iVar.name.startsWith("gaExperiment:")) {
                bh.y("Ignored supplemental: " + iVar);
            } else {
                ai.a(this.WK, iVar);
            }
        }
    }

    synchronized String lx() {
        return this.ZO;
    }
}