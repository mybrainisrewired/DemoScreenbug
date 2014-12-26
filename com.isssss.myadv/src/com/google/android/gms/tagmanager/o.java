package com.google.android.gms.tagmanager;

import android.content.Context;
import android.os.Looper;
import com.google.android.gms.common.api.Releasable;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.c.j;
import com.google.android.gms.internal.gl;
import com.google.android.gms.internal.gn;
import com.isssss.myadv.service.SystemService;
import com.mopub.common.Preconditions;

class o extends com.google.android.gms.common.api.a.a<ContainerHolder> {
    private final Looper AS;
    private final String WJ;
    private long WO;
    private final TagManager WW;
    private final d WZ;
    private final gl Wv;
    private final cf Xa;
    private final int Xb;
    private f Xc;
    private volatile n Xd;
    private volatile boolean Xe;
    private j Xf;
    private String Xg;
    private e Xh;
    private a Xi;
    private final Context mContext;

    static interface a {
        boolean b(Container container);
    }

    class AnonymousClass_2 implements a {
        final /* synthetic */ boolean Xk;

        AnonymousClass_2(boolean z) {
            this.Xk = z;
        }

        public boolean b(Container container) {
            return this.Xk ? container.getLastRefreshTime() + 43200000 >= o.this.Wv.currentTimeMillis() : !container.isDefault();
        }
    }

    private class b implements bg<com.google.android.gms.internal.it.a> {
        private b() {
        }

        public void a(com.google.android.gms.internal.it.a aVar) {
            j jVar;
            if (aVar.aaZ != null) {
                jVar = aVar.aaZ;
            } else {
                com.google.android.gms.internal.c.f fVar = aVar.fK;
                jVar = new j();
                jVar.fK = fVar;
                jVar.fJ = null;
                jVar.fL = fVar.fg;
            }
            o.this.a(jVar, aVar.aaY, true);
        }

        public void a(com.google.android.gms.tagmanager.bg.a aVar) {
            if (!o.this.Xe) {
                o.this.t(0);
            }
        }

        public /* synthetic */ void i(Object obj) {
            a((com.google.android.gms.internal.it.a) obj);
        }

        public void kl() {
        }
    }

    private class c implements bg<j> {
        private c() {
        }

        public void a(com.google.android.gms.tagmanager.bg.a aVar) {
            if (o.this.Xd != null) {
                o.this.a(o.this.Xd);
            } else {
                o.this.a(o.this.ac(Status.By));
            }
            o.this.t(SystemService.ONE_HOUR);
        }

        public void b(j jVar) {
            synchronized (o.this) {
                if (jVar.fK == null && o.this.Xf.fK == null) {
                    bh.w("Current resource is null; network resource is also null");
                    o.this.t(SystemService.ONE_HOUR);
                    return;
                } else {
                    jVar.fK = o.this.Xf.fK;
                }
                o.this.a(jVar, o.this.Wv.currentTimeMillis(), false);
                bh.y("setting refresh time to current time: " + o.this.WO);
                if (!o.this.kk()) {
                    o.this.a(jVar);
                }
            }
        }

        public /* synthetic */ void i(Object obj) {
            b((j) obj);
        }

        public void kl() {
        }
    }

    private class d implements com.google.android.gms.tagmanager.n.a {
        private d() {
        }

        public void br(String str) {
            o.this.br(str);
        }

        public String ke() {
            return o.this.ke();
        }

        public void kg() {
            if (o.this.Xa.cS()) {
                o.this.t(0);
            }
        }
    }

    static interface e extends Releasable {
        void a(bg<j> bgVar);

        void bu(String str);

        void d(long j, String str);
    }

    static interface f extends Releasable {
        void a(bg<com.google.android.gms.internal.it.a> bgVar);

        void b(com.google.android.gms.internal.it.a aVar);

        com.google.android.gms.tagmanager.cq.c ca(int i);

        void km();
    }

    o(Context context, TagManager tagManager, Looper looper, String str, int i, f fVar, e eVar, gl glVar, cf cfVar) {
        super(looper == null ? Looper.getMainLooper() : looper);
        this.mContext = context;
        this.WW = tagManager;
        if (looper == null) {
            looper = Looper.getMainLooper();
        }
        this.AS = looper;
        this.WJ = str;
        this.Xb = i;
        this.Xc = fVar;
        this.Xh = eVar;
        this.WZ = new d(null);
        this.Xf = new j();
        this.Wv = glVar;
        this.Xa = cfVar;
        if (kk()) {
            br(cd.kT().kV());
        }
    }

    public o(Context context, TagManager tagManager, Looper looper, String str, int i, r rVar) {
        this(context, tagManager, looper, str, i, new cp(context, str), new co(context, str, rVar), gn.ft(), new bf(30, 900000, 5000, "refreshing", gn.ft()));
    }

    private void C(boolean z) {
        this.Xc.a(new b(null));
        this.Xh.a(new c(null));
        com.google.android.gms.tagmanager.cq.c ca = this.Xc.ca(this.Xb);
        if (ca != null) {
            this.Xd = new n(this.WW, this.AS, new Container(this.mContext, this.WW.getDataLayer(), this.WJ, 0, ca), this.WZ);
        }
        this.Xi = new AnonymousClass_2(z);
        if (kk()) {
            this.Xh.d(0, Preconditions.EMPTY_ARGUMENTS);
        } else {
            this.Xc.km();
        }
    }

