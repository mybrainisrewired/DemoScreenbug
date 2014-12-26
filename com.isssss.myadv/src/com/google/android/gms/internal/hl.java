package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.LocationRequest;
import com.millennialmedia.android.MMAdView;

public class hl implements Creator<hk> {
    static void a(hk hkVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE, hkVar.xH);
        b.a(parcel, (int)MMAdView.TRANSITION_UP, hkVar.hZ(), i, false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, hkVar.getInterval());
        b.c(parcel, MMAdView.TRANSITION_RANDOM, hkVar.getPriority());
        b.F(parcel, p);
    }

    public hk aF(Parcel parcel) {
        int o = a.o(parcel);
        int i = 0;
        hg hgVar = null;
        long j = hk.OF;
        int i2 = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_UP:
                    hgVar = (hg) a.a(parcel, n, hg.CREATOR);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    j = a.i(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    i2 = a.g(parcel, n);
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
            return new hk(i, hgVar, j, i2);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public hk[] bG(int i) {
        return new hk[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return aF(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return bG(x0);
    }
}