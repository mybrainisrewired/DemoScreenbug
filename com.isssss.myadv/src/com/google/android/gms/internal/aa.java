package com.google.android.gms.internal;

import java.util.ArrayList;
import java.util.WeakHashMap;

public final class aa implements ac {
    private final Object li;
    private WeakHashMap<dh, ab> lj;
    private ArrayList<ab> lk;

    public aa() {
        this.li = new Object();
        this.lj = new WeakHashMap();
        this.lk = new ArrayList();
    }

    public ab a(ak akVar, dh dhVar) {
        ab abVar;
        synchronized (this.li) {
            if (c(dhVar)) {
                abVar = (ab) this.lj.get(dhVar);
            } else {
                abVar = new ab(akVar, dhVar);
                abVar.a(this);
                this.lj.put(dhVar, abVar);
                this.lk.add(abVar);
            }
        }
        return abVar;
    }

    public void a(ab abVar) {
        synchronized (this.li) {
            if (!abVar.at()) {
                this.lk.remove(abVar);
            }
        }
    }

    public boolean c(dh dhVar) {
        boolean z;
        synchronized (this.li) {
            ab abVar = (ab) this.lj.get(dhVar);
            z = abVar != null && abVar.at();
        }
        return z;
    }

    public void d(dh dhVar) {
        synchronized (this.li) {
            ab abVar = (ab) this.lj.get(dhVar);
            if (abVar != null) {
                abVar.ar();
            }
        }
    }
}