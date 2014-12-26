package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class al implements Creator<ak> {
    static void a(ak akVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, akVar.versionCode);
        b.a(parcel, (int)MMAdView.TRANSITION_UP, akVar.lS, false);
        b.c(parcel, MMAdView.TRANSITION_DOWN, akVar.height);
        b.c(parcel, MMAdView.TRANSITION_RANDOM, akVar.heightPixels);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, akVar.lT);
        b.c(parcel, ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, akVar.width);
        b.c(parcel, ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, akVar.widthPixels);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, akVar.lU, i, false);
        b.F(parcel, p);
    }

    public ak b(Parcel parcel) {
        ak[] akVarArr = null;
        int i = 0;
        int o = a.o(parcel);
        int i2 = 0;
        boolean z = false;
        int i3 = 0;
        int i4 = 0;
        String str = null;
        int i5 = 0;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i5 = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    str = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    i4 = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    i3 = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    z = a.c(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    i2 = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    i = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    akVarArr = (ak[]) a.b(parcel, n, ak.CREATOR);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new ak(i5, str, i4, i3, z, i2, i, akVarArr);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public ak[] c(int i) {
        return new ak[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return b(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return c(x0);
    }
}