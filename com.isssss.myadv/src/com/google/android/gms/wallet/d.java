package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class d implements SafeParcelable {
    public static final Creator<d> CREATOR;
    LoyaltyWalletObject abg;
    private final int xH;

    static {
        CREATOR = new e();
    }

    d() {
        this.xH = 1;
    }

    d(int i, LoyaltyWalletObject loyaltyWalletObject) {
        this.xH = i;
        this.abg = loyaltyWalletObject;
    }

    public int describeContents() {
        return 0;
    }

    public int getVersionCode() {
        return this.xH;
    }

    public void writeToParcel(Parcel dest, int flags) {
        e.a(this, dest, flags);
    }
}