package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.util.List;

public class PolylineOptionsCreator implements Creator<PolylineOptions> {
    public static final int CONTENT_DESCRIPTION = 0;

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

    public PolylineOptions createFromParcel(Parcel parcel) {
        float f = BitmapDescriptorFactory.HUE_RED;
        boolean z = false;
        int o = a.o(parcel);
        List list = null;
        boolean z2 = false;
        int i = 0;
        float f2 = 0.0f;
        int i2 = 0;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i2 = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    list = a.c(parcel, n, LatLng.CREATOR);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    f2 = a.k(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    i = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    f = a.k(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    z2 = a.c(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    z = a.c(parcel, n);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new PolylineOptions(i2, list, f2, i, f, z2, z);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public PolylineOptions[] newArray(int size) {
        return new PolylineOptions[size];
    }
}