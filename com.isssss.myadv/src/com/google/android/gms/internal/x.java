package com.google.android.gms.internal;

import android.os.Handler;
import com.isssss.myadv.service.SystemService;
import java.lang.ref.WeakReference;

public final class x {
    private final a kV;
    private final Runnable kW;
    private ah kX;
    private boolean kY;
    private boolean kZ;
    private long la;

    class AnonymousClass_1 implements Runnable {
        private final WeakReference<v> lb;
        final /* synthetic */ v lc;

        AnonymousClass_1(v vVar) {
            this.lc = vVar;
            this.lb = new WeakReference(this.lc);
        }

        public void run() {
            x.this.kY = false;
            v vVar = (v) this.lb.get();
            if (vVar != null) {
                vVar.b(x.this.kX);
            }
        }
    }

    public static class a {
        private final Handler mHandler;

        public a(Handler handler) {
            this.mHandler = handler;
        }

        public boolean postDelayed(Runnable runnable, long timeFromNowInMillis) {
            return this.mHandler.postDelayed(runnable, timeFromNowInMillis);
        }

        public void removeCallbacks(Runnable runnable) {
            this.mHandler.removeCallbacks(runnable);
        }
    }

    public x(v vVar) {
        this(vVar, new a(dv.rp));
    }

    x(v vVar, a aVar) {
        this.kY = false;
        this.kZ = false;
        this.la = 0;
        this.kV = aVar;
        this.kW = new AnonymousClass_1(vVar);
    }

    public void a(ah ahVar, long j) {
        if (this.kY) {
            dw.z("An ad refresh is already scheduled.");
        } else {
            this.kX = ahVar;
            this.kY = true;
            this.la = j;
            if (!this.kZ) {
                dw.x("Scheduling ad refresh " + j + " milliseconds from now.");
                this.kV.postDelayed(this.kW, j);
            }
        }
    }

    public void cancel() {
        this.kY = false;
        this.kV.removeCallbacks(this.kW);
    }

    public void d(ah ahVar) {
        a(ahVar, (long)SystemService.MINUTE);
    }

    public void pause() {
        this.kZ = true;
        if (this.kY) {
            this.kV.removeCallbacks(this.kW);
        }
    }

    public void resume() {
        this.kZ = false;
        if (this.kY) {
            this.kY = false;
            a(this.kX, this.la);
        }
    }
}