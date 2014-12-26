package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.millennialmedia.android.MMAdView;

public class jv implements Creator<ju> {
    static void a(ju juVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, juVar.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, juVar.ado);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, juVar.adp);
        b.F(parcel, p);
    }

    public ju bv(Parcel parcel) {
        long j = 0;
        int o = a.o(parcel);
        int i = 0;
        long j2 = 0;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    j2 = a.i(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    j = a.i(parcel, n);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new ju(i, j2, j);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public ju[] cJ(int i) {
        return new ju[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return bv(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return cJ(x0);
    }
}