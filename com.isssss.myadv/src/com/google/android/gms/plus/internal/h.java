package com.google.android.gms.plus.internal;

import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.fo;
import java.util.Arrays;

public class h implements SafeParcelable {
    public static final j CREATOR;
    private final String[] Uk;
    private final String[] Ul;
    private final String[] Um;
    private final String Un;
    private final String Uo;
    private final String Up;
    private final String Uq;
    private final PlusCommonExtras Ur;
    private final String wG;
    private final int xH;

    static {
        CREATOR = new j();
    }

    h(int i, String str, String[] strArr, String[] strArr2, String[] strArr3, String str2, String str3, String str4, String str5, PlusCommonExtras plusCommonExtras) {
        this.xH = i;
        this.wG = str;
        this.Uk = strArr;
        this.Ul = strArr2;
        this.Um = strArr3;
        this.Un = str2;
        this.Uo = str3;
        this.Up = str4;
        this.Uq = str5;
        this.Ur = plusCommonExtras;
    }

    public h(String str, String[] strArr, String[] strArr2, String[] strArr3, String str2, String str3, String str4, PlusCommonExtras plusCommonExtras) {
        this.xH = 1;
        this.wG = str;
        this.Uk = strArr;
        this.Ul = strArr2;
        this.Um = strArr3;
        this.Un = str2;
        this.Uo = str3;
        this.Up = str4;
        this.Uq = null;
        this.Ur = plusCommonExtras;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof h)) {
            return false;
        }
        h obj2 = (h) obj;
        return this.xH == obj2.xH && fo.equal(this.wG, obj2.wG) && Arrays.equals(this.Uk, obj2.Uk) && Arrays.equals(this.Ul, obj2.Ul) && Arrays.equals(this.Um, obj2.Um) && fo.equal(this.Un, obj2.Un) && fo.equal(this.Uo, obj2.Uo) && fo.equal(this.Up, obj2.Up) && fo.equal(this.Uq, obj2.Uq) && fo.equal(this.Ur, obj2.Ur);
    }

    public String getAccountName() {
        return this.wG;
    }

    public int getVersionCode() {
        return this.xH;
    }

    public int hashCode() {
        return fo.hashCode(new Object[]{Integer.valueOf(this.xH), this.wG, this.Uk, this.Ul, this.Um, this.Un, this.Uo, this.Up, this.Uq, this.Ur});
    }

    public String[] iP() {
        return this.Uk;
    }

    public String[] iQ() {
        return this.Ul;
    }

    public String[] iR() {
        return this.Um;
    }

    public String iS() {
        return this.Un;
    }

    public String iT() {
        return this.Uo;
    }

    public String iU() {
        return this.Up;
    }

    public String iV() {
        return this.Uq;
    }

    public PlusCommonExtras iW() {
        return this.Ur;
    }

    public Bundle iX() {
        Bundle bundle = new Bundle();
        bundle.setClassLoader(PlusCommonExtras.class.getClassLoader());
        this.Ur.m(bundle);
        return bundle;
    }

    public String toString() {
        return fo.e(this).a("versionCode", Integer.valueOf(this.xH)).a("accountName", this.wG).a("requestedScopes", this.Uk).a("visibleActivities", this.Ul).a("requiredFeatures", this.Um).a("packageNameForAuth", this.Un).a("callingPackageName", this.Uo).a("applicationName", this.Up).a("extra", this.Ur.toString()).toString();
    }

    public void writeToParcel(Parcel out, int flags) {
        j.a(this, out, flags);
    }
}