package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.internal.ga.a;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class gb implements Creator<a> {
    static void a(a aVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, aVar.getVersionCode());
        b.c(parcel, MMAdView.TRANSITION_UP, aVar.eW());
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, aVar.fc());
        b.c(parcel, MMAdView.TRANSITION_RANDOM, aVar.eX());
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, aVar.fd());
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, aVar.fe(), false);
        b.c(parcel, ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, aVar.ff());
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, aVar.fh(), false);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, aVar.fj(), i, false);
        b.F(parcel, p);
    }

    public a[] V(int i) {
        return new a[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return t(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return V(x0);
    }

    public a t(Parcel parcel) {
        fv fvVar = null;
        int i = 0;
        int o = com.google.android.gms.common.internal.safeparcel.a.o(parcel);
        String str = null;
        String str2 = null;
        boolean z = false;
        int i2 = 0;
        boolean z2 = false;
        int i3 = 0;
        int i4 = 0;
        while (parcel.dataPosition() < o) {
            int n = com.google.android.gms.common.internal.safeparcel.a.n(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i4 = com.google.android.gms.common.internal.safeparcel.a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    i3 = com.google.android.gms.common.internal.safeparcel.a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    z2 = com.google.android.gms.common.internal.safeparcel.a.c(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    i2 = com.google.android.gms.common.internal.safeparcel.a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    z = com.google.android.gms.common.internal.safeparcel.a.c(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    str2 = com.google.android.gms.common.internal.safeparcel.a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    i = com.google.android.gms.common.internal.safeparcel.a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    str = com.google.android.gms.common.internal.safeparcel.a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    fvVar = (fv) com.google.android.gms.common.internal.safeparcel.a.a(parcel, n, fv.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new a(i4, i3, z2, i2, z, str2, i, str, fvVar);
        }
        throw new com.google.android.gms.common.internal.safeparcel.a.a("Overread allowed size end=" + o, parcel);
    }
}