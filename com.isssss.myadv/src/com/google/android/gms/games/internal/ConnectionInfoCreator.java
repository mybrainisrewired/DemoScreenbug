package com.google.android.gms.games.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.millennialmedia.android.MMAdView;

public class ConnectionInfoCreator implements Creator<ConnectionInfo> {
    static void a(ConnectionInfo connectionInfo, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.a(parcel, 1, connectionInfo.gi(), false);
        b.c(parcel, GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE, connectionInfo.getVersionCode());
        b.c(parcel, MMAdView.TRANSITION_UP, connectionInfo.gj());
        b.F(parcel, p);
    }

    public ConnectionInfo[] aW(int i) {
        return new ConnectionInfo[i];
    }

    public ConnectionInfo ap(Parcel parcel) {
        int i = 0;
        int o = a.o(parcel);
        String str = null;
        int i2 = 0;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    str = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    i = a.g(parcel, n);
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
            return new ConnectionInfo(i2, str, i);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return ap(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return aW(x0);
    }
}