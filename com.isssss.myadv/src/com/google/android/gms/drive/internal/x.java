package com.google.android.gms.drive.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.drive.DriveId;
import com.millennialmedia.android.MMAdView;

public class x implements Creator<ListParentsRequest> {
    static void a(ListParentsRequest listParentsRequest, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, listParentsRequest.xH);
        b.a(parcel, (int)MMAdView.TRANSITION_UP, listParentsRequest.FB, i, false);
        b.F(parcel, p);
    }

    public ListParentsRequest M(Parcel parcel) {
        int o = a.o(parcel);
        int i = 0;
        DriveId driveId = null;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    driveId = (DriveId) a.a(parcel, n, DriveId.CREATOR);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new ListParentsRequest(i, driveId);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public ListParentsRequest[] aq(int i) {
        return new ListParentsRequest[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return M(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return aq(x0);
    }
}