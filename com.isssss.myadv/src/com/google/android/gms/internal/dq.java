package com.google.android.gms.internal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.net.UrlQuerySanitizer.ParameterValuePair;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.google.android.gms.ads.AdActivity;
import com.google.android.gms.cast.Cast;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class dq {
    private static final Object px;
    private static boolean re;
    private static String rf;
    private static boolean rg;

    static class AnonymousClass_1 implements Runnable {
        final /* synthetic */ Context pB;

        AnonymousClass_1(Context context) {
            this.pB = context;
        }

        public void run() {
            synchronized (px) {
                rf = dq.j(this.pB);
                px.notifyAll();
            }
        }
    }

    private static final class a extends BroadcastReceiver {
        private a() {
        }

        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.USER_PRESENT".equals(intent.getAction())) {
                re = true;
            } else if ("android.intent.action.SCREEN_OFF".equals(intent.getAction())) {
                re = false;
            }
        }
    }

    static {
        px = new Object();
        re = true;
        rg = false;
    }

    public static String a(Readable readable) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        CharSequence allocate = CharBuffer.allocate(AccessibilityNodeInfoCompat.ACTION_PREVIOUS_HTML_ELEMENT);
        while (true) {
            int read = readable.read(allocate);
            if (read == -1) {
                return stringBuilder.toString();
            }
            allocate.flip();
            stringBuilder.append(allocate, 0, read);
        }
    }

    private static JSONArray a(Collection<?> collection) throws JSONException {
        JSONArray jSONArray = new JSONArray();
        Iterator it = collection.iterator();
        while (it.hasNext()) {
            a(jSONArray, it.next());
        }
        return jSONArray;
    }

    static JSONArray a(Object[] objArr) throws JSONException {
        JSONArray jSONArray = new JSONArray();
        int length = objArr.length;
        int i = 0;
        while (i < length) {
            a(jSONArray, objArr[i]);
            i++;
        }
        return jSONArray;
    }

    private static JSONObject a(Bundle bundle) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        Iterator it = bundle.keySet().iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            a(jSONObject, str, bundle.get(str));
        }
        return jSONObject;
    }

    public static void a(Context context, String str, WebSettings webSettings) {
        webSettings.setUserAgentString(b(context, str));
    }

    public static void a(Context context, String str, List<String> list) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            new du(context, str, (String) it.next()).start();
        }
    }

    public static void a(Context context, String str, boolean z, HttpURLConnection httpURLConnection) {
        httpURLConnection.setConnectTimeout(60000);
        httpURLConnection.setInstanceFollowRedirects(z);
        httpURLConnection.setReadTimeout(60000);
        httpURLConnection.setRequestProperty("User-Agent", b(context, str));
        httpURLConnection.setUseCaches(false);
    }

    public static void a(WebView webView) {
        if (VERSION.SDK_INT >= 11) {
            ds.a(webView);
        }
    }

    private static void a(JSONArray jSONArray, Object obj) throws JSONException {
        if (obj instanceof Bundle) {
            jSONArray.put(a((Bundle) obj));
        } else if (obj instanceof Map) {
            jSONArray.put(p((Map) obj));
        } else if (obj instanceof Collection) {
            jSONArray.put(a((Collection) obj));
        } else if (obj instanceof Object[]) {
            jSONArray.put(a((Object[]) obj));
        } else {
            jSONArray.put(obj);
        }
    }

    private static void a(JSONObject jSONObject, String str, Object obj) throws JSONException {
        if (obj instanceof Bundle) {
            jSONObject.put(str, a((Bundle) obj));
        } else if (obj instanceof Map) {
            jSONObject.put(str, p((Map) obj));
        } else if (obj instanceof Collection) {
            if (str == null) {
                str = "null";
            }
            jSONObject.put(str, a((Collection) obj));
        } else if (obj instanceof Object[]) {
            jSONObject.put(str, a(Arrays.asList((Object[]) obj)));
        } else {
            jSONObject.put(str, obj);
        }
    }

    public static boolean a(PackageManager packageManager, String str, String str2) {
        return packageManager.checkPermission(str2, str) == 0;
    }

    public static boolean a(ClassLoader classLoader, Class<?> cls, String str) {
        try {
            return cls.isAssignableFrom(Class.forName(str, false, classLoader));
        } catch (Throwable th) {
            return false;
        }
    }

    private static String b(Context context, String str) {
        String str2;
        synchronized (px) {
            if (rf != null) {
                str2 = rf;
            } else {
                if (VERSION.SDK_INT >= 17) {
                    rf = dt.getDefaultUserAgent(context);
                } else if (dv.bD()) {
                    rf = j(context);
                } else {
                    dv.rp.post(new AnonymousClass_1(context));
                    while (rf == null) {
                        try {
                            px.wait();
                        } catch (InterruptedException e) {
                            str2 = rf;
                        }
                    }
                }
                rf += " (Mobile; " + str + ")";
                str2 = rf;
            }
        }
        return str2;
    }

    public static Map<String, String> b(Uri uri) {
        if (uri == null) {
            return null;
        }
        HashMap hashMap = new HashMap();
        UrlQuerySanitizer urlQuerySanitizer = new UrlQuerySanitizer();
        urlQuerySanitizer.setAllowUnregisteredParamaters(true);
        urlQuerySanitizer.setUnregisteredParameterValueSanitizer(UrlQuerySanitizer.getAllButNulLegal());
        urlQuerySanitizer.parseUrl(uri.toString());
        Iterator it = urlQuerySanitizer.getParameterList().iterator();
        while (it.hasNext()) {
            ParameterValuePair parameterValuePair = (ParameterValuePair) it.next();
            hashMap.put(parameterValuePair.mParameter, parameterValuePair.mValue);
        }
        return hashMap;
    }

    public static void b(WebView webView) {
        if (VERSION.SDK_INT >= 11) {
            ds.b(webView);
        }
    }

    public static int bA() {
        return VERSION.SDK_INT >= 9 ? ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES : 1;
    }

    public static boolean by() {
        return re;
    }

    public static int bz() {
        return VERSION.SDK_INT >= 9 ? ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES : 0;
    }

    public static boolean h(Context context) {
        Intent intent = new Intent();
        intent.setClassName(context, AdActivity.CLASS_NAME);
        ResolveInfo resolveActivity = context.getPackageManager().resolveActivity(intent, Cast.MAX_MESSAGE_LENGTH);
        if (resolveActivity == null || resolveActivity.activityInfo == null) {
            dw.z("Could not find com.google.android.gms.ads.AdActivity, please make sure it is declared in AndroidManifest.xml.");
            return false;
        } else {
            boolean z;
            String str = "com.google.android.gms.ads.AdActivity requires the android:configChanges value to contain \"%s\".";
            if ((resolveActivity.activityInfo.configChanges & 16) == 0) {
                dw.z(String.format(str, new Object[]{"keyboard"}));
                z = false;
            } else {
                z = true;
            }
            if ((resolveActivity.activityInfo.configChanges & 32) == 0) {
                dw.z(String.format(str, new Object[]{"keyboardHidden"}));
                z = false;
            }
            if ((resolveActivity.activityInfo.configChanges & 128) == 0) {
                dw.z(String.format(str, new Object[]{"orientation"}));
                z = false;
            }
            if ((resolveActivity.activityInfo.configChanges & 256) == 0) {
                dw.z(String.format(str, new Object[]{"screenLayout"}));
                z = false;
            }
            if ((resolveActivity.activityInfo.configChanges & 512) == 0) {
                dw.z(String.format(str, new Object[]{"uiMode"}));
                z = false;
            }
            if ((resolveActivity.activityInfo.configChanges & 1024) == 0) {
                dw.z(String.format(str, new Object[]{"screenSize"}));
                z = false;
            }
            if ((resolveActivity.activityInfo.configChanges & 2048) != 0) {
                return z;
            }
            dw.z(String.format(str, new Object[]{"smallestScreenSize"}));
            return false;
        }
    }

    public static void i(Context context) {
        if (!rg) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.USER_PRESENT");
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
            context.getApplicationContext().registerReceiver(new a(), intentFilter);
            rg = true;
        }
    }

    private static String j(Context context) {
        return new WebView(context).getSettings().getUserAgentString();
    }

    public static JSONObject p(Map<String, ?> map) throws JSONException {
        try {
            JSONObject jSONObject = new JSONObject();
            Iterator it = map.keySet().iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                a(jSONObject, str, map.get(str));
            }
            return jSONObject;
        } catch (ClassCastException e) {
            throw new JSONException("Could not convert map to JSON: " + e.getMessage());
        }
    }

    public static String r(String str) {
        return Uri.parse(str).buildUpon().query(null).build().toString();
    }
}