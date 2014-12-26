package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.internal.gd.a;
import com.millennialmedia.android.MMAdView;
import java.util.ArrayList;

public class gf implements Creator<a> {
    static void a(a aVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, aVar.versionCode);
        b.a(parcel, (int)MMAdView.TRANSITION_UP, aVar.className, false);
        b.b(parcel, MMAdView.TRANSITION_DOWN, aVar.El, false);
        b.F(parcel, p);
    }

    public a[] Y(int i) {
        return new a[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return w(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return Y(x0);
    }

    public a w(Parcel parcel) {
        ArrayList arrayList = null;
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
                    arrayList = com.google.android.gms.common.internal.safeparcel.a.c(parcel, n, gd.b.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new a(i, str, arrayList);
        }
        throw new com.google.android.gms.common.internal.safeparcel.a.a("Overread allowed size end=" + o, parcel);
    }
}