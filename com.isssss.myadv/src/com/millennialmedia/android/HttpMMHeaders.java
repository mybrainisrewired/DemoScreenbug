package com.millennialmedia.android;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.Serializable;
import java.util.Map;
import org.apache.http.Header;

class HttpMMHeaders implements Parcelable, Serializable {
    public static final Creator<HttpMMHeaders> CREATOR;
    private static final String HEADER_MM_ACID = "X-MM-ACID";
    private static final String HEADER_MM_CUSTOM_CLOSE = "X-MM-USE-CUSTOM-CLOSE";
    private static final String HEADER_MM_ENABLE_HARDWARE_ACCEL = "X-MM-ENABLE-HARDWARE-ACCELERATION";
    private static final String HEADER_MM_TRANSITION = "X-MM-TRANSITION";
    private static final String HEADER_MM_TRANSITION_DURATION = "X-MM-TRANSITION-DURATION";
    private static final String HEADER_MM_TRANSPARENT = "X-MM-TRANSPARENT";
    private static final String TAG;
    static final long serialVersionUID = 3168621112125974L;
    String acid;
    boolean enableHardwareAccel;
    boolean isTransparent;
    String transition;
    long transitionTimeInMillis;
    boolean useCustomClose;

    static {
        TAG = HttpMMHeaders.class.getName();
        CREATOR = new Creator<HttpMMHeaders>() {
            public HttpMMHeaders createFromParcel(Parcel in) {
                return new HttpMMHeaders(in);
            }

            public HttpMMHeaders[] newArray(int size) {
                return new HttpMMHeaders[size];
            }
        };
    }

    public HttpMMHeaders(Parcel in) {
        try {
            boolean[] booleanValues = new boolean[3];
            in.readBooleanArray(booleanValues);
            this.isTransparent = booleanValues[0];
            this.useCustomClose = booleanValues[1];
            this.enableHardwareAccel = booleanValues[2];
            this.transition = in.readString();
            this.acid = in.readString();
            this.transitionTimeInMillis = in.readLong();
        } catch (Exception e) {
            MMLog.e(TAG, "Header serializing failed", e);
        }
    }

    public HttpMMHeaders(Header[] allHeaders) {
        Header[] arr$ = allHeaders;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            Header header = arr$[i$];
            checkTransparent(header);
            checkTransition(header);
            checkTransitionDuration(header);
            checkUseCustomClose(header);
            checkEnableHardwareAccel(header);
            checkAcid(header);
            i$++;
        }
    }

    private void checkAcid(Header header) {
        if (HEADER_MM_ACID.equalsIgnoreCase(header.getName())) {
            this.acid = header.getValue();
        }
    }

    private void checkEnableHardwareAccel(Header header) {
        if (HEADER_MM_ENABLE_HARDWARE_ACCEL.equalsIgnoreCase(header.getName())) {
            this.enableHardwareAccel = Boolean.parseBoolean(header.getValue());
        }
    }

    private void checkTransition(Header header) {
        if (HEADER_MM_TRANSITION.equalsIgnoreCase(header.getName())) {
            this.transition = header.getValue();
        }
    }

    private void checkTransitionDuration(Header header) {
        if (HEADER_MM_TRANSITION_DURATION.equalsIgnoreCase(header.getName())) {
            String value = header.getValue();
            if (value != null) {
                this.transitionTimeInMillis = (long) (Float.parseFloat(value) * 1000.0f);
            }
        }
    }

    private void checkTransparent(Header header) {
        if (HEADER_MM_TRANSPARENT.equalsIgnoreCase(header.getName())) {
            String value = header.getValue();
            if (value != null) {
                this.isTransparent = Boolean.parseBoolean(value);
            }
        }
    }

    private void checkUseCustomClose(Header header) {
        if (HEADER_MM_CUSTOM_CLOSE.equalsIgnoreCase(header.getName())) {
            this.useCustomClose = Boolean.parseBoolean(header.getValue());
        }
    }

    public int describeContents() {
        return 0;
    }

    void updateArgumentsWithSettings(Map<String, String> arguments) {
        arguments.put("transparent", String.valueOf(this.isTransparent));
        arguments.put("transition", String.valueOf(this.transition));
        arguments.put("acid", String.valueOf(this.acid));
        arguments.put("transitionDuration", String.valueOf(this.transitionTimeInMillis));
        arguments.put("useCustomClose", String.valueOf(this.useCustomClose));
        arguments.put("enableHardwareAccel", String.valueOf(this.enableHardwareAccel));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBooleanArray(new boolean[]{this.isTransparent, this.useCustomClose, this.enableHardwareAccel});
        dest.writeString(this.transition);
        dest.writeString(this.acid);
        dest.writeLong(this.transitionTimeInMillis);
    }
}