package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.internal.db.a;
import com.google.android.gms.internal.ff.e;

public class cw extends ff<db> {
    final int pe;

    public cw(Context context, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener, int i) {
        super(context, connectionCallbacks, onConnectionFailedListener, new String[0]);
        this.pe = i;
    }

    protected void a(fm fmVar, e eVar) throws RemoteException {
        fmVar.g(eVar, this.pe, getContext().getPackageName(), new Bundle());
    }

    protected String bg() {
        return "com.google.android.gms.ads.service.START";
    }

    protected String bh() {
        return "com.google.android.gms.ads.internal.request.IAdRequestService";
    }

    public db bi() {
        return (db) super.eM();
    }

    protected db q(IBinder iBinder) {
        return a.s(iBinder);
    }

    protected /* synthetic */ IInterface r(IBinder iBinder) {
        return q(iBinder);
    }
}