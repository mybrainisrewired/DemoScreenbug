package com.facebook.ads.internal;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.millennialmedia.android.MMAdView;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONObject;
import org.json.JSONTokener;

public class AdRequest {
    private static final String ADS_ENDPOINT = "network_ads";
    private static final int AD_REQUEST_TIMEOUT_MS = 30000;
    private static final String AD_TYPE_PARAM = "ad_type";
    private static final String APP_BUILD_PARAM = "app_build";
    private static final String APP_VERSION_PARAM = "app_version";
    private static final String ATTRIBUTION_ID_PARAM = "attribution_id";
    private static final String CHILD_DIRECTED_PARAM = "child_directed";
    private static final String CLIENT_EVENTS_PARAM = "events";
    private static final String DEFAULT_ENCODING = "utf-8";
    private static final String DEVICE_ID_PARAM = "device_id";
    private static final String DEVICE_ID_TRACKING_ENABLED_PARAM = "tracking_enabled";
    private static final String GRAPH_URL_BASE = "https://graph.facebook.com";
    private static final String GRAPH_URL_BASE_PREFIX_FORMAT = "http://graph.%s.facebook.com";
    private static final String HEIGHT_PARAM = "height";
    private static final String LOCALE_PARAM = "locale";
    private static final String NATIVE_ADS_ENDPOINT = "network_ads_native";
    private static final String OS = "Android";
    private static final String OS_PARAM = "os";
    private static final String OS_VERSION_PARAM = "os_version";
    private static final String PACKAGE_NAME_PARAM = "package_name";
    private static final String PLACEMENT_ID_PARAM = "placement_id";
    private static final String SCREEN_HEIGHT_PARAM = "screen_height";
    private static final String SCREEN_WIDTH_PARAM = "screen_width";
    private static final String SDK_CAPABILITIES_PARAM = "sdk_capabilities";
    private static final String SDK_VERSION = "sdk_version";
    private static final String TAG;
    private static final String TEST_MODE_PARAM = "test_mode";
    private static final String WIDTH_PARAM = "width";
    private final AdSize adSize;
    private final AdType adType;
    private final Callback callback;
    private final Context context;
    private final String placementId;
    private final boolean testMode;
    private final String userAgentString;

    static /* synthetic */ class AnonymousClass_2 {
        static final /* synthetic */ int[] $SwitchMap$com$facebook$ads$internal$AdType;

        static {
            $SwitchMap$com$facebook$ads$internal$AdType = new int[AdType.values().length];
            try {
                $SwitchMap$com$facebook$ads$internal$AdType[AdType.NATIVE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            $SwitchMap$com$facebook$ads$internal$AdType[AdType.HTML.ordinal()] = 2;
        }
    }

    private static class AdRequestResponse {
        JSONObject body;
        AdError error;

        private AdRequestResponse() {
            this.body = null;
            this.error = null;
        }
    }

    public static interface Callback {
        void onCompleted(AdResponse adResponse);

        void onError(AdError adError);
    }

    static {
        TAG = AdRequest.class.getSimpleName();
    }

    public AdRequest(Context context, String placementId, AdSize adSize, AdType adType, boolean testMode, Callback callback) {
        if (placementId == null || placementId.length() < 1) {
            throw new IllegalArgumentException("placementId");
        } else if (adSize == null) {
            throw new IllegalArgumentException("adSize");
        } else if (callback == null) {
            throw new IllegalArgumentException("callback");
        } else {
            this.context = context;
            this.placementId = placementId;
            this.adSize = adSize;
            this.userAgentString = AdWebViewUtils.getUserAgentString(context, adType);
            this.adType = adType;
            this.testMode = testMode;
            this.callback = callback;
        }
    }

    private void addAdvertisingInfoParams(Map<String, Object> params, Fb4aData fb4aData) {
        boolean z = true;
        AdvertisingIdInfo advertisingIdInfo = AdvertisingIdInfo.getAdvertisingIdInfo(this.context, fb4aData);
        if (advertisingIdInfo == null) {
            params.put(DEVICE_ID_TRACKING_ENABLED_PARAM, Boolean.valueOf(true));
        } else {
            String str = DEVICE_ID_TRACKING_ENABLED_PARAM;
            if (advertisingIdInfo.isLimitAdTrackingEnabled()) {
                z = false;
            }
            params.put(str, Boolean.valueOf(z));
            if (!advertisingIdInfo.isLimitAdTrackingEnabled()) {
                params.put(DEVICE_ID_PARAM, advertisingIdInfo.getId());
            }
        }
    }

    private void addAppInfoParams(Map<String, Object> params) {
        params.put(PACKAGE_NAME_PARAM, this.context.getPackageName());
    }

    private void addDeviceInfoParams(Map<String, Object> params) {
        int i = 0;
        params.put(OS_PARAM, OS);
        params.put(OS_VERSION_PARAM, VERSION.RELEASE);
        DisplayMetrics metrics = this.context.getResources().getDisplayMetrics();
        int screenHeight = (int) (((float) metrics.heightPixels) / metrics.density);
        params.put(SCREEN_WIDTH_PARAM, Integer.valueOf((int) (((float) metrics.widthPixels) / metrics.density)));
        params.put(SCREEN_HEIGHT_PARAM, Integer.valueOf(screenHeight));
        try {
            PackageInfo pInfo = this.context.getPackageManager().getPackageInfo(this.context.getPackageName(), 0);
            params.put(APP_BUILD_PARAM, Integer.valueOf(pInfo.versionCode));
            params.put(APP_VERSION_PARAM, pInfo.versionName);
        } catch (NameNotFoundException e) {
            params.put(APP_VERSION_PARAM, Integer.valueOf(i));
        }
        Locale locale = this.context.getResources().getConfiguration().locale;
        if (locale == null) {
            locale = Locale.getDefault();
        }
        params.put(LOCALE_PARAM, locale.toString());
    }

    private AdRequestResponse createResponsesFromStream(InputStream stream) {
        int i = -1;
        AdRequestResponse response = new AdRequestResponse();
        try {
            Object resultObject = new JSONTokener(AdUtilities.readStreamToString(stream)).nextValue();
            if (resultObject instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) resultObject;
                if (jsonObject.has("error")) {
                    JSONObject error = (JSONObject) AdUtilities.getStringPropertyAsJSON(jsonObject, "error");
                    response.error = new AdError(error.optInt("code", -1), error.optString("message", null));
                } else {
                    response.body = jsonObject;
                    response.error = null;
                }
            }
        } catch (Exception e) {
            response.error = new AdError(i, e.getMessage());
        }
        return (response.body == null && response.error == null) ? null : response;
    }

    private String getAdsEndpoint() {
        switch (AnonymousClass_2.$SwitchMap$com$facebook$ads$internal$AdType[this.adType.ordinal()]) {
            case MMAdView.TRANSITION_FADE:
                return NATIVE_ADS_ENDPOINT;
            default:
                return ADS_ENDPOINT;
        }
    }

    private String getQueryString(Map<String, Object> params) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder(512);
        boolean first = true;
        Iterator i$ = params.entrySet().iterator();
        while (i$.hasNext()) {
            Entry<String, Object> entry = (Entry) i$.next();
            if (first) {
                first = false;
            } else {
                sb.append("&");
            }
            sb.append(URLEncoder.encode((String) entry.getKey(), DEFAULT_ENCODING)).append("=").append(URLEncoder.encode(String.valueOf(entry.getValue()), DEFAULT_ENCODING));
        }
        return sb.toString();
    }

