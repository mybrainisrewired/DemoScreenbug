package com.google.android.gms.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.appstate.AppStateBuffer;
import com.google.android.gms.appstate.AppStateManager.StateConflictResult;
import com.google.android.gms.appstate.AppStateManager.StateDeletedResult;
import com.google.android.gms.appstate.AppStateManager.StateListResult;
import com.google.android.gms.appstate.AppStateManager.StateLoadedResult;
import com.google.android.gms.appstate.AppStateManager.StateResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataHolder;
import com.millennialmedia.android.MMAdView;

public final class ei extends ff<ek> {
    private final String wG;

    final class h extends b<com.google.android.gms.common.api.a.d<Status>> {
        private final Status wJ;

        public h(com.google.android.gms.common.api.a.d<Status> dVar, Status status) {
            super(dVar);
            this.wJ = status;
        }

        public /* synthetic */ void a(Object obj) {
            c((com.google.android.gms.common.api.a.d) obj);
        }

        public void c(com.google.android.gms.common.api.a.d<Status> dVar) {
            dVar.b(this.wJ);
        }

        protected void dx() {
        }
    }

    final class b extends b<com.google.android.gms.common.api.a.d<StateDeletedResult>> implements StateDeletedResult {
        private final Status wJ;
        private final int wK;

        public b(com.google.android.gms.common.api.a.d<StateDeletedResult> dVar, Status status, int i) {
            super(dVar);
            this.wJ = status;
            this.wK = i;
        }

        public /* synthetic */ void a(Object obj) {
            c((com.google.android.gms.common.api.a.d) obj);
        }

        public void c(com.google.android.gms.common.api.a.d<StateDeletedResult> dVar) {
            dVar.b(this);
        }

        protected void dx() {
        }

        public int getStateKey() {
            return this.wK;
        }

        public Status getStatus() {
            return this.wJ;
        }
    }

    final class d extends com.google.android.gms.internal.ff.d<com.google.android.gms.common.api.a.d<StateListResult>> implements StateListResult {
        private final Status wJ;
        private final AppStateBuffer wL;

        public d(com.google.android.gms.common.api.a.d<StateListResult> dVar, Status status, DataHolder dataHolder) {
            super(dVar, dataHolder);
            this.wJ = status;
            this.wL = new AppStateBuffer(dataHolder);
        }

        public void a(com.google.android.gms.common.api.a.d<StateListResult> dVar, DataHolder dataHolder) {
            dVar.b(this);
        }

        public AppStateBuffer getStateBuffer() {
            return this.wL;
        }

        public Status getStatus() {
            return this.wJ;
        }
    }

    final class f extends com.google.android.gms.internal.ff.d<com.google.android.gms.common.api.a.d<StateResult>> implements StateConflictResult, StateLoadedResult, StateResult {
        private final Status wJ;
        private final int wK;
        private final AppStateBuffer wL;

        public f(com.google.android.gms.common.api.a.d<StateResult> dVar, int i, DataHolder dataHolder) {
            super(dVar, dataHolder);
            this.wK = i;
            this.wJ = new Status(dataHolder.getStatusCode());
            this.wL = new AppStateBuffer(dataHolder);
        }

        private boolean dy() {
            return this.wJ.getStatusCode() == 2000;
        }

        public void a(com.google.android.gms.common.api.a.d<StateResult> dVar, DataHolder dataHolder) {
            dVar.b(this);
        }

        public StateConflictResult getConflictResult() {
            return dy() ? this : null;
        }

        public StateLoadedResult getLoadedResult() {
            return dy() ? null : this;
        }

        public byte[] getLocalData() {
            return this.wL.getCount() == 0 ? null : this.wL.get(0).getLocalData();
        }

        public String getResolvedVersion() {
            return this.wL.getCount() == 0 ? null : this.wL.get(0).getConflictVersion();
        }

        public byte[] getServerData() {
            return this.wL.getCount() == 0 ? null : this.wL.get(0).getConflictData();
        }

        public int getStateKey() {
            return this.wK;
        }

        public Status getStatus() {
            return this.wJ;
        }

        public void release() {
            this.wL.close();
        }
    }

    final class a extends eh {
        private final com.google.android.gms.common.api.a.d<StateDeletedResult> wH;

        public a(Object obj) {
            this.wH = (com.google.android.gms.common.api.a.d) fq.b(obj, (Object)"Result holder must not be null");
        }

        public void b(int i, int i2) {
            ei.this.a(new b(this.wH, new Status(i), i2));
        }
    }

    final class c extends eh {
        private final com.google.android.gms.common.api.a.d<StateListResult> wH;

        public c(Object obj) {
            this.wH = (com.google.android.gms.common.api.a.d) fq.b(obj, (Object)"Result holder must not be null");
        }

        public void a(DataHolder dataHolder) {
            ei.this.a(new d(this.wH, new Status(dataHolder.getStatusCode()), dataHolder));
        }
    }

