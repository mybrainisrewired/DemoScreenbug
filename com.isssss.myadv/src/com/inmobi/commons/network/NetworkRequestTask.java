package com.inmobi.commons.network;

import com.inmobi.commons.internal.InternalSDKUtil;
import com.inmobi.commons.internal.Log;
import com.inmobi.commons.network.Request.Method;
import com.inmobi.commons.network.abstraction.INetworkListener;
import com.mopub.common.Preconditions;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Iterator;

public class NetworkRequestTask implements Runnable {
    private Request a;
    private INetworkListener b;
    private HttpURLConnection c;

    NetworkRequestTask(Request request, INetworkListener iNetworkListener) {
        this.a = request;
        this.b = iNetworkListener;
    }

    private HttpURLConnection a(String str) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
        a(httpURLConnection);
        return httpURLConnection;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void a() {
        throw new UnsupportedOperationException("Method not decompiled: com.inmobi.commons.network.NetworkRequestTask.a():void");
        /*
        r5 = this;
        r2 = 0;
        r0 = "[InMobi]-4.5.0";
        r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0072, OutOfMemoryError -> 0x00cf }
        r1.<init>();	 Catch:{ Exception -> 0x0072, OutOfMemoryError -> 0x00cf }
        r3 = "Http Status Code: ";
        r1 = r1.append(r3);	 Catch:{ Exception -> 0x0072, OutOfMemoryError -> 0x00cf }
        r3 = r5.c;	 Catch:{ Exception -> 0x0072, OutOfMemoryError -> 0x00cf }
        r3 = r3.getResponseCode();	 Catch:{ Exception -> 0x0072, OutOfMemoryError -> 0x00cf }
        r1 = r1.append(r3);	 Catch:{ Exception -> 0x0072, OutOfMemoryError -> 0x00cf }
        r1 = r1.toString();	 Catch:{ Exception -> 0x0072, OutOfMemoryError -> 0x00cf }
        com.inmobi.commons.internal.Log.debug(r0, r1);	 Catch:{ Exception -> 0x0072, OutOfMemoryError -> 0x00cf }
        r0 = r5.c;	 Catch:{ Exception -> 0x0072, OutOfMemoryError -> 0x00cf }
        r0 = r0.getResponseCode();	 Catch:{ Exception -> 0x0072, OutOfMemoryError -> 0x00cf }
        r1 = "[InMobi]-4.5.0";
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0072, OutOfMemoryError -> 0x00cf }
        r3.<init>();	 Catch:{ Exception -> 0x0072, OutOfMemoryError -> 0x00cf }
        r4 = "Status Code: ";
        r3 = r3.append(r4);	 Catch:{ Exception -> 0x0072, OutOfMemoryError -> 0x00cf }
        r3 = r3.append(r0);	 Catch:{ Exception -> 0x0072, OutOfMemoryError -> 0x00cf }
        r3 = r3.toString();	 Catch:{ Exception -> 0x0072, OutOfMemoryError -> 0x00cf }
        com.inmobi.commons.internal.Log.internal(r1, r3);	 Catch:{ Exception -> 0x0072, OutOfMemoryError -> 0x00cf }
        r1 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r0 != r1) goto L_0x00ea;
    L_0x0041:
        r1 = new java.io.BufferedReader;	 Catch:{ all -> 0x0108 }
        r0 = new java.io.InputStreamReader;	 Catch:{ all -> 0x0108 }
        r3 = r5.c;	 Catch:{ all -> 0x0108 }
        r3 = r3.getInputStream();	 Catch:{ all -> 0x0108 }
        r4 = "UTF-8";
        r0.<init>(r3, r4);	 Catch:{ all -> 0x0108 }
        r1.<init>(r0);	 Catch:{ all -> 0x0108 }
        r0 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0068 }
        r0.<init>();	 Catch:{ all -> 0x0068 }
    L_0x0058:
        r2 = r1.readLine();	 Catch:{ all -> 0x0068 }
        if (r2 == 0) goto L_0x008d;
    L_0x005e:
        r2 = r0.append(r2);	 Catch:{ all -> 0x0068 }
        r3 = "\n";
        r2.append(r3);	 Catch:{ all -> 0x0068 }
        goto L_0x0058;
    L_0x0068:
        r0 = move-exception;
    L_0x0069:
        r2 = r5.c;	 Catch:{ Exception -> 0x0072, OutOfMemoryError -> 0x00cf }
        r2.disconnect();	 Catch:{ Exception -> 0x0072, OutOfMemoryError -> 0x00cf }
        r5.a(r1);	 Catch:{ Exception -> 0x0072, OutOfMemoryError -> 0x00cf }
        throw r0;	 Catch:{ Exception -> 0x0072, OutOfMemoryError -> 0x00cf }
    L_0x0072:
        r0 = move-exception;
        r1 = "[InMobi]-4.5.0";
        r2 = "Failed to retrieve response";
        com.inmobi.commons.internal.Log.internal(r1, r2, r0);
        r0 = new com.inmobi.commons.network.Response;
        r1 = com.inmobi.commons.network.ErrorCode.CONNECTION_ERROR;
        r0.<init>(r1);
        r1 = r5.b;
        if (r1 == 0) goto L_0x008c;
    L_0x0085:
        r1 = r5.b;
        r2 = r5.a;
        r1.onRequestFailed(r2, r0);
    L_0x008c:
        return;
    L_0x008d:
        r0 = r0.toString();	 Catch:{ all -> 0x0068 }
        r2 = "[InMobi]-4.5.0";
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0068 }
        r3.<init>();	 Catch:{ all -> 0x0068 }
        r4 = "Response: ";
        r3 = r3.append(r4);	 Catch:{ all -> 0x0068 }
        r3 = r3.append(r0);	 Catch:{ all -> 0x0068 }
        r3 = r3.toString();	 Catch:{ all -> 0x0068 }
        com.inmobi.commons.internal.Log.debug(r2, r3);	 Catch:{ all -> 0x0068 }
        r2 = new com.inmobi.commons.network.Response;	 Catch:{ all -> 0x0068 }
        r3 = r5.c;	 Catch:{ all -> 0x0068 }
        r3 = r3.getResponseCode();	 Catch:{ all -> 0x0068 }
        r4 = r5.c;	 Catch:{ all -> 0x0068 }
        r4 = r4.getHeaderFields();	 Catch:{ all -> 0x0068 }
        r2.<init>(r0, r3, r4);	 Catch:{ all -> 0x0068 }
        r0 = r5.b;	 Catch:{ all -> 0x0068 }
        if (r0 == 0) goto L_0x00c5;
    L_0x00be:
        r0 = r5.b;	 Catch:{ all -> 0x0068 }
        r3 = r5.a;	 Catch:{ all -> 0x0068 }
        r0.onRequestSucceded(r3, r2);	 Catch:{ all -> 0x0068 }
    L_0x00c5:
        r2 = r1;
    L_0x00c6:
        r0 = r5.c;	 Catch:{ Exception -> 0x0072, OutOfMemoryError -> 0x00cf }
        r0.disconnect();	 Catch:{ Exception -> 0x0072, OutOfMemoryError -> 0x00cf }
        r5.a(r2);	 Catch:{ Exception -> 0x0072, OutOfMemoryError -> 0x00cf }
        goto L_0x008c;
    L_0x00cf:
        r0 = move-exception;
        r1 = "[InMobi]-4.5.0";
        r2 = "Out of memory error received while retieving network response";
        com.inmobi.commons.internal.Log.internal(r1, r2, r0);
        r0 = new com.inmobi.commons.network.Response;
        r1 = com.inmobi.commons.network.ErrorCode.INTERNAL_ERROR;
        r0.<init>(r1);
        r1 = r5.b;
        if (r1 == 0) goto L_0x008c;
    L_0x00e2:
        r1 = r5.b;
        r2 = r5.a;
        r1.onRequestFailed(r2, r0);
        goto L_0x008c;
    L_0x00ea:
        r0 = new com.inmobi.commons.network.Response;	 Catch:{ all -> 0x0108 }
        r1 = 0;
        r3 = r5.c;	 Catch:{ all -> 0x0108 }
        r3 = r3.getResponseCode();	 Catch:{ all -> 0x0108 }
        r4 = r5.c;	 Catch:{ all -> 0x0108 }
        r4 = r4.getHeaderFields();	 Catch:{ all -> 0x0108 }
        r0.<init>(r1, r3, r4);	 Catch:{ all -> 0x0108 }
        r1 = r5.b;	 Catch:{ all -> 0x0108 }
        if (r1 == 0) goto L_0x00c6;
    L_0x0100:
        r1 = r5.b;	 Catch:{ all -> 0x0108 }
        r3 = r5.a;	 Catch:{ all -> 0x0108 }
        r1.onRequestFailed(r3, r0);	 Catch:{ all -> 0x0108 }
        goto L_0x00c6;
    L_0x0108:
        r0 = move-exception;
        r1 = r2;
        goto L_0x0069;
        */
    }

