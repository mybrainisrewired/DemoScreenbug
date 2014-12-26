package com.google.android.gms.plus;

import android.content.Context;
import android.os.Looper;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.ApiOptions.Optional;
import com.google.android.gms.common.api.Api.b;
import com.google.android.gms.common.api.Api.c;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.internal.fc;
import com.google.android.gms.internal.fq;
import com.google.android.gms.internal.hy;
import com.google.android.gms.internal.hz;
import com.google.android.gms.internal.ia;
import com.google.android.gms.internal.ib;
import com.google.android.gms.plus.internal.PlusCommonExtras;
import com.google.android.gms.plus.internal.e;
import com.google.android.gms.plus.internal.h;
import com.millennialmedia.android.MMAdView;
import java.util.HashSet;
import java.util.Set;

public final class Plus {
    public static final Api<PlusOptions> API;
    public static final Account AccountApi;
    public static final Moments MomentsApi;
    public static final People PeopleApi;
    public static final Scope SCOPE_PLUS_LOGIN;
    public static final Scope SCOPE_PLUS_PROFILE;
    public static final a TI;
    public static final c<e> wx;
    static final b<e, PlusOptions> wy;

    public static final class PlusOptions implements Optional {
        final String TJ;
        final Set<String> TK;

        public static final class Builder {
            String TJ;
            final Set<String> TK;

            public Builder() {
                this.TK = new HashSet();
            }

            public com.google.android.gms.plus.Plus.PlusOptions.Builder addActivityTypes(Object activityTypes) {
                fq.b(activityTypes, (Object)"activityTypes may not be null.");
                int i = 0;
                while (i < activityTypes.length) {
                    this.TK.add(activityTypes[i]);
                    i++;
                }
                return this;
            }

            public com.google.android.gms.plus.Plus.PlusOptions build() {
                return new com.google.android.gms.plus.Plus.PlusOptions(null);
            }

            public com.google.android.gms.plus.Plus.PlusOptions.Builder setServerClientId(String clientId) {
                this.TJ = clientId;
                return this;
            }
        }

        private PlusOptions() {
            this.TJ = null;
            this.TK = new HashSet();
        }

        private PlusOptions(Builder builder) {
            this.TJ = builder.TJ;
            this.TK = builder.TK;
        }

        public static Builder builder() {
            return new Builder();
        }
    }

    public static abstract class a<R extends Result> extends com.google.android.gms.common.api.a.b<R, e> {
        public a() {
            super(wx);
        }
    }

    static {
        wx = new c();
        wy = new b<e, PlusOptions>() {
            public e a(Context context, Looper looper, fc fcVar, com.google.android.gms.plus.Plus.PlusOptions plusOptions, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
                if (plusOptions == null) {
                    plusOptions = new com.google.android.gms.plus.Plus.PlusOptions();
                }
                e eVar = eVar;
                Context context2 = context;
                Looper looper2 = looper;
                ConnectionCallbacks connectionCallbacks2 = connectionCallbacks;
                OnConnectionFailedListener onConnectionFailedListener2 = onConnectionFailedListener;
                h hVar = new h(fcVar.eC(), fcVar.eF(), (String[]) plusOptions.TK.toArray(new String[0]), new String[0], context.getPackageName(), context.getPackageName(), null, new PlusCommonExtras());
                return eVar;
            }

            public int getPriority() {
                return MMAdView.TRANSITION_UP;
            }
        };
        API = new Api(wy, wx, new Scope[0]);
        SCOPE_PLUS_LOGIN = new Scope(Scopes.PLUS_LOGIN);
        SCOPE_PLUS_PROFILE = new Scope(Scopes.PLUS_ME);
        MomentsApi = new ia();
        PeopleApi = new ib();
        AccountApi = new hy();
        TI = new hz();
    }

    private Plus() {
    }

    public static e a(GoogleApiClient googleApiClient, c cVar) {
        boolean z = true;
        fq.b(googleApiClient != null, (Object)"GoogleApiClient parameter is required.");
        fq.a(googleApiClient.isConnected(), "GoogleApiClient must be connected.");
        e eVar = (e) googleApiClient.a(cVar);
        if (eVar == null) {
            z = false;
        }
        fq.a(z, "GoogleApiClient is not configured to use the Plus.API Api. Pass this into GoogleApiClient.Builder#addApi() to use this feature.");
        return eVar;
    }
}