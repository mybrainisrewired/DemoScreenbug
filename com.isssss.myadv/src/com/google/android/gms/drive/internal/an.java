package com.google.android.gms.drive.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.metadata.internal.MetadataBundle;
import com.millennialmedia.android.MMAdView;

public class an implements Creator<UpdateMetadataRequest> {
    static void a(UpdateMetadataRequest updateMetadataRequest, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, updateMetadataRequest.xH);
        b.a(parcel, (int)MMAdView.TRANSITION_UP, updateMetadataRequest.EV, i, false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, updateMetadataRequest.EW, i, false);
        b.F(parcel, p);
    }

    public UpdateMetadataRequest[] aE(int i) {
        return new UpdateMetadataRequest[i];
    }

    public UpdateMetadataRequest aa(Parcel parcel) {
        MetadataBundle metadataBundle = null;
        int o = a.o(parcel);
        int i = 0;
        DriveId driveId = null;
        while (parcel.dataPosition() < o) {
            DriveId driveId2;
            int i2;
            MetadataBundle metadataBundle2;
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    MetadataBundle metadataBundle3 = metadataBundle;
                    driveId2 = driveId;
                    i2 = a.g(parcel, n);
                    metadataBundle2 = metadataBundle3;
                    break;
                case MMAdView.TRANSITION_UP:
                    i2 = i;
                    DriveId driveId3 = (DriveId) a.a(parcel, n, DriveId.CREATOR);
                    metadataBundle2 = metadataBundle;
                    driveId2 = driveId3;
                    break;
                case MMAdView.TRANSITION_DOWN:
                    metadataBundle2 = (MetadataBundle) a.a(parcel, n, MetadataBundle.CREATOR);
                    driveId2 = driveId;
                    i2 = i;
                    break;
                default:
                    a.b(parcel, n);
                    metadataBundle2 = metadataBundle;
                    driveId2 = driveId;
                    i2 = i;
                    break;
            }
            i = i2;
            driveId = driveId2;
            metadataBundle = metadataBundle2;
        }
        if (parcel.dataPosition() == o) {
            return new UpdateMetadataRequest(i, driveId, metadataBundle);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return aa(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return aE(x0);
    }
}