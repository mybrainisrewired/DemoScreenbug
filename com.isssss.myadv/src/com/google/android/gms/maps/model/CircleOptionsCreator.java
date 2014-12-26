package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class CircleOptionsCreator implements Creator<CircleOptions> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void a(CircleOptions circleOptions, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, circleOptions.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, circleOptions.getCenter(), i, false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, circleOptions.getRadius());
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, circleOptions.getStrokeWidth());
        b.c(parcel, ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, circleOptions.getStrokeColor());
        b.c(parcel, ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, circleOptions.getFillColor());
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, circleOptions.getZIndex());
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, circleOptions.isVisible());
        b.F(parcel, p);
    }

    public CircleOptions createFromParcel(Parcel parcel) {
        float f = BitmapDescriptorFactory.HUE_RED;
        boolean z = false;
        int o = a.o(parcel);
        LatLng latLng = null;
        double d = 0.0d;
        int i = 0;
        int i2 = 0;
        float f2 = 0.0f;
        int i3 = 0;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i3 = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    latLng = (LatLng) a.a(parcel, n, LatLng.CREATOR);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    d = a.l(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    f2 = a.k(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    i2 = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    i = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    f = a.k(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    z = a.c(parcel, n);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new CircleOptions(i3, latLng, d, f2, i2, i, f, z);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public CircleOptions[] newArray(int size) {
        return new CircleOptions[size];
    }
}