package com.google.android.gms.drive.events;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.drive.DriveId;

public final class ConflictEvent implements SafeParcelable, DriveEvent {
    public static final Creator<ConflictEvent> CREATOR;
    final DriveId Ew;
    final int xH;

    static {
        CREATOR = new b();
    }

    ConflictEvent(int versionCode, DriveId driveId) {
        this.xH = versionCode;
        this.Ew = driveId;
    }

    public int describeContents() {
        return 0;
    }

    public DriveId getDriveId() {
        return this.Ew;
    }

    public int getType() {
        return 1;
    }

    public String toString() {
        return String.format("ConflictEvent [id=%s]", new Object[]{this.Ew});
    }

    public void writeToParcel(Parcel dest, int flags) {
        b.a(this, dest, flags);
    }
}