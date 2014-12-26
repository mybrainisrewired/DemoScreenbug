package com.mopub.nativeads;

import android.graphics.Bitmap;
import android.widget.ImageView;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.Utils;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Map;
import java.util.WeakHashMap;

class ImageViewService {
    private static final WeakHashMap<ImageView, Long> sImageViewRequestIds;

    private static class MyImageViewServiceListener implements ImageServiceListener {
        private final WeakReference<ImageView> mImageView;
        private final long mUniqueId;
        private final String mUrl;

        MyImageViewServiceListener(String url, ImageView imageView, long uniqueId) {
            this.mUrl = url;
            this.mImageView = new WeakReference(imageView);
            this.mUniqueId = uniqueId;
        }

        public void onFail() {
            MoPubLog.d("Failed to load image for ImageView");
        }

        public void onSuccess(Map<String, Bitmap> bitmaps) {
            ImageView imageView = (ImageView) this.mImageView.get();
            if (imageView != null && bitmaps != null && bitmaps.containsKey(this.mUrl)) {
                Long uniqueId = (Long) sImageViewRequestIds.get(imageView);
                if (uniqueId != null && this.mUniqueId == uniqueId.longValue()) {
                    imageView.setImageBitmap((Bitmap) bitmaps.get(this.mUrl));
                }
            }
        }
    }

    static {
        sImageViewRequestIds = new WeakHashMap();
    }

    private ImageViewService() {
    }

    @Deprecated
    @VisibleForTesting
    static Long getImageViewUniqueId(ImageView imageView) {
        return (Long) sImageViewRequestIds.get(imageView);
    }

    static void loadImageView(String url, ImageView imageView) {
        if (imageView == null) {
            MoPubLog.d("Attempted to load an image into a null ImageView");
        } else {
            imageView.setImageDrawable(null);
            if (url != null) {
                long uniqueId = Utils.generateUniqueId();
                sImageViewRequestIds.put(imageView, Long.valueOf(uniqueId));
                ImageService.get(Arrays.asList(new String[]{url}), new MyImageViewServiceListener(url, imageView, uniqueId));
            }
        }
    }

    @Deprecated
    @VisibleForTesting
    static void setImageViewUniqueId(ImageView imageView, long uniqueId) {
        sImageViewRequestIds.put(imageView, Long.valueOf(uniqueId));
    }
}