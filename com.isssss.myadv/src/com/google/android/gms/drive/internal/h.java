package com.google.android.gms.drive.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.drive.Contents;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.metadata.internal.MetadataBundle;
import com.millennialmedia.android.MMAdView;

public class h implements Creator<CreateFileRequest> {
    static void a(CreateFileRequest createFileRequest, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, createFileRequest.xH);
        b.a(parcel, (int)MMAdView.TRANSITION_UP, createFileRequest.Fa, i, false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, createFileRequest.EZ, i, false);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, createFileRequest.EX, i, false);
        b.F(parcel, p);
    }

    public CreateFileRequest I(Parcel parcel) {
        Contents contents = null;
        int o = a.o(parcel);
        int i = 0;
        MetadataBundle metadataBundle = null;
        DriveId driveId = null;
        while (parcel.dataPosition() < o) {
            MetadataBundle metadataBundle2;
            DriveId driveId2;
            int i2;
            Contents contents2;
            int n = a.n(parcel);
            Contents contents3;
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    contents3 = contents;
                    metadataBundle2 = metadataBundle;
                    driveId2 = driveId;
                    i2 = a.g(parcel, n);
                    contents2 = contents3;
                    break;
                case MMAdView.TRANSITION_UP:
                    i2 = i;
                    MetadataBundle metadataBundle3 = metadataBundle;
                    driveId2 = (DriveId) a.a(parcel, n, DriveId.CREATOR);
                    contents2 = contents;
                    metadataBundle2 = metadataBundle3;
                    break;
                case MMAdView.TRANSITION_DOWN:
                    driveId2 = driveId;
                    i2 = i;
                    contents3 = contents;
                    metadataBundle2 = (MetadataBundle) a.a(parcel, n, MetadataBundle.CREATOR);
                    contents2 = contents3;
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    contents2 = (Contents) a.a(parcel, n, Contents.CREATOR);
                    metadataBundle2 = metadataBundle;
                    driveId2 = driveId;
                    i2 = i;
                    break;
                default:
                    a.b(parcel, n);
                    contents2 = contents;
                    metadataBundle2 = metadataBundle;
                    driveId2 = driveId;
                    i2 = i;
                    break;
            }
            i = i2;
            driveId = driveId2;
            metadataBundle = metadataBundle2;
            contents = contents2;
        }
        if (parcel.dataPosition() == o) {
            return new CreateFileRequest(i, driveId, metadataBundle, contents);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public CreateFileRequest[] am(int i) {
        return new CreateFileRequest[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return I(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return am(x0);
    }
}