package com.inmobi.monetization.internal;

import android.app.Activity;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Handler;
import com.inmobi.androidsdk.IMBrowserActivity;
import com.inmobi.commons.ads.cache.AdDatabaseHelper;
import com.inmobi.commons.data.DeviceInfo;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.inmobi.commons.internal.InternalSDKUtil;
import com.inmobi.commons.internal.Log;
import com.inmobi.commons.network.Response;
import com.inmobi.monetization.internal.configs.Initializer;
import com.inmobi.monetization.internal.configs.NetworkEventType;
import com.inmobi.monetization.internal.imai.IMAIController;
import com.inmobi.monetization.internal.imai.IMAIController.InterstitialAdStateListener;
import com.inmobi.re.container.IMWebView;
import com.inmobi.re.container.IMWebView.IMWebViewListener;
import com.isssss.myadv.dao.AdvConfigTable;
import com.mopub.common.Preconditions;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class InterstitialAd extends Ad implements InterstitialAdStateListener {
    long a;
    boolean b;
    private Activity d;
    private long e;
    private IMWebView f;
    private long g;
    private Object h;
    private Response i;
    private boolean j;
    private b k;
    private IMWebViewListener l;

    class a implements Runnable {
        a() {
        }

        public void run() {
            InterstitialAd.this.c();
        }
    }

    static class b extends Handler {
        private final WeakReference<InterstitialAd> a;

        public b(InterstitialAd interstitialAd) {
            this.a = new WeakReference(interstitialAd);
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void handleMessage(android.os.Message r6) {
            throw new UnsupportedOperationException("Method not decompiled: com.inmobi.monetization.internal.InterstitialAd.b.handleMessage(android.os.Message):void");
            /*
            r5 = this;
            r0 = r5.a;
            r0 = r0.get();
            r0 = (com.inmobi.monetization.internal.InterstitialAd) r0;
            if (r0 == 0) goto L_0x000f;
        L_0x000a:
            r1 = r6.what;	 Catch:{ Exception -> 0x0047 }
            switch(r1) {
                case 301: goto L_0x0010;
                default: goto L_0x000f;
            };	 Catch:{ Exception -> 0x0047 }
        L_0x000f:
            return;
        L_0x0010:
            r1 = r0.f;	 Catch:{ Exception -> 0x0047 }
            r1.cancelLoad();	 Catch:{ Exception -> 0x0047 }
            r1 = r0.f;	 Catch:{ Exception -> 0x0047 }
            r1.stopLoading();	 Catch:{ Exception -> 0x0047 }
            r1 = r0.f;	 Catch:{ Exception -> 0x0047 }
            r1.deinit();	 Catch:{ Exception -> 0x0047 }
            r1 = 0;
            r0.f = r1;	 Catch:{ Exception -> 0x0047 }
            r1 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x0047 }
            r3 = r0.e;	 Catch:{ Exception -> 0x0047 }
            r1 = r1 - r3;
            r3 = r0.i;	 Catch:{ Exception -> 0x0047 }
            r4 = com.inmobi.monetization.internal.configs.NetworkEventType.RENDER_TIMEOUT;	 Catch:{ Exception -> 0x0047 }
            r0.collectMetrics(r3, r1, r4);	 Catch:{ Exception -> 0x0047 }
            r1 = r0.mAdListener;	 Catch:{ Exception -> 0x0047 }
            if (r1 == 0) goto L_0x000f;
        L_0x003f:
            r0 = r0.mAdListener;	 Catch:{ Exception -> 0x0047 }
            r1 = com.inmobi.monetization.internal.AdErrorCode.AD_RENDERING_TIMEOUT;	 Catch:{ Exception -> 0x0047 }
            r0.onAdRequestFailed(r1);	 Catch:{ Exception -> 0x0047 }
            goto L_0x000f;
        L_0x0047:
            r0 = move-exception;
            r1 = "[InMobi]-[Monetization]";
            r2 = "Exception handling message in Interstitial";
            com.inmobi.commons.internal.Log.internal(r1, r2, r0);
            goto L_0x000f;
            */
        }
    }

    public InterstitialAd(Activity activity, long j) {
        super(j);
        this.g = 0;
        this.h = null;
        this.a = 0;
        this.b = true;
        this.i = null;
        this.j = false;
        this.k = new b(this);
        this.l = new c(this);
        this.d = activity;
        this.j = initialize();
    }

    public InterstitialAd(Activity activity, String str) {
        super(str);
        this.g = 0;
        this.h = null;
        this.a = 0;
        this.b = true;
        this.i = null;
        this.j = false;
        this.k = new b(this);
        this.l = new c(this);
        this.d = activity;
        this.j = initialize();
    }

    private static int a() {
        return DeviceInfo.isTablet(InternalSDKUtil.getContext()) ? ApiEventType.API_MRAID_GET_SCREEN_SIZE : ApiEventType.API_MRAID_IS_VIEWABLE;
    }

    private boolean a(Object obj) {
        try {
            Field declaredField = obj.getClass().getDeclaredField("mIsPlayableReady");
            declaredField.setAccessible(true);
            return ((Boolean) declaredField.get(obj)).booleanValue();
        } catch (Exception e) {
            return false;
        }
    }

    private void b() {
        try {
            if (this.mAdListener != null) {
                this.mAdListener.onShowAdScreen();
            }
            Intent intent = new Intent(this.d, IMBrowserActivity.class);
            intent.putExtra(IMBrowserActivity.EXTRA_BROWSER_ACTIVITY_TYPE, IMBrowserActivity.INTERSTITIAL_ACTIVITY);
            intent.putExtra(IMBrowserActivity.ANIMATED, this.g);
            IMBrowserActivity.setWebview(this.f);
            IMBrowserActivity.setOriginalActivity(this.d);
            this.d.startActivity(intent);
        } catch (Exception e) {
            Log.debug(Constants.LOG_TAG, "Error showing ad ", e);
        }
    }

    private void b(Object obj) {
        try {
            Method declaredMethod = obj.getClass().getDeclaredMethod(AdvConfigTable.COLUMN_SHOW, new Class[]{IMAdListener.class});
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(obj, new Object[]{this.mAdListener});
        } catch (Exception e) {
            Log.internal(Constants.LOG_TAG, "Failed to display playable ad");
            b();
        }
    }

    private void c() {
        String responseBody;
        long currentTimeMillis = System.currentTimeMillis() - this.mFetchStartTime;
        if (this.i != null) {
            responseBody = this.i.getResponseBody();
        } else {
            responseBody = null;
        }
        if (responseBody != null) {
            String replace = responseBody.replace("@__imm_aft@", Preconditions.EMPTY_ARGUMENTS + currentTimeMillis);
            if (VERSION.SDK_INT <= 8) {
                replace.replaceAll("%", "%25");
            }
            this.e = System.currentTimeMillis();
            this.k.sendEmptyMessageDelayed(301, (long) Initializer.getConfigParams().getRenderTimeOut());
            this.f.loadDataWithBaseURL(Preconditions.EMPTY_ARGUMENTS, replace, "text/html", null, null);
        } else {
            Log.debug(Constants.LOG_TAG, "Cannot load Ad. Invalid Ad Response");
            if (this.mAdListener != null) {
                this.mAdListener.onAdRequestFailed(AdErrorCode.INTERNAL_ERROR);
            }
        }
    }

    private boolean d() {
        try {
            Class.forName("com.inmobi.monetization.internal.thirdparty.PlayableAdsManager");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Object e() {
        try {
            return Class.forName("com.inmobi.monetization.internal.thirdparty.PlayableAdsManager").getConstructor(new Class[]{Activity.class, IMWebView.class}).newInstance(new Object[]{this.d, this.f});
        } catch (Exception e) {
            Log.internal(Constants.LOG_TAG, "Exception creating playable ads", e);
            return null;
        }
    }

    private void f() {
        if (this.f == null) {
            this.f = new IMWebView(this.d, this.l, true, false);
            if (!this.b) {
                this.f.disableHardwareAcceleration();
            }
            IMAIController iMAIController = new IMAIController(this.f);
            iMAIController.setInterstitialAdStateListener(this);
            this.f.addJavascriptInterface(iMAIController, IMAIController.IMAI_BRIDGE);
        }
    }

    public void destroy() {
        try {
            if (this.j) {
                if (this.f != null) {
                    this.f.destroy();
                }
                this.f = null;
            } else {
                Log.debug(Constants.LOG_TAG, "Please check for initialization error on the ad. The activity or appId cannot be null or blank");
            }
        } catch (Exception e) {
            Log.debug(Constants.LOG_TAG, "Unable to destroy webview, or it has been destroyed already.");
        }
    }

    public void disableHardwareAcceleration() {
        if (this.j) {
            this.b = false;
            if (this.f != null) {
                this.f.disableHardwareAcceleration();
            }
        } else {
            Log.debug(Constants.LOG_TAG, "Please check for initialization error on the ad. The activity or appId cannot be null or blank");
        }
    }

    protected Map<String, String> getAdFormatParams() {
        Map<String, String> hashMap = new HashMap();
        hashMap.put("format", AD_FORMAT.IMAI.toString().toLowerCase(Locale.getDefault()));
        hashMap.put("mk-ads", "1");
        hashMap.put("mk-ad-slot", String.valueOf(a()));
        hashMap.put(AdDatabaseHelper.COLUMN_ADTYPE, "int");
        if (d()) {
            hashMap.put("playable", String.valueOf(1));
        }
        return hashMap;
    }

    public void handleResponse(i iVar, Response response) {
        try {
            this.i = response;
            this.d.runOnUiThread(new a());
        } catch (Exception e) {
            Log.debug(Constants.LOG_TAG, "Error retrieving ad ", e);
            if (this.mAdListener != null) {
                this.mAdListener.onAdRequestFailed(AdErrorCode.INTERNAL_ERROR);
            }
        }
    }

    protected boolean initialize() {
        if (this.d == null) {
            Log.debug(Constants.LOG_TAG, InvalidManifestErrorMessages.MSG_NIL_ACTIVITY);
            return false;
        } else {
            this.d = h.a(this.d);
            setAdListener(this.mAdListener);
            f();
            if (d()) {
                this.h = e();
            }
            return super.initialize();
        }
    }

    public void loadAd() {
        if (this.j) {
            f();
            super.loadAd();
        } else {
            Log.debug(Constants.LOG_TAG, "Please check for initialization error on the ad. The activity or appId cannot be null or blank");
        }
    }

    public void onAdFailed() {
        this.k.removeMessages(301);
        if (this.mAdListener != null) {
            this.mAdListener.onAdRequestFailed(AdErrorCode.INTERNAL_ERROR);
        }
    }

    public void onAdReady() {
        collectMetrics(this.i, System.currentTimeMillis() - this.e, NetworkEventType.RENDER_COMPLETE);
        this.k.removeMessages(301);
        if (this.mAdListener != null) {
            this.mAdListener.onAdRequestSucceeded();
        }
    }

    public void show() {
        try {
            if (this.j) {
                Log.debug(Constants.LOG_TAG, "Showing the Interstitial Ad. ");
                if (d() && this.h != null && a(this.h)) {
                    b(this.h);
                } else {
                    b();
                }
            } else {
                Log.debug(Constants.LOG_TAG, "Please check for initialization error on the ad. The activity or appId cannot be null or blank");
            }
        } catch (Exception e) {
            Log.debug(Constants.LOG_TAG, "Error showing ad ", e);
        }
    }

    public void show(long j) {
        if (this.j) {
            this.g = j;
            show();
        } else {
            Log.debug(Constants.LOG_TAG, "Please check for initialization error on the ad. The activity or appId cannot be null or blank");
        }
    }

    public void stopLoading() {
        if (this.j) {
            super.stopLoading();
        } else {
            Log.debug(Constants.LOG_TAG, "Please check for initialization error on the ad. The activity or appId cannot be null or blank");
        }
    }
}