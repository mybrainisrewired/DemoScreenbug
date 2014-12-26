package com.google.android.gms.plus.internal;

import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.safeparcel.c;
import com.google.android.gms.internal.fo;
import com.mopub.common.Preconditions;

public class PlusCommonExtras implements SafeParcelable {
    public static final f CREATOR;
    public static String TAG;
    private String Uh;
    private String Ui;
    private final int xH;

    static {
        TAG = "PlusCommonExtras";
        CREATOR = new f();
    }

    public PlusCommonExtras() {
        this.xH = 1;
        this.Uh = Preconditions.EMPTY_ARGUMENTS;
        this.Ui = Preconditions.EMPTY_ARGUMENTS;
    }

    PlusCommonExtras(int versionCode, String gpsrc, String clientCallingPackage) {
        this.xH = versionCode;
        this.Uh = gpsrc;
        this.Ui = clientCallingPackage;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof PlusCommonExtras)) {
            return false;
        }
        PlusCommonExtras obj2 = (PlusCommonExtras) obj;
        return this.xH == obj2.xH && fo.equal(this.Uh, obj2.Uh) && fo.equal(this.Ui, obj2.Ui);
    }

    public int getVersionCode() {
        return this.xH;
    }

    public int hashCode() {
        return fo.hashCode(new Object[]{Integer.valueOf(this.xH), this.Uh, this.Ui});
    }

    public String iN() {
        return this.Uh;
    }

    public String iO() {
        return this.Ui;
    }

    public void m(Bundle bundle) {
        bundle.putByteArray("android.gms.plus.internal.PlusCommonExtras.extraPlusCommon", c.a(this));
    }

    public String toString() {
        return fo.e(this).a("versionCode", Integer.valueOf(this.xH)).a("Gpsrc", this.Uh).a("ClientCallingPackage", this.Ui).toString();
    }

    public void writeToParcel(Parcel out, int flags) {
        f.a(this, out, flags);
    }
}