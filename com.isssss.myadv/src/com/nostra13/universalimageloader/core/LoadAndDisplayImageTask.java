package com.nostra13.universalimageloader.core;

import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.FailReason.FailType;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import com.nostra13.universalimageloader.core.decode.ImageDecoder;
import com.nostra13.universalimageloader.core.decode.ImageDecodingInfo;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.nostra13.universalimageloader.utils.IoUtils;
import com.nostra13.universalimageloader.utils.L;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.Reference;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

final class LoadAndDisplayImageTask implements Runnable {
    private static final int BUFFER_SIZE = 32768;
    private static final String ERROR_POST_PROCESSOR_NULL = "Pre-processor returned null [%s]";
    private static final String ERROR_PRE_PROCESSOR_NULL = "Pre-processor returned null [%s]";
    private static final String ERROR_PROCESSOR_FOR_DISC_CACHE_NULL = "Bitmap processor for disc cache returned null [%s]";
    private static final String LOG_CACHE_IMAGE_IN_MEMORY = "Cache image in memory [%s]";
    private static final String LOG_CACHE_IMAGE_ON_DISC = "Cache image on disc [%s]";
    private static final String LOG_DELAY_BEFORE_LOADING = "Delay %d ms before loading...  [%s]";
    private static final String LOG_GET_IMAGE_FROM_MEMORY_CACHE_AFTER_WAITING = "...Get cached bitmap from memory after waiting. [%s]";
    private static final String LOG_LOAD_IMAGE_FROM_DISC_CACHE = "Load image from disc cache [%s]";
    private static final String LOG_LOAD_IMAGE_FROM_NETWORK = "Load image from network [%s]";
    private static final String LOG_POSTPROCESS_IMAGE = "PostProcess image before displaying [%s]";
    private static final String LOG_PREPROCESS_IMAGE = "PreProcess image before caching in memory [%s]";
    private static final String LOG_PROCESS_IMAGE_BEFORE_CACHE_ON_DISC = "Process image before cache on disc [%s]";
    private static final String LOG_RESUME_AFTER_PAUSE = ".. Resume loading [%s]";
    private static final String LOG_START_DISPLAY_IMAGE_TASK = "Start display image task [%s]";
    private static final String LOG_TASK_CANCELLED_IMAGEVIEW_LOST = "ImageView was collected by GC. Task is cancelled. [%s]";
    private static final String LOG_TASK_CANCELLED_IMAGEVIEW_REUSED = "ImageView is reused for another image. Task is cancelled. [%s]";
    private static final String LOG_TASK_INTERRUPTED = "Task was interrupted [%s]";
    private static final String LOG_WAITING_FOR_IMAGE_LOADED = "Image already is loading. Waiting... [%s]";
    private static final String LOG_WAITING_FOR_RESUME = "ImageLoader is paused. Waiting...  [%s]";
    private final ImageLoaderConfiguration configuration;
    private final ImageDecoder decoder;
    private final ImageDownloader downloader;
    private final ImageLoaderEngine engine;
    private final Handler handler;
    private final ImageLoadingInfo imageLoadingInfo;
    private boolean imageViewCollected;
    final Reference<ImageView> imageViewRef;
    final ImageLoadingListener listener;
    private LoadedFrom loadedFrom;
    private final String memoryCacheKey;
    private final ImageDownloader networkDeniedDownloader;
    final DisplayImageOptions options;
    private final ImageDownloader slowNetworkDownloader;
    private final ImageSize targetSize;
    final String uri;
    private final boolean writeLogs;

    class AnonymousClass_1 implements Runnable {
        private final /* synthetic */ Throwable val$failCause;
        private final /* synthetic */ FailType val$failType;

        AnonymousClass_1(FailType failType, Throwable th) {
            this.val$failType = failType;
            this.val$failCause = th;
        }

        public void run() {
            ImageView imageView = (ImageView) LoadAndDisplayImageTask.this.imageViewRef.get();
            if (imageView != null) {
                if (LoadAndDisplayImageTask.this.options.shouldShowImageResOnFail()) {
                    imageView.setImageResource(LoadAndDisplayImageTask.this.options.getImageResOnFail());
                } else if (LoadAndDisplayImageTask.this.options.shouldShowBitmapOnFail()) {
                    imageView.setImageBitmap(LoadAndDisplayImageTask.this.options.getBitmapOnFail());
                }
            }
            LoadAndDisplayImageTask.this.listener.onLoadingFailed(LoadAndDisplayImageTask.this.uri, imageView, new FailReason(this.val$failType, this.val$failCause));
        }
    }