    private Map<String, Object> getRequestParameters() {
        Map<String, Object> params = new HashMap();
        Fb4aData fb4aData = AdUtilities.getFb4aData(this.context.getContentResolver());
        params.put(AD_TYPE_PARAM, Integer.valueOf(this.adType.getValue()));
        params.put(SDK_CAPABILITIES_PARAM, AdSdkCapability.getSupportedCapabilitiesAsJSONString());
        params.put(SDK_VERSION, AdSdkVersion.BUILD);
        params.put(PLACEMENT_ID_PARAM, this.placementId);
        params.put(ATTRIBUTION_ID_PARAM, fb4aData.attributionId);
        params.put(WIDTH_PARAM, Integer.valueOf(this.adSize.getWidth()));
        params.put(HEIGHT_PARAM, Integer.valueOf(this.adSize.getHeight()));
        params.put(TEST_MODE_PARAM, Boolean.valueOf(this.testMode));
        params.put(CHILD_DIRECTED_PARAM, Boolean.valueOf(AdSettings.isChildDirected()));
        params.put(CLIENT_EVENTS_PARAM, AdClientEventManager.dumpClientEventToJson());
        addDeviceInfoParams(params);
        addAppInfoParams(params);
        addAdvertisingInfoParams(params, fb4aData);
        return params;
    }

    private URL getUrlForRequest() throws MalformedURLException {
        String urlBase;
        if (StringUtils.isNullOrEmpty(AdSettings.getUrlPrefix())) {
            urlBase = GRAPH_URL_BASE;
        } else {
            urlBase = String.format(GRAPH_URL_BASE_PREFIX_FORMAT, new Object[]{urlPrefix});
        }
        return new URL(String.format("%s/%s", new Object[]{urlBase, getAdsEndpoint()}));
    }

    private HttpURLConnection makeRequest() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) getUrlForRequest().openConnection();
        connection.setRequestProperty("User-Agent", this.userAgentString);
        connection.setRequestProperty(MraidCommandStorePicture.MIME_TYPE_HEADER, "application/x-www-form-urlencoded");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setConnectTimeout(AD_REQUEST_TIMEOUT_MS);
        connection.setReadTimeout(AD_REQUEST_TIMEOUT_MS);
        String queryString = getQueryString(getRequestParameters());
        OutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, DEFAULT_ENCODING));
        writer.write(queryString);
        writer.flush();
        writer.close();
        outputStream.close();
        connection.connect();
        return connection;
    }

    public AsyncTask executeAsync() {
        AdAnalogData.startUpdate(this.context);
        return new AsyncTask<Void, Void, AdRequestResponse>() {
            protected AdRequestResponse doInBackground(Void... params) {
                return AdRequest.this.executeConnectionAndWait();
            }

            protected void onPostExecute(AdRequestResponse response) {
                if (response == null) {
                    AdRequest.this.callback.onError(AdError.INTERNAL_ERROR);
                } else if (response.error != null) {
                    AdRequest.this.callback.onError(response.error);
                } else {
                    AdRequest.this.callback.onCompleted(AdResponse.parseResponse(AdRequest.this.context, response.body));
                }
            }
        }.execute(new Void[0]);
    }

    public AdRequestResponse executeConnectionAndWait() {
        InputStream stream = null;
        HttpURLConnection connection = null;
        try {
            connection = makeRequest();
            if (connection.getResponseCode() >= 400) {
                stream = connection.getErrorStream();
            } else {
                stream = connection.getInputStream();
            }
            AdRequestResponse createResponsesFromStream = createResponsesFromStream(stream);
            AdUtilities.closeQuietly(stream);
            if (connection != null) {
                connection.disconnect();
            }
            return createResponsesFromStream;
        } catch (Throwable th) {
            AdUtilities.closeQuietly(stream);
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}