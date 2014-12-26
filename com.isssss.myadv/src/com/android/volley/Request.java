package com.android.volley;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import com.android.volley.Cache.Entry;
import com.android.volley.Response.ErrorListener;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

public abstract class Request<T> implements Comparable<Request<T>> {
    private static final String DEFAULT_PARAMS_ENCODING = "UTF-8";
    private static final long SLOW_REQUEST_THRESHOLD_MS = 3000;
    private Entry mCacheEntry;
    private boolean mCanceled;
    private final int mDefaultTrafficStatsTag;
    private final ErrorListener mErrorListener;
    private final MarkerLog mEventLog;
    private final int mMethod;
    private long mRequestBirthTime;
    private RequestQueue mRequestQueue;
    private boolean mResponseDelivered;
    private RetryPolicy mRetryPolicy;
    private Integer mSequence;
    private boolean mShouldCache;
    private Object mTag;
    private final String mUrl;

    class AnonymousClass_1 implements Runnable {
        private final /* synthetic */ String val$tag;
        private final /* synthetic */ long val$threadId;

        AnonymousClass_1(String str, long j) {
            this.val$tag = str;
            this.val$threadId = j;
        }

        public void run() {
            Request.this.mEventLog.add(this.val$tag, this.val$threadId);
            Request.this.mEventLog.finish(toString());
        }
    }

    public static interface Method {
        public static final int DELETE = 3;
        public static final int DEPRECATED_GET_OR_POST = -1;
        public static final int GET = 0;
        public static final int POST = 1;
        public static final int PUT = 2;
    }

    public enum Priority {
        LOW,
        NORMAL,
        HIGH,
        IMMEDIATE;

        static {
            LOW = new com.android.volley.Request.Priority("LOW", 0);
            NORMAL = new com.android.volley.Request.Priority("NORMAL", 1);
            HIGH = new com.android.volley.Request.Priority("HIGH", 2);
            IMMEDIATE = new com.android.volley.Request.Priority("IMMEDIATE", 3);
            ENUM$VALUES = new com.android.volley.Request.Priority[]{LOW, NORMAL, HIGH, IMMEDIATE};
        }
    }

    public Request(int method, String url, ErrorListener listener) {
        MarkerLog markerLog;
        if (MarkerLog.ENABLED) {
            markerLog = new MarkerLog();
        } else {
            markerLog = null;
        }
        this.mEventLog = markerLog;
        this.mShouldCache = false;
        this.mCanceled = false;
        this.mResponseDelivered = false;
        this.mRequestBirthTime = 0;
        this.mCacheEntry = null;
        this.mMethod = method;
        this.mUrl = url;
        this.mErrorListener = listener;
        setRetryPolicy(new DefaultRetryPolicy());
        this.mDefaultTrafficStatsTag = TextUtils.isEmpty(url) ? 0 : Uri.parse(url).getHost().hashCode();
    }

    public Request(String url, ErrorListener listener) {
        this(-1, url, listener);
    }

