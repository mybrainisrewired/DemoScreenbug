package com.mopub.nativeads;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.logging.MoPubLog;
import com.mopub.nativeads.MoPubNative.MoPubNativeListener;
import java.lang.ref.WeakReference;

@Deprecated
public final class AdapterHelper {
    private final WeakReference<Activity> mActivity;
    private final Context mApplicationContext;
    private final int mInterval;
    private final int mStart;

    @Deprecated
    public AdapterHelper(Context context, int start, int interval) throws IllegalArgumentException {
        if (context == null) {
            throw new IllegalArgumentException("Illegal argument: Context was null.");
        } else if (!(context instanceof Activity)) {
            throw new IllegalArgumentException("Illegal argument: Context must be instance of Activity.");
        } else if (start < 0) {
            throw new IllegalArgumentException("Illegal argument: negative starting position.");
        } else if (interval < 2) {
            throw new IllegalArgumentException("Illegal argument: interval must be at least 2.");
        } else {
            this.mActivity = new WeakReference((Activity) context);
            this.mApplicationContext = context.getApplicationContext();
            this.mStart = start;
            this.mInterval = interval;
        }
    }

    private int numberOfAdsSeenUpToPosition(int position) {
        return position <= this.mStart ? 0 : ((int) Math.floor(((double) (position - this.mStart)) / ((double) this.mInterval))) + 1;
    }

    private int numberOfAdsThatCouldFitWithContent(int contentRowCount) {
        if (contentRowCount <= this.mStart) {
            return 0;
        }
        int spacesBetweenAds = this.mInterval - 1;
        return (contentRowCount - this.mStart) % spacesBetweenAds == 0 ? (contentRowCount - this.mStart) / spacesBetweenAds : ((int) Math.floor(((double) (contentRowCount - this.mStart)) / ((double) spacesBetweenAds))) + 1;
    }

    @Deprecated
    @VisibleForTesting
    void clearActivityContext() {
        this.mActivity.clear();
    }

    @Deprecated
    public View getAdView(View convertView, ViewGroup parent, NativeResponse nativeResponse, ViewBinder viewBinder, MoPubNativeListener moPubNativeListener) {
        Activity activity = (Activity) this.mActivity.get();
        if (activity != null) {
            return NativeAdViewHelper.getAdView(convertView, parent, activity, nativeResponse, viewBinder, moPubNativeListener);
        }
        MoPubLog.d("Weak reference to Activity Context in AdapterHelper became null. Returning empty view.");
        return new View(this.mApplicationContext);
    }

    @Deprecated
    public boolean isAdPosition(int position) {
        return position >= this.mStart && (position - this.mStart) % this.mInterval == 0;
    }

    @Deprecated
    public int shiftedCount(int originalCount) {
        return numberOfAdsThatCouldFitWithContent(originalCount) + originalCount;
    }

    @Deprecated
    public int shiftedPosition(int position) {
        return position - numberOfAdsSeenUpToPosition(position);
    }
}