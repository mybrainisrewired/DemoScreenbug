package com.nostra13.universalimageloader.core.display;

import android.graphics.Bitmap;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;

public class TurnPageDisplayer implements BitmapDisplayer {
    private final int durationMillis;
    private final int h;
    private final int w;
    private final int x;
    private final int y;

    public TurnPageDisplayer(int durationMillis, int x, int y, int w, int h) {
        this.durationMillis = durationMillis;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public static void animate(ImageView imageView, int durationMillis, int x, int y, int w, int h) {
        RotateAnimation animate = new RotateAnimation(0.0f, 360.0f);
        animate.setInterpolator(new AccelerateDecelerateInterpolator());
        animate.setDuration((long) durationMillis);
        imageView.startAnimation(animate);
    }

    public Bitmap display(Bitmap bitmap, ImageView imageView, LoadedFrom loadedFrom) {
        imageView.setImageBitmap(bitmap);
        animate(imageView, this.durationMillis, this.x, this.y, this.w, this.h);
        return bitmap;
    }
}