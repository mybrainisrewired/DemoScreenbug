package com.google.android.gms.drive.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.drive.query.Query;
import com.millennialmedia.android.MMAdView;

public class aj implements Creator<QueryRequest> {
    static void a(QueryRequest queryRequest, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, queryRequest.xH);
        b.a(parcel, (int)MMAdView.TRANSITION_UP, queryRequest.FL, i, false);
        b.F(parcel, p);
    }

    public QueryRequest X(Parcel parcel) {
        int o = a.o(parcel);
        int i = 0;
        Query query = null;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    query = (Query) a.a(parcel, n, Query.CREATOR);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new QueryRequest(i, query);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public QueryRequest[] aB(int i) {
        return new QueryRequest[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return X(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return aB(x0);
    }
}