package com.android.systemui.statusbar.tablet;

import android.animation.LayoutTransition;
import android.animation.LayoutTransition.TransitionListener;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Slog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.WindowManagerImpl;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.internal.statusbar.StatusBarIcon;
import com.android.internal.statusbar.StatusBarNotification;
import com.android.systemui.R;
import com.android.systemui.statusbar.StatusBarIconView;

public class TabletTicker extends Handler implements TransitionListener {
    private static final int ADVANCE_DELAY = 5000;
    private static final boolean CLICKABLE_TICKER = true;
    private static final int MSG_ADVANCE = 1;
    private static final int QUEUE_LENGTH = 3;
    private static final String TAG = "StatusBar.TabletTicker";
    private TabletStatusBar mBar;
    private Context mContext;
    private IBinder mCurrentKey;
    private StatusBarNotification mCurrentNotification;
    private View mCurrentView;
    private IBinder[] mKeys;
    private final int mLargeIconHeight;
    private LayoutTransition mLayoutTransition;
    private StatusBarNotification[] mQueue;
    private int mQueuePos;
    private ViewGroup mWindow;
    private boolean mWindowShouldClose;

    class AnonymousClass_1 implements OnClickListener {
        final /* synthetic */ OnClickListener val$clicker;

        AnonymousClass_1(OnClickListener onClickListener) {
            this.val$clicker = onClickListener;
        }

        public void onClick(View v) {
            TabletTicker.this.halt();
            this.val$clicker.onClick(v);
        }
    }

    public TabletTicker(TabletStatusBar bar) {
        this.mKeys = new IBinder[3];
        this.mQueue = new StatusBarNotification[3];
        this.mBar = bar;
        this.mContext = bar.getContext();
        this.mLargeIconHeight = this.mContext.getResources().getDimensionPixelSize(17104902);
    }

    private void advance() {
        boolean z = CLICKABLE_TICKER;
        if (this.mCurrentView != null) {
            if (this.mWindow != null) {
                this.mWindow.removeView(this.mCurrentView);
            }
            this.mCurrentView = null;
            this.mCurrentKey = null;
            this.mCurrentNotification = null;
        }
        dequeue();
        while (this.mCurrentNotification != null) {
            this.mCurrentView = makeTickerView(this.mCurrentNotification);
            if (this.mCurrentView != null) {
                if (this.mWindow == null) {
                    this.mWindow = makeWindow();
                    WindowManagerImpl.getDefault().addView(this.mWindow, this.mWindow.getLayoutParams());
                }
                this.mWindow.addView(this.mCurrentView);
                sendEmptyMessageDelayed(MSG_ADVANCE, 5000);
                break;
            } else {
                dequeue();
            }
        }
        if (this.mCurrentView != null || this.mWindow == null) {
            z = false;
        }
        this.mWindowShouldClose = z;
    }

    private void dequeue() {
        this.mCurrentKey = this.mKeys[0];
        this.mCurrentNotification = this.mQueue[0];
        int N = this.mQueuePos;
        int i = 0;
        while (i < N) {
            this.mKeys[i] = this.mKeys[i + 1];
            this.mQueue[i] = this.mQueue[i + 1];
            i++;
        }
        this.mKeys[N] = null;
        this.mQueue[N] = null;
        if (this.mQueuePos > 0) {
            this.mQueuePos--;
        }
    }

