package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;

public abstract class cv extends do_ {
    private final cx mQ;
    private final com.google.android.gms.internal.cu.a pc;

    public static final class a extends cv {
        private final Context mContext;

        public a(Context context, cx cxVar, com.google.android.gms.internal.cu.a aVar) {
            super(cxVar, aVar);
            this.mContext = context;
        }

        public void be() {
        }

        public db bf() {
            return dc.a(this.mContext, new ax(), new bg());
        }
    }

    public static final class b extends cv implements ConnectionCallbacks, OnConnectionFailedListener {
        private final Object li;
        private final com.google.android.gms.internal.cu.a pc;
        private final cw pd;

        public b(Context context, cx cxVar, com.google.android.gms.internal.cu.a aVar) {
            super(cxVar, aVar);
            this.li = new Object();
            this.pc = aVar;
            this.pd = new cw(context, this, this, cxVar.kK.rs);
            this.pd.connect();
        }

        public void be() {
            synchronized (this.li) {
                if (this.pd.isConnected() || this.pd.isConnecting()) {
                    this.pd.disconnect();
                }
            }
        }

        public db bf() {
            db bi;
            synchronized (this.li) {
                try {
                    bi = this.pd.bi();
                } catch (IllegalStateException e) {
                    bi = null;
                }
            }
            return bi;
        }

        public void onConnected(Bundle connectionHint) {
            start();
        }

        public void onConnectionFailed(ConnectionResult result) {
            this.pc.a(new cz(0));
        }

        public void onDisconnected() {
            dw.v("Disconnected from remote ad request service.");
        }
    }

    public cv(cx cxVar, com.google.android.gms.internal.cu.a aVar) {
        this.mQ = cxVar;
        this.pc = aVar;
    }

    private static cz a(db dbVar, cx cxVar) {
        try {
            return dbVar.b(cxVar);
        } catch (RemoteException e) {
            dw.c("Could not fetch ad response from ad request service.", e);
            return null;
        }
    }

    public final void aY() {
        cz czVar;
        db bf = bf();
        if (bf == null) {
            czVar = new cz(0);
        } else {
            czVar = a(bf, this.mQ);
            if (czVar == null) {
                czVar = new cz(0);
            }
        }
        be();
        this.pc.a(czVar);
    }

    public abstract void be();

    public abstract db bf();

    public final void onStop() {
        be();
    }
}