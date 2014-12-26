package com.mopub.mobileads;

import android.net.Uri;
import com.mopub.mobileads.CustomEventInterstitial.CustomEventInterstitialListener;
import java.util.Map;

class MraidInterstitial extends ResponseBodyInterstitial {
    private String mHtmlData;

    MraidInterstitial() {
    }

    protected void extractExtras(Map<String, String> serverExtras) {
        this.mHtmlData = Uri.decode((String) serverExtras.get(AdFetcher.HTML_RESPONSE_BODY_KEY));
    }

    protected void preRenderHtml(CustomEventInterstitialListener customEventInterstitialListener) {
        MraidActivity.preRenderHtml(this.mContext, customEventInterstitialListener, this.mHtmlData);
    }

    protected void showInterstitial() {
        MraidActivity.start(this.mContext, this.mHtmlData, this.mAdConfiguration);
    }
}