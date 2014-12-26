package com.inmobi.monetization.internal.configs;

import android.content.Context;
import com.inmobi.commons.InMobi;
import com.inmobi.commons.cache.CacheController;
import com.inmobi.commons.cache.CacheController.Validator;
import com.inmobi.commons.internal.InternalSDKUtil;
import com.inmobi.commons.internal.Log;
import com.inmobi.commons.metric.Logger;
import com.inmobi.commons.uid.UID;
import com.inmobi.monetization.internal.Constants;
import java.util.HashMap;
import java.util.Map;

public class Initializer {
    public static final String PRODUCT_ADNETWORK = "adNetwork";
    private static Context a;
    private static Map<String, String> b;
    private static Logger c;
    private static ConfigParams d;
    private static Validator e;

    static {
        a = null;
        b = new HashMap();
        c = new Logger(1, "network");
        d = new ConfigParams();
        e = new b();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void a(android.content.Context r3) {
        throw new UnsupportedOperationException("Method not decompiled: com.inmobi.monetization.internal.configs.Initializer.a(android.content.Context):void");
        /*
        if (r3 == 0) goto L_0x002b;
    L_0x0002:
        r0 = a;
        if (r0 != 0) goto L_0x002b;
    L_0x0006:
        r0 = a;
        if (r0 == 0) goto L_0x000b;
    L_0x000a:
        return;
    L_0x000b:
        r0 = r3.getApplicationContext();
        a = r0;
        r0 = getUidMap(r3);
        b = r0;
        r0 = "adNetwork";
        r1 = b;	 Catch:{ Exception -> 0x0029 }
        r2 = e;	 Catch:{ Exception -> 0x0029 }
        r0 = com.inmobi.commons.cache.CacheController.getConfig(r0, r3, r1, r2);	 Catch:{ Exception -> 0x0029 }
        r0 = r0.getData();	 Catch:{ Exception -> 0x0029 }
        b(r0);	 Catch:{ Exception -> 0x0029 }
        goto L_0x000a;
    L_0x0029:
        r0 = move-exception;
        goto L_0x000a;
    L_0x002b:
        r0 = a;
        if (r0 != 0) goto L_0x000a;
    L_0x002f:
        if (r3 != 0) goto L_0x000a;
    L_0x0031:
        r0 = new java.lang.NullPointerException;
        r0.<init>();
        throw r0;
        */
    }

    private static void b(Context context) {
        a(context);
        try {
            CacheController.getConfig(PRODUCT_ADNETWORK, context, b, e);
        } catch (Exception e) {
        }
    }

    private static boolean b(Map<String, Object> map) {
        b = getUidMap(a);
        try {
            Map populateToNewMap = InternalSDKUtil.populateToNewMap((Map) map.get("AND"), (Map) map.get("common"), true);
            ConfigParams configParams = new ConfigParams();
            configParams.setFromMap(populateToNewMap);
            d = configParams;
            c.setMetricConfigParams(configParams.getMetric());
            return true;
        } catch (Exception e) {
            Log.internal(Constants.LOG_TAG, "Config couldn't be parsed", e);
            return false;
        }
    }

    public static ConfigParams getConfigParams() {
        if (!(InternalSDKUtil.getContext() == null || InMobi.getAppId() == null)) {
            b(InternalSDKUtil.getContext());
        }
        return d;
    }

    public static Logger getLogger() {
        return c;
    }

    public static Map<String, String> getUidMap(Context context) {
        return UID.getInstance().getMapForEncryption(d.getDeviceIdMaskMap());
    }
}