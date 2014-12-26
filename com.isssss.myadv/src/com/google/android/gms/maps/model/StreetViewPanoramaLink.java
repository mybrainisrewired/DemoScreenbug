package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.fo;

public class StreetViewPanoramaLink implements SafeParcelable {
    public static final StreetViewPanoramaLinkCreator CREATOR;
    public final float bearing;
    public final String panoId;
    private final int xH;

    static {
        CREATOR = new StreetViewPanoramaLinkCreator();
    }

    StreetViewPanoramaLink(int versionCode, String panoId, float bearing) {
        this.xH = versionCode;
        this.panoId = panoId;
        if (((double) bearing) <= 0.0d) {
            bearing = bearing % 360.0f + 360.0f;
        }
        this.bearing = bearing % 360.0f;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(StreetViewPanoramaLink o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StreetViewPanoramaLink)) {
            return false;
        }
        o = o;
        return this.panoId.equals(o.panoId) && Float.floatToIntBits(this.bearing) == Float.floatToIntBits(o.bearing);
    }

    int getVersionCode() {
        return this.xH;
    }

    public int hashCode() {
        return fo.hashCode(new Object[]{this.panoId, Float.valueOf(this.bearing)});
    }

    public String toString() {
        return fo.e(this).a("panoId", this.panoId).a("bearing", Float.valueOf(this.bearing)).toString();
    }

    public void writeToParcel(Parcel out, int flags) {
        StreetViewPanoramaLinkCreator.a(this, out, flags);
    }
}