    private View makeTickerView(StatusBarNotification notification) {
        LayoutParams lp;
        Notification n = notification.notification;
        LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
        int iconId = n.largeIcon != null ? R.id.right_icon : R.id.left_icon;
        ViewGroup group;
        if (n.tickerView != null) {
            group = (ViewGroup) inflater.inflate(R.layout.system_bar_ticker_panel, null, false);
            FrameLayout content = (FrameLayout) group.findViewById(R.id.ticker_expanded);
            View expanded = null;
            Exception exception = null;
            try {
                expanded = n.tickerView.apply(this.mContext, content);
            } catch (RuntimeException e) {
                exception = e;
            }
            if (expanded == null) {
                Slog.e(TAG, "couldn't inflate view for notification " + (notification.pkg + "/0x" + Integer.toHexString(notification.id)), exception);
                return null;
            } else {
                LayoutParams layoutParams = lp;
                content.addView(expanded, lp);
            }
        } else if (n.tickerText != null) {
            group = inflater.inflate(R.layout.system_bar_ticker_compat, this.mWindow, false);
            ImageView iv = (ImageView) group.findViewById(iconId);
            iv.setImageDrawable(StatusBarIconView.getIcon(this.mContext, new StatusBarIcon(notification.pkg, n.icon, n.iconLevel, 0, n.tickerText)));
            iv.setVisibility(0);
            ((TextView) group.findViewById(R.id.text)).setText(n.tickerText);
        } else {
            throw new RuntimeException("tickerView==null && tickerText==null");
        }
        ImageView largeIcon = (ImageView) group.findViewById(R.id.large_icon);
        if (n.largeIcon != null) {
            largeIcon.setImageBitmap(n.largeIcon);
            largeIcon.setVisibility(0);
            lp = largeIcon.getLayoutParams();
            int statusBarHeight = this.mBar.getStatusBarHeight();
            if (n.largeIcon.getHeight() <= statusBarHeight) {
                lp.height = statusBarHeight;
            } else {
                lp.height = this.mLargeIconHeight;
            }
            largeIcon.setLayoutParams(lp);
        }
        PendingIntent contentIntent = notification.notification.contentIntent;
        if (contentIntent != null) {
            group.setOnClickListener(new AnonymousClass_1(this.mBar.makeClicker(contentIntent, notification.pkg, notification.tag, notification.id)));
        } else {
            group.setOnClickListener(null);
        }
        return group;
    }

    private ViewGroup makeWindow() {
        Resources res = this.mContext.getResources();
        FrameLayout view = new FrameLayout(this.mContext);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(res.getDimensionPixelSize(R.dimen.notification_ticker_width), this.mLargeIconHeight, 2024, 776 | 32, -3);
        lp.gravity = 85;
        this.mLayoutTransition = new LayoutTransition();
        this.mLayoutTransition.addTransitionListener(this);
        view.setLayoutTransition(this.mLayoutTransition);
        lp.setTitle("NotificationTicker");
        view.setLayoutParams(lp);
        return view;
    }

    public void add(IBinder key, StatusBarNotification notification) {
        remove(key, false);
        this.mKeys[this.mQueuePos] = key;
        this.mQueue[this.mQueuePos] = notification;
        if (this.mQueuePos == 0 && this.mCurrentNotification == null) {
            sendEmptyMessage(MSG_ADVANCE);
        }
        if (this.mQueuePos < 2) {
            this.mQueuePos++;
        }
    }

    public void endTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {
        if (this.mWindowShouldClose) {
            WindowManagerImpl.getDefault().removeView(this.mWindow);
            this.mWindow = null;
            this.mWindowShouldClose = false;
            this.mBar.doneTicking();
        }
    }

    public void halt() {
        removeMessages(MSG_ADVANCE);
        if (this.mCurrentView != null || this.mQueuePos != 0) {
            int i = 0;
            while (i < 3) {
                this.mKeys[i] = null;
                this.mQueue[i] = null;
                i++;
            }
            this.mQueuePos = 0;
            sendEmptyMessage(MSG_ADVANCE);
        }
    }

    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_ADVANCE:
                advance();
            default:
                break;
        }
    }

    public void remove(IBinder key) {
        remove(key, CLICKABLE_TICKER);
    }

    public void remove(IBinder key, boolean advance) {
        if (this.mCurrentKey != key) {
            int i = 0;
            while (i < 3) {
                if (this.mKeys[i] == key) {
                    while (i < 2) {
                        this.mKeys[i] = this.mKeys[i + 1];
                        this.mQueue[i] = this.mQueue[i + 1];
                        i++;
                    }
                    this.mKeys[2] = null;
                    this.mQueue[2] = null;
                    if (this.mQueuePos > 0) {
                        this.mQueuePos--;
                        return;
                    } else {
                        return;
                    }
                } else {
                    i++;
                }
            }
        } else if (advance) {
            removeMessages(MSG_ADVANCE);
            sendEmptyMessage(MSG_ADVANCE);
        }
    }

    public void startTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {
    }
}