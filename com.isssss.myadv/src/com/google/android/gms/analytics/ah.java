package com.google.android.gms.analytics;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.loopme.LoopMe;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;

class ah implements n {
    private final Context mContext;
    private GoogleAnalytics sX;
    private final String vI;
    private final HttpClient vJ;
    private URL vK;

    ah(HttpClient httpClient, Context context) {
        this(httpClient, GoogleAnalytics.getInstance(context), context);
    }

    ah(HttpClient httpClient, GoogleAnalytics googleAnalytics, Context context) {
        this.mContext = context.getApplicationContext();
        this.vI = a("GoogleAnalytics", LoopMe.SDK_VERSION, VERSION.RELEASE, ak.a(Locale.getDefault()), Build.MODEL, Build.ID);
        this.vJ = httpClient;
        this.sX = googleAnalytics;
    }

    private void a(ab abVar, URL url, boolean z) {
        if (!TextUtils.isEmpty(abVar.cU()) && db()) {
            URL url2;
            if (url == null) {
                try {
                    url2 = this.vK != null ? this.vK : new URL("https://ssl.google-analytics.com/collect");
                } catch (MalformedURLException e) {
                }
            } else {
                url2 = url;
            }
            HttpHost httpHost = new HttpHost(url2.getHost(), url2.getPort(), url2.getProtocol());
            try {
                HttpEntityEnclosingRequest c = c(abVar.cU(), url2.getPath());
                if (c != null) {
                    c.addHeader("Host", httpHost.toHostString());
                    if (aa.cT()) {
                        a(c);
                    }
                    if (z) {
                        q.p(this.mContext);
                    }
                    HttpResponse execute = this.vJ.execute(httpHost, c);
                    int statusCode = execute.getStatusLine().getStatusCode();
                    HttpEntity entity = execute.getEntity();
                    if (entity != null) {
                        entity.consumeContent();
                    }
                    if (statusCode != 200) {
                        aa.z("Bad response: " + execute.getStatusLine().getStatusCode());
                    }
                }
            } catch (ClientProtocolException e2) {
                aa.z("ClientProtocolException sending monitoring hit.");
            } catch (IOException e3) {
                IOException iOException = e3;
                aa.z("Exception sending monitoring hit: " + iOException.getClass().getSimpleName());
                aa.z(iOException.getMessage());
            }
        }
    }

    private void a(HttpEntityEnclosingRequest httpEntityEnclosingRequest) {
        StringBuffer stringBuffer = new StringBuffer();
        Header[] allHeaders = httpEntityEnclosingRequest.getAllHeaders();
        int length = allHeaders.length;
        int i = 0;
        while (i < length) {
            stringBuffer.append(allHeaders[i].toString()).append("\n");
            i++;
        }
        stringBuffer.append(httpEntityEnclosingRequest.getRequestLine().toString()).append("\n");
        if (httpEntityEnclosingRequest.getEntity() != null) {
            try {
                InputStream content = httpEntityEnclosingRequest.getEntity().getContent();
                if (content != null) {
                    int available = content.available();
                    if (available > 0) {
                        byte[] bArr = new byte[available];
                        content.read(bArr);
                        stringBuffer.append("POST:\n");
                        stringBuffer.append(new String(bArr)).append("\n");
                    }
                }
            } catch (IOException e) {
                aa.y("Error Writing hit to log...");
            }
        }
        aa.y(stringBuffer.toString());
    }

