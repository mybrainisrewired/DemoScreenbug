package com.google.android.gms.plus.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.millennialmedia.android.MMAdView;

public class f implements Creator<PlusCommonExtras> {
    static void a(PlusCommonExtras plusCommonExtras, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.a(parcel, 1, plusCommonExtras.iN(), false);
        b.c(parcel, GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE, plusCommonExtras.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, plusCommonExtras.iO(), false);
        b.F(parcel, p);
    }

    public PlusCommonExtras aJ(Parcel parcel) {
        String str = null;
        int o = a.o(parcel);
        int i = 0;
        String str2 = null;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    str2 = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    str = a.n(parcel, n);
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
            return new PlusCommonExtras(i, str2, str);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public PlusCommonExtras[] bM(int i) {
        return new PlusCommonExtras[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return aJ(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return bM(x0);
    }
}