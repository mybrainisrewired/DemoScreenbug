package com.inmobi.commons.cache;

import com.google.android.gms.plus.PlusShare;
import com.inmobi.androidsdk.IMBrowserActivity;
import com.inmobi.commons.cache.CacheController.Committer;
import com.inmobi.commons.cache.CacheController.Validator;
import com.inmobi.commons.cache.RetryMechanism.RetryRunnable;
import com.inmobi.commons.internal.InternalSDKUtil;
import com.inmobi.commons.internal.Log;
import com.inmobi.commons.network.RequestBuilderUtils;
import com.inmobi.monetization.internal.imai.db.ClickDatabaseManager;
import com.mopub.common.Preconditions;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductCacheConfig extends ProductConfig {
    public static final int DEFAULT_EXPIRY = 432000;
    public static final int DEFAULT_INTERVAL = 60;
    public static final int DEFAULT_MAX_RETRY = 3;
    private static Timer a;
    private AtomicBoolean b;
    private Map<String, String> c;
    private Validator d;
    private RetryMechanism e;
    private Committer f;
    private long g;

    private class b {
        private String b;
        private long c;

        private b() {
        }
    }

    class a implements RetryRunnable {
        a() {
        }

        public void completed() {
            ProductCacheConfig.this.b.set(false);
        }

        public void run() throws Exception {
            try {
                if (InternalSDKUtil.checkNetworkAvailibility(InternalSDKUtil.getContext())) {
                    ProductCacheConfig.this.a();
                } else {
                    throw new IOException("Network unavailable");
                }
            } catch (Exception e) {
                throw e;
            }
        }
    }

    static {
        a = new Timer();
    }

    public ProductCacheConfig(JSONObject jSONObject) {
        this.b = new AtomicBoolean(false);
        this.c = new HashMap();
        this.d = null;
        this.e = new RetryMechanism(3, 60000, a);
        try {
            loadFromJSON(jSONObject);
        } catch (Exception e) {
            Log.internal(InternalSDKUtil.LOGGING_TAG, "JSON retrieved is invalid.");
        }
    }

    public ProductCacheConfig(JSONObject jSONObject, Committer committer) {
        this(jSONObject);
        this.f = committer;
    }

    private void a() throws Exception {
        try {
            b c = c();
            String a = c.b;
            if (a != null) {
                if (this.d == null) {
                    setData(a);
                } else if (this.d.validate(a(a))) {
                    setData(a);
                } else {
                    throw new IOException("Invalid config.");
                }
                setTimestamp((int) (System.currentTimeMillis() / 1000));
                this.g = c.c;
                Log.internal(InternalSDKUtil.LOGGING_TAG, "Product with url " + getUrl() + " accepted data " + getRawData());
                this.f.onCommit();
            } else {
                setTimestamp((int) (System.currentTimeMillis() / 1000));
                this.g = c.c;
                this.f.onCommit();
            }
        } catch (Exception e) {
            Throwable th = e;
            Log.internal(InternalSDKUtil.LOGGING_TAG, "Error connecting to url, or " + getUrl() + " did not return 200. Purge cache update.", th);
            throw th;
        }
    }

    private void b() {
        if (this.b.compareAndSet(false, true)) {
            this.e.rescheduleTimer(new a());
        }
    }

    private b c() throws IOException {
        String str = Preconditions.EMPTY_ARGUMENTS;
        Map hashMap = new HashMap();
        RequestBuilderUtils.fillIdentityMap(hashMap, null, true);
        RequestBuilderUtils.fillAppInfoMap(hashMap);
        String encodeMapAndconvertToDelimitedString = InternalSDKUtil.encodeMapAndconvertToDelimitedString(hashMap, "&");
        if (encodeMapAndconvertToDelimitedString == null || Preconditions.EMPTY_ARGUMENTS.equals(encodeMapAndconvertToDelimitedString)) {
            encodeMapAndconvertToDelimitedString = getUrl();
        } else if (getUrl().endsWith("?")) {
            encodeMapAndconvertToDelimitedString = getUrl() + encodeMapAndconvertToDelimitedString;
        } else if (getUrl().contains("?")) {
            encodeMapAndconvertToDelimitedString = getUrl() + "&" + encodeMapAndconvertToDelimitedString;
        } else {
            encodeMapAndconvertToDelimitedString = getUrl() + "?" + encodeMapAndconvertToDelimitedString;
        }
        URL url = new URL(encodeMapAndconvertToDelimitedString);
        Log.internal(InternalSDKUtil.LOGGING_TAG, "Sending request to " + encodeMapAndconvertToDelimitedString + " to retreive cache..");
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestProperty("User-Agent", InternalSDKUtil.getUserAgent());
        httpURLConnection.setIfModifiedSince(this.g);
        httpURLConnection.setRequestMethod("GET");
        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode == 304) {
            b bVar = new b(null);
            bVar.b = null;
            bVar.c = httpURLConnection.getLastModified();
            return bVar;
        } else if (responseCode != 200) {
            throw new IOException("Server did not return 200. ");
        } else {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    str = str + readLine;
                } else {
                    bufferedReader.close();
                    b bVar2 = new b(null);
                    bVar2.b = str;
                    bVar2.c = httpURLConnection.getLastModified();
                    return bVar2;
                }
            }
        }
    }

    public ProductConfig getConfig() {
        return this;
    }

    public String getData(Validator validator) {
        if (validator != null) {
            this.d = validator;
        }
        setRetryNumber(0);
        if (getTimestamp() + getExpiry() - ((int) (System.currentTimeMillis() / 1000)) <= 0) {
            b();
        }
        return getRawData();
    }

    public String getData(Map<String, String> map, Validator validator) {
        if (map != null) {
            this.c = map;
        }
        return getData(validator);
    }

    public Map<String, String> getMap() {
        return this.c;
    }

    public Validator getValidator() {
        return this.d;
    }

    public final void loadFromJSON(JSONObject jSONObject) {
        setExpiry(InternalSDKUtil.getIntFromJSON(jSONObject, "expiry", DEFAULT_EXPIRY));
        setMaxRetry(InternalSDKUtil.getIntFromJSON(jSONObject, "maxRetry", DEFAULT_MAX_RETRY));
        setRetryInterval(InternalSDKUtil.getIntFromJSON(jSONObject, "retryInterval", DEFAULT_INTERVAL));
        this.g = InternalSDKUtil.getLongFromJSON(jSONObject, "lastModified", 0);
        setUrl(InternalSDKUtil.getStringFromJSON(jSONObject, PlusShare.KEY_CALL_TO_ACTION_URL, Preconditions.EMPTY_ARGUMENTS));
        setProtocol(InternalSDKUtil.getStringFromJSON(jSONObject, "protocol", "json"));
        this.e = new RetryMechanism(getMaxRetry(), getRetryInterval() * 1000, a);
        setTimestamp(InternalSDKUtil.getIntFromJSON(jSONObject, ClickDatabaseManager.COLUMN_TIMESTAMP, 0));
        setData(InternalSDKUtil.getStringFromJSON(jSONObject, IMBrowserActivity.EXPANDDATA, null));
    }

    protected void reset() {
        setExpiry(0);
        setRetryInterval(0);
        setMaxRetry(0);
        setTimestamp(0);
        setUrl(null);
        setProtocol(null);
        setData(null);
    }

    public void setMap(Map<String, String> map) {
        this.c = map;
    }

    public void setValidator(Validator validator) {
        this.d = validator;
    }

    public JSONObject toJSON() {
        JSONObject jSONObject;
        try {
            jSONObject = new JSONObject("{expiry:" + getExpiry() + "," + "maxRetry:" + getMaxRetry() + "," + "retryInterval:" + getRetryInterval() + "," + "protocol:" + getProtocol() + ",timestamp:" + getTimestamp() + "}");
            try {
                jSONObject.put(PlusShare.KEY_CALL_TO_ACTION_URL, getUrl());
                jSONObject.put(IMBrowserActivity.EXPANDDATA, getRawData());
                jSONObject.put("lastModified", this.g);
            } catch (JSONException e) {
                th = e;
                Log.internal(InternalSDKUtil.LOGGING_TAG, "Ill formed JSON product(" + getUrl() + ") toString", th);
                return jSONObject;
            }
        } catch (JSONException e2) {
            jSONObject = new JSONObject();
            Throwable th2 = e2;
            Log.internal(InternalSDKUtil.LOGGING_TAG, "Ill formed JSON product(" + getUrl() + ") toString", th2);
            return jSONObject;
        }
        return jSONObject;
    }

    public String toString() {
        return toJSON().toString();
    }
}