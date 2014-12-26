package com.facebook.ads.internal;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.lang.reflect.Constructor;

public class AdWebViewUtils {
    public static final long DEFAULT_IMPRESSION_DELAY_MS = 1000;
    public static final String FBAD_SCHEME = "fbad";
    public static final String WEBVIEW_BASE_URL = "https://www.facebook.com/";
    private static String browserUserAgentString;

    static {
        browserUserAgentString = null;
    }

    public static void config(WebView adWebView, WebViewClient adWebViewClient, AdWebViewInterface javascriptInterface) {
        adWebView.getSettings().setJavaScriptEnabled(true);
        adWebView.getSettings().setSupportZoom(false);
        adWebView.setHorizontalScrollBarEnabled(false);
        adWebView.setHorizontalScrollbarOverlay(false);
        adWebView.setVerticalScrollBarEnabled(false);
        adWebView.setVerticalScrollbarOverlay(false);
        adWebView.addJavascriptInterface(javascriptInterface, "AdControl");
        adWebView.setWebViewClient(adWebViewClient);
    }

    @TargetApi(17)
    private static String getDefaultUserAgentL17(Context context) {
        return WebSettings.getDefaultUserAgent(context);
    }

    public static String getUserAgentString(Context context, AdType adType) {
        if (AdType.NATIVE == adType) {
            return System.getProperty("http.agent");
        }
        if (browserUserAgentString == null) {
            if (VERSION.SDK_INT >= 17) {
                browserUserAgentString = getDefaultUserAgentL17(context);
            } else {
                try {
                    browserUserAgentString = getUserAgentStringByReflection(context, "android.webkit.WebSettings", "android.webkit.WebView");
                } catch (Exception e) {
                    try {
                        browserUserAgentString = getUserAgentStringByReflection(context, "android.webkit.WebSettingsClassic", "android.webkit.WebViewClassic");
                    } catch (Exception e2) {
                        WebView webView = new WebView(context.getApplicationContext());
                        browserUserAgentString = webView.getSettings().getUserAgentString();
                        webView.destroy();
                    }
                }
            }
        }
        return browserUserAgentString;
    }

    private static String getUserAgentStringByReflection(Context context, String webSettingsClassName, String webViewClassName) throws Exception {
        boolean z = false;
        Class<?> webSettingsClass = Class.forName(webSettingsClassName);
        Constructor<?> constructor = webSettingsClass.getDeclaredConstructor(new Class[]{Context.class, Class.forName(webViewClassName)});
        constructor.setAccessible(true);
        String str = (String) webSettingsClass.getMethod("getUserAgentString", new Class[z]).invoke(constructor.newInstance(new Object[]{context, null}), new Object[0]);
        constructor.setAccessible(z);
        return str;
    }
}