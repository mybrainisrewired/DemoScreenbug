package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.millennialmedia.android.MMAdView;
import java.util.ArrayList;

public class jn implements Creator<jm> {
    static void a(jm jmVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, jmVar.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, jmVar.add, false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, jmVar.ade, false);
        b.b(parcel, MMAdView.TRANSITION_RANDOM, jmVar.adf, false);
        b.F(parcel, p);
    }

    public jm br(Parcel parcel) {
        String str = null;
        int o = a.o(parcel);
        int i = 0;
        ArrayList fs = gi.fs();
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
                    fs = a.c(parcel, n, jk.CREATOR);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new jm(i, str2, str, fs);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public jm[] cF(int i) {
        return new jm[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return br(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return cF(x0);
    }
}