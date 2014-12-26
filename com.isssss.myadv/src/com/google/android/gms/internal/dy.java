package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class dy implements Creator<dx> {
    static void a(dx dxVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, dxVar.versionCode);
        b.a(parcel, (int)MMAdView.TRANSITION_UP, dxVar.rq, false);
        b.c(parcel, MMAdView.TRANSITION_DOWN, dxVar.rr);
        b.c(parcel, MMAdView.TRANSITION_RANDOM, dxVar.rs);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, dxVar.rt);
        b.F(parcel, p);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return h(x0);
    }

    public dx h(Parcel parcel) {
        boolean z = false;
        int o = a.o(parcel);
        String str = null;
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i3 = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    str = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    i2 = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    i = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    z = a.c(parcel, n);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new dx(i3, str, i2, i, z);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return o(x0);
    }

    public dx[] o(int i) {
        return new dx[i];
    }
}