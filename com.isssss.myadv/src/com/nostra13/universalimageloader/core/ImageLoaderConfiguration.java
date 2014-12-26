package com.nostra13.universalimageloader.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import com.nostra13.universalimageloader.cache.disc.DiscCacheAware;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.MemoryCacheAware;
import com.nostra13.universalimageloader.cache.memory.impl.FuzzyKeyMemoryCache;
import com.nostra13.universalimageloader.core.assist.MemoryCacheUtil;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.ImageDecoder;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.core.download.NetworkDeniedImageDownloader;
import com.nostra13.universalimageloader.core.download.SlowNetworkImageDownloader;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;
import com.nostra13.universalimageloader.utils.L;
import java.util.concurrent.Executor;

public final class ImageLoaderConfiguration {
    final Context context;
    final boolean customExecutor;
    final boolean customExecutorForCachedImages;
    final ImageDecoder decoder;
    final DisplayImageOptions defaultDisplayImageOptions;
    final DiscCacheAware discCache;
    final ImageDownloader downloader;
    final CompressFormat imageCompressFormatForDiscCache;
    final int imageQualityForDiscCache;
    final int maxImageHeightForDiscCache;
    final int maxImageHeightForMemoryCache;
    final int maxImageWidthForDiscCache;
    final int maxImageWidthForMemoryCache;
    final MemoryCacheAware<String, Bitmap> memoryCache;
    final ImageDownloader networkDeniedDownloader;
    final BitmapProcessor processorForDiscCache;
    final DiscCacheAware reserveDiscCache;
    final ImageDownloader slowNetworkDownloader;
    final Executor taskExecutor;
    final Executor taskExecutorForCachedImages;
    final QueueProcessingType tasksProcessingType;
    final int threadPoolSize;
    final int threadPriority;
    final boolean writeLogs;

    public static class Builder {
        public static final QueueProcessingType DEFAULT_TASK_PROCESSING_TYPE;
        public static final int DEFAULT_THREAD_POOL_SIZE = 3;
        public static final int DEFAULT_THREAD_PRIORITY = 4;
        private static final String WARNING_OVERLAP_DISC_CACHE_NAME_GENERATOR = "discCache() and discCacheFileNameGenerator() calls overlap each other";
        private static final String WARNING_OVERLAP_DISC_CACHE_PARAMS = "discCache(), discCacheSize() and discCacheFileCount calls overlap each other";
        private static final String WARNING_OVERLAP_EXECUTOR = "threadPoolSize(), threadPriority() and tasksProcessingOrder() calls can overlap taskExecutor() and taskExecutorForCachedImages() calls.";
        private static final String WARNING_OVERLAP_MEMORY_CACHE = "memoryCache() and memoryCacheSize() calls overlap each other";
        private Context context;
        private boolean customExecutor;
        private boolean customExecutorForCachedImages;
        private ImageDecoder decoder;
        private DisplayImageOptions defaultDisplayImageOptions;
        private boolean denyCacheImageMultipleSizesInMemory;
        private DiscCacheAware discCache;
        private int discCacheFileCount;
        private FileNameGenerator discCacheFileNameGenerator;
        private int discCacheSize;
        private ImageDownloader downloader;
        private CompressFormat imageCompressFormatForDiscCache;
        private int imageQualityForDiscCache;
        private int maxImageHeightForDiscCache;
        private int maxImageHeightForMemoryCache;
        private int maxImageWidthForDiscCache;
        private int maxImageWidthForMemoryCache;
        private MemoryCacheAware<String, Bitmap> memoryCache;
        private int memoryCacheSize;
        private BitmapProcessor processorForDiscCache;
        private Executor taskExecutor;
        private Executor taskExecutorForCachedImages;
        private QueueProcessingType tasksProcessingType;
        private int threadPoolSize;
        private int threadPriority;
        private boolean writeLogs;

        static {
            DEFAULT_TASK_PROCESSING_TYPE = QueueProcessingType.FIFO;
        }

        public Builder(Context context) {
            this.maxImageWidthForMemoryCache = 0;
            this.maxImageHeightForMemoryCache = 0;
            this.maxImageWidthForDiscCache = 0;
            this.maxImageHeightForDiscCache = 0;
            this.imageCompressFormatForDiscCache = null;
            this.imageQualityForDiscCache = 0;
            this.processorForDiscCache = null;
            this.taskExecutor = null;
            this.taskExecutorForCachedImages = null;
            this.customExecutor = false;
            this.customExecutorForCachedImages = false;
            this.threadPoolSize = 3;
            this.threadPriority = 4;
            this.denyCacheImageMultipleSizesInMemory = false;
            this.tasksProcessingType = DEFAULT_TASK_PROCESSING_TYPE;
            this.memoryCacheSize = 0;
            this.discCacheSize = 0;
            this.discCacheFileCount = 0;
            this.memoryCache = null;
            this.discCache = null;
            this.discCacheFileNameGenerator = null;
            this.downloader = null;
            this.defaultDisplayImageOptions = null;
            this.writeLogs = false;
            this.context = context.getApplicationContext();
        }

