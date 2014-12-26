package com.google.android.gms.drive.query.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.millennialmedia.android.MMAdView;

public class c implements Creator<FieldWithSortOrder> {
    static void a(FieldWithSortOrder fieldWithSortOrder, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE, fieldWithSortOrder.xH);
        b.a(parcel, 1, fieldWithSortOrder.FM, false);
        b.a(parcel, (int)MMAdView.TRANSITION_UP, fieldWithSortOrder.GJ);
        b.F(parcel, p);
    }

    public FieldWithSortOrder[] aK(int i) {
        return new FieldWithSortOrder[i];
    }

    public FieldWithSortOrder ag(Parcel parcel) {
        boolean z = false;
        int o = a.o(parcel);
        String str = null;
        int i = 0;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    str = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    z = a.c(parcel, n);
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
            return new FieldWithSortOrder(i, str, z);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return ag(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return aK(x0);
    }
}