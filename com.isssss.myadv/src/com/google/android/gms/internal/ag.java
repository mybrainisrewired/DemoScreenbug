package com.google.android.gms.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.dynamic.e;
import com.google.android.gms.dynamic.g;
import com.google.android.gms.internal.ap.a;

public final class ag extends g<aq> {
    private static final ag lG;

    static {
        lG = new ag();
    }

    private ag() {
        super("com.google.android.gms.ads.AdManagerCreatorImpl");
    }

    public static ap a(Context context, ak akVar, String str, bp bpVar) {
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) == 0) {
            ap b = lG.b(context, akVar, str, bpVar);
            if (b != null) {
                return b;
            }
        }
        dw.v("Using AdManager from the client jar.");
        return new v(context, akVar, str, bpVar, new dx(4452000, 4452000, true));
    }

    private ap b(Context context, ak akVar, String str, bp bpVar) {
        ap apVar = null;
        try {
            return a.f(((aq) z(context)).a(e.h(context), akVar, str, bpVar, GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE));
        } catch (RemoteException e) {
            dw.c("Could not create remote AdManager.", e);
            return apVar;
        } catch (g.a e2) {
            dw.c("Could not create remote AdManager.", e2);
            return apVar;
        }
    }

    protected aq c(IBinder iBinder) {
        return aq.a.g(iBinder);
    }

    protected /* synthetic */ Object d(IBinder iBinder) {
        return c(iBinder);
    }
}