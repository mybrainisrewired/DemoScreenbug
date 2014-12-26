package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.millennialmedia.android.MMAdView;

public class jt implements Creator<js> {
    static void a(js jsVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, jsVar.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, jsVar.adn, false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, jsVar.pm, false);
        b.F(parcel, p);
    }

    public js bu(Parcel parcel) {
        String str = null;
        int o = a.o(parcel);
        int i = 0;
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
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new js(i, str2, str);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public js[] cI(int i) {
        return new js[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return bu(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return cI(x0);
    }
}