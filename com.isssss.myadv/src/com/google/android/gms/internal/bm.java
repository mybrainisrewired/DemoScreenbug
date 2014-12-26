package com.google.android.gms.internal;

import android.content.Context;
import android.os.RemoteException;
import android.os.SystemClock;
import com.google.android.gms.dynamic.e;
import com.google.android.gms.internal.bn.a;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public final class bm implements a {
    private final ah kX;
    private final bq ky;
    private final Object li;
    private final Context mContext;
    private final String nn;
    private final long no;
    private final bi np;
    private final ak nq;
    private final dx nr;
    private br ns;
    private int nt;

    class AnonymousClass_1 implements Runnable {
        final /* synthetic */ bl nu;

        AnonymousClass_1(bl blVar) {
            this.nu = blVar;
        }

        public void run() {
            synchronized (bm.this.li) {
                if (bm.this.nt != -2) {
                } else {
                    bm.this.ns = bm.this.aJ();
                    if (bm.this.ns == null) {
                        bm.this.f(MMAdView.TRANSITION_RANDOM);
                    } else {
                        this.nu.a(bm.this);
                        bm.this.a(this.nu);
                    }
                }
            }
        }
    }

    public bm(Context context, String str, bq bqVar, bj bjVar, bi biVar, ah ahVar, ak akVar, dx dxVar) {
        this.li = new Object();
        this.nt = -2;
        this.mContext = context;
        this.nn = str;
        this.ky = bqVar;
        this.no = bjVar.nd != -1 ? bjVar.nd : 10000;
        this.np = biVar;
        this.kX = ahVar;
        this.nq = akVar;
        this.nr = dxVar;
    }

    private void a(long j, long j2, long j3, long j4) {
        while (this.nt == -2) {
            b(j, j2, j3, j4);
        }
    }

    private void a(bl blVar) {
        try {
            if (this.nr.rs < 4100000) {
                if (this.nq.lT) {
                    this.ns.a(e.h(this.mContext), this.kX, this.np.nb, blVar);
                } else {
                    this.ns.a(e.h(this.mContext), this.nq, this.kX, this.np.nb, (bs)blVar);
                }
            } else if (this.nq.lT) {
                this.ns.a(e.h(this.mContext), this.kX, this.np.nb, this.np.mW, (bs)blVar);
            } else {
                this.ns.a(e.h(this.mContext), this.nq, this.kX, this.np.nb, this.np.mW, blVar);
            }
        } catch (RemoteException e) {
            dw.c("Could not request ad from mediation adapter.", e);
            f(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES);
        }
    }

    private br aJ() {
        dw.x("Instantiating mediation adapter: " + this.nn);
        try {
            return this.ky.m(this.nn);
        } catch (RemoteException e) {
            dw.a("Could not instantiate mediation adapter: " + this.nn, e);
            return null;
        }
    }

    private void b(long j, long j2, long j3, long j4) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long j5 = j2 - elapsedRealtime - j;
        elapsedRealtime = j4 - elapsedRealtime - j3;
        if (j5 <= 0 || elapsedRealtime <= 0) {
            dw.x("Timed out waiting for adapter.");
            this.nt = 3;
        } else {
            try {
                this.li.wait(Math.min(j5, elapsedRealtime));
            } catch (InterruptedException e) {
                this.nt = -1;
            }
        }
    }

    public bn b(long j, long j2) {
        bn bnVar;
        synchronized (this.li) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            bl blVar = new bl();
            dv.rp.post(new AnonymousClass_1(blVar));
            a(elapsedRealtime, this.no, j, j2);
            bnVar = new bn(this.np, this.ns, this.nn, blVar, this.nt);
        }
        return bnVar;
    }

    public void cancel() {
        synchronized (this.li) {
            try {
                if (this.ns != null) {
                    this.ns.destroy();
                }
            } catch (RemoteException e) {
                dw.c("Could not destroy mediation adapter.", e);
            }
            this.nt = -1;
            this.li.notify();
        }
    }

    public void f(int i) {
        synchronized (this.li) {
            this.nt = i;
            this.li.notify();
        }
    }
}