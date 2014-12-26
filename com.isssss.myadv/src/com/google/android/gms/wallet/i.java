package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class i implements Creator<LineItem> {
    static void a(LineItem lineItem, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, lineItem.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, lineItem.description, false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, lineItem.abv, false);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, lineItem.abw, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, lineItem.abc, false);
        b.c(parcel, ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, lineItem.abx);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, lineItem.abd, false);
        b.F(parcel, p);
    }

    public LineItem be(Parcel parcel) {
        int i = 0;
        String str = null;
        int o = a.o(parcel);
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        int i2 = 0;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i2 = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    str5 = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    str4 = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    str3 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    str2 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    i = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    str = a.n(parcel, n);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new LineItem(i2, str5, str4, str3, str2, i, str);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public LineItem[] cq(int i) {
        return new LineItem[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return be(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return cq(x0);
    }
}