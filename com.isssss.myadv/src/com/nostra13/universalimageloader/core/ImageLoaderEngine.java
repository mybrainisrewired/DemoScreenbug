package com.nostra13.universalimageloader.core;

import android.widget.ImageView;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

class ImageLoaderEngine {
    private final Map<Integer, String> cacheKeysForImageViews;
    final ImageLoaderConfiguration configuration;
    private final AtomicBoolean networkDenied;
    private final AtomicBoolean paused;
    private final AtomicBoolean slowNetwork;
    private ExecutorService taskDistributor;
    private Executor taskExecutor;
    private Executor taskExecutorForCachedImages;
    private final Map<String, ReentrantLock> uriLocks;

    class AnonymousClass_1 implements Runnable {
        private final /* synthetic */ LoadAndDisplayImageTask val$task;

        AnonymousClass_1(LoadAndDisplayImageTask loadAndDisplayImageTask) {
            this.val$task = loadAndDisplayImageTask;
        }

        public void run() {
            boolean isImageCachedOnDisc = ImageLoaderEngine.this.configuration.discCache.get(this.val$task.getLoadingUri()).exists();
            ImageLoaderEngine.this.initExecutorsIfNeed();
            if (isImageCachedOnDisc) {
                ImageLoaderEngine.this.taskExecutorForCachedImages.execute(this.val$task);
            } else {
                ImageLoaderEngine.this.taskExecutor.execute(this.val$task);
            }
        }
    }

    ImageLoaderEngine(ImageLoaderConfiguration configuration) {
        this.cacheKeysForImageViews = Collections.synchronizedMap(new HashMap());
        this.uriLocks = new WeakHashMap();
        this.paused = new AtomicBoolean(false);
        this.networkDenied = new AtomicBoolean(false);
        this.slowNetwork = new AtomicBoolean(false);
        this.configuration = configuration;
        this.taskExecutor = configuration.taskExecutor;
        this.taskExecutorForCachedImages = configuration.taskExecutorForCachedImages;
        this.taskDistributor = Executors.newCachedThreadPool();
    }

    private Executor createTaskExecutor() {
        return DefaultConfigurationFactory.createExecutor(this.configuration.threadPoolSize, this.configuration.threadPriority, this.configuration.tasksProcessingType);
    }

    private void initExecutorsIfNeed() {
        if (!this.configuration.customExecutor && ((ExecutorService) this.taskExecutor).isShutdown()) {
            this.taskExecutor = createTaskExecutor();
        }
        if (!this.configuration.customExecutorForCachedImages && ((ExecutorService) this.taskExecutorForCachedImages).isShutdown()) {
            this.taskExecutorForCachedImages = createTaskExecutor();
        }
    }

    void cancelDisplayTaskFor(ImageView imageView) {
        this.cacheKeysForImageViews.remove(Integer.valueOf(imageView.hashCode()));
    }

    void denyNetworkDownloads(boolean denyNetworkDownloads) {
        this.networkDenied.set(denyNetworkDownloads);
    }

    String getLoadingUriForView(ImageView imageView) {
        return (String) this.cacheKeysForImageViews.get(Integer.valueOf(imageView.hashCode()));
    }

    ReentrantLock getLockForUri(String uri) {
        ReentrantLock lock = (ReentrantLock) this.uriLocks.get(uri);
        if (lock != null) {
            return lock;
        }
        lock = new ReentrantLock();
        this.uriLocks.put(uri, lock);
        return lock;
    }

    AtomicBoolean getPause() {
        return this.paused;
    }

    void handleSlowNetwork(boolean handleSlowNetwork) {
        this.slowNetwork.set(handleSlowNetwork);
    }

    boolean isNetworkDenied() {
        return this.networkDenied.get();
    }

    boolean isSlowNetwork() {
        return this.slowNetwork.get();
    }

    void pause() {
        this.paused.set(true);
    }

    void prepareDisplayTaskFor(ImageView imageView, String memoryCacheKey) {
        this.cacheKeysForImageViews.put(Integer.valueOf(imageView.hashCode()), memoryCacheKey);
    }

    void resume() {
        synchronized (this.paused) {
            this.paused.set(false);
            this.paused.notifyAll();
        }
    }

    void stop() {
        if (!this.configuration.customExecutor) {
            ((ExecutorService) this.taskExecutor).shutdownNow();
        }
        if (!this.configuration.customExecutorForCachedImages) {
            ((ExecutorService) this.taskExecutorForCachedImages).shutdownNow();
        }
        this.cacheKeysForImageViews.clear();
        this.uriLocks.clear();
    }

    void submit(LoadAndDisplayImageTask task) {
        this.taskDistributor.execute(new AnonymousClass_1(task));
    }

    void submit(ProcessAndDisplayImageTask task) {
        initExecutorsIfNeed();
        this.taskExecutorForCachedImages.execute(task);
    }
}