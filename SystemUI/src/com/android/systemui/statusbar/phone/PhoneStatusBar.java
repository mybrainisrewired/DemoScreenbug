package com.android.systemui.statusbar.phone;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.app.ActivityManagerNative;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Debug;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Slog;
import android.view.Choreographer;
import android.view.Display;
import android.view.IWindowManager;
import android.view.IWindowManager.Stub;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.WindowManagerImpl;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.android.internal.statusbar.StatusBarIcon;
import com.android.internal.statusbar.StatusBarNotification;
import com.android.systemui.R;
import com.android.systemui.recent.RecentTasksLoader;
import com.android.systemui.recent.RecentsCallback;
import com.android.systemui.statusbar.BaseStatusBar;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.NotificationData.Entry;
import com.android.systemui.statusbar.RotationToggle;
import com.android.systemui.statusbar.SignalClusterView;
import com.android.systemui.statusbar.StatusBarIconView;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.DateView;
import com.android.systemui.statusbar.policy.IntruderAlertView;
import com.android.systemui.statusbar.policy.LocationController;
import com.android.systemui.statusbar.policy.NetworkController;
import com.android.systemui.statusbar.policy.NotificationRowLayout;
import com.android.systemui.statusbar.policy.OnSizeChangedListener;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

public class PhoneStatusBar extends BaseStatusBar {
    public static final String ACTION_STATUSBAR_START = "com.android.internal.policy.statusbar.START";
    public static final boolean CHATTY = false;
    private static final boolean CLOSE_PANEL_WHEN_EMPTIED = true;
    public static final boolean DEBUG = false;
    private static final boolean DIM_BEHIND_EXPANDED_PANEL = true;
    public static final boolean DUMPTRUCK = true;
    private static final int HIDE_ICONS_BELOW_SCORE = -10;
    private static final int INTRUDER_ALERT_DECAY_MS = 0;
    private static final int MSG_CLOSE_NOTIFICATION_PANEL = 1001;
    private static final int MSG_OPEN_NOTIFICATION_PANEL = 1000;
    private static final int NOTIFICATION_PRIORITY_MULTIPLIER = 10;
    private static final boolean SHOW_CARRIER_LABEL = true;
    public static final boolean SPEW = false;
    static final String TAG = "PhoneStatusBar";
    int[] mAbsPos;
    float mAnimAccel;
    long mAnimLastTimeNanos;
    float mAnimVel;
    float mAnimY;
    boolean mAnimating;
    boolean mAnimatingReveal;
    final Runnable mAnimationCallback;
    BatteryController mBatteryController;
    private BroadcastReceiver mBroadcastReceiver;
    private TextView mCarrierLabel;
    private int mCarrierLabelHeight;
    private boolean mCarrierLabelVisible;
    Choreographer mChoreographer;
    View mClearButton;
    private OnClickListener mClearButtonListener;
    CloseDragHandle mCloseView;
    private int mCloseViewHeight;
    boolean mClosing;
    private float mCollapseAccelPx;
    private float mCollapseMinDisplayFraction;
    DateView mDateView;
    int mDisabled;
    Display mDisplay;
    DisplayMetrics mDisplayMetrics;
    int mEdgeBorder;
    private float mExpandAccelPx;
    private float mExpandMinDisplayFraction;
    boolean mExpanded;
    View mExpandedContents;
    boolean mExpandedVisible;
    private float mFlingCollapseMinVelocityPx;
    private float mFlingExpandMinVelocityPx;
    private float mFlingGestureMaxOutputVelocityPx;
    private float mFlingGestureMaxXVelocityPx;
    float mFlingVelocity;
    int mFlingY;
    OnFocusChangeListener mFocusChangeListener;
    OnTouchListener mHomeSearchActionListener;
    int mIconHPadding;
    PhoneStatusBarPolicy mIconPolicy;
    int mIconSize;
    LinearLayout mIcons;
    private IntruderAlertView mIntruderAlertView;
    private AnimatorSet mLightsOnAnimation;
    private AnimatorSet mLightsOutAnimation;
    LocationController mLocationController;
    private final AnimatorListener mMakeIconsInvisible;
    View mMoreIcon;
    int mNaturalBarHeight;
    private NavigationBarView mNavigationBarView;
    private int mNavigationIconHints;
    NetworkController mNetworkController;
    IconMerger mNotificationIcons;
    View mNotificationPanel;
    final Rect mNotificationPanelBackgroundPadding;
    int mNotificationPanelGravity;
    boolean mNotificationPanelIsFullScreenWidth;
    int mNotificationPanelMarginBottomPx;
    int mNotificationPanelMarginLeftPx;
    int mNotificationPanelMinHeight;
    private boolean mPanelSlightlyVisible;
    private final Runnable mPerformFling;
    private final Runnable mPerformSelfExpandFling;
    int mPixelFormat;
    int[] mPositionTmp;
    Runnable mPostCollapseCleanup;
    Object mQueueLock;
    private OnClickListener mRecentsClickListener;
    final Runnable mRevealAnimationCallback;
    RotationToggle mRotationButton;
    ScrollView mScrollView;
    private float mSelfCollapseVelocityPx;
    private float mSelfExpandVelocityPx;
    View mSettingsButton;
    private OnClickListener mSettingsButtonListener;
    private int mShowSearchHoldoff;
    private Runnable mShowSearchPanel;
    private final Runnable mStartRevealAnimation;
    Runnable mStartTracing;
    PhoneStatusBarView mStatusBarView;
    StatusBarWindowView mStatusBarWindow;
    LinearLayout mStatusIcons;
    Runnable mStopTracing;
    int mSystemUiVisibility;
    private Ticker mTicker;
    private View mTickerView;
    private boolean mTicking;
    AnimationListener mTickingDoneListener;
    boolean mTracking;
    int mTrackingPosition;
    VelocityTracker mVelocityTracker;
    int mViewDelta;
    IWindowManager mWindowManager;

    class AnonymousClass_13 extends AnimatorListenerAdapter {
        final /* synthetic */ View val$nlo;

        AnonymousClass_13(View view) {
            this.val$nlo = view;
        }

        public void onAnimationEnd(Animator _a) {
            this.val$nlo.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
        }
    }

    private class ExpandedDialog extends Dialog {
        ExpandedDialog(Context context) {
            super(context, 16973840);
        }

        public boolean dispatchKeyEvent(KeyEvent event) {
            boolean down = event.getAction() == 0 ? true : SPEW;
            switch (event.getKeyCode()) {
                case CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL:
                    if (down) {
                        return SHOW_CARRIER_LABEL;
                    }
                    PhoneStatusBar.this.animateCollapse();
                    return SHOW_CARRIER_LABEL;
                default:
                    return super.dispatchKeyEvent(event);
            }
        }
    }

    private static class FastColorDrawable extends Drawable {
        private final int mColor;

        public FastColorDrawable(int color) {
            this.mColor = -16777216 | color;
        }

        public void draw(Canvas canvas) {
            canvas.drawColor(this.mColor, Mode.SRC);
        }

        public int getOpacity() {
            return -1;
        }

        public void setAlpha(int alpha) {
        }

        public void setBounds(int left, int top, int right, int bottom) {
        }

        public void setBounds(Rect bounds) {
        }

        public void setColorFilter(ColorFilter cf) {
        }
    }

    private class H extends H {
        private H() {
            super();
        }

        public void handleMessage(Message m) {
            super.handleMessage(m);
            switch (m.what) {
                case MSG_OPEN_NOTIFICATION_PANEL:
                    PhoneStatusBar.this.animateExpand();
                case MSG_CLOSE_NOTIFICATION_PANEL:
                    PhoneStatusBar.this.animateCollapse();
                case 1026:
                    PhoneStatusBar.this.setIntruderAlertVisibility(SHOW_CARRIER_LABEL);
                case 1027:
                    PhoneStatusBar.this.setIntruderAlertVisibility(SPEW);
                    PhoneStatusBar.this.mCurrentlyIntrudingNotification = null;
                default:
                    break;
            }
        }
    }

    private class MyTicker extends Ticker {
        MyTicker(Context context, View sb) {
            super(context, sb);
        }

        public void tickerDone() {
            PhoneStatusBar.this.mIcons.setVisibility(INTRUDER_ALERT_DECAY_MS);
            PhoneStatusBar.this.mTickerView.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
            PhoneStatusBar.this.mIcons.startAnimation(PhoneStatusBar.this.loadAnim(17432621, null));
            PhoneStatusBar.this.mTickerView.startAnimation(PhoneStatusBar.this.loadAnim(17432623, PhoneStatusBar.this.mTickingDoneListener));
        }

        public void tickerHalting() {
            PhoneStatusBar.this.mIcons.setVisibility(INTRUDER_ALERT_DECAY_MS);
            PhoneStatusBar.this.mTickerView.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
            PhoneStatusBar.this.mIcons.startAnimation(PhoneStatusBar.this.loadAnim(17432576, null));
            PhoneStatusBar.this.mTickerView.startAnimation(PhoneStatusBar.this.loadAnim(17432577, PhoneStatusBar.this.mTickingDoneListener));
        }

        public void tickerStarting() {
            PhoneStatusBar.this.mTicking = SHOW_CARRIER_LABEL;
            PhoneStatusBar.this.mIcons.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
            PhoneStatusBar.this.mTickerView.setVisibility(INTRUDER_ALERT_DECAY_MS);
            PhoneStatusBar.this.mTickerView.startAnimation(PhoneStatusBar.this.loadAnim(17432625, null));
            PhoneStatusBar.this.mIcons.startAnimation(PhoneStatusBar.this.loadAnim(17432626, null));
        }
    }

    private class NotificationClicker implements OnClickListener {
        private int mId;
        private PendingIntent mIntent;
        private String mPkg;
        private String mTag;

        NotificationClicker(PendingIntent intent, String pkg, String tag, int id) {
            this.mIntent = intent;
            this.mPkg = pkg;
            this.mTag = tag;
            this.mId = id;
        }

        public void onClick(View v) {
            try {
                ActivityManagerNative.getDefault().resumeAppSwitches();
                ActivityManagerNative.getDefault().dismissKeyguardOnNextActivity();
            } catch (RemoteException e) {
            }
            if (this.mIntent != null) {
                int[] pos = new int[2];
                v.getLocationOnScreen(pos);
                Intent overlay = new Intent();
                overlay.setSourceBounds(new Rect(pos[0], pos[1], pos[0] + v.getWidth(), pos[1] + v.getHeight()));
                try {
                    this.mIntent.send(PhoneStatusBar.this.mContext, INTRUDER_ALERT_DECAY_MS, overlay);
                } catch (CanceledException e2) {
                    Slog.w(TAG, "Sending contentIntent failed: " + e2);
                }
                KeyguardManager kgm = (KeyguardManager) PhoneStatusBar.this.mContext.getSystemService("keyguard");
                if (kgm != null) {
                    kgm.exitKeyguardSecurely(null);
                }
            }
            try {
                PhoneStatusBar.this.mBarService.onNotificationClick(this.mPkg, this.mTag, this.mId);
            } catch (RemoteException e3) {
            }
            PhoneStatusBar.this.animateCollapse();
            PhoneStatusBar.this.mHandler.sendEmptyMessage(1027);
        }
    }

