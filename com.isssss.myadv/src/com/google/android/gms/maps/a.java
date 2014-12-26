package com.google.android.gms.maps;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.b;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class a {
    static void a(GoogleMapOptions googleMapOptions, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, googleMapOptions.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, googleMapOptions.ig());
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, googleMapOptions.ih());
        b.c(parcel, MMAdView.TRANSITION_RANDOM, googleMapOptions.getMapType());
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, googleMapOptions.getCamera(), i, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, googleMapOptions.ii());
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, googleMapOptions.ij());
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, googleMapOptions.ik());
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, googleMapOptions.il());
        b.a(parcel, (int)ApiEventType.API_MRAID_USE_CUSTOM_CLOSE, googleMapOptions.im());
        b.a(parcel, (int)ApiEventType.API_MRAID_EXPAND, googleMapOptions.in());
        b.F(parcel, p);
    }
}