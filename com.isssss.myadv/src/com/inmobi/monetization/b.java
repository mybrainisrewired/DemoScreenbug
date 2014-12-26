package com.inmobi.monetization;

import com.inmobi.commons.internal.Log;
import com.inmobi.monetization.internal.AdErrorCode;
import com.inmobi.monetization.internal.Constants;
import com.inmobi.monetization.internal.IMAdListener;
import com.inmobi.monetization.internal.objects.NativeAdsCache;
import java.util.Map;

// compiled from: IMNative.java
class b implements IMAdListener {
    final /* synthetic */ IMNative a;

    // compiled from: IMNative.java
    class a implements Runnable {
        final /* synthetic */ AdErrorCode a;

        a(AdErrorCode adErrorCode) {
            this.a = adErrorCode;
        }

        public void run() {
            if (b.this.a.d != null) {
                b.this.a.d.onNativeRequestFailed(IMErrorCode.a(this.a));
            }
        }
    }

    // compiled from: IMNative.java
    class b implements Runnable {
        b() {
        }

        public void run() {
            try {
                if (b.this.d != null) {
                    b.this.d.onNativeRequestSucceeded(b.this);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.debug(Constants.LOG_TAG, "Failed to give callback");
            }
        }
    }

    b(IMNative iMNative) {
        this.a = iMNative;
    }

    public void onAdInteraction(Map<String, String> map) {
    }

    public void onAdRequestFailed(AdErrorCode adErrorCode) {
        try {
            this.a.e.getHandler().post(new a(adErrorCode));
        } catch (Exception e) {
            Log.debug(Constants.LOG_TAG, "Failed to give callback");
        }
    }

    public void onAdRequestSucceeded() {
        try {
            IMNative cachedAd = NativeAdsCache.getInstance().getCachedAd(this.a.f);
            this.a.a = cachedAd.a;
            this.a.b = cachedAd.b;
            this.a.c = cachedAd.c;
            this.a.e.getHandler().post(new b());
        } catch (Exception e) {
            Log.debug(Constants.LOG_TAG, "Failed to give callback");
        }
    }

    public void onDismissAdScreen() {
    }

    public void onIncentCompleted(Map<Object, Object> map) {
    }

    public void onLeaveApplication() {
    }

    public void onShowAdScreen() {
    }
}