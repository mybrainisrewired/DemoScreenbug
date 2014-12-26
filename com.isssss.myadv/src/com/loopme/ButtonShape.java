package com.loopme;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

class ButtonShape extends ShapeDrawable {
    private int fill;
    private final Paint fillpaint;
    private int strokeWidth;
    private final Paint strokepaint;

    public ButtonShape(Shape s, int fill, int stroke, int strokeWidth) {
        super(s);
        this.fill = fill;
        this.strokeWidth = strokeWidth;
        this.fillpaint = new Paint(getPaint());
        this.fillpaint.setColor(fill);
        this.strokepaint = new Paint(this.fillpaint);
        this.strokepaint.setStyle(Style.STROKE);
        this.strokepaint.setStrokeWidth((float) strokeWidth);
        this.strokepaint.setColor(stroke);
    }

    protected void onDraw(Shape shape, Canvas canvas, Paint paint) {
        this.fillpaint.setShader(new LinearGradient(0.0f, 0.0f, 0.0f, shape.getHeight(), this.fill, Utilities.getEndColor(this.fill), TileMode.MIRROR));
        shape.resize((float) canvas.getClipBounds().right, (float) canvas.getClipBounds().bottom);
        shape.draw(canvas, this.fillpaint);
        Matrix matrix = new Matrix();
        matrix.setRectToRect(new RectF(0.0f, 0.0f, (float) canvas.getClipBounds().right, (float) canvas.getClipBounds().bottom), new RectF((float) (this.strokeWidth / 2), (float) (this.strokeWidth / 2), (float) (canvas.getClipBounds().right - this.strokeWidth / 2), (float) (canvas.getClipBounds().bottom - this.strokeWidth / 2)), ScaleToFit.FILL);
        canvas.concat(matrix);
        shape.draw(canvas, this.strokepaint);
    }
}