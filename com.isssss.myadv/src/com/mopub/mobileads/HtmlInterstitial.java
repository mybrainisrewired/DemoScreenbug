package com.mopub.mobileads;

import android.net.Uri;
import com.mopub.mobileads.CustomEventInterstitial.CustomEventInterstitialListener;
import java.util.Map;

public class HtmlInterstitial extends ResponseBodyInterstitial {
    private String mClickthroughUrl;
    private String mHtmlData;
    private boolean mIsScrollable;
    private String mRedirectUrl;

    protected void extractExtras(Map<String, String> serverExtras) {
        this.mHtmlData = Uri.decode((String) serverExtras.get(AdFetcher.HTML_RESPONSE_BODY_KEY));
        this.mIsScrollable = Boolean.valueOf((String) serverExtras.get(AdFetcher.SCROLLABLE_KEY)).booleanValue();
        this.mRedirectUrl = (String) serverExtras.get(AdFetcher.REDIRECT_URL_KEY);
        this.mClickthroughUrl = (String) serverExtras.get(AdFetcher.CLICKTHROUGH_URL_KEY);
    }

    protected void preRenderHtml(CustomEventInterstitialListener customEventInterstitialListener) {
        MoPubActivity.preRenderHtml(this.mContext, customEventInterstitialListener, this.mHtmlData);
    }

    protected void showInterstitial() {
        MoPubActivity.start(this.mContext, this.mHtmlData, this.mIsScrollable, this.mRedirectUrl, this.mClickthroughUrl, this.mAdConfiguration);
    }
}