package com.google.android.gms.plus.internal;

import android.app.PendingIntent;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.internal.ff;
import com.google.android.gms.internal.fk;
import com.google.android.gms.internal.fm;
import com.google.android.gms.internal.gg;
import com.google.android.gms.internal.ie;
import com.google.android.gms.internal.ih;
import com.google.android.gms.plus.Moments.LoadMomentsResult;
import com.google.android.gms.plus.People.LoadPeopleResult;
import com.google.android.gms.plus.model.moments.Moment;
import com.google.android.gms.plus.model.moments.MomentBuffer;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class e extends ff<d> implements GooglePlayServicesClient {
    private Person Ub;
    private final h Uc;

    final class d extends b<com.google.android.gms.common.api.a.d<Status>> {
        private final Status wJ;

        public d(com.google.android.gms.common.api.a.d<Status> dVar, Status status) {
            super(dVar);
            this.wJ = status;
        }

        protected /* synthetic */ void a(Object obj) {
            c((com.google.android.gms.common.api.a.d) obj);
        }

        protected void c(com.google.android.gms.common.api.a.d<Status> dVar) {
            if (dVar != null) {
                dVar.b(this.wJ);
            }
        }

        protected void dx() {
        }
    }

    final class h extends b<com.google.android.gms.common.api.a.d<Status>> {
        private final Status wJ;

        public h(com.google.android.gms.common.api.a.d<Status> dVar, Status status) {
            super(dVar);
            this.wJ = status;
        }

        protected /* synthetic */ void a(Object obj) {
            c((com.google.android.gms.common.api.a.d) obj);
        }

        protected void c(com.google.android.gms.common.api.a.d<Status> dVar) {
            e.this.disconnect();
            if (dVar != null) {
                dVar.b(this.wJ);
            }
        }

        protected void dx() {
        }
    }

    final class c extends com.google.android.gms.internal.ff.d<com.google.android.gms.common.api.a.d<LoadMomentsResult>> implements LoadMomentsResult {
        private final String EM;
        private final String Ue;
        private MomentBuffer Uf;
        private final Status wJ;

        public c(com.google.android.gms.common.api.a.d<LoadMomentsResult> dVar, Status status, DataHolder dataHolder, String str, String str2) {
            super(dVar, dataHolder);
            this.wJ = status;
            this.EM = str;
            this.Ue = str2;
        }

        protected void a(com.google.android.gms.common.api.a.d<LoadMomentsResult> dVar, DataHolder dataHolder) {
            this.Uf = dataHolder != null ? new MomentBuffer(dataHolder) : null;
            dVar.b(this);
        }

        public MomentBuffer getMomentBuffer() {
            return this.Uf;
        }

        public String getNextPageToken() {
            return this.EM;
        }

        public Status getStatus() {
            return this.wJ;
        }

        public String getUpdated() {
            return this.Ue;
        }

        public void release() {
            if (this.Uf != null) {
                this.Uf.close();
            }
        }
    }

    final class f extends com.google.android.gms.internal.ff.d<com.google.android.gms.common.api.a.d<LoadPeopleResult>> implements LoadPeopleResult {
        private final String EM;
        private PersonBuffer Ug;
        private final Status wJ;

        public f(com.google.android.gms.common.api.a.d<LoadPeopleResult> dVar, Status status, DataHolder dataHolder, String str) {
            super(dVar, dataHolder);
            this.wJ = status;
            this.EM = str;
        }

        protected void a(com.google.android.gms.common.api.a.d<LoadPeopleResult> dVar, DataHolder dataHolder) {
            this.Ug = dataHolder != null ? new PersonBuffer(dataHolder) : null;
            dVar.b(this);
        }

        public String getNextPageToken() {
            return this.EM;
        }

        public PersonBuffer getPersonBuffer() {
            return this.Ug;
        }

        public Status getStatus() {
            return this.wJ;
        }

        public void release() {
            if (this.Ug != null) {
                this.Ug.close();
            }
        }
    }

    final class a extends a {
        private final com.google.android.gms.common.api.a.d<Status> TG;

        public a(com.google.android.gms.common.api.a.d<Status> dVar) {
            this.TG = dVar;
        }

        public void Z(Status status) {
            e.this.a(new d(this.TG, status));
        }
    }

    final class b extends a {
        private final com.google.android.gms.common.api.a.d<LoadMomentsResult> TG;

        public b(com.google.android.gms.common.api.a.d<LoadMomentsResult> dVar) {
            this.TG = dVar;
        }

        public void a(DataHolder dataHolder, String str, String str2) {
            DataHolder dataHolder2;
            Status status = new Status(dataHolder.getStatusCode(), null, dataHolder.getMetadata() != null ? (PendingIntent) dataHolder.getMetadata().getParcelable("pendingIntent") : null);
            if (status.isSuccess() || dataHolder == null) {
                dataHolder2 = dataHolder;
            } else {
                if (!dataHolder.isClosed()) {
                    dataHolder.close();
                }
                dataHolder2 = null;
            }
            e.this.a(new c(this.TG, status, dataHolder2, str, str2));
        }
    }

    final class e extends a {
        private final com.google.android.gms.common.api.a.d<LoadPeopleResult> TG;

        public e(com.google.android.gms.common.api.a.d<LoadPeopleResult> dVar) {
            this.TG = dVar;
        }

        public void a(DataHolder dataHolder, String str) {
            DataHolder dataHolder2;
            Status status = new Status(dataHolder.getStatusCode(), null, dataHolder.getMetadata() != null ? (PendingIntent) dataHolder.getMetadata().getParcelable("pendingIntent") : null);
            if (status.isSuccess() || dataHolder == null) {
                dataHolder2 = dataHolder;
            } else {
                if (!dataHolder.isClosed()) {
                    dataHolder.close();
                }
                dataHolder2 = null;
            }
            e.this.a(new f(this.TG, status, dataHolder2, str));
        }
    }

    final class g extends a {
        private final com.google.android.gms.common.api.a.d<Status> TG;

        public g(com.google.android.gms.common.api.a.d<Status> dVar) {
            this.TG = dVar;
        }

        public void e(int i, Bundle bundle) {
            e.this.a(new h(this.TG, new Status(i, null, bundle != null ? (PendingIntent) bundle.getParcelable("pendingIntent") : null)));
        }
    }

    public e(Context context, Looper looper, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener, h hVar) {
        super(context, looper, connectionCallbacks, onConnectionFailedListener, hVar.iP());
        this.Uc = hVar;
    }

    @Deprecated
    public e(Context context, GooglePlayServicesClient.ConnectionCallbacks connectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener onConnectionFailedListener, h hVar) {
        this(context, context.getMainLooper(), new com.google.android.gms.internal.ff.c(connectionCallbacks), new com.google.android.gms.internal.ff.g(onConnectionFailedListener), hVar);
    }

    public fk a(com.google.android.gms.common.api.a.d<LoadPeopleResult> dVar, int i, String str) {
        String str2 = null;
        bT();
        Object eVar = new e(dVar);
        try {
            return ((d) eM()).a(eVar, 1, i, -1, str);
        } catch (RemoteException e) {
            eVar.a(DataHolder.empty(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES), str2);
            return str2;
        }
    }

    protected void a(int i, IBinder iBinder, Bundle bundle) {
        if (i == 0 && bundle != null && bundle.containsKey("loaded_person")) {
            this.Ub = ih.i(bundle.getByteArray("loaded_person"));
        }
        super.a(i, iBinder, bundle);
    }

    public void a(com.google.android.gms.common.api.a.d<LoadMomentsResult> dVar, int i, String str, Uri uri, String str2, String str3) {
        bT();
        Object bVar = dVar != null ? new b(dVar) : null;
        try {
            ((d) eM()).a(bVar, i, str, uri, str2, str3);
        } catch (RemoteException e) {
            bVar.a(DataHolder.empty(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES), null, null);
        }
    }

    public void a(com.google.android.gms.common.api.a.d<Status> dVar, Moment moment) {
        bT();
        if (dVar != null) {
            a aVar = new a(dVar);
        } else {
            Object obj = null;
        }
        try {
            ((d) eM()).a(bVar, gg.a((ie) moment));
        } catch (RemoteException e) {
            th = e;
            if (bVar == null) {
                Throwable th2;
                throw new IllegalStateException(th2);
            }
            bVar.Z(new Status(8, null, null));
        }
    }

    public void a(com.google.android.gms.common.api.a.d<LoadPeopleResult> dVar, Collection<String> collection) {
        bT();
        b eVar = new e(dVar);
        try {
            ((d) eM()).a(eVar, new ArrayList(collection));
        } catch (RemoteException e) {
            eVar.a(DataHolder.empty(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES), null);
        }
    }

    protected void a(fm fmVar, com.google.android.gms.internal.ff.e eVar) throws RemoteException {
        Bundle iX = this.Uc.iX();
        iX.putStringArray(GoogleAuthUtil.KEY_REQUEST_VISIBLE_ACTIVITIES, this.Uc.iQ());
        fmVar.a(eVar, GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE, this.Uc.iT(), this.Uc.iS(), eL(), this.Uc.getAccountName(), iX);
    }

    protected d aR(IBinder iBinder) {
        return com.google.android.gms.plus.internal.d.a.aQ(iBinder);
    }

    protected String bg() {
        return "com.google.android.gms.plus.service.START";
    }

    public boolean bg(String str) {
        return Arrays.asList(eL()).contains(str);
    }

    protected String bh() {
        return "com.google.android.gms.plus.internal.IPlusService";
    }

    public void clearDefaultAccount() {
        bT();
        try {
            this.Ub = null;
            ((d) eM()).clearDefaultAccount();
        } catch (RemoteException e) {
            throw new IllegalStateException(e);
        }
    }

    public void d(com.google.android.gms.common.api.a.d dVar, String[] strArr) {
        a(dVar, Arrays.asList(strArr));
    }

    public String getAccountName() {
        bT();
        try {
            return ((d) eM()).getAccountName();
        } catch (RemoteException e) {
            throw new IllegalStateException(e);
        }
    }

    public Person getCurrentPerson() {
        bT();
        return this.Ub;
    }

    public void l(com.google.android.gms.common.api.a.d<LoadMomentsResult> dVar) {
        a(dVar, ApiEventType.API_MRAID_GET_MAX_SIZE, null, null, null, "me");
    }

    public void m(com.google.android.gms.common.api.a.d<LoadPeopleResult> dVar) {
        String str = null;
        bT();
        Object eVar = new e(dVar);
        try {
            ((d) eM()).a(eVar, MMAdView.TRANSITION_UP, 1, -1, null);
        } catch (RemoteException e) {
            eVar.a(DataHolder.empty(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES), str);
        }
    }

    public void n(com.google.android.gms.common.api.a.d<Status> dVar) {
        bT();
        clearDefaultAccount();
        Object gVar = new g(dVar);
        try {
            ((d) eM()).b(gVar);
        } catch (RemoteException e) {
            gVar.e(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, null);
        }
    }

    public fk o(com.google.android.gms.common.api.a.d dVar, String str) {
        return a(dVar, 0, str);
    }

    protected /* synthetic */ IInterface r(IBinder iBinder) {
        return aR(iBinder);
    }

    public void removeMoment(String momentId) {
        bT();
        try {
            ((d) eM()).removeMoment(momentId);
        } catch (RemoteException e) {
            throw new IllegalStateException(e);
        }
    }
}