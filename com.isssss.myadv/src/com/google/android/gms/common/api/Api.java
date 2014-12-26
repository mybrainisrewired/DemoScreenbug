package com.google.android.gms.common.api;

import android.content.Context;
import android.os.Looper;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.internal.fc;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Api<O extends ApiOptions> {
    private final ArrayList<Scope> AA;
    private final b<?, O> Ay;
    private final c<?> Az;

    public static interface ApiOptions {

        public static interface HasOptions extends com.google.android.gms.common.api.Api.ApiOptions {
        }

        public static interface NotRequiredOptions extends com.google.android.gms.common.api.Api.ApiOptions {
        }

        public static final class NoOptions implements com.google.android.gms.common.api.Api.ApiOptions.NotRequiredOptions {
            private NoOptions() {
            }
        }

        public static interface Optional extends com.google.android.gms.common.api.Api.ApiOptions.HasOptions, com.google.android.gms.common.api.Api.ApiOptions.NotRequiredOptions {
        }
    }

    public static interface a {
        void connect();

        void disconnect();

        Looper getLooper();

        boolean isConnected();
    }

    public static interface b<T extends com.google.android.gms.common.api.Api.a, O> {
        T a(Context context, Looper looper, fc fcVar, O o, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener);

        int getPriority();
    }

    public static final class c<C extends com.google.android.gms.common.api.Api.a> {
    }

    public <C extends a> Api(b<C, O> clientBuilder, c<C> clientKey, Scope... impliedScopes) {
        this.Ay = clientBuilder;
        this.Az = clientKey;
        this.AA = new ArrayList(Arrays.asList(impliedScopes));
    }

    public b<?, O> dY() {
        return this.Ay;
    }

    public List<Scope> dZ() {
        return this.AA;
    }

    public c<?> ea() {
        return this.Az;
    }
}