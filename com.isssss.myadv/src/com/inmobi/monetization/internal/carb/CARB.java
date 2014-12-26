package com.inmobi.monetization.internal.carb;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.media.TransportMediator;
import android.telephony.TelephonyManager;
import com.inmobi.commons.InMobi;
import com.inmobi.commons.data.DeviceInfo;
import com.inmobi.commons.internal.EncryptionUtils;
import com.inmobi.commons.internal.InternalSDKUtil;
import com.inmobi.commons.internal.Log;
import com.inmobi.commons.thinICE.icedatacollector.IceDataCollector;
import com.inmobi.commons.thinICE.wifi.WifiInfo;
import com.inmobi.commons.uid.UID;
import com.inmobi.monetization.internal.Constants;
import com.inmobi.monetization.internal.configs.PkInitilaizer;
import com.mopub.common.Preconditions;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONArray;

public class CARB {
    public static final String LOGGING_TAG = "[InMobi]-[CARB]-4.5.0";
    private static CARB g;
    private static SharedPreferences j;
    private static AtomicBoolean k;
    private static Thread l;
    private static AtomicBoolean m;
    private static Thread n;
    private static String o;
    private static String p;
    private static String q;
    byte[] a;
    byte[] b;
    String c;
    String d;
    String e;
    CarbCallback f;
    private String h;
    private String i;
    private final int r;
    private final int s;
    private byte[] t;
    private byte[] u;
    private byte[] v;
    private byte[] w;
    private ArrayList<CarbInfo> x;

    public static interface CarbCallback {
        void postFailed();

        void postSuccess();
    }

    class a implements Runnable {
        int a;

