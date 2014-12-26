package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.internal.gi;
import com.google.android.gms.internal.jm;
import com.google.android.gms.internal.jo;
import com.google.android.gms.internal.js;
import com.google.android.gms.internal.ju;
import com.google.android.gms.internal.jw;
import com.google.android.gms.internal.jy;
import com.google.android.gms.maps.model.LatLng;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.util.ArrayList;

public class j implements Creator<LoyaltyWalletObject> {
    static void a(LoyaltyWalletObject loyaltyWalletObject, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, loyaltyWalletObject.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, loyaltyWalletObject.eC, false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, loyaltyWalletObject.abz, false);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, loyaltyWalletObject.abA, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, loyaltyWalletObject.abB, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, loyaltyWalletObject.abC, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, loyaltyWalletObject.abD, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, loyaltyWalletObject.abE, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, loyaltyWalletObject.abF, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_USE_CUSTOM_CLOSE, loyaltyWalletObject.abG, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_EXPAND, loyaltyWalletObject.abH, false);
        b.c(parcel, ApiEventType.API_MRAID_RESIZE, loyaltyWalletObject.state);
        b.b(parcel, ApiEventType.API_MRAID_CLOSE, loyaltyWalletObject.abI, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_IS_VIEWABLE, loyaltyWalletObject.abJ, i, false);
        b.b(parcel, ApiEventType.API_MRAID_GET_PLACEMENT_TYPE, loyaltyWalletObject.abK, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_SCREEN_SIZE, loyaltyWalletObject.abM, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION, loyaltyWalletObject.abL, false);
        b.a(parcel, (int)Encoder.LINE_GROUPS, loyaltyWalletObject.abO);
        b.b(parcel, ApiEventType.API_MRAID_GET_CURRENT_POSITION, loyaltyWalletObject.abN, false);
        b.b(parcel, ApiEventType.API_MRAID_POST_TO_SOCIAL, loyaltyWalletObject.abQ, false);
        b.b(parcel, ApiEventType.API_MRAID_GET_MAX_SIZE, loyaltyWalletObject.abP, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_STORE_PICTURE, loyaltyWalletObject.abS, i, false);
        b.b(parcel, ApiEventType.API_MRAID_SUPPORTS, loyaltyWalletObject.abR, false);
        b.F(parcel, p);
    }

    public LoyaltyWalletObject bf(Parcel parcel) {
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
        int i2 = 0;
        ArrayList fs = gi.fs();
        ju juVar = null;
        ArrayList fs2 = gi.fs();
        String str11 = null;
        String str12 = null;
        ArrayList fs3 = gi.fs();
        boolean z = false;
        ArrayList fs4 = gi.fs();
        ArrayList fs5 = gi.fs();
        ArrayList fs6 = gi.fs();
        jo joVar = null;
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
                    i2 = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_CLOSE:
                    fs = a.c(parcel, n, jy.CREATOR);
                    break;
                case ApiEventType.API_MRAID_IS_VIEWABLE:
                    juVar = (ju) a.a(parcel, n, ju.CREATOR);
                    break;
                case ApiEventType.API_MRAID_GET_PLACEMENT_TYPE:
                    fs2 = a.c(parcel, n, LatLng.CREATOR);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION:
                    str11 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_SCREEN_SIZE:
                    str12 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_CURRENT_POSITION:
                    fs3 = a.c(parcel, n, jm.CREATOR);
                    break;
                case Encoder.LINE_GROUPS:
                    z = a.c(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_MAX_SIZE:
                    fs4 = a.c(parcel, n, jw.CREATOR);
                    break;
                case ApiEventType.API_MRAID_POST_TO_SOCIAL:
                    fs5 = a.c(parcel, n, js.CREATOR);
                    break;
                case ApiEventType.API_MRAID_SUPPORTS:
                    fs6 = a.c(parcel, n, jw.CREATOR);
                    break;
                case ApiEventType.API_MRAID_STORE_PICTURE:
                    joVar = (jo) a.a(parcel, n, jo.CREATOR);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new LoyaltyWalletObject(i, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, i2, fs, juVar, fs2, str11, str12, fs3, z, fs4, fs5, fs6, joVar);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public LoyaltyWalletObject[] cr(int i) {
        return new LoyaltyWalletObject[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return bf(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return cr(x0);
    }
}