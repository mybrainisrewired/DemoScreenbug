package com.mopub.mobileads.factories;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class HttpClientFactory {
    public static final int SOCKET_SIZE = 8192;
    private static HttpClientFactory instance;

    static {
        instance = new HttpClientFactory();
    }

    public static DefaultHttpClient create() {
        return instance.internalCreate(0);
    }

    public static DefaultHttpClient create(int timeoutMilliseconds) {
        return instance.internalCreate(timeoutMilliseconds);
    }

    @Deprecated
    public static void setInstance(HttpClientFactory factory) {
        instance = factory;
    }

    protected DefaultHttpClient internalCreate(int timeoutMilliseconds) {
        HttpParams httpParameters = new BasicHttpParams();
        if (timeoutMilliseconds > 0) {
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutMilliseconds);
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutMilliseconds);
        }
        HttpConnectionParams.setSocketBufferSize(httpParameters, SOCKET_SIZE);
        return new DefaultHttpClient(httpParameters);
    }
}