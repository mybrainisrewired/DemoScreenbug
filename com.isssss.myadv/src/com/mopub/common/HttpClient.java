package com.mopub.common;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.os.Handler;
import android.os.Looper;
import android.webkit.WebView;
import com.mopub.common.DownloadTask.DownloadTaskListener;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.AsyncTasks;
import com.mopub.common.util.DeviceUtils;
import com.mopub.common.util.ResponseHeader;
import java.util.Arrays;
import java.util.Iterator;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class HttpClient {
    private static final int CONNECTION_TIMEOUT = 10000;
    private static final int SOCKET_TIMEOUT = 10000;
    private static String sWebViewUserAgent;

    class AnonymousClass_2 implements Runnable {
        private final /* synthetic */ Context val$appContext;
        private final /* synthetic */ DownloadTaskListener val$downloadTaskListener;
        private final /* synthetic */ Iterable val$urls;

        AnonymousClass_2(Iterable iterable, Context context, DownloadTaskListener downloadTaskListener) {
            this.val$urls = iterable;
            this.val$appContext = context;
            this.val$downloadTaskListener = downloadTaskListener;
        }

        public void run() {
            Iterator it = this.val$urls.iterator();
            while (it.hasNext()) {
                String url = (String) it.next();
                try {
                    HttpGet httpGet = HttpClient.initializeHttpGet(url, this.val$appContext);
                    AsyncTasks.safeExecuteOnExecutor(new DownloadTask(this.val$downloadTaskListener), new HttpUriRequest[]{httpGet});
                } catch (Exception e) {
                    MoPubLog.d(new StringBuilder("Failed to hit tracking endpoint: ").append(url).toString());
                }
            }
        }
    }

    public static AndroidHttpClient getHttpClient() {
        AndroidHttpClient httpClient = AndroidHttpClient.newInstance(DeviceUtils.getUserAgent());
        HttpParams params = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, SOCKET_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, SOCKET_TIMEOUT);
        HttpClientParams.setRedirecting(params, true);
        return httpClient;
    }

    public static synchronized String getWebViewUserAgent() {
        String str;
        synchronized (HttpClient.class) {
            str = sWebViewUserAgent;
        }
        return str;
    }

    public static HttpGet initializeHttpGet(String url, Context context) {
        HttpGet httpGet = new HttpGet(url);
        if (getWebViewUserAgent() == null && context != null) {
            setWebViewUserAgent(new WebView(context).getSettings().getUserAgentString());
        }
        String webViewUserAgent = getWebViewUserAgent();
        if (webViewUserAgent != null) {
            httpGet.addHeader(ResponseHeader.USER_AGENT.getKey(), webViewUserAgent);
        }
        return httpGet;
    }

    public static void makeTrackingHttpRequest(Iterable<String> urls, Context context) {
        if (urls != null && context != null) {
            new Handler(Looper.getMainLooper()).post(new AnonymousClass_2(urls, context.getApplicationContext(), new DownloadTaskListener() {
                public void onComplete(String url, DownloadResponse downloadResponse) {
                    if (downloadResponse == null || downloadResponse.getStatusCode() != 200) {
                        MoPubLog.d(new StringBuilder("Failed to hit tracking endpoint: ").append(url).toString());
                    } else if (HttpResponses.asResponseString(downloadResponse) != null) {
                        MoPubLog.d(new StringBuilder("Successfully hit tracking endpoint: ").append(url).toString());
                    } else {
                        MoPubLog.d(new StringBuilder("Failed to hit tracking endpoint: ").append(url).toString());
                    }
                }
            }));
        }
    }

    public static void makeTrackingHttpRequest(String url, Context context) {
        makeTrackingHttpRequest(Arrays.asList(new String[]{url}), context);
    }

    public static synchronized void setWebViewUserAgent(String userAgent) {
        synchronized (HttpClient.class) {
            sWebViewUserAgent = userAgent;
        }
    }
}