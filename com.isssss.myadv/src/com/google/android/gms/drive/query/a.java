package com.google.android.gms.drive.query;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.drive.query.internal.LogicalFilter;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.millennialmedia.android.MMAdView;

public class a implements Creator<Query> {
    static void a(Query query, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE, query.xH);
        b.a(parcel, 1, query.GA, i, false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, query.GB, false);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, query.GC, i, false);
        b.F(parcel, p);
    }

    public Query[] aG(int i) {
        return new Query[i];
    }

    public Query ac(Parcel parcel) {
        SortOrder sortOrder = null;
        int o = com.google.android.gms.common.internal.safeparcel.a.o(parcel);
        int i = 0;
        String str = null;
        LogicalFilter logicalFilter = null;
        while (parcel.dataPosition() < o) {
            int i2;
            LogicalFilter logicalFilter2;
            SortOrder sortOrder2;
            String str2;
            int n = com.google.android.gms.common.internal.safeparcel.a.n(parcel);
            SortOrder sortOrder3;
            switch (com.google.android.gms.common.internal.safeparcel.a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i2 = i;
                    String str3 = str;
                    logicalFilter2 = (LogicalFilter) com.google.android.gms.common.internal.safeparcel.a.a(parcel, n, LogicalFilter.CREATOR);
                    sortOrder2 = sortOrder;
                    str2 = str3;
                    break;
                case MMAdView.TRANSITION_DOWN:
                    logicalFilter2 = logicalFilter;
                    i2 = i;
                    sortOrder3 = sortOrder;
                    str2 = com.google.android.gms.common.internal.safeparcel.a.n(parcel, n);
                    sortOrder2 = sortOrder3;
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    sortOrder2 = (SortOrder) com.google.android.gms.common.internal.safeparcel.a.a(parcel, n, SortOrder.CREATOR);
                    str2 = str;
                    logicalFilter2 = logicalFilter;
                    i2 = i;
                    break;
                case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                    sortOrder3 = sortOrder;
                    str2 = str;
                    logicalFilter2 = logicalFilter;
                    i2 = com.google.android.gms.common.internal.safeparcel.a.g(parcel, n);
                    sortOrder2 = sortOrder3;
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, n);
                    sortOrder2 = sortOrder;
                    str2 = str;
                    logicalFilter2 = logicalFilter;
                    i2 = i;
                    break;
            }
            i = i2;
            logicalFilter = logicalFilter2;
            str = str2;
            sortOrder = sortOrder2;
        }
        if (parcel.dataPosition() == o) {
            return new Query(i, logicalFilter, str, sortOrder);
        }
        throw new com.google.android.gms.common.internal.safeparcel.a.a("Overread allowed size end=" + o, parcel);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return ac(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return aG(x0);
    }
}