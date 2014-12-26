package com.mopub.nativeads;

import android.view.View;
import java.util.Map;
import java.util.Set;

interface NativeAdInterface {
    void clear(View view);

    void destroy();

    String getCallToAction();

    String getClickDestinationUrl();

    Object getExtra(String str);

    Map<String, Object> getExtras();

    String getIconImageUrl();

    int getImpressionMinPercentageViewed();

    int getImpressionMinTimeViewed();

    Set<String> getImpressionTrackers();

    String getMainImageUrl();

    Double getStarRating();

    String getText();

    String getTitle();

    void handleClick(View view);

    boolean isOverridingClickTracker();

    boolean isOverridingImpressionTracker();

    void prepare(View view);

    void recordImpression();

    void setNativeEventListener(NativeEventListener nativeEventListener);
}