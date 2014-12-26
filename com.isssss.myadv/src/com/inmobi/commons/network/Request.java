package com.inmobi.commons.network;

import com.inmobi.commons.internal.InternalSDKUtil;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.Preconditions;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class Request {
    protected static HashMap<String, Object> mHeaders;
    private Format a;
    private final String b;
    private String c;
    private Method d;
    private int e;
    protected HashMap<String, Object> mReqParams;

    public enum Format {
        KEY_VAL,
        JSON;

        static {
            KEY_VAL = new com.inmobi.commons.network.Request.Format("KEY_VAL", 0);
            JSON = new com.inmobi.commons.network.Request.Format("JSON", 1);
            a = new com.inmobi.commons.network.Request.Format[]{KEY_VAL, JSON};
        }
    }

    public enum Method {
        GET,
        POST,
        PUT;

        static {
            GET = new com.inmobi.commons.network.Request.Method("GET", 0);
            POST = new com.inmobi.commons.network.Request.Method("POST", 1);
            PUT = new com.inmobi.commons.network.Request.Method("PUT", 2);
            a = new com.inmobi.commons.network.Request.Method[]{GET, POST, PUT};
        }
    }

    static /* synthetic */ class a {
        static final /* synthetic */ int[] a;

        static {
            a = new int[com.inmobi.commons.network.Request.Format.values().length];
            try {
                a[com.inmobi.commons.network.Request.Format.KEY_VAL.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            a[com.inmobi.commons.network.Request.Format.JSON.ordinal()] = 2;
        }
    }

    public Request(String str, Format format, Method method) {
        this.a = Format.KEY_VAL;
        this.b = "User-Agent";
        this.c = null;
        this.d = Method.POST;
        this.e = 0;
        this.mReqParams = new HashMap();
        mHeaders = new HashMap();
        RequestBuilderUtils.fillIdentityMap(this.mReqParams, null, true);
        mHeaders.put("User-Agent", InternalSDKUtil.getUserAgent());
        this.a = format;
        this.c = str;
        this.d = method;
    }

    private String a() {
        Map encodedMap = InternalSDKUtil.getEncodedMap(this.mReqParams);
        switch (a.a[this.a.ordinal()]) {
            case MMAdView.TRANSITION_FADE:
                return InternalSDKUtil.encodeMapAndconvertToDelimitedString(this.mReqParams, "&");
            case MMAdView.TRANSITION_UP:
                return new JSONObject(encodedMap).toString();
            default:
                return null;
        }
    }

    public void fillAppInfo() {
        RequestBuilderUtils.fillAppInfoMap(this.mReqParams);
    }

    public void fillCustomInfo(Map<String, Object> map) {
        if (map != null) {
            this.mReqParams.putAll(map);
        }
    }

    public void fillDemogInfo() {
        RequestBuilderUtils.fillDemogMap(this.mReqParams);
    }

    public void fillDeviceInfo() {
        RequestBuilderUtils.fillDeviceMap(this.mReqParams);
    }

    public void fillLocationInfo() {
        RequestBuilderUtils.fillLocationMap(this.mReqParams);
    }

    public Map<String, String> getHeaders() {
        return InternalSDKUtil.getEncodedMap(mHeaders);
    }

    protected String getPostBody() {
        return this.d != Method.GET ? a() : null;
    }

    protected String getQueryParams() {
        return this.d == Method.GET ? a() : null;
    }

    protected Method getRequestMethod() {
        return this.d;
    }

    public int getTimeout() {
        return this.e;
    }

    protected String getUrl() {
        return this.c;
    }

    public void setTimeout(int i) {
        this.e = i;
    }

    protected void setUrl(String str) {
        int i;
        int i2 = 1;
        if (str != null) {
            i = 1;
        } else {
            i = 0;
        }
        if (Preconditions.EMPTY_ARGUMENTS.equals(str.trim())) {
            i2 = 0;
        }
        if ((i & i2) != 0) {
            this.c = str;
        }
    }
}