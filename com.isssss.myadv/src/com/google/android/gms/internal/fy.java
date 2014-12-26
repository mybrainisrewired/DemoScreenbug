package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.millennialmedia.android.MMAdView;
import java.util.ArrayList;

public class fy implements Creator<fx> {
    static void a(fx fxVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, fxVar.getVersionCode());
        b.b(parcel, MMAdView.TRANSITION_UP, fxVar.eV(), false);
        b.F(parcel, p);
    }

    public fx[] T(int i) {
        return new fx[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return r(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return T(x0);
    }

    public fx r(Parcel parcel) {
        int o = a.o(parcel);
        int i = 0;
        ArrayList arrayList = null;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    arrayList = a.c(parcel, n, fx.a.CREATOR);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new fx(i, arrayList);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }
}