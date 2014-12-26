package com.inmobi.commons.analytics.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.inmobi.monetization.internal.InvalidManifestErrorMessages;

public class AnalyticsConnectivityReceiver extends BroadcastReceiver {
    private a a;
    private boolean b;

    static interface a {
        void a();

        void b();
    }

    public AnalyticsConnectivityReceiver(Context context, a aVar) {
        this.a = aVar;
        bind(context);
    }

    public final void bind(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(InvalidManifestErrorMessages.CONNECTIVITY_INTENT_ACTION);
        context.registerReceiver(this, intentFilter);
    }

    public boolean isConnected() {
        return this.b;
    }

    public void onReceive(Context context, Intent intent) {
        if (intent.getBooleanExtra("noConnectivity", false)) {
            this.b = false;
            if (this.a != null) {
                this.a.a();
            }
        } else if (!intent.getBooleanExtra("noConnectivity", false)) {
            this.b = true;
            if (this.a != null) {
                this.a.b();
            }
        }
    }

    public void unbind(Context context) {
        context.unregisterReceiver(this);
    }
}