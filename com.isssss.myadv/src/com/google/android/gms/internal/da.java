package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.util.List;

public class da implements Creator<cz> {
    static void a(cz czVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, czVar.versionCode);
        b.a(parcel, (int)MMAdView.TRANSITION_UP, czVar.ol, false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, czVar.pm, false);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, czVar.ne, false);
        b.c(parcel, ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, czVar.errorCode);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, czVar.nf, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, czVar.pn);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, czVar.po);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, czVar.pp);
        b.a(parcel, (int)ApiEventType.API_MRAID_USE_CUSTOM_CLOSE, czVar.pq, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_EXPAND, czVar.ni);
        b.c(parcel, ApiEventType.API_MRAID_RESIZE, czVar.orientation);
        b.a(parcel, (int)ApiEventType.API_MRAID_CLOSE, czVar.pr, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_IS_VIEWABLE, czVar.ps);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_PLACEMENT_TYPE, czVar.pt, false);
        b.a(parcel, (int)Encoder.LINE_GROUPS, czVar.pv, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_CURRENT_POSITION, czVar.pu);
        b.a(parcel, (int)ApiEventType.API_MRAID_POST_TO_SOCIAL, czVar.pw, false);
        b.F(parcel, p);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return g(x0);
    }

    public cz g(Parcel parcel) {
        int o = a.o(parcel);
        int i = 0;
        String str = null;
        String str2 = null;
        List list = null;
        int i2 = 0;
        List list2 = null;
        long j = 0;
        boolean z = false;
        long j2 = 0;
        List list3 = null;
        long j3 = 0;
        int i3 = 0;
        String str3 = null;
        long j4 = 0;
        String str4 = null;
        boolean z2 = false;
        String str5 = null;
        String str6 = null;
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
                    list = a.A(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    i2 = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    list2 = a.A(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    j = a.i(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    z = a.c(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    j2 = a.i(parcel, n);
                    break;
                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                    list3 = a.A(parcel, n);
                    break;
                case ApiEventType.API_MRAID_EXPAND:
                    j3 = a.i(parcel, n);
                    break;
                case ApiEventType.API_MRAID_RESIZE:
                    i3 = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_CLOSE:
                    str3 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_IS_VIEWABLE:
                    j4 = a.i(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_PLACEMENT_TYPE:
                    str4 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_CURRENT_POSITION:
                    z2 = a.c(parcel, n);
                    break;
                case Encoder.LINE_GROUPS:
                    str5 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_POST_TO_SOCIAL:
                    str6 = a.n(parcel, n);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new cz(i, str, str2, list, i2, list2, j, z, j2, list3, j3, i3, str3, j4, str4, z2, str5, str6);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public cz[] l(int i) {
        return new cz[i];
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return l(x0);
    }
}