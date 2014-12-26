package com.mopub.mobileads;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import com.mopub.common.util.VersionCode;
import com.mopub.mobileads.CustomEventInterstitial.CustomEventInterstitialListener;

public class HtmlInterstitialWebView extends BaseHtmlWebView {
    protected static final String MOPUB_JS_INTERFACE_NAME = "mopubUriInterface";
    private Handler mHandler;

    final class AnonymousClass_1MoPubUriJavascriptInterface {
        private final /* synthetic */ MoPubUriJavascriptFireFinishLoadListener val$moPubUriJavascriptFireFinishLoadListener;

        class AnonymousClass_1 implements Runnable {
            private final /* synthetic */ MoPubUriJavascriptFireFinishLoadListener val$moPubUriJavascriptFireFinishLoadListener;

            AnonymousClass_1(MoPubUriJavascriptFireFinishLoadListener moPubUriJavascriptFireFinishLoadListener) {
                this.val$moPubUriJavascriptFireFinishLoadListener = moPubUriJavascriptFireFinishLoadListener;
            }

            public void run() {
                this.val$moPubUriJavascriptFireFinishLoadListener.onInterstitialLoaded();
            }
        }

        AnonymousClass_1MoPubUriJavascriptInterface(MoPubUriJavascriptFireFinishLoadListener moPubUriJavascriptFireFinishLoadListener) {
            this.val$moPubUriJavascriptFireFinishLoadListener = moPubUriJavascriptFireFinishLoadListener;
        }

        @JavascriptInterface
        public boolean fireFinishLoad() {
            HtmlInterstitialWebView.this.postHandlerRunnable(new AnonymousClass_1(this.val$moPubUriJavascriptFireFinishLoadListener));
            return true;
        }
    }

    static interface MoPubUriJavascriptFireFinishLoadListener {
        void onInterstitialLoaded();
    }

    class AnonymousClass_1 implements MoPubUriJavascriptFireFinishLoadListener {
        private final /* synthetic */ CustomEventInterstitialListener val$customEventInterstitialListener;

        AnonymousClass_1(CustomEventInterstitialListener customEventInterstitialListener) {
            this.val$customEventInterstitialListener = customEventInterstitialListener;
        }

        public void onInterstitialLoaded() {
            if (!HtmlInterstitialWebView.this.mIsDestroyed) {
                this.val$customEventInterstitialListener.onInterstitialLoaded();
            }
        }
    }

    static class HtmlInterstitialWebViewListener implements HtmlWebViewListener {
        private final CustomEventInterstitialListener mCustomEventInterstitialListener;

        public HtmlInterstitialWebViewListener(CustomEventInterstitialListener customEventInterstitialListener) {
            this.mCustomEventInterstitialListener = customEventInterstitialListener;
        }

        public void onClicked() {
            this.mCustomEventInterstitialListener.onInterstitialClicked();
        }

        public void onCollapsed() {
        }

        public void onFailed(MoPubErrorCode errorCode) {
            this.mCustomEventInterstitialListener.onInterstitialFailed(errorCode);
        }

        public void onLoaded(BaseHtmlWebView mHtmlWebView) {
            this.mCustomEventInterstitialListener.onInterstitialLoaded();
        }
    }

    public HtmlInterstitialWebView(Context context, AdConfiguration adConfiguration) {
        super(context, adConfiguration);
        this.mHandler = new Handler();
    }

    private void postHandlerRunnable(Runnable r) {
        this.mHandler.post(r);
    }

    void addMoPubUriJavascriptInterface(MoPubUriJavascriptFireFinishLoadListener moPubUriJavascriptFireFinishLoadListener) {
        addJavascriptInterface(new AnonymousClass_1MoPubUriJavascriptInterface(moPubUriJavascriptFireFinishLoadListener), MOPUB_JS_INTERFACE_NAME);
    }

    @TargetApi(11)
    public void destroy() {
        if (VersionCode.currentApiLevel().isAtLeast(VersionCode.HONEYCOMB)) {
            removeJavascriptInterface(MOPUB_JS_INTERFACE_NAME);
        }
        super.destroy();
    }

    public void init(CustomEventInterstitialListener customEventInterstitialListener, boolean isScrollable, String redirectUrl, String clickthroughUrl) {
        super.init(isScrollable);
        setWebViewClient(new HtmlWebViewClient(new HtmlInterstitialWebViewListener(customEventInterstitialListener), this, clickthroughUrl, redirectUrl));
        addMoPubUriJavascriptInterface(new AnonymousClass_1(customEventInterstitialListener));
    }
}