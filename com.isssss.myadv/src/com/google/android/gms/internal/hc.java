package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.ff.e;
import com.google.android.gms.location.LocationClient.OnAddGeofencesResultListener;
import com.google.android.gms.location.LocationClient.OnRemoveGeofencesResultListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationStatusCodes;
import com.millennialmedia.android.MMAdView;

public class hc extends ff<ha> {
    private final hf<ha> Ok;
    private final hb Oq;
    private final hr Or;
    private final String Os;
    private final Context mContext;
    private final String wG;

    private final class a extends b<OnAddGeofencesResultListener> {
        private final int Ah;
        private final String[] Ot;

        public a(OnAddGeofencesResultListener onAddGeofencesResultListener, int i, String[] strArr) {
            super(onAddGeofencesResultListener);
            this.Ah = LocationStatusCodes.bz(i);
            this.Ot = strArr;
        }

        protected void a(OnAddGeofencesResultListener onAddGeofencesResultListener) {
            if (onAddGeofencesResultListener != null) {
                onAddGeofencesResultListener.onAddGeofencesResult(this.Ah, this.Ot);
            }
        }

        protected void dx() {
        }
    }

    private final class c implements hf<ha> {
        private c() {
        }

        public void bT() {
            hc.this.bT();
        }

        public /* synthetic */ IInterface eM() {
            return hR();
        }

        public ha hR() {
            return (ha) hc.this.eM();
        }
    }

    private final class d extends b<OnRemoveGeofencesResultListener> {
        private final int Ah;
        private final String[] Ot;
        private final int Oy;
        private final PendingIntent mPendingIntent;

        public d(hc hcVar, int i, OnRemoveGeofencesResultListener onRemoveGeofencesResultListener, int i2, PendingIntent pendingIntent) {
            boolean z = true;
            hc.this = hcVar;
            super(onRemoveGeofencesResultListener);
            if (i != 1) {
                z = false;
            }
            fb.x(z);
            this.Oy = i;
            this.Ah = LocationStatusCodes.bz(i2);
            this.mPendingIntent = pendingIntent;
            this.Ot = null;
        }

        public d(int i, OnRemoveGeofencesResultListener onRemoveGeofencesResultListener, int i2, String[] strArr) {
            super(onRemoveGeofencesResultListener);
            fb.x(i == 2);
            this.Oy = i;
            this.Ah = LocationStatusCodes.bz(i2);
            this.Ot = strArr;
            this.mPendingIntent = null;
        }

        protected void a(OnRemoveGeofencesResultListener onRemoveGeofencesResultListener) {
            if (onRemoveGeofencesResultListener != null) {
                switch (this.Oy) {
                    case MMAdView.TRANSITION_FADE:
                        onRemoveGeofencesResultListener.onRemoveGeofencesByPendingIntentResult(this.Ah, this.mPendingIntent);
                    case MMAdView.TRANSITION_UP:
                        onRemoveGeofencesResultListener.onRemoveGeofencesByRequestIdsResult(this.Ah, this.Ot);
                    default:
                        Log.wtf("LocationClientImpl", "Unsupported action: " + this.Oy);
                }
            }
        }

        protected void dx() {
        }
    }

    private static final class b extends com.google.android.gms.internal.gz.a {
        private OnAddGeofencesResultListener Ov;
        private OnRemoveGeofencesResultListener Ow;
        private hc Ox;

        public b(OnAddGeofencesResultListener onAddGeofencesResultListener, hc hcVar) {
            this.Ov = onAddGeofencesResultListener;
            this.Ow = null;
            this.Ox = hcVar;
        }

        public b(OnRemoveGeofencesResultListener onRemoveGeofencesResultListener, hc hcVar) {
            this.Ow = onRemoveGeofencesResultListener;
            this.Ov = null;
            this.Ox = hcVar;
        }

        public void onAddGeofencesResult(int statusCode, String[] geofenceRequestIds) throws RemoteException {
            if (this.Ox == null) {
                Log.wtf("LocationClientImpl", "onAddGeofenceResult called multiple times");
            } else {
                hc hcVar = this.Ox;
                hc hcVar2 = this.Ox;
                hcVar2.getClass();
                hcVar.a(new a(this.Ov, statusCode, geofenceRequestIds));
                this.Ox = null;
                this.Ov = null;
                this.Ow = null;
            }
        }

        public void onRemoveGeofencesByPendingIntentResult(int statusCode, PendingIntent pendingIntent) {
            if (this.Ox == null) {
                Log.wtf("LocationClientImpl", "onRemoveGeofencesByPendingIntentResult called multiple times");
            } else {
                hc hcVar = this.Ox;
                hc hcVar2 = this.Ox;
                hcVar2.getClass();
                hcVar.a(new d(hcVar2, 1, this.Ow, statusCode, pendingIntent));
                this.Ox = null;
                this.Ov = null;
                this.Ow = null;
            }
        }

