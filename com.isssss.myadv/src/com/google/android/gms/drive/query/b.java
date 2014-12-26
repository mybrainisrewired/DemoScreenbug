package com.google.android.gms.drive.query;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.drive.query.internal.FieldWithSortOrder;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.millennialmedia.android.MMAdView;
import java.util.List;

public class b implements Creator<SortOrder> {
    static void a(SortOrder sortOrder, Parcel parcel, int i) {
        int p = com.google.android.gms.common.internal.safeparcel.b.p(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE, sortOrder.xH);
        com.google.android.gms.common.internal.safeparcel.b.b(parcel, 1, sortOrder.GF, false);
        com.google.android.gms.common.internal.safeparcel.b.F(parcel, p);
    }

    public SortOrder[] aH(int i) {
        return new SortOrder[i];
    }

    public SortOrder ad(Parcel parcel) {
        int o = a.o(parcel);
        int i = 0;
        List list = null;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    list = a.c(parcel, n, FieldWithSortOrder.CREATOR);
                    break;
                case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                    i = a.g(parcel, n);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new SortOrder(i, list);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return ad(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return aH(x0);
    }
}