package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class b {
    static void a(CircleOptions circleOptions, Parcel parcel, int i) {
        int p = com.google.android.gms.common.internal.safeparcel.b.p(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, circleOptions.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, (int)MMAdView.TRANSITION_UP, circleOptions.getCenter(), i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, (int)MMAdView.TRANSITION_DOWN, circleOptions.getRadius());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, circleOptions.getStrokeWidth());
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, circleOptions.getStrokeColor());
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, circleOptions.getFillColor());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, (int)ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, circleOptions.getZIndex());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, (int)ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, circleOptions.isVisible());
        com.google.android.gms.common.internal.safeparcel.b.F(parcel, p);
    }
}