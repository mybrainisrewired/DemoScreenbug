package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class jz implements Creator<jy> {
    static void a(jy jyVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, jyVar.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, jyVar.adn, false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, jyVar.pm, false);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, jyVar.adr, i, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, jyVar.ads, i, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, jyVar.adt, i, false);
        b.F(parcel, p);
    }

    public jy bx(Parcel parcel) {
        jw jwVar = null;
        int o = a.o(parcel);
        int i = 0;
        jw jwVar2 = null;
        ju juVar = null;
        String str = null;
        String str2 = null;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    str2 = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    str = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    juVar = (ju) a.a(parcel, n, ju.CREATOR);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    jwVar2 = (jw) a.a(parcel, n, jw.CREATOR);
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    jwVar = (jw) a.a(parcel, n, jw.CREATOR);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new jy(i, str2, str, juVar, jwVar2, jwVar);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public jy[] cL(int i) {
        return new jy[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return bx(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return cL(x0);
    }
}