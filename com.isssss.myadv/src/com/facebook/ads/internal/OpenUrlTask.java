package com.facebook.ads.internal;

import android.os.AsyncTask;
import android.util.Log;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class OpenUrlTask extends AsyncTask<String, Void, Void> {
    private static final String INVALID_ADDRESS = "#";
    private static final String TAG;
    private Map<String, String> extraData;

    static {
        TAG = OpenUrlTask.class.getSimpleName();
    }

    public OpenUrlTask() {
        this.extraData = null;
    }

    public OpenUrlTask(Map<String, String> extraData) {
        this.extraData = extraData;
    }

    private String addAnalogInfo(String url) {
        try {
            return addData(url, "analog", AdUtilities.jsonEncode(AdAnalogData.getAnalogInfo()));
        } catch (Exception e) {
            AdClientEventManager.addClientEvent(AdClientEvent.newErrorEvent(e));
            return url;
        }
    }

    private String addData(String url, String key, String data) {
        if (StringUtils.isNullOrEmpty(url) || StringUtils.isNullOrEmpty(key) || StringUtils.isNullOrEmpty(data)) {
            return url;
        }
        return url + (url.contains("?") ? "&" : "?") + key + "=" + URLEncoder.encode(data);
    }

    protected Void doInBackground(String... urls) {
        String url = urls[0];
        if (!(StringUtils.isNullOrEmpty(url) || url.equals(INVALID_ADDRESS))) {
            url = addAnalogInfo(url);
            if (this.extraData != null) {
                Iterator i$ = this.extraData.keySet().iterator();
                while (i$.hasNext()) {
                    String key = (String) i$.next();
                    url = addData(url, key, (String) this.extraData.get(key));
                }
            }
            try {
                new DefaultHttpClient().execute(new HttpGet(url));
            } catch (Exception e) {
                Exception ex = e;
                Log.e(TAG, "Error opening url: " + url, ex);
                AdClientEventManager.addClientEvent(AdClientEvent.newErrorEvent(ex));
            }
        }
        return null;
    }
}