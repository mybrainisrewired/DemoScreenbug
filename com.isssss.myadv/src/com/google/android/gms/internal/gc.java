package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.internal.ga.a;
import com.google.android.gms.internal.gd.b;
import com.millennialmedia.android.MMAdView;

public class gc implements Creator<b> {
    static void a(b bVar, Parcel parcel, int i) {
        int p = com.google.android.gms.common.internal.safeparcel.b.p(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, bVar.versionCode);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, (int)MMAdView.TRANSITION_UP, bVar.eM, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, (int)MMAdView.TRANSITION_DOWN, bVar.Em, i, false);
        com.google.android.gms.common.internal.safeparcel.b.F(parcel, p);
    }

    public b[] W(int i) {
        return new b[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return u(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return W(x0);
    }

    public b u(Parcel parcel) {
        a aVar = null;
        int o = com.google.android.gms.common.internal.safeparcel.a.o(parcel);
        int i = 0;
        String str = null;
        while (parcel.dataPosition() < o) {
            int n = com.google.android.gms.common.internal.safeparcel.a.n(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i = com.google.android.gms.common.internal.safeparcel.a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    str = com.google.android.gms.common.internal.safeparcel.a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    aVar = (a) com.google.android.gms.common.internal.safeparcel.a.a(parcel, n, a.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new b(i, str, aVar);
        }
        throw new com.google.android.gms.common.internal.safeparcel.a.a("Overread allowed size end=" + o, parcel);
    }
}