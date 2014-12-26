package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class gd implements SafeParcelable {
    public static final ge CREATOR;
    private final HashMap<String, HashMap<String, com.google.android.gms.internal.ga.a<?, ?>>> Ei;
    private final ArrayList<a> Ej;
    private final String Ek;
    private final int xH;

    public static class a implements SafeParcelable {
        public static final gf CREATOR;
        final ArrayList<com.google.android.gms.internal.gd.b> El;
        final String className;
        final int versionCode;

        static {
            CREATOR = new gf();
        }

        a(int i, String str, ArrayList<com.google.android.gms.internal.gd.b> arrayList) {
            this.versionCode = i;
            this.className = str;
            this.El = arrayList;
        }

        a(String str, HashMap<String, com.google.android.gms.internal.ga.a<?, ?>> hashMap) {
            this.versionCode = 1;
            this.className = str;
            this.El = b(hashMap);
        }

        private static ArrayList<com.google.android.gms.internal.gd.b> b(HashMap<String, com.google.android.gms.internal.ga.a<?, ?>> hashMap) {
            if (hashMap == null) {
                return null;
            }
            ArrayList<com.google.android.gms.internal.gd.b> arrayList = new ArrayList();
            Iterator it = hashMap.keySet().iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                arrayList.add(new com.google.android.gms.internal.gd.b(str, (com.google.android.gms.internal.ga.a) hashMap.get(str)));
            }
            return arrayList;
        }

        public int describeContents() {
            gf gfVar = CREATOR;
            return 0;
        }

        HashMap<String, com.google.android.gms.internal.ga.a<?, ?>> fp() {
            HashMap<String, com.google.android.gms.internal.ga.a<?, ?>> hashMap = new HashMap();
            int size = this.El.size();
            int i = 0;
            while (i < size) {
                com.google.android.gms.internal.gd.b bVar = (com.google.android.gms.internal.gd.b) this.El.get(i);
                hashMap.put(bVar.eM, bVar.Em);
                i++;
            }
            return hashMap;
        }

        public void writeToParcel(Parcel out, int flags) {
            gf gfVar = CREATOR;
            gf.a(this, out, flags);
        }
    }

    public static class b implements SafeParcelable {
        public static final gc CREATOR;
        final com.google.android.gms.internal.ga.a<?, ?> Em;
        final String eM;
        final int versionCode;

        static {
            CREATOR = new gc();
        }

        b(int i, String str, com.google.android.gms.internal.ga.a<?, ?> aVar) {
            this.versionCode = i;
            this.eM = str;
            this.Em = aVar;
        }

        b(String str, com.google.android.gms.internal.ga.a<?, ?> aVar) {
            this.versionCode = 1;
            this.eM = str;
            this.Em = aVar;
        }

        public int describeContents() {
            gc gcVar = CREATOR;
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            gc gcVar = CREATOR;
            gc.a(this, out, flags);
        }
    }

    static {
        CREATOR = new ge();
    }

    gd(int i, ArrayList arrayList, String str) {
        this.xH = i;
        this.Ej = null;
        this.Ei = b(arrayList);
        this.Ek = (String) fq.f(str);
        fl();
    }

    public gd(Class<? extends ga> cls) {
        this.xH = 1;
        this.Ej = null;
        this.Ei = new HashMap();
        this.Ek = cls.getCanonicalName();
    }

    private static HashMap<String, HashMap<String, com.google.android.gms.internal.ga.a<?, ?>>> b(ArrayList<a> arrayList) {
        HashMap<String, HashMap<String, com.google.android.gms.internal.ga.a<?, ?>>> hashMap = new HashMap();
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            a aVar = (a) arrayList.get(i);
            hashMap.put(aVar.className, aVar.fp());
            i++;
        }
        return hashMap;
    }

    public void a(Class<? extends ga> cls, HashMap<String, com.google.android.gms.internal.ga.a<?, ?>> hashMap) {
        this.Ei.put(cls.getCanonicalName(), hashMap);
    }

    public HashMap<String, com.google.android.gms.internal.ga.a<?, ?>> au(String str) {
        return (HashMap) this.Ei.get(str);
    }

    public boolean b(Class<? extends ga> cls) {
        return this.Ei.containsKey(cls.getCanonicalName());
    }

    public int describeContents() {
        ge geVar = CREATOR;
        return 0;
    }

    public void fl() {
        Iterator it = this.Ei.keySet().iterator();
        while (it.hasNext()) {
            HashMap hashMap = (HashMap) this.Ei.get((String) it.next());
            Iterator it2 = hashMap.keySet().iterator();
            while (it2.hasNext()) {
                ((com.google.android.gms.internal.ga.a) hashMap.get((String) it2.next())).a(this);
            }
        }
    }

    public void fm() {
        Iterator it = this.Ei.keySet().iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            HashMap hashMap = (HashMap) this.Ei.get(str);
            HashMap hashMap2 = new HashMap();
            Iterator it2 = hashMap.keySet().iterator();
            while (it2.hasNext()) {
                String str2 = (String) it2.next();
                hashMap2.put(str2, ((com.google.android.gms.internal.ga.a) hashMap.get(str2)).fb());
            }
            this.Ei.put(str, hashMap2);
        }
    }

    ArrayList<a> fn() {
        ArrayList<a> arrayList = new ArrayList();
        Iterator it = this.Ei.keySet().iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            arrayList.add(new a(str, (HashMap) this.Ei.get(str)));
        }
        return arrayList;
    }

    public String fo() {
        return this.Ek;
    }

    int getVersionCode() {
        return this.xH;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator it = this.Ei.keySet().iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            stringBuilder.append(str).append(":\n");
            HashMap hashMap = (HashMap) this.Ei.get(str);
            Iterator it2 = hashMap.keySet().iterator();
            while (it2.hasNext()) {
                String str2 = (String) it2.next();
                stringBuilder.append("  ").append(str2).append(": ");
                stringBuilder.append(hashMap.get(str2));
            }
        }
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel out, int flags) {
        ge geVar = CREATOR;
        ge.a(this, out, flags);
    }
}