    public LoadAndDisplayImageTask(ImageLoaderEngine engine, ImageLoadingInfo imageLoadingInfo, Handler handler) {
        this.loadedFrom = LoadedFrom.NETWORK;
        this.imageViewCollected = false;
        this.engine = engine;
        this.imageLoadingInfo = imageLoadingInfo;
        this.handler = handler;
        this.configuration = engine.configuration;
        this.downloader = this.configuration.downloader;
        this.networkDeniedDownloader = this.configuration.networkDeniedDownloader;
        this.slowNetworkDownloader = this.configuration.slowNetworkDownloader;
        this.decoder = this.configuration.decoder;
        this.writeLogs = this.configuration.writeLogs;
        this.uri = imageLoadingInfo.uri;
        this.memoryCacheKey = imageLoadingInfo.memoryCacheKey;
        this.imageViewRef = imageLoadingInfo.imageViewRef;
        this.targetSize = imageLoadingInfo.targetSize;
        this.options = imageLoadingInfo.options;
        this.listener = imageLoadingInfo.listener;
    }

    private ImageView checkImageViewRef() {
        ImageView imageView = (ImageView) this.imageViewRef.get();
        if (imageView == null) {
            this.imageViewCollected = true;
            log(LOG_TASK_CANCELLED_IMAGEVIEW_LOST);
            fireCancelEvent();
        }
        return imageView;
    }

    private boolean checkImageViewReused(ImageView imageView) {
        String currentCacheKey = this.engine.getLoadingUriForView(imageView);
        boolean imageViewWasReused = !this.memoryCacheKey.equals(currentCacheKey);
        if (imageViewWasReused) {
            log(LOG_TASK_CANCELLED_IMAGEVIEW_REUSED);
            fireCancelEvent();
        }
        return imageViewWasReused;
    }

    private boolean checkTaskIsInterrupted() {
        boolean interrupted = Thread.interrupted();
        if (interrupted) {
            log(LOG_TASK_INTERRUPTED);
        }
        return interrupted;
    }

    private boolean checkTaskIsNotActual() {
        ImageView imageView = checkImageViewRef();
        return imageView == null || checkImageViewReused(imageView);
    }

    private Bitmap decodeImage(String imageUri) throws IOException {
        ImageView imageView = checkImageViewRef();
        if (imageView == null) {
            return null;
        }
        String str = imageUri;
        return this.decoder.decode(new ImageDecodingInfo(this.memoryCacheKey, str, this.targetSize, ViewScaleType.fromImageView(imageView), getDownloader(), this.options));
    }

    private boolean delayIfNeed() {
        if (!this.options.shouldDelayBeforeLoading()) {
            return false;
        }
        log(LOG_DELAY_BEFORE_LOADING, new Object[]{Integer.valueOf(this.options.getDelayBeforeLoading()), this.memoryCacheKey});
        try {
            Thread.sleep((long) this.options.getDelayBeforeLoading());
            return checkTaskIsNotActual();
        } catch (InterruptedException e) {
            L.e(LOG_TASK_INTERRUPTED, new Object[]{this.memoryCacheKey});
            return true;
        }
    }

    private void downloadImage(File targetFile) throws IOException {
        InputStream is = getDownloader().getStream(this.uri, this.options.getExtraForDownloader());
        try {
            OutputStream os = new BufferedOutputStream(new FileOutputStream(targetFile), 32768);
            IoUtils.copyStream(is, os);
            IoUtils.closeSilently(os);
            IoUtils.closeSilently(is);
        } catch (Throwable th) {
        }
    }

