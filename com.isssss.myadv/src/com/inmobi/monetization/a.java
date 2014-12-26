package com.inmobi.monetization;

import com.google.android.gms.location.LocationRequest;
import com.inmobi.androidsdk.IMBrowserActivity;
import com.inmobi.monetization.IMInterstitial.State;
import com.inmobi.monetization.internal.AdErrorCode;
import com.inmobi.monetization.internal.IMAdListener;
import java.util.Map;

// compiled from: IMInterstitial.java
class a implements IMAdListener {
    final /* synthetic */ IMInterstitial a;

    a(IMInterstitial iMInterstitial) {
        this.a = iMInterstitial;
    }

    public void onAdInteraction(Map<String, String> map) {
        this.a.a(LocationRequest.PRIORITY_NO_POWER, null, map);
    }

    public void onAdRequestFailed(AdErrorCode adErrorCode) {
        this.a.h = State.INIT;
        this.a.a(IMBrowserActivity.INTERSTITIAL_ACTIVITY, adErrorCode, null);
    }

    public void onAdRequestSucceeded() {
        this.a.h = State.READY;
        this.a.a(LocationRequest.PRIORITY_HIGH_ACCURACY, null, null);
    }

    public void onDismissAdScreen() {
        this.a.h = State.INIT;
        this.a.a(103, null, null);
    }

    public void onIncentCompleted(Map<Object, Object> map) {
        this.a.a(106, null, map);
    }

    public void onLeaveApplication() {
        this.a.a(LocationRequest.PRIORITY_LOW_POWER, null, null);
    }

    public void onShowAdScreen() {
        this.a.h = State.ACTIVE;
        this.a.a(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY, null, null);
    }
}