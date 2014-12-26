package com.mopub.mobileads.util;

import android.util.Log;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class WebViews {
    private static final String LOGTAG = "MoPub - WebViewsUtil";

    public static void onPause(WebView webView) {
        try {
            WebView.class.getDeclaredMethod("onPause", new Class[0]).invoke(webView, new Object[0]);
        } catch (Exception e) {
        }
    }

    public static void onResume(WebView webView) {
        try {
            WebView.class.getDeclaredMethod("onResume", new Class[0]).invoke(webView, new Object[0]);
        } catch (Exception e) {
        }
    }

    public static void setDisableJSChromeClient(WebView webView) {
        webView.setWebChromeClient(new WebChromeClient() {
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Log.d(LOGTAG, message);
                return true;
            }

            public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
                Log.d(LOGTAG, message);
                return true;
            }

            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                Log.d(LOGTAG, message);
                return true;
            }

            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                Log.d(LOGTAG, message);
                return true;
            }
        });
    }
}