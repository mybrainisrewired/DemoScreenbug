package com.mopub.mobileads;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.LocationService.LocationAwareness;
import com.mopub.common.MoPub;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.ResponseHeader;
import com.mopub.mobileads.factories.CustomEventInterstitialAdapterFactory;
import java.util.Map;

public class MoPubInterstitial implements CustomEventInterstitialAdapterListener {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$mopub$mobileads$MoPubInterstitial$InterstitialState;
    private Activity mActivity;
    private String mAdUnitId;
    private InterstitialState mCurrentInterstitialState;
    private CustomEventInterstitialAdapter mCustomEventInterstitialAdapter;
    private InterstitialAdListener mInterstitialAdListener;
    private MoPubInterstitialView mInterstitialView;
    private boolean mIsDestroyed;
    private MoPubInterstitialListener mListener;

    public static interface InterstitialAdListener {
        void onInterstitialClicked(MoPubInterstitial moPubInterstitial);

        void onInterstitialDismissed(MoPubInterstitial moPubInterstitial);

        void onInterstitialFailed(MoPubInterstitial moPubInterstitial, MoPubErrorCode moPubErrorCode);

        void onInterstitialLoaded(MoPubInterstitial moPubInterstitial);

        void onInterstitialShown(MoPubInterstitial moPubInterstitial);
    }

    private enum InterstitialState {
        CUSTOM_EVENT_AD_READY,
        NOT_READY;

        static {
            CUSTOM_EVENT_AD_READY = new InterstitialState("CUSTOM_EVENT_AD_READY", 0);
            NOT_READY = new InterstitialState("NOT_READY", 1);
            ENUM$VALUES = new InterstitialState[]{CUSTOM_EVENT_AD_READY, NOT_READY};
        }

        boolean isReady() {
            return this != NOT_READY;
        }
    }

    @Deprecated
    public static interface MoPubInterstitialListener {
        void OnInterstitialFailed();

        void OnInterstitialLoaded();
    }

    public class MoPubInterstitialView extends MoPubView {
        public MoPubInterstitialView(Context context) {
            super(context);
            setAutorefreshEnabled(false);
        }

        protected void adFailed(MoPubErrorCode errorCode) {
            if (MoPubInterstitial.this.mInterstitialAdListener != null) {
                MoPubInterstitial.this.mInterstitialAdListener.onInterstitialFailed(MoPubInterstitial.this, errorCode);
            }
        }

        protected void loadCustomEvent(Map<String, String> paramsMap) {
            if (paramsMap == null) {
                MoPubLog.d("Couldn't invoke custom event because the server did not specify one.");
                loadFailUrl(MoPubErrorCode.ADAPTER_NOT_FOUND);
            } else {
                if (MoPubInterstitial.this.mCustomEventInterstitialAdapter != null) {
                    MoPubInterstitial.this.mCustomEventInterstitialAdapter.invalidate();
                }
                MoPubLog.d("Loading custom event interstitial adapter.");
                MoPubInterstitial.this.mCustomEventInterstitialAdapter = CustomEventInterstitialAdapterFactory.create(MoPubInterstitial.this, (String) paramsMap.get(ResponseHeader.CUSTOM_EVENT_NAME.getKey()), (String) paramsMap.get(ResponseHeader.CUSTOM_EVENT_DATA.getKey()));
                MoPubInterstitial.this.mCustomEventInterstitialAdapter.setAdapterListener(MoPubInterstitial.this);
                MoPubInterstitial.this.mCustomEventInterstitialAdapter.loadInterstitial();
            }
        }