    public PhoneStatusBar() {
        this.mNaturalBarHeight = -1;
        this.mIconSize = -1;
        this.mIconHPadding = -1;
        this.mQueueLock = new Object();
        this.mNotificationPanelBackgroundPadding = new Rect();
        this.mCarrierLabelVisible = false;
        this.mPositionTmp = new int[2];
        this.mNavigationBarView = null;
        this.mAnimatingReveal = false;
        this.mAbsPos = new int[2];
        this.mPostCollapseCleanup = null;
        this.mDisabled = 0;
        this.mSystemUiVisibility = 0;
        this.mDisplayMetrics = new DisplayMetrics();
        this.mNavigationIconHints = 0;
        this.mMakeIconsInvisible = new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                if (PhoneStatusBar.this.mIcons.getAlpha() == 0.0f) {
                    Slog.d(TAG, "makeIconsInvisible");
                    PhoneStatusBar.this.mIcons.setVisibility(CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL);
                }
            }
        };
        this.mStartRevealAnimation = new Runnable() {
            public void run() {
                PhoneStatusBar.this.mAnimAccel = PhoneStatusBar.this.mExpandAccelPx;
                PhoneStatusBar.this.mAnimVel = PhoneStatusBar.this.mFlingExpandMinVelocityPx;
                PhoneStatusBar.this.mAnimY = (float) PhoneStatusBar.this.getStatusBarHeight();
                PhoneStatusBar.this.updateExpandedViewPos((int) PhoneStatusBar.this.mAnimY);
                PhoneStatusBar.this.mAnimating = true;
                PhoneStatusBar.this.mAnimatingReveal = true;
                PhoneStatusBar.this.resetLastAnimTime();
                PhoneStatusBar.this.mChoreographer.removeCallbacks(1, PhoneStatusBar.this.mAnimationCallback, null);
                PhoneStatusBar.this.mChoreographer.removeCallbacks(1, PhoneStatusBar.this.mRevealAnimationCallback, null);
                PhoneStatusBar.this.mChoreographer.postCallback(1, PhoneStatusBar.this.mRevealAnimationCallback, null);
            }
        };
        this.mPerformSelfExpandFling = new Runnable() {
            public void run() {
                PhoneStatusBar.this.performFling(INTRUDER_ALERT_DECAY_MS, PhoneStatusBar.this.mSelfExpandVelocityPx, SHOW_CARRIER_LABEL);
            }
        };
        this.mPerformFling = new Runnable() {
            public void run() {
                PhoneStatusBar.this.performFling(PhoneStatusBar.this.mFlingY + PhoneStatusBar.this.mViewDelta, PhoneStatusBar.this.mFlingVelocity, SPEW);
            }
        };
        this.mRecentsClickListener = new OnClickListener() {
            public void onClick(View v) {
                PhoneStatusBar.this.toggleRecentApps();
            }
        };
        this.mShowSearchHoldoff = 0;
        this.mShowSearchPanel = new Runnable() {
            public void run() {
                PhoneStatusBar.this.showSearchPanel();
            }
        };
        this.mHomeSearchActionListener = new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case INTRUDER_ALERT_DECAY_MS:
                        if (!(PhoneStatusBar.this.shouldDisableNavbarGestures() || PhoneStatusBar.this.inKeyguardRestrictedInputMode())) {
                            PhoneStatusBar.this.mHandler.removeCallbacks(PhoneStatusBar.this.mShowSearchPanel);
                            PhoneStatusBar.this.mHandler.postDelayed(PhoneStatusBar.this.mShowSearchPanel, (long) PhoneStatusBar.this.mShowSearchHoldoff);
                        }
                        break;
                    case CommandQueue.FLAG_EXCLUDE_SEARCH_PANEL:
                    case RecentsCallback.SWIPE_DOWN:
                        PhoneStatusBar.this.mHandler.removeCallbacks(PhoneStatusBar.this.mShowSearchPanel);
                        break;
                }
                return SPEW;
            }
        };
        this.mAnimationCallback = new Runnable() {
            public void run() {
                PhoneStatusBar.this.doAnimation(PhoneStatusBar.this.mChoreographer.getFrameTimeNanos());
            }
        };
        this.mRevealAnimationCallback = new Runnable() {
            public void run() {
                PhoneStatusBar.this.doRevealAnimation(PhoneStatusBar.this.mChoreographer.getFrameTimeNanos());
            }
        };
        this.mFocusChangeListener = new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                v.setSelected(hasFocus);
            }
        };
        this.mTickingDoneListener = new AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                PhoneStatusBar.this.mTicking = SPEW;
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        };
        this.mClearButtonListener = new OnClickListener() {

            class AnonymousClass_1 implements Runnable {
                final /* synthetic */ ArrayList val$snapshot;

                class AnonymousClass_2 implements Runnable {
                    final /* synthetic */ View val$_v;
                    final /* synthetic */ int val$velocity;

                    AnonymousClass_2(View view, int i) {
                        this.val$_v = view;
                        this.val$velocity = i;
                    }

                    public void run() {
                        AnonymousClass_1.this.this$1.this$0.mPile.dismissRowAnimated(this.val$_v, this.val$velocity);
                    }
                }

                AnonymousClass_1(ArrayList arrayList) {
                    this.val$snapshot = arrayList;
                }

                public void run() {
                    int currentDelay = 140;
                    int totalDelay = INTRUDER_ALERT_DECAY_MS;
                    AnonymousClass_19.this.this$0.mPile.setViewRemoval(SPEW);
                    AnonymousClass_19.this.this$0.mPostCollapseCleanup = new Runnable() {
                        public void run() {
                            try {
                                AnonymousClass_1.this.this$1.this$0.mPile.setViewRemoval(SHOW_CARRIER_LABEL);
                                AnonymousClass_1.this.this$1.this$0.mBarService.onClearAllNotifications();
                            } catch (Exception e) {
                            }
                        }
                    };
                    int velocity = ((View) this.val$snapshot.get(INTRUDER_ALERT_DECAY_MS)).getWidth() * 8;
                    Iterator i$ = this.val$snapshot.iterator();
                    while (i$.hasNext()) {
                        AnonymousClass_19.this.this$0.mHandler.postDelayed(new AnonymousClass_2((View) i$.next(), velocity), (long) totalDelay);
                        currentDelay = Math.max(50, currentDelay - 10);
                        totalDelay += currentDelay;
                    }
                    AnonymousClass_19.this.this$0.mHandler.postDelayed(new Runnable() {
                        public void run() {
                            AnonymousClass_1.this.this$1.this$0.animateCollapse(INTRUDER_ALERT_DECAY_MS);
                        }
                    }, (long) (totalDelay + 225));
                }
            }

            final int mini(int a, int b) {
                return b > a ? a : b;
            }

            public void onClick(View v) {
                synchronized (PhoneStatusBar.this.mNotificationData) {
                    int numChildren = PhoneStatusBar.this.mPile.getChildCount();
                    int scrollTop = PhoneStatusBar.this.mScrollView.getScrollY();
                    int scrollBottom = scrollTop + PhoneStatusBar.this.mScrollView.getHeight();
                    ArrayList<View> snapshot = new ArrayList(numChildren);
                    int i = INTRUDER_ALERT_DECAY_MS;
                    while (i < numChildren) {
                        View child = PhoneStatusBar.this.mPile.getChildAt(i);
                        if (PhoneStatusBar.this.mPile.canChildBeDismissed(child) && child.getBottom() > scrollTop && child.getTop() < scrollBottom) {
                            snapshot.add(child);
                        }
                        i++;
                    }
                    if (snapshot.isEmpty()) {
                        PhoneStatusBar.this.animateCollapse(INTRUDER_ALERT_DECAY_MS);
                    } else {
                        new Thread(new AnonymousClass_1(snapshot)).start();
                    }
                }
            }
        };
        this.mSettingsButtonListener = new OnClickListener() {
            public void onClick(View v) {
                if (PhoneStatusBar.this.isDeviceProvisioned()) {
                    try {
                        ActivityManagerNative.getDefault().dismissKeyguardOnNextActivity();
                    } catch (RemoteException e) {
                    }
                    v.getContext().startActivity(new Intent("android.settings.SETTINGS").setFlags(268435456));
                    PhoneStatusBar.this.animateCollapse();
                }
            }
        };
        this.mBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if ("android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(action) || "android.intent.action.SCREEN_OFF".equals(action)) {
                    int flags = INTRUDER_ALERT_DECAY_MS;
                    if ("android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(action)) {
                        String reason = intent.getStringExtra("reason");
                        if (reason != null && reason.equals(BaseStatusBar.SYSTEM_DIALOG_REASON_RECENT_APPS)) {
                            flags = 0 | 2;
                        }
                    }
                    PhoneStatusBar.this.animateCollapse(flags);
                } else if ("android.intent.action.CONFIGURATION_CHANGED".equals(action)) {
                    PhoneStatusBar.this.updateResources();
                    PhoneStatusBar.this.repositionNavigationBar();
                    PhoneStatusBar.this.updateExpandedViewPos(BaseStatusBar.EXPANDED_LEAVE_ALONE);
                }
            }
        };
        this.mStartTracing = new Runnable() {
            public void run() {
                PhoneStatusBar.this.vibrate();
                SystemClock.sleep(250);
                Slog.d(TAG, "startTracing");
                Debug.startMethodTracing("/data/statusbar-traces/trace");
                PhoneStatusBar.this.mHandler.postDelayed(PhoneStatusBar.this.mStopTracing, 10000);
            }
        };
        this.mStopTracing = new Runnable() {
            public void run() {
                Debug.stopMethodTracing();
                Slog.d(TAG, "stopTracing");
                PhoneStatusBar.this.vibrate();
            }
        };
    }

    private void addIntruderView() {
        int height = getStatusBarHeight();
        LayoutParams lp = new LayoutParams(-1, -2, 2014, 8520488, -3);
        lp.gravity = 55;
        lp.setTitle("IntruderAlert");
        lp.packageName = this.mContext.getPackageName();
        lp.windowAnimations = 2131623949;
        WindowManagerImpl.getDefault().addView(this.mIntruderAlertView, lp);
    }

    private void addNavigationBar() {
        if (this.mNavigationBarView != null) {
            prepareNavigationBarView();
            WindowManagerImpl.getDefault().addView(this.mNavigationBarView, getNavigationBarLayoutParams());
        }
    }

    private void addStatusBarWindow() {
        LayoutParams lp = new LayoutParams(-1, getStatusBarHeight(), 2000, 8388680, -3);
        lp.flags |= 16777216;
        lp.gravity = getStatusBarGravity();
        lp.setTitle("StatusBar");
        lp.packageName = this.mContext.getPackageName();
        makeStatusBarView();
        WindowManagerImpl.getDefault().addView(this.mStatusBarWindow, lp);
    }

    private boolean areLightsOn() {
        return (this.mSystemUiVisibility & 1) == 0 ? SHOW_CARRIER_LABEL : SPEW;
    }

    private int getCloseViewHeight() {
        return this.mCloseViewHeight;
    }

    private LayoutParams getNavigationBarLayoutParams() {
        LayoutParams lp = new LayoutParams(-1, -1, 2019, 8388712, -1);
        if (ActivityManager.isHighEndGfx(this.mDisplay)) {
            lp.flags |= 16777216;
        }
        lp.setTitle("NavigationBar");
        lp.windowAnimations = 0;
        return lp;
    }

    private static void getNinePatchPadding(Drawable d, Rect outPadding) {
        if (d instanceof NinePatchDrawable) {
            ((NinePatchDrawable) d).getPadding(outPadding);
        }
    }

    private Animation loadAnim(int id, AnimationListener listener) {
        Animation anim = AnimationUtils.loadAnimation(this.mContext, id);
        if (listener != null) {
            anim.setAnimationListener(listener);
        }
        return anim;
    }

    private void loadNotificationShade() {
        if (this.mPile != null) {
            int N = this.mNotificationData.size();
            ArrayList<View> toShow = new ArrayList();
            boolean provisioned = isDeviceProvisioned();
            int i = INTRUDER_ALERT_DECAY_MS;
            while (i < N) {
                Entry ent = this.mNotificationData.get(N - i - 1);
                if (provisioned || showNotificationEvenIfUnprovisioned(ent.notification)) {
                    toShow.add(ent.row);
                }
                i++;
            }
            ArrayList<View> toRemove = new ArrayList();
            i = INTRUDER_ALERT_DECAY_MS;
            while (i < this.mPile.getChildCount()) {
                View child = this.mPile.getChildAt(i);
                if (!toShow.contains(child)) {
                    toRemove.add(child);
                }
                i++;
            }
            Iterator i$ = toRemove.iterator();
            while (i$.hasNext()) {
                this.mPile.removeView((View) i$.next());
            }
            i = INTRUDER_ALERT_DECAY_MS;
            while (i < toShow.size()) {
                View v = (View) toShow.get(i);
                if (v.getParent() == null) {
                    this.mPile.addView(v, i);
                }
                i++;
            }
            this.mSettingsButton.setEnabled(isDeviceProvisioned());
        }
    }

    private void makeExpandedVisible(boolean revealAfterDraw) {
        if (!this.mExpandedVisible) {
            this.mExpandedVisible = true;
            this.mPile.setLayoutTransitionsEnabled(SHOW_CARRIER_LABEL);
            if (this.mNavigationBarView != null) {
                this.mNavigationBarView.setSlippery(SHOW_CARRIER_LABEL);
            }
            updateCarrierLabelVisibility(SHOW_CARRIER_LABEL);
            updateExpandedViewPos(BaseStatusBar.EXPANDED_LEAVE_ALONE);
            LayoutParams lp = (LayoutParams) this.mStatusBarWindow.getLayoutParams();
            lp.flags &= -9;
            lp.flags |= 131072;
            lp.height = -1;
            WindowManagerImpl.getDefault().updateViewLayout(this.mStatusBarWindow, lp);
            if (revealAfterDraw) {
                this.mHandler.post(this.mStartRevealAnimation);
            }
            visibilityChanged(SHOW_CARRIER_LABEL);
        }
    }

    private void notifyUiVisibilityChanged() {
        try {
            this.mWindowManager.statusBarVisibilityChanged(this.mSystemUiVisibility);
        } catch (RemoteException e) {
        }
    }

    private void prepareNavigationBarView() {
        this.mNavigationBarView.reorient();
        this.mNavigationBarView.getRecentsButton().setOnClickListener(this.mRecentsClickListener);
        this.mNavigationBarView.getRecentsButton().setOnTouchListener(this.mRecentsPanel);
        this.mNavigationBarView.getHomeButton().setOnTouchListener(this.mHomeSearchActionListener);
        updateSearchPanel();
    }

    private void reloadAllNotificationIcons() {
        if (this.mNotificationIcons != null) {
            this.mNotificationIcons.removeAllViews();
            updateNotificationIcons();
        }
    }

    private void repositionNavigationBar() {
        if (this.mNavigationBarView != null) {
            prepareNavigationBarView();
            WindowManagerImpl.getDefault().updateViewLayout(this.mNavigationBarView, getNavigationBarLayoutParams());
        }
    }

    static final float saturate(float a) {
        if (a < 0.0f) {
            return 0.0f;
        }
        return a > 1.0f ? 1.0f : a;
    }

    private void setIntruderAlertVisibility(boolean vis) {
    }

    private void setPileLayers(int layerType) {
        int count = this.mPile.getChildCount();
        int i;
        switch (layerType) {
            case INTRUDER_ALERT_DECAY_MS:
                i = INTRUDER_ALERT_DECAY_MS;
                while (i < count) {
                    this.mPile.getChildAt(i).setLayerType(layerType, null);
                    i++;
                }
            case CommandQueue.FLAG_EXCLUDE_RECENTS_PANEL:
                int[] location = new int[2];
                this.mNotificationPanel.getLocationInWindow(location);
                int left = location[0];
                int top = location[1];
                int right = left + this.mNotificationPanel.getWidth();
                int bottom = top + getExpandedViewMaxHeight();
                Rect childBounds = new Rect();
                i = INTRUDER_ALERT_DECAY_MS;
                while (i < count) {
                    View view = this.mPile.getChildAt(i);
                    view.getLocationInWindow(location);
                    childBounds.set(location[0], location[1], location[0] + view.getWidth(), location[1] + view.getHeight());
                    if (childBounds.intersects(left, top, right, bottom)) {
                        view.setLayerType(layerType, null);
                    }
                    i++;
                }
            default:
                break;
        }
    }

    private void setStatusBarLowProfile(boolean lightsOut) {
        if (this.mLightsOutAnimation == null) {
            View notifications = this.mStatusBarView.findViewById(R.id.notification_icon_area);
            View systemIcons = this.mStatusBarView.findViewById(R.id.statusIcons);
            View signal = this.mStatusBarView.findViewById(R.id.signal_cluster);
            View battery = this.mStatusBarView.findViewById(R.id.battery);
            View clock = this.mStatusBarView.findViewById(R.id.clock);
            this.mLightsOutAnimation = new AnimatorSet();
            AnimatorSet animatorSet = this.mLightsOutAnimation;
            Animator[] animatorArr = new Animator[5];
            animatorArr[0] = ObjectAnimator.ofFloat(notifications, View.ALPHA, new float[]{0.0f});
            animatorArr[1] = ObjectAnimator.ofFloat(systemIcons, View.ALPHA, new float[]{0.0f});
            animatorArr[2] = ObjectAnimator.ofFloat(signal, View.ALPHA, new float[]{0.0f});
            animatorArr[3] = ObjectAnimator.ofFloat(battery, View.ALPHA, new float[]{0.5f});
            animatorArr[4] = ObjectAnimator.ofFloat(clock, View.ALPHA, new float[]{0.5f});
            animatorSet.playTogether(animatorArr);
            this.mLightsOutAnimation.setDuration(750);
            this.mLightsOnAnimation = new AnimatorSet();
            animatorSet = this.mLightsOnAnimation;
            animatorArr = new Animator[5];
            animatorArr[0] = ObjectAnimator.ofFloat(notifications, View.ALPHA, new float[]{1.0f});
            animatorArr[1] = ObjectAnimator.ofFloat(systemIcons, View.ALPHA, new float[]{1.0f});
            animatorArr[2] = ObjectAnimator.ofFloat(signal, View.ALPHA, new float[]{1.0f});
            animatorArr[3] = ObjectAnimator.ofFloat(battery, View.ALPHA, new float[]{1.0f});
            animatorArr[4] = ObjectAnimator.ofFloat(clock, View.ALPHA, new float[]{1.0f});
            animatorSet.playTogether(animatorArr);
            this.mLightsOnAnimation.setDuration(250);
        }
        this.mLightsOutAnimation.cancel();
        this.mLightsOnAnimation.cancel();
        (lightsOut ? this.mLightsOutAnimation : this.mLightsOnAnimation).start();
        setAreThereNotifications();
    }

    private void trackMovement(MotionEvent event) {
        float deltaX = event.getRawX() - event.getX();
        float deltaY = event.getRawY() - event.getY();
        event.offsetLocation(deltaX, deltaY);
        this.mVelocityTracker.addMovement(event);
        event.offsetLocation(-deltaX, -deltaY);
    }

    private void updateShowSearchHoldoff() {
        this.mShowSearchHoldoff = this.mContext.getResources().getInteger(R.integer.config_show_search_delay);
    }

    public static String viewInfo(View v) {
        return "[(" + v.getLeft() + "," + v.getTop() + ")(" + v.getRight() + "," + v.getBottom() + ") " + v.getWidth() + "x" + v.getHeight() + "]";
    }

    public void addIcon(String slot, int index, int viewIndex, StatusBarIcon icon) {
        StatusBarIconView view = new StatusBarIconView(this.mContext, slot, null);
        view.set(icon);
        this.mStatusIcons.addView(view, viewIndex, new LinearLayout.LayoutParams(this.mIconSize, this.mIconSize));
    }

    public void addNotification(IBinder key, StatusBarNotification notification) {
        Slog.d(TAG, "addNotification score=" + notification.score);
        if (addNotificationViews(key, notification) != null) {
            try {
                boolean immersive = ActivityManagerNative.getDefault().isTopActivityImmersive();
            } catch (RemoteException e) {
            }
            if (notification.notification.fullScreenIntent != null) {
                Slog.d(TAG, "Notification has fullScreenIntent; sending fullScreenIntent");
                try {
                    notification.notification.fullScreenIntent.send();
                } catch (CanceledException e2) {
                }
            } else if (this.mCurrentlyIntrudingNotification == null) {
                tick(null, notification, SHOW_CARRIER_LABEL);
            }
            setAreThereNotifications();
            updateExpandedViewPos(BaseStatusBar.EXPANDED_LEAVE_ALONE);
        }
    }

    public void animateCollapse() {
        animateCollapse(INTRUDER_ALERT_DECAY_MS);
    }

    public void animateCollapse(int flags) {
        animateCollapse(flags, 1.0f);
    }

    public void animateCollapse(int flags, float velocityMultiplier) {
        if ((flags & 2) == 0) {
            this.mHandler.removeMessages(1021);
            this.mHandler.sendEmptyMessage(1021);
        }
        if ((flags & 1) == 0) {
            this.mHandler.removeMessages(1025);
            this.mHandler.sendEmptyMessage(1025);
        }
        if (this.mExpandedVisible) {
            int y;
            if (this.mAnimating) {
                y = (int) this.mAnimY;
            } else {
                y = getExpandedViewMaxHeight() - 1;
            }
            this.mExpanded = true;
            prepareTracking(y, SPEW);
            performFling(y, (-this.mSelfCollapseVelocityPx) * velocityMultiplier, SHOW_CARRIER_LABEL);
        }
    }

    public void animateExpand() {
        if ((this.mDisabled & 65536) == 0 && !this.mExpanded) {
            prepareTracking(INTRUDER_ALERT_DECAY_MS, SHOW_CARRIER_LABEL);
            this.mHandler.post(this.mPerformSelfExpandFling);
        }
    }

    public void createAndAddWindows() {
        addStatusBarWindow();
    }

    protected H createHandler() {
        return new H(null);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void disable(int r12_state) {
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.PhoneStatusBar.disable(int):void");
        /*
        r11 = this;
        r10 = 8388608; // 0x800000 float:1.17549435E-38 double:4.144523E-317;
        r6 = 1048576; // 0x100000 float:1.469368E-39 double:5.180654E-318;
        r9 = 524288; // 0x80000 float:7.34684E-40 double:2.590327E-318;
        r8 = 131072; // 0x20000 float:1.83671E-40 double:6.47582E-319;
        r7 = 65536; // 0x10000 float:9.18355E-41 double:3.2379E-319;
        r2 = r11.mDisabled;
        r0 = r12 ^ r2;
        r11.mDisabled = r12;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r4 = "disable: < ";
        r1.append(r4);
        r4 = r12 & r7;
        if (r4 == 0) goto L_0x0165;
    L_0x001e:
        r4 = "EXPAND";
    L_0x0020:
        r1.append(r4);
        r4 = r0 & r7;
        if (r4 == 0) goto L_0x0169;
    L_0x0027:
        r4 = "* ";
    L_0x0029:
        r1.append(r4);
        r4 = r12 & r8;
        if (r4 == 0) goto L_0x016d;
    L_0x0030:
        r4 = "ICONS";
    L_0x0032:
        r1.append(r4);
        r4 = r0 & r8;
        if (r4 == 0) goto L_0x0171;
    L_0x0039:
        r4 = "* ";
    L_0x003b:
        r1.append(r4);
        r4 = 262144; // 0x40000 float:3.67342E-40 double:1.295163E-318;
        r4 = r4 & r12;
        if (r4 == 0) goto L_0x0175;
    L_0x0043:
        r4 = "ALERTS";
    L_0x0045:
        r1.append(r4);
        r4 = 262144; // 0x40000 float:3.67342E-40 double:1.295163E-318;
        r4 = r4 & r0;
        if (r4 == 0) goto L_0x0179;
    L_0x004d:
        r4 = "* ";
    L_0x004f:
        r1.append(r4);
        r4 = r12 & r9;
        if (r4 == 0) goto L_0x017d;
    L_0x0056:
        r4 = "TICKER";
    L_0x0058:
        r1.append(r4);
        r4 = r0 & r9;
        if (r4 == 0) goto L_0x0181;
    L_0x005f:
        r4 = "* ";
    L_0x0061:
        r1.append(r4);
        r4 = r12 & r6;
        if (r4 == 0) goto L_0x0185;
    L_0x0068:
        r4 = "SYSTEM_INFO";
    L_0x006a:
        r1.append(r4);
        r4 = r0 & r6;
        if (r4 == 0) goto L_0x0189;
    L_0x0071:
        r4 = "* ";
    L_0x0073:
        r1.append(r4);
        r4 = 4194304; // 0x400000 float:5.877472E-39 double:2.0722615E-317;
        r4 = r4 & r12;
        if (r4 == 0) goto L_0x018d;
    L_0x007b:
        r4 = "BACK";
    L_0x007d:
        r1.append(r4);
        r4 = 4194304; // 0x400000 float:5.877472E-39 double:2.0722615E-317;
        r4 = r4 & r0;
        if (r4 == 0) goto L_0x0191;
    L_0x0085:
        r4 = "* ";
    L_0x0087:
        r1.append(r4);
        r4 = 2097152; // 0x200000 float:2.938736E-39 double:1.0361308E-317;
        r4 = r4 & r12;
        if (r4 == 0) goto L_0x0195;
    L_0x008f:
        r4 = "HOME";
    L_0x0091:
        r1.append(r4);
        r4 = 2097152; // 0x200000 float:2.938736E-39 double:1.0361308E-317;
        r4 = r4 & r0;
        if (r4 == 0) goto L_0x0199;
    L_0x0099:
        r4 = "* ";
    L_0x009b:
        r1.append(r4);
        r4 = 16777216; // 0x1000000 float:2.3509887E-38 double:8.289046E-317;
        r4 = r4 & r12;
        if (r4 == 0) goto L_0x019d;
    L_0x00a3:
        r4 = "RECENT";
    L_0x00a5:
        r1.append(r4);
        r4 = 16777216; // 0x1000000 float:2.3509887E-38 double:8.289046E-317;
        r4 = r4 & r0;
        if (r4 == 0) goto L_0x01a1;
    L_0x00ad:
        r4 = "* ";
    L_0x00af:
        r1.append(r4);
        r4 = r12 & r10;
        if (r4 == 0) goto L_0x01a5;
    L_0x00b6:
        r4 = "CLOCK";
    L_0x00b8:
        r1.append(r4);
        r4 = r0 & r10;
        if (r4 == 0) goto L_0x01a9;
    L_0x00bf:
        r4 = "* ";
    L_0x00c1:
        r1.append(r4);
        r4 = ">";
        r1.append(r4);
        r4 = "PhoneStatusBar";
        r5 = r1.toString();
        android.util.Slog.d(r4, r5);
        r4 = r0 & r6;
        if (r4 == 0) goto L_0x011b;
    L_0x00d6:
        r4 = r11.mIcons;
        r4 = r4.animate();
        r4.cancel();
        r4 = r12 & r6;
        if (r4 == 0) goto L_0x01ad;
    L_0x00e3:
        r4 = r11.mTicking;
        if (r4 == 0) goto L_0x00ec;
    L_0x00e7:
        r4 = r11.mTicker;
        r4.halt();
    L_0x00ec:
        r4 = r11.mIcons;
        r4 = r4.animate();
        r5 = 0;
        r4 = r4.alpha(r5);
        r5 = r11.mNaturalBarHeight;
        r5 = (float) r5;
        r6 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r5 = r5 * r6;
        r4 = r4.translationY(r5);
        r5 = 175; // 0xaf float:2.45E-43 double:8.65E-322;
        r4 = r4.setDuration(r5);
        r5 = new android.view.animation.DecelerateInterpolator;
        r6 = 1069547520; // 0x3fc00000 float:1.5 double:5.28426686E-315;
        r5.<init>(r6);
        r4 = r4.setInterpolator(r5);
        r5 = r11.mMakeIconsInvisible;
        r4 = r4.setListener(r5);
        r4.start();
    L_0x011b:
        r4 = r0 & r10;
        if (r4 == 0) goto L_0x0127;
    L_0x011f:
        r4 = r12 & r10;
        if (r4 != 0) goto L_0x01e0;
    L_0x0123:
        r3 = 1;
    L_0x0124:
        r11.showClock(r3);
    L_0x0127:
        r4 = r0 & r7;
        if (r4 == 0) goto L_0x0132;
    L_0x012b:
        r4 = r12 & r7;
        if (r4 == 0) goto L_0x0132;
    L_0x012f:
        r11.animateCollapse();
    L_0x0132:
        r4 = 23068672; // 0x1600000 float:4.1142302E-38 double:1.13974383E-316;
        r4 = r4 & r0;
        if (r4 == 0) goto L_0x0153;
    L_0x0137:
        r4 = r11.mNavigationBarView;
        if (r4 == 0) goto L_0x0140;
    L_0x013b:
        r4 = r11.mNavigationBarView;
        r4.setDisabledFlags(r12);
    L_0x0140:
        r4 = 16777216; // 0x1000000 float:2.3509887E-38 double:8.289046E-317;
        r4 = r4 & r12;
        if (r4 == 0) goto L_0x0153;
    L_0x0145:
        r4 = r11.mHandler;
        r5 = 1021; // 0x3fd float:1.431E-42 double:5.044E-321;
        r4.removeMessages(r5);
        r4 = r11.mHandler;
        r5 = 1021; // 0x3fd float:1.431E-42 double:5.044E-321;
        r4.sendEmptyMessage(r5);
    L_0x0153:
        r4 = r0 & r8;
        if (r4 == 0) goto L_0x01f8;
    L_0x0157:
        r4 = r12 & r8;
        if (r4 == 0) goto L_0x01ec;
    L_0x015b:
        r4 = r11.mTicking;
        if (r4 == 0) goto L_0x01e3;
    L_0x015f:
        r4 = r11.mTicker;
        r4.halt();
    L_0x0164:
        return;
    L_0x0165:
        r4 = "expand";
        goto L_0x0020;
    L_0x0169:
        r4 = " ";
        goto L_0x0029;
    L_0x016d:
        r4 = "icons";
        goto L_0x0032;
    L_0x0171:
        r4 = " ";
        goto L_0x003b;
    L_0x0175:
        r4 = "alerts";
        goto L_0x0045;
    L_0x0179:
        r4 = " ";
        goto L_0x004f;
    L_0x017d:
        r4 = "ticker";
        goto L_0x0058;
    L_0x0181:
        r4 = " ";
        goto L_0x0061;
    L_0x0185:
        r4 = "system_info";
        goto L_0x006a;
    L_0x0189:
        r4 = " ";
        goto L_0x0073;
    L_0x018d:
        r4 = "back";
        goto L_0x007d;
    L_0x0191:
        r4 = " ";
        goto L_0x0087;
    L_0x0195:
        r4 = "home";
        goto L_0x0091;
    L_0x0199:
        r4 = " ";
        goto L_0x009b;
    L_0x019d:
        r4 = "recent";
        goto L_0x00a5;
    L_0x01a1:
        r4 = " ";
        goto L_0x00af;
    L_0x01a5:
        r4 = "clock";
        goto L_0x00b8;
    L_0x01a9:
        r4 = " ";
        goto L_0x00c1;
    L_0x01ad:
        r4 = r11.mIcons;
        r5 = 0;
        r4.setVisibility(r5);
        r4 = r11.mIcons;
        r4 = r4.animate();
        r5 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r4 = r4.alpha(r5);
        r5 = 0;
        r4 = r4.translationY(r5);
        r5 = 0;
        r4 = r4.setStartDelay(r5);
        r5 = new android.view.animation.DecelerateInterpolator;
        r6 = 1069547520; // 0x3fc00000 float:1.5 double:5.28426686E-315;
        r5.<init>(r6);
        r4 = r4.setInterpolator(r5);
        r5 = 175; // 0xaf float:2.45E-43 double:8.65E-322;
        r4 = r4.setDuration(r5);
        r4.start();
        goto L_0x011b;
    L_0x01e0:
        r3 = 0;
        goto L_0x0124;
    L_0x01e3:
        r4 = 0;
        r5 = 17432577; // 0x10a0001 float:2.53466E-38 double:8.6128374E-317;
        r11.setNotificationIconVisibility(r4, r5);
        goto L_0x0164;
    L_0x01ec:
        r4 = r11.mExpandedVisible;
        if (r4 != 0) goto L_0x0164;
    L_0x01f0:
        r4 = 1;
        r5 = 17432576; // 0x10a0000 float:2.5346597E-38 double:8.612837E-317;
        r11.setNotificationIconVisibility(r4, r5);
        goto L_0x0164;
    L_0x01f8:
        r4 = r0 & r9;
        if (r4 == 0) goto L_0x0164;
    L_0x01fc:
        r4 = r11.mTicking;
        if (r4 == 0) goto L_0x0164;
    L_0x0200:
        r4 = r12 & r9;
        if (r4 == 0) goto L_0x0164;
    L_0x0204:
        r4 = r11.mTicker;
        r4.halt();
        goto L_0x0164;
        */
    }

    public void dismissIntruder() {
        if (this.mCurrentlyIntrudingNotification != null) {
            try {
                this.mBarService.onNotificationClear(this.mCurrentlyIntrudingNotification.pkg, this.mCurrentlyIntrudingNotification.tag, this.mCurrentlyIntrudingNotification.id);
            } catch (RemoteException e) {
            }
        }
    }

    void doAnimation(long frameTimeNanos) {
        if (this.mAnimating) {
            incrementAnim(frameTimeNanos);
            if (this.mAnimY >= ((float) (getExpandedViewMaxHeight() - 1)) && !this.mClosing) {
                this.mAnimating = false;
                updateExpandedViewPos(BaseStatusBar.EXPANDED_FULL_OPEN);
                performExpand();
            } else if (this.mAnimY == 0.0f && this.mAnimAccel == 0.0f && this.mClosing) {
                this.mAnimating = false;
                performCollapse();
            } else {
                if (this.mAnimY < ((float) getStatusBarHeight()) && this.mClosing) {
                    this.mAnimY = 0.0f;
                    this.mAnimAccel = 0.0f;
                    this.mAnimVel = 0.0f;
                }
                updateExpandedViewPos((int) this.mAnimY);
                this.mChoreographer.postCallback(1, this.mAnimationCallback, null);
            }
        }
    }

    void doRevealAnimation(long frameTimeNanos) {
        int h = this.mNotificationPanelMinHeight;
        if (this.mAnimatingReveal && this.mAnimating && this.mAnimY < ((float) h)) {
            incrementAnim(frameTimeNanos);
            if (this.mAnimY >= ((float) h)) {
                this.mAnimY = (float) h;
                updateExpandedViewPos((int) this.mAnimY);
            } else {
                updateExpandedViewPos((int) this.mAnimY);
                this.mChoreographer.postCallback(1, this.mRevealAnimationCallback, null);
            }
        }
    }

    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        int N;
        int i;
        synchronized (this.mQueueLock) {
            String str;
            pw.println("Current Status Bar state:");
            pw.println("  mExpanded=" + this.mExpanded + ", mExpandedVisible=" + this.mExpandedVisible + ", mTrackingPosition=" + this.mTrackingPosition);
            pw.println("  mTicking=" + this.mTicking);
            pw.println("  mTracking=" + this.mTracking);
            StringBuilder append = new StringBuilder().append("  mNotificationPanel=");
            if (this.mNotificationPanel == null) {
                str = "null";
            } else {
                str = this.mNotificationPanel + " params=" + this.mNotificationPanel.getLayoutParams().debug("");
            }
            pw.println(append.append(str).toString());
            pw.println("  mAnimating=" + this.mAnimating + ", mAnimY=" + this.mAnimY + ", mAnimVel=" + this.mAnimVel + ", mAnimAccel=" + this.mAnimAccel);
            pw.println("  mAnimLastTimeNanos=" + this.mAnimLastTimeNanos);
            pw.println("  mAnimatingReveal=" + this.mAnimatingReveal + " mViewDelta=" + this.mViewDelta);
            pw.println("  mDisplayMetrics=" + this.mDisplayMetrics);
            pw.println("  mPile: " + viewInfo(this.mPile));
            pw.println("  mCloseView: " + viewInfo(this.mCloseView));
            pw.println("  mTickerView: " + viewInfo(this.mTickerView));
            pw.println("  mScrollView: " + viewInfo(this.mScrollView) + " scroll " + this.mScrollView.getScrollX() + "," + this.mScrollView.getScrollY());
        }
        pw.print("  mNavigationBarView=");
        if (this.mNavigationBarView == null) {
            pw.println("null");
        } else {
            this.mNavigationBarView.dump(fd, pw, args);
        }
        synchronized (this.mNotificationData) {
            N = this.mNotificationData.size();
            pw.println("  notification icons: " + N);
            i = INTRUDER_ALERT_DECAY_MS;
            while (i < N) {
                Entry e = this.mNotificationData.get(i);
                pw.println("    [" + i + "] key=" + e.key + " icon=" + e.icon);
                StatusBarNotification n = e.notification;
                pw.println("         pkg=" + n.pkg + " id=" + n.id + " score=" + n.score);
                pw.println("         notification=" + n.notification);
                pw.println("         tickerText=\"" + n.notification.tickerText + "\"");
                i++;
            }
        }
        N = this.mStatusIcons.getChildCount();
        pw.println("  system icons: " + N);
        i = INTRUDER_ALERT_DECAY_MS;
        while (i < N) {
            pw.println("    [" + i + "] icon=" + ((StatusBarIconView) this.mStatusIcons.getChildAt(i)));
            i++;
        }
        this.mNetworkController.dump(fd, pw, args);
    }

    protected int getExpandedViewMaxHeight() {
        return this.mDisplayMetrics.heightPixels - this.mNotificationPanelMarginBottomPx;
    }

    protected LayoutParams getRecentsLayoutParams(ViewGroup.LayoutParams layoutParams) {
        LayoutParams lp = new LayoutParams(layoutParams.width, layoutParams.height, 2014, 8519936, false ? -1 : -3);
        if (ActivityManager.isHighEndGfx(this.mDisplay)) {
            lp.flags |= 16777216;
        } else {
            lp.flags |= 2;
            lp.dimAmount = 0.75f;
        }
        lp.gravity = 83;
        lp.setTitle("RecentsPanel");
        lp.windowAnimations = 16974313;
        lp.softInputMode = 49;
        return lp;
    }

    protected LayoutParams getSearchLayoutParams(ViewGroup.LayoutParams layoutParams) {
        LayoutParams lp = new LayoutParams(-1, -1, 2024, 8519936, false ? -1 : -3);
        if (ActivityManager.isHighEndGfx(this.mDisplay)) {
            lp.flags |= 16777216;
        }
        lp.gravity = 83;
        lp.setTitle("SearchPanel");
        lp.windowAnimations = 16974313;
        lp.softInputMode = 49;
        return lp;
    }

    protected int getStatusBarGravity() {
        return 55;
    }

    public int getStatusBarHeight() {
        if (this.mNaturalBarHeight < 0) {
            this.mNaturalBarHeight = this.mContext.getResources().getDimensionPixelSize(17104906);
        }
        return this.mNaturalBarHeight;
    }

    protected void haltTicker() {
        this.mTicker.halt();
    }

    public void hideSearchPanel() {
        super.hideSearchPanel();
        LayoutParams lp = (LayoutParams) this.mNavigationBarView.getLayoutParams();
        lp.flags |= 32;
        WindowManagerImpl.getDefault().updateViewLayout(this.mNavigationBarView, lp);
    }

    void incrementAnim(long frameTimeNanos) {
        float t = ((float) Math.max(frameTimeNanos - this.mAnimLastTimeNanos, 0)) * 1.0E-9f;
        float y = this.mAnimY;
        float v = this.mAnimVel;
        float a = this.mAnimAccel;
        this.mAnimY = v * t + y + ((0.5f * a) * t) * t;
        this.mAnimVel = a * t + v;
        this.mAnimLastTimeNanos = frameTimeNanos;
    }

    boolean interceptTouchEvent(MotionEvent event) {
        if ((this.mDisabled & 65536) != 0) {
            return SPEW;
        }
        int action = event.getAction();
        int statusBarSize = getStatusBarHeight();
        int hitSize = statusBarSize * 2;
        int y = (int) event.getRawY();
        if (action == 0) {
            if (!areLightsOn()) {
                setLightsOn(SHOW_CARRIER_LABEL);
            }
            if (this.mExpanded) {
                this.mCloseView.getLocationOnScreen(this.mAbsPos);
                this.mViewDelta = this.mAbsPos[1] + getCloseViewHeight() + this.mNotificationPanelBackgroundPadding.top + this.mNotificationPanelBackgroundPadding.bottom - y;
            } else {
                this.mViewDelta = statusBarSize - y;
            }
            if ((!this.mExpanded && y < hitSize) || (this.mExpanded && y > getExpandedViewMaxHeight() - hitSize)) {
                int x = (int) event.getRawX();
                int edgeBorder = this.mEdgeBorder;
                if (x >= edgeBorder && x < this.mDisplayMetrics.widthPixels - edgeBorder) {
                    prepareTracking(y, !this.mExpanded ? SHOW_CARRIER_LABEL : SPEW);
                    trackMovement(event);
                }
            }
        } else if (this.mTracking) {
            trackMovement(event);
            if (action == 2) {
                this.mAnimatingReveal = false;
                updateExpandedViewPos(this.mViewDelta + y);
            } else {
                this.mVelocityTracker.computeCurrentVelocity(MSG_OPEN_NOTIFICATION_PANEL);
                float yVel = this.mVelocityTracker.getYVelocity();
                boolean negative = yVel < 0.0f ? SHOW_CARRIER_LABEL : SPEW;
                float xVel = this.mVelocityTracker.getXVelocity();
                if (xVel < 0.0f) {
                    xVel = -xVel;
                }
                if (xVel > this.mFlingGestureMaxXVelocityPx) {
                    xVel = this.mFlingGestureMaxXVelocityPx;
                }
                float vel = (float) Math.hypot((double) yVel, (double) xVel);
                if (vel > this.mFlingGestureMaxOutputVelocityPx) {
                    vel = this.mFlingGestureMaxOutputVelocityPx;
                }
                if (negative) {
                    vel = -vel;
                }
                if (this.mTrackingPosition == this.mNotificationPanelMinHeight) {
                    this.mFlingY = this.mTrackingPosition;
                    this.mViewDelta = 0;
                } else {
                    this.mFlingY = y;
                }
                this.mFlingVelocity = vel;
                this.mHandler.post(this.mPerformFling);
            }
        }
        return SPEW;
    }

    protected void loadDimens() {
        Resources res = this.mContext.getResources();
        this.mNaturalBarHeight = res.getDimensionPixelSize(17104906);
        int newIconSize = res.getDimensionPixelSize(17104910);
        int newIconHPadding = res.getDimensionPixelSize(R.dimen.status_bar_icon_padding);
        if (!(newIconHPadding == this.mIconHPadding && newIconSize == this.mIconSize)) {
            this.mIconHPadding = newIconHPadding;
            this.mIconSize = newIconSize;
        }
        this.mEdgeBorder = res.getDimensionPixelSize(R.dimen.status_bar_edge_ignore);
        this.mSelfExpandVelocityPx = res.getDimension(R.dimen.self_expand_velocity);
        this.mSelfCollapseVelocityPx = res.getDimension(R.dimen.self_collapse_velocity);
        this.mFlingExpandMinVelocityPx = res.getDimension(R.dimen.fling_expand_min_velocity);
        this.mFlingCollapseMinVelocityPx = res.getDimension(R.dimen.fling_collapse_min_velocity);
        this.mCollapseMinDisplayFraction = res.getFraction(R.dimen.collapse_min_display_fraction, 1, 1);
        this.mExpandMinDisplayFraction = res.getFraction(R.dimen.expand_min_display_fraction, 1, 1);
        this.mExpandAccelPx = res.getDimension(R.dimen.expand_accel);
        this.mCollapseAccelPx = res.getDimension(R.dimen.collapse_accel);
        this.mFlingGestureMaxXVelocityPx = res.getDimension(R.dimen.fling_gesture_max_x_velocity);
        this.mFlingGestureMaxOutputVelocityPx = res.getDimension(R.dimen.fling_gesture_max_output_velocity);
        this.mNotificationPanelMarginBottomPx = (int) res.getDimension(R.dimen.notification_panel_margin_bottom);
        this.mNotificationPanelMarginLeftPx = (int) res.getDimension(R.dimen.notification_panel_margin_left);
        this.mNotificationPanelGravity = res.getInteger(R.integer.notification_panel_layout_gravity);
        if (this.mNotificationPanelGravity <= 0) {
            this.mNotificationPanelGravity = 48;
        }
        getNinePatchPadding(res.getDrawable(R.drawable.notification_panel_bg), this.mNotificationPanelBackgroundPadding);
        this.mNotificationPanelMinHeight = res.getDimensionPixelSize(R.dimen.close_handle_underlap) + res.getDimensionPixelSize(R.dimen.notification_panel_padding_top) + res.getDimensionPixelSize(R.dimen.notification_panel_header_height) + this.mNotificationPanelBackgroundPadding.top + this.mNotificationPanelBackgroundPadding.bottom;
        this.mCarrierLabelHeight = res.getDimensionPixelSize(R.dimen.carrier_label_height);
    }

    protected PhoneStatusBarView makeStatusBarView() {
        Context context = this.mContext;
        Resources res = context.getResources();
        updateDisplaySize();
        loadDimens();
        this.mIconSize = res.getDimensionPixelSize(17104910);
        this.mStatusBarWindow = (StatusBarWindowView) View.inflate(context, R.layout.super_status_bar, null);
        this.mStatusBarWindow.mService = this;
        this.mStatusBarWindow.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0 && PhoneStatusBar.this.mExpanded && !PhoneStatusBar.this.mAnimating) {
                    PhoneStatusBar.this.animateCollapse();
                }
                return PhoneStatusBar.this.mStatusBarWindow.onTouchEvent(event);
            }
        });
        this.mStatusBarView = (PhoneStatusBarView) this.mStatusBarWindow.findViewById(R.id.status_bar);
        this.mNotificationPanel = this.mStatusBarWindow.findViewById(R.id.notification_panel);
        this.mNotificationPanel.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return SHOW_CARRIER_LABEL;
            }
        });
        this.mNotificationPanelIsFullScreenWidth = this.mNotificationPanel.getLayoutParams().width == -1 ? SHOW_CARRIER_LABEL : false;
        this.mNotificationPanel.setSystemUiVisibility((this.mNotificationPanelIsFullScreenWidth ? 0 : 1048576) | 524288);
        if (!ActivityManager.isHighEndGfx(this.mDisplay)) {
            this.mStatusBarWindow.setBackground(null);
            this.mNotificationPanel.setBackground(new FastColorDrawable(context.getResources().getColor(R.color.notification_panel_solid_background)));
        }
        updateShowSearchHoldoff();
        this.mStatusBarView.mService = this;
        this.mChoreographer = Choreographer.getInstance();
        try {
            if (this.mWindowManager.hasNavigationBar()) {
                this.mNavigationBarView = (NavigationBarView) View.inflate(context, R.layout.navigation_bar, null);
                this.mNavigationBarView.setDisabledFlags(this.mDisabled);
                this.mNavigationBarView.setBar(this);
            }
        } catch (RemoteException e) {
        }
        this.mPixelFormat = -1;
        this.mStatusIcons = (LinearLayout) this.mStatusBarView.findViewById(R.id.statusIcons);
        this.mNotificationIcons = (IconMerger) this.mStatusBarView.findViewById(R.id.notificationIcons);
        this.mNotificationIcons.setOverflowIndicator(this.mMoreIcon);
        this.mIcons = (LinearLayout) this.mStatusBarView.findViewById(R.id.icons);
        this.mTickerView = this.mStatusBarView.findViewById(R.id.ticker);
        this.mPile = (NotificationRowLayout) this.mStatusBarWindow.findViewById(R.id.latestItems);
        this.mPile.setLayoutTransitionsEnabled(SPEW);
        this.mPile.setLongPressListener(getNotificationLongClicker());
        this.mPile.setOnSizeChangedListener(new OnSizeChangedListener() {
            public void onSizeChanged(View view, int w, int h, int oldw, int oldh) {
                PhoneStatusBar.this.updateCarrierLabelVisibility(SPEW);
            }
        });
        this.mExpandedContents = this.mPile;
        this.mClearButton = this.mStatusBarWindow.findViewById(R.id.clear_all_button);
        this.mClearButton.setOnClickListener(this.mClearButtonListener);
        this.mClearButton.setAlpha(0.0f);
        this.mClearButton.setVisibility(CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL);
        this.mClearButton.setEnabled(SPEW);
        this.mDateView = (DateView) this.mStatusBarWindow.findViewById(R.id.date);
        this.mSettingsButton = this.mStatusBarWindow.findViewById(R.id.settings_button);
        this.mSettingsButton.setOnClickListener(this.mSettingsButtonListener);
        this.mRotationButton = (RotationToggle) this.mStatusBarWindow.findViewById(R.id.rotation_lock_button);
        this.mCarrierLabel = (TextView) this.mStatusBarWindow.findViewById(R.id.carrier_label);
        this.mCarrierLabel.setVisibility(this.mCarrierLabelVisible ? 0 : 4);
        this.mScrollView = (ScrollView) this.mStatusBarWindow.findViewById(R.id.scroll);
        this.mScrollView.setVerticalScrollBarEnabled(SPEW);
        this.mTicker = new MyTicker(context, this.mStatusBarView);
        ((TickerView) this.mStatusBarView.findViewById(R.id.tickerText)).mTicker = this.mTicker;
        this.mCloseView = (CloseDragHandle) this.mStatusBarWindow.findViewById(R.id.close);
        this.mCloseView.mService = this;
        this.mCloseViewHeight = res.getDimensionPixelSize(R.dimen.close_handle_height);
        this.mEdgeBorder = res.getDimensionPixelSize(R.dimen.status_bar_edge_ignore);
        setAreThereNotifications();
        this.mLocationController = new LocationController(this.mContext);
        this.mBatteryController = new BatteryController(this.mContext);
        this.mBatteryController.addIconView((ImageView) this.mStatusBarView.findViewById(R.id.battery));
        this.mNetworkController = new NetworkController(this.mContext);
        SignalClusterView signalCluster = (SignalClusterView) this.mStatusBarView.findViewById(R.id.signal_cluster);
        this.mNetworkController.addSignalCluster(signalCluster);
        signalCluster.setNetworkController(this.mNetworkController);
        if (this.mNetworkController.hasMobileDataFeature()) {
            this.mNetworkController.addMobileLabelView(this.mCarrierLabel);
        } else {
            this.mNetworkController.addCombinedLabelView(this.mCarrierLabel);
        }
        this.mRecentTasksLoader = new RecentTasksLoader(context);
        updateRecentsPanel();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.CONFIGURATION_CHANGED");
        filter.addAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
        filter.addAction("android.intent.action.SCREEN_OFF");
        context.registerReceiver(this.mBroadcastReceiver, filter);
        return this.mStatusBarView;
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        updateRecentsPanel();
        updateShowSearchHoldoff();
    }

    void performCollapse() {
        if (this.mExpandedVisible) {
            this.mExpandedVisible = false;
            this.mPile.setLayoutTransitionsEnabled(SPEW);
            if (this.mNavigationBarView != null) {
                this.mNavigationBarView.setSlippery(SPEW);
            }
            visibilityChanged(SPEW);
            LayoutParams lp = (LayoutParams) this.mStatusBarWindow.getLayoutParams();
            lp.height = getStatusBarHeight();
            lp.flags |= 8;
            lp.flags &= -131073;
            WindowManagerImpl.getDefault().updateViewLayout(this.mStatusBarWindow, lp);
            if ((this.mDisabled & 131072) == 0) {
                setNotificationIconVisibility(SHOW_CARRIER_LABEL, 17432576);
            }
            if (this.mExpanded) {
                this.mExpanded = false;
                dismissPopups();
                if (this.mPostCollapseCleanup != null) {
                    this.mPostCollapseCleanup.run();
                    this.mPostCollapseCleanup = null;
                }
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    void performDisableActions(int r7_net) {
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.PhoneStatusBar.performDisableActions(int):void");
        /*
        r6 = this;
        r5 = 524288; // 0x80000 float:7.34684E-40 double:2.590327E-318;
        r4 = 131072; // 0x20000 float:1.83671E-40 double:6.47582E-319;
        r3 = 65536; // 0x10000 float:9.18355E-41 double:3.2379E-319;
        r1 = r6.mDisabled;
        r0 = r7 ^ r1;
        r6.mDisabled = r7;
        r2 = r0 & r3;
        if (r2 == 0) goto L_0x001e;
    L_0x0010:
        r2 = r7 & r3;
        if (r2 == 0) goto L_0x001e;
    L_0x0014:
        r2 = "PhoneStatusBar";
        r3 = "DISABLE_EXPAND: yes";
        android.util.Slog.d(r2, r3);
        r6.animateCollapse();
    L_0x001e:
        r2 = r0 & r4;
        if (r2 == 0) goto L_0x0057;
    L_0x0022:
        r2 = r7 & r4;
        if (r2 == 0) goto L_0x0045;
    L_0x0026:
        r2 = "PhoneStatusBar";
        r3 = "DISABLE_NOTIFICATION_ICONS: yes";
        android.util.Slog.d(r2, r3);
        r2 = r6.mTicking;
        if (r2 == 0) goto L_0x003d;
    L_0x0031:
        r2 = r6.mNotificationIcons;
        r3 = 4;
        r2.setVisibility(r3);
        r2 = r6.mTicker;
        r2.halt();
    L_0x003c:
        return;
    L_0x003d:
        r2 = 0;
        r3 = 17432577; // 0x10a0001 float:2.53466E-38 double:8.6128374E-317;
        r6.setNotificationIconVisibility(r2, r3);
        goto L_0x003c;
    L_0x0045:
        r2 = "PhoneStatusBar";
        r3 = "DISABLE_NOTIFICATION_ICONS: no";
        android.util.Slog.d(r2, r3);
        r2 = r6.mExpandedVisible;
        if (r2 != 0) goto L_0x003c;
    L_0x0050:
        r2 = 1;
        r3 = 17432576; // 0x10a0000 float:2.5346597E-38 double:8.612837E-317;
        r6.setNotificationIconVisibility(r2, r3);
        goto L_0x003c;
    L_0x0057:
        r2 = r0 & r5;
        if (r2 == 0) goto L_0x003c;
    L_0x005b:
        r2 = r6.mTicking;
        if (r2 == 0) goto L_0x003c;
    L_0x005f:
        r2 = r7 & r5;
        if (r2 == 0) goto L_0x003c;
    L_0x0063:
        r2 = r6.mTicker;
        r2.halt();
        goto L_0x003c;
        */
    }

    void performExpand() {
        if ((this.mDisabled & 65536) == 0 && !this.mExpanded) {
            this.mExpanded = true;
            makeExpandedVisible(SPEW);
            updateExpandedViewPos(BaseStatusBar.EXPANDED_FULL_OPEN);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    void performFling(int r9_y, float r10_vel, boolean r11_always) {
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.PhoneStatusBar.performFling(int, float, boolean):void");
        /*
        r8 = this;
        r0 = 0;
        r7 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = 0;
        r1 = 1;
        r5 = 0;
        r8.mAnimatingReveal = r0;
        r2 = (float) r9;
        r8.mAnimY = r2;
        r8.mAnimVel = r10;
        r2 = r8.mExpanded;
        if (r2 == 0) goto L_0x006c;
    L_0x0011:
        if (r11 != 0) goto L_0x0060;
    L_0x0013:
        r2 = r8.mFlingCollapseMinVelocityPx;
        r2 = (r10 > r2 ? 1 : (r10 == r2 ? 0 : -1));
        if (r2 > 0) goto L_0x002f;
    L_0x0019:
        r2 = (float) r9;
        r3 = r8.getExpandedViewMaxHeight();
        r3 = (float) r3;
        r4 = r8.mCollapseMinDisplayFraction;
        r4 = r7 - r4;
        r3 = r3 * r4;
        r2 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1));
        if (r2 <= 0) goto L_0x0060;
    L_0x0028:
        r2 = r8.mFlingExpandMinVelocityPx;
        r2 = -r2;
        r2 = (r10 > r2 ? 1 : (r10 == r2 ? 0 : -1));
        if (r2 <= 0) goto L_0x0060;
    L_0x002f:
        r2 = r8.mExpandAccelPx;
        r8.mAnimAccel = r2;
        r2 = (r10 > r5 ? 1 : (r10 == r5 ? 0 : -1));
        if (r2 >= 0) goto L_0x0039;
    L_0x0037:
        r8.mAnimVel = r5;
    L_0x0039:
        r8.resetLastAnimTime();
        r8.mAnimating = r1;
        r2 = r8.mAnimAccel;
        r2 = (r2 > r5 ? 1 : (r2 == r5 ? 0 : -1));
        if (r2 >= 0) goto L_0x0045;
    L_0x0044:
        r0 = r1;
    L_0x0045:
        r8.mClosing = r0;
        r0 = r8.mChoreographer;
        r2 = r8.mAnimationCallback;
        r0.removeCallbacks(r1, r2, r6);
        r0 = r8.mChoreographer;
        r2 = r8.mRevealAnimationCallback;
        r0.removeCallbacks(r1, r2, r6);
        r0 = r8.mChoreographer;
        r2 = r8.mAnimationCallback;
        r0.postCallback(r1, r2, r6);
        r8.stopTracking();
        return;
    L_0x0060:
        r2 = r8.mCollapseAccelPx;
        r2 = -r2;
        r8.mAnimAccel = r2;
        r2 = (r10 > r5 ? 1 : (r10 == r5 ? 0 : -1));
        if (r2 <= 0) goto L_0x0039;
    L_0x0069:
        r8.mAnimVel = r5;
        goto L_0x0039;
    L_0x006c:
        if (r11 != 0) goto L_0x008a;
    L_0x006e:
        r2 = r8.mFlingExpandMinVelocityPx;
        r2 = (r10 > r2 ? 1 : (r10 == r2 ? 0 : -1));
        if (r2 > 0) goto L_0x008a;
    L_0x0074:
        r2 = (float) r9;
        r3 = r8.getExpandedViewMaxHeight();
        r3 = (float) r3;
        r4 = r8.mExpandMinDisplayFraction;
        r4 = r7 - r4;
        r3 = r3 * r4;
        r2 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1));
        if (r2 <= 0) goto L_0x0095;
    L_0x0083:
        r2 = r8.mFlingCollapseMinVelocityPx;
        r2 = -r2;
        r2 = (r10 > r2 ? 1 : (r10 == r2 ? 0 : -1));
        if (r2 <= 0) goto L_0x0095;
    L_0x008a:
        r2 = r8.mExpandAccelPx;
        r8.mAnimAccel = r2;
        r2 = (r10 > r5 ? 1 : (r10 == r5 ? 0 : -1));
        if (r2 >= 0) goto L_0x0039;
    L_0x0092:
        r8.mAnimVel = r5;
        goto L_0x0039;
    L_0x0095:
        r2 = r8.mCollapseAccelPx;
        r2 = -r2;
        r8.mAnimAccel = r2;
        r2 = (r10 > r5 ? 1 : (r10 == r5 ? 0 : -1));
        if (r2 <= 0) goto L_0x0039;
    L_0x009e:
        r8.mAnimVel = r5;
        goto L_0x0039;
        */
    }

    void postStartTracing() {
        this.mHandler.postDelayed(this.mStartTracing, 3000);
    }

    void prepareTracking(int y, boolean opening) {
        this.mCloseView.setPressed(SHOW_CARRIER_LABEL);
        this.mTracking = true;
        setPileLayers(CommandQueue.FLAG_EXCLUDE_RECENTS_PANEL);
        this.mVelocityTracker = VelocityTracker.obtain();
        if (opening) {
            makeExpandedVisible(SHOW_CARRIER_LABEL);
        } else {
            if (this.mAnimating) {
                this.mAnimating = false;
                this.mChoreographer.removeCallbacks(1, this.mAnimationCallback, null);
            }
            updateExpandedViewPos(this.mViewDelta + y);
        }
    }

    public void removeIcon(String slot, int index, int viewIndex) {
        this.mStatusIcons.removeViewAt(viewIndex);
    }

    public void removeNotification(IBinder key) {
        StatusBarNotification old = removeNotificationViews(key);
        if (old != null) {
            this.mTicker.removeEntry(old);
            updateExpandedViewPos(BaseStatusBar.EXPANDED_LEAVE_ALONE);
            if (this.mNotificationData.size() == 0 && !this.mAnimating) {
                animateCollapse();
            }
        }
        setAreThereNotifications();
    }

    void resetLastAnimTime() {
        this.mAnimLastTimeNanos = System.nanoTime();
    }

    protected void setAreThereNotifications() {
        boolean any;
        boolean clearable;
        boolean showDot;
        float f = 1.0f;
        int i = 1;
        if (this.mNotificationData.size() > 0) {
            any = true;
        } else {
            any = false;
        }
        if (any && this.mNotificationData.hasClearableItems()) {
            clearable = true;
        } else {
            clearable = false;
        }
        View view;
        float f2;
        if (this.mClearButton.isShown()) {
            boolean z;
            if (this.mClearButton.getAlpha() == 1.0f) {
                z = true;
            } else {
                z = false;
            }
            if (clearable != z) {
                view = this.mClearButton;
                String str = "alpha";
                float[] fArr = new float[1];
                if (clearable) {
                    z = true;
                } else {
                    z = false;
                }
                fArr[0] = f2;
                ObjectAnimator clearAnimation = ObjectAnimator.ofFloat(view, str, fArr).setDuration(250);
                clearAnimation.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        if (PhoneStatusBar.this.mClearButton.getAlpha() <= 0.0f) {
                            PhoneStatusBar.this.mClearButton.setVisibility(CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL);
                        }
                    }

                    public void onAnimationStart(Animator animation) {
                        if (PhoneStatusBar.this.mClearButton.getAlpha() <= 0.0f) {
                            PhoneStatusBar.this.mClearButton.setVisibility(INTRUDER_ALERT_DECAY_MS);
                        }
                    }
                });
                clearAnimation.start();
            }
        } else {
            view = this.mClearButton;
            if (clearable) {
                f2 = 1.0f;
            } else {
                f2 = 0.0f;
            }
            view.setAlpha(f2);
            this.mClearButton.setVisibility(clearable ? 0 : CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL);
        }
        this.mClearButton.setEnabled(clearable);
        View nlo = this.mStatusBarView.findViewById(R.id.notification_lights_out);
        if (!any || areLightsOn()) {
            showDot = false;
        } else {
            showDot = true;
        }
        if (nlo.getAlpha() != 1.0f) {
            i = 0;
        }
        if (showDot != z) {
            long j;
            AnimatorListener animatorListener;
            if (showDot) {
                nlo.setAlpha(0.0f);
                nlo.setVisibility(INTRUDER_ALERT_DECAY_MS);
            }
            ViewPropertyAnimator animate = nlo.animate();
            if (!showDot) {
                f = 0.0f;
            }
            animate = animate.alpha(f);
            if (showDot) {
                j = 750;
            } else {
                j = 250;
            }
            ViewPropertyAnimator interpolator = animate.setDuration(j).setInterpolator(new AccelerateInterpolator(2.0f));
            if (showDot) {
                animatorListener = null;
            } else {
                animatorListener = new AnonymousClass_13(nlo);
            }
            interpolator.setListener(animatorListener).start();
        }
        updateCarrierLabelVisibility(SPEW);
    }

    public void setHardKeyboardStatus(boolean available, boolean enabled) {
    }

    public void setImeWindowStatus(IBinder token, int vis, int backDisposition) {
        boolean altBack = (backDisposition == 2 || (vis & 2) != 0) ? SHOW_CARRIER_LABEL : SPEW;
        this.mCommandQueue.setNavigationIconHints(altBack ? this.mNavigationIconHints | 8 : this.mNavigationIconHints & -9);
    }

    public void setLightsOn(boolean on) {
        Log.v(TAG, "setLightsOn(" + on + ")");
        if (on) {
            setSystemUiVisibility(INTRUDER_ALERT_DECAY_MS, 1);
        } else {
            setSystemUiVisibility(1, 1);
        }
    }

    public void setNavigationIconHints(int hints) {
        if (hints != this.mNavigationIconHints) {
            this.mNavigationIconHints = hints;
            if (this.mNavigationBarView != null) {
                this.mNavigationBarView.setNavigationIconHints(hints);
            }
        }
    }

    void setNotificationIconVisibility(boolean visible, int anim) {
        int old = this.mNotificationIcons.getVisibility();
        int v = visible ? INTRUDER_ALERT_DECAY_MS : CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL;
        if (old != v) {
            this.mNotificationIcons.setVisibility(v);
            this.mNotificationIcons.startAnimation(loadAnim(anim, null));
        }
    }

    public void setSystemUiVisibility(int vis, int mask) {
        int oldVal = this.mSystemUiVisibility;
        int newVal = ((mask ^ -1) & oldVal) | (vis & mask);
        int diff = newVal ^ oldVal;
        if (diff != 0) {
            this.mSystemUiVisibility = newVal;
            if ((diff & 1) != 0) {
                boolean lightsOut = (vis & 1) != 0 ? SHOW_CARRIER_LABEL : SPEW;
                if (lightsOut) {
                    animateCollapse();
                    if (this.mTicking) {
                        this.mTicker.halt();
                    }
                }
                if (this.mNavigationBarView != null) {
                    this.mNavigationBarView.setLowProfile(lightsOut);
                }
                setStatusBarLowProfile(lightsOut);
            }
            notifyUiVisibilityChanged();
        }
    }

    protected boolean shouldDisableNavbarGestures() {
        return (this.mExpanded || (this.mDisabled & 2097152) != 0) ? SHOW_CARRIER_LABEL : SPEW;
    }

    public void showClock(boolean show) {
        if (this.mStatusBarView != null) {
            View clock = this.mStatusBarView.findViewById(R.id.clock);
            if (clock != null) {
                clock.setVisibility(show ? INTRUDER_ALERT_DECAY_MS : CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
            }
        }
    }

    public void showSearchPanel() {
        super.showSearchPanel();
        LayoutParams lp = (LayoutParams) this.mNavigationBarView.getLayoutParams();
        lp.flags &= -33;
        WindowManagerImpl.getDefault().updateViewLayout(this.mNavigationBarView, lp);
    }

    public void start() {
        this.mDisplay = ((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay();
        this.mWindowManager = Stub.asInterface(ServiceManager.getService("window"));
        super.start();
        addNavigationBar();
        this.mIconPolicy = new PhoneStatusBarPolicy(this.mContext);
    }

    void stopTracking() {
        if (this.mTracking) {
            this.mTracking = false;
            setPileLayers(INTRUDER_ALERT_DECAY_MS);
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
            this.mCloseView.setPressed(SPEW);
        }
    }

    protected void tick(IBinder key, StatusBarNotification n, boolean firstTime) {
        if (areLightsOn() && isDeviceProvisioned() && n.notification.tickerText != null && this.mStatusBarWindow.getWindowToken() != null && (this.mDisabled & 655360) == 0) {
            this.mTicker.addEntry(n);
        }
    }

    public void topAppWindowChanged(boolean showMenu) {
        if (this.mNavigationBarView != null) {
            this.mNavigationBarView.setMenuVisibility(showMenu);
        }
        if (showMenu) {
            setLightsOn(SHOW_CARRIER_LABEL);
        }
    }

    protected void updateCarrierLabelVisibility(boolean force) {
        boolean makeVisible = this.mPile.getHeight() < this.mScrollView.getHeight() - this.mCarrierLabelHeight ? SHOW_CARRIER_LABEL : false;
        if (force || this.mCarrierLabelVisible != makeVisible) {
            float f;
            AnimatorListener animatorListener;
            this.mCarrierLabelVisible = makeVisible;
            this.mCarrierLabel.animate().cancel();
            if (makeVisible) {
                this.mCarrierLabel.setVisibility(INTRUDER_ALERT_DECAY_MS);
            }
            ViewPropertyAnimator animate = this.mCarrierLabel.animate();
            if (makeVisible) {
                f = 1.0f;
            } else {
                f = 0.0f;
            }
            animate = animate.alpha(f).setDuration(150);
            if (makeVisible) {
                animatorListener = null;
            } else {
                animatorListener = new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        if (!PhoneStatusBar.this.mCarrierLabelVisible) {
                            PhoneStatusBar.this.mCarrierLabel.setVisibility(CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL);
                            PhoneStatusBar.this.mCarrierLabel.setAlpha(0.0f);
                        }
                    }
                };
            }
            animate.setListener(animatorListener).start();
        }
    }

    void updateDisplaySize() {
        this.mDisplay.getMetrics(this.mDisplayMetrics);
    }

    void updateExpandedInvisiblePosition() {
        this.mTrackingPosition = -this.mDisplayMetrics.heightPixels;
    }

    protected void updateExpandedViewPos(int expandedPosition) {
        int disph = getExpandedViewMaxHeight();
        if (this.mExpandedVisible) {
            int panelh;
            if (expandedPosition == -10001) {
                panelh = disph;
            } else if (expandedPosition == -10000) {
                panelh = this.mTrackingPosition;
            } else if (expandedPosition <= disph) {
                panelh = expandedPosition;
            } else {
                panelh = disph;
            }
            if (panelh > disph || !(panelh >= disph || this.mTracking || this.mAnimating)) {
                panelh = disph;
            } else if (panelh < 0) {
                panelh = INTRUDER_ALERT_DECAY_MS;
            }
            if (panelh != this.mTrackingPosition) {
                this.mTrackingPosition = panelh;
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) this.mNotificationPanel.getLayoutParams();
                lp.height = panelh;
                lp.gravity = this.mNotificationPanelGravity;
                lp.leftMargin = this.mNotificationPanelMarginLeftPx;
                this.mNotificationPanel.setLayoutParams(lp);
                int barh = getCloseViewHeight() + getStatusBarHeight();
                float frac = saturate(((float) (panelh - barh)) / ((float) (disph - barh)));
                if (ActivityManager.isHighEndGfx(this.mDisplay)) {
                    this.mStatusBarWindow.setBackgroundColor(((int) (176.0f * ((float) (1.0d - (0.5d * (1.0d - Math.cos(3.141590118408203d * Math.pow((double) (1.0f - frac), 2.200000047683716d)))))))) << 24);
                }
                updateCarrierLabelVisibility(SPEW);
            }
        } else {
            updateExpandedInvisiblePosition();
        }
    }

    public void updateIcon(String slot, int index, int viewIndex, StatusBarIcon old, StatusBarIcon icon) {
        ((StatusBarIconView) this.mStatusIcons.getChildAt(viewIndex)).set(icon);
    }

    protected void updateNotificationIcons() {
        if (this.mNotificationIcons != null) {
            loadNotificationShade();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(this.mIconSize + this.mIconHPadding * 2, this.mNaturalBarHeight);
            int N = this.mNotificationData.size();
            ArrayList<View> toShow = new ArrayList();
            boolean provisioned = isDeviceProvisioned();
            int i = INTRUDER_ALERT_DECAY_MS;
            while (i < N) {
                Entry ent = this.mNotificationData.get(N - i - 1);
                if ((provisioned && ent.notification.score >= -10) || showNotificationEvenIfUnprovisioned(ent.notification)) {
                    toShow.add(ent.icon);
                }
                i++;
            }
            ArrayList<View> toRemove = new ArrayList();
            i = INTRUDER_ALERT_DECAY_MS;
            while (i < this.mNotificationIcons.getChildCount()) {
                View child = this.mNotificationIcons.getChildAt(i);
                if (!toShow.contains(child)) {
                    toRemove.add(child);
                }
                i++;
            }
            Iterator i$ = toRemove.iterator();
            while (i$.hasNext()) {
                this.mNotificationIcons.removeView((View) i$.next());
            }
            i = INTRUDER_ALERT_DECAY_MS;
            while (i < toShow.size()) {
                View v = (View) toShow.get(i);
                if (v.getParent() == null) {
                    this.mNotificationIcons.addView(v, i, params);
                }
                i++;
            }
        }
    }

    protected void updateRecentsPanel() {
        super.updateRecentsPanel(R.layout.status_bar_recent_panel);
        this.mRecentsPanel.setMinSwipeAlpha(0.03f);
        if (this.mNavigationBarView != null) {
            this.mNavigationBarView.getRecentsButton().setOnTouchListener(this.mRecentsPanel);
        }
    }

    void updateResources() {
        Context context = this.mContext;
        Resources res = context.getResources();
        if (this.mClearButton instanceof TextView) {
            ((TextView) this.mClearButton).setText(context.getText(R.string.status_bar_clear_all_button));
        }
        loadDimens();
    }

    protected void updateSearchPanel() {
        super.updateSearchPanel();
        this.mSearchPanelView.setStatusBarView(this.mNavigationBarView);
        this.mNavigationBarView.setDelegateView(this.mSearchPanelView);
    }

    void vibrate() {
        ((Vibrator) this.mContext.getSystemService("vibrator")).vibrate(250);
    }
}