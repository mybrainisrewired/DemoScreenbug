package com.inmobi.commons.analytics.bootstrapper;

import android.content.Context;
import com.inmobi.commons.InMobi;
import com.inmobi.commons.analytics.util.AnalyticsUtils;
import com.inmobi.commons.cache.CacheController;
import com.inmobi.commons.cache.CacheController.Validator;
import com.inmobi.commons.internal.InternalSDKUtil;
import com.inmobi.commons.internal.Log;
import com.inmobi.commons.internal.ThinICE;
import com.inmobi.commons.uid.UID;
import java.util.HashMap;
import java.util.Map;

public class AnalyticsInitializer {
    public static final String PRODUCT_ANALYTICS = "ltvp";
    private static Context a;
    private static Map<String, String> b;
    private static AnalyticsConfigParams c;
    private static Validator d;

    static {
        a = null;
        b = new HashMap();
        c = new AnalyticsConfigParams();
        d = new a();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void a(android.content.Context r4) {
        throw new UnsupportedOperationException("Method not decompiled: com.inmobi.commons.analytics.bootstrapper.AnalyticsInitializer.a(android.content.Context):void");
        /*
        if (r4 == 0) goto L_0x0056;
    L_0x0002:
        r0 = a;
        if (r0 != 0) goto L_0x0056;
    L_0x0006:
        r0 = a;
        if (r0 == 0) goto L_0x000b;
    L_0x000a:
        return;
    L_0x000b:
        r0 = r4.getApplicationContext();
        a = r0;
        r0 = getUidMap(r4);
        b = r0;
        r0 = "ltvp";
        r1 = b;	 Catch:{ CommonsException -> 0x002f, Exception -> 0x004d }
        r2 = d;	 Catch:{ CommonsException -> 0x002f, Exception -> 0x004d }
        r0 = com.inmobi.commons.cache.CacheController.getConfig(r0, r4, r1, r2);	 Catch:{ CommonsException -> 0x002f, Exception -> 0x004d }
        r1 = r0.getRawData();	 Catch:{ CommonsException -> 0x002f, Exception -> 0x004d }
        if (r1 == 0) goto L_0x000a;
    L_0x0027:
        r0 = r0.getData();	 Catch:{ CommonsException -> 0x002f, Exception -> 0x004d }
        b(r0);	 Catch:{ CommonsException -> 0x002f, Exception -> 0x004d }
        goto L_0x000a;
    L_0x002f:
        r0 = move-exception;
        r1 = "[InMobi]-[Analytics]-4.5.0";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Exception while retreiving configs due to commons Exception with code ";
        r2 = r2.append(r3);
        r0 = r0.getCode();
        r0 = r2.append(r0);
        r0 = r0.toString();
        com.inmobi.commons.internal.Log.internal(r1, r0);
        goto L_0x000a;
    L_0x004d:
        r0 = move-exception;
        r1 = "[InMobi]-[Analytics]-4.5.0";
        r2 = "Exception while retreiving configs.";
        com.inmobi.commons.internal.Log.internal(r1, r2, r0);
        goto L_0x000a;
    L_0x0056:
        r0 = a;
        if (r0 != 0) goto L_0x000a;
    L_0x005a:
        if (r4 != 0) goto L_0x000a;
    L_0x005c:
        r0 = a;
        r0.getApplicationContext();
        goto L_0x000a;
        */
    }

    private static void b(Context context) {
        a(context);
        b = getUidMap(a);
        try {
            CacheController.getConfig(PRODUCT_ANALYTICS, context, b, d);
        } catch (Exception e) {
        }
    }

    private static boolean b(Map<String, Object> map) {
        AnalyticsConfigParams analyticsConfigParams = new AnalyticsConfigParams();
        try {
            analyticsConfigParams.setFromMap((Map) map.get("common"));
            c = analyticsConfigParams;
            ThinICE.setConfig(analyticsConfigParams.getThinIceConfig());
            return true;
        } catch (Exception e) {
            Log.internal(AnalyticsUtils.ANALYTICS_LOGGING_TAG, "Exception while saving configs.", e);
            return false;
        }
    }

    public static AnalyticsConfigParams getConfigParams() {
        if (!(InternalSDKUtil.getContext() == null || InMobi.getAppId() == null)) {
            b(InternalSDKUtil.getContext());
        }
        return c;
    }

    public static AnalyticsConfigParams getRawConfigParams() {
        return c;
    }

    public static Map<String, String> getUidMap(Context context) {
        return UID.getInstance().getMapForEncryption(null);
    }
}