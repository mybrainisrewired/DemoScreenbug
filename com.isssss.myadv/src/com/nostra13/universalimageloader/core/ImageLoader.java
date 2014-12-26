package com.nostra13.universalimageloader.core;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.nostra13.universalimageloader.cache.disc.DiscCacheAware;
import com.nostra13.universalimageloader.cache.memory.MemoryCacheAware;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.assist.MemoryCacheUtil;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FakeBitmapDisplayer;
import com.nostra13.universalimageloader.utils.ImageSizeUtils;
import com.nostra13.universalimageloader.utils.L;
import java.io.File;
import java.util.Iterator;

public class ImageLoader {
    private static final String ERROR_INIT_CONFIG_WITH_NULL = "ImageLoader configuration can not be initialized with null";
    private static final String ERROR_NOT_INIT = "ImageLoader must be init with configuration before using";
    private static final String ERROR_WRONG_ARGUMENTS = "Wrong arguments were passed to displayImage() method (ImageView reference must not be null)";
    static final String LOG_DESTROY = "Destroy ImageLoader";
    static final String LOG_INIT_CONFIG = "Initialize ImageLoader with configuration";
    static final String LOG_LOAD_IMAGE_FROM_MEMORY_CACHE = "Load image from memory cache [%s]";
    public static final String TAG;
    private static final String WARNING_RE_INIT_CONFIG = "Try to initialize ImageLoader which had already been initialized before. To re-init ImageLoader with new configuration call ImageLoader.destroy() at first.";
    private static volatile ImageLoader instance;
    private ImageLoaderConfiguration configuration;
    private final ImageLoadingListener emptyListener;
    private ImageLoaderEngine engine;
    private final BitmapDisplayer fakeBitmapDisplayer;

    static {
        TAG = ImageLoader.class.getSimpleName();
    }

    protected ImageLoader() {
        this.emptyListener = new SimpleImageLoadingListener();
        this.fakeBitmapDisplayer = new FakeBitmapDisplayer();
    }

    private void checkConfiguration() {
        if (this.configuration == null) {
            throw new IllegalStateException(ERROR_NOT_INIT);
        }
    }

    public static ImageLoader getInstance() {
        if (instance == null) {
            synchronized (ImageLoader.class) {
                if (instance == null) {
                    instance = new ImageLoader();
                }
            }
        }
        return instance;
    }

    public void addDiskCache(String url, File file) {
        this.configuration.discCache.put(url, file);
    }

    public void cancelDisplayTask(ImageView imageView) {
        this.engine.cancelDisplayTaskFor(imageView);
    }

    public void clearDiscCache() {
        checkConfiguration();
        this.configuration.discCache.clear();
    }

    public void clearMemoryCache() {
        checkConfiguration();
        this.configuration.memoryCache.clear();
    }

    public void createLocalCacheBitmap(String requestUrl, Bitmap cachedBitmap) {
        DisplayMetrics displayMetrics = this.configuration.context.getResources().getDisplayMetrics();
        this.configuration.memoryCache.put(MemoryCacheUtil.generateKey(requestUrl, new ImageSize(displayMetrics.widthPixels, displayMetrics.heightPixels)), cachedBitmap);
    }

    public void denyNetworkDownloads(boolean denyNetworkDownloads) {
        this.engine.denyNetworkDownloads(denyNetworkDownloads);
    }

    public void destroy() {
        if (this.configuration != null && this.configuration.writeLogs) {
            L.d(LOG_DESTROY, new Object[0]);
        }
        stop();
        this.engine = null;
        this.configuration = null;
    }

    public void displayImage(String uri, ImageView imageView) {
        displayImage(uri, imageView, null, null);
    }

    public void displayImage(String uri, ImageView imageView, DisplayImageOptions options) {
        displayImage(uri, imageView, options, null);
    }

