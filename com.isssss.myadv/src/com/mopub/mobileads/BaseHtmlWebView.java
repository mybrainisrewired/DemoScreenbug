package com.mopub.mobileads;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.VersionCode;

public class BaseHtmlWebView extends BaseWebView implements UserClickListener {
    private boolean mClicked;
    private final ViewGestureDetector mViewGestureDetector;

    class AnonymousClass_1 implements OnTouchListener {
        private final /* synthetic */ boolean val$isScrollable;

        AnonymousClass_1(boolean z) {
            this.val$isScrollable = z;
        }

        public boolean onTouch(View v, MotionEvent event) {
            BaseHtmlWebView.this.mViewGestureDetector.sendTouchEvent(event);
            return event.getAction() == 2 && !this.val$isScrollable;
        }
    }

    public BaseHtmlWebView(Context context, AdConfiguration adConfiguration) {
        super(context);
        disableScrollingAndZoom();
        getSettings().setJavaScriptEnabled(true);
        this.mViewGestureDetector = new ViewGestureDetector(context, this, adConfiguration);
        this.mViewGestureDetector.setUserClickListener(this);
        if (VersionCode.currentApiLevel().isAtLeast(VersionCode.ICE_CREAM_SANDWICH)) {
            enablePlugins(true);
        }
        setBackgroundColor(0);
    }

    private void disableScrollingAndZoom() {
        setHorizontalScrollBarEnabled(false);
        setHorizontalScrollbarOverlay(false);
        setVerticalScrollBarEnabled(false);
        setVerticalScrollbarOverlay(false);
        getSettings().setSupportZoom(false);
    }

    public void init(boolean isScrollable) {
        initializeOnTouchListener(isScrollable);
    }

    void initializeOnTouchListener(boolean isScrollable) {
        setOnTouchListener(new AnonymousClass_1(isScrollable));
    }

    void loadHtmlResponse(String htmlResponse) {
        loadDataWithBaseURL("http://ads.mopub.com/", htmlResponse, "text/html", "utf-8", null);
    }

    public void loadUrl(String url) {
        if (url != null) {
            MoPubLog.d(new StringBuilder("Loading url: ").append(url).toString());
            if (url.startsWith("javascript:")) {
                super.loadUrl(url);
            }
        }
    }

    public void onResetUserClick() {
        this.mClicked = false;
    }

    public void onUserClick() {
        this.mClicked = true;
    }

    public boolean wasClicked() {
        return this.mClicked;
    }
}