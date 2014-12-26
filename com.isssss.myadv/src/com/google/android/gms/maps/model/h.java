package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.b;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class h {
    static void a(PolylineOptions polylineOptions, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, polylineOptions.getVersionCode());
        b.b(parcel, MMAdView.TRANSITION_UP, polylineOptions.getPoints(), false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, polylineOptions.getWidth());
        b.c(parcel, MMAdView.TRANSITION_RANDOM, polylineOptions.getColor());
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, polylineOptions.getZIndex());
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, polylineOptions.isVisible());
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, polylineOptions.isGeodesic());
        b.F(parcel, p);
    }
}