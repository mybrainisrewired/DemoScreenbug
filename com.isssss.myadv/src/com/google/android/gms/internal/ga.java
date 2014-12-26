package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class ga {

    public static interface b<I, O> {
        int eW();

        int eX();

        I g(O o);
    }

    public static class a<I, O> implements SafeParcelable {
        public static final gb CREATOR;
        protected final int DY;
        protected final boolean DZ;
        protected final int Ea;
        protected final boolean Eb;
        protected final String Ec;
        protected final int Ed;
        protected final Class<? extends ga> Ee;
        protected final String Ef;
        private gd Eg;
        private com.google.android.gms.internal.ga.b<I, O> Eh;
        private final int xH;

        static {
            CREATOR = new gb();
        }

        a(int i, int i2, boolean z, int i3, boolean z2, String str, int i4, String str2, fv fvVar) {
            this.xH = i;
            this.DY = i2;
            this.DZ = z;
            this.Ea = i3;
            this.Eb = z2;
            this.Ec = str;
            this.Ed = i4;
            if (str2 == null) {
                this.Ee = null;
                this.Ef = null;
            } else {
                this.Ee = gg.class;
                this.Ef = str2;
            }
            if (fvVar == null) {
                this.Eh = null;
            } else {
                this.Eh = fvVar.eU();
            }
        }

        protected a(int i, boolean z, int i2, boolean z2, String str, int i3, Class<? extends ga> cls, com.google.android.gms.internal.ga.b<I, O> bVar) {
            this.xH = 1;
            this.DY = i;
            this.DZ = z;
            this.Ea = i2;
            this.Eb = z2;
            this.Ec = str;
            this.Ed = i3;
            this.Ee = cls;
            if (cls == null) {
                this.Ef = null;
            } else {
                this.Ef = cls.getCanonicalName();
            }
            this.Eh = bVar;
        }

        public static com.google.android.gms.internal.ga.a a(String str, int i, com.google.android.gms.internal.ga.b<?, ?> bVar, boolean z) {
            return new com.google.android.gms.internal.ga.a(bVar.eW(), z, bVar.eX(), false, str, i, null, bVar);
        }

        public static <T extends ga> com.google.android.gms.internal.ga.a<T, T> a(String str, int i, Class<T> cls) {
            return new com.google.android.gms.internal.ga.a(11, false, 11, false, str, i, cls, null);
        }

        public static <T extends ga> com.google.android.gms.internal.ga.a<ArrayList<T>, ArrayList<T>> b(String str, int i, Class<T> cls) {
            return new com.google.android.gms.internal.ga.a(11, true, 11, true, str, i, cls, null);
        }

        public static com.google.android.gms.internal.ga.a<Integer, Integer> g(String str, int i) {
            return new com.google.android.gms.internal.ga.a(0, false, 0, false, str, i, null, null);
        }

        public static com.google.android.gms.internal.ga.a<Double, Double> h(String str, int i) {
            return new com.google.android.gms.internal.ga.a(4, false, 4, false, str, i, null, null);
        }

        public static com.google.android.gms.internal.ga.a<Boolean, Boolean> i(String str, int i) {
            return new com.google.android.gms.internal.ga.a(6, false, 6, false, str, i, null, null);
        }

        public static com.google.android.gms.internal.ga.a<String, String> j(String str, int i) {
            return new com.google.android.gms.internal.ga.a(7, false, 7, false, str, i, null, null);
        }

        public static com.google.android.gms.internal.ga.a<ArrayList<String>, ArrayList<String>> k(String str, int i) {
            return new com.google.android.gms.internal.ga.a(7, true, 7, true, str, i, null, null);
        }

        public void a(gd gdVar) {
            this.Eg = gdVar;
        }

        public int describeContents() {
            gb gbVar = CREATOR;
            return 0;
        }

        public int eW() {
            return this.DY;
        }

        public int eX() {
            return this.Ea;
        }

        public com.google.android.gms.internal.ga.a<I, O> fb() {
            return new com.google.android.gms.internal.ga.a(this.xH, this.DY, this.DZ, this.Ea, this.Eb, this.Ec, this.Ed, this.Ef, fj());
        }

        public boolean fc() {
            return this.DZ;
        }

        public boolean fd() {
            return this.Eb;
        }

        public String fe() {
            return this.Ec;
        }

        public int ff() {
            return this.Ed;
        }

        public Class<? extends ga> fg() {
            return this.Ee;
        }

        String fh() {
            return this.Ef == null ? null : this.Ef;
        }

        public boolean fi() {
            return this.Eh != null;
        }

        fv fj() {
            return this.Eh == null ? null : fv.a(this.Eh);
        }

        public HashMap<String, com.google.android.gms.internal.ga.a<?, ?>> fk() {
            fq.f(this.Ef);
            fq.f(this.Eg);
            return this.Eg.au(this.Ef);
        }

        public I g(O o) {
            return this.Eh.g(o);
        }

        public int getVersionCode() {
            return this.xH;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Field\n");
            stringBuilder.append("            versionCode=").append(this.xH).append('\n');
            stringBuilder.append("                 typeIn=").append(this.DY).append('\n');
            stringBuilder.append("            typeInArray=").append(this.DZ).append('\n');
            stringBuilder.append("                typeOut=").append(this.Ea).append('\n');
            stringBuilder.append("           typeOutArray=").append(this.Eb).append('\n');
            stringBuilder.append("        outputFieldName=").append(this.Ec).append('\n');
            stringBuilder.append("      safeParcelFieldId=").append(this.Ed).append('\n');
            stringBuilder.append("       concreteTypeName=").append(fh()).append('\n');
            if (fg() != null) {
                stringBuilder.append("     concreteType.class=").append(fg().getCanonicalName()).append('\n');
            }
            stringBuilder.append("          converterName=").append(this.Eh == null ? "null" : this.Eh.getClass().getCanonicalName()).append('\n');
            return stringBuilder.toString();
        }

        public void writeToParcel(Parcel out, int flags) {
            gb gbVar = CREATOR;
            gb.a(this, out, flags);
        }
    }

    private void a(StringBuilder stringBuilder, a aVar, Object obj) {
        if (aVar.eW() == 11) {
            stringBuilder.append(((ga) aVar.fg().cast(obj)).toString());
        } else if (aVar.eW() == 7) {
            stringBuilder.append("\"");
            stringBuilder.append(gp.av((String) obj));
            stringBuilder.append("\"");
        } else {
            stringBuilder.append(obj);
        }
    }

    private void a(StringBuilder stringBuilder, a aVar, ArrayList<Object> arrayList) {
        stringBuilder.append("[");
        int i = 0;
        int size = arrayList.size();
        while (i < size) {
            if (i > 0) {
                stringBuilder.append(",");
            }
            Object obj = arrayList.get(i);
            if (obj != null) {
                a(stringBuilder, aVar, obj);
            }
            i++;
        }
        stringBuilder.append("]");
    }

    protected <O, I> I a(a<I, O> aVar, Object obj) {
        return aVar.Eh != null ? aVar.g(obj) : obj;
    }

    protected boolean a(a aVar) {
        return aVar.eX() == 11 ? aVar.fd() ? at(aVar.fe()) : as(aVar.fe()) : ar(aVar.fe());
    }

    protected abstract Object aq(String str);

    protected abstract boolean ar(String str);

    protected boolean as(String str) {
        throw new UnsupportedOperationException("Concrete types not supported");
    }

    protected boolean at(String str) {
        throw new UnsupportedOperationException("Concrete type arrays not supported");
    }

    protected Object b(a aVar) {
        boolean z = true;
        String fe = aVar.fe();
        if (aVar.fg() == null) {
            return aq(aVar.fe());
        }
        if (aq(aVar.fe()) != null) {
            z = false;
        }
        fq.a(z, "Concrete field shouldn't be value object: " + aVar.fe());
        Map fa = aVar.fd() ? fa() : eZ();
        if (fa != null) {
            return fa.get(fe);
        }
        try {
            return getClass().getMethod("get" + Character.toUpperCase(fe.charAt(0)) + fe.substring(1), new Class[0]).invoke(this, new Object[0]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public abstract HashMap<String, a<?, ?>> eY();

    public HashMap<String, Object> eZ() {
        return null;
    }

    public HashMap<String, Object> fa() {
        return null;
    }

    public String toString() {
        HashMap eY = eY();
        StringBuilder stringBuilder = new StringBuilder(100);
        Iterator it = eY.keySet().iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            a aVar = (a) eY.get(str);
            if (a(aVar)) {
                Object a = a(aVar, b(aVar));
                if (stringBuilder.length() == 0) {
                    stringBuilder.append("{");
                } else {
                    stringBuilder.append(",");
                }
                stringBuilder.append("\"").append(str).append("\":");
                if (a == null) {
                    stringBuilder.append("null");
                } else {
                    switch (aVar.eX()) {
                        case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                            stringBuilder.append("\"").append(gj.d((byte[]) a)).append("\"");
                            break;
                        case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                            stringBuilder.append("\"").append(gj.e((byte[]) a)).append("\"");
                            break;
                        case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                            gq.a(stringBuilder, (HashMap) a);
                            break;
                        default:
                            if (aVar.fc()) {
                                a(stringBuilder, aVar, (ArrayList) a);
                            } else {
                                a(stringBuilder, aVar, a);
                            }
                            break;
                    }
                }
            }
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.append("}");
        } else {
            stringBuilder.append("{}");
        }
        return stringBuilder.toString();
    }
}