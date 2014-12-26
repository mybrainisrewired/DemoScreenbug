package com.mopub.mobileads;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import com.millennialmedia.android.MMAd;
import com.millennialmedia.android.MMException;
import com.millennialmedia.android.MMInterstitial;
import com.millennialmedia.android.MMRequest;
import com.millennialmedia.android.MMSDK;
import com.millennialmedia.android.RequestListener;
import com.mopub.mobileads.CustomEventInterstitial.CustomEventInterstitialListener;
import java.util.Map;

class MillennialInterstitial extends CustomEventInterstitial {
    public static final String APID_KEY = "adUnitID";
    private CustomEventInterstitialListener mInterstitialListener;
    private MMInterstitial mMillennialInterstitial;

    class MillennialInterstitialRequestListener implements RequestListener {
        MillennialInterstitialRequestListener() {
        }

        public void MMAdOverlayClosed(MMAd mmAd) {
            Log.d("MoPub", "Millennial interstitial ad dismissed.");
            MillennialInterstitial.this.mInterstitialListener.onInterstitialDismissed();
        }

        public void MMAdOverlayLaunched(MMAd mmAd) {
            Log.d("MoPub", "Showing Millennial interstitial ad.");
            MillennialInterstitial.this.mInterstitialListener.onInterstitialShown();
        }

        public void MMAdRequestIsCaching(MMAd mmAd) {
        }

        public void onSingleTap(MMAd mmAd) {
            Log.d("MoPub", "Millennial interstitial clicked.");
            MillennialInterstitial.this.mInterstitialListener.onInterstitialClicked();
        }

        public void requestCompleted(MMAd mmAd) {
            if (MillennialInterstitial.this.mMillennialInterstitial.isAdAvailable()) {
                Log.d("MoPub", "Millennial interstitial ad loaded successfully.");
                MillennialInterstitial.this.mInterstitialListener.onInterstitialLoaded();
            } else {
                Log.d("MoPub", "Millennial interstitial request completed, but no ad was available.");
                MillennialInterstitial.this.mInterstitialListener.onInterstitialFailed(MoPubErrorCode.NETWORK_INVALID_STATE);
            }
        }

        public void requestFailed(MMAd mmAd, MMException e) {
            if (MillennialInterstitial.this.mMillennialInterstitial == null || e == null) {
                MillennialInterstitial.this.mInterstitialListener.onInterstitialFailed(MoPubErrorCode.NETWORK_INVALID_STATE);
            } else if (e.getCode() == 17 && MillennialInterstitial.this.mMillennialInterstitial.isAdAvailable()) {
                Log.d("MoPub", "Millennial interstitial loaded successfully from cache.");
                MillennialInterstitial.this.mInterstitialListener.onInterstitialLoaded();
            } else {
                Log.d("MoPub", "Millennial interstitial ad failed to load.");
                MillennialInterstitial.this.mInterstitialListener.onInterstitialFailed(MoPubErrorCode.NETWORK_NO_FILL);
            }
        }
    }

    MillennialInterstitial() {
    }

    private boolean extrasAreValid(Map<String, String> serverExtras) {
        return serverExtras.containsKey(APID_KEY);
    }

    protected void loadInterstitial(Context context, CustomEventInterstitialListener customEventInterstitialListener, Map<String, Object> localExtras, Map<String, String> serverExtras) {
        this.mInterstitialListener = customEventInterstitialListener;
        if (extrasAreValid(serverExtras)) {
            String apid = (String) serverExtras.get(APID_KEY);
            MMSDK.initialize(context);
            Location location = (Location) localExtras.get("location");
            if (location != null) {
                MMRequest.setUserLocation(location);
            }
            this.mMillennialInterstitial = new MMInterstitial(context);
            this.mMillennialInterstitial.setListener(new MillennialInterstitialRequestListener());
            this.mMillennialInterstitial.setMMRequest(new MMRequest());
            this.mMillennialInterstitial.setApid(apid);
            this.mMillennialInterstitial.fetch();
        } else {
            this.mInterstitialListener.onInterstitialFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
        }
    }

    protected void onInvalidate() {
        if (this.mMillennialInterstitial != null) {
            this.mMillennialInterstitial.setListener(null);
        }
    }

    protected void showInterstitial() {
        if (this.mMillennialInterstitial.isAdAvailable()) {
            this.mMillennialInterstitial.display();
        } else {
            Log.d("MoPub", "Tried to show a Millennial interstitial ad before it finished loading. Please try again.");
        }
    }
}