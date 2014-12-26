package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.identity.intents.model.CountrySpecification;
import java.util.ArrayList;
import java.util.Collection;

public final class MaskedWalletRequest implements SafeParcelable {
    public static final Creator<MaskedWalletRequest> CREATOR;
    boolean abV;
    boolean abW;
    boolean abX;
    String abY;
    String abZ;
    String abd;
    String abi;
    Cart abr;
    boolean aca;
    boolean acb;
    CountrySpecification[] acc;
    boolean acd;
    boolean ace;
    ArrayList<CountrySpecification> acf;
    private final int xH;

    public final class Builder {
        private Builder() {
        }

        public com.google.android.gms.wallet.MaskedWalletRequest.Builder addAllowedCountrySpecificationForShipping(CountrySpecification countrySpecification) {
            if (MaskedWalletRequest.this.acf == null) {
                MaskedWalletRequest.this.acf = new ArrayList();
            }
            MaskedWalletRequest.this.acf.add(countrySpecification);
            return this;
        }

        public com.google.android.gms.wallet.MaskedWalletRequest.Builder addAllowedCountrySpecificationsForShipping(Collection<CountrySpecification> countrySpecifications) {
            if (countrySpecifications != null) {
                if (MaskedWalletRequest.this.acf == null) {
                    MaskedWalletRequest.this.acf = new ArrayList();
                }
                MaskedWalletRequest.this.acf.addAll(countrySpecifications);
            }
            return this;
        }

        public MaskedWalletRequest build() {
            return MaskedWalletRequest.this;
        }

        public com.google.android.gms.wallet.MaskedWalletRequest.Builder setAllowDebitCard(boolean allowDebitCard) {
            MaskedWalletRequest.this.ace = allowDebitCard;
            return this;
        }

        public com.google.android.gms.wallet.MaskedWalletRequest.Builder setAllowPrepaidCard(boolean allowPrepaidCard) {
            MaskedWalletRequest.this.acd = allowPrepaidCard;
            return this;
        }

        public com.google.android.gms.wallet.MaskedWalletRequest.Builder setCart(Cart cart) {
            MaskedWalletRequest.this.abr = cart;
            return this;
        }

        public com.google.android.gms.wallet.MaskedWalletRequest.Builder setCurrencyCode(String currencyCode) {
            MaskedWalletRequest.this.abd = currencyCode;
            return this;
        }

        public com.google.android.gms.wallet.MaskedWalletRequest.Builder setEstimatedTotalPrice(String estimatedTotalPrice) {
            MaskedWalletRequest.this.abY = estimatedTotalPrice;
            return this;
        }

        public com.google.android.gms.wallet.MaskedWalletRequest.Builder setIsBillingAgreement(boolean isBillingAgreement) {
            MaskedWalletRequest.this.acb = isBillingAgreement;
            return this;
        }

        public com.google.android.gms.wallet.MaskedWalletRequest.Builder setMerchantName(String merchantName) {
            MaskedWalletRequest.this.abZ = merchantName;
            return this;
        }

        public com.google.android.gms.wallet.MaskedWalletRequest.Builder setMerchantTransactionId(String merchantTransactionId) {
            MaskedWalletRequest.this.abi = merchantTransactionId;
            return this;
        }

        public com.google.android.gms.wallet.MaskedWalletRequest.Builder setPhoneNumberRequired(boolean phoneNumberRequired) {
            MaskedWalletRequest.this.abV = phoneNumberRequired;
            return this;
        }

        public com.google.android.gms.wallet.MaskedWalletRequest.Builder setShippingAddressRequired(boolean shippingAddressRequired) {
            MaskedWalletRequest.this.abW = shippingAddressRequired;
            return this;
        }

        public com.google.android.gms.wallet.MaskedWalletRequest.Builder setShouldRetrieveWalletObjects(boolean shouldRetrieveWalletObjects) {
            MaskedWalletRequest.this.aca = shouldRetrieveWalletObjects;
            return this;
        }

        public com.google.android.gms.wallet.MaskedWalletRequest.Builder setUseMinimalBillingAddress(boolean useMinimalBillingAddress) {
            MaskedWalletRequest.this.abX = useMinimalBillingAddress;
            return this;
        }
    }

    static {
        CREATOR = new l();
    }

    MaskedWalletRequest() {
        this.xH = 3;
        this.acd = true;
        this.ace = true;
    }

    MaskedWalletRequest(int versionCode, String merchantTransactionId, boolean phoneNumberRequired, boolean shippingAddressRequired, boolean useMinimalBillingAddress, String estimatedTotalPrice, String currencyCode, String merchantName, Cart cart, boolean shouldRetrieveWalletObjects, boolean isBillingAgreement, CountrySpecification[] allowedShippingCountrySpecifications, boolean allowPrepaidCard, boolean allowDebitCard, ArrayList<CountrySpecification> allowedCountrySpecificationsForShipping) {
        this.xH = versionCode;
        this.abi = merchantTransactionId;
        this.abV = phoneNumberRequired;
        this.abW = shippingAddressRequired;
        this.abX = useMinimalBillingAddress;
        this.abY = estimatedTotalPrice;
        this.abd = currencyCode;
        this.abZ = merchantName;
        this.abr = cart;
        this.aca = shouldRetrieveWalletObjects;
        this.acb = isBillingAgreement;
        this.acc = allowedShippingCountrySpecifications;
        this.acd = allowPrepaidCard;
        this.ace = allowDebitCard;
        this.acf = allowedCountrySpecificationsForShipping;
    }

    public static Builder newBuilder() {
        MaskedWalletRequest maskedWalletRequest = new MaskedWalletRequest();
        maskedWalletRequest.getClass();
        return new Builder(null);
    }

    public boolean allowDebitCard() {
        return this.ace;
    }

    public boolean allowPrepaidCard() {
        return this.acd;
    }

    public int describeContents() {
        return 0;
    }

    public ArrayList<CountrySpecification> getAllowedCountrySpecificationsForShipping() {
        return this.acf;
    }

    public CountrySpecification[] getAllowedShippingCountrySpecifications() {
        return this.acc;
    }

    public Cart getCart() {
        return this.abr;
    }

    public String getCurrencyCode() {
        return this.abd;
    }

    public String getEstimatedTotalPrice() {
        return this.abY;
    }

    public String getMerchantName() {
        return this.abZ;
    }

    public String getMerchantTransactionId() {
        return this.abi;
    }

    public int getVersionCode() {
        return this.xH;
    }

    public boolean isBillingAgreement() {
        return this.acb;
    }

    public boolean isPhoneNumberRequired() {
        return this.abV;
    }

    public boolean isShippingAddressRequired() {
        return this.abW;
    }

    public boolean shouldRetrieveWalletObjects() {
        return this.aca;
    }

    public boolean useMinimalBillingAddress() {
        return this.abX;
    }

    public void writeToParcel(Parcel dest, int flags) {
        l.a(this, dest, flags);
    }
}