package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.millennialmedia.android.MMAdView;
import java.util.List;

public class hh implements Creator<hg> {
    static void a(hg hgVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.b(parcel, 1, hgVar.OA, false);
        b.c(parcel, GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE, hgVar.xH);
        b.a(parcel, (int)MMAdView.TRANSITION_UP, hgVar.hW(), false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, hgVar.hX());
        b.F(parcel, p);
    }

    public hg aD(Parcel parcel) {
        String str = null;
        boolean z = false;
        int o = a.o(parcel);
        List list = null;
        int i = 0;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    list = a.c(parcel, n, hm.CREATOR);
                    break;
                case MMAdView.TRANSITION_UP:
                    str = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
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
            return new hg(i, list, str, z);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public hg[] bE(int i) {
        return new hg[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return aD(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return bE(x0);
    }
}