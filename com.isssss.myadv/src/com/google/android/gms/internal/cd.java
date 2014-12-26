package com.google.android.gms.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class cd implements Creator<ce> {
    static void a(ce ceVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, ceVar.versionCode);
        b.a(parcel, (int)MMAdView.TRANSITION_UP, ceVar.og, i, false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, ceVar.aO(), false);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, ceVar.aP(), false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, ceVar.aQ(), false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, ceVar.aR(), false);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, ceVar.ol, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, ceVar.om);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, ceVar.on, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_USE_CUSTOM_CLOSE, ceVar.aT(), false);
        b.c(parcel, ApiEventType.API_MRAID_EXPAND, ceVar.orientation);
        b.c(parcel, ApiEventType.API_MRAID_RESIZE, ceVar.op);
        b.a(parcel, (int)ApiEventType.API_MRAID_CLOSE, ceVar.nO, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_IS_VIEWABLE, ceVar.kK, i, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_PLACEMENT_TYPE, ceVar.aS(), false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION, ceVar.or, false);
        b.F(parcel, p);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return e(x0);
    }

    public ce e(Parcel parcel) {
        int o = a.o(parcel);
        int i = 0;
        cb cbVar = null;
        IBinder iBinder = null;
        IBinder iBinder2 = null;
        IBinder iBinder3 = null;
        IBinder iBinder4 = null;
        String str = null;
        boolean z = false;
        String str2 = null;
        IBinder iBinder5 = null;
        int i2 = 0;
        int i3 = 0;
        String str3 = null;
        dx dxVar = null;
        IBinder iBinder6 = null;
        String str4 = null;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    cbVar = (cb) a.a(parcel, n, cb.CREATOR);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    iBinder = a.o(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    iBinder2 = a.o(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    iBinder3 = a.o(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    iBinder4 = a.o(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    str = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    z = a.c(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    str2 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                    iBinder5 = a.o(parcel, n);
                    break;
                case ApiEventType.API_MRAID_EXPAND:
                    i2 = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_RESIZE:
                    i3 = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_CLOSE:
                    str3 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_IS_VIEWABLE:
                    dxVar = (dx) a.a(parcel, n, dx.CREATOR);
                    break;
                case ApiEventType.API_MRAID_GET_PLACEMENT_TYPE:
                    iBinder6 = a.o(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION:
                    str4 = a.n(parcel, n);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new ce(i, cbVar, iBinder, iBinder2, iBinder3, iBinder4, str, z, str2, iBinder5, i2, i3, str3, dxVar, iBinder6, str4);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public ce[] i(int i) {
        return new ce[i];
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return i(x0);
    }
}