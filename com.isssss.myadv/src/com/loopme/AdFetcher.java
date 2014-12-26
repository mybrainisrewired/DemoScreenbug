package com.loopme;

import android.os.AsyncTask;
import android.webkit.WebView;
import com.google.android.gms.wallet.WalletConstants;
import com.millennialmedia.android.MMAdView;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

class AdFetcher extends AsyncTask<String, Void, AdParams> {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$loopme$AdFormat = null;
    private static final String JSON_EXPIRED_TIME = "ad_expiry_time";
    private static final String JSON_FORMAT = "format";
    private static final String JSON_ORIENTATION = "orientation";
    private static final String JSON_REFRESH_TIME = "ad_refresh_time";
    private static final String JSON_SCRIPT = "script";
    private static final String JSON_SETTINGS = "settings";
    private static final String LOG_TAG;
    private LoopMeError mLoopMeError;
    private final WebView mWebView;

    static /* synthetic */ int[] $SWITCH_TABLE$com$loopme$AdFormat() {
        int[] iArr = $SWITCH_TABLE$com$loopme$AdFormat;
        if (iArr == null) {
            iArr = new int[AdFormat.values().length];
            try {
                iArr[AdFormat.BANNER.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[AdFormat.INTERSTITIAL.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            $SWITCH_TABLE$com$loopme$AdFormat = iArr;
        }
        return iArr;
    }

    static {
        LOG_TAG = AdFetcher.class.getSimpleName();
    }

    public AdFetcher(WebView webview) {
        this.mWebView = webview;
        if (this.mWebView == null) {
            throw new IllegalArgumentException("Wrong parameter");
        }
    }

    private String getRequestedAdFormat() {
        return BaseLoopMeHolder.get().getAdFormat().toString();
    }

    private String getResponse(String url) {
        String result = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            Utilities.log(LOG_TAG, new StringBuilder(String.valueOf(BaseLoopMeHolder.get().getAdFormat().toString())).append(" loads ad with URL: ").append(url).toString(), LogLevel.DEBUG);
            HttpResponse responce = httpClient.execute(httpGet);
            HttpEntity entity = responce.getEntity();
            switch (responce.getStatusLine().getStatusCode()) {
                case 200:
                    InputStream is = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder sb = new StringBuilder();
                    while (true) {
                        String line = reader.readLine();
                        if (line == null) {
                            result = sb.toString();
                            is.close();
                            return result;
                        } else {
                            sb.append(new StringBuilder(String.valueOf(line)).append("\n").toString());
                        }
                    }
                    break;
                case 204:
                    this.mLoopMeError = new LoopMeError("No ads found");
                    return result;
                case WalletConstants.ERROR_CODE_INVALID_PARAMETERS:
                    this.mLoopMeError = new LoopMeError("Invalid app key");
                    return result;
                default:
                    this.mLoopMeError = new LoopMeError("Unknown status code from server");
                    return result;
            }
        } catch (Exception e) {
            this.mLoopMeError = new LoopMeError(new StringBuilder("Exception while get response with message ").append(e.getMessage()).toString());
            return result;
        }
    }

    private void loadFail(LoopMeError error) {
        BaseLoopMe base = BaseLoopMeHolder.get();
        switch ($SWITCH_TABLE$com$loopme$AdFormat()[base.getAdFormat().ordinal()]) {
            case MMAdView.TRANSITION_FADE:
                LoopMeBanner banner = (LoopMeBanner) base;
                banner.onLoopMeBannerLoadFail(banner, error);
            case MMAdView.TRANSITION_UP:
                LoopMeInterstitial interstitial = (LoopMeInterstitial) base;
                interstitial.onLoopMeInterstitialLoadFail(interstitial, error);
            default:
                throw new IllegalArgumentException("loadFail - unknown ad format");
        }
    }

    private int parseInt(JSONObject object, String jsonParam) {
        int value = 0;
        try {
            return object.getInt(jsonParam);
        } catch (JSONException e) {
            Utilities.log(LOG_TAG, new StringBuilder(String.valueOf(jsonParam)).append(" absent").toString(), LogLevel.DEBUG);
            return value;
        }
    }

    private AdParams parseJson(String result) {
        if (result == null) {
            loadFail(this.mLoopMeError);
            return null;
        } else {
            try {
                JSONObject object = (JSONObject) new JSONTokener(result).nextValue();
                JSONObject settings = object.getJSONObject(JSON_SETTINGS);
                String format = settings.getString(JSON_FORMAT);
                if (format.equalsIgnoreCase(getRequestedAdFormat())) {
                    return new AdParamsBuilder(format).html(parseString(object, JSON_SCRIPT)).orientation(parseString(settings, JSON_ORIENTATION)).refreshTime(parseInt(settings, JSON_REFRESH_TIME)).expiredTime(parseInt(settings, JSON_EXPIRED_TIME)).build();
                }
                loadFail(new LoopMeError("Wrong Ad format"));
                return null;
            } catch (JSONException e) {
                Utilities.log(LOG_TAG, e.getMessage(), LogLevel.ERROR);
                loadFail(new LoopMeError("Exception during json parse"));
                return null;
            } catch (ClassCastException e2) {
                Utilities.log(LOG_TAG, e2.getMessage(), LogLevel.ERROR);
                loadFail(new LoopMeError("Exception during json parse"));
                return null;
            }
        }
    }

    private String parseString(JSONObject object, String jsonParam) {
        String value = null;
        try {
            return object.getString(jsonParam);
        } catch (JSONException e) {
            Utilities.log(LOG_TAG, new StringBuilder(String.valueOf(jsonParam)).append(" absent").toString(), LogLevel.DEBUG);
            return value;
        }
    }

    protected AdParams doInBackground(String... params) {
        return parseJson(getResponse(params[0]));
    }

    protected void onPostExecute(AdParams result) {
        super.onPostExecute(result);
        if (result != null) {
            BaseLoopMeHolder.get().setAdParams(result);
            String html = result.getHtml();
            if (html == null || html.isEmpty()) {
                loadFail(new LoopMeError("Broken response"));
            } else {
                this.mWebView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
            }
        }
    }
}