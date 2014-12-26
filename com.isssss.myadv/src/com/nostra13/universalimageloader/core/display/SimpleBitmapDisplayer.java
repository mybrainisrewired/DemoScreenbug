package com.nostra13.universalimageloader.core.display;

import android.graphics.Bitmap;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;

public final class SimpleBitmapDisplayer implements BitmapDisplayer {
    public Bitmap display(Bitmap bitmap, ImageView imageView, LoadedFrom loadedFrom) {
        imageView.setImageBitmap(bitmap);
        return bitmap;
    }
}