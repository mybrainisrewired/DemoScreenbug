package com.loopme;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import com.google.android.gms.plus.PlusShare;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.Preconditions;

public final class LoopMeAdDetailActivity extends Activity {
    private static final String LOG_TAG;
    private WebView mAdDetailWebview;
    private BaseLoopMe mBaseLoopMe;
    private boolean mIsActivityRecreated;
    private AdDetailLayout mLayout;

    class AnonymousClass_1 implements OnClickListener {
        private final /* synthetic */ WebView val$webView;

        AnonymousClass_1(WebView webView) {
            this.val$webView = webView;
        }

        public void onClick(View v) {
            if (this.val$webView.canGoBack()) {
                LoopMeAdDetailActivity.this.mLayout.getProgressBar().setVisibility(0);
                this.val$webView.goBack();
            }
        }
    }

    class AnonymousClass_3 implements OnClickListener {
        private final /* synthetic */ WebView val$webView;

        AnonymousClass_3(WebView webView) {
            this.val$webView = webView;
        }

        public void onClick(View v) {
            LoopMeAdDetailActivity.this.mLayout.getProgressBar().setVisibility(0);
            this.val$webView.reload();
        }
    }

    class AnonymousClass_4 implements OnClickListener {
        private final /* synthetic */ WebView val$webView;

        AnonymousClass_4(WebView webView) {
            this.val$webView = webView;
        }

        public void onClick(View v) {
            LoopMeAdDetailActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.val$webView.getUrl())));
            if (LoopMeAdDetailActivity.this.mBaseLoopMe.getAdFormat() == AdFormat.BANNER) {
                LoopMeBanner banner = (LoopMeBanner) LoopMeAdDetailActivity.this.mBaseLoopMe;
                banner.onLoopMeBannerLeaveApp(banner);
            } else {
                LoopMeInterstitial interstitial = (LoopMeInterstitial) LoopMeAdDetailActivity.this.mBaseLoopMe;
                interstitial.onLoopMeInterstitialLeaveApp(interstitial);
            }
        }
    }

    static {
        LOG_TAG = LoopMeAdDetailActivity.class.getSimpleName();
    }

    public LoopMeAdDetailActivity() {
        this.mIsActivityRecreated = true;
    }

    private AdDetailLayout getLayout() {
        return this.mLayout;
    }

    private void initButtonListeners(WebView webView) {
        this.mLayout.getBackButton().setOnClickListener(new AnonymousClass_1(webView));
        this.mLayout.getCloseButton().setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                LoopMeAdListActivity.finishAdListActivity();
                LoopMeAdDetailActivity.this.finish();
            }
        });
        this.mLayout.getRefreshButton().setOnClickListener(new AnonymousClass_3(webView));
        this.mLayout.getNativeButton().setOnClickListener(new AnonymousClass_4(webView));
    }

    private void initWebView(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setPluginState(PluginState.ON);
        if (VERSION.SDK_INT < 18) {
            webSettings.setPluginsEnabled(true);
        }
        webSettings.setBuiltInZoomControls(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webView.setInitialScale(1);
        webView.setWebViewClient(new AdDetailWebViewClient(this.mBaseLoopMe, this.mLayout.getProgressBar(), this.mLayout.getBackButton()));
    }

    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setSoftInputMode(ApiEventType.API_MRAID_PLAY_AUDIO);
        this.mBaseLoopMe = BaseLoopMeHolder.get();
        String str = Preconditions.EMPTY_ARGUMENTS;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            str = extras.getString(PlusShare.KEY_CALL_TO_ACTION_URL);
        }
        requestWindowFeature(1);
        this.mLayout = new AdDetailLayout(this);
        setContentView(this.mLayout);
        this.mAdDetailWebview = this.mLayout.getWebView();
        initWebView(this.mAdDetailWebview);
        if (bundle != null) {
            this.mAdDetailWebview.restoreState(bundle);
        } else {
            this.mAdDetailWebview.loadUrl(str);
        }
        initButtonListeners(this.mAdDetailWebview);
    }

    public final boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return super.onKeyDown(keyCode, event);
        }
        if (this.mAdDetailWebview.canGoBack()) {
            this.mAdDetailWebview.goBack();
            return false;
        } else {
            finish();
            return true;
        }
    }

    protected final void onPause() {
        super.onPause();
        if (VERSION.SDK_INT >= 11) {
            this.mAdDetailWebview.onPause();
        }
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Utilities.log(LOG_TAG, "onRestoreInstanceState", LogLevel.DEBUG);
        this.mIsActivityRecreated = true;
        super.onRestoreInstanceState(savedInstanceState);
    }

    protected void onResume() {
        super.onResume();
        if (!this.mIsActivityRecreated) {
            finish();
        }
        this.mIsActivityRecreated = false;
        this.mLayout.getProgressBar().setVisibility(MMAdView.TRANSITION_RANDOM);
    }

    protected void onSaveInstanceState(Bundle outState) {
        this.mAdDetailWebview.saveState(outState);
        super.onSaveInstanceState(outState);
    }
}