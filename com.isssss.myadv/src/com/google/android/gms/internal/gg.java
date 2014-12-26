package com.google.android.gms.internal;

import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.internal.ga.a;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class gg extends ga implements SafeParcelable {
    public static final gh CREATOR;
    private final gd Eg;
    private final Parcel En;
    private final int Eo;
    private int Ep;
    private int Eq;
    private final String mClassName;
    private final int xH;

    static {
        CREATOR = new gh();
    }

    gg(int i, Parcel parcel, gd gdVar) {
        this.xH = i;
        this.En = (Parcel) fq.f(parcel);
        this.Eo = 2;
        this.Eg = gdVar;
        if (this.Eg == null) {
            this.mClassName = null;
        } else {
            this.mClassName = this.Eg.fo();
        }
        this.Ep = 2;
    }

    private gg(SafeParcelable safeParcelable, gd gdVar, String str) {
        this.xH = 1;
        this.En = Parcel.obtain();
        safeParcelable.writeToParcel(this.En, 0);
        this.Eo = 1;
        this.Eg = (gd) fq.f(gdVar);
        this.mClassName = (String) fq.f(str);
        this.Ep = 2;
    }

    public static <T extends ga & SafeParcelable> gg a(T t) {
        String canonicalName = t.getClass().getCanonicalName();
        return new gg((SafeParcelable) t, b(t), canonicalName);
    }

    private static void a(gd gdVar, ga gaVar) {
        Class cls = gaVar.getClass();
        if (!gdVar.b(cls)) {
            HashMap eY = gaVar.eY();
            gdVar.a(cls, gaVar.eY());
            Iterator it = eY.keySet().iterator();
            while (it.hasNext()) {
                a aVar = (a) eY.get((String) it.next());
                Class fg = aVar.fg();
                if (fg != null) {
                    try {
                        a(gdVar, (ga) fg.newInstance());
                    } catch (InstantiationException e) {
                        throw new IllegalStateException("Could not instantiate an object of type " + aVar.fg().getCanonicalName(), e);
                    } catch (IllegalAccessException e2) {
                        throw new IllegalStateException("Could not access object of type " + aVar.fg().getCanonicalName(), e2);
                    }
                }
            }
        }
    }

    private void a(StringBuilder stringBuilder, int i, Object obj) {
        switch (i) {
            case MMAdView.TRANSITION_NONE:
            case MMAdView.TRANSITION_FADE:
            case MMAdView.TRANSITION_UP:
            case MMAdView.TRANSITION_DOWN:
            case MMAdView.TRANSITION_RANDOM:
            case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
            case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                stringBuilder.append(obj);
            case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                stringBuilder.append("\"").append(gp.av(obj.toString())).append("\"");
            case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                stringBuilder.append("\"").append(gj.d((byte[]) obj)).append("\"");
            case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                stringBuilder.append("\"").append(gj.e((byte[]) obj));
                stringBuilder.append("\"");
            case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                gq.a(stringBuilder, (HashMap) obj);
            case ApiEventType.API_MRAID_EXPAND:
                throw new IllegalArgumentException("Method does not accept concrete type.");
            default:
                throw new IllegalArgumentException("Unknown type = " + i);
        }
    }

    private void a(StringBuilder stringBuilder, a aVar, Parcel parcel, int i) {
        switch (aVar.eX()) {
            case MMAdView.TRANSITION_NONE:
                b(stringBuilder, aVar, a(aVar, Integer.valueOf(com.google.android.gms.common.internal.safeparcel.a.g(parcel, i))));
            case MMAdView.TRANSITION_FADE:
                b(stringBuilder, aVar, a(aVar, com.google.android.gms.common.internal.safeparcel.a.j(parcel, i)));
            case MMAdView.TRANSITION_UP:
                b(stringBuilder, aVar, a(aVar, Long.valueOf(com.google.android.gms.common.internal.safeparcel.a.i(parcel, i))));
            case MMAdView.TRANSITION_DOWN:
                b(stringBuilder, aVar, a(aVar, Float.valueOf(com.google.android.gms.common.internal.safeparcel.a.k(parcel, i))));
            case MMAdView.TRANSITION_RANDOM:
                b(stringBuilder, aVar, a(aVar, Double.valueOf(com.google.android.gms.common.internal.safeparcel.a.l(parcel, i))));
            case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                b(stringBuilder, aVar, a(aVar, com.google.android.gms.common.internal.safeparcel.a.m(parcel, i)));
            case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                b(stringBuilder, aVar, a(aVar, Boolean.valueOf(com.google.android.gms.common.internal.safeparcel.a.c(parcel, i))));
            case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                b(stringBuilder, aVar, a(aVar, com.google.android.gms.common.internal.safeparcel.a.n(parcel, i)));
            case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
            case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                b(stringBuilder, aVar, a(aVar, com.google.android.gms.common.internal.safeparcel.a.q(parcel, i)));
            case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                b(stringBuilder, aVar, a(aVar, c(com.google.android.gms.common.internal.safeparcel.a.p(parcel, i))));
            case ApiEventType.API_MRAID_EXPAND:
                throw new IllegalArgumentException("Method does not accept concrete type.");
            default:
                throw new IllegalArgumentException("Unknown field out type = " + aVar.eX());
        }
    }

    private void a(StringBuilder stringBuilder, String str, a<?, ?> aVar, Parcel parcel, int i) {
        stringBuilder.append("\"").append(str).append("\":");
        if (aVar.fi()) {
            a(stringBuilder, aVar, parcel, i);
        } else {
            b(stringBuilder, aVar, parcel, i);
        }
    }

    private void a(StringBuilder stringBuilder, HashMap hashMap, Parcel parcel) {
        HashMap c = c(hashMap);
        stringBuilder.append('{');
        int o = com.google.android.gms.common.internal.safeparcel.a.o(parcel);
        int i = 0;
        while (parcel.dataPosition() < o) {
            int n = com.google.android.gms.common.internal.safeparcel.a.n(parcel);
            Entry entry = (Entry) c.get(Integer.valueOf(com.google.android.gms.common.internal.safeparcel.a.R(n)));
            if (entry != null) {
                if (i != 0) {
                    stringBuilder.append(",");
                }
                a(stringBuilder, (String) entry.getKey(), (a) entry.getValue(), parcel, n);
                i = 1;
            }
        }
        if (parcel.dataPosition() != o) {
            throw new com.google.android.gms.common.internal.safeparcel.a.a("Overread allowed size end=" + o, parcel);
        }
        stringBuilder.append('}');
    }

    private static gd b(ga gaVar) {
        gd gdVar = new gd(gaVar.getClass());
        a(gdVar, gaVar);
        gdVar.fm();
        gdVar.fl();
        return gdVar;
    }

    private void b(StringBuilder stringBuilder, a<?, ?> aVar, Parcel parcel, int i) {
        if (aVar.fd()) {
            stringBuilder.append("[");
            switch (aVar.eX()) {
                case MMAdView.TRANSITION_NONE:
                    gi.a(stringBuilder, com.google.android.gms.common.internal.safeparcel.a.t(parcel, i));
                    break;
                case MMAdView.TRANSITION_FADE:
                    gi.a(stringBuilder, com.google.android.gms.common.internal.safeparcel.a.v(parcel, i));
                    break;
                case MMAdView.TRANSITION_UP:
                    gi.a(stringBuilder, com.google.android.gms.common.internal.safeparcel.a.u(parcel, i));
                    break;
                case MMAdView.TRANSITION_DOWN:
                    gi.a(stringBuilder, com.google.android.gms.common.internal.safeparcel.a.w(parcel, i));
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    gi.a(stringBuilder, com.google.android.gms.common.internal.safeparcel.a.x(parcel, i));
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    gi.a(stringBuilder, com.google.android.gms.common.internal.safeparcel.a.y(parcel, i));
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    gi.a(stringBuilder, com.google.android.gms.common.internal.safeparcel.a.s(parcel, i));
                    break;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    gi.a(stringBuilder, com.google.android.gms.common.internal.safeparcel.a.z(parcel, i));
                    break;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                    throw new UnsupportedOperationException("List of type BASE64, BASE64_URL_SAFE, or STRING_MAP is not supported");
                case ApiEventType.API_MRAID_EXPAND:
                    Parcel[] C = com.google.android.gms.common.internal.safeparcel.a.C(parcel, i);
                    int length = C.length;
                    int i2 = 0;
                    while (i2 < length) {
                        if (i2 > 0) {
                            stringBuilder.append(",");
                        }
                        C[i2].setDataPosition(0);
                        a(stringBuilder, aVar.fk(), C[i2]);
                        i2++;
                    }
                    break;
                default:
                    throw new IllegalStateException("Unknown field type out.");
            }
            stringBuilder.append("]");
        } else {
            switch (aVar.eX()) {
                case MMAdView.TRANSITION_NONE:
                    stringBuilder.append(com.google.android.gms.common.internal.safeparcel.a.g(parcel, i));
                case MMAdView.TRANSITION_FADE:
                    stringBuilder.append(com.google.android.gms.common.internal.safeparcel.a.j(parcel, i));
                case MMAdView.TRANSITION_UP:
                    stringBuilder.append(com.google.android.gms.common.internal.safeparcel.a.i(parcel, i));
                case MMAdView.TRANSITION_DOWN:
                    stringBuilder.append(com.google.android.gms.common.internal.safeparcel.a.k(parcel, i));
                case MMAdView.TRANSITION_RANDOM:
                    stringBuilder.append(com.google.android.gms.common.internal.safeparcel.a.l(parcel, i));
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    stringBuilder.append(com.google.android.gms.common.internal.safeparcel.a.m(parcel, i));
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    stringBuilder.append(com.google.android.gms.common.internal.safeparcel.a.c(parcel, i));
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    stringBuilder.append("\"").append(gp.av(com.google.android.gms.common.internal.safeparcel.a.n(parcel, i))).append("\"");
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    stringBuilder.append("\"").append(gj.d(com.google.android.gms.common.internal.safeparcel.a.q(parcel, i))).append("\"");
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    stringBuilder.append("\"").append(gj.e(com.google.android.gms.common.internal.safeparcel.a.q(parcel, i)));
                    stringBuilder.append("\"");
                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                    Bundle p = com.google.android.gms.common.internal.safeparcel.a.p(parcel, i);
                    Set keySet = p.keySet();
                    keySet.size();
                    stringBuilder.append("{");
                    Iterator it = keySet.iterator();
                    int i3 = 1;
                    while (it.hasNext()) {
                        String str = (String) it.next();
                        if (i3 == 0) {
                            stringBuilder.append(",");
                        }
                        stringBuilder.append("\"").append(str).append("\"");
                        stringBuilder.append(":");
                        stringBuilder.append("\"").append(gp.av(p.getString(str))).append("\"");
                        boolean z = false;
                    }
                    stringBuilder.append("}");
                case ApiEventType.API_MRAID_EXPAND:
                    Parcel B = com.google.android.gms.common.internal.safeparcel.a.B(parcel, i);
                    B.setDataPosition(0);
                    a(stringBuilder, aVar.fk(), B);
                default:
                    throw new IllegalStateException("Unknown field type out");
            }
        }
    }

    private void b(StringBuilder stringBuilder, a aVar, Object obj) {
        if (aVar.fc()) {
            b(stringBuilder, aVar, (ArrayList) obj);
        } else {
            a(stringBuilder, aVar.eW(), obj);
        }
    }

    private void b(StringBuilder stringBuilder, a<?, ?> aVar, ArrayList<?> arrayList) {
        stringBuilder.append("[");
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            if (i != 0) {
                stringBuilder.append(",");
            }
            a(stringBuilder, aVar.eW(), arrayList.get(i));
            i++;
        }
        stringBuilder.append("]");
    }

    public static HashMap<String, String> c(Bundle bundle) {
        HashMap<String, String> hashMap = new HashMap();
        Iterator it = bundle.keySet().iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            hashMap.put(str, bundle.getString(str));
        }
        return hashMap;
    }

    private static HashMap<Integer, Entry<String, a<?, ?>>> c(HashMap<String, a<?, ?>> hashMap) {
        HashMap<Integer, Entry<String, a<?, ?>>> hashMap2 = new HashMap();
        Iterator it = hashMap.entrySet().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            hashMap2.put(Integer.valueOf(((a) entry.getValue()).ff()), entry);
        }
        return hashMap2;
    }

    protected Object aq(String str) {
        throw new UnsupportedOperationException("Converting to JSON does not require this method.");
    }

    protected boolean ar(String str) {
        throw new UnsupportedOperationException("Converting to JSON does not require this method.");
    }

    public int describeContents() {
        gh ghVar = CREATOR;
        return 0;
    }

    public HashMap<String, a<?, ?>> eY() {
        return this.Eg == null ? null : this.Eg.au(this.mClassName);
    }

    public Parcel fq() {
        switch (this.Ep) {
            case MMAdView.TRANSITION_NONE:
                this.Eq = b.p(this.En);
                b.F(this.En, this.Eq);
                this.Ep = 2;
                break;
            case MMAdView.TRANSITION_FADE:
                b.F(this.En, this.Eq);
                this.Ep = 2;
                break;
        }
        return this.En;
    }

    gd fr() {
        switch (this.Eo) {
            case MMAdView.TRANSITION_NONE:
                return null;
            case MMAdView.TRANSITION_FADE:
                return this.Eg;
            case MMAdView.TRANSITION_UP:
                return this.Eg;
            default:
                throw new IllegalStateException("Invalid creation type: " + this.Eo);
        }
    }

    public int getVersionCode() {
        return this.xH;
    }

    public String toString() {
        fq.b(this.Eg, (Object)"Cannot convert to JSON on client side.");
        Parcel fq = fq();
        fq.setDataPosition(0);
        StringBuilder stringBuilder = new StringBuilder(100);
        a(stringBuilder, this.Eg.au(this.mClassName), fq);
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel out, int flags) {
        gh ghVar = CREATOR;
        gh.a(this, out, flags);
    }
}