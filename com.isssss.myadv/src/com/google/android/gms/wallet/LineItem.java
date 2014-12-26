package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class LineItem implements SafeParcelable {
    public static final Creator<LineItem> CREATOR;
    String abc;
    String abd;
    String abv;
    String abw;
    int abx;
    String description;
    private final int xH;

    public final class Builder {
        private Builder() {
        }

        public LineItem build() {
            return LineItem.this;
        }

        public com.google.android.gms.wallet.LineItem.Builder setCurrencyCode(String currencyCode) {
            LineItem.this.abd = currencyCode;
            return this;
        }

        public com.google.android.gms.wallet.LineItem.Builder setDescription(String description) {
            LineItem.this.description = description;
            return this;
        }

        public com.google.android.gms.wallet.LineItem.Builder setQuantity(String quantity) {
            LineItem.this.abv = quantity;
            return this;
        }

        public com.google.android.gms.wallet.LineItem.Builder setRole(int role) {
            LineItem.this.abx = role;
            return this;
        }

        public com.google.android.gms.wallet.LineItem.Builder setTotalPrice(String totalPrice) {
            LineItem.this.abc = totalPrice;
            return this;
        }

        public com.google.android.gms.wallet.LineItem.Builder setUnitPrice(String unitPrice) {
            LineItem.this.abw = unitPrice;
            return this;
        }
    }

    public static interface Role {
        public static final int REGULAR = 0;
        public static final int SHIPPING = 2;
        public static final int TAX = 1;
    }

    static {
        CREATOR = new i();
    }

    LineItem() {
        this.xH = 1;
        this.abx = 0;
    }

    LineItem(int versionCode, String description, String quantity, String unitPrice, String totalPrice, int role, String currencyCode) {
        this.xH = versionCode;
        this.description = description;
        this.abv = quantity;
        this.abw = unitPrice;
        this.abc = totalPrice;
        this.abx = role;
        this.abd = currencyCode;
    }

    public static Builder newBuilder() {
        LineItem lineItem = new LineItem();
        lineItem.getClass();
        return new Builder(null);
    }

    public int describeContents() {
        return 0;
    }

    public String getCurrencyCode() {
        return this.abd;
    }

    public String getDescription() {
        return this.description;
    }

    public String getQuantity() {
        return this.abv;
    }

    public int getRole() {
        return this.abx;
    }

    public String getTotalPrice() {
        return this.abc;
    }

    public String getUnitPrice() {
        return this.abw;
    }

    public int getVersionCode() {
        return this.xH;
    }

    public void writeToParcel(Parcel dest, int flags) {
        i.a(this, dest, flags);
    }
}