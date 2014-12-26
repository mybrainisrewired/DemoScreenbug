package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.ga.b;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public final class fx implements SafeParcelable, b<String, Integer> {
    public static final fy CREATOR;
    private final HashMap<String, Integer> DT;
    private final HashMap<Integer, String> DU;
    private final ArrayList<a> DV;
    private final int xH;

    public static final class a implements SafeParcelable {
        public static final fz CREATOR;
        final String DW;
        final int DX;
        final int versionCode;

        static {
            CREATOR = new fz();
        }

        a(int i, String str, int i2) {
            this.versionCode = i;
            this.DW = str;
            this.DX = i2;
        }

        a(String str, int i) {
            this.versionCode = 1;
            this.DW = str;
            this.DX = i;
        }

        public int describeContents() {
            fz fzVar = CREATOR;
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            fz fzVar = CREATOR;
            fz.a(this, out, flags);
        }
    }

    static {
        CREATOR = new fy();
    }

    public fx() {
        this.xH = 1;
        this.DT = new HashMap();
        this.DU = new HashMap();
        this.DV = null;
    }

    fx(int i, ArrayList arrayList) {
        this.xH = i;
        this.DT = new HashMap();
        this.DU = new HashMap();
        this.DV = null;
        a(arrayList);
    }

    private void a(ArrayList<a> arrayList) {
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            a aVar = (a) it.next();
            f(aVar.DW, aVar.DX);
        }
    }

    public String a(Integer num) {
        String str = (String) this.DU.get(num);
        return (str == null && this.DT.containsKey("gms_unknown")) ? "gms_unknown" : str;
    }

    public int describeContents() {
        fy fyVar = CREATOR;
        return 0;
    }

    ArrayList<a> eV() {
        ArrayList<a> arrayList = new ArrayList();
        Iterator it = this.DT.keySet().iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            arrayList.add(new a(str, ((Integer) this.DT.get(str)).intValue()));
        }
        return arrayList;
    }

    public int eW() {
        return ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES;
    }

    public int eX() {
        return 0;
    }

    public fx f(String str, int i) {
        this.DT.put(str, Integer.valueOf(i));
        this.DU.put(Integer.valueOf(i), str);
        return this;
    }

    public /* synthetic */ Object g(Object obj) {
        return a((Integer) obj);
    }

    int getVersionCode() {
        return this.xH;
    }

    public void writeToParcel(Parcel out, int flags) {
        fy fyVar = CREATOR;
        fy.a(this, out, flags);
    }
}