package com.facebook.ads.internal;

import android.content.Context;
import com.facebook.ads.internal.AdHandler.ImpressionHelper;

public class NativeAdHandler extends AdHandler {
    public NativeAdHandler(ImpressionHelper impressionHelper, long sendImpressionDelay, NativeAdDataModel adDataModel, Context context) {
        super(impressionHelper, sendImpressionDelay, context);
        setAdDataModel(adDataModel);
    }

    protected synchronized void sendImpression() {
        ((NativeAdDataModel) this.adDataModel).logImpression();
    }
}