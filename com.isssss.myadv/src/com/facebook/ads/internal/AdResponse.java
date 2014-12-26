package com.facebook.ads.internal;

import android.content.Context;
import com.facebook.ads.AdError;
import com.inmobi.androidsdk.IMBrowserActivity;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class AdResponse {
    private static final int DEFAULT_MIN_VIEWABILITY_PERCENTAGE = 10;
    private static final int DEFAULT_REFRESH_INTERVAL_SECONDS = 0;
    private static final int DEFAULT_REFRESH_THRESHOLD_SECONDS = 20;
    private final List<AdDataModel> dataModels;
    private final AdError error;
    private final int refreshIntervalMillis;
    private final int refreshThresholdMillis;
    private final int viewabilityThreshold;

    private AdResponse(int refreshIntervalMillis, int refreshThresholdMillis, int viewabilityThresholdPercent, List<AdDataModel> dataModels, AdError error) {
        this.refreshIntervalMillis = refreshIntervalMillis;
        this.refreshThresholdMillis = refreshThresholdMillis;
        this.viewabilityThreshold = viewabilityThresholdPercent;
        this.dataModels = dataModels;
        this.error = error;
    }

    public static AdResponse parseResponse(Context context, JSONObject jsonObject) {
        int refreshIntervalInMilli = jsonObject.optInt("refresh", DEFAULT_REFRESH_INTERVAL_SECONDS) * 1000;
        int refreshThresholdMilli = jsonObject.optInt("refresh_threshold", DEFAULT_REFRESH_THRESHOLD_SECONDS) * 1000;
        int viewabilityThreshold = jsonObject.optInt("min_viewability_percentage", DEFAULT_MIN_VIEWABILITY_PERCENTAGE);
        AdError adError = null;
        JSONObject errorObject = jsonObject.optJSONObject("reason");
        if (errorObject != null) {
            adError = new AdError(errorObject.optInt("code"), errorObject.optString("message"));
        }
        int adType = jsonObject.optInt("ad_type");
        List<AdDataModel> dataModels = new ArrayList();
        JSONArray adsArray = jsonObject.optJSONArray("ads");
        if (adsArray != null && adsArray.length() > 0) {
            int i = DEFAULT_REFRESH_INTERVAL_SECONDS;
            while (i < adsArray.length()) {
                JSONObject adData = adsArray.optJSONObject(i);
                if (adData != null) {
                    AdDataModel dataModel = null;
                    if (adType == AdType.HTML.getValue()) {
                        dataModel = HtmlAdDataModel.fromJSONObject(adData.optJSONObject(IMBrowserActivity.EXPANDDATA));
                    } else if (adType == AdType.NATIVE.getValue()) {
                        dataModel = NativeAdDataModel.fromJSONObject(adData.optJSONObject("metadata"));
                    }
                    if (!(dataModel == null || AdInvalidationUtils.shouldInvalidate(context, dataModel))) {
                        dataModels.add(dataModel);
                    }
                }
                i++;
            }
            if (dataModels.isEmpty()) {
                adError = AdError.NO_FILL;
            }
        }
        return new AdResponse(refreshIntervalInMilli, refreshThresholdMilli, viewabilityThreshold, dataModels, adError);
    }

    public AdDataModel getDataModel() {
        return (this.dataModels == null || this.dataModels.isEmpty()) ? null : (AdDataModel) this.dataModels.get(DEFAULT_REFRESH_INTERVAL_SECONDS);
    }

    public AdError getError() {
        return this.error;
    }

    public int getRefreshIntervalMillis() {
        return this.refreshIntervalMillis;
    }

    public int getRefreshThresholdMillis() {
        return this.refreshThresholdMillis;
    }

    public int getViewabilityThreshold() {
        return this.viewabilityThreshold;
    }
}