package com.mopub.nativeads;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import com.mopub.common.VisibleForTesting;
import com.mopub.nativeads.MoPubNativeAdPositioning.MoPubClientPositioning;
import com.mopub.nativeads.MoPubNativeAdPositioning.MoPubServerPositioning;
import com.mopub.nativeads.PositioningSource.PositioningListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.WeakHashMap;

public class MoPubStreamAdPlacer {
    private static final int MAX_VISIBLE_RANGE = 100;
    private static final int RANGE_BUFFER = 10;
    private MoPubNativeAdLoadedListener mAdLoadedListener;
    private MoPubAdRenderer mAdRenderer;
    private final NativeAdSource mAdSource;
    private String mAdUnitId;
    private final Context mContext;
    private boolean mHasPlacedAds;
    private boolean mHasReceivedAds;
    private boolean mHasReceivedPositions;
    private final ImpressionTracker mImpressionTracker;
    private int mItemCount;
    private final WeakHashMap<View, NativeResponse> mNativeResponseMap;
    private boolean mNeedsPlacement;
    private PlacementData mPendingPlacementData;
    private PlacementData mPlacementData;
    private final Handler mPlacementHandler;
    private final Runnable mPlacementRunnable;
    private final PositioningSource mPositioningSource;
    private int mVisibleRangeEnd;
    private int mVisibleRangeStart;

    public MoPubStreamAdPlacer(Context context) {
        this(context, MoPubNativeAdPositioning.serverPositioning());
    }

    public MoPubStreamAdPlacer(Context context, MoPubClientPositioning adPositioning) {
        this(context, new NativeAdSource(), new ImpressionTracker(context), new ClientPositioningSource(adPositioning));
    }

    public MoPubStreamAdPlacer(Context context, MoPubServerPositioning adPositioning) {
        this(context, new NativeAdSource(), new ImpressionTracker(context), new ServerPositioningSource(context));
    }

    @VisibleForTesting
    MoPubStreamAdPlacer(Context context, NativeAdSource adSource, ImpressionTracker impressionTracker, PositioningSource positioningSource) {
        this.mContext = context;
        this.mImpressionTracker = impressionTracker;
        this.mPositioningSource = positioningSource;
        this.mAdSource = adSource;
        this.mPlacementData = PlacementData.empty();
        this.mNativeResponseMap = new WeakHashMap();
        this.mPlacementHandler = new Handler();
        this.mPlacementRunnable = new Runnable() {
            public void run() {
                if (MoPubStreamAdPlacer.this.mNeedsPlacement) {
                    MoPubStreamAdPlacer.this.placeAds();
                    MoPubStreamAdPlacer.this.mNeedsPlacement = false;
                }
            }
        };
        this.mVisibleRangeStart = 0;
        this.mVisibleRangeEnd = 0;
    }

    private void clearNativeResponse(View view) {
        this.mImpressionTracker.removeView(view);
        NativeResponse lastNativeResponse = (NativeResponse) this.mNativeResponseMap.get(view);
        if (lastNativeResponse != null) {
            lastNativeResponse.clear(view);
        }
    }

    private NativeAdData createAdData(int position, NativeResponse adResponse) {
        return new NativeAdData(this.mAdUnitId, this.mAdRenderer, adResponse);
    }

    private void notifyNeedsPlacement() {
        if (!this.mNeedsPlacement) {
            this.mNeedsPlacement = true;
            this.mPlacementHandler.post(this.mPlacementRunnable);
        }
    }

    private void placeAds() {
        if (tryPlaceAdsInRange(this.mVisibleRangeStart, this.mVisibleRangeEnd)) {
            tryPlaceAdsInRange(this.mVisibleRangeEnd, this.mVisibleRangeEnd + 10);
        }
    }

    private void placeInitialAds(PlacementData placementData) {
        removeAdsInRange(0, this.mItemCount);
        this.mPlacementData = placementData;
        placeAds();
        this.mHasPlacedAds = true;
    }

    private void prepareNativeResponse(NativeResponse nativeResponse, View view) {
        this.mNativeResponseMap.put(view, nativeResponse);
        if (!nativeResponse.isOverridingImpressionTracker()) {
            this.mImpressionTracker.addView(view, nativeResponse);
        }
        nativeResponse.prepare(view);
    }

    private boolean tryPlaceAd(int position) {
        NativeResponse adResponse = this.mAdSource.dequeueAd();
        if (adResponse == null) {
            return false;
        }
        this.mPlacementData.placeAd(position, createAdData(position, adResponse));
        this.mItemCount++;
        if (this.mAdLoadedListener != null) {
            this.mAdLoadedListener.onAdLoaded(position);
        }
        return true;
    }

    private boolean tryPlaceAdsInRange(int start, int end) {
        int position = start;
        int lastPosition = end - 1;
        while (position <= lastPosition && position != -1 && position < this.mItemCount) {
            if (this.mPlacementData.shouldPlaceAd(position) && !tryPlaceAd(position)) {
                return false;
            }
            lastPosition++;
            position = this.mPlacementData.nextInsertionPosition(position);
        }
        return true;
    }

    public void clearAds() {
        removeAdsInRange(0, this.mItemCount);
        this.mAdSource.clear();
    }

    public void destroy() {
        this.mPlacementHandler.removeMessages(0);
        this.mAdSource.clear();
        this.mImpressionTracker.destroy();
        this.mPlacementData.clearAds();
    }

