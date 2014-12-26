package com.nostra13.universalimageloader.core;

import android.widget.ImageView;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.concurrent.locks.ReentrantLock;

final class ImageLoadingInfo {
    final Reference<ImageView> imageViewRef;
    final ImageLoadingListener listener;
    final ReentrantLock loadFromUriLock;
    final String memoryCacheKey;
    final DisplayImageOptions options;
    final ImageSize targetSize;
    final String uri;

    public ImageLoadingInfo(String uri, ImageView imageView, ImageSize targetSize, String memoryCacheKey, DisplayImageOptions options, ImageLoadingListener listener, ReentrantLock loadFromUriLock) {
        this.uri = uri;
        this.imageViewRef = new WeakReference(imageView);
        this.targetSize = targetSize;
        this.options = options;
        this.listener = listener;
        this.loadFromUriLock = loadFromUriLock;
        this.memoryCacheKey = memoryCacheKey;
    }
}