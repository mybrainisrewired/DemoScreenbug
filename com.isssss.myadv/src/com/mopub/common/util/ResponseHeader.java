package com.mopub.common.util;

public enum ResponseHeader {
    AD_TIMEOUT("X-AdTimeout"),
    AD_TYPE("X-Adtype"),
    CLICKTHROUGH_URL("X-Clickthrough"),
    CUSTOM_EVENT_DATA("X-Custom-Event-Class-Data"),
    CUSTOM_EVENT_NAME("X-Custom-Event-Class-Name"),
    CUSTOM_EVENT_HTML_DATA("X-Custom-Event-Html-Data"),
    DSP_CREATIVE_ID("X-DspCreativeid"),
    FAIL_URL("X-Failurl"),
    FULL_AD_TYPE("X-Fulladtype"),
    HEIGHT("X-Height"),
    IMPRESSION_URL("X-Imptracker"),
    REDIRECT_URL("X-Launchpage"),
    NATIVE_PARAMS("X-Nativeparams"),
    NETWORK_TYPE("X-Networktype"),
    REFRESH_TIME("X-Refreshtime"),
    SCROLLABLE("X-Scrollable"),
    WARMUP("X-Warmup"),
    WIDTH("X-Width"),
    LOCATION("Location"),
    USER_AGENT("User-Agent"),
    CUSTOM_SELECTOR("X-Customselector");
    private final String key;

    static {
        String str = "X-AdTimeout";
        AD_TIMEOUT = new ResponseHeader("AD_TIMEOUT", 0, "X-AdTimeout");
        str = "X-Adtype";
        AD_TYPE = new ResponseHeader("AD_TYPE", 1, "X-Adtype");
        str = "X-Clickthrough";
        CLICKTHROUGH_URL = new ResponseHeader("CLICKTHROUGH_URL", 2, "X-Clickthrough");
        str = "X-Custom-Event-Class-Data";
        CUSTOM_EVENT_DATA = new ResponseHeader("CUSTOM_EVENT_DATA", 3, "X-Custom-Event-Class-Data");
        str = "X-Custom-Event-Class-Name";
        CUSTOM_EVENT_NAME = new ResponseHeader("CUSTOM_EVENT_NAME", 4, "X-Custom-Event-Class-Name");
        String str2 = "X-Custom-Event-Html-Data";
        CUSTOM_EVENT_HTML_DATA = new ResponseHeader("CUSTOM_EVENT_HTML_DATA", 5, "X-Custom-Event-Html-Data");
        str2 = "X-DspCreativeid";
        DSP_CREATIVE_ID = new ResponseHeader("DSP_CREATIVE_ID", 6, "X-DspCreativeid");
        str2 = "X-Failurl";
        FAIL_URL = new ResponseHeader("FAIL_URL", 7, "X-Failurl");
        str2 = "X-Fulladtype";
        FULL_AD_TYPE = new ResponseHeader("FULL_AD_TYPE", 8, "X-Fulladtype");
        str2 = "X-Height";
        HEIGHT = new ResponseHeader("HEIGHT", 9, "X-Height");
        str2 = "X-Imptracker";
        IMPRESSION_URL = new ResponseHeader("IMPRESSION_URL", 10, "X-Imptracker");
        str2 = "X-Launchpage";
        REDIRECT_URL = new ResponseHeader("REDIRECT_URL", 11, "X-Launchpage");
        str2 = "X-Nativeparams";
        NATIVE_PARAMS = new ResponseHeader("NATIVE_PARAMS", 12, "X-Nativeparams");
        str2 = "X-Networktype";
        NETWORK_TYPE = new ResponseHeader("NETWORK_TYPE", 13, "X-Networktype");
        str2 = "X-Refreshtime";
        REFRESH_TIME = new ResponseHeader("REFRESH_TIME", 14, "X-Refreshtime");
        str2 = "X-Scrollable";
        SCROLLABLE = new ResponseHeader("SCROLLABLE", 15, "X-Scrollable");
        str2 = "X-Warmup";
        WARMUP = new ResponseHeader("WARMUP", 16, "X-Warmup");
        str2 = "X-Width";
        WIDTH = new ResponseHeader("WIDTH", 17, "X-Width");
        str2 = "Location";
        LOCATION = new ResponseHeader("LOCATION", 18, "Location");
        str2 = "User-Agent";
        USER_AGENT = new ResponseHeader("USER_AGENT", 19, "User-Agent");
        str2 = "X-Customselector";
        CUSTOM_SELECTOR = new ResponseHeader("CUSTOM_SELECTOR", 20, "X-Customselector");
        ENUM$VALUES = new ResponseHeader[]{AD_TIMEOUT, AD_TYPE, CLICKTHROUGH_URL, CUSTOM_EVENT_DATA, CUSTOM_EVENT_NAME, CUSTOM_EVENT_HTML_DATA, DSP_CREATIVE_ID, FAIL_URL, FULL_AD_TYPE, HEIGHT, IMPRESSION_URL, REDIRECT_URL, NATIVE_PARAMS, NETWORK_TYPE, REFRESH_TIME, SCROLLABLE, WARMUP, WIDTH, LOCATION, USER_AGENT, CUSTOM_SELECTOR};
    }

    private ResponseHeader(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}