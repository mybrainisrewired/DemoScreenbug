package com.google.android.gms.drive.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.millennialmedia.android.MMAdView;

public class aa implements Creator<OnDownloadProgressResponse> {
    static void a(OnDownloadProgressResponse onDownloadProgressResponse, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, onDownloadProgressResponse.xH);
        b.a(parcel, (int)MMAdView.TRANSITION_UP, onDownloadProgressResponse.FF);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, onDownloadProgressResponse.FG);
        b.F(parcel, p);
    }

    public OnDownloadProgressResponse O(Parcel parcel) {
        long j = 0;
        int o = a.o(parcel);
        int i = 0;
        long j2 = 0;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    j2 = a.i(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    j = a.i(parcel, n);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new OnDownloadProgressResponse(i, j2, j);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public OnDownloadProgressResponse[] as(int i) {
        return new OnDownloadProgressResponse[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return O(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return as(x0);
    }
}