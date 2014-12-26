package com.wmt.util;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;

public class WmtScrollerBar {
    private int mAlpha;
    private Paint mPaint;
    private int mScrollerLength;
    private int mScrollerWidth;

    public WmtScrollerBar(int width) {
        this.mAlpha = 170;
        this.mScrollerWidth = width;
        this.mPaint = new Paint();
        this.mPaint.setColor(-12303292);
        this.mPaint.setAlpha(this.mAlpha);
        this.mPaint.setStrokeCap(Cap.ROUND);
        this.mPaint.setStrokeWidth((float) this.mScrollerWidth);
    }

    public void draw(Canvas canvas) {
    }

    public int getScrollerLength() {
        return this.mScrollerLength;
    }

    public void resetFade() {
    }

    public void setScrollerLength(int len) {
        this.mScrollerLength = len;
    }

    public void stepFade() {
    }
}