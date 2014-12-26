package com.facebook.ads.internal;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.facebook.ads.NativeAd.Image;
import com.facebook.ads.NativeAd.Rating;
import com.facebook.ads.internal.action.AdAction;
import com.facebook.ads.internal.action.AdActionFactory;
import com.isssss.myadv.constant.ParamConst;
import com.isssss.myadv.dao.BannerInfoTable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NativeAdDataModel implements AdDataModel {
    private static final String TAG;
    private final Uri adCommand;
    private final String body;
    private final String callToAction;
    private boolean clickLogged;
    private final String clickReportUrl;
    private final Collection<String> detectionStrings;
    private final Image icon;
    private final Image image;
    private boolean impressionLogged;
    private final String impressionReportUrl;
    private final AdInvalidationBehavior invalidationBehavior;
    private final String socialContext;
    private final Rating starRating;
    private final String title;

    static {
        TAG = NativeAdDataModel.class.getSimpleName();
    }

    private NativeAdDataModel(Uri adCommand, String title, String body, String callToAction, String socialContext, Image icon, Image image, Rating starRating, String impressionReportUrl, String clickReportUrl, AdInvalidationBehavior invalidationBehavior, Collection<String> detectionStrings) {
        this.adCommand = adCommand;
        this.title = title;
        this.body = body;
        this.callToAction = callToAction;
        this.socialContext = socialContext;
        this.icon = icon;
        this.image = image;
        this.starRating = starRating;
        this.impressionReportUrl = impressionReportUrl;
        this.clickReportUrl = clickReportUrl;
        this.invalidationBehavior = invalidationBehavior;
        this.detectionStrings = detectionStrings;
    }

    public static NativeAdDataModel fromJSONObject(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        Uri adCommand = Uri.parse(jsonObject.optString("fbad_command"));
        String title = jsonObject.optString(BannerInfoTable.COLUMN_TITLE);
        String body = jsonObject.optString("body");
        String callToAction = jsonObject.optString("call_to_action");
        String socialContext = jsonObject.optString("social_context");
        Image icon = Image.fromJSONObject(jsonObject.optJSONObject(ParamConst.ICON_URL));
        Image image = Image.fromJSONObject(jsonObject.optJSONObject("image"));
        Rating starRating = Rating.fromJSONObject(jsonObject.optJSONObject("star_rating"));
        String impressionReportUrl = jsonObject.optString("impression_report_url");
        String clickReportUrl = jsonObject.optString("click_report_url");
        AdInvalidationBehavior invalidationBehavior = AdInvalidationBehavior.fromString(jsonObject.optString("invalidation_behavior"));
        JSONArray detectionStringsArray = null;
        try {
            detectionStringsArray = new JSONArray(jsonObject.optString("detection_strings"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new NativeAdDataModel(adCommand, title, body, callToAction, socialContext, icon, image, starRating, impressionReportUrl, clickReportUrl, invalidationBehavior, AdInvalidationUtils.parseDetectionStrings(detectionStringsArray));
    }

    public String getBody() {
        return this.body;
    }

    public String getCallToAction() {
        return this.callToAction;
    }

    public Collection<String> getDetectionStrings() {
        return this.detectionStrings;
    }

    public Image getIcon() {
        return this.icon;
    }

    public Image getImage() {
        return this.image;
    }

    public AdInvalidationBehavior getInvalidationBehavior() {
        return this.invalidationBehavior;
    }

    public String getSocialContext() {
        return this.socialContext;
    }

    public Rating getStarRating() {
        return this.starRating;
    }

    public String getTitle() {
        return this.title;
    }

    public void handleClick(Context context, Map<String, Object> touchData) {
        if (!this.clickLogged) {
            Map<String, String> extraData = new HashMap();
            extraData.put("touch", AdUtilities.jsonEncode(touchData));
            new OpenUrlTask(extraData).execute(new String[]{this.clickReportUrl});
            this.clickLogged = true;
            AdUtilities.displayDebugMessage(context, "Click logged");
        }
        AdAction adAction = AdActionFactory.getAdAction(context, this.adCommand);
        if (adAction != null) {
            try {
                adAction.execute(null);
            } catch (Exception e) {
                Log.e(TAG, "Error executing action", e);
            }
        }
    }

    public boolean isValid() {
        return (this.title == null || this.title.length() <= 0 || this.callToAction == null || this.callToAction.length() <= 0 || this.icon == null || this.image == null) ? false : true;
    }

    public void logImpression() {
        if (!this.impressionLogged) {
            new OpenUrlTask().execute(new String[]{this.impressionReportUrl});
            this.impressionLogged = true;
        }
    }
}