    public Object getAdData(int position) {
        return this.mPlacementData.getPlacedAd(position);
    }

    public View getAdView(int position, View convertView, ViewGroup parent) {
        if (!isAd(position)) {
            return null;
        }
        View view;
        NativeAdData adData = this.mPlacementData.getPlacedAd(position);
        MoPubAdRenderer adRenderer = adData.getAdRenderer();
        if (convertView != null) {
            view = convertView;
        } else {
            view = adRenderer.createAdView(this.mContext, parent);
        }
        NativeResponse nativeResponse = adData.getAd();
        if (nativeResponse.equals(this.mNativeResponseMap.get(view))) {
            return view;
        }
        clearNativeResponse(view);
        prepareNativeResponse(nativeResponse, view);
        adRenderer.renderAdView(view, nativeResponse);
        return view;
    }

    public int getAdViewType(int position) {
        return isAd(position) ? 0 : 1;
    }

    public int getAdViewTypeCount() {
        return 1;
    }

    public int getAdjustedCount(int originalCount) {
        return this.mPlacementData.getAdjustedCount(originalCount);
    }

    public int getAdjustedPosition(int originalPosition) {
        return this.mPlacementData.getAdjustedPosition(originalPosition);
    }

    public int getOriginalCount(int count) {
        return this.mPlacementData.getOriginalCount(count);
    }

    public int getOriginalPosition(int position) {
        return this.mPlacementData.getOriginalPosition(position);
    }

    @VisibleForTesting
    void handleAdsAvailable() {
        if (this.mHasPlacedAds) {
            notifyNeedsPlacement();
        } else {
            if (this.mHasReceivedPositions) {
                placeInitialAds(this.mPendingPlacementData);
            }
            this.mHasReceivedAds = true;
        }
    }

    @VisibleForTesting
    void handlePositioningLoad(MoPubClientPositioning positioning) {
        PlacementData placementData = PlacementData.fromAdPositioning(positioning);
        if (this.mHasReceivedAds) {
            placeInitialAds(placementData);
        } else {
            this.mPendingPlacementData = placementData;
        }
        this.mHasReceivedPositions = true;
    }

    public void insertItem(int originalPosition) {
        this.mPlacementData.insertItem(originalPosition);
    }

    public boolean isAd(int position) {
        return this.mPlacementData.isPlacedAd(position);
    }

    public void loadAds(String adUnitId) {
        loadAds(adUnitId, null);
    }

    public void loadAds(String adUnitId, RequestParameters requestParameters) {
        this.mAdUnitId = adUnitId;
        this.mHasPlacedAds = false;
        this.mHasReceivedPositions = false;
        this.mHasReceivedAds = false;
        this.mPositioningSource.loadPositions(adUnitId, new PositioningListener() {
            public void onFailed() {
            }

            public void onLoad(MoPubClientPositioning positioning) {
                MoPubStreamAdPlacer.this.handlePositioningLoad(positioning);
            }
        });
        this.mAdSource.setAdSourceListener(new AdSourceListener() {
            public void onAdsAvailable() {
                MoPubStreamAdPlacer.this.handleAdsAvailable();
            }
        });
        this.mAdSource.loadAds(this.mContext, adUnitId, requestParameters);
    }

    public void moveItem(int originalPosition, int newPosition) {
        this.mPlacementData.moveItem(originalPosition, newPosition);
    }

    public void placeAdsInRange(int startPosition, int endPosition) {
        this.mVisibleRangeStart = startPosition;
        this.mVisibleRangeEnd = Math.min(endPosition, startPosition + 100);
        notifyNeedsPlacement();
    }

    public void registerAdRenderer(MoPubAdRenderer adRenderer) {
        this.mAdRenderer = adRenderer;
    }

    public int removeAdsInRange(int originalStartPosition, int originalEndPosition) {
        int[] positions = this.mPlacementData.getPlacedAdPositions();
        int adjustedStartRange = this.mPlacementData.getAdjustedPosition(originalStartPosition);
        int adjustedEndRange = this.mPlacementData.getAdjustedPosition(originalEndPosition);
        ArrayList<Integer> removedPositions = new ArrayList();
        int i = positions.length - 1;
        while (i >= 0) {
            int position = positions[i];
            if (position >= adjustedStartRange && position < adjustedEndRange) {
                removedPositions.add(Integer.valueOf(position));
                if (position < this.mVisibleRangeStart) {
                    this.mVisibleRangeStart--;
                }
                this.mItemCount--;
            }
            i--;
        }
        int clearedAdsCount = this.mPlacementData.clearAdsInRange(adjustedStartRange, adjustedEndRange);
        Iterator it = removedPositions.iterator();
        while (it.hasNext()) {
            this.mAdLoadedListener.onAdRemoved(((Integer) it.next()).intValue());
        }
        return clearedAdsCount;
    }

    public void removeItem(int originalPosition) {
        this.mPlacementData.removeItem(originalPosition);
    }

    public void setAdLoadedListener(MoPubNativeAdLoadedListener listener) {
        this.mAdLoadedListener = listener;
    }

    public void setItemCount(int originalCount) {
        this.mItemCount = this.mPlacementData.getAdjustedCount(originalCount);
        notifyNeedsPlacement();
    }
}