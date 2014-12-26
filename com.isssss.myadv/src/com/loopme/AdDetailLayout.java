package com.loopme;

import android.content.Context;
import android.os.Build.VERSION;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.loopme.utilites.Drawables;
import com.loopme.utilites.ScreenMetrics;

final class AdDetailLayout extends RelativeLayout {
    private final WebView mAdDetailWebview;
    private final Button mBackBtn;
    private final Button mCloseBtn;
    private final RelativeLayout mFooterView;
    private final Button mNativeBtn;
    private final ProgressBar mProgressBtn;
    private final Button mRefreshBtn;

    public AdDetailLayout(Context context) {
        super(context);
        LayoutParams params = new LayoutParams(-1, -1);
        setLayoutParams(params);
        this.mAdDetailWebview = new WebView(context);
        this.mAdDetailWebview.setLayoutParams(params);
        addView(this.mAdDetailWebview);
        this.mFooterView = new RelativeLayout(context);
        configFooterView(context);
        LinearLayout buttonsContainer = new LinearLayout(context);
        configButtonsContainer(buttonsContainer);
        LayoutParams buttons_params = new LayoutParams(ScreenMetrics.getInstance().getDisplayMetrics(context).widthPixels / 5, -1);
        int size = ScreenMetrics.getInstance().getHeaderHeight(context) / 2;
        LayoutParams pb_params = new LayoutParams(size, size);
        pb_params.addRule(ApiEventType.API_MRAID_CLOSE);
        this.mProgressBtn = new ProgressBar(context);
        configProgressButton(context, buttonsContainer, buttons_params, pb_params);
        this.mBackBtn = new Button(context);
        configBackButton(context, buttonsContainer, buttons_params, pb_params);
        this.mRefreshBtn = new Button(context);
        configRefreshButton(context, buttonsContainer, buttons_params, pb_params);
        this.mNativeBtn = new Button(context);
        configNativeButton(context, buttonsContainer, buttons_params, pb_params);
        this.mCloseBtn = new Button(context);
        configCloseButton(context, buttonsContainer, buttons_params, pb_params);
        this.mFooterView.addView(initBottomWhiteLineView(context));
    }

    private void configBackButton(Context context, LinearLayout buttonsContainer, LayoutParams buttons_params, LayoutParams pb_params) {
        RelativeLayout backLayout = new RelativeLayout(context);
        backLayout.setLayoutParams(buttons_params);
        if (VERSION.SDK_INT < 16) {
            this.mBackBtn.setBackgroundDrawable(Drawables.BTN_BACK_INACTIVE.decodeImage(context));
        } else {
            this.mBackBtn.setBackground(Drawables.BTN_BACK_INACTIVE.decodeImage(context));
        }
        this.mBackBtn.setLayoutParams(pb_params);
        backLayout.addView(this.mBackBtn);
        buttonsContainer.addView(backLayout);
    }

    private void configButtonsContainer(LinearLayout buttonsContainer) {
        buttonsContainer.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        buttonsContainer.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        this.mFooterView.addView(buttonsContainer);
    }

    private void configCloseButton(Context context, LinearLayout buttonsContainer, LayoutParams buttons_params, LayoutParams pb_params) {
        RelativeLayout closeLayout = new RelativeLayout(context);
        closeLayout.setLayoutParams(buttons_params);
        if (VERSION.SDK_INT < 16) {
            this.mCloseBtn.setBackgroundDrawable(Drawables.BTN_CLOSE.decodeImage(context));
        } else {
            this.mCloseBtn.setBackground(Drawables.BTN_CLOSE.decodeImage(context));
        }
        this.mCloseBtn.setLayoutParams(pb_params);
        closeLayout.addView(this.mCloseBtn);
        buttonsContainer.addView(closeLayout);
    }

    private void configFooterView(Context context) {
        LayoutParams footer_params = new LayoutParams(-1, ScreenMetrics.getInstance().getHeaderHeight(context));
        footer_params.addRule(ApiEventType.API_MRAID_RESIZE);
        this.mFooterView.setLayoutParams(footer_params);
        addView(this.mFooterView);
    }

    private void configNativeButton(Context context, LinearLayout buttonsContainer, LayoutParams buttons_params, LayoutParams pb_params) {
        RelativeLayout nativeLayout = new RelativeLayout(context);
        nativeLayout.setLayoutParams(buttons_params);
        if (VERSION.SDK_INT < 16) {
            this.mNativeBtn.setBackgroundDrawable(Drawables.BTN_NATIVE_BROWSER.decodeImage(context));
        } else {
            this.mNativeBtn.setBackground(Drawables.BTN_NATIVE_BROWSER.decodeImage(context));
        }
        this.mNativeBtn.setLayoutParams(pb_params);
        nativeLayout.addView(this.mNativeBtn);
        buttonsContainer.addView(nativeLayout);
    }

    private void configProgressButton(Context context, LinearLayout buttonsContainer, LayoutParams buttons_params, LayoutParams pb_params) {
        RelativeLayout progressLayout = new RelativeLayout(context);
        progressLayout.setLayoutParams(buttons_params);
        this.mProgressBtn.setLayoutParams(pb_params);
        progressLayout.addView(this.mProgressBtn);
        buttonsContainer.addView(progressLayout);
    }

    private void configRefreshButton(Context context, LinearLayout buttonsContainer, LayoutParams buttons_params, LayoutParams pb_params) {
        RelativeLayout refreshLayout = new RelativeLayout(context);
        refreshLayout.setLayoutParams(buttons_params);
        if (VERSION.SDK_INT < 16) {
            this.mRefreshBtn.setBackgroundDrawable(Drawables.BTN_REFRESH.decodeImage(context));
        } else {
            this.mRefreshBtn.setBackground(Drawables.BTN_REFRESH.decodeImage(context));
        }
        this.mRefreshBtn.setLayoutParams(pb_params);
        refreshLayout.addView(this.mRefreshBtn);
        buttonsContainer.addView(refreshLayout);
    }

    private View initBottomWhiteLineView(Context context) {
        View whiteLine = new View(context);
        LayoutParams whiteLineParams = new LayoutParams(-1, 1);
        whiteLineParams.addRule(ApiEventType.API_MRAID_RESIZE);
        whiteLine.setLayoutParams(whiteLineParams);
        whiteLine.setBackgroundColor(-1);
        return whiteLine;
    }

    public Button getBackButton() {
        return this.mBackBtn;
    }

    public Button getCloseButton() {
        return this.mCloseBtn;
    }

    public RelativeLayout getFooter() {
        return this.mFooterView;
    }

    public Button getNativeButton() {
        return this.mNativeBtn;
    }

    public ProgressBar getProgressBar() {
        return this.mProgressBtn;
    }

    public Button getRefreshButton() {
        return this.mRefreshBtn;
    }

    public WebView getWebView() {
        return this.mAdDetailWebview;
    }
}