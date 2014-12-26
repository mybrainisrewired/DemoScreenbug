package com.millennialmedia.android;

import android.text.TextUtils;
import com.mopub.mobileads.CustomEventBannerAdapter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

class HttpGetRequest {
    private static final String TAG = "HttpGetRequest";
    private HttpClient client;
    private HttpGet getRequest;

    static class AnonymousClass_1 implements Runnable {
        final /* synthetic */ String[] val$urls;

        AnonymousClass_1(String[] strArr) {
            this.val$urls = strArr;
        }

        public void run() {
            String[] arr$ = this.val$urls;
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                String url = arr$[i$];
                MMLog.v(TAG, String.format("Logging event to: %s", new Object[]{url}));
                try {
                    new HttpGetRequest().get(url);
                } catch (Exception e) {
                    MMLog.e(TAG, "Logging request failed.", e);
                }
                i$++;
            }
        }
    }

    HttpGetRequest() {
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, CustomEventBannerAdapter.DEFAULT_BANNER_TIMEOUT_DELAY);
        this.client = new DefaultHttpClient(params);
        this.getRequest = new HttpGet();
    }

    HttpGetRequest(int timeout) {
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, CustomEventBannerAdapter.DEFAULT_BANNER_TIMEOUT_DELAY);
        HttpConnectionParams.setSoTimeout(params, timeout);
        this.client = new DefaultHttpClient(params);
        this.getRequest = new HttpGet();
    }

    static String convertStreamToString(InputStream is) throws IOException {
        OutOfMemoryError e;
        Throwable th;
        BufferedReader reader = null;
        if (is == null) {
            throw new IOException("Stream is null.");
        }
        try {
            BufferedReader reader2 = new BufferedReader(new InputStreamReader(is), 4096);
            try {
                StringBuilder sb = new StringBuilder();
                while (true) {
                    String line = reader2.readLine();
                    if (line == null) {
                        break;
                    }
                    sb.append(line + "\n");
                }
                if (reader2 != null) {
                    try {
                        reader2.close();
                    } catch (IOException e2) {
                        MMLog.e(TAG, "Error closing file", e2);
                    }
                }
                return sb.toString();
            } catch (OutOfMemoryError e3) {
                e = e3;
                reader = reader2;
                try {
                    MMLog.e(TAG, "Out of Memeory: ", e);
                    throw new IOException("Out of memory.");
                } catch (Throwable th2) {
                    th = th2;
                    if (reader != null) {
                        reader.close();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                reader = reader2;
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e4) {
                        MMLog.e(TAG, "Error closing file", e4);
                    }
                }
                throw th;
            }
        } catch (OutOfMemoryError e5) {
            e = e5;
            MMLog.e(TAG, "Out of Memeory: ", e);
            throw new IOException("Out of memory.");
        }
    }

    static void log(String[] urls) {
        if (urls != null && urls.length > 0) {
            ThreadUtils.execute(new AnonymousClass_1(urls));
        }
    }

    HttpResponse get(String url) throws Exception {
        HttpResponse response = null;
        if (!TextUtils.isEmpty(url)) {
            try {
                this.getRequest.setURI(new URI(url));
                response = this.client.execute(this.getRequest);
            } catch (OutOfMemoryError e) {
                MMLog.e(TAG, "Out of memory!", e);
                return null;
            } catch (Exception e2) {
                Exception ex = e2;
                if (ex == null) {
                    return null;
                }
                MMLog.e(TAG, "Error connecting:", ex);
                return null;
            }
        }
        return response;
    }

    void trackConversion(String goalId, boolean isFirstLaunch, long installTime, TreeMap<String, String> extraParams) throws Exception {
        try {
            StringBuilder urlBuilder = new StringBuilder("http://cvt.mydas.mobi/handleConversion?firstlaunch=" + (isFirstLaunch ? 1 : 0));
            if (goalId != null) {
                urlBuilder.append("&goalId=" + goalId);
            }
            if (installTime > 0) {
                urlBuilder.append("&installtime=" + (installTime / 1000));
            }
            if (extraParams != null) {
                Iterator i$ = extraParams.entrySet().iterator();
                while (i$.hasNext()) {
                    Entry<String, String> entry = (Entry) i$.next();
                    urlBuilder.append(String.format("&%s=%s", new Object[]{entry.getKey(), URLEncoder.encode((String) entry.getValue(), "UTF-8")}));
                }
            }
            MMLog.d(TAG, String.format("Sending conversion tracker report: %s", new Object[]{urlBuilder.toString()}));
            this.getRequest.setURI(new URI(url));
            if (this.client.execute(this.getRequest).getStatusLine().getStatusCode() == 200) {
                MMLog.v(TAG, String.format("Successful conversion tracking event: %d", new Object[]{Integer.valueOf(response.getStatusLine().getStatusCode())}));
            } else {
                MMLog.e(TAG, String.format("Conversion tracking error: %d", new Object[]{Integer.valueOf(response.getStatusLine().getStatusCode())}));
            }
        } catch (IOException e) {
            MMLog.e(TAG, "Conversion tracking error: ", e);
        }
    }
}