package com.inmobi.commons.analytics.iat.impl.config;

import android.content.Context;
import com.inmobi.commons.InMobi;
import com.inmobi.commons.cache.CacheController;
import com.inmobi.commons.cache.CacheController.Validator;
import com.inmobi.commons.internal.InternalSDKUtil;
import com.inmobi.commons.metric.Logger;
import com.inmobi.commons.uid.UID;
import java.util.Map;

public class AdTrackerInitializer {
    public static final String PRODUCT_IAT = "iat";
    private static Context a;
    private static Map<String, String> b;
    private static AdTrackerConfigParams c;
    private static Logger d;
    private static Validator e;

    static {
        a = null;
        c = new AdTrackerConfigParams();
        d = new Logger(2, PRODUCT_IAT);
        e = new a();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void a(android.content.Context r3) {
        throw new UnsupportedOperationException("Method not decompiled: com.inmobi.commons.analytics.iat.impl.config.AdTrackerInitializer.a(android.content.Context):void");
        /*
        if (r3 == 0) goto L_0x003e;
    L_0x0002:
        r0 = a;
        if (r0 != 0) goto L_0x003e;
    L_0x0006:
        r0 = a;
        if (r0 == 0) goto L_0x000b;
    L_0x000a:
        return;
    L_0x000b:
        r0 = r3.getApplicationContext();
        a = r0;
        r0 = com.inmobi.commons.uid.UID.getInstance();
        r1 = getConfigParams();
        r1 = r1.getDeviceIdMaskMap();
        r0 = r0.getMapForEncryption(r1);
        b = r0;
        r0 = "iat";
        r1 = b;	 Catch:{ Exception -> 0x0035 }
        r2 = e;	 Catch:{ Exception -> 0x0035 }
        r0 = com.inmobi.commons.cache.CacheController.getConfig(r0, r3, r1, r2);	 Catch:{ Exception -> 0x0035 }
        r0 = r0.getData();	 Catch:{ Exception -> 0x0035 }
        b(r0);	 Catch:{ Exception -> 0x0035 }
        goto L_0x000a;
    L_0x0035:
        r0 = move-exception;
        r0 = "[InMobi]-[AdTracker]-4.5.0";
        r1 = "Exception while retreiving configs.";
        com.inmobi.commons.internal.Log.internal(r0, r1);
        goto L_0x000a;
    L_0x003e:
        r0 = a;
        if (r0 != 0) goto L_0x000a;
    L_0x0042:
        if (r3 != 0) goto L_0x000a;
    L_0x0044:
        r0 = a;
        r0.getApplicationContext();
        goto L_0x000a;
        */
    }

    private static void b(Context context) {
        a(context);
        try {
            CacheController.getConfig(PRODUCT_IAT, context, b, e);
        } catch (Exception e) {
        }
    }

    private static boolean b(Map<String, Object> map) {
        b = getUidMap(a);
        Map populateToNewMap = InternalSDKUtil.populateToNewMap((Map) map.get("AND"), (Map) map.get("common"), true);
        try {
            AdTrackerConfigParams adTrackerConfigParams = new AdTrackerConfigParams();
            adTrackerConfigParams.setFromMap(populateToNewMap);
            c = adTrackerConfigParams;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static AdTrackerConfigParams getConfigParams() {
        if (!(InternalSDKUtil.getContext() == null || InMobi.getAppId() == null)) {
            b(InternalSDKUtil.getContext());
        }
        d.setMetricConfigParams(c.getMetric());
        return c;
    }

    public static Logger getLogger() {
        return d;
    }

    public static Map<String, String> getUidMap(Context context) {
        return UID.getInstance().getMapForEncryption(getConfigParams().getDeviceIdMaskMap());
    }
}