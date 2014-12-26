package com.android.systemui.statusbar.policy;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class FixedSizeDrawable extends Drawable {
    int mBottom;
    Drawable mDrawable;
    int mLeft;
    int mRight;
    int mTop;

    public FixedSizeDrawable(Drawable that) {
        this.mDrawable = that;
    }

    public void draw(Canvas canvas) {
        this.mDrawable.draw(canvas);
    }

    public int getOpacity() {
        return this.mDrawable.getOpacity();
    }

    public void setAlpha(int alpha) {
        this.mDrawable.setAlpha(alpha);
    }

    public void setBounds(int l, int t, int r, int b) {
        this.mDrawable.setBounds(this.mLeft, this.mTop, this.mRight, this.mBottom);
    }

    public void setBounds(Rect bounds) {
        this.mDrawable.setBounds(this.mLeft, this.mTop, this.mRight, this.mBottom);
    }

    public void setColorFilter(ColorFilter cf) {
        this.mDrawable.setColorFilter(cf);
    }

    public void setFixedBounds(int l, int t, int r, int b) {
        this.mLeft = l;
        this.mTop = t;
        this.mRight = r;
        this.mBottom = b;
    }
}