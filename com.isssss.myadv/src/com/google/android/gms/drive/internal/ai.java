package com.google.android.gms.drive.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.drive.DriveId;
import com.millennialmedia.android.MMAdView;

public class ai implements Creator<OpenFileIntentSenderRequest> {
    static void a(OpenFileIntentSenderRequest openFileIntentSenderRequest, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, openFileIntentSenderRequest.xH);
        b.a(parcel, (int)MMAdView.TRANSITION_UP, openFileIntentSenderRequest.EB, false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, openFileIntentSenderRequest.EQ, false);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, openFileIntentSenderRequest.EC, i, false);
        b.F(parcel, p);
    }

    public OpenFileIntentSenderRequest W(Parcel parcel) {
        DriveId driveId = null;
        int o = a.o(parcel);
        int i = 0;
        String[] strArr = null;
        String str = null;
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
                    strArr = a.z(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    driveId = (DriveId) a.a(parcel, n, DriveId.CREATOR);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new OpenFileIntentSenderRequest(i, str, strArr, driveId);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public OpenFileIntentSenderRequest[] aA(int i) {
        return new OpenFileIntentSenderRequest[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return W(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return aA(x0);
    }
}