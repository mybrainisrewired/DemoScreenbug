package com.mopub.mobileads;

import android.content.Context;
import com.mopub.mobileads.CustomEventBanner.CustomEventBannerListener;

public class HtmlBannerWebView extends BaseHtmlWebView {
    public static final String EXTRA_AD_CLICK_DATA = "com.mopub.intent.extra.AD_CLICK_DATA";

    static class HtmlBannerWebViewListener implements HtmlWebViewListener {
        private final CustomEventBannerListener mCustomEventBannerListener;

        public HtmlBannerWebViewListener(CustomEventBannerListener customEventBannerListener) {
            this.mCustomEventBannerListener = customEventBannerListener;
        }

        public void onClicked() {
            this.mCustomEventBannerListener.onBannerClicked();
        }

        public void onCollapsed() {
            this.mCustomEventBannerListener.onBannerCollapsed();
        }

        public void onFailed(MoPubErrorCode errorCode) {
            this.mCustomEventBannerListener.onBannerFailed(errorCode);
        }

        public void onLoaded(BaseHtmlWebView htmlWebView) {
            this.mCustomEventBannerListener.onBannerLoaded(htmlWebView);
        }
    }

    public HtmlBannerWebView(Context context, AdConfiguration adConfiguration) {
        super(context, adConfiguration);
    }

    public void init(CustomEventBannerListener customEventBannerListener, boolean isScrollable, String redirectUrl, String clickthroughUrl) {
        super.init(isScrollable);
        setWebViewClient(new HtmlWebViewClient(new HtmlBannerWebViewListener(customEventBannerListener), this, clickthroughUrl, redirectUrl));
    }
}