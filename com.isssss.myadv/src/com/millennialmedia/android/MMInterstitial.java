package com.millennialmedia.android;

import android.content.Context;
import com.google.android.gms.location.LocationRequest;

public final class MMInterstitial implements MMAd {
    private static final String TAG = "MMInterstitial";
    MMAdImpl adImpl;
    int externalId;

    class MMInterstitialAdImpl extends MMAdImpl {
        public MMInterstitialAdImpl(Context context) {
            super(context);
        }

        MMInterstitial getCallingAd() {
            return MMInterstitial.this;
        }
    }

    public MMInterstitial(Context context) {
        this.adImpl = new MMInterstitialAdImpl(context.getApplicationContext());
        this.adImpl.adType = "i";
    }

    private void fetchInternal() {
        if (isAdAvailable()) {
            MMLog.d(TAG, "Ad already fetched and ready for display...");
            Event.requestFailed(this.adImpl, new MMException(17));
        } else {
            MMLog.d(TAG, "Fetching new ad...");
            this.adImpl.requestAd();
        }
    }

    public boolean display() {
        return display(false);
    }

    public boolean display(boolean throwError) {
        if (MMSDK.isUiThread()) {
            try {
                int error = displayInternal();
                if (error == 0 || !throwError) {
                    return error == 0;
                } else {
                    throw new MMException(error);
                }
            } catch (Exception e) {
                Exception e2 = e;
                if (!throwError) {
                    return false;
                }
                throw new MMException(e2);
            }
        } else {
            MMLog.e(TAG, MMException.getErrorCodeMessage(MMAdView.TRANSITION_DOWN));
            return false;
        }
    }

    int displayInternal() {
        try {
            MMAdImplController.assignAdViewController(this.adImpl);
            if (this.adImpl.controller != null) {
                return this.adImpl.controller.display(this.adImpl);
            }
        } catch (Exception e) {
            Exception e2 = e;
            MMLog.e(TAG, "There was an exception displaying a cached ad. ", e2);
            e2.printStackTrace();
        }
        return LocationRequest.PRIORITY_HIGH_ACCURACY;
    }

    public void fetch() {
        if (this.adImpl == null || this.adImpl.requestListener == null) {
            fetchInternal();
        } else {
            fetch(this.adImpl.mmRequest, this.adImpl.requestListener);
        }
    }

    public void fetch(MMRequest request) {
        if (this.adImpl == null || this.adImpl.requestListener == null) {
            fetchInternal();
        } else {
            fetch(request, this.adImpl.requestListener);
        }
    }

    public void fetch(MMRequest request, RequestListener listener) {
        if (this.adImpl != null) {
            this.adImpl.mmRequest = request;
            this.adImpl.requestListener = listener;
        }
        fetchInternal();
    }

    public String getApid() {
        return this.adImpl.getApid();
    }

    public boolean getIgnoresDensityScaling() {
        return this.adImpl.getIgnoresDensityScaling();
    }

    public RequestListener getListener() {
        return this.adImpl.getListener();
    }

    public MMRequest getMMRequest() {
        return this.adImpl.getMMRequest();
    }

    public boolean isAdAvailable() {
        if (MMSDK.isUiThread()) {
            try {
                MMAdImplController.assignAdViewController(this.adImpl);
                return this.adImpl.controller != null && this.adImpl.controller.isAdAvailable(this.adImpl) == 0;
            } catch (Exception e) {
                Exception e2 = e;
                MMLog.e(TAG, "There was an exception checking for a cached ad. ", e2);
                e2.printStackTrace();
                return false;
            }
        } else {
            MMLog.e(TAG, MMException.getErrorCodeMessage(MMAdView.TRANSITION_DOWN));
            return false;
        }
    }

    public void setApid(String apid) {
        this.adImpl.setApid(apid);
    }

    public void setIgnoresDensityScaling(boolean ignoresDensityScaling) {
        this.adImpl.setIgnoresDensityScaling(ignoresDensityScaling);
    }

    public void setListener(RequestListener listener) {
        this.adImpl.setListener(listener);
    }

    public void setMMRequest(MMRequest request) {
        this.adImpl.setMMRequest(request);
    }
}