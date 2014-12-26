package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.a.d;
import com.google.android.gms.internal.ff.e;
import com.google.android.gms.panorama.PanoramaApi.PanoramaResult;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;

public class hx extends ff<hv> {

    final class b extends com.google.android.gms.internal.hu.a {
        private final d<com.google.android.gms.panorama.PanoramaApi.a> TF;
        private final d<PanoramaResult> TG;
        private final Uri TH;

        public b(d<com.google.android.gms.panorama.PanoramaApi.a> dVar, d<PanoramaResult> dVar2, Uri uri) {
            this.TF = dVar;
            this.TG = dVar2;
            this.TH = uri;
        }

        public void a(int i, Bundle bundle, int i2, Intent intent) {
            if (this.TH != null) {
                hx.this.getContext().revokeUriPermission(this.TH, 1);
            }
            Status status = new Status(i, null, bundle != null ? (PendingIntent) bundle.getParcelable("pendingIntent") : null);
            if (this.TG != null) {
                hx.this.a(new c(this.TG, status, intent));
            } else if (this.TF != null) {
                hx.this.a(new a(this.TF, status, i2, intent));
            }
        }
    }

    final class c extends b<d<PanoramaResult>> implements PanoramaResult {
        private final Status TC;
        private final Intent TD;

        public c(d<PanoramaResult> dVar, Status status, Intent intent) {
            super(dVar);
            this.TC = status;
            this.TD = intent;
        }

        protected /* synthetic */ void a(Object obj) {
            c((d) obj);
        }

        protected void c(d<PanoramaResult> dVar) {
            dVar.b(this);
        }

        protected void dx() {
        }

        public Status getStatus() {
            return this.TC;
        }

        public Intent getViewerIntent() {
            return this.TD;
        }
    }

    final class a extends b<d<com.google.android.gms.panorama.PanoramaApi.a>> implements com.google.android.gms.panorama.PanoramaApi.a {
        public final Status TC;
        public final Intent TD;
        public final int type;

        public a(d<com.google.android.gms.panorama.PanoramaApi.a> dVar, Status status, int i, Intent intent) {
            super(dVar);
            this.TC = status;
            this.type = i;
            this.TD = intent;
        }

        protected /* synthetic */ void a(Object obj) {
            c((d) obj);
        }

        protected void c(d<com.google.android.gms.panorama.PanoramaApi.a> dVar) {
            dVar.b(this);
        }

        protected void dx() {
        }

        public Status getStatus() {
            return this.TC;
        }

        public Intent getViewerIntent() {
            return this.TD;
        }
    }

    public hx(Context context, Looper looper, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, connectionCallbacks, onConnectionFailedListener, (String[]) 0);
    }

    public void a(d<PanoramaResult> dVar, Uri uri, boolean z) {
        a(new b(null, dVar, z ? uri : null), uri, null, z);
    }

    protected void a(fm fmVar, e eVar) throws RemoteException {
        fmVar.a(eVar, GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE, getContext().getPackageName(), new Bundle());
    }

    public void a(b bVar, Uri uri, Bundle bundle, boolean z) {
        bT();
        if (z) {
            getContext().grantUriPermission(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE, uri, 1);
        }
        try {
            ((hv) eM()).a(bVar, uri, bundle, z);
        } catch (RemoteException e) {
            bVar.a(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, null, 0, null);
        }
    }

    public hv aN(IBinder iBinder) {
        return com.google.android.gms.internal.hv.a.aM(iBinder);
    }

    protected String bg() {
        return "com.google.android.gms.panorama.service.START";
    }

    protected String bh() {
        return "com.google.android.gms.panorama.internal.IPanoramaService";
    }

    public /* synthetic */ IInterface r(IBinder iBinder) {
        return aN(iBinder);
    }
}