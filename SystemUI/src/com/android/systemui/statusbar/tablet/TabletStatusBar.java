package com.android.systemui.statusbar.tablet;

import android.animation.LayoutTransition;
import android.animation.LayoutTransition.TransitionListener;
import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.app.ActivityManagerNative;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.PendingIntent.CanceledException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import android.util.Slog;
import android.view.Display;
import android.view.IWindowManager;
import android.view.IWindowManager.Stub;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.WindowManagerImpl;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.android.internal.statusbar.StatusBarIcon;
import com.android.internal.statusbar.StatusBarNotification;
import com.android.systemui.R;
import com.android.systemui.recent.RecentTasksLoader;
import com.android.systemui.recent.RecentsCallback;
import com.android.systemui.recent.RecentsPanelView.OnRecentsPanelVisibilityChangedListener;
import com.android.systemui.statusbar.BaseStatusBar;
import com.android.systemui.statusbar.BaseStatusBar.TouchOutsideListener;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.DoNotDisturb;
import com.android.systemui.statusbar.NotificationData.Entry;
import com.android.systemui.statusbar.SignalClusterView;
import com.android.systemui.statusbar.StatusBarIconView;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.BluetoothController;
import com.android.systemui.statusbar.policy.CompatModeButton;
import com.android.systemui.statusbar.policy.DisplayController;
import com.android.systemui.statusbar.policy.LocationController;
import com.android.systemui.statusbar.policy.NetworkController;
import com.android.systemui.statusbar.policy.NotificationRowLayout;
import com.android.systemui.statusbar.policy.Prefs;
import com.android.systemui.statusbar.tablet.InputMethodsPanel.OnHardKeyboardEnabledChangeListener;
import com.android.systemui.usb.StorageNotification;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

public class TabletStatusBar extends BaseStatusBar implements OnHardKeyboardEnabledChangeListener, OnRecentsPanelVisibilityChangedListener {
    public static final boolean DEBUG = false;
    public static final boolean DEBUG_COMPAT_HELP = false;
    private static final boolean FAKE_SPACE_BAR = true;
    private static final int HIDE_ICONS_BELOW_SCORE = -10;
    public static final int MSG_CLOSE_COMPAT_MODE_PANEL = 1051;
    public static final int MSG_CLOSE_INPUT_METHODS_PANEL = 1041;
    public static final int MSG_CLOSE_NOTIFICATION_PANEL = 1001;
    public static final int MSG_CLOSE_NOTIFICATION_PEEK = 1003;
    public static final int MSG_HIDE_CHROME = 1031;
    public static final int MSG_OPEN_COMPAT_MODE_PANEL = 1050;
    public static final int MSG_OPEN_INPUT_METHODS_PANEL = 1040;
    public static final int MSG_OPEN_NOTIFICATION_PANEL = 1000;
    public static final int MSG_OPEN_NOTIFICATION_PEEK = 1002;
    public static final int MSG_SHOW_CHROME = 1030;
    public static final int MSG_STOP_TICKER = 2000;
    static final int NOTIFICATION_PEEK_FADE_DELAY = 3000;
    static final int NOTIFICATION_PEEK_HOLD_THRESH = 200;
    private static final int NOTIFICATION_PRIORITY_MULTIPLIER = 10;
    public static final String TAG = "TabletStatusBar";
    private boolean mAltBackButtonEnabledForIme;
    ImageView mBackButton;
    ViewGroup mBarContents;
    BatteryController mBatteryController;
    BluetoothController mBluetoothController;
    private BroadcastReceiver mBroadcastReceiver;
    CompatModeButton mCompatModeButton;
    private CompatModePanel mCompatModePanel;
    View mCompatibilityHelpDialog;
    int mDisabled;
    DisplayController mDisplayController;
    DoNotDisturb mDoNotDisturb;
    View mFakeSpaceBar;
    ViewGroup mFeedbackIconArea;
    View mHomeButton;
    private OnTouchListener mHomeSearchActionListener;
    int mIconHPadding;
    IconLayout mIconLayout;
    int mIconSize;
    InputMethodButton mInputMethodSwitchButton;
    private InputMethodsPanel mInputMethodsPanel;
    LocationController mLocationController;
    private int mMaxNotificationIcons;
    View mMenuButton;
    int mMenuNavIconWidth;
    int mNaturalBarHeight;
    int mNavIconWidth;
    ViewGroup mNavigationArea;
    private int mNavigationIconHints;
    NetworkController mNetworkController;
    View mNotificationArea;
    Entry mNotificationDNDDummyEntry;
    boolean mNotificationDNDMode;
    int mNotificationFlingVelocity;
    NotificationIconArea mNotificationIconArea;
    NotificationPanel mNotificationPanel;
    LayoutParams mNotificationPanelParams;
    int mNotificationPeekIndex;
    IBinder mNotificationPeekKey;
    ViewGroup mNotificationPeekRow;
    LayoutTransition mNotificationPeekScrubLeft;
    LayoutTransition mNotificationPeekScrubRight;
    int mNotificationPeekTapDuration;
    NotificationPeekPanel mNotificationPeekWindow;
    View mNotificationTrigger;
    private OnClickListener mOnClickListener;
    View mRecentButton;
    View mShadow;
    private int mShowSearchHoldoff;
    private Runnable mShowSearchPanel;
    KeyEvent mSpaceBarKeyEvent;
    TabletStatusBarView mStatusBarView;
    private StorageManager mStorageManager;
    private int mSystemUiVisibility;
    TabletTicker mTicker;
    View mVolumeDownButton;
    View mVolumeUpButton;
    IWindowManager mWindowManager;

    private class H extends H {
        private H() {
            super();
        }

