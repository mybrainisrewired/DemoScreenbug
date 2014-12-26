package com.mopub.mobileads.factories;

import android.content.Context;
import com.mopub.mobileads.AdConfiguration;
import com.mopub.mobileads.CustomEventInterstitial.CustomEventInterstitialListener;
import com.mopub.mobileads.HtmlInterstitialWebView;

public class HtmlInterstitialWebViewFactory {
    protected static HtmlInterstitialWebViewFactory instance;

    static {
        instance = new HtmlInterstitialWebViewFactory();
    }

    public static HtmlInterstitialWebView create(Context context, CustomEventInterstitialListener customEventInterstitialListener, boolean isScrollable, String redirectUrl, String clickthroughUrl, AdConfiguration adConfiguration) {
        return instance.internalCreate(context, customEventInterstitialListener, isScrollable, redirectUrl, clickthroughUrl, adConfiguration);
    }

    @Deprecated
    public static void setInstance(HtmlInterstitialWebViewFactory factory) {
        instance = factory;
    }

    public HtmlInterstitialWebView internalCreate(Context context, CustomEventInterstitialListener customEventInterstitialListener, boolean isScrollable, String redirectUrl, String clickthroughUrl, AdConfiguration adConfiguration) {
        HtmlInterstitialWebView htmlInterstitialWebView = new HtmlInterstitialWebView(context, adConfiguration);
        htmlInterstitialWebView.init(customEventInterstitialListener, isScrollable, redirectUrl, clickthroughUrl);
        return htmlInterstitialWebView;
    }
}