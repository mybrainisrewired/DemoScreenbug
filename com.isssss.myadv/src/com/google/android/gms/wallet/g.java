package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.millennialmedia.android.MMAdView;

public class g implements Creator<FullWalletRequest> {
    static void a(FullWalletRequest fullWalletRequest, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, fullWalletRequest.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, fullWalletRequest.abh, false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, fullWalletRequest.abi, false);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, fullWalletRequest.abr, i, false);
        b.F(parcel, p);
    }

    public FullWalletRequest bc(Parcel parcel) {
        Cart cart = null;
        int o = a.o(parcel);
        int i = 0;
        String str = null;
        String str2 = null;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    str2 = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    str = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    cart = (Cart) a.a(parcel, n, Cart.CREATOR);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new FullWalletRequest(i, str2, str, cart);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public FullWalletRequest[] co(int i) {
        return new FullWalletRequest[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return bc(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return co(x0);
    }
}