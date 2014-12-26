package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.millennialmedia.android.MMAdView;

public class LatLngBoundsCreator implements Creator<LatLngBounds> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void a(LatLngBounds latLngBounds, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, latLngBounds.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, latLngBounds.southwest, i, false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, latLngBounds.northeast, i, false);
        b.F(parcel, p);
    }

    public LatLngBounds createFromParcel(Parcel parcel) {
        LatLng latLng = null;
        int o = a.o(parcel);
        int i = 0;
        LatLng latLng2 = null;
        while (parcel.dataPosition() < o) {
            int i2;
            LatLng latLng3;
            int n = a.n(parcel);
            LatLng latLng4;
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    latLng4 = latLng;
                    latLng = latLng2;
                    i2 = a.g(parcel, n);
                    latLng3 = latLng4;
                    break;
                case MMAdView.TRANSITION_UP:
                    i2 = i;
                    latLng4 = (LatLng) a.a(parcel, n, CREATOR);
                    latLng3 = latLng;
                    latLng = latLng4;
                    break;
                case MMAdView.TRANSITION_DOWN:
                    latLng3 = (LatLng) a.a(parcel, n, CREATOR);
                    latLng = latLng2;
                    i2 = i;
                    break;
                default:
                    a.b(parcel, n);
                    latLng3 = latLng;
                    latLng = latLng2;
                    i2 = i;
                    break;
            }
            i = i2;
            latLng2 = latLng;
            latLng = latLng3;
        }
        if (parcel.dataPosition() == o) {
            return new LatLngBounds(i, latLng2, latLng);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public LatLngBounds[] newArray(int size) {
        return new LatLngBounds[size];
    }
}