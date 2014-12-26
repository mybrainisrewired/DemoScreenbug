package com.nostra13.universalimageloader.core;

import android.graphics.Bitmap;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.utils.L;
import java.lang.ref.Reference;

final class DisplayBitmapTask implements Runnable {
    private static final String LOG_DISPLAY_IMAGE_IN_IMAGEVIEW = "Display image in ImageView (loaded from %1$s) [%2$s]";
    private static final String LOG_TASK_CANCELLED_IMAGEVIEW_LOST = "ImageView was collected by GC. Task is cancelled. [%s]";
    private static final String LOG_TASK_CANCELLED_IMAGEVIEW_REUSED = "ImageView is reused for another image. Task is cancelled. [%s]";
    private final Bitmap bitmap;
    private final BitmapDisplayer displayer;
    private final ImageLoaderEngine engine;
    private final String imageUri;
    private final Reference<ImageView> imageViewRef;
    private final ImageLoadingListener listener;
    private final LoadedFrom loadedFrom;
    private boolean loggingEnabled;
    private final String memoryCacheKey;

    public DisplayBitmapTask(Bitmap bitmap, ImageLoadingInfo imageLoadingInfo, ImageLoaderEngine engine, LoadedFrom loadedFrom) {
        this.bitmap = bitmap;
        this.imageUri = imageLoadingInfo.uri;
        this.imageViewRef = imageLoadingInfo.imageViewRef;
        this.memoryCacheKey = imageLoadingInfo.memoryCacheKey;
        this.displayer = imageLoadingInfo.options.getDisplayer();
        this.listener = imageLoadingInfo.listener;
        this.engine = engine;
        this.loadedFrom = loadedFrom;
    }

    private boolean isViewWasReused(ImageView imageView) {
        String currentCacheKey = this.engine.getLoadingUriForView(imageView);
        return !this.memoryCacheKey.equals(currentCacheKey);
    }

    public void run() {
        ImageView imageView = (ImageView) this.imageViewRef.get();
        if (imageView == null) {
            if (this.loggingEnabled) {
                L.d(LOG_TASK_CANCELLED_IMAGEVIEW_LOST, new Object[]{this.memoryCacheKey});
            }
            this.listener.onLoadingCancelled(this.imageUri, imageView);
        } else if (isViewWasReused(imageView)) {
            if (this.loggingEnabled) {
                L.d(LOG_TASK_CANCELLED_IMAGEVIEW_REUSED, new Object[]{this.memoryCacheKey});
            }
            this.listener.onLoadingCancelled(this.imageUri, imageView);
        } else {
            if (this.loggingEnabled) {
                L.d(LOG_DISPLAY_IMAGE_IN_IMAGEVIEW, new Object[]{this.loadedFrom, this.memoryCacheKey});
            }
            this.listener.onLoadingComplete(this.imageUri, imageView, this.displayer.display(this.bitmap, imageView, this.loadedFrom));
            this.engine.cancelDisplayTaskFor(imageView);
        }
    }

    void setLoggingEnabled(boolean loggingEnabled) {
        this.loggingEnabled = loggingEnabled;
    }
}