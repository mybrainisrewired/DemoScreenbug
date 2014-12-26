package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.internal.ih.h;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.util.HashSet;
import java.util.Set;

public class ir implements Creator<h> {
    static void a(h hVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        Set ja = hVar.ja();
        if (ja.contains(Integer.valueOf(1))) {
            b.c(parcel, 1, hVar.getVersionCode());
        }
        if (ja.contains(Integer.valueOf(MMAdView.TRANSITION_DOWN))) {
            b.c(parcel, MMAdView.TRANSITION_DOWN, hVar.jN());
        }
        if (ja.contains(Integer.valueOf(MMAdView.TRANSITION_RANDOM))) {
            b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, hVar.getValue(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, hVar.getLabel(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES))) {
            b.c(parcel, ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, hVar.getType());
        }
        b.F(parcel, p);
    }

    public h aW(Parcel parcel) {
        String str = null;
        int i = 0;
        int o = a.o(parcel);
        Set hashSet = new HashSet();
        int i2 = 0;
        String str2 = null;
        int i3 = 0;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i3 = a.g(parcel, n);
                    hashSet.add(Integer.valueOf(1));
                    break;
                case MMAdView.TRANSITION_DOWN:
                    i = a.g(parcel, n);
                    hashSet.add(Integer.valueOf(MMAdView.TRANSITION_DOWN));
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    str = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(MMAdView.TRANSITION_RANDOM));
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    str2 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES));
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    i2 = a.g(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES));
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new h(hashSet, i3, str2, i2, str, i);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public h[] bZ(int i) {
        return new h[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return aW(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return bZ(x0);
    }
}