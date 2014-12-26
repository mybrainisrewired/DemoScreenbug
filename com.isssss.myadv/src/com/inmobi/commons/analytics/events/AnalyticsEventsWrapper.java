package com.inmobi.commons.analytics.events;

import com.inmobi.commons.InMobi;
import com.inmobi.commons.analytics.db.AnalyticsEventsQueue;
import com.inmobi.commons.analytics.db.FunctionSetUserAttribute;
import com.inmobi.commons.analytics.net.AnalyticsNetworkManager;
import com.inmobi.commons.analytics.util.AnalyticsUtils;
import com.inmobi.commons.analytics.util.SessionInfo;
import com.inmobi.commons.internal.InternalSDKUtil;
import com.inmobi.commons.internal.Log;
import com.mopub.common.Preconditions;

public final class AnalyticsEventsWrapper {
    private static AnalyticsEventsWrapper a;
    private static boolean c;
    private AnalyticsEventsQueue b;

    public enum IMItemType {
        CONSUMABLE,
        DURABLE,
        PERSONALIZATION;

        static {
            CONSUMABLE = new com.inmobi.commons.analytics.events.AnalyticsEventsWrapper.IMItemType("CONSUMABLE", 0);
            DURABLE = new com.inmobi.commons.analytics.events.AnalyticsEventsWrapper.IMItemType("DURABLE", 1);
            PERSONALIZATION = new com.inmobi.commons.analytics.events.AnalyticsEventsWrapper.IMItemType("PERSONALIZATION", 2);
            a = new com.inmobi.commons.analytics.events.AnalyticsEventsWrapper.IMItemType[]{CONSUMABLE, DURABLE, PERSONALIZATION};
        }
    }

    public enum IMSectionStatus {
        COMPLETED,
        FAILED,
        CANCELED;

        static {
            COMPLETED = new com.inmobi.commons.analytics.events.AnalyticsEventsWrapper.IMSectionStatus("COMPLETED", 0);
            FAILED = new com.inmobi.commons.analytics.events.AnalyticsEventsWrapper.IMSectionStatus("FAILED", 1);
            CANCELED = new com.inmobi.commons.analytics.events.AnalyticsEventsWrapper.IMSectionStatus("CANCELED", 2);
            a = new com.inmobi.commons.analytics.events.AnalyticsEventsWrapper.IMSectionStatus[]{COMPLETED, FAILED, CANCELED};
        }
    }

    static {
        c = false;
    }

    private AnalyticsEventsWrapper() {
    }

    private void a(String str) {
        Log.debug(AnalyticsUtils.ANALYTICS_LOGGING_TAG, "IllegalArgumentError: " + str);
    }

    private boolean a() {
        if (InternalSDKUtil.getContext() != null && SessionInfo.getSessionId(InternalSDKUtil.getContext()) == null) {
            startSession(InMobi.getAppId(), null);
        } else if (SessionInfo.getSessionId(InternalSDKUtil.getContext()) == null) {
            Log.internal(AnalyticsUtils.ANALYTICS_LOGGING_TAG, AnalyticsUtils.INITIALIZE_NOT_CALLED);
            return false;
        }
        return true;
    }

    public static synchronized AnalyticsEventsWrapper getInstance() {
        AnalyticsEventsWrapper analyticsEventsWrapper;
        synchronized (AnalyticsEventsWrapper.class) {
            if (AnalyticsUtils.getWebviewUserAgent() == null) {
                AnalyticsUtils.setWebviewUserAgent(InternalSDKUtil.getUserAgent());
            }
            if (a == null) {
                a = new AnalyticsEventsWrapper();
                AnalyticsUtils.setStartHandle(false);
                AnalyticsNetworkManager.startInstance();
            }
            a.b = AnalyticsEventsQueue.getInstance();
            analyticsEventsWrapper = a;
        }
        return analyticsEventsWrapper;
    }

    public static boolean isEventsUser() {
        return c;
    }

