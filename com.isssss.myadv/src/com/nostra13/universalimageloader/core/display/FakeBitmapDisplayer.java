package com.nostra13.universalimageloader.core.display;

import android.graphics.Bitmap;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;

public final class FakeBitmapDisplayer implements BitmapDisplayer {
    public Bitmap display(Bitmap bitmap, ImageView imageView, LoadedFrom loadedFrom) {
        return bitmap;
    }
}