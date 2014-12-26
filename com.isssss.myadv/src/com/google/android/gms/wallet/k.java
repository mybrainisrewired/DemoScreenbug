package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.identity.intents.model.UserAddress;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class k implements Creator<MaskedWallet> {
    static void a(MaskedWallet maskedWallet, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, maskedWallet.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, maskedWallet.abh, false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, maskedWallet.abi, false);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, maskedWallet.abn, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, maskedWallet.abk, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, maskedWallet.abl, i, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, maskedWallet.abm, i, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, maskedWallet.abT, i, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, maskedWallet.abU, i, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_USE_CUSTOM_CLOSE, maskedWallet.abo, i, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_EXPAND, maskedWallet.abp, i, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_RESIZE, maskedWallet.abq, i, false);
        b.F(parcel, p);
    }

    public MaskedWallet bg(Parcel parcel) {
        int o = a.o(parcel);
        int i = 0;
        String str = null;
        String str2 = null;
        String[] strArr = null;
        String str3 = null;
        Address address = null;
        Address address2 = null;
        LoyaltyWalletObject[] loyaltyWalletObjectArr = null;
        OfferWalletObject[] offerWalletObjectArr = null;
        UserAddress userAddress = null;
        UserAddress userAddress2 = null;
        InstrumentInfo[] instrumentInfoArr = null;
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
                    strArr = a.z(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    str3 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    address = (Address) a.a(parcel, n, Address.CREATOR);
                    break;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    address2 = (Address) a.a(parcel, n, Address.CREATOR);
                    break;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    loyaltyWalletObjectArr = (LoyaltyWalletObject[]) a.b(parcel, n, LoyaltyWalletObject.CREATOR);
                    break;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    offerWalletObjectArr = (OfferWalletObject[]) a.b(parcel, n, OfferWalletObject.CREATOR);
                    break;
                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                    userAddress = (UserAddress) a.a(parcel, n, UserAddress.CREATOR);
                    break;
                case ApiEventType.API_MRAID_EXPAND:
                    userAddress2 = (UserAddress) a.a(parcel, n, UserAddress.CREATOR);
                    break;
                case ApiEventType.API_MRAID_RESIZE:
                    instrumentInfoArr = (InstrumentInfo[]) a.b(parcel, n, InstrumentInfo.CREATOR);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new MaskedWallet(i, str, str2, strArr, str3, address, address2, loyaltyWalletObjectArr, offerWalletObjectArr, userAddress, userAddress2, instrumentInfoArr);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return bg(x0);
    }

    public MaskedWallet[] cs(int i) {
        return new MaskedWallet[i];
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return cs(x0);
    }
}