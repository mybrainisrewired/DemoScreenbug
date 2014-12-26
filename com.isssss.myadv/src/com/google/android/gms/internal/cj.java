package com.google.android.gms.internal;

import android.app.Activity;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.gms.dynamic.e;
import com.google.android.gms.dynamic.g;

public final class cj extends g<cl> {
    private static final cj oC;

    private static final class a extends Exception {
        public a(String str) {
            super(str);
        }
    }

    static {
        oC = new cj();
    }

    private cj() {
        super("com.google.android.gms.ads.AdOverlayCreatorImpl");
    }

    public static ck a(Activity activity) {
        try {
            if (!b(activity)) {
                return oC.c(activity);
            }
            dw.v("Using AdOverlay from the client jar.");
            return new cc(activity);
        } catch (a e) {
            dw.z(e.getMessage());
            return null;
        }
    }

    private static boolean b(Activity activity) throws a {
        Intent intent = activity.getIntent();
        if (intent.hasExtra("com.google.android.gms.ads.internal.overlay.useClientJar")) {
            return intent.getBooleanExtra("com.google.android.gms.ads.internal.overlay.useClientJar", false);
        }
        throw new a("Ad overlay requires the useClientJar flag in intent extras.");
    }

    private ck c(Activity activity) {
        ck ckVar = null;
        try {
            return com.google.android.gms.internal.ck.a.m(((cl) z(activity)).a(e.h(activity)));
        } catch (RemoteException e) {
            dw.c("Could not create remote AdOverlay.", e);
            return ckVar;
        } catch (com.google.android.gms.dynamic.g.a e2) {
            dw.c("Could not create remote AdOverlay.", e2);
            return ckVar;
        }
    }

    protected /* synthetic */ Object d(IBinder iBinder) {
        return l(iBinder);
    }

    protected cl l(IBinder iBinder) {
        return com.google.android.gms.internal.cl.a.n(iBinder);
    }
}