        protected void trackImpression() {
            MoPubLog.d("Tracking impression for interstitial.");
            if (this.mAdViewController != null) {
                this.mAdViewController.trackImpression();
            }
        }
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$mopub$mobileads$MoPubInterstitial$InterstitialState() {
        int[] iArr = $SWITCH_TABLE$com$mopub$mobileads$MoPubInterstitial$InterstitialState;
        if (iArr == null) {
            iArr = new int[InterstitialState.values().length];
            try {
                iArr[InterstitialState.CUSTOM_EVENT_AD_READY.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[InterstitialState.NOT_READY.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            $SWITCH_TABLE$com$mopub$mobileads$MoPubInterstitial$InterstitialState = iArr;
        }
        return iArr;
    }

    public MoPubInterstitial(Activity activity, String id) {
        this.mActivity = activity;
        this.mAdUnitId = id;
        this.mInterstitialView = new MoPubInterstitialView(this.mActivity);
        this.mInterstitialView.setAdUnitId(this.mAdUnitId);
        this.mCurrentInterstitialState = InterstitialState.NOT_READY;
    }

    private void resetCurrentInterstitial() {
        this.mCurrentInterstitialState = InterstitialState.NOT_READY;
        if (this.mCustomEventInterstitialAdapter != null) {
            this.mCustomEventInterstitialAdapter.invalidate();
            this.mCustomEventInterstitialAdapter = null;
        }
        this.mIsDestroyed = false;
    }

    private void showCustomEventInterstitial() {
        if (this.mCustomEventInterstitialAdapter != null) {
            this.mCustomEventInterstitialAdapter.showInterstitial();
        }
    }

    @Deprecated
    public void customEventActionWillBegin() {
        if (this.mInterstitialView != null) {
            this.mInterstitialView.registerClick();
        }
    }

    @Deprecated
    public void customEventDidFailToLoadAd() {
        if (this.mInterstitialView != null) {
            this.mInterstitialView.loadFailUrl(MoPubErrorCode.UNSPECIFIED);
        }
    }

    @Deprecated
    public void customEventDidLoadAd() {
        if (this.mInterstitialView != null) {
            this.mInterstitialView.trackImpression();
        }
    }

    public void destroy() {
        this.mIsDestroyed = true;
        if (this.mCustomEventInterstitialAdapter != null) {
            this.mCustomEventInterstitialAdapter.invalidate();
            this.mCustomEventInterstitialAdapter = null;
        }
        this.mInterstitialView.setBannerAdListener(null);
        this.mInterstitialView.destroy();
    }

    public void forceRefresh() {
        resetCurrentInterstitial();
        this.mInterstitialView.forceRefresh();
    }

    public Activity getActivity() {
        return this.mActivity;
    }

    Integer getAdTimeoutDelay() {
        return this.mInterstitialView.getAdTimeoutDelay();
    }

    public InterstitialAdListener getInterstitialAdListener() {
        return this.mInterstitialAdListener;
    }

    public String getKeywords() {
        return this.mInterstitialView.getKeywords();
    }

    @Deprecated
    public MoPubInterstitialListener getListener() {
        return this.mListener;
    }

    public Map<String, Object> getLocalExtras() {
        return this.mInterstitialView.getLocalExtras();
    }

    public Location getLocation() {
        return this.mInterstitialView.getLocation();
    }

    @Deprecated
    public LocationAwareness getLocationAwareness() {
        return LocationAwareness.fromMoPubLocationAwareness(MoPub.getLocationAwareness());
    }

    @Deprecated
    public int getLocationPrecision() {
        return MoPub.getLocationPrecision();
    }

    MoPubInterstitialView getMoPubInterstitialView() {
        return this.mInterstitialView;
    }

    public boolean getTesting() {
        return this.mInterstitialView.getTesting();
    }

    boolean isDestroyed() {
        return this.mIsDestroyed;
    }

    @Deprecated
    public boolean isFacebookSupported() {
        return false;
    }

    public boolean isReady() {
        return this.mCurrentInterstitialState.isReady();
    }

    public void load() {
        resetCurrentInterstitial();
        this.mInterstitialView.loadAd();
    }

    public void onCustomEventInterstitialClicked() {
        if (!isDestroyed()) {
            this.mInterstitialView.registerClick();
            if (this.mInterstitialAdListener != null) {
                this.mInterstitialAdListener.onInterstitialClicked(this);
            }
        }
    }

    public void onCustomEventInterstitialDismissed() {
        if (!isDestroyed()) {
            this.mCurrentInterstitialState = InterstitialState.NOT_READY;
            if (this.mInterstitialAdListener != null) {
                this.mInterstitialAdListener.onInterstitialDismissed(this);
            }
        }
    }

    public void onCustomEventInterstitialFailed(MoPubErrorCode errorCode) {
        if (!isDestroyed()) {
            this.mCurrentInterstitialState = InterstitialState.NOT_READY;
            this.mInterstitialView.loadFailUrl(errorCode);
        }
    }

    public void onCustomEventInterstitialLoaded() {
        if (!this.mIsDestroyed) {
            this.mCurrentInterstitialState = InterstitialState.CUSTOM_EVENT_AD_READY;
            if (this.mInterstitialAdListener != null) {
                this.mInterstitialAdListener.onInterstitialLoaded(this);
            } else if (this.mListener != null) {
                this.mListener.OnInterstitialLoaded();
            }
        }
    }

    public void onCustomEventInterstitialShown() {
        if (!isDestroyed()) {
            this.mInterstitialView.trackImpression();
            if (this.mInterstitialAdListener != null) {
                this.mInterstitialAdListener.onInterstitialShown(this);
            }
        }
    }

    @Deprecated
    public void setFacebookSupported(boolean enabled) {
    }

    public void setInterstitialAdListener(InterstitialAdListener listener) {
        this.mInterstitialAdListener = listener;
    }

    @Deprecated
    void setInterstitialView(MoPubInterstitialView interstitialView) {
        this.mInterstitialView = interstitialView;
    }

    public void setKeywords(String keywords) {
        this.mInterstitialView.setKeywords(keywords);
    }

    @Deprecated
    public void setListener(MoPubInterstitialListener listener) {
        this.mListener = listener;
    }

    public void setLocalExtras(Map<String, Object> extras) {
        this.mInterstitialView.setLocalExtras(extras);
    }

    @Deprecated
    public void setLocationAwareness(LocationAwareness locationAwareness) {
        MoPub.setLocationAwareness(locationAwareness.getNewLocationAwareness());
    }

    @Deprecated
    public void setLocationPrecision(int precision) {
        MoPub.setLocationPrecision(precision);
    }

    public void setTesting(boolean testing) {
        this.mInterstitialView.setTesting(testing);
    }

    public boolean show() {
        switch ($SWITCH_TABLE$com$mopub$mobileads$MoPubInterstitial$InterstitialState()[this.mCurrentInterstitialState.ordinal()]) {
            case MMAdView.TRANSITION_FADE:
                showCustomEventInterstitial();
                return true;
            default:
                return false;
        }
    }
}