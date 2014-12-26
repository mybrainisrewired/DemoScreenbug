package com.google.android.gms.internal;

import android.os.Bundle;
import android.os.SystemClock;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class di {
    private final Object li;
    private boolean pV;
    private final String qA;
    private long qB;
    private long qC;
    private long qD;
    private long qE;
    private long qF;
    private long qG;
    private final dj qx;
    private final LinkedList<a> qy;
    private final String qz;

    private static final class a {
        private long qH;
        private long qI;

        public a() {
            this.qH = -1;
            this.qI = -1;
        }

        public long bn() {
            return this.qI;
        }

        public void bo() {
            this.qI = SystemClock.elapsedRealtime();
        }

        public void bp() {
            this.qH = SystemClock.elapsedRealtime();
        }

        public Bundle toBundle() {
            Bundle bundle = new Bundle();
            bundle.putLong("topen", this.qH);
            bundle.putLong("tclose", this.qI);
            return bundle;
        }
    }

    public di(dj djVar, String str, String str2) {
        this.li = new Object();
        this.qB = -1;
        this.qC = -1;
        this.pV = false;
        this.qD = -1;
        this.qE = 0;
        this.qF = -1;
        this.qG = -1;
        this.qx = djVar;
        this.qz = str;
        this.qA = str2;
        this.qy = new LinkedList();
    }

    public di(String str, String str2) {
        this(dj.bq(), str, str2);
    }

    public void bk() {
        synchronized (this.li) {
            if (this.qG != -1 && this.qC == -1) {
                this.qC = SystemClock.elapsedRealtime();
                this.qx.a(this);
            }
            dj djVar = this.qx;
            dj.bu().bk();
        }
    }

    public void bl() {
        synchronized (this.li) {
            if (this.qG != -1) {
                a aVar = new a();
                aVar.bp();
                this.qy.add(aVar);
                this.qE++;
                dj djVar = this.qx;
                dj.bu().bl();
                this.qx.a(this);
            }
        }
    }

    public void bm() {
        synchronized (this.li) {
            if (!(this.qG == -1 || this.qy.isEmpty())) {
                a aVar = (a) this.qy.getLast();
                if (aVar.bn() == -1) {
                    aVar.bo();
                    this.qx.a(this);
                }
            }
        }
    }

    public void f(ah ahVar) {
        synchronized (this.li) {
            this.qF = SystemClock.elapsedRealtime();
            dj djVar = this.qx;
            dj.bu().b(ahVar, this.qF);
        }
    }

    public void h(long j) {
        synchronized (this.li) {
            this.qG = j;
            if (this.qG != -1) {
                this.qx.a(this);
            }
        }
    }

    public void i(long j) {
        synchronized (this.li) {
            if (this.qG != -1) {
                this.qB = j;
                this.qx.a(this);
            }
        }
    }

    public void m(boolean z) {
        synchronized (this.li) {
            if (this.qG != -1) {
                this.qD = SystemClock.elapsedRealtime();
                if (!z) {
                    this.qC = this.qD;
                    this.qx.a(this);
                }
            }
        }
    }

    public void n(boolean z) {
        synchronized (this.li) {
            if (this.qG != -1) {
                this.pV = z;
                this.qx.a(this);
            }
        }
    }

    public Bundle toBundle() {
        Bundle bundle;
        synchronized (this.li) {
            bundle = new Bundle();
            bundle.putString("seqnum", this.qz);
            bundle.putString("slotid", this.qA);
            bundle.putBoolean("ismediation", this.pV);
            bundle.putLong("treq", this.qF);
            bundle.putLong("tresponse", this.qG);
            bundle.putLong("timp", this.qC);
            bundle.putLong("tload", this.qD);
            bundle.putLong("pcc", this.qE);
            bundle.putLong("tfetch", this.qB);
            ArrayList arrayList = new ArrayList();
            Iterator it = this.qy.iterator();
            while (it.hasNext()) {
                arrayList.add(((a) it.next()).toBundle());
            }
            bundle.putParcelableArrayList("tclick", arrayList);
        }
        return bundle;
    }
}