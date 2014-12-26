package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.internal.ih.f;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.util.HashSet;
import java.util.Set;

public class ip implements Creator<f> {
    static void a(f fVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        Set ja = fVar.ja();
        if (ja.contains(Integer.valueOf(1))) {
            b.c(parcel, 1, fVar.getVersionCode());
        }
        if (ja.contains(Integer.valueOf(MMAdView.TRANSITION_UP))) {
            b.a(parcel, (int)MMAdView.TRANSITION_UP, fVar.getDepartment(), true);
        }
        if (ja.contains(Integer.valueOf(MMAdView.TRANSITION_DOWN))) {
            b.a(parcel, (int)MMAdView.TRANSITION_DOWN, fVar.getDescription(), true);
        }
        if (ja.contains(Integer.valueOf(MMAdView.TRANSITION_RANDOM))) {
            b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, fVar.getEndDate(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, fVar.getLocation(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, fVar.getName(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, fVar.isPrimary());
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, fVar.getStartDate(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, fVar.getTitle(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_USE_CUSTOM_CLOSE))) {
            b.c(parcel, ApiEventType.API_MRAID_USE_CUSTOM_CLOSE, fVar.getType());
        }
        b.F(parcel, p);
    }

    public f aU(Parcel parcel) {
        int i = 0;
        String str = null;
        int o = a.o(parcel);
        Set hashSet = new HashSet();
        String str2 = null;
        boolean z = false;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
        String str7 = null;
        int i2 = 0;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i2 = a.g(parcel, n);
                    hashSet.add(Integer.valueOf(1));
                    break;
                case MMAdView.TRANSITION_UP:
                    str7 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(MMAdView.TRANSITION_UP));
                    break;
                case MMAdView.TRANSITION_DOWN:
                    str6 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(MMAdView.TRANSITION_DOWN));
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    str5 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(MMAdView.TRANSITION_RANDOM));
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    str4 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES));
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    str3 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES));
                    break;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    z = a.c(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES));
                    break;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    str2 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES));
                    break;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    str = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES));
                    break;
                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                    i = a.g(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_USE_CUSTOM_CLOSE));
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new f(hashSet, i2, str7, str6, str5, str4, str3, z, str2, str, i);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public f[] bX(int i) {
        return new f[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return aU(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return bX(x0);
    }
}