package com.millennialmedia.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

@Deprecated
public class MMBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION_AD_SINGLE_TAP = "millennialmedia.action.ACTION_AD_SINGLE_TAP";
    public static final String ACTION_DISPLAY_STARTED = "millennialmedia.action.ACTION_DISPLAY_STARTED";
    public static final String ACTION_FETCH_FAILED = "millennialmedia.action.ACTION_FETCH_FAILED";
    public static final String ACTION_FETCH_STARTED_CACHING = "millennialmedia.action.ACTION_FETCH_STARTED_CACHING";
    public static final String ACTION_FETCH_SUCCEEDED = "millennialmedia.action.ACTION_FETCH_SUCCEEDED";
    public static final String ACTION_GETAD_FAILED = "millennialmedia.action.ACTION_GETAD_FAILED";
    public static final String ACTION_GETAD_SUCCEEDED = "millennialmedia.action.ACTION_GETAD_SUCCEEDED";
    public static final String ACTION_INTENT_STARTED = "millennialmedia.action.ACTION_INTENT_STARTED";
    public static final String ACTION_OVERLAY_CLOSED = "millennialmedia.action.ACTION_OVERLAY_CLOSED";
    public static final String ACTION_OVERLAY_OPENED = "millennialmedia.action.ACTION_OVERLAY_OPENED";
    @Deprecated
    public static final String ACTION_OVERLAY_TAP = "millennialmedia.action.ACTION_OVERLAY_TAP";
    public static final String CATEGORY_SDK = "millennialmedia.category.CATEGORY_SDK";
    private static final String TAG = "MMBroadcastReceiver";

    public static IntentFilter createIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addCategory(CATEGORY_SDK);
        filter.addAction(ACTION_DISPLAY_STARTED);
        filter.addAction(ACTION_FETCH_FAILED);
        filter.addAction(ACTION_FETCH_SUCCEEDED);
        filter.addAction(ACTION_FETCH_STARTED_CACHING);
        filter.addAction(ACTION_GETAD_FAILED);
        filter.addAction(ACTION_GETAD_SUCCEEDED);
        filter.addAction(ACTION_INTENT_STARTED);
        filter.addAction(ACTION_OVERLAY_CLOSED);
        filter.addAction(ACTION_OVERLAY_OPENED);
        filter.addAction(ACTION_OVERLAY_TAP);
        filter.addAction(ACTION_AD_SINGLE_TAP);
        return filter;
    }

    public void displayStarted(MMAd ad) {
        MMLog.i(TAG, "Millennial Media display started.");
    }

    public void fetchFailure(MMAd ad) {
        MMLog.i(TAG, "Millennial Media fetch failed.");
    }

    public void fetchFinishedCaching(MMAd ad) {
        MMLog.i(TAG, "Millennial Media fetch finished caching.");
    }

    public void fetchStartedCaching(MMAd ad) {
        MMLog.i(TAG, "Millennial Media fetch started caching.");
    }

    public void getAdFailure(MMAd ad) {
        MMLog.i(TAG, "Millennial Media ad Failure.");
    }

    public void getAdSuccess(MMAd ad) {
        MMLog.i(TAG, "Millennial Media ad Success.");
    }

    public void intentStarted(MMAd ad, String intent) {
        if (intent != null) {
            MMLog.i(TAG, "Millennial Media started intent: " + intent);
        }
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String packageName = intent.getStringExtra("packageName");
        long internalId = intent.getLongExtra("internalId", -4);
        MMAd ad = null;
        if (context.getPackageName().equals(packageName)) {
            if (internalId != -4) {
                MMAdImpl adImpl = MMAdImplController.getAdImplWithId(internalId);
                if (adImpl != null) {
                    ad = adImpl.getCallingAd();
                }
            }
            MMLog.v(TAG, " @@ Intent - Ad in receiver = " + (ad == null ? " null" : ad.toString()));
            if (action.equals(ACTION_OVERLAY_OPENED)) {
                overlayOpened(ad);
            } else if (action.equals(ACTION_OVERLAY_CLOSED)) {
                overlayClosed(ad);
            } else if (action.equals(ACTION_OVERLAY_TAP)) {
                overlayTap(ad);
            } else if (action.equals(ACTION_AD_SINGLE_TAP)) {
                onSingleTap(ad);
            } else if (action.equals(ACTION_DISPLAY_STARTED)) {
                displayStarted(ad);
            } else if (action.equals(ACTION_FETCH_FAILED)) {
                fetchFailure(ad);
            } else if (action.equals(ACTION_FETCH_SUCCEEDED)) {
                fetchFinishedCaching(ad);
            } else if (action.equals(ACTION_FETCH_STARTED_CACHING)) {
                fetchStartedCaching(ad);
            } else if (action.equals(ACTION_GETAD_FAILED)) {
                getAdFailure(ad);
            } else if (action.equals(ACTION_GETAD_SUCCEEDED)) {
                getAdSuccess(ad);
            } else if (action.equals(ACTION_INTENT_STARTED)) {
                intentStarted(ad, intent.getStringExtra("intentType"));
            }
        }
    }

    public void onSingleTap(MMAd ad) {
        MMLog.i(TAG, "Millennial Media ad Tap.");
    }

    public void overlayClosed(MMAd ad) {
        MMLog.i(TAG, "Millennial Media overlay closed.");
    }

    public void overlayOpened(MMAd ad) {
        MMLog.i(TAG, "Millennial Media overlay opened.");
    }

    @Deprecated
    public void overlayTap(MMAd ad) {
        MMLog.i(TAG, "Millennial Media overlay Tap.");
    }
}