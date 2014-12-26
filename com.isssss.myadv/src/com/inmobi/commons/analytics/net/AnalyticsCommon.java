package com.inmobi.commons.analytics.net;

import android.util.Log;
import com.inmobi.commons.analytics.util.AnalyticsUtils;
import com.inmobi.commons.internal.InternalSDKUtil;
import com.mopub.common.Preconditions;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class AnalyticsCommon {

    public static interface HttpRequestCallback {
        public static final int HTTP_FAILURE = 1;
        public static final int HTTP_SUCCESS = 0;

        void notifyResult(int i, Object obj);
    }

    private static void a(HttpURLConnection httpURLConnection) throws ProtocolException {
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setConnectTimeout(60000);
        httpURLConnection.setReadTimeout(60000);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty(MraidCommandStorePicture.MIME_TYPE_HEADER, "application/x-www-form-urlencoded");
        httpURLConnection.setRequestProperty("User-Agent", InternalSDKUtil.getUserAgent());
    }

    public static String getURLEncoded(String str) {
        String str2 = Preconditions.EMPTY_ARGUMENTS;
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {
            return Preconditions.EMPTY_ARGUMENTS;
        }
    }

    public void closeResource(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                Log.d(AnalyticsUtils.ANALYTICS_LOGGING_TAG, "Exception closing resource: " + closeable, e);
            }
        }
    }

    public void postData(HttpURLConnection httpURLConnection, String str) throws IOException {
        httpURLConnection.setRequestProperty("Content-Length", Integer.toString(str.length()));
        try {
            Closeable bufferedWriter = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream()));
            try {
                bufferedWriter.write(str);
                closeResource(bufferedWriter);
            } catch (Throwable th) {
                th = th;
                closeResource(bufferedWriter);
                throw th;
            }
        } catch (Throwable th2) {
            Throwable th3 = th2;
            bufferedWriter = null;
            closeResource(bufferedWriter);
            throw th3;
        }
    }

    public HttpURLConnection setupConnection(String str) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
        a(httpURLConnection);
        return httpURLConnection;
    }
}