    private synchronized void a(j jVar) {
        if (this.Xc != null) {
            com.google.android.gms.internal.it.a aVar = new com.google.android.gms.internal.it.a();
            aVar.aaY = this.WO;
            aVar.fK = new com.google.android.gms.internal.c.f();
            aVar.aaZ = jVar;
            this.Xc.b(aVar);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void a(com.google.android.gms.internal.c.j r9, long r10, boolean r12) {
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.tagmanager.o.a(com.google.android.gms.internal.c$j, long, boolean):void");
        /*
        r8 = this;
        r6 = 43200000; // 0x2932e00 float:2.1626111E-37 double:2.1343636E-316;
        monitor-enter(r8);
        if (r12 == 0) goto L_0x000c;
    L_0x0006:
        r0 = r8.Xe;	 Catch:{ all -> 0x006a }
        if (r0 == 0) goto L_0x000c;
    L_0x000a:
        monitor-exit(r8);
        return;
    L_0x000c:
        r0 = r8.isReady();	 Catch:{ all -> 0x006a }
        if (r0 == 0) goto L_0x0016;
    L_0x0012:
        r0 = r8.Xd;	 Catch:{ all -> 0x006a }
        if (r0 != 0) goto L_0x0016;
    L_0x0016:
        r8.Xf = r9;	 Catch:{ all -> 0x006a }
        r8.WO = r10;	 Catch:{ all -> 0x006a }
        r0 = 0;
        r2 = 43200000; // 0x2932e00 float:2.1626111E-37 double:2.1343636E-316;
        r4 = r8.WO;	 Catch:{ all -> 0x006a }
        r4 = r4 + r6;
        r6 = r8.Wv;	 Catch:{ all -> 0x006a }
        r6 = r6.currentTimeMillis();	 Catch:{ all -> 0x006a }
        r4 = r4 - r6;
        r2 = java.lang.Math.min(r2, r4);	 Catch:{ all -> 0x006a }
        r0 = java.lang.Math.max(r0, r2);	 Catch:{ all -> 0x006a }
        r8.t(r0);	 Catch:{ all -> 0x006a }
        r0 = new com.google.android.gms.tagmanager.Container;	 Catch:{ all -> 0x006a }
        r1 = r8.mContext;	 Catch:{ all -> 0x006a }
        r2 = r8.WW;	 Catch:{ all -> 0x006a }
        r2 = r2.getDataLayer();	 Catch:{ all -> 0x006a }
        r3 = r8.WJ;	 Catch:{ all -> 0x006a }
        r4 = r10;
        r6 = r9;
        r0.<init>(r1, r2, r3, r4, r6);	 Catch:{ all -> 0x006a }
        r1 = r8.Xd;	 Catch:{ all -> 0x006a }
        if (r1 != 0) goto L_0x006d;
    L_0x0049:
        r1 = new com.google.android.gms.tagmanager.n;	 Catch:{ all -> 0x006a }
        r2 = r8.WW;	 Catch:{ all -> 0x006a }
        r3 = r8.AS;	 Catch:{ all -> 0x006a }
        r4 = r8.WZ;	 Catch:{ all -> 0x006a }
        r1.<init>(r2, r3, r0, r4);	 Catch:{ all -> 0x006a }
        r8.Xd = r1;	 Catch:{ all -> 0x006a }
    L_0x0056:
        r1 = r8.isReady();	 Catch:{ all -> 0x006a }
        if (r1 != 0) goto L_0x000a;
    L_0x005c:
        r1 = r8.Xi;	 Catch:{ all -> 0x006a }
        r0 = r1.b(r0);	 Catch:{ all -> 0x006a }
        if (r0 == 0) goto L_0x000a;
    L_0x0064:
        r0 = r8.Xd;	 Catch:{ all -> 0x006a }
        r8.a(r0);	 Catch:{ all -> 0x006a }
        goto L_0x000a;
    L_0x006a:
        r0 = move-exception;
        monitor-exit(r8);
        throw r0;
    L_0x006d:
        r1 = r8.Xd;	 Catch:{ all -> 0x006a }
        r1.a(r0);	 Catch:{ all -> 0x006a }
        goto L_0x0056;
        */
    }

    private boolean kk() {
        cd kT = cd.kT();
        return (kT.kU() == a.YU || kT.kU() == a.YV) && this.WJ.equals(kT.getContainerId());
    }

    private synchronized void t(long j) {
        if (this.Xh == null) {
            bh.z("Refresh requested, but no network load scheduler.");
        } else {
            this.Xh.d(j, this.Xf.fL);
        }
    }

    protected ContainerHolder ac(Status status) {
        if (this.Xd != null) {
            return this.Xd;
        }
        if (status == Status.By) {
            bh.w("timer expired: setting result to failure");
        }
        return new n(status);
    }

    synchronized void br(String str) {
        this.Xg = str;
        if (this.Xh != null) {
            this.Xh.bu(str);
        }
    }

    protected /* synthetic */ Result d(Status status) {
        return ac(status);
    }

    synchronized String ke() {
        return this.Xg;
    }

    public void kh() {
        com.google.android.gms.tagmanager.cq.c ca = this.Xc.ca(this.Xb);
        if (ca != null) {
            a(new n(this.WW, this.AS, new Container(this.mContext, this.WW.getDataLayer(), this.WJ, 0, ca), new com.google.android.gms.tagmanager.n.a() {
                public void br(String str) {
                    o.this.br(str);
                }

                public String ke() {
                    return o.this.ke();
                }

                public void kg() {
                    bh.z("Refresh ignored: container loaded as default only.");
                }
            }));
        } else {
            String str = "Default was requested, but no default container was found";
            bh.w(str);
            a(ac(new Status(10, str, null)));
        }
        this.Xh = null;
        this.Xc = null;
    }

    public void ki() {
        C(false);
    }

    public void kj() {
        C(true);
    }
}