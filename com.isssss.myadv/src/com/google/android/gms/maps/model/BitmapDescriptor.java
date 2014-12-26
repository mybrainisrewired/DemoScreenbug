package com.google.android.gms.maps.model;

import com.google.android.gms.dynamic.d;
import com.google.android.gms.internal.fq;

public final class BitmapDescriptor {
    private final d Rn;

    BitmapDescriptor(d remoteObject) {
        this.Rn = (d) fq.f(remoteObject);
    }

    public d id() {
        return this.Rn;
    }
}