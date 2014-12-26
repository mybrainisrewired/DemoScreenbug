package com.google.android.gms.drive.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.events.DriveEvent;
import com.google.android.gms.drive.events.DriveEvent.Listener;
import com.google.android.gms.drive.events.c;
import com.google.android.gms.drive.internal.u.a;
import com.google.android.gms.internal.fc;
import com.google.android.gms.internal.ff;
import com.google.android.gms.internal.ff.e;
import com.google.android.gms.internal.fm;
import com.google.android.gms.internal.fq;
import com.inmobi.commons.analytics.db.AnalyticsEvent;
import java.util.HashMap;
import java.util.Map;

public class n extends ff<u> {
    private DriveId Fh;
    private DriveId Fi;
    final ConnectionCallbacks Fj;
    Map<DriveId, Map<Listener<?>, s<?>>> Fk;
    private final String wG;

    class AnonymousClass_1 extends j {
        final /* synthetic */ DriveId Fl;
        final /* synthetic */ int Fm;
        final /* synthetic */ s Fn;

        AnonymousClass_1(DriveId driveId, int i, s sVar) {
            this.Fl = driveId;
            this.Fm = i;
            this.Fn = sVar;
        }

        protected void a(n nVar) throws RemoteException {
            nVar.fE().a(new AddEventListenerRequest(this.Fl, this.Fm, null), this.Fn, null, new al(this));
        }
    }

    class AnonymousClass_2 extends j {
        final /* synthetic */ DriveId Fl;
        final /* synthetic */ int Fm;
        final /* synthetic */ s Fn;

        AnonymousClass_2(DriveId driveId, int i, s sVar) {
            this.Fl = driveId;
            this.Fm = i;
            this.Fn = sVar;
        }

        protected void a(n nVar) throws RemoteException {
            nVar.fE().a(new RemoveEventListenerRequest(this.Fl, this.Fm), this.Fn, null, new al(this));
        }
    }

    public n(Context context, Looper looper, fc fcVar, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener, String[] strArr) {
        super(context, looper, connectionCallbacks, onConnectionFailedListener, strArr);
        this.Fk = new HashMap();
        this.wG = (String) fq.b(fcVar.eC(), (Object)"Must call Api.ClientBuilder.setAccountName()");
        this.Fj = connectionCallbacks;
    }

    protected u F(IBinder iBinder) {
        return a.G(iBinder);
    }

    <C extends DriveEvent> PendingResult<Status> a(GoogleApiClient googleApiClient, DriveId driveId, int i, Object obj) {
        PendingResult<Status> kVar;
        fq.b(c.a(i, driveId), AnalyticsEvent.EVENT_ID);
        fq.b(obj, (Object)"listener");
        fq.a(isConnected(), "Client must be connected");
        synchronized (this.Fk) {
            Map map = (Map) this.Fk.get(driveId);
            if (map == null) {
                map = new HashMap();
                this.Fk.put(driveId, map);
            }
            if (map.containsKey(obj)) {
                kVar = new k(googleApiClient, Status.Bv);
            } else {
                s sVar = new s(getLooper(), i, obj);
                map.put(obj, sVar);
                kVar = googleApiClient.b(new AnonymousClass_1(driveId, i, sVar));
            }
        }
        return kVar;
    }

    protected void a(int i, IBinder iBinder, Bundle bundle) {
        if (bundle != null) {
            bundle.setClassLoader(getClass().getClassLoader());
            this.Fh = (DriveId) bundle.getParcelable("com.google.android.gms.drive.root_id");
            this.Fi = (DriveId) bundle.getParcelable("com.google.android.gms.drive.appdata_id");
        }
        super.a(i, iBinder, bundle);
    }

    protected void a(fm fmVar, e eVar) throws RemoteException {
        String packageName = getContext().getPackageName();
        fq.f(eVar);
        fq.f(packageName);
        fq.f(eL());
        fmVar.a(eVar, GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE, packageName, eL(), this.wG, new Bundle());
    }

    PendingResult<Status> b(GoogleApiClient googleApiClient, DriveId driveId, int i, Object obj) {
        PendingResult<Status> kVar;
        fq.b(c.a(i, driveId), AnalyticsEvent.EVENT_ID);
        fq.b(obj, (Object)"listener");
        fq.a(isConnected(), "Client must be connected");
        synchronized (this.Fk) {
            Map map = (Map) this.Fk.get(driveId);
            if (map == null) {
                kVar = new k(googleApiClient, Status.Bv);
            } else {
                s sVar = (s) map.remove(obj);
                if (sVar == null) {
                    kVar = new k(googleApiClient, Status.Bv);
                } else {
                    if (map.isEmpty()) {
                        this.Fk.remove(driveId);
                    }
                    kVar = googleApiClient.b(new AnonymousClass_2(driveId, i, sVar));
                }
            }
        }
        return kVar;
    }

    protected String bg() {
        return "com.google.android.gms.drive.ApiService.START";
    }

    protected String bh() {
        return "com.google.android.gms.drive.internal.IDriveService";
    }

    public void disconnect() {
        u uVar = (u) eM();
        if (uVar != null) {
            try {
                uVar.a(new DisconnectRequest());
            } catch (RemoteException e) {
            }
        }
        super.disconnect();
        this.Fk.clear();
    }

    public u fE() {
        return (u) eM();
    }

    public DriveId fF() {
        return this.Fh;
    }

    public DriveId fG() {
        return this.Fi;
    }

    protected /* synthetic */ IInterface r(IBinder iBinder) {
        return F(iBinder);
    }
}