    private void a(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                Log.debug(InternalSDKUtil.LOGGING_TAG, "Exception closing resource: " + closeable, e);
            }
        }
    }

    private void a(HttpURLConnection httpURLConnection) throws ProtocolException {
        httpURLConnection.setConnectTimeout(this.a.getTimeout());
        httpURLConnection.setReadTimeout(this.a.getTimeout());
        Iterator it = this.a.getHeaders().keySet().iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            httpURLConnection.setRequestProperty(str, (String) this.a.getHeaders().get(str));
        }
        httpURLConnection.setUseCaches(false);
        Method requestMethod = this.a.getRequestMethod();
        if (requestMethod != Method.GET) {
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
        }
        httpURLConnection.setRequestMethod(requestMethod.toString());
        httpURLConnection.setRequestProperty("content-type", "application/x-www-form-urlencoded");
    }

    private void b(String str) throws IOException {
        this.c.setRequestProperty("Content-Length", Integer.toString(str.length()));
        try {
            Closeable bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.c.getOutputStream()));
            try {
                bufferedWriter.write(str);
                a(bufferedWriter);
            } catch (Throwable th) {
                th = th;
                a(bufferedWriter);
                throw th;
            }
        } catch (Throwable th2) {
            Throwable th3 = th2;
            bufferedWriter = null;
            a(bufferedWriter);
            throw th3;
        }
    }

    public void run() {
        try {
            String queryParams = this.a.getQueryParams();
            String postBody = this.a.getPostBody();
            String url = this.a.getUrl();
            if (!(queryParams == null || Preconditions.EMPTY_ARGUMENTS.equals(queryParams.trim()))) {
                url = url + "?" + queryParams;
            }
            Log.internal(InternalSDKUtil.LOGGING_TAG, "URL:" + url);
            this.c = a(url);
            if (this.a.getRequestMethod() == Method.GET || !(postBody == null || Preconditions.EMPTY_ARGUMENTS.equals(postBody))) {
                if (this.a.getRequestMethod() != Method.GET) {
                    Log.debug(InternalSDKUtil.LOGGING_TAG, "Post body:" + postBody);
                    b(postBody);
                }
                a();
            } else {
                this.b.onRequestFailed(this.a, new Response(ErrorCode.INTERNAL_ERROR));
            }
        } catch (Exception e) {
            Throwable th = e;
            response = new Response(ErrorCode.NETWORK_ERROR);
            if (this.b != null) {
                Response response2;
                this.b.onRequestFailed(this.a, response2);
            }
            Log.debug(InternalSDKUtil.LOGGING_TAG, "Failed to make network request", th);
        }
    }
}