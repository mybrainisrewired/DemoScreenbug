package com.inmobi.monetization;

import android.view.ViewGroup;
import com.inmobi.commons.InMobi;
import com.inmobi.commons.internal.Log;
import com.inmobi.monetization.internal.Constants;
import com.inmobi.monetization.internal.IMAdListener;
import com.inmobi.monetization.internal.InvalidManifestErrorMessages;
import com.inmobi.monetization.internal.NativeAd;
import com.mopub.common.Preconditions;
import java.util.HashMap;
import java.util.Map;

public class IMNative {
    private String a;
    private String b;
    private String c;
    private IMNativeListener d;
    NativeAd e;
    private String f;
    private IMAdListener g;

    public IMNative(IMNativeListener iMNativeListener) {
        this.a = null;
        this.b = null;
        this.c = null;
        this.d = null;
        this.g = new b(this);
        this.f = InMobi.getAppId();
        a(iMNativeListener);
    }

    public IMNative(String str, IMNativeListener iMNativeListener) {
        this.a = null;
        this.b = null;
        this.c = null;
        this.d = null;
        this.g = new b(this);
        this.f = str;
        a(iMNativeListener);
    }

    protected IMNative(String str, String str2, String str3) {
        this.a = null;
        this.b = null;
        this.c = null;
        this.d = null;
        this.g = new b(this);
        this.a = str;
        this.b = str2;
        this.c = str3;
    }

    private void a(IMNativeListener iMNativeListener) {
        this.d = iMNativeListener;
        this.e = new NativeAd(this.f);
        this.e.setAdListener(this.g);
    }

    public void attachToView(ViewGroup viewGroup) {
        if (this.e != null) {
            this.e.attachToView(viewGroup, this.b, this.c);
        }
    }

    public void detachFromView() {
        if (this.e != null) {
            this.e.detachFromView();
        }
    }

    public String getContent() {
        return this.a;
    }

    public void handleClick(HashMap<String, String> hashMap) {
        if (this.e != null) {
            this.e.handleClick(hashMap);
        }
    }

    public void loadAd() {
        if (this.e != null) {
            this.e.loadAd();
        }
    }

    public void setKeywords(String str) {
        if (str == null || Preconditions.EMPTY_ARGUMENTS.equals(str.trim())) {
            Log.debug(InvalidManifestErrorMessages.LOGGING_TAG, "Keywords cannot be null or blank.");
        } else if (this.e != null) {
            this.e.setKeywords(str);
        }
    }

    @Deprecated
    public void setRefTagParam(String str, String str2) {
        if (str == null || str2 == null) {
            Log.debug(Constants.LOG_TAG, InvalidManifestErrorMessages.MSG_NIL_KEY_VALUE);
        } else if (str.trim().equals(Preconditions.EMPTY_ARGUMENTS) || str2.trim().equals(Preconditions.EMPTY_ARGUMENTS)) {
            Log.debug(Constants.LOG_TAG, InvalidManifestErrorMessages.MSG_EMPTY_KEY_VALUE);
        } else {
            Map hashMap = new HashMap();
            hashMap.put(str, str2);
            if (this.e != null) {
                this.e.setRequestParams(hashMap);
            }
        }
    }

    public void setRequestParams(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            Log.debug(InvalidManifestErrorMessages.LOGGING_TAG, "Request params cannot be null or empty.");
        }
        if (this.e != null) {
            this.e.setRequestParams(map);
        }
    }
}