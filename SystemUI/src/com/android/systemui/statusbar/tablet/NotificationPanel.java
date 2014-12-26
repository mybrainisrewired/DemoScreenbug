package com.android.systemui.statusbar.tablet;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;
import com.android.systemui.ExpandHelper;
import com.android.systemui.R;
import com.android.systemui.recent.RecentsCallback;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.policy.NotificationRowLayout;

public class NotificationPanel extends RelativeLayout implements StatusBarPanel, OnClickListener {
    static final boolean DEBUG = false;
    static final int PANEL_FADE_DURATION = 150;
    static final String TAG = "Tablet/NotificationPanel";
    static Interpolator sAccelerateInterpolator;
    static Interpolator sDecelerateInterpolator;
    private NotificationRowLayout latestItems;
    TabletStatusBar mBar;
    Choreographer mChoreo;
    View mClearButton;
    private OnClickListener mClearButtonListener;
    Rect mContentArea;
    ViewGroup mContentFrame;
    float mContentFrameMissingTranslation;
    ViewGroup mContentParent;
    private ExpandHelper mExpandHelper;
    boolean mHasClearableNotifications;
    View mNotificationButton;
    int mNotificationCount;
    View mNotificationScroller;
    private OnPreDrawListener mPreDrawListener;
    View mSettingsButton;
    View mSettingsView;
    boolean mShowing;
    NotificationPanelTitle mTitleArea;

    class AnonymousClass_3 extends AnimatorListenerAdapter {
        final /* synthetic */ View val$toHide;
        final /* synthetic */ View val$toShow;

        AnonymousClass_3(View view, View view2) {
            this.val$toHide = view;
            this.val$toShow = view2;
        }

        public void onAnimationEnd(Animator _a) {
            this.val$toHide.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
            if (this.val$toShow != null) {
                this.val$toShow.setVisibility(0);
                if (this.val$toShow == NotificationPanel.this.mSettingsView || NotificationPanel.this.mNotificationCount > 0) {
                    ObjectAnimator.ofFloat(this.val$toShow, "alpha", new float[]{0.0f, 1.0f}).setDuration(150).start();
                }
                if (this.val$toHide == NotificationPanel.this.mSettingsView) {
                    NotificationPanel.this.removeSettingsView();
                }
            }
            NotificationPanel.this.updateClearButton();
            NotificationPanel.this.updatePanelModeButtons();
        }
    }

    private class Choreographer implements AnimatorListener {
        static final int CLOSE_DURATION = 250;
        static final int OPEN_DURATION = 250;
        final int HYPERSPACE_OFFRAMP;
        AnimatorSet mContentAnim;
        int mPanelHeight;
        boolean mVisible;

        Choreographer() {
            this.HYPERSPACE_OFFRAMP = 200;
        }

        void createAnimation(boolean appearing) {
            float end;
            float start;
            float y = NotificationPanel.this.mContentParent.getTranslationY();
            if (appearing) {
                end = 0.0f;
                if (NotificationPanel.this.mNotificationCount == 0) {
                    end = 0.0f + NotificationPanel.this.mContentFrameMissingTranslation;
                }
                start = 200.0f + end;
            } else {
                start = y;
                end = y + 200.0f;
            }
            Animator posAnim = ObjectAnimator.ofFloat(NotificationPanel.this.mContentParent, "translationY", new float[]{start, end});
            posAnim.setInterpolator(appearing ? sDecelerateInterpolator : sAccelerateInterpolator);
            if (this.mContentAnim != null && this.mContentAnim.isRunning()) {
                this.mContentAnim.cancel();
            }
            ViewGroup viewGroup = NotificationPanel.this.mContentParent;
            String str = "alpha";
            float[] fArr = new float[1];
            fArr[0] = appearing ? 1065353216 : 0;
            Animator fadeAnim = ObjectAnimator.ofFloat(viewGroup, str, fArr);
            fadeAnim.setInterpolator(appearing ? sAccelerateInterpolator : sDecelerateInterpolator);
            this.mContentAnim = new AnimatorSet();
            this.mContentAnim.play(fadeAnim).with(posAnim);
            AnimatorSet animatorSet = this.mContentAnim;
            if (appearing) {
                animatorSet.setDuration((long) 250);
                this.mContentAnim.addListener(this);
            } else {
                animatorSet.setDuration((long) 250);
                this.mContentAnim.addListener(this);
            }
        }

