package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.b;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class c {
    static void a(GroundOverlayOptions groundOverlayOptions, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, groundOverlayOptions.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, groundOverlayOptions.iD(), false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, groundOverlayOptions.getLocation(), i, false);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, groundOverlayOptions.getWidth());
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, groundOverlayOptions.getHeight());
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, groundOverlayOptions.getBounds(), i, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, groundOverlayOptions.getBearing());
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, groundOverlayOptions.getZIndex());
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, groundOverlayOptions.isVisible());
        b.a(parcel, (int)ApiEventType.API_MRAID_USE_CUSTOM_CLOSE, groundOverlayOptions.getTransparency());
        b.a(parcel, (int)ApiEventType.API_MRAID_EXPAND, groundOverlayOptions.getAnchorU());
        b.a(parcel, (int)ApiEventType.API_MRAID_RESIZE, groundOverlayOptions.getAnchorV());
        b.F(parcel, p);
    }
}