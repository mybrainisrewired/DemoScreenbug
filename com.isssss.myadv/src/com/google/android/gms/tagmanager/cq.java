package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.c.h;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class cq {

    public static class a {
        private final Map<String, com.google.android.gms.internal.d.a> Zp;
        private final com.google.android.gms.internal.d.a Zq;

        private a(Map<String, com.google.android.gms.internal.d.a> map, com.google.android.gms.internal.d.a aVar) {
            this.Zp = map;
            this.Zq = aVar;
        }

        public static com.google.android.gms.tagmanager.cq.b ld() {
            return new com.google.android.gms.tagmanager.cq.b();
        }

        public void a(String str, com.google.android.gms.internal.d.a aVar) {
            this.Zp.put(str, aVar);
        }

        public Map<String, com.google.android.gms.internal.d.a> le() {
            return Collections.unmodifiableMap(this.Zp);
        }

        public com.google.android.gms.internal.d.a lf() {
            return this.Zq;
        }

        public String toString() {
            return "Properties: " + le() + " pushAfterEvaluate: " + this.Zq;
        }
    }

    public static class b {
        private final Map<String, com.google.android.gms.internal.d.a> Zp;
        private com.google.android.gms.internal.d.a Zq;

        private b() {
            this.Zp = new HashMap();
        }

        public com.google.android.gms.tagmanager.cq.b b(String str, com.google.android.gms.internal.d.a aVar) {
            this.Zp.put(str, aVar);
            return this;
        }

        public com.google.android.gms.tagmanager.cq.b i(com.google.android.gms.internal.d.a aVar) {
            this.Zq = aVar;
            return this;
        }

        public com.google.android.gms.tagmanager.cq.a lg() {
            return new com.google.android.gms.tagmanager.cq.a(this.Zq, null);
        }
    }

    public static class c {
        private final String Xl;
        private final List<com.google.android.gms.tagmanager.cq.e> Zr;
        private final Map<String, List<com.google.android.gms.tagmanager.cq.a>> Zs;
        private final int Zt;

        private c(List<com.google.android.gms.tagmanager.cq.e> list, Map<String, List<com.google.android.gms.tagmanager.cq.a>> map, String str, int i) {
            this.Zr = Collections.unmodifiableList(list);
            this.Zs = Collections.unmodifiableMap(map);
            this.Xl = str;
            this.Zt = i;
        }

        public static com.google.android.gms.tagmanager.cq.d lh() {
            return new com.google.android.gms.tagmanager.cq.d();
        }

        public String getVersion() {
            return this.Xl;
        }

        public List<com.google.android.gms.tagmanager.cq.e> li() {
            return this.Zr;
        }

        public Map<String, List<com.google.android.gms.tagmanager.cq.a>> lj() {
            return this.Zs;
        }

        public String toString() {
            return "Rules: " + li() + "  Macros: " + this.Zs;
        }
    }

    public static class d {
        private String Xl;
        private final List<com.google.android.gms.tagmanager.cq.e> Zr;
        private final Map<String, List<com.google.android.gms.tagmanager.cq.a>> Zs;
        private int Zt;

        private d() {
            this.Zr = new ArrayList();
            this.Zs = new HashMap();
            this.Xl = Preconditions.EMPTY_ARGUMENTS;
            this.Zt = 0;
        }

        public com.google.android.gms.tagmanager.cq.d a(com.google.android.gms.tagmanager.cq.a aVar) {
            String j = dh.j((com.google.android.gms.internal.d.a) aVar.le().get(com.google.android.gms.internal.b.cI.toString()));
            List list = (List) this.Zs.get(j);
            if (list == null) {
                list = new ArrayList();
                this.Zs.put(j, list);
            }
            list.add(aVar);
            return this;
        }

        public com.google.android.gms.tagmanager.cq.d a(com.google.android.gms.tagmanager.cq.e eVar) {
            this.Zr.add(eVar);
            return this;
        }

        public com.google.android.gms.tagmanager.cq.d bM(String str) {
            this.Xl = str;
            return this;
        }

        public com.google.android.gms.tagmanager.cq.d ch(int i) {
            this.Zt = i;
            return this;
        }

        public com.google.android.gms.tagmanager.cq.c lk() {
            return new com.google.android.gms.tagmanager.cq.c(this.Zs, this.Xl, this.Zt, null);
        }
    }

    public static class e {
        private final List<String> ZA;
        private final List<String> ZB;
        private final List<String> ZC;
        private final List<String> ZD;
        private final List<com.google.android.gms.tagmanager.cq.a> Zu;
        private final List<com.google.android.gms.tagmanager.cq.a> Zv;
        private final List<com.google.android.gms.tagmanager.cq.a> Zw;
        private final List<com.google.android.gms.tagmanager.cq.a> Zx;
        private final List<com.google.android.gms.tagmanager.cq.a> Zy;
        private final List<com.google.android.gms.tagmanager.cq.a> Zz;

        private e(List<com.google.android.gms.tagmanager.cq.a> list, List<com.google.android.gms.tagmanager.cq.a> list2, List<com.google.android.gms.tagmanager.cq.a> list3, List<com.google.android.gms.tagmanager.cq.a> list4, List<com.google.android.gms.tagmanager.cq.a> list5, List<com.google.android.gms.tagmanager.cq.a> list6, List<String> list7, List<String> list8, List<String> list9, List<String> list10) {
            this.Zu = Collections.unmodifiableList(list);
            this.Zv = Collections.unmodifiableList(list2);
            this.Zw = Collections.unmodifiableList(list3);
            this.Zx = Collections.unmodifiableList(list4);
            this.Zy = Collections.unmodifiableList(list5);
            this.Zz = Collections.unmodifiableList(list6);
            this.ZA = Collections.unmodifiableList(list7);
            this.ZB = Collections.unmodifiableList(list8);
            this.ZC = Collections.unmodifiableList(list9);
            this.ZD = Collections.unmodifiableList(list10);
        }

        public static com.google.android.gms.tagmanager.cq.f ll() {
            return new com.google.android.gms.tagmanager.cq.f();
        }

        public List<com.google.android.gms.tagmanager.cq.a> lm() {
            return this.Zu;
        }

        public List<com.google.android.gms.tagmanager.cq.a> ln() {
            return this.Zv;
        }

        public List<com.google.android.gms.tagmanager.cq.a> lo() {
            return this.Zw;
        }

        public List<com.google.android.gms.tagmanager.cq.a> lp() {
            return this.Zx;
        }

        public List<com.google.android.gms.tagmanager.cq.a> lq() {
            return this.Zy;
        }

        public List<String> lr() {
            return this.ZA;
        }

        public List<String> ls() {
            return this.ZB;
        }

        public List<String> lt() {
            return this.ZC;
        }

        public List<String> lu() {
            return this.ZD;
        }

        public List<com.google.android.gms.tagmanager.cq.a> lv() {
            return this.Zz;
        }

        public String toString() {
            return "Positive predicates: " + lm() + "  Negative predicates: " + ln() + "  Add tags: " + lo() + "  Remove tags: " + lp() + "  Add macros: " + lq() + "  Remove macros: " + lv();
        }
    }

    public static class f {
        private final List<String> ZA;
        private final List<String> ZB;
        private final List<String> ZC;
        private final List<String> ZD;
        private final List<com.google.android.gms.tagmanager.cq.a> Zu;
        private final List<com.google.android.gms.tagmanager.cq.a> Zv;
        private final List<com.google.android.gms.tagmanager.cq.a> Zw;
        private final List<com.google.android.gms.tagmanager.cq.a> Zx;
        private final List<com.google.android.gms.tagmanager.cq.a> Zy;
        private final List<com.google.android.gms.tagmanager.cq.a> Zz;

        private f() {
            this.Zu = new ArrayList();
            this.Zv = new ArrayList();
            this.Zw = new ArrayList();
            this.Zx = new ArrayList();
            this.Zy = new ArrayList();
            this.Zz = new ArrayList();
            this.ZA = new ArrayList();
            this.ZB = new ArrayList();
            this.ZC = new ArrayList();
            this.ZD = new ArrayList();
        }

        public com.google.android.gms.tagmanager.cq.f b(com.google.android.gms.tagmanager.cq.a aVar) {
            this.Zu.add(aVar);
            return this;
        }

        public com.google.android.gms.tagmanager.cq.f bN(String str) {
            this.ZC.add(str);
            return this;
        }

        public com.google.android.gms.tagmanager.cq.f bO(String str) {
            this.ZD.add(str);
            return this;
        }

        public com.google.android.gms.tagmanager.cq.f bP(String str) {
            this.ZA.add(str);
            return this;
        }

        public com.google.android.gms.tagmanager.cq.f bQ(String str) {
            this.ZB.add(str);
            return this;
        }

        public com.google.android.gms.tagmanager.cq.f c(com.google.android.gms.tagmanager.cq.a aVar) {
            this.Zv.add(aVar);
            return this;
        }

        public com.google.android.gms.tagmanager.cq.f d(com.google.android.gms.tagmanager.cq.a aVar) {
            this.Zw.add(aVar);
            return this;
        }

        public com.google.android.gms.tagmanager.cq.f e(com.google.android.gms.tagmanager.cq.a aVar) {
            this.Zx.add(aVar);
            return this;
        }

        public com.google.android.gms.tagmanager.cq.f f(com.google.android.gms.tagmanager.cq.a aVar) {
            this.Zy.add(aVar);
            return this;
        }

        public com.google.android.gms.tagmanager.cq.f g(com.google.android.gms.tagmanager.cq.a aVar) {
            this.Zz.add(aVar);
            return this;
        }

        public com.google.android.gms.tagmanager.cq.e lw() {
            return new com.google.android.gms.tagmanager.cq.e(this.Zv, this.Zw, this.Zx, this.Zy, this.Zz, this.ZA, this.ZB, this.ZC, this.ZD, null);
        }
    }

    public static class g extends Exception {
        public g(String str) {
            super(str);
        }
    }

    private static com.google.android.gms.internal.d.a a(int i, com.google.android.gms.internal.c.f fVar, com.google.android.gms.internal.d.a[] aVarArr, Set set) throws g {
        int i2 = 0;
        if (set.contains(Integer.valueOf(i))) {
            bL("Value cycle detected.  Current value reference: " + i + "." + "  Previous value references: " + set + ".");
        }
        com.google.android.gms.internal.d.a aVar = (com.google.android.gms.internal.d.a) a(fVar.eX, i, "values");
        if (aVarArr[i] != null) {
            return aVarArr[i];
        }
        com.google.android.gms.internal.d.a aVar2 = null;
        set.add(Integer.valueOf(i));
        h h;
        int[] iArr;
        int length;
        int i3;
        int i4;
        switch (aVar.type) {
            case MMAdView.TRANSITION_FADE:
            case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
            case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
            case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                aVar2 = aVar;
                break;
            case MMAdView.TRANSITION_UP:
                h = h(aVar);
                aVar2 = g(aVar);
                aVar2.fO = new com.google.android.gms.internal.d.a[h.fz.length];
                iArr = h.fz;
                length = iArr.length;
                i3 = 0;
                while (i2 < length) {
                    i4 = i3 + 1;
                    aVar2.fO[i3] = a(iArr[i2], fVar, aVarArr, set);
                    i2++;
                    i3 = i4;
                }
                break;
            case MMAdView.TRANSITION_DOWN:
                aVar2 = g(aVar);
                h h2 = h(aVar);
                if (h2.fA.length != h2.fB.length) {
                    bL("Uneven map keys (" + h2.fA.length + ") and map values (" + h2.fB.length + ")");
                }
                aVar2.fP = new com.google.android.gms.internal.d.a[h2.fA.length];
                aVar2.fQ = new com.google.android.gms.internal.d.a[h2.fA.length];
                int[] iArr2 = h2.fA;
                int length2 = iArr2.length;
                i3 = 0;
                i4 = 0;
                while (i3 < length2) {
                    int i5 = i4 + 1;
                    aVar2.fP[i4] = a(iArr2[i3], fVar, aVarArr, set);
                    i3++;
                    i4 = i5;
                }
                iArr = h2.fB;
                length = iArr.length;
                i3 = 0;
                while (i2 < length) {
                    i4 = i3 + 1;
                    aVar2.fQ[i3] = a(iArr[i2], fVar, aVarArr, set);
                    i2++;
                    i3 = i4;
                }
                break;
            case MMAdView.TRANSITION_RANDOM:
                aVar2 = g(aVar);
                aVar2.fR = dh.j(a(h(aVar).fE, fVar, aVarArr, set));
                break;
            case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                aVar2 = g(aVar);
                h = h(aVar);
                aVar2.fV = new com.google.android.gms.internal.d.a[h.fD.length];
                iArr = h.fD;
                length = iArr.length;
                i3 = 0;
                while (i2 < length) {
                    i4 = i3 + 1;
                    aVar2.fV[i3] = a(iArr[i2], fVar, aVarArr, set);
                    i2++;
                    i3 = i4;
                }
                break;
        }
        if (aVar2 == null) {
            bL("Invalid value: " + aVar);
        }
        aVarArr[i] = aVar2;
        set.remove(Integer.valueOf(i));
        return aVar2;
    }

    private static a a(com.google.android.gms.internal.c.b bVar, com.google.android.gms.internal.c.f fVar, com.google.android.gms.internal.d.a[] aVarArr, int i) throws g {
        b ld = a.ld();
        int[] iArr = bVar.eH;
        int length = iArr.length;
        int i2 = 0;
        while (i2 < length) {
            com.google.android.gms.internal.c.e eVar = (com.google.android.gms.internal.c.e) a(fVar.eY, Integer.valueOf(iArr[i2]).intValue(), "properties");
            String str = (String) a(fVar.eW, eVar.key, "keys");
            com.google.android.gms.internal.d.a aVar = (com.google.android.gms.internal.d.a) a(aVarArr, eVar.value, "values");
            if (com.google.android.gms.internal.b.dB.toString().equals(str)) {
                ld.i(aVar);
            } else {
                ld.b(str, aVar);
            }
            i2++;
        }
        return ld.lg();
    }

    private static e a(com.google.android.gms.internal.c.g gVar, List<a> list, List<a> list2, List<a> list3, com.google.android.gms.internal.c.f fVar) {
        f ll = e.ll();
        int[] iArr = gVar.fn;
        int length = iArr.length;
        int i = 0;
        while (i < length) {
            ll.b((a) list3.get(Integer.valueOf(iArr[i]).intValue()));
            i++;
        }
        iArr = gVar.fo;
        length = iArr.length;
        i = 0;
        while (i < length) {
            ll.c((a) list3.get(Integer.valueOf(iArr[i]).intValue()));
            i++;
        }
        iArr = gVar.fp;
        length = iArr.length;
        i = 0;
        while (i < length) {
            ll.d((a) list.get(Integer.valueOf(iArr[i]).intValue()));
            i++;
        }
        int[] iArr2 = gVar.fr;
        int length2 = iArr2.length;
        int i2 = 0;
        while (i2 < length2) {
            ll.bN(fVar.eX[Integer.valueOf(iArr2[i2]).intValue()].fN);
            i2++;
        }
        iArr = gVar.fq;
        length = iArr.length;
        i = 0;
        while (i < length) {
            ll.e((a) list.get(Integer.valueOf(iArr[i]).intValue()));
            i++;
        }
        iArr2 = gVar.fs;
        length2 = iArr2.length;
        i2 = 0;
        while (i2 < length2) {
            ll.bO(fVar.eX[Integer.valueOf(iArr2[i2]).intValue()].fN);
            i2++;
        }
        iArr = gVar.ft;
        length = iArr.length;
        i = 0;
        while (i < length) {
            ll.f((a) list2.get(Integer.valueOf(iArr[i]).intValue()));
            i++;
        }
        iArr2 = gVar.fv;
        length2 = iArr2.length;
        i2 = 0;
        while (i2 < length2) {
            ll.bP(fVar.eX[Integer.valueOf(iArr2[i2]).intValue()].fN);
            i2++;
        }
        iArr = gVar.fu;
        length = iArr.length;
        i = 0;
        while (i < length) {
            ll.g((a) list2.get(Integer.valueOf(iArr[i]).intValue()));
            i++;
        }
        iArr2 = gVar.fw;
        length2 = iArr2.length;
        i2 = 0;
        while (i2 < length2) {
            ll.bQ(fVar.eX[Integer.valueOf(iArr2[i2]).intValue()].fN);
            i2++;
        }
        return ll.lw();
    }

    private static <T> T a(T[] tArr, int i, String str) throws g {
        if (i < 0 || i >= tArr.length) {
            bL("Index out of bounds detected: " + i + " in " + str);
        }
        return tArr[i];
    }

    public static c b(com.google.android.gms.internal.c.f fVar) throws g {
        int i = 0;
        com.google.android.gms.internal.d.a[] aVarArr = new com.google.android.gms.internal.d.a[fVar.eX.length];
        int i2 = 0;
        while (i2 < fVar.eX.length) {
            a(i2, fVar, aVarArr, new HashSet(0));
            i2++;
        }
        d lh = c.lh();
        List arrayList = new ArrayList();
        i2 = 0;
        while (i2 < fVar.fa.length) {
            arrayList.add(a(fVar.fa[i2], fVar, aVarArr, i2));
            i2++;
        }
        List arrayList2 = new ArrayList();
        i2 = 0;
        while (i2 < fVar.fb.length) {
            arrayList2.add(a(fVar.fb[i2], fVar, aVarArr, i2));
            i2++;
        }
        List arrayList3 = new ArrayList();
        i2 = 0;
        while (i2 < fVar.eZ.length) {
            a a = a(fVar.eZ[i2], fVar, aVarArr, i2);
            lh.a(a);
            arrayList3.add(a);
            i2++;
        }
        com.google.android.gms.internal.c.g[] gVarArr = fVar.fc;
        int length = gVarArr.length;
        while (i < length) {
            lh.a(a(gVarArr[i], arrayList, arrayList3, arrayList2, fVar));
            i++;
        }
        lh.bM(fVar.fg);
        lh.ch(fVar.fl);
        return lh.lk();
    }

    public static void b(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[1024];
        while (true) {
            int read = inputStream.read(bArr);
            if (read != -1) {
                outputStream.write(bArr, 0, read);
            } else {
                return;
            }
        }
    }

    private static void bL(String str) throws g {
        bh.w(str);
        throw new g(str);
    }

    public static com.google.android.gms.internal.d.a g(com.google.android.gms.internal.d.a aVar) {
        com.google.android.gms.internal.d.a aVar2 = new com.google.android.gms.internal.d.a();
        aVar2.type = aVar.type;
        aVar2.fW = (int[]) aVar.fW.clone();
        if (aVar.fX) {
            aVar2.fX = aVar.fX;
        }
        return aVar2;
    }

    private static h h(com.google.android.gms.internal.d.a aVar) throws g {
        if (((h) aVar.a(h.fx)) == null) {
            bL("Expected a ServingValue and didn't get one. Value is: " + aVar);
        }
        return (h) aVar.a(h.fx);
    }
}