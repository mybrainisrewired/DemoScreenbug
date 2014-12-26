package com.inmobi.monetization;

import com.google.android.gms.location.LocationRequest;
import com.inmobi.androidsdk.IMBrowserActivity;
import com.inmobi.monetization.internal.AdErrorCode;
import com.inmobi.monetization.internal.IMAdListener;
import java.util.Map;

// compiled from: IMBanner.java
class c implements IMAdListener {
    final /* synthetic */ IMBanner a;

    c(IMBanner iMBanner) {
        this.a = iMBanner;
    }

    public void onAdInteraction(Map<String, String> map) {
        this.a.a(LocationRequest.PRIORITY_NO_POWER, null, map);
    }

    public void onAdRequestFailed(AdErrorCode adErrorCode) {
        this.a.a(IMBrowserActivity.INTERSTITIAL_ACTIVITY, adErrorCode, null);
    }

    public void onAdRequestSucceeded() {
        this.a.a(LocationRequest.PRIORITY_HIGH_ACCURACY, null, null);
    }

    public void onDismissAdScreen() {
        this.a.a(103, null, null);
    }

    public void onIncentCompleted(Map<Object, Object> map) {
    }

    public void onLeaveApplication() {
        this.a.a(LocationRequest.PRIORITY_LOW_POWER, null, null);
    }

    public void onShowAdScreen() {
        this.a.a(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY, null, null);
    }
}