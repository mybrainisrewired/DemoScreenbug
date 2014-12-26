package com.mopub.mobileads;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.android.volley.DefaultRetryPolicy;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.util.Dips;
import com.mopub.common.util.Utils;
import com.mopub.mobileads.resource.CloseButtonDrawable;
import com.mopub.mobileads.resource.CountdownDrawable;
import com.mopub.mobileads.resource.LearnMoreDrawable;

class VastVideoToolbar extends LinearLayout {
    private static final int THRESHOLD_FOR_HIDING_VIDEO_DURATION = 200;
    private static final int TOOLBAR_HEIGHT_DIPS = 44;
    private final ToolbarWidget mCloseButtonWidget;
    private final ToolbarWidget mCountdownWidget;
    private final ToolbarWidget mDurationWidget;
    private final ToolbarWidget mLearnMoreWidget;

    public VastVideoToolbar(Context context) {
        super(context);
        setId((int) Utils.generateUniqueId());
        setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        setLayoutParams(new LayoutParams(-1, Dips.dipsToIntPixels(44.0f, getContext())));
        setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        getBackground().setAlpha(180);
        this.mDurationWidget = createDurationWidget();
        this.mLearnMoreWidget = createLearnMoreWidget();
        this.mCountdownWidget = createCountdownWidget();
        this.mCloseButtonWidget = createCloseButtonWidget();
        addView(this.mDurationWidget);
        addView(this.mLearnMoreWidget);
        addView(this.mCountdownWidget);
        addView(this.mCloseButtonWidget);
    }

    private ToolbarWidget createCloseButtonWidget() {
        return new Builder(getContext()).weight(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT).widgetGravity(ApiEventType.API_MRAID_POST_TO_SOCIAL).defaultText("Close").drawable(new CloseButtonDrawable()).visibility(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES).build();
    }

    private ToolbarWidget createCountdownWidget() {
        return new Builder(getContext()).weight(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT).widgetGravity(ApiEventType.API_MRAID_POST_TO_SOCIAL).defaultText("Skip in").drawable(new CountdownDrawable(getContext())).visibility(MMAdView.TRANSITION_RANDOM).build();
    }

    private ToolbarWidget createDurationWidget() {
        return new Builder(getContext()).weight(2.0f).widgetGravity(Encoder.LINE_GROUPS).hasText().textAlign(ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES).build();
    }

    private ToolbarWidget createLearnMoreWidget() {
        return new Builder(getContext()).weight(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT).widgetGravity(ApiEventType.API_MRAID_POST_TO_SOCIAL).defaultText("Learn More").drawable(new LearnMoreDrawable()).visibility(MMAdView.TRANSITION_RANDOM).build();
    }

    @Deprecated
    ToolbarWidget getCloseButtonWidget() {
        return this.mCloseButtonWidget;
    }

    @Deprecated
    ToolbarWidget getCountdownWidget() {
        return this.mCountdownWidget;
    }

    String getDisplaySeconds(long millisecondsRemaining) {
        return String.valueOf(Math.round(Math.ceil((double) (((float) millisecondsRemaining) / 1000.0f))));
    }

    @Deprecated
    ToolbarWidget getDurationWidget() {
        return this.mDurationWidget;
    }

    @Deprecated
    ToolbarWidget getLearnMoreWidget() {
        return this.mLearnMoreWidget;
    }

    void makeInteractable() {
        this.mCountdownWidget.setVisibility(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES);
        this.mLearnMoreWidget.setVisibility(0);
        this.mCloseButtonWidget.setVisibility(0);
    }

    void setCloseButtonOnTouchListener(OnTouchListener onTouchListener) {
        this.mCloseButtonWidget.setOnTouchListener(onTouchListener);
    }

    void setLearnMoreButtonOnTouchListener(OnTouchListener onTouchListener) {
        this.mLearnMoreWidget.setOnTouchListener(onTouchListener);
    }

    void updateCountdownWidget(int remainingTime) {
        if (remainingTime >= 0 && this.mCountdownWidget.getVisibility() == 4) {
            this.mCloseButtonWidget.setVisibility(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES);
            this.mCountdownWidget.setVisibility(0);
        }
        this.mCountdownWidget.updateImageText(getDisplaySeconds((long) remainingTime));
    }

    void updateDurationWidget(int remainingTime) {
        if (remainingTime >= 200) {
            this.mDurationWidget.updateText(new StringBuilder("Ends in ").append(getDisplaySeconds((long) remainingTime)).append(" seconds").toString());
        } else if (remainingTime >= 0) {
            this.mDurationWidget.updateText("Thanks for watching");
        }
    }
}