package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.b;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class g {
    static void a(PolygonOptions polygonOptions, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, polygonOptions.getVersionCode());
        b.b(parcel, MMAdView.TRANSITION_UP, polygonOptions.getPoints(), false);
        b.c(parcel, MMAdView.TRANSITION_DOWN, polygonOptions.iF(), false);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, polygonOptions.getStrokeWidth());
        b.c(parcel, ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, polygonOptions.getStrokeColor());
        b.c(parcel, ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, polygonOptions.getFillColor());
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, polygonOptions.getZIndex());
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, polygonOptions.isVisible());
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, polygonOptions.isGeodesic());
        b.F(parcel, p);
    }
}