package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.b;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class a {
    static void a(CameraPosition cameraPosition, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, cameraPosition.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, cameraPosition.target, i, false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, cameraPosition.zoom);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, cameraPosition.tilt);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, cameraPosition.bearing);
        b.F(parcel, p);
    }
}