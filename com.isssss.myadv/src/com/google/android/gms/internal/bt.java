package com.google.android.gms.internal;

import com.google.android.gms.ads.mediation.MediationAdRequest;
import java.util.Date;
import java.util.Set;

public final class bt implements MediationAdRequest {
    private final Date d;
    private final Set<String> f;
    private final boolean g;
    private final int lZ;
    private final int nD;

    public bt(Date date, int i, Set<String> set, boolean z, int i2) {
        this.d = date;
        this.lZ = i;
        this.f = set;
        this.g = z;
        this.nD = i2;
    }

    public Date getBirthday() {
        return this.d;
    }

    public int getGender() {
        return this.lZ;
    }

    public Set<String> getKeywords() {
        return this.f;
    }

    public boolean isTesting() {
        return this.g;
    }

    public int taggedForChildDirectedTreatment() {
        return this.nD;
    }
}