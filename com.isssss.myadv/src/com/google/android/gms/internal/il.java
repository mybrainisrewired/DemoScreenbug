package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.internal.ih.b.a;
import com.millennialmedia.android.MMAdView;
import java.util.HashSet;
import java.util.Set;

public class il implements Creator<a> {
    static void a(a aVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        Set ja = aVar.ja();
        if (ja.contains(Integer.valueOf(1))) {
            b.c(parcel, 1, aVar.getVersionCode());
        }
        if (ja.contains(Integer.valueOf(MMAdView.TRANSITION_UP))) {
            b.c(parcel, MMAdView.TRANSITION_UP, aVar.getLeftImageOffset());
        }
        if (ja.contains(Integer.valueOf(MMAdView.TRANSITION_DOWN))) {
            b.c(parcel, MMAdView.TRANSITION_DOWN, aVar.getTopImageOffset());
        }
        b.F(parcel, p);
    }

    public a aQ(Parcel parcel) {
        int i = 0;
        int o = com.google.android.gms.common.internal.safeparcel.a.o(parcel);
        Set hashSet = new HashSet();
        int i2 = 0;
        int i3 = 0;
        while (parcel.dataPosition() < o) {
            int n = com.google.android.gms.common.internal.safeparcel.a.n(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i3 = com.google.android.gms.common.internal.safeparcel.a.g(parcel, n);
                    hashSet.add(Integer.valueOf(1));
                    break;
                case MMAdView.TRANSITION_UP:
                    i2 = com.google.android.gms.common.internal.safeparcel.a.g(parcel, n);
                    hashSet.add(Integer.valueOf(MMAdView.TRANSITION_UP));
                    break;
                case MMAdView.TRANSITION_DOWN:
                    i = com.google.android.gms.common.internal.safeparcel.a.g(parcel, n);
                    hashSet.add(Integer.valueOf(MMAdView.TRANSITION_DOWN));
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new a(hashSet, i3, i2, i);
        }
        throw new com.google.android.gms.common.internal.safeparcel.a.a("Overread allowed size end=" + o, parcel);
    }

    public a[] bT(int i) {
        return new a[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return aQ(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return bT(x0);
    }
}