package com.google.android.gms.identity.intents;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.identity.intents.model.CountrySpecification;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class UserAddressRequest implements SafeParcelable {
    public static final Creator<UserAddressRequest> CREATOR;
    List<CountrySpecification> Ny;
    private final int xH;

    public final class Builder {
        private Builder() {
        }

        public com.google.android.gms.identity.intents.UserAddressRequest.Builder addAllowedCountrySpecification(CountrySpecification countrySpecification) {
            if (UserAddressRequest.this.Ny == null) {
                UserAddressRequest.this.Ny = new ArrayList();
            }
            UserAddressRequest.this.Ny.add(countrySpecification);
            return this;
        }

        public com.google.android.gms.identity.intents.UserAddressRequest.Builder addAllowedCountrySpecifications(Collection<CountrySpecification> countrySpecifications) {
            if (UserAddressRequest.this.Ny == null) {
                UserAddressRequest.this.Ny = new ArrayList();
            }
            UserAddressRequest.this.Ny.addAll(countrySpecifications);
            return this;
        }

        public UserAddressRequest build() {
            if (UserAddressRequest.this.Ny != null) {
                UserAddressRequest.this.Ny = Collections.unmodifiableList(UserAddressRequest.this.Ny);
            }
            return UserAddressRequest.this;
        }
    }

    static {
        CREATOR = new a();
    }

    UserAddressRequest() {
        this.xH = 1;
    }

    UserAddressRequest(int versionCode, List<CountrySpecification> allowedCountrySpecifications) {
        this.xH = versionCode;
        this.Ny = allowedCountrySpecifications;
    }

    public static Builder newBuilder() {
        UserAddressRequest userAddressRequest = new UserAddressRequest();
        userAddressRequest.getClass();
        return new Builder(null);
    }

    public int describeContents() {
        return 0;
    }

    public int getVersionCode() {
        return this.xH;
    }

    public void writeToParcel(Parcel out, int flags) {
        a.a(this, out, flags);
    }
}