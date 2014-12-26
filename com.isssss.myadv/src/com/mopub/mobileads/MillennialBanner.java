package com.mopub.mobileads;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import com.millennialmedia.android.MMAd;
import com.millennialmedia.android.MMAdView;
import com.millennialmedia.android.MMException;
import com.millennialmedia.android.MMRequest;
import com.millennialmedia.android.MMSDK;
import com.millennialmedia.android.RequestListener;
import com.mopub.mobileads.CustomEventBanner.CustomEventBannerListener;
import java.util.Map;

class MillennialBanner extends CustomEventBanner {
    public static final String AD_HEIGHT_KEY = "adHeight";
    public static final String AD_WIDTH_KEY = "adWidth";
    public static final String APID_KEY = "adUnitID";
    private CustomEventBannerListener mBannerListener;
    private MMAdView mMillennialAdView;

    class MillennialBannerRequestListener implements RequestListener {
        MillennialBannerRequestListener() {
        }

        public void MMAdOverlayClosed(MMAd mmAd) {
            Log.d("MoPub", "Millennial banner ad closed.");
            MillennialBanner.this.mBannerListener.onBannerCollapsed();
        }

        public void MMAdOverlayLaunched(MMAd mmAd) {
            Log.d("MoPub", "Millennial banner ad Launched.");
            MillennialBanner.this.mBannerListener.onBannerExpanded();
        }

        public void MMAdRequestIsCaching(MMAd mmAd) {
        }

        public void onSingleTap(MMAd mmAd) {
            MillennialBanner.this.mBannerListener.onBannerClicked();
        }

        public void requestCompleted(MMAd mmAd) {
            Log.d("MoPub", "Millennial banner ad loaded successfully. Showing ad...");
            MillennialBanner.this.mBannerListener.onBannerLoaded(MillennialBanner.this.mMillennialAdView);
        }

        public void requestFailed(MMAd mmAd, MMException e) {
            Log.d("MoPub", "Millennial banner ad failed to load.");
            MillennialBanner.this.mBannerListener.onBannerFailed(MoPubErrorCode.NETWORK_NO_FILL);
        }
    }

    MillennialBanner() {
    }

    private boolean extrasAreValid(Map<String, String> serverExtras) {
        try {
            Integer.parseInt((String) serverExtras.get(AD_WIDTH_KEY));
            Integer.parseInt((String) serverExtras.get(AD_HEIGHT_KEY));
            return serverExtras.containsKey(APID_KEY);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Deprecated
    MMAdView getMMAdView() {
        return this.mMillennialAdView;
    }

    protected void loadBanner(Context context, CustomEventBannerListener customEventBannerListener, Map<String, Object> localExtras, Map<String, String> serverExtras) {
        this.mBannerListener = customEventBannerListener;
        if (extrasAreValid(serverExtras)) {
            String apid = (String) serverExtras.get(APID_KEY);
            int width = Integer.parseInt((String) serverExtras.get(AD_WIDTH_KEY));
            int height = Integer.parseInt((String) serverExtras.get(AD_HEIGHT_KEY));
            MMSDK.initialize(context);
            this.mMillennialAdView = new MMAdView(context);
            this.mMillennialAdView.setListener(new MillennialBannerRequestListener());
            this.mMillennialAdView.setApid(apid);
            this.mMillennialAdView.setWidth(width);
            this.mMillennialAdView.setHeight(height);
            Location location = (Location) localExtras.get("location");
            if (location != null) {
                MMRequest.setUserLocation(location);
            }
            this.mMillennialAdView.setMMRequest(new MMRequest());
            this.mMillennialAdView.setId(MMSDK.getDefaultAdId());
            AdViewController.setShouldHonorServerDimensions(this.mMillennialAdView);
            this.mMillennialAdView.getAd();
        } else {
            this.mBannerListener.onBannerFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
        }
    }

    protected void onInvalidate() {
        if (this.mMillennialAdView != null) {
            this.mMillennialAdView.setListener(null);
        }
    }
}