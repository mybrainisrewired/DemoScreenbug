package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.internal.ih.b.b;
import com.millennialmedia.android.MMAdView;
import java.util.HashSet;
import java.util.Set;

public class im implements Creator<b> {
    static void a(b bVar, Parcel parcel, int i) {
        int p = com.google.android.gms.common.internal.safeparcel.b.p(parcel);
        Set ja = bVar.ja();
        if (ja.contains(Integer.valueOf(1))) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, bVar.getVersionCode());
        }
        if (ja.contains(Integer.valueOf(MMAdView.TRANSITION_UP))) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, MMAdView.TRANSITION_UP, bVar.getHeight());
        }
        if (ja.contains(Integer.valueOf(MMAdView.TRANSITION_DOWN))) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, (int)MMAdView.TRANSITION_DOWN, bVar.getUrl(), true);
        }
        if (ja.contains(Integer.valueOf(MMAdView.TRANSITION_RANDOM))) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, MMAdView.TRANSITION_RANDOM, bVar.getWidth());
        }
        com.google.android.gms.common.internal.safeparcel.b.F(parcel, p);
    }

    public b aR(Parcel parcel) {
        int i = 0;
        int o = a.o(parcel);
        Set hashSet = new HashSet();
        String str = null;
        int i2 = 0;
        int i3 = 0;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i3 = a.g(parcel, n);
                    hashSet.add(Integer.valueOf(1));
                    break;
                case MMAdView.TRANSITION_UP:
                    i2 = a.g(parcel, n);
                    hashSet.add(Integer.valueOf(MMAdView.TRANSITION_UP));
                    break;
                case MMAdView.TRANSITION_DOWN:
                    str = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(MMAdView.TRANSITION_DOWN));
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
            return new b(hashSet, i3, i2, str, i);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public b[] bU(int i) {
        return new b[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return aR(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return bU(x0);
    }
}