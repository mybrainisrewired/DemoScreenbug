package com.mopub.mobileads;

import android.content.Context;
import android.net.Uri;
import com.mopub.mobileads.CustomEventBanner.CustomEventBannerListener;
import com.mopub.mobileads.factories.HtmlBannerWebViewFactory;
import java.util.Map;

public class HtmlBanner extends CustomEventBanner {
    private HtmlBannerWebView mHtmlBannerWebView;

    private boolean extrasAreValid(Map<String, String> serverExtras) {
        return serverExtras.containsKey(AdFetcher.HTML_RESPONSE_BODY_KEY);
    }

    protected void loadBanner(Context context, CustomEventBannerListener customEventBannerListener, Map<String, Object> localExtras, Map<String, String> serverExtras) {
        if (extrasAreValid(serverExtras)) {
            String htmlData = Uri.decode((String) serverExtras.get(AdFetcher.HTML_RESPONSE_BODY_KEY));
            String redirectUrl = (String) serverExtras.get(AdFetcher.REDIRECT_URL_KEY);
            String clickthroughUrl = (String) serverExtras.get(AdFetcher.CLICKTHROUGH_URL_KEY);
            Boolean isScrollable = Boolean.valueOf((String) serverExtras.get(AdFetcher.SCROLLABLE_KEY));
            Context context2 = context;
            CustomEventBannerListener customEventBannerListener2 = customEventBannerListener;
            this.mHtmlBannerWebView = HtmlBannerWebViewFactory.create(context2, customEventBannerListener2, isScrollable.booleanValue(), redirectUrl, clickthroughUrl, AdConfiguration.extractFromMap(localExtras));
            AdViewController.setShouldHonorServerDimensions(this.mHtmlBannerWebView);
            this.mHtmlBannerWebView.loadHtmlResponse(htmlData);
        } else {
            customEventBannerListener.onBannerFailed(MoPubErrorCode.NETWORK_INVALID_STATE);
        }
    }

    protected void onInvalidate() {
        if (this.mHtmlBannerWebView != null) {
            this.mHtmlBannerWebView.destroy();
        }
    }
}