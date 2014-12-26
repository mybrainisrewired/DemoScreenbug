package com.inmobi.monetization.internal.configs;

import android.content.Context;
import com.inmobi.commons.InMobi;
import com.inmobi.commons.cache.CacheController;
import com.inmobi.commons.cache.CacheController.Validator;
import com.inmobi.commons.internal.InternalSDKUtil;
import com.inmobi.commons.internal.Log;
import com.inmobi.commons.uid.UID;
import com.inmobi.re.controller.util.Constants;
import java.util.HashMap;
import java.util.Map;

public class PkInitilaizer {
    public static final String PRODUCT_PK = "pk";
    private static Context a;
    private static Map<String, String> b;
    private static PkParams c;
    private static Validator d;

    static {
        a = null;
        b = new HashMap();
        c = new PkParams();
        d = new a();
    }

    private static void a(Context context) {
        b(context);
        try {
            CacheController.getConfig(PRODUCT_PK, context, b, d);
        } catch (Exception e) {
        }
    }

    static boolean a(Map<String, Object> map) {
        Log.internal("SK", "Saving config to map");
        b = getUidMap(a);
        try {
            PkParams pkParams = new PkParams();
            pkParams.setFromMap(map);
            c = pkParams;
            return true;
        } catch (Exception e) {
            Log.internal(Constants.RENDERING_LOG_TAG, "Config couldn't be parsed", e);
            return false;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void b(android.content.Context r3) {
        throw new UnsupportedOperationException("Method not decompiled: com.inmobi.monetization.internal.configs.PkInitilaizer.b(android.content.Context):void");
        /*
        if (r3 == 0) goto L_0x003b;
    L_0x0002:
        r0 = a;
        if (r0 != 0) goto L_0x003b;
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
        r0 = "pk";
        r1 = b;	 Catch:{ Exception -> 0x0039 }
        r2 = d;	 Catch:{ Exception -> 0x0039 }
        r0 = com.inmobi.commons.cache.CacheController.getConfig(r0, r3, r1, r2);	 Catch:{ Exception -> 0x0039 }
        r0 = r0.getData();	 Catch:{ Exception -> 0x0039 }
        if (r0 == 0) goto L_0x000a;
    L_0x0027:
        r0 = "pk";
        r1 = b;	 Catch:{ Exception -> 0x0039 }
        r2 = d;	 Catch:{ Exception -> 0x0039 }
        r0 = com.inmobi.commons.cache.CacheController.getConfig(r0, r3, r1, r2);	 Catch:{ Exception -> 0x0039 }
        r0 = r0.getData();	 Catch:{ Exception -> 0x0039 }
        a(r0);	 Catch:{ Exception -> 0x0039 }
        goto L_0x000a;
    L_0x0039:
        r0 = move-exception;
        goto L_0x000a;
    L_0x003b:
        r0 = a;
        if (r0 != 0) goto L_0x000a;
    L_0x003f:
        if (r3 != 0) goto L_0x000a;
    L_0x0041:
        r0 = new java.lang.NullPointerException;
        r0.<init>();
        throw r0;
        */
    }

    public static PkParams getConfigParams() {
        if (!(InternalSDKUtil.getContext() == null || InMobi.getAppId() == null)) {
            a(InternalSDKUtil.getContext());
        }
        return c;
    }

    public static Map<String, String> getUidMap(Context context) {
        return UID.getInstance().getMapForEncryption(null);
    }

    public static void initialize() {
        getConfigParams();
    }
}