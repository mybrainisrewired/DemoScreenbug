package com.loopme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import com.google.android.gms.drive.DriveFile;

public final class LoopMeInterstitial extends BaseLoopMe implements LoopMeInterstitialListener {
    private static final String LOG_TAG;
    private static final int MAX_CLOSE_BTN_DELAY = 60000;
    private static final int MIN_CLOSE_BTN_DELAY = 0;
    private AdListWebViewClient mAdListWebViewClient;
    private volatile int mCloseBtnDelay;
    private ExpirationTimer mExpirationTimer;
    private String mJsEventName;
    private String mJsTime;
    private LoopMeInterstitialListener mListener;
    private Handler mTimeoutHandler;
    private WebView mWebView;

    class AnonymousClass_1 implements Runnable {
        private final /* synthetic */ AdFetcher val$fetcher;

        AnonymousClass_1(AdFetcher adFetcher) {
            this.val$fetcher = adFetcher;
        }

        public void run() {
            if (!LoopMeInterstitial.this.mInit) {
                LoopMeInterstitial.this.mWebView.stopLoading();
                this.val$fetcher.cancel(true);
                LoopMeInterstitial.this.onLoopMeInterstitialLoadFail(LoopMeInterstitial.this, new LoopMeError("Timeout during loading"));
            }
        }
    }

    class AnonymousClass_3 implements Runnable {
        private final /* synthetic */ LoopMeError val$error;

        AnonymousClass_3(LoopMeError loopMeError) {
            this.val$error = loopMeError;
        }

        public void run() {
            LoopMeInterstitial.this.mListener.onLoopMeInterstitialLoadFail(LoopMeInterstitial.this, this.val$error);
        }
    }

