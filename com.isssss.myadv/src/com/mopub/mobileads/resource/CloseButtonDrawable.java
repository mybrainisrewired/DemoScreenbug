package com.mopub.mobileads.resource;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Point;

public class CloseButtonDrawable extends CircleDrawable {
    private Point bottomLeftPoint;
    private Point bottomRightPoint;
    private Point centerPoint;
    private final Paint closeButtonPaint;
    private int mDisplacement;
    private Point topLeftPoint;
    private Point topRightPoint;

    public CloseButtonDrawable() {
        this.closeButtonPaint = new Paint(getPaint());
        this.closeButtonPaint.setStrokeWidth(4.5f);
        this.closeButtonPaint.setStrokeCap(Cap.ROUND);
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        this.mDisplacement = (int) ((0.5f * ((float) getRadius())) / ((float) Math.sqrt(2.0d)));
        this.centerPoint = new Point(getCenterX(), getCenterY());
        this.bottomLeftPoint = new Point(this.centerPoint);
        this.bottomLeftPoint.offset(-this.mDisplacement, this.mDisplacement);
        this.topLeftPoint = new Point(this.centerPoint);
        this.topLeftPoint.offset(-this.mDisplacement, -this.mDisplacement);
        this.topRightPoint = new Point(this.centerPoint);
        this.topRightPoint.offset(this.mDisplacement, -this.mDisplacement);
        this.bottomRightPoint = new Point(this.centerPoint);
        this.bottomRightPoint.offset(this.mDisplacement, this.mDisplacement);
        canvas.drawLine((float) this.bottomLeftPoint.x, (float) this.bottomLeftPoint.y, (float) this.topRightPoint.x, (float) this.topRightPoint.y, this.closeButtonPaint);
        canvas.drawLine((float) this.topLeftPoint.x, (float) this.topLeftPoint.y, (float) this.bottomRightPoint.x, (float) this.bottomRightPoint.y, this.closeButtonPaint);
    }
}