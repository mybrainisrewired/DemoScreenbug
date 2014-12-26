package com.android.volley.toolbox;

import android.os.SystemClock;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.Cache.Entry;
import com.android.volley.Network;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.millennialmedia.android.MMAdView;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.cookie.DateUtils;

public class BasicNetwork implements Network {
    protected static final boolean DEBUG;
    private static int DEFAULT_POOL_SIZE;
    private static int SLOW_REQUEST_THRESHOLD_MS;
    protected final HttpStack mHttpStack;
    protected final ByteArrayPool mPool;

    static {
        DEBUG = VolleyLog.DEBUG;
        SLOW_REQUEST_THRESHOLD_MS = 3000;
        DEFAULT_POOL_SIZE = 4096;
    }

    public BasicNetwork(HttpStack httpStack) {
        this(httpStack, new ByteArrayPool(DEFAULT_POOL_SIZE));
    }

    public BasicNetwork(HttpStack httpStack, ByteArrayPool pool) {
        this.mHttpStack = httpStack;
        this.mPool = pool;
    }

    private void addCacheHeaders(Map<String, String> headers, Entry entry) {
        if (entry != null) {
            if (entry.etag != null) {
                headers.put("If-None-Match", entry.etag);
            }
            if (entry.serverDate > 0) {
                headers.put("If-Modified-Since", DateUtils.formatDate(new Date(entry.serverDate)));
            }
        }
    }

    private static void attemptRetryOnException(String logPrefix, Request<?> request, VolleyError exception) throws VolleyError {
        int i = MMAdView.TRANSITION_UP;
        int i2 = 1;
        int i3 = 0;
        RetryPolicy retryPolicy = request.getRetryPolicy();
        int oldTimeout = request.getTimeoutMs();
        try {
            retryPolicy.retry(exception);
            String str = "%s-retry [timeout=%s]";
            Object[] objArr = new Object[i];
            objArr[i3] = logPrefix;
            objArr[i2] = Integer.valueOf(oldTimeout);
            request.addMarker(String.format(str, objArr));
        } catch (VolleyError e) {
            VolleyError e2 = e;
            str = "%s-timeout-giveup [timeout=%s]";
            objArr = new Object[i];
            objArr[i3] = logPrefix;
            objArr[i2] = Integer.valueOf(oldTimeout);
            request.addMarker(String.format(str, objArr));
            throw e2;
        }
    }

    private static Map<String, String> convertHeaders(Header[] headers) {
        Map<String, String> result = new HashMap();
        int i = 0;
        while (i < headers.length) {
            result.put(headers[i].getName(), headers[i].getValue());
            i++;
        }
        return result;
    }

