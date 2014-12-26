package com.google.android.gms.common.api;

import android.os.DeadObjectException;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.util.Pair;
import com.google.android.gms.internal.fk;
import com.google.android.gms.internal.fq;
import com.millennialmedia.android.MMAdView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class a {

    public static class c<R extends Result> extends Handler {
        public c() {
            this(Looper.getMainLooper());
        }

        public c(Looper looper) {
            super(looper);
        }

        public void a(ResultCallback<R> resultCallback, R r) {
            sendMessage(obtainMessage(1, new Pair(resultCallback, r)));
        }

        public void a(com.google.android.gms.common.api.a.a<R> aVar, long j) {
            sendMessageDelayed(obtainMessage(MMAdView.TRANSITION_UP, aVar), j);
        }

        protected void b(ResultCallback<R> resultCallback, R r) {
            resultCallback.onResult(r);
        }

        public void eg() {
            removeMessages(MMAdView.TRANSITION_UP);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MMAdView.TRANSITION_FADE:
                    Pair pair = (Pair) msg.obj;
                    b((ResultCallback) pair.first, (Result) pair.second);
                case MMAdView.TRANSITION_UP:
                    ((com.google.android.gms.common.api.a.a) msg.obj).ee();
                default:
                    Log.wtf("GoogleApi", "Don't know how to handle this message.");
            }
        }
    }

    public static interface d<R> {
        void b(R r);
    }

    public static abstract class a<R extends Result> implements PendingResult<R>, com.google.android.gms.common.api.a.d<R> {
        private final Object AB;
        private com.google.android.gms.common.api.a.c<R> AC;
        private final CountDownLatch AD;
        private final ArrayList<com.google.android.gms.common.api.PendingResult.a> AE;
        private ResultCallback<R> AF;
        private volatile R AG;
        private volatile boolean AH;
        private boolean AI;
        private boolean AJ;
        private fk AK;

        a() {
            this.AB = new Object();
            this.AD = new CountDownLatch(1);
            this.AE = new ArrayList();
        }

        public a(Looper looper) {
            this.AB = new Object();
            this.AD = new CountDownLatch(1);
            this.AE = new ArrayList();
            this.AC = new com.google.android.gms.common.api.a.c(looper);
        }

        public a(com.google.android.gms.common.api.a.c<R> cVar) {
            this.AB = new Object();
            this.AD = new CountDownLatch(1);
            this.AE = new ArrayList();
            this.AC = cVar;
        }

        private void b(R r) {
            this.AG = r;
            this.AK = null;
            this.AD.countDown();
            Status status = this.AG.getStatus();
            if (this.AF != null) {
                this.AC.eg();
                if (!this.AI) {
                    this.AC.a(this.AF, eb());
                }
            }
            Iterator it = this.AE.iterator();
            while (it.hasNext()) {
                ((com.google.android.gms.common.api.PendingResult.a) it.next()).l(status);
            }
            this.AE.clear();
        }

        private void c(Result result) {
            if (result instanceof Releasable) {
                try {
                    ((Releasable) result).release();
                } catch (Exception e) {
                    Log.w("AbstractPendingResult", "Unable to release " + this, e);
                }
            }
        }

        private R eb() {
            R r;
            synchronized (this.AB) {
                fq.a(!this.AH, "Result has already been consumed.");
                fq.a(isReady(), "Result is not ready.");
                r = this.AG;
                ec();
            }
            return r;
        }

        private void ed() {
            synchronized (this.AB) {
                if (!isReady()) {
                    a(d(Status.Bw));
                    this.AJ = true;
                }
            }
        }

        private void ee() {
            synchronized (this.AB) {
                if (!isReady()) {
                    a(d(Status.By));
                    this.AJ = true;
                }
            }
        }

        public final void a(com.google.android.gms.common.api.PendingResult.a aVar) {
            fq.a(!this.AH, "Result has already been consumed.");
            synchronized (this.AB) {
                if (isReady()) {
                    aVar.l(this.AG.getStatus());
                } else {
                    this.AE.add(aVar);
                }
            }
        }

        public final void a(Result result) {
            boolean z = true;
            synchronized (this.AB) {
                if (this.AJ || this.AI) {
                    c(result);
                } else {
                    fq.a(!isReady(), "Results have already been set");
                    if (this.AH) {
                        z = false;
                    }
                    fq.a(z, "Result has already been consumed");
                    b(result);
                }
            }
        }

        protected void a(com.google.android.gms.common.api.a.c<R> cVar) {
            this.AC = cVar;
        }

        protected final void a(fk fkVar) {
            synchronized (this.AB) {
                this.AK = fkVar;
            }
        }

        public final R await() {
            boolean z = false;
            fq.a(!this.AH, "Result has already been consumed");
            if (isReady() || Looper.myLooper() != Looper.getMainLooper()) {
                z = true;
            }
            fq.a(z, "await must not be called on the UI thread");
            try {
                this.AD.await();
            } catch (InterruptedException e) {
                ed();
            }
            fq.a(isReady(), "Result is not ready.");
            return eb();
        }

        public final R await(long time, TimeUnit units) {
            boolean z = false;
            fq.a(!this.AH, "Result has already been consumed.");
            if (isReady() || Looper.myLooper() != Looper.getMainLooper()) {
                z = true;
            }
            fq.a(z, "await must not be called on the UI thread");
            try {
                if (!this.AD.await(time, units)) {
                    ee();
                }
            } catch (InterruptedException e) {
                ed();
            }
            fq.a(isReady(), "Result is not ready.");
            return eb();
        }

        public /* synthetic */ void b(Object obj) {
            a((Result) obj);
        }

        public void cancel() {
            synchronized (this.AB) {
                if (this.AI) {
                } else {
                    if (this.AK != null) {
                        try {
                            this.AK.cancel();
                        } catch (RemoteException e) {
                        }
                    }
                    c(this.AG);
                    this.AF = null;
                    this.AI = true;
                    b(d(Status.Bz));
                }
            }
        }

        protected abstract R d(Status status);

        protected void ec() {
            this.AH = true;
            this.AG = null;
            this.AF = null;
        }

        public boolean isCanceled() {
            boolean z;
            synchronized (this.AB) {
                z = this.AI;
            }
            return z;
        }

        public final boolean isReady() {
            return this.AD.getCount() == 0;
        }

        public final void setResultCallback(ResultCallback callback) {
            fq.a(!this.AH, "Result has already been consumed.");
            synchronized (this.AB) {
                if (isCanceled()) {
                } else {
                    if (isReady()) {
                        this.AC.a(callback, eb());
                    } else {
                        this.AF = callback;
                    }
                }
            }
        }

        public final void setResultCallback(ResultCallback callback, long time, TimeUnit units) {
            fq.a(!this.AH, "Result has already been consumed.");
            synchronized (this.AB) {
                if (isCanceled()) {
                } else {
                    if (isReady()) {
                        this.AC.a(callback, eb());
                    } else {
                        this.AF = callback;
                        this.AC.a(this, units.toMillis(time));
                    }
                }
            }
        }
    }

    public static abstract class b<R extends Result, A extends com.google.android.gms.common.api.Api.a> extends com.google.android.gms.common.api.a.a<R> implements c<A> {
        private a AL;
        private final com.google.android.gms.common.api.Api.c<A> Az;

        protected b(com.google.android.gms.common.api.Api.c<A> cVar) {
            this.Az = (com.google.android.gms.common.api.Api.c) fq.f(cVar);
        }

        private void a(RemoteException remoteException) {
            k(new Status(8, remoteException.getLocalizedMessage(), null));
        }

        protected abstract void a(A a) throws RemoteException;

        public void a(a aVar) {
            this.AL = aVar;
        }

        public final void b(com.google.android.gms.common.api.Api.a aVar) throws DeadObjectException {
            a(new com.google.android.gms.common.api.a.c(aVar.getLooper()));
            try {
                a(aVar);
            } catch (DeadObjectException e) {
                RemoteException remoteException = e;
                a(remoteException);
                throw remoteException;
            } catch (RemoteException e2) {
                a(e2);
            }
        }

        public final com.google.android.gms.common.api.Api.c<A> ea() {
            return this.Az;
        }

        protected void ec() {
            super.ec();
            if (this.AL != null) {
                this.AL.b(this);
                this.AL = null;
            }
        }

        public int ef() {
            return 0;
        }

        public final void k(Status status) {
            fq.b(!status.isSuccess(), (Object)"Failed result must not be success");
            a(d(status));
        }
    }
}