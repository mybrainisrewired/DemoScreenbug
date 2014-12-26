package com.google.android.gms.drive.query.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class FieldWithSortOrder implements SafeParcelable {
    public static final c CREATOR;
    final String FM;
    final boolean GJ;
    final int xH;

    static {
        CREATOR = new c();
    }

    FieldWithSortOrder(int versionCode, String fieldName, boolean isSortAscending) {
        this.xH = versionCode;
        this.FM = fieldName;
        this.GJ = isSortAscending;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        c.a(this, out, flags);
    }
}