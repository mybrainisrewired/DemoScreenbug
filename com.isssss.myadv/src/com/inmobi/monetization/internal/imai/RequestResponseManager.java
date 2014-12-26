package com.inmobi.monetization.internal.imai;

import android.content.Context;
import android.os.Handler;
import com.inmobi.commons.analytics.net.AnalyticsCommon.HttpRequestCallback;
import com.inmobi.commons.internal.InternalSDKUtil;
import com.inmobi.commons.internal.Log;
import com.inmobi.monetization.internal.Constants;
import com.inmobi.monetization.internal.configs.Initializer;
import com.mopub.common.Preconditions;
import java.util.concurrent.atomic.AtomicBoolean;

public final class RequestResponseManager {
    static Thread a;
    static Handler b;
    static AtomicBoolean c;
    private static AtomicBoolean g;
    private static AtomicBoolean i;
    public static AtomicBoolean isSynced;
    public static IMAIClickEventList mDBWriterQueue;
    public static IMAIClickEventList mNetworkQueue;
    String d;
    String e;
    String f;
    private WebviewLoader h;

    class a implements Runnable {
        final /* synthetic */ Context a;
        final /* synthetic */ HttpRequestCallback b;

        a(Context context, HttpRequestCallback httpRequestCallback) {
            this.a = context;
            this.b = httpRequestCallback;
        }