        a() {
            this.a = 1;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private int a(java.lang.String r11) {
            throw new UnsupportedOperationException("Method not decompiled: com.inmobi.monetization.internal.carb.CARB.a.a(java.lang.String):int");
            /*
            r10 = this;
            r4 = 0;
            r2 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
            r5 = 0;
            r1 = -1;
            r0 = com.inmobi.commons.internal.InternalSDKUtil.getContext();
            r0 = com.inmobi.commons.internal.InternalSDKUtil.checkNetworkAvailibility(r0);
            if (r0 != 0) goto L_0x0011;
        L_0x000f:
            r0 = r1;
        L_0x0010:
            return r0;
        L_0x0011:
            r3 = new java.net.URL;	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r0 = com.inmobi.monetization.internal.carb.CarbInitializer.getConfigParams();	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r0 = r0.getCarbEndpoint();	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r3.<init>(r0);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r0 = r3.openConnection();	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r0 = (java.net.HttpURLConnection) r0;	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r6 = "User-Agent";
            r7 = com.inmobi.commons.internal.InternalSDKUtil.getUserAgent();	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r0.setRequestProperty(r6, r7);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r6 = com.inmobi.monetization.internal.carb.CarbInitializer.getConfigParams();	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r6 = r6.getTimeoutInterval();	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r8 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
            r6 = r6 * r8;
            r8 = (int) r6;	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r0.setConnectTimeout(r8);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r6 = (int) r6;	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r0.setReadTimeout(r6);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r6 = "user-agent";
            r7 = com.inmobi.commons.data.DeviceInfo.getPhoneDefaultUserAgent();	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r0.setRequestProperty(r6, r7);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r6 = 0;
            r0.setUseCaches(r6);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r6 = 1;
            r0.setDoOutput(r6);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r6 = 1;
            r0.setDoInput(r6);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r6 = "POST";
            r0.setRequestMethod(r6);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r6 = "content-type";
            r7 = "application/x-www-form-urlencoded";
            r0.setRequestProperty(r6, r7);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r6 = "Content-Length";
            r7 = r11.length();	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r7 = java.lang.Integer.toString(r7);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r0.setRequestProperty(r6, r7);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r6 = r3.getHost();	 Catch:{ UnknownHostException -> 0x00f5 }
            java.net.InetAddress.getByName(r6);	 Catch:{ UnknownHostException -> 0x00f5 }
            r3 = new java.io.BufferedWriter;	 Catch:{ all -> 0x0139 }
            r6 = new java.io.OutputStreamWriter;	 Catch:{ all -> 0x0139 }
            r7 = r0.getOutputStream();	 Catch:{ all -> 0x0139 }
            r6.<init>(r7);	 Catch:{ all -> 0x0139 }
            r3.<init>(r6);	 Catch:{ all -> 0x0139 }
            r3.write(r11);	 Catch:{ all -> 0x0260 }
            if (r3 == 0) goto L_0x008b;
        L_0x0088:
            r3.close();	 Catch:{ IOException -> 0x0115, MalformedURLException -> 0x00ea, JSONException -> 0x0130 }
        L_0x008b:
            r3 = "[InMobi]-[CARB]-4.5.0";
            r6 = new java.lang.StringBuilder;	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r6.<init>();	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r7 = "Get CARB list status: ";
            r6 = r6.append(r7);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r7 = r0.getResponseCode();	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r6 = r6.append(r7);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r6 = r6.toString();	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            com.inmobi.commons.internal.Log.internal(r3, r6);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r3 = r0.getResponseCode();	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            if (r3 != r2) goto L_0x025a;
        L_0x00ad:
            r3 = j;	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r3 = r3.edit();	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r6 = com.inmobi.monetization.internal.carb.CARB.this;	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r6 = r6.h;	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r7 = java.lang.System.currentTimeMillis();	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r3.putLong(r6, r7);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r3.commit();	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r3 = new java.io.BufferedReader;	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r6 = new java.io.InputStreamReader;	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r0 = r0.getInputStream();	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r7 = "UTF-8";
            r6.<init>(r0, r7);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r3.<init>(r6);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r0 = new java.lang.StringBuilder;	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r0.<init>();	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
        L_0x00da:
            r6 = r3.readLine();	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            if (r6 == 0) goto L_0x015b;
        L_0x00e0:
            r6 = r0.append(r6);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r7 = "\n";
            r6.append(r7);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            goto L_0x00da;
        L_0x00ea:
            r0 = move-exception;
            r0 = "[InMobi]-[CARB]-4.5.0";
            r2 = "Malformed URL";
            com.inmobi.commons.internal.Log.internal(r0, r2);
        L_0x00f2:
            r0 = r1;
            goto L_0x0010;
        L_0x00f5:
            r0 = move-exception;
            r0 = new java.net.MalformedURLException;	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r2 = new java.lang.StringBuilder;	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r2.<init>();	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r4 = "Malformed URL: ";
            r2 = r2.append(r4);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r3 = r3.toString();	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r2 = r2.append(r3);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r2 = r2.toString();	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r0.<init>(r2);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            throw r0;	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
        L_0x0113:
            r0 = move-exception;
            goto L_0x00f2;
        L_0x0115:
            r6 = move-exception;
            r7 = "[InMobi]-[Monetization]";
            r8 = new java.lang.StringBuilder;	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r8.<init>();	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r9 = "Exception closing resource: ";
            r8 = r8.append(r9);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r3 = r8.append(r3);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r3 = r3.toString();	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            com.inmobi.commons.internal.Log.internal(r7, r3, r6);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            goto L_0x008b;
        L_0x0130:
            r0 = move-exception;
            r0 = "[InMobi]-[CARB]-4.5.0";
            r2 = "Invalid JSON response";
            com.inmobi.commons.internal.Log.internal(r0, r2);
            goto L_0x00f2;
        L_0x0139:
            r0 = move-exception;
            r2 = r4;
        L_0x013b:
            if (r2 == 0) goto L_0x0140;
        L_0x013d:
            r2.close();	 Catch:{ IOException -> 0x0141, MalformedURLException -> 0x00ea, JSONException -> 0x0130 }
        L_0x0140:
            throw r0;	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
        L_0x0141:
            r3 = move-exception;
            r4 = "[InMobi]-[Monetization]";
            r5 = new java.lang.StringBuilder;	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r5.<init>();	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r6 = "Exception closing resource: ";
            r5 = r5.append(r6);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r2 = r5.append(r2);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r2 = r2.toString();	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            com.inmobi.commons.internal.Log.internal(r4, r2, r3);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            goto L_0x0140;
        L_0x015b:
            r0 = r0.toString();	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r3 = "[InMobi]-[CARB]-4.5.0";
            r6 = new java.lang.StringBuilder;	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r6.<init>();	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r7 = "RESPONSE: ";
            r6 = r6.append(r7);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r6 = r6.append(r0);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r6 = r6.toString();	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            com.inmobi.commons.internal.Log.internal(r3, r6);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r0 = r0.getBytes();	 Catch:{ Exception -> 0x01dd }
            r3 = 0;
            r0 = com.inmobi.commons.internal.Base64.decode(r0, r3);	 Catch:{ Exception -> 0x01dd }
            r3 = com.inmobi.monetization.internal.carb.CARB.this;	 Catch:{ Exception -> 0x01dd }
            r3 = r3.t;	 Catch:{ Exception -> 0x01dd }
            r6 = com.inmobi.monetization.internal.carb.CARB.this;	 Catch:{ Exception -> 0x01dd }
            r6 = r6.u;	 Catch:{ Exception -> 0x01dd }
            r3 = com.inmobi.commons.internal.EncryptionUtils.DeAe(r0, r3, r6);	 Catch:{ Exception -> 0x01dd }
            r0 = new java.lang.String;	 Catch:{ Exception -> 0x01dd }
            r0.<init>(r3);	 Catch:{ Exception -> 0x01dd }
            r4 = r0;
        L_0x0196:
            if (r4 == 0) goto L_0x01e6;
        L_0x0198:
            r0 = "[InMobi]-[CARB]-4.5.0";
            r3 = new java.lang.StringBuilder;	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r3.<init>();	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r6 = "Get list after decryption: ";
            r3 = r3.append(r6);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r3 = r3.append(r4);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r3 = r3.toString();	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            com.inmobi.commons.internal.Log.internal(r0, r3);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r0 = new org.json.JSONObject;	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r0.<init>(r4);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r3 = "success";
            r3 = r0.getBoolean(r3);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            if (r3 == 0) goto L_0x0252;
        L_0x01bd:
            r3 = "data";
            r0 = r0.getJSONObject(r3);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r3 = "req_id";
            r4 = r0.getString(r3);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r3 = "p_apps";
            r6 = r0.getJSONArray(r3);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r0 = r6.length();	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            if (r0 != 0) goto L_0x01f5;
        L_0x01d5:
            r0 = com.inmobi.monetization.internal.carb.CARB.this;	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r0.e();	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r0 = r1;
            goto L_0x0010;
        L_0x01dd:
            r0 = move-exception;
            r3 = "[InMobi]-[CARB]-4.5.0";
            r6 = "Exception in carb ";
            com.inmobi.commons.internal.Log.internal(r3, r6, r0);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            goto L_0x0196;
        L_0x01e6:
            r0 = "[InMobi]-[CARB]-4.5.0";
            r2 = "Unable to decrypt response or response not encrypted";
            com.inmobi.commons.internal.Log.internal(r0, r2);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r0 = com.inmobi.monetization.internal.carb.CARB.this;	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r0.e();	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r0 = r1;
            goto L_0x0010;
        L_0x01f5:
            r0 = r5;
        L_0x01f6:
            r3 = r6.length();	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            if (r0 >= r3) goto L_0x0245;
        L_0x01fc:
            r3 = r6.getJSONObject(r0);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r5 = "";
            r5 = "";
            r5 = "bid";
            r5 = r3.getString(r5);	 Catch:{ JSONException -> 0x021f, MalformedURLException -> 0x00ea, IOException -> 0x0113 }
            r7 = "inm_id";
            r3 = r3.getString(r7);	 Catch:{ JSONException -> 0x021f, MalformedURLException -> 0x00ea, IOException -> 0x0113 }
            if (r3 == 0) goto L_0x021a;
        L_0x0212:
            r7 = "";
            r7 = r7.equals(r3);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            if (r7 == 0) goto L_0x0228;
        L_0x021a:
            r3 = r0 + 1;
            r5 = r0;
            r0 = r3;
            goto L_0x01f6;
        L_0x021f:
            r3 = move-exception;
            r3 = "[InMobi]-[CARB]-4.5.0";
            r5 = "BID or INM missing";
            com.inmobi.commons.internal.Log.internal(r3, r5);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            goto L_0x021a;
        L_0x0228:
            r7 = new com.inmobi.monetization.internal.carb.CarbInfo;	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r7.<init>();	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r7.setBid(r5);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r7.setInmId(r3);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r3 = com.inmobi.monetization.internal.carb.CARB.this;	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r3 = r3.a(r5);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            if (r3 != 0) goto L_0x021a;
        L_0x023b:
            r3 = com.inmobi.monetization.internal.carb.CARB.this;	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r3 = r3.x;	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r3.add(r7);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            goto L_0x021a;
        L_0x0245:
            r0 = com.inmobi.monetization.internal.carb.CARB.this;	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r3 = com.inmobi.monetization.internal.carb.CARB.this;	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r3 = r3.x;	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r5 = r5 + 1;
            r0.a(r3, r4, r5);	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
        L_0x0252:
            r0 = com.inmobi.monetization.internal.carb.CARB.this;	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r0.e();	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            r0 = r2;
            goto L_0x0010;
        L_0x025a:
            r0 = r0.getResponseCode();	 Catch:{ MalformedURLException -> 0x00ea, IOException -> 0x0113, JSONException -> 0x0130 }
            goto L_0x0010;
        L_0x0260:
            r0 = move-exception;
            r2 = r3;
            goto L_0x013b;
            */
        }

        private void a() {
            CARB.this.c();
            CARB.this.t = EncryptionUtils.keag();
            CARB.this.c = PkInitilaizer.getConfigParams().getExponent();
            CARB.this.d = PkInitilaizer.getConfigParams().getModulus();
            CARB.this.e = PkInitilaizer.getConfigParams().getVersion();
            if (CARB.this.c.equals(Preconditions.EMPTY_ARGUMENTS) || CARB.this.d.equals(Preconditions.EMPTY_ARGUMENTS) || CARB.this.e.equals(Preconditions.EMPTY_ARGUMENTS)) {
                Log.internal(LOGGING_TAG, "Exception retreiving Carb info due to key problem");
                CARB.this.e();
            } else {
                String b = b();
                Log.internal(LOGGING_TAG, "Unencrypted postbody :" + b);
                String a = CARB.this.a(b, CARB.this.t, CARB.this.u, CARB.this.a, CARB.this.d, CARB.this.c);
                if (a == null) {
                    CARB.this.e();
                } else if (a(a) != 200) {
                    long retryInterval = CarbInitializer.getConfigParams().getRetryInterval() * 1000;
                    if (this.a <= CarbInitializer.getConfigParams().getRetryCount()) {
                        try {
                            Thread.sleep(retryInterval * ((long) this.a));
                        } catch (InterruptedException e) {
                        }
                        Log.internal(LOGGING_TAG, "Get carb info Failed. Retrying count: " + this.a);
                        this.a++;
                        if (Looper.myLooper() == null) {
                            Looper.prepare();
                        }
                        new Handler().postDelayed(this, 0);
                        Looper.loop();
                        Looper.myLooper().quit();
                    } else {
                        this.a = 0;
                        Editor edit = j.edit();
                        edit.putLong(CARB.this.h, System.currentTimeMillis());
                        edit.commit();
                        CARB.this.f();
                    }
                }
            }
        }

        private String b() {
            StringBuffer stringBuffer = new StringBuffer();
            CARB.fillCarbInfo();
            stringBuffer.append("mk-siteid=");
            stringBuffer.append(CARB.getURLEncoded(InMobi.getAppId()));
            String str = "pr-SAND-" + InternalSDKUtil.getInMobiInternalVersion(InternalSDKUtil.INMOBI_SDK_RELEASE_VERSION) + "-" + InternalSDKUtil.INMOBI_SDK_RELEASE_DATE;
            stringBuffer.append("&mk-version=");
            stringBuffer.append(CARB.getURLEncoded(str));
            str = UID.getInstance().getJSON(CarbInitializer.getConfigParams().getDeviceIdMaskMap());
            stringBuffer.append("&u-id-map=");
            stringBuffer.append(CARB.getURLEncoded(str));
            stringBuffer.append("&u-appbid=");
            stringBuffer.append(CARB.getURLEncoded(CARB.getAppBid()));
            stringBuffer.append("&u-appver=");
            stringBuffer.append(CARB.getURLEncoded(CARB.getAppVer()));
            stringBuffer.append("&h-user-agent=");
            stringBuffer.append(CARB.getURLEncoded(DeviceInfo.getPhoneDefaultUserAgent()));
            stringBuffer.append("&d-localization=");
            stringBuffer.append(CARB.getURLEncoded(DeviceInfo.getLocalization()));
            stringBuffer.append("&d-nettype=");
            stringBuffer.append(CARB.getURLEncoded(DeviceInfo.getNetworkType()));
            WifiInfo wifiInfo = null;
            try {
                wifiInfo = IceDataCollector.getConnectedWifiInfo(InternalSDKUtil.getContext());
            } catch (Exception e) {
                Log.internal(InternalSDKUtil.LOGGING_TAG, "No wifi permissions set, unable to send wifi data");
            }
            if (wifiInfo != null) {
                stringBuffer.append("&c-ap-bssid=");
                stringBuffer.append(Long.toString(wifiInfo.bssid));
            }
            return stringBuffer.toString();
        }

        public void run() {
            a();
        }
    }

