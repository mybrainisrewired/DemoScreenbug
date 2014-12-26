package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.b;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class f {
    static void a(MarkerOptions markerOptions, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, markerOptions.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, markerOptions.getPosition(), i, false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, markerOptions.getTitle(), false);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, markerOptions.getSnippet(), false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, markerOptions.iE(), false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, markerOptions.getAnchorU());
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, markerOptions.getAnchorV());
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, markerOptions.isDraggable());
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, markerOptions.isVisible());
        b.F(parcel, p);
    }
}