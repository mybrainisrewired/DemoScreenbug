package com.google.android.gms.appstate;

import android.content.Context;
import android.os.Looper;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.ApiOptions.NoOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Releasable;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.ei;
import com.google.android.gms.internal.fc;
import com.google.android.gms.internal.fq;
import com.mopub.nativeads.MoPubNativeAdPositioning.MoPubClientPositioning;

public final class AppStateManager {
    public static final Api<NoOptions> API;
    public static final Scope SCOPE_APP_STATE;
    static final com.google.android.gms.common.api.Api.c<ei> wx;
    private static final com.google.android.gms.common.api.Api.b<ei, NoOptions> wy;

    public static interface StateConflictResult extends Releasable, Result {
        byte[] getLocalData();

        String getResolvedVersion();

        byte[] getServerData();

        int getStateKey();
    }

    public static interface StateDeletedResult extends Result {
        int getStateKey();
    }

    public static interface StateListResult extends Result {
        AppStateBuffer getStateBuffer();
    }

    public static interface StateLoadedResult extends Releasable, Result {
        byte[] getLocalData();

        int getStateKey();
    }

    public static interface StateResult extends Releasable, Result {
        com.google.android.gms.appstate.AppStateManager.StateConflictResult getConflictResult();

        com.google.android.gms.appstate.AppStateManager.StateLoadedResult getLoadedResult();
    }

    static class AnonymousClass_2 implements com.google.android.gms.appstate.AppStateManager.StateResult {
        final /* synthetic */ Status wz;

        AnonymousClass_2(Status status) {
            this.wz = status;
        }

        public com.google.android.gms.appstate.AppStateManager.StateConflictResult getConflictResult() {
            return null;
        }

        public com.google.android.gms.appstate.AppStateManager.StateLoadedResult getLoadedResult() {
            return null;
        }

        public Status getStatus() {
            return this.wz;
        }

        public void release() {
        }
    }

    public static abstract class a<R extends Result> extends com.google.android.gms.common.api.a.b<R, ei> {
        public a() {
            super(wx);
        }
    }

    private static abstract class b extends com.google.android.gms.appstate.AppStateManager.a<com.google.android.gms.appstate.AppStateManager.StateDeletedResult> {
        private b() {
        }
    }

    private static abstract class c extends com.google.android.gms.appstate.AppStateManager.a<com.google.android.gms.appstate.AppStateManager.StateListResult> {

        class AnonymousClass_1 implements com.google.android.gms.appstate.AppStateManager.StateListResult {
            final /* synthetic */ Status wz;

            AnonymousClass_1(Status status) {
                this.wz = status;
            }

            public AppStateBuffer getStateBuffer() {
                return new AppStateBuffer(null);
            }

            public Status getStatus() {
                return this.wz;
            }
        }

        private c() {
        }

        public /* synthetic */ Result d(Status status) {
            return e(status);
        }

        public com.google.android.gms.appstate.AppStateManager.StateListResult e(Status status) {
            return new AnonymousClass_1(status);
        }
    }

    private static abstract class d extends com.google.android.gms.appstate.AppStateManager.a<Status> {
        private d() {
        }

        public /* synthetic */ Result d(Status status) {
            return f(status);
        }

        public Status f(Status status) {
            return status;
        }
    }

    private static abstract class e extends com.google.android.gms.appstate.AppStateManager.a<com.google.android.gms.appstate.AppStateManager.StateResult> {
        private e() {
        }

        public /* synthetic */ Result d(Status status) {
            return g(status);
        }

        public com.google.android.gms.appstate.AppStateManager.StateResult g(Status status) {
            return AppStateManager.a(status);
        }
    }

    static class AnonymousClass_3 extends e {
        final /* synthetic */ int wA;
        final /* synthetic */ byte[] wB;

        AnonymousClass_3(int i, byte[] bArr) {
            this.wA = i;
            this.wB = bArr;
            super();
        }

        protected void a(ei eiVar) {
            eiVar.a(null, this.wA, this.wB);
        }
    }

    static class AnonymousClass_4 extends e {
        final /* synthetic */ int wA;
        final /* synthetic */ byte[] wB;

        AnonymousClass_4(int i, byte[] bArr) {
            this.wA = i;
            this.wB = bArr;
            super();
        }

        protected void a(ei eiVar) {
            eiVar.a(this, this.wA, this.wB);
        }
    }

    static class AnonymousClass_5 extends b {
        final /* synthetic */ int wA;

