package com.inmobi.monetization.internal;

import android.os.Handler;
import android.view.ViewGroup;
import com.inmobi.commons.internal.InternalSDKUtil;
import com.inmobi.commons.internal.Log;
import com.inmobi.monetization.internal.configs.Initializer;
import com.inmobi.monetization.internal.objects.NativeAdsCache;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NativeAd extends Ad {
    public static final String KEY_CONTEXTCODE = "contextCode";
    public static final String KEY_NAMESPACE = "namespace";
    public static final String KEY_PUBCONTENT = "pubContent";
    g a;
    boolean b;
    private Handler d;
    private boolean e;
    private a f;
    private String g;
    private boolean h;
    public boolean loggingEnabled;
    public Object mImpId;

    private enum a {
        INIT,
        LOADING,
        READY,
        ATTACHED,
        DETACHED,
        ERROR,
        UNKNOWN;

        static {
            a = new a("INIT", 0);
            b = new a("LOADING", 1);
            c = new a("READY", 2);
            d = new a("ATTACHED", 3);
            e = new a("DETACHED", 4);
            f = new a("ERROR", 5);
            g = new a("UNKNOWN", 6);
            h = new a[]{a, b, c, d, e, f, g};
        }
    }

    class b implements Runnable {
        final /* synthetic */ HashMap a;

        b(HashMap hashMap) {
            this.a = hashMap;
        }

        public void run() {
            try {
                NativeAd.this.a.a(this.a);
            } catch (Exception e) {
                Log.debug(Constants.LOG_TAG, "Failed to track click");
            }
        }
    }

    class c implements Runnable {
        final /* synthetic */ ViewGroup a;
        final /* synthetic */ String b;
        final /* synthetic */ String c;

        c(ViewGroup viewGroup, String str, String str2) {
            this.a = viewGroup;
            this.b = str;
            this.c = str2;
        }

        public void run() {
            try {
                NativeAd.this.a = new g(this.a.getContext(), this.b, this.c);
                this.a.addView(NativeAd.this.a);
            } catch (Exception e) {
                android.util.Log.e(Constants.LOG_TAG, "Failed to attach the view");
                NativeAd.this.a(a.f);
            }
        }
    }

    class d implements Runnable {
        d() {
        }

        public void run() {
            try {
                if (NativeAd.this != null) {
                    NativeAd.this.a();
                    NativeAd.this = null;
                } else {
                    android.util.Log.e(Constants.LOG_TAG, "Please attach the native ad view before calling detach");
                    NativeAd.this.a(a.f);
                }
            } catch (Exception e) {
                NativeAd.this.a(a.f);
                Log.debug(Constants.LOG_TAG, "Failed to detach a view");
            }
        }
    }

    public NativeAd(String str) {
        super(str);
        this.e = false;
        this.f = a.g;
        this.a = null;
        this.g = null;
        this.b = false;
        this.h = false;
        this.e = initialize();
        this.g = str;
    }

    private void a() {
        NativeAdsCache.getInstance().removeExpiredAds();
        int numCachedAds = NativeAdsCache.getInstance().getNumCachedAds(this.g);
        int i = Initializer.getConfigParams().getNativeSdkConfigParams().getmMinLimit();
        if (numCachedAds == 0) {
            super.loadAd();
        } else if (numCachedAds < i) {
            this.b = true;
            if (this.mAdListener != null) {
                this.mAdListener.onAdRequestSucceeded();
            }
            super.loadAd();
        } else {
            this.b = true;
            if (this.mAdListener != null) {
                this.mAdListener.onAdRequestSucceeded();
            }
        }
    }

    private synchronized void a(a aVar) {
        this.f = aVar;
    }

    private synchronized a b() {
        return this.f;
    }

    public void attachToView(ViewGroup viewGroup, String str, String str2) {
        try {
            if (!InternalSDKUtil.isInitializedSuccessfully()) {
                android.util.Log.e(Constants.LOG_TAG, "Please initialize inmobi before requesting for native ads");
            } else if (!this.e) {
                Log.debug(Constants.LOG_TAG, "Please check for initialization error on the ad. The activity or appId cannot be null or blank");
            } else if (!this.h) {
                android.util.Log.e(Constants.LOG_TAG, "Please load a native ad before attach");
            } else if (viewGroup == null) {
                android.util.Log.e(Constants.LOG_TAG, "Please pass a valid view to attach");
            } else if (b() == a.d) {
                Log.debug(Constants.LOG_TAG, "Ad is already attached");
            } else if (b() != a.c) {
                android.util.Log.e(Constants.LOG_TAG, "Cannot attach an ad which is not ready or detached from view");
            } else if (this.d == null) {
                android.util.Log.e(Constants.LOG_TAG, "Please create a native ad instance in the main thread");
            } else {
                this.d.post(new c(viewGroup, str, str2));
                a(a.d);
            }
        } catch (Exception e) {
            android.util.Log.e(Constants.LOG_TAG, "Please pass a valid view to attach");
        }
    }

    public void detachFromView() {
        if (!InternalSDKUtil.isInitializedSuccessfully()) {
            android.util.Log.e(Constants.LOG_TAG, "Please initialize inmobi before requesting for native ads");
        } else if (!this.e) {
            Log.debug(Constants.LOG_TAG, "Please check for initialization error on the ad. The activity or appId cannot be null or blank");
        } else if (!this.h) {
            android.util.Log.e(Constants.LOG_TAG, "Please load a native ad before detach");
        } else if (b() != a.d) {
            Log.debug(Constants.LOG_TAG, "Please attach the native ad view before calling detach");
        } else if (this.d == null) {
            android.util.Log.e(Constants.LOG_TAG, "Please create a native ad instance in the main thread");
        } else {
            this.d.post(new d());
            this.h = false;
            a(a.e);
        }
    }

    protected Map<String, String> getAdFormatParams() {
        Map<String, String> hashMap = new HashMap();
        hashMap.put("format", AD_FORMAT.NATIVE.toString().toLowerCase(Locale.getDefault()));
        hashMap.put("mk-ads", String.valueOf(Initializer.getConfigParams().getNativeSdkConfigParams().getmFetchLimit()));
        return hashMap;
    }

    public Handler getHandler() {
        return this.d;
    }

    public void handleClick(HashMap<String, String> hashMap) {
        if (!InternalSDKUtil.isInitializedSuccessfully()) {
            android.util.Log.e(Constants.LOG_TAG, "Please initialize inmobi before requesting for native ads");
        } else if (!this.e) {
            Log.debug(Constants.LOG_TAG, "Please check for initialization error on the ad. The activity or appId cannot be null or blank");
        } else if (!this.h) {
            android.util.Log.e(Constants.LOG_TAG, "Cannot handle click, native ad not loaded or detached from view");
        } else if (b() != a.d) {
            Log.debug(Constants.LOG_TAG, "Please attach to view before handling any events");
        } else if (this.d == null) {
            android.util.Log.e(Constants.LOG_TAG, "Please create a native ad instance in the main thread");
        } else {
            this.d.post(new b(hashMap));
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void handleResponse(com.inmobi.monetization.internal.i r10, com.inmobi.commons.network.Response r11) {
        throw new UnsupportedOperationException("Method not decompiled: com.inmobi.monetization.internal.NativeAd.handleResponse(com.inmobi.monetization.internal.i, com.inmobi.commons.network.Response):void");
        /*
        r9 = this;
        if (r11 == 0) goto L_0x003c;
    L_0x0002:
        r0 = r11.getStatusCode();	 Catch:{ Exception -> 0x00ad }
        r1 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r0 != r1) goto L_0x0102;
    L_0x000a:
        r0 = new org.json.JSONObject;	 Catch:{ Exception -> 0x00ad }
        r1 = r11.getResponseBody();	 Catch:{ Exception -> 0x00ad }
        r0.<init>(r1);	 Catch:{ Exception -> 0x00ad }
        r1 = "ads";
        r3 = r0.getJSONArray(r1);	 Catch:{ Exception -> 0x00ad }
        if (r3 == 0) goto L_0x003c;
    L_0x001b:
        r0 = r3.length();	 Catch:{ Exception -> 0x00ad }
        if (r0 != 0) goto L_0x003d;
    L_0x0021:
        r0 = "[InMobi]-[Network]-4.5.0";
        r1 = "Server returned No Fill ";
        com.inmobi.commons.internal.Log.debug(r0, r1);	 Catch:{ Exception -> 0x00ad }
        r0 = r9.mAdListener;	 Catch:{ Exception -> 0x00ad }
        if (r0 == 0) goto L_0x003c;
    L_0x002c:
        r0 = r9.b;	 Catch:{ Exception -> 0x00ad }
        if (r0 != 0) goto L_0x003c;
    L_0x0030:
        r0 = com.inmobi.monetization.internal.NativeAd.a.f;	 Catch:{ Exception -> 0x00ad }
        r9.a(r0);	 Catch:{ Exception -> 0x00ad }
        r0 = r9.mAdListener;	 Catch:{ Exception -> 0x00ad }
        r1 = com.inmobi.monetization.internal.AdErrorCode.NO_FILL;	 Catch:{ Exception -> 0x00ad }
        r0.onAdRequestFailed(r1);	 Catch:{ Exception -> 0x00ad }
    L_0x003c:
        return;
    L_0x003d:
        r4 = new java.util.ArrayList;	 Catch:{ Exception -> 0x00ad }
        r4.<init>();	 Catch:{ Exception -> 0x00ad }
        r1 = r3.length();	 Catch:{ Exception -> 0x00ad }
        r0 = com.inmobi.monetization.internal.configs.Initializer.getConfigParams();	 Catch:{ Exception -> 0x00ad }
        r0 = r0.getNativeSdkConfigParams();	 Catch:{ Exception -> 0x00ad }
        r0 = r0.getmFetchLimit();	 Catch:{ Exception -> 0x00ad }
        if (r1 <= r0) goto L_0x0144;
    L_0x0054:
        r1 = 0;
        r2 = r1;
    L_0x0056:
        if (r2 >= r0) goto L_0x00c7;
    L_0x0058:
        r1 = r3.getJSONObject(r2);	 Catch:{ Exception -> 0x00a4 }
        r5 = "pubContent";
        r5 = r1.optString(r5);	 Catch:{ Exception -> 0x00a4 }
        r6 = "contextCode";
        r6 = r1.optString(r6);	 Catch:{ Exception -> 0x00a4 }
        r7 = "namespace";
        r7 = r1.optString(r7);	 Catch:{ Exception -> 0x00a4 }
        if (r5 == 0) goto L_0x007c;
    L_0x0070:
        r8 = "";
        r5 = r5.trim();	 Catch:{ Exception -> 0x00a4 }
        r5 = r8.equals(r5);	 Catch:{ Exception -> 0x00a4 }
        if (r5 == 0) goto L_0x0080;
    L_0x007c:
        r1 = r2 + 1;
        r2 = r1;
        goto L_0x0056;
    L_0x0080:
        if (r6 == 0) goto L_0x007c;
    L_0x0082:
        r5 = "";
        r6 = r6.trim();	 Catch:{ Exception -> 0x00a4 }
        r5 = r5.equals(r6);	 Catch:{ Exception -> 0x00a4 }
        if (r5 != 0) goto L_0x007c;
    L_0x008e:
        if (r7 == 0) goto L_0x007c;
    L_0x0090:
        r5 = "";
        r6 = r7.trim();	 Catch:{ Exception -> 0x00a4 }
        r5 = r5.equals(r6);	 Catch:{ Exception -> 0x00a4 }
        if (r5 != 0) goto L_0x007c;
    L_0x009c:
        r1 = r1.toString();	 Catch:{ Exception -> 0x00a4 }
        r4.add(r1);	 Catch:{ Exception -> 0x00a4 }
        goto L_0x007c;
    L_0x00a4:
        r1 = move-exception;
        r5 = "[InMobi]-[Monetization]";
        r6 = "JSON Exception";
        com.inmobi.commons.internal.Log.internal(r5, r6, r1);	 Catch:{ Exception -> 0x00ad }
        goto L_0x007c;
    L_0x00ad:
        r0 = move-exception;
        r0 = "[InMobi]-[Monetization]";
        r1 = "Exception retrieving native ad";
        com.inmobi.commons.internal.Log.internal(r0, r1);
        r0 = r9.mAdListener;
        if (r0 == 0) goto L_0x003c;
    L_0x00b9:
        r0 = com.inmobi.monetization.internal.NativeAd.a.f;
        r9.a(r0);
        r0 = r9.mAdListener;
        r1 = com.inmobi.monetization.internal.AdErrorCode.INTERNAL_ERROR;
        r0.onAdRequestFailed(r1);
        goto L_0x003c;
    L_0x00c7:
        r0 = r9.mAdListener;	 Catch:{ Exception -> 0x00ad }
        if (r0 == 0) goto L_0x003c;
    L_0x00cb:
        r0 = r4.size();	 Catch:{ Exception -> 0x00ad }
        if (r0 <= 0) goto L_0x00ed;
    L_0x00d1:
        r0 = com.inmobi.monetization.internal.objects.NativeAdsCache.getInstance();	 Catch:{ Exception -> 0x00ad }
        r1 = r9.g;	 Catch:{ Exception -> 0x00ad }
        r0.saveNativeAds(r1, r4);	 Catch:{ Exception -> 0x00ad }
        r0 = com.inmobi.monetization.internal.NativeAd.a.c;	 Catch:{ Exception -> 0x00ad }
        r9.a(r0);	 Catch:{ Exception -> 0x00ad }
        r0 = 1;
        r9.h = r0;	 Catch:{ Exception -> 0x00ad }
        r0 = r9.b;	 Catch:{ Exception -> 0x00ad }
        if (r0 != 0) goto L_0x003c;
    L_0x00e6:
        r0 = r9.mAdListener;	 Catch:{ Exception -> 0x00ad }
        r0.onAdRequestSucceeded();	 Catch:{ Exception -> 0x00ad }
        goto L_0x003c;
    L_0x00ed:
        r0 = com.inmobi.monetization.internal.NativeAd.a.f;	 Catch:{ Exception -> 0x00ad }
        r9.a(r0);	 Catch:{ Exception -> 0x00ad }
        r0 = "[InMobi]-[Network]-4.5.0";
        r1 = "Server Error";
        com.inmobi.commons.internal.Log.debug(r0, r1);	 Catch:{ Exception -> 0x00ad }
        r0 = r9.mAdListener;	 Catch:{ Exception -> 0x00ad }
        r1 = com.inmobi.monetization.internal.AdErrorCode.INTERNAL_ERROR;	 Catch:{ Exception -> 0x00ad }
        r0.onAdRequestFailed(r1);	 Catch:{ Exception -> 0x00ad }
        goto L_0x003c;
    L_0x0102:
        r0 = r11.getStatusCode();	 Catch:{ Exception -> 0x00ad }
        r1 = 400; // 0x190 float:5.6E-43 double:1.976E-321;
        if (r0 != r1) goto L_0x0127;
    L_0x010a:
        r0 = "[InMobi]-[Network]-4.5.0";
        r1 = "Invalid App Id.Please check the app Id in the adrequest is valid and in active state";
        com.inmobi.commons.internal.Log.debug(r0, r1);	 Catch:{ Exception -> 0x00ad }
        r0 = r9.mAdListener;	 Catch:{ Exception -> 0x00ad }
        if (r0 == 0) goto L_0x003c;
    L_0x0115:
        r0 = r9.b;	 Catch:{ Exception -> 0x00ad }
        if (r0 != 0) goto L_0x003c;
    L_0x0119:
        r0 = com.inmobi.monetization.internal.NativeAd.a.f;	 Catch:{ Exception -> 0x00ad }
        r9.a(r0);	 Catch:{ Exception -> 0x00ad }
        r0 = r9.mAdListener;	 Catch:{ Exception -> 0x00ad }
        r1 = com.inmobi.monetization.internal.AdErrorCode.INVALID_APP_ID;	 Catch:{ Exception -> 0x00ad }
        r0.onAdRequestFailed(r1);	 Catch:{ Exception -> 0x00ad }
        goto L_0x003c;
    L_0x0127:
        r0 = "[InMobi]-[Network]-4.5.0";
        r1 = "Server Error";
        com.inmobi.commons.internal.Log.debug(r0, r1);	 Catch:{ Exception -> 0x00ad }
        r0 = r9.mAdListener;	 Catch:{ Exception -> 0x00ad }
        if (r0 == 0) goto L_0x003c;
    L_0x0132:
        r0 = r9.b;	 Catch:{ Exception -> 0x00ad }
        if (r0 != 0) goto L_0x003c;
    L_0x0136:
        r0 = com.inmobi.monetization.internal.NativeAd.a.f;	 Catch:{ Exception -> 0x00ad }
        r9.a(r0);	 Catch:{ Exception -> 0x00ad }
        r0 = r9.mAdListener;	 Catch:{ Exception -> 0x00ad }
        r1 = com.inmobi.monetization.internal.AdErrorCode.INTERNAL_ERROR;	 Catch:{ Exception -> 0x00ad }
        r0.onAdRequestFailed(r1);	 Catch:{ Exception -> 0x00ad }
        goto L_0x003c;
    L_0x0144:
        r0 = r1;
        goto L_0x0054;
        */
    }

    protected boolean initialize() {
        try {
            this.d = new Handler();
            a(a.a);
            return super.initialize();
        } catch (Throwable th) {
            android.util.Log.e(Constants.LOG_TAG, "Please create a native ad instance in the main thread");
            return false;
        }
    }

    public void loadAd() {
        if (this.e) {
            this.b = false;
            if (this.d == null) {
                android.util.Log.e(Constants.LOG_TAG, "Please create a native ad instance in the main thread");
            } else if (b() == a.a || b() != a.g) {
                Log.debug(Constants.LOG_TAG, "Loading Native Ad");
                a(a.b);
                a();
            } else if (b() == a.b) {
                android.util.Log.e(Constants.LOG_TAG, "Ad is already loading. Please wait");
            } else if (b() != a.g) {
                Log.debug(Constants.LOG_TAG, "Loading native ad");
                if (b() == a.d) {
                    detachFromView();
                }
                a(a.b);
                a();
            }
        } else {
            Log.debug(Constants.LOG_TAG, "Please check for initialization error on the ad. The activity or appId cannot be null or blank");
        }
    }
}