package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.util.List;

public class hp implements Creator<ho> {
    static void a(ho hoVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.a(parcel, 1, hoVar.getName(), false);
        b.c(parcel, GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE, hoVar.xH);
        b.a(parcel, (int)MMAdView.TRANSITION_UP, hoVar.ia(), i, false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, hoVar.getAddress(), false);
        b.b(parcel, MMAdView.TRANSITION_RANDOM, hoVar.ib(), false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, hoVar.getPhoneNumber(), false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, hoVar.ic(), false);
        b.F(parcel, p);
    }

    public ho aH(Parcel parcel) {
        String str = null;
        int o = a.o(parcel);
        int i = 0;
        String str2 = null;
        List list = null;
        String str3 = null;
        LatLng latLng = null;
        String str4 = null;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    str4 = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    latLng = (LatLng) a.a(parcel, n, LatLng.CREATOR);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    str3 = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    list = a.c(parcel, n, hm.CREATOR);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    str2 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
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
            return new ho(i, str4, latLng, str3, list, str2, str);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public ho[] bI(int i) {
        return new ho[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return aH(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return bI(x0);
    }
}