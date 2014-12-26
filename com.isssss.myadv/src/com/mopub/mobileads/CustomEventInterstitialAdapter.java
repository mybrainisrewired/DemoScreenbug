package com.mopub.mobileads;

import android.content.Context;
import android.os.Handler;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.Json;
import com.mopub.mobileads.CustomEventInterstitial.CustomEventInterstitialListener;
import com.mopub.mobileads.factories.CustomEventInterstitialFactory;
import java.util.HashMap;
import java.util.Map;

public class CustomEventInterstitialAdapter implements CustomEventInterstitialListener {
    public static final int DEFAULT_INTERSTITIAL_TIMEOUT_DELAY = 30000;
    private Context mContext;
    private CustomEventInterstitial mCustomEventInterstitial;
    private CustomEventInterstitialAdapterListener mCustomEventInterstitialAdapterListener;
    private final Handler mHandler;
    private boolean mInvalidated;
    private Map<String, Object> mLocalExtras;
    private final MoPubInterstitial mMoPubInterstitial;
    private Map<String, String> mServerExtras;
    private final Runnable mTimeout;

    static interface CustomEventInterstitialAdapterListener {
        void onCustomEventInterstitialClicked();

        void onCustomEventInterstitialDismissed();

        void onCustomEventInterstitialFailed(MoPubErrorCode moPubErrorCode);

        void onCustomEventInterstitialLoaded();

        void onCustomEventInterstitialShown();
    }

    public CustomEventInterstitialAdapter(MoPubInterstitial moPubInterstitial, String className, String jsonParams) {
        this.mHandler = new Handler();
        this.mMoPubInterstitial = moPubInterstitial;
        this.mServerExtras = new HashMap();
        this.mLocalExtras = new HashMap();
        this.mContext = this.mMoPubInterstitial.getActivity();
        this.mTimeout = new Runnable() {
            public void run() {
                MoPubLog.d("Third-party network timed out.");
                CustomEventInterstitialAdapter.this.onInterstitialFailed(MoPubErrorCode.NETWORK_TIMEOUT);
                CustomEventInterstitialAdapter.this.invalidate();
            }
        };
        MoPubLog.d(new StringBuilder("Attempting to invoke custom event: ").append(className).toString());
        try {
            this.mCustomEventInterstitial = CustomEventInterstitialFactory.create(className);
            try {
                this.mServerExtras = Json.jsonStringToMap(jsonParams);
            } catch (Exception e) {
                MoPubLog.d(new StringBuilder("Failed to create Map from JSON: ").append(jsonParams).toString());
            }
            this.mLocalExtras = this.mMoPubInterstitial.getLocalExtras();
            if (this.mMoPubInterstitial.getLocation() != null) {
                this.mLocalExtras.put("location", this.mMoPubInterstitial.getLocation());
            }
            AdViewController adViewController = this.mMoPubInterstitial.getMoPubInterstitialView().getAdViewController();
            if (adViewController != null) {
                this.mLocalExtras.put(AdFetcher.AD_CONFIGURATION_KEY, adViewController.getAdConfiguration());
            }
        } catch (Exception e2) {
            MoPubLog.d(new StringBuilder("Couldn't locate or instantiate custom event: ").append(className).append(".").toString());
            this.mMoPubInterstitial.onCustomEventInterstitialFailed(MoPubErrorCode.ADAPTER_NOT_FOUND);
        }
    }

    private void cancelTimeout() {
        this.mHandler.removeCallbacks(this.mTimeout);
    }

    private int getTimeoutDelayMilliseconds() {
        return (this.mMoPubInterstitial == null || this.mMoPubInterstitial.getAdTimeoutDelay() == null || this.mMoPubInterstitial.getAdTimeoutDelay().intValue() < 0) ? DEFAULT_INTERSTITIAL_TIMEOUT_DELAY : this.mMoPubInterstitial.getAdTimeoutDelay().intValue() * 1000;
    }

    void invalidate() {
        if (this.mCustomEventInterstitial != null) {
            this.mCustomEventInterstitial.onInvalidate();
        }
        this.mCustomEventInterstitial = null;
        this.mContext = null;
        this.mServerExtras = null;
        this.mLocalExtras = null;
        this.mCustomEventInterstitialAdapterListener = null;
        this.mInvalidated = true;
    }

    boolean isInvalidated() {
        return this.mInvalidated;
    }

    void loadInterstitial() {
        if (!isInvalidated() && this.mCustomEventInterstitial != null) {
            if (getTimeoutDelayMilliseconds() > 0) {
                this.mHandler.postDelayed(this.mTimeout, (long) getTimeoutDelayMilliseconds());
            }
            this.mCustomEventInterstitial.loadInterstitial(this.mContext, this, this.mLocalExtras, this.mServerExtras);
        }
    }

    public void onInterstitialClicked() {
        if (!isInvalidated() && this.mCustomEventInterstitialAdapterListener != null) {
            this.mCustomEventInterstitialAdapterListener.onCustomEventInterstitialClicked();
        }
    }

    public void onInterstitialDismissed() {
        if (!isInvalidated() && this.mCustomEventInterstitialAdapterListener != null) {
            this.mCustomEventInterstitialAdapterListener.onCustomEventInterstitialDismissed();
        }
    }

    public void onInterstitialFailed(MoPubErrorCode errorCode) {
        if (!isInvalidated() && this.mCustomEventInterstitialAdapterListener != null) {
            if (errorCode == null) {
                errorCode = MoPubErrorCode.UNSPECIFIED;
            }
            cancelTimeout();
            this.mCustomEventInterstitialAdapterListener.onCustomEventInterstitialFailed(errorCode);
        }
    }

    public void onInterstitialLoaded() {
        if (!isInvalidated()) {
            cancelTimeout();
            if (this.mCustomEventInterstitialAdapterListener != null) {
                this.mCustomEventInterstitialAdapterListener.onCustomEventInterstitialLoaded();
            }
        }
    }

    public void onInterstitialShown() {
        if (!isInvalidated() && this.mCustomEventInterstitialAdapterListener != null) {
            this.mCustomEventInterstitialAdapterListener.onCustomEventInterstitialShown();
        }
    }

    public void onLeaveApplication() {
        onInterstitialClicked();
    }

    void setAdapterListener(CustomEventInterstitialAdapterListener listener) {
        this.mCustomEventInterstitialAdapterListener = listener;
    }

    @Deprecated
    void setCustomEventInterstitial(CustomEventInterstitial interstitial) {
        this.mCustomEventInterstitial = interstitial;
    }

    void showInterstitial() {
        if (!isInvalidated() && this.mCustomEventInterstitial != null) {
            this.mCustomEventInterstitial.showInterstitial();
        }
    }
}