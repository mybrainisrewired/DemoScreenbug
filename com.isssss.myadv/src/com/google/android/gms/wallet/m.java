package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.millennialmedia.android.MMAdView;

public class m implements Creator<NotifyTransactionStatusRequest> {
    static void a(NotifyTransactionStatusRequest notifyTransactionStatusRequest, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, notifyTransactionStatusRequest.xH);
        b.a(parcel, (int)MMAdView.TRANSITION_UP, notifyTransactionStatusRequest.abh, false);
        b.c(parcel, MMAdView.TRANSITION_DOWN, notifyTransactionStatusRequest.status);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, notifyTransactionStatusRequest.ach, false);
        b.F(parcel, p);
    }

    public NotifyTransactionStatusRequest bi(Parcel parcel) {
        String str = null;
        int i = 0;
        int o = a.o(parcel);
        String str2 = null;
        int i2 = 0;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i2 = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    str2 = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    i = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    str = a.n(parcel, n);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new NotifyTransactionStatusRequest(i2, str2, i, str);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return bi(x0);
    }

    public NotifyTransactionStatusRequest[] cu(int i) {
        return new NotifyTransactionStatusRequest[i];
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return cu(x0);
    }
}