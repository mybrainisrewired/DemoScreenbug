package com.google.android.gms.plus;

import android.content.Context;
import android.net.Uri;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.a.d;
import com.google.android.gms.plus.Moments.LoadMomentsResult;
import com.google.android.gms.plus.People.LoadPeopleResult;
import com.google.android.gms.plus.internal.e;
import com.google.android.gms.plus.internal.i;
import com.google.android.gms.plus.model.moments.Moment;
import com.google.android.gms.plus.model.moments.MomentBuffer;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;
import java.util.Collection;

@Deprecated
public class PlusClient implements GooglePlayServicesClient {
    final e TL;

    @Deprecated
    public static class Builder {
        private final ConnectionCallbacks TQ;
        private final OnConnectionFailedListener TR;
        private final i TS;
        private final Context mContext;

        public Builder(Context context, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener connectionFailedListener) {
            this.mContext = context;
            this.TQ = connectionCallbacks;
            this.TR = connectionFailedListener;
            this.TS = new i(this.mContext);
        }

        public PlusClient build() {
            return new PlusClient(new e(this.mContext, this.TQ, this.TR, this.TS.iZ()));
        }

        public com.google.android.gms.plus.PlusClient.Builder clearScopes() {
            this.TS.iY();
            return this;
        }

        public com.google.android.gms.plus.PlusClient.Builder setAccountName(String accountName) {
            this.TS.bh(accountName);
            return this;
        }

        public com.google.android.gms.plus.PlusClient.Builder setActions(String... actions) {
            this.TS.f(actions);
            return this;
        }

        public com.google.android.gms.plus.PlusClient.Builder setScopes(String... scopes) {
            this.TS.e(scopes);
            return this;
        }
    }

    @Deprecated
    public static interface OnAccessRevokedListener {
        void onAccessRevoked(ConnectionResult connectionResult);
    }

    @Deprecated
    public static interface OnMomentsLoadedListener {
        @Deprecated
        void onMomentsLoaded(ConnectionResult connectionResult, MomentBuffer momentBuffer, String str, String str2);
    }

    @Deprecated
    public static interface OnPeopleLoadedListener {
        void onPeopleLoaded(ConnectionResult connectionResult, PersonBuffer personBuffer, String str);
    }

    @Deprecated
    public static interface OrderBy {
        @Deprecated
        public static final int ALPHABETICAL = 0;
        @Deprecated
        public static final int BEST = 1;
    }

    class AnonymousClass_1 implements d<LoadMomentsResult> {
        final /* synthetic */ com.google.android.gms.plus.PlusClient.OnMomentsLoadedListener TM;

        AnonymousClass_1(com.google.android.gms.plus.PlusClient.OnMomentsLoadedListener onMomentsLoadedListener) {
            this.TM = onMomentsLoadedListener;
        }

        public void a(LoadMomentsResult loadMomentsResult) {
            this.TM.onMomentsLoaded(loadMomentsResult.getStatus().eq(), loadMomentsResult.getMomentBuffer(), loadMomentsResult.getNextPageToken(), loadMomentsResult.getUpdated());
        }

        public /* synthetic */ void b(Object obj) {
            a((LoadMomentsResult) obj);
        }
    }

    class AnonymousClass_2 implements d<LoadMomentsResult> {
        final /* synthetic */ com.google.android.gms.plus.PlusClient.OnMomentsLoadedListener TM;

        AnonymousClass_2(com.google.android.gms.plus.PlusClient.OnMomentsLoadedListener onMomentsLoadedListener) {
            this.TM = onMomentsLoadedListener;
        }

        public void a(LoadMomentsResult loadMomentsResult) {
            this.TM.onMomentsLoaded(loadMomentsResult.getStatus().eq(), loadMomentsResult.getMomentBuffer(), loadMomentsResult.getNextPageToken(), loadMomentsResult.getUpdated());
        }

        public /* synthetic */ void b(Object obj) {
            a((LoadMomentsResult) obj);
        }
    }

    class AnonymousClass_3 implements d<LoadPeopleResult> {
        final /* synthetic */ com.google.android.gms.plus.PlusClient.OnPeopleLoadedListener TO;

        AnonymousClass_3(com.google.android.gms.plus.PlusClient.OnPeopleLoadedListener onPeopleLoadedListener) {
            this.TO = onPeopleLoadedListener;
        }

        public void a(LoadPeopleResult loadPeopleResult) {
            this.TO.onPeopleLoaded(loadPeopleResult.getStatus().eq(), loadPeopleResult.getPersonBuffer(), loadPeopleResult.getNextPageToken());
        }

        public /* synthetic */ void b(Object obj) {
            a((LoadPeopleResult) obj);
        }
    }

    class AnonymousClass_4 implements d<LoadPeopleResult> {
        final /* synthetic */ com.google.android.gms.plus.PlusClient.OnPeopleLoadedListener TO;

        AnonymousClass_4(com.google.android.gms.plus.PlusClient.OnPeopleLoadedListener onPeopleLoadedListener) {
            this.TO = onPeopleLoadedListener;
        }

        public void a(LoadPeopleResult loadPeopleResult) {
            this.TO.onPeopleLoaded(loadPeopleResult.getStatus().eq(), loadPeopleResult.getPersonBuffer(), loadPeopleResult.getNextPageToken());
        }

        public /* synthetic */ void b(Object obj) {
            a((LoadPeopleResult) obj);
        }
    }

