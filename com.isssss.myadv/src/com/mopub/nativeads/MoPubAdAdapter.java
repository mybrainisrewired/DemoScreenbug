package com.mopub.nativeads;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.logging.MoPubLog;
import com.mopub.nativeads.MoPubNativeAdPositioning.MoPubClientPositioning;
import com.mopub.nativeads.MoPubNativeAdPositioning.MoPubServerPositioning;
import java.util.Iterator;
import java.util.List;
import java.util.WeakHashMap;

public class MoPubAdAdapter extends BaseAdapter {
    private MoPubNativeAdLoadedListener mAdLoadedListener;
    private final Adapter mOriginalAdapter;
    private final MoPubStreamAdPlacer mStreamAdPlacer;
    private final WeakHashMap<View, Integer> mViewPositionMap;
    private final VisibilityTracker mVisibilityTracker;

    class AnonymousClass_4 implements OnItemClickListener {
        private final /* synthetic */ OnItemClickListener val$listener;

        AnonymousClass_4(OnItemClickListener onItemClickListener) {
            this.val$listener = onItemClickListener;
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            if (!MoPubAdAdapter.this.mStreamAdPlacer.isAd(position)) {
                this.val$listener.onItemClick(adapterView, view, MoPubAdAdapter.this.mStreamAdPlacer.getOriginalPosition(position), id);
            }
        }
    }

    class AnonymousClass_5 implements OnItemLongClickListener {
        private final /* synthetic */ OnItemLongClickListener val$listener;

