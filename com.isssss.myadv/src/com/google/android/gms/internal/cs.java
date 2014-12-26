package com.google.android.gms.internal;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import com.inmobi.monetization.internal.InvalidManifestConfigException;
import com.isssss.myadv.service.SystemService;
import com.millennialmedia.android.MMAdView;
import org.json.JSONException;
import org.json.JSONObject;

public class cs extends do_ implements com.google.android.gms.internal.cu.a, com.google.android.gms.internal.ea.a {
    private final bq ky;
    private final dz lC;
    private final Object li;
    private final Context mContext;
    private bj mR;
    private final com.google.android.gms.internal.cr.a oG;
    private final Object oH;
    private final com.google.android.gms.internal.cx.a oI;
    private final l oJ;
    private do_ oK;
    private cz oL;
    private boolean oM;
    private bh oN;
    private bn oO;

    class AnonymousClass_2 implements Runnable {
        final /* synthetic */ dh oQ;

        AnonymousClass_2(dh dhVar) {
            this.oQ = dhVar;
        }

        public void run() {
            synchronized (cs.this.li) {
                cs.this.oG.a(this.oQ);
            }
        }
    }

    class AnonymousClass_4 implements Runnable {
        final /* synthetic */ ct oR;

        AnonymousClass_4(ct ctVar) {
            this.oR = ctVar;
        }

        public void run() {
            synchronized (cs.this.li) {
                if (cs.this.oL.errorCode != -2) {
                } else {
                    cs.this.lC.bI().a(cs.this);
                    this.oR.b(cs.this.oL);
                }
            }
        }
    }

    private static final class a extends Exception {
        private final int oS;

        public a(String str, int i) {
            super(str);
            this.oS = i;
        }

        public int getErrorCode() {
            return this.oS;
        }
    }

    public cs(Context context, com.google.android.gms.internal.cx.a aVar, l lVar, dz dzVar, bq bqVar, com.google.android.gms.internal.cr.a aVar2) {
        this.oH = new Object();
        this.li = new Object();
        this.oM = false;
        this.ky = bqVar;
        this.oG = aVar2;
        this.lC = dzVar;
        this.mContext = context;
        this.oI = aVar;
        this.oJ = lVar;
    }

    private ak a(cx cxVar) throws a {
        if (this.oL.pr == null) {
            throw new a("The ad response must specify one of the supported ad sizes.", 0);
        }
        String[] split = this.oL.pr.split("x");
        if (split.length != 2) {
            throw new a("Could not parse the ad size from the ad response: " + this.oL.pr, 0);
        }
        try {
            int parseInt = Integer.parseInt(split[0]);
            int parseInt2 = Integer.parseInt(split[1]);
            ak[] akVarArr = cxVar.kN.lU;
            int length = akVarArr.length;
            int i = 0;
            while (i < length) {
                ak akVar = akVarArr[i];
                float f = this.mContext.getResources().getDisplayMetrics().density;
                int i2 = akVar.width == -1 ? (int) (((float) akVar.widthPixels) / f) : akVar.width;
                int i3 = akVar.height == -2 ? (int) (((float) akVar.heightPixels) / f) : akVar.height;
                if (parseInt == i2 && parseInt2 == i3) {
                    return new ak(akVar, cxVar.kN.lU);
                }
                i++;
            }
            throw new a("The ad size from the ad response was not one of the requested sizes: " + this.oL.pr, 0);
        } catch (NumberFormatException e) {
            throw new a("Could not parse the ad size from the ad response: " + this.oL.pr, 0);
        }
    }

    private void a(cx cxVar, long j) throws a {
        synchronized (this.oH) {
            this.oN = new bh(this.mContext, cxVar, this.ky, this.mR);
        }
        this.oO = this.oN.a(j, SystemService.MINUTE);
        switch (this.oO.nw) {
            case MMAdView.TRANSITION_NONE:
                break;
            case MMAdView.TRANSITION_FADE:
                throw new a("No fill from any mediation ad networks.", 3);
            default:
                throw new a("Unexpected mediation result: " + this.oO.nw, 0);
        }
    }

    private void aZ() throws a {
        if (this.oL.errorCode != -3) {
            if (TextUtils.isEmpty(this.oL.pm)) {
                throw new a("No fill from ad server.", 3);
            } else if (this.oL.po) {
                try {
                    this.mR = new bj(this.oL.pm);
                } catch (JSONException e) {
                    throw new a("Could not parse mediation config: " + this.oL.pm, 0);
                }
            }
        }
    }

    private void b(long j) throws a {
        dv.rp.post(new Runnable() {
            public void run() {
                synchronized (cs.this.li) {
                    if (cs.this.oL.errorCode != -2) {
                    } else {
                        cs.this.lC.bI().a(cs.this);
                        if (cs.this.oL.errorCode == -3) {
                            dw.y("Loading URL in WebView: " + cs.this.oL.ol);
                            cs.this.lC.loadUrl(cs.this.oL.ol);
                        } else {
                            dw.y("Loading HTML in WebView.");
                            cs.this.lC.loadDataWithBaseURL(dq.r(cs.this.oL.ol), cs.this.oL.pm, "text/html", "UTF-8", null);
                        }
                    }
                }
            }
        });
        e(j);
    }

