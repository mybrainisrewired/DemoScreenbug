package com.inmobi.commons.internal;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.inmobi.commons.analytics.bootstrapper.AnalyticsInitializer;
import com.inmobi.commons.internal.ActivityRecognitionSampler.ActivitySample;
import com.millennialmedia.android.MMAdView;

// compiled from: ActivityRecognitionSampler.java
static class a extends Handler {
    a(Looper looper) {
        super(looper);
    }

    public void handleMessage(Message message) {
        switch (message.what) {
            case MMAdView.TRANSITION_FADE:
                int detectedActivity = ActivityRecognitionManager.getDetectedActivity();
                if (detectedActivity != -1 && ((long) ActivityRecognitionSampler.d.size()) <= AnalyticsInitializer.getConfigParams().getThinIceConfig().getActivityDetectionMaxSize()) {
                    ActivityRecognitionSampler.d.add(new ActivitySample(detectedActivity, System.currentTimeMillis()));
                    ActivityRecognitionSampler.b();
                }
            default:
                break;
        }
    }
}