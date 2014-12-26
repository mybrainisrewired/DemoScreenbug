package com.google.android.gms.drive;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Base64;
import android.util.Log;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.drive.internal.y;
import com.google.android.gms.internal.fq;
import com.google.android.gms.internal.ks;
import com.google.android.gms.internal.kt;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.mopub.common.Preconditions;

public class DriveId implements SafeParcelable {
    public static final Creator<DriveId> CREATOR;
    final String EH;
    final long EI;
    final long EJ;
    private volatile String EK;
    final int xH;

    static {
        CREATOR = new d();
    }

    DriveId(int versionCode, String resourceId, long sqlId, long databaseInstanceId) {
        boolean z = false;
        this.EK = null;
        this.xH = versionCode;
        this.EH = resourceId;
        fq.z(!Preconditions.EMPTY_ARGUMENTS.equals(resourceId));
        if (!(resourceId == null && sqlId == -1)) {
            z = true;
        }
        fq.z(z);
        this.EI = sqlId;
        this.EJ = databaseInstanceId;
    }

    public DriveId(String resourceId, long sqlId, long databaseInstanceId) {
        this(1, resourceId, sqlId, databaseInstanceId);
    }

    public static DriveId aw(String str) {
        fq.f(str);
        return new DriveId(str, -1, -1);
    }

    public static DriveId decodeFromString(String s) {
        fq.b(s.startsWith("DriveId:"), "Invalid DriveId: " + s);
        return f(Base64.decode(s.substring("DriveId:".length()), ApiEventType.API_MRAID_USE_CUSTOM_CLOSE));
    }

    static DriveId f(byte[] bArr) {
        try {
            y g = y.g(bArr);
            return new DriveId(g.versionCode, Preconditions.EMPTY_ARGUMENTS.equals(g.FC) ? null : g.FC, g.FD, g.FE);
        } catch (ks e) {
            throw new IllegalArgumentException();
        }
    }

    public int describeContents() {
        return 0;
    }

    public final String encodeToString() {
        if (this.EK == null) {
            this.EK = "DriveId:" + Base64.encodeToString(fC(), ApiEventType.API_MRAID_USE_CUSTOM_CLOSE);
        }
        return this.EK;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof DriveId)) {
            return false;
        }
        DriveId obj2 = (DriveId) obj;
        if (obj2.EJ != this.EJ) {
            Log.w("DriveId", "Attempt to compare invalid DriveId detected. Has local storage been cleared?");
            return false;
        } else if (obj2.EI == -1 && this.EI == -1) {
            return obj2.EH.equals(this.EH);
        } else {
            return obj2.EI == this.EI;
        }
    }

    final byte[] fC() {
        kt yVar = new y();
        yVar.versionCode = this.xH;
        yVar.FC = this.EH == null ? Preconditions.EMPTY_ARGUMENTS : this.EH;
        yVar.FD = this.EI;
        yVar.FE = this.EJ;
        return kt.d(yVar);
    }

    public String getResourceId() {
        return this.EH;
    }

    public int hashCode() {
        return this.EI == -1 ? this.EH.hashCode() : (String.valueOf(this.EJ) + String.valueOf(this.EI)).hashCode();
    }

    public String toString() {
        return encodeToString();
    }

    public void writeToParcel(Parcel out, int flags) {
        d.a(this, out, flags);
    }
}