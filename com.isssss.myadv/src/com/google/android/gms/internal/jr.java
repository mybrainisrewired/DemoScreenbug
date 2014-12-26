package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class jr implements Creator<jo> {
    static void a(jo joVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, joVar.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, joVar.label, false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, joVar.adg, i, false);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, joVar.type, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, joVar.abJ, i, false);
        b.F(parcel, p);
    }

    public jo bt(Parcel parcel) {
        ju juVar = null;
        int o = a.o(parcel);
        int i = 0;
        String str = null;
        jp jpVar = null;
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
                    jpVar = (jp) a.a(parcel, n, jp.CREATOR);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    str = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    juVar = (ju) a.a(parcel, n, ju.CREATOR);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new jo(i, str2, jpVar, str, juVar);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public jo[] cH(int i) {
        return new jo[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return bt(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return cH(x0);
    }
}