    private boolean downloadSizedImage(File targetFile, int maxWidth, int maxHeight) throws IOException {
        Bitmap bmp = this.decoder.decode(new ImageDecodingInfo(this.memoryCacheKey, this.uri, new ImageSize(maxWidth, maxHeight), ViewScaleType.FIT_INSIDE, getDownloader(), new Builder().cloneFrom(this.options).imageScaleType(ImageScaleType.IN_SAMPLE_INT).build()));
        if (bmp == null) {
            return false;
        }
        if (this.configuration.processorForDiscCache != null) {
            log(LOG_PROCESS_IMAGE_BEFORE_CACHE_ON_DISC);
            bmp = this.configuration.processorForDiscCache.process(bmp);
            if (bmp == null) {
                L.e(ERROR_PROCESSOR_FOR_DISC_CACHE_NULL, new Object[]{this.memoryCacheKey});
                return false;
            }
        }
        OutputStream os = new BufferedOutputStream(new FileOutputStream(targetFile), 32768);
        boolean savedSuccessfully = bmp.compress(this.configuration.imageCompressFormatForDiscCache, this.configuration.imageQualityForDiscCache, os);
        IoUtils.closeSilently(os);
        bmp.recycle();
        return savedSuccessfully;
    }

    private void fireCancelEvent() {
        if (!Thread.interrupted()) {
            this.handler.post(new Runnable() {
                public void run() {
                    LoadAndDisplayImageTask.this.listener.onLoadingCancelled(LoadAndDisplayImageTask.this.uri, (View) LoadAndDisplayImageTask.this.imageViewRef.get());
                }
            });
        }
    }

    private void fireFailEvent(FailType failType, Throwable failCause) {
        if (!Thread.interrupted()) {
            this.handler.post(new AnonymousClass_1(failType, failCause));
        }
    }

    private ImageDownloader getDownloader() {
        if (this.engine.isNetworkDenied()) {
            return this.networkDeniedDownloader;
        }
        return this.engine.isSlowNetwork() ? this.slowNetworkDownloader : this.downloader;
    }

    private File getImageFileInDiscCache() {
        File imageFile = this.configuration.discCache.get(this.uri);
        File cacheDir = imageFile.getParentFile();
        if (cacheDir == null || !(cacheDir.exists() || cacheDir.mkdirs())) {
            imageFile = this.configuration.reserveDiscCache.get(this.uri);
            cacheDir = imageFile.getParentFile();
            if (!(cacheDir == null || cacheDir.exists())) {
                cacheDir.mkdirs();
            }
        }
        return imageFile;
    }

    private void log(String message) {
        if (this.writeLogs) {
            L.d(message, new Object[]{this.memoryCacheKey});
        }
    }

    private void log(String message, Object... args) {
        if (this.writeLogs) {
            L.d(message, args);
        }
    }

    private String tryCacheImageOnDisc(File targetFile) {
        log(LOG_CACHE_IMAGE_ON_DISC);
        try {
            int width = this.configuration.maxImageWidthForDiscCache;
            int height = this.configuration.maxImageHeightForDiscCache;
            boolean saved = false;
            if (width > 0 || height > 0) {
                saved = downloadSizedImage(targetFile, width, height);
            }
            if (!saved) {
                downloadImage(targetFile);
            }
            this.configuration.discCache.put(this.uri, targetFile);
            return Scheme.FILE.wrap(targetFile.getAbsolutePath());
        } catch (IOException e) {
            L.e(e);
            return this.uri;
        }
    }

