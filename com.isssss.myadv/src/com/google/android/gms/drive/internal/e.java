package com.google.android.gms.drive.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.drive.Contents;
import com.millennialmedia.android.MMAdView;

public class e implements Creator<CloseContentsRequest> {
    static void a(CloseContentsRequest closeContentsRequest, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, closeContentsRequest.xH);
        b.a(parcel, (int)MMAdView.TRANSITION_UP, closeContentsRequest.EX, i, false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, closeContentsRequest.EY, false);
        b.F(parcel, p);
    }

    public CloseContentsRequest F(Parcel parcel) {
        Boolean bool = null;
        int o = a.o(parcel);
        int i = 0;
        Contents contents = null;
        while (parcel.dataPosition() < o) {
            Contents contents2;
            int i2;
            Boolean bool2;
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    Boolean bool3 = bool;
                    contents2 = contents;
                    i2 = a.g(parcel, n);
                    bool2 = bool3;
                    break;
                case MMAdView.TRANSITION_UP:
                    i2 = i;
                    Contents contents3 = (Contents) a.a(parcel, n, Contents.CREATOR);
                    bool2 = bool;
                    contents2 = contents3;
                    break;
                case MMAdView.TRANSITION_DOWN:
                    bool2 = a.d(parcel, n);
                    contents2 = contents;
                    i2 = i;
                    break;
                default:
                    a.b(parcel, n);
                    bool2 = bool;
                    contents2 = contents;
                    i2 = i;
                    break;
            }
            i = i2;
            contents = contents2;
            bool = bool2;
        }
        if (parcel.dataPosition() == o) {
            return new CloseContentsRequest(i, contents, bool);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public CloseContentsRequest[] aj(int i) {
        return new CloseContentsRequest[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return F(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return aj(x0);
    }
}