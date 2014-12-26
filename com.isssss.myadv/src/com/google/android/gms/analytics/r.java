package com.google.android.gms.analytics;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import com.google.android.gms.analytics.u.a;

class r extends af {
    private static final Object sF;
    private static r sR;
    private Context mContext;
    private Handler mHandler;
    private d sG;
    private volatile f sH;
    private int sI;
    private boolean sJ;
    private boolean sK;
    private String sL;
    private boolean sM;
    private boolean sN;
    private e sO;
    private q sP;
    private boolean sQ;

    static {
        sF = new Object();
    }

    private r() {
        this.sI = 1800;
        this.sJ = true;
        this.sM = true;
        this.sN = true;
        this.sO = new e() {
            public void r(boolean z) {
                r.this.a(z, r.this.sM);
            }
        };
        this.sQ = false;
    }

    public static r ci() {
        if (sR == null) {
            sR = new r();
        }
        return sR;
    }

    private void cj() {
        this.sP = new q(this);
        this.sP.o(this.mContext);
    }

    private void ck() {
        this.mHandler = new Handler(this.mContext.getMainLooper(), new Callback() {
            public boolean handleMessage(Message msg) {
                if (1 == msg.what && sF.equals(msg.obj)) {
                    u.cy().t(true);
                    r.this.dispatchLocalHits();
                    u.cy().t(false);
                    if (r.this.sI > 0 && !r.this.sQ) {
                        r.this.mHandler.sendMessageDelayed(r.this.mHandler.obtainMessage(1, sF), (long) (r.this.sI * 1000));
                    }
                }
                return true;
            }
        });
        if (this.sI > 0) {
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1, sF), (long) (this.sI * 1000));
        }
    }

    synchronized void a(Context context, f fVar) {
        if (this.mContext == null) {
            this.mContext = context.getApplicationContext();
            if (this.sH == null) {
                this.sH = fVar;
                if (this.sJ) {
                    dispatchLocalHits();
                    this.sJ = false;
                }
                if (this.sK) {
                    bY();
                    this.sK = false;
                }
            }
        }
    }

    synchronized void a(boolean z, boolean z2) {
        if (!(this.sQ == z && this.sM == z2)) {
            if (z || !z2) {
                if (this.sI > 0) {
                    this.mHandler.removeMessages(1, sF);
                }
            }
            if (!z && z2 && this.sI > 0) {
                this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1, sF), (long) (this.sI * 1000));
            }
            StringBuilder append = new StringBuilder().append("PowerSaveMode ");
            String str = (z || !z2) ? "initiated." : "terminated.";
            aa.y(append.append(str).toString());
            this.sQ = z;
            this.sM = z2;
        }
    }

    void bY() {
        if (this.sH == null) {
            aa.y("setForceLocalDispatch() queued. It will be called once initialization is complete.");
            this.sK = true;
        } else {
            u.cy().a(a.uN);
            this.sH.bY();
        }
    }

    synchronized d cl() {
        if (this.sG == null && this.mContext == null) {
            throw new IllegalStateException("Cant get a store unless we have a context");
        }
        this.sG = new ac(this.sO, this.mContext);
        if (this.sL != null) {
            this.sG.bX().F(this.sL);
            this.sL = null;
        }
        if (this.mHandler == null) {
            ck();
        }
        if (this.sP == null && this.sN) {
            cj();
        }
        return this.sG;
    }

    synchronized void cm() {
        if (!this.sQ && this.sM && this.sI > 0) {
            this.mHandler.removeMessages(1, sF);
            this.mHandler.sendMessage(this.mHandler.obtainMessage(1, sF));
        }
    }

    synchronized void dispatchLocalHits() {
        if (this.sH == null) {
            aa.y("Dispatch call queued. Dispatch will run once initialization is complete.");
            this.sJ = true;
        } else {
            u.cy().a(a.uA);
            this.sH.bW();
        }
    }

    synchronized void s(boolean z) {
        a(this.sQ, z);
    }

    synchronized void setLocalDispatchPeriod(int dispatchPeriodInSeconds) {
        if (this.mHandler == null) {
            aa.y("Dispatch period set with null handler. Dispatch will run once initialization is complete.");
            this.sI = dispatchPeriodInSeconds;
        } else {
            u.cy().a(a.uB);
            if (!this.sQ && this.sM && this.sI > 0) {
                this.mHandler.removeMessages(1, sF);
            }
            this.sI = dispatchPeriodInSeconds;
            if (dispatchPeriodInSeconds > 0 && !this.sQ && this.sM) {
                this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1, sF), (long) (dispatchPeriodInSeconds * 1000));
            }
        }
    }
}