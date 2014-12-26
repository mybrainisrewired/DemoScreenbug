package com.mopub.mobileads;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import com.mopub.common.CacheService;
import com.mopub.common.HttpClient;
import com.mopub.common.logging.MoPubLog;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

public class VastVideoDownloadTask extends AsyncTask<String, Void, Boolean> {
    private static final int MAX_VIDEO_SIZE = 26214400;
    private final VastVideoDownloadTaskListener mVastVideoDownloadTaskListener;

    public static interface VastVideoDownloadTaskListener {
        void onComplete(boolean z);
    }

    public VastVideoDownloadTask(VastVideoDownloadTaskListener listener) {
        this.mVastVideoDownloadTaskListener = listener;
    }

    protected Boolean doInBackground(String... params) {
        if (params == null || params[0] == null) {
            return Boolean.valueOf(false);
        }
        String videoUrl = params[0];
        AndroidHttpClient httpClient = null;
        try {
            httpClient = HttpClient.getHttpClient();
            HttpResponse response = httpClient.execute(new HttpGet(videoUrl));
            if (response == null || response.getEntity() == null) {
                throw new IOException(new StringBuilder("Obtained null response from video url: ").append(videoUrl).toString());
            } else if (response.getEntity().getContentLength() > 26214400) {
                throw new IOException("Video exceeded max download size");
            } else {
                InputStream inputStream = new BufferedInputStream(response.getEntity().getContent());
                boolean diskPutResult = CacheService.putToDiskCache(videoUrl, inputStream);
                inputStream.close();
                valueOf = Boolean.valueOf(diskPutResult);
                if (httpClient == null) {
                    return valueOf;
                }
                httpClient.close();
                return valueOf;
            }
        } catch (Exception e) {
            try {
                Boolean valueOf2;
                MoPubLog.d(new StringBuilder("Failed to download video: ").append(e.getMessage()).toString());
                valueOf2 = Boolean.valueOf(false);
                if (httpClient == null) {
                    return valueOf2;
                }
                httpClient.close();
                return valueOf2;
            } catch (Throwable th) {
                if (httpClient != null) {
                    httpClient.close();
                }
            }
        }
    }

    protected void onCancelled() {
        onPostExecute(Boolean.valueOf(false));
    }

    protected void onPostExecute(Boolean success) {
        if (this.mVastVideoDownloadTaskListener != null) {
            this.mVastVideoDownloadTaskListener.onComplete(success.booleanValue());
        }
    }
}