package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.FrameLayout;
import com.android.systemui.R;
import com.android.systemui.statusbar.BaseStatusBar;

public class PhoneStatusBarView extends FrameLayout {
    static final int DIM_ANIM_TIME = 400;
    private static final String TAG = "PhoneStatusBarView";
    Rect mButtonBounds;
    boolean mCapturingEvents;
    int mEndAlpha;
    long mEndTime;
    boolean mNightMode;
    ViewGroup mNotificationIcons;
    PhoneStatusBar mService;
    int mStartAlpha;
    int mStartX;
    int mStartY;
    ViewGroup mStatusIcons;
    boolean mTracking;

    public PhoneStatusBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mNightMode = false;
        this.mStartAlpha = 0;
        this.mEndAlpha = 0;
        this.mEndTime = 0;
        this.mButtonBounds = new Rect();
        this.mCapturingEvents = true;
    }

    private int getViewOffset(View v) {
        int offset = 0;
        while (v != this) {
            offset += v.getLeft();
            ViewParent p = v.getParent();
            if (v instanceof View) {
                v = (View) p;
            } else {
                throw new RuntimeException(v + " is not a child of " + this);
            }
        }
        return offset;
    }

    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        int alpha = getCurAlpha();
        if (alpha != 0) {
            canvas.drawARGB(alpha, 0, 0, 0);
        }
        if (alpha != this.mEndAlpha) {
            invalidate();
        }
    }

    int getCurAlpha() {
        long time = SystemClock.uptimeMillis();
        return time > this.mEndTime ? this.mEndAlpha : this.mEndAlpha - ((int) ((((long) (this.mEndAlpha - this.mStartAlpha)) * (this.mEndTime - time)) / 400));
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        boolean nightMode;
        int i = 0;
        super.onConfigurationChanged(newConfig);
        this.mService.updateDisplaySize();
        if ((newConfig.uiMode & 48) == 32) {
            nightMode = true;
        } else {
            nightMode = false;
        }
        if (this.mNightMode != nightMode) {
            this.mNightMode = nightMode;
            this.mStartAlpha = getCurAlpha();
            if (this.mNightMode) {
                i = 128;
            }
            this.mEndAlpha = i;
            this.mEndTime = SystemClock.uptimeMillis() + 400;
            invalidate();
        }
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mNotificationIcons = (ViewGroup) findViewById(R.id.notificationIcons);
        this.mStatusIcons = (ViewGroup) findViewById(R.id.statusIcons);
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (event.getAction() == 0 && this.mButtonBounds.contains((int) event.getX(), (int) event.getY())) {
            this.mCapturingEvents = false;
            return false;
        } else {
            this.mCapturingEvents = true;
            return !this.mService.interceptTouchEvent(event) ? super.onInterceptTouchEvent(event) : true;
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    public boolean onRequestSendAccessibilityEvent(View child, AccessibilityEvent event) {
        if (!super.onRequestSendAccessibilityEvent(child, event)) {
            return false;
        }
        AccessibilityEvent record = AccessibilityEvent.obtain();
        onInitializeAccessibilityEvent(record);
        dispatchPopulateAccessibilityEvent(record);
        event.appendRecord(record);
        return true;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mService.updateExpandedViewPos(BaseStatusBar.EXPANDED_LEAVE_ALONE);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!this.mCapturingEvents) {
            return false;
        }
        if (event.getAction() != 0) {
            this.mService.interceptTouchEvent(event);
        }
        return true;
    }
}