package com.google.android.gms.drive.events;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.drive.DriveId;
import com.millennialmedia.android.MMAdView;

public class a implements Creator<ChangeEvent> {
    static void a(ChangeEvent changeEvent, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, changeEvent.xH);
        b.a(parcel, (int)MMAdView.TRANSITION_UP, changeEvent.Ew, i, false);
        b.c(parcel, MMAdView.TRANSITION_DOWN, changeEvent.ER);
        b.F(parcel, p);
    }

    public ChangeEvent A(Parcel parcel) {
        int i = 0;
        int o = com.google.android.gms.common.internal.safeparcel.a.o(parcel);
        DriveId driveId = null;
        int i2 = 0;
        while (parcel.dataPosition() < o) {
            DriveId driveId2;
            int i3;
            int n = com.google.android.gms.common.internal.safeparcel.a.n(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    int i4 = i;
                    driveId2 = driveId;
                    i3 = com.google.android.gms.common.internal.safeparcel.a.g(parcel, n);
                    n = i4;
                    break;
                case MMAdView.TRANSITION_UP:
                    i3 = i2;
                    DriveId driveId3 = (DriveId) com.google.android.gms.common.internal.safeparcel.a.a(parcel, n, DriveId.CREATOR);
                    n = i;
                    driveId2 = driveId3;
                    break;
                case MMAdView.TRANSITION_DOWN:
                    n = com.google.android.gms.common.internal.safeparcel.a.g(parcel, n);
                    driveId2 = driveId;
                    i3 = i2;
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, n);
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
            return new ChangeEvent(i2, driveId, i);
        }
        throw new com.google.android.gms.common.internal.safeparcel.a.a("Overread allowed size end=" + o, parcel);
    }

    public ChangeEvent[] ae(int i) {
        return new ChangeEvent[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return A(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return ae(x0);
    }
}