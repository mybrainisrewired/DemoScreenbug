package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Slog;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import com.android.systemui.R;
import com.android.systemui.SwipeHelper;
import com.android.systemui.SwipeHelper.Callback;
import com.android.systemui.statusbar.BaseStatusBar;

public class IntruderAlertView extends LinearLayout implements Callback {
    private static final boolean DEBUG = false;
    private static final String TAG = "IntruderAlertView";
    BaseStatusBar mBar;
    private ViewGroup mContentHolder;
    private RemoteViews mIntruderRemoteViews;
    private OnClickListener mOnClickListener;
    private SwipeHelper mSwipeHelper;
    Rect mTmpRect;

    public IntruderAlertView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IntruderAlertView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mTmpRect = new Rect();
        setOrientation(1);
    }

    public void applyIntruderContent(RemoteViews intruderView, OnClickListener listener) {
        this.mIntruderRemoteViews = intruderView;
        this.mOnClickListener = listener;
        if (this.mContentHolder != null) {
            this.mContentHolder.setX(0.0f);
            this.mContentHolder.setVisibility(0);
            this.mContentHolder.setAlpha(1.0f);
            this.mContentHolder.removeAllViews();
            View content = intruderView.apply(getContext(), this.mContentHolder);
            if (listener != null) {
                content.setOnClickListener(listener);
                Drawable bg = getResources().getDrawable(R.drawable.intruder_row_bg);
                if (bg == null) {
                    Log.e(TAG, String.format("Can't find background drawable id=0x%08x", new Object[]{Integer.valueOf(R.drawable.intruder_row_bg)}));
                } else {
                    content.setBackgroundDrawable(bg);
                }
            }
            this.mContentHolder.addView(content);
        }
    }

    public boolean canChildBeDismissed(View v) {
        return true;
    }

    public View getChildAtPosition(MotionEvent ev) {
        return this.mContentHolder;
    }

    public View getChildContentView(View v) {
        return v;
    }

    public void onAttachedToWindow() {
        this.mSwipeHelper = new SwipeHelper(0, this, getResources().getDisplayMetrics().density, (float) ViewConfiguration.get(getContext()).getScaledPagingTouchSlop());
        this.mContentHolder = (ViewGroup) findViewById(R.id.contentHolder);
        if (this.mIntruderRemoteViews != null) {
            applyIntruderContent(this.mIntruderRemoteViews, this.mOnClickListener);
        }
    }

    public void onBeginDrag(View v) {
    }

    public void onChildDismissed(View v) {
        Slog.v(TAG, "User swiped intruder to dismiss");
        this.mBar.dismissIntruder();
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mSwipeHelper.setDensityScale(getResources().getDisplayMetrics().density);
        this.mSwipeHelper.setPagingTouchSlop((float) ViewConfiguration.get(getContext()).getScaledPagingTouchSlop());
    }

    public void onDragCancelled(View v) {
        this.mContentHolder.setAlpha(1.0f);
    }

    public void onDraw(Canvas c) {
        super.onDraw(c);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return (this.mSwipeHelper.onInterceptTouchEvent(ev) || super.onInterceptTouchEvent(ev)) ? true : DEBUG;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        return (this.mSwipeHelper.onTouchEvent(ev) || super.onTouchEvent(ev)) ? true : DEBUG;
    }

    public void setBar(BaseStatusBar bar) {
        this.mBar = bar;
    }
}