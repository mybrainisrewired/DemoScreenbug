package com.google.android.gms.internal;

import android.os.Bundle;
import android.os.RemoteException;
import com.google.ads.mediation.MediationServerParameters;
import com.google.android.gms.ads.mediation.MediationAdapter;
import com.google.android.gms.ads.mediation.NetworkExtras;
import com.google.android.gms.internal.bq.a;
import java.util.Map;

public final class bp extends a {
    private Map<Class<? extends NetworkExtras>, NetworkExtras> nB;
    private Map<Class<? extends MediationAdapter>, Bundle> nC;

    private <NETWORK_EXTRAS extends com.google.ads.mediation.NetworkExtras, SERVER_PARAMETERS extends MediationServerParameters> br n(String str) throws RemoteException {
        Class forName = Class.forName(str, false, bp.class.getClassLoader());
        if (com.google.ads.mediation.MediationAdapter.class.isAssignableFrom(forName)) {
            com.google.ads.mediation.MediationAdapter mediationAdapter = (com.google.ads.mediation.MediationAdapter) forName.newInstance();
            return new bw(mediationAdapter, (com.google.ads.mediation.NetworkExtras) this.nB.get(mediationAdapter.getAdditionalParametersType()));
        } else if (MediationAdapter.class.isAssignableFrom(forName)) {
            return new bu((MediationAdapter) forName.newInstance(), (Bundle) this.nC.get(forName));
        } else {
            dw.z("Could not instantiate mediation adapter: " + str + " (not a valid adapter).");
            throw new RemoteException();
        }
    }

    public void c(Map<Class<? extends NetworkExtras>, NetworkExtras> map) {
        this.nB = map;
    }

    public void d(Map<Class<? extends MediationAdapter>, Bundle> map) {
        this.nC = map;
    }

    public br m(String str) throws RemoteException {
        return n(str);
    }
}