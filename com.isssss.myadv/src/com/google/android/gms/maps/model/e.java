package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.b;
import com.millennialmedia.android.MMAdView;

public class e {
    static void a(LatLng latLng, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, latLng.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, latLng.latitude);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, latLng.longitude);
        b.F(parcel, p);
    }
}