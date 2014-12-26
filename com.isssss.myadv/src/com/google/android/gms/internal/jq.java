package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class jq implements Creator<jp> {
    static void a(jp jpVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, jpVar.getVersionCode());
        b.c(parcel, MMAdView.TRANSITION_UP, jpVar.adh);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, jpVar.adi, false);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, jpVar.adj);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, jpVar.adk, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, jpVar.adl);
        b.c(parcel, ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, jpVar.adm);
        b.F(parcel, p);
    }

    public jp bs(Parcel parcel) {
        String str = null;
        int i = 0;
        int o = a.o(parcel);
        double d = 0.0d;
        long j = 0;
        int i2 = -1;
        String str2 = null;
        int i3 = 0;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i3 = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    i = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    str2 = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    d = a.l(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    str = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    j = a.i(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    i2 = a.g(parcel, n);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new jp(i3, i, str2, d, str, j, i2);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public jp[] cG(int i) {
        return new jp[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return bs(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return cG(x0);
    }
}