package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.b;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class a implements Creator<Address> {
    static void a(Address address, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, address.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, address.name, false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, address.NB, false);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, address.NC, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, address.ND, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, address.qd, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, address.aba, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, address.abb, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, address.NI, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_USE_CUSTOM_CLOSE, address.NK, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_EXPAND, address.NL);
        b.a(parcel, (int)ApiEventType.API_MRAID_RESIZE, address.NM, false);
        b.F(parcel, p);
    }

    public Address aX(Parcel parcel) {
        int o = com.google.android.gms.common.internal.safeparcel.a.o(parcel);
        int i = 0;
        String str = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
        String str7 = null;
        String str8 = null;
        String str9 = null;
        boolean z = false;
        String str10 = null;
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
                    str2 = com.google.android.gms.common.internal.safeparcel.a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    str3 = com.google.android.gms.common.internal.safeparcel.a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    str4 = com.google.android.gms.common.internal.safeparcel.a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    str5 = com.google.android.gms.common.internal.safeparcel.a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    str6 = com.google.android.gms.common.internal.safeparcel.a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    str7 = com.google.android.gms.common.internal.safeparcel.a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    str8 = com.google.android.gms.common.internal.safeparcel.a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                    str9 = com.google.android.gms.common.internal.safeparcel.a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_EXPAND:
                    z = com.google.android.gms.common.internal.safeparcel.a.c(parcel, n);
                    break;
                case ApiEventType.API_MRAID_RESIZE:
                    str10 = com.google.android.gms.common.internal.safeparcel.a.n(parcel, n);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new Address(i, str, str2, str3, str4, str5, str6, str7, str8, str9, z, str10);
        }
        throw new com.google.android.gms.common.internal.safeparcel.a.a("Overread allowed size end=" + o, parcel);
    }

    public Address[] cj(int i) {
        return new Address[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return aX(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return cj(x0);
    }
}