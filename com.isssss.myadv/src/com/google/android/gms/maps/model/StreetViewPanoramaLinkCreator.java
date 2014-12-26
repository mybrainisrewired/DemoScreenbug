package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.millennialmedia.android.MMAdView;

public class StreetViewPanoramaLinkCreator implements Creator<StreetViewPanoramaLink> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void a(StreetViewPanoramaLink streetViewPanoramaLink, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, streetViewPanoramaLink.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, streetViewPanoramaLink.panoId, false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, streetViewPanoramaLink.bearing);
        b.F(parcel, p);
    }

    public StreetViewPanoramaLink createFromParcel(Parcel parcel) {
        int o = a.o(parcel);
        int i = 0;
        String str = null;
        float f = BitmapDescriptorFactory.HUE_RED;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    str = a.n(parcel, n);
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
            return new StreetViewPanoramaLink(i, str, f);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public StreetViewPanoramaLink[] newArray(int size) {
        return new StreetViewPanoramaLink[size];
    }
}