    class b implements Runnable {
        int a;
        final /* synthetic */ ArrayList b;
        final /* synthetic */ String c;
        final /* synthetic */ int d;

        b(ArrayList arrayList, String str, int i) {
            this.b = arrayList;
            this.c = str;
            this.d = i;
            this.a = 1;
        }

        private int a(String str) {
            BufferedWriter bufferedWriter;
            if (!InternalSDKUtil.checkNetworkAvailibility(InternalSDKUtil.getContext())) {
                return -1;
            }
            try {
                URL url = new URL(CarbInitializer.getConfigParams().getCarbPostpoint());
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestProperty("User-Agent", InternalSDKUtil.getUserAgent());
                long timeoutInterval = CarbInitializer.getConfigParams().getTimeoutInterval() * 1000;
                httpURLConnection.setConnectTimeout((int) timeoutInterval);
                httpURLConnection.setReadTimeout((int) timeoutInterval);
                httpURLConnection.setRequestProperty("user-agent", DeviceInfo.getPhoneDefaultUserAgent());
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                httpURLConnection.setRequestProperty("Content-Length", Integer.toString(str.length()));
                InetAddress.getByName(url.getHost());
                try {
                    BufferedWriter bufferedWriter2 = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream()));
                    try {
                        bufferedWriter2.write(str);
                        if (bufferedWriter2 != null) {
                            try {
                                bufferedWriter2.close();
                            } catch (IOException e) {
                                Log.internal(Constants.LOG_TAG, "Exception closing resource: " + bufferedWriter2, e);
                            } catch (MalformedURLException e2) {
                                Log.internal(LOGGING_TAG, "Malformed URL");
                                return -1;
                            }
                        }
                        return httpURLConnection.getResponseCode() == 200 ? 200 : httpURLConnection.getResponseCode();
                    } catch (Throwable th) {
                        th = th;
                        bufferedWriter = bufferedWriter2;
                        if (bufferedWriter != null) {
                            bufferedWriter.close();
                        }
                        throw th;
                    }
                } catch (Throwable th2) {
                    Throwable th3 = th2;
                    bufferedWriter = null;
                    if (bufferedWriter != null) {
                        try {
                            bufferedWriter.close();
                        } catch (IOException e3) {
                            Log.internal(Constants.LOG_TAG, "Exception closing resource: " + bufferedWriter, e3);
                        } catch (MalformedURLException e22) {
                            Log.internal(LOGGING_TAG, "Malformed URL");
                            return -1;
                        }
                    }
                    throw th3;
                }
            } catch (UnknownHostException e4) {
                throw new MalformedURLException("Malformed URL: " + url.toString());
            } catch (MalformedURLException e222) {
                Log.internal(LOGGING_TAG, "Malformed URL");
                return -1;
            } catch (IOException e5) {
                return -1;
            }
        }

