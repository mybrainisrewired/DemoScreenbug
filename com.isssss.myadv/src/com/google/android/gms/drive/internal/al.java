package com.google.android.gms.drive.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.a.d;

public class al extends c {
    private final d<Status> wH;

    public al(d<Status> dVar) {
        this.wH = dVar;
    }

    public void m(Status status) throws RemoteException {
        this.wH.b(status);
    }

    public void onSuccess() throws RemoteException {
        this.wH.b(Status.Bv);
    }
}