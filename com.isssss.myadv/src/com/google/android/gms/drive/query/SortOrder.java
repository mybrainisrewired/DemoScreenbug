package com.google.android.gms.drive.query;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.drive.query.internal.FieldWithSortOrder;
import java.util.List;

public class SortOrder implements SafeParcelable {
    public static final Creator<SortOrder> CREATOR;
    final List<FieldWithSortOrder> GF;
    final int xH;

    static {
        CREATOR = new b();
    }

    SortOrder(int versionCode, List<FieldWithSortOrder> sortingFields) {
        this.xH = versionCode;
        this.GF = sortingFields;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        b.a(this, out, flags);
    }
}