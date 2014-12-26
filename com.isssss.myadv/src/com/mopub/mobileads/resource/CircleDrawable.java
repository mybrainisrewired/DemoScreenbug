package com.mopub.mobileads.resource;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;

public abstract class CircleDrawable extends Drawable {
    private final Paint mPaint;

    public CircleDrawable() {
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStrokeWidth(3.0f);
        this.mPaint.setColor(-1);
        this.mPaint.setStyle(Style.STROKE);
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle((float) getCenterX(), (float) getCenterY(), (float) getRadius(), this.mPaint);
    }

    protected int getCenterX() {
        return getBounds().width() / 2;
    }

    protected int getCenterY() {
        return getBounds().height() / 2;
    }

    public int getOpacity() {
        return 0;
    }

    protected Paint getPaint() {
        return this.mPaint;
    }

    protected int getRadius() {
        return Math.min(getCenterX(), getCenterY());
    }

    public void setAlpha(int i) {
    }

    public void setColorFilter(ColorFilter colorFilter) {
    }
}