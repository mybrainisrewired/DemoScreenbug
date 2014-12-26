package com.android.systemui.statusbar.tablet;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.android.systemui.R;

public class NotificationLinearLayout extends LinearLayout {
    private static final String TAG = "NotificationLinearLayout";
    int mInsetLeft;
    Drawable mItemGlow;
    Rect mTmp;

    public NotificationLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NotificationLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mTmp = new Rect();
        this.mItemGlow = context.getResources().getDrawable(R.drawable.notify_item_glow_bottom);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NotificationLinearLayout, defStyle, 0);
        this.mInsetLeft = a.getDimensionPixelSize(0, 0);
        a.recycle();
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect padding = this.mTmp;
        Drawable glow = this.mItemGlow;
        glow.getPadding(padding);
        int glowHeight = glow.getIntrinsicHeight();
        int insetLeft = this.mInsetLeft;
        int N = getChildCount();
        int i = 0;
        while (i < N) {
            View child = getChildAt(i);
            int childBottom = child.getBottom();
            glow.setBounds(child.getLeft() - padding.left + insetLeft, childBottom, child.getRight() - padding.right, childBottom + glowHeight);
            glow.draw(canvas);
            i++;
        }
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        setWillNotDraw(false);
    }
}