    class AnonymousClass_5 implements d<LoadPeopleResult> {
        final /* synthetic */ com.google.android.gms.plus.PlusClient.OnPeopleLoadedListener TO;

        AnonymousClass_5(com.google.android.gms.plus.PlusClient.OnPeopleLoadedListener onPeopleLoadedListener) {
            this.TO = onPeopleLoadedListener;
        }

        public void a(LoadPeopleResult loadPeopleResult) {
            this.TO.onPeopleLoaded(loadPeopleResult.getStatus().eq(), loadPeopleResult.getPersonBuffer(), loadPeopleResult.getNextPageToken());
        }

        public /* synthetic */ void b(Object obj) {
            a((LoadPeopleResult) obj);
        }
    }

    class AnonymousClass_6 implements d<LoadPeopleResult> {
        final /* synthetic */ com.google.android.gms.plus.PlusClient.OnPeopleLoadedListener TO;

        AnonymousClass_6(com.google.android.gms.plus.PlusClient.OnPeopleLoadedListener onPeopleLoadedListener) {
            this.TO = onPeopleLoadedListener;
        }

        public void a(LoadPeopleResult loadPeopleResult) {
            this.TO.onPeopleLoaded(loadPeopleResult.getStatus().eq(), loadPeopleResult.getPersonBuffer(), loadPeopleResult.getNextPageToken());
        }

        public /* synthetic */ void b(Object obj) {
            a((LoadPeopleResult) obj);
        }
    }

    class AnonymousClass_7 implements d<Status> {
        final /* synthetic */ com.google.android.gms.plus.PlusClient.OnAccessRevokedListener TP;

        AnonymousClass_7(com.google.android.gms.plus.PlusClient.OnAccessRevokedListener onAccessRevokedListener) {
            this.TP = onAccessRevokedListener;
        }

        public void Y(Status status) {
            this.TP.onAccessRevoked(status.getStatus().eq());
        }

        public /* synthetic */ void b(Object obj) {
            Y((Status) obj);
        }
    }

    PlusClient(e plusClientImpl) {
        this.TL = plusClientImpl;
    }

    @Deprecated
    public void clearDefaultAccount() {
        this.TL.clearDefaultAccount();
    }

    @Deprecated
    public void connect() {
        this.TL.connect();
    }

    @Deprecated
    public void disconnect() {
        this.TL.disconnect();
    }

    @Deprecated
    public String getAccountName() {
        return this.TL.getAccountName();
    }

    @Deprecated
    public Person getCurrentPerson() {
        return this.TL.getCurrentPerson();
    }

    e iI() {
        return this.TL;
    }

    @Deprecated
    public boolean isConnected() {
        return this.TL.isConnected();
    }

    @Deprecated
    public boolean isConnecting() {
        return this.TL.isConnecting();
    }

    @Deprecated
    public boolean isConnectionCallbacksRegistered(ConnectionCallbacks listener) {
        return this.TL.isConnectionCallbacksRegistered(listener);
    }

    @Deprecated
    public boolean isConnectionFailedListenerRegistered(OnConnectionFailedListener listener) {
        return this.TL.isConnectionFailedListenerRegistered(listener);
    }

    @Deprecated
    public void loadMoments(OnMomentsLoadedListener listener) {
        this.TL.l(new AnonymousClass_1(listener));
    }

    @Deprecated
    public void loadMoments(OnMomentsLoadedListener listener, int maxResults, String pageToken, Uri targetUrl, String type, String userId) {
        this.TL.a(new AnonymousClass_2(listener), maxResults, pageToken, targetUrl, type, userId);
    }

    @Deprecated
    public void loadPeople(OnPeopleLoadedListener listener, Collection personIds) {
        this.TL.a(new AnonymousClass_5(listener), personIds);
    }

    @Deprecated
    public void loadPeople(OnPeopleLoadedListener listener, String... personIds) {
        this.TL.d(new AnonymousClass_6(listener), personIds);
    }

    @Deprecated
    public void loadVisiblePeople(OnPeopleLoadedListener listener, int orderBy, String pageToken) {
        this.TL.a(new AnonymousClass_3(listener), orderBy, pageToken);
    }

    @Deprecated
    public void loadVisiblePeople(OnPeopleLoadedListener listener, String pageToken) {
        this.TL.o(new AnonymousClass_4(listener), pageToken);
    }

    @Deprecated
    public void registerConnectionCallbacks(ConnectionCallbacks listener) {
        this.TL.registerConnectionCallbacks(listener);
    }

    @Deprecated
    public void registerConnectionFailedListener(OnConnectionFailedListener listener) {
        this.TL.registerConnectionFailedListener(listener);
    }

    @Deprecated
    public void removeMoment(String momentId) {
        this.TL.removeMoment(momentId);
    }

    @Deprecated
    public void revokeAccessAndDisconnect(OnAccessRevokedListener listener) {
        this.TL.n(new AnonymousClass_7(listener));
    }

    @Deprecated
    public void unregisterConnectionCallbacks(ConnectionCallbacks listener) {
        this.TL.unregisterConnectionCallbacks(listener);
    }

    @Deprecated
    public void unregisterConnectionFailedListener(OnConnectionFailedListener listener) {
        this.TL.unregisterConnectionFailedListener(listener);
    }

    @Deprecated
    public void writeMoment(Moment moment) {
        this.TL.a(null, moment);
    }
}