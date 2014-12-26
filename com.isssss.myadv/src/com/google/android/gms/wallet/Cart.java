package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.ArrayList;
import java.util.List;

public final class Cart implements SafeParcelable {
    public static final Creator<Cart> CREATOR;
    String abc;
    String abd;
    ArrayList<LineItem> abe;
    private final int xH;

    public final class Builder {
        private Builder() {
        }

        public com.google.android.gms.wallet.Cart.Builder addLineItem(LineItem lineItem) {
            Cart.this.abe.add(lineItem);
            return this;
        }

        public Cart build() {
            return Cart.this;
        }

        public com.google.android.gms.wallet.Cart.Builder setCurrencyCode(String currencyCode) {
            Cart.this.abd = currencyCode;
            return this;
        }

        public com.google.android.gms.wallet.Cart.Builder setLineItems(List<LineItem> lineItems) {
            Cart.this.abe.clear();
            Cart.this.abe.addAll(lineItems);
            return this;
        }

        public com.google.android.gms.wallet.Cart.Builder setTotalPrice(String totalPrice) {
            Cart.this.abc = totalPrice;
            return this;
        }
    }

    static {
        CREATOR = new b();
    }

    Cart() {
        this.xH = 1;
        this.abe = new ArrayList();
    }

    Cart(int versionCode, String totalPrice, String currencyCode, ArrayList<LineItem> lineItems) {
        this.xH = versionCode;
        this.abc = totalPrice;
        this.abd = currencyCode;
        this.abe = lineItems;
    }

    public static Builder newBuilder() {
        Cart cart = new Cart();
        cart.getClass();
        return new Builder(null);
    }

    public int describeContents() {
        return 0;
    }

    public String getCurrencyCode() {
        return this.abd;
    }

    public ArrayList<LineItem> getLineItems() {
        return this.abe;
    }

    public String getTotalPrice() {
        return this.abc;
    }

    public int getVersionCode() {
        return this.xH;
    }

    public void writeToParcel(Parcel dest, int flags) {
        b.a(this, dest, flags);
    }
}