package com.inmobi.commons.analytics.bootstrapper;

import com.inmobi.commons.internal.InternalSDKUtil;
import java.util.Map;

public class AutomaticCaptureConfig {
    private boolean a;
    private boolean b;
    private boolean c;

    public AutomaticCaptureConfig() {
        this.a = true;
        this.b = false;
        this.c = true;
    }

    public boolean isAutoLocationCaptureEnabled() {
        return this.c;
    }

    public boolean isAutoPurchaseCaptureEnabled() {
        return this.b;
    }

    public boolean isAutoSessionCaptureEnabled() {
        return this.a;
    }

    public void setFromMap(Map<String, Object> map) {
        this.a = InternalSDKUtil.getBooleanFromMap(map, "session");
        this.b = InternalSDKUtil.getBooleanFromMap(map, "purchase");
        this.c = InternalSDKUtil.getBooleanFromMap(map, "location");
    }
}