    public static void setIsEventsUser() {
        c = true;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void beginSection(int r4, java.lang.String r5, java.util.Map<java.lang.String, java.lang.String> r6) {
        throw new UnsupportedOperationException("Method not decompiled: com.inmobi.commons.analytics.events.AnalyticsEventsWrapper.beginSection(int, java.lang.String, java.util.Map):void");
        /*
        r3 = this;
        if (r5 != 0) goto L_0x0008;
    L_0x0002:
        r0 = "arguments cannot be null";
        r3.a(r0);	 Catch:{ Exception -> 0x0018 }
    L_0x0007:
        return;
    L_0x0008:
        if (r6 == 0) goto L_0x0021;
    L_0x000a:
        r0 = r6.size();	 Catch:{ Exception -> 0x0018 }
        r1 = 10;
        if (r0 <= r1) goto L_0x0021;
    L_0x0012:
        r0 = "attribute map cannot exceed 10 values";
        r3.a(r0);	 Catch:{ Exception -> 0x0018 }
        goto L_0x0007;
    L_0x0018:
        r0 = move-exception;
        r1 = "[InMobi]-[Analytics]-4.5.0";
        r2 = "Begin Section Exception";
        com.inmobi.commons.internal.Log.internal(r1, r2, r0);
        goto L_0x0007;
    L_0x0021:
        r0 = r3.a();	 Catch:{ Exception -> 0x0018 }
        if (r0 == 0) goto L_0x0007;
    L_0x0027:
        r0 = new com.inmobi.commons.analytics.db.FunctionLevelBegin;	 Catch:{ Exception -> 0x0018 }
        r1 = com.inmobi.commons.internal.InternalSDKUtil.getContext();	 Catch:{ Exception -> 0x0018 }
        r0.<init>(r1, r4, r5, r6);	 Catch:{ Exception -> 0x0018 }
        r1 = r3.b;	 Catch:{ Exception -> 0x0018 }
        r1.addElement(r0);	 Catch:{ Exception -> 0x0018 }
        r0 = r3.b;	 Catch:{ Exception -> 0x0018 }
        r0.processFunctions();	 Catch:{ Exception -> 0x0018 }
        goto L_0x0007;
        */
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void endSection(int r7, java.lang.String r8, java.util.Map<java.lang.String, java.lang.String> r9) {
        throw new UnsupportedOperationException("Method not decompiled: com.inmobi.commons.analytics.events.AnalyticsEventsWrapper.endSection(int, java.lang.String, java.util.Map):void");
        /*
        r6 = this;
        if (r8 != 0) goto L_0x0008;
    L_0x0002:
        r0 = "arguments cannot be null";
        r6.a(r0);	 Catch:{ Exception -> 0x0018 }
    L_0x0007:
        return;
    L_0x0008:
        if (r9 == 0) goto L_0x0021;
    L_0x000a:
        r0 = r9.size();	 Catch:{ Exception -> 0x0018 }
        r1 = 10;
        if (r0 <= r1) goto L_0x0021;
    L_0x0012:
        r0 = "attribute map cannot exceed 10 values";
        r6.a(r0);	 Catch:{ Exception -> 0x0018 }
        goto L_0x0007;
    L_0x0018:
        r0 = move-exception;
        r1 = "[InMobi]-[Analytics]-4.5.0";
        r2 = "End Section Exception";
        com.inmobi.commons.internal.Log.internal(r1, r2, r0);
        goto L_0x0007;
    L_0x0021:
        r0 = r6.a();	 Catch:{ Exception -> 0x0018 }
        if (r0 == 0) goto L_0x0007;
    L_0x0027:
        r0 = new com.inmobi.commons.analytics.db.FunctionLevelEnd;	 Catch:{ Exception -> 0x0018 }
        r1 = com.inmobi.commons.internal.InternalSDKUtil.getContext();	 Catch:{ Exception -> 0x0018 }
        r4 = 0;
        r2 = r7;
        r3 = r8;
        r5 = r9;
        r0.<init>(r1, r2, r3, r4, r5);	 Catch:{ Exception -> 0x0018 }
        r1 = r6.b;	 Catch:{ Exception -> 0x0018 }
        r1.addElement(r0);	 Catch:{ Exception -> 0x0018 }
        r0 = r6.b;	 Catch:{ Exception -> 0x0018 }
        r0.processFunctions();	 Catch:{ Exception -> 0x0018 }
        goto L_0x0007;
        */
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void endSession(java.util.Map<java.lang.String, java.lang.String> r4) {
        throw new UnsupportedOperationException("Method not decompiled: com.inmobi.commons.analytics.events.AnalyticsEventsWrapper.endSession(java.util.Map):void");
        /*
        r3 = this;
        if (r4 == 0) goto L_0x0010;
    L_0x0002:
        r0 = r4.size();	 Catch:{ Exception -> 0x0024 }
        r1 = 10;
        if (r0 <= r1) goto L_0x0010;
    L_0x000a:
        r0 = "attribute map cannot exceed 10 values";
        r3.a(r0);	 Catch:{ Exception -> 0x0024 }
    L_0x000f:
        return;
    L_0x0010:
        r0 = new com.inmobi.commons.analytics.db.FunctionEndSession;	 Catch:{ Exception -> 0x0024 }
        r1 = com.inmobi.commons.internal.InternalSDKUtil.getContext();	 Catch:{ Exception -> 0x0024 }
        r0.<init>(r1, r4);	 Catch:{ Exception -> 0x0024 }
        r1 = r3.b;	 Catch:{ Exception -> 0x0024 }
        r1.addElement(r0);	 Catch:{ Exception -> 0x0024 }
        r0 = r3.b;	 Catch:{ Exception -> 0x0024 }
        r0.processFunctions();	 Catch:{ Exception -> 0x0024 }
        goto L_0x000f;
    L_0x0024:
        r0 = move-exception;
        r1 = "[InMobi]-[Analytics]-4.5.0";
        r2 = "End Session Exception";
        com.inmobi.commons.internal.Log.internal(r1, r2, r0);
        goto L_0x000f;
        */
    }

    public void setUserAttribute(String str, String str2) {
        if (str == null || str.trim().equals(Preconditions.EMPTY_ARGUMENTS) || str2 == null || str2.trim().equals(Preconditions.EMPTY_ARGUMENTS)) {
            a("arguments cannot be null or empty");
        } else if (str.length() > 15 || str2.length() > 20) {
            a("attribute name cannot exceed 15 chars and attribute val cannot exceed 20 chars. Please pass a valid attribute");
        } else {
            try {
                if (a()) {
                    this.b.addElement(new FunctionSetUserAttribute(InternalSDKUtil.getContext(), str, str2));
                    this.b.processFunctions();
                }
            } catch (Exception e) {
                Log.internal(AnalyticsUtils.ANALYTICS_LOGGING_TAG, "Set User Attribute Exception", e);
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void startSession(java.lang.String r4, java.util.Map<java.lang.String, java.lang.String> r5) {
        throw new UnsupportedOperationException("Method not decompiled: com.inmobi.commons.analytics.events.AnalyticsEventsWrapper.startSession(java.lang.String, java.util.Map):void");
        /*
        r3 = this;
        if (r4 == 0) goto L_0x000e;
    L_0x0002:
        r0 = r4.trim();	 Catch:{ Exception -> 0x0024 }
        r1 = "";
        r0 = r0.equals(r1);	 Catch:{ Exception -> 0x0024 }
        if (r0 == 0) goto L_0x0014;
    L_0x000e:
        r0 = "appid cannot be null or empty";
        r3.a(r0);	 Catch:{ Exception -> 0x0024 }
    L_0x0013:
        return;
    L_0x0014:
        if (r5 == 0) goto L_0x002d;
    L_0x0016:
        r0 = r5.size();	 Catch:{ Exception -> 0x0024 }
        r1 = 10;
        if (r0 <= r1) goto L_0x002d;
    L_0x001e:
        r0 = "attribute map cannot exceed 10 values";
        r3.a(r0);	 Catch:{ Exception -> 0x0024 }
        goto L_0x0013;
    L_0x0024:
        r0 = move-exception;
        r1 = "[InMobi]-[Analytics]-4.5.0";
        r2 = "Init exception";
        com.inmobi.commons.internal.Log.internal(r1, r2, r0);
        goto L_0x0013;
    L_0x002d:
        r0 = new com.inmobi.commons.analytics.db.FunctionStartSession;	 Catch:{ Exception -> 0x0024 }
        r1 = com.inmobi.commons.internal.InternalSDKUtil.getContext();	 Catch:{ Exception -> 0x0024 }
        r0.<init>(r1, r4, r5);	 Catch:{ Exception -> 0x0024 }
        r1 = r3.b;	 Catch:{ Exception -> 0x0024 }
        r1.addElement(r0);	 Catch:{ Exception -> 0x0024 }
        r0 = r3.b;	 Catch:{ Exception -> 0x0024 }
        r0.processFunctions();	 Catch:{ Exception -> 0x0024 }
        goto L_0x0013;
        */
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void tagEvent(java.lang.String r4, java.util.Map<java.lang.String, java.lang.String> r5) {
        throw new UnsupportedOperationException("Method not decompiled: com.inmobi.commons.analytics.events.AnalyticsEventsWrapper.tagEvent(java.lang.String, java.util.Map):void");
        /*
        r3 = this;
        if (r4 == 0) goto L_0x000e;
    L_0x0002:
        r0 = r4.trim();	 Catch:{ Exception -> 0x0024 }
        r1 = "";
        r0 = r0.equals(r1);	 Catch:{ Exception -> 0x0024 }
        if (r0 == 0) goto L_0x0014;
    L_0x000e:
        r0 = "arguments cannot be null or empty";
        r3.a(r0);	 Catch:{ Exception -> 0x0024 }
    L_0x0013:
        return;
    L_0x0014:
        if (r5 == 0) goto L_0x002d;
    L_0x0016:
        r0 = r5.size();	 Catch:{ Exception -> 0x0024 }
        r1 = 10;
        if (r0 <= r1) goto L_0x002d;
    L_0x001e:
        r0 = "attribute map cannot exceed 10 values";
        r3.a(r0);	 Catch:{ Exception -> 0x0024 }
        goto L_0x0013;
    L_0x0024:
        r0 = move-exception;
        r1 = "[InMobi]-[Analytics]-4.5.0";
        r2 = "Tag Event Exception";
        com.inmobi.commons.internal.Log.internal(r1, r2, r0);
        goto L_0x0013;
    L_0x002d:
        r0 = r3.a();	 Catch:{ Exception -> 0x0024 }
        if (r0 == 0) goto L_0x0013;
    L_0x0033:
        r0 = new com.inmobi.commons.analytics.db.FunctionTagEvent;	 Catch:{ Exception -> 0x0024 }
        r1 = com.inmobi.commons.internal.InternalSDKUtil.getContext();	 Catch:{ Exception -> 0x0024 }
        r0.<init>(r1, r4, r5);	 Catch:{ Exception -> 0x0024 }
        r1 = r3.b;	 Catch:{ Exception -> 0x0024 }
        r1.addElement(r0);	 Catch:{ Exception -> 0x0024 }
        r0 = r3.b;	 Catch:{ Exception -> 0x0024 }
        r0.processFunctions();	 Catch:{ Exception -> 0x0024 }
        goto L_0x0013;
        */
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void tagTransactionManually(android.content.Intent r4, android.os.Bundle r5) {
        throw new UnsupportedOperationException("Method not decompiled: com.inmobi.commons.analytics.events.AnalyticsEventsWrapper.tagTransactionManually(android.content.Intent, android.os.Bundle):void");
        /*
        r3 = this;
        if (r4 != 0) goto L_0x0008;
    L_0x0002:
        r0 = "transaction intent cannot be null or empty";
        r3.a(r0);	 Catch:{ Exception -> 0x0022 }
    L_0x0007:
        return;
    L_0x0008:
        r0 = r3.a();	 Catch:{ Exception -> 0x0022 }
        if (r0 == 0) goto L_0x0007;
    L_0x000e:
        r0 = new com.inmobi.commons.analytics.db.FunctionTagTransaction;	 Catch:{ Exception -> 0x0022 }
        r1 = com.inmobi.commons.internal.InternalSDKUtil.getContext();	 Catch:{ Exception -> 0x0022 }
        r0.<init>(r1, r4, r5);	 Catch:{ Exception -> 0x0022 }
        r1 = r3.b;	 Catch:{ Exception -> 0x0022 }
        r1.addElement(r0);	 Catch:{ Exception -> 0x0022 }
        r0 = r3.b;	 Catch:{ Exception -> 0x0022 }
        r0.processFunctions();	 Catch:{ Exception -> 0x0022 }
        goto L_0x0007;
    L_0x0022:
        r0 = move-exception;
        r1 = "[InMobi]-[Analytics]-4.5.0";
        r2 = "Tag Transaction Manually Exception";
        com.inmobi.commons.internal.Log.internal(r1, r2, r0);
        goto L_0x0007;
        */
    }
}