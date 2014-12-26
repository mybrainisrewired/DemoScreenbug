package com.mopub.nativeads;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.logging.MoPubLog;
import java.util.WeakHashMap;

public class MoPubNativeAdRenderer implements MoPubAdRenderer<NativeResponse> {
    private final ViewBinder mViewBinder;
    private final WeakHashMap<View, NativeViewHolder> mViewHolderMap;

    public MoPubNativeAdRenderer(ViewBinder viewBinder) {
        this.mViewBinder = viewBinder;
        this.mViewHolderMap = new WeakHashMap();
    }

    private void populateConvertViewSubViews(View view, NativeViewHolder nativeViewHolder, NativeResponse nativeResponse, ViewBinder viewBinder) {
        nativeViewHolder.update(nativeResponse);
        nativeViewHolder.updateExtras(view, nativeResponse, viewBinder);
    }

    public View createAdView(Context context, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(this.mViewBinder.layoutId, parent, false);
    }

    @VisibleForTesting
    NativeViewHolder getOrCreateNativeViewHolder(View view, ViewBinder viewBinder) {
        NativeViewHolder nativeViewHolder = (NativeViewHolder) this.mViewHolderMap.get(view);
        if (nativeViewHolder != null) {
            return nativeViewHolder;
        }
        nativeViewHolder = NativeViewHolder.fromViewBinder(view, viewBinder);
        this.mViewHolderMap.put(view, nativeViewHolder);
        return nativeViewHolder;
    }

    public void renderAdView(View view, NativeResponse nativeResponse) {
        NativeViewHolder nativeViewHolder = getOrCreateNativeViewHolder(view, this.mViewBinder);
        if (nativeViewHolder == null) {
            MoPubLog.d("Could not create NativeViewHolder.");
        } else {
            populateConvertViewSubViews(view, nativeViewHolder, nativeResponse, this.mViewBinder);
            view.setVisibility(0);
        }
    }
}