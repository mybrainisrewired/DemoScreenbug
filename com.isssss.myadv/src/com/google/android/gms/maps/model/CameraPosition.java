package com.google.android.gms.maps.model;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.util.AttributeSet;
import com.google.android.gms.R;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.fo;
import com.google.android.gms.internal.fq;
import com.google.android.gms.maps.internal.v;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public final class CameraPosition implements SafeParcelable {
    public static final CameraPositionCreator CREATOR;
    public final float bearing;
    public final LatLng target;
    public final float tilt;
    private final int xH;
    public final float zoom;

    public static final class Builder {
        private LatLng SD;
        private float SE;
        private float SF;
        private float SG;

        public Builder(CameraPosition previous) {
            this.SD = previous.target;
            this.SE = previous.zoom;
            this.SF = previous.tilt;
            this.SG = previous.bearing;
        }

        public com.google.android.gms.maps.model.CameraPosition.Builder bearing(float bearing) {
            this.SG = bearing;
            return this;
        }

        public CameraPosition build() {
            return new CameraPosition(this.SD, this.SE, this.SF, this.SG);
        }

        public com.google.android.gms.maps.model.CameraPosition.Builder target(LatLng location) {
            this.SD = location;
            return this;
        }

        public com.google.android.gms.maps.model.CameraPosition.Builder tilt(float tilt) {
            this.SF = tilt;
            return this;
        }

        public com.google.android.gms.maps.model.CameraPosition.Builder zoom(float zoom) {
            this.SE = zoom;
            return this;
        }
    }

    static {
        CREATOR = new CameraPositionCreator();
    }

    CameraPosition(int versionCode, Object target, float zoom, float tilt, float bearing) {
        fq.b(target, (Object)"null camera target");
        boolean z = 0.0f <= tilt && tilt <= 90.0f;
        fq.b(z, (Object)"Tilt needs to be between 0 and 90 inclusive");
        this.xH = versionCode;
        this.target = target;
        this.zoom = zoom;
        this.tilt = tilt + 0.0f;
        if (((double) bearing) <= 0.0d) {
            bearing = bearing % 360.0f + 360.0f;
        }
        this.bearing = bearing % 360.0f;
    }

    public CameraPosition(LatLng target, float zoom, float tilt, float bearing) {
        this(1, target, zoom, tilt, bearing);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(CameraPosition camera) {
        return new Builder(camera);
    }

    public static CameraPosition createFromAttributes(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return null;
        }
        TypedArray obtainAttributes = context.getResources().obtainAttributes(attrs, R.styleable.MapAttrs);
        LatLng latLng = new LatLng((double) (obtainAttributes.hasValue(MMAdView.TRANSITION_UP) ? obtainAttributes.getFloat(MMAdView.TRANSITION_UP, BitmapDescriptorFactory.HUE_RED) : 0.0f), (double) (obtainAttributes.hasValue(MMAdView.TRANSITION_DOWN) ? obtainAttributes.getFloat(MMAdView.TRANSITION_DOWN, BitmapDescriptorFactory.HUE_RED) : 0.0f));
        Builder builder = builder();
        builder.target(latLng);
        if (obtainAttributes.hasValue(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES)) {
            builder.zoom(obtainAttributes.getFloat(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, BitmapDescriptorFactory.HUE_RED));
        }
        if (obtainAttributes.hasValue(1)) {
            builder.bearing(obtainAttributes.getFloat(1, BitmapDescriptorFactory.HUE_RED));
        }
        if (obtainAttributes.hasValue(MMAdView.TRANSITION_RANDOM)) {
            builder.tilt(obtainAttributes.getFloat(MMAdView.TRANSITION_RANDOM, BitmapDescriptorFactory.HUE_RED));
        }
        return builder.build();
    }

    public static final CameraPosition fromLatLngZoom(LatLng target, float zoom) {
        return new CameraPosition(target, zoom, 0.0f, 0.0f);
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(CameraPosition o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CameraPosition)) {
            return false;
        }
        o = o;
        return this.target.equals(o.target) && Float.floatToIntBits(this.zoom) == Float.floatToIntBits(o.zoom) && Float.floatToIntBits(this.tilt) == Float.floatToIntBits(o.tilt) && Float.floatToIntBits(this.bearing) == Float.floatToIntBits(o.bearing);
    }

    int getVersionCode() {
        return this.xH;
    }

    public int hashCode() {
        return fo.hashCode(new Object[]{this.target, Float.valueOf(this.zoom), Float.valueOf(this.tilt), Float.valueOf(this.bearing)});
    }

    public String toString() {
        return fo.e(this).a("target", this.target).a("zoom", Float.valueOf(this.zoom)).a("tilt", Float.valueOf(this.tilt)).a("bearing", Float.valueOf(this.bearing)).toString();
    }

    public void writeToParcel(Parcel out, int flags) {
        if (v.iB()) {
            a.a(this, out, flags);
        } else {
            CameraPositionCreator.a(this, out, flags);
        }
    }
}