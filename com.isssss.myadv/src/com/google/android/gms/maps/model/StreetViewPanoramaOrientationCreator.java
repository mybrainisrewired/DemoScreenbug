package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.millennialmedia.android.MMAdView;

public class StreetViewPanoramaOrientationCreator implements Creator<StreetViewPanoramaOrientation> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void a(StreetViewPanoramaOrientation streetViewPanoramaOrientation, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, streetViewPanoramaOrientation.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, streetViewPanoramaOrientation.tilt);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, streetViewPanoramaOrientation.bearing);
        b.F(parcel, p);
    }

    public StreetViewPanoramaOrientation createFromParcel(Parcel parcel) {
        float f = BitmapDescriptorFactory.HUE_RED;
        int o = a.o(parcel);
        int i = 0;
        float f2 = 0.0f;
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
                    f = a.k(parcel, n);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new StreetViewPanoramaOrientation(i, f2, f);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public StreetViewPanoramaOrientation[] newArray(int size) {
        return new StreetViewPanoramaOrientation[size];
    }
}