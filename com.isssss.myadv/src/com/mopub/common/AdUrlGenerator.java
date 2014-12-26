package com.mopub.common;

import android.content.Context;
import android.location.Location;
import com.inmobi.commons.analytics.db.AnalyticsEvent;
import com.inmobi.commons.analytics.iat.impl.AdTrackerConstants;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.ClientMetadata.MoPubNetworkType;
import com.mopub.common.util.IntentUtils;

public abstract class AdUrlGenerator extends BaseUrlGenerator {
    private static TwitterAppInstalledStatus sTwitterAppInstalledStatus;
    protected String mAdUnitId;
    protected Context mContext;
    protected String mKeywords;
    protected Location mLocation;

    public enum TwitterAppInstalledStatus {
        UNKNOWN,
        NOT_INSTALLED,
        INSTALLED;

        static {
            UNKNOWN = new com.mopub.common.AdUrlGenerator.TwitterAppInstalledStatus("UNKNOWN", 0);
            NOT_INSTALLED = new com.mopub.common.AdUrlGenerator.TwitterAppInstalledStatus("NOT_INSTALLED", 1);
            INSTALLED = new com.mopub.common.AdUrlGenerator.TwitterAppInstalledStatus("INSTALLED", 2);
            ENUM$VALUES = new com.mopub.common.AdUrlGenerator.TwitterAppInstalledStatus[]{UNKNOWN, NOT_INSTALLED, INSTALLED};
        }
    }

    static {
        sTwitterAppInstalledStatus = TwitterAppInstalledStatus.UNKNOWN;
    }

    public AdUrlGenerator(Context context) {
        this.mContext = context;
    }

    private void addParam(String key, MoPubNetworkType value) {
        addParam(key, value.toString());
    }

    private int mncPortionLength(String networkOperator) {
        return Math.min(MMAdView.TRANSITION_DOWN, networkOperator.length());
    }

    @Deprecated
    public static void setTwitterAppInstalledStatus(TwitterAppInstalledStatus status) {
        sTwitterAppInstalledStatus = status;
    }

    public TwitterAppInstalledStatus getTwitterAppInstallStatus() {
        return IntentUtils.canHandleTwitterUrl(this.mContext) ? TwitterAppInstalledStatus.INSTALLED : TwitterAppInstalledStatus.NOT_INSTALLED;
    }

    protected void setAdUnitId(String adUnitId) {
        addParam(AnalyticsEvent.EVENT_ID, adUnitId);
    }

    protected void setCarrierName(String networkOperatorName) {
        addParam("cn", networkOperatorName);
    }

    protected void setDensity(float density) {
        addParam("sc_a", density);
    }

    protected void setIsoCountryCode(String networkCountryIso) {
        addParam("iso", networkCountryIso);
    }

    protected void setKeywords(String keywords) {
        addParam("q", keywords);
    }

    protected void setLocation(Location location) {
        if (location != null) {
            addParam("ll", new StringBuilder(String.valueOf(location.getLatitude())).append(",").append(location.getLongitude()).toString());
            addParam("lla", ((int) location.getAccuracy()));
        }
    }

    protected void setMccCode(String networkOperator) {
        addParam("mcc", networkOperator == null ? Preconditions.EMPTY_ARGUMENTS : networkOperator.substring(0, mncPortionLength(networkOperator)));
    }

    protected void setMncCode(String networkOperator) {
        addParam("mnc", networkOperator == null ? Preconditions.EMPTY_ARGUMENTS : networkOperator.substring(mncPortionLength(networkOperator)));
    }

    protected void setMraidFlag(boolean mraid) {
        if (mraid) {
            addParam("mr", "1");
        }
    }

    protected void setNetworkType(MoPubNetworkType networkType) {
        addParam("ct", networkType);
    }

    protected void setOrientation(String orientation) {
        addParam("o", orientation);
    }

    protected void setSdkVersion(String sdkVersion) {
        addParam("nv", sdkVersion);
    }

    protected void setTimezone(String timeZoneOffsetString) {
        addParam("z", timeZoneOffsetString);
    }

    protected void setTwitterAppInstalledFlag() {
        if (sTwitterAppInstalledStatus == TwitterAppInstalledStatus.UNKNOWN) {
            sTwitterAppInstalledStatus = getTwitterAppInstallStatus();
        }
        if (sTwitterAppInstalledStatus == TwitterAppInstalledStatus.INSTALLED) {
            addParam(AdTrackerConstants.TIMESTAMP, "1");
        }
    }

    public AdUrlGenerator withAdUnitId(String adUnitId) {
        this.mAdUnitId = adUnitId;
        return this;
    }

    @Deprecated
    public AdUrlGenerator withFacebookSupported(boolean enabled) {
        return this;
    }

    public AdUrlGenerator withKeywords(String keywords) {
        this.mKeywords = keywords;
        return this;
    }

    public AdUrlGenerator withLocation(Location location) {
        this.mLocation = location;
        return this;
    }
}