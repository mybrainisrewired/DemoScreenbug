package com.mopub.nativeads;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import com.mopub.common.VisibleForTesting;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;

class ImpressionTracker {
    private static final int PERIOD = 250;
    private final Handler mPollHandler;
    private final PollingRunnable mPollingRunnable;
    private final Map<View, TimestampWrapper<NativeResponse>> mPollingViews;
    private final Map<View, NativeResponse> mTrackedViews;
    private final VisibilityChecker mVisibilityChecker;
    private final VisibilityTracker mVisibilityTracker;
    private VisibilityTrackerListener mVisibilityTrackerListener;

    @VisibleForTesting
    class PollingRunnable implements Runnable {
        private final ArrayList<View> mRemovedViews;

        PollingRunnable() {
            this.mRemovedViews = new ArrayList();
        }

        public void run() {
            Iterator it = ImpressionTracker.this.mPollingViews.entrySet().iterator();
            while (it.hasNext()) {
                Entry<View, TimestampWrapper<NativeResponse>> entry = (Entry) it.next();
                View view = (View) entry.getKey();
                TimestampWrapper<NativeResponse> timestampWrapper = (TimestampWrapper) entry.getValue();
                if (ImpressionTracker.this.mVisibilityChecker.hasRequiredTimeElapsed(timestampWrapper.mCreatedTimestamp, ((NativeResponse) timestampWrapper.mInstance).getImpressionMinTimeViewed())) {
                    ((NativeResponse) timestampWrapper.mInstance).recordImpression(view);
                    this.mRemovedViews.add(view);
                }
            }
            Iterator it2 = this.mRemovedViews.iterator();
            while (it2.hasNext()) {
                ImpressionTracker.this.removeView(it2.next());
            }
            this.mRemovedViews.clear();
            if (!ImpressionTracker.this.mPollingViews.isEmpty()) {
                ImpressionTracker.this.scheduleNextPoll();
            }
        }
    }

    ImpressionTracker(Context context) {
        this(new WeakHashMap(), new WeakHashMap(), new VisibilityChecker(), new VisibilityTracker(context), new Handler());
    }

    @VisibleForTesting
    ImpressionTracker(Map<View, NativeResponse> trackedViews, Map<View, TimestampWrapper<NativeResponse>> pollingViews, VisibilityChecker visibilityChecker, VisibilityTracker visibilityTracker, Handler handler) {
        this.mTrackedViews = trackedViews;
        this.mPollingViews = pollingViews;
        this.mVisibilityChecker = visibilityChecker;
        this.mVisibilityTracker = visibilityTracker;
        this.mVisibilityTrackerListener = new VisibilityTrackerListener() {
            public void onVisibilityChanged(List<View> visibleViews, List<View> invisibleViews) {
                Iterator it = visibleViews.iterator();
                while (it.hasNext()) {
                    View view = (View) it.next();
                    NativeResponse nativeResponse = (NativeResponse) ImpressionTracker.this.mTrackedViews.get(view);
                    if (nativeResponse == null) {
                        ImpressionTracker.this.removeView(view);
                    } else {
                        TimestampWrapper<NativeResponse> polling = (TimestampWrapper) ImpressionTracker.this.mPollingViews.get(view);
                        if (polling == null || !nativeResponse.equals(polling.mInstance)) {
                            ImpressionTracker.this.mPollingViews.put(view, new TimestampWrapper(nativeResponse));
                        }
                    }
                }
                it = invisibleViews.iterator();
                while (it.hasNext()) {
                    ImpressionTracker.this.mPollingViews.remove(it.next());
                }
                ImpressionTracker.this.scheduleNextPoll();
            }
        };
        this.mVisibilityTracker.setVisibilityTrackerListener(this.mVisibilityTrackerListener);
        this.mPollHandler = handler;
        this.mPollingRunnable = new PollingRunnable();
    }

    private void removePollingView(View view) {
        this.mPollingViews.remove(view);
    }

    void addView(View view, NativeResponse nativeResponse) {
        if (this.mTrackedViews.get(view) != nativeResponse) {
            removeView(view);
            if (!nativeResponse.getRecordedImpression() && !nativeResponse.isDestroyed()) {
                this.mTrackedViews.put(view, nativeResponse);
                this.mVisibilityTracker.addView(view, nativeResponse.getImpressionMinPercentageViewed());
            }
        }
    }

    void clear() {
        this.mTrackedViews.clear();
        this.mPollingViews.clear();
        this.mVisibilityTracker.clear();
        this.mPollHandler.removeMessages(0);
    }

    void destroy() {
        clear();
        this.mVisibilityTracker.destroy();
        this.mVisibilityTrackerListener = null;
    }

    @Deprecated
    @VisibleForTesting
    VisibilityTrackerListener getVisibilityTrackerListener() {
        return this.mVisibilityTrackerListener;
    }

    void removeView(View view) {
        this.mTrackedViews.remove(view);
        removePollingView(view);
        this.mVisibilityTracker.removeView(view);
    }

    @VisibleForTesting
    void scheduleNextPoll() {
        if (!this.mPollHandler.hasMessages(0)) {
            this.mPollHandler.postDelayed(this.mPollingRunnable, 250);
        }
    }
}