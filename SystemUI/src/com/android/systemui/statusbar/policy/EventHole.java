package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.graphics.Region.Op;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver.InternalInsetsInfo;
import android.view.ViewTreeObserver.OnComputeInternalInsetsListener;
import com.android.systemui.recent.RecentsCallback;

public class EventHole extends View implements OnComputeInternalInsetsListener {
    private static final String TAG = "StatusBar.EventHole";
    private int[] mLoc;
    private boolean mWindowVis;

    public EventHole(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EventHole(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        this.mLoc = new int[2];
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnComputeInternalInsetsListener(this);
    }

    public void onComputeInternalInsets(InternalInsetsInfo info) {
        boolean visible;
        if (!isShown() || !this.mWindowVis || getWidth() <= 0 || getHeight() <= 0) {
            visible = false;
        } else {
            visible = true;
        }
        int[] loc = this.mLoc;
        getLocationInWindow(loc);
        int l = loc[0];
        int r = l + getWidth();
        int t = loc[1];
        int b = t + getHeight();
        View top = this;
        while (top.getParent() instanceof View) {
            top = top.getParent();
        }
        if (visible) {
            info.setTouchableInsets(RecentsCallback.SWIPE_DOWN);
            info.touchableRegion.set(0, 0, top.getWidth(), top.getHeight());
            info.touchableRegion.op(l, t, r, b, Op.DIFFERENCE);
        } else {
            info.setTouchableInsets(0);
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnComputeInternalInsetsListener(this);
    }

    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        this.mWindowVis = visibility == 0;
    }
}