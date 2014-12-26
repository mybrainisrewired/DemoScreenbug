package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.util.HashSet;
import java.util.Set;

public class if_ implements Creator<ie> {
    static void a(ie ieVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        Set ja = ieVar.ja();
        if (ja.contains(Integer.valueOf(1))) {
            b.c(parcel, 1, ieVar.getVersionCode());
        }
        if (ja.contains(Integer.valueOf(MMAdView.TRANSITION_UP))) {
            b.a(parcel, (int)MMAdView.TRANSITION_UP, ieVar.getId(), true);
        }
        if (ja.contains(Integer.valueOf(MMAdView.TRANSITION_RANDOM))) {
            b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, ieVar.jr(), i, true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, ieVar.getStartDate(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, ieVar.js(), i, true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, ieVar.getType(), true);
        }
        b.F(parcel, p);
    }

    public ie aM(Parcel parcel) {
        String str = null;
        int o = a.o(parcel);
        Set hashSet = new HashSet();
        int i = 0;
        ic icVar = null;
        String str2 = null;
        ic icVar2 = null;
        String str3 = null;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            ic icVar3;
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i = a.g(parcel, n);
                    hashSet.add(Integer.valueOf(1));
                    break;
                case MMAdView.TRANSITION_UP:
                    str3 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(MMAdView.TRANSITION_UP));
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    icVar3 = (ic) a.a(parcel, n, ic.CREATOR);
                    hashSet.add(Integer.valueOf(MMAdView.TRANSITION_RANDOM));
                    icVar2 = icVar3;
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    str2 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES));
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    icVar3 = (ic) a.a(parcel, n, ic.CREATOR);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES));
                    icVar = icVar3;
                    break;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    str = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES));
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new ie(hashSet, i, str3, icVar2, str2, icVar, str);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public ie[] bP(int i) {
        return new ie[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return aM(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return bP(x0);
    }
}