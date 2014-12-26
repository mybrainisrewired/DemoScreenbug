package com.google.android.gms.plus.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class j implements Creator<h> {
    static void a(h hVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.a(parcel, 1, hVar.getAccountName(), false);
        b.c(parcel, GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE, hVar.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, hVar.iP(), false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, hVar.iQ(), false);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, hVar.iR(), false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, hVar.iS(), false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, hVar.iT(), false);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, hVar.iU(), false);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, hVar.iV(), false);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, hVar.iW(), i, false);
        b.F(parcel, p);
    }

    public h aK(Parcel parcel) {
        PlusCommonExtras plusCommonExtras = null;
        int o = a.o(parcel);
        int i = 0;
        String str = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String[] strArr = null;
        String[] strArr2 = null;
        String[] strArr3 = null;
        String str5 = null;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    str5 = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    strArr3 = a.z(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    strArr2 = a.z(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    strArr = a.z(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    str4 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    str3 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    str2 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    str = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    plusCommonExtras = (PlusCommonExtras) a.a(parcel, n, PlusCommonExtras.CREATOR);
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
            return new h(i, str5, strArr3, strArr2, strArr, str4, str3, str2, str, plusCommonExtras);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public h[] bN(int i) {
        return new h[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return aK(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return bN(x0);
    }
}