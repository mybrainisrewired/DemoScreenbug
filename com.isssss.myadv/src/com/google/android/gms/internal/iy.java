package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.millennialmedia.android.MMAdView;

public class iy implements Creator<ix> {
    static void a(ix ixVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, ixVar.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, ixVar.act, false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, ixVar.acu, false);
        b.F(parcel, p);
    }

    public ix bm(Parcel parcel) {
        String[] strArr = null;
        int o = a.o(parcel);
        int i = 0;
        byte[][] bArr = (byte[][]) null;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    strArr = a.z(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    bArr = a.r(parcel, n);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new ix(i, strArr, bArr);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return bm(x0);
    }

    public ix[] cy(int i) {
        return new ix[i];
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return cy(x0);
    }
}