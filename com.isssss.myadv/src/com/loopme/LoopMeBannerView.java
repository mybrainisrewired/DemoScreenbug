package com.loopme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.millennialmedia.android.MMAdView;

public class LoopMeBannerView extends RelativeLayout {
    private static final String LOG_TAG;
    private int mHeight;
    private WebView mWebView;
    private int mWidth;

    static {
        LOG_TAG = LoopMeBannerView.class.getSimpleName();
    }

    public LoopMeBannerView(Context context) {
        super(context);
        init(context);
    }

    public LoopMeBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(context, attrs);
        init(context);
    }

    public LoopMeBannerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        parseAttributes(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.mWebView = new WebView(context);
        if (this.mWidth == 0 || this.mHeight == 0) {
            Utilities.log(LOG_TAG, "Banner size is 0. Use setViewSize(width, height)", LogLevel.DEBUG);
        }
        this.mWebView.setLayoutParams(new LayoutParams(this.mWidth, this.mHeight));
        addView(this.mWebView);
        initWebView(this.mWebView);
    }

    @SuppressLint({"NewApi"})
    private void initWebView(WebView webview) {
        webview.setVerticalScrollBarEnabled(false);
        WebSettings webSettings = webview.getSettings();
        webSettings.setCacheMode(MMAdView.TRANSITION_UP);
        webSettings.setSupportZoom(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
        webSettings.setJavaScriptEnabled(true);
        if (VERSION.SDK_INT < 18) {
            webSettings.setPluginsEnabled(true);
            webSettings.setPluginState(PluginState.ON);
        }
        webview.setInitialScale(1);
        webview.setBackgroundColor(-7829368);
        webview.clearCache(true);
        webview.clearHistory();
        webview.setWebChromeClient(new WebChromeClient());
        if (VERSION.SDK_INT >= 11) {
            webview.setLayerType(1, null);
        }
    }

    private void parseAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, new int[]{16842960, 16842996, 16842997});
        int id = ta.getResourceId(0, -1);
        this.mWidth = ta.getDimensionPixelSize(1, -1);
        this.mHeight = ta.getDimensionPixelSize(MMAdView.TRANSITION_UP, -1);
        ta.recycle();
    }

    WebView getWebView() {
        return this.mWebView;
    }

    public void setViewSize(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
        this.mWebView.setLayoutParams(new LayoutParams(this.mWidth, this.mHeight));
    }

    void setWebViewClient(AdListWebViewClient client) {
        this.mWebView.setWebViewClient(client);
    }
}