package com.google.android.gms.drive.query.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class d implements Creator<FilterHolder> {
    static void a(FilterHolder filterHolder, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.a(parcel, 1, filterHolder.GK, i, false);
        b.c(parcel, GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE, filterHolder.xH);
        b.a(parcel, (int)MMAdView.TRANSITION_UP, filterHolder.GL, i, false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, filterHolder.GM, i, false);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, filterHolder.GN, i, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, filterHolder.GO, i, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, filterHolder.GP, i, false);
        b.F(parcel, p);
    }

    public FilterHolder[] aL(int i) {
        return new FilterHolder[i];
    }

    public FilterHolder ah(Parcel parcel) {
        MatchAllFilter matchAllFilter = null;
        int o = a.o(parcel);
        int i = 0;
        InFilter inFilter = null;
        NotFilter notFilter = null;
        LogicalFilter logicalFilter = null;
        FieldOnlyFilter fieldOnlyFilter = null;
        ComparisonFilter comparisonFilter = null;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    comparisonFilter = (ComparisonFilter) a.a(parcel, n, ComparisonFilter.CREATOR);
                    break;
                case MMAdView.TRANSITION_UP:
                    fieldOnlyFilter = (FieldOnlyFilter) a.a(parcel, n, FieldOnlyFilter.CREATOR);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    logicalFilter = (LogicalFilter) a.a(parcel, n, LogicalFilter.CREATOR);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    notFilter = (NotFilter) a.a(parcel, n, NotFilter.CREATOR);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    inFilter = (InFilter) a.a(parcel, n, InFilter.CREATOR);
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    matchAllFilter = (MatchAllFilter) a.a(parcel, n, MatchAllFilter.CREATOR);
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
            return new FilterHolder(i, comparisonFilter, fieldOnlyFilter, logicalFilter, notFilter, inFilter, matchAllFilter);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return ah(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return aL(x0);
    }
}