        public void run() {
            throw new UnsupportedOperationException("Method not decompiled: com.inmobi.monetization.internal.imai.RequestResponseManager.a.run():void");
            /* JADX: method processing error */
/*
            Error: jadx.core.utils.exceptions.JadxOverflowException: Regions stack size limit reached
	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:42)
	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:62)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:29)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:16)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
	at jadx.core.ProcessClass.process(ProcessClass.java:22)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:209)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:133)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1145)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615)
	at java.lang.Thread.run(Thread.java:745)
*/
            /*
            r11 = this;
            r10 = 1;
        L_0x0001:
            r0 = isSynced;	 Catch:{ Exception -> 0x00a2 }
            r0 = r0.get();	 Catch:{ Exception -> 0x00a2 }
            if (r0 != 0) goto L_0x0204;
        L_0x0009:
            r0 = isSynced;	 Catch:{ Exception -> 0x00a2 }
            r1 = 1;
            r0.set(r1);	 Catch:{ Exception -> 0x00a2 }
            r0 = mNetworkQueue;	 Catch:{ Exception -> 0x00a2 }
            if (r0 == 0) goto L_0x001b;
        L_0x0013:
            r0 = mNetworkQueue;	 Catch:{ Exception -> 0x00a2 }
            r0 = r0.isEmpty();	 Catch:{ Exception -> 0x00a2 }
            if (r0 == 0) goto L_0x0040;
        L_0x001b:
            r0 = mDBWriterQueue;	 Catch:{ Exception -> 0x00a2 }
            if (r0 != 0) goto L_0x0034;
        L_0x001f:
            r0 = mDBWriterQueue;	 Catch:{ Exception -> 0x00a2 }
            r0 = r0.isEmpty();	 Catch:{ Exception -> 0x00a2 }
            if (r0 == 0) goto L_0x0034;
        L_0x0027:
            r0 = "[InMobi]-[Monetization]";
            r1 = "Click event list empty";
            com.inmobi.commons.internal.Log.internal(r0, r1);	 Catch:{ Exception -> 0x00a2 }
            r0 = com.inmobi.monetization.internal.imai.RequestResponseManager.this;	 Catch:{ Exception -> 0x00a2 }
            r0.deinit();	 Catch:{ Exception -> 0x00a2 }
        L_0x0033:
            return;
        L_0x0034:
            r0 = mNetworkQueue;	 Catch:{ Exception -> 0x00a2 }
            r1 = mDBWriterQueue;	 Catch:{ Exception -> 0x00a2 }
            r0.addAll(r1);	 Catch:{ Exception -> 0x00a2 }
            r0 = mDBWriterQueue;	 Catch:{ Exception -> 0x00a2 }
            r0.clear();	 Catch:{ Exception -> 0x00a2 }
        L_0x0040:
            r0 = mNetworkQueue;	 Catch:{ Exception -> 0x0094 }
            r0 = r0.isEmpty();	 Catch:{ Exception -> 0x0094 }
            if (r0 != 0) goto L_0x0001;
        L_0x0048:
            r0 = mNetworkQueue;	 Catch:{ Exception -> 0x0094 }
            r1 = 0;
            r0 = r0.remove(r1);	 Catch:{ Exception -> 0x0094 }
            r0 = (com.inmobi.monetization.internal.imai.db.ClickData) r0;	 Catch:{ Exception -> 0x0094 }
            r2 = r0.getClickUrl();	 Catch:{ Exception -> 0x0094 }
            r3 = r0.getRetryCount();	 Catch:{ Exception -> 0x0094 }
            r4 = r0.isPingWv();	 Catch:{ Exception -> 0x0094 }
            r5 = r0.isFollowRedirects();	 Catch:{ Exception -> 0x0094 }
            r1 = com.inmobi.monetization.internal.configs.Initializer.getConfigParams();	 Catch:{ Exception -> 0x0094 }
            r1 = r1.getImai();	 Catch:{ Exception -> 0x0094 }
            r1 = r1.getRetryInterval();	 Catch:{ Exception -> 0x0094 }
            r6 = r11.a;	 Catch:{ Exception -> 0x0094 }
            r6 = com.inmobi.commons.internal.InternalSDKUtil.checkNetworkAvailibility(r6);	 Catch:{ Exception -> 0x0094 }
            if (r6 != 0) goto L_0x00ab;
        L_0x0075:
            r1 = "[InMobi]-[Monetization]";
            r2 = "Cannot process click. Network Not available";
            com.inmobi.commons.internal.Log.internal(r1, r2);	 Catch:{ Exception -> 0x0094 }
            if (r3 <= r10) goto L_0x0083;
        L_0x007e:
            r1 = mDBWriterQueue;	 Catch:{ Exception -> 0x0094 }
            r1.add(r0);	 Catch:{ Exception -> 0x0094 }
        L_0x0083:
            r0 = r11.b;	 Catch:{ Exception -> 0x0094 }
            if (r0 == 0) goto L_0x008e;
        L_0x0087:
            r0 = r11.b;	 Catch:{ Exception -> 0x0094 }
            r1 = 1;
            r2 = 0;
            r0.notifyResult(r1, r2);	 Catch:{ Exception -> 0x0094 }
        L_0x008e:
            r0 = com.inmobi.monetization.internal.imai.RequestResponseManager.this;	 Catch:{ Exception -> 0x0094 }
            r0.deinit();	 Catch:{ Exception -> 0x0094 }
            goto L_0x0033;
        L_0x0094:
            r0 = move-exception;
            r1 = "[InMobi]-[Monetization]";
            r2 = "Exception pinging click in background";
            com.inmobi.commons.internal.Log.internal(r1, r2, r0);	 Catch:{ Exception -> 0x00a2 }
            r0 = com.inmobi.monetization.internal.imai.RequestResponseManager.this;	 Catch:{ Exception -> 0x00a2 }
            r0.deinit();	 Catch:{ Exception -> 0x00a2 }
            goto L_0x0033;
        L_0x00a2:
            r0 = move-exception;
            r1 = "[InMobi]-[Monetization]";
            r2 = "Exception ping to server ";
            com.inmobi.commons.internal.Log.internal(r1, r2, r0);
            goto L_0x0033;
        L_0x00ab:
            r6 = com.inmobi.monetization.internal.configs.Initializer.getConfigParams();	 Catch:{ Exception -> 0x0094 }
            r6 = r6.getImai();	 Catch:{ Exception -> 0x0094 }
            r6 = r6.getMaxRetry();	 Catch:{ Exception -> 0x0094 }
            r7 = i;	 Catch:{ Exception -> 0x0094 }
            r7 = r7.get();	 Catch:{ Exception -> 0x0094 }
            if (r7 != 0) goto L_0x00ed;
        L_0x00c1:
            if (r3 >= r6) goto L_0x00ed;
        L_0x00c3:
            r6 = "[InMobi]-[Monetization]";
            r7 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0094 }
            r7.<init>();	 Catch:{ Exception -> 0x0094 }
            r8 = "Retrying to ping in background after ";
            r7 = r7.append(r8);	 Catch:{ Exception -> 0x0094 }
            r8 = r1 / 1000;
            r7 = r7.append(r8);	 Catch:{ Exception -> 0x0094 }
            r8 = " secs";
            r7 = r7.append(r8);	 Catch:{ Exception -> 0x0094 }
            r7 = r7.toString();	 Catch:{ Exception -> 0x0094 }
            com.inmobi.commons.internal.Log.internal(r6, r7);	 Catch:{ Exception -> 0x0094 }
            r6 = a;	 Catch:{ Exception -> 0x0094 }
            monitor-enter(r6);	 Catch:{ Exception -> 0x0094 }
            r7 = a;	 Catch:{ InterruptedException -> 0x0152 }
            r8 = (long) r1;	 Catch:{ InterruptedException -> 0x0152 }
            r7.wait(r8);	 Catch:{ InterruptedException -> 0x0152 }
        L_0x00ec:
            monitor-exit(r6);	 Catch:{ all -> 0x015b }
        L_0x00ed:
            r1 = "[InMobi]-[Monetization]";
            r6 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0094 }
            r6.<init>();	 Catch:{ Exception -> 0x0094 }
            r7 = "Processing click in background: ";
            r6 = r6.append(r7);	 Catch:{ Exception -> 0x0094 }
            r6 = r6.append(r2);	 Catch:{ Exception -> 0x0094 }
            r6 = r6.toString();	 Catch:{ Exception -> 0x0094 }
            com.inmobi.commons.internal.Log.internal(r1, r6);	 Catch:{ Exception -> 0x0094 }
            if (r4 == 0) goto L_0x018e;
        L_0x0107:
            r1 = com.inmobi.monetization.internal.imai.RequestResponseManager.this;	 Catch:{ Exception -> 0x0094 }
            r1 = r1.processClickUrlInWebview(r2);	 Catch:{ Exception -> 0x0094 }
            if (r1 == 0) goto L_0x015e;
        L_0x010f:
            r0 = "[InMobi]-[Monetization]";
            r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0094 }
            r1.<init>();	 Catch:{ Exception -> 0x0094 }
            r3 = "Ping in webview successful: ";
            r1 = r1.append(r3);	 Catch:{ Exception -> 0x0094 }
            r1 = r1.append(r2);	 Catch:{ Exception -> 0x0094 }
            r1 = r1.toString();	 Catch:{ Exception -> 0x0094 }
            com.inmobi.commons.internal.Log.internal(r0, r1);	 Catch:{ Exception -> 0x0094 }
            r0 = r11.b;	 Catch:{ Exception -> 0x0094 }
            if (r0 == 0) goto L_0x0132;
        L_0x012b:
            r0 = r11.b;	 Catch:{ Exception -> 0x0094 }
            r1 = 0;
            r2 = 0;
            r0.notifyResult(r1, r2);	 Catch:{ Exception -> 0x0094 }
        L_0x0132:
            r0 = mDBWriterQueue;	 Catch:{ Exception -> 0x0094 }
            r0 = r0.size();	 Catch:{ Exception -> 0x0094 }
            r1 = com.inmobi.monetization.internal.configs.Initializer.getConfigParams();	 Catch:{ Exception -> 0x0094 }
            r1 = r1.getImai();	 Catch:{ Exception -> 0x0094 }
            r1 = r1.getmDefaultEventsBatch();	 Catch:{ Exception -> 0x0094 }
            if (r0 <= r1) goto L_0x0040;
        L_0x0146:
            r0 = mDBWriterQueue;	 Catch:{ Exception -> 0x0094 }
            r0.saveClickEvents();	 Catch:{ Exception -> 0x0094 }
            r0 = mDBWriterQueue;	 Catch:{ Exception -> 0x0094 }
            r0.clear();	 Catch:{ Exception -> 0x0094 }
            goto L_0x0040;
        L_0x0152:
            r1 = move-exception;
            r7 = "[InMobi]-[Monetization]";
            r8 = "Network thread wait failure";
            com.inmobi.commons.internal.Log.internal(r7, r8, r1);	 Catch:{ all -> 0x015b }
            goto L_0x00ec;
        L_0x015b:
            r0 = move-exception;
            monitor-exit(r6);	 Catch:{ all -> 0x015b }
            throw r0;	 Catch:{ Exception -> 0x0094 }
        L_0x015e:
            r1 = r3 + -1;
            r0.setRetryCount(r1);	 Catch:{ Exception -> 0x0094 }
            if (r3 <= r10) goto L_0x016a;
        L_0x0165:
            r1 = mDBWriterQueue;	 Catch:{ Exception -> 0x0094 }
            r1.add(r0);	 Catch:{ Exception -> 0x0094 }
        L_0x016a:
            r0 = "[InMobi]-[Monetization]";
            r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0094 }
            r1.<init>();	 Catch:{ Exception -> 0x0094 }
            r3 = "Ping in webview failed: ";
            r1 = r1.append(r3);	 Catch:{ Exception -> 0x0094 }
            r1 = r1.append(r2);	 Catch:{ Exception -> 0x0094 }
            r1 = r1.toString();	 Catch:{ Exception -> 0x0094 }
            com.inmobi.commons.internal.Log.internal(r0, r1);	 Catch:{ Exception -> 0x0094 }
            r0 = r11.b;	 Catch:{ Exception -> 0x0094 }
            if (r0 == 0) goto L_0x0132;
        L_0x0186:
            r0 = r11.b;	 Catch:{ Exception -> 0x0094 }
            r1 = 1;
            r2 = 0;
            r0.notifyResult(r1, r2);	 Catch:{ Exception -> 0x0094 }
            goto L_0x0132;
        L_0x018e:
            r1 = com.inmobi.monetization.internal.imai.RequestResponseManager.this;	 Catch:{ Exception -> 0x0094 }
            r1 = r1.processClickHttpClient(r2, r5);	 Catch:{ Exception -> 0x0094 }
            if (r1 == 0) goto L_0x01bb;
        L_0x0196:
            r0 = "[InMobi]-[Monetization]";
            r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0094 }
            r1.<init>();	 Catch:{ Exception -> 0x0094 }
            r3 = "Ping successful: ";
            r1 = r1.append(r3);	 Catch:{ Exception -> 0x0094 }
            r1 = r1.append(r2);	 Catch:{ Exception -> 0x0094 }
            r1 = r1.toString();	 Catch:{ Exception -> 0x0094 }
            com.inmobi.commons.internal.Log.internal(r0, r1);	 Catch:{ Exception -> 0x0094 }
            r0 = r11.b;	 Catch:{ Exception -> 0x0094 }
            if (r0 == 0) goto L_0x0132;
        L_0x01b2:
            r0 = r11.b;	 Catch:{ Exception -> 0x0094 }
            r1 = 0;
            r2 = 0;
            r0.notifyResult(r1, r2);	 Catch:{ Exception -> 0x0094 }
            goto L_0x0132;
        L_0x01bb:
            r1 = r3 + -1;
            r0.setRetryCount(r1);	 Catch:{ Exception -> 0x0094 }
            if (r3 <= r10) goto L_0x01c7;
        L_0x01c2:
            r1 = mDBWriterQueue;	 Catch:{ Exception -> 0x0094 }
            r1.add(r0);	 Catch:{ Exception -> 0x0094 }
        L_0x01c7:
            r0 = "[InMobi]-[Monetization]";
            r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0094 }
            r1.<init>();	 Catch:{ Exception -> 0x0094 }
            r3 = "Ping  failed: ";
            r1 = r1.append(r3);	 Catch:{ Exception -> 0x0094 }
            r1 = r1.append(r2);	 Catch:{ Exception -> 0x0094 }
            r1 = r1.toString();	 Catch:{ Exception -> 0x0094 }
            com.inmobi.commons.internal.Log.internal(r0, r1);	 Catch:{ Exception -> 0x0094 }
            r0 = r11.b;	 Catch:{ Exception -> 0x0094 }
            if (r0 == 0) goto L_0x01ea;
        L_0x01e3:
            r0 = r11.b;	 Catch:{ Exception -> 0x0094 }
            r1 = 1;
            r3 = 0;
            r0.notifyResult(r1, r3);	 Catch:{ Exception -> 0x0094 }
        L_0x01ea:
            r0 = "[InMobi]-[Monetization]";
            r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0094 }
            r1.<init>();	 Catch:{ Exception -> 0x0094 }
            r3 = "Ping failed: ";
            r1 = r1.append(r3);	 Catch:{ Exception -> 0x0094 }
            r1 = r1.append(r2);	 Catch:{ Exception -> 0x0094 }
            r1 = r1.toString();	 Catch:{ Exception -> 0x0094 }
            com.inmobi.commons.internal.Log.internal(r0, r1);	 Catch:{ Exception -> 0x0094 }
            goto L_0x0132;
        L_0x0204:
            r0 = com.inmobi.monetization.internal.imai.RequestResponseManager.this;	 Catch:{ Exception -> 0x00a2 }
            r0.deinit();	 Catch:{ Exception -> 0x00a2 }
            goto L_0x0033;
            */
        }
    }

