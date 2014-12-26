package com.mopub.nativeads;

import android.os.AsyncTask;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.AsyncTasks;
import com.mopub.common.util.IntentUtils;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

class UrlResolutionTask extends AsyncTask<String, Void, String> {
    private static final int REDIRECT_LIMIT = 10;
    private final UrlResolutionListener mListener;

    static interface UrlResolutionListener {
        void onFailure();

        void onSuccess(String str);
    }

    UrlResolutionTask(UrlResolutionListener listener) {
        this.mListener = listener;
    }

    private String getRedirectLocation(String urlString) throws IOException {
        HttpURLConnection httpUrlConnection = null;
        try {
            httpUrlConnection = (HttpURLConnection) new URL(urlString).openConnection();
            httpUrlConnection.setInstanceFollowRedirects(false);
            int responseCode = httpUrlConnection.getResponseCode();
            if (responseCode < 300 || responseCode >= 400) {
                if (httpUrlConnection != null) {
                    httpUrlConnection.disconnect();
                }
                return null;
            } else {
                String headerField = httpUrlConnection.getHeaderField("Location");
                if (httpUrlConnection == null) {
                    return headerField;
                }
                httpUrlConnection.disconnect();
                return headerField;
            }
        } catch (Throwable th) {
            if (httpUrlConnection != null) {
                httpUrlConnection.disconnect();
            }
        }
    }

    public static void getResolvedUrl(String urlString, UrlResolutionListener listener) {
        try {
            AsyncTasks.safeExecuteOnExecutor(new UrlResolutionTask(listener), new String[]{urlString});
        } catch (Exception e) {
            MoPubLog.d("Failed to resolve url", e);
            listener.onFailure();
        }
    }

    protected String doInBackground(String... urls) {
        if (urls == null || urls.length == 0) {
            return null;
        }
        String previousUrl = null;
        try {
            String locationUrl = urls[0];
            int redirectCount = 0;
            while (locationUrl != null && redirectCount < 10) {
                if (!IntentUtils.isHttpUrl(locationUrl)) {
                    return locationUrl;
                }
                previousUrl = locationUrl;
                locationUrl = getRedirectLocation(locationUrl);
                redirectCount++;
            }
            return previousUrl;
        } catch (IOException e) {
            return null;
        }
    }

    protected void onCancelled() {
        super.onCancelled();
        this.mListener.onFailure();
    }

    protected void onPostExecute(String resolvedUrl) {
        super.onPostExecute(resolvedUrl);
        if (isCancelled() || resolvedUrl == null) {
            onCancelled();
        } else {
            this.mListener.onSuccess(resolvedUrl);
        }
    }
}