package com.loopme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build.VERSION;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import com.loopme.utilites.Drawables;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.Preconditions;

class AdDetailWebViewClient extends WebViewClient {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$loopme$AdFormat = null;
    private static final String HEADER_PLAIN_TEXT = "plain/text";
    private static final String LOG_TAG;
    private static final String URL_GEO1 = "https://maps.google.com";
    private static final String URL_GEO2 = "geo:";
    private static final String URL_MAILTO = "mailto:";
    private static final String URL_MARKET = "https://play.google.com/store/apps/";
    private static final String URL_MARKET_INTENT = "market://";
    private static final String URL_TEL = "tel:";
    private static final String URL_YOUTUBE = "vnd.youtube:";
    private static final String URL_YOUTUBE_REDIRECT1 = "http://www.youtube.com/";
    private static final String URL_YOUTUBE_REDIRECT2 = "m.youtube.com";
    private Button mBackButton;
    private BaseLoopMe mBaseLoopMe;
    private View mProgress;

    static /* synthetic */ int[] $SWITCH_TABLE$com$loopme$AdFormat() {
        int[] iArr = $SWITCH_TABLE$com$loopme$AdFormat;
        if (iArr == null) {
            iArr = new int[AdFormat.values().length];
            try {
                iArr[AdFormat.BANNER.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[AdFormat.INTERSTITIAL.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            $SWITCH_TABLE$com$loopme$AdFormat = iArr;
        }
        return iArr;
    }

    static {
        LOG_TAG = AdDetailWebViewClient.class.getSimpleName();
    }

    public AdDetailWebViewClient(BaseLoopMe baseLoopme, View progress, Button backButton) {
        if (baseLoopme == null || progress == null || backButton == null) {
            Utilities.log(LOG_TAG, "Illegal argument(s)", LogLevel.ERROR);
        } else {
            this.mBaseLoopMe = baseLoopme;
            this.mProgress = progress;
            this.mBackButton = backButton;
        }
    }

    private void goMarket(String url) {
        switch ($SWITCH_TABLE$com$loopme$AdFormat()[this.mBaseLoopMe.getAdFormat().ordinal()]) {
            case MMAdView.TRANSITION_FADE:
                LoopMeBanner banner = (LoopMeBanner) this.mBaseLoopMe;
                banner.onLoopMeBannerLeaveApp(banner);
                break;
            case MMAdView.TRANSITION_UP:
                LoopMeInterstitial interstitial = (LoopMeInterstitial) this.mBaseLoopMe;
                interstitial.onLoopMeInterstitialLeaveApp(interstitial);
                break;
            default:
                throw new IllegalArgumentException("Unknown ad format");
        }
        url = url.replace(URL_MARKET, URL_MARKET_INTENT);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse(url));
        this.mBaseLoopMe.getActivity().startActivity(intent);
    }

    private void goYoutube(String url) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse(url));
        this.mBaseLoopMe.getActivity().startActivity(intent);
    }

    private boolean isMarket(String url) {
        return url.indexOf(URL_MARKET) >= 0 || url.indexOf(URL_MARKET_INTENT) >= 0;
    }

    private boolean isYoutube(String url) {
        return url.indexOf(URL_YOUTUBE_REDIRECT1) >= 0 || url.indexOf(URL_YOUTUBE_REDIRECT2) >= 0 || url.indexOf(URL_YOUTUBE) >= 0;
    }

    public final void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        this.mProgress.setVisibility(MMAdView.TRANSITION_RANDOM);
        if (view.canGoBack()) {
            if (VERSION.SDK_INT < 16) {
                this.mBackButton.setBackgroundDrawable(Drawables.BTN_BACK_ACTIVE.decodeImage(this.mBaseLoopMe.getActivity()));
            } else {
                this.mBackButton.setBackground(Drawables.BTN_BACK_ACTIVE.decodeImage(this.mBaseLoopMe.getActivity()));
            }
        } else if (VERSION.SDK_INT < 16) {
            this.mBackButton.setBackgroundDrawable(Drawables.BTN_BACK_INACTIVE.decodeImage(this.mBaseLoopMe.getActivity()));
        } else {
            this.mBackButton.setBackground(Drawables.BTN_BACK_INACTIVE.decodeImage(this.mBaseLoopMe.getActivity()));
        }
    }

    public final void onPageStarted(WebView view, String url, Bitmap favicon) {
        this.mProgress.setVisibility(0);
        if (isMarket(url)) {
            goMarket(url);
        } else if (isYoutube(url)) {
            goYoutube(url);
        } else {
            super.onPageStarted(view, url, favicon);
        }
    }

    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        Utilities.log(LOG_TAG, new StringBuilder("onReceivedError code: ").append(errorCode).append(" description: ").append(description).toString(), LogLevel.ERROR);
        this.mProgress.setVisibility(MMAdView.TRANSITION_RANDOM);
        this.mBaseLoopMe.getActivity().finish();
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Utilities.log(LOG_TAG, new StringBuilder("shouldOverrideUrlLoading url=").append(url).toString(), LogLevel.DEBUG);
        this.mProgress.setVisibility(0);
        if (url.startsWith(URL_TEL)) {
            this.mBaseLoopMe.getActivity().startActivity(new Intent("android.intent.action.DIAL", Uri.parse(url)));
        } else if (url.startsWith(URL_MAILTO)) {
            url = url.replaceFirst(URL_MAILTO, Preconditions.EMPTY_ARGUMENTS).trim();
            Intent i = new Intent("android.intent.action.SEND");
            i.setType(HEADER_PLAIN_TEXT).putExtra("android.intent.extra.EMAIL", new String[]{url});
            this.mBaseLoopMe.getActivity().startActivity(i);
        } else if (url.startsWith(URL_GEO1) || url.startsWith(URL_GEO2)) {
            this.mBaseLoopMe.getActivity().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
        } else if (isMarket(url)) {
            goMarket(url);
        } else if (!isYoutube(url)) {
            return false;
        } else {
            goYoutube(url);
        }
        return true;
    }
}