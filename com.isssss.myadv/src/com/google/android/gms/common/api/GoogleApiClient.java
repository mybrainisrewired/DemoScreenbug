package com.google.android.gms.common.api;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api.ApiOptions;
import com.google.android.gms.common.api.Api.ApiOptions.HasOptions;
import com.google.android.gms.common.api.Api.ApiOptions.NotRequiredOptions;
import com.google.android.gms.common.api.Api.a;
import com.google.android.gms.common.api.Api.c;
import com.google.android.gms.common.api.a.b;
import com.google.android.gms.internal.fc;
import com.google.android.gms.internal.fq;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface GoogleApiClient {

    public static final class Builder {
        private Looper AS;
        private final Set<String> AT;
        private int AU;
        private View AV;
        private String AW;
        private final Map<Api<?>, ApiOptions> AX;
        private final Set<com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks> AY;
        private final Set<com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener> AZ;
        private final Context mContext;
        private String wG;

        public Builder(Context context) {
            this.AT = new HashSet();
            this.AX = new HashMap();
            this.AY = new HashSet();
            this.AZ = new HashSet();
            this.mContext = context;
            this.AS = context.getMainLooper();
            this.AW = context.getPackageName();
        }

        public Builder(Context context, Object connectedListener, Object connectionFailedListener) {
            this(context);
            fq.b(connectedListener, (Object)"Must provide a connected listener");
            this.AY.add(connectedListener);
            fq.b(connectionFailedListener, (Object)"Must provide a connection failed listener");
            this.AZ.add(connectionFailedListener);
        }

        public com.google.android.gms.common.api.GoogleApiClient.Builder addApi(Api<? extends NotRequiredOptions> api) {
            this.AX.put(api, null);
            List dZ = api.dZ();
            int size = dZ.size();
            int i = 0;
            while (i < size) {
                this.AT.add(((Scope) dZ.get(i)).en());
                i++;
            }
            return this;
        }

        public <O extends HasOptions> com.google.android.gms.common.api.GoogleApiClient.Builder addApi(Api<O> api, Object options) {
            fq.b(options, (Object)"Null options are not permitted for this Api");
            this.AX.put(api, options);
            List dZ = api.dZ();
            int size = dZ.size();
            int i = 0;
            while (i < size) {
                this.AT.add(((Scope) dZ.get(i)).en());
                i++;
            }
            return this;
        }

        public com.google.android.gms.common.api.GoogleApiClient.Builder addConnectionCallbacks(com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks listener) {
            this.AY.add(listener);
            return this;
        }

        public com.google.android.gms.common.api.GoogleApiClient.Builder addOnConnectionFailedListener(com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener listener) {
            this.AZ.add(listener);
            return this;
        }

        public com.google.android.gms.common.api.GoogleApiClient.Builder addScope(Scope scope) {
            this.AT.add(scope.en());
            return this;
        }

        public GoogleApiClient build() {
            return new b(this.mContext, this.AS, eh(), this.AX, this.AY, this.AZ);
        }

        public fc eh() {
            return new fc(this.wG, this.AT, this.AU, this.AV, this.AW);
        }

        public com.google.android.gms.common.api.GoogleApiClient.Builder setAccountName(String accountName) {
            this.wG = accountName;
            return this;
        }

        public com.google.android.gms.common.api.GoogleApiClient.Builder setGravityForPopups(int gravityForPopups) {
            this.AU = gravityForPopups;
            return this;
        }

        public com.google.android.gms.common.api.GoogleApiClient.Builder setHandler(Object handler) {
            fq.b(handler, (Object)"Handler must not be null");
            this.AS = handler.getLooper();
            return this;
        }

        public com.google.android.gms.common.api.GoogleApiClient.Builder setViewForPopups(View viewForPopups) {
            this.AV = viewForPopups;
            return this;
        }

        public com.google.android.gms.common.api.GoogleApiClient.Builder useDefaultAccount() {
            return setAccountName("<<default account>>");
        }
    }

    public static interface ConnectionCallbacks {
        public static final int CAUSE_NETWORK_LOST = 2;
        public static final int CAUSE_SERVICE_DISCONNECTED = 1;

        void onConnected(Bundle bundle);

        void onConnectionSuspended(int i);
    }

    public static interface OnConnectionFailedListener extends com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener {
        void onConnectionFailed(ConnectionResult connectionResult);
    }

    <C extends a> C a(c<C> cVar);

    <A extends a, T extends b<? extends Result, A>> T a(T t);

    <A extends a, T extends b<? extends Result, A>> T b(T t);

    ConnectionResult blockingConnect(long j, TimeUnit timeUnit);

    void connect();

    void disconnect();

    Looper getLooper();

    boolean isConnected();

    boolean isConnecting();

    boolean isConnectionCallbacksRegistered(ConnectionCallbacks connectionCallbacks);

    boolean isConnectionFailedListenerRegistered(OnConnectionFailedListener onConnectionFailedListener);

    void reconnect();

    void registerConnectionCallbacks(ConnectionCallbacks connectionCallbacks);

    void registerConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener);

    void unregisterConnectionCallbacks(ConnectionCallbacks connectionCallbacks);

    void unregisterConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener);
}