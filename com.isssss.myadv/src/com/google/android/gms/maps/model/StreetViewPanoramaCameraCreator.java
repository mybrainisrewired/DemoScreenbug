package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.millennialmedia.android.MMAdView;

public class StreetViewPanoramaCameraCreator implements Creator<StreetViewPanoramaCamera> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void a(StreetViewPanoramaCamera streetViewPanoramaCamera, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, streetViewPanoramaCamera.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, streetViewPanoramaCamera.zoom);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, streetViewPanoramaCamera.tilt);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, streetViewPanoramaCamera.bearing);
        b.F(parcel, p);
    }

    public StreetViewPanoramaCamera createFromParcel(Parcel parcel) {
        float f = BitmapDescriptorFactory.HUE_RED;
        int o = a.o(parcel);
        float f2 = 0.0f;
        int i = 0;
        float f3 = 0.0f;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    f2 = a.k(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    f3 = a.k(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    f = a.k(parcel, n);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new StreetViewPanoramaCamera(i, f2, f3, f);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public StreetViewPanoramaCamera[] newArray(int size) {
        return new StreetViewPanoramaCamera[size];
    }
}