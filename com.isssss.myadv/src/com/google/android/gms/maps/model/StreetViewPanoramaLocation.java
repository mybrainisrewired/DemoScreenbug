package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.fo;
import com.isssss.myadv.dao.BannerConfigTable;

public class StreetViewPanoramaLocation implements SafeParcelable {
    public static final StreetViewPanoramaLocationCreator CREATOR;
    public final StreetViewPanoramaLink[] links;
    public final String panoId;
    public final LatLng position;
    private final int xH;

    static {
        CREATOR = new StreetViewPanoramaLocationCreator();
    }

    StreetViewPanoramaLocation(int versionCode, StreetViewPanoramaLink[] links, LatLng position, String panoId) {
        this.xH = versionCode;
        this.links = links;
        this.position = position;
        this.panoId = panoId;
    }

    public StreetViewPanoramaLocation(StreetViewPanoramaLink[] links, LatLng position, String panoId) {
        this(1, links, position, panoId);
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(StreetViewPanoramaLocation o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StreetViewPanoramaLocation)) {
            return false;
        }
        o = o;
        return this.panoId.equals(o.panoId) && this.position.equals(o.position);
    }

    int getVersionCode() {
        return this.xH;
    }

    public int hashCode() {
        return fo.hashCode(new Object[]{this.position, this.panoId});
    }

    public String toString() {
        return fo.e(this).a("panoId", this.panoId).a(BannerConfigTable.COLUMN_POSITION, this.position.toString()).toString();
    }

    public void writeToParcel(Parcel out, int flags) {
        StreetViewPanoramaLocationCreator.a(this, out, flags);
    }
}