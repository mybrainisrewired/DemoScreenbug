package com.google.android.gms.maps;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class StreetViewPanoramaOptionsCreator implements Creator<StreetViewPanoramaOptions> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void a(StreetViewPanoramaOptions streetViewPanoramaOptions, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, streetViewPanoramaOptions.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, streetViewPanoramaOptions.getStreetViewPanoramaCamera(), i, false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, streetViewPanoramaOptions.getPanoramaId(), false);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, streetViewPanoramaOptions.getPosition(), i, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, streetViewPanoramaOptions.getRadius(), false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, streetViewPanoramaOptions.it());
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, streetViewPanoramaOptions.il());
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, streetViewPanoramaOptions.iu());
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, streetViewPanoramaOptions.iv());
        b.a(parcel, (int)ApiEventType.API_MRAID_USE_CUSTOM_CLOSE, streetViewPanoramaOptions.ih());
        b.F(parcel, p);
    }

    public StreetViewPanoramaOptions createFromParcel(Parcel parcel) {
        Integer num = null;
        byte b = (byte) 0;
        int o = a.o(parcel);
        byte b2 = (byte) 0;
        byte b3 = (byte) 0;
        byte b4 = (byte) 0;
        byte b5 = (byte) 0;
        LatLng latLng = null;
        String str = null;
        StreetViewPanoramaCamera streetViewPanoramaCamera = null;
        int i = 0;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    streetViewPanoramaCamera = (StreetViewPanoramaCamera) a.a(parcel, n, StreetViewPanoramaCamera.CREATOR);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    str = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    latLng = (LatLng) a.a(parcel, n, LatLng.CREATOR);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    num = a.h(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    b5 = a.e(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    b4 = a.e(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    b3 = a.e(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    b2 = a.e(parcel, n);
                    break;
                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                    b = a.e(parcel, n);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new StreetViewPanoramaOptions(i, streetViewPanoramaCamera, str, latLng, num, b5, b4, b3, b2, b);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public StreetViewPanoramaOptions[] newArray(int size) {
        return new StreetViewPanoramaOptions[size];
    }
}