        class AnonymousClass_1 implements com.google.android.gms.appstate.AppStateManager.StateDeletedResult {
            final /* synthetic */ Status wz;

            AnonymousClass_1(Status status) {
                this.wz = status;
            }

            public int getStateKey() {
                return AnonymousClass_5.this.wA;
            }

            public Status getStatus() {
                return this.wz;
            }
        }

        AnonymousClass_5(int i) {
            this.wA = i;
            super();
        }

        protected void a(ei eiVar) {
            eiVar.a(this, this.wA);
        }

        public com.google.android.gms.appstate.AppStateManager.StateDeletedResult c(Status status) {
            return new AnonymousClass_1(status);
        }

        public /* synthetic */ Result d(Status status) {
            return c(status);
        }
    }

    static class AnonymousClass_6 extends e {
        final /* synthetic */ int wA;

        AnonymousClass_6(int i) {
            this.wA = i;
            super();
        }

        protected void a(ei eiVar) {
            eiVar.b(this, this.wA);
        }
    }

    static class AnonymousClass_8 extends e {
        final /* synthetic */ int wA;
        final /* synthetic */ String wD;
        final /* synthetic */ byte[] wE;

        AnonymousClass_8(int i, String str, byte[] bArr) {
            this.wA = i;
            this.wD = str;
            this.wE = bArr;
            super();
        }

        protected void a(ei eiVar) {
            eiVar.a(this, this.wA, this.wD, this.wE);
        }
    }

    static {
        wx = new com.google.android.gms.common.api.Api.c();
        wy = new com.google.android.gms.common.api.Api.b<ei, NoOptions>() {
            public ei a(Context context, Looper looper, fc fcVar, NoOptions noOptions, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
                return new ei(context, looper, connectionCallbacks, onConnectionFailedListener, fcVar.eC(), (String[]) fcVar.eE().toArray(new String[0]));
            }

            public int getPriority() {
                return MoPubClientPositioning.NO_REPEAT;
            }
        };
        SCOPE_APP_STATE = new Scope(Scopes.APP_STATE);
        API = new Api(wy, wx, new Scope[]{SCOPE_APP_STATE});
    }

    private AppStateManager() {
    }

    private static StateResult a(Status status) {
        return new AnonymousClass_2(status);
    }

    public static ei a(GoogleApiClient googleApiClient) {
        boolean z = true;
        fq.b(googleApiClient != null, (Object)"GoogleApiClient parameter is required.");
        fq.a(googleApiClient.isConnected(), "GoogleApiClient must be connected.");
        ei eiVar = (ei) googleApiClient.a(wx);
        if (eiVar == null) {
            z = false;
        }
        fq.a(z, "GoogleApiClient is not configured to use the AppState API. Pass AppStateManager.API into GoogleApiClient.Builder#addApi() to use this feature.");
        return eiVar;
    }

    public static PendingResult<StateDeletedResult> delete(GoogleApiClient googleApiClient, int stateKey) {
        return googleApiClient.b(new AnonymousClass_5(stateKey));
    }

    public static int getMaxNumKeys(GoogleApiClient googleApiClient) {
        return a(googleApiClient).dw();
    }

    public static int getMaxStateSize(GoogleApiClient googleApiClient) {
        return a(googleApiClient).dv();
    }

    public static PendingResult<StateListResult> list(GoogleApiClient googleApiClient) {
        return googleApiClient.a(new c() {
            {
                super();
            }

            protected void a(ei eiVar) {
                eiVar.a(this);
            }
        });
    }

    public static PendingResult<StateResult> load(GoogleApiClient googleApiClient, int stateKey) {
        return googleApiClient.a(new AnonymousClass_6(stateKey));
    }

    public static PendingResult<StateResult> resolve(GoogleApiClient googleApiClient, int stateKey, String resolvedVersion, byte[] resolvedData) {
        return googleApiClient.b(new AnonymousClass_8(stateKey, resolvedVersion, resolvedData));
    }

    public static PendingResult<Status> signOut(GoogleApiClient googleApiClient) {
        return googleApiClient.b(new d() {
            {
                super();
            }

            protected void a(ei eiVar) {
                eiVar.b(this);
            }
        });
    }

    public static void update(GoogleApiClient googleApiClient, int stateKey, byte[] data) {
        googleApiClient.b(new AnonymousClass_3(stateKey, data));
    }

    public static PendingResult<StateResult> updateImmediate(GoogleApiClient googleApiClient, int stateKey, byte[] data) {
        return googleApiClient.b(new AnonymousClass_4(stateKey, data));
    }
}