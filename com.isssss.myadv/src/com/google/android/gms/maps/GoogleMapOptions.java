package com.google.android.gms.maps;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.util.AttributeSet;
import com.google.android.gms.R;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.internal.a;
import com.google.android.gms.maps.internal.v;
import com.google.android.gms.maps.model.CameraPosition;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;

public final class GoogleMapOptions implements SafeParcelable {
    public static final GoogleMapOptionsCreator CREATOR;
    private Boolean RI;
    private Boolean RJ;
    private int RK;
    private CameraPosition RL;
    private Boolean RM;
    private Boolean RN;
    private Boolean RO;
    private Boolean RP;
    private Boolean RQ;
    private Boolean RR;
    private final int xH;

    static {
        CREATOR = new GoogleMapOptionsCreator();
    }

    public GoogleMapOptions() {
        this.RK = -1;
        this.xH = 1;
    }

    GoogleMapOptions(int versionCode, byte zOrderOnTop, byte useViewLifecycleInFragment, int mapType, CameraPosition camera, byte zoomControlsEnabled, byte compassEnabled, byte scrollGesturesEnabled, byte zoomGesturesEnabled, byte tiltGesturesEnabled, byte rotateGesturesEnabled) {
        this.RK = -1;
        this.xH = versionCode;
        this.RI = a.a(zOrderOnTop);
        this.RJ = a.a(useViewLifecycleInFragment);
        this.RK = mapType;
        this.RL = camera;
        this.RM = a.a(zoomControlsEnabled);
        this.RN = a.a(compassEnabled);
        this.RO = a.a(scrollGesturesEnabled);
        this.RP = a.a(zoomGesturesEnabled);
        this.RQ = a.a(tiltGesturesEnabled);
        this.RR = a.a(rotateGesturesEnabled);
    }

    public static GoogleMapOptions createFromAttributes(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return null;
        }
        TypedArray obtainAttributes = context.getResources().obtainAttributes(attrs, R.styleable.MapAttrs);
        GoogleMapOptions googleMapOptions = new GoogleMapOptions();
        if (obtainAttributes.hasValue(0)) {
            googleMapOptions.mapType(obtainAttributes.getInt(0, -1));
        }
        if (obtainAttributes.hasValue(ApiEventType.API_MRAID_CLOSE)) {
            googleMapOptions.zOrderOnTop(obtainAttributes.getBoolean(ApiEventType.API_MRAID_CLOSE, false));
        }
        if (obtainAttributes.hasValue(ApiEventType.API_MRAID_RESIZE)) {
            googleMapOptions.useViewLifecycleInFragment(obtainAttributes.getBoolean(ApiEventType.API_MRAID_RESIZE, false));
        }
        if (obtainAttributes.hasValue(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES)) {
            googleMapOptions.compassEnabled(obtainAttributes.getBoolean(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, true));
        }
        if (obtainAttributes.hasValue(ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES)) {
            googleMapOptions.rotateGesturesEnabled(obtainAttributes.getBoolean(ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, true));
        }
        if (obtainAttributes.hasValue(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES)) {
            googleMapOptions.scrollGesturesEnabled(obtainAttributes.getBoolean(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, true));
        }
        if (obtainAttributes.hasValue(ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES)) {
            googleMapOptions.tiltGesturesEnabled(obtainAttributes.getBoolean(ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, true));
        }
        if (obtainAttributes.hasValue(ApiEventType.API_MRAID_EXPAND)) {
            googleMapOptions.zoomGesturesEnabled(obtainAttributes.getBoolean(ApiEventType.API_MRAID_EXPAND, true));
        }
        if (obtainAttributes.hasValue(ApiEventType.API_MRAID_USE_CUSTOM_CLOSE)) {
            googleMapOptions.zoomControlsEnabled(obtainAttributes.getBoolean(ApiEventType.API_MRAID_USE_CUSTOM_CLOSE, true));
        }
        googleMapOptions.camera(CameraPosition.createFromAttributes(context, attrs));
        obtainAttributes.recycle();
        return googleMapOptions;
    }

    public GoogleMapOptions camera(CameraPosition camera) {
        this.RL = camera;
        return this;
    }

    public GoogleMapOptions compassEnabled(boolean enabled) {
        this.RN = Boolean.valueOf(enabled);
        return this;
    }

    public int describeContents() {
        return 0;
    }

    public CameraPosition getCamera() {
        return this.RL;
    }

    public Boolean getCompassEnabled() {
        return this.RN;
    }

    public int getMapType() {
        return this.RK;
    }

    public Boolean getRotateGesturesEnabled() {
        return this.RR;
    }

    public Boolean getScrollGesturesEnabled() {
        return this.RO;
    }

    public Boolean getTiltGesturesEnabled() {
        return this.RQ;
    }

    public Boolean getUseViewLifecycleInFragment() {
        return this.RJ;
    }

    int getVersionCode() {
        return this.xH;
    }

    public Boolean getZOrderOnTop() {
        return this.RI;
    }

    public Boolean getZoomControlsEnabled() {
        return this.RM;
    }

    public Boolean getZoomGesturesEnabled() {
        return this.RP;
    }

    byte ig() {
        return a.c(this.RI);
    }

    byte ih() {
        return a.c(this.RJ);
    }

    byte ii() {
        return a.c(this.RM);
    }

    byte ij() {
        return a.c(this.RN);
    }

    byte ik() {
        return a.c(this.RO);
    }

    byte il() {
        return a.c(this.RP);
    }

    byte im() {
        return a.c(this.RQ);
    }

    byte in() {
        return a.c(this.RR);
    }

    public GoogleMapOptions mapType(int mapType) {
        this.RK = mapType;
        return this;
    }

    public GoogleMapOptions rotateGesturesEnabled(boolean enabled) {
        this.RR = Boolean.valueOf(enabled);
        return this;
    }

    public GoogleMapOptions scrollGesturesEnabled(boolean enabled) {
        this.RO = Boolean.valueOf(enabled);
        return this;
    }

    public GoogleMapOptions tiltGesturesEnabled(boolean enabled) {
        this.RQ = Boolean.valueOf(enabled);
        return this;
    }

    public GoogleMapOptions useViewLifecycleInFragment(boolean useViewLifecycleInFragment) {
        this.RJ = Boolean.valueOf(useViewLifecycleInFragment);
        return this;
    }

    public void writeToParcel(Parcel out, int flags) {
        if (v.iB()) {
            a.a(this, out, flags);
        } else {
            GoogleMapOptionsCreator.a(this, out, flags);
        }
    }

    public GoogleMapOptions zOrderOnTop(boolean zOrderOnTop) {
        this.RI = Boolean.valueOf(zOrderOnTop);
        return this;
    }

    public GoogleMapOptions zoomControlsEnabled(boolean enabled) {
        this.RM = Boolean.valueOf(enabled);
        return this;
    }

    public GoogleMapOptions zoomGesturesEnabled(boolean enabled) {
        this.RP = Boolean.valueOf(enabled);
        return this;
    }
}