package com.google.android.gms.internal;

import android.location.Location;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.util.List;

public class ai implements Creator<ah> {
    static void a(ah ahVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, ahVar.versionCode);
        b.a(parcel, (int)MMAdView.TRANSITION_UP, ahVar.lH);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, ahVar.extras, false);
        b.c(parcel, MMAdView.TRANSITION_RANDOM, ahVar.lI);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, ahVar.lJ, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, ahVar.lK);
        b.c(parcel, ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, ahVar.lL);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, ahVar.lM);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, ahVar.lN, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_USE_CUSTOM_CLOSE, ahVar.lO, i, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_EXPAND, ahVar.lP, i, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_RESIZE, ahVar.lQ, false);
        b.F(parcel, p);
    }

    public ah a(Parcel parcel) {
        int o = a.o(parcel);
        int i = 0;
        long j = 0;
        Bundle bundle = null;
        int i2 = 0;
        List list = null;
        boolean z = false;
        int i3 = 0;
        boolean z2 = false;
        String str = null;
        av avVar = null;
        Location location = null;
        String str2 = null;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    j = a.i(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    bundle = a.p(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    i2 = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    list = a.A(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    z = a.c(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    i3 = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    z2 = a.c(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    str = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                    avVar = (av) a.a(parcel, n, av.CREATOR);
                    break;
                case ApiEventType.API_MRAID_EXPAND:
                    location = (Location) a.a(parcel, n, Location.CREATOR);
                    break;
                case ApiEventType.API_MRAID_RESIZE:
                    str2 = a.n(parcel, n);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new ah(i, j, bundle, i2, list, z, i3, z2, str, avVar, location, str2);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public ah[] b(int i) {
        return new ah[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return a(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return b(x0);
    }
}