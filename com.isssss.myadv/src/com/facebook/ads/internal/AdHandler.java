package com.facebook.ads.internal;

import android.content.Context;
import android.os.Handler;

public abstract class AdHandler {
    protected AdDataModel adDataModel;
    private Context context;
    private final Handler handler;
    protected final ImpressionHelper impressionHelper;
    private volatile boolean impressionRetryScheduled;
    private volatile boolean impressionSent;
    private final long sendImpressionDelay;
    private final Runnable sendImpressionRunnable;

    public static interface ImpressionHelper {
        void afterImpressionSent();

        void onLoggingImpression();

        boolean shouldSendImpression();
    }

    public AdHandler(ImpressionHelper impressionHelper, long sendImpressionDelay, Context context) {
        this.sendImpressionRunnable = new Runnable() {
            public void run() {
                AdHandler.this.impressionRetryScheduled = false;
                AdHandler.this.trySendImpression();
            }
        };
        this.context = context;
        this.impressionHelper = impressionHelper;
        this.sendImpressionDelay = sendImpressionDelay;
        this.handler = new Handler();
    }

    public synchronized void cancelImpressionRetry() {
        if (this.impressionRetryScheduled) {
            this.handler.removeCallbacks(this.sendImpressionRunnable);
            this.impressionRetryScheduled = false;
        }
    }

    public AdDataModel getAdDataModel() {
        return this.adDataModel;
    }

    public synchronized void scheduleImpressionRetry() {
        if (!(this.impressionSent || this.impressionRetryScheduled || this.adDataModel == null)) {
            this.handler.postDelayed(this.sendImpressionRunnable, this.sendImpressionDelay);
            this.impressionRetryScheduled = true;
        }
    }

    protected abstract void sendImpression();

    public void setAdDataModel(AdDataModel adDataModel) {
        this.adDataModel = adDataModel;
        this.impressionSent = false;
        this.impressionRetryScheduled = false;
    }

    public synchronized void trySendImpression() {
        if (!(this.adDataModel == null || this.impressionSent)) {
            if (this.impressionHelper == null || this.impressionHelper.shouldSendImpression()) {
                if (this.impressionHelper != null) {
                    this.impressionHelper.onLoggingImpression();
                }
                sendImpression();
                this.impressionSent = true;
                AdUtilities.displayDebugMessage(this.context, "Impression logged");
            } else {
                scheduleImpressionRetry();
            }
        }
    }
}