package com.google.android.gms.drive.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.millennialmedia.android.MMAdView;

public class ad implements Creator<OnListEntriesResponse> {
    static void a(OnListEntriesResponse onListEntriesResponse, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, onListEntriesResponse.xH);
        b.a(parcel, (int)MMAdView.TRANSITION_UP, onListEntriesResponse.FJ, i, false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, onListEntriesResponse.Fg);
        b.F(parcel, p);
    }

    public OnListEntriesResponse R(Parcel parcel) {
        boolean z = false;
        int o = a.o(parcel);
        DataHolder dataHolder = null;
        int i = 0;
        while (parcel.dataPosition() < o) {
            DataHolder dataHolder2;
            int i2;
            boolean z2;
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    boolean z3 = z;
                    dataHolder2 = dataHolder;
                    i2 = a.g(parcel, n);
                    z2 = z3;
                    break;
                case MMAdView.TRANSITION_UP:
                    i2 = i;
                    DataHolder dataHolder3 = (DataHolder) a.a(parcel, n, DataHolder.CREATOR);
                    z2 = z;
                    dataHolder2 = dataHolder3;
                    break;
                case MMAdView.TRANSITION_DOWN:
                    z2 = a.c(parcel, n);
                    dataHolder2 = dataHolder;
                    i2 = i;
                    break;
                default:
                    a.b(parcel, n);
                    z2 = z;
                    dataHolder2 = dataHolder;
                    i2 = i;
                    break;
            }
            i = i2;
            dataHolder = dataHolder2;
            z = z2;
        }
        if (parcel.dataPosition() == o) {
            return new OnListEntriesResponse(i, dataHolder, z);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public OnListEntriesResponse[] av(int i) {
        return new OnListEntriesResponse[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return R(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return av(x0);
    }
}