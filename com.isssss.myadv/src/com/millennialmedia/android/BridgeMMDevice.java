package com.millennialmedia.android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.google.android.gms.cast.Cast;
import com.google.android.gms.plus.PlusShare;
import com.inmobi.commons.analytics.iat.impl.AdTrackerConstants;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class BridgeMMDevice extends MMJSObject {
    private static final String CALL = "call";
    private static final String COMPOSE_EMAIL = "composeEmail";
    private static final String COMPOSE_SMS = "composeSms";
    private static final String ENABLE_HARDWARE_ACCEL = "enableHardwareAcceleration";
    private static final String GET_AVAIL_SCHEMES = "getAvailableSchemes";
    private static final String GET_INFO = "getInfo";
    private static final String GET_LOCATION = "getLocation";
    private static final String GET_ORIENTATION = "getOrientation";
    private static final String IS_SCHEME_AVAIL = "isSchemeAvailable";
    private static final String OPEN_APP_STORE = "openAppStore";
    private static final String OPEN_URL = "openUrl";
    private static final String SET_MMDID = "setMMDID";
    private static final String SHOW_MAP = "showMap";
    private static final String TAG = "BridgeMMDevice";
    private static final String TWEET = "tweet";

    BridgeMMDevice() {
    }

    static JSONObject getDeviceInfo(Context context) {
        JSONObject jsonCookieObject;
        JSONObject jsonObject = null;
        try {
            JSONObject jsonObject2 = new JSONObject();
            try {
                jsonObject2.put("sdkVersion", MMSDK.VERSION);
                jsonObject2.put("connection", MMSDK.getConnectionType(context));
                jsonObject2.put("platform", "Android");
                if (VERSION.RELEASE != null) {
                    jsonObject2.put("version", VERSION.RELEASE);
                }
                if (Build.MODEL != null) {
                    jsonObject2.put("device", Build.MODEL);
                }
                jsonObject2.put("mmdid", MMSDK.getMMdid(context));
                DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                jsonObject2.put("density", new Float(metrics.density));
                jsonObject2.put(MMLayout.KEY_HEIGHT, new Integer(metrics.heightPixels));
                jsonObject2.put(MMLayout.KEY_WIDTH, new Integer(metrics.widthPixels));
                Locale locale = Locale.getDefault();
                if (locale != null) {
                    jsonObject2.put("language", locale.getLanguage());
                    jsonObject2.put("country", locale.getCountry());
                }
                JSONObject jsonCookieObject2 = new JSONObject();
                try {
                    jsonCookieObject2.put("name", "MAC-ID");
                    jsonCookieObject2.put("path", "/");
                    jsonCookieObject2.put("value", MMSDK.macId);
                    JSONArray jsonCookieArray = new JSONArray();
                    try {
                        jsonCookieArray.put(jsonCookieObject2);
                        jsonObject2.put("cookies", jsonCookieArray);
                        return jsonObject2;
                    } catch (JSONException e) {
                        e = e;
                        jsonObject = jsonObject2;
                        MMLog.e(TAG, "Bridge getting deviceInfo json exception: ", e);
                        return jsonObject;
                    }
                } catch (JSONException e2) {
                    e = e2;
                    jsonObject = jsonObject2;
                    MMLog.e(TAG, "Bridge getting deviceInfo json exception: ", e);
                    return jsonObject;
                }
            } catch (JSONException e3) {
                e = e3;
                jsonObject = jsonObject2;
                MMLog.e(TAG, "Bridge getting deviceInfo json exception: ", e);
                return jsonObject;
            }
        } catch (JSONException e4) {
            JSONException e5 = e4;
            MMLog.e(TAG, "Bridge getting deviceInfo json exception: ", e5);
            return jsonObject;
        }
    }

    public MMJSResponse call(Map<String, String> arguments) {
        Context context = (Context) this.contextRef.get();
        String number = (String) arguments.get("number");
        if (context == null || number == null) {
            return null;
        }
        Intent intent;
        MMLog.d(TAG, String.format("Dialing Phone: %s", new Object[]{number}));
        if (Boolean.parseBoolean((String) arguments.get("dial")) && context.checkCallingOrSelfPermission("android.permission.CALL_PHONE") == 0) {
            intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + number));
        } else {
            intent = new Intent("android.intent.action.VIEW", Uri.parse("tel:" + number));
        }
        IntentUtils.startActivity(context, intent);
        Event.intentStarted(context, Event.INTENT_PHONE_CALL, getAdImplId((String) arguments.get("PROPERTY_EXPANDING")));
        return MMJSResponse.responseWithSuccess();
    }

    public MMJSResponse composeEmail(Map<String, String> arguments) {
        Context context = (Context) this.contextRef.get();
        String recipients = (String) arguments.get("recipient");
        String subject = (String) arguments.get("subject");
        String message = (String) arguments.get("message");
        if (context == null) {
            return null;
        }
        MMLog.d(TAG, "Creating email");
        Intent emailIntent = new Intent("android.intent.action.SEND");
        emailIntent.setType("plain/text");
        if (recipients != null) {
            emailIntent.putExtra("android.intent.extra.EMAIL", recipients.split(","));
        }
        if (subject != null) {
            emailIntent.putExtra("android.intent.extra.SUBJECT", subject);
        }
        if (message != null) {
            emailIntent.putExtra("android.intent.extra.TEXT", message);
        }
        IntentUtils.startActivity(context, emailIntent);
        Event.intentStarted(context, Event.INTENT_EMAIL, getAdImplId((String) arguments.get("PROPERTY_EXPANDING")));
        return MMJSResponse.responseWithSuccess();
    }

    public MMJSResponse composeSms(Map<String, String> arguments) {
        Context context = (Context) this.contextRef.get();
        String number = (String) arguments.get("number");
        String message = (String) arguments.get("message");
        if (context == null || number == null) {
            return null;
        }
        MMLog.d(TAG, String.format("Creating sms: %s", new Object[]{number}));
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("sms:" + number));
        if (message != null) {
            intent.putExtra("sms_body", message);
        }
        IntentUtils.startActivity(context, intent);
        Event.intentStarted(context, Event.INTENT_TXT_MESSAGE, getAdImplId((String) arguments.get("PROPERTY_EXPANDING")));
        return MMJSResponse.responseWithSuccess("SMS Sent");
    }

    public MMJSResponse enableHardwareAcceleration(Map<String, String> arguments) {
        MMLog.d(TAG, "hardware accel call" + arguments);
        String enabled = (String) arguments.get("enabled");
        MMWebView webView = (MMWebView) this.mmWebViewRef.get();
        if (webView == null || webView == null) {
            return null;
        }
        if (Boolean.parseBoolean(enabled)) {
            webView.enableHardwareAcceleration();
        } else {
            webView.disableAllAcceleration();
        }
        return MMJSResponse.responseWithSuccess();
    }

    MMJSResponse executeCommand(String name, Map<String, String> arguments) {
        if (CALL.equals(name)) {
            return call(arguments);
        }
        if (COMPOSE_EMAIL.equals(name)) {
            return composeEmail(arguments);
        }
        if (COMPOSE_SMS.equals(name)) {
            return composeSms(arguments);
        }
        if (ENABLE_HARDWARE_ACCEL.equals(name)) {
            return enableHardwareAcceleration(arguments);
        }
        if (GET_AVAIL_SCHEMES.equals(name)) {
            return getAvailableSchemes(arguments);
        }
        if (GET_INFO.equals(name)) {
            return getInfo(arguments);
        }
        if (GET_LOCATION.equals(name)) {
            return getLocation(arguments);
        }
        if (GET_ORIENTATION.equals(name)) {
            return getOrientation(arguments);
        }
        if (IS_SCHEME_AVAIL.equals(name)) {
            return isSchemeAvailable(arguments);
        }
        if (OPEN_APP_STORE.equals(name)) {
            return openAppStore(arguments);
        }
        if (OPEN_URL.equals(name)) {
            return openUrl(arguments);
        }
        if (SET_MMDID.equals(name)) {
            return setMMDID(arguments);
        }
        if (SHOW_MAP.equals(name)) {
            return showMap(arguments);
        }
        return TWEET.equals(name) ? tweet(arguments) : null;
    }

    public MMJSResponse getAvailableSchemes(Map<String, String> arguments) {
        Context context = (Context) this.contextRef.get();
        if (context == null) {
            return null;
        }
        HandShake handShake = HandShake.sharedHandShake(context);
        MMJSResponse response = new MMJSResponse();
        response.result = 1;
        response.response = handShake.getSchemesJSONArray(context);
        return response;
    }

    public MMJSResponse getInfo(Map<String, String> arguments) {
        Context context = (Context) this.contextRef.get();
        if (context == null) {
            return null;
        }
        MMJSResponse response = new MMJSResponse();
        response.result = 1;
        response.response = getDeviceInfo(context);
        return response;
    }

    public MMJSResponse getLocation(Map<String, String> arguments) {
        if (MMRequest.location == null) {
            return MMJSResponse.responseWithError("location object has not been set");
        }
        JSONObject jsonObject = null;
        try {
            JSONObject jsonObject2 = new JSONObject();
            try {
                jsonObject2.put("lat", Double.toString(MMRequest.location.getLatitude()));
                jsonObject2.put("long", Double.toString(MMRequest.location.getLongitude()));
                if (MMRequest.location.hasAccuracy()) {
                    jsonObject2.put("ha", Float.toString(MMRequest.location.getAccuracy()));
                    jsonObject2.put("va", Float.toString(MMRequest.location.getAccuracy()));
                }
                if (MMRequest.location.hasSpeed()) {
                    jsonObject2.put("spd", Float.toString(MMRequest.location.getSpeed()));
                }
                if (MMRequest.location.hasBearing()) {
                    jsonObject2.put("brg", Float.toString(MMRequest.location.getBearing()));
                }
                if (MMRequest.location.hasAltitude()) {
                    jsonObject2.put("alt", Double.toString(MMRequest.location.getAltitude()));
                }
                jsonObject2.put("tslr", Long.toString(MMRequest.location.getTime()));
                jsonObject = jsonObject2;
            } catch (JSONException e) {
                e = e;
                jsonObject = jsonObject2;
                MMLog.e(TAG, "Bridge getLocation json exception: ", e);
                MMJSResponse response = new MMJSResponse();
                response.result = 1;
                response.response = jsonObject;
                return response;
            }
        } catch (JSONException e2) {
            JSONException e3 = e2;
            MMLog.e(TAG, "Bridge getLocation json exception: ", e3);
            MMJSResponse response2 = new MMJSResponse();
            response2.result = 1;
            response2.response = jsonObject;
            return response2;
        }
        MMJSResponse response22 = new MMJSResponse();
        response22.result = 1;
        response22.response = jsonObject;
        return response22;
    }

    public MMJSResponse getOrientation(Map<String, String> arguments) {
        Context context = (Context) this.contextRef.get();
        if (context == null) {
            return null;
        }
        int orientation = context.getResources().getConfiguration().orientation;
        if (orientation == 0) {
            orientation = ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getOrientation();
        }
        MMJSResponse response = new MMJSResponse();
        response.result = 1;
        switch (orientation) {
            case MMAdView.TRANSITION_UP:
                response.response = "landscape";
                return response;
            default:
                response.response = "portrait";
                return response;
        }
    }

    public MMJSResponse isSchemeAvailable(Map<String, String> arguments) {
        String scheme = (String) arguments.get("scheme");
        if (!scheme.contains(":")) {
            scheme = scheme + ":";
        }
        Context context = (Context) this.contextRef.get();
        if (!(scheme == null || context == null)) {
            if (context.getPackageManager().queryIntentActivities(new Intent("android.intent.action.VIEW", Uri.parse(scheme)), Cast.MAX_MESSAGE_LENGTH).size() > 0) {
                return MMJSResponse.responseWithSuccess(scheme);
            }
        }
        return MMJSResponse.responseWithError(scheme);
    }

    public MMJSResponse openAppStore(Map<String, String> arguments) {
        Context context = (Context) this.contextRef.get();
        String id = (String) arguments.get("appId");
        String referrer = (String) arguments.get(AdTrackerConstants.REFERRER);
        if (context == null || id == null) {
            return null;
        }
        MMLog.d(TAG, String.format("Opening marketplace: %s", new Object[]{id}));
        Intent intent = new Intent("android.intent.action.VIEW");
        if (Build.MANUFACTURER.equals("Amazon")) {
            intent.setData(Uri.parse(String.format("amzn://apps/android?p=%s", new Object[]{id})));
        } else if (referrer != null) {
            intent.setData(Uri.parse(String.format("market://details?id=%s&referrer=%s", new Object[]{id, URLEncoder.encode(referrer)})));
        } else {
            intent.setData(Uri.parse("market://details?id=" + id));
        }
        Event.intentStarted(context, Event.INTENT_MARKET, getAdImplId((String) arguments.get("PROPERTY_EXPANDING")));
        IntentUtils.startActivity(context, intent);
        return MMJSResponse.responseWithSuccess();
    }

    public MMJSResponse openUrl(Map<String, String> arguments) {
        Context context = (Context) this.contextRef.get();
        String url = (String) arguments.get(PlusShare.KEY_CALL_TO_ACTION_URL);
        if (context == null || url == null) {
            return MMJSResponse.responseWithError("URL could not be opened");
        }
        MMLog.d(TAG, String.format("Opening: %s", new Object[]{url}));
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
        if (intent.getScheme().startsWith("http") || intent.getScheme().startsWith("https")) {
            Event.intentStarted(context, Event.INTENT_EXTERNAL_BROWSER, getAdImplId((String) arguments.get("PROPERTY_EXPANDING")));
        }
        IntentUtils.startActivity(context, intent);
        return MMJSResponse.responseWithSuccess("Overlay opened");
    }

    public MMJSResponse setMMDID(Map<String, String> arguments) {
        String mmdid = (String) arguments.get("mmdid");
        Context context = (Context) this.contextRef.get();
        if (context == null) {
            return null;
        }
        HandShake.sharedHandShake(context).setMMdid(context, mmdid);
        return MMJSResponse.responseWithSuccess("MMDID is set");
    }

    public MMJSResponse showMap(Map<String, String> arguments) {
        Context context = (Context) this.contextRef.get();
        String location = (String) arguments.get("location");
        if (context == null || location == null) {
            return null;
        }
        MMLog.d(TAG, String.format("Launching Google Maps: %s", new Object[]{location}));
        IntentUtils.startActivity(context, new Intent("android.intent.action.VIEW", Uri.parse("geo:" + location)));
        Event.intentStarted(context, Event.INTENT_MAPS, getAdImplId((String) arguments.get("PROPERTY_EXPANDING")));
        return MMJSResponse.responseWithSuccess("Map successfully opened");
    }

    public MMJSResponse tweet(Map<String, String> arguments) {
        return null;
    }
}