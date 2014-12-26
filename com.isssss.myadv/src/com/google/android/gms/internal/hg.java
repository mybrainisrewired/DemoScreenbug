package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.mopub.common.Preconditions;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class hg implements SafeParcelable {
    public static final hh CREATOR;
    final List<hm> OA;
    private final String OB;
    private final boolean OC;
    private final Set<hm> OD;
    final int xH;

    static {
        CREATOR = new hh();
    }

    hg(int i, List<hm> list, String str, boolean z) {
        this.xH = i;
        this.OA = list == null ? Collections.emptyList() : Collections.unmodifiableList(list);
        if (str == null) {
            str = Preconditions.EMPTY_ARGUMENTS;
        }
        this.OB = str;
        this.OC = z;
        if (this.OA.isEmpty()) {
            this.OD = Collections.emptySet();
        } else {
            this.OD = Collections.unmodifiableSet(new HashSet(this.OA));
        }
    }

    public int describeContents() {
        hh hhVar = CREATOR;
        return 0;
    }

    public boolean equals(hg object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof hg)) {
            return false;
        }
        object = object;
        return this.OD.equals(object.OD) && this.OC == object.OC;
    }

    @Deprecated
    public String hW() {
        return this.OB;
    }

    public boolean hX() {
        return this.OC;
    }

    public int hashCode() {
        return fo.hashCode(new Object[]{this.OD, Boolean.valueOf(this.OC)});
    }

    public String toString() {
        return fo.e(this).a("types", this.OD).a("requireOpenNow", Boolean.valueOf(this.OC)).toString();
    }

    public void writeToParcel(Parcel parcel, int flags) {
        hh hhVar = CREATOR;
        hh.a(this, parcel, flags);
    }
}