package com.inmobi.monetization.internal;

import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.inmobi.commons.internal.EncryptionUtils;
import com.inmobi.commons.internal.InternalSDKUtil;
import com.inmobi.commons.internal.Log;
import com.inmobi.commons.network.Request;
import com.inmobi.commons.network.Request.Format;
import com.inmobi.commons.network.Request.Method;
import com.inmobi.commons.network.RequestBuilderUtils;
import com.inmobi.monetization.internal.configs.Initializer;
import com.inmobi.monetization.internal.configs.PkInitilaizer;
import com.mopub.common.Preconditions;
import java.util.HashMap;
import java.util.Map;

// compiled from: AdRequest.java
class i extends Request {
    protected static String a;
    private static byte[] e;
    private static byte[] f;
    private static byte[] g;
    String b;
    String c;
    String d;

    static {
        a = "http://i.w.inmobi.com/showad.asm";
    }

    public i() {
        super(a, Format.KEY_VAL, Method.POST);
        this.b = Preconditions.EMPTY_ARGUMENTS;
        this.c = Preconditions.EMPTY_ARGUMENTS;
        this.d = Preconditions.EMPTY_ARGUMENTS;
        RequestBuilderUtils.fillIdentityMap(this.mReqParams, Initializer.getConfigParams().getDeviceIdMaskMap(), false);
        RequestBuilderUtils.fillAppInfoMap(this.mReqParams);
        RequestBuilderUtils.fillDemogMap(this.mReqParams);
        RequestBuilderUtils.fillDeviceMap(this.mReqParams);
        RequestBuilderUtils.fillLocationMap(this.mReqParams);
        setTimeout(Initializer.getConfigParams().getFetchTimeOut());
    }

    String a(String str) {
        Map hashMap = new HashMap();
        g = EncryptionUtils.generateKey(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES);
        f = EncryptionUtils.generateKey(ApiEventType.API_MRAID_GET_ORIENTATION);
        e = EncryptionUtils.keag();
        this.b = PkInitilaizer.getConfigParams().getExponent();
        this.c = PkInitilaizer.getConfigParams().getModulus();
        this.d = PkInitilaizer.getConfigParams().getVersion();
        if (this.b.equals(Preconditions.EMPTY_ARGUMENTS) || this.c.equals(Preconditions.EMPTY_ARGUMENTS) || this.d.equals(Preconditions.EMPTY_ARGUMENTS)) {
            Log.debug(InvalidManifestErrorMessages.LOGGING_TAG, "Exception retreiving Ad due to key problem");
            return null;
        } else {
            hashMap.put("sm", EncryptionUtils.SeMeGe(str, e, f, g, this.c, this.b));
            hashMap.put("sn", this.d);
            return InternalSDKUtil.encodeMapAndconvertToDelimitedString(hashMap, "&");
        }
    }

    void a(Map<String, String> map) {
        if (this.mReqParams != null && map != null && !map.isEmpty()) {
            this.mReqParams.putAll(map);
        }
    }

    byte[] a() {
        return f;
    }

    void b(Map<String, String> map) {
        if (this.mReqParams != null && map != null) {
            this.mReqParams.putAll(map);
        }
    }

    byte[] b() {
        return e;
    }

    protected String getPostBody() {
        String postBody = super.getPostBody();
        Log.internal(Constants.LOG_TAG, "Raw Postbody: " + postBody);
        return a(postBody);
    }

    protected void setUrl(String str) {
        super.setUrl(str);
    }
}