package com.facebook.ads.internal;

import android.content.Intent;
import android.os.Bundle;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HtmlAdDataModel implements AdDataModel {
    private static final String ACTIVATION_COMMAND_KEY = "activation_command";
    private static final String DETECTION_STRINGS_KEY = "detection_strings";
    private static final String INVALIDATION_BEHAVIOR_KEY = "invalidation_behavior";
    private static final String MARKUP_KEY = "markup";
    private static final String NATIVE_IMPRESSION_URL = "native_impression_report_url";
    private static final String SECONDARY_ACTIVATION_COMMAND_KEY = "secondary_activation_command";
    private static final String SECONDARY_MARKUP_KEY = "secondary_markup";
    private final String activationCommand;
    private final Collection<String> detectionStrings;
    private final AdInvalidationBehavior invalidationBehavior;
    private final String markup;
    private final String nativeImpressionUrl;
    private final String secondaryActivationCommand;
    private final String secondaryMarkup;

    private HtmlAdDataModel(String markup, String secondaryMarkup, String activationCommand, String secondaryActivationCommand, String nativeImpressionUrl, AdInvalidationBehavior invalidationBehavior, Collection<String> detectionStrings) {
        this.markup = markup;
        this.secondaryMarkup = secondaryMarkup;
        this.activationCommand = activationCommand;
        this.secondaryActivationCommand = secondaryActivationCommand;
        this.nativeImpressionUrl = nativeImpressionUrl;
        this.invalidationBehavior = invalidationBehavior;
        this.detectionStrings = detectionStrings;
    }

    public static HtmlAdDataModel fromBundle(Bundle instanceState) {
        return new HtmlAdDataModel(instanceState.getString(MARKUP_KEY), null, instanceState.getString(ACTIVATION_COMMAND_KEY), null, instanceState.getString(NATIVE_IMPRESSION_URL), AdInvalidationBehavior.NONE, null);
    }

    public static HtmlAdDataModel fromIntentExtra(Intent intent) {
        return new HtmlAdDataModel(intent.getStringExtra(MARKUP_KEY), null, intent.getStringExtra(ACTIVATION_COMMAND_KEY), null, intent.getStringExtra(NATIVE_IMPRESSION_URL), AdInvalidationBehavior.NONE, null);
    }

    public static HtmlAdDataModel fromJSONObject(JSONObject dataObject) {
        if (dataObject == null) {
            return null;
        }
        String markup = dataObject.optString(MARKUP_KEY);
        String secondaryMarkup = dataObject.optString(SECONDARY_MARKUP_KEY);
        String activationCommand = dataObject.optString(ACTIVATION_COMMAND_KEY);
        String secondaryActivationCommand = dataObject.optString(SECONDARY_ACTIVATION_COMMAND_KEY);
        String nativeImpressionUrl = dataObject.optString(NATIVE_IMPRESSION_URL);
        AdInvalidationBehavior invalidationBehavior = AdInvalidationBehavior.fromString(dataObject.optString(INVALIDATION_BEHAVIOR_KEY));
        JSONArray detectionStringsArray = null;
        try {
            detectionStringsArray = new JSONArray(dataObject.optString(DETECTION_STRINGS_KEY));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new HtmlAdDataModel(markup, secondaryMarkup, activationCommand, secondaryActivationCommand, nativeImpressionUrl, invalidationBehavior, AdInvalidationUtils.parseDetectionStrings(detectionStringsArray));
    }

    public void addSecondaryToIntentExtra(Intent intent) {
        intent.putExtra(SECONDARY_MARKUP_KEY, this.secondaryMarkup);
        intent.putExtra(SECONDARY_ACTIVATION_COMMAND_KEY, this.secondaryActivationCommand);
    }

    public void addToIntentExtra(Intent intent) {
        intent.putExtra(MARKUP_KEY, this.markup);
        intent.putExtra(ACTIVATION_COMMAND_KEY, this.activationCommand);
        intent.putExtra(NATIVE_IMPRESSION_URL, this.nativeImpressionUrl);
    }

    public String getActivationCommand() {
        return this.activationCommand;
    }

    public Map<String, String> getDataModelMap() {
        Map<String, String> map = new HashMap();
        map.put(MARKUP_KEY, this.secondaryMarkup);
        map.put(ACTIVATION_COMMAND_KEY, this.secondaryActivationCommand);
        map.put(NATIVE_IMPRESSION_URL, this.nativeImpressionUrl);
        return map;
    }

    public Collection<String> getDetectionStrings() {
        return this.detectionStrings;
    }

    public AdInvalidationBehavior getInvalidationBehavior() {
        return this.invalidationBehavior;
    }

    public String getMarkup() {
        return this.markup;
    }

    public String getNativeImpressionUrl() {
        return this.nativeImpressionUrl;
    }

    public String getSendImpressionCommand() {
        return "facebookAd.sendImpression();";
    }

    public Bundle saveToBundle() {
        Bundle instanceState = new Bundle();
        instanceState.putString(MARKUP_KEY, this.markup);
        instanceState.putString(NATIVE_IMPRESSION_URL, this.nativeImpressionUrl);
        return instanceState;
    }
}