    private void d(long j) throws a {
        while (f(j)) {
            if (this.oL != null) {
                synchronized (this.oH) {
                    this.oK = null;
                }
                if (this.oL.errorCode != -2 && this.oL.errorCode != -3) {
                    throw new a("There was a problem getting an ad response. ErrorCode: " + this.oL.errorCode, this.oL.errorCode);
                }
                return;
            }
        }
        throw new a("Timed out waiting for ad response.", 2);
    }

    private void e(long j) throws a {
        while (f(j)) {
            if (this.oM) {
                return;
            }
        }
        throw new a("Timed out waiting for WebView to finish loading.", 2);
    }

    private boolean f(long j) throws a {
        long elapsedRealtime = 60000 - SystemClock.elapsedRealtime() - j;
        if (elapsedRealtime <= 0) {
            return false;
        }
        try {
            this.li.wait(elapsedRealtime);
            return true;
        } catch (InterruptedException e) {
            throw new a("Ad request cancelled.", -1);
        }
    }

    public void a(cz czVar) {
        synchronized (this.li) {
            dw.v("Received ad response.");
            this.oL = czVar;
            this.li.notify();
        }
    }

    public void a(dz dzVar) {
        synchronized (this.li) {
            dw.v("WebView finished loading.");
            this.oM = true;
            this.li.notify();
        }
    }

    public void aY() {
        a aVar;
        synchronized (this.li) {
            long j;
            ak akVar;
            dw.v("AdLoaderBackgroundTask started.");
            cx cxVar = new cx(this.oI, this.oJ.y().a(this.mContext));
            ak akVar2 = null;
            int i = InvalidManifestConfigException.MISSING_ACTIVITY_DECLARATION;
            long j2 = -1;
            try {
                long elapsedRealtime = SystemClock.elapsedRealtime();
                do_ a = cu.a(this.mContext, cxVar, this);
                synchronized (this.oH) {
                    this.oK = a;
                    if (this.oK == null) {
                        throw new a("Could not start the ad request service.", 0);
                    }
                }
                d(elapsedRealtime);
                j2 = SystemClock.elapsedRealtime();
                aZ();
                if (cxVar.kN.lU != null) {
                    akVar2 = a(cxVar);
                }
                if (this.oL.po) {
                    a(cxVar, elapsedRealtime);
                } else if (this.oL.pu) {
                    c(elapsedRealtime);
                } else {
                    b(elapsedRealtime);
                }
                j = j2;
                akVar = akVar2;
            } catch (a e) {
                aVar = e;
                i = aVar.getErrorCode();
                if (i == 3 || i == -1) {
                    dw.x(aVar.getMessage());
                } else {
                    dw.z(aVar.getMessage());
                }
                if (this.oL == null) {
                    this.oL = new cz(i);
                } else {
                    this.oL = new cz(i, this.oL.ni);
                }
                dv.rp.post(new Runnable() {
                    public void run() {
                        cs.this.onStop();
                    }
                });
                j = j2;
                akVar = akVar2;
            }
            if (!TextUtils.isEmpty(this.oL.pw)) {
                try {
                    String str = this.oL.pw;
                    JSONObject jSONObject = jSONObject;
                } catch (Exception e2) {
                    dw.b("Error parsing the JSON for Active View.", e2);
                }
                dv.rp.post(new AnonymousClass_2(new dh(cxVar.pg, this.lC, this.oL.ne, i, this.oL.nf, this.oL.pq, this.oL.orientation, this.oL.ni, cxVar.pj, this.oL.po, this.oO == null ? this.oO.nx : null, this.oO == null ? this.oO.ny : null, this.oO == null ? this.oO.nz : null, this.mR, this.oO == null ? this.oO.nA : null, this.oL.pp, akVar, this.oL.pn, j, this.oL.ps, this.oL.pt, jSONObject)));
            }
            JSONObject jSONObject2 = null;
            if (this.oO == null) {
            }
            if (this.oO == null) {
            }
            if (this.oO == null) {
            }
            if (this.oO == null) {
            }
            dv.rp.post(new AnonymousClass_2(new dh(cxVar.pg, this.lC, this.oL.ne, i, this.oL.nf, this.oL.pq, this.oL.orientation, this.oL.ni, cxVar.pj, this.oL.po, this.oO == null ? this.oO.nx : null, this.oO == null ? this.oO.ny : null, this.oO == null ? this.oO.nz : null, this.mR, this.oO == null ? this.oO.nA : null, this.oL.pp, akVar, this.oL.pn, j, this.oL.ps, this.oL.pt, jSONObject2)));
        }
    }

    protected void c(long j) throws a {
        int i;
        int i2;
        ak R = this.lC.R();
        if (R.lT) {
            i = this.mContext.getResources().getDisplayMetrics().widthPixels;
            i2 = this.mContext.getResources().getDisplayMetrics().heightPixels;
        } else {
            i = R.widthPixels;
            i2 = R.heightPixels;
        }
        ct ctVar = new ct(this, this.lC, i, i2);
        dv.rp.post(new AnonymousClass_4(ctVar));
        e(j);
        if (ctVar.bc()) {
            dw.v("Ad-Network indicated no fill with passback URL.");
            throw new a("AdNetwork sent passback url", 3);
        } else if (!ctVar.bd()) {
            throw new a("AdNetwork timed out", 2);
        }
    }

    public void onStop() {
        synchronized (this.oH) {
            if (this.oK != null) {
                this.oK.cancel();
            }
            this.lC.stopLoading();
            dq.a(this.lC);
            if (this.oN != null) {
                this.oN.cancel();
            }
        }
    }
}