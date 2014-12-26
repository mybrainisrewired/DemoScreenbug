package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import com.android.systemui.ExpandHelper;
import com.android.systemui.R;
import com.android.systemui.recent.RecentsCallback;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.policy.NotificationRowLayout;

public class StatusBarWindowView extends FrameLayout {
    private static final String TAG = "StatusBarWindowView";
    private NotificationRowLayout latestItems;
    private ExpandHelper mExpandHelper;
    PhoneStatusBar mService;

    public StatusBarWindowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setMotionEventSplittingEnabled(false);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean down = event.getAction() == 0;
        switch (event.getKeyCode()) {
            case CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL:
                if (down) {
                    return true;
                }
                this.mService.animateCollapse();
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.latestItems = (NotificationRowLayout) findViewById(R.id.latestItems);
        ScrollView scroller = (ScrollView) findViewById(R.id.scroll);
        this.mExpandHelper = new ExpandHelper(this.mContext, this.latestItems, getResources().getDimensionPixelSize(R.dimen.notification_row_min_height), getResources().getDimensionPixelSize(R.dimen.notification_row_max_height));
        this.mExpandHelper.setEventSource(this);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        MotionEvent cancellation = MotionEvent.obtain(ev);
        cancellation.setAction(RecentsCallback.SWIPE_DOWN);
        boolean intercept = this.mExpandHelper.onInterceptTouchEvent(ev) || super.onInterceptTouchEvent(ev);
        if (intercept) {
            this.latestItems.onInterceptTouchEvent(cancellation);
        }
        return intercept;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        return this.mExpandHelper.onTouchEvent(ev) || super.onTouchEvent(ev);
    }
}