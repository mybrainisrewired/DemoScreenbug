package com.millennialmedia.android;

class BannerWebViewClient extends MMWebViewClient {
    BannerWebViewClient(MMWebViewClientListener mmWebViewClientListener, RedirectionListenerImpl redirListener) {
        super(mmWebViewClientListener, redirListener);
    }

    void setMraidState(MMWebView mmWebView) {
        mmWebView.setMraidPlacementTypeInline();
        mmWebView.setMraidDefault();
        mmWebView.setMraidReady();
    }
}