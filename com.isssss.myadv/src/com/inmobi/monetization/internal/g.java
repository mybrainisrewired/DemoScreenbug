package com.inmobi.monetization.internal;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.inmobi.commons.internal.Log;
import com.inmobi.monetization.internal.imai.IMAIController;
import com.inmobi.re.container.IMWebView;
import com.millennialmedia.android.MMAdView;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONObject;

// compiled from: TrackerView.java
class g extends View {
    WebViewClient a;
    private IMWebView b;
    private boolean c;
    private String d;
    private ArrayList<String> e;
    private boolean f;

    public g(Context context, String str, String str2) {
        super(context);
        this.c = false;
        this.e = null;
        this.f = false;
        this.a = new f(this);
        if (str != null && str2 != null) {
            this.d = str2;
            LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
            layoutParams.addRule(ApiEventType.API_MRAID_USE_CUSTOM_CLOSE);
            setLayoutParams(layoutParams);
            setBackgroundColor(0);
            IMWebView.setIMAIController(IMAIController.class);
            this.b = new IMWebView(context, null, false, false);
            this.b.getSettings().setJavaScriptEnabled(true);
            this.b.getSettings().setCacheMode(MMAdView.TRANSITION_UP);
            this.b.setWebViewClient(this.a);
            this.b.loadData(str, "text/html", "UTF-8");
            this.e = new ArrayList();
            setId(999);
        }
    }

    private String b() {
        return this.d + "recordEvent(18)";
    }

    private String b(HashMap<String, String> hashMap) {
        StringBuilder stringBuilder = new StringBuilder();
        if (hashMap == null || hashMap.isEmpty()) {
            stringBuilder.append(this.d + "recordEvent(8)");
            return stringBuilder.toString();
        } else {
            stringBuilder.append(this.d + "recordEvent(8, ");
            stringBuilder.append(new JSONObject(hashMap).toString());
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    public void a() {
        if (this.b != null) {
            this.b.destroy();
            this.b = null;
        }
        if (this.e != null) {
            this.e.clear();
            this.e = null;
        }
        this.c = false;
        this.f = false;
    }

    public void a(String str) {
        Log.debug(Constants.LOG_TAG, "Handle Impression");
        b(str);
    }

    public void a(HashMap hashMap) {
        Log.debug(Constants.LOG_TAG, "Handle Click");
        String b = b(hashMap);
        if (this.f) {
            b(b);
        } else if (this.e != null) {
            this.e.add(b);
        }
    }

    public void b(String str) {
        if (str != null) {
            try {
                if (str.length() < 400) {
                    Log.internal(Constants.LOG_TAG, str);
                }
                if (this.b != null) {
                    this.b.loadUrl("javascript:try{" + str + "}catch(e){}");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void onWindowVisibilityChanged(int i) {
        super.onWindowVisibilityChanged(i);
        if (i == 0 && !this.c) {
            this.c = true;
            if (this.f) {
                a(b());
            } else if (this.e != null) {
                this.e.add(b());
            }
        }
    }
}