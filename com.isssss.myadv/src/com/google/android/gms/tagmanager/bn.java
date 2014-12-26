package com.google.android.gms.tagmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import com.inmobi.monetization.internal.InvalidManifestErrorMessages;

class bn extends BroadcastReceiver {
    static final String sD;
    private final cw YE;

    static {
        sD = bn.class.getName();
    }

    bn(cw cwVar) {
        this.YE = cwVar;
    }

    public static void p(Context context) {
        Intent intent = new Intent("com.google.analytics.RADIO_POWERED");
        intent.addCategory(context.getPackageName());
        intent.putExtra(sD, true);
        context.sendBroadcast(intent);
    }

    public void o(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(InvalidManifestErrorMessages.CONNECTIVITY_INTENT_ACTION);
        context.registerReceiver(this, intentFilter);
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.google.analytics.RADIO_POWERED");
        intentFilter.addCategory(context.getPackageName());
        context.registerReceiver(this, intentFilter);
    }

    public void onReceive(Context ctx, Intent intent) {
        String action = intent.getAction();
        if (InvalidManifestErrorMessages.CONNECTIVITY_INTENT_ACTION.equals(action)) {
            Bundle extras = intent.getExtras();
            Boolean bool = Boolean.FALSE;
            if (extras != null) {
                bool = Boolean.valueOf(intent.getExtras().getBoolean("noConnectivity"));
            }
            this.YE.s(!bool.booleanValue());
        } else if ("com.google.analytics.RADIO_POWERED".equals(action) && !intent.hasExtra(sD)) {
            this.YE.cm();
        }
    }
}