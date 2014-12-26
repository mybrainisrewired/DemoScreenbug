package com.google.android.gms.internal;

import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.common.data.b;
import com.google.android.gms.wearable.d;

public class ke extends b implements d {
    public ke(DataHolder dataHolder, int i) {
        super(dataHolder, i);
    }

    public /* synthetic */ Object freeze() {
        return mf();
    }

    public String getId() {
        return getString("asset_id");
    }

    public String mc() {
        return getString("asset_key");
    }

    public d mf() {
        return new kd(this);
    }
}