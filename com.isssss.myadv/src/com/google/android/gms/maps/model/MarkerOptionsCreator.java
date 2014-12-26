package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.android.volley.DefaultRetryPolicy;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class MarkerOptionsCreator implements Creator<MarkerOptions> {
    public static final int CONTENT_DESCRIPTION = 0;

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
        b.a(parcel, (int)ApiEventType.API_MRAID_USE_CUSTOM_CLOSE, markerOptions.isFlat());
        b.a(parcel, (int)ApiEventType.API_MRAID_EXPAND, markerOptions.getRotation());
        b.a(parcel, (int)ApiEventType.API_MRAID_RESIZE, markerOptions.getInfoWindowAnchorU());
        b.a(parcel, (int)ApiEventType.API_MRAID_CLOSE, markerOptions.getInfoWindowAnchorV());
        b.a(parcel, (int)ApiEventType.API_MRAID_IS_VIEWABLE, markerOptions.getAlpha());
        b.F(parcel, p);
    }

    public MarkerOptions createFromParcel(Parcel parcel) {
        int o = a.o(parcel);
        int i = 0;
        LatLng latLng = null;
        String str = null;
        String str2 = null;
        IBinder iBinder = null;
        float f = BitmapDescriptorFactory.HUE_RED;
        float f2 = BitmapDescriptorFactory.HUE_RED;
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        float f3 = BitmapDescriptorFactory.HUE_RED;
        float f4 = 0.5f;
        float f5 = BitmapDescriptorFactory.HUE_RED;
        float f6 = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    latLng = (LatLng) a.a(parcel, n, LatLng.CREATOR);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    str = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    str2 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    iBinder = a.o(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    f = a.k(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    f2 = a.k(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    z = a.c(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    z2 = a.c(parcel, n);
                    break;
                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                    z3 = a.c(parcel, n);
                    break;
                case ApiEventType.API_MRAID_EXPAND:
                    f3 = a.k(parcel, n);
                    break;
                case ApiEventType.API_MRAID_RESIZE:
                    f4 = a.k(parcel, n);
                    break;
                case ApiEventType.API_MRAID_CLOSE:
                    f5 = a.k(parcel, n);
                    break;
                case ApiEventType.API_MRAID_IS_VIEWABLE:
                    f6 = a.k(parcel, n);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new MarkerOptions(i, latLng, str, str2, iBinder, f, f2, z, z2, z3, f3, f4, f5, f6);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public MarkerOptions[] newArray(int size) {
        return new MarkerOptions[size];
    }
}