        private String a(ArrayList<CarbInfo> arrayList, String str, int i) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("req_id=");
            stringBuffer.append(CARB.getURLEncoded(str));
            JSONArray jSONArray = new JSONArray();
            int size = arrayList.size();
            int i2 = 0;
            while (i2 < size) {
                jSONArray.put(((CarbInfo) arrayList.get(i2)).getInmId());
                i2++;
            }
            stringBuffer.append("&p_a_apps=");
            stringBuffer.append(CARB.getURLEncoded(jSONArray.toString()));
            stringBuffer.append("&i_till=");
            stringBuffer.append(i);
            String json = UID.getInstance().getJSON(CarbInitializer.getConfigParams().getDeviceIdMaskMap());
            stringBuffer.append("&u-id-map=");
            stringBuffer.append(CARB.getURLEncoded(json));
            return stringBuffer.toString();
        }

        public void run() {
            CARB.this.d();
            CARB.this.v = EncryptionUtils.keag();
            CARB.this.c = PkInitilaizer.getConfigParams().getExponent();
            CARB.this.d = PkInitilaizer.getConfigParams().getModulus();
            CARB.this = PkInitilaizer.getConfigParams().getVersion();
            if (CARB.this.c.equals(Preconditions.EMPTY_ARGUMENTS) || CARB.this.d.equals(Preconditions.EMPTY_ARGUMENTS) || CARB.this.equals(Preconditions.EMPTY_ARGUMENTS)) {
                Log.internal(LOGGING_TAG, "Exception retreiving Carb info due to key problem");
                CARB.this.f();
            } else {
                String a = a(this.b, this.c, this.d);
                Log.internal(LOGGING_TAG, "PostBody Before encryption: " + a);
                String a2 = CARB.this.a(a, CARB.this.v, CARB.this.w, CARB.this.b, CARB.this.d, CARB.this.c);
                if (a2 == null) {
                    Log.internal(LOGGING_TAG, "POST message cannot be encrypted");
                    CARB.this.e();
                } else {
                    int a3 = a(a2);
                    Log.internal(LOGGING_TAG, "Post Response to CARB server: " + a3);
                    if (200 == a3) {
                        if (CARB.this.f != null) {
                            CARB.this.f.postSuccess();
                        }
                        CARB.this.f();
                    } else {
                        if (CARB.this.f != null) {
                            CARB.this.f.postFailed();
                        }
                        long retryInterval = CarbInitializer.getConfigParams().getRetryInterval() * 1000;
                        if (this.a <= CarbInitializer.getConfigParams().getRetryCount()) {
                            try {
                                Thread.sleep(retryInterval * ((long) this.a));
                            } catch (InterruptedException e) {
                            }
                            Log.internal(LOGGING_TAG, "POSt to carb failed. Retrying count: " + this.a);
                            this.a++;
                            if (Looper.myLooper() == null) {
                                Looper.prepare();
                            }
                            new Handler().postDelayed(this, 0);
                            Looper.loop();
                            Looper.myLooper().quit();
                        } else {
                            this.a = 0;
                            CARB.this.f();
                        }
                    }
                }
            }
        }
    }

    static {
        g = new CARB();
        k = new AtomicBoolean(false);
        m = new AtomicBoolean(false);
        o = Preconditions.EMPTY_ARGUMENTS;
        p = Preconditions.EMPTY_ARGUMENTS;
        q = Preconditions.EMPTY_ARGUMENTS;
    }

    private CARB() {
        this.h = "carb_last_req_time";
        this.i = "carbpreference";
        this.r = 8;
        this.s = 16;
        this.c = Preconditions.EMPTY_ARGUMENTS;
        this.d = Preconditions.EMPTY_ARGUMENTS;
        this.e = Preconditions.EMPTY_ARGUMENTS;
        this.f = null;
        this.x = new ArrayList();
        CarbInitializer.initialize();
        j = InternalSDKUtil.getContext().getSharedPreferences(this.i, 0);
    }

    private String a(String str, byte[] bArr, byte[] bArr2, byte[] bArr3, String str2, String str3) {
        String SeMeGe = EncryptionUtils.SeMeGe(str, bArr, bArr2, bArr3, str2, str3);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("sm=");
        stringBuilder.append(SeMeGe);
        stringBuilder.append("&sn=");
        stringBuilder.append(this.e);
        SeMeGe = stringBuilder.toString();
        Log.internal(LOGGING_TAG, SeMeGe);
        return SeMeGe;
    }

    private synchronized void a(ArrayList<CarbInfo> arrayList, String str, int i) {
        if (m.compareAndSet(false, true)) {
            m.set(true);
            n = new Thread(new b(arrayList, str, i));
            n.start();
        }
    }

    private boolean a(String str) {
        try {
            InternalSDKUtil.getContext().getPackageManager().getPackageInfo(str, 1);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    private synchronized void b() {
        this.x.clear();
        if (k.compareAndSet(false, true)) {
            l = new Thread(new a());
            l.start();
        }
    }

    private void c() {
        try {
            this.a = new byte[8];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(this.a);
            this.u = new byte[16];
            secureRandom.nextBytes(this.u);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void d() {
        try {
            this.b = new byte[8];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(this.b);
            this.w = new byte[16];
            secureRandom.nextBytes(this.w);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void e() {
        if (k != null) {
            k.set(false);
        }
    }

    private void f() {
        if (m != null) {
            m.set(false);
        }
    }

    public static void fillCarbInfo() {
        try {
            Context context = InternalSDKUtil.getContext();
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), TransportMediator.FLAG_KEY_MEDIA_NEXT);
            if (applicationInfo != null) {
                setAppBId(applicationInfo.packageName);
                setAppDisplayName(applicationInfo.loadLabel(packageManager).toString());
            }
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), TransportMediator.FLAG_KEY_MEDIA_NEXT);
            String str = null;
            if (packageInfo != null) {
                str = packageInfo.versionName;
                if (str == null || str.equals(Preconditions.EMPTY_ARGUMENTS)) {
                    str = packageInfo.versionCode + Preconditions.EMPTY_ARGUMENTS;
                }
            }
            if (str != null && !str.equals(Preconditions.EMPTY_ARGUMENTS)) {
                setAppVer(str);
            }
        } catch (Exception e) {
            Log.internal(InternalSDKUtil.LOGGING_TAG, "Failed to fill CarbInfo", e);
        }
    }

    public static String getAppBid() {
        return o;
    }

    public static String getAppDisplayName() {
        return q;
    }

    public static String getAppVer() {
        return p;
    }

    public static Object getCountryISO(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        return telephonyManager.getNetworkCountryIso().equals(Preconditions.EMPTY_ARGUMENTS) ? context.getResources().getConfiguration().locale.getISO3Country() : telephonyManager.getNetworkCountryIso();
    }

    public static CARB getInstance() {
        return g;
    }

    public static String getURLDecoded(String str, String str2) {
        try {
            return URLDecoder.decode(str, str2);
        } catch (Exception e) {
            return Preconditions.EMPTY_ARGUMENTS;
        }
    }

    public static String getURLEncoded(String str) {
        String str2 = Preconditions.EMPTY_ARGUMENTS;
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {
            return Preconditions.EMPTY_ARGUMENTS;
        }
    }

    public static void setAppBId(String str) {
        o = str;
    }

    public static void setAppDisplayName(String str) {
        q = str;
    }

    public static void setAppVer(String str) {
        p = str;
    }

    public void setCallBack(CarbCallback carbCallback) {
        this.f = carbCallback;
    }

    public synchronized void startCarb() {
        if (!CarbInitializer.getConfigParams().isCarbEnabled()) {
            Log.internal(LOGGING_TAG, "CARB feature disabled.");
        } else if (InternalSDKUtil.checkNetworkAvailibility(InternalSDKUtil.getContext())) {
            long j = j.getLong(this.h, 0);
            if (0 != j) {
                if (j + CarbInitializer.getConfigParams().getRetreiveFrequncy() - System.currentTimeMillis() > 0) {
                    Log.internal(LOGGING_TAG, "CARB request interval not reached. NO request");
                } else if (k.get()) {
                    Log.internal(LOGGING_TAG, "CARB request is in progress");
                } else {
                    Log.internal(LOGGING_TAG, "CARB request interval reached. Requesting again");
                    b();
                }
            } else if (k.get()) {
                Log.internal(LOGGING_TAG, "First CARB request is in progress");
            } else {
                Log.internal(LOGGING_TAG, "Requesting CARB first time");
                b();
            }
        }
    }
}