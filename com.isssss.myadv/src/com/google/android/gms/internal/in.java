package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.internal.ih.c;
import com.millennialmedia.android.MMAdView;
import java.util.HashSet;
import java.util.Set;

public class in implements Creator<c> {
    static void a(c cVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        Set ja = cVar.ja();
        if (ja.contains(Integer.valueOf(1))) {
            b.c(parcel, 1, cVar.getVersionCode());
        }
        if (ja.contains(Integer.valueOf(MMAdView.TRANSITION_UP))) {
            b.a(parcel, (int)MMAdView.TRANSITION_UP, cVar.getUrl(), true);
        }
        b.F(parcel, p);
    }

    public c aS(Parcel parcel) {
        int o = a.o(parcel);
        Set hashSet = new HashSet();
        int i = 0;
        String str = null;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i = a.g(parcel, n);
                    hashSet.add(Integer.valueOf(1));
                    break;
                case MMAdView.TRANSITION_UP:
                    str = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(MMAdView.TRANSITION_UP));
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new c(hashSet, i, str);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public c[] bV(int i) {
        return new c[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return aS(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return bV(x0);
    }
}