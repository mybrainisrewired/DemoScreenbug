package com.google.android.gms.common.api;

import android.content.Context;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api.ApiOptions;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.internal.fc;
import com.google.android.gms.internal.fg;
import com.google.android.gms.internal.fq;
import com.millennialmedia.android.MMAdView;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

final class b implements GoogleApiClient {
    private final a AL;
    private final Looper AS;
    private final Lock Ba;
    private final Condition Bb;
    private final fg Bc;
    final Queue<c<?>> Bd;
    private ConnectionResult Be;
    private int Bf;
    private int Bg;
    private int Bh;
    private boolean Bi;
    private int Bj;
    private long Bk;
    final Handler Bl;
    private final Bundle Bm;
    private final Map<com.google.android.gms.common.api.Api.c<?>, com.google.android.gms.common.api.Api.a> Bn;
    private boolean Bo;
    final Set<c<?>> Bp;
    final ConnectionCallbacks Bq;
    private final com.google.android.gms.internal.fg.b Br;

    static interface a {
        void b(c<?> cVar);
    }

    class b extends Handler {
        b(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                b.this.Ba.lock();
                if (!(b.this.isConnected() || b.this.isConnecting())) {
                    b.this.connect();
                }
                b.this.Ba.unlock();
            } else {
                Log.wtf("GoogleApiClientImpl", "Don't know how to handle this message.");
            }
        }
    }

    static interface c<A extends com.google.android.gms.common.api.Api.a> {
        void a(a aVar);

        void b(A a) throws DeadObjectException;

        void cancel();

        com.google.android.gms.common.api.Api.c<A> ea();

        int ef();

        void k(Status status);
    }

    class AnonymousClass_4 implements OnConnectionFailedListener {
        final /* synthetic */ com.google.android.gms.common.api.Api.b Bt;

        AnonymousClass_4(com.google.android.gms.common.api.Api.b bVar) {
            this.Bt = bVar;
        }

        public void onConnectionFailed(ConnectionResult result) {
            b.this.Ba.lock();
            if (b.this.Be == null || this.Bt.getPriority() < b.this.Bf) {
                b.this.Be = result;
                b.this.Bf = this.Bt.getPriority();
            }
            b.this.ei();
            b.this.Ba.unlock();
        }
    }

    public b(Context context, Looper looper, fc fcVar, Map<Api<?>, ApiOptions> map, Set<ConnectionCallbacks> set, Set<OnConnectionFailedListener> set2) {
        this.Ba = new ReentrantLock();
        this.Bb = this.Ba.newCondition();
        this.Bd = new LinkedList();
        this.Bg = 4;
        this.Bh = 0;
        this.Bi = false;
        this.Bk = 5000;
        this.Bm = new Bundle();
        this.Bn = new HashMap();
        this.Bp = new HashSet();
        this.AL = new a() {
            public void b(c<?> cVar) {
                b.this.Ba.lock();
                b.this.Bp.remove(cVar);
                b.this.Ba.unlock();
            }
        };
        this.Bq = new ConnectionCallbacks() {
            public void onConnected(Bundle connectionHint) {
                b.this.Ba.lock();
                if (b.this.Bg == 1) {
                    if (connectionHint != null) {
                        b.this.Bm.putAll(connectionHint);
                    }
                    b.this.ei();
                }
                b.this.Ba.unlock();
            }

            public void onConnectionSuspended(int cause) {
                b.this.Ba.lock();
                b.this.E(cause);
                switch (cause) {
                    case MMAdView.TRANSITION_FADE:
                        if (b.this.ek()) {
                            b.this.Ba.unlock();
                            return;
                        } else {
                            b.this.Bh = MMAdView.TRANSITION_UP;
                            b.this.Bl.sendMessageDelayed(b.this.Bl.obtainMessage(1), b.this.Bk);
                        }
                        break;
                    case MMAdView.TRANSITION_UP:
                        b.this.connect();
                        break;
                }
                b.this.Ba.unlock();
            }
        };
        this.Br = new com.google.android.gms.internal.fg.b() {
            public Bundle dG() {
                return null;
            }

            public boolean em() {
                return b.this.Bo;
            }

            public boolean isConnected() {
                return b.this.isConnected();
            }
        };
        this.Bc = new fg(context, looper, this.Br);
        this.AS = looper;
        this.Bl = new b(looper);
        Iterator it = set.iterator();
        while (it.hasNext()) {
            this.Bc.registerConnectionCallbacks((ConnectionCallbacks) it.next());
        }
        it = set2.iterator();
        while (it.hasNext()) {
            this.Bc.registerConnectionFailedListener((OnConnectionFailedListener) it.next());
        }
        Iterator it2 = map.keySet().iterator();
        while (it2.hasNext()) {
            Api api = (Api) it2.next();
            com.google.android.gms.common.api.Api.b dY = api.dY();
            Object obj = map.get(api);
            this.Bn.put(api.ea(), a(dY, obj, context, looper, fcVar, this.Bq, new AnonymousClass_4(dY)));
        }
    }

    private void E(int i) {
        this.Ba.lock();
        if (this.Bg != 3) {
            if (i == -1) {
                Iterator it;
                if (isConnecting()) {
                    it = this.Bd.iterator();
                    while (it.hasNext()) {
                        c cVar = (c) it.next();
                        if (cVar.ef() != 1) {
                            cVar.cancel();
                            it.remove();
                        }
                    }
                } else {
                    this.Bd.clear();
                }
                it = this.Bp.iterator();
                while (it.hasNext()) {
                    ((c) it.next()).cancel();
                }
                this.Bp.clear();
                if (this.Be == null && !this.Bd.isEmpty()) {
                    this.Bi = true;
                    this.Ba.unlock();
                    return;
                }
            }
            boolean isConnecting = isConnecting();
            boolean isConnected = isConnected();
            this.Bg = 3;
            if (isConnecting) {
                if (i == -1) {
                    this.Be = null;
                }
                this.Bb.signalAll();
            }
            this.Bo = false;
            Iterator it2 = this.Bn.values().iterator();
            while (it2.hasNext()) {
                com.google.android.gms.common.api.Api.a aVar = (com.google.android.gms.common.api.Api.a) it2.next();
                if (aVar.isConnected()) {
                    aVar.disconnect();
                }
            }
            this.Bo = true;
            this.Bg = 4;
            if (isConnected) {
                if (i != -1) {
                    this.Bc.O(i);
                }
                this.Bo = false;
            }
        }
        this.Ba.unlock();
    }

    private static <C extends com.google.android.gms.common.api.Api.a, O> C a(com.google.android.gms.common.api.Api.b<C, O> bVar, Object obj, Context context, Looper looper, fc fcVar, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        return bVar.a(context, looper, fcVar, obj, connectionCallbacks, onConnectionFailedListener);
    }

    private <A extends com.google.android.gms.common.api.Api.a> void a(c<A> cVar) throws DeadObjectException {
        boolean z = true;
        this.Ba.lock();
        boolean z2 = isConnected() || ek();
        fq.a(z2, "GoogleApiClient is not connected yet.");
        if (cVar.ea() == null) {
            z = false;
        }
        fq.b(z, (Object)"This task can not be executed or enqueued (it's probably a Batch or malformed)");
        this.Bp.add(cVar);
        cVar.a(this.AL);
        if (ek()) {
            cVar.k(new Status(8));
            this.Ba.unlock();
        } else {
            cVar.b(a(cVar.ea()));
            this.Ba.unlock();
        }
    }

    private void ei() {
        this.Ba.lock();
        this.Bj--;
        if (this.Bj == 0) {
            if (this.Be != null) {
                this.Bi = false;
                E(MMAdView.TRANSITION_DOWN);
                if (ek()) {
                    this.Bh--;
                }
                if (ek()) {
                    this.Bl.sendMessageDelayed(this.Bl.obtainMessage(1), this.Bk);
                } else {
                    this.Bc.a(this.Be);
                }
                this.Bo = false;
            } else {
                this.Bg = 2;
                el();
                this.Bb.signalAll();
                ej();
                if (this.Bi) {
                    this.Bi = false;
                    E(-1);
                } else {
                    this.Bc.b(this.Bm.isEmpty() ? null : this.Bm);
                }
            }
        }
        this.Ba.unlock();
    }

    private void ej() {
        boolean z = isConnected() || ek();
        fq.a(z, "GoogleApiClient is not connected yet.");
        this.Ba.lock();
        while (!this.Bd.isEmpty()) {
            try {
                a((c) this.Bd.remove());
            } catch (DeadObjectException e) {
                Log.w("GoogleApiClientImpl", "Service died while flushing queue", e);
            }
        }
        this.Ba.unlock();
    }

    private boolean ek() {
        this.Ba.lock();
        boolean z = this.Bh != 0;
        this.Ba.unlock();
        return z;
    }

    private void el() {
        this.Ba.lock();
        this.Bh = 0;
        this.Bl.removeMessages(1);
        this.Ba.unlock();
    }

    public <C extends com.google.android.gms.common.api.Api.a> C a(com.google.android.gms.common.api.Api.c<C> cVar) {
        Object obj = (com.google.android.gms.common.api.Api.a) this.Bn.get(cVar);
        fq.b(obj, (Object)"Appropriate Api was not requested.");
        return obj;
    }

    public <A extends com.google.android.gms.common.api.Api.a, T extends com.google.android.gms.common.api.a.b<? extends Result, A>> T a(com.google.android.gms.common.api.a.b bVar) {
        this.Ba.lock();
        if (isConnected()) {
            b(bVar);
        } else {
            this.Bd.add(bVar);
        }
        this.Ba.unlock();
        return bVar;
    }

    public <A extends com.google.android.gms.common.api.Api.a, T extends com.google.android.gms.common.api.a.b<? extends Result, A>> T b(c cVar) {
        boolean z = isConnected() || ek();
        fq.a(z, "GoogleApiClient is not connected yet.");
        ej();
        try {
            a(cVar);
        } catch (DeadObjectException e) {
            E(1);
        }
        return cVar;
    }

    public ConnectionResult blockingConnect(long timeout, TimeUnit unit) {
        ConnectionResult connectionResult;
        fq.a(Looper.myLooper() != Looper.getMainLooper(), "blockingConnect must not be called on the UI thread");
        this.Ba.lock();
        connect();
        long toNanos = unit.toNanos(timeout);
        while (isConnecting()) {
            try {
                toNanos = this.Bb.awaitNanos(toNanos);
                if (toNanos <= 0) {
                    connectionResult = new ConnectionResult(14, null);
                    this.Ba.unlock();
                    return connectionResult;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                connectionResult = new ConnectionResult(15, null);
                this.Ba.unlock();
                return connectionResult;
            }
        }
        if (isConnected()) {
            connectionResult = ConnectionResult.Ag;
            this.Ba.unlock();
            return connectionResult;
        } else if (this.Be != null) {
            connectionResult = this.Be;
            this.Ba.unlock();
            return connectionResult;
        } else {
            connectionResult = new ConnectionResult(13, null);
            this.Ba.unlock();
            return connectionResult;
        }
    }

    public void connect() {
        this.Ba.lock();
        this.Bi = false;
        if (isConnected() || isConnecting()) {
            this.Ba.unlock();
        } else {
            this.Bo = true;
            this.Be = null;
            this.Bg = 1;
            this.Bm.clear();
            this.Bj = this.Bn.size();
            Iterator it = this.Bn.values().iterator();
            while (it.hasNext()) {
                ((com.google.android.gms.common.api.Api.a) it.next()).connect();
            }
            this.Ba.unlock();
        }
    }

    public void disconnect() {
        el();
        E(-1);
    }

    public Looper getLooper() {
        return this.AS;
    }

    public boolean isConnected() {
        this.Ba.lock();
        boolean z = this.Bg == 2;
        this.Ba.unlock();
        return z;
    }

    public boolean isConnecting() {
        boolean z = true;
        this.Ba.lock();
        if (this.Bg != 1) {
            z = false;
        }
        this.Ba.unlock();
        return z;
    }

    public boolean isConnectionCallbacksRegistered(ConnectionCallbacks listener) {
        return this.Bc.isConnectionCallbacksRegistered(listener);
    }

    public boolean isConnectionFailedListenerRegistered(OnConnectionFailedListener listener) {
        return this.Bc.isConnectionFailedListenerRegistered(listener);
    }

    public void reconnect() {
        disconnect();
        connect();
    }

    public void registerConnectionCallbacks(ConnectionCallbacks listener) {
        this.Bc.registerConnectionCallbacks(listener);
    }

    public void registerConnectionFailedListener(OnConnectionFailedListener listener) {
        this.Bc.registerConnectionFailedListener(listener);
    }

    public void unregisterConnectionCallbacks(ConnectionCallbacks listener) {
        this.Bc.unregisterConnectionCallbacks(listener);
    }

    public void unregisterConnectionFailedListener(OnConnectionFailedListener listener) {
        this.Bc.unregisterConnectionFailedListener(listener);
    }
}