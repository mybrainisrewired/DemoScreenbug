package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.fq;

public final class NotifyTransactionStatusRequest implements SafeParcelable {
    public static final Creator<NotifyTransactionStatusRequest> CREATOR;
    String abh;
    String ach;
    int status;
    final int xH;

    public final class Builder {
        private Builder() {
        }

        public NotifyTransactionStatusRequest build() {
            boolean z = true;
            fq.b(!TextUtils.isEmpty(NotifyTransactionStatusRequest.this.abh), (Object)"googleTransactionId is required");
            if (NotifyTransactionStatusRequest.this.status < 1 || NotifyTransactionStatusRequest.this.status > 8) {
                z = false;
            }
            fq.b(z, (Object)"status is an unrecognized value");
            return NotifyTransactionStatusRequest.this;
        }

        public com.google.android.gms.wallet.NotifyTransactionStatusRequest.Builder setDetailedReason(String detailedReason) {
            NotifyTransactionStatusRequest.this.ach = detailedReason;
            return this;
        }

        public com.google.android.gms.wallet.NotifyTransactionStatusRequest.Builder setGoogleTransactionId(String googleTransactionId) {
            NotifyTransactionStatusRequest.this.abh = googleTransactionId;
            return this;
        }

        public com.google.android.gms.wallet.NotifyTransactionStatusRequest.Builder setStatus(int status) {
            NotifyTransactionStatusRequest.this.status = status;
            return this;
        }
    }

    public static interface Status {
        public static final int SUCCESS = 1;

        public static interface Error {
            public static final int AVS_DECLINE = 7;
            public static final int BAD_CARD = 4;
            public static final int BAD_CVC = 3;
            public static final int DECLINED = 5;
            public static final int FRAUD_DECLINE = 8;
            public static final int OTHER = 6;
            public static final int UNKNOWN = 2;
        }
    }

    static {
        CREATOR = new m();
    }

    NotifyTransactionStatusRequest() {
        this.xH = 1;
    }

    NotifyTransactionStatusRequest(int versionCode, String googleTransactionId, int status, String detailedReason) {
        this.xH = versionCode;
        this.abh = googleTransactionId;
        this.status = status;
        this.ach = detailedReason;
    }

    public static Builder newBuilder() {
        NotifyTransactionStatusRequest notifyTransactionStatusRequest = new NotifyTransactionStatusRequest();
        notifyTransactionStatusRequest.getClass();
        return new Builder(null);
    }

    public int describeContents() {
        return 0;
    }

    public String getDetailedReason() {
        return this.ach;
    }

    public String getGoogleTransactionId() {
        return this.abh;
    }

    public int getStatus() {
        return this.status;
    }

    public int getVersionCode() {
        return this.xH;
    }

    public void writeToParcel(Parcel out, int flags) {
        m.a(this, out, flags);
    }
}