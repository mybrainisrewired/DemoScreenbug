package com.mopub.nativeads;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import com.mopub.common.logging.MoPubLog;
import com.mopub.nativeads.CustomEventNative.ImageListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

abstract class BaseForwardingNativeAd implements NativeAdInterface {
    private static final int IMPRESSION_MIN_PERCENTAGE_VIEWED = 50;
    static final double MAX_STAR_RATING = 5.0d;
    static final double MIN_STAR_RATING = 0.0d;
    private String mCallToAction;
    private String mClickDestinationUrl;
    private final Map<String, Object> mExtras;
    private String mIconImageUrl;
    private int mImpressionMinTimeViewed;
    private final Set<String> mImpressionTrackers;
    private boolean mIsOverridingClickTracker;
    private boolean mIsOverridingImpressionTracker;
    private String mMainImageUrl;
    private NativeEventListener mNativeEventListener;
    private Double mStarRating;
    private String mText;
    private String mTitle;

    static interface NativeEventListener {
        void onAdClicked();

        void onAdImpressed();
    }

    class AnonymousClass_1 implements ImageServiceListener {
        private final /* synthetic */ ImageListener val$imageListener;

        AnonymousClass_1(ImageListener imageListener) {
            this.val$imageListener = imageListener;
        }

        public void onFail() {
            this.val$imageListener.onImagesFailedToCache(NativeErrorCode.IMAGE_DOWNLOAD_FAILURE);
        }

        public void onSuccess(Map<String, Bitmap> bitmaps) {
            this.val$imageListener.onImagesCached();
        }
    }

    BaseForwardingNativeAd() {
        this.mImpressionMinTimeViewed = 1000;
        this.mImpressionTrackers = new HashSet();
        this.mExtras = new HashMap();
    }

    static void preCacheImages(Context context, List<String> imageUrls, ImageListener imageListener) {
        ImageService.get(context, imageUrls, new AnonymousClass_1(imageListener));
    }

    final void addExtra(String key, Object value) {
        this.mExtras.put(key, value);
    }

    final void addImpressionTracker(String url) {
        this.mImpressionTrackers.add(url);
    }

    public void clear(View view) {
    }

    public void destroy() {
    }

    public final String getCallToAction() {
        return this.mCallToAction;
    }

    public final String getClickDestinationUrl() {
        return this.mClickDestinationUrl;
    }

    public final Object getExtra(String key) {
        return this.mExtras.get(key);
    }

    public final Map<String, Object> getExtras() {
        return new HashMap(this.mExtras);
    }

    public final String getIconImageUrl() {
        return this.mIconImageUrl;
    }

    public final int getImpressionMinPercentageViewed() {
        return IMPRESSION_MIN_PERCENTAGE_VIEWED;
    }

    public final int getImpressionMinTimeViewed() {
        return this.mImpressionMinTimeViewed;
    }

    public final Set<String> getImpressionTrackers() {
        return new HashSet(this.mImpressionTrackers);
    }

    public final String getMainImageUrl() {
        return this.mMainImageUrl;
    }

    public final Double getStarRating() {
        return this.mStarRating;
    }

    public final String getText() {
        return this.mText;
    }

    public final String getTitle() {
        return this.mTitle;
    }

    public void handleClick(View view) {
    }

    public final boolean isOverridingClickTracker() {
        return this.mIsOverridingClickTracker;
    }

    public final boolean isOverridingImpressionTracker() {
        return this.mIsOverridingImpressionTracker;
    }

    protected final void notifyAdClicked() {
        this.mNativeEventListener.onAdClicked();
    }

    protected final void notifyAdImpressed() {
        this.mNativeEventListener.onAdImpressed();
    }

    public void prepare(View view) {
    }

    public void recordImpression() {
    }

    final void setCallToAction(String callToAction) {
        this.mCallToAction = callToAction;
    }

    final void setClickDestinationUrl(String clickDestinationUrl) {
        this.mClickDestinationUrl = clickDestinationUrl;
    }

    final void setIconImageUrl(String iconImageUrl) {
        this.mIconImageUrl = iconImageUrl;
    }

    final void setImpressionMinTimeViewed(int impressionMinTimeViewed) {
        if (impressionMinTimeViewed >= 0) {
            this.mImpressionMinTimeViewed = impressionMinTimeViewed;
        }
    }

    final void setMainImageUrl(String mainImageUrl) {
        this.mMainImageUrl = mainImageUrl;
    }

    public final void setNativeEventListener(NativeEventListener nativeEventListener) {
        this.mNativeEventListener = nativeEventListener;
    }

    final void setOverridingClickTracker(boolean isOverridingClickTracker) {
        this.mIsOverridingClickTracker = isOverridingClickTracker;
    }

    final void setOverridingImpressionTracker(boolean isOverridingImpressionTracker) {
        this.mIsOverridingImpressionTracker = isOverridingImpressionTracker;
    }

    final void setStarRating(Double starRating) {
        if (starRating == null) {
            this.mStarRating = null;
        } else if (starRating.doubleValue() < 0.0d || starRating.doubleValue() > 5.0d) {
            MoPubLog.d(new StringBuilder("Ignoring attempt to set invalid star rating (").append(starRating).append("). Must be ").append("between ").append(0.0d).append(" and ").append(MAX_STAR_RATING).append(".").toString());
        } else {
            this.mStarRating = starRating;
        }
    }

    final void setText(String text) {
        this.mText = text;
    }

    final void setTitle(String title) {
        this.mTitle = title;
    }
}