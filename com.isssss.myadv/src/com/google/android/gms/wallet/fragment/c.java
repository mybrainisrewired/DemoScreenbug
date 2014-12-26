package com.google.android.gms.wallet.fragment;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.millennialmedia.android.MMAdView;

public class c implements Creator<WalletFragmentStyle> {
    static void a(WalletFragmentStyle walletFragmentStyle, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, walletFragmentStyle.xH);
        b.a(parcel, (int)MMAdView.TRANSITION_UP, walletFragmentStyle.acT, false);
        b.c(parcel, MMAdView.TRANSITION_DOWN, walletFragmentStyle.acU);
        b.F(parcel, p);
    }

    public WalletFragmentStyle bp(Parcel parcel) {
        int i = 0;
        int o = a.o(parcel);
        Bundle bundle = null;
        int i2 = 0;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i2 = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    bundle = a.p(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    i = a.g(parcel, n);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new WalletFragmentStyle(i2, bundle, i);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public WalletFragmentStyle[] cC(int i) {
        return new WalletFragmentStyle[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return bp(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return cC(x0);
    }
}