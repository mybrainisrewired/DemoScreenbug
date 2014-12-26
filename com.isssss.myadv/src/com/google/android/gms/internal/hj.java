package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.millennialmedia.android.MMAdView;

public class hj implements Creator<hi> {
    static void a(hi hiVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, hiVar.xH);
        b.a(parcel, (int)MMAdView.TRANSITION_UP, hiVar.hY(), false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, hiVar.getTag(), false);
        b.F(parcel, p);
    }

    public hi aE(Parcel parcel) {
        String str = null;
        int o = a.o(parcel);
        int i = 0;
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
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new hi(i, str2, str);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public hi[] bF(int i) {
        return new hi[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return aE(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return bF(x0);
    }
}