package com.google.android.gms.internal;

import android.app.Activity;
import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.dynamic.c;
import com.google.android.gms.dynamic.e;
import com.google.android.gms.dynamic.g;
import com.google.android.gms.dynamic.g.a;
import com.google.android.gms.wallet.fragment.WalletFragmentOptions;

public class jh extends g<jc> {
    private static jh adc;

    protected jh() {
        super("com.google.android.gms.wallet.dynamite.WalletDynamiteCreatorImpl");
    }

    public static iz a(Activity activity, c cVar, WalletFragmentOptions walletFragmentOptions, ja jaVar) throws GooglePlayServicesNotAvailableException {
        int isGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (isGooglePlayServicesAvailable != 0) {
            throw new GooglePlayServicesNotAvailableException(isGooglePlayServicesAvailable);
        }
        try {
            return ((jc) lY().z(activity)).a(e.h(activity), cVar, walletFragmentOptions, jaVar);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (a e2) {
            throw new RuntimeException(e2);
        }
    }

    private static jh lY() {
        if (adc == null) {
            adc = new jh();
        }
        return adc;
    }

    protected jc aZ(IBinder iBinder) {
        return jc.a.aV(iBinder);
    }

    protected /* synthetic */ Object d(IBinder iBinder) {
        return aZ(iBinder);
    }
}