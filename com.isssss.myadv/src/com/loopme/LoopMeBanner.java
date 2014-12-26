package com.loopme;

import android.app.Activity;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.CountDownTimer;
import android.os.Handler;
import android.webkit.WebView;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;

public class LoopMeBanner extends BaseLoopMe implements LoopMeBannerListener {
    private static final String LOG_TAG;
    private LoopMeBannerView mBannerView;
    private LoopMeBannerListener mListener;
    private volatile RefreshBannerTimer mRefreshTimer;

    class AnonymousClass_1 implements Runnable {
        private final /* synthetic */ boolean val$silentMode;

        AnonymousClass_1(boolean z) {
            this.val$silentMode = z;
        }

        public void run() {
            if (!LoopMeBanner.this.mInit) {
                LoopMeBanner.this.mBannerView.getWebView().stopLoading();
                if (this.val$silentMode) {
                    Utilities.log(LOG_TAG, "Timeout during loading", LogLevel.ERROR);
                } else {
                    LoopMeBanner.this.onLoopMeBannerLoadFail(LoopMeBanner.this, new LoopMeError("Timeout during loading"));
                }
            }
        }
    }

    class AnonymousClass_2 implements Runnable {
        private final /* synthetic */ LoopMeError val$error;

        AnonymousClass_2(LoopMeError loopMeError) {
            this.val$error = loopMeError;
        }

        public void run() {
            LoopMeBanner.this.mListener.onLoopMeBannerLoadFail(LoopMeBanner.this, this.val$error);
        }
    }

    private class RefreshBannerTimer extends CountDownTimer {
        private volatile int counter;

        public RefreshBannerTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            this.counter = 1;
            Utilities.log(LOG_TAG, new StringBuilder("Schedule refresh ad every ").append(countDownInterval).append("ms").toString(), LogLevel.DEBUG);
        }

        public void onFinish() {
        }

