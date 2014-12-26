package com.google.android.gms.drive.query.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.inmobi.commons.analytics.iat.impl.AdTrackerConstants;

public class Operator implements SafeParcelable {
    public static final Creator<Operator> CREATOR;
    public static final Operator GU;
    public static final Operator GV;
    public static final Operator GW;
    public static final Operator GX;
    public static final Operator GY;
    public static final Operator GZ;
    public static final Operator Ha;
    public static final Operator Hb;
    public static final Operator Hc;
    final String mTag;
    final int xH;

    static {
        CREATOR = new j();
        GU = new Operator("=");
        GV = new Operator("<");
        GW = new Operator("<=");
        GX = new Operator(">");
        GY = new Operator(">=");
        GZ = new Operator(AdTrackerConstants.ANDROID);
        Ha = new Operator("or");
        Hb = new Operator("not");
        Hc = new Operator("contains");
    }

    Operator(int versionCode, String tag) {
        this.xH = versionCode;
        this.mTag = tag;
    }

    private Operator(String tag) {
        this(1, tag);
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Operator obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        obj = obj;
        return this.mTag == null ? obj.mTag == null : this.mTag.equals(obj.mTag);
    }

    public int hashCode() {
        return (this.mTag == null ? 0 : this.mTag.hashCode()) + 31;
    }

    public void writeToParcel(Parcel out, int flags) {
        j.a(this, out, flags);
    }
}