        public void handleMessage(Message m) {
            super.handleMessage(m);
            int N;
            Entry entry;
            switch (m.what) {
                case MSG_OPEN_NOTIFICATION_PANEL:
                    if (!TabletStatusBar.this.mNotificationPanel.isShowing()) {
                        TabletStatusBar.this.mNotificationPanel.show(FAKE_SPACE_BAR, FAKE_SPACE_BAR);
                        TabletStatusBar.this.mNotificationArea.setVisibility(CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL);
                        TabletStatusBar.this.mTicker.halt();
                    }
                case MSG_CLOSE_NOTIFICATION_PANEL:
                    if (TabletStatusBar.this.mNotificationPanel.isShowing()) {
                        TabletStatusBar.this.mNotificationPanel.show(DEBUG_COMPAT_HELP, FAKE_SPACE_BAR);
                        TabletStatusBar.this.mNotificationArea.setVisibility(0);
                    }
                case MSG_OPEN_NOTIFICATION_PEEK:
                    if (m.arg1 >= 0) {
                        N = TabletStatusBar.this.mNotificationData.size();
                        if (!TabletStatusBar.this.mNotificationDNDMode && TabletStatusBar.this.mNotificationPeekIndex >= 0 && TabletStatusBar.this.mNotificationPeekIndex < N) {
                            TabletStatusBar.this.mNotificationData.get(N - 1 - TabletStatusBar.this.mNotificationPeekIndex).icon.setBackgroundColor(0);
                            TabletStatusBar.this.mNotificationPeekIndex = -1;
                            TabletStatusBar.this.mNotificationPeekKey = null;
                        }
                        int peekIndex = m.arg1;
                        if (peekIndex < N) {
                            if (TabletStatusBar.this.mNotificationDNDMode) {
                                entry = TabletStatusBar.this.mNotificationDNDDummyEntry;
                            } else {
                                entry = TabletStatusBar.this.mNotificationData.get(N - 1 - peekIndex);
                            }
                            Entry copy = new Entry(entry.key, entry.notification, entry.icon);
                            TabletStatusBar.this.inflateViews(copy, TabletStatusBar.this.mNotificationPeekRow);
                            if (TabletStatusBar.this.mNotificationDNDMode) {
                                copy.content.setOnClickListener(new OnClickListener() {
                                    public void onClick(View v) {
                                        Editor editor = Prefs.edit(H.this.this$0.mContext);
                                        editor.putBoolean(Prefs.DO_NOT_DISTURB_PREF, DEBUG_COMPAT_HELP);
                                        editor.apply();
                                        H.this.this$0.animateCollapse();
                                        H.this.this$0.visibilityChanged(DEBUG_COMPAT_HELP);
                                    }
                                });
                            }
                            entry.icon.setBackgroundColor(553648127);
                            TabletStatusBar.this.mNotificationPeekRow.removeAllViews();
                            TabletStatusBar.this.mNotificationPeekRow.addView(copy.row);
                            TabletStatusBar.this.mNotificationPeekWindow.setVisibility(0);
                            TabletStatusBar.this.mNotificationPanel.show(DEBUG_COMPAT_HELP, FAKE_SPACE_BAR);
                            TabletStatusBar.this.mNotificationPeekIndex = peekIndex;
                            TabletStatusBar.this.mNotificationPeekKey = entry.key;
                        }
                    }
                case MSG_CLOSE_NOTIFICATION_PEEK:
                    TabletStatusBar.this.mNotificationPeekWindow.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
                    TabletStatusBar.this.mNotificationPeekRow.removeAllViews();
                    N = TabletStatusBar.this.mNotificationData.size();
                    if (TabletStatusBar.this.mNotificationPeekIndex >= 0 && TabletStatusBar.this.mNotificationPeekIndex < N) {
                        if (TabletStatusBar.this.mNotificationDNDMode) {
                            entry = TabletStatusBar.this.mNotificationDNDDummyEntry;
                        } else {
                            entry = TabletStatusBar.this.mNotificationData.get(N - 1 - TabletStatusBar.this.mNotificationPeekIndex);
                        }
                        entry.icon.setBackgroundColor(0);
                    }
                    TabletStatusBar.this.mNotificationPeekIndex = -1;
                    TabletStatusBar.this.mNotificationPeekKey = null;
                case MSG_SHOW_CHROME:
                    TabletStatusBar.this.mBarContents.setVisibility(0);
                    TabletStatusBar.this.mShadow.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
                    TabletStatusBar.access$1672(TabletStatusBar.this, -2);
                    TabletStatusBar.this.notifyUiVisibilityChanged();
                case MSG_HIDE_CHROME:
                    TabletStatusBar.this.animateCollapse();
                    TabletStatusBar.this.visibilityChanged(DEBUG_COMPAT_HELP);
                    TabletStatusBar.this.mBarContents.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
                    TabletStatusBar.this.mShadow.setVisibility(0);
                    TabletStatusBar.access$1676(TabletStatusBar.this, 1);
                    TabletStatusBar.this.notifyUiVisibilityChanged();
                case MSG_OPEN_INPUT_METHODS_PANEL:
                    if (TabletStatusBar.this.mInputMethodsPanel != null) {
                        TabletStatusBar.this.mInputMethodsPanel.openPanel();
                    }
                case MSG_CLOSE_INPUT_METHODS_PANEL:
                    if (TabletStatusBar.this.mInputMethodsPanel != null) {
                        TabletStatusBar.this.mInputMethodsPanel.closePanel(DEBUG_COMPAT_HELP);
                    }
                case MSG_OPEN_COMPAT_MODE_PANEL:
                    if (TabletStatusBar.this.mCompatModePanel != null) {
                        TabletStatusBar.this.mCompatModePanel.openPanel();
                    }
                case MSG_CLOSE_COMPAT_MODE_PANEL:
                    if (TabletStatusBar.this.mCompatModePanel != null) {
                        TabletStatusBar.this.mCompatModePanel.closePanel();
                    }
                case MSG_STOP_TICKER:
                    TabletStatusBar.this.mTicker.halt();
                default:
                    break;
            }
        }
    }

    private class NotificationIconTouchListener implements OnTouchListener {
        float mInitialTouchX;
        float mInitialTouchY;
        int mPeekIndex;
        int mTouchSlop;
        VelocityTracker mVT;

        public NotificationIconTouchListener() {
            this.mTouchSlop = ViewConfiguration.get(TabletStatusBar.this.getContext()).getScaledTouchSlop();
        }

        public boolean onTouch(View v, MotionEvent event) {
            boolean peeking;
            if (TabletStatusBar.this.mNotificationPeekWindow.getVisibility() != 8) {
                peeking = true;
            } else {
                peeking = false;
            }
            boolean panelShowing = TabletStatusBar.this.mNotificationPanel.isShowing();
            if (panelShowing) {
                return DEBUG_COMPAT_HELP;
            }
            int numIcons = TabletStatusBar.this.mIconLayout.getChildCount();
            int newPeekIndex = (int) ((event.getX() * ((float) numIcons)) / ((float) TabletStatusBar.this.mIconLayout.getWidth()));
            if (newPeekIndex > numIcons - 1) {
                newPeekIndex = numIcons - 1;
            } else if (newPeekIndex < 0) {
                newPeekIndex = 0;
            }
            int action = event.getAction();
            Message peekMsg;
            switch (action) {
                case CommandQueue.FLAG_EXCLUDE_NONE:
                    this.mVT = VelocityTracker.obtain();
                    this.mInitialTouchX = event.getX();
                    this.mInitialTouchY = event.getY();
                    this.mPeekIndex = -1;
                    if (newPeekIndex != this.mPeekIndex) {
                        this.mPeekIndex = newPeekIndex;
                        peekMsg = TabletStatusBar.this.mHandler.obtainMessage(MSG_OPEN_NOTIFICATION_PEEK);
                        peekMsg.arg1 = this.mPeekIndex;
                        TabletStatusBar.this.mHandler.removeMessages(MSG_OPEN_NOTIFICATION_PEEK);
                        if (peeking) {
                            TabletStatusBar.this.mHandler.sendMessageDelayed(peekMsg, 200);
                        } else {
                            TabletStatusBar.this.mHandler.sendMessage(peekMsg);
                        }
                    }
                    if (this.mVT != null) {
                        this.mVT.addMovement(event);
                        this.mVT.computeCurrentVelocity(MSG_OPEN_NOTIFICATION_PANEL);
                        if (!panelShowing) {
                            if ((peeking && this.mVT.getYVelocity() < ((float) ((-TabletStatusBar.this.mNotificationFlingVelocity) * 3))) || this.mVT.getYVelocity() < ((float) (-TabletStatusBar.this.mNotificationFlingVelocity))) {
                                TabletStatusBar.this.mHandler.removeMessages(MSG_OPEN_NOTIFICATION_PEEK);
                                TabletStatusBar.this.mHandler.removeMessages(MSG_OPEN_NOTIFICATION_PANEL);
                                TabletStatusBar.this.mHandler.sendEmptyMessage(MSG_CLOSE_NOTIFICATION_PEEK);
                                TabletStatusBar.this.mHandler.sendEmptyMessage(MSG_OPEN_NOTIFICATION_PANEL);
                            }
                        }
                    }
                    return true;
                case CommandQueue.FLAG_EXCLUDE_SEARCH_PANEL:
                case RecentsCallback.SWIPE_DOWN:
                    TabletStatusBar.this.mHandler.removeMessages(MSG_OPEN_NOTIFICATION_PEEK);
                    if (!peeking && action == 1 && Math.abs(event.getX() - this.mInitialTouchX) < ((float) this.mTouchSlop) && Math.abs(event.getY() - this.mInitialTouchY) < ((float) (this.mTouchSlop / 3)) && ((int) event.getY()) < v.getBottom()) {
                        peekMsg = TabletStatusBar.this.mHandler.obtainMessage(MSG_OPEN_NOTIFICATION_PEEK);
                        peekMsg.arg1 = this.mPeekIndex;
                        TabletStatusBar.this.mHandler.removeMessages(MSG_OPEN_NOTIFICATION_PEEK);
                        TabletStatusBar.this.mHandler.sendMessage(peekMsg);
                        v.sendAccessibilityEvent(1);
                        v.playSoundEffect(0);
                        peeking = FAKE_SPACE_BAR;
                    }
                    if (peeking) {
                        TabletStatusBar.this.resetNotificationPeekFadeTimer();
                    }
                    this.mVT.recycle();
                    this.mVT = null;
                    return true;
                case CommandQueue.FLAG_EXCLUDE_RECENTS_PANEL:
                case CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL:
                    if (newPeekIndex != this.mPeekIndex) {
                        this.mPeekIndex = newPeekIndex;
                        peekMsg = TabletStatusBar.this.mHandler.obtainMessage(MSG_OPEN_NOTIFICATION_PEEK);
                        peekMsg.arg1 = this.mPeekIndex;
                        TabletStatusBar.this.mHandler.removeMessages(MSG_OPEN_NOTIFICATION_PEEK);
                        if (peeking) {
                            TabletStatusBar.this.mHandler.sendMessageDelayed(peekMsg, 200);
                        } else {
                            TabletStatusBar.this.mHandler.sendMessage(peekMsg);
                        }
                    }
                    if (this.mVT != null) {
                        this.mVT.addMovement(event);
                        this.mVT.computeCurrentVelocity(MSG_OPEN_NOTIFICATION_PANEL);
                        if (panelShowing) {
                            TabletStatusBar.this.mHandler.removeMessages(MSG_OPEN_NOTIFICATION_PEEK);
                            TabletStatusBar.this.mHandler.removeMessages(MSG_OPEN_NOTIFICATION_PANEL);
                            TabletStatusBar.this.mHandler.sendEmptyMessage(MSG_CLOSE_NOTIFICATION_PEEK);
                            TabletStatusBar.this.mHandler.sendEmptyMessage(MSG_OPEN_NOTIFICATION_PANEL);
                        }
                    }
                    return true;
                default:
                    return DEBUG_COMPAT_HELP;
            }
        }
    }

