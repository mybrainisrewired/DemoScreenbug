package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.internal.fc.a;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.millennialmedia.android.MMAdView;
import java.util.List;

public class fp implements Creator<a> {
    static void a(a aVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.a(parcel, 1, aVar.getAccountName(), false);
        b.c(parcel, GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE, aVar.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, aVar.eE(), false);
        b.c(parcel, MMAdView.TRANSITION_DOWN, aVar.eD());
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, aVar.eG(), false);
        b.F(parcel, p);
    }

    public a[] Q(int i) {
        return new a[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return m(x0);
    }

    public a m(Parcel parcel) {
        int i = 0;
        String str = null;
        int o = com.google.android.gms.common.internal.safeparcel.a.o(parcel);
        List list = null;
        String str2 = null;
        int i2 = 0;
        while (parcel.dataPosition() < o) {
            int n = com.google.android.gms.common.internal.safeparcel.a.n(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    str2 = com.google.android.gms.common.internal.safeparcel.a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    list = com.google.android.gms.common.internal.safeparcel.a.A(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    i = com.google.android.gms.common.internal.safeparcel.a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    str = com.google.android.gms.common.internal.safeparcel.a.n(parcel, n);
                    break;
                case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                    i2 = com.google.android.gms.common.internal.safeparcel.a.g(parcel, n);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new a(i2, str2, list, i, str);
        }
        throw new com.google.android.gms.common.internal.safeparcel.a.a("Overread allowed size end=" + o, parcel);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return Q(x0);
    }
}