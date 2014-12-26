package com.millennialmedia.android;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.millennialmedia.google.gson.Gson;
import com.millennialmedia.google.gson.annotations.SerializedName;
import com.mopub.common.Preconditions;

class OverlaySettings implements Parcelable {
    public static final Creator<OverlaySettings> CREATOR;
    static final String PROPERTIES_ACID = "acid";
    static final String PROPERTIES_ALLOW_ORIENTATION_CHANGE = "allowOrientationChange";
    static final String PROPERTIES_CUSTOM_CLOSE = "useCustomClose";
    static final String PROPERTIES_ENABLE_HARDWARE_ACCEL = "enableHardwareAccel";
    static final String PROPERTIES_FORCE_ORIENTATION = "forceOrientation";
    static final String PROPERTIES_HEIGHT = "height";
    static final String PROPERTIES_MODAL = "modal";
    static final String PROPERTIES_ORIENTATION = "orientation";
    static final String PROPERTIES_TRANSITION = "transition";
    static final String PROPERTIES_TRANSITION_DURATION = "transitionDuration";
    static final String PROPERTIES_TRANSPARENT = "transparent";
    static final String PROPERTIES_WIDTH = "width";
    private static final String TAG = "OverlaySettings";
    String adUrl;
    boolean allowOrientationChange;
    String content;
    long creatorAdImplId;
    boolean hasLoadedExpandUrl;
    int height;
    boolean isFromInterstitial;
    @SerializedName("transparent")
    private boolean isTransparent;
    HttpMMHeaders mmHeaders;
    boolean modal;
    String orientation;
    boolean shouldLaunchToOverlay;
    int shouldResizeOverlay;
    private String transition;
    @SerializedName("transitionDuration")
    private long transitionTimeInMillis;
    String urlToLoad;
    private boolean useCustomClose;
    int width;

    static {
        CREATOR = new Creator<OverlaySettings>() {
            public OverlaySettings createFromParcel(Parcel in) {
                return new OverlaySettings(in);
            }

            public OverlaySettings[] newArray(int size) {
                return new OverlaySettings[size];
            }
        };
    }

    OverlaySettings() {
        this.transition = Preconditions.EMPTY_ARGUMENTS;
        this.orientation = Preconditions.EMPTY_ARGUMENTS;
        this.urlToLoad = Preconditions.EMPTY_ARGUMENTS;
        this.allowOrientationChange = true;
        this.content = Preconditions.EMPTY_ARGUMENTS;
        this.adUrl = Preconditions.EMPTY_ARGUMENTS;
        this.hasLoadedExpandUrl = false;
    }

    OverlaySettings(Parcel in) {
        long j = 0;
        this.transition = Preconditions.EMPTY_ARGUMENTS;
        this.orientation = Preconditions.EMPTY_ARGUMENTS;
        this.urlToLoad = Preconditions.EMPTY_ARGUMENTS;
        this.allowOrientationChange = true;
        this.content = Preconditions.EMPTY_ARGUMENTS;
        this.adUrl = Preconditions.EMPTY_ARGUMENTS;
        this.hasLoadedExpandUrl = false;
        try {
            boolean[] booleanValues = new boolean[6];
            in.readBooleanArray(booleanValues);
            this.shouldLaunchToOverlay = booleanValues[0];
            this.isTransparent = booleanValues[1];
            this.useCustomClose = booleanValues[2];
            this.modal = booleanValues[3];
            this.isFromInterstitial = booleanValues[4];
            this.allowOrientationChange = booleanValues[5];
            this.shouldResizeOverlay = in.readInt();
            this.transition = in.readString();
            this.transitionTimeInMillis = in.readLong();
            if (this.transitionTimeInMillis >= 0) {
                j = this.transitionTimeInMillis;
            }
            this.transitionTimeInMillis = j;
            this.orientation = in.readString();
            this.creatorAdImplId = in.readLong();
            this.urlToLoad = in.readString();
            this.height = in.readInt();
            this.width = in.readInt();
            this.content = in.readString();
            this.adUrl = in.readString();
            this.mmHeaders = (HttpMMHeaders) in.readParcelable(HttpMMHeaders.class.getClassLoader());
        } catch (Exception e) {
            MMLog.e(TAG, "Exception Overlaysettings creationg from parcel: ", e);
        }
    }

