package com.nostra13.universalimageloader.core.assist;

import android.graphics.Bitmap;
import android.view.View;

public class SimpleImageLoadingListener implements ImageLoadingListener {
    public void onLoadingCancelled(String imageUri, View view) {
    }

    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
    }

    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
    }

    public void onLoadingStarted(String imageUri, View view) {
    }
}