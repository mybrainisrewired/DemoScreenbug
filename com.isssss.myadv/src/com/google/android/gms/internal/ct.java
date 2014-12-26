package com.google.android.gms.internal;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View.MeasureSpec;
import android.webkit.WebView;
import com.millennialmedia.android.MMAdView;

public class ct implements Runnable {
    private final int kr;
    private final int ks;
    protected final dz lC;
    private final Handler oT;
    private final long oU;
    private long oV;
    private com.google.android.gms.internal.ea.a oW;
    protected boolean oX;
    protected boolean oY;

    protected final class a extends AsyncTask<Void, Void, Boolean> {
        private final WebView oZ;
        private Bitmap pa;

        public a(WebView webView) {
            this.oZ = webView;
        }

        protected synchronized Boolean a(Void... voidArr) {
            Boolean valueOf;
            int width = this.pa.getWidth();
            int height = this.pa.getHeight();
            if (width == 0 || height == 0) {
                valueOf = Boolean.valueOf(false);
            } else {
                int i = 0;
                int i2 = 0;
                while (i < width) {
                    int i3 = 0;
                    while (i3 < height) {
                        if (this.pa.getPixel(i, i3) != 0) {
                            i2++;
                        }
                        i3 += 10;
                    }
                    i += 10;
                }
                valueOf = Boolean.valueOf(((double) i) / (((double) (width * height)) / 100.0d) > 0.1d);
            }
            return valueOf;
        }

        protected void a(Boolean bool) {
            ct.c(ct.this);
            if (bool.booleanValue() || ct.this.bc() || ct.this.oV <= 0) {
                ct.this.oY = bool.booleanValue();
                ct.this.oW.a(ct.this.lC);
            } else if (ct.this.oV > 0) {
                if (dw.n(MMAdView.TRANSITION_UP)) {
                    dw.v("Ad not detected, scheduling another run.");
                }
                ct.this.oT.postDelayed(ct.this, ct.this.oU);
            }
        }

        protected /* synthetic */ Object doInBackground(Object[] x0) {
            return a((Void[]) x0);
        }

        protected /* synthetic */ void onPostExecute(Object x0) {
            a((Boolean) x0);
        }

        protected synchronized void onPreExecute() {
            this.pa = Bitmap.createBitmap(ct.this.kr, ct.this.ks, Config.ARGB_8888);
            this.oZ.setVisibility(0);
            this.oZ.measure(MeasureSpec.makeMeasureSpec(ct.this.kr, 0), MeasureSpec.makeMeasureSpec(ct.this.ks, 0));
            this.oZ.layout(0, 0, ct.this.kr, ct.this.ks);
            this.oZ.draw(new Canvas(this.pa));
            this.oZ.invalidate();
        }
    }

    public ct(com.google.android.gms.internal.ea.a aVar, dz dzVar, int i, int i2) {
        this(aVar, dzVar, i, i2, 200, 50);
    }

    public ct(com.google.android.gms.internal.ea.a aVar, dz dzVar, int i, int i2, long j, long j2) {
        this.oU = j;
        this.oV = j2;
        this.oT = new Handler(Looper.getMainLooper());
        this.lC = dzVar;
        this.oW = aVar;
        this.oX = false;
        this.oY = false;
        this.ks = i2;
        this.kr = i;
    }

    static /* synthetic */ long c(ct ctVar) {
        long j = ctVar.oV - 1;
        ctVar.oV = j;
        return j;
    }

    public void a(cz czVar, ee eeVar) {
        this.lC.setWebViewClient(eeVar);
        this.lC.loadDataWithBaseURL(TextUtils.isEmpty(czVar.ol) ? null : dq.r(czVar.ol), czVar.pm, "text/html", "UTF-8", null);
    }

    public void b(cz czVar) {
        a(czVar, new ee(this, this.lC, czVar.pv));
    }

    public void ba() {
        this.oT.postDelayed(this, this.oU);
    }

    public synchronized void bb() {
        this.oX = true;
    }

    public synchronized boolean bc() {
        return this.oX;
    }

    public boolean bd() {
        return this.oY;
    }

    public void run() {
        if (this.lC == null || bc()) {
            this.oW.a(this.lC);
        } else {
            new a(this.lC).execute(new Void[0]);
        }
    }
}