    static final OverlaySettings createFromJson(String json) {
        return (OverlaySettings) new Gson().fromJson(json, OverlaySettings.class);
    }

    public int describeContents() {
        return 0;
    }

    boolean enableHardwareAccel() {
        return this.mmHeaders != null && this.mmHeaders.enableHardwareAccel;
    }

    String getAcid() {
        return (this.mmHeaders == null || TextUtils.isEmpty(this.mmHeaders.acid)) ? Preconditions.EMPTY_ARGUMENTS : this.mmHeaders.acid;
    }

    boolean getIsTransparent() {
        return this.isTransparent || (this.mmHeaders != null && this.mmHeaders.isTransparent);
    }

    String getTransition() {
        if (TextUtils.isEmpty(this.transition)) {
            return (this.mmHeaders == null || TextUtils.isEmpty(this.mmHeaders.transition)) ? "none" : this.mmHeaders.transition;
        } else {
            return this.transition;
        }
    }

    long getTransitionDurationInMillis() {
        if (this.transitionTimeInMillis > 0) {
            return this.transitionTimeInMillis;
        }
        return this.mmHeaders != null ? this.mmHeaders.transitionTimeInMillis : 0;
    }

    boolean getUseCustomClose() {
        return this.useCustomClose || (this.mmHeaders != null && this.mmHeaders.useCustomClose);
    }

    boolean hasExpandUrl() {
        return (this.urlToLoad == null || this.urlToLoad.equals(Preconditions.EMPTY_ARGUMENTS)) ? false : true;
    }

    boolean hasLoadedExpandUrl() {
        if (this.hasLoadedExpandUrl) {
            return true;
        }
        this.hasLoadedExpandUrl = true;
        return false;
    }

    boolean isExpanded() {
        return (this.isFromInterstitial || this.creatorAdImplId == 0) ? false : true;
    }

    boolean isFromInterstitial() {
        return this.isFromInterstitial && this.creatorAdImplId != 0;
    }

    void log() {
        MMLog.v(TAG, toString());
    }

    void setIsTransparent(boolean transparent) {
        this.isTransparent = transparent;
    }

    void setTransition(String trans) {
        this.transition = trans;
    }

    void setTransitionDurationInMillis(long transTimeMillis) {
        this.transitionTimeInMillis = transTimeMillis;
    }

    void setUseCustomClose(boolean isUseCustomClose) {
        this.useCustomClose = isUseCustomClose;
    }

    void setWebMMHeaders(HttpMMHeaders headers) {
        this.mmHeaders = headers;
    }

    public String toString() {
        return String.format("height %d width %d modal %b urlToLoad %s creatorAdImplId %s shouldResizeOverlay: %d transitionTime: %d overlayTransition: %s shouldMakeOverlayTransparent: %b shouldShowCustomClose: %b Orientation: %s", new Object[]{Integer.valueOf(this.height), Integer.valueOf(this.width), Boolean.valueOf(this.modal), this.urlToLoad, Long.valueOf(this.creatorAdImplId), Integer.valueOf(this.shouldResizeOverlay), Long.valueOf(this.transitionTimeInMillis), this.transition, Boolean.valueOf(this.isTransparent), Boolean.valueOf(this.useCustomClose), this.orientation});
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBooleanArray(new boolean[]{this.shouldLaunchToOverlay, this.isTransparent, this.useCustomClose, this.modal, this.isFromInterstitial, this.allowOrientationChange});
        dest.writeInt(this.shouldResizeOverlay);
        dest.writeString(this.transition);
        dest.writeLong(this.transitionTimeInMillis);
        dest.writeString(this.orientation);
        dest.writeLong(this.creatorAdImplId);
        dest.writeString(this.urlToLoad);
        dest.writeInt(this.height);
        dest.writeInt(this.width);
        dest.writeString(this.content);
        dest.writeString(this.adUrl);
        dest.writeParcelable(this.mmHeaders, flags);
    }
}