package com.google.android.gms.internal;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.MutableContextWrapper;
import android.net.Uri;
import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View.MeasureSpec;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.google.android.gms.drive.DriveFile;
import com.millennialmedia.android.MMAdView;
import com.mopub.nativeads.MoPubNativeAdPositioning.MoPubClientPositioning;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class dz extends WebView implements DownloadListener {
    private final Object li;
    private final WindowManager ls;
    private ak nq;
    private final dx nr;
    private final l oJ;
    private final ea ru;
    private final a rv;
    private cc rw;
    private boolean rx;
    private boolean ry;

    private static class a extends MutableContextWrapper {
        private Context lp;
        private Activity rz;

        public a(Context context) {
            super(context);
            setBaseContext(context);
        }

        public void setBaseContext(Context base) {
            this.lp = base.getApplicationContext();
            if (base instanceof Activity) {
                Activity base2 = (Activity) base;
            } else {
                base = null;
            }
            this.rz = base;
            super.setBaseContext(this.lp);
        }

        public void startActivity(Intent intent) {
            if (this.rz != null) {
                this.rz.startActivity(intent);
            } else {
                intent.setFlags(DriveFile.MODE_READ_ONLY);
                this.lp.startActivity(intent);
            }
        }
    }

    private dz(Context context, ak akVar, boolean z, boolean z2, l lVar, dx dxVar) {
        super(context);
        this.li = new Object();
        this.rv = context;
        this.nq = akVar;
        this.rx = z;
        this.oJ = lVar;
        this.nr = dxVar;
        this.ls = (WindowManager) getContext().getSystemService("window");
        setBackgroundColor(0);
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSavePassword(false);
        settings.setSupportMultipleWindows(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        dq.a(context, dxVar.rq, settings);
        if (VERSION.SDK_INT >= 17) {
            dt.a(getContext(), settings);
        } else if (VERSION.SDK_INT >= 11) {
            ds.a(getContext(), settings);
        }
        setDownloadListener(this);
        if (VERSION.SDK_INT >= 11) {
            this.ru = new ec(this, z2);
        } else {
            this.ru = new ea(this, z2);
        }
        setWebViewClient(this.ru);
        if (VERSION.SDK_INT >= 14) {
            setWebChromeClient(new ed(this));
        } else if (VERSION.SDK_INT >= 11) {
            setWebChromeClient(new eb(this));
        }
        bM();
    }

    public static dz a(Context context, ak akVar, boolean z, boolean z2, l lVar, dx dxVar) {
        return new dz(new a(context), akVar, z, z2, lVar, dxVar);
    }

    private void bM() {
        synchronized (this.li) {
            if (this.rx || this.nq.lT) {
                if (VERSION.SDK_INT < 14) {
                    dw.v("Disabling hardware acceleration on an overlay.");
                    bN();
                } else {
                    dw.v("Enabling hardware acceleration on an overlay.");
                    bO();
                }
            } else if (VERSION.SDK_INT < 18) {
                dw.v("Disabling hardware acceleration on an AdView.");
                bN();
            } else {
                dw.v("Enabling hardware acceleration on an AdView.");
                bO();
            }
        }
    }

    private void bN() {
        synchronized (this.li) {
            if (!this.ry && VERSION.SDK_INT >= 11) {
                ds.d(this);
            }
            this.ry = true;
        }
    }

    private void bO() {
        synchronized (this.li) {
            if (this.ry && VERSION.SDK_INT >= 11) {
                ds.e(this);
            }
            this.ry = false;
        }
    }

    public ak R() {
        ak akVar;
        synchronized (this.li) {
            akVar = this.nq;
        }
        return akVar;
    }

    public void a(Context context, ak akVar) {
        synchronized (this.li) {
            this.rv.setBaseContext(context);
            this.rw = null;
            this.nq = akVar;
            this.rx = false;
            dq.b(this);
            loadUrl("about:blank");
            this.ru.reset();
        }
    }

    public void a(ak akVar) {
        synchronized (this.li) {
            this.nq = akVar;
            requestLayout();
        }
    }

    public void a(cc ccVar) {
        synchronized (this.li) {
            this.rw = ccVar;
        }
    }

    public void a(String str, Map<String, ?> map) {
        try {
            b(str, dq.p(map));
        } catch (JSONException e) {
            dw.z("Could not convert parameters to JSON.");
        }
    }

    public void a(String str, JSONObject jSONObject) {
        if (jSONObject == null) {
            jSONObject = new JSONObject();
        }
        String toString = jSONObject.toString();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("javascript:" + str + "(");
        stringBuilder.append(toString);
        stringBuilder.append(");");
        loadUrl(stringBuilder.toString());
    }

    public void b(String str, JSONObject jSONObject) {
        if (jSONObject == null) {
            jSONObject = new JSONObject();
        }
        String toString = jSONObject.toString();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("javascript:AFMA_ReceiveMessage('");
        stringBuilder.append(str);
        stringBuilder.append("'");
        stringBuilder.append(",");
        stringBuilder.append(toString);
        stringBuilder.append(");");
        dw.y("Dispatching AFMA event: " + stringBuilder);
        loadUrl(stringBuilder.toString());
    }

    public void bE() {
        if (bI().bP()) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            Display defaultDisplay = this.ls.getDefaultDisplay();
            defaultDisplay.getMetrics(displayMetrics);
            try {
                b("onScreenInfoChanged", new JSONObject().put(MMLayout.KEY_WIDTH, displayMetrics.widthPixels).put(MMLayout.KEY_HEIGHT, displayMetrics.heightPixels).put("density", (double) displayMetrics.density).put("rotation", defaultDisplay.getRotation()));
            } catch (JSONException e) {
                dw.b("Error occured while obtaining screen information.", e);
            }
        }
    }

    public void bF() {
        Map hashMap = new HashMap(1);
        hashMap.put("version", this.nr.rq);
        a("onhide", hashMap);
    }

    public void bG() {
        Map hashMap = new HashMap(1);
        hashMap.put("version", this.nr.rq);
        a("onshow", hashMap);
    }

    public cc bH() {
        cc ccVar;
        synchronized (this.li) {
            ccVar = this.rw;
        }
        return ccVar;
    }

    public ea bI() {
        return this.ru;
    }

    public l bJ() {
        return this.oJ;
    }

    public dx bK() {
        return this.nr;
    }

    public boolean bL() {
        boolean z;
        synchronized (this.li) {
            z = this.rx;
        }
        return z;
    }

    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long size) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setDataAndType(Uri.parse(url), mimeType);
            getContext().startActivity(intent);
        } catch (ActivityNotFoundException e) {
            dw.v("Couldn't find an Activity to view url/mimetype: " + url + " / " + mimeType);
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        boolean z = MoPubClientPositioning.NO_REPEAT;
        synchronized (this.li) {
            if (isInEditMode() || this.rx) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            } else {
                int mode = MeasureSpec.getMode(widthMeasureSpec);
                int size = MeasureSpec.getSize(widthMeasureSpec);
                int mode2 = MeasureSpec.getMode(heightMeasureSpec);
                int size2 = MeasureSpec.getSize(heightMeasureSpec);
                if (mode == Integer.MIN_VALUE || mode == 1073741824) {
                    mode = size;
                } else {
                    boolean z2 = true;
                }
                if (mode2 == Integer.MIN_VALUE || mode2 == 1073741824) {
                    int i = size2;
                }
                if (this.nq.widthPixels > mode || this.nq.heightPixels > i) {
                    dw.z("Not enough space to show ad. Needs " + this.nq.widthPixels + "x" + this.nq.heightPixels + " pixels, but only has " + size + "x" + size2 + " pixels.");
                    if (getVisibility() != 8) {
                        setVisibility(MMAdView.TRANSITION_RANDOM);
                    }
                    setMeasuredDimension(0, 0);
                } else {
                    if (getVisibility() != 8) {
                        setVisibility(0);
                    }
                    setMeasuredDimension(this.nq.widthPixels, this.nq.heightPixels);
                }
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.oJ != null) {
            this.oJ.a(event);
        }
        return super.onTouchEvent(event);
    }

    public void p(boolean z) {
        synchronized (this.li) {
            this.rx = z;
            bM();
        }
    }

    public void setContext(Context context) {
        this.rv.setBaseContext(context);
    }
}