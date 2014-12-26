package com.mopub.nativeads;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.logging.MoPubLog;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;

class VisibilityTracker {
    @VisibleForTesting
    static final int NUM_ACCESSES_BEFORE_TRIMMING = 50;
    private static final int VISIBILITY_THROTTLE_MILLIS = 100;
    private long mAccessCounter;
    private boolean mIsVisibilityScheduled;
    @VisibleForTesting
    OnPreDrawListener mOnPreDrawListener;
    @VisibleForTesting
    final WeakReference<View> mRootView;
    private final Map<View, TrackingInfo> mTrackedViews;
    private final ArrayList<View> mTrimmedViews;
    private final VisibilityChecker mVisibilityChecker;
    private final Handler mVisibilityHandler;
    private final VisibilityRunnable mVisibilityRunnable;
    private VisibilityTrackerListener mVisibilityTrackerListener;

    static class TrackingInfo {
        long mAccessOrder;
        int mMinViewablePercent;

        TrackingInfo() {
        }
    }

    static class VisibilityChecker {
        private final Rect mClipRect;

        VisibilityChecker() {
            this.mClipRect = new Rect();
        }

        boolean hasRequiredTimeElapsed(long startTimeMillis, int minTimeViewed) {
            return SystemClock.uptimeMillis() - startTimeMillis >= ((long) minTimeViewed);
        }

        boolean isVisible(View view, int minPercentageViewed) {
            if (view == null || view.getVisibility() != 0 || view.getParent() == null || !view.getGlobalVisibleRect(this.mClipRect)) {
                return false;
            }
            long visibleViewArea = ((long) this.mClipRect.height()) * ((long) this.mClipRect.width());
            long totalViewArea = ((long) view.getHeight()) * ((long) view.getWidth());
            return totalViewArea > 0 && 100 * visibleViewArea >= ((long) minPercentageViewed) * totalViewArea;
        }
    }

    class VisibilityRunnable implements Runnable {
        private final ArrayList<View> mInvisibleViews;
        private final ArrayList<View> mVisibleViews;

        VisibilityRunnable() {
            this.mInvisibleViews = new ArrayList();
            this.mVisibleViews = new ArrayList();
        }

        public void run() {
            VisibilityTracker.this.mIsVisibilityScheduled = false;
            Iterator it = VisibilityTracker.this.mTrackedViews.entrySet().iterator();
            while (it.hasNext()) {
                Entry<View, TrackingInfo> entry = (Entry) it.next();
                View view = (View) entry.getKey();
                if (VisibilityTracker.this.mVisibilityChecker.isVisible(view, ((TrackingInfo) entry.getValue()).mMinViewablePercent)) {
                    this.mVisibleViews.add(view);
                } else {
                    this.mInvisibleViews.add(view);
                }
            }
            if (VisibilityTracker.this.mVisibilityTrackerListener != null) {
                VisibilityTracker.this.mVisibilityTrackerListener.onVisibilityChanged(this.mVisibleViews, this.mInvisibleViews);
            }
            this.mVisibleViews.clear();
            this.mInvisibleViews.clear();
        }
    }

    static interface VisibilityTrackerListener {
        void onVisibilityChanged(List<View> list, List<View> list2);
    }

    public VisibilityTracker(Context context) {
        this(context, new WeakHashMap(10), new VisibilityChecker(), new Handler());
    }

    @VisibleForTesting
    VisibilityTracker(Context context, Map<View, TrackingInfo> trackedViews, VisibilityChecker visibilityChecker, Handler visibilityHandler) {
        this.mAccessCounter = 0;
        this.mTrackedViews = trackedViews;
        this.mVisibilityChecker = visibilityChecker;
        this.mVisibilityHandler = visibilityHandler;
        this.mVisibilityRunnable = new VisibilityRunnable();
        this.mTrimmedViews = new ArrayList(50);
        View rootView = ((Activity) context).getWindow().getDecorView();
        this.mRootView = new WeakReference(rootView);
        ViewTreeObserver viewTreeObserver = rootView.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            this.mOnPreDrawListener = new OnPreDrawListener() {
                public boolean onPreDraw() {
                    VisibilityTracker.this.scheduleVisibilityCheck();
                    return true;
                }
            };
            viewTreeObserver.addOnPreDrawListener(this.mOnPreDrawListener);
        } else {
            MoPubLog.w("Visibility Tracker was unable to track views because the root view tree observer was not alive");
        }
    }

    private void trimTrackedViews(long minAccessOrder) {
        Iterator it = this.mTrackedViews.entrySet().iterator();
        while (it.hasNext()) {
            Entry<View, TrackingInfo> entry = (Entry) it.next();
            if (((TrackingInfo) entry.getValue()).mAccessOrder < minAccessOrder) {
                this.mTrimmedViews.add((View) entry.getKey());
            }
        }
        Iterator it2 = this.mTrimmedViews.iterator();
        while (it2.hasNext()) {
            removeView((View) it2.next());
        }
        this.mTrimmedViews.clear();
    }

    void addView(View view, int minPercentageViewed) {
        TrackingInfo trackingInfo = (TrackingInfo) this.mTrackedViews.get(view);
        if (trackingInfo == null) {
            trackingInfo = new TrackingInfo();
            this.mTrackedViews.put(view, trackingInfo);
            scheduleVisibilityCheck();
        }
        trackingInfo.mMinViewablePercent = minPercentageViewed;
        trackingInfo.mAccessOrder = this.mAccessCounter;
        this.mAccessCounter++;
        if (this.mAccessCounter % 50 == 0) {
            trimTrackedViews(this.mAccessCounter - 50);
        }
    }

    void clear() {
        this.mTrackedViews.clear();
        this.mVisibilityHandler.removeMessages(0);
        this.mIsVisibilityScheduled = false;
    }

    void destroy() {
        clear();
        View rootView = (View) this.mRootView.get();
        if (!(rootView == null || this.mOnPreDrawListener == null)) {
            ViewTreeObserver viewTreeObserver = rootView.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.removeOnPreDrawListener(this.mOnPreDrawListener);
            }
            this.mOnPreDrawListener = null;
        }
        this.mVisibilityTrackerListener = null;
    }

    void removeView(View view) {
        this.mTrackedViews.remove(view);
    }

    void scheduleVisibilityCheck() {
        if (!this.mIsVisibilityScheduled) {
            this.mIsVisibilityScheduled = true;
            this.mVisibilityHandler.postDelayed(this.mVisibilityRunnable, 100);
        }
    }

    void setVisibilityTrackerListener(VisibilityTrackerListener visibilityTrackerListener) {
        this.mVisibilityTrackerListener = visibilityTrackerListener;
    }
}