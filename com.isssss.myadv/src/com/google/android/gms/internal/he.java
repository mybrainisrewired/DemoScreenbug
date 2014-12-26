package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class he implements Creator<hd> {
    static void a(hd hdVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.a(parcel, 1, hdVar.getRequestId(), false);
        b.c(parcel, GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE, hdVar.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, hdVar.getExpirationTime());
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, hdVar.hS());
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, hdVar.getLatitude());
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, hdVar.getLongitude());
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, hdVar.hT());
        b.c(parcel, ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, hdVar.hU());
        b.c(parcel, ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, hdVar.getNotificationResponsiveness());
        b.c(parcel, ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, hdVar.hV());
        b.F(parcel, p);
    }

    public hd aC(Parcel parcel) {
        int o = a.o(parcel);
        int i = 0;
        String str = null;
        int i2 = 0;
        short s = (short) 0;
        double d = 0.0d;
        double d2 = 0.0d;
        float f = BitmapDescriptorFactory.HUE_RED;
        long j = 0;
        int i3 = 0;
        int i4 = -1;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    str = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    j = a.i(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    s = a.f(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    d = a.l(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    d2 = a.l(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    f = a.k(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    i2 = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    i3 = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    i4 = a.g(parcel, n);
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
            return new hd(i, str, i2, s, d, d2, f, j, i3, i4);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public hd[] bD(int i) {
        return new hd[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return aC(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return bD(x0);
    }
}