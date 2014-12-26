package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.millennialmedia.android.MMAdView;
import java.util.ArrayList;

public class ge implements Creator<gd> {
    static void a(gd gdVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, gdVar.getVersionCode());
        b.b(parcel, MMAdView.TRANSITION_UP, gdVar.fn(), false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, gdVar.fo(), false);
        b.F(parcel, p);
    }

    public gd[] X(int i) {
        return new gd[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return v(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return X(x0);
    }

    public gd v(Parcel parcel) {
        String str = null;
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
                    arrayList = a.c(parcel, n, gd.a.CREATOR);
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
            return new gd(i, arrayList, str);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }
}