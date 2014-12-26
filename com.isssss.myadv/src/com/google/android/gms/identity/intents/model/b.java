package com.google.android.gms.identity.intents.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class b implements Creator<UserAddress> {
    static void a(UserAddress userAddress, Parcel parcel, int i) {
        int p = com.google.android.gms.common.internal.safeparcel.b.p(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, userAddress.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, (int)MMAdView.TRANSITION_UP, userAddress.name, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, (int)MMAdView.TRANSITION_DOWN, userAddress.NB, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, userAddress.NC, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, userAddress.ND, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, (int)ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, userAddress.NE, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, (int)ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, userAddress.NF, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, (int)ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, userAddress.NG, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, (int)ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, userAddress.NH, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, (int)ApiEventType.API_MRAID_USE_CUSTOM_CLOSE, userAddress.qd, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, (int)ApiEventType.API_MRAID_EXPAND, userAddress.NI, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, (int)ApiEventType.API_MRAID_RESIZE, userAddress.NJ, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, (int)ApiEventType.API_MRAID_CLOSE, userAddress.NK, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, (int)ApiEventType.API_MRAID_IS_VIEWABLE, userAddress.NL);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, (int)ApiEventType.API_MRAID_GET_PLACEMENT_TYPE, userAddress.NM, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION, userAddress.NN, false);
        com.google.android.gms.common.internal.safeparcel.b.F(parcel, p);
    }

    public UserAddress aA(Parcel parcel) {
        int o = a.o(parcel);
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
        String str10 = null;
        String str11 = null;
        String str12 = null;
        boolean z = false;
        String str13 = null;
        String str14 = null;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    str = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    str2 = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    str3 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    str4 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    str5 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    str6 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    str7 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    str8 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                    str9 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_EXPAND:
                    str10 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_RESIZE:
                    str11 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_CLOSE:
                    str12 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_IS_VIEWABLE:
                    z = a.c(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_PLACEMENT_TYPE:
                    str13 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION:
                    str14 = a.n(parcel, n);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new UserAddress(i, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, z, str13, str14);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public UserAddress[] bu(int i) {
        return new UserAddress[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return aA(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return bu(x0);
    }
}