    static {
        mNetworkQueue = null;
        mDBWriterQueue = new IMAIClickEventList();
        g = null;
        b = new Handler();
        c = null;
        i = null;
    }

    public RequestResponseManager() {
        this.h = null;
        this.d = Preconditions.EMPTY_ARGUMENTS;
        this.e = Preconditions.EMPTY_ARGUMENTS;
        this.f = Preconditions.EMPTY_ARGUMENTS;
    }

    public void deinit() {
        try {
            if (g != null) {
                g.set(false);
            }
            if (mDBWriterQueue != null) {
                mDBWriterQueue.saveClickEvents();
                mDBWriterQueue.clear();
            }
            isSynced.set(false);
            if (mNetworkQueue != null) {
                mNetworkQueue.clear();
            }
            mNetworkQueue = null;
        } catch (Exception e) {
            Log.internal(Constants.LOG_TAG, "Request Response Manager deinit failed", e);
        }
    }

    public void init() {
        try {
            if (mNetworkQueue == null) {
                mNetworkQueue = IMAIClickEventList.getLoggedClickEvents();
            }
            if (g == null) {
                g = new AtomicBoolean(false);
            }
            i = new AtomicBoolean(true);
            isSynced = new AtomicBoolean(false);
            c = new AtomicBoolean(false);
        } catch (Exception e) {
            Log.internal(Constants.LOG_TAG, "Request Response Manager init failed", e);
        }
    }

