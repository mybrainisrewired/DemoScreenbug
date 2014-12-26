package com.google.android.gms.common.api;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.IntentSender.SendIntentException;
import android.os.Parcel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.fo;

public final class Status implements Result, SafeParcelable {
    public static final Status Bv;
    public static final Status Bw;
    public static final Status Bx;
    public static final Status By;
    public static final Status Bz;
    public static final StatusCreator CREATOR;
    private final int Ah;
    private final String BA;
    private final PendingIntent mPendingIntent;
    private final int xH;

    static {
        Bv = new Status(0);
        Bw = new Status(14);
        Bx = new Status(8);
        By = new Status(15);
        Bz = new Status(16);
        CREATOR = new StatusCreator();
    }

    public Status(int statusCode) {
        this(1, statusCode, null, null);
    }

    Status(int versionCode, int statusCode, String statusMessage, PendingIntent pendingIntent) {
        this.xH = versionCode;
        this.Ah = statusCode;
        this.BA = statusMessage;
        this.mPendingIntent = pendingIntent;
    }

    public Status(int statusCode, String statusMessage, PendingIntent pendingIntent) {
        this(1, statusCode, statusMessage, pendingIntent);
    }

    private String dW() {
        return this.BA != null ? this.BA : CommonStatusCodes.getStatusCodeString(this.Ah);
    }

    public int describeContents() {
        return 0;
    }

    PendingIntent eo() {
        return this.mPendingIntent;
    }

    String ep() {
        return this.BA;
    }

    @Deprecated
    public ConnectionResult eq() {
        return new ConnectionResult(this.Ah, this.mPendingIntent);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Status)) {
            return false;
        }
        Status obj2 = (Status) obj;
        return this.xH == obj2.xH && this.Ah == obj2.Ah && fo.equal(this.BA, obj2.BA) && fo.equal(this.mPendingIntent, obj2.mPendingIntent);
    }

    public PendingIntent getResolution() {
        return this.mPendingIntent;
    }

    public Status getStatus() {
        return this;
    }

    public int getStatusCode() {
        return this.Ah;
    }

    int getVersionCode() {
        return this.xH;
    }

    public boolean hasResolution() {
        return this.mPendingIntent != null;
    }

    public int hashCode() {
        return fo.hashCode(new Object[]{Integer.valueOf(this.xH), Integer.valueOf(this.Ah), this.BA, this.mPendingIntent});
    }

    public boolean isCanceled() {
        return this.Ah == 16;
    }

    public boolean isInterrupted() {
        return this.Ah == 14;
    }

    public boolean isSuccess() {
        return this.Ah <= 0;
    }

    public void startResolutionForResult(Activity activity, int requestCode) throws SendIntentException {
        if (hasResolution()) {
            activity.startIntentSenderForResult(this.mPendingIntent.getIntentSender(), requestCode, null, 0, 0, 0);
        }
    }

    public String toString() {
        return fo.e(this).a("statusCode", dW()).a("resolution", this.mPendingIntent).toString();
    }

    public void writeToParcel(Parcel out, int flags) {
        StatusCreator.a(this, out, flags);
    }
}