    final class e extends eh {
        private final com.google.android.gms.common.api.a.d<StateResult> wH;

        public e(Object obj) {
            this.wH = (com.google.android.gms.common.api.a.d) fq.b(obj, (Object)"Result holder must not be null");
        }

        public void a(int i, DataHolder dataHolder) {
            ei.this.a(new f(this.wH, i, dataHolder));
        }
    }

    final class g extends eh {
        com.google.android.gms.common.api.a.d<Status> wH;

        public g(Object obj) {
            this.wH = (com.google.android.gms.common.api.a.d) fq.b(obj, (Object)"Holder must not be null");
        }

        public void du() {
            ei.this.a(new h(this.wH, new Status(0)));
        }
    }

    public ei(Context context, Looper looper, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener, String str, String[] strArr) {
        super(context, looper, connectionCallbacks, onConnectionFailedListener, strArr);
        this.wG = (String) fq.f(str);
    }

    public void a(com.google.android.gms.common.api.a.d<StateListResult> dVar) {
        try {
            ((ek) eM()).a(new c(dVar));
        } catch (RemoteException e) {
            Log.w("AppStateClient", "service died");
        }
    }

    public void a(com.google.android.gms.common.api.a.d<StateDeletedResult> dVar, int i) {
        try {
            ((ek) eM()).b(new a(dVar), i);
        } catch (RemoteException e) {
            Log.w("AppStateClient", "service died");
        }
    }

    public void a(com.google.android.gms.common.api.a.d<StateResult> dVar, int i, String str, byte[] bArr) {
        try {
            ((ek) eM()).a(new e(dVar), i, str, bArr);
        } catch (RemoteException e) {
            Log.w("AppStateClient", "service died");
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void a(com.google.android.gms.common.api.a.d<com.google.android.gms.appstate.AppStateManager.StateResult> r3, int r4, byte[] r5) {
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.ei.a(com.google.android.gms.common.api.a$d, int, byte[]):void");
        /*
        r2 = this;
        if (r3 != 0) goto L_0x000e;
    L_0x0002:
        r0 = 0;
        r1 = r0;
    L_0x0004:
        r0 = r2.eM();	 Catch:{ RemoteException -> 0x0015 }
        r0 = (com.google.android.gms.internal.ek) r0;	 Catch:{ RemoteException -> 0x0015 }
        r0.a(r1, r4, r5);	 Catch:{ RemoteException -> 0x0015 }
    L_0x000d:
        return;
    L_0x000e:
        r0 = new com.google.android.gms.internal.ei$e;	 Catch:{ RemoteException -> 0x0015 }
        r0.<init>(r3);	 Catch:{ RemoteException -> 0x0015 }
        r1 = r0;
        goto L_0x0004;
    L_0x0015:
        r0 = move-exception;
        r0 = "AppStateClient";
        r1 = "service died";
        android.util.Log.w(r0, r1);
        goto L_0x000d;
        */
    }

    protected void a(fm fmVar, com.google.android.gms.internal.ff.e eVar) throws RemoteException {
        fmVar.a((fl)eVar, (int)GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE, getContext().getPackageName(), this.wG, eL());
    }

    public void b(com.google.android.gms.common.api.a.d<Status> dVar) {
        try {
            ((ek) eM()).b(new g(dVar));
        } catch (RemoteException e) {
            Log.w("AppStateClient", "service died");
        }
    }

    public void b(com.google.android.gms.common.api.a.d<StateResult> dVar, int i) {
        try {
            ((ek) eM()).a(new e(dVar), i);
        } catch (RemoteException e) {
            Log.w("AppStateClient", "service died");
        }
    }

    protected void b(String... strArr) {
        int i = 0;
        boolean z = false;
        while (i < strArr.length) {
            if (strArr[i].equals(Scopes.APP_STATE)) {
                z = true;
            }
            i++;
        }
        fq.a(z, String.format("App State APIs requires %s to function.", new Object[]{Scopes.APP_STATE}));
    }

    protected String bg() {
        return "com.google.android.gms.appstate.service.START";
    }

    protected String bh() {
        return "com.google.android.gms.appstate.internal.IAppStateService";
    }

    public int dv() {
        int i = MMAdView.TRANSITION_UP;
        try {
            return ((ek) eM()).dv();
        } catch (RemoteException e) {
            Log.w("AppStateClient", "service died");
            return i;
        }
    }

    public int dw() {
        int i = MMAdView.TRANSITION_UP;
        try {
            return ((ek) eM()).dw();
        } catch (RemoteException e) {
            Log.w("AppStateClient", "service died");
            return i;
        }
    }

    protected /* synthetic */ IInterface r(IBinder iBinder) {
        return u(iBinder);
    }

    protected ek u(IBinder iBinder) {
        return com.google.android.gms.internal.ek.a.w(iBinder);
    }
}