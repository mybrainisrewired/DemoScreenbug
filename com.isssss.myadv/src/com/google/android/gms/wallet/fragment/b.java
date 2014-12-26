package com.google.android.gms.wallet.fragment;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class b implements Creator<WalletFragmentOptions> {
    static void a(WalletFragmentOptions walletFragmentOptions, Parcel parcel, int i) {
        int p = com.google.android.gms.common.internal.safeparcel.b.p(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, walletFragmentOptions.xH);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, MMAdView.TRANSITION_UP, walletFragmentOptions.getEnvironment());
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, MMAdView.TRANSITION_DOWN, walletFragmentOptions.getTheme());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, walletFragmentOptions.getFragmentStyle(), i, false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, walletFragmentOptions.getMode());
        com.google.android.gms.common.internal.safeparcel.b.F(parcel, p);
    }

    public WalletFragmentOptions bo(Parcel parcel) {
        int i = 1;
        int i2 = 0;
        int o = a.o(parcel);
        WalletFragmentStyle walletFragmentStyle = null;
        int i3 = 1;
        int i4 = 0;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i4 = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    i3 = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    i2 = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    walletFragmentStyle = (WalletFragmentStyle) a.a(parcel, n, WalletFragmentStyle.CREATOR);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    i = a.g(parcel, n);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new WalletFragmentOptions(i4, i3, i2, walletFragmentStyle, i);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public WalletFragmentOptions[] cB(int i) {
        return new WalletFragmentOptions[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return bo(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return cB(x0);
    }
}