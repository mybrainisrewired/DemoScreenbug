package com.google.android.gms.drive.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.metadata.internal.MetadataBundle;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class g implements Creator<CreateFileIntentSenderRequest> {
    static void a(CreateFileIntentSenderRequest createFileIntentSenderRequest, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, createFileIntentSenderRequest.xH);
        b.a(parcel, (int)MMAdView.TRANSITION_UP, createFileIntentSenderRequest.EZ, i, false);
        b.c(parcel, MMAdView.TRANSITION_DOWN, createFileIntentSenderRequest.Eu);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, createFileIntentSenderRequest.EB, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, createFileIntentSenderRequest.EC, i, false);
        b.F(parcel, p);
    }

    public CreateFileIntentSenderRequest H(Parcel parcel) {
        int i = 0;
        DriveId driveId = null;
        int o = a.o(parcel);
        String str = null;
        MetadataBundle metadataBundle = null;
        int i2 = 0;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i2 = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    metadataBundle = (MetadataBundle) a.a(parcel, n, MetadataBundle.CREATOR);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    i = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    str = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    driveId = (DriveId) a.a(parcel, n, DriveId.CREATOR);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new CreateFileIntentSenderRequest(i2, metadataBundle, i, str, driveId);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public CreateFileIntentSenderRequest[] al(int i) {
        return new CreateFileIntentSenderRequest[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return H(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return al(x0);
    }
}