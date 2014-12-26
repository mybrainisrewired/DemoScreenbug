package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class FullWalletRequest implements SafeParcelable {
    public static final Creator<FullWalletRequest> CREATOR;
    String abh;
    String abi;
    Cart abr;
    private final int xH;

    public final class Builder {
        private Builder() {
        }

        public FullWalletRequest build() {
            return FullWalletRequest.this;
        }

        public com.google.android.gms.wallet.FullWalletRequest.Builder setCart(Cart cart) {
            FullWalletRequest.this.abr = cart;
            return this;
        }

        public com.google.android.gms.wallet.FullWalletRequest.Builder setGoogleTransactionId(String googleTransactionId) {
            FullWalletRequest.this.abh = googleTransactionId;
            return this;
        }

        public com.google.android.gms.wallet.FullWalletRequest.Builder setMerchantTransactionId(String merchantTransactionId) {
            FullWalletRequest.this.abi = merchantTransactionId;
            return this;
        }
    }

    static {
        CREATOR = new g();
    }

    FullWalletRequest() {
        this.xH = 1;
    }

    FullWalletRequest(int versionCode, String googleTransactionId, String merchantTransactionId, Cart cart) {
        this.xH = versionCode;
        this.abh = googleTransactionId;
        this.abi = merchantTransactionId;
        this.abr = cart;
    }

    public static Builder newBuilder() {
        FullWalletRequest fullWalletRequest = new FullWalletRequest();
        fullWalletRequest.getClass();
        return new Builder(null);
    }

    public int describeContents() {
        return 0;
    }

    public Cart getCart() {
        return this.abr;
    }

    public String getGoogleTransactionId() {
        return this.abh;
    }

    public String getMerchantTransactionId() {
        return this.abi;
    }

    public int getVersionCode() {
        return this.xH;
    }

    public void writeToParcel(Parcel dest, int flags) {
        g.a(this, dest, flags);
    }
}