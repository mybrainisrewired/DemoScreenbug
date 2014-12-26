package com.android.systemui.statusbar.tablet;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import com.android.systemui.R;
import com.android.systemui.statusbar.BaseStatusBar;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.DelegateViewHelper;
import com.android.systemui.statusbar.policy.KeyButtonView;

public class TabletStatusBarView extends FrameLayout {
    private final int MAX_PANELS;
    private boolean displayLock;
    private Context mContext;
    private DelegateViewHelper mDelegateHelper;
    private Handler mHandler;
    private final View[] mIgnoreChildren;
    private final View[] mPanels;
    private final int[] mPos;

    public TabletStatusBarView(Context context) {
        this(context, null);
        this.mContext = context;
    }

    public TabletStatusBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.displayLock = true;
        this.MAX_PANELS = 5;
        this.mIgnoreChildren = new View[5];
        this.mPanels = new View[5];
        this.mPos = new int[2];
        this.mContext = context;
        this.mDelegateHelper = new DelegateViewHelper(this);
    }

    private boolean eventInside(View v, MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        int[] p = this.mPos;
        v.getLocationInWindow(p);
        return x >= p[0] && x < p[0] + v.getWidth() && y >= p[1] && y < p[1] + v.getHeight();
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == 0) {
            this.mHandler.removeMessages(MSG_CLOSE_NOTIFICATION_PANEL);
            this.mHandler.sendEmptyMessage(MSG_CLOSE_NOTIFICATION_PANEL);
            this.mHandler.removeMessages(MSG_CLOSE_INPUT_METHODS_PANEL);
            this.mHandler.sendEmptyMessage(MSG_CLOSE_INPUT_METHODS_PANEL);
            this.mHandler.removeMessages(MSG_STOP_TICKER);
            this.mHandler.sendEmptyMessage(MSG_STOP_TICKER);
            int i = 0;
            while (i < this.mPanels.length) {
                if (this.mPanels[i] != null && this.mPanels[i].getVisibility() == 0 && eventInside(this.mIgnoreChildren[i], ev)) {
                    return true;
                }
                i++;
            }
        }
        return (this.mDelegateHelper == null || !this.mDelegateHelper.onInterceptTouchEvent(ev)) ? super.onInterceptTouchEvent(ev) : true;
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        View view = findViewById(R.id.navigationArea);
        if (view == null) {
            view = findViewById(R.id.nav_buttons);
        }
        Configuration config = this.mContext.getResources().getConfiguration();
        KeyButtonView volume_up = (KeyButtonView) findViewById(R.id.volume_up);
        KeyButtonView volume_down = (KeyButtonView) findViewById(R.id.volume_down);
        KeyButtonView menu = (KeyButtonView) findViewById(R.id.menu);
        if (!this.displayLock) {
            volume_up.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
            volume_down.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
            menu.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
        } else if (config.screenWidthDp > 480) {
            volume_up.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
            volume_down.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
            menu.setVisibility(0);
        } else {
            volume_up.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
            volume_down.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
        }
        this.mDelegateHelper.setSourceView(view);
        this.mDelegateHelper.setInitialTouchRegion(new View[]{view});
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        KeyButtonView volume_up = (KeyButtonView) findViewById(R.id.volume_up);
        KeyButtonView volume_down = (KeyButtonView) findViewById(R.id.volume_down);
        KeyButtonView menu = (KeyButtonView) findViewById(R.id.menu);
        View navigationArea = findViewById(R.id.navigationArea);
        View mNotificationArea = findViewById(R.id.notificationArea);
        if (!this.displayLock) {
            return;
        }
        if (w < navigationArea.getWidth() + mNotificationArea.getWidth()) {
            volume_up.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
            volume_down.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
        } else {
            if (this.mContext.getResources().getBoolean(R.bool.hasVolumeButton)) {
                volume_up.setVisibility(0);
                volume_down.setVisibility(0);
            } else {
                volume_up.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
                volume_down.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
            }
            menu.setVisibility(0);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.mDelegateHelper != null) {
            this.mDelegateHelper.onInterceptTouchEvent(event);
        }
        return true;
    }

    public void setBar(BaseStatusBar phoneStatusBar) {
        this.mDelegateHelper.setBar(phoneStatusBar);
    }

    public void setDelegateView(View view) {
        this.mDelegateHelper.setDelegateView(view);
    }

    public void setHandler(Handler h) {
        this.mHandler = h;
    }

    public void setIgnoreChildren(int index, View ignore, View panel) {
        this.mIgnoreChildren[index] = ignore;
        this.mPanels[index] = panel;
    }

    public void setShowVolume(boolean show, Context mContext) {
        this.displayLock = show;
        Configuration config = mContext.getResources().getConfiguration();
        KeyButtonView volume_up = (KeyButtonView) findViewById(R.id.volume_up);
        KeyButtonView volume_down = (KeyButtonView) findViewById(R.id.volume_down);
        KeyButtonView menu = (KeyButtonView) findViewById(R.id.menu);
        if (!show) {
            volume_up.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
            volume_down.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
            menu.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
        } else if (config.screenWidthDp > 480) {
            if (mContext.getResources().getBoolean(R.bool.hasVolumeButton)) {
                volume_up.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
                volume_down.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
            } else {
                volume_up.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
                volume_down.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
            }
            menu.setVisibility(0);
        } else {
            menu.setVisibility(0);
        }
    }
}