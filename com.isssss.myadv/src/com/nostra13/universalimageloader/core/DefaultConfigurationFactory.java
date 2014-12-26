package com.nostra13.universalimageloader.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build.VERSION;
import com.nostra13.universalimageloader.cache.disc.DiscCacheAware;
import com.nostra13.universalimageloader.cache.disc.impl.FileCountLimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.impl.TotalSizeLimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.MemoryCacheAware;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.assist.deque.LIFOLinkedBlockingDeque;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.decode.ImageDecoder;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultConfigurationFactory {

    private static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber;
        private final ThreadGroup group;
        private final String namePrefix;
        private final AtomicInteger threadNumber;
        private final int threadPriority;

        static {
            poolNumber = new AtomicInteger(1);
        }

        DefaultThreadFactory(int threadPriority) {
            this.threadNumber = new AtomicInteger(1);
            this.threadPriority = threadPriority;
            SecurityManager s = System.getSecurityManager();
            this.group = s != null ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            this.namePrefix = new StringBuilder("pool-").append(poolNumber.getAndIncrement()).append("-thread-").toString();
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(this.group, r, new StringBuilder(String.valueOf(this.namePrefix)).append(this.threadNumber.getAndIncrement()).toString(), 0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            t.setPriority(this.threadPriority);
            return t;
        }
    }

    public static BitmapDisplayer createBitmapDisplayer() {
        return new SimpleBitmapDisplayer();
    }

    public static DiscCacheAware createDiscCache(Context context, FileNameGenerator discCacheFileNameGenerator, int discCacheSize, int discCacheFileCount) {
        if (discCacheSize > 0) {
            return new TotalSizeLimitedDiscCache(StorageUtils.getIndividualCacheDirectory(context), discCacheFileNameGenerator, discCacheSize);
        }
        return discCacheFileCount > 0 ? new FileCountLimitedDiscCache(StorageUtils.getIndividualCacheDirectory(context), discCacheFileNameGenerator, discCacheFileCount) : new UnlimitedDiscCache(StorageUtils.getCacheDirectory(context), discCacheFileNameGenerator);
    }

    public static Executor createExecutor(int threadPoolSize, int threadPriority, QueueProcessingType tasksProcessingType) {
        return new ThreadPoolExecutor(threadPoolSize, threadPoolSize, 0, TimeUnit.MILLISECONDS, tasksProcessingType == QueueProcessingType.LIFO ? new LIFOLinkedBlockingDeque() : new LinkedBlockingQueue(), createThreadFactory(threadPriority));
    }

    public static FileNameGenerator createFileNameGenerator() {
        return new HashCodeFileNameGenerator();
    }

    public static ImageDecoder createImageDecoder(boolean loggingEnabled) {
        return new BaseImageDecoder(loggingEnabled);
    }

    public static ImageDownloader createImageDownloader(Context context) {
        return new BaseImageDownloader(context);
    }

    public static MemoryCacheAware<String, Bitmap> createMemoryCache(int memoryCacheSize) {
        if (memoryCacheSize == 0) {
            memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 8);
        }
        return VERSION.SDK_INT >= 9 ? new LruMemoryCache(memoryCacheSize) : new LRULimitedMemoryCache(memoryCacheSize);
    }

    public static DiscCacheAware createReserveDiscCache(Context context) {
        File cacheDir = context.getCacheDir();
        File individualDir = new File(cacheDir, "uil-images");
        if (individualDir.exists() || individualDir.mkdir()) {
            cacheDir = individualDir;
        }
        return new TotalSizeLimitedDiscCache(cacheDir, 2097152);
    }

    private static ThreadFactory createThreadFactory(int threadPriority) {
        return new DefaultThreadFactory(threadPriority);
    }
}