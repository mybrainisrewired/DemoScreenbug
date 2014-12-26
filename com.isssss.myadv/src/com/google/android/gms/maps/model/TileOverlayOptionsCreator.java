package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class TileOverlayOptionsCreator implements Creator<TileOverlayOptions> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void a(TileOverlayOptions tileOverlayOptions, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, tileOverlayOptions.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, tileOverlayOptions.iG(), false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, tileOverlayOptions.isVisible());
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, tileOverlayOptions.getZIndex());
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, tileOverlayOptions.getFadeIn());
        b.F(parcel, p);
    }

    public TileOverlayOptions createFromParcel(Parcel parcel) {
        boolean z = false;
        int o = a.o(parcel);
        IBinder iBinder = null;
        float f = BitmapDescriptorFactory.HUE_RED;
        boolean z2 = true;
        int i = 0;
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
                    z = a.c(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    f = a.k(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    z2 = a.c(parcel, n);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new TileOverlayOptions(i, iBinder, z, f, z2);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public TileOverlayOptions[] newArray(int size) {
        return new TileOverlayOptions[size];
    }
}