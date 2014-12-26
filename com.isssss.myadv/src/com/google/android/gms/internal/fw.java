package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.millennialmedia.android.MMAdView;

public class fw implements Creator<fv> {
    static void a(fv fvVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, fvVar.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, fvVar.eT(), i, false);
        b.F(parcel, p);
    }

    public fv[] S(int i) {
        return new fv[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return q(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return S(x0);
    }

    public fv q(Parcel parcel) {
        int o = a.o(parcel);
        int i = 0;
        fx fxVar = null;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    fxVar = (fx) a.a(parcel, n, fx.CREATOR);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new fv(i, fxVar);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }
}