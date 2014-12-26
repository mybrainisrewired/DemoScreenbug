package com.google.android.gms.drive.query.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.drive.query.Filter;

public class MatchAllFilter implements SafeParcelable, Filter {
    public static final h CREATOR;
    final int xH;

    static {
        CREATOR = new h();
    }

    public MatchAllFilter() {
        this(1);
    }

    MatchAllFilter(int versionCode) {
        this.xH = versionCode;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        h.a(this, out, flags);
    }
}