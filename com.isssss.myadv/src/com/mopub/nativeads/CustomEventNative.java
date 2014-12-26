package com.mopub.nativeads;

import android.content.Context;
import java.util.Map;

public abstract class CustomEventNative {

    public static interface CustomEventNativeListener {
        void onNativeAdFailed(NativeErrorCode nativeErrorCode);

        void onNativeAdLoaded(NativeAdInterface nativeAdInterface);
    }

    public static interface ImageListener {
        void onImagesCached();

        void onImagesFailedToCache(NativeErrorCode nativeErrorCode);
    }

    protected abstract void loadNativeAd(Context context, CustomEventNativeListener customEventNativeListener, Map<String, Object> map, Map<String, String> map2);
}