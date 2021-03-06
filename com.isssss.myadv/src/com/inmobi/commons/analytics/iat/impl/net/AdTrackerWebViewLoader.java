package com.inmobi.commons.analytics.iat.impl.net;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.net.http.SslError;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.inmobi.commons.analytics.iat.impl.AdTrackerConstants;
import com.inmobi.commons.analytics.iat.impl.AdTrackerUtils;
import com.inmobi.commons.analytics.iat.impl.Goal;
import com.inmobi.commons.analytics.iat.impl.config.AdTrackerEventType;
import com.inmobi.commons.analytics.iat.impl.config.AdTrackerInitializer;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.inmobi.commons.internal.FileOperations;
import com.inmobi.commons.internal.InternalSDKUtil;
import com.inmobi.commons.internal.Log;
import com.millennialmedia.android.MMAdView;
import java.net.URLDecoder;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONObject;

public class AdTrackerWebViewLoader {
    private static WebView a;
    private static AtomicBoolean b;
    private long c;
    private long d;
    private Goal e;

    protected static class JSInterface {
        protected JSInterface() {
        }

        @JavascriptInterface
        public String getParams() {
            String preferences = FileOperations.getPreferences(InternalSDKUtil.getContext(), AdTrackerConstants.IMPREF_FILE, AdTrackerConstants.REFERRER);
            String webViewRequestParam = AdTrackerRequestResponseBuilder.getWebViewRequestParam();
            if (preferences != null) {
                webViewRequestParam = webViewRequestParam + "&referrer=" + preferences;
            }
            Log.debug(AdTrackerConstants.IAT_LOGGING_TAG, "Request param for webview" + webViewRequestParam);
            return webViewRequestParam;
        }
    }

    protected class MyWebViewClient extends WebViewClient {
        protected MyWebViewClient() {
        }

        public void onReceivedError(WebView webView, int i, String str, String str2) {
            try {
                if (b.compareAndSet(true, false)) {
                    AdTrackerNetworkInterface.setWebviewUploadStatus(false);
                    synchronized (AdTrackerNetworkInterface.getNetworkThread()) {
                        AdTrackerNetworkInterface.getNetworkThread().notify();
                    }
                    AdTrackerUtils.reportMetric(AdTrackerEventType.GOAL_FAILURE, AdTrackerWebViewLoader.this.e, 0, 0, i, null);
                    Log.internal(AdTrackerConstants.IAT_LOGGING_TAG, "Webview Received Error");
                }
            } catch (Exception e) {
                Log.internal(AdTrackerConstants.IAT_LOGGING_TAG, "Exception onReceived Error", e);
            }
            super.onReceivedError(webView, i, str, str2);
        }

