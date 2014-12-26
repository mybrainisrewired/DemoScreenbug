package com.google.android.gms.location;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.fo;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class b implements SafeParcelable {
    public static final c CREATOR;
    int Oh;
    int Oi;
    long Oj;
    private final int xH;

    static {
        CREATOR = new c();
    }

    b(int i, int i2, int i3, long j) {
        this.xH = i;
        this.Oh = i2;
        this.Oi = i3;
        this.Oj = j;
    }

    private String by(int i) {
        switch (i) {
            case MMAdView.TRANSITION_NONE:
                return "STATUS_SUCCESSFUL";
            case MMAdView.TRANSITION_UP:
                return "STATUS_TIMED_OUT_ON_SCAN";
            case MMAdView.TRANSITION_DOWN:
                return "STATUS_NO_INFO_IN_DATABASE";
            case MMAdView.TRANSITION_RANDOM:
                return "STATUS_INVALID_SCAN";
            case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                return "STATUS_UNABLE_TO_QUERY_DATABASE";
            case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                return "STATUS_SCANS_DISABLED_IN_SETTINGS";
            case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                return "STATUS_LOCATION_DISABLED_IN_SETTINGS";
            case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                return "STATUS_IN_PROGRESS";
            default:
                return "STATUS_UNKNOWN";
        }
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object other) {
        if (!(other instanceof b)) {
            return false;
        }
        b other2 = (b) other;
        return this.Oh == other2.Oh && this.Oi == other2.Oi && this.Oj == other2.Oj;
    }

    int getVersionCode() {
        return this.xH;
    }

    public int hashCode() {
        return fo.hashCode(new Object[]{Integer.valueOf(this.Oh), Integer.valueOf(this.Oi), Long.valueOf(this.Oj)});
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("LocationStatus[cell status: ").append(by(this.Oh));
        stringBuilder.append(", wifi status: ").append(by(this.Oi));
        stringBuilder.append(", elapsed realtime ns: ").append(this.Oj);
        stringBuilder.append(']');
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel parcel, int flags) {
        c.a(this, parcel, flags);
    }
}