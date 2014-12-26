package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.identity.intents.model.CountrySpecification;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.util.ArrayList;

public class l implements Creator<MaskedWalletRequest> {
    static void a(MaskedWalletRequest maskedWalletRequest, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, maskedWalletRequest.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, maskedWalletRequest.abi, false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, maskedWalletRequest.abV);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, maskedWalletRequest.abW);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, maskedWalletRequest.abX);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, maskedWalletRequest.abY, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, maskedWalletRequest.abd, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, maskedWalletRequest.abZ, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, maskedWalletRequest.abr, i, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_USE_CUSTOM_CLOSE, maskedWalletRequest.aca);
        b.a(parcel, (int)ApiEventType.API_MRAID_EXPAND, maskedWalletRequest.acb);
        b.a(parcel, (int)ApiEventType.API_MRAID_RESIZE, maskedWalletRequest.acc, i, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_CLOSE, maskedWalletRequest.acd);
        b.a(parcel, (int)ApiEventType.API_MRAID_IS_VIEWABLE, maskedWalletRequest.ace);
        b.b(parcel, ApiEventType.API_MRAID_GET_PLACEMENT_TYPE, maskedWalletRequest.acf, false);
        b.F(parcel, p);
    }

    public MaskedWalletRequest bh(Parcel parcel) {
        int o = a.o(parcel);
        int i = 0;
        String str = null;
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        Cart cart = null;
        boolean z4 = false;
        boolean z5 = false;
        CountrySpecification[] countrySpecificationArr = null;
        boolean z6 = true;
        boolean z7 = true;
        ArrayList arrayList = null;
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
                    z = a.c(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    z2 = a.c(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    z3 = a.c(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    str2 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    str3 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    str4 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    cart = (Cart) a.a(parcel, n, Cart.CREATOR);
                    break;
                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                    z4 = a.c(parcel, n);
                    break;
                case ApiEventType.API_MRAID_EXPAND:
                    z5 = a.c(parcel, n);
                    break;
                case ApiEventType.API_MRAID_RESIZE:
                    countrySpecificationArr = (CountrySpecification[]) a.b(parcel, n, CountrySpecification.CREATOR);
                    break;
                case ApiEventType.API_MRAID_CLOSE:
                    z6 = a.c(parcel, n);
                    break;
                case ApiEventType.API_MRAID_IS_VIEWABLE:
                    z7 = a.c(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_PLACEMENT_TYPE:
                    arrayList = a.c(parcel, n, CountrySpecification.CREATOR);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new MaskedWalletRequest(i, str, z, z2, z3, str2, str3, str4, cart, z4, z5, countrySpecificationArr, z6, z7, arrayList);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return bh(x0);
    }

    public MaskedWalletRequest[] ct(int i) {
        return new MaskedWalletRequest[i];
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return ct(x0);
    }
}