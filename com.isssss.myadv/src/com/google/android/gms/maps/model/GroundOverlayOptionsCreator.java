package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class GroundOverlayOptionsCreator implements Creator<GroundOverlayOptions> {
    public static final int CONTENT_DESCRIPTION = 0;

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

    public GroundOverlayOptions createFromParcel(Parcel parcel) {
        int o = a.o(parcel);
        int i = 0;
        IBinder iBinder = null;
        LatLng latLng = null;
        float f = BitmapDescriptorFactory.HUE_RED;
        float f2 = BitmapDescriptorFactory.HUE_RED;
        LatLngBounds latLngBounds = null;
        float f3 = BitmapDescriptorFactory.HUE_RED;
        float f4 = BitmapDescriptorFactory.HUE_RED;
        boolean z = false;
        float f5 = BitmapDescriptorFactory.HUE_RED;
        float f6 = BitmapDescriptorFactory.HUE_RED;
        float f7 = BitmapDescriptorFactory.HUE_RED;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    iBinder = a.o(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    latLng = (LatLng) a.a(parcel, n, LatLng.CREATOR);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    f = a.k(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    f2 = a.k(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    latLngBounds = (LatLngBounds) a.a(parcel, n, LatLngBounds.CREATOR);
                    break;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    f3 = a.k(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    f4 = a.k(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    z = a.c(parcel, n);
                    break;
                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                    f5 = a.k(parcel, n);
                    break;
                case ApiEventType.API_MRAID_EXPAND:
                    f6 = a.k(parcel, n);
                    break;
                case ApiEventType.API_MRAID_RESIZE:
                    f7 = a.k(parcel, n);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new GroundOverlayOptions(i, iBinder, latLng, f, f2, latLngBounds, f3, f4, z, f5, f6, f7);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public GroundOverlayOptions[] newArray(int size) {
        return new GroundOverlayOptions[size];
    }
}