package com.google.android.gms.internal;

import android.content.Context;
import android.os.RemoteException;
import java.util.Iterator;

public final class bh {
    private final bq ky;
    private final Object li;
    private final Context mContext;
    private final cx mQ;
    private final bj mR;
    private boolean mS;
    private bm mT;

    class AnonymousClass_1 implements Runnable {
        final /* synthetic */ bn mU;

        AnonymousClass_1(bn bnVar) {
            this.mU = bnVar;
        }

        public void run() {
            try {
                this.mU.ny.destroy();
            } catch (RemoteException e) {
                dw.c("Could not destroy mediation adapter.", e);
            }
        }
    }

    public bh(Context context, cx cxVar, bq bqVar, bj bjVar) {
        this.li = new Object();
        this.mS = false;
        this.mContext = context;
        this.mQ = cxVar;
        this.ky = bqVar;
        this.mR = bjVar;
    }

    public bn a(long j, long j2) {
        dw.v("Starting mediation.");
        Iterator it = this.mR.nc.iterator();
        while (it.hasNext()) {
            bi biVar = (bi) it.next();
            dw.x("Trying mediation network: " + biVar.mX);
            Iterator it2 = biVar.mY.iterator();
            while (it2.hasNext()) {
                String str = (String) it2.next();
                synchronized (this.li) {
                    bn bnVar;
                    if (this.mS) {
                        bnVar = new bn(-1);
                        return bnVar;
                    } else {
                        this.mT = new bm(this.mContext, str, this.ky, this.mR, biVar, this.mQ.pg, this.mQ.kN, this.mQ.kK);
                        bnVar = this.mT.b(j, j2);
                        if (bnVar.nw == 0) {
                            dw.v("Adapter succeeded.");
                            return bnVar;
                        } else if (bnVar.ny != null) {
                            dv.rp.post(new AnonymousClass_1(bnVar));
                        }
                    }
                }
            }
        }
        return new bn(1);
    }

    public void cancel() {
        synchronized (this.li) {
            this.mS = true;
            if (this.mT != null) {
                this.mT.cancel();
            }
        }
    }
}