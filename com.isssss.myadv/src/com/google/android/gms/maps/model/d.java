package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.b;
import com.millennialmedia.android.MMAdView;

public class d {
    static void a(LatLngBounds latLngBounds, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, latLngBounds.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, latLngBounds.southwest, i, false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, latLngBounds.northeast, i, false);
        b.F(parcel, p);
    }
}