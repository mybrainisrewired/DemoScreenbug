package com.android.volley.toolbox;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class ImageLoader {
    private int mBatchResponseDelayMs;
    private final HashMap<String, BatchedImageRequest> mBatchedResponses;
    private final ImageCache mCache;
    private final Handler mHandler;
    private final HashMap<String, BatchedImageRequest> mInFlightRequests;
    private final RequestQueue mRequestQueue;
    private Runnable mRunnable;
    private Map<String, View> mViewMap;

    private class BatchedImageRequest {
        private final LinkedList<com.android.volley.toolbox.ImageLoader.ImageContainer> mContainers;
        private VolleyError mError;
        private final Request<?> mRequest;
        private Bitmap mResponseBitmap;

        public BatchedImageRequest(Request<?> request, com.android.volley.toolbox.ImageLoader.ImageContainer container) {
            this.mContainers = new LinkedList();
            this.mRequest = request;
            this.mContainers.add(container);
        }

        public void addContainer(com.android.volley.toolbox.ImageLoader.ImageContainer container) {
            this.mContainers.add(container);
        }

        public VolleyError getError() {
            return this.mError;
        }

        public boolean removeContainerAndCancelIfNecessary(com.android.volley.toolbox.ImageLoader.ImageContainer container) {
            this.mContainers.remove(container);
            if (this.mContainers.size() != 0) {
                return false;
            }
            this.mRequest.cancel();
            return true;
        }

        public void setError(VolleyError error) {
            this.mError = error;
        }
    }

    public static interface ImageCache {
        void clearCache();

        Bitmap getBitmap(String str);

        Bitmap getBitmapOnlyFromMemoryCache(String str);

        void putBitmap(String str, Bitmap bitmap);

        void removeCache(String str);
    }

    public class ImageContainer {
        private Bitmap mBitmap;
        private final String mCacheKey;
        private final com.android.volley.toolbox.ImageLoader.ImageListener mListener;
        private final String mRequestUrl;

        public ImageContainer(Bitmap bitmap, String requestUrl, String cacheKey, com.android.volley.toolbox.ImageLoader.ImageListener listener) {
            this.mBitmap = bitmap;
            this.mRequestUrl = requestUrl;
            this.mCacheKey = cacheKey;
            this.mListener = listener;
        }

        public void cancelRequest() {
            if (this.mListener != null) {
                BatchedImageRequest request = (BatchedImageRequest) ImageLoader.this.mInFlightRequests.get(this.mCacheKey);
                if (request == null) {
                    request = ImageLoader.this.mBatchedResponses.get(this.mCacheKey);
                    if (request != null) {
                        request.removeContainerAndCancelIfNecessary(this);
                        if (request.mContainers.size() == 0) {
                            ImageLoader.this.mBatchedResponses.remove(this.mCacheKey);
                        }
                    }
                } else if (request.removeContainerAndCancelIfNecessary(this)) {
                    ImageLoader.this.mInFlightRequests.remove(this.mCacheKey);
                }
            }
        }

        public Bitmap getBitmap() {
            return this.mBitmap;
        }

        public String getRequestUrl() {
            return this.mRequestUrl;
        }
    }

    class AnonymousClass_2 implements Listener<Bitmap> {
        private final /* synthetic */ String val$cacheKey;

        AnonymousClass_2(String str) {
            this.val$cacheKey = str;
        }

        public void onResponse(Bitmap response) {
            ImageLoader.this.onGetImageSuccess(this.val$cacheKey, response);
        }
    }

    class AnonymousClass_3 implements ErrorListener {
        private final /* synthetic */ String val$cacheKey;

        AnonymousClass_3(String str) {
            this.val$cacheKey = str;
        }

        public void onErrorResponse(VolleyError error) {
            ImageLoader.this.onGetImageError(this.val$cacheKey, error);
        }
    }

    public static interface ImageListener extends ErrorListener {
        void onResponse(com.android.volley.toolbox.ImageLoader.ImageContainer imageContainer, boolean z);
    }

    class AnonymousClass_1 implements com.android.volley.toolbox.ImageLoader.ImageListener {
        private final /* synthetic */ int val$defaultImageResId;
        private final /* synthetic */ int val$errorImageResId;
        private final /* synthetic */ ImageView val$view;

        AnonymousClass_1(int i, ImageView imageView, int i2) {
            this.val$errorImageResId = i;
            this.val$view = imageView;
            this.val$defaultImageResId = i2;
        }

        public void onErrorResponse(VolleyError error) {
            if (this.val$errorImageResId != 0) {
                this.val$view.setImageResource(this.val$errorImageResId);
            }
        }

        public void onResponse(com.android.volley.toolbox.ImageLoader.ImageContainer response, boolean isImmediate) {
            if (response.getBitmap() != null) {
                this.val$view.setImageBitmap(response.getBitmap());
            } else if (this.val$defaultImageResId != 0) {
                this.val$view.setImageResource(this.val$defaultImageResId);
            }
        }
    }

    public ImageLoader(RequestQueue queue, ImageCache imageCache) {
        this.mBatchResponseDelayMs = 100;
        this.mInFlightRequests = new HashMap();
        this.mBatchedResponses = new HashMap();
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mViewMap = new LinkedHashMap();
        this.mRequestQueue = queue;
        this.mCache = imageCache;
    }

    private void batchResponse(String cacheKey, BatchedImageRequest request) {
        this.mBatchedResponses.put(cacheKey, request);
        if (this.mRunnable == null) {
            this.mRunnable = new Runnable() {
                public void run() {
                    Iterator it = ImageLoader.this.mBatchedResponses.values().iterator();
                    while (it.hasNext()) {
                        BatchedImageRequest bir = (BatchedImageRequest) it.next();
                        Iterator it2 = bir.mContainers.iterator();
                        while (it2.hasNext()) {
                            com.android.volley.toolbox.ImageLoader.ImageContainer container = (com.android.volley.toolbox.ImageLoader.ImageContainer) it2.next();
                            if (container.mListener != null) {
                                if (bir.getError() == null) {
                                    container.mBitmap = bir.mResponseBitmap;
                                    container.mListener.onResponse(container, false);
                                } else {
                                    container.mListener.onErrorResponse(bir.getError());
                                }
                            }
                        }
                    }
                    ImageLoader.this.mBatchedResponses.clear();
                    ImageLoader.this.mRunnable = null;
                }
            };
            this.mHandler.postDelayed(this.mRunnable, (long) this.mBatchResponseDelayMs);
        }
    }

    private static String getCacheKey(String url, int maxWidth, int maxHeight) {
        return new StringBuilder(url.length() + 12).append("#W").append(maxWidth).append("#H").append(maxHeight).append(url).toString();
    }

    public static ImageListener getImageListener(ImageView view, int defaultImageResId, int errorImageResId) {
        return new AnonymousClass_1(errorImageResId, view, defaultImageResId);
    }

    private void onGetImageError(String cacheKey, VolleyError error) {
        BatchedImageRequest request = (BatchedImageRequest) this.mInFlightRequests.remove(cacheKey);
        request.setError(error);
        if (request != null) {
            batchResponse(cacheKey, request);
        }
    }

    private void onGetImageSuccess(String cacheKey, Bitmap response) {
        this.mCache.putBitmap(cacheKey, response);
        View view = (View) this.mViewMap.get(cacheKey);
        if (view != null) {
            view.startAnimation(ScaleAnimationExt.getmScaleAnimation());
            this.mViewMap.remove(cacheKey);
            Log.e("*******************", "start animate");
        } else {
            Log.e("#####################", "view is null");
        }
        BatchedImageRequest request = (BatchedImageRequest) this.mInFlightRequests.remove(cacheKey);
        if (request != null) {
            request.mResponseBitmap = response;
            batchResponse(cacheKey, request);
        }
    }

    private void throwIfNotOnMainThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("ImageLoader must be invoked from the main thread.");
        }
    }

    public void clearCache() {
        this.mCache.clearCache();
        this.mViewMap.clear();
    }

    public void createLocalCacheBitmap(String requestUrl, Bitmap cachedBitmap) {
        this.mCache.putBitmap(getCacheKey(requestUrl, 0, 0), cachedBitmap);
    }

    public ImageContainer get(String requestUrl, ImageListener listener) {
        return get(requestUrl, listener, 0, 0);
    }

    public ImageContainer get(String requestUrl, ImageListener imageListener, int maxWidth, int maxHeight) {
        throwIfNotOnMainThread();
        String cacheKey = getCacheKey(requestUrl, maxWidth, maxHeight);
        Bitmap cachedBitmap = this.mCache.getBitmap(cacheKey);
        if (cachedBitmap == null || cachedBitmap.isRecycled()) {
            ImageContainer imageContainer = new ImageContainer(null, requestUrl, cacheKey, imageListener);
            imageListener.onResponse(imageContainer, true);
            BatchedImageRequest request = (BatchedImageRequest) this.mInFlightRequests.get(cacheKey);
            if (request != null) {
                request.addContainer(imageContainer);
                return imageContainer;
            } else {
                ErrorListener anonymousClass_3 = errorListener;
                ImageLoader imageLoader = this;
                Request<?> newRequest = new ImageRequest(requestUrl, new AnonymousClass_2(cacheKey), maxWidth, maxHeight, Config.RGB_565, errorListener);
                this.mRequestQueue.add(newRequest);
                this.mInFlightRequests.put(cacheKey, new BatchedImageRequest(newRequest, imageContainer));
                return imageContainer;
            }
        } else {
            ImageContainer container = new ImageContainer(cachedBitmap, requestUrl, null, null);
            imageListener.onResponse(container, true);
            return container;
        }
    }

    public Bitmap getBitmapOnlyFromMemoryCache(String requestUrl) {
        return this.mCache.getBitmapOnlyFromMemoryCache(getCacheKey(requestUrl, 0, 0));
    }

    public Bitmap getImageBitmap(String requestUrl) {
        return this.mCache.getBitmap(getCacheKey(requestUrl, 0, 0));
    }

    public boolean isCached(String requestUrl, int maxWidth, int maxHeight) {
        throwIfNotOnMainThread();
        String cacheKey = getCacheKey(requestUrl, maxWidth, maxHeight);
        return this.mCache.getBitmap(cacheKey) != null;
    }

    public void putViewMap(String url, View view) {
        String str = getCacheKey(url, 0, 0);
        if (this.mViewMap.get(str) == null) {
            this.mViewMap.put(str, view);
        }
    }

    public void removeCache(String requestUrl) {
        this.mCache.removeCache(getCacheKey(requestUrl, 0, 0));
    }

    public void setBatchedResponseDelay(int newBatchedResponseDelayMs) {
        this.mBatchResponseDelayMs = newBatchedResponseDelayMs;
    }
}