    private class AdListWebChromeClient extends WebChromeClient {
        private AdListWebChromeClient() {
        }

        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            Utilities.log(LOG_TAG, new StringBuilder("Console: ").append(consoleMessage.message()).append(" line: ").append(consoleMessage.lineNumber()).append(" source: ").append(consoleMessage.sourceId()).toString(), LogLevel.ERROR);
            return super.onConsoleMessage(consoleMessage);
        }
    }

    private class AndroidFunction {
        private AndroidFunction() {
        }

        @JavascriptInterface
        public void getDelayValue(String str) {
            try {
                LoopMeInterstitial.this.mCloseBtnDelay = Integer.parseInt(str);
                if (LoopMeInterstitial.this.mCloseBtnDelay > 60000) {
                    LoopMeInterstitial.this.mCloseBtnDelay = MAX_CLOSE_BTN_DELAY;
                } else if (LoopMeInterstitial.this.mCloseBtnDelay < 0) {
                    LoopMeInterstitial.this.mCloseBtnDelay = 0;
                } else {
                    Utilities.log(LOG_TAG, new StringBuilder("Detected close button delay ").append(LoopMeInterstitial.this.mCloseBtnDelay).append("ms").toString(), LogLevel.DEBUG);
                }
            } catch (NumberFormatException e) {
                Utilities.log(LOG_TAG, "Close button delay absent", LogLevel.ERROR);
            }
        }

        @JavascriptInterface
        public void jsEvent(String eventName, String time) {
            Utilities.log(LOG_TAG, new StringBuilder("Received event from js (").append(eventName).append(", ").append(time).append(")").toString(), LogLevel.DEBUG);
            LoopMeInterstitial.this.setJsEvent(eventName, time);
        }

        @JavascriptInterface
        public void playVideo(String url) {
            Utilities.log(LOG_TAG, new StringBuilder("Start play video from url:").append(url).toString(), LogLevel.DEBUG);
            LoopMeInterstitial.this.switchToVideo(url);
        }
    }

    private class ExpirationTimer extends CountDownTimer {
        public ExpirationTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            Utilities.log(LOG_TAG, "Start schedule expiration", LogLevel.DEBUG);
        }

        public void onFinish() {
            LoopMeInterstitial.this.mIsReady = false;
            LoopMeInterstitial.this.onLoopMeInterstitialExpired(LoopMeInterstitial.this);
        }

        public void onTick(long millisUntilFinished) {
        }
    }

    static {
        LOG_TAG = LoopMeInterstitial.class.getSimpleName();
    }

    public LoopMeInterstitial(Activity activity, String appKey) {
        this.mTimeoutHandler = new Handler();
        this.mCloseBtnDelay = 5000;
        Utilities.log(LOG_TAG, new StringBuilder("Start creating interstitial with app key: ").append(appKey).toString(), LogLevel.INFO);
        if (activity == null || appKey == null || appKey.length() == 0) {
            throw new IllegalArgumentException("Wrong parameters");
        }
        LoopMe.getInstance(activity);
        this.mActivity = activity;
        this.mAppKey = appKey;
        initWebView();
    }

    private void initWebView() {
        if (this.mWebView == null) {
            this.mWebView = new WebView(this.mActivity);
            this.mWebView.setVerticalScrollBarEnabled(false);
            WebSettings webSettings = this.mWebView.getSettings();
            webSettings.setSupportZoom(false);
            webSettings.setPluginState(PluginState.ON);
            if (VERSION.SDK_INT < 18) {
                webSettings.setPluginsEnabled(true);
            }
            this.mWebView.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
            this.mAdListWebViewClient = new AdListWebViewClient(this, false);
            webSettings.setJavaScriptEnabled(true);
            this.mWebView.addJavascriptInterface(new AndroidFunction(null), "android");
            this.mWebView.setWebChromeClient(new AdListWebChromeClient(null));
            this.mWebView.setWebViewClient(this.mAdListWebViewClient);
        }
    }

    private void setJsEvent(String eventName, String time) {
        this.mJsEventName = eventName;
        this.mJsTime = time;
    }

    private void startExpirationTimer() {
        if (this.mExpirationTimer == null && this.mAdParams != null) {
            this.mExpirationTimer = new ExpirationTimer((long) this.mAdParams.getExpiredTime(), 60000);
            this.mExpirationTimer.start();
        }
    }

    private void stopExpirationTimer() {
        if (this.mExpirationTimer != null) {
            Utilities.log(LOG_TAG, "Stop schedule expiration", LogLevel.DEBUG);
            this.mExpirationTimer.cancel();
            this.mExpirationTimer = null;
        }
    }

    private void switchToVideo(String videoUrl) {
        if (VERSION.SDK_INT <= 18) {
            BaseLoopMeHolder.put(this);
            Intent intent = new Intent(getActivity(), LoopMeVideoViewActivity.class);
            intent.putExtra("videourl", videoUrl);
            intent.addFlags(DriveFile.MODE_READ_ONLY);
            getActivity().startActivity(intent);
        }
    }

    public void addListener(LoopMeInterstitialListener listener) {
        if (listener != null) {
            this.mListener = listener;
        }
    }

    public void destroy() {
        removeListener();
        stopExpirationTimer();
        this.mTimeoutHandler.removeCallbacksAndMessages(null);
    }

    void fetchAd(boolean silentMode) {
        this.mTimeoutHandler.removeCallbacksAndMessages(null);
        this.mInit = false;
        BaseLoopMeHolder.put(this);
        String requestUrl = new AdRequest(this.mActivity).getRequestUrl(this.mAppKey);
        AdFetcher fetcher = new AdFetcher(this.mWebView);
        fetcher.execute(new String[]{requestUrl});
        this.mTimeoutHandler.postDelayed(new AnonymousClass_1(fetcher), 10000);
    }

    AdFormat getAdFormat() {
        return AdFormat.INTERSTITIAL;
    }

    public String getAppKey() {
        return this.mAppKey;
    }

    int getCloseBtnDelay() {
        return this.mCloseBtnDelay;
    }

    String getJsEventName() {
        return this.mJsEventName;
    }

    String getJsTime() {
        return this.mJsTime;
    }

    public /* bridge */ /* synthetic */ boolean getTestMode() {
        return super.getTestMode();
    }

    WebView getWebView() {
        return this.mWebView;
    }

    public boolean isReady() {
        return this.mIsReady;
    }

    public synchronized boolean load() {
        boolean z = true;
        synchronized (this) {
            Utilities.log(LOG_TAG, "Start loading interstitial", LogLevel.INFO);
            if (isReady()) {
                Utilities.log(LOG_TAG, "Interstitial already loaded", LogLevel.INFO);
                onLoopMeInterstitialLoadSuccess(this);
            } else if (VERSION.SDK_INT < 14) {
                onLoopMeInterstitialLoadFail(this, new LoopMeError("Not supported Android version. Expected Android 4.0+"));
                z = false;
            } else if (!Utilities.isOnline(this.mActivity)) {
                onLoopMeInterstitialLoadFail(this, new LoopMeError("No internet connection"));
                z = false;
            } else if (LoopMe.getInstance(this.mActivity).isInitComplete()) {
                fetchAd(false);
            } else {
                BaseLoopMeHolder.put(this);
                Utilities.log(LOG_TAG, "Start initialization google adv id", LogLevel.DEBUG);
                new AdvertisingIdTask(false).execute(new Context[]{this.mActivity});
            }
        }
        return z;
    }

    public void onLoopMeExitNoClicked(LoopMeInterstitial interstitial) {
        Utilities.log(LOG_TAG, "No button on exit popup was clicked", LogLevel.INFO);
        if (this.mListener != null) {
            this.mListener.onLoopMeExitNoClicked(this);
        }
    }

    public void onLoopMeExitYesClicked(LoopMeInterstitial interstitial) {
        Utilities.log(LOG_TAG, "Yes button on exit popup was clicked", LogLevel.INFO);
        if (this.mListener != null) {
            this.mListener.onLoopMeExitYesClicked(this);
        }
    }

    public void onLoopMeInterstitialClicked(LoopMeInterstitial interstitial) {
        Utilities.log(LOG_TAG, "Ad received tap event", LogLevel.INFO);
        if (this.mListener != null) {
            this.mListener.onLoopMeInterstitialClicked(this);
        }
    }

    public void onLoopMeInterstitialExpired(LoopMeInterstitial interstitial) {
        this.mExpirationTimer = null;
        if (this.mListener != null) {
            Utilities.log(LOG_TAG, "Ads content expired", LogLevel.INFO);
            this.mListener.onLoopMeInterstitialExpired(this);
        }
    }

    public void onLoopMeInterstitialHide(LoopMeInterstitial interstitial) {
        this.mIsReady = false;
        Utilities.log(LOG_TAG, "Ad disappeared from screen", LogLevel.INFO);
        if (this.mListener != null) {
            this.mListener.onLoopMeInterstitialHide(this);
        }
    }

    public void onLoopMeInterstitialLeaveApp(LoopMeInterstitial interstitial) {
        Utilities.log(LOG_TAG, "Leaving application", LogLevel.INFO);
        if (this.mListener != null) {
            this.mListener.onLoopMeInterstitialLeaveApp(this);
        }
    }

    public void onLoopMeInterstitialLoadFail(LoopMeInterstitial interstitial, LoopMeError error) {
        Utilities.log(LOG_TAG, new StringBuilder("Ad fails to load: ").append(error.getMessage()).toString(), LogLevel.INFO);
        this.mIsReady = false;
        this.mInit = true;
        if (this.mListener != null) {
            this.mActivity.runOnUiThread(new AnonymousClass_3(error));
        }
    }

    public void onLoopMeInterstitialLoadSuccess(LoopMeInterstitial interstitial) {
        startExpirationTimer();
        Utilities.log(LOG_TAG, "Ad successfully loaded", LogLevel.INFO);
        this.mIsReady = true;
        this.mInit = true;
        if (this.mListener != null) {
            this.mActivity.runOnUiThread(new Runnable() {
                public void run() {
                    LoopMeInterstitial.this.mListener.onLoopMeInterstitialLoadSuccess(LoopMeInterstitial.this);
                }
            });
        }
    }

    public void onLoopMeInterstitialShow(LoopMeInterstitial interstitial) {
        Utilities.log(LOG_TAG, "Ad appeared on screen", LogLevel.INFO);
        if (this.mListener != null) {
            this.mListener.onLoopMeInterstitialShow(this);
        }
    }

    public void removeListener() {
        this.mListener = null;
    }

    void setReadyStatus(boolean status) {
        this.mIsReady = status;
    }

    public /* bridge */ /* synthetic */ void setTestMode(boolean z) {
        super.setTestMode(z);
    }

    public boolean show() {
        Utilities.log(LOG_TAG, "Interstitial will present fullscreen ad", LogLevel.INFO);
        if (isReady()) {
            stopExpirationTimer();
            return true;
        } else {
            Utilities.log(LOG_TAG, "Interstitial is not ready", LogLevel.INFO);
            return false;
        }
    }
}