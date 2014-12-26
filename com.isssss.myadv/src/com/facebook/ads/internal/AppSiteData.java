package com.facebook.ads.internal;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class AppSiteData {
    private final String appLinkUri;
    private final String className;
    private final String fallbackUrl;
    private final List<String> keyHashes;
    private final String marketUri;
    private final String packageName;

    private AppSiteData(String packageName, String className, String appLinkUri, List<String> keyHashes, String marketUri, String fallbackUrl) {
        this.packageName = packageName;
        this.className = className;
        this.appLinkUri = appLinkUri;
        this.keyHashes = keyHashes;
        this.marketUri = marketUri;
        this.fallbackUrl = fallbackUrl;
    }

    public static AppSiteData fromJSONObject(JSONObject dataObject) {
        if (dataObject == null) {
            return null;
        }
        String packageName = dataObject.optString("package");
        String appsite = dataObject.optString("appsite");
        String appsiteUri = dataObject.optString("appsite_url");
        JSONArray keyHashesArray = dataObject.optJSONArray("key_hashes");
        List<String> keyHashes = new ArrayList();
        if (keyHashesArray != null) {
            int i = 0;
            while (i < keyHashesArray.length()) {
                keyHashes.add(keyHashesArray.optString(i));
                i++;
            }
        }
        return new AppSiteData(packageName, appsite, appsiteUri, keyHashes, dataObject.optString("market_uri"), dataObject.optString("fallback_url"));
    }

    public String getAppLinkUri() {
        return this.appLinkUri;
    }

    public String getClassName() {
        return this.className;
    }

    public String getFallbackUrl() {
        return this.fallbackUrl;
    }

    public List<String> getKeyHashes() {
        return this.keyHashes;
    }

    public String getMarketUri() {
        return this.marketUri;
    }

    public String getPackageName() {
        return this.packageName;
    }
}