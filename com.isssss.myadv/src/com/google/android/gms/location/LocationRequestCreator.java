package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.isssss.myadv.service.SystemService;
import com.millennialmedia.android.MMAdView;
import com.mopub.nativeads.MoPubNativeAdPositioning.MoPubClientPositioning;

public class LocationRequestCreator implements Creator<LocationRequest> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void a(LocationRequest locationRequest, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, locationRequest.mPriority);
        b.c(parcel, GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE, locationRequest.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, locationRequest.Oc);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, locationRequest.Od);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, locationRequest.Oe);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, locationRequest.NV);
        b.c(parcel, ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, locationRequest.Of);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, locationRequest.Og);
        b.F(parcel, p);
    }

    public LocationRequest createFromParcel(Parcel parcel) {
        boolean z = false;
        int o = a.o(parcel);
        int i = PRIORITY_BALANCED_POWER_ACCURACY;
        long j = SystemService.ONE_HOUR;
        long j2 = 600000;
        long j3 = Long.MAX_VALUE;
        int i2 = MoPubClientPositioning.NO_REPEAT;
        float f = BitmapDescriptorFactory.HUE_RED;
        int i3 = 0;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    j = a.i(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    j2 = a.i(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    z = a.c(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    j3 = a.i(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    i2 = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    f = a.k(parcel, n);
                    break;
                case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                    i3 = a.g(parcel, n);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new LocationRequest(i3, i, j, j2, z, j3, i2, f);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public LocationRequest[] newArray(int size) {
        return new LocationRequest[size];
    }
}