    private HttpEntityEnclosingRequest c(String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            aa.z("Empty hit, discarding.");
            return null;
        } else {
            HttpEntityEnclosingRequest basicHttpEntityEnclosingRequest;
            String str3 = str2 + "?" + str;
            if (str3.length() < 2036) {
                basicHttpEntityEnclosingRequest = new BasicHttpEntityEnclosingRequest("GET", str3);
            } else {
                basicHttpEntityEnclosingRequest = new BasicHttpEntityEnclosingRequest("POST", str2);
                try {
                    basicHttpEntityEnclosingRequest.setEntity(new StringEntity(str));
                } catch (UnsupportedEncodingException e) {
                    aa.z("Encoding error, discarding hit");
                    return null;
                }
            }
            basicHttpEntityEnclosingRequest.addHeader("User-Agent", this.vI);
            return basicHttpEntityEnclosingRequest;
        }
    }

    public void F(String str) {
        try {
            this.vK = new URL(str);
        } catch (MalformedURLException e) {
            this.vK = null;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int a(java.util.List<com.google.android.gms.analytics.x> r13, com.google.android.gms.analytics.ab r14, boolean r15) {
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.analytics.ah.a(java.util.List, com.google.android.gms.analytics.ab, boolean):int");
        /*
        r12 = this;
        r5 = 0;
        r0 = r13.size();
        r1 = 40;
        r7 = java.lang.Math.min(r0, r1);
        r0 = "_hr";
        r1 = r13.size();
        r14.c(r0, r1);
        r2 = 0;
        r3 = 0;
        r1 = 1;
        r0 = 0;
        r6 = r0;
    L_0x0019:
        if (r6 >= r7) goto L_0x0158;
    L_0x001b:
        r0 = r13.get(r6);
        r0 = (com.google.android.gms.analytics.x) r0;
        r4 = r12.a(r0);
        if (r4 != 0) goto L_0x005a;
    L_0x0027:
        r4 = com.google.android.gms.analytics.aa.cT();
        if (r4 == 0) goto L_0x0054;
    L_0x002d:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r8 = "No destination: discarding hit: ";
        r4 = r4.append(r8);
        r0 = r0.cO();
        r0 = r4.append(r0);
        r0 = r0.toString();
        com.google.android.gms.analytics.aa.z(r0);
    L_0x0047:
        r0 = r5 + 1;
        r2 = r2 + 1;
        r11 = r3;
        r3 = r0;
        r0 = r11;
    L_0x004e:
        r4 = r6 + 1;
        r6 = r4;
        r5 = r3;
        r3 = r0;
        goto L_0x0019;
    L_0x0054:
        r0 = "No destination: discarding hit.";
        com.google.android.gms.analytics.aa.z(r0);
        goto L_0x0047;
    L_0x005a:
        r3 = new org.apache.http.HttpHost;
        r8 = r4.getHost();
        r9 = r4.getPort();
        r10 = r4.getProtocol();
        r3.<init>(r8, r9, r10);
        r8 = r4.getPath();
        r9 = r0.cO();
        r9 = android.text.TextUtils.isEmpty(r9);
        if (r9 == 0) goto L_0x0088;
    L_0x0079:
        r0 = "";
    L_0x007b:
        r8 = r12.c(r0, r8);
        if (r8 != 0) goto L_0x0091;
    L_0x0081:
        r0 = r5 + 1;
        r2 = r2 + 1;
        r3 = r0;
        r0 = r4;
        goto L_0x004e;
    L_0x0088:
        r9 = java.lang.System.currentTimeMillis();
        r0 = com.google.android.gms.analytics.y.a(r0, r9);
        goto L_0x007b;
    L_0x0091:
        r9 = "Host";
        r10 = r3.toHostString();
        r8.addHeader(r9, r10);
        r9 = com.google.android.gms.analytics.aa.cT();
        if (r9 == 0) goto L_0x00a3;
    L_0x00a0:
        r12.a(r8);
    L_0x00a3:
        r9 = r0.length();
        r10 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
        if (r9 <= r10) goto L_0x00c1;
    L_0x00ab:
        r3 = "Hit too long (> 8192 bytes)--not sent";
        com.google.android.gms.analytics.aa.z(r3);
        r2 = r2 + 1;
    L_0x00b2:
        r3 = "_td";
        r0 = r0.getBytes();
        r0 = r0.length;
        r14.c(r3, r0);
        r0 = r5 + 1;
        r3 = r0;
        r0 = r4;
        goto L_0x004e;
    L_0x00c1:
        r9 = r12.sX;
        r9 = r9.isDryRunEnabled();
        if (r9 == 0) goto L_0x00cf;
    L_0x00c9:
        r3 = "Dry run enabled. Hit not actually sent.";
        com.google.android.gms.analytics.aa.x(r3);
        goto L_0x00b2;
    L_0x00cf:
        if (r1 == 0) goto L_0x00d7;
    L_0x00d1:
        r9 = r12.mContext;	 Catch:{ ClientProtocolException -> 0x0111, IOException -> 0x011d }
        com.google.android.gms.analytics.q.p(r9);	 Catch:{ ClientProtocolException -> 0x0111, IOException -> 0x011d }
        r1 = 0;
    L_0x00d7:
        r9 = r12.vJ;	 Catch:{ ClientProtocolException -> 0x0111, IOException -> 0x011d }
        r3 = r9.execute(r3, r8);	 Catch:{ ClientProtocolException -> 0x0111, IOException -> 0x011d }
        r8 = r3.getStatusLine();	 Catch:{ ClientProtocolException -> 0x0111, IOException -> 0x011d }
        r8 = r8.getStatusCode();	 Catch:{ ClientProtocolException -> 0x0111, IOException -> 0x011d }
        r9 = r3.getEntity();	 Catch:{ ClientProtocolException -> 0x0111, IOException -> 0x011d }
        if (r9 == 0) goto L_0x00ee;
    L_0x00eb:
        r9.consumeContent();	 Catch:{ ClientProtocolException -> 0x0111, IOException -> 0x011d }
    L_0x00ee:
        r9 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r8 == r9) goto L_0x00b2;
    L_0x00f2:
        r8 = new java.lang.StringBuilder;	 Catch:{ ClientProtocolException -> 0x0111, IOException -> 0x011d }
        r8.<init>();	 Catch:{ ClientProtocolException -> 0x0111, IOException -> 0x011d }
        r9 = "Bad response: ";
        r8 = r8.append(r9);	 Catch:{ ClientProtocolException -> 0x0111, IOException -> 0x011d }
        r3 = r3.getStatusLine();	 Catch:{ ClientProtocolException -> 0x0111, IOException -> 0x011d }
        r3 = r3.getStatusCode();	 Catch:{ ClientProtocolException -> 0x0111, IOException -> 0x011d }
        r3 = r8.append(r3);	 Catch:{ ClientProtocolException -> 0x0111, IOException -> 0x011d }
        r3 = r3.toString();	 Catch:{ ClientProtocolException -> 0x0111, IOException -> 0x011d }
        com.google.android.gms.analytics.aa.z(r3);	 Catch:{ ClientProtocolException -> 0x0111, IOException -> 0x011d }
        goto L_0x00b2;
    L_0x0111:
        r3 = move-exception;
        r3 = "ClientProtocolException sending hit; discarding hit...";
        com.google.android.gms.analytics.aa.z(r3);
        r3 = "_hd";
        r14.c(r3, r2);
        goto L_0x00b2;
    L_0x011d:
        r0 = move-exception;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r6 = "Exception sending hit: ";
        r3 = r3.append(r6);
        r6 = r0.getClass();
        r6 = r6.getSimpleName();
        r3 = r3.append(r6);
        r3 = r3.toString();
        com.google.android.gms.analytics.aa.z(r3);
        r0 = r0.getMessage();
        com.google.android.gms.analytics.aa.z(r0);
        r0 = "_de";
        r3 = 1;
        r14.c(r0, r3);
        r0 = "_hd";
        r14.c(r0, r2);
        r0 = "_hs";
        r14.c(r0, r5);
        r12.a(r14, r4, r1);
        r0 = r5;
    L_0x0157:
        return r0;
    L_0x0158:
        r0 = "_hd";
        r14.c(r0, r2);
        r0 = "_hs";
        r14.c(r0, r5);
        if (r15 == 0) goto L_0x0167;
    L_0x0164:
        r12.a(r14, r3, r1);
    L_0x0167:
        r0 = r5;
        goto L_0x0157;
        */
    }

    String a(String str, String str2, String str3, String str4, String str5, String str6) {
        return String.format("%s/%s (Linux; U; Android %s; %s; %s Build/%s)", new Object[]{str, str2, str3, str4, str5, str6});
    }

    URL a(x xVar) {
        if (this.vK != null) {
            return this.vK;
        }
        try {
            return new URL("http:".equals(xVar.cR()) ? "http://www.google-analytics.com/collect" : "https://ssl.google-analytics.com/collect");
        } catch (MalformedURLException e) {
            aa.w("Error trying to parse the hardcoded host url. This really shouldn't happen.");
            return null;
        }
    }

    public boolean ch() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) this.mContext.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            return true;
        }
        aa.y("...no network connectivity");
        return false;
    }

    boolean db() {
        return Math.random() * 100.0d <= 1.0d;
    }
}