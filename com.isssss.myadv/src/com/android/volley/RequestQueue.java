package com.android.volley;

import android.os.Handler;
import android.os.Looper;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class RequestQueue {
    private static final int DEFAULT_NETWORK_THREAD_POOL_SIZE = 4;
    private final Cache mCache;
    private CacheDispatcher mCacheDispatcher;
    private final PriorityBlockingQueue<Request> mCacheQueue;
    private final Set<Request> mCurrentRequests;
    private final ResponseDelivery mDelivery;
    private NetworkDispatcher[] mDispatchers;
    private final Network mNetwork;
    private final PriorityBlockingQueue<Request> mNetworkQueue;
    private AtomicInteger mSequenceGenerator;
    private final Map<String, Queue<Request>> mWaitingRequests;

    public static interface RequestFilter {
        boolean apply(Request<?> request);
    }

    class AnonymousClass_1 implements com.android.volley.RequestQueue.RequestFilter {
        private final /* synthetic */ Object val$tag;

        AnonymousClass_1(Object obj) {
            this.val$tag = obj;
        }

        public boolean apply(Request<?> request) {
            return request.getTag() == this.val$tag;
        }
    }

    public RequestQueue(Cache cache, Network network) {
        this(cache, network, 4);
    }

    public RequestQueue(Cache cache, Network network, int threadPoolSize) {
        this(cache, network, threadPoolSize, new ExecutorDelivery(new Handler(Looper.getMainLooper())));
    }

    public RequestQueue(Cache cache, Network network, int threadPoolSize, ResponseDelivery delivery) {
        this.mSequenceGenerator = new AtomicInteger();
        this.mWaitingRequests = new HashMap();
        this.mCurrentRequests = new HashSet();
        this.mCacheQueue = new PriorityBlockingQueue();
        this.mNetworkQueue = new PriorityBlockingQueue();
        this.mCache = cache;
        this.mNetwork = network;
        this.mDispatchers = new NetworkDispatcher[threadPoolSize];
        this.mDelivery = delivery;
    }

    public Request add(Request request) {
        request.setRequestQueue(this);
        synchronized (this.mCurrentRequests) {
            this.mCurrentRequests.add(request);
        }
        request.setSequence(getSequenceNumber());
        request.addMarker("add-to-queue");
        if (request.shouldCache()) {
            synchronized (this.mWaitingRequests) {
                String cacheKey = request.getCacheKey();
                if (this.mWaitingRequests.containsKey(cacheKey)) {
                    Queue<Request> stagedRequests = (Queue) this.mWaitingRequests.get(cacheKey);
                    if (stagedRequests == null) {
                        stagedRequests = new LinkedList();
                    }
                    stagedRequests.add(request);
                    this.mWaitingRequests.put(cacheKey, stagedRequests);
                    if (VolleyLog.DEBUG) {
                        VolleyLog.v("Request for cacheKey=%s is in flight, putting on hold.", new Object[]{cacheKey});
                    }
                } else {
                    this.mWaitingRequests.put(cacheKey, null);
                    this.mCacheQueue.add(request);
                }
            }
        } else {
            this.mNetworkQueue.add(request);
        }
        return request;
    }

    public void cancelAll(RequestFilter filter) {
        synchronized (this.mCurrentRequests) {
            Iterator it = this.mCurrentRequests.iterator();
            while (it.hasNext()) {
                Request<?> request = (Request) it.next();
                if (filter.apply(request)) {
                    request.cancel();
                }
            }
        }
    }

    public void cancelAll(Object tag) {
        if (tag == null) {
            throw new IllegalArgumentException("Cannot cancelAll with a null tag");
        }
        cancelAll(new AnonymousClass_1(tag));
    }

    void finish(Request request) {
        synchronized (this.mCurrentRequests) {
            this.mCurrentRequests.remove(request);
        }
        if (request.shouldCache()) {
            synchronized (this.mWaitingRequests) {
                Queue<Request> waitingRequests = (Queue) this.mWaitingRequests.remove(request.getCacheKey());
                if (waitingRequests != null) {
                    if (VolleyLog.DEBUG) {
                        VolleyLog.v("Releasing %d waiting requests for cacheKey=%s.", new Object[]{Integer.valueOf(waitingRequests.size()), cacheKey});
                    }
                    this.mCacheQueue.addAll(waitingRequests);
                }
            }
        }
    }

    public Cache getCache() {
        return this.mCache;
    }

    public int getSequenceNumber() {
        return this.mSequenceGenerator.incrementAndGet();
    }

    public void start() {
        stop();
        this.mCacheDispatcher = new CacheDispatcher(this.mCacheQueue, this.mNetworkQueue, this.mCache, this.mDelivery);
        this.mCacheDispatcher.start();
        int i = 0;
        while (i < this.mDispatchers.length) {
            NetworkDispatcher networkDispatcher = new NetworkDispatcher(this.mNetworkQueue, this.mNetwork, this.mCache, this.mDelivery);
            this.mDispatchers[i] = networkDispatcher;
            networkDispatcher.start();
            i++;
        }
    }

    public void stop() {
        if (this.mCacheDispatcher != null) {
            this.mCacheDispatcher.quit();
        }
        int i = 0;
        while (i < this.mDispatchers.length) {
            if (this.mDispatchers[i] != null) {
                this.mDispatchers[i].quit();
            }
            i++;
        }
    }
}