    private byte[] encodeParameters(Map<String, String> params, String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();
        try {
            Iterator it = params.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry) it.next();
                encodedParams.append(URLEncoder.encode((String) entry.getKey(), paramsEncoding));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode((String) entry.getValue(), paramsEncoding));
                encodedParams.append('&');
            }
            return encodedParams.toString().getBytes(paramsEncoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(new StringBuilder("Encoding not supported: ").append(paramsEncoding).toString(), e);
        }
    }

    public void addMarker(String tag) {
        if (MarkerLog.ENABLED) {
            this.mEventLog.add(tag, Thread.currentThread().getId());
        } else if (this.mRequestBirthTime == 0) {
            this.mRequestBirthTime = SystemClock.elapsedRealtime();
        }
    }

    public void cancel() {
        this.mCanceled = true;
    }

    public int compareTo(Request<T> other) {
        Priority left = getPriority();
        Priority right = other.getPriority();
        return left == right ? this.mSequence.intValue() - other.mSequence.intValue() : right.ordinal() - left.ordinal();
    }

    public void deliverError(VolleyError error) {
        if (this.mErrorListener != null) {
            this.mErrorListener.onErrorResponse(error);
        }
    }

    protected abstract void deliverResponse(T t);

    void finish(String tag) {
        if (this.mRequestQueue != null) {
            this.mRequestQueue.finish(this);
        }
        if (MarkerLog.ENABLED) {
            long threadId = Thread.currentThread().getId();
            if (Looper.myLooper() != Looper.getMainLooper()) {
                new Handler(Looper.getMainLooper()).post(new AnonymousClass_1(tag, threadId));
            } else {
                this.mEventLog.add(tag, threadId);
                this.mEventLog.finish(toString());
            }
        } else {
            if (SystemClock.elapsedRealtime() - this.mRequestBirthTime >= 3000) {
                VolleyLog.d("%d ms: %s", new Object[]{Long.valueOf(requestTime), toString()});
            }
        }
    }

    public byte[] getBody() throws AuthFailureError {
        Map<String, String> params = getParams();
        return (params == null || params.size() <= 0) ? null : encodeParameters(params, getParamsEncoding());
    }

    public String getBodyContentType() {
        return new StringBuilder("application/x-www-form-urlencoded; charset=").append(getParamsEncoding()).toString();
    }

    public Entry getCacheEntry() {
        return this.mCacheEntry;
    }

    public String getCacheKey() {
        return getUrl();
    }

    public Map<String, String> getHeaders() throws AuthFailureError {
        return Collections.emptyMap();
    }

    public int getMethod() {
        return this.mMethod;
    }

    protected Map<String, String> getParams() throws AuthFailureError {
        return null;
    }

    protected String getParamsEncoding() {
        return DEFAULT_PARAMS_ENCODING;
    }

    public byte[] getPostBody() throws AuthFailureError {
        Map<String, String> postParams = getPostParams();
        return (postParams == null || postParams.size() <= 0) ? null : encodeParameters(postParams, getPostParamsEncoding());
    }

    public String getPostBodyContentType() {
        return getBodyContentType();
    }

    protected Map<String, String> getPostParams() throws AuthFailureError {
        return getParams();
    }

    protected String getPostParamsEncoding() {
        return getParamsEncoding();
    }

    public Priority getPriority() {
        return Priority.NORMAL;
    }

    public RetryPolicy getRetryPolicy() {
        return this.mRetryPolicy;
    }

    public final int getSequence() {
        if (this.mSequence != null) {
            return this.mSequence.intValue();
        }
        throw new IllegalStateException("getSequence called before setSequence");
    }

    public Object getTag() {
        return this.mTag;
    }

    public final int getTimeoutMs() {
        return this.mRetryPolicy.getCurrentTimeout();
    }

    public int getTrafficStatsTag() {
        return this.mDefaultTrafficStatsTag;
    }

    public String getUrl() {
        return this.mUrl;
    }

    public boolean hasHadResponseDelivered() {
        return this.mResponseDelivered;
    }

    public boolean isCanceled() {
        return this.mCanceled;
    }

    public void markDelivered() {
        this.mResponseDelivered = true;
    }

    protected VolleyError parseNetworkError(VolleyError volleyError) {
        return volleyError;
    }

    protected abstract Response<T> parseNetworkResponse(NetworkResponse networkResponse);

    public void setCacheEntry(Entry entry) {
        this.mCacheEntry = entry;
    }

    public void setRequestQueue(RequestQueue requestQueue) {
        this.mRequestQueue = requestQueue;
    }

    public void setRetryPolicy(RetryPolicy retryPolicy) {
        this.mRetryPolicy = retryPolicy;
    }

    public final void setSequence(int sequence) {
        this.mSequence = Integer.valueOf(sequence);
    }

    public final void setShouldCache(boolean shouldCache) {
        this.mShouldCache = shouldCache;
    }

    public void setTag(Object tag) {
        this.mTag = tag;
    }

    public final boolean shouldCache() {
        return this.mShouldCache;
    }

    public String toString() {
        return new StringBuilder(String.valueOf(this.mCanceled ? "[X] " : "[ ] ")).append(getUrl()).append(" ").append(new StringBuilder("0x").append(Integer.toHexString(getTrafficStatsTag())).toString()).append(" ").append(getPriority()).append(" ").append(this.mSequence).toString();
    }

    public void writeBody(HttpURLConnection connection) throws Exception {
    }
}