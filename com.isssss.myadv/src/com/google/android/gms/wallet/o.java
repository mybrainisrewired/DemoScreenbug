package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class o implements Creator<ProxyCard> {
    static void a(ProxyCard proxyCard, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, proxyCard.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, proxyCard.ack, false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, proxyCard.acl, false);
        b.c(parcel, MMAdView.TRANSITION_RANDOM, proxyCard.acm);
        b.c(parcel, ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, proxyCard.acn);
        b.F(parcel, p);
    }

    public ProxyCard bk(Parcel parcel) {
        String str = null;
        int i = 0;
        int o = a.o(parcel);
        int i2 = 0;
        String str2 = null;
        int i3 = 0;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i3 = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    str2 = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    str = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    i2 = a.g(parcel, n);
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
            return new ProxyCard(i3, str2, str, i2, i);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return bk(x0);
    }

    public ProxyCard[] cw(int i) {
        return new ProxyCard[i];
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return cw(x0);
    }
}