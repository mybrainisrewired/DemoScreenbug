package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.concurrent.TimeUnit;

public final class hk implements SafeParcelable {
    public static final hl CREATOR;
    static final long OF;
    private final hg OG;
    private final long Oc;
    private final int mPriority;
    final int xH;

    static {
        CREATOR = new hl();
        OF = TimeUnit.HOURS.toMillis(1);
    }

    public hk(int i, hg hgVar, long j, int i2) {
        this.xH = i;
        this.OG = hgVar;
        this.Oc = j;
        this.mPriority = i2;
    }

    public int describeContents() {
        hl hlVar = CREATOR;
        return 0;
    }

    public boolean equals(hk object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof hk)) {
            return false;
        }
        object = object;
        return this.OG.equals(object.OG) && this.Oc == object.Oc && this.mPriority == object.mPriority;
    }

    public long getInterval() {
        return this.Oc;
    }

    public int getPriority() {
        return this.mPriority;
    }

    public hg hZ() {
        return this.OG;
    }

    public int hashCode() {
        return fo.hashCode(new Object[]{this.OG, Long.valueOf(this.Oc), Integer.valueOf(this.mPriority)});
    }

    public String toString() {
        return fo.e(this).a("filter", this.OG).a("interval", Long.valueOf(this.Oc)).a("priority", Integer.valueOf(this.mPriority)).toString();
    }

    public void writeToParcel(Parcel parcel, int flags) {
        hl hlVar = CREATOR;
        hl.a(this, parcel, flags);
    }
}