package com.android.volley;

import android.annotation.SuppressLint;
import android.net.TrafficStats;
import android.os.Build.VERSION;
import android.os.Process;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import java.util.concurrent.BlockingQueue;

public class NetworkDispatcher extends Thread {
    private final Cache mCache;
    private final ResponseDelivery mDelivery;
    private final Network mNetwork;
    private final BlockingQueue<Request> mQueue;
    private volatile boolean mQuit;

    public NetworkDispatcher(BlockingQueue<Request> queue, Network network, Cache cache, ResponseDelivery delivery) {
        this.mQuit = false;
        this.mQueue = queue;
        this.mNetwork = network;
        this.mCache = cache;
        this.mDelivery = delivery;
    }

    private void parseAndDeliverNetworkError(Request<?> request, VolleyError error) {
        this.mDelivery.postError(request, request.parseNetworkError(error));
    }

    public void quit() {
        this.mQuit = true;
        interrupt();
    }

    @SuppressLint({"NewApi"})
    public void run() {
        Process.setThreadPriority(ApiEventType.API_MRAID_USE_CUSTOM_CLOSE);
        while (true) {
            try {
                Request request = (Request) this.mQueue.take();
                try {
                    request.addMarker("network-queue-take");
                    if (request.isCanceled()) {
                        request.finish("network-discard-cancelled");
                    } else {
                        if (VERSION.SDK_INT >= 14) {
                            TrafficStats.setThreadStatsTag(request.getTrafficStatsTag());
                        }
                        NetworkResponse networkResponse = this.mNetwork.performRequest(request);
                        request.addMarker("network-http-complete");
                        if (networkResponse.notModified && request.hasHadResponseDelivered()) {
                            request.finish("not-modified");
                        } else {
                            Response<?> response = request.parseNetworkResponse(networkResponse);
                            request.addMarker("network-parse-complete");
                            if (request.shouldCache() && response.cacheEntry != null) {
                                this.mCache.put(request.getCacheKey(), response.cacheEntry);
                                request.addMarker("network-cache-written");
                            }
                            request.markDelivered();
                            this.mDelivery.postResponse(request, response);
                        }
                    }
                } catch (VolleyError e) {
                    parseAndDeliverNetworkError(request, e);
                } catch (Exception e2) {
                    Throwable e3 = e2;
                    VolleyLog.e(e3, "Unhandled exception %s", new Object[]{e3.toString()});
                    this.mDelivery.postError(request, new VolleyError(e3));
                }
            } catch (InterruptedException e4) {
                if (this.mQuit) {
                }
            }
        }
    }
}