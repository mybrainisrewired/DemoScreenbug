package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.b;
import com.millennialmedia.android.MMAdView;

public class i {
    static void a(Tile tile, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, tile.getVersionCode());
        b.c(parcel, MMAdView.TRANSITION_UP, tile.width);
        b.c(parcel, MMAdView.TRANSITION_DOWN, tile.height);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, tile.data, false);
        b.F(parcel, p);
    }
}