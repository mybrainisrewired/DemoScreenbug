package com.google.android.gms.drive.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.drive.DriveId;
import com.millennialmedia.android.MMAdView;

public class am implements Creator<TrashResourceRequest> {
    static void a(TrashResourceRequest trashResourceRequest, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, trashResourceRequest.xH);
        b.a(parcel, (int)MMAdView.TRANSITION_UP, trashResourceRequest.EV, i, false);
        b.F(parcel, p);
    }

    public TrashResourceRequest Z(Parcel parcel) {
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
            return new TrashResourceRequest(i, driveId);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public TrashResourceRequest[] aD(int i) {
        return new TrashResourceRequest[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return Z(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return aD(x0);
    }
}