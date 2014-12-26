package com.google.android.gms.tagmanager;

import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

class av implements bl {
    private HttpClient Yg;

    av() {
    }

    private InputStream a(HttpClient httpClient, HttpResponse httpResponse) throws IOException {
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            bh.y("Success response");
            return httpResponse.getEntity().getContent();
        } else {
            String str = "Bad response: " + statusCode;
            if (statusCode == 404) {
                throw new FileNotFoundException(str);
            }
            throw new IOException(str);
        }
    }

    private void a(HttpClient httpClient) {
        if (httpClient != null && httpClient.getConnectionManager() != null) {
            httpClient.getConnectionManager().shutdown();
        }
    }

    public InputStream bD(String str) throws IOException {
        this.Yg = kF();
        return a(this.Yg, this.Yg.execute(new HttpGet(str)));
    }

    public void close() {
        a(this.Yg);
    }

    HttpClient kF() {
        HttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT);
        HttpConnectionParams.setSoTimeout(basicHttpParams, BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT);
        return new DefaultHttpClient(basicHttpParams);
    }
}