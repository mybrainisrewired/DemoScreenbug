package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.millennialmedia.android.MMAdView;

public class StreetViewPanoramaLocationCreator implements Creator<StreetViewPanoramaLocation> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void a(StreetViewPanoramaLocation streetViewPanoramaLocation, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, streetViewPanoramaLocation.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, streetViewPanoramaLocation.links, i, false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, streetViewPanoramaLocation.position, i, false);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, streetViewPanoramaLocation.panoId, false);
        b.F(parcel, p);
    }

    public StreetViewPanoramaLocation createFromParcel(Parcel parcel) {
        String str = null;
        int o = a.o(parcel);
        int i = 0;
        LatLng latLng = null;
        StreetViewPanoramaLink[] streetViewPanoramaLinkArr = null;
        while (parcel.dataPosition() < o) {
            LatLng latLng2;
            StreetViewPanoramaLink[] streetViewPanoramaLinkArr2;
            int i2;
            String str2;
            int n = a.n(parcel);
            String str3;
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    str3 = str;
                    latLng2 = latLng;
                    streetViewPanoramaLinkArr2 = streetViewPanoramaLinkArr;
                    i2 = a.g(parcel, n);
                    str2 = str3;
                    break;
                case MMAdView.TRANSITION_UP:
                    i2 = i;
                    LatLng latLng3 = latLng;
                    streetViewPanoramaLinkArr2 = (StreetViewPanoramaLink[]) a.b(parcel, n, StreetViewPanoramaLink.CREATOR);
                    str2 = str;
                    latLng2 = latLng3;
                    break;
                case MMAdView.TRANSITION_DOWN:
                    streetViewPanoramaLinkArr2 = streetViewPanoramaLinkArr;
                    i2 = i;
                    str3 = str;
                    latLng2 = (LatLng) a.a(parcel, n, LatLng.CREATOR);
                    str2 = str3;
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    str2 = a.n(parcel, n);
                    latLng2 = latLng;
                    streetViewPanoramaLinkArr2 = streetViewPanoramaLinkArr;
                    i2 = i;
                    break;
                default:
                    a.b(parcel, n);
                    str2 = str;
                    latLng2 = latLng;
                    streetViewPanoramaLinkArr2 = streetViewPanoramaLinkArr;
                    i2 = i;
                    break;
            }
            i = i2;
            streetViewPanoramaLinkArr = streetViewPanoramaLinkArr2;
            latLng = latLng2;
            str = str2;
        }
        if (parcel.dataPosition() == o) {
            return new StreetViewPanoramaLocation(i, streetViewPanoramaLinkArr, latLng, str);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public StreetViewPanoramaLocation[] newArray(int size) {
        return new StreetViewPanoramaLocation[size];
    }
}