    public void processClick(Context context, HttpRequestCallback httpRequestCallback) {
        try {
            if (g.compareAndSet(false, true)) {
                a = new Thread(new a(context, httpRequestCallback));
                a.setPriority(1);
                a.start();
            }
        } catch (Exception e) {
            Log.internal(Constants.LOG_TAG, "Exception ping ", e);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean processClickHttpClient(java.lang.String r8, boolean r9) {
        throw new UnsupportedOperationException("Method not decompiled: com.inmobi.monetization.internal.imai.RequestResponseManager.processClickHttpClient(java.lang.String, boolean):boolean");
        /*
        r7 = this;
        r3 = 1;
        r2 = 0;
        r1 = 0;
        r0 = "[InMobi]-[Monetization]";
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x009c, all -> 0x008d }
        r4.<init>();	 Catch:{ Exception -> 0x009c, all -> 0x008d }
        r5 = "Processing click in http client ";
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x009c, all -> 0x008d }
        r4 = r4.append(r8);	 Catch:{ Exception -> 0x009c, all -> 0x008d }
        r4 = r4.toString();	 Catch:{ Exception -> 0x009c, all -> 0x008d }
        com.inmobi.commons.internal.Log.internal(r0, r4);	 Catch:{ Exception -> 0x009c, all -> 0x008d }
        r0 = com.inmobi.monetization.internal.configs.Initializer.getConfigParams();	 Catch:{ Exception -> 0x009c, all -> 0x008d }
        r0 = r0.getImai();	 Catch:{ Exception -> 0x009c, all -> 0x008d }
        r4 = r0.getPingTimeOut();	 Catch:{ Exception -> 0x009c, all -> 0x008d }
        r0 = new java.net.URL;	 Catch:{ Exception -> 0x009c, all -> 0x008d }
        r0.<init>(r8);	 Catch:{ Exception -> 0x009c, all -> 0x008d }
        r0 = r0.openConnection();	 Catch:{ Exception -> 0x009c, all -> 0x008d }
        r0 = (java.net.HttpURLConnection) r0;	 Catch:{ Exception -> 0x009c, all -> 0x008d }
        r0.setInstanceFollowRedirects(r9);	 Catch:{ Exception -> 0x00a2, all -> 0x0094 }
        r1 = "User-Agent";
        r5 = com.inmobi.commons.internal.InternalSDKUtil.getUserAgent();	 Catch:{ Exception -> 0x00a2, all -> 0x0094 }
        r0.setRequestProperty(r1, r5);	 Catch:{ Exception -> 0x00a2, all -> 0x0094 }
        r0.setConnectTimeout(r4);	 Catch:{ Exception -> 0x00a2, all -> 0x0094 }
        r0.setReadTimeout(r4);	 Catch:{ Exception -> 0x00a2, all -> 0x0094 }
        r1 = 0;
        r0.setUseCaches(r1);	 Catch:{ Exception -> 0x00a2, all -> 0x0094 }
        r1 = "user-agent";
        r4 = com.inmobi.commons.internal.InternalSDKUtil.getSavedUserAgent();	 Catch:{ Exception -> 0x00a2, all -> 0x0094 }
        r0.setRequestProperty(r1, r4);	 Catch:{ Exception -> 0x00a2, all -> 0x0094 }
        r1 = "GET";
        r0.setRequestMethod(r1);	 Catch:{ Exception -> 0x00a2, all -> 0x0094 }
        r1 = r0.getResponseCode();	 Catch:{ Exception -> 0x00a2, all -> 0x0094 }
        r4 = 400; // 0x190 float:5.6E-43 double:1.976E-321;
        if (r1 >= r4) goto L_0x00a9;
    L_0x005e:
        r1 = r3;
    L_0x005f:
        if (r3 != r1) goto L_0x006e;
    L_0x0061:
        r2 = i;	 Catch:{ Exception -> 0x0075, all -> 0x0094 }
        r3 = 1;
        r2.set(r3);	 Catch:{ Exception -> 0x0075, all -> 0x0094 }
    L_0x0067:
        if (r0 == 0) goto L_0x00a7;
    L_0x0069:
        r0.disconnect();
        r0 = r1;
    L_0x006d:
        return r0;
    L_0x006e:
        r2 = i;	 Catch:{ Exception -> 0x0075, all -> 0x0094 }
        r3 = 0;
        r2.set(r3);	 Catch:{ Exception -> 0x0075, all -> 0x0094 }
        goto L_0x0067;
    L_0x0075:
        r2 = move-exception;
        r6 = r2;
        r2 = r0;
        r0 = r1;
        r1 = r6;
    L_0x007a:
        r3 = i;	 Catch:{ all -> 0x0099 }
        r4 = 0;
        r3.set(r4);	 Catch:{ all -> 0x0099 }
        r3 = "[InMobi]-[Monetization]";
        r4 = "Click in background exception";
        com.inmobi.commons.internal.Log.internal(r3, r4, r1);	 Catch:{ all -> 0x0099 }
        if (r2 == 0) goto L_0x006d;
    L_0x0089:
        r2.disconnect();
        goto L_0x006d;
    L_0x008d:
        r0 = move-exception;
    L_0x008e:
        if (r1 == 0) goto L_0x0093;
    L_0x0090:
        r1.disconnect();
    L_0x0093:
        throw r0;
    L_0x0094:
        r1 = move-exception;
        r6 = r1;
        r1 = r0;
        r0 = r6;
        goto L_0x008e;
    L_0x0099:
        r0 = move-exception;
        r1 = r2;
        goto L_0x008e;
    L_0x009c:
        r0 = move-exception;
        r6 = r0;
        r0 = r2;
        r2 = r1;
        r1 = r6;
        goto L_0x007a;
    L_0x00a2:
        r1 = move-exception;
        r6 = r0;
        r0 = r2;
        r2 = r6;
        goto L_0x007a;
    L_0x00a7:
        r0 = r1;
        goto L_0x006d;
    L_0x00a9:
        r1 = r2;
        goto L_0x005f;
        */
    }

    public boolean processClickUrlInWebview(String str) {
        try {
            Log.internal(Constants.LOG_TAG, "Processing click in webview " + str);
            this.h = new WebviewLoader(InternalSDKUtil.getContext());
            int pingTimeOut = Initializer.getConfigParams().getImai().getPingTimeOut();
            this.h.loadInWebview(str, null);
            synchronized (a) {
                try {
                    a.wait((long) pingTimeOut);
                } catch (InterruptedException e) {
                    Log.internal(Constants.LOG_TAG, "Network thread wait failure", e);
                }
            }
            if (true == c.get()) {
                i.set(true);
            } else {
                i.set(false);
                WebviewLoader.b.set(false);
            }
            this.h.deinit(pingTimeOut);
            return c.get();
        } catch (Exception e2) {
            Log.internal(Constants.LOG_TAG, "ping in webview exception", e2);
            return c.get();
        }
    }
}