package com.google.android.gms.analytics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.inmobi.monetization.internal.InvalidManifestErrorMessages;

class q extends BroadcastReceiver {
    static final String sD;
    private final af sE;

    static {
        sD = q.class.getName();
    }

    q(af afVar) {
        this.sE = afVar;
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
        boolean z = false;
        String action = intent.getAction();
        if (InvalidManifestErrorMessages.CONNECTIVITY_INTENT_ACTION.equals(action)) {
            boolean booleanExtra = intent.getBooleanExtra("noConnectivity", false);
            af afVar = this.sE;
            if (!booleanExtra) {
                z = true;
            }
            afVar.s(z);
        } else if ("com.google.analytics.RADIO_POWERED".equals(action) && !intent.hasExtra(sD)) {
            this.sE.cm();
        }
    }
}