package com.mopub.mobileads;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.Json;
import com.mopub.mobileads.CustomEventBanner.CustomEventBannerListener;
import com.mopub.mobileads.factories.CustomEventBannerFactory;
import java.util.HashMap;
import java.util.Map;

public class CustomEventBannerAdapter implements CustomEventBannerListener {
    public static final int DEFAULT_BANNER_TIMEOUT_DELAY = 10000;
    private Context mContext;
    private CustomEventBanner mCustomEventBanner;
    private final Handler mHandler;
    private boolean mInvalidated;
    private Map<String, Object> mLocalExtras;
    private MoPubView mMoPubView;
    private Map<String, String> mServerExtras;
    private boolean mStoredAutorefresh;
    private final Runnable mTimeout;

    public CustomEventBannerAdapter(MoPubView moPubView, String className, String classData) {
        this.mHandler = new Handler();
        this.mMoPubView = moPubView;
        this.mContext = moPubView.getContext();
        this.mLocalExtras = new HashMap();
        this.mServerExtras = new HashMap();
        this.mTimeout = new Runnable() {
            public void run() {
                MoPubLog.d("Third-party network timed out.");
                CustomEventBannerAdapter.this.onBannerFailed(MoPubErrorCode.NETWORK_TIMEOUT);
                CustomEventBannerAdapter.this.invalidate();
            }
        };
        MoPubLog.d(new StringBuilder("Attempting to invoke custom event: ").append(className).toString());
        try {
            this.mCustomEventBanner = CustomEventBannerFactory.create(className);
            try {
                this.mServerExtras = Json.jsonStringToMap(classData);
            } catch (Exception e) {
                MoPubLog.d(new StringBuilder("Failed to create Map from JSON: ").append(classData).append(e.toString()).toString());
            }
            this.mLocalExtras = this.mMoPubView.getLocalExtras();
            if (this.mMoPubView.getLocation() != null) {
                this.mLocalExtras.put("location", this.mMoPubView.getLocation());
            }
            if (this.mMoPubView.getAdViewController() != null) {
                this.mLocalExtras.put(AdFetcher.AD_CONFIGURATION_KEY, this.mMoPubView.getAdViewController().getAdConfiguration());
            }
        } catch (Exception e2) {
            MoPubLog.d(new StringBuilder("Couldn't locate or instantiate custom event: ").append(className).append(".").toString());
            this.mMoPubView.loadFailUrl(MoPubErrorCode.ADAPTER_NOT_FOUND);
        }
    }

    private void cancelTimeout() {
        this.mHandler.removeCallbacks(this.mTimeout);
    }

    private int getTimeoutDelayMilliseconds() {
        return (this.mMoPubView == null || this.mMoPubView.getAdTimeoutDelay() == null || this.mMoPubView.getAdTimeoutDelay().intValue() < 0) ? DEFAULT_BANNER_TIMEOUT_DELAY : this.mMoPubView.getAdTimeoutDelay().intValue() * 1000;
    }

    void invalidate() {
        if (this.mCustomEventBanner != null) {
            this.mCustomEventBanner.onInvalidate();
        }
        this.mContext = null;
        this.mCustomEventBanner = null;
        this.mLocalExtras = null;
        this.mServerExtras = null;
        this.mInvalidated = true;
    }

    boolean isInvalidated() {
        return this.mInvalidated;
    }

    void loadAd() {
        if (!isInvalidated() && this.mCustomEventBanner != null) {
            if (getTimeoutDelayMilliseconds() > 0) {
                this.mHandler.postDelayed(this.mTimeout, (long) getTimeoutDelayMilliseconds());
            }
            this.mCustomEventBanner.loadBanner(this.mContext, this, this.mLocalExtras, this.mServerExtras);
        }
    }

    public void onBannerClicked() {
        if (!isInvalidated() && this.mMoPubView != null) {
            this.mMoPubView.registerClick();
        }
    }

    public void onBannerCollapsed() {
        if (!isInvalidated()) {
            this.mMoPubView.setAutorefreshEnabled(this.mStoredAutorefresh);
            this.mMoPubView.adClosed();
        }
    }

    public void onBannerExpanded() {
        if (!isInvalidated()) {
            this.mStoredAutorefresh = this.mMoPubView.getAutorefreshEnabled();
            this.mMoPubView.setAutorefreshEnabled(false);
            this.mMoPubView.adPresentedOverlay();
        }
    }

    public void onBannerFailed(MoPubErrorCode errorCode) {
        if (!isInvalidated() && this.mMoPubView != null) {
            if (errorCode == null) {
                errorCode = MoPubErrorCode.UNSPECIFIED;
            }
            cancelTimeout();
            this.mMoPubView.loadFailUrl(errorCode);
        }
    }

    public void onBannerLoaded(View bannerView) {
        if (!isInvalidated()) {
            cancelTimeout();
            if (this.mMoPubView != null) {
                this.mMoPubView.nativeAdLoaded();
                this.mMoPubView.setAdContentView(bannerView);
                if (!(bannerView instanceof HtmlBannerWebView)) {
                    this.mMoPubView.trackNativeImpression();
                }
            }
        }
    }

    public void onLeaveApplication() {
        onBannerClicked();
    }
}