        private void initEmptyFieldsWithDefaultValues() {
            if (this.taskExecutor == null) {
                this.taskExecutor = DefaultConfigurationFactory.createExecutor(this.threadPoolSize, this.threadPriority, this.tasksProcessingType);
            } else {
                this.customExecutor = true;
            }
            if (this.taskExecutorForCachedImages == null) {
                this.taskExecutorForCachedImages = DefaultConfigurationFactory.createExecutor(this.threadPoolSize, this.threadPriority, this.tasksProcessingType);
            } else {
                this.customExecutorForCachedImages = true;
            }
            if (this.discCache == null) {
                if (this.discCacheFileNameGenerator == null) {
                    this.discCacheFileNameGenerator = DefaultConfigurationFactory.createFileNameGenerator();
                }
                this.discCache = DefaultConfigurationFactory.createDiscCache(this.context, this.discCacheFileNameGenerator, this.discCacheSize, this.discCacheFileCount);
            }
            if (this.memoryCache == null) {
                this.memoryCache = DefaultConfigurationFactory.createMemoryCache(this.memoryCacheSize);
            }
            if (this.denyCacheImageMultipleSizesInMemory) {
                this.memoryCache = new FuzzyKeyMemoryCache(this.memoryCache, MemoryCacheUtil.createFuzzyKeyComparator());
            }
            if (this.downloader == null) {
                this.downloader = DefaultConfigurationFactory.createImageDownloader(this.context);
            }
            if (this.decoder == null) {
                this.decoder = DefaultConfigurationFactory.createImageDecoder(this.writeLogs);
            }
            if (this.defaultDisplayImageOptions == null) {
                this.defaultDisplayImageOptions = DisplayImageOptions.createSimple();
            }
        }

        public ImageLoaderConfiguration build() {
            initEmptyFieldsWithDefaultValues();
            return new ImageLoaderConfiguration(null);
        }

        public com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder defaultDisplayImageOptions(DisplayImageOptions defaultDisplayImageOptions) {
            this.defaultDisplayImageOptions = defaultDisplayImageOptions;
            return this;
        }

        public com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder denyCacheImageMultipleSizesInMemory() {
            this.denyCacheImageMultipleSizesInMemory = true;
            return this;
        }

        public com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder discCache(DiscCacheAware discCache) {
            if (this.discCacheSize > 0 || this.discCacheFileCount > 0) {
                L.w(WARNING_OVERLAP_DISC_CACHE_PARAMS, new Object[0]);
            }
            if (this.discCacheFileNameGenerator != null) {
                L.w(WARNING_OVERLAP_DISC_CACHE_NAME_GENERATOR, new Object[0]);
            }
            this.discCache = discCache;
            return this;
        }

        public com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder discCacheExtraOptions(int maxImageWidthForDiscCache, int maxImageHeightForDiscCache, CompressFormat compressFormat, int compressQuality, BitmapProcessor processorForDiscCache) {
            this.maxImageWidthForDiscCache = maxImageWidthForDiscCache;
            this.maxImageHeightForDiscCache = maxImageHeightForDiscCache;
            this.imageCompressFormatForDiscCache = compressFormat;
            this.imageQualityForDiscCache = compressQuality;
            this.processorForDiscCache = processorForDiscCache;
            return this;
        }

        public com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder discCacheFileCount(int maxFileCount) {
            if (maxFileCount <= 0) {
                throw new IllegalArgumentException("maxFileCount must be a positive number");
            }
            if (this.discCache != null || this.discCacheSize > 0) {
                L.w(WARNING_OVERLAP_DISC_CACHE_PARAMS, new Object[0]);
            }
            this.discCacheSize = 0;
            this.discCacheFileCount = maxFileCount;
            return this;
        }

        public com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder discCacheFileNameGenerator(FileNameGenerator fileNameGenerator) {
            if (this.discCache != null) {
                L.w(WARNING_OVERLAP_DISC_CACHE_NAME_GENERATOR, new Object[0]);
            }
            this.discCacheFileNameGenerator = fileNameGenerator;
            return this;
        }

        public com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder discCacheSize(int maxCacheSize) {
            if (maxCacheSize <= 0) {
                throw new IllegalArgumentException("maxCacheSize must be a positive number");
            }
            if (this.discCache != null || this.discCacheFileCount > 0) {
                L.w(WARNING_OVERLAP_DISC_CACHE_PARAMS, new Object[0]);
            }
            this.discCacheSize = maxCacheSize;
            return this;
        }

        public com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder imageDecoder(ImageDecoder imageDecoder) {
            this.decoder = imageDecoder;
            return this;
        }

        public com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder imageDownloader(ImageDownloader imageDownloader) {
            this.downloader = imageDownloader;
            return this;
        }

        public com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder memoryCache(MemoryCacheAware<String, Bitmap> memoryCache) {
            if (this.memoryCacheSize != 0) {
                L.w(WARNING_OVERLAP_MEMORY_CACHE, new Object[0]);
            }
            this.memoryCache = memoryCache;
            return this;
        }

