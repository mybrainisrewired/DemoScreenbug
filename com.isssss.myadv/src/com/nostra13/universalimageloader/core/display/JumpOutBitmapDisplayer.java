package com.nostra13.universalimageloader.core.display;

import android.graphics.Bitmap;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;

public class JumpOutBitmapDisplayer implements BitmapDisplayer {
    private final int durationMillis;

    public JumpOutBitmapDisplayer(int durationMillis) {
        this.durationMillis = durationMillis;
    }

    public static void animate(ImageView imageView, int durationMillis) {
        ScaleAnimation scale = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, 1, 0.5f, 1, 0.5f);
        scale.setDuration((long) durationMillis);
        scale.setInterpolator(new OvershootInterpolator());
        imageView.startAnimation(scale);
    }

    public Bitmap display(Bitmap bitmap, ImageView imageView, LoadedFrom loadedFrom) {
        imageView.setImageBitmap(bitmap);
        animate(imageView, this.durationMillis);
        return bitmap;
    }
}