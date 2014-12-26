package com.google.android.gms.analytics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.inmobi.commons.analytics.iat.impl.AdTrackerConstants;
import com.inmobi.commons.internal.InternalSDKUtil;

public final class CampaignTrackingReceiver extends BroadcastReceiver {
    public void onReceive(Context ctx, Intent intent) {
        String stringExtra = intent.getStringExtra(AdTrackerConstants.REFERRER);
        if (InternalSDKUtil.ACTION_RECEIVER_REFERRER.equals(intent.getAction()) && stringExtra != null) {
            Intent intent2 = new Intent(ctx, CampaignTrackingService.class);
            intent2.putExtra(AdTrackerConstants.REFERRER, stringExtra);
            ctx.startService(intent2);
        }
    }
}