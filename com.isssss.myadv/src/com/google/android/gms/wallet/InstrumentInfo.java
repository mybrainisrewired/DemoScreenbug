package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class InstrumentInfo implements SafeParcelable {
    public static final Creator<InstrumentInfo> CREATOR;
    private String abt;
    private String abu;
    private final int xH;

    static {
        CREATOR = new h();
    }

    InstrumentInfo(int versionCode, String instrumentType, String instrumentDetails) {
        this.xH = versionCode;
        this.abt = instrumentType;
        this.abu = instrumentDetails;
    }

    public int describeContents() {
        return 0;
    }

    public String getInstrumentDetails() {
        return this.abu;
    }

    public String getInstrumentType() {
        return this.abt;
    }

    public int getVersionCode() {
        return this.xH;
    }

    public void writeToParcel(Parcel out, int flags) {
        h.a(this, out, flags);
    }
}