    private class NotificationTriggerTouchListener implements OnTouchListener {
        private Runnable mHiliteOnR;
        float mInitialTouchX;
        float mInitialTouchY;
        int mTouchSlop;
        VelocityTracker mVT;

        public NotificationTriggerTouchListener() {
            this.mHiliteOnR = new Runnable() {
                public void run() {
                    NotificationTriggerTouchListener.this.this$0.mNotificationArea.setBackgroundResource(17302500);
                }
            };
            this.mTouchSlop = ViewConfiguration.get(TabletStatusBar.this.getContext()).getScaledTouchSlop();
        }

        public void hilite(boolean on) {
            if (on) {
                TabletStatusBar.this.mNotificationArea.postDelayed(this.mHiliteOnR, 100);
            } else {
                TabletStatusBar.this.mNotificationArea.removeCallbacks(this.mHiliteOnR);
                TabletStatusBar.this.mNotificationArea.setBackgroundDrawable(null);
            }
        }

        public boolean onTouch(View v, MotionEvent event) {
            if ((TabletStatusBar.this.mDisabled & 65536) != 0) {
                return FAKE_SPACE_BAR;
            }
            int action = event.getAction();
            switch (action) {
                case CommandQueue.FLAG_EXCLUDE_NONE:
                    this.mVT = VelocityTracker.obtain();
                    this.mInitialTouchX = event.getX();
                    this.mInitialTouchY = event.getY();
                    hilite(FAKE_SPACE_BAR);
                    if (this.mVT != null) {
                        return FAKE_SPACE_BAR;
                    }
                    this.mVT.addMovement(event);
                    this.mVT.computeCurrentVelocity(MSG_OPEN_NOTIFICATION_PANEL);
                    if (this.mVT.getYVelocity() < ((float) (-TabletStatusBar.this.mNotificationFlingVelocity))) {
                        return FAKE_SPACE_BAR;
                    }
                    TabletStatusBar.this.animateExpand();
                    TabletStatusBar.this.visibilityChanged(FAKE_SPACE_BAR);
                    hilite(DEBUG_COMPAT_HELP);
                    this.mVT.recycle();
                    this.mVT = null;
                    return FAKE_SPACE_BAR;
                case CommandQueue.FLAG_EXCLUDE_SEARCH_PANEL:
                case RecentsCallback.SWIPE_DOWN:
                    hilite(DEBUG_COMPAT_HELP);
                    if (this.mVT != null) {
                        if (action == 1 && Math.abs(event.getX() - this.mInitialTouchX) < ((float) this.mTouchSlop) && Math.abs(event.getY() - this.mInitialTouchY) < ((float) (this.mTouchSlop / 3)) && ((int) event.getY()) < v.getBottom()) {
                            TabletStatusBar.this.animateExpand();
                            TabletStatusBar.this.visibilityChanged(FAKE_SPACE_BAR);
                            v.sendAccessibilityEvent(1);
                            v.playSoundEffect(0);
                        }
                        this.mVT.recycle();
                        this.mVT = null;
                        return FAKE_SPACE_BAR;
                    }
                    return false;
                case CommandQueue.FLAG_EXCLUDE_RECENTS_PANEL:
                case CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL:
                    if (this.mVT != null) {
                        return FAKE_SPACE_BAR;
                    }
                    this.mVT.addMovement(event);
                    this.mVT.computeCurrentVelocity(MSG_OPEN_NOTIFICATION_PANEL);
                    if (this.mVT.getYVelocity() < ((float) (-TabletStatusBar.this.mNotificationFlingVelocity))) {
                        return FAKE_SPACE_BAR;
                    }
                    TabletStatusBar.this.animateExpand();
                    TabletStatusBar.this.visibilityChanged(FAKE_SPACE_BAR);
                    hilite(DEBUG_COMPAT_HELP);
                    this.mVT.recycle();
                    this.mVT = null;
                    return FAKE_SPACE_BAR;
                default:
                    return false;
            }
        }
    }

