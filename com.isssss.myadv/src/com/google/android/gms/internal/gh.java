package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.millennialmedia.android.MMAdView;

public class gh implements Creator<gg> {
    static void a(gg ggVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, ggVar.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, ggVar.fq(), false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, ggVar.fr(), i, false);
        b.F(parcel, p);
    }

    public gg[] Z(int i) {
        return new gg[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return x(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return Z(x0);
    }

    public gg x(Parcel parcel) {
        gd gdVar = null;
        int o = a.o(parcel);
        int i = 0;
        Parcel parcel2 = null;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    parcel2 = a.B(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    gdVar = (gd) a.a(parcel, n, gd.CREATOR);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new gg(i, parcel2, gdVar);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }
}