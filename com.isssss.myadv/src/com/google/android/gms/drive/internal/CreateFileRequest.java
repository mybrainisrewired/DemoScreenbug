package com.google.android.gms.drive.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.drive.Contents;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.metadata.internal.MetadataBundle;
import com.google.android.gms.internal.fq;

public class CreateFileRequest implements SafeParcelable {
    public static final Creator<CreateFileRequest> CREATOR;
    final Contents EX;
    final MetadataBundle EZ;
    final DriveId Fa;
    final int xH;

    static {
        CREATOR = new h();
    }

    CreateFileRequest(int versionCode, DriveId parentDriveId, MetadataBundle metadata, Contents contentsReference) {
        this.xH = versionCode;
        this.Fa = (DriveId) fq.f(parentDriveId);
        this.EZ = (MetadataBundle) fq.f(metadata);
        this.EX = (Contents) fq.f(contentsReference);
    }

    public CreateFileRequest(DriveId parentDriveId, MetadataBundle metadata, Contents contentsReference) {
        this(1, parentDriveId, metadata, contentsReference);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        h.a(this, dest, flags);
    }
}