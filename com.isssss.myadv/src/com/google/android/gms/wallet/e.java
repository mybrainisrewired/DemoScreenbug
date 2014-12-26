package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.millennialmedia.android.MMAdView;

public class e implements Creator<d> {
    static void a(d dVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, dVar.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, dVar.abg, i, false);
        b.F(parcel, p);
    }

    public d ba(Parcel parcel) {
        int o = a.o(parcel);
        int i = 0;
        LoyaltyWalletObject loyaltyWalletObject = null;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    loyaltyWalletObject = (LoyaltyWalletObject) a.a(parcel, n, LoyaltyWalletObject.CREATOR);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new d(i, loyaltyWalletObject);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public d[] cm(int i) {
        return new d[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return ba(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return cm(x0);
    }
}