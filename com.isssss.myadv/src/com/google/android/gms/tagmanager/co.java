package com.google.android.gms.tagmanager;

import android.content.Context;
import com.google.android.gms.internal.c.j;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

class co implements e {
    private final String WJ;
    private String Xg;
    private bg<j> Zf;
    private r Zg;
    private final ScheduledExecutorService Zi;
    private final a Zj;
    private ScheduledFuture<?> Zk;
    private boolean mClosed;
    private final Context mContext;

    static interface a {
        cn a(r rVar);
    }

    static interface b {
        ScheduledExecutorService la();
    }

    public co(Context context, String str, r rVar) {
        this(context, str, rVar, null, null);
    }

    co(Context context, String str, r rVar, b bVar, a aVar) {
        this.Zg = rVar;
        this.mContext = context;
        this.WJ = str;
        if (bVar == null) {
            bVar = new b() {
                public ScheduledExecutorService la() {
                    return Executors.newSingleThreadScheduledExecutor();
                }
            };
        }
        this.Zi = bVar.la();
        if (aVar == null) {
            this.Zj = new a() {
                public cn a(r rVar) {
                    return new cn(co.this.mContext, co.this.WJ, rVar);
                }
            };
        } else {
            this.Zj = aVar;
        }
    }

    private cn bK(String str) {
        cn a = this.Zj.a(this.Zg);
        a.a(this.Zf);
        a.bu(this.Xg);
        a.bJ(str);
        return a;
    }

    private synchronized void kZ() {
        if (this.mClosed) {
            throw new IllegalStateException("called method after closed");
        }
    }

    public synchronized void a(bg<j> bgVar) {
        kZ();
        this.Zf = bgVar;
    }

    public synchronized void bu(String str) {
        kZ();
        this.Xg = str;
    }

    public synchronized void d(long j, String str) {
        bh.y("loadAfterDelay: containerId=" + this.WJ + " delay=" + j);
        kZ();
        if (this.Zf == null) {
            throw new IllegalStateException("callback must be set before loadAfterDelay() is called.");
        }
        if (this.Zk != null) {
            this.Zk.cancel(false);
        }
        this.Zk = this.Zi.schedule(bK(str), j, TimeUnit.MILLISECONDS);
    }

    public synchronized void release() {
        kZ();
        if (this.Zk != null) {
            this.Zk.cancel(false);
        }
        this.Zi.shutdown();
        this.mClosed = true;
    }
}