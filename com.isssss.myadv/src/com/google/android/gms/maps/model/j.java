package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.b;
import com.millennialmedia.android.MMAdView;

public class j {
    static void a(TileOverlayOptions tileOverlayOptions, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, tileOverlayOptions.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, tileOverlayOptions.iG(), false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, tileOverlayOptions.isVisible());
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, tileOverlayOptions.getZIndex());
        b.F(parcel, p);
    }
}