        public com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder memoryCacheExtraOptions(int maxImageWidthForMemoryCache, int maxImageHeightForMemoryCache) {
            this.maxImageWidthForMemoryCache = maxImageWidthForMemoryCache;
            this.maxImageHeightForMemoryCache = maxImageHeightForMemoryCache;
            return this;
        }

        public com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder memoryCacheSize(int memoryCacheSize) {
            if (memoryCacheSize <= 0) {
                throw new IllegalArgumentException("memoryCacheSize must be a positive number");
            }
            if (this.memoryCache != null) {
                L.w(WARNING_OVERLAP_MEMORY_CACHE, new Object[0]);
            }
            this.memoryCacheSize = memoryCacheSize;
            return this;
        }

        public com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder memoryCacheSizePercentage(int avaialbleMemoryPercent) {
            if (avaialbleMemoryPercent <= 0 || avaialbleMemoryPercent >= 100) {
                throw new IllegalArgumentException("avaialbleMemoryPercent must be in range (0 < % < 100)");
            }
            if (this.memoryCache != null) {
                L.w(WARNING_OVERLAP_MEMORY_CACHE, new Object[0]);
            }
            this.memoryCacheSize = (int) (((float) Runtime.getRuntime().maxMemory()) * (((float) avaialbleMemoryPercent) / 100.0f));
            return this;
        }

        public com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder taskExecutor(Executor executor) {
            if (!(this.threadPoolSize == 3 && this.threadPriority == 4 && this.tasksProcessingType == DEFAULT_TASK_PROCESSING_TYPE)) {
                L.w(WARNING_OVERLAP_EXECUTOR, new Object[0]);
            }
            this.taskExecutor = executor;
            return this;
        }

        public com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder taskExecutorForCachedImages(Executor executorForCachedImages) {
            if (!(this.threadPoolSize == 3 && this.threadPriority == 4 && this.tasksProcessingType == DEFAULT_TASK_PROCESSING_TYPE)) {
                L.w(WARNING_OVERLAP_EXECUTOR, new Object[0]);
            }
            this.taskExecutorForCachedImages = executorForCachedImages;
            return this;
        }

        public com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder tasksProcessingOrder(QueueProcessingType tasksProcessingType) {
            if (!(this.taskExecutor == null && this.taskExecutorForCachedImages == null)) {
                L.w(WARNING_OVERLAP_EXECUTOR, new Object[0]);
            }
            this.tasksProcessingType = tasksProcessingType;
            return this;
        }

        public com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder threadPoolSize(int threadPoolSize) {
            if (!(this.taskExecutor == null && this.taskExecutorForCachedImages == null)) {
                L.w(WARNING_OVERLAP_EXECUTOR, new Object[0]);
            }
            this.threadPoolSize = threadPoolSize;
            return this;
        }

        public com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder threadPriority(int threadPriority) {
            if (!(this.taskExecutor == null && this.taskExecutorForCachedImages == null)) {
                L.w(WARNING_OVERLAP_EXECUTOR, new Object[0]);
            }
            if (threadPriority < 1) {
                this.threadPriority = 1;
            } else if (threadPriority <= 10) {
                this.threadPriority = threadPriority;
            }
            return this;
        }

        public com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder writeDebugLogs() {
            this.writeLogs = true;
            return this;
        }
    }

    private ImageLoaderConfiguration(Builder builder) {
        this.context = builder.context;
        this.maxImageWidthForMemoryCache = builder.maxImageWidthForMemoryCache;
        this.maxImageHeightForMemoryCache = builder.maxImageHeightForMemoryCache;
        this.maxImageWidthForDiscCache = builder.maxImageWidthForDiscCache;
        this.maxImageHeightForDiscCache = builder.maxImageHeightForDiscCache;
        this.imageCompressFormatForDiscCache = builder.imageCompressFormatForDiscCache;
        this.imageQualityForDiscCache = builder.imageQualityForDiscCache;
        this.processorForDiscCache = builder.processorForDiscCache;
        this.taskExecutor = builder.taskExecutor;
        this.taskExecutorForCachedImages = builder.taskExecutorForCachedImages;
        this.threadPoolSize = builder.threadPoolSize;
        this.threadPriority = builder.threadPriority;
        this.tasksProcessingType = builder.tasksProcessingType;
        this.discCache = builder.discCache;
        this.memoryCache = builder.memoryCache;
        this.defaultDisplayImageOptions = builder.defaultDisplayImageOptions;
        this.writeLogs = builder.writeLogs;
        this.downloader = builder.downloader;
        this.decoder = builder.decoder;
        this.customExecutor = builder.customExecutor;
        this.customExecutorForCachedImages = builder.customExecutorForCachedImages;
        this.networkDeniedDownloader = new NetworkDeniedImageDownloader(this.downloader);
        this.slowNetworkDownloader = new SlowNetworkImageDownloader(this.downloader);
        this.reserveDiscCache = DefaultConfigurationFactory.createReserveDiscCache(this.context);
    }

    public static ImageLoaderConfiguration createDefault(Context context) {
        return new Builder(context).build();
    }
}