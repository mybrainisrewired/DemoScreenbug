package com.mopub.nativeads;

import android.os.Handler;
import com.mopub.nativeads.MoPubNativeAdPositioning.MoPubClientPositioning;
import com.mopub.nativeads.PositioningSource.PositioningListener;

class ClientPositioningSource implements PositioningSource {
    private Handler mHandler;
    private final MoPubClientPositioning mPositioning;

    class AnonymousClass_1 implements Runnable {
        private final /* synthetic */ PositioningListener val$listener;

        AnonymousClass_1(PositioningListener positioningListener) {
            this.val$listener = positioningListener;
        }

        public void run() {
            this.val$listener.onLoad(ClientPositioningSource.this.mPositioning);
        }
    }

    ClientPositioningSource(MoPubClientPositioning positioning) {
        this.mHandler = new Handler();
        this.mPositioning = MoPubNativeAdPositioning.clone(positioning);
    }

    public void loadPositions(String adUnitId, PositioningListener listener) {
        this.mHandler.post(new AnonymousClass_1(listener));
    }
}