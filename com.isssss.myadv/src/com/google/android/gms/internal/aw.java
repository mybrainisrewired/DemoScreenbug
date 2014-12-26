package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class aw implements Creator<av> {
    static void a(av avVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, avVar.versionCode);
        b.c(parcel, MMAdView.TRANSITION_UP, avVar.mq);
        b.c(parcel, MMAdView.TRANSITION_DOWN, avVar.backgroundColor);
        b.c(parcel, MMAdView.TRANSITION_RANDOM, avVar.mr);
        b.c(parcel, ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, avVar.ms);
        b.c(parcel, ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, avVar.mt);
        b.c(parcel, ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, avVar.mu);
        b.c(parcel, ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, avVar.mv);
        b.c(parcel, ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, avVar.mw);
        b.a(parcel, (int)ApiEventType.API_MRAID_USE_CUSTOM_CLOSE, avVar.mx, false);
        b.c(parcel, ApiEventType.API_MRAID_EXPAND, avVar.my);
        b.a(parcel, (int)ApiEventType.API_MRAID_RESIZE, avVar.mz, false);
        b.c(parcel, ApiEventType.API_MRAID_CLOSE, avVar.mA);
        b.c(parcel, ApiEventType.API_MRAID_IS_VIEWABLE, avVar.mB);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_PLACEMENT_TYPE, avVar.mC, false);
        b.F(parcel, p);
    }

    public av c(Parcel parcel) {
        int o = a.o(parcel);
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        int i9 = 0;
        String str = null;
        int i10 = 0;
        String str2 = null;
        int i11 = 0;
        int i12 = 0;
        String str3 = null;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    i2 = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    i3 = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    i4 = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    i5 = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    i6 = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    i7 = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    i8 = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    i9 = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                    str = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_EXPAND:
                    i10 = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_RESIZE:
                    str2 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_CLOSE:
                    i11 = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_IS_VIEWABLE:
                    i12 = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_PLACEMENT_TYPE:
                    str3 = a.n(parcel, n);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new av(i, i2, i3, i4, i5, i6, i7, i8, i9, str, i10, str2, i11, i12, str3);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return c(x0);
    }

    public av[] e(int i) {
        return new av[i];
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return e(x0);
    }
}