        public void onAnimationCancel(Animator animation) {
        }

        public void onAnimationEnd(Animator animation) {
            if (!this.mVisible) {
                NotificationPanel.this.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
            }
            NotificationPanel.this.mContentParent.setLayerType(0, null);
            this.mContentAnim = null;
            if (this.mVisible) {
                NotificationPanel.this.updateClearButton();
            }
        }

        public void onAnimationRepeat(Animator animation) {
        }

        public void onAnimationStart(Animator animation) {
        }

        void startAnimation(boolean appearing) {
            createAnimation(appearing);
            this.mContentAnim.start();
            this.mVisible = appearing;
            if (!this.mVisible) {
                NotificationPanel.this.updateClearButton();
            }
        }
    }

    static {
        sAccelerateInterpolator = new AccelerateInterpolator();
        sDecelerateInterpolator = new DecelerateInterpolator();
    }

    public NotificationPanel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NotificationPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mHasClearableNotifications = false;
        this.mNotificationCount = 0;
        this.mContentArea = new Rect();
        this.mChoreo = new Choreographer();
        this.mClearButtonListener = new OnClickListener() {
            public void onClick(View v) {
                NotificationPanel.this.mBar.clearAll();
            }
        };
        this.mPreDrawListener = new OnPreDrawListener() {
            public boolean onPreDraw() {
                NotificationPanel.this.getViewTreeObserver().removeOnPreDrawListener(this);
                NotificationPanel.this.mChoreo.startAnimation(true);
                return DEBUG;
            }
        };
    }

    void addSettingsView() {
        this.mSettingsView = LayoutInflater.from(getContext()).inflate(R.layout.system_bar_settings_view, this.mContentFrame, DEBUG);
        this.mSettingsView.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
        this.mContentFrame.addView(this.mSettingsView);
    }

    public boolean dispatchHoverEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        return (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) ? true : super.dispatchHoverEvent(event);
    }

    public View getClearButton() {
        return this.mClearButton;
    }

    public boolean isInContentArea(int x, int y) {
        this.mContentArea.left = this.mContentFrame.getLeft() + this.mContentFrame.getPaddingLeft();
        this.mContentArea.top = this.mContentFrame.getTop() + this.mContentFrame.getPaddingTop() + ((int) this.mContentParent.getTranslationY());
        this.mContentArea.right = this.mContentFrame.getRight() - this.mContentFrame.getPaddingRight();
        this.mContentArea.bottom = this.mContentFrame.getBottom() - this.mContentFrame.getPaddingBottom();
        offsetDescendantRectToMyCoords(this.mContentParent, this.mContentArea);
        return this.mContentArea.contains(x, y);
    }

    public boolean isShowing() {
        return this.mShowing;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.latestItems = (NotificationRowLayout) findViewById(R.id.content);
        this.mExpandHelper = new ExpandHelper(this.mContext, this.latestItems, getResources().getDimensionPixelSize(R.dimen.notification_row_min_height), getResources().getDimensionPixelSize(R.dimen.notification_row_max_height));
        this.mExpandHelper.setEventSource(this);
        this.mExpandHelper.setGravity(80);
    }

    public void onClick(View v) {
        if (this.mSettingsButton.isEnabled() && v == this.mTitleArea) {
            swapPanels();
        }
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        setWillNotDraw(DEBUG);
        this.mContentParent = (ViewGroup) findViewById(R.id.content_parent);
        this.mContentParent.bringToFront();
        this.mTitleArea = (NotificationPanelTitle) findViewById(R.id.title_area);
        this.mTitleArea.setPanel(this);
        this.mSettingsButton = findViewById(R.id.settings_button);
        this.mNotificationButton = findViewById(R.id.notification_button);
        this.mNotificationScroller = findViewById(R.id.notification_scroller);
        this.mContentFrame = (ViewGroup) findViewById(R.id.content_frame);
        this.mContentFrameMissingTranslation = 0.0f;
        this.mClearButton = findViewById(R.id.clear_all_button);
        this.mClearButton.setOnClickListener(this.mClearButtonListener);
        this.mShowing = false;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        MotionEvent cancellation = MotionEvent.obtain(ev);
        cancellation.setAction(RecentsCallback.SWIPE_DOWN);
        boolean intercept = (this.mExpandHelper.onInterceptTouchEvent(ev) || super.onInterceptTouchEvent(ev)) ? true : DEBUG;
        if (intercept) {
            this.latestItems.onInterceptTouchEvent(cancellation);
        }
        return intercept;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        return (this.mExpandHelper.onTouchEvent(ev) || super.onTouchEvent(ev)) ? true : DEBUG;
    }

    public void onVisibilityChanged(View v, int vis) {
        super.onVisibilityChanged(v, vis);
        if (vis != 0) {
            if (this.mSettingsView != null) {
                removeSettingsView();
            }
            this.mNotificationScroller.setVisibility(0);
            this.mNotificationScroller.setAlpha(1.0f);
            this.mNotificationScroller.scrollTo(0, 0);
            updatePanelModeButtons();
        }
    }

    void removeSettingsView() {
        if (this.mSettingsView != null) {
            this.mContentFrame.removeView(this.mSettingsView);
            this.mSettingsView = null;
        }
    }

    public void setBar(TabletStatusBar b) {
        this.mBar = b;
    }

    public void setClearable(boolean clearable) {
        this.mHasClearableNotifications = clearable;
    }

    public void setContentFrameVisible(boolean showing, boolean animate) {
    }

    public void setNotificationCount(int n) {
        this.mNotificationCount = n;
    }

    public void setSettingsEnabled(boolean settingsEnabled) {
        if (this.mSettingsButton != null) {
            this.mSettingsButton.setEnabled(settingsEnabled);
            if (this.mNotificationButton.getVisibility() != 0) {
                this.mSettingsButton.setVisibility(settingsEnabled ? 0 : CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
            }
        }
    }

    public void show(boolean show, boolean animate) {
        int i = 0;
        if (!animate) {
            this.mShowing = show;
            if (!show) {
                i = CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL;
            }
            setVisibility(i);
        } else if (this.mShowing != show) {
            this.mShowing = show;
            if (show) {
                setVisibility(0);
                this.mContentParent.setLayerType(CommandQueue.FLAG_EXCLUDE_RECENTS_PANEL, null);
                getViewTreeObserver().addOnPreDrawListener(this.mPreDrawListener);
            } else {
                this.mChoreo.startAnimation(show);
            }
        }
    }

    public void swapPanels() {
        View toShow;
        View toHide;
        if (this.mSettingsView == null) {
            addSettingsView();
            toShow = this.mSettingsView;
            toHide = this.mNotificationScroller;
        } else {
            toShow = this.mNotificationScroller;
            toHide = this.mSettingsView;
        }
        Animator a = ObjectAnimator.ofFloat(toHide, "alpha", new float[]{1.0f, 0.0f}).setDuration(150);
        a.addListener(new AnonymousClass_3(toHide, toShow));
        a.start();
    }

    public void updateClearButton() {
        int i = 0;
        if (this.mBar != null) {
            boolean showX = isShowing() && this.mHasClearableNotifications && this.mNotificationScroller.getVisibility() == 0;
            View clearButton = getClearButton();
            if (!showX) {
                i = CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL;
            }
            clearButton.setVisibility(i);
        }
    }

    public void updatePanelModeButtons() {
        boolean settingsVisible;
        int i;
        int i2 = 0;
        if (this.mSettingsView != null) {
            settingsVisible = true;
        } else {
            settingsVisible = false;
        }
        View view = this.mSettingsButton;
        if (settingsVisible || !this.mSettingsButton.isEnabled()) {
            i = 8;
        } else {
            i = 0;
        }
        view.setVisibility(i);
        View view2 = this.mNotificationButton;
        if (!settingsVisible) {
            i2 = 8;
        }
        view2.setVisibility(i2);
    }
}