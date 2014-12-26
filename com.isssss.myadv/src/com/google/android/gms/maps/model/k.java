package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.b;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class k {
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
}