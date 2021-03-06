package com.google.android.gms.drive.query.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.drive.metadata.internal.MetadataBundle;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.millennialmedia.android.MMAdView;

public class b implements Creator<FieldOnlyFilter> {
    static void a(FieldOnlyFilter fieldOnlyFilter, Parcel parcel, int i) {
        int p = com.google.android.gms.common.internal.safeparcel.b.p(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE, fieldOnlyFilter.xH);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 1, fieldOnlyFilter.GH, i, false);
        com.google.android.gms.common.internal.safeparcel.b.F(parcel, p);
    }

    public FieldOnlyFilter[] aJ(int i) {
        return new FieldOnlyFilter[i];
    }

    public FieldOnlyFilter af(Parcel parcel) {
        int o = a.o(parcel);
        int i = 0;
        MetadataBundle metadataBundle = null;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    metadataBundle = (MetadataBundle) a.a(parcel, n, MetadataBundle.CREATOR);
                    break;
                case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                    i = a.g(parcel, n);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new FieldOnlyFilter(i, metadataBundle);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return af(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return aJ(x0);
    }
}