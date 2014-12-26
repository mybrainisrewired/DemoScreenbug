package com.millennialmedia.android;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

abstract class MMWebViewClient extends WebViewClient {
    private static final String TAG = "MMWebViewClient";
    private ExecutorService cachedExecutor;
    boolean isLastMMCommandResize;
    MMWebViewClientListener mmWebViewClientListener;
    RedirectionListenerImpl redirectListenerImpl;

    static abstract class MMWebViewClientListener {
        MMWebViewClientListener() {
        }

        void onPageFinished(String url) {
        }

        void onPageStarted(String url) {
        }
    }

    MMWebViewClient(MMWebViewClientListener mmWebViewClientListener, RedirectionListenerImpl redirListener) {
        this.cachedExecutor = Executors.newCachedThreadPool();
        this.mmWebViewClientListener = mmWebViewClientListener;
        this.redirectListenerImpl = redirListener;
    }

    public void onPageFinished(WebView webView, String url) {
        MMWebView mmWebView = (MMWebView) webView;
        if (!mmWebView.isOriginalUrl(url)) {
            this.mmWebViewClientListener.onPageFinished(url);
            mmWebView.setAdProperties();
            setMraidState(mmWebView);
            MMLog.d(TAG, "onPageFinished webview: " + mmWebView.toString() + "url is " + url);
        }
        super.onPageFinished(webView, url);
    }

    public void onPageStarted(WebView webView, String urlString, Bitmap favicon) {
        MMLog.d(TAG, String.format("onPageStarted: %s", new Object[]{urlString}));
        this.mmWebViewClientListener.onPageStarted(urlString);
        MMWebView mmWebView = (MMWebView) webView;
        mmWebView.mraidState = "loading";
        mmWebView.requiresPreAdSizeFix = false;
        super.onPageStarted(webView, urlString, favicon);
    }

    public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
        MMLog.e(TAG, String.format("Error: %s %s %s", new Object[]{Integer.valueOf(errorCode), description, failingUrl}));
    }

    abstract void setMraidState(MMWebView mMWebView);

    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        MMWebView mmWebView = (MMWebView) webView;
        if (mmWebView.isOriginalUrl(url)) {
            return true;
        }
        MMLog.v(TAG, "@@@@@@@@@@SHOULDOVERRIDELOADING@@@@@@@@@@@@@ Url is " + url + " for " + webView);
        if (url.substring(0, ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES).equalsIgnoreCase("mmsdk:")) {
            MMLog.v(TAG, "Running JS bridge command: " + url);
            MMCommand command = new MMCommand((MMWebView) webView, url);
            this.isLastMMCommandResize = command.isResizeCommand();
            this.cachedExecutor.execute(command);
            return true;
        } else if (this.redirectListenerImpl.isExpandingToUrl()) {
            return false;
        } else {
            this.redirectListenerImpl.url = url;
            this.redirectListenerImpl.weakContext = new WeakReference(webView.getContext());
            this.redirectListenerImpl.creatorAdImplInternalId = mmWebView.creatorAdImplId;
            HttpRedirection.startActivityFromUri(this.redirectListenerImpl);
            return true;
        }
    }
}