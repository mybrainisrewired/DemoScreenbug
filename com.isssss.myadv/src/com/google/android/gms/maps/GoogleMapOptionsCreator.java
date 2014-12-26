package com.google.android.gms.maps;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.maps.model.CameraPosition;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class GoogleMapOptionsCreator implements Creator<GoogleMapOptions> {
    public static final int CONTENT_DESCRIPTION = 0;

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

    public GoogleMapOptions createFromParcel(Parcel parcel) {
        byte b = (byte) 0;
        int o = a.o(parcel);
        CameraPosition cameraPosition = null;
        byte b2 = (byte) 0;
        byte b3 = (byte) 0;
        byte b4 = (byte) 0;
        byte b5 = (byte) 0;
        byte b6 = (byte) 0;
        int i = 0;
        byte b7 = (byte) 0;
        byte b8 = (byte) 0;
        int i2 = 0;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i2 = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    b8 = a.e(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    b7 = a.e(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    i = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    cameraPosition = (CameraPosition) a.a(parcel, n, CameraPosition.CREATOR);
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    b6 = a.e(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    b5 = a.e(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    b4 = a.e(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    b3 = a.e(parcel, n);
                    break;
                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                    b2 = a.e(parcel, n);
                    break;
                case ApiEventType.API_MRAID_EXPAND:
                    b = a.e(parcel, n);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new GoogleMapOptions(i2, b8, b7, i, cameraPosition, b6, b5, b4, b3, b2, b);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public GoogleMapOptions[] newArray(int size) {
        return new GoogleMapOptions[size];
    }
}