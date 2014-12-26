package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.fo;
import com.google.android.gms.internal.fq;

public class StreetViewPanoramaCamera implements SafeParcelable {
    public static final StreetViewPanoramaCameraCreator CREATOR;
    private StreetViewPanoramaOrientation Tr;
    public final float bearing;
    public final float tilt;
    private final int xH;
    public final float zoom;

    public static final class Builder {
        public float bearing;
        public float tilt;
        public float zoom;

        public Builder(StreetViewPanoramaCamera previous) {
            this.zoom = previous.zoom;
            this.bearing = previous.bearing;
            this.tilt = previous.tilt;
        }

        public com.google.android.gms.maps.model.StreetViewPanoramaCamera.Builder bearing(float bearing) {
            this.bearing = bearing;
            return this;
        }

        public StreetViewPanoramaCamera build() {
            return new StreetViewPanoramaCamera(this.zoom, this.tilt, this.bearing);
        }

        public com.google.android.gms.maps.model.StreetViewPanoramaCamera.Builder orientation(StreetViewPanoramaOrientation orientation) {
            this.tilt = orientation.tilt;
            this.bearing = orientation.bearing;
            return this;
        }

        public com.google.android.gms.maps.model.StreetViewPanoramaCamera.Builder tilt(float tilt) {
            this.tilt = tilt;
            return this;
        }

        public com.google.android.gms.maps.model.StreetViewPanoramaCamera.Builder zoom(float zoom) {
            this.zoom = zoom;
            return this;
        }
    }

    static {
        CREATOR = new StreetViewPanoramaCameraCreator();
    }

    public StreetViewPanoramaCamera(float zoom, float tilt, float bearing) {
        this(1, zoom, tilt, bearing);
    }

    StreetViewPanoramaCamera(int versionCode, float zoom, float tilt, float bearing) {
        boolean z = -90.0f <= tilt && tilt <= 90.0f;
        fq.b(z, (Object)"Tilt needs to be between -90 and 90 inclusive");
        this.xH = versionCode;
        this.zoom = zoom;
        this.tilt = 0.0f + tilt;
        this.bearing = (((double) bearing) <= 0.0d ? (bearing % 360.0f) + 360.0f : bearing) % 360.0f;
        this.Tr = new com.google.android.gms.maps.model.StreetViewPanoramaOrientation.Builder().tilt(tilt).bearing(bearing).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(StreetViewPanoramaCamera camera) {
        return new Builder(camera);
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(StreetViewPanoramaCamera o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StreetViewPanoramaCamera)) {
            return false;
        }
        o = o;
        return Float.floatToIntBits(this.zoom) == Float.floatToIntBits(o.zoom) && Float.floatToIntBits(this.tilt) == Float.floatToIntBits(o.tilt) && Float.floatToIntBits(this.bearing) == Float.floatToIntBits(o.bearing);
    }

    public StreetViewPanoramaOrientation getOrientation() {
        return this.Tr;
    }

    int getVersionCode() {
        return this.xH;
    }

    public int hashCode() {
        return fo.hashCode(new Object[]{Float.valueOf(this.zoom), Float.valueOf(this.tilt), Float.valueOf(this.bearing)});
    }

    public String toString() {
        return fo.e(this).a("zoom", Float.valueOf(this.zoom)).a("tilt", Float.valueOf(this.tilt)).a("bearing", Float.valueOf(this.bearing)).toString();
    }

    public void writeToParcel(Parcel out, int flags) {
        StreetViewPanoramaCameraCreator.a(this, out, flags);
    }
}