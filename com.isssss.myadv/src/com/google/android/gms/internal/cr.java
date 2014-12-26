package com.google.android.gms.internal;

import android.content.Context;

public final class cr {

    public static interface a {
        void a(dh dhVar);
    }

    public static do_ a(Context context, com.google.android.gms.internal.cx.a aVar, l lVar, dz dzVar, bq bqVar, a aVar2) {
        do_ csVar = new cs(context, aVar, lVar, dzVar, bqVar, aVar2);
        csVar.start();
        return csVar;
    }
}