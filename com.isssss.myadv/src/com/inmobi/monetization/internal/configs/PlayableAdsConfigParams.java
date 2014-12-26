package com.inmobi.monetization.internal.configs;

import com.inmobi.commons.internal.InternalSDKUtil;
import java.util.Map;

public class PlayableAdsConfigParams {
    String a;
    String b;

    public PlayableAdsConfigParams() {
        this.a = "3b3941b6-4683-400a-a542-6ccd3d13abe6";
        this.b = "f1478eab-8535-4c85-8ab7-5bdf3f2f7706";
    }

    public String getSecretKey() {
        return this.b;
    }

    public String getSecretToken() {
        return this.a;
    }

    public void setFromMap(Map<String, Object> map) {
        this.a = InternalSDKUtil.getStringFromMap(map, "st");
        this.b = InternalSDKUtil.getStringFromMap(map, "sk");
    }
}