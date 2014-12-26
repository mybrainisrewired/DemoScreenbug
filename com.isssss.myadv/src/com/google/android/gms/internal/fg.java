package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import java.util.ArrayList;
import java.util.Iterator;

public final class fg {
    private final b Do;
    private final ArrayList<ConnectionCallbacks> Dp;
    final ArrayList<ConnectionCallbacks> Dq;
    private boolean Dr;
    private final ArrayList<OnConnectionFailedListener> Ds;
    private final Handler mHandler;

    final class a extends Handler {
        public a(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                synchronized (fg.this.Dp) {
                    if (fg.this.Do.em() && fg.this.Do.isConnected() && fg.this.Dp.contains(msg.obj)) {
                        ((ConnectionCallbacks) msg.obj).onConnected(fg.this.Do.dG());
                    }
                }
            } else {
                Log.wtf("GmsClientEvents", "Don't know how to handle this message.");
            }
        }
    }

    public static interface b {
        Bundle dG();

        boolean em();

        boolean isConnected();
    }

    public fg(Context context, Looper looper, b bVar) {
        this.Dp = new ArrayList();
        this.Dq = new ArrayList();
        this.Dr = false;
        this.Ds = new ArrayList();
        this.Do = bVar;
        this.mHandler = new a(looper);
    }

    public void O(int i) {
        this.mHandler.removeMessages(1);
        synchronized (this.Dp) {
            this.Dr = true;
            Iterator it = new ArrayList(this.Dp).iterator();
            while (it.hasNext()) {
                ConnectionCallbacks connectionCallbacks = (ConnectionCallbacks) it.next();
                if (!this.Do.em()) {
                    break;
                } else if (this.Dp.contains(connectionCallbacks)) {
                    connectionCallbacks.onConnectionSuspended(i);
                }
            }
            this.Dr = false;
        }
    }

    public void a(ConnectionResult connectionResult) {
        this.mHandler.removeMessages(1);
        synchronized (this.Ds) {
            Iterator it = new ArrayList(this.Ds).iterator();
            while (it.hasNext()) {
                OnConnectionFailedListener onConnectionFailedListener = (OnConnectionFailedListener) it.next();
                if (!this.Do.em()) {
                    return;
                } else if (this.Ds.contains(onConnectionFailedListener)) {
                    onConnectionFailedListener.onConnectionFailed(connectionResult);
                }
            }
        }
    }

    public void b(Bundle bundle) {
        boolean z = true;
        synchronized (this.Dp) {
            fq.x(!this.Dr);
            this.mHandler.removeMessages(1);
            this.Dr = true;
            if (this.Dq.size() != 0) {
                z = false;
            }
            fq.x(z);
            Iterator it = new ArrayList(this.Dp).iterator();
            while (it.hasNext()) {
                ConnectionCallbacks connectionCallbacks = (ConnectionCallbacks) it.next();
                if (!this.Do.em() || !this.Do.isConnected()) {
                    break;
                } else if (!this.Dq.contains(connectionCallbacks)) {
                    connectionCallbacks.onConnected(bundle);
                }
            }
            this.Dq.clear();
            this.Dr = false;
        }
    }

    protected void bV() {
        synchronized (this.Dp) {
            b(this.Do.dG());
        }
    }

    public boolean isConnectionCallbacksRegistered(ConnectionCallbacks listener) {
        boolean contains;
        fq.f(listener);
        synchronized (this.Dp) {
            contains = this.Dp.contains(listener);
        }
        return contains;
    }

    public boolean isConnectionFailedListenerRegistered(OnConnectionFailedListener listener) {
        boolean contains;
        fq.f(listener);
        synchronized (this.Ds) {
            contains = this.Ds.contains(listener);
        }
        return contains;
    }

    public void registerConnectionCallbacks(ConnectionCallbacks listener) {
        fq.f(listener);
        synchronized (this.Dp) {
            if (this.Dp.contains(listener)) {
                Log.w("GmsClientEvents", "registerConnectionCallbacks(): listener " + listener + " is already registered");
            } else {
                this.Dp.add(listener);
            }
        }
        if (this.Do.isConnected()) {
            this.mHandler.sendMessage(this.mHandler.obtainMessage(1, listener));
        }
    }

    public void registerConnectionFailedListener(OnConnectionFailedListener listener) {
        fq.f(listener);
        synchronized (this.Ds) {
            if (this.Ds.contains(listener)) {
                Log.w("GmsClientEvents", "registerConnectionFailedListener(): listener " + listener + " is already registered");
            } else {
                this.Ds.add(listener);
            }
        }
    }

    public void unregisterConnectionCallbacks(ConnectionCallbacks listener) {
        fq.f(listener);
        synchronized (this.Dp) {
            if (this.Dp != null) {
                if (!this.Dp.remove(listener)) {
                    Log.w("GmsClientEvents", "unregisterConnectionCallbacks(): listener " + listener + " not found");
                } else if (this.Dr) {
                    this.Dq.add(listener);
                }
            }
        }
    }

    public void unregisterConnectionFailedListener(OnConnectionFailedListener listener) {
        fq.f(listener);
        synchronized (this.Ds) {
            if (!(this.Ds == null || this.Ds.remove(listener))) {
                Log.w("GmsClientEvents", "unregisterConnectionFailedListener(): listener " + listener + " not found");
            }
        }
    }
}