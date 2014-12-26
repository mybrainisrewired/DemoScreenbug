package com.mopub.nativeads;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public interface MoPubAdRenderer<T> {
    View createAdView(Context context, ViewGroup viewGroup);

    void renderAdView(View view, T t);
}