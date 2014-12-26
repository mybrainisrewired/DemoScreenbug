package com.android.systemui;

import android.animation.LayoutTransition;
import android.app.ActivityOptions;
import android.app.ActivityOptions.OnAnimationStartedListener;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.provider.Settings.System;
import android.util.AttributeSet;
import android.util.Slog;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.FrameLayout;
import com.android.internal.widget.multiwaveview.GlowPadView;
import com.android.internal.widget.multiwaveview.GlowPadView.OnTriggerListener;
import com.android.systemui.recent.RecentsCallback;
import com.android.systemui.recent.StatusBarTouchProxy;
import com.android.systemui.statusbar.BaseStatusBar;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.tablet.StatusBarPanel;

public class SearchPanelView extends FrameLayout implements StatusBarPanel, OnAnimationStartedListener {
    private static final String ASSIST_ICON_METADATA_NAME = "com.android.systemui.action_assist_icon";
    static final boolean DEBUG = false;
    private static final int SEARCH_PANEL_HOLD_DURATION = 0;
    static final String TAG = "SearchPanelView";
    private BaseStatusBar mBar;
    private final Context mContext;
    private GlowPadView mGlowPadView;
    final GlowPadTriggerListener mGlowPadViewListener;
    private final OnPreDrawListener mPreDrawListener;
    private View mSearchTargetsContainer;
    private boolean mShowing;
    private StatusBarTouchProxy mStatusBarTouchProxy;

    class GlowPadTriggerListener implements OnTriggerListener {
        boolean mWaitingForLaunch;

        GlowPadTriggerListener() {
        }

        public void onFinishFinalAnimation() {
        }

        public void onGrabbed(View v, int handle) {
        }

        public void onGrabbedStateChange(View v, int handle) {
            if (!this.mWaitingForLaunch && handle == 0) {
                SearchPanelView.this.mBar.hideSearchPanel();
            }
        }

        public void onReleased(View v, int handle) {
        }

        public void onTrigger(View v, int target) {
            int resId = SearchPanelView.this.mGlowPadView.getResourceIdForTarget(target);
        }
    }

    public SearchPanelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchPanelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mGlowPadViewListener = new GlowPadTriggerListener();
        this.mPreDrawListener = new OnPreDrawListener() {
            public boolean onPreDraw() {
                SearchPanelView.this.getViewTreeObserver().removeOnPreDrawListener(this);
                SearchPanelView.this.mGlowPadView.resumeAnimations();
                return DEBUG;
            }
        };
        this.mContext = context;
    }

    private LayoutTransition createLayoutTransitioner() {
        LayoutTransition transitioner = new LayoutTransition();
        transitioner.setDuration(200);
        transitioner.setStartDelay(1, 0);
        transitioner.setAnimator(RecentsCallback.SWIPE_DOWN, null);
        return transitioner;
    }

    private void maybeSwapSearchIcon() {
        Intent intent = SearchManager.getAssistIntent(this.mContext);
        if (intent != null) {
            ComponentName component = intent.getComponent();
            if (component != null && this.mGlowPadView.replaceTargetDrawablesIfPresent(component, ASSIST_ICON_METADATA_NAME, 17302164)) {
            }
        }
    }

    private boolean pointInside(int x, int y, View v) {
        return (x < v.getLeft() || x >= v.getRight() || y < v.getTop() || y >= v.getBottom()) ? DEBUG : true;
    }

    private void startAssistActivity() {
        this.mBar.animateCollapse(1);
        Intent intent = SearchManager.getAssistIntent(this.mContext);
        if (intent != null) {
            try {
                ActivityOptions opts = ActivityOptions.makeCustomAnimation(this.mContext, R.anim.search_launch_enter, R.anim.search_launch_exit, getHandler(), this);
                intent.addFlags(268435456);
                this.mContext.startActivity(intent, opts.toBundle());
            } catch (ActivityNotFoundException e) {
                Slog.w(TAG, "Activity not found for " + intent.getAction());
                onAnimationStarted();
            }
        }
    }

    private void vibrate() {
        Context context = getContext();
        if (System.getInt(context.getContentResolver(), "haptic_feedback_enabled", 1) != 0) {
            ((Vibrator) context.getSystemService("vibrator")).vibrate((long) context.getResources().getInteger(R.integer.config_search_panel_view_vibration_duration));
        }
    }

    public boolean dispatchHoverEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        return (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) ? true : super.dispatchHoverEvent(event);
    }

    public void hide(boolean animate) {
        if (this.mBar != null) {
            this.mBar.animateCollapse(SEARCH_PANEL_HOLD_DURATION);
        } else {
            setVisibility(CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL);
        }
    }

    public boolean isAssistantAvailable() {
        return SearchManager.getAssistIntent(this.mContext) != null ? true : DEBUG;
    }

    public boolean isInContentArea(int x, int y) {
        if (pointInside(x, y, this.mSearchTargetsContainer)) {
            return true;
        }
        return (this.mStatusBarTouchProxy == null || !pointInside(x, y, this.mStatusBarTouchProxy)) ? DEBUG : true;
    }

    public boolean isShowing() {
        return this.mShowing;
    }

    public void onAnimationStarted() {
        postDelayed(new Runnable() {
            public void run() {
                SearchPanelView.this.mGlowPadViewListener.mWaitingForLaunch = false;
                SearchPanelView.this.mBar.hideSearchPanel();
            }
        }, 0);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mContext.getSystemService("layout_inflater");
        this.mSearchTargetsContainer = findViewById(R.id.search_panel_container);
        this.mStatusBarTouchProxy = (StatusBarTouchProxy) findViewById(R.id.status_bar_touch_proxy);
        this.mGlowPadView = (GlowPadView) findViewById(R.id.glow_pad_view);
        this.mGlowPadView.setOnTriggerListener(this.mGlowPadViewListener);
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    public void setBar(BaseStatusBar bar) {
        this.mBar = bar;
    }

    public void setStatusBarView(View statusBarView) {
        if (this.mStatusBarTouchProxy != null) {
            this.mStatusBarTouchProxy.setStatusBar(statusBarView);
        }
    }

    public void show(boolean show, boolean animate) {
        if (!show) {
            ((ViewGroup) this.mSearchTargetsContainer).setLayoutTransition(animate ? createLayoutTransitioner() : null);
        }
        this.mShowing = show;
        if (show) {
            maybeSwapSearchIcon();
            if (getVisibility() != 0) {
                setVisibility(SEARCH_PANEL_HOLD_DURATION);
                this.mGlowPadView.suspendAnimations();
                this.mGlowPadView.ping();
                getViewTreeObserver().addOnPreDrawListener(this.mPreDrawListener);
                vibrate();
            }
            setFocusable(true);
            setFocusableInTouchMode(true);
            requestFocus();
        } else {
            setVisibility(CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL);
        }
    }
}