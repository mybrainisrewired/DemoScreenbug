package com.loopme;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.plus.PlusShare;
import com.mopub.common.Preconditions;

class AdListWebViewClient extends WebViewClient {
    private static final String LOG_TAG;
    private static final String LOOPME_CLOSE = "loopme://close";
    private static final String LOOPME_FAIL = "loopme://failLoad";
    private static final String LOOPME_SUCCESS = "loopme://finishLoad?totalCampaigns=";
    private final BaseLoopMe mBaseLoopMe;
    private final boolean mSilentMode;

    static {
        LOG_TAG = AdListWebViewClient.class.getSimpleName();
    }

    public AdListWebViewClient(BaseLoopMe baseLoopme, boolean silentMode) {
        this.mBaseLoopMe = baseLoopme;
        this.mSilentMode = silentMode;
        if (baseLoopme == null) {
            throw new IllegalArgumentException("Wrong parameter");
        }
    }

    private void loadFail(LoopMeError error) {
        if (this.mSilentMode) {
            this.mBaseLoopMe.setInitStatus(false);
        } else if (this.mBaseLoopMe.getAdFormat() == AdFormat.BANNER) {
            LoopMeBanner banner = (LoopMeBanner) this.mBaseLoopMe;
            banner.onLoopMeBannerLoadFail(banner, error);
        } else {
            LoopMeInterstitial interstitial = (LoopMeInterstitial) this.mBaseLoopMe;
            interstitial.onLoopMeInterstitialLoadFail(interstitial, error);
        }
    }

    private void loadSuccess() {
        if (this.mSilentMode) {
            this.mBaseLoopMe.setInitStatus(true);
            ((LoopMeBanner) this.mBaseLoopMe).callWebViewAppearHelper();
        } else if (this.mBaseLoopMe.getAdFormat() == AdFormat.BANNER) {
            ((LoopMeBanner) this.mBaseLoopMe).loadComplete();
        } else {
            LoopMeInterstitial interstitial = (LoopMeInterstitial) this.mBaseLoopMe;
            interstitial.onLoopMeInterstitialLoadSuccess(interstitial);
        }
    }

    private void logShareEvent(String url) {
        String event = Preconditions.EMPTY_ARGUMENTS;
        try {
            event = Uri.parse(url).getQueryParameter("et");
        } catch (UnsupportedOperationException e) {
            Utilities.log(LOG_TAG, e.getMessage(), LogLevel.ERROR);
        }
        if (event != null && event.length() > 0) {
            Utilities.log(LOG_TAG, new StringBuilder("Event: ").append(event).toString(), LogLevel.DEBUG);
        }
    }

    private void startAdDetailActivity(String url) {
        if (this.mBaseLoopMe == null) {
            Utilities.log(LOG_TAG, "mBaseLoopMe is not inited", LogLevel.ERROR);
        } else {
            Activity activity = this.mBaseLoopMe.getActivity();
            BaseLoopMeHolder.put(this.mBaseLoopMe);
            Intent intent = new Intent(activity, LoopMeAdDetailActivity.class);
            intent.putExtra(PlusShare.KEY_CALL_TO_ACTION_URL, url);
            intent.addFlags(DriveFile.MODE_READ_ONLY);
            if (this.mBaseLoopMe.getAdFormat() == AdFormat.BANNER) {
                LoopMeBanner banner = (LoopMeBanner) this.mBaseLoopMe;
                banner.onLoopMeBannerClicked(banner);
            } else {
                LoopMeInterstitial interstitial = (LoopMeInterstitial) this.mBaseLoopMe;
                interstitial.onLoopMeInterstitialClicked(interstitial);
            }
            activity.startActivity(intent);
        }
    }

    public void onLoadResource(WebView view, String url) {
        if (Utilities.isAutoTestsEventsEnabled()) {
            logShareEvent(url);
        }
        super.onLoadResource(view, url);
    }

    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        loadFail(new LoopMeError(new StringBuilder("onReceivedError ").append(description).toString()));
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith(LOOPME_FAIL)) {
            loadFail(new LoopMeError(new StringBuilder("Ad received specific URL: ").append(url).toString()));
        } else if (url.startsWith(LOOPME_CLOSE)) {
            loadFail(new LoopMeError(new StringBuilder("Ad received specific URL: ").append(url).toString()));
        } else if (url.startsWith(LOOPME_SUCCESS)) {
            Utilities.log(LOG_TAG, new StringBuilder("Ad received specific URL: ").append(url).toString(), LogLevel.DEBUG);
            loadSuccess();
            view.loadUrl("javascript:window.android.getDelayValue(document.getElementById(\"LOOPME_script\").dataset.delay);");
        } else {
            startAdDetailActivity(url);
        }
        return true;
    }
}