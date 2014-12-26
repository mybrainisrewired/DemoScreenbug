package com.google.android.gms.internal;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class cy implements Creator<cx> {
    static void a(cx cxVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, cxVar.versionCode);
        b.a(parcel, (int)MMAdView.TRANSITION_UP, cxVar.pf, false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, cxVar.pg, i, false);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, cxVar.kN, i, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, cxVar.kH, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, cxVar.applicationInfo, i, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, cxVar.ph, i, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, cxVar.pi, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, cxVar.pj, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_USE_CUSTOM_CLOSE, cxVar.pk, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_EXPAND, cxVar.kK, i, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_RESIZE, cxVar.pl, false);
        b.F(parcel, p);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return f(x0);
    }

    public cx f(Parcel parcel) {
        int o = a.o(parcel);
        int i = 0;
        Bundle bundle = null;
        ah ahVar = null;
        ak akVar = null;
        String str = null;
        ApplicationInfo applicationInfo = null;
        PackageInfo packageInfo = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        dx dxVar = null;
        Bundle bundle2 = null;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    bundle = a.p(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    ahVar = (ah) a.a(parcel, n, ah.CREATOR);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    akVar = (ak) a.a(parcel, n, ak.CREATOR);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    str = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    applicationInfo = (ApplicationInfo) a.a(parcel, n, ApplicationInfo.CREATOR);
                    break;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    packageInfo = (PackageInfo) a.a(parcel, n, PackageInfo.CREATOR);
                    break;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    str2 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    str3 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                    str4 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_EXPAND:
                    dxVar = (dx) a.a(parcel, n, dx.CREATOR);
                    break;
                case ApiEventType.API_MRAID_RESIZE:
                    bundle2 = a.p(parcel, n);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new cx(i, bundle, ahVar, akVar, str, applicationInfo, packageInfo, str2, str3, str4, dxVar, bundle2);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public cx[] k(int i) {
        return new cx[i];
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return k(x0);
    }
}