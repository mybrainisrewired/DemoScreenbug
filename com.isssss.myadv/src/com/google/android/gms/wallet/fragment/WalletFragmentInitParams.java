package com.google.android.gms.wallet.fragment;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.fq;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.MaskedWalletRequest;

public final class WalletFragmentInitParams implements SafeParcelable {
    public static final Creator<WalletFragmentInitParams> CREATOR;
    private MaskedWalletRequest acB;
    private int acO;
    private MaskedWallet acP;
    private String wG;
    final int xH;

    public final class Builder {
        private Builder() {
        }

        public WalletFragmentInitParams build() {
            boolean z = true;
            boolean z2 = (WalletFragmentInitParams.this.acP != null && WalletFragmentInitParams.this.acB == null) || (WalletFragmentInitParams.this.acP == null && WalletFragmentInitParams.this.acB != null);
            fq.a(z2, "Exactly one of MaskedWallet or MaskedWalletRequest is required");
            if (WalletFragmentInitParams.this.acO < 0) {
                z = false;
            }
            fq.a(z, "masked wallet request code is required and must be non-negative");
            return WalletFragmentInitParams.this;
        }

        public com.google.android.gms.wallet.fragment.WalletFragmentInitParams.Builder setAccountName(String accountName) {
            WalletFragmentInitParams.this.wG = accountName;
            return this;
        }

        public com.google.android.gms.wallet.fragment.WalletFragmentInitParams.Builder setMaskedWallet(MaskedWallet maskedWallet) {
            WalletFragmentInitParams.this.acP = maskedWallet;
            return this;
        }

        public com.google.android.gms.wallet.fragment.WalletFragmentInitParams.Builder setMaskedWalletRequest(MaskedWalletRequest request) {
            WalletFragmentInitParams.this.acB = request;
            return this;
        }

        public com.google.android.gms.wallet.fragment.WalletFragmentInitParams.Builder setMaskedWalletRequestCode(int requestCode) {
            WalletFragmentInitParams.this.acO = requestCode;
            return this;
        }
    }

    static {
        CREATOR = new a();
    }

    private WalletFragmentInitParams() {
        this.xH = 1;
        this.acO = -1;
    }

    WalletFragmentInitParams(int versionCode, String accountName, MaskedWalletRequest maskedWalletRequest, int maskedWalletRequestCode, MaskedWallet maskedWallet) {
        this.xH = versionCode;
        this.wG = accountName;
        this.acB = maskedWalletRequest;
        this.acO = maskedWalletRequestCode;
        this.acP = maskedWallet;
    }

    public static Builder newBuilder() {
        WalletFragmentInitParams walletFragmentInitParams = new WalletFragmentInitParams();
        walletFragmentInitParams.getClass();
        return new Builder(null);
    }

    public int describeContents() {
        return 0;
    }

    public String getAccountName() {
        return this.wG;
    }

    public MaskedWallet getMaskedWallet() {
        return this.acP;
    }

    public MaskedWalletRequest getMaskedWalletRequest() {
        return this.acB;
    }

    public int getMaskedWalletRequestCode() {
        return this.acO;
    }

    public void writeToParcel(Parcel dest, int flags) {
        a.a(this, dest, flags);
    }
}