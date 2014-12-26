package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class TrackingPatternView extends View {
    private Paint mPaint;
    private Bitmap mTexture;
    private int mTextureHeight;
    private int mTextureWidth;

    public TrackingPatternView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mTexture = BitmapFactory.decodeResource(getResources(), 17302871);
        this.mTextureWidth = this.mTexture.getWidth();
        this.mTextureHeight = this.mTexture.getHeight();
        this.mPaint = new Paint();
        this.mPaint.setDither(false);
    }

    public void onDraw(Canvas canvas) {
        Bitmap texture = this.mTexture;
        Paint paint = this.mPaint;
        int width = getWidth();
        int height = getHeight();
        int textureWidth = this.mTextureWidth;
        int textureHeight = this.mTextureHeight;
        int x = 0;
        while (x < width) {
            int y = 0;
            while (y < height) {
                canvas.drawBitmap(texture, (float) x, (float) y, paint);
                y += textureHeight;
            }
            x += textureWidth;
        }
    }
}