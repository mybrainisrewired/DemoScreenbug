package com.millennialmedia.android;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import com.inmobi.commons.ads.cache.AdDatabaseHelper;
import java.lang.ref.WeakReference;
import org.json.JSONException;
import org.json.JSONObject;

class AdProperties {
    private static final String TAG;
    WeakReference<Context> contextRef;

    static {
        TAG = AdProperties.class.getName();
    }

    AdProperties(Context context) {
        this.contextRef = new WeakReference(context);
    }

    private JSONObject getPermissions() throws JSONException {
        JSONObject params = new JSONObject();
        params.put("android.permission.ACCESS_FINE_LOCATION", getContext().checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION") == 0);
        return params;
    }

    private JSONObject getScreen() throws JSONException {
        JSONObject params = new JSONObject();
        params.put(MMLayout.KEY_HEIGHT, getScreenDpiIndependentHeight());
        params.put(MMLayout.KEY_WIDTH, getScreenDpiIndependentWidth());
        return params;
    }

    private JSONObject getSupports() throws JSONException {
        JSONObject params = new JSONObject();
        Context context = getContext();
        params.put(Event.INTENT_TXT_MESSAGE, Boolean.parseBoolean(MMSDK.getSupportsSms(context)));
        params.put(Event.INTENT_PHONE_CALL, Boolean.parseBoolean(MMSDK.getSupportsTel(context)));
        params.put(Event.INTENT_CALENDAR_EVENT, MMSDK.getSupportsCalendar());
        params.put("storePicture", false);
        params.put("inlineVideo", true);
        return params;
    }

    String getAdDpiIndependentHeight() {
        return getScreenDpiIndependentHeight();
    }

    String getAdDpiIndependentWidth() {
        return getScreenDpiIndependentWidth();
    }

    public JSONObject getAdProperties(View adView) {
        JSONObject adProps = new JSONObject();
        try {
            adProps.put("screen", getScreen());
            adProps.put(AdDatabaseHelper.TABLE_AD, Utils.getViewDimensions(adView));
            adProps.put("do", MMSDK.getOrientation(getContext()));
            adProps.put("supports", getSupports());
            adProps.put("device", BridgeMMDevice.getDeviceInfo(getContext()));
            adProps.put("permissions", getPermissions());
            adProps.put("maxSize", getScreen());
        } catch (JSONException e) {
            MMLog.e(TAG, "Error when building ad properties", e);
        }
        return adProps;
    }

    Context getContext() {
        return (Context) this.contextRef.get();
    }

    String getScreenDpiIndependentHeight() {
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        return String.valueOf((int) (((float) metrics.heightPixels) / metrics.density));
    }

    String getScreenDpiIndependentWidth() {
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        return String.valueOf((int) (((float) metrics.widthPixels) / metrics.density));
    }
}