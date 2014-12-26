package com.google.android.gms.drive.events;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.drive.DriveId;
import com.millennialmedia.android.MMAdView;

public class b implements Creator<ConflictEvent> {
    static void a(ConflictEvent conflictEvent, Parcel parcel, int i) {
        int p = com.google.android.gms.common.internal.safeparcel.b.p(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, conflictEvent.xH);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, (int)MMAdView.TRANSITION_UP, conflictEvent.Ew, i, false);
        com.google.android.gms.common.internal.safeparcel.b.F(parcel, p);
    }

    public ConflictEvent B(Parcel parcel) {
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
            return new ConflictEvent(i, driveId);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public ConflictEvent[] af(int i) {
        return new ConflictEvent[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return B(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return af(x0);
    }
}