    private Bitmap tryLoadBitmap() {
        Bitmap bitmap = null;
        File imageFile = getImageFileInDiscCache();
        Bitmap bitmap2 = null;
        try {
            if (imageFile.exists()) {
                log(LOG_LOAD_IMAGE_FROM_DISC_CACHE);
                this.loadedFrom = LoadedFrom.DISC_CACHE;
                bitmap2 = decodeImage(Scheme.FILE.wrap(imageFile.getAbsolutePath()));
                if (this.imageViewCollected) {
                    return bitmap;
                }
            }
            if (bitmap2 == null || bitmap2.getWidth() <= 0 || bitmap2.getHeight() <= 0) {
                log(LOG_LOAD_IMAGE_FROM_NETWORK);
                this.loadedFrom = LoadedFrom.NETWORK;
                String imageUriForDecoding = this.options.isCacheOnDisc() ? tryCacheImageOnDisc(imageFile) : this.uri;
                if (!checkTaskIsNotActual()) {
                    bitmap2 = decodeImage(imageUriForDecoding);
                    if (this.imageViewCollected) {
                        return bitmap;
                    }
                    if (bitmap2 == null || bitmap2.getWidth() <= 0 || bitmap2.getHeight() <= 0) {
                        fireFailEvent(FailType.DECODING_ERROR, null);
                    }
                }
            }
        } catch (IllegalStateException e) {
            fireFailEvent(FailType.NETWORK_DENIED, bitmap);
        } catch (IOException e2) {
            IOException e3 = e2;
            L.e(e3);
            fireFailEvent(FailType.IO_ERROR, e3);
            if (imageFile.exists()) {
                imageFile.delete();
            }
        } catch (OutOfMemoryError e4) {
            OutOfMemoryError e5 = e4;
            L.e(e5);
            fireFailEvent(FailType.OUT_OF_MEMORY, e5);
        } catch (Throwable th) {
            Throwable e6 = th;
            L.e(e6);
            fireFailEvent(FailType.UNKNOWN, e6);
        }
        return bitmap2;
    }

    private boolean waitIfPaused() {
        AtomicBoolean pause = this.engine.getPause();
        synchronized (pause) {
            if (pause.get()) {
                log(LOG_WAITING_FOR_RESUME);
                try {
                    pause.wait();
                    log(LOG_RESUME_AFTER_PAUSE);
                } catch (InterruptedException e) {
                    L.e(LOG_TASK_INTERRUPTED, new Object[]{this.memoryCacheKey});
                    return true;
                }
            }
        }
        return checkTaskIsNotActual();
    }

    String getLoadingUri() {
        return this.uri;
    }

    public void run() {
        if (!waitIfPaused() && !delayIfNeed()) {
            ReentrantLock loadFromUriLock = this.imageLoadingInfo.loadFromUriLock;
            log(LOG_START_DISPLAY_IMAGE_TASK);
            if (loadFromUriLock.isLocked()) {
                log(LOG_WAITING_FOR_IMAGE_LOADED);
            }
            loadFromUriLock.lock();
            if (checkTaskIsNotActual()) {
                loadFromUriLock.unlock();
            } else {
                Bitmap bmp = (Bitmap) this.configuration.memoryCache.get(this.memoryCacheKey);
                if (bmp == null) {
                    bmp = tryLoadBitmap();
                    if (this.imageViewCollected) {
                        loadFromUriLock.unlock();
                        return;
                    } else if (bmp == null) {
                        loadFromUriLock.unlock();
                        return;
                    } else if (checkTaskIsNotActual() || checkTaskIsInterrupted()) {
                        loadFromUriLock.unlock();
                        return;
                    } else {
                        if (this.options.shouldPreProcess()) {
                            log(LOG_PREPROCESS_IMAGE);
                            bmp = this.options.getPreProcessor().process(bmp);
                            if (bmp == null) {
                                L.e(ERROR_PRE_PROCESSOR_NULL, new Object[0]);
                            }
                        }
                        if (bmp != null && this.options.isCacheInMemory()) {
                            log(LOG_CACHE_IMAGE_IN_MEMORY);
                            this.configuration.memoryCache.put(this.memoryCacheKey, bmp);
                        }
                    }
                } else {
                    this.loadedFrom = LoadedFrom.MEMORY_CACHE;
                    log(LOG_GET_IMAGE_FROM_MEMORY_CACHE_AFTER_WAITING);
                }
                if (bmp != null && this.options.shouldPostProcess()) {
                    log(LOG_POSTPROCESS_IMAGE);
                    bmp = this.options.getPostProcessor().process(bmp);
                    if (bmp == null) {
                        L.e(ERROR_PRE_PROCESSOR_NULL, new Object[]{this.memoryCacheKey});
                    }
                }
                loadFromUriLock.unlock();
                if (!checkTaskIsNotActual() && !checkTaskIsInterrupted()) {
                    DisplayBitmapTask displayBitmapTask = new DisplayBitmapTask(bmp, this.imageLoadingInfo, this.engine, this.loadedFrom);
                    displayBitmapTask.setLoggingEnabled(this.writeLogs);
                    this.handler.post(displayBitmapTask);
                }
            }
        }
    }
}