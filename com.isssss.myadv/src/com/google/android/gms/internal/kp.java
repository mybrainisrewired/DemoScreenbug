package com.google.android.gms.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class kp<M extends kp<M>> extends kt {
    protected List<kv> adU;

    public final <T> T a(kq<M, T> kqVar) {
        return kqVar.f(this.adU);
    }

    public void a(ko koVar) throws IOException {
        int size = this.adU == null ? 0 : this.adU.size();
        int i = 0;
        while (i < size) {
            kv kvVar = (kv) this.adU.get(i);
            koVar.da(kvVar.tag);
            koVar.p(kvVar.adZ);
            i++;
        }
    }

    protected final boolean a(kn knVar, int i) throws IOException {
        int position = knVar.getPosition();
        if (!knVar.cQ(i)) {
            return false;
        }
        if (this.adU == null) {
            this.adU = new ArrayList();
        }
        this.adU.add(new kv(i, knVar.h(position, knVar.getPosition() - position)));
        return true;
    }

    protected int mx() {
        int i = 0;
        int i2 = 0;
        while (i < (this.adU == null ? 0 : this.adU.size())) {
            kv kvVar = (kv) this.adU.get(i);
            i2 = i2 + ko.db(kvVar.tag) + kvVar.adZ.length;
            i++;
        }
        return i2;
    }
}