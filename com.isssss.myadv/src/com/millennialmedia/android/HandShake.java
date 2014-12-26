package com.millennialmedia.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.WindowManager.BadTokenException;
import android.webkit.URLUtil;
import android.widget.Toast;
import com.google.android.gms.cast.Cast;
import com.inmobi.commons.analytics.db.AnalyticsSQLiteHelper;
import com.millennialmedia.google.gson.Gson;
import com.mopub.common.Preconditions;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class HandShake {
    static final String BASE_URL = "http://androidsdk.ads.mp.mydas.mobi/";
    static final String BASE_URL_PATH = "getAd.php5?";
    private static final String HANDSHAKE_FALLBACK_URL = "http://ads.mp.mydas.mobi/appConfigServlet?apid=";
    private static final String HANDSHAKE_HTTPS_SCHEME = "https://";
    private static final String HANDSHAKE_HTTP_SCHEME = "http://";
    private static final String HANDSHAKE_URL_HOST = "ads.mp.mydas.mobi/";
    private static final String HANDSHAKE_URL_OVERRIDE_PARMS = "?apid=";
    private static final String HANDSHAKE_URL_PARMS = "appConfigServlet?apid=";
    private static final String KEY_CACHED_VIDEOS = "handshake_cachedvideos5.0";
    private static final String TAG = "HandShake";
    private static String adUrl;
    static String apid;
    private static boolean forceRefresh;
    private static String handShakeURL;
    private static HandShake sharedInstance;
    long adRefreshSecs;
    private final LinkedHashMap<String, AdTypeHandShake> adTypeHandShakes;
    private WeakReference<Context> appContextRef;
    DTOCachedVideo[] cachedVideos;
    private WeakReference<Context> contextRef;
    long creativeCacheTimeout;
    private long deferredViewTimeout;
    String endSessionURL;
    private long handShakeCallback;
    private final Handler handler;
    boolean hardwareAccelerationEnabled;
    boolean kill;
    private long lastHandShake;
    String mmdid;
    String mmjsUrl;
    private String noVideosToCacheURL;
    NuanceCredentials nuanceCredentials;
    private final ArrayList<Scheme> schemes;
    private String schemesList;
    String startSessionURL;
    private final Runnable updateHandShakeRunnable;

    class AnonymousClass_1 implements Runnable {
        final /* synthetic */ boolean val$isInitialize;

        AnonymousClass_1(boolean z) {
            this.val$isInitialize = z;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            throw new UnsupportedOperationException("Method not decompiled: com.millennialmedia.android.HandShake.AnonymousClass_1.run():void");
            /*
            r15 = this;
            r10 = com.millennialmedia.android.HandShake.this;
            r10 = r10.contextRef;
            r9 = r10.get();
            r9 = (android.content.Context) r9;
            if (r9 != 0) goto L_0x001a;
        L_0x000e:
            r10 = com.millennialmedia.android.HandShake.this;
            r10 = r10.appContextRef;
            r9 = r10.get();
            r9 = (android.content.Context) r9;
        L_0x001a:
            if (r9 != 0) goto L_0x001d;
        L_0x001c:
            return;
        L_0x001d:
            r8 = 0;
            r4 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r4.<init>();	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r0 = new java.util.TreeMap;	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r0.<init>();	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r10 = "ua";
            r11 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r11.<init>();	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r12 = "Android:";
            r11 = r11.append(r12);	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r12 = android.os.Build.MODEL;	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r11 = r11.append(r12);	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r11 = r11.toString();	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r0.put(r10, r11);	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r10 = com.millennialmedia.android.HandShake.this;	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r8 = r10.isFirstLaunch(r9);	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            if (r8 == 0) goto L_0x0051;
        L_0x004a:
            r10 = "firstlaunch";
            r11 = "1";
            r0.put(r10, r11);	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
        L_0x0051:
            r10 = r15.val$isInitialize;	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            if (r10 == 0) goto L_0x005c;
        L_0x0055:
            r10 = "init";
            r11 = "1";
            r0.put(r10, r11);	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
        L_0x005c:
            com.millennialmedia.android.MMSDK.insertUrlCommonValues(r9, r0);	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r10 = r0.entrySet();	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r6 = r10.iterator();	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
        L_0x0067:
            r10 = r6.hasNext();	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            if (r10 == 0) goto L_0x00a8;
        L_0x006d:
            r2 = r6.next();	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r2 = (java.util.Map.Entry) r2;	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r11 = "&%s=%s";
            r10 = 2;
            r12 = new java.lang.Object[r10];	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r10 = 0;
            r13 = r2.getKey();	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r12[r10] = r13;	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r13 = 1;
            r10 = r2.getValue();	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r10 = (java.lang.String) r10;	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r14 = "UTF-8";
            r10 = java.net.URLEncoder.encode(r10, r14);	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r12[r13] = r10;	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r10 = java.lang.String.format(r11, r12);	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r4.append(r10);	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            goto L_0x0067;
        L_0x0096:
            r7 = move-exception;
            r10 = "HandShake";
            r11 = "Could not get a handshake. ";
            com.millennialmedia.android.MMLog.e(r10, r11, r7);	 Catch:{ all -> 0x01fb }
            r8 = 0;
            if (r8 == 0) goto L_0x001c;
        L_0x00a1:
            r10 = com.millennialmedia.android.HandShake.this;
            r10.sentFirstLaunch(r9);
            goto L_0x001c;
        L_0x00a8:
            r10 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r10.<init>();	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r11 = handShakeURL;	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r10 = r10.append(r11);	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r11 = apid;	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r10 = r10.append(r11);	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r11 = r4.toString();	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r10 = r10.append(r11);	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r3 = r10.toString();	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r10 = "HandShake";
            r11 = "Performing handshake: %s";
            r12 = 1;
            r12 = new java.lang.Object[r12];	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r13 = 0;
            r12[r13] = r3;	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r11 = java.lang.String.format(r11, r12);	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            com.millennialmedia.android.MMLog.v(r10, r11);	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r5 = 0;
            r10 = new com.millennialmedia.android.HttpGetRequest;	 Catch:{ IOException -> 0x01d5, Exception -> 0x01df }
            r11 = 3000; // 0xbb8 float:4.204E-42 double:1.482E-320;
            r10.<init>(r11);	 Catch:{ IOException -> 0x01d5, Exception -> 0x01df }
            r5 = r10.get(r3);	 Catch:{ IOException -> 0x01d5, Exception -> 0x01df }
        L_0x00e4:
            if (r5 == 0) goto L_0x00f2;
        L_0x00e6:
            r10 = r5.getStatusLine();	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r10 = r10.getStatusCode();	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r11 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
            if (r10 == r11) goto L_0x013a;
        L_0x00f2:
            r10 = handShakeURL;	 Catch:{ IOException -> 0x01f1, Exception -> 0x01df }
            r11 = "https://";
            r12 = "http://";
            r10 = r10.replaceFirst(r11, r12);	 Catch:{ IOException -> 0x01f1, Exception -> 0x01df }
            handShakeURL = r10;	 Catch:{ IOException -> 0x01f1, Exception -> 0x01df }
            r10 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x01f1, Exception -> 0x01df }
            r10.<init>();	 Catch:{ IOException -> 0x01f1, Exception -> 0x01df }
            r11 = handShakeURL;	 Catch:{ IOException -> 0x01f1, Exception -> 0x01df }
            r10 = r10.append(r11);	 Catch:{ IOException -> 0x01f1, Exception -> 0x01df }
            r11 = apid;	 Catch:{ IOException -> 0x01f1, Exception -> 0x01df }
            r10 = r10.append(r11);	 Catch:{ IOException -> 0x01f1, Exception -> 0x01df }
            r11 = r4.toString();	 Catch:{ IOException -> 0x01f1, Exception -> 0x01df }
            r10 = r10.append(r11);	 Catch:{ IOException -> 0x01f1, Exception -> 0x01df }
            r3 = r10.toString();	 Catch:{ IOException -> 0x01f1, Exception -> 0x01df }
            r10 = "HandShake";
            r11 = "Performing handshake (HTTP Fallback): %s";
            r12 = 1;
            r12 = new java.lang.Object[r12];	 Catch:{ IOException -> 0x01f1, Exception -> 0x01df }
            r13 = 0;
            r12[r13] = r3;	 Catch:{ IOException -> 0x01f1, Exception -> 0x01df }
            r11 = java.lang.String.format(r11, r12);	 Catch:{ IOException -> 0x01f1, Exception -> 0x01df }
            com.millennialmedia.android.MMLog.v(r10, r11);	 Catch:{ IOException -> 0x01f1, Exception -> 0x01df }
            r10 = new com.millennialmedia.android.HttpGetRequest;	 Catch:{ IOException -> 0x01f1, Exception -> 0x01df }
            r10.<init>();	 Catch:{ IOException -> 0x01f1, Exception -> 0x01df }
            r5 = r10.get(r3);	 Catch:{ IOException -> 0x01f1, Exception -> 0x01df }
        L_0x013a:
            if (r5 == 0) goto L_0x0148;
        L_0x013c:
            r10 = r5.getStatusLine();	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r10 = r10.getStatusCode();	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r11 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
            if (r10 == r11) goto L_0x0186;
        L_0x0148:
            r10 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0204, Exception -> 0x01df }
            r10.<init>();	 Catch:{ IOException -> 0x0204, Exception -> 0x01df }
            r11 = "http://ads.mp.mydas.mobi/appConfigServlet?apid=";
            r10 = r10.append(r11);	 Catch:{ IOException -> 0x0204, Exception -> 0x01df }
            r11 = apid;	 Catch:{ IOException -> 0x0204, Exception -> 0x01df }
            r10 = r10.append(r11);	 Catch:{ IOException -> 0x0204, Exception -> 0x01df }
            r11 = r4.toString();	 Catch:{ IOException -> 0x0204, Exception -> 0x01df }
            r10 = r10.append(r11);	 Catch:{ IOException -> 0x0204, Exception -> 0x01df }
            r3 = r10.toString();	 Catch:{ IOException -> 0x0204, Exception -> 0x01df }
            r10 = "HandShake";
            r11 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0204, Exception -> 0x01df }
            r11.<init>();	 Catch:{ IOException -> 0x0204, Exception -> 0x01df }
            r12 = "Performing handshake (HTTP Fallback Original): ";
            r11 = r11.append(r12);	 Catch:{ IOException -> 0x0204, Exception -> 0x01df }
            r11 = r11.append(r3);	 Catch:{ IOException -> 0x0204, Exception -> 0x01df }
            r11 = r11.toString();	 Catch:{ IOException -> 0x0204, Exception -> 0x01df }
            com.millennialmedia.android.MMLog.v(r10, r11);	 Catch:{ IOException -> 0x0204, Exception -> 0x01df }
            r10 = new com.millennialmedia.android.HttpGetRequest;	 Catch:{ IOException -> 0x0204, Exception -> 0x01df }
            r10.<init>();	 Catch:{ IOException -> 0x0204, Exception -> 0x01df }
            r5 = r10.get(r3);	 Catch:{ IOException -> 0x0204, Exception -> 0x01df }
        L_0x0186:
            if (r5 == 0) goto L_0x020e;
        L_0x0188:
            r10 = r5.getStatusLine();	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r10 = r10.getStatusCode();	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r11 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
            if (r10 != r11) goto L_0x020e;
        L_0x0194:
            r10 = com.millennialmedia.android.HandShake.this;	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r11 = com.millennialmedia.android.HandShake.this;	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r12 = r5.getEntity();	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r12 = r12.getContent();	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r12 = com.millennialmedia.android.HttpGetRequest.convertStreamToString(r12);	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r11 = r11.parseJson(r12);	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r10.deserializeFromObj(r11);	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r10 = com.millennialmedia.android.HandShake.this;	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r10.saveHandShake(r9);	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r10 = com.millennialmedia.android.HandShake.this;	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r10 = r10.handler;	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r11 = com.millennialmedia.android.HandShake.this;	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r11 = r11.updateHandShakeRunnable;	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r12 = com.millennialmedia.android.HandShake.this;	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r12 = r12.handShakeCallback;	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r10.postDelayed(r11, r12);	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            r10 = "HandShake";
            r11 = "Obtained a new handshake";
            com.millennialmedia.android.MMLog.v(r10, r11);	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
        L_0x01cc:
            if (r8 == 0) goto L_0x001c;
        L_0x01ce:
            r10 = com.millennialmedia.android.HandShake.this;
            r10.sentFirstLaunch(r9);
            goto L_0x001c;
        L_0x01d5:
            r7 = move-exception;
            r10 = "HandShake";
            r11 = "Could not get a handshake. ";
            com.millennialmedia.android.MMLog.e(r10, r11, r7);	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            goto L_0x00e4;
        L_0x01df:
            r1 = move-exception;
            r10 = "HandShake";
            r11 = "Could not get a handshake. ";
            com.millennialmedia.android.MMLog.e(r10, r11, r1);	 Catch:{ all -> 0x01fb }
            r8 = 0;
            if (r8 == 0) goto L_0x001c;
        L_0x01ea:
            r10 = com.millennialmedia.android.HandShake.this;
            r10.sentFirstLaunch(r9);
            goto L_0x001c;
        L_0x01f1:
            r7 = move-exception;
            r10 = "HandShake";
            r11 = "Could not get a handshake. ";
            com.millennialmedia.android.MMLog.e(r10, r11, r7);	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            goto L_0x013a;
        L_0x01fb:
            r10 = move-exception;
            if (r8 == 0) goto L_0x0203;
        L_0x01fe:
            r11 = com.millennialmedia.android.HandShake.this;
            r11.sentFirstLaunch(r9);
        L_0x0203:
            throw r10;
        L_0x0204:
            r7 = move-exception;
            r10 = "HandShake";
            r11 = "Could not get a handshake. ";
            com.millennialmedia.android.MMLog.e(r10, r11, r7);	 Catch:{ IOException -> 0x0096, Exception -> 0x01df }
            goto L_0x0186;
        L_0x020e:
            r8 = 0;
            goto L_0x01cc;
            */
        }
    }

    class AnonymousClass_3 implements Runnable {
        final /* synthetic */ String val$message;
        final /* synthetic */ Context val$toastContext;

        AnonymousClass_3(Context context, String str) {
            this.val$toastContext = context;
            this.val$message = str;
        }

        public void run() {
            try {
                Toast.makeText(this.val$toastContext, "Error: " + this.val$message, 1).show();
            } catch (BadTokenException e) {
                MMLog.e(TAG, "Error with toast token", e);
            }
        }
    }

    private class AdTypeHandShake {
        boolean downloading;
        long lastVideo;
        long videoInterval;

        AdTypeHandShake() {
            this.lastVideo = 0;
            this.videoInterval = 0;
        }

        boolean canDisplayCachedAd(long cachedTime) {
            return System.currentTimeMillis() - cachedTime < HandShake.this.deferredViewTimeout;
        }

        boolean canRequestVideo(Context context) {
            long time = System.currentTimeMillis();
            MMLog.d(TAG, "canRequestVideo() Current Time:" + time + " Last Video: " + (this.lastVideo / 1000) + "  Diff: " + ((time - this.lastVideo) / 1000) + "  Video interval: " + (this.videoInterval / 1000));
            return System.currentTimeMillis() - this.lastVideo > this.videoInterval;
        }

        void deserializeFromObj(JSONObject adTypeObject) {
            if (adTypeObject != null) {
                this.videoInterval = adTypeObject.optLong("videointerval") * 1000;
            }
        }

        boolean load(SharedPreferences settings, String adType) {
            boolean settingsFound = false;
            if (settings.contains("handshake_lastvideo_" + adType)) {
                this.lastVideo = settings.getLong("handshake_lastvideo_" + adType, this.lastVideo);
                settingsFound = true;
            }
            if (!settings.contains("handshake_videointerval_" + adType)) {
                return settingsFound;
            }
            this.videoInterval = settings.getLong("handshake_videointerval_" + adType, this.videoInterval);
            return true;
        }

        void loadLastVideo(Context context, String adType) {
            SharedPreferences settings = context.getSharedPreferences("MillennialMediaSettings", 0);
            if (settings != null && settings.contains("handshake_lastvideo_" + adType)) {
                this.lastVideo = settings.getLong("handshake_lastvideo_" + adType, this.lastVideo);
            }
        }

        void save(Context context, String adType) {
            Editor editor = context.getSharedPreferences("MillennialMediaSettings", 0).edit();
            save(editor, adType);
            editor.commit();
        }

        void save(Editor editor, String adType) {
            editor.putLong("handshake_lastvideo_" + adType, this.lastVideo);
            editor.putLong("handshake_videointerval_" + adType, this.videoInterval);
        }

        void updateLastVideoViewedTime(Context context, String adType) {
            this.lastVideo = System.currentTimeMillis();
            save(context, adType);
        }
    }

    static class NuanceCredentials {
        String appID;
        String appKey;
        int port;
        String server;
        String sessionID;

        private NuanceCredentials() {
        }

        public String toString() {
            return "Credentials: appid=" + this.appID + " server=" + this.server + " port=" + this.port + " appKey=" + this.appKey + "sessionID=" + this.sessionID;
        }
    }

    private class Scheme {
        int id;
        String scheme;

        Scheme() {
        }

        Scheme(String scheme, int id) {
            this.scheme = scheme;
            this.id = id;
        }

        boolean checkAvailability(Context context) {
            Intent intent;
            if (this.scheme.contains("://")) {
                intent = new Intent("android.intent.action.VIEW", Uri.parse(this.scheme));
            } else {
                intent = new Intent("android.intent.action.VIEW", Uri.parse(this.scheme + "://"));
            }
            return context.getPackageManager().queryIntentActivities(intent, Cast.MAX_MESSAGE_LENGTH).size() > 0;
        }

        void deserializeFromObj(JSONObject schemeObject) {
            if (schemeObject != null) {
                this.scheme = schemeObject.optString("scheme", null);
                this.id = schemeObject.optInt("schemeid");
            }
        }
    }

    static {
        apid = MMSDK.DEFAULT_BANNER_APID;
        handShakeURL = "https://ads.mp.mydas.mobi/appConfigServlet?apid=";
        adUrl = "http://androidsdk.ads.mp.mydas.mobi/getAd.php5?";
    }

    private HandShake() {
        this.handler = new Handler(Looper.getMainLooper());
        this.adTypeHandShakes = new LinkedHashMap();
        this.schemes = new ArrayList();
        this.deferredViewTimeout = 3600000;
        this.kill = false;
        this.handShakeCallback = 86400000;
        this.creativeCacheTimeout = 259200000;
        this.updateHandShakeRunnable = new Runnable() {
            public void run() {
                Context context = (Context) HandShake.this.contextRef.get();
                if (context == null) {
                    context = HandShake.this.appContextRef.get();
                }
                if (context != null) {
                    HandShake.sharedHandShake(context);
                }
            }
        };
    }

    private HandShake(Context context) {
        this.handler = new Handler(Looper.getMainLooper());
        this.adTypeHandShakes = new LinkedHashMap();
        this.schemes = new ArrayList();
        this.deferredViewTimeout = 3600000;
        this.kill = false;
        this.handShakeCallback = 86400000;
        this.creativeCacheTimeout = 259200000;
        this.updateHandShakeRunnable = new Runnable() {
            public void run() {
                Context context = (Context) HandShake.this.contextRef.get();
                if (context == null) {
                    context = HandShake.this.appContextRef.get();
                }
                if (context != null) {
                    HandShake.sharedHandShake(context);
                }
            }
        };
        this.contextRef = new WeakReference(context);
        this.appContextRef = new WeakReference(context.getApplicationContext());
        if (forceRefresh || !loadHandShake(context) || System.currentTimeMillis() - this.lastHandShake > this.handShakeCallback) {
            forceRefresh = false;
            this.lastHandShake = System.currentTimeMillis();
            requestHandshake(false);
        }
    }

    private void deserializeFromObj(JSONObject handShakeObject) {
        Context context = (Context) this.contextRef.get();
        if (context == null) {
            context = this.appContextRef.get();
        }
        if (context == null) {
            MMLog.e(TAG, "No context for handshake");
        } else if (handShakeObject != null) {
            try {
                int i;
                JSONObject jsonObject;
                JSONArray jsonArray = handShakeObject.optJSONArray("errors");
                if (jsonArray != null) {
                    i = 0;
                    while (i < jsonArray.length()) {
                        jsonObject = jsonArray.optJSONObject(i);
                        if (jsonObject != null) {
                            String message = jsonObject.optString("message", null);
                            String type = jsonObject.optString(AnalyticsSQLiteHelper.EVENT_LIST_TYPE, null);
                            if (!(message == null || type == null)) {
                                if (type.equalsIgnoreCase("log")) {
                                    MMLog.e(TAG, message);
                                } else if (type.equalsIgnoreCase("prompt")) {
                                    Context toastContext = context;
                                    Runnable anonymousClass_3 = runnable;
                                    HandShake handShake = this;
                                    this.handler.post(runnable);
                                }
                            }
                        }
                        i++;
                    }
                }
                jsonObject = handShakeObject.optJSONObject("adtypes");
                if (jsonObject != null) {
                    String[] adTypes = MMAdImpl.getAdTypes();
                    i = 0;
                    while (i < adTypes.length) {
                        JSONObject adTypeObject = jsonObject.optJSONObject(adTypes[i]);
                        if (adTypeObject != null) {
                            AdTypeHandShake adTypeHandShake = new AdTypeHandShake();
                            adTypeHandShake.deserializeFromObj(adTypeObject);
                            adTypeHandShake.loadLastVideo(context, adTypes[i]);
                            this.adTypeHandShakes.put(adTypes[i], adTypeHandShake);
                        }
                        i++;
                    }
                }
                synchronized (this) {
                    jsonArray = handShakeObject.optJSONArray("schemes");
                    if (jsonArray != null) {
                        if (this.schemes != null && this.schemes.size() > 0) {
                            this.schemes.removeAll(this.schemes);
                        }
                        i = 0;
                        while (i < jsonArray.length()) {
                            jsonObject = jsonArray.optJSONObject(i);
                            if (jsonObject != null) {
                                Scheme scheme = new Scheme();
                                scheme.deserializeFromObj(jsonObject);
                                this.schemes.add(scheme);
                            }
                            i++;
                        }
                    }
                }
                this.adRefreshSecs = handShakeObject.optLong("adrefresh", 0);
                this.deferredViewTimeout = handShakeObject.optLong("deferredviewtimeout", 3600) * 1000;
                this.kill = handShakeObject.optBoolean("kill");
                setAdUrl(handShakeObject.optString("baseURL"));
                this.handShakeCallback = handShakeObject.optLong("handshakecallback", 86400) * 1000;
                this.creativeCacheTimeout = handShakeObject.optLong("creativeCacheTimeout", 259200) * 1000;
                this.hardwareAccelerationEnabled = handShakeObject.optBoolean("hardwareAccelerationEnabled");
                this.startSessionURL = handShakeObject.optString("startSessionURL");
                this.endSessionURL = handShakeObject.optString("endSessionURL");
                this.nuanceCredentials = (NuanceCredentials) new Gson().fromJson(handShakeObject.optString("nuanceCredentials"), NuanceCredentials.class);
                this.mmjsUrl = handShakeObject.optString("mmjs");
                handleCachedVideos(handShakeObject, context);
                if (TextUtils.isEmpty(this.mmjsUrl) || MRaid.isMRaidUpdated(context, this.mmjsUrl)) {
                    MMLog.w(TAG, "Not downloading MMJS - (" + this.mmjsUrl + ")");
                } else {
                    MRaid.downloadMraidJs((Context) this.appContextRef.get(), this.mmjsUrl);
                }
            } catch (Exception e) {
                MMLog.e(TAG, "Error deserializing handshake", e);
            }
        }
    }

    static String getAdUrl() {
        return (TextUtils.isEmpty(adUrl) || !URLUtil.isHttpUrl(adUrl.replace(BASE_URL_PATH, Preconditions.EMPTY_ARGUMENTS))) ? "http://androidsdk.ads.mp.mydas.mobi/getAd.php5?" : adUrl;
    }

    private void handleCachedVideos(JSONObject handShakeObject, Context context) {
        JSONArray jsonArray = handShakeObject.optJSONArray("cachedVideos");
        if (jsonArray != null) {
            this.cachedVideos = (DTOCachedVideo[]) new Gson().fromJson(jsonArray.toString(), DTOCachedVideo[].class);
            MMLog.d(TAG, this.cachedVideos.toString());
        }
        this.noVideosToCacheURL = handShakeObject.optString("noVideosToCacheURL");
        if (this.cachedVideos != null) {
            PreCacheWorker.preCacheVideos(this.cachedVideos, context, this.noVideosToCacheURL);
        }
    }

    private boolean isFirstLaunch(Context tempContext) {
        return tempContext == null ? false : tempContext.getSharedPreferences("MillennialMediaSettings", 0).getBoolean("firstlaunchHandshake", true);
    }

    private boolean loadHandShake(Context context) {
        boolean z = false;
        SharedPreferences settings = context.getSharedPreferences("MillennialMediaSettings", 0);
        if (settings == null) {
            return false;
        }
        if (settings.contains("handshake_deferredviewtimeout")) {
            this.deferredViewTimeout = settings.getLong("handshake_deferredviewtimeout", this.deferredViewTimeout);
            z = true;
        }
        if (settings.contains("handshake_baseUrl")) {
            adUrl = settings.getString("handshake_baseUrl", adUrl);
            z = true;
        }
        if (settings.contains("handshake_callback")) {
            this.handShakeCallback = settings.getLong("handshake_callback", this.handShakeCallback);
            z = true;
        }
        if (settings.contains("handshake_hardwareAccelerationEnabled")) {
            this.hardwareAccelerationEnabled = settings.getBoolean("handshake_hardwareAccelerationEnabled", false);
            z = true;
        }
        if (settings.contains("handshake_startSessionURL")) {
            this.startSessionURL = settings.getString("handshake_startSessionURL", Preconditions.EMPTY_ARGUMENTS);
            z = true;
        }
        if (settings.contains("handshake_endSessionURL")) {
            this.endSessionURL = settings.getString("handshake_endSessionURL", Preconditions.EMPTY_ARGUMENTS);
            z = true;
        }
        if (settings.contains("handshake_nuanceCredentials")) {
            this.nuanceCredentials = (NuanceCredentials) new Gson().fromJson(settings.getString("handshake_nuanceCredentials", Preconditions.EMPTY_ARGUMENTS), NuanceCredentials.class);
            z = true;
        }
        if (settings.contains("handshake_mmdid")) {
            setMMdid(context, settings.getString("handshake_mmdid", this.mmdid), false);
            z = true;
        }
        if (settings.contains("handshake_creativecachetimeout")) {
            this.creativeCacheTimeout = settings.getLong("handshake_creativecachetimeout", this.creativeCacheTimeout);
            z = true;
        }
        if (settings.contains("handshake_mmjs")) {
            this.mmjsUrl = settings.getString("handshake_mmjs", this.mmjsUrl);
            z = true;
        }
        String[] adTypes = MMAdImpl.getAdTypes();
        int i = 0;
        while (i < adTypes.length) {
            AdTypeHandShake adTypeHandShake = new AdTypeHandShake();
            if (adTypeHandShake.load(settings, adTypes[i])) {
                z = true;
                this.adTypeHandShakes.put(adTypes[i], adTypeHandShake);
            }
            i++;
        }
        synchronized (this) {
            if (settings.contains("handshake_schemes")) {
                String schemesStr = settings.getString("handshake_schemes", Preconditions.EMPTY_ARGUMENTS);
                if (schemesStr.length() > 0) {
                    String[] arr$ = schemesStr.split("\n");
                    int len$ = arr$.length;
                    int i$ = 0;
                    while (i$ < len$) {
                        String[] parts = arr$[i$].split("\t");
                        if (parts.length >= 2) {
                            this.schemes.add(new Scheme(parts[0], Integer.parseInt(parts[1])));
                        }
                        i$++;
                    }
                    z = true;
                }
            }
        }
        if (settings.contains(KEY_CACHED_VIDEOS)) {
            String savedVideos = settings.getString(KEY_CACHED_VIDEOS, Preconditions.EMPTY_ARGUMENTS);
            if (savedVideos.length() > 0) {
                this.cachedVideos = (DTOCachedVideo[]) new Gson().fromJson(savedVideos, DTOCachedVideo[].class);
            }
        }
        if (settings.contains("handshake_lasthandshake")) {
            this.lastHandShake = settings.getLong("handshake_lasthandshake", this.lastHandShake);
            z = true;
        }
        if (z) {
            MMLog.d(TAG, "Handshake successfully loaded from shared preferences.");
            if (System.currentTimeMillis() - this.lastHandShake < this.handShakeCallback) {
                this.handler.postDelayed(this.updateHandShakeRunnable, this.handShakeCallback - System.currentTimeMillis() - this.lastHandShake);
            }
            this.noVideosToCacheURL = settings.getString("handshake_novideostocacheurl", Preconditions.EMPTY_ARGUMENTS);
            if (this.cachedVideos != null) {
                PreCacheWorker.preCacheVideos(this.cachedVideos, context, this.noVideosToCacheURL);
            }
        }
        return z;
    }

    private JSONObject parseJson(String jsonString) {
        MMLog.d(TAG, String.format("JSON String: %s", new Object[]{jsonString}));
        if (jsonString != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                MMLog.v(TAG, jsonObject.toString());
                if (jsonObject.has("mmishake")) {
                    return jsonObject.getJSONObject("mmishake");
                }
            } catch (JSONException e) {
                MMLog.e(TAG, "Error parsing json", e);
            }
        }
        return null;
    }

    private void requestHandshake(boolean isInitialize) {
        Context context = (Context) this.contextRef.get();
        if (context != null) {
            String handShakeUrl = context.getSharedPreferences("MillennialMediaSettings", 0).getString("handShakeUrl", null);
            if (handShakeUrl != null) {
                setHandShakeURL(handShakeUrl);
            }
        }
        ThreadUtils.execute(new AnonymousClass_1(isInitialize));
    }

    private void saveHandShake(Context context) {
        Editor editor = context.getSharedPreferences("MillennialMediaSettings", 0).edit();
        editor.putLong("handshake_deferredviewtimeout", this.deferredViewTimeout);
        editor.putBoolean("handshake_kill", this.kill);
        editor.putString("handshake_baseUrl", adUrl);
        editor.putLong("handshake_callback", this.handShakeCallback);
        editor.putBoolean("handshake_hardwareAccelerationEnabled", this.hardwareAccelerationEnabled);
        editor.putString("handshake_startSessionURL", this.startSessionURL);
        if (this.nuanceCredentials != null) {
            editor.putString("handshake_nuanceCredentials", new Gson().toJson(this.nuanceCredentials));
        }
        editor.putString("handshake_endSessionURL", this.endSessionURL);
        editor.putLong("handshake_creativecaetimeout", this.creativeCacheTimeout);
        editor.putString("handshake_mmjs", this.mmjsUrl);
        Iterator i$ = this.adTypeHandShakes.keySet().iterator();
        while (i$.hasNext()) {
            String adType = (String) i$.next();
            ((AdTypeHandShake) this.adTypeHandShakes.get(adType)).save(editor, adType);
        }
        synchronized (this) {
            StringBuilder stringBuilder = new StringBuilder();
            int i = 0;
            while (i < this.schemes.size()) {
                Scheme scheme = (Scheme) this.schemes.get(i);
                if (i > 0) {
                    stringBuilder.append("\n");
                }
                stringBuilder.append(scheme.scheme + "\t" + scheme.id);
                i++;
            }
            editor.putString("handshake_schemes", stringBuilder.toString());
        }
        if (this.cachedVideos != null) {
            editor.putString(KEY_CACHED_VIDEOS, new Gson().toJson(this.cachedVideos));
        }
        editor.putString("handshake_novideostocacheurl", this.noVideosToCacheURL);
        editor.putLong("handshake_lasthandshake", this.lastHandShake);
        editor.commit();
    }

    private void sentFirstLaunch(Context tempContext) {
        if (tempContext != null) {
            Editor editor = tempContext.getSharedPreferences("MillennialMediaSettings", 0).edit();
            editor.putBoolean("firstlaunchHandshake", false);
            editor.commit();
        }
    }

    static void setAdUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            if (url.endsWith("/")) {
                adUrl = url + BASE_URL_PATH;
            } else {
                adUrl = url + "/" + BASE_URL_PATH;
            }
        }
    }

    static synchronized void setHandShakeURL(Context context, String url) {
        synchronized (HandShake.class) {
            if (setHandShakeURL(url)) {
                forceRefresh = true;
                sharedInstance = new HandShake(context);
            }
        }
    }

    static synchronized boolean setHandShakeURL(String url) {
        boolean z;
        synchronized (HandShake.class) {
            if (TextUtils.isEmpty(url)) {
                z = false;
            } else {
                if (url.startsWith(HANDSHAKE_HTTP_SCHEME)) {
                    url = url.replaceFirst(HANDSHAKE_HTTP_SCHEME, HANDSHAKE_HTTPS_SCHEME);
                }
                handShakeURL = url + HANDSHAKE_URL_OVERRIDE_PARMS;
                z = true;
            }
        }
        return z;
    }

    static synchronized HandShake sharedHandShake(Context context) {
        HandShake handShake;
        synchronized (HandShake.class) {
            if (apid == null) {
                MMLog.e(TAG, "No apid set for the handshake.");
                handShake = null;
            } else {
                if (sharedInstance == null) {
                    sharedInstance = new HandShake(context);
                } else if (System.currentTimeMillis() - sharedInstance.lastHandShake > sharedInstance.handShakeCallback) {
                    MMLog.d(TAG, "Handshake expired, requesting new handshake from the server.");
                    sharedInstance = new HandShake(context);
                }
                handShake = sharedInstance;
            }
        }
        return handShake;
    }

    synchronized boolean canDisplayCachedAd(String adType, long cachedTime) {
        boolean canDisplayCachedAd;
        AdTypeHandShake adTypeHandShake = (AdTypeHandShake) this.adTypeHandShakes.get(adType);
        if (adTypeHandShake != null) {
            canDisplayCachedAd = adTypeHandShake.canDisplayCachedAd(cachedTime);
        } else {
            canDisplayCachedAd = true;
        }
        return canDisplayCachedAd;
    }

    synchronized boolean canRequestVideo(Context context, String adType) {
        boolean canRequestVideo;
        AdTypeHandShake adTypeHandShake = (AdTypeHandShake) this.adTypeHandShakes.get(adType);
        if (adTypeHandShake != null) {
            canRequestVideo = adTypeHandShake.canRequestVideo(context);
        } else {
            canRequestVideo = true;
        }
        return canRequestVideo;
    }

    void endSession() {
        if (!TextUtils.isEmpty(this.endSessionURL)) {
            HttpUtils.executeUrl(this.endSessionURL);
        }
    }

    synchronized JSONArray getSchemesJSONArray(Context context) {
        JSONArray array;
        array = new JSONArray();
        if (this.schemes.size() > 0) {
            Iterator i$ = this.schemes.iterator();
            while (i$.hasNext()) {
                Scheme scheme = (Scheme) i$.next();
                if (scheme.checkAvailability(context)) {
                    try {
                        JSONObject schemeObject = new JSONObject();
                        schemeObject.put("scheme", scheme.scheme);
                        schemeObject.put("schemeid", scheme.id);
                        array.put(schemeObject);
                    } catch (JSONException e) {
                        MMLog.e(TAG, "Json error getting scheme", e);
                    }
                }
            }
        }
        return array;
    }

    synchronized String getSchemesList(Context context) {
        if (this.schemesList == null && this.schemes.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            Iterator i$ = this.schemes.iterator();
            while (i$.hasNext()) {
                Scheme scheme = (Scheme) i$.next();
                if (scheme.checkAvailability(context)) {
                    if (stringBuilder.length() > 0) {
                        stringBuilder.append("," + scheme.id);
                    } else {
                        stringBuilder.append(Integer.toString(scheme.id));
                    }
                }
            }
            if (stringBuilder.length() > 0) {
                this.schemesList = stringBuilder.toString();
            }
        }
        return this.schemesList;
    }

    synchronized boolean isAdTypeDownloading(String adType) {
        boolean z;
        AdTypeHandShake adTypeHandShake = (AdTypeHandShake) this.adTypeHandShakes.get(adType);
        if (adTypeHandShake != null) {
            z = adTypeHandShake.downloading;
        } else {
            z = false;
        }
        return z;
    }

    synchronized void lockAdTypeDownload(String adType) {
        AdTypeHandShake adTypeHandShake = (AdTypeHandShake) this.adTypeHandShakes.get(adType);
        if (adTypeHandShake != null) {
            adTypeHandShake.downloading = true;
        }
    }

    void sendInitRequest() {
        requestHandshake(true);
    }

    void setMMdid(Context context, String newMMdid) {
        setMMdid(context, newMMdid, true);
    }

    synchronized void setMMdid(Context context, String newMMdid, boolean persist) {
        if (newMMdid != null) {
            if (newMMdid.length() == 0 || newMMdid.equals("NULL")) {
                this.mmdid = null;
                MMSDK.setMMdid(this.mmdid);
                if (persist) {
                    Editor editor = context.getSharedPreferences("MillennialMediaSettings", 0).edit();
                    editor.putString("handshake_mmdid", this.mmdid);
                    editor.commit();
                }
            }
        }
        this.mmdid = newMMdid;
        MMSDK.setMMdid(this.mmdid);
        if (persist) {
            Editor editor2 = context.getSharedPreferences("MillennialMediaSettings", 0).edit();
            editor2.putString("handshake_mmdid", this.mmdid);
            editor2.commit();
        }
    }

    void startSession() {
        if (!TextUtils.isEmpty(this.startSessionURL)) {
            HttpUtils.executeUrl(this.startSessionURL);
        }
    }

    synchronized void unlockAdTypeDownload(String adType) {
        AdTypeHandShake adTypeHandShake = (AdTypeHandShake) this.adTypeHandShakes.get(adType);
        if (adTypeHandShake != null) {
            adTypeHandShake.downloading = false;
        }
    }

    synchronized void updateLastVideoViewedTime(Context context, String adType) {
        AdTypeHandShake adTypeHandShake = (AdTypeHandShake) this.adTypeHandShakes.get(adType);
        if (adTypeHandShake != null) {
            adTypeHandShake.updateLastVideoViewedTime(context, adType);
        }
    }
}