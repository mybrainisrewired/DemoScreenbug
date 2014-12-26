package com.google.android.gms.wallet.fragment;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class a implements Creator<WalletFragmentInitParams> {
    static void a(WalletFragmentInitParams walletFragmentInitParams, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, walletFragmentInitParams.xH);
        b.a(parcel, (int)MMAdView.TRANSITION_UP, walletFragmentInitParams.getAccountName(), false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, walletFragmentInitParams.getMaskedWalletRequest(), i, false);
        b.c(parcel, MMAdView.TRANSITION_RANDOM, walletFragmentInitParams.getMaskedWalletRequestCode());
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, walletFragmentInitParams.getMaskedWallet(), i, false);
        b.F(parcel, p);
    }

    public WalletFragmentInitParams bn(Parcel parcel) {
        MaskedWallet maskedWallet = null;
        int o = com.google.android.gms.common.internal.safeparcel.a.o(parcel);
        int i = 0;
        int i2 = -1;
        MaskedWalletRequest maskedWalletRequest = null;
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
                    maskedWalletRequest = (MaskedWalletRequest) com.google.android.gms.common.internal.safeparcel.a.a(parcel, n, MaskedWalletRequest.CREATOR);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    i2 = com.google.android.gms.common.internal.safeparcel.a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    maskedWallet = (MaskedWallet) com.google.android.gms.common.internal.safeparcel.a.a(parcel, n, MaskedWallet.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new WalletFragmentInitParams(i, str, maskedWalletRequest, i2, maskedWallet);
        }
        throw new com.google.android.gms.common.internal.safeparcel.a.a("Overread allowed size end=" + o, parcel);
    }

    public WalletFragmentInitParams[] cA(int i) {
        return new WalletFragmentInitParams[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return bn(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return cA(x0);
    }
}