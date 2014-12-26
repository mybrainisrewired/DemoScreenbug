package com.mopub.mobileads;

import android.content.Context;
import android.os.Build;
import android.provider.Settings.Secure;
import android.webkit.WebView;
import com.mopub.common.MoPub;
import com.mopub.common.Preconditions;
import com.mopub.common.util.DateAndTime;
import com.mopub.common.util.ResponseHeader;
import com.mopub.common.util.Utils;
import com.mopub.common.util.VersionCode;
import com.mopub.mobileads.util.HttpResponses;
import java.io.Serializable;
import java.util.Map;
import org.apache.http.HttpResponse;

public class AdConfiguration implements Serializable {
    private static final int DEFAULT_REFRESH_TIME_MILLISECONDS = 60000;
    private static final int MINIMUM_REFRESH_TIME_MILLISECONDS = 10000;
    private static final String mPlatform = "Android";
    private static final long serialVersionUID = 0;
    private Integer mAdTimeoutDelay;
    private String mAdType;
    private String mAdUnitId;
    private long mBroadcastIdentifier;
    private String mClickthroughUrl;
    private final String mDeviceLocale;
    private final String mDeviceModel;
    private String mDspCreativeId;
    private String mFailUrl;
    private final String mHashedUdid;
    private int mHeight;
    private String mImpressionUrl;
    private String mNetworkType;
    private final int mPlatformVersion;
    private String mRedirectUrl;
    private int mRefreshTimeMilliseconds;
    private String mResponseString;
    private final String mSdkVersion;
    private long mTimeStamp;
    private final String mUserAgent;
    private int mWidth;

    AdConfiguration(Context context) {
        setDefaults();
        if (context != null) {
            String udid = Secure.getString(context.getContentResolver(), "android_id");
            if (udid == null) {
                udid = Preconditions.EMPTY_ARGUMENTS;
            }
            this.mHashedUdid = Utils.sha1(udid);
            this.mUserAgent = new WebView(context).getSettings().getUserAgentString();
            this.mDeviceLocale = context.getResources().getConfiguration().locale.toString();
        } else {
            this.mHashedUdid = null;
            this.mUserAgent = null;
            this.mDeviceLocale = null;
        }
        this.mBroadcastIdentifier = Utils.generateUniqueId();
        this.mDeviceModel = new StringBuilder(String.valueOf(Build.MANUFACTURER)).append(" ").append(Build.MODEL).toString();
        this.mPlatformVersion = VersionCode.currentApiLevel().getApiLevel();
        this.mSdkVersion = MoPub.SDK_VERSION;
    }

    static AdConfiguration extractFromMap(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        Object adConfiguration = map.get(AdFetcher.AD_CONFIGURATION_KEY);
        return adConfiguration instanceof AdConfiguration ? (AdConfiguration) adConfiguration : null;
    }

    private void setDefaults() {
        this.mBroadcastIdentifier = 0;
        this.mAdUnitId = null;
        this.mResponseString = null;
        this.mAdType = null;
        this.mNetworkType = null;
        this.mRedirectUrl = null;
        this.mClickthroughUrl = null;
        this.mImpressionUrl = null;
        this.mTimeStamp = DateAndTime.now().getTime();
        this.mWidth = 0;
        this.mHeight = 0;
        this.mAdTimeoutDelay = null;
        this.mRefreshTimeMilliseconds = 60000;
        this.mFailUrl = null;
        this.mDspCreativeId = null;
    }

    void addHttpResponse(HttpResponse httpResponse) {
        this.mAdType = HttpResponses.extractHeader(httpResponse, ResponseHeader.AD_TYPE);
        this.mNetworkType = HttpResponses.extractHeader(httpResponse, ResponseHeader.NETWORK_TYPE);
        this.mRedirectUrl = HttpResponses.extractHeader(httpResponse, ResponseHeader.REDIRECT_URL);
        this.mClickthroughUrl = HttpResponses.extractHeader(httpResponse, ResponseHeader.CLICKTHROUGH_URL);
        this.mFailUrl = HttpResponses.extractHeader(httpResponse, ResponseHeader.FAIL_URL);
        this.mImpressionUrl = HttpResponses.extractHeader(httpResponse, ResponseHeader.IMPRESSION_URL);
        this.mTimeStamp = DateAndTime.now().getTime();
        this.mWidth = HttpResponses.extractIntHeader(httpResponse, ResponseHeader.WIDTH, 0);
        this.mHeight = HttpResponses.extractIntHeader(httpResponse, ResponseHeader.HEIGHT, 0);
        this.mAdTimeoutDelay = HttpResponses.extractIntegerHeader(httpResponse, ResponseHeader.AD_TIMEOUT);
        if (httpResponse.containsHeader(ResponseHeader.REFRESH_TIME.getKey())) {
            this.mRefreshTimeMilliseconds = HttpResponses.extractIntHeader(httpResponse, ResponseHeader.REFRESH_TIME, 0) * 1000;
            this.mRefreshTimeMilliseconds = Math.max(this.mRefreshTimeMilliseconds, MINIMUM_REFRESH_TIME_MILLISECONDS);
        } else {
            this.mRefreshTimeMilliseconds = 0;
        }
        this.mDspCreativeId = HttpResponses.extractHeader(httpResponse, ResponseHeader.DSP_CREATIVE_ID);
    }

    void cleanup() {
        setDefaults();
    }

    Integer getAdTimeoutDelay() {
        return this.mAdTimeoutDelay;
    }

    String getAdType() {
        return this.mAdType;
    }

    String getAdUnitId() {
        return this.mAdUnitId;
    }

    long getBroadcastIdentifier() {
        return this.mBroadcastIdentifier;
    }

    String getClickthroughUrl() {
        return this.mClickthroughUrl;
    }

    String getDeviceLocale() {
        return this.mDeviceLocale;
    }

    String getDeviceModel() {
        return this.mDeviceModel;
    }

    String getDspCreativeId() {
        return this.mDspCreativeId;
    }

    String getFailUrl() {
        return this.mFailUrl;
    }

    String getHashedUdid() {
        return this.mHashedUdid;
    }

    int getHeight() {
        return this.mHeight;
    }

    String getImpressionUrl() {
        return this.mImpressionUrl;
    }

    String getNetworkType() {
        return this.mNetworkType;
    }

    String getPlatform() {
        return mPlatform;
    }

    int getPlatformVersion() {
        return this.mPlatformVersion;
    }

    String getRedirectUrl() {
        return this.mRedirectUrl;
    }

    int getRefreshTimeMilliseconds() {
        return this.mRefreshTimeMilliseconds;
    }

    String getResponseString() {
        return this.mResponseString;
    }

    String getSdkVersion() {
        return this.mSdkVersion;
    }

    long getTimeStamp() {
        return this.mTimeStamp;
    }

    String getUserAgent() {
        return this.mUserAgent;
    }

    int getWidth() {
        return this.mWidth;
    }

    void setAdUnitId(String adUnitId) {
        this.mAdUnitId = adUnitId;
    }

    @Deprecated
    void setClickthroughUrl(String clickthroughUrl) {
        this.mClickthroughUrl = clickthroughUrl;
    }

    void setFailUrl(String failUrl) {
        this.mFailUrl = failUrl;
    }

    @Deprecated
    void setRefreshTimeMilliseconds(int refreshTimeMilliseconds) {
        this.mRefreshTimeMilliseconds = refreshTimeMilliseconds;
    }

    void setResponseString(String responseString) {
        this.mResponseString = responseString;
    }
}