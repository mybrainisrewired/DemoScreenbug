package com.wmt.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

class FastBitmapDrawable extends Drawable {
    private int mAlpha;
    private Bitmap mBitmap;
    private int mHeight;
    private final Paint mPaint;
    private int mWidth;

    FastBitmapDrawable(Bitmap b) {
        this.mPaint = new Paint();
        this.mAlpha = 255;
        this.mBitmap = b;
        if (b != null) {
            this.mWidth = this.mBitmap.getWidth();
            this.mHeight = this.mBitmap.getHeight();
        } else {
            this.mHeight = 0;
            this.mWidth = 0;
        }
    }

    public void draw(Canvas canvas) {
        Rect r = getBounds();
        canvas.drawBitmap(this.mBitmap, (float) r.left, (float) r.top, this.mPaint);
    }

    public int getAlpha() {
        return this.mAlpha;
    }

    public Bitmap getBitmap() {
        return this.mBitmap;
    }

    public int getIntrinsicHeight() {
        return this.mHeight;
    }

    public int getIntrinsicWidth() {
        return this.mWidth;
    }

    public int getMinimumHeight() {
        return this.mHeight;
    }

    public int getMinimumWidth() {
        return this.mWidth;
    }

    public int getOpacity() {
        return -3;
    }

    public void setAlpha(int alpha) {
        this.mAlpha = alpha;
        this.mPaint.setAlpha(alpha);
    }

    public void setBitmap(Bitmap b) {
        this.mBitmap = b;
        if (b != null) {
            this.mWidth = this.mBitmap.getWidth();
            this.mHeight = this.mBitmap.getHeight();
        } else {
            this.mHeight = 0;
            this.mWidth = 0;
        }
    }

    public void setColorFilter(ColorFilter cf) {
        this.mPaint.setColorFilter(cf);
    }

    public void setFilterBitmap(boolean filterBitmap) {
        this.mPaint.setFilterBitmap(filterBitmap);
    }
}