package com.mopub.mobileads.resource;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import com.mopub.common.Preconditions;
import com.mopub.common.util.Dips;

public class CountdownDrawable extends CircleDrawable implements TextDrawable {
    private static final float TEXT_SIZE_SP = 18.0f;
    private String mSecondsRemaining;
    private final Paint mTextPaint;
    private Rect mTextRect;
    private final float textSizePixels;

    public CountdownDrawable(Context context) {
        this.mSecondsRemaining = Preconditions.EMPTY_ARGUMENTS;
        this.mTextPaint = new Paint();
        this.textSizePixels = Dips.dipsToFloatPixels(TEXT_SIZE_SP, context);
        this.mTextPaint.setTextSize(this.textSizePixels);
        this.mTextPaint.setAntiAlias(true);
        this.mTextPaint.setColor(-1);
        this.mTextPaint.setStyle(Style.FILL);
        this.mTextPaint.setTextAlign(Align.LEFT);
        this.mTextRect = new Rect();
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        String text = String.valueOf(this.mSecondsRemaining);
        this.mTextPaint.getTextBounds(text, 0, text.length(), this.mTextRect);
        canvas.drawText(text, (float) (getCenterX() - this.mTextRect.width() / 2), (float) (getCenterY() + this.mTextRect.height() / 2), this.mTextPaint);
    }

    public void updateText(String text) {
        if (!this.mSecondsRemaining.equals(text)) {
            this.mSecondsRemaining = text;
            invalidateSelf();
        }
    }
}