package com.google.android.gms.internal;

import com.google.android.gms.wearable.d;

public class kd implements d {
    private final String Xy;
    private final String wp;

    public kd(d dVar) {
        this.wp = dVar.getId();
        this.Xy = dVar.mc();
    }

    public /* synthetic */ Object freeze() {
        return mf();
    }

    public String getId() {
        return this.wp;
    }

    public boolean isDataValid() {
        return true;
    }

    public String mc() {
        return this.Xy;
    }

    public d mf() {
        return this;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DataItemAssetEntity[");
        stringBuilder.append("@");
        stringBuilder.append(Integer.toHexString(hashCode()));
        if (this.wp == null) {
            stringBuilder.append(",noid");
        } else {
            stringBuilder.append(",");
            stringBuilder.append(this.wp);
        }
        stringBuilder.append(", key=");
        stringBuilder.append(this.Xy);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}