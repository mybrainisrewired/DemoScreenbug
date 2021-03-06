package com.facebook.ads.internal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Handler;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.internal.AdRequest.Callback;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AdRequestController {
    private static final String ANDROID_PERMISSION_ACCESS_NETWORK_STATE = "android.permission.ACCESS_NETWORK_STATE";
    private static final int DEFAULT_REFRESH_THRESHOLD_MILLIS = 20000;
    private static final int MIN_REFRESH_INTERVAL_MILLIS = 30000;
    private static Map<String, Long> lastRequestTimes;
    private final AdSize adSize;
    private final AdType adType;
    private final Callback adViewRequestCallback;
    private final Context context;
    private int currentVisibility;
    private final Handler handler;
    private boolean initialLoadFinished;
    private AsyncTask lastRequest;
    private final String placementId;
    private int refreshInterval;
    private final Runnable refreshRunnable;
    private volatile boolean refreshScheduled;
    private int refreshThreshold;
    private final ScreenStateReceiver screenStateReceiver;
    private boolean shouldRefresh;

    private class ScreenStateReceiver extends BroadcastReceiver {
        private ScreenStateReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.SCREEN_OFF".equals(action)) {
                AdRequestController.this.cancelRefresh(intent.getAction());
            } else if ("android.intent.action.SCREEN_ON".equals(action) && AdRequestController.this.currentVisibility == 0) {
                AdRequestController.this.scheduleRefresh(intent.getAction());
            }
        }
    }

    static {
        lastRequestTimes = new ConcurrentHashMap();
    }

    public AdRequestController(Context context, String placementId, AdSize adSize, boolean shouldRefresh, AdType adType, Callback adViewRequestCallback) {
        this.refreshInterval = 30000;
        this.refreshThreshold = 20000;
        this.initialLoadFinished = false;
        this.refreshScheduled = false;
        this.currentVisibility = 8;
        if (adViewRequestCallback == null) {
            throw new IllegalArgumentException("adViewRequestCallback");
        }
        this.context = context;
        this.placementId = placementId;
        this.adSize = adSize;
        this.shouldRefresh = shouldRefresh;
        this.adType = adType;
        this.adViewRequestCallback = adViewRequestCallback;
        this.screenStateReceiver = new ScreenStateReceiver(null);
        this.handler = new Handler();
        this.refreshRunnable = new Runnable() {
            public void run() {
                AdRequestController.this.refreshScheduled = false;
                AdRequestController.this.loadAd();
            }
        };
        registerScreenStateReceiver();
    }

    private synchronized void cancelRefresh(String reason) {
        if (this.refreshScheduled) {
            this.handler.removeCallbacks(this.refreshRunnable);
            this.refreshScheduled = false;
        }
    }

    private boolean isNetworkConnected() {
        if (this.context.checkCallingOrSelfPermission(ANDROID_PERMISSION_ACCESS_NETWORK_STATE) != 0) {
            return true;
        }
        NetworkInfo activeNetwork = ((ConnectivityManager) this.context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    private void registerScreenStateReceiver() {
        if (this.shouldRefresh) {
            IntentFilter filter = new IntentFilter("android.intent.action.SCREEN_ON");
            filter.addAction("android.intent.action.SCREEN_OFF");
            this.context.registerReceiver(this.screenStateReceiver, filter);
        }
    }

    private void unregisterScreenStateReceiver() {
        if (this.shouldRefresh) {
            this.context.unregisterReceiver(this.screenStateReceiver);
        }
    }

    public void destroy() {
        unregisterScreenStateReceiver();
        cancelRefresh("destroy");
    }

    public void disableRefresh() {
        this.shouldRefresh = false;
    }

    public synchronized void loadAd() {
        long now = System.currentTimeMillis();
        if (this.shouldRefresh) {
            Long lastRefresh = (Long) lastRequestTimes.get(this.placementId);
            if (lastRefresh != null && now < lastRefresh.longValue() + ((long) this.refreshThreshold)) {
                this.adViewRequestCallback.onError(AdError.LOAD_TOO_FREQUENTLY);
            }
        }
        if (this.refreshScheduled) {
            cancelRefresh(null);
        }
        if (!(this.lastRequest == null || this.lastRequest.getStatus() == Status.FINISHED)) {
            this.lastRequest.cancel(true);
        }
        if (isNetworkConnected()) {
            if (this.shouldRefresh) {
                lastRequestTimes.put(this.placementId, Long.valueOf(now));
            }
            this.lastRequest = new AdRequest(this.context, this.placementId, this.adSize, this.adType, AdSettings.isTestMode(this.context), new Callback() {
                public void onCompleted(AdResponse adResponse) {
                    AdRequestController.this.refreshInterval = adResponse.getRefreshIntervalMillis();
                    AdRequestController.this.refreshThreshold = adResponse.getRefreshThresholdMillis();
                    AdRequestController.this.adViewRequestCallback.onCompleted(adResponse);
                    AdRequestController.this.initialLoadFinished = true;
                }

                public void onError(AdError error) {
                    AdRequestController.this.adViewRequestCallback.onError(error);
                    AdRequestController.this.initialLoadFinished = true;
                    AdRequestController.this.scheduleRefresh("onError");
                }
            }).executeAsync();
        } else {
            this.refreshInterval = 30000;
            this.refreshThreshold = 20000;
            this.adViewRequestCallback.onError(new AdError(-1, "network unavailable"));
            scheduleRefresh("no network connection");
        }
    }

    public void onWindowVisibilityChanged(int visibility) {
        this.currentVisibility = visibility;
        if (visibility != 0) {
            cancelRefresh("onWindowVisibilityChanged");
        } else if (this.initialLoadFinished) {
            scheduleRefresh("onWindowVisibilityChanged");
        }
    }

    public synchronized void scheduleRefresh(String reason) {
        if (this.shouldRefresh) {
            if (this.refreshInterval > 0 && !this.refreshScheduled) {
                this.handler.postDelayed(this.refreshRunnable, (long) this.refreshInterval);
                this.refreshScheduled = true;
            }
        }
    }
}