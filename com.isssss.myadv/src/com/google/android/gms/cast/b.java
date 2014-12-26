package com.google.android.gms.cast;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.images.WebImage;
import com.google.android.gms.common.internal.safeparcel.a;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.util.List;

public class b implements Creator<CastDevice> {
    static void a(CastDevice castDevice, Parcel parcel, int i) {
        int p = com.google.android.gms.common.internal.safeparcel.b.p(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, castDevice.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, (int)MMAdView.TRANSITION_UP, castDevice.getDeviceId(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, (int)MMAdView.TRANSITION_DOWN, castDevice.yb, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, castDevice.getFriendlyName(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, castDevice.getModelName(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, (int)ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, castDevice.getDeviceVersion(), false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, castDevice.getServicePort());
        com.google.android.gms.common.internal.safeparcel.b.b(parcel, ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, castDevice.getIcons(), false);
        com.google.android.gms.common.internal.safeparcel.b.F(parcel, p);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return k(x0);
    }

    public CastDevice k(Parcel parcel) {
        int i = 0;
        List list = null;
        int o = a.o(parcel);
        String str = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        int i2 = 0;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i2 = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    str5 = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    str4 = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    str3 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    str2 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    str = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    i = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    list = a.c(parcel, n, WebImage.CREATOR);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new CastDevice(i2, str5, str4, str3, str2, str, i, list);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return y(x0);
    }

    public CastDevice[] y(int i) {
        return new CastDevice[i];
    }
}