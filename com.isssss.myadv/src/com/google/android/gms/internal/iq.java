package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.internal.ih.g;
import com.millennialmedia.android.MMAdView;
import java.util.HashSet;
import java.util.Set;

public class iq implements Creator<g> {
    static void a(g gVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        Set ja = gVar.ja();
        if (ja.contains(Integer.valueOf(1))) {
            b.c(parcel, 1, gVar.getVersionCode());
        }
        if (ja.contains(Integer.valueOf(MMAdView.TRANSITION_UP))) {
            b.a(parcel, (int)MMAdView.TRANSITION_UP, gVar.isPrimary());
        }
        if (ja.contains(Integer.valueOf(MMAdView.TRANSITION_DOWN))) {
            b.a(parcel, (int)MMAdView.TRANSITION_DOWN, gVar.getValue(), true);
        }
        b.F(parcel, p);
    }

    public g aV(Parcel parcel) {
        boolean z = false;
        int o = a.o(parcel);
        Set hashSet = new HashSet();
        String str = null;
        int i = 0;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i = a.g(parcel, n);
                    hashSet.add(Integer.valueOf(1));
                    break;
                case MMAdView.TRANSITION_UP:
                    z = a.c(parcel, n);
                    hashSet.add(Integer.valueOf(MMAdView.TRANSITION_UP));
                    break;
                case MMAdView.TRANSITION_DOWN:
                    str = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(MMAdView.TRANSITION_DOWN));
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new g(hashSet, i, z, str);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public g[] bY(int i) {
        return new g[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return aV(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return bY(x0);
    }
}