        public void onRemoveGeofencesByRequestIdsResult(int statusCode, String[] geofenceRequestIds) {
            if (this.Ox == null) {
                Log.wtf("LocationClientImpl", "onRemoveGeofencesByRequestIdsResult called multiple times");
            } else {
                hc hcVar = this.Ox;
                hc hcVar2 = this.Ox;
                hcVar2.getClass();
                hcVar.a(new d((hc)2, (int)this.Ow, (OnRemoveGeofencesResultListener)statusCode, (int)geofenceRequestIds));
                this.Ox = null;
                this.Ov = null;
                this.Ow = null;
            }
        }
    }

    public hc(Context context, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener, String str) {
        super(context, connectionCallbacks, onConnectionFailedListener, new String[0]);
        this.Ok = new c(null);
        this.mContext = context;
        this.Oq = new hb(context, this.Ok);
        this.Os = str;
        this.wG = null;
        this.Or = new hr(getContext(), context.getPackageName(), this.Ok);
    }

    protected ha X(IBinder iBinder) {
        return com.google.android.gms.internal.ha.a.W(iBinder);
    }

    protected void a(fm fmVar, e eVar) throws RemoteException {
        Bundle bundle = new Bundle();
        bundle.putString("client_name", this.Os);
        fmVar.e(eVar, GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE, getContext().getPackageName(), bundle);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addGeofences(java.util.List r4_geofences, android.app.PendingIntent r5_pendingIntent, com.google.android.gms.location.LocationClient.OnAddGeofencesResultListener r6_listener) {
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.hc.addGeofences(java.util.List, android.app.PendingIntent, com.google.android.gms.location.LocationClient$OnAddGeofencesResultListener):void");
        /*
        r3 = this;
        r3.bT();
        if (r4 == 0) goto L_0x0031;
    L_0x0005:
        r0 = r4.size();
        if (r0 <= 0) goto L_0x0031;
    L_0x000b:
        r0 = 1;
    L_0x000c:
        r1 = "At least one geofence must be specified.";
        com.google.android.gms.internal.fq.b(r0, r1);
        r0 = "PendingIntent must be specified.";
        com.google.android.gms.internal.fq.b(r5, r0);
        r0 = "OnAddGeofencesResultListener not provided.";
        com.google.android.gms.internal.fq.b(r6, r0);
        if (r6 != 0) goto L_0x0033;
    L_0x001d:
        r0 = 0;
        r1 = r0;
    L_0x001f:
        r0 = r3.eM();	 Catch:{ RemoteException -> 0x003a }
        r0 = (com.google.android.gms.internal.ha) r0;	 Catch:{ RemoteException -> 0x003a }
        r2 = r3.getContext();	 Catch:{ RemoteException -> 0x003a }
        r2 = r2.getPackageName();	 Catch:{ RemoteException -> 0x003a }
        r0.a(r4, r5, r1, r2);	 Catch:{ RemoteException -> 0x003a }
        return;
    L_0x0031:
        r0 = 0;
        goto L_0x000c;
    L_0x0033:
        r0 = new com.google.android.gms.internal.hc$b;	 Catch:{ RemoteException -> 0x003a }
        r0.<init>(r6, r3);	 Catch:{ RemoteException -> 0x003a }
        r1 = r0;
        goto L_0x001f;
    L_0x003a:
        r0 = move-exception;
        r1 = new java.lang.IllegalStateException;
        r1.<init>(r0);
        throw r1;
        */
    }

    protected String bg() {
        return "com.google.android.location.internal.GoogleLocationManagerService.START";
    }

    protected String bh() {
        return "com.google.android.gms.location.internal.IGoogleLocationManagerService";
    }

    public void disconnect() {
        synchronized (this.Oq) {
            if (isConnected()) {
                this.Oq.removeAllListeners();
                this.Oq.hQ();
            }
            super.disconnect();
        }
    }

    public Location getLastLocation() {
        return this.Oq.getLastLocation();
    }

    protected /* synthetic */ IInterface r(IBinder iBinder) {
        return X(iBinder);
    }

    public void removeActivityUpdates(PendingIntent callbackIntent) {
        bT();
        fq.f(callbackIntent);
        try {
            ((ha) eM()).removeActivityUpdates(callbackIntent);
        } catch (RemoteException e) {
            throw new IllegalStateException(e);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void removeGeofences(android.app.PendingIntent r4_pendingIntent, com.google.android.gms.location.LocationClient.OnRemoveGeofencesResultListener r5_listener) {
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.hc.removeGeofences(android.app.PendingIntent, com.google.android.gms.location.LocationClient$OnRemoveGeofencesResultListener):void");
        /*
        r3 = this;
        r3.bT();
        r0 = "PendingIntent must be specified.";
        com.google.android.gms.internal.fq.b(r4, r0);
        r0 = "OnRemoveGeofencesResultListener not provided.";
        com.google.android.gms.internal.fq.b(r5, r0);
        if (r5 != 0) goto L_0x0023;
    L_0x000f:
        r0 = 0;
        r1 = r0;
    L_0x0011:
        r0 = r3.eM();	 Catch:{ RemoteException -> 0x002a }
        r0 = (com.google.android.gms.internal.ha) r0;	 Catch:{ RemoteException -> 0x002a }
        r2 = r3.getContext();	 Catch:{ RemoteException -> 0x002a }
        r2 = r2.getPackageName();	 Catch:{ RemoteException -> 0x002a }
        r0.a(r4, r1, r2);	 Catch:{ RemoteException -> 0x002a }
        return;
    L_0x0023:
        r0 = new com.google.android.gms.internal.hc$b;	 Catch:{ RemoteException -> 0x002a }
        r0.<init>(r5, r3);	 Catch:{ RemoteException -> 0x002a }
        r1 = r0;
        goto L_0x0011;
    L_0x002a:
        r0 = move-exception;
        r1 = new java.lang.IllegalStateException;
        r1.<init>(r0);
        throw r1;
        */
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void removeGeofences(java.util.List<java.lang.String> r5_geofenceRequestIds, com.google.android.gms.location.LocationClient.OnRemoveGeofencesResultListener r6_listener) {
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.hc.removeGeofences(java.util.List, com.google.android.gms.location.LocationClient$OnRemoveGeofencesResultListener):void");
        /*
        r4 = this;
        r1 = 0;
        r4.bT();
        if (r5 == 0) goto L_0x0035;
    L_0x0006:
        r0 = r5.size();
        if (r0 <= 0) goto L_0x0035;
    L_0x000c:
        r0 = 1;
    L_0x000d:
        r2 = "geofenceRequestIds can't be null nor empty.";
        com.google.android.gms.internal.fq.b(r0, r2);
        r0 = "OnRemoveGeofencesResultListener not provided.";
        com.google.android.gms.internal.fq.b(r6, r0);
        r0 = new java.lang.String[r1];
        r0 = r5.toArray(r0);
        r0 = (java.lang.String[]) r0;
        if (r6 != 0) goto L_0x0037;
    L_0x0021:
        r1 = 0;
        r2 = r1;
    L_0x0023:
        r1 = r4.eM();	 Catch:{ RemoteException -> 0x003e }
        r1 = (com.google.android.gms.internal.ha) r1;	 Catch:{ RemoteException -> 0x003e }
        r3 = r4.getContext();	 Catch:{ RemoteException -> 0x003e }
        r3 = r3.getPackageName();	 Catch:{ RemoteException -> 0x003e }
        r1.a(r0, r2, r3);	 Catch:{ RemoteException -> 0x003e }
        return;
    L_0x0035:
        r0 = r1;
        goto L_0x000d;
    L_0x0037:
        r1 = new com.google.android.gms.internal.hc$b;	 Catch:{ RemoteException -> 0x003e }
        r1.<init>(r6, r4);	 Catch:{ RemoteException -> 0x003e }
        r2 = r1;
        goto L_0x0023;
    L_0x003e:
        r0 = move-exception;
        r1 = new java.lang.IllegalStateException;
        r1.<init>(r0);
        throw r1;
        */
    }

    public void removeLocationUpdates(PendingIntent callbackIntent) {
        this.Oq.removeLocationUpdates(callbackIntent);
    }

    public void removeLocationUpdates(LocationListener listener) {
        this.Oq.removeLocationUpdates(listener);
    }

    public void requestActivityUpdates(long detectionIntervalMillis, PendingIntent callbackIntent) {
        boolean z = true;
        bT();
        fq.f(callbackIntent);
        if (detectionIntervalMillis < 0) {
            z = false;
        }
        fq.b(z, (Object)"detectionIntervalMillis must be >= 0");
        try {
            ((ha) eM()).a(detectionIntervalMillis, true, callbackIntent);
        } catch (RemoteException e) {
            throw new IllegalStateException(e);
        }
    }

    public void requestLocationUpdates(LocationRequest request, PendingIntent callbackIntent) {
        this.Oq.requestLocationUpdates(request, callbackIntent);
    }

    public void requestLocationUpdates(LocationRequest request, LocationListener listener) {
        requestLocationUpdates(request, listener, null);
    }

    public void requestLocationUpdates(LocationRequest request, LocationListener listener, Looper looper) {
        synchronized (this.Oq) {
            this.Oq.requestLocationUpdates(request, listener, looper);
        }
    }

    public void setMockLocation(Location mockLocation) {
        this.Oq.setMockLocation(mockLocation);
    }

    public void setMockMode(boolean isMockMode) {
        this.Oq.setMockMode(isMockMode);
    }
}