package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.internal.ih.b;
import com.millennialmedia.android.MMAdView;
import java.util.HashSet;
import java.util.Set;

public class ik implements Creator<b> {
    static void a(b bVar, Parcel parcel, int i) {
        int p = com.google.android.gms.common.internal.safeparcel.b.p(parcel);
        Set ja = bVar.ja();
        if (ja.contains(Integer.valueOf(1))) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, bVar.getVersionCode());
        }
        if (ja.contains(Integer.valueOf(MMAdView.TRANSITION_UP))) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, (int)MMAdView.TRANSITION_UP, bVar.jE(), i, true);
        }
        if (ja.contains(Integer.valueOf(MMAdView.TRANSITION_DOWN))) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, (int)MMAdView.TRANSITION_DOWN, bVar.jF(), i, true);
        }
        if (ja.contains(Integer.valueOf(MMAdView.TRANSITION_RANDOM))) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, MMAdView.TRANSITION_RANDOM, bVar.getLayout());
        }
        com.google.android.gms.common.internal.safeparcel.b.F(parcel, p);
    }

    public b aP(Parcel parcel) {
        b.b bVar = null;
        int i = 0;
        int o = a.o(parcel);
        Set hashSet = new HashSet();
        b.a aVar = null;
        int i2 = 0;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i2 = a.g(parcel, n);
                    hashSet.add(Integer.valueOf(1));
                    break;
                case MMAdView.TRANSITION_UP:
                    b.a aVar2 = (b.a) a.a(parcel, n, b.a.CREATOR);
                    hashSet.add(Integer.valueOf(MMAdView.TRANSITION_UP));
                    aVar = aVar2;
                    break;
                case MMAdView.TRANSITION_DOWN:
                    b.b bVar2 = (b.b) a.a(parcel, n, b.b.CREATOR);
                    hashSet.add(Integer.valueOf(MMAdView.TRANSITION_DOWN));
                    bVar = bVar2;
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    i = a.g(parcel, n);
                    hashSet.add(Integer.valueOf(MMAdView.TRANSITION_RANDOM));
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new b(hashSet, i2, aVar, bVar, i);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public b[] bS(int i) {
        return new b[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return aP(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return bS(x0);
    }
}