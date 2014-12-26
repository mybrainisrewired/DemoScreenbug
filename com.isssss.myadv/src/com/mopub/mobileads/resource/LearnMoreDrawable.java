package com.mopub.mobileads.resource;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Point;

public class LearnMoreDrawable extends CircleDrawable {
    private Point bottomLeftPoint;
    private Point centerPoint;
    private final Paint learnMorePaint;
    private Point leftBarbPoint;
    private int mBarbLength;
    private int mDisplacement;
    private Point rightBarbPoint;
    private Point topRightPoint;

    public LearnMoreDrawable() {
        this.learnMorePaint = new Paint(getPaint());
        this.learnMorePaint.setStrokeWidth(4.5f);
        this.learnMorePaint.setStrokeCap(Cap.ROUND);
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        this.mDisplacement = (int) (((double) (0.5f * ((float) getRadius()))) / Math.sqrt(2.0d));
        this.mBarbLength = (int) (1.5f * ((float) this.mDisplacement));
        this.centerPoint = new Point(getCenterX(), getCenterY());
        this.bottomLeftPoint = new Point(this.centerPoint);
        this.bottomLeftPoint.offset(-this.mDisplacement, this.mDisplacement);
        this.topRightPoint = new Point(this.centerPoint);
        this.topRightPoint.offset(this.mDisplacement, -this.mDisplacement);
        this.leftBarbPoint = new Point(this.topRightPoint);
        this.leftBarbPoint.offset(-this.mBarbLength, 0);
        this.rightBarbPoint = new Point(this.topRightPoint);
        this.rightBarbPoint.offset(0, this.mBarbLength);
        canvas.drawLine((float) this.bottomLeftPoint.x, (float) this.bottomLeftPoint.y, (float) this.topRightPoint.x, (float) this.topRightPoint.y, this.learnMorePaint);
        canvas.drawLine((float) this.topRightPoint.x, (float) this.topRightPoint.y, (float) this.leftBarbPoint.x, (float) this.leftBarbPoint.y, this.learnMorePaint);
        canvas.drawLine((float) this.topRightPoint.x, (float) this.topRightPoint.y, (float) this.rightBarbPoint.x, (float) this.rightBarbPoint.y, this.learnMorePaint);
    }
}