package com.mopub.nativeads;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import com.mopub.common.VisibleForTesting;
import com.mopub.nativeads.MoPubNative.MoPubNativeNetworkListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class NativeAdSource {
    private static final int CACHE_LIMIT = 3;
    private static final int DEFAULT_RETRY_TIME_MILLISECONDS = 1000;
    private static final int EXPIRATION_TIME_MILLISECONDS = 900000;
    private static final double EXPONENTIAL_BACKOFF_FACTOR = 2.0d;
    private static final int MAXIMUM_RETRY_TIME_MILLISECONDS = 300000;
    private AdSourceListener mAdSourceListener;
    private MoPubNative mMoPubNative;
    private final MoPubNativeNetworkListener mMoPubNativeNetworkListener;
    private final List<TimestampWrapper<NativeResponse>> mNativeAdCache;
    private final Handler mReplenishCacheHandler;
    private final Runnable mReplenishCacheRunnable;
    @VisibleForTesting
    boolean mRequestInFlight;
    private RequestParameters mRequestParameters;
    @VisibleForTesting
    boolean mRetryInFlight;
    @VisibleForTesting
    int mRetryTimeMilliseconds;
    @VisibleForTesting
    int mSequenceNumber;

    static interface AdSourceListener {
        void onAdsAvailable();
    }

    NativeAdSource() {
        this(new ArrayList(3), new Handler());
    }

    @VisibleForTesting
    NativeAdSource(List<TimestampWrapper<NativeResponse>> nativeAdCache, Handler replenishCacheHandler) {
        this.mNativeAdCache = nativeAdCache;
        this.mReplenishCacheHandler = replenishCacheHandler;
        this.mReplenishCacheRunnable = new Runnable() {
            public void run() {
                NativeAdSource.this.mRetryInFlight = false;
                NativeAdSource.this.replenishCache();
            }
        };
        this.mMoPubNativeNetworkListener = new MoPubNativeNetworkListener() {
            public void onNativeFail(NativeErrorCode errorCode) {
                NativeAdSource.this.mRequestInFlight = false;
                if (NativeAdSource.this.mRetryTimeMilliseconds >= 300000) {
                    NativeAdSource.this.resetRetryTime();
                } else {
                    NativeAdSource.this.updateRetryTime();
                    NativeAdSource.this.mRetryInFlight = true;
                    NativeAdSource.this.mReplenishCacheHandler.postDelayed(NativeAdSource.this.mReplenishCacheRunnable, (long) NativeAdSource.this.mRetryTimeMilliseconds);
                }
            }

            public void onNativeLoad(NativeResponse nativeResponse) {
                if (NativeAdSource.this.mMoPubNative != null) {
                    NativeAdSource.this.mRequestInFlight = false;
                    NativeAdSource nativeAdSource = NativeAdSource.this;
                    nativeAdSource.mSequenceNumber++;
                    NativeAdSource.this.resetRetryTime();
                    NativeAdSource.this.mNativeAdCache.add(new TimestampWrapper(nativeResponse));
                    if (NativeAdSource.this.mNativeAdCache.size() == 1 && NativeAdSource.this.mAdSourceListener != null) {
                        NativeAdSource.this.mAdSourceListener.onAdsAvailable();
                    }
                    NativeAdSource.this.replenishCache();
                }
            }
        };
        this.mSequenceNumber = 0;
        this.mRetryTimeMilliseconds = 1000;
    }

    void clear() {
        if (this.mMoPubNative != null) {
            this.mMoPubNative.destroy();
            this.mMoPubNative = null;
        }
        this.mRequestParameters = null;
        Iterator it = this.mNativeAdCache.iterator();
        while (it.hasNext()) {
            ((NativeResponse) ((TimestampWrapper) it.next()).mInstance).destroy();
        }
        this.mNativeAdCache.clear();
        this.mReplenishCacheHandler.removeMessages(0);
        this.mRequestInFlight = false;
        this.mSequenceNumber = 0;
        resetRetryTime();
    }

    NativeResponse dequeueAd() {
        long now = SystemClock.uptimeMillis();
        if (!(this.mRequestInFlight || this.mRetryInFlight)) {
            this.mReplenishCacheHandler.post(this.mReplenishCacheRunnable);
        }
        while (!this.mNativeAdCache.isEmpty()) {
            TimestampWrapper<NativeResponse> responseWrapper = (TimestampWrapper) this.mNativeAdCache.remove(0);
            if (now - responseWrapper.mCreatedTimestamp < 900000) {
                return (NativeResponse) responseWrapper.mInstance;
            }
        }
        return null;
    }

    @Deprecated
    @VisibleForTesting
    MoPubNativeNetworkListener getMoPubNativeNetworkListener() {
        return this.mMoPubNativeNetworkListener;
    }

    void loadAds(Context context, String adUnitId, RequestParameters requestParameters) {
        loadAds(requestParameters, new MoPubNative(context, adUnitId, this.mMoPubNativeNetworkListener));
    }

    @VisibleForTesting
    void loadAds(RequestParameters requestParameters, MoPubNative moPubNative) {
        clear();
        this.mRequestParameters = requestParameters;
        this.mMoPubNative = moPubNative;
        replenishCache();
    }

    @VisibleForTesting
    void replenishCache() {
        if (!this.mRequestInFlight && this.mMoPubNative != null && this.mNativeAdCache.size() < 3) {
            this.mRequestInFlight = true;
            this.mMoPubNative.loadNativeAd(this.mRequestParameters, Integer.valueOf(this.mSequenceNumber));
        }
    }

    @VisibleForTesting
    void resetRetryTime() {
        this.mRetryTimeMilliseconds = 1000;
    }

    void setAdSourceListener(AdSourceListener adSourceListener) {
        this.mAdSourceListener = adSourceListener;
    }

    @Deprecated
    @VisibleForTesting
    void setMoPubNative(MoPubNative moPubNative) {
        this.mMoPubNative = moPubNative;
    }

    @VisibleForTesting
    void updateRetryTime() {
        this.mRetryTimeMilliseconds = (int) (((double) this.mRetryTimeMilliseconds) * 2.0d);
        if (this.mRetryTimeMilliseconds > 300000) {
            this.mRetryTimeMilliseconds = 300000;
        }
    }
}