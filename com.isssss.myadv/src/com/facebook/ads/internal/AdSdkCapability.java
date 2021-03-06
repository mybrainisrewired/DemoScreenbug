package com.facebook.ads.internal;

import org.json.JSONArray;

enum AdSdkCapability {
    APP_AD(0),
    LINK_AD(1),
    APP_AD_V2(2),
    LINK_AD_V2(3),
    APP_ENGAGEMENT_AD(4),
    AD_CHOICES(5),
    JS_TRIGGER(6),
    JS_TRIGGER_NO_AUTO_IMP_LOGGING(7);
    private static final AdSdkCapability[] supportedCapabilities;
    private static final String supportedCapabilitiesStr;
    private final int value;

    static {
        supportedCapabilities = new AdSdkCapability[]{LINK_AD_V2, APP_ENGAGEMENT_AD, AD_CHOICES, JS_TRIGGER_NO_AUTO_IMP_LOGGING};
        JSONArray array = new JSONArray();
        AdSdkCapability[] arr$ = supportedCapabilities;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            array.put(arr$[i$].getValue());
            i$++;
        }
        supportedCapabilitiesStr = array.toString();
    }

    private AdSdkCapability(int value) {
        this.value = value;
    }

    public static String getSupportedCapabilitiesAsJSONString() {
        return supportedCapabilitiesStr;
    }

    int getValue() {
        return this.value;
    }
}