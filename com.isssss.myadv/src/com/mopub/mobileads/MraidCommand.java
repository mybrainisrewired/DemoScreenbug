package com.mopub.mobileads;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.mopub.mobileads.MraidView.PlacementType;
import java.util.Map;

abstract class MraidCommand {
    protected static final String URI_KEY = "uri";
    protected Map<String, String> mParams;
    protected MraidView mView;

    MraidCommand(Map<String, String> params, MraidView view) {
        this.mParams = params;
        this.mView = view;
    }

    abstract void execute();

    protected boolean getBooleanFromParamsForKey(String key) {
        return "true".equals(this.mParams.get(key));
    }

    protected float getFloatFromParamsForKey(String key) {
        if (((String) this.mParams.get(key)) == null) {
            return BitmapDescriptorFactory.HUE_RED;
        }
        try {
            return Float.parseFloat(key);
        } catch (NumberFormatException e) {
            return BitmapDescriptorFactory.HUE_RED;
        }
    }

    protected int getIntFromParamsForKey(String key) {
        String s = (String) this.mParams.get(key);
        if (s == null) {
            return -1;
        }
        try {
            return Integer.parseInt(s, ApiEventType.API_MRAID_USE_CUSTOM_CLOSE);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    protected String getStringFromParamsForKey(String key) {
        return (String) this.mParams.get(key);
    }

    protected boolean isCommandDependentOnUserClick(PlacementType placementType) {
        return false;
    }
}