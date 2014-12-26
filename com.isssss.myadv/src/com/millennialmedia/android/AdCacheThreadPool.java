package com.millennialmedia.android;

import android.content.Context;
import java.lang.ref.WeakReference;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

final class AdCacheThreadPool {
    private static AdCacheThreadPool sharedThreadPool;
    private ThreadPoolExecutor executor;
    private PriorityBlockingQueue queue;

    private class AdCacheTask implements Runnable, Comparable<AdCacheTask> {
        private CachedAd ad;
        private String adName;
        private WeakReference<Context> contextRef;
        private WeakReference<AdCacheTaskListener> listenerRef;

        AdCacheTask(Context context, String adName, CachedAd ad, AdCacheTaskListener listener) {
            this.contextRef = new WeakReference(context.getApplicationContext());
            this.adName = adName;
            this.ad = ad;
            if (listener != null) {
                this.listenerRef = new WeakReference(listener);
            }
        }

        public int compareTo(AdCacheTask other) {
            return this.ad.downloadPriority - other.ad.downloadPriority;
        }

        public boolean equals(AdCacheTask other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof AdCacheTask)) {
                return false;
            }
            return this.ad.equals(other.ad);
        }

        public void run() {
            String str = null;
            AdCacheTaskListener listener = null;
            if (this.listenerRef != null) {
                listener = this.listenerRef.get();
            }
            if (listener != null) {
                listener.downloadStart(this.ad);
            }
            HandShake.sharedHandShake((Context) this.contextRef.get()).lockAdTypeDownload(this.adName);
            boolean success = this.ad.download((Context) this.contextRef.get());
            HandShake.sharedHandShake((Context) this.contextRef.get()).unlockAdTypeDownload(this.adName);
            if (success) {
                AdCache.setIncompleteDownload((Context) this.contextRef.get(), this.adName, null);
            } else {
                String incompleteId = AdCache.getIncompleteDownload((Context) this.contextRef.get(), this.adName);
                if (incompleteId == null || !this.ad.getId().equals(incompleteId)) {
                    Context context = (Context) this.contextRef.get();
                    String str2 = this.adName;
                    if (!this.ad.downloadAllOrNothing) {
                        str = this.ad.getId();
                    }
                    AdCache.setIncompleteDownload(context, str2, str);
                } else {
                    this.ad.delete((Context) this.contextRef.get());
                    AdCache.setIncompleteDownload((Context) this.contextRef.get(), this.adName, null);
                }
            }
            if (listener != null) {
                listener.downloadCompleted(this.ad, success);
            }
        }
    }

    private AdCacheThreadPool() {
        TimeUnit timeUnit = TimeUnit.SECONDS;
        BlockingQueue priorityBlockingQueue = new PriorityBlockingQueue(32);
        this.queue = priorityBlockingQueue;
        this.executor = new ThreadPoolExecutor(1, 2, 30, timeUnit, priorityBlockingQueue);
    }

    static synchronized AdCacheThreadPool sharedThreadPool() {
        AdCacheThreadPool adCacheThreadPool;
        synchronized (AdCacheThreadPool.class) {
            if (sharedThreadPool == null) {
                sharedThreadPool = new AdCacheThreadPool();
            }
            adCacheThreadPool = sharedThreadPool;
        }
        return adCacheThreadPool;
    }

    synchronized boolean startDownloadTask(Context context, String adName, CachedAd ad, AdCacheTaskListener listener) {
        boolean z;
        if (!(context == null || ad == null)) {
            AdCacheTask task = new AdCacheTask(context, adName, ad, listener);
            if (!(this.queue.contains(task) || ad.isOnDisk(context))) {
                this.executor.execute(task);
                z = true;
            }
        }
        z = false;
        return z;
    }
}