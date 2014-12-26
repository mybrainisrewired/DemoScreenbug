package com.google.android.gms.wearable;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.common.data.d;
import com.google.android.gms.internal.kc;

public class b extends d<a> implements Result {
    private final Status wJ;

    public b(DataHolder dataHolder) {
        super(dataHolder);
        this.wJ = new Status(dataHolder.getStatusCode());
    }

    protected /* synthetic */ Object c(int i, int i2) {
        return g(i, i2);
    }

    protected a g(int i, int i2) {
        return new kc(this.BB, i, i2);
    }

    protected String getPrimaryDataMarkerColumn() {
        return "path";
    }

    public Status getStatus() {
        return this.wJ;
    }
}