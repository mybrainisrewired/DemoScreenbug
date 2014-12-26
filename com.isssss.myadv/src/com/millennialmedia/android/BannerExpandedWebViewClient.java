package com.millennialmedia.android;

class BannerExpandedWebViewClient extends MMWebViewClient {
    BannerExpandedWebViewClient(MMWebViewClientListener mmWebViewClientListener, RedirectionListenerImpl redirListener) {
        super(mmWebViewClientListener, redirListener);
    }

    void setMraidState(MMWebView mmWebView) {
        mmWebView.setMraidPlacementTypeInline();
        mmWebView.setMraidExpanded();
        mmWebView.setMraidReady();
    }
}