        @TargetApi(8)
        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
            Log.internal(AdTrackerConstants.IAT_LOGGING_TAG, "SSL Error. Proceeding" + sslError);
            try {
                if (b.compareAndSet(true, false)) {
                    AdTrackerNetworkInterface.setWebviewUploadStatus(false);
                    synchronized (AdTrackerNetworkInterface.getNetworkThread()) {
                        AdTrackerNetworkInterface.getNetworkThread().notify();
                    }
                    AdTrackerUtils.reportMetric(AdTrackerEventType.GOAL_FAILURE, AdTrackerWebViewLoader.this.e, 0, 0, sslError.getPrimaryError(), null);
                    Log.internal(AdTrackerConstants.IAT_LOGGING_TAG, "Webview Received SSL Error");
                }
            } catch (Exception e) {
                Log.internal(AdTrackerConstants.IAT_LOGGING_TAG, "Exception onReceived SSL Error", e);
            }
            sslErrorHandler.proceed();
        }

        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            try {
                if (b.compareAndSet(true, false)) {
                    AdTrackerWebViewLoader.this.d = System.currentTimeMillis() - AdTrackerWebViewLoader.this.c;
                    if (str.contains(AdTrackerInitializer.PRODUCT_IAT)) {
                        d a = AdTrackerWebViewLoader.this.a(str.substring(ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES));
                        if (a == null) {
                            AdTrackerNetworkInterface.setWebviewUploadStatus(false);
                        } else {
                            FileOperations.setPreferences(InternalSDKUtil.getContext(), AdTrackerConstants.IMPREF_FILE, AdTrackerConstants.ERRORCODE, Integer.toString(AdTrackerWebViewLoader.this));
                            if (5000 != AdTrackerWebViewLoader.this) {
                                AdTrackerNetworkInterface.setWebviewUploadStatus(false);
                                AdTrackerUtils.reportMetric(AdTrackerEventType.GOAL_FAILURE, AdTrackerWebViewLoader.this.e, 0, 0, AdTrackerWebViewLoader.this, null);
                            } else if (AdTrackerWebViewLoader.this.b(a.b)) {
                                AdTrackerNetworkInterface.setWebviewUploadStatus(true);
                            } else {
                                AdTrackerNetworkInterface.setWebviewUploadStatus(false);
                            }
                        }
                    }
                    synchronized (AdTrackerNetworkInterface.getNetworkThread()) {
                        AdTrackerNetworkInterface.getNetworkThread().notify();
                    }
                }
                webView.loadUrl(str);
            } catch (Exception e) {
                Log.internal(AdTrackerConstants.IAT_LOGGING_TAG, "Exception Should Override Error", e);
            }
            return true;
        }
    }

    class a implements Runnable {
        a() {
        }

        public void run() {
            if (a != null) {
                a.stopLoading();
                a.destroy();
                a = null;
                b.set(false);
            }
        }
    }

    class b implements Runnable {
        b() {
        }

        public void run() {
            AdTrackerWebViewLoader.this.c = System.currentTimeMillis();
            b.set(true);
            Log.internal(AdTrackerConstants.IAT_LOGGING_TAG, "Load Webview: " + AdTrackerNetworkInterface.b());
            a.loadUrl(AdTrackerNetworkInterface.b());
        }
    }

    class c implements Runnable {
        c() {
        }

        public void run() {
            if (a != null) {
                b.set(false);
            }
            a.stopLoading();
        }
    }

    private static class d {
        private int a;
        private String b;

        public d() {
            this.a = 0;
            this.b = null;
        }
    }

    class e implements Runnable {
        e() {
        }

        public void run() {
            b = new AtomicBoolean(false);
            a = new WebView(InternalSDKUtil.getContext());
            a.setWebViewClient(new MyWebViewClient());
            a.getSettings().setJavaScriptEnabled(true);
            a.getSettings().setCacheMode(MMAdView.TRANSITION_UP);
            a.addJavascriptInterface(new JSInterface(), AdTrackerConstants.IATNAMESPACE);
        }
    }

    static {
        a = null;
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    public AdTrackerWebViewLoader() {
        this.c = 0;
        this.d = 0;
        AdTrackerNetworkInterface.getUIHandler().post(new e());
    }

    private d a(String str) {
        d dVar = null;
        int i = 0;
        d dVar2 = new d();
        try {
            int i2;
            String str2;
            String[] split = str.split("&");
            int i3 = i;
            d dVar3 = dVar;
            int i4 = i;
            while (i3 < split.length) {
                String[] split2 = split[i3].split("=");
                i2 = i;
                while (i2 < split2.length) {
                    if (AdTrackerConstants.ERROR.equals(split2[i2])) {
                        i4 = Integer.parseInt(split2[i2 + 1]);
                    } else if (AdTrackerConstants.RESPONSE.equals(split2[i2])) {
                        str2 = split2[i2 + 1];
                    }
                    i2++;
                }
                i3++;
            }
            dVar2.a = i4;
            dVar2.b = str2;
            if (5003 == i4) {
                Log.internal(AdTrackerConstants.IAT_LOGGING_TAG, "Webview Timeout " + str2);
            } else if (5001 == i4) {
                Log.internal(AdTrackerConstants.IAT_LOGGING_TAG, "Invalid params passed " + str2);
            } else if (5002 == i4) {
                Log.internal(AdTrackerConstants.IAT_LOGGING_TAG, "XMLHTTP request not supported " + str2);
            } else if (5005 == i4) {
                Log.internal(AdTrackerConstants.IAT_LOGGING_TAG, "Invalid JSON Response " + str2);
            } else if (5004 == i4) {
                Log.internal(AdTrackerConstants.IAT_LOGGING_TAG, "Webview Server Error " + str2);
            } else if (5000 == i4) {
                Log.internal(AdTrackerConstants.IAT_LOGGING_TAG, "Webview response " + URLDecoder.decode(str2, "utf-8"));
            }
            return dVar2;
        } catch (Exception e) {
            th = e;
            i2 = i4;
        }
    }

    private boolean b(String str) {
        try {
            JSONObject jSONObject = new JSONObject(URLDecoder.decode(str, "utf-8"));
            JSONObject jSONObject2 = jSONObject.getJSONObject(AdTrackerConstants.VALIDIDS);
            String string = jSONObject.getString(AdTrackerConstants.ERRORMSG);
            int i = jSONObject.getInt(AdTrackerConstants.TIMETOLIVE);
            int i2 = jSONObject.getInt(AdTrackerConstants.ERRORCODE);
            if (i2 != 6000) {
                AdTrackerUtils.reportMetric(AdTrackerEventType.GOAL_FAILURE, this.e, 0, 0, i2, null);
                Log.debug(AdTrackerConstants.IAT_LOGGING_TAG, "Failed to upload goal in webview" + string);
                return false;
            } else {
                AdTrackerUtils.reportMetric(AdTrackerEventType.GOAL_SUCCESS, this.e, 0, this.d, 0, null);
                String toString = jSONObject2.toString();
                if (i2 == 6001) {
                    toString = null;
                }
                FileOperations.setPreferences(InternalSDKUtil.getContext(), AdTrackerConstants.IMPREF_FILE, AdTrackerConstants.VALIDIDS, toString);
                FileOperations.setPreferences(InternalSDKUtil.getContext(), AdTrackerConstants.IMPREF_FILE, AdTrackerConstants.TIMETOLIVE, Integer.toString(i));
                return true;
            }
        } catch (Exception e) {
            Throwable th = e;
            AdTrackerUtils.reportMetric(AdTrackerEventType.GOAL_FAILURE, this.e, 0, 0, AdTrackerConstants.EXCEPTION, th.getMessage());
            Log.internal(AdTrackerConstants.IAT_LOGGING_TAG, "Failed to upload goal in webview", th);
            return false;
        }
    }

    public static WebView getWebview() {
        return a;
    }

    public static boolean isWebviewLoading() {
        return b.get();
    }

    public void deinit(int i) {
        AdTrackerNetworkInterface.getUIHandler().postDelayed(new a(), (long) i);
    }

    public boolean loadWebview(Goal goal) {
        this.e = goal;
        try {
            AdTrackerNetworkInterface.getUIHandler().post(new b());
            return true;
        } catch (Exception e) {
            Log.internal(AdTrackerConstants.IAT_LOGGING_TAG, e.toString());
            return false;
        }
    }

    public void stopLoading() {
        AdTrackerNetworkInterface.getUIHandler().post(new c());
    }
}