    public TabletStatusBar() {
        this.mNaturalBarHeight = -1;
        this.mIconSize = -1;
        this.mIconHPadding = -1;
        this.mNavIconWidth = -1;
        this.mMenuNavIconWidth = -1;
        this.mMaxNotificationIcons = 5;
        this.mSpaceBarKeyEvent = null;
        this.mCompatibilityHelpDialog = null;
        this.mDisabled = 0;
        this.mSystemUiVisibility = 0;
        this.mNavigationIconHints = 0;
        this.mShowSearchHoldoff = 0;
        this.mShowSearchPanel = new Runnable() {
            public void run() {
                TabletStatusBar.this.showSearchPanel();
            }
        };
        this.mHomeSearchActionListener = new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case CommandQueue.FLAG_EXCLUDE_NONE:
                        if (!(TabletStatusBar.this.shouldDisableNavbarGestures() || TabletStatusBar.this.inKeyguardRestrictedInputMode())) {
                            TabletStatusBar.this.mHandler.removeCallbacks(TabletStatusBar.this.mShowSearchPanel);
                            TabletStatusBar.this.mHandler.postDelayed(TabletStatusBar.this.mShowSearchPanel, (long) TabletStatusBar.this.mShowSearchHoldoff);
                        }
                        break;
                    case CommandQueue.FLAG_EXCLUDE_SEARCH_PANEL:
                    case RecentsCallback.SWIPE_DOWN:
                        TabletStatusBar.this.mHandler.removeCallbacks(TabletStatusBar.this.mShowSearchPanel);
                        break;
                }
                return DEBUG_COMPAT_HELP;
            }
        };
        this.mOnClickListener = new OnClickListener() {
            public void onClick(View v) {
                if (v == TabletStatusBar.this.mRecentButton) {
                    TabletStatusBar.this.onClickRecentButton();
                } else if (v == TabletStatusBar.this.mInputMethodSwitchButton) {
                    TabletStatusBar.this.onClickInputMethodSwitchButton();
                } else if (v == TabletStatusBar.this.mCompatModeButton) {
                    TabletStatusBar.this.onClickCompatModeButton();
                }
            }
        };
        this.mBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if ("android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(action) || "android.intent.action.SCREEN_OFF".equals(action)) {
                    int flags = 0;
                    if ("android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(action)) {
                        String reason = intent.getStringExtra("reason");
                        if (reason != null && reason.equals(BaseStatusBar.SYSTEM_DIALOG_REASON_RECENT_APPS)) {
                            flags = 0 | 2;
                        }
                    }
                    if ("android.intent.action.SCREEN_OFF".equals(action)) {
                        TabletStatusBar.this.mRecentsPanel.show(DEBUG_COMPAT_HELP, DEBUG_COMPAT_HELP);
                        flags |= 2;
                    }
                    TabletStatusBar.this.animateCollapse(flags);
                }
            }
        };
    }

    static /* synthetic */ int access$1672(TabletStatusBar x0, int x1) {
        int i = x0.mSystemUiVisibility & x1;
        x0.mSystemUiVisibility = i;
        return i;
    }

    static /* synthetic */ int access$1676(TabletStatusBar x0, int x1) {
        int i = x0.mSystemUiVisibility | x1;
        x0.mSystemUiVisibility = i;
        return i;
    }

    private void addStatusBarWindow() {
        View sb = makeStatusBarView();
        LayoutParams lp = new LayoutParams(-1, -1, 2019, 8388680, 4);
        lp.gravity = getStatusBarGravity();
        lp.setTitle("SystemBar");
        lp.packageName = this.mContext.getPackageName();
        WindowManagerImpl.getDefault().addView(sb, lp);
    }

    private int getNotificationPanelHeight() {
        Resources res = this.mContext.getResources();
        Display d = WindowManagerImpl.getDefault().getDefaultDisplay();
        Point size = new Point();
        d.getRealSize(size);
        return Math.max(res.getDimensionPixelSize(R.dimen.notification_panel_min_height), size.y);
    }

    private boolean hasTicker(Notification n) {
        return (n.tickerView == null && TextUtils.isEmpty(n.tickerText)) ? DEBUG_COMPAT_HELP : FAKE_SPACE_BAR;
    }

    private void hideCompatibilityHelp() {
        if (this.mCompatibilityHelpDialog != null) {
            WindowManagerImpl.getDefault().removeView(this.mCompatibilityHelpDialog);
            this.mCompatibilityHelpDialog = null;
        }
    }

    private boolean isImmersive() {
        try {
            return ActivityManagerNative.getDefault().isTopActivityImmersive();
        } catch (RemoteException e) {
            return DEBUG_COMPAT_HELP;
        }
    }

    private void loadNotificationPanel() {
        int N = this.mNotificationData.size();
        ArrayList<View> toShow = new ArrayList();
        boolean provisioned = isDeviceProvisioned();
        int i = 0;
        while (i < N) {
            Entry ent = this.mNotificationData.get(N - i - 1);
            if (provisioned || showNotificationEvenIfUnprovisioned(ent.notification)) {
                toShow.add(ent.row);
            }
            i++;
        }
        ArrayList<View> toRemove = new ArrayList();
        i = 0;
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
        i = 0;
        while (i < toShow.size()) {
            View v = (View) toShow.get(i);
            if (v.getParent() == null) {
                this.mPile.addView(v, Math.min(toShow.size() - 1 - i, this.mPile.getChildCount()));
            }
            i++;
        }
        this.mNotificationPanel.setNotificationCount(toShow.size());
        this.mNotificationPanel.setSettingsEnabled(isDeviceProvisioned());
    }

    private void notifyUiVisibilityChanged() {
        try {
            this.mWindowManager.statusBarVisibilityChanged(this.mSystemUiVisibility);
        } catch (RemoteException e) {
        }
    }

    private void reloadAllNotificationIcons() {
        if (this.mIconLayout != null) {
            this.mIconLayout.removeAllViews();
            updateNotificationIcons();
        }
    }

    private void setNavigationVisibility(int visibility) {
        boolean disableHome;
        boolean disableRecent;
        boolean disableBack;
        boolean disableVolumeUp;
        boolean disableVolumeDown;
        int i;
        int i2 = CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL;
        boolean z = FAKE_SPACE_BAR;
        if ((2097152 & visibility) != 0) {
            disableHome = true;
        } else {
            disableHome = false;
        }
        if ((16777216 & visibility) != 0) {
            disableRecent = true;
        } else {
            disableRecent = false;
        }
        if ((4194304 & visibility) != 0) {
            disableBack = true;
        } else {
            disableBack = false;
        }
        if (visibility != 0) {
            disableVolumeUp = true;
        } else {
            disableVolumeUp = false;
        }
        if (visibility != 0) {
            disableVolumeDown = true;
        } else {
            disableVolumeDown = false;
        }
        ImageView imageView = this.mBackButton;
        if (disableBack) {
            i = 4;
        } else {
            i = 0;
        }
        imageView.setVisibility(i);
        View view = this.mHomeButton;
        if (disableHome) {
            i = 4;
        } else {
            i = 0;
        }
        view.setVisibility(i);
        view = this.mRecentButton;
        if (disableRecent) {
            i = 4;
        } else {
            i = 0;
        }
        view.setVisibility(i);
        if (this.mContext.getResources().getBoolean(R.bool.hasVolumeButton)) {
            view = this.mVolumeUpButton;
            if (disableVolumeUp) {
                i = 4;
            } else {
                i = 0;
            }
            view.setVisibility(i);
            View view2 = this.mVolumeDownButton;
            if (!disableVolumeDown) {
                i2 = 0;
            }
            view2.setVisibility(i2);
        }
        InputMethodButton inputMethodButton = this.mInputMethodSwitchButton;
        if ((1048576 & visibility) == 0) {
            z = false;
        }
        inputMethodButton.setScreenLocked(z);
    }

    private void showCompatibilityHelp() {
        if (this.mCompatibilityHelpDialog == null) {
            this.mCompatibilityHelpDialog = View.inflate(this.mContext, R.layout.compat_mode_help, null);
            this.mCompatibilityHelpDialog.findViewById(R.id.button).setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    TabletStatusBar.this.hideCompatibilityHelp();
                    Editor editor = Prefs.edit(TabletStatusBar.this.mContext);
                    editor.putBoolean(Prefs.SHOWN_COMPAT_MODE_HELP, FAKE_SPACE_BAR);
                    editor.apply();
                }
            });
            LayoutParams lp = new LayoutParams(-1, -1, 2008, 131840, -3);
            lp.setTitle("CompatibilityModeDialog");
            lp.softInputMode = 49;
            lp.windowAnimations = 16974311;
            WindowManagerImpl.getDefault().addView(this.mCompatibilityHelpDialog, lp);
        }
    }

    public void addIcon(String slot, int index, int viewIndex, StatusBarIcon icon) {
    }

    public void addNotification(IBinder key, StatusBarNotification notification) {
        addNotificationViews(key, notification);
        boolean immersive = isImmersive();
        if (notification.notification.fullScreenIntent != null) {
            Slog.w(TAG, "Notification has fullScreenIntent and activity is not immersive; sending fullScreenIntent");
            try {
                notification.notification.fullScreenIntent.send();
            } catch (CanceledException e) {
            }
        } else {
            tick(key, notification, FAKE_SPACE_BAR);
        }
        setAreThereNotifications();
    }

    protected void addPanelWindows() {
        Context context = this.mContext;
        Resources res = this.mContext.getResources();
        this.mNotificationPanel = (NotificationPanel) View.inflate(context, R.layout.system_bar_notification_panel, null);
        this.mNotificationPanel.setBar(this);
        this.mNotificationPanel.show(DEBUG_COMPAT_HELP, DEBUG_COMPAT_HELP);
        this.mNotificationPanel.setOnTouchListener(new TouchOutsideListener(1001, this.mNotificationPanel));
        this.mBatteryController.addIconView((ImageView) this.mNotificationPanel.findViewById(R.id.battery));
        this.mBatteryController.addLabelView((TextView) this.mNotificationPanel.findViewById(R.id.battery_text));
        this.mBluetoothController.addIconView((ImageView) this.mNotificationPanel.findViewById(R.id.bluetooth));
        ImageView mobileRSSI = (ImageView) this.mNotificationPanel.findViewById(R.id.mobile_signal);
        if (mobileRSSI != null) {
            this.mNetworkController.addPhoneSignalIconView(mobileRSSI);
        }
        ImageView wifiRSSI = (ImageView) this.mNotificationPanel.findViewById(R.id.wifi_signal);
        if (wifiRSSI != null) {
            this.mNetworkController.addWifiIconView(wifiRSSI);
        }
        this.mNetworkController.addWifiLabelView((TextView) this.mNotificationPanel.findViewById(R.id.wifi_text));
        this.mNetworkController.addDataTypeIconView((ImageView) this.mNotificationPanel.findViewById(R.id.mobile_type));
        this.mNetworkController.addMobileLabelView((TextView) this.mNotificationPanel.findViewById(R.id.mobile_text));
        this.mNetworkController.addCombinedLabelView((TextView) this.mBarContents.findViewById(R.id.network_text));
        this.mStatusBarView.setIgnoreChildren(0, this.mNotificationTrigger, this.mNotificationPanel);
        LayoutParams lp = new LayoutParams(res.getDimensionPixelSize(R.dimen.notification_panel_width), getNotificationPanelHeight(), 2024, 25297664, -3);
        this.mNotificationPanelParams = lp;
        lp.gravity = 85;
        lp.setTitle("NotificationPanel");
        lp.softInputMode = 49;
        lp.windowAnimations = 16973824;
        WindowManagerImpl.getDefault().addView(this.mNotificationPanel, lp);
        this.mRecentTasksLoader = new RecentTasksLoader(context);
        updateRecentsPanel();
        this.mStatusBarView.setBar(this);
        updateSearchPanel();
        this.mInputMethodsPanel = (InputMethodsPanel) View.inflate(context, R.layout.system_bar_input_methods_panel, null);
        this.mInputMethodsPanel.setHardKeyboardEnabledChangeListener(this);
        this.mInputMethodsPanel.setOnTouchListener(new TouchOutsideListener(1041, this.mInputMethodsPanel));
        this.mInputMethodsPanel.setImeSwitchButton(this.mInputMethodSwitchButton);
        this.mStatusBarView.setIgnoreChildren(CommandQueue.FLAG_EXCLUDE_RECENTS_PANEL, this.mInputMethodSwitchButton, this.mInputMethodsPanel);
        lp = new LayoutParams(-2, -2, 2014, 25297152, -3);
        lp.gravity = 85;
        lp.setTitle("InputMethodsPanel");
        lp.windowAnimations = 2131623947;
        WindowManagerImpl.getDefault().addView(this.mInputMethodsPanel, lp);
        this.mCompatModePanel = (CompatModePanel) View.inflate(context, R.layout.system_bar_compat_mode_panel, null);
        this.mCompatModePanel.setOnTouchListener(new TouchOutsideListener(1051, this.mCompatModePanel));
        this.mCompatModePanel.setTrigger(this.mCompatModeButton);
        this.mCompatModePanel.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
        this.mStatusBarView.setIgnoreChildren(RecentsCallback.SWIPE_DOWN, this.mCompatModeButton, this.mCompatModePanel);
        lp = new LayoutParams(250, -2, 2014, 25297152, -3);
        lp.gravity = 85;
        lp.setTitle("CompatModePanel");
        lp.windowAnimations = 16973826;
        WindowManagerImpl.getDefault().addView(this.mCompatModePanel, lp);
        this.mRecentButton.setOnTouchListener(this.mRecentsPanel);
        this.mPile = (NotificationRowLayout) this.mNotificationPanel.findViewById(R.id.content);
        this.mPile.removeAllViews();
        this.mPile.setLongPressListener(getNotificationLongClicker());
        ((ScrollView) this.mPile.getParent()).setFillViewport(FAKE_SPACE_BAR);
    }

    public void animateCollapse() {
        animateCollapse(0);
    }

    public void animateCollapse(int flags) {
        if ((flags & 4) == 0) {
            this.mHandler.removeMessages(MSG_CLOSE_NOTIFICATION_PANEL);
            this.mHandler.sendEmptyMessage(MSG_CLOSE_NOTIFICATION_PANEL);
        }
        if ((flags & 2) == 0) {
            this.mHandler.removeMessages(1021);
            this.mHandler.sendEmptyMessage(1021);
        }
        if ((flags & 1) == 0) {
            this.mHandler.removeMessages(1025);
            this.mHandler.sendEmptyMessage(1025);
        }
        if ((flags & 8) == 0) {
            this.mHandler.removeMessages(MSG_CLOSE_INPUT_METHODS_PANEL);
            this.mHandler.sendEmptyMessage(MSG_CLOSE_INPUT_METHODS_PANEL);
        }
        if ((flags & 16) == 0) {
            this.mHandler.removeMessages(MSG_CLOSE_COMPAT_MODE_PANEL);
            this.mHandler.sendEmptyMessage(MSG_CLOSE_COMPAT_MODE_PANEL);
        }
    }

    public void animateExpand() {
        this.mHandler.removeMessages(MSG_OPEN_NOTIFICATION_PANEL);
        this.mHandler.sendEmptyMessage(MSG_OPEN_NOTIFICATION_PANEL);
    }

    public void clearAll() {
        try {
            this.mBarService.onClearAllNotifications();
        } catch (RemoteException e) {
        }
        animateCollapse();
        visibilityChanged(DEBUG_COMPAT_HELP);
    }

    protected void createAndAddWindows() {
        addStatusBarWindow();
        addPanelWindows();
    }

    protected H createHandler() {
        return new H(null);
    }

    public void disable(int state) {
        int diff = state ^ this.mDisabled;
        this.mDisabled = state;
        if ((8388608 & diff) != 0) {
            boolean show = (8388608 & state) == 0;
            this.mStatusBarView.setShowVolume(show, this.mContext);
            Slog.i(TAG, "DISABLE_CLOCK: " + (show ? "no" : "yes"));
            showClock(show);
        }
        if ((1048576 & diff) != 0) {
            if ((1048576 & state) == 0) {
                show = true;
            } else {
                show = false;
            }
            Slog.i(TAG, "DISABLE_SYSTEM_INFO: " + (show ? "no" : "yes"));
            this.mNotificationTrigger.setVisibility(show ? 0 : CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
        }
        if (!((diff & 65536) == 0 || (state & 65536) == 0)) {
            Slog.i(TAG, "DISABLE_EXPAND: yes");
            animateCollapse();
            visibilityChanged(DEBUG_COMPAT_HELP);
        }
        if ((diff & 131072) != 0) {
            this.mNotificationDNDMode = Prefs.read(this.mContext).getBoolean(Prefs.DO_NOT_DISTURB_PREF, DEBUG_COMPAT_HELP);
            if ((state & 131072) != 0) {
                Slog.i(TAG, "DISABLE_NOTIFICATION_ICONS: yes" + (this.mNotificationDNDMode ? " (DND)" : ""));
                this.mTicker.halt();
            } else {
                Slog.i(TAG, "DISABLE_NOTIFICATION_ICONS: no" + (this.mNotificationDNDMode ? " (DND)" : ""));
            }
            reloadAllNotificationIcons();
        } else if (!((524288 & diff) == 0 || (524288 & state) == 0)) {
            this.mTicker.halt();
        }
        if ((23068672 & diff) != 0) {
            setNavigationVisibility(state);
            if ((16777216 & state) != 0) {
                this.mHandler.removeMessages(1021);
                this.mHandler.sendEmptyMessage(1021);
            }
        }
    }

    public void doneTicking() {
        this.mFeedbackIconArea.setVisibility(0);
    }

    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        pw.print("mDisabled=0x");
        pw.println(Integer.toHexString(this.mDisabled));
        pw.println("mNetworkController:");
        this.mNetworkController.dump(fd, pw, args);
    }

    public Context getContext() {
        return this.mContext;
    }

    protected int getExpandedViewMaxHeight() {
        return getNotificationPanelHeight();
    }

    protected LayoutParams getRecentsLayoutParams(ViewGroup.LayoutParams layoutParams) {
        LayoutParams lp = new LayoutParams((int) this.mContext.getResources().getDimension(R.dimen.status_bar_recents_width), -1, 2024, 25297152, -3);
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
        } else {
            lp.flags |= 2;
            lp.dimAmount = 0.7f;
        }
        lp.gravity = 83;
        lp.setTitle("SearchPanel");
        lp.windowAnimations = 16974313;
        lp.softInputMode = 49;
        return lp;
    }

    protected int getStatusBarGravity() {
        return 87;
    }

    public int getStatusBarHeight() {
        return this.mStatusBarView != null ? this.mStatusBarView.getHeight() : this.mContext.getResources().getDimensionPixelSize(17104907);
    }

    public View getStatusBarView() {
        return this.mStatusBarView;
    }

    protected void haltTicker() {
        this.mTicker.halt();
    }

    public void hideSearchPanel() {
        super.hideSearchPanel();
        LayoutParams lp = (LayoutParams) this.mStatusBarView.getLayoutParams();
        lp.flags |= 32;
        WindowManagerImpl.getDefault().updateViewLayout(this.mStatusBarView, lp);
    }

    protected boolean isTopNotification(ViewGroup parent, Entry entry) {
        return (parent == null || entry == null || parent.indexOfChild(entry.row) != parent.getChildCount() - 1) ? DEBUG_COMPAT_HELP : FAKE_SPACE_BAR;
    }

    protected void loadDimens() {
        Resources res = this.mContext.getResources();
        this.mNaturalBarHeight = res.getDimensionPixelSize(17104907);
        int newIconSize = res.getDimensionPixelSize(17104913);
        int newIconHPadding = res.getDimensionPixelSize(R.dimen.status_bar_icon_padding);
        int newNavIconWidth = res.getDimensionPixelSize(R.dimen.navigation_key_width);
        int newMenuNavIconWidth = res.getDimensionPixelSize(R.dimen.navigation_menu_key_width);
        if (!(this.mNavigationArea == null || newNavIconWidth == this.mNavIconWidth)) {
            this.mNavIconWidth = newNavIconWidth;
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(this.mNavIconWidth, -1);
            this.mBackButton.setLayoutParams(lp);
            this.mHomeButton.setLayoutParams(lp);
            this.mRecentButton.setLayoutParams(lp);
        }
        if (!(this.mNavigationArea == null || newMenuNavIconWidth == this.mMenuNavIconWidth)) {
            this.mMenuNavIconWidth = newMenuNavIconWidth;
            this.mMenuButton.setLayoutParams(new LinearLayout.LayoutParams(this.mMenuNavIconWidth, -1));
        }
        if (!(newIconHPadding == this.mIconHPadding && newIconSize == this.mIconSize)) {
            this.mIconHPadding = newIconHPadding;
            this.mIconSize = newIconSize;
            reloadAllNotificationIcons();
        }
        int numIcons = res.getInteger(R.integer.config_maxNotificationIcons);
        if (numIcons != this.mMaxNotificationIcons) {
            this.mMaxNotificationIcons = numIcons;
            reloadAllNotificationIcons();
        }
    }

    protected View makeStatusBarView() {
        Context context = this.mContext;
        this.mWindowManager = Stub.asInterface(ServiceManager.getService("window"));
        loadDimens();
        TabletStatusBarView sb = (TabletStatusBarView) View.inflate(context, R.layout.system_bar, null);
        this.mStatusBarView = sb;
        sb.setHandler(this.mHandler);
        try {
            if (this.mWindowManager.hasNavigationBar()) {
                Slog.e(TAG, "Tablet device cannot show navigation bar and system bar");
            }
        } catch (RemoteException e) {
        }
        this.mBarContents = (ViewGroup) sb.findViewById(R.id.bar_contents);
        this.mNotificationArea = sb.findViewById(R.id.notificationArea);
        this.mNotificationArea.setOnTouchListener(new NotificationTriggerTouchListener());
        this.mNotificationTrigger = sb.findViewById(R.id.notificationTrigger);
        this.mNotificationIconArea = (NotificationIconArea) sb.findViewById(R.id.notificationIcons);
        this.mIconLayout = (IconLayout) sb.findViewById(R.id.icons);
        ViewConfiguration vc = ViewConfiguration.get(context);
        this.mNotificationPeekTapDuration = ViewConfiguration.getTapTimeout();
        this.mNotificationFlingVelocity = 300;
        this.mTicker = new TabletTicker(this);
        this.mLocationController = new LocationController(this.mContext);
        this.mDoNotDisturb = new DoNotDisturb(this.mContext);
        this.mBatteryController = new BatteryController(this.mContext);
        this.mDisplayController = new DisplayController(this.mContext);
        this.mBatteryController.addIconView((ImageView) sb.findViewById(R.id.battery));
        this.mBluetoothController = new BluetoothController(this.mContext);
        this.mBluetoothController.addIconView((ImageView) sb.findViewById(R.id.bluetooth));
        this.mNetworkController = new NetworkController(this.mContext);
        this.mNetworkController.addSignalCluster((SignalClusterView) sb.findViewById(R.id.signal_cluster));
        this.mBackButton = (ImageView) sb.findViewById(R.id.back);
        this.mNavigationArea = (ViewGroup) sb.findViewById(R.id.navigationArea);
        this.mHomeButton = this.mNavigationArea.findViewById(R.id.home);
        this.mMenuButton = this.mNavigationArea.findViewById(R.id.menu);
        this.mVolumeDownButton = this.mNavigationArea.findViewById(R.id.volume_down);
        this.mVolumeUpButton = this.mNavigationArea.findViewById(R.id.volume_up);
        this.mRecentButton = this.mNavigationArea.findViewById(R.id.recent_apps);
        this.mRecentButton.setOnClickListener(this.mOnClickListener);
        LayoutTransition lt = new LayoutTransition();
        lt.setDuration(250);
        lt.setDuration(0, 0);
        lt.setDuration(1, 0);
        lt.addTransitionListener(new TransitionListener() {
            public void endTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {
                TabletStatusBar.this.mBarContents.invalidate();
            }

            public void startTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {
            }
        });
        this.mNavigationArea.setLayoutTransition(lt);
        this.mNavigationArea.setMotionEventSplittingEnabled(DEBUG_COMPAT_HELP);
        this.mFeedbackIconArea = (ViewGroup) sb.findViewById(R.id.feedbackIconArea);
        this.mInputMethodSwitchButton = (InputMethodButton) sb.findViewById(R.id.imeSwitchButton);
        this.mInputMethodSwitchButton.setOnClickListener(this.mOnClickListener);
        this.mCompatModeButton = (CompatModeButton) sb.findViewById(R.id.compatModeButton);
        this.mCompatModeButton.setOnClickListener(this.mOnClickListener);
        this.mCompatModeButton.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
        this.mFakeSpaceBar = sb.findViewById(R.id.fake_space_bar);
        this.mShadow = sb.findViewById(R.id.bar_shadow);
        this.mShadow.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent ev) {
                if (ev.getAction() == 0) {
                    TabletStatusBar.this.mShadow.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
                    TabletStatusBar.this.mBarContents.setVisibility(0);
                    try {
                        TabletStatusBar.this.mBarService.setSystemUiVisibility(0, 1);
                    } catch (RemoteException e) {
                    }
                }
                return DEBUG_COMPAT_HELP;
            }
        });
        LayoutTransition xition = new LayoutTransition();
        float[] fArr = new float[2];
        xition.setAnimator(CommandQueue.FLAG_EXCLUDE_RECENTS_PANEL, ObjectAnimator.ofFloat(null, "alpha", new float[]{0.5f, 1.0f}));
        xition.setDuration(CommandQueue.FLAG_EXCLUDE_RECENTS_PANEL, 200);
        xition.setStartDelay(CommandQueue.FLAG_EXCLUDE_RECENTS_PANEL, 0);
        fArr = new float[2];
        xition.setAnimator(RecentsCallback.SWIPE_DOWN, ObjectAnimator.ofFloat(null, "alpha", new float[]{1.0f, 0.0f}));
        xition.setDuration(RecentsCallback.SWIPE_DOWN, 750);
        xition.setStartDelay(RecentsCallback.SWIPE_DOWN, 0);
        ((ViewGroup) sb.findViewById(R.id.bar_contents_holder)).setLayoutTransition(xition);
        xition = new LayoutTransition();
        fArr = new float[2];
        xition.setAnimator(CommandQueue.FLAG_EXCLUDE_RECENTS_PANEL, ObjectAnimator.ofFloat(null, "alpha", new float[]{0.0f, 1.0f}));
        xition.setDuration(CommandQueue.FLAG_EXCLUDE_RECENTS_PANEL, 750);
        xition.setStartDelay(CommandQueue.FLAG_EXCLUDE_RECENTS_PANEL, 0);
        fArr = new float[2];
        xition.setAnimator(RecentsCallback.SWIPE_DOWN, ObjectAnimator.ofFloat(null, "alpha", new float[]{1.0f, 0.0f}));
        xition.setDuration(RecentsCallback.SWIPE_DOWN, 0);
        xition.setStartDelay(RecentsCallback.SWIPE_DOWN, 0);
        ((ViewGroup) sb.findViewById(R.id.bar_shadow_holder)).setLayoutTransition(xition);
        setAreThereNotifications();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
        filter.addAction("android.intent.action.SCREEN_OFF");
        context.registerReceiver(this.mBroadcastReceiver, filter);
        return sb;
    }

    public void onBarHeightChanged(int height) {
        LayoutParams lp = (LayoutParams) this.mStatusBarView.getLayoutParams();
        if (lp != null && lp.height != height) {
            lp.height = height;
            WindowManagerImpl.getDefault().updateViewLayout(this.mStatusBarView, lp);
        }
    }

    public void onClickCompatModeButton() {
        int msg = this.mCompatModePanel.getVisibility() == 8 ? MSG_OPEN_COMPAT_MODE_PANEL : MSG_CLOSE_COMPAT_MODE_PANEL;
        this.mHandler.removeMessages(msg);
        this.mHandler.sendEmptyMessage(msg);
    }

    public void onClickInputMethodSwitchButton() {
        int msg = this.mInputMethodsPanel.getVisibility() == 8 ? MSG_OPEN_INPUT_METHODS_PANEL : MSG_CLOSE_INPUT_METHODS_PANEL;
        this.mHandler.removeMessages(msg);
        this.mHandler.sendEmptyMessage(msg);
    }

    public void onClickRecentButton() {
        if ((this.mDisabled & 65536) == 0) {
            int msg = this.mRecentsPanel.getVisibility() == 0 ? 1021 : 1020;
            this.mHandler.removeMessages(msg);
            this.mHandler.sendEmptyMessage(msg);
        }
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        loadDimens();
        this.mNotificationPanelParams.height = getNotificationPanelHeight();
        WindowManagerImpl.getDefault().updateViewLayout(this.mNotificationPanel, this.mNotificationPanelParams);
        this.mRecentsPanel.updateValuesFromResources();
        this.mShowSearchHoldoff = this.mContext.getResources().getInteger(R.integer.config_show_search_delay);
        updateSearchPanel();
    }

    public void onHardKeyboardEnabledChange(boolean enabled) {
        try {
            this.mBarService.setHardKeyboardEnabled(enabled);
        } catch (RemoteException e) {
        }
    }

    public void onRecentsPanelVisibilityChanged(boolean visible) {
        boolean altBack = (visible || this.mAltBackButtonEnabledForIme) ? FAKE_SPACE_BAR : DEBUG_COMPAT_HELP;
        this.mCommandQueue.setNavigationIconHints(altBack ? this.mNavigationIconHints | 8 : this.mNavigationIconHints & -9);
    }

    public void removeIcon(String slot, int index, int viewIndex) {
    }

    public void removeNotification(IBinder key) {
        removeNotificationViews(key);
        this.mTicker.remove(key);
        setAreThereNotifications();
    }

    public void resetNotificationPeekFadeTimer() {
        this.mHandler.removeMessages(MSG_CLOSE_NOTIFICATION_PEEK);
        this.mHandler.sendEmptyMessageDelayed(MSG_CLOSE_NOTIFICATION_PEEK, 3000);
    }

    protected void setAreThereNotifications() {
        if (this.mNotificationPanel != null) {
            NotificationPanel notificationPanel = this.mNotificationPanel;
            boolean z = (isDeviceProvisioned() && this.mNotificationData.hasClearableItems()) ? FAKE_SPACE_BAR : DEBUG_COMPAT_HELP;
            notificationPanel.setClearable(z);
        }
    }

    public void setHardKeyboardStatus(boolean available, boolean enabled) {
        this.mInputMethodSwitchButton.setHardKeyboardStatus(available);
        updateNotificationIcons();
        this.mInputMethodsPanel.setHardKeyboardStatus(available, enabled);
    }

    public void setImeWindowStatus(IBinder token, int vis, int backDisposition) {
        boolean z;
        boolean altBack;
        int i = 0;
        InputMethodButton inputMethodButton = this.mInputMethodSwitchButton;
        if ((vis & 1) != 0) {
            z = true;
        } else {
            z = false;
        }
        inputMethodButton.setImeWindowStatus(token, z);
        updateNotificationIcons();
        this.mInputMethodsPanel.setImeToken(token);
        if (backDisposition == 2 || (vis & 2) != 0) {
            altBack = true;
        } else {
            altBack = false;
        }
        this.mAltBackButtonEnabledForIme = altBack;
        this.mCommandQueue.setNavigationIconHints(altBack ? this.mNavigationIconHints | 8 : this.mNavigationIconHints & -9);
        View view = this.mFakeSpaceBar;
        if ((vis & 2) == 0) {
            i = CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL;
        }
        view.setVisibility(i);
    }

    public void setLightsOn(boolean on) {
        if (this.mMenuButton.getVisibility() == 0) {
            on = FAKE_SPACE_BAR;
        }
        Slog.v(TAG, "setLightsOn(" + on + ")");
        if (on) {
            setSystemUiVisibility(0, 1);
        } else {
            setSystemUiVisibility(1, 1);
        }
    }

    public void setNavigationIconHints(int hints) {
        float f = 0.5f;
        if (hints != this.mNavigationIconHints) {
            float f2;
            this.mNavigationIconHints = hints;
            this.mBackButton.setAlpha((hints & 1) != 0 ? 0.5f : 1.0f);
            View view = this.mHomeButton;
            if ((hints & 2) != 0) {
                f2 = 0.5f;
            } else {
                f2 = 1.0f;
            }
            view.setAlpha(f2);
            View view2 = this.mRecentButton;
            if ((hints & 4) == 0) {
                f = 1.0f;
            }
            view2.setAlpha(f);
            this.mBackButton.setImageResource((hints & 8) != 0 ? R.drawable.ic_sysbar_back_ime : R.drawable.ic_sysbar_back);
        }
    }

    public void setSystemUiVisibility(int vis, int mask) {
        int i = MSG_SHOW_CHROME;
        int oldVal = this.mSystemUiVisibility;
        int newVal = ((mask ^ -1) & oldVal) | (vis & mask);
        int diff = newVal ^ oldVal;
        if (diff != 0) {
            this.mSystemUiVisibility = newVal;
            if ((diff & 1) != 0) {
                this.mHandler.removeMessages(MSG_HIDE_CHROME);
                this.mHandler.removeMessages(MSG_SHOW_CHROME);
                H h = this.mHandler;
                if ((vis & 1) != 0) {
                    i = 1031;
                }
                h.sendEmptyMessage(i);
            }
            notifyUiVisibilityChanged();
        }
    }

    protected boolean shouldDisableNavbarGestures() {
        return (this.mNotificationPanel.getVisibility() == 0 || (this.mDisabled & 2097152) != 0) ? FAKE_SPACE_BAR : DEBUG_COMPAT_HELP;
    }

    public void showClock(boolean show) {
        int i = 0;
        View clock = this.mBarContents.findViewById(R.id.clock);
        View network_text = this.mBarContents.findViewById(R.id.network_text);
        if (clock != null) {
            clock.setVisibility(show ? 0 : 8);
        }
        if (network_text != null) {
            if (show) {
                i = 8;
            }
            network_text.setVisibility(i);
        }
    }

    public void showSearchPanel() {
        super.showSearchPanel();
        LayoutParams lp = (LayoutParams) this.mStatusBarView.getLayoutParams();
        lp.flags &= -33;
        WindowManagerImpl.getDefault().updateViewLayout(this.mStatusBarView, lp);
    }

    public void start() {
        super.start();
        this.mStorageManager = (StorageManager) this.mContext.getSystemService("storage");
        this.mStorageManager.registerListener(new StorageNotification(this.mContext));
    }

    protected void tick(IBinder key, StatusBarNotification n, boolean firstTime) {
        if (!this.mNotificationPanel.isShowing()) {
            if ((firstTime || (n.notification.flags & 8) == 0) && hasTicker(n.notification) && this.mStatusBarView.getWindowToken() != null && (this.mDisabled & 655360) == 0) {
                this.mTicker.add(key, n);
                this.mFeedbackIconArea.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
            }
        }
    }

    public void topAppWindowChanged(boolean showMenu) {
        if (showMenu) {
            setLightsOn(FAKE_SPACE_BAR);
        }
        this.mCompatModeButton.refresh();
        if (this.mCompatModeButton.getVisibility() != 0) {
            hideCompatibilityHelp();
            this.mCompatModePanel.closePanel();
        } else if (!Prefs.read(this.mContext).getBoolean(Prefs.SHOWN_COMPAT_MODE_HELP, DEBUG_COMPAT_HELP)) {
            showCompatibilityHelp();
        }
    }

    protected void updateExpandedViewPos(int expandedPosition) {
    }

    public void updateIcon(String slot, int index, int viewIndex, StatusBarIcon old, StatusBarIcon icon) {
    }

    protected void updateNotificationIcons() {
        if (this.mIconLayout != null) {
            loadNotificationPanel();
            int i = this.mIconSize + this.mIconHPadding * 2;
            int i2 = this.mNaturalBarHeight;
            ViewGroup.LayoutParams layoutParams = params;
            if (this.mNotificationDNDMode) {
                if (this.mIconLayout.getChildCount() == 0) {
                    Notification dndNotification = new Builder(this.mContext).setContentTitle(this.mContext.getText(R.string.notifications_off_title)).setContentText(this.mContext.getText(R.string.notifications_off_text)).setSmallIcon(R.drawable.ic_notification_dnd).setOngoing(FAKE_SPACE_BAR).getNotification();
                    StatusBarIconView iconView = new StatusBarIconView(this.mContext, "_dnd", dndNotification);
                    iconView.setImageResource(R.drawable.ic_notification_dnd);
                    iconView.setScaleType(ScaleType.CENTER_INSIDE);
                    iconView.setPadding(this.mIconHPadding, 0, this.mIconHPadding, 0);
                    StatusBarNotification statusBarNotification = new StatusBarNotification("", 0, "", 0, 0, 2, dndNotification);
                    Entry entry = entry;
                    IBinder iBinder = null;
                    this.mNotificationDNDDummyEntry = entry;
                    this.mIconLayout.addView(iconView, params);
                }
            } else if ((this.mDisabled & 131072) == 0) {
                int N = this.mNotificationData.size();
                ArrayList<View> toShow = new ArrayList();
                int maxNotificationIconsCount = this.mMaxNotificationIcons;
                if (this.mInputMethodSwitchButton.getVisibility() != 8) {
                    maxNotificationIconsCount--;
                }
                if (this.mCompatModeButton.getVisibility() != 8) {
                    maxNotificationIconsCount--;
                }
                boolean provisioned = isDeviceProvisioned();
                int i3 = 0;
                while (toShow.size() < maxNotificationIconsCount && i3 < N) {
                    Entry ent = this.mNotificationData.get(N - i3 - 1);
                    if ((provisioned && ent.notification.score >= -10) || showNotificationEvenIfUnprovisioned(ent.notification)) {
                        toShow.add(ent.icon);
                    }
                    i3++;
                }
                ArrayList<View> toRemove = new ArrayList();
                i3 = 0;
                while (i3 < this.mIconLayout.getChildCount()) {
                    View child = this.mIconLayout.getChildAt(i3);
                    if (!toShow.contains(child)) {
                        toRemove.add(child);
                    }
                    i3++;
                }
                Iterator i$ = toRemove.iterator();
                while (i$.hasNext()) {
                    this.mIconLayout.removeView((View) i$.next());
                }
                i3 = 0;
                while (i3 < toShow.size()) {
                    View v = (View) toShow.get(i3);
                    v.setPadding(this.mIconHPadding, 0, this.mIconHPadding, 0);
                    if (v.getParent() == null) {
                        this.mIconLayout.addView(v, i3, params);
                    }
                    i3++;
                }
            }
        }
    }

    protected void updateRecentsPanel() {
        super.updateRecentsPanel(R.layout.system_bar_recent_panel);
        this.mRecentsPanel.setStatusBarView(this.mStatusBarView);
    }

    protected void updateSearchPanel() {
        super.updateSearchPanel();
        this.mSearchPanelView.setStatusBarView(this.mStatusBarView);
        this.mStatusBarView.setDelegateView(this.mSearchPanelView);
    }

    protected void workAroundBadLayerDrawableOpacity(View v) {
        Drawable bgd = v.getBackground();
        if (bgd instanceof LayerDrawable) {
            LayerDrawable d = (LayerDrawable) bgd;
            v.setBackgroundDrawable(null);
            d.setOpacity(-3);
            v.setBackgroundDrawable(d);
        }
    }
}