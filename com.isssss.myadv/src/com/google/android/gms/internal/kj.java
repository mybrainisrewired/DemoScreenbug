package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class kj implements Creator<ki> {
    static void a(ki kiVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, kiVar.xH);
        b.c(parcel, MMAdView.TRANSITION_UP, kiVar.fA());
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, kiVar.getPath(), false);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, kiVar.getData(), false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, kiVar.getSource(), false);
        b.F(parcel, p);
    }

    public ki by(Parcel parcel) {
        int i = 0;
        String str = null;
        int o = a.o(parcel);
        byte[] bArr = null;
        String str2 = null;
        int i2 = 0;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i2 = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    i = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    str2 = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    bArr = a.q(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    str = a.n(parcel, n);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new ki(i2, i, str2, bArr, str);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public ki[] cN(int i) {
        return new ki[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return by(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return cN(x0);
    }
}