package com.inmobi.monetization.internal;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.inmobi.commons.internal.Log;

// compiled from: TrackerView.java
class f extends WebViewClient {
    final /* synthetic */ g a;

    f(g gVar) {
        this.a = gVar;
    }

    public void onPageFinished(WebView webView, String str) {
        Log.internal(Constants.LOG_TAG, "Native ad context code loaded");
        g.a(this.a, true);
        if (g.a(this.a) != null && !g.a(this.a).isEmpty()) {
            int i = 0;
            while (i < g.a(this.a).size()) {
                this.a.b((String) g.a(this.a).get(i));
                i++;
            }
            g.a(this.a).clear();
            g.a(this.a, null);
        }
    }
}