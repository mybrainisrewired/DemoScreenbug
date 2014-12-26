package com.google.android.gms.internal;

import android.content.Context;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ec extends ea {
    public ec(dz dzVar, boolean z) {
        super(dzVar, z);
    }

    protected WebResourceResponse d(Context context, String str, String str2) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str2).openConnection();
        dq.a(context, str, true, httpURLConnection);
        httpURLConnection.connect();
        WebResourceResponse webResourceResponse = new WebResourceResponse("application/javascript", "UTF-8", new ByteArrayInputStream(dq.a(new InputStreamReader(httpURLConnection.getInputStream())).getBytes("UTF-8")));
        httpURLConnection.disconnect();
        return webResourceResponse;
    }

    public WebResourceResponse shouldInterceptRequest(WebView webView, String url) {
        try {
            if (!"mraid.js".equalsIgnoreCase(new File(url).getName())) {
                return super.shouldInterceptRequest(webView, url);
            }
            if (webView instanceof dz) {
                dz dzVar = (dz) webView;
                dzVar.bI().aM();
                if (dzVar.R().lT) {
                    dw.y("shouldInterceptRequest(http://media.admob.com/mraid/v1/mraid_app_interstitial.js)");
                    return d(dzVar.getContext(), this.lC.bK().rq, "http://media.admob.com/mraid/v1/mraid_app_interstitial.js");
                } else if (dzVar.bL()) {
                    dw.y("shouldInterceptRequest(http://media.admob.com/mraid/v1/mraid_app_expanded_banner.js)");
                    return d(dzVar.getContext(), this.lC.bK().rq, "http://media.admob.com/mraid/v1/mraid_app_expanded_banner.js");
                } else {
                    dw.y("shouldInterceptRequest(http://media.admob.com/mraid/v1/mraid_app_banner.js)");
                    return d(dzVar.getContext(), this.lC.bK().rq, "http://media.admob.com/mraid/v1/mraid_app_banner.js");
                }
            } else {
                dw.z("Tried to intercept request from a WebView that wasn't an AdWebView.");
                return super.shouldInterceptRequest(webView, url);
            }
        } catch (IOException e) {
            dw.z("Could not fetching MRAID JS. " + e.getMessage());
            return super.shouldInterceptRequest(webView, url);
        }
    }
}