        AnonymousClass_5(OnItemLongClickListener onItemLongClickListener) {
            this.val$listener = onItemLongClickListener;
        }

        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
            if (!MoPubAdAdapter.this.isAd(position)) {
                if (!this.val$listener.onItemLongClick(adapterView, view, MoPubAdAdapter.this.mStreamAdPlacer.getOriginalPosition(position), id)) {
                    return false;
                }
            }
            return true;
        }
    }

    class AnonymousClass_6 implements OnItemSelectedListener {
        private final /* synthetic */ OnItemSelectedListener val$listener;

        AnonymousClass_6(OnItemSelectedListener onItemSelectedListener) {
            this.val$listener = onItemSelectedListener;
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            if (!MoPubAdAdapter.this.isAd(position)) {
                this.val$listener.onItemSelected(adapterView, view, MoPubAdAdapter.this.mStreamAdPlacer.getOriginalPosition(position), id);
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
            this.val$listener.onNothingSelected(adapterView);
        }
    }

    public MoPubAdAdapter(Context context, Adapter originalAdapter) {
        this(context, originalAdapter, MoPubNativeAdPositioning.serverPositioning());
    }

    public MoPubAdAdapter(Context context, Adapter originalAdapter, MoPubClientPositioning adPositioning) {
        this(new MoPubStreamAdPlacer(context, adPositioning), originalAdapter, new VisibilityTracker(context));
    }

    public MoPubAdAdapter(Context context, Adapter originalAdapter, MoPubServerPositioning adPositioning) {
        this(new MoPubStreamAdPlacer(context, adPositioning), originalAdapter, new VisibilityTracker(context));
    }

    @VisibleForTesting
    MoPubAdAdapter(MoPubStreamAdPlacer streamAdPlacer, Adapter originalAdapter, VisibilityTracker visibilityTracker) {
        this.mOriginalAdapter = originalAdapter;
        this.mStreamAdPlacer = streamAdPlacer;
        this.mViewPositionMap = new WeakHashMap();
        this.mVisibilityTracker = visibilityTracker;
        this.mVisibilityTracker.setVisibilityTrackerListener(new VisibilityTrackerListener() {
            public void onVisibilityChanged(List<View> visibleViews, List<View> invisibleViews) {
                MoPubAdAdapter.this.handleVisibilityChange(visibleViews);
            }
        });
        this.mOriginalAdapter.registerDataSetObserver(new DataSetObserver() {
            public void onChanged() {
                MoPubAdAdapter.this.mStreamAdPlacer.setItemCount(MoPubAdAdapter.this.mOriginalAdapter.getCount());
                MoPubAdAdapter.this.notifyDataSetChanged();
            }

            public void onInvalidated() {
                MoPubAdAdapter.this.notifyDataSetInvalidated();
            }
        });
        this.mStreamAdPlacer.setAdLoadedListener(new MoPubNativeAdLoadedListener() {
            public void onAdLoaded(int position) {
                MoPubAdAdapter.this.handleAdLoaded(position);
            }

            public void onAdRemoved(int position) {
                MoPubAdAdapter.this.handleAdRemoved(position);
            }
        });
        this.mStreamAdPlacer.setItemCount(this.mOriginalAdapter.getCount());
    }

    private void handleVisibilityChange(List<View> visibleViews) {
        int min = MoPubClientPositioning.NO_REPEAT;
        int max = 0;
        Iterator it = visibleViews.iterator();
        while (it.hasNext()) {
            Integer pos = (Integer) this.mViewPositionMap.get((View) it.next());
            if (pos != null) {
                min = Math.min(pos.intValue(), min);
                max = Math.max(pos.intValue(), max);
            }
        }
        this.mStreamAdPlacer.placeAdsInRange(min, max + 1);
    }

    public boolean areAllItemsEnabled() {
        return this.mOriginalAdapter instanceof ListAdapter && ((ListAdapter) this.mOriginalAdapter).areAllItemsEnabled();
    }

    public void clearAds() {
        this.mStreamAdPlacer.clearAds();
    }

    public void destroy() {
        this.mStreamAdPlacer.destroy();
        this.mVisibilityTracker.destroy();
    }

    public int getAdjustedPosition(int originalPosition) {
        return this.mStreamAdPlacer.getAdjustedPosition(originalPosition);
    }

    public int getCount() {
        return this.mStreamAdPlacer.getAdjustedCount(this.mOriginalAdapter.getCount());
    }

    public Object getItem(int position) {
        Object ad = this.mStreamAdPlacer.getAdData(position);
        return ad != null ? ad : this.mOriginalAdapter.getItem(this.mStreamAdPlacer.getOriginalPosition(position));
    }

    public long getItemId(int position) {
        Object adData = this.mStreamAdPlacer.getAdData(position);
        return adData != null ? (long) (System.identityHashCode(adData) ^ -1 + 1) : this.mOriginalAdapter.getItemId(this.mStreamAdPlacer.getOriginalPosition(position));
    }

    public int getItemViewType(int position) {
        int adType = this.mStreamAdPlacer.getAdViewType(position);
        return adType != 0 ? this.mOriginalAdapter.getViewTypeCount() + adType - 1 : this.mOriginalAdapter.getItemViewType(this.mStreamAdPlacer.getOriginalPosition(position));
    }

    public int getOriginalPosition(int position) {
        return this.mStreamAdPlacer.getOriginalPosition(position);
    }

    public View getView(int position, View view, ViewGroup viewGroup) {
        View resultView;
        View adView = this.mStreamAdPlacer.getAdView(position, view, viewGroup);
        if (adView != null) {
            resultView = adView;
        } else {
            resultView = this.mOriginalAdapter.getView(this.mStreamAdPlacer.getOriginalPosition(position), view, viewGroup);
        }
        this.mViewPositionMap.put(resultView, Integer.valueOf(position));
        this.mVisibilityTracker.addView(resultView, 0);
        return resultView;
    }

    public int getViewTypeCount() {
        return this.mOriginalAdapter.getViewTypeCount() + this.mStreamAdPlacer.getAdViewTypeCount();
    }

    @VisibleForTesting
    void handleAdLoaded(int position) {
        if (this.mAdLoadedListener != null) {
            this.mAdLoadedListener.onAdLoaded(position);
        }
        notifyDataSetChanged();
    }

    @VisibleForTesting
    void handleAdRemoved(int position) {
        if (this.mAdLoadedListener != null) {
            this.mAdLoadedListener.onAdRemoved(position);
        }
        notifyDataSetChanged();
    }

    public boolean hasStableIds() {
        return this.mOriginalAdapter.hasStableIds();
    }

    public void insertItem(int originalPosition) {
        this.mStreamAdPlacer.insertItem(originalPosition);
    }

    public boolean isAd(int position) {
        return this.mStreamAdPlacer.isAd(position);
    }

    public boolean isEmpty() {
        return this.mOriginalAdapter.isEmpty() && this.mStreamAdPlacer.getAdjustedCount(0) == 0;
    }

    public boolean isEnabled(int position) {
        return isAd(position) || (this.mOriginalAdapter instanceof ListAdapter && ((ListAdapter) this.mOriginalAdapter).isEnabled(this.mStreamAdPlacer.getOriginalPosition(position)));
    }

    public void loadAds(String adUnitId) {
        this.mStreamAdPlacer.loadAds(adUnitId);
    }

    public void loadAds(String adUnitId, RequestParameters requestParameters) {
        this.mStreamAdPlacer.loadAds(adUnitId, requestParameters);
    }

    public void refreshAds(ListView listView, String adUnitId) {
        refreshAds(listView, adUnitId, null);
    }

    public void refreshAds(ListView listView, String adUnitId, RequestParameters requestParameters) {
        if (listView.getAdapter() != this) {
            MoPubLog.w("You called refreshAds on a ListView whose adapter is not an ad placer");
        } else {
            View firstView = listView.getChildAt(0);
            int offsetY = firstView == null ? 0 : firstView.getTop();
            int firstPosition = listView.getFirstVisiblePosition();
            int startRange = Math.max(firstPosition - 1, 0);
            while (this.mStreamAdPlacer.isAd(startRange) && startRange > 0) {
                startRange--;
            }
            int lastPosition = listView.getLastVisiblePosition();
            while (this.mStreamAdPlacer.isAd(lastPosition) && lastPosition < getCount() - 1) {
                lastPosition++;
            }
            int originalStartRange = this.mStreamAdPlacer.getOriginalPosition(startRange);
            this.mStreamAdPlacer.removeAdsInRange(this.mStreamAdPlacer.getOriginalCount(lastPosition + 1), this.mStreamAdPlacer.getOriginalCount(getCount()));
            int numAdsRemoved = this.mStreamAdPlacer.removeAdsInRange(0, originalStartRange);
            if (numAdsRemoved > 0) {
                listView.setSelectionFromTop(firstPosition - numAdsRemoved, offsetY);
            }
            loadAds(adUnitId, requestParameters);
        }
    }

    public final void registerAdRenderer(MoPubAdRenderer adRenderer) {
        if (adRenderer == null) {
            MoPubLog.w("Tried to set a null ad renderer on the placer.");
        } else {
            this.mStreamAdPlacer.registerAdRenderer(adRenderer);
        }
    }

    public void removeItem(int originalPosition) {
        this.mStreamAdPlacer.removeItem(originalPosition);
    }

    public final void setAdLoadedListener(MoPubNativeAdLoadedListener listener) {
        this.mAdLoadedListener = listener;
    }

    public void setOnClickListener(ListView listView, OnItemClickListener listener) {
        listView.setOnItemClickListener(new AnonymousClass_4(listener));
    }

    public void setOnItemLongClickListener(ListView listView, OnItemLongClickListener listener) {
        listView.setOnItemLongClickListener(new AnonymousClass_5(listener));
    }

    public void setOnItemSelectedListener(ListView listView, OnItemSelectedListener listener) {
        listView.setOnItemSelectedListener(new AnonymousClass_6(listener));
    }

    public void setSelection(ListView listView, int originalPosition) {
        listView.setSelection(this.mStreamAdPlacer.getAdjustedPosition(originalPosition));
    }

    public void smoothScrollToPosition(ListView listView, int originalPosition) {
        listView.smoothScrollToPosition(this.mStreamAdPlacer.getAdjustedPosition(originalPosition));
    }
}