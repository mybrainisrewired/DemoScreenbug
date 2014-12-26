package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class hi implements SafeParcelable {
    public static final hj CREATOR;
    private final String OE;
    private final String mTag;
    final int xH;

    static {
        CREATOR = new hj();
    }

    hi(int i, String str, String str2) {
        this.xH = i;
        this.OE = str;
        this.mTag = str2;
    }

    public int describeContents() {
        hj hjVar = CREATOR;
        return 0;
    }

    public boolean equals(Object that) {
        if (!(that instanceof hi)) {
            return false;
        }
        hi that2 = (hi) that;
        return fo.equal(this.OE, that2.OE) && fo.equal(this.mTag, that2.mTag);
    }

    public String getTag() {
        return this.mTag;
    }

    public String hY() {
        return this.OE;
    }

    public int hashCode() {
        return fo.hashCode(new Object[]{this.OE, this.mTag});
    }

    public String toString() {
        return fo.e(this).a("mPlaceId", this.OE).a("mTag", this.mTag).toString();
    }

    public void writeToParcel(Parcel out, int flags) {
        hj hjVar = CREATOR;
        hj.a(this, out, flags);
    }
}