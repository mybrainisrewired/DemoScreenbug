package com.google.android.gms.common.data;

import android.database.CursorWindow;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.millennialmedia.android.MMAdView;

public class DataHolderCreator implements Creator<DataHolder> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void a(DataHolder dataHolder, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.a(parcel, 1, dataHolder.er(), false);
        b.c(parcel, GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE, dataHolder.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, dataHolder.es(), i, false);
        b.c(parcel, MMAdView.TRANSITION_DOWN, dataHolder.getStatusCode());
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, dataHolder.getMetadata(), false);
        b.F(parcel, p);
    }

    public DataHolder createFromParcel(Parcel parcel) {
        int i = 0;
        Bundle bundle = null;
        int o = a.o(parcel);
        CursorWindow[] cursorWindowArr = null;
        String[] strArr = null;
        int i2 = 0;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    strArr = a.z(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    cursorWindowArr = (CursorWindow[]) a.b(parcel, n, CursorWindow.CREATOR);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    i = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    bundle = a.p(parcel, n);
                    break;
                case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                    i2 = a.g(parcel, n);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() != o) {
            throw new a.a("Overread allowed size end=" + o, parcel);
        }
        DataHolder dataHolder = new DataHolder(i2, strArr, cursorWindowArr, i, bundle);
        dataHolder.validateContents();
        return dataHolder;
    }

    public DataHolder[] newArray(int size) {
        return new DataHolder[size];
    }
}