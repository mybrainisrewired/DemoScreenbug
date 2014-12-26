package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class ca implements Creator<cb> {
    static void a(cb cbVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, cbVar.versionCode);
        b.a(parcel, (int)MMAdView.TRANSITION_UP, cbVar.nN, false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, cbVar.nO, false);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, cbVar.mimeType, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, cbVar.packageName, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, cbVar.nP, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, cbVar.nQ, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, cbVar.nR, false);
        b.F(parcel, p);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return d(x0);
    }

    public cb d(Parcel parcel) {
        String str = null;
        int o = a.o(parcel);
        int i = 0;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
        String str7 = null;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    str7 = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    str6 = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    str5 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    str4 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    str3 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    str2 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    str = a.n(parcel, n);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new cb(i, str7, str6, str5, str4, str3, str2, str);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public cb[] h(int i) {
        return new cb[i];
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return h(x0);
    }
}