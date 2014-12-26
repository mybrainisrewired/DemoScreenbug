package com.loopme;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageButton;

public final class LoopMeAdListActivity extends Activity {
    private static final String EVENT_NAME = "eventName";
    private static final String FINISH_EVENT = "finish";
    private static final String JS_INTENT_ACTION = "com.loopme.jsevent";
    private static final String LAND = "landscape";
    private static final String LOG_TAG;
    private static final String PORT = "portrait";
    private static final String X_SECONDS_EVENT = "x_seconds";
    private static LoopMeAdListActivity mInstance;
    private boolean mIsShowFirstTime;
    private AdListLayout mLayout;
    private LoopMeInterstitial mLoopmeInterstitial;
    private JsEventReceiver mReceiver;
    private WebView mWebView;

    class AnonymousClass_1 implements Runnable {
        private final /* synthetic */ ImageButton val$btn;

        AnonymousClass_1(ImageButton imageButton) {
            this.val$btn = imageButton;
        }

        public void run() {
            this.val$btn.setVisibility(0);
            Utilities.log(LOG_TAG, "Close button did appear", LogLevel.DEBUG);
            LoopMeAdListActivity.this.setCloseButtonListener(LoopMeAdListActivity.this.mLayout.getCloseArea());
        }
    }

    private class JsEventReceiver extends BroadcastReceiver {
        private JsEventReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String eventName = intent.getStringExtra(EVENT_NAME);
            if (!action.equalsIgnoreCase(JS_INTENT_ACTION)) {
                return;
            }
            if (eventName.equalsIgnoreCase(FINISH_EVENT)) {
                Utilities.log(LOG_TAG, "javascript:sendLoopmeSdkEvent('finish')", LogLevel.DEBUG);
                LoopMeAdListActivity.this.mWebView.loadUrl("javascript:sendLoopmeSdkEvent('finish')");
            } else if (eventName.equalsIgnoreCase(X_SECONDS_EVENT)) {
                Utilities.log(LOG_TAG, "javascript:sendLoopmeSdkEvent('x_seconds')", LogLevel.DEBUG);
                LoopMeAdListActivity.this.mWebView.loadUrl("javascript:sendLoopmeSdkEvent('x_seconds')");
            }
        }
    }

    static {
        LOG_TAG = LoopMeAdListActivity.class.getSimpleName();
    }

    public LoopMeAdListActivity() {
        this.mIsShowFirstTime = true;
    }

    private void applyOrientationFromResponse() {
        String or = this.mLoopmeInterstitial.getAdParams().getAdOrientation();
        if (or != null) {
            if (or.equalsIgnoreCase(PORT)) {
                setRequestedOrientation(1);
            } else if (or.equalsIgnoreCase(LAND)) {
                setRequestedOrientation(0);
            }
        }
    }

    static void finishAdListActivity() {
        if (mInstance != null) {
            mInstance.finish();
        }
    }

    private View getCloseArea() {
        return this.mLayout.getCloseArea();
    }

    private void hideAds() {
        if (this.mLoopmeInterstitial != null) {
            this.mLoopmeInterstitial.onLoopMeInterstitialHide(this.mLoopmeInterstitial);
        }
    }

    private void initJsEventReceiver() {
        this.mReceiver = new JsEventReceiver(null);
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mReceiver, new IntentFilter(JS_INTENT_ACTION));
    }

    private void setCloseButtonDelay(int delay) {
        ImageButton btn = this.mLayout.getCloseButton();
        btn.postDelayed(new AnonymousClass_1(btn), (long) delay);
    }

    private void setCloseButtonListener(View button) {
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                LoopMeAdListActivity.this.finish();
            }
        });
    }

    private void webViewDidDisappear() {
        Utilities.log(LOG_TAG, "Call JavaScript webViewDidDisappearHelper()", LogLevel.DEBUG);
        this.mWebView.loadUrl("javascript:window.webViewDidDisappearHelper()");
        this.mWebView.clearCache(true);
        this.mWebView.clearHistory();
    }

    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mLoopmeInterstitial = (LoopMeInterstitial) BaseLoopMeHolder.get();
        requestWindowFeature(1);
        applyOrientationFromResponse();
        this.mLayout = new AdListLayout(this);
        this.mWebView = this.mLoopmeInterstitial.getWebView();
        this.mLayout.addView(this.mWebView, 0);
        setContentView(this.mLayout);
        Utilities.log(LOG_TAG, "Call JavaScript webviewDidAppearHelper()", LogLevel.DEBUG);
        this.mWebView.loadUrl("javascript:window.webviewDidAppearHelper()");
        setCloseButtonDelay(this.mLoopmeInterstitial.getCloseBtnDelay());
        initJsEventReceiver();
        mInstance = this;
    }

    protected void onDestroy() {
        if (VERSION.SDK_INT < 19) {
            webViewDidDisappear();
        }
        hideAds();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mReceiver);
        this.mLayout.removeAllViews();
        super.onDestroy();
    }

    protected void onPause() {
        if (VERSION.SDK_INT >= 19) {
            webViewDidDisappear();
        }
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
        if (this.mLoopmeInterstitial != null && this.mIsShowFirstTime) {
            this.mLoopmeInterstitial.onLoopMeInterstitialShow(this.mLoopmeInterstitial);
            this.mIsShowFirstTime = false;
        }
    }
}