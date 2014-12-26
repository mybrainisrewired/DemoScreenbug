package com.android.systemui.statusbar;

import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import com.android.systemui.R;

public class DelegateViewHelper {
    private BaseStatusBar mBar;
    private View mDelegateView;
    private float[] mDownPoint;
    RectF mInitialTouch;
    private boolean mPanelShowing;
    private View mSourceView;
    private boolean mStarted;
    private boolean mSwapXY;
    private int[] mTempPoint;
    private float mTriggerThreshhold;

    public DelegateViewHelper(View sourceView) {
        this.mTempPoint = new int[2];
        this.mDownPoint = new float[2];
        this.mInitialTouch = new RectF();
        this.mSwapXY = false;
        setSourceView(sourceView);
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.mSourceView == null || this.mDelegateView == null || this.mBar.shouldDisableNavbarGestures() || this.mBar.inKeyguardRestrictedInputMode()) {
            return false;
        }
        this.mSourceView.getLocationOnScreen(this.mTempPoint);
        float sourceX = (float) this.mTempPoint[0];
        float sourceY = (float) this.mTempPoint[1];
        switch (event.getAction()) {
            case CommandQueue.FLAG_EXCLUDE_NONE:
                boolean z;
                if (this.mDelegateView.getVisibility() == 0) {
                    z = true;
                } else {
                    z = false;
                }
                this.mPanelShowing = z;
                this.mDownPoint[0] = event.getX();
                this.mDownPoint[1] = event.getY();
                this.mStarted = this.mInitialTouch.contains(this.mDownPoint[0] + sourceX, this.mDownPoint[1] + sourceY);
                break;
        }
        if (!this.mStarted) {
            return false;
        }
        this.mDelegateView.getLocationOnScreen(this.mTempPoint);
        float deltaX = sourceX - ((float) this.mTempPoint[0]);
        float deltaY = sourceY - ((float) this.mTempPoint[1]);
        event.offsetLocation(deltaX, deltaY);
        this.mDelegateView.dispatchTouchEvent(event);
        event.offsetLocation(-deltaX, -deltaY);
        return this.mPanelShowing;
    }

    public void setBar(BaseStatusBar phoneStatusBar) {
        this.mBar = phoneStatusBar;
    }

    public void setDelegateView(View view) {
        this.mDelegateView = view;
    }

    public void setInitialTouchRegion(View... views) {
        RectF bounds = new RectF();
        int[] p = new int[2];
        int i = 0;
        while (i < views.length) {
            View view = views[i];
            if (view != null) {
                view.getLocationOnScreen(p);
                if (i == 0) {
                    bounds.set((float) p[0], (float) p[1], (float) (p[0] + view.getWidth()), (float) (p[1] + view.getHeight()));
                } else {
                    bounds.union((float) p[0], (float) p[1], (float) (p[0] + view.getWidth()), (float) (p[1] + view.getHeight()));
                }
            }
            i++;
        }
        this.mInitialTouch.set(bounds);
    }

    public void setSourceView(View view) {
        this.mSourceView = view;
        if (this.mSourceView != null) {
            this.mTriggerThreshhold = this.mSourceView.getContext().getResources().getDimension(R.dimen.navbar_search_up_threshhold);
        }
    }

    public void setSwapXY(boolean swap) {
        this.mSwapXY = swap;
    }
}