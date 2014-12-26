package com.facebook.ads;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.facebook.ads.internal.AdHandler.ImpressionHelper;
import com.facebook.ads.internal.AdWebViewInterface;
import com.facebook.ads.internal.AdWebViewUtils;
import com.facebook.ads.internal.HtmlAdDataModel;
import com.facebook.ads.internal.HtmlAdHandler;
import com.facebook.ads.internal.StringUtils;
import com.facebook.ads.internal.action.AdAction;
import com.facebook.ads.internal.action.AdActionFactory;
import com.millennialmedia.android.MMAdView;

public class InterstitialAdActivity extends Activity {
    private static final int AD_WEBVIEW_ID = 100001;
    private static final String DATA_MODEL_KEY = "dataModel";
    private static final String LAST_REQUESTED_ORIENTATION_KEY = "lastRequestedOrientation";
    private static final int ORIENTATION_REVERSE_LANDSCAPE = 8;
    private static final int ORIENTATION_REVERSE_PORTRAIT = 9;
    private static final String TAG;
    private HtmlAdHandler adHandler;
    private WebView adWebView;
    private HtmlAdDataModel dataModel;
    private int displayHeight;
    private int displayWidth;
    private boolean isRestart;
    private int lastRequestedOrientation;
    private String placementId;

    private class AdWebViewClient extends WebViewClient {
        private static final String FBAD_CLOSE = "close";

        private AdWebViewClient() {
        }

        public void onLoadResource(WebView view, String url) {
            InterstitialAdActivity.this.adHandler.activateAd();
        }

        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            String urlPrefix = AdSettings.getUrlPrefix();
            if (StringUtils.isNullOrEmpty(urlPrefix) || !urlPrefix.endsWith(".sb")) {
                handler.cancel();
            } else {
                handler.proceed();
            }
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Uri uri = Uri.parse(url);
            if (AdWebViewUtils.FBAD_SCHEME.equals(uri.getScheme()) && FBAD_CLOSE.equals(uri.getAuthority())) {
                InterstitialAdActivity.this.finish();
            } else {
                InterstitialAdActivity.this.sendBroadcastForEvent(INTERSTITIAL_CLICKED);
                AdAction adAction = AdActionFactory.getAdAction(InterstitialAdActivity.this, uri);
                if (adAction != null) {
                    try {
                        adAction.execute(null);
                    } catch (Exception e) {
                        Log.e(TAG, "Error executing action", e);
                    }
                }
            }
            return true;
        }
    }

    static {
        TAG = InterstitialAdActivity.class.getSimpleName();
    }

    public InterstitialAdActivity() {
        this.isRestart = false;
    }

    private void loadAdFromIntentOrSavedState(Intent intent, Bundle savedInstanceState) {
        if (savedInstanceState == null || !savedInstanceState.containsKey(DATA_MODEL_KEY)) {
            this.displayWidth = intent.getIntExtra(DISPLAY_WIDTH_INTENT_EXTRA, 0);
            this.displayHeight = intent.getIntExtra(DISPLAY_HEIGHT_INTENT_EXTRA, 0);
            this.placementId = intent.getStringExtra(INTERSTITIAL_UNIQUE_ID_EXTRA);
            this.dataModel = HtmlAdDataModel.fromIntentExtra(intent);
            if (this.dataModel != null) {
                this.adHandler.setAdDataModel(this.dataModel);
                this.adWebView.loadDataWithBaseURL(AdWebViewUtils.WEBVIEW_BASE_URL, this.dataModel.getMarkup(), "text/html", "utf-8", null);
            }
        } else {
            this.dataModel = HtmlAdDataModel.fromBundle(savedInstanceState.getBundle(DATA_MODEL_KEY));
            if (this.dataModel != null) {
                this.adWebView.loadDataWithBaseURL(AdWebViewUtils.WEBVIEW_BASE_URL, this.dataModel.getMarkup(), "text/html", "utf-8", null);
            }
            this.lastRequestedOrientation = savedInstanceState.getInt(LAST_REQUESTED_ORIENTATION_KEY, -1);
            this.placementId = savedInstanceState.getString(INTERSTITIAL_UNIQUE_ID_EXTRA);
            this.isRestart = true;
        }
    }

    private void sendBroadcastForEvent(String event) {
        Intent intent = new Intent(event);
        intent.putExtra(INTERSTITIAL_UNIQUE_ID_EXTRA, this.placementId);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void setScreenOrientation(int displayWidth, int displayHeight) {
        boolean defaultInPortrait;
        if (displayHeight >= displayWidth) {
            defaultInPortrait = true;
        } else {
            defaultInPortrait = false;
        }
        int currentOrientation = ((WindowManager) getSystemService("window")).getDefaultDisplay().getRotation();
        if (defaultInPortrait) {
            switch (currentOrientation) {
                case MMAdView.TRANSITION_FADE:
                case MMAdView.TRANSITION_UP:
                    setRequestedOrientation(ORIENTATION_REVERSE_PORTRAIT);
                default:
                    setRequestedOrientation(1);
            }
        } else {
            switch (currentOrientation) {
                case MMAdView.TRANSITION_UP:
                case MMAdView.TRANSITION_DOWN:
                    setRequestedOrientation(ORIENTATION_REVERSE_LANDSCAPE);
                default:
                    setRequestedOrientation(0);
            }
        }
    }

    public void finish() {
        this.adHandler.cancelImpressionRetry();
        this.adWebView.loadUrl("about:blank");
        this.adWebView.clearCache(true);
        sendBroadcastForEvent(INTERSTITIAL_DISMISSED);
        super.finish();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT, AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
        RelativeLayout relativeLayout = new RelativeLayout(this);
        this.adWebView = new WebView(this);
        this.adWebView.setId(AD_WEBVIEW_ID);
        this.adWebView.setLayoutParams(new LayoutParams(-1, -1));
        AdWebViewUtils.config(this.adWebView, new AdWebViewClient(null), new AdWebViewInterface());
        relativeLayout.addView(this.adWebView);
        this.adHandler = new HtmlAdHandler(this.adWebView, new ImpressionHelper() {
            public void afterImpressionSent() {
            }

            public void onLoggingImpression() {
                InterstitialAdActivity.this.sendBroadcastForEvent(IMPRESSION_WILL_LOG);
            }

            public boolean shouldSendImpression() {
                return true;
            }
        }, 1000, this);
        setContentView(relativeLayout, new LayoutParams(-1, -1));
        loadAdFromIntentOrSavedState(getIntent(), savedInstanceState);
        sendBroadcastForEvent(INTERSTITIAL_DISPLAYED);
    }

    public void onRestart() {
        super.onRestart();
        this.isRestart = true;
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (this.dataModel != null) {
            savedInstanceState.putBundle(DATA_MODEL_KEY, this.dataModel.saveToBundle());
        }
        savedInstanceState.putInt(LAST_REQUESTED_ORIENTATION_KEY, this.lastRequestedOrientation);
        savedInstanceState.putString(INTERSTITIAL_UNIQUE_ID_EXTRA, this.placementId);
    }

    public void onStart() {
        super.onStart();
        if (!this.isRestart) {
            setScreenOrientation(this.displayWidth, this.displayHeight);
        } else if (this.lastRequestedOrientation >= 0) {
            setRequestedOrientation(this.lastRequestedOrientation);
            this.lastRequestedOrientation = -1;
        }
        this.isRestart = false;
    }

    public void setRequestedOrientation(int requestedOrientation) {
        this.lastRequestedOrientation = requestedOrientation;
        super.setRequestedOrientation(requestedOrientation);
    }
}