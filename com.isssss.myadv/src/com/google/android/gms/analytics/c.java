package com.google.android.gms.analytics;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.ef;
import com.google.android.gms.internal.eg;
import com.millennialmedia.android.MMAdView;
import java.util.List;
import java.util.Map;

class c implements b {
    private Context mContext;
    private ServiceConnection sj;
    private b sk;
    private c sl;
    private eg sm;

    final class a implements ServiceConnection {
        a() {
        }

        public void onServiceConnected(ComponentName component, IBinder binder) {
            aa.y("service connected, binder: " + binder);
            try {
                if ("com.google.android.gms.analytics.internal.IAnalyticsService".equals(binder.getInterfaceDescriptor())) {
                    aa.y("bound to service");
                    c.this.sm = com.google.android.gms.internal.eg.a.t(binder);
                    c.this.bU();
                    return;
                }
            } catch (RemoteException e) {
            }
            c.this.mContext.unbindService(this);
            c.this.sj = null;
            c.this.sl.a(MMAdView.TRANSITION_UP, null);
        }

        public void onServiceDisconnected(ComponentName component) {
            aa.y("service disconnected: " + component);
            c.this.sj = null;
            c.this.sk.onDisconnected();
        }
    }

    public static interface b {
        void onConnected();

        void onDisconnected();
    }

    public static interface c {
        void a(int i, Intent intent);
    }

    public c(Context context, b bVar, c cVar) {
        this.mContext = context;
        if (bVar == null) {
            throw new IllegalArgumentException("onConnectedListener cannot be null");
        }
        this.sk = bVar;
        if (cVar == null) {
            throw new IllegalArgumentException("onConnectionFailedListener cannot be null");
        }
        this.sl = cVar;
    }

    private eg bS() {
        bT();
        return this.sm;
    }

    private void bU() {
        bV();
    }

    private void bV() {
        this.sk.onConnected();
    }

    public void a(Map<String, String> map, long j, String str, List<ef> list) {
        try {
            bS().a(map, j, str, list);
        } catch (RemoteException e) {
            aa.w("sendHit failed: " + e);
        }
    }

    public void bR() {
        try {
            bS().bR();
        } catch (RemoteException e) {
            aa.w("clear hits failed: " + e);
        }
    }

    protected void bT() {
        if (!isConnected()) {
            throw new IllegalStateException("Not connected. Call connect() and wait for onConnected() to be called.");
        }
    }

    public void connect() {
        Intent intent = new Intent("com.google.android.gms.analytics.service.START");
        intent.setComponent(new ComponentName(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE, "com.google.android.gms.analytics.service.AnalyticsService"));
        intent.putExtra("app_package_name", this.mContext.getPackageName());
        if (this.sj != null) {
            aa.w("Calling connect() while still connected, missing disconnect().");
        } else {
            this.sj = new a();
            boolean bindService = this.mContext.bindService(intent, this.sj, 129);
            aa.y("connect: bindService returned " + bindService + " for " + intent);
            if (!bindService) {
                this.sj = null;
                this.sl.a(1, null);
            }
        }
    }

    public void disconnect() {
        this.sm = null;
        if (this.sj != null) {
            try {
                this.mContext.unbindService(this.sj);
            } catch (IllegalStateException e) {
            } catch (IllegalArgumentException e2) {
            }
            this.sj = null;
            this.sk.onDisconnected();
        }
    }

    public boolean isConnected() {
        return this.sm != null;
    }
}