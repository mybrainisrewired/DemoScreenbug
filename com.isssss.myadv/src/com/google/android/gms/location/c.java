package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.millennialmedia.android.MMAdView;

public class c implements Creator<b> {
    static void a(b bVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, bVar.Oh);
        b.c(parcel, GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE, bVar.getVersionCode());
        b.c(parcel, MMAdView.TRANSITION_UP, bVar.Oi);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, bVar.Oj);
        b.F(parcel, p);
    }

    public b aB(Parcel parcel) {
        int i = 1;
        int o = a.o(parcel);
        int i2 = 0;
        long j = 0;
        int i3 = 1;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i3 = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    i = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    j = a.i(parcel, n);
                    break;
                case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                    i2 = a.g(parcel, n);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new b(i2, i3, i, j);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public b[] bA(int i) {
        return new b[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return aB(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return bA(x0);
    }
}