    private byte[] entityToBytes(HttpEntity entity) throws IOException, ServerError {
        int i = 0;
        PoolingByteArrayOutputStream bytes = new PoolingByteArrayOutputStream(this.mPool, (int) entity.getContentLength());
        byte[] buffer = null;
        try {
            InputStream in = entity.getContent();
            if (in == null) {
                throw new ServerError();
            }
            buffer = this.mPool.getBuf(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
            while (true) {
                int count = in.read(buffer);
                if (count == -1) {
                    byte[] toByteArray = bytes.toByteArray();
                    try {
                        entity.consumeContent();
                    } catch (IOException e) {
                        VolleyLog.v("Error occured when calling consumingContent", new Object[i]);
                    }
                    this.mPool.returnBuf(buffer);
                    bytes.close();
                    return toByteArray;
                } else {
                    bytes.write(buffer, 0, count);
                }
            }
        } catch (Throwable th) {
            try {
                entity.consumeContent();
            } catch (IOException e2) {
                VolleyLog.v("Error occured when calling consumingContent", new Object[i]);
            }
        }
    }

    private void logSlowRequests(long requestLifetime, Request<?> request, byte[] responseContents, StatusLine statusLine) {
        if (DEBUG || requestLifetime > ((long) SLOW_REQUEST_THRESHOLD_MS)) {
            String str = "HTTP response for request=<%s> [lifetime=%d], [size=%s], [rc=%d], [retryCount=%s]";
            Object[] objArr = new Object[5];
            objArr[0] = request;
            objArr[1] = Long.valueOf(requestLifetime);
            objArr[2] = responseContents != null ? Integer.valueOf(responseContents.length) : "null";
            objArr[3] = Integer.valueOf(statusLine.getStatusCode());
            objArr[4] = Integer.valueOf(request.getRetryPolicy().getCurrentRetryCount());
            VolleyLog.d(str, objArr);
        }
    }

    protected void logError(String what, String url, long start) {
        long now = SystemClock.elapsedRealtime();
        VolleyLog.v("HTTP ERROR(%s) %d ms to fetch %s", new Object[]{what, Long.valueOf(now - start), url});
    }

    public NetworkResponse performRequest(Request<?> request) throws VolleyError {
        int statusCode;
        long requestStart = SystemClock.elapsedRealtime();
        while (true) {
            long performRequesttime = System.currentTimeMillis();
            Log.w("JsonRequest", new StringBuilder("test1111 start time ").append(performRequesttime).append(request.getUrl()).toString());
            HttpResponse httpResponse = null;
            byte[] responseContents = null;
            Map<String, String> responseHeaders = new HashMap();
            try {
                Map<String, String> headers = new HashMap();
                addCacheHeaders(headers, request.getCacheEntry());
                httpResponse = this.mHttpStack.performRequest(request, headers);
                StatusLine statusLine = httpResponse.getStatusLine();
                statusCode = statusLine.getStatusCode();
                responseHeaders = convertHeaders(httpResponse.getAllHeaders());
                if (statusCode == 304) {
                    return new NetworkResponse(304, request.getCacheEntry().data, responseHeaders, true);
                }
                responseContents = entityToBytes(httpResponse.getEntity());
                logSlowRequests(SystemClock.elapsedRealtime() - requestStart, request, responseContents, statusLine);
                if (statusCode == 200 || statusCode == 204) {
                    Log.w("JsonRequest", new StringBuilder("test1111 performRequest time ").append(System.currentTimeMillis() - performRequesttime).append(request.getUrl()).toString());
                    return new NetworkResponse(statusCode, responseContents, responseHeaders, false);
                } else {
                    throw new IOException();
                }
            } catch (SocketTimeoutException e) {
                Log.w("JsonRequest", new StringBuilder("test1111 SocketTimeoutException ").append(request.getUrl()).toString());
                attemptRetryOnException("socket", request, new TimeoutError());
            } catch (ConnectTimeoutException e2) {
                Log.w("JsonRequest", new StringBuilder("test1111 ConnectTimeoutException ").append(request.getUrl()).toString());
                attemptRetryOnException("connection", request, new TimeoutError());
            } catch (MalformedURLException e3) {
                throw new RuntimeException(new StringBuilder("Bad URL ").append(request.getUrl()).toString(), e3);
            } catch (IOException e4) {
                e = e4;
                if (httpResponse != null) {
                    statusCode = httpResponse.getStatusLine().getStatusCode();
                    VolleyLog.e("Unexpected response code %d for %s", new Object[]{Integer.valueOf(statusCode), request.getUrl()});
                    if (responseContents != null) {
                        NetworkResponse networkResponse = new NetworkResponse(statusCode, responseContents, responseHeaders, false);
                        if (statusCode == 401 || statusCode == 403) {
                            attemptRetryOnException("auth", request, new AuthFailureError(networkResponse));
                        } else {
                            throw new ServerError(networkResponse);
                        }
                    } else {
                        throw new NetworkError(null);
                    }
                } else {
                    IOException e5;
                    throw new NoConnectionError(e5);
                }
            } catch (Exception e6) {
            }
        }
    }
}