    public void displayImage(String uri, ImageView imageView, DisplayImageOptions options, ImageLoadingListener listener) {
        checkConfiguration();
        if (imageView == null) {
            throw new IllegalArgumentException(ERROR_WRONG_ARGUMENTS);
        }
        if (listener == null) {
            listener = this.emptyListener;
        }
        if (options == null) {
            options = this.configuration.defaultDisplayImageOptions;
        }
        if (TextUtils.isEmpty(uri)) {
            this.engine.cancelDisplayTaskFor(imageView);
            listener.onLoadingStarted(uri, imageView);
            if (options.shouldShowImageResForEmptyUri()) {
                imageView.setImageResource(options.getImageResForEmptyUri());
            } else if (options.shouldShowBitmapForEmptyUri()) {
                imageView.setImageBitmap(options.getBitmapForEmptyUri());
            } else {
                imageView.setImageDrawable(null);
            }
            listener.onLoadingComplete(uri, imageView, null);
        } else {
            ImageSize targetSize = ImageSizeUtils.defineTargetSizeForView(imageView, this.configuration.maxImageWidthForMemoryCache, this.configuration.maxImageHeightForMemoryCache);
            String memoryCacheKey = MemoryCacheUtil.generateKey(uri, targetSize);
            this.engine.prepareDisplayTaskFor(imageView, memoryCacheKey);
            listener.onLoadingStarted(uri, imageView);
            Bitmap bmp = (Bitmap) this.configuration.memoryCache.get(memoryCacheKey);
            if (bmp == null || bmp.isRecycled()) {
                if (options.shouldShowImageResOnLoading()) {
                    imageView.setImageResource(options.getImageResOnLoading());
                } else if (options.shouldShowBitmapOnLoading()) {
                    imageView.setImageBitmap(options.getBitmapOnLoading());
                } else if (options.isResetViewBeforeLoading()) {
                    imageView.setImageDrawable(null);
                }
                this.engine.submit(new LoadAndDisplayImageTask(this.engine, new ImageLoadingInfo(uri, imageView, targetSize, memoryCacheKey, options, listener, this.engine.getLockForUri(uri)), options.getHandler()));
            } else {
                if (this.configuration.writeLogs) {
                    L.d(LOG_LOAD_IMAGE_FROM_MEMORY_CACHE, new Object[]{memoryCacheKey});
                }
                if (options.shouldPostProcess()) {
                    this.engine.submit(new ProcessAndDisplayImageTask(this.engine, bmp, new ImageLoadingInfo(uri, imageView, targetSize, memoryCacheKey, options, listener, this.engine.getLockForUri(uri)), options.getHandler()));
                } else {
                    options.getDisplayer().display(bmp, imageView, LoadedFrom.MEMORY_CACHE);
                    listener.onLoadingComplete(uri, imageView, bmp);
                }
            }
        }
    }

    public void displayImage(String uri, ImageView imageView, ImageLoadingListener listener) {
        displayImage(uri, imageView, null, listener);
    }

    public DiscCacheAware getDiscCache() {
        checkConfiguration();
        return this.configuration.discCache;
    }

    public Bitmap getImageBitmap(String requestUrl) {
        DisplayMetrics displayMetrics = this.configuration.context.getResources().getDisplayMetrics();
        return (Bitmap) this.configuration.memoryCache.get(MemoryCacheUtil.generateKey(requestUrl, new ImageSize(displayMetrics.widthPixels, displayMetrics.heightPixels)));
    }

    public String getLoadingUriForView(ImageView imageView) {
        return this.engine.getLoadingUriForView(imageView);
    }

    public File getLocalDiskCachePath(String requestUrl) {
        return this.configuration.discCache.get(requestUrl);
    }

    public MemoryCacheAware<String, Bitmap> getMemoryCache() {
        checkConfiguration();
        return this.configuration.memoryCache;
    }

    public void handleSlowNetwork(boolean handleSlowNetwork) {
        this.engine.handleSlowNetwork(handleSlowNetwork);
    }

    public synchronized void init(ImageLoaderConfiguration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException(ERROR_INIT_CONFIG_WITH_NULL);
        } else if (this.configuration == null) {
            if (configuration.writeLogs) {
                L.d(LOG_INIT_CONFIG, new Object[0]);
            }
            this.engine = new ImageLoaderEngine(configuration);
            this.configuration = configuration;
        } else {
            L.w(WARNING_RE_INIT_CONFIG, new Object[0]);
        }
    }

    public boolean isInited() {
        return this.configuration != null;
    }

    public void loadImage(String uri, DisplayImageOptions options, ImageLoadingListener listener) {
        loadImage(uri, null, options, listener);
    }

    public void loadImage(String uri, ImageLoadingListener listener) {
        loadImage(uri, null, null, listener);
    }

    public void loadImage(String uri, ImageSize targetImageSize, DisplayImageOptions options, ImageLoadingListener listener) {
        DisplayImageOptions optionsWithFakeDisplayer;
        checkConfiguration();
        if (targetImageSize == null) {
            targetImageSize = new ImageSize(this.configuration.maxImageWidthForMemoryCache, this.configuration.maxImageHeightForMemoryCache);
        }
        if (options == null) {
            options = this.configuration.defaultDisplayImageOptions;
        }
        if (options.getDisplayer() instanceof FakeBitmapDisplayer) {
            optionsWithFakeDisplayer = options;
        } else {
            optionsWithFakeDisplayer = new Builder().cloneFrom(options).displayer(this.fakeBitmapDisplayer).build();
        }
        ImageView fakeImage = new ImageView(this.configuration.context);
        fakeImage.setLayoutParams(new LayoutParams(targetImageSize.getWidth(), targetImageSize.getHeight()));
        fakeImage.setScaleType(ScaleType.CENTER_CROP);
        displayImage(uri, fakeImage, optionsWithFakeDisplayer, listener);
    }

    public void loadImage(String uri, ImageSize minImageSize, ImageLoadingListener listener) {
        loadImage(uri, minImageSize, null, listener);
    }

    public void pause() {
        this.engine.pause();
    }

    public void removeCache(String requestUrl) {
        File file = this.configuration.discCache.get(requestUrl);
        if (file.exists()) {
            file.delete();
        }
        Iterator it = this.configuration.memoryCache.keys().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            if (key.startsWith(requestUrl)) {
                this.configuration.memoryCache.remove(key);
            }
        }
    }

    public void resume() {
        this.engine.resume();
    }

    public void stop() {
        this.engine.stop();
    }
}