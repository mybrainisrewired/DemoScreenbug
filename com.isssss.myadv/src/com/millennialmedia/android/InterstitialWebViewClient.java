package com.millennialmedia.android;

class InterstitialWebViewClient extends MMWebViewClient {
    InterstitialWebViewClient(MMWebViewClientListener mmWebViewClientListener, RedirectionListenerImpl redirListener) {
        super(mmWebViewClientListener, redirListener);
    }

    void setMraidState(MMWebView mmWebView) {
        mmWebView.setMraidPlacementTypeInterstitial();
        mmWebView.setMraidDefault();
        mmWebView.setMraidReady();
    }
}