package com.google.android.gms.drive.internal;

import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.drive.DriveId;
import com.millennialmedia.android.MMAdView;

public class a implements Creator<AddEventListenerRequest> {
    static void a(AddEventListenerRequest addEventListenerRequest, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, addEventListenerRequest.xH);
        b.a(parcel, (int)MMAdView.TRANSITION_UP, addEventListenerRequest.Ew, i, false);
        b.c(parcel, MMAdView.TRANSITION_DOWN, addEventListenerRequest.ES);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, addEventListenerRequest.ET, i, false);
        b.F(parcel, p);
    }

    public AddEventListenerRequest C(Parcel parcel) {
        PendingIntent pendingIntent = null;
        int i = 0;
        int o = com.google.android.gms.common.internal.safeparcel.a.o(parcel);
        DriveId driveId = null;
        int i2 = 0;
        while (parcel.dataPosition() < o) {
            int i3;
            DriveId driveId2;
            int i4;
            PendingIntent pendingIntent2;
            int n = com.google.android.gms.common.internal.safeparcel.a.n(parcel);
            PendingIntent pendingIntent3;
            switch (com.google.android.gms.common.internal.safeparcel.a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    pendingIntent3 = pendingIntent;
                    i3 = i;
                    driveId2 = driveId;
                    i4 = com.google.android.gms.common.internal.safeparcel.a.g(parcel, n);
                    pendingIntent2 = pendingIntent3;
                    break;
                case MMAdView.TRANSITION_UP:
                    i4 = i2;
                    int i5 = i;
                    driveId2 = (DriveId) com.google.android.gms.common.internal.safeparcel.a.a(parcel, n, DriveId.CREATOR);
                    pendingIntent2 = pendingIntent;
                    i3 = i5;
                    break;
                case MMAdView.TRANSITION_DOWN:
                    driveId2 = driveId;
                    i4 = i2;
                    pendingIntent3 = pendingIntent;
                    i3 = com.google.android.gms.common.internal.safeparcel.a.g(parcel, n);
                    pendingIntent2 = pendingIntent3;
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    pendingIntent2 = (PendingIntent) com.google.android.gms.common.internal.safeparcel.a.a(parcel, n, PendingIntent.CREATOR);
                    i3 = i;
                    driveId2 = driveId;
                    i4 = i2;
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, n);
                    pendingIntent2 = pendingIntent;
                    i3 = i;
                    driveId2 = driveId;
                    i4 = i2;
                    break;
            }
            i2 = i4;
            driveId = driveId2;
            i = i3;
            pendingIntent = pendingIntent2;
        }
        if (parcel.dataPosition() == o) {
            return new AddEventListenerRequest(i2, driveId, i, pendingIntent);
        }
        throw new com.google.android.gms.common.internal.safeparcel.a.a("Overread allowed size end=" + o, parcel);
    }

    public AddEventListenerRequest[] ag(int i) {
        return new AddEventListenerRequest[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return C(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return ag(x0);
    }
}