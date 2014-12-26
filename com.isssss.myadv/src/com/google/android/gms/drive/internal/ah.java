package com.google.android.gms.drive.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.drive.DriveId;
import com.millennialmedia.android.MMAdView;

public class ah implements Creator<OpenContentsRequest> {
    static void a(OpenContentsRequest openContentsRequest, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, openContentsRequest.xH);
        b.a(parcel, (int)MMAdView.TRANSITION_UP, openContentsRequest.EV, i, false);
        b.c(parcel, MMAdView.TRANSITION_DOWN, openContentsRequest.Ev);
        b.F(parcel, p);
    }

    public OpenContentsRequest V(Parcel parcel) {
        int i = 0;
        int o = a.o(parcel);
        DriveId driveId = null;
        int i2 = 0;
        while (parcel.dataPosition() < o) {
            DriveId driveId2;
            int i3;
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    int i4 = i;
                    driveId2 = driveId;
                    i3 = a.g(parcel, n);
                    n = i4;
                    break;
                case MMAdView.TRANSITION_UP:
                    i3 = i2;
                    DriveId driveId3 = (DriveId) a.a(parcel, n, DriveId.CREATOR);
                    n = i;
                    driveId2 = driveId3;
                    break;
                case MMAdView.TRANSITION_DOWN:
                    n = a.g(parcel, n);
                    driveId2 = driveId;
                    i3 = i2;
                    break;
                default:
                    a.b(parcel, n);
                    n = i;
                    driveId2 = driveId;
                    i3 = i2;
                    break;
            }
            i2 = i3;
            driveId = driveId2;
            i = n;
        }
        if (parcel.dataPosition() == o) {
            return new OpenContentsRequest(i2, driveId, i);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public OpenContentsRequest[] az(int i) {
        return new OpenContentsRequest[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return V(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return az(x0);
    }
}