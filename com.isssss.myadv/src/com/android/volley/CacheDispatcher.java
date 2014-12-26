package com.android.volley;

import android.os.Process;
import com.android.volley.Cache.Entry;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import java.util.concurrent.BlockingQueue;

public class CacheDispatcher extends Thread {
    private static final boolean DEBUG;
    private final Cache mCache;
    private final BlockingQueue<Request> mCacheQueue;
    private final ResponseDelivery mDelivery;
    private final BlockingQueue<Request> mNetworkQueue;
    private volatile boolean mQuit;

    class AnonymousClass_1 implements Runnable {
        private final /* synthetic */ Request val$request;

        AnonymousClass_1(Request request) {
            this.val$request = request;
        }

        public void run() {
            try {
                CacheDispatcher.this.mNetworkQueue.put(this.val$request);
            } catch (InterruptedException e) {
            }
        }
    }

    static {
        DEBUG = VolleyLog.DEBUG;
    }

    public CacheDispatcher(BlockingQueue<Request> cacheQueue, BlockingQueue<Request> networkQueue, Cache cache, ResponseDelivery delivery) {
        this.mQuit = false;
        this.mCacheQueue = cacheQueue;
        this.mNetworkQueue = networkQueue;
        this.mCache = cache;
        this.mDelivery = delivery;
    }

    public void quit() {
        this.mQuit = true;
        interrupt();
    }

    public void run() {
        if (DEBUG) {
            VolleyLog.v("start new dispatcher", new Object[0]);
        }
        Process.setThreadPriority(ApiEventType.API_MRAID_USE_CUSTOM_CLOSE);
        this.mCache.initialize();
        while (true) {
            try {
                Request request = (Request) this.mCacheQueue.take();
                request.addMarker("cache-queue-take");
                if (request.isCanceled()) {
                    request.finish("cache-discard-canceled");
                } else {
                    Entry entry = this.mCache.get(request.getCacheKey());
                    if (entry == null) {
                        request.addMarker("cache-miss");
                        this.mNetworkQueue.put(request);
                    } else if (entry.isExpired()) {
                        request.addMarker("cache-hit-expired");
                        request.setCacheEntry(entry);
                        this.mNetworkQueue.put(request);
                    } else {
                        request.addMarker("cache-hit");
                        Response<?> response = request.parseNetworkResponse(new NetworkResponse(entry.data, entry.responseHeaders));
                        request.addMarker("cache-hit-parsed");
                        if (entry.refreshNeeded()) {
                            request.addMarker("cache-hit-refresh-needed");
                            request.setCacheEntry(entry);
                            response.intermediate = true;
                            this.mDelivery.postResponse(request, response, new AnonymousClass_1(request));
                        } else {
                            this.mDelivery.postResponse(request, response);
                        }
                    }
                }
            } catch (InterruptedException e) {
                if (this.mQuit) {
                }
            }
        }
    }
}