        public void onTick(long millisUntilFinished) {
            if (this.counter == 1) {
                this.counter++;
            } else {
                LoopMeBanner.this.callWebViewDisappearHelper();
                Utilities.log(LOG_TAG, "Refresh banner", LogLevel.DEBUG);
                LoopMeBanner.this.refreshBanner();
            }
        }
    }

    static {
        LOG_TAG = LoopMeBanner.class.getSimpleName();
    }

    public LoopMeBanner(Activity activity, String appKey, LoopMeBannerView bannerview) {
        Utilities.log(LOG_TAG, new StringBuilder("Start creating banner with app key: ").append(appKey).toString(), LogLevel.INFO);
        if (activity == null || bannerview == null || appKey == null || appKey.length() == 0) {
            throw new IllegalArgumentException();
        }
        LoopMe.getInstance(activity);
        this.mActivity = activity;
        this.mAppKey = appKey;
        this.mBannerView = bannerview;
    }

    private WebView getWebView() {
        return this.mBannerView.getWebView();
    }

    private void refreshBanner() {
        if (Utilities.isOnline(this.mActivity)) {
            fetchAd(true);
        }
    }

    private void startRefreshTimer() {
        if (getAdParams() != null) {
            this.mRefreshTimer = new RefreshBannerTimer(Long.MAX_VALUE, (long) getAdParams().getAdRefreshTime());
            this.mRefreshTimer.start();
        }
    }

    private void stopRefreshTimer() {
        if (this.mRefreshTimer != null) {
            Utilities.log(LOG_TAG, "Stop schedule refresh ad", LogLevel.DEBUG);
            this.mRefreshTimer.cancel();
            this.mRefreshTimer = null;
        }
    }

    public void addListener(LoopMeBannerListener listener) {
        if (listener != null) {
            this.mListener = listener;
        }
    }

    void callWebViewAppearHelper() {
        Utilities.log(LOG_TAG, "Call JavaScript webviewDidAppearHelper()", LogLevel.DEBUG);
        this.mBannerView.getWebView().loadUrl("javascript:window.webviewDidAppearHelper()");
    }

    void callWebViewDisappearHelper() {
        Utilities.log(LOG_TAG, "Call JavaScript webViewDidDisappearHelper()", LogLevel.DEBUG);
        this.mBannerView.getWebView().loadUrl("javascript:window.webViewDidDisappearHelper()");
    }

    void fetchAd(boolean silentMode) {
        this.mBannerView.setWebViewClient(new AdListWebViewClient(this, silentMode));
        BaseLoopMeHolder.put(this);
        String requestUrl = new AdRequest(this.mActivity).getRequestUrl(this.mAppKey);
        this.mInit = false;
        new Handler().postDelayed(new AnonymousClass_1(silentMode), 10000);
        new AdFetcher(this.mBannerView.getWebView()).execute(new String[]{requestUrl});
    }

    AdFormat getAdFormat() {
        return AdFormat.BANNER;
    }

    public /* bridge */ /* synthetic */ String getAppKey() {
        return super.getAppKey();
    }

    public /* bridge */ /* synthetic */ boolean getTestMode() {
        return super.getTestMode();
    }

    public void hide() {
        Utilities.log(LOG_TAG, "Ad will disappear from screen", LogLevel.DEBUG);
        stopRefreshTimer();
        if (this.mBannerView.getVisibility() == 0) {
            this.mBannerView.setVisibility(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES);
            callWebViewDisappearHelper();
            onLoopMeBannerHide(this);
        }
    }

    void loadComplete() {
        Utilities.log(LOG_TAG, "Banner successfully loaded", LogLevel.INFO);
        this.mIsReady = true;
        this.mInit = true;
        showBanner();
    }

    public void onLoopMeBannerClicked(LoopMeBanner banner) {
        Utilities.log(LOG_TAG, "Ad received tap event", LogLevel.INFO);
        if (this.mListener != null) {
            this.mListener.onLoopMeBannerClicked(this);
        }
    }

    public void onLoopMeBannerHide(LoopMeBanner banner) {
        Utilities.log(LOG_TAG, "Ad disappeared from screen", LogLevel.INFO);
        this.mIsReady = false;
        if (this.mListener != null) {
            this.mListener.onLoopMeBannerHide(this);
        }
    }

    public void onLoopMeBannerLeaveApp(LoopMeBanner banner) {
        Utilities.log(LOG_TAG, "Leaving application", LogLevel.INFO);
        if (this.mListener != null) {
            this.mListener.onLoopMeBannerLeaveApp(this);
        }
    }

    public void onLoopMeBannerLoadFail(LoopMeBanner banner, LoopMeError error) {
        Utilities.log(LOG_TAG, new StringBuilder("Ad fails to load: ").append(error.getMessage()).toString(), LogLevel.INFO);
        this.mIsReady = false;
        this.mInit = true;
        if (this.mListener != null) {
            this.mActivity.runOnUiThread(new AnonymousClass_2(error));
        }
    }

    public void onLoopMeBannerShow(LoopMeBanner banner) {
        Utilities.log(LOG_TAG, "Ad appeared on screen", LogLevel.INFO);
        if (this.mListener != null) {
            this.mListener.onLoopMeBannerShow(this);
        }
    }

    public void removeListener() {
        this.mListener = null;
    }

    public /* bridge */ /* synthetic */ void setTestMode(boolean z) {
        super.setTestMode(z);
    }

    public synchronized void show() {
        Utilities.log(LOG_TAG, "Banner did start loading ad", LogLevel.INFO);
        if (this.mIsReady) {
            Utilities.log(LOG_TAG, "Banner already loaded", LogLevel.INFO);
            showBanner();
        } else if (VERSION.SDK_INT < 14) {
            onLoopMeBannerLoadFail(this, new LoopMeError("Not supported Android version. Expected Android 4.0+"));
        } else if (!Utilities.isOnline(this.mActivity)) {
            onLoopMeBannerLoadFail(this, new LoopMeError("No internet connection"));
        } else if (LoopMe.getInstance(this.mActivity).isInitComplete()) {
            fetchAd(false);
        } else {
            BaseLoopMeHolder.put(this);
            Utilities.log(LOG_TAG, "Start initialization adv id", LogLevel.DEBUG);
            new AdvertisingIdTask(false).execute(new Context[]{this.mActivity});
        }
    }

    void showBanner() {
        Utilities.log(LOG_TAG, "Banner did start showing ad", LogLevel.INFO);
        if (this.mBannerView.isShown()) {
            Utilities.log(LOG_TAG, "Banner already shown", LogLevel.INFO);
        } else if (this.mIsReady) {
            this.mBannerView.setVisibility(0);
            onLoopMeBannerShow(this);
            callWebViewAppearHelper();
            startRefreshTimer();
        }
    }
}