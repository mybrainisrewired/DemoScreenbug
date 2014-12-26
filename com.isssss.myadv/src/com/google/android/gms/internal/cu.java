package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.cv.b;

public final class cu {

    public static interface a {
        void a(cz czVar);
    }

    public static do_ a(Context context, cx cxVar, a aVar) {
        return cxVar.kK.rt ? b(context, cxVar, aVar) : c(context, cxVar, aVar);
    }

    private static do_ b(Context context, cx cxVar, a aVar) {
        dw.v("Fetching ad response from local ad request service.");
        do_ aVar2 = new com.google.android.gms.internal.cv.a(context, cxVar, aVar);
        aVar2.start();
        return aVar2;
    }

    private static do_ c(Context context, cx cxVar, a aVar) {
        dw.v("Fetching ad response from remote ad request service.");
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) == 0) {
            return new b(context, cxVar, aVar);
        }
        dw.z("Failed to connect to remote ad request service.");
        return null;
    }
}