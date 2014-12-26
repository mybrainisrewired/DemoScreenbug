package com.mopub.nativeads;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.logging.MoPubLog;
import com.mopub.nativeads.MoPubNative.MoPubNativeListener;
import java.util.WeakHashMap;

@Deprecated
class NativeAdViewHelper {
    @VisibleForTesting
    static final WeakHashMap<Context, ImpressionTracker> sImpressionTrackerMap;
    private static final WeakHashMap<View, NativeResponse> sNativeResponseMap;

    static {
        sImpressionTrackerMap = new WeakHashMap();
        sNativeResponseMap = new WeakHashMap();
    }

    private NativeAdViewHelper() {
    }

    private static void clearNativeResponse(Context context, View view) {
        getImpressionTracker(context).removeView(view);
        NativeResponse nativeResponse = (NativeResponse) sNativeResponseMap.get(view);
        if (nativeResponse != null) {
            nativeResponse.clear(view);
        }
    }

    @Deprecated
    static View getAdView(View convertView, ViewGroup parent, Context context, NativeResponse nativeResponse, ViewBinder viewBinder, MoPubNativeListener moPubNativeListener) {
        if (viewBinder == null) {
            MoPubLog.d("ViewBinder is null, returning empty view.");
            return new View(context);
        } else {
            MoPubNativeAdRenderer moPubNativeAdRenderer = new MoPubNativeAdRenderer(viewBinder);
            if (convertView == null) {
                convertView = moPubNativeAdRenderer.createAdView(context, parent);
            }
            clearNativeResponse(context, convertView);
            if (nativeResponse == null) {
                MoPubLog.d("NativeResponse is null, returning hidden view.");
                convertView.setVisibility(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES);
                return convertView;
            } else if (nativeResponse.isDestroyed()) {
                MoPubLog.d("NativeResponse is destroyed, returning hidden view.");
                convertView.setVisibility(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES);
                return convertView;
            } else {
                prepareNativeResponse(context, convertView, nativeResponse);
                moPubNativeAdRenderer.renderAdView(convertView, nativeResponse);
                return convertView;
            }
        }
    }

    private static ImpressionTracker getImpressionTracker(Context context) {
        ImpressionTracker impressionTracker = (ImpressionTracker) sImpressionTrackerMap.get(context);
        if (impressionTracker != null) {
            return impressionTracker;
        }
        impressionTracker = new ImpressionTracker(context);
        sImpressionTrackerMap.put(context, impressionTracker);
        return impressionTracker;
    }

    private static void prepareNativeResponse(Context context, View view, NativeResponse nativeResponse) {
        sNativeResponseMap.put(view, nativeResponse);
        if (!nativeResponse.isOverridingImpressionTracker()) {
            getImpressionTracker(context).addView(view, nativeResponse);
        }
        nativeResponse.prepare(view);
    }
}