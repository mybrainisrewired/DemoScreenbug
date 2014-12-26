package com.google.android.gms.drive;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.millennialmedia.android.MMAdView;

public class d implements Creator<DriveId> {
    static void a(DriveId driveId, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, driveId.xH);
        b.a(parcel, (int)MMAdView.TRANSITION_UP, driveId.EH, false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, driveId.EI);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, driveId.EJ);
        b.F(parcel, p);
    }

    public DriveId[] ad(int i) {
        return new DriveId[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return z(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return ad(x0);
    }

    public DriveId z(Parcel parcel) {
        long j = 0;
        int o = a.o(parcel);
        int i = 0;
        String str = null;
        long j2 = 0;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    str = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    j2 = a.i(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    j = a.i(parcel, n);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new DriveId(i, str, j2, j);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }
}