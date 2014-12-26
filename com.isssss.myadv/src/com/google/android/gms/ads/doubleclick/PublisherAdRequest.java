package com.google.android.gms.ads.doubleclick;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.mediation.MediationAdapter;
import com.google.android.gms.ads.mediation.NetworkExtras;
import com.google.android.gms.internal.as;
import com.google.android.gms.internal.as.a;
import com.google.android.gms.internal.fq;
import java.util.Date;
import java.util.Set;

public final class PublisherAdRequest {
    public static final String DEVICE_ID_EMULATOR;
    public static final int ERROR_CODE_INTERNAL_ERROR = 0;
    public static final int ERROR_CODE_INVALID_REQUEST = 1;
    public static final int ERROR_CODE_NETWORK_ERROR = 2;
    public static final int ERROR_CODE_NO_FILL = 3;
    public static final int GENDER_FEMALE = 2;
    public static final int GENDER_MALE = 1;
    public static final int GENDER_UNKNOWN = 0;
    private final as kp;

    public static final class Builder {
        private final a kq;

        public Builder() {
            this.kq = new a();
        }

        public com.google.android.gms.ads.doubleclick.PublisherAdRequest.Builder addKeyword(String keyword) {
            this.kq.g(keyword);
            return this;
        }

        public com.google.android.gms.ads.doubleclick.PublisherAdRequest.Builder addNetworkExtras(NetworkExtras networkExtras) {
            this.kq.a(networkExtras);
            return this;
        }

        public com.google.android.gms.ads.doubleclick.PublisherAdRequest.Builder addNetworkExtrasBundle(Class<? extends MediationAdapter> adapterClass, Bundle networkExtras) {
            this.kq.a(adapterClass, networkExtras);
            return this;
        }

        public com.google.android.gms.ads.doubleclick.PublisherAdRequest.Builder addTestDevice(String deviceId) {
            this.kq.h(deviceId);
            return this;
        }

        public PublisherAdRequest build() {
            return new PublisherAdRequest(null);
        }

        public com.google.android.gms.ads.doubleclick.PublisherAdRequest.Builder setBirthday(Date birthday) {
            this.kq.a(birthday);
            return this;
        }

        public com.google.android.gms.ads.doubleclick.PublisherAdRequest.Builder setContentUrl(String contentUrl) {
            fq.b((Object)contentUrl, (Object)"Content URL must be non-null.");
            fq.b(contentUrl, (Object)"Content URL must be non-empty.");
            fq.a(contentUrl.length() <= 512, "Content URL must not exceed %d in length.  Provided length was %d.", new Object[]{Integer.valueOf(AdRequest.MAX_CONTENT_URL_LENGTH), Integer.valueOf(contentUrl.length())});
            this.kq.i(contentUrl);
            return this;
        }

        public com.google.android.gms.ads.doubleclick.PublisherAdRequest.Builder setGender(int gender) {
            this.kq.d(gender);
            return this;
        }

        public com.google.android.gms.ads.doubleclick.PublisherAdRequest.Builder setLocation(Location location) {
            this.kq.a(location);
            return this;
        }

        public com.google.android.gms.ads.doubleclick.PublisherAdRequest.Builder setManualImpressionsEnabled(boolean manualImpressionsEnabled) {
            this.kq.f(manualImpressionsEnabled);
            return this;
        }

        public com.google.android.gms.ads.doubleclick.PublisherAdRequest.Builder setPublisherProvidedId(String publisherProvidedId) {
            this.kq.j(publisherProvidedId);
            return this;
        }

        public com.google.android.gms.ads.doubleclick.PublisherAdRequest.Builder tagForChildDirectedTreatment(boolean tagForChildDirectedTreatment) {
            this.kq.g(tagForChildDirectedTreatment);
            return this;
        }
    }

    static {
        DEVICE_ID_EMULATOR = as.DEVICE_ID_EMULATOR;
    }

    private PublisherAdRequest(Builder builder) {
        this.kp = new as(builder.kq);
    }

    as O() {
        return this.kp;
    }

    public Date getBirthday() {
        return this.kp.getBirthday();
    }

    public String getContentUrl() {
        return this.kp.getContentUrl();
    }

    public int getGender() {
        return this.kp.getGender();
    }

    public Set<String> getKeywords() {
        return this.kp.getKeywords();
    }

    public Location getLocation() {
        return this.kp.getLocation();
    }

    public boolean getManualImpressionsEnabled() {
        return this.kp.getManualImpressionsEnabled();
    }

    @Deprecated
    public <T extends NetworkExtras> T getNetworkExtras(Class<T> networkExtrasClass) {
        return this.kp.getNetworkExtras(networkExtrasClass);
    }

    public <T extends MediationAdapter> Bundle getNetworkExtrasBundle(Class<T> adapterClass) {
        return this.kp.getNetworkExtrasBundle(adapterClass);
    }

    public String getPublisherProvidedId() {
        return this.kp.getPublisherProvidedId();
    }

    public boolean isTestDevice(Context context) {
        return this.kp.isTestDevice(context);
    }
}