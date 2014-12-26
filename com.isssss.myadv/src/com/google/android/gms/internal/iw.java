package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.millennialmedia.android.MMAdView;

public class iw implements Creator<iv> {
    static void a(iv ivVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, ivVar.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, ivVar.acs, false);
        b.F(parcel, p);
    }

    public iv bl(Parcel parcel) {
        int o = a.o(parcel);
        int i = 0;
        int[] iArr = null;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    iArr = a.t(parcel, n);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new iv(i, iArr);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return bl(x0);
    }

    public iv[] cx(int i) {
        return new iv[i];
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return cx(x0);
    }
}