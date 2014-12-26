package com.google.android.gms.analytics;

import android.content.Context;
import android.content.Intent;
import com.google.android.gms.internal.ef;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

class s implements ag, com.google.android.gms.analytics.c.b, com.google.android.gms.analytics.c.c {
    private final Context mContext;
    private d sG;
    private final f sH;
    private boolean sJ;
    private volatile long sT;
    private volatile a sU;
    private volatile b sV;
    private d sW;
    private final GoogleAnalytics sX;
    private final Queue<d> sY;
    private volatile int sZ;
    private volatile Timer ta;
    private volatile Timer tb;
    private volatile Timer tc;
    private boolean td;
    private boolean te;
    private boolean tf;
    private i tg;
    private long th;

    static /* synthetic */ class AnonymousClass_3 {
        static final /* synthetic */ int[] tj;

        static {
            tj = new int[a.values().length];
            try {
                tj[a.tm.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                tj[a.tl.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                tj[a.tk.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                tj[a.to.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                tj[a.tp.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            tj[a.tq.ordinal()] = 6;
        }
    }

    private enum a {
        CONNECTING,
        CONNECTED_SERVICE,
        CONNECTED_LOCAL,
        BLOCKED,
        PENDING_CONNECTION,
        PENDING_DISCONNECT,
        DISCONNECTED;

        static {
            tk = new a("CONNECTING", 0);
            tl = new a("CONNECTED_SERVICE", 1);
            tm = new a("CONNECTED_LOCAL", 2);
            tn = new a("BLOCKED", 3);
            to = new a("PENDING_CONNECTION", 4);
            tp = new a("PENDING_DISCONNECT", 5);
            tq = new a("DISCONNECTED", 6);
            tr = new a[]{tk, tl, tm, tn, to, tp, tq};
        }
    }

    private class b extends TimerTask {
        private b() {
        }

        public void run() {
            if (s.this.sU == a.tl && s.this.sY.isEmpty() && s.this.sT + s.this.th < s.this.tg.currentTimeMillis()) {
                aa.y("Disconnecting due to inactivity");
                s.this.be();
            } else {
                s.this.tc.schedule(new b(), s.this.th);
            }
        }
    }

    private class c extends TimerTask {
        private c() {
        }

        public void run() {
            if (s.this.sU == a.tk) {
                s.this.cs();
            }
        }
    }

    private static class d {
        private final Map<String, String> ts;
        private final long tt;
        private final String tu;
        private final List<ef> tv;

        public d(Map<String, String> map, long j, String str, List<ef> list) {
            this.ts = map;
            this.tt = j;
            this.tu = str;
            this.tv = list;
        }

        public Map<String, String> cv() {
            return this.ts;
        }

        public long cw() {
            return this.tt;
        }

        public List<ef> cx() {
            return this.tv;
        }

        public String getPath() {
            return this.tu;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("PATH: ");
            stringBuilder.append(this.tu);
            if (this.ts != null) {
                stringBuilder.append("  PARAMS: ");
                Iterator it = this.ts.entrySet().iterator();
                while (it.hasNext()) {
                    Entry entry = (Entry) it.next();
                    stringBuilder.append((String) entry.getKey());
                    stringBuilder.append("=");
                    stringBuilder.append((String) entry.getValue());
                    stringBuilder.append(",  ");
                }
            }
            return stringBuilder.toString();
        }
    }

    private class e extends TimerTask {
        private e() {
        }

        public void run() {
            s.this.ct();
        }
    }

    s(Context context, f fVar) {
        this(context, fVar, null, GoogleAnalytics.getInstance(context));
    }

    s(Context context, f fVar, d dVar, GoogleAnalytics googleAnalytics) {
        this.sY = new ConcurrentLinkedQueue();
        this.th = 300000;
        this.sW = dVar;
        this.mContext = context;
        this.sH = fVar;
        this.sX = googleAnalytics;
        this.tg = new i() {
            public long currentTimeMillis() {
                return System.currentTimeMillis();
            }
        };
        this.sZ = 0;
        this.sU = a.tq;
    }

    private Timer a(Timer timer) {
        if (timer != null) {
            timer.cancel();
        }
        return null;
    }

    private synchronized void be() {
        if (this.sV != null && this.sU == a.tl) {
            this.sU = a.tp;
            this.sV.disconnect();
        }
    }

    private void co() {
        this.ta = a(this.ta);
        this.tb = a(this.tb);
        this.tc = a(this.tc);
    }

    private synchronized void cq() {
        if (Thread.currentThread().equals(this.sH.getThread())) {
            if (this.td) {
                bR();
            }
            d dVar;
            switch (AnonymousClass_3.tj[this.sU.ordinal()]) {
                case MMAdView.TRANSITION_FADE:
                    while (!this.sY.isEmpty()) {
                        dVar = (d) this.sY.poll();
                        aa.y("Sending hit to store  " + dVar);
                        this.sG.a(dVar.cv(), dVar.cw(), dVar.getPath(), dVar.cx());
                    }
                    if (this.sJ) {
                        cr();
                    }
                    break;
                case MMAdView.TRANSITION_UP:
                    while (!this.sY.isEmpty()) {
                        dVar = (d) this.sY.peek();
                        aa.y("Sending hit to service   " + dVar);
                        if (this.sX.isDryRunEnabled()) {
                            aa.y("Dry run enabled. Hit not actually sent to service.");
                        } else {
                            this.sV.a(dVar.cv(), dVar.cw(), dVar.getPath(), dVar.cx());
                        }
                        this.sY.poll();
                    }
                    this.sT = this.tg.currentTimeMillis();
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    aa.y("Need to reconnect");
                    if (!this.sY.isEmpty()) {
                        ct();
                    }
                    break;
                default:
                    break;
            }
        } else {
            this.sH.bZ().add(new Runnable() {
                public void run() {
                    s.this.cq();
                }
            });
        }
    }

    private void cr() {
        this.sG.bW();
        this.sJ = false;
    }

    private synchronized void cs() {
        if (this.sU != a.tm) {
            co();
            aa.y("falling back to local store");
            if (this.sW != null) {
                this.sG = this.sW;
            } else {
                r ci = r.ci();
                ci.a(this.mContext, this.sH);
                this.sG = ci.cl();
            }
            this.sU = a.tm;
            cq();
        }
    }

    private synchronized void ct() {
        if (this.tf || this.sV == null || this.sU == a.tm) {
            aa.z("client not initialized.");
            cs();
        } else {
            try {
                this.sZ++;
                a(this.tb);
                this.sU = a.tk;
                this.tb = new Timer("Failed Connect");
                this.tb.schedule(new c(null), 3000);
                aa.y("connecting to Analytics service");
                this.sV.connect();
            } catch (SecurityException e) {
                aa.z("security exception on connectToService");
                cs();
            }
        }
    }

    private void cu() {
        this.ta = a(this.ta);
        this.ta = new Timer("Service Reconnect");
        this.ta.schedule(new e(null), 5000);
    }

    public synchronized void a(int i, Intent intent) {
        this.sU = a.to;
        if (this.sZ < 2) {
            aa.z("Service unavailable (code=" + i + "), will retry.");
            cu();
        } else {
            aa.z("Service unavailable (code=" + i + "), using local store.");
            cs();
        }
    }

    public void b(Map<String, String> map, long j, String str, List<ef> list) {
        aa.y("putHit called");
        this.sY.add(new d(map, j, str, list));
        cq();
    }

    public void bR() {
        aa.y("clearHits called");
        this.sY.clear();
        switch (AnonymousClass_3.tj[this.sU.ordinal()]) {
            case MMAdView.TRANSITION_FADE:
                this.sG.j(0);
                this.td = false;
            case MMAdView.TRANSITION_UP:
                this.sV.bR();
                this.td = false;
            default:
                this.td = true;
        }
    }

    public void bW() {
        switch (AnonymousClass_3.tj[this.sU.ordinal()]) {
            case MMAdView.TRANSITION_FADE:
                cr();
            case MMAdView.TRANSITION_UP:
                break;
            default:
                this.sJ = true;
        }
    }

    public synchronized void bY() {
        if (!this.tf) {
            aa.y("setForceLocalDispatch called.");
            this.tf = true;
            switch (AnonymousClass_3.tj[this.sU.ordinal()]) {
                case MMAdView.TRANSITION_FADE:
                case MMAdView.TRANSITION_RANDOM:
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    break;
                case MMAdView.TRANSITION_UP:
                    be();
                    break;
                case MMAdView.TRANSITION_DOWN:
                    this.te = true;
                    break;
                default:
                    break;
            }
        }
    }

    public void cp() {
        if (this.sV == null) {
            this.sV = new c(this.mContext, this, this);
            ct();
        }
    }

    public synchronized void onConnected() {
        this.tb = a(this.tb);
        this.sZ = 0;
        aa.y("Connected to service");
        this.sU = a.tl;
        if (this.te) {
            be();
            this.te = false;
        } else {
            cq();
            this.tc = a(this.tc);
            this.tc = new Timer("disconnect check");
            this.tc.schedule(new b(null), this.th);
        }
    }

    public synchronized void onDisconnected() {
        if (this.sU == a.tp) {
            aa.y("Disconnected from service");
            co();
            this.sU = a.tq;
        } else {
            aa.y("Unexpected disconnect.");
            this.sU = a.to;
            if (this.sZ < 2) {
                cu();
            } else {
                cs();
            }
        }
    }
}