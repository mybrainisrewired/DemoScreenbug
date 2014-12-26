package com.mopub.nativeads;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import com.mopub.common.CacheService;
import com.mopub.common.DownloadResponse;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.VersionCode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

class ImageService {
    private static final int TWO_MEGABYTES = 2097152;
    private static int sTargetWidth;

    static interface ImageServiceListener {
        void onFail();

        void onSuccess(Map<String, Bitmap> map);
    }

    private static class ImageDiskTaskManagerListener implements TaskManagerListener<Bitmap> {
        private final Map<String, Bitmap> mBitmaps;
        private final ImageServiceListener mImageServiceListener;

        ImageDiskTaskManagerListener(ImageServiceListener imageServiceListener, Map<String, Bitmap> bitmaps) {
            this.mImageServiceListener = imageServiceListener;
            this.mBitmaps = bitmaps;
        }

        public void onFail() {
            this.mImageServiceListener.onFail();
        }

        public void onSuccess(Map<String, Bitmap> diskBitmaps) {
            List<String> urlDiskMisses = new ArrayList();
            Iterator it = diskBitmaps.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, Bitmap> entry = (Entry) it.next();
                if (entry.getValue() == null) {
                    urlDiskMisses.add((String) entry.getKey());
                } else {
                    ImageService.putBitmapInCache((String) entry.getKey(), (Bitmap) entry.getValue());
                    this.mBitmaps.put((String) entry.getKey(), (Bitmap) entry.getValue());
                }
            }
            if (urlDiskMisses.isEmpty()) {
                this.mImageServiceListener.onSuccess(this.mBitmaps);
            } else {
                try {
                    new ImageDownloadTaskManager(urlDiskMisses, new ImageDownloadResponseListener(this.mImageServiceListener, this.mBitmaps), sTargetWidth).execute();
                } catch (IllegalArgumentException e) {
                    MoPubLog.d("Unable to initialize ImageDownloadTaskManager", e);
                    this.mImageServiceListener.onFail();
                }
            }
        }
    }

    private static class ImageDownloadResponseListener implements TaskManagerListener<DownloadResponse> {
        private final Map<String, Bitmap> mBitmaps;
        private final ImageServiceListener mImageServiceListener;

        ImageDownloadResponseListener(ImageServiceListener imageServiceListener, Map<String, Bitmap> bitmaps) {
            this.mImageServiceListener = imageServiceListener;
            this.mBitmaps = bitmaps;
        }

        public void onFail() {
            this.mImageServiceListener.onFail();
        }

        public void onSuccess(Map<String, DownloadResponse> responses) {
            Iterator it = responses.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, DownloadResponse> entry = (Entry) it.next();
                Bitmap bitmap = ImageService.asBitmap((DownloadResponse) entry.getValue(), sTargetWidth);
                String key = (String) entry.getKey();
                if (bitmap == null) {
                    MoPubLog.d(new StringBuilder("Error decoding image for url: ").append((String) entry.getKey()).toString());
                    onFail();
                    return;
                } else {
                    ImageService.putDataInCache(key, bitmap, ((DownloadResponse) entry.getValue()).getByteArray());
                    this.mBitmaps.put(key, bitmap);
                }
            }
            this.mImageServiceListener.onSuccess(this.mBitmaps);
        }
    }

    static {
        sTargetWidth = -1;
    }

    ImageService() {
    }

    public static Bitmap asBitmap(DownloadResponse downloadResponse, int requestedWidth) {
        return downloadResponse == null ? null : byteArrayToBitmap(downloadResponse.getByteArray(), requestedWidth);
    }

    public static Bitmap byteArrayToBitmap(byte[] bytes, int requestedWidth) {
        if (requestedWidth <= 0) {
            return null;
        }
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        options.inSampleSize = calculateInSampleSize(options.outWidth, requestedWidth);
        while (getMemBytes(options) > 2097152) {
            options.inSampleSize *= 2;
        }
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        if (bitmap == null) {
            return null;
        }
        if (bitmap.getWidth() <= requestedWidth) {
            return bitmap;
        }
        Bitmap subsampledBitmap = bitmap;
        bitmap = Bitmap.createScaledBitmap(subsampledBitmap, requestedWidth, (int) ((((double) bitmap.getHeight()) * ((double) requestedWidth)) / ((double) bitmap.getWidth())), true);
        subsampledBitmap.recycle();
        return bitmap;
    }

    public static int calculateInSampleSize(int nativeWidth, int requestedWidth) {
        int inSampleSize = 1;
        if (nativeWidth > requestedWidth) {
            while ((nativeWidth / 2) / inSampleSize >= requestedWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    @VisibleForTesting
    static void clear() {
        sTargetWidth = -1;
    }

    static void get(Context context, List<String> urls, ImageServiceListener imageServiceListener) {
        initialize(context);
        CacheService.initialize(context);
        get(urls, imageServiceListener);
    }

    static void get(List<String> urls, ImageServiceListener imageServiceListener) {
        Map<String, Bitmap> cacheBitmaps = new HashMap(urls.size());
        List<String> urlCacheMisses = getBitmapsFromMemoryCache(urls, cacheBitmaps);
        if (urlCacheMisses.isEmpty()) {
            imageServiceListener.onSuccess(cacheBitmaps);
        } else {
            try {
                new ImageDiskTaskManager(urlCacheMisses, new ImageDiskTaskManagerListener(imageServiceListener, cacheBitmaps), sTargetWidth).execute();
            } catch (IllegalArgumentException e) {
                MoPubLog.d("Unable to initialize ImageDiskTaskManager", e);
                imageServiceListener.onFail();
            }
        }
    }

    @Deprecated
    static Bitmap getBitmapFromDiskCache(String key) {
        byte[] bytes = CacheService.getFromDiskCache(key);
        return bytes != null ? byteArrayToBitmap(bytes, sTargetWidth) : null;
    }

    static Bitmap getBitmapFromMemoryCache(String key) {
        return CacheService.getFromBitmapCache(key);
    }

    static List<String> getBitmapsFromMemoryCache(List<String> urls, Map<String, Bitmap> hits) {
        List<String> cacheMisses = new ArrayList();
        Iterator it = urls.iterator();
        while (it.hasNext()) {
            String url = (String) it.next();
            Bitmap bitmap = getBitmapFromMemoryCache(url);
            if (bitmap != null) {
                hits.put(url, bitmap);
            } else {
                cacheMisses.add(url);
            }
        }
        return cacheMisses;
    }

    public static long getMemBytes(Options options) {
        return (((4 * ((long) options.outWidth)) * ((long) options.outHeight)) / ((long) options.inSampleSize)) / ((long) options.inSampleSize);
    }

    @VisibleForTesting
    static int getTargetWidth() {
        return sTargetWidth;
    }

    @TargetApi(13)
    @VisibleForTesting
    static void initialize(Context context) {
        if (sTargetWidth == -1) {
            Display display = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
            Point size = new Point();
            if (VersionCode.currentApiLevel().isBelow(VersionCode.HONEYCOMB_MR2)) {
                size.set(display.getWidth(), display.getHeight());
            } else {
                display.getSize(size);
            }
            sTargetWidth = Math.min(size.x, size.y);
        }
    }

    static void putBitmapInCache(String key, Bitmap bitmap) {
        CacheService.putToBitmapCache(key, bitmap);
    }

    static void putDataInCache(String key, Bitmap bitmap, byte[] byteData) {
        CacheService.putToBitmapCache(key, bitmap);
        CacheService.putToDiskCacheAsync(key, byteData);
    }
}