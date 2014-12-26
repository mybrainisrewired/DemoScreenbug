package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class VisibleRegionCreator implements Creator<VisibleRegion> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void a(VisibleRegion visibleRegion, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, visibleRegion.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, visibleRegion.nearLeft, i, false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, visibleRegion.nearRight, i, false);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, visibleRegion.farLeft, i, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, visibleRegion.farRight, i, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, visibleRegion.latLngBounds, i, false);
        b.F(parcel, p);
    }

    public VisibleRegion createFromParcel(Parcel parcel) {
        LatLngBounds latLngBounds = null;
        int o = a.o(parcel);
        int i = 0;
        LatLng latLng = null;
        LatLng latLng2 = null;
        LatLng latLng3 = null;
        LatLng latLng4 = null;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    latLng4 = (LatLng) a.a(parcel, n, LatLng.CREATOR);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    latLng3 = (LatLng) a.a(parcel, n, LatLng.CREATOR);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    latLng2 = (LatLng) a.a(parcel, n, LatLng.CREATOR);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    latLng = (LatLng) a.a(parcel, n, LatLng.CREATOR);
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    latLngBounds = (LatLngBounds) a.a(parcel, n, LatLngBounds.CREATOR);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new VisibleRegion(i, latLng4, latLng3, latLng2, latLng, latLngBounds);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public VisibleRegion[] newArray(int size) {
        return new VisibleRegion[size];
    }
}