package com.google.android.gms.internal;

import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.millennialmedia.android.MMAdView;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ea extends WebViewClient {
    protected final dz lC;
    private final Object li;
    private az mF;
    private bc mP;
    private a oW;
    private final HashMap<String, bb> rA;
    private u rB;
    private cf rC;
    private boolean rD;
    private boolean rE;
    private ci rF;

    class AnonymousClass_1 implements Runnable {
        final /* synthetic */ cc rG;

        AnonymousClass_1(cc ccVar) {
            this.rG = ccVar;
        }

        public void run() {
            this.rG.aM();
        }
    }

    public static interface a {
        void a(dz dzVar);
    }

    public ea(dz dzVar, boolean z) {
        this.rA = new HashMap();
        this.li = new Object();
        this.rD = false;
        this.lC = dzVar;
        this.rE = z;
    }

    private static boolean c(Uri uri) {
        String scheme = uri.getScheme();
        return "http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme);
    }

    private void d(Uri uri) {
        String path = uri.getPath();
        bb bbVar = (bb) this.rA.get(path);
        if (bbVar != null) {
            Map b = dq.b(uri);
            if (dw.n(MMAdView.TRANSITION_UP)) {
                dw.y("Received GMSG: " + path);
                Iterator it = b.keySet().iterator();
                while (it.hasNext()) {
                    path = (String) it.next();
                    dw.y("  " + path + ": " + ((String) b.get(path)));
                }
            }
            bbVar.b(this.lC, b);
        } else {
            dw.y("No GMSG handler found for GMSG: " + uri);
        }
    }

    public final void a(cb cbVar) {
        cf cfVar = null;
        boolean bL = this.lC.bL();
        u uVar = (!bL || this.lC.R().lT) ? this.rB : null;
        if (!bL) {
            cfVar = this.rC;
        }
        a(new ce(cbVar, uVar, cfVar, this.rF, this.lC.bK()));
    }

    protected void a(ce ceVar) {
        cc.a(this.lC.getContext(), ceVar);
    }

    public final void a(a aVar) {
        this.oW = aVar;
    }

    public void a(u uVar, cf cfVar, az azVar, ci ciVar, boolean z, bc bcVar) {
        a("/appEvent", new ay(azVar));
        a("/canOpenURLs", ba.mH);
        a("/click", ba.mI);
        a("/close", ba.mJ);
        a("/customClose", ba.mK);
        a("/httpTrack", ba.mL);
        a("/log", ba.mM);
        a("/open", new bd(bcVar));
        a("/touch", ba.mN);
        a("/video", ba.mO);
        this.rB = uVar;
        this.rC = cfVar;
        this.mF = azVar;
        this.mP = bcVar;
        this.rF = ciVar;
        q(z);
    }

    public final void a(String str, bb bbVar) {
        this.rA.put(str, bbVar);
    }

    public final void a(boolean z, int i) {
        u uVar = (!this.lC.bL() || this.lC.R().lT) ? this.rB : null;
        a(new ce(uVar, this.rC, this.rF, this.lC, z, i, this.lC.bK()));
    }

    public final void a(boolean z, int i, String str) {
        cf cfVar = null;
        boolean bL = this.lC.bL();
        u uVar = (!bL || this.lC.R().lT) ? this.rB : null;
        if (!bL) {
            cfVar = this.rC;
        }
        a(new ce(uVar, cfVar, this.mF, this.rF, this.lC, z, i, str, this.lC.bK(), this.mP));
    }

    public final void a(boolean z, int i, String str, String str2) {
        boolean bL = this.lC.bL();
        u uVar = (!bL || this.lC.R().lT) ? this.rB : null;
        a(new ce(uVar, bL ? null : this.rC, this.mF, this.rF, this.lC, z, i, str, str2, this.lC.bK(), this.mP));
    }

    public final void aM() {
        synchronized (this.li) {
            this.rD = false;
            this.rE = true;
            cc bH = this.lC.bH();
            if (bH != null) {
                if (dv.bD()) {
                    bH.aM();
                } else {
                    dv.rp.post(new AnonymousClass_1(bH));
                }
            }
        }
    }

    public boolean bP() {
        boolean z;
        synchronized (this.li) {
            z = this.rE;
        }
        return z;
    }

    public final void onLoadResource(WebView webView, String url) {
        dw.y("Loading resource: " + url);
        Uri parse = Uri.parse(url);
        if ("gmsg".equalsIgnoreCase(parse.getScheme()) && "mobileads.google.com".equalsIgnoreCase(parse.getHost())) {
            d(parse);
        }
    }

    public final void onPageFinished(WebView webView, String url) {
        if (this.oW != null) {
            this.oW.a(this.lC);
            this.oW = null;
        }
    }

    public final void q(boolean z) {
        this.rD = z;
    }

    public final void reset() {
        synchronized (this.li) {
            this.rA.clear();
            this.rB = null;
            this.rC = null;
            this.oW = null;
            this.mF = null;
            this.rD = false;
            this.rE = false;
            this.mP = null;
            this.rF = null;
        }
    }

    public final boolean shouldOverrideUrlLoading(WebView webView, String url) {
        dw.y("AdWebView shouldOverrideUrlLoading: " + url);
        Uri parse = Uri.parse(url);
        if ("gmsg".equalsIgnoreCase(parse.getScheme()) && "mobileads.google.com".equalsIgnoreCase(parse.getHost())) {
            d(parse);
        } else if (this.rD && webView == this.lC && c(parse)) {
            return super.shouldOverrideUrlLoading(webView, url);
        } else {
            if (this.lC.willNotDraw()) {
                dw.z("AdWebView unable to handle URL: " + url);
            } else {
                Uri uri;
                try {
                    l bJ = this.lC.bJ();
                    if (bJ != null && bJ.a(parse)) {
                        parse = bJ.a(parse, this.lC.getContext());
                    }
                    uri = parse;
                } catch (m e) {
                    dw.z("Unable to append parameter to URL: " + url);
                    uri = parse;
                }
                a(new cb("android.intent.action.VIEW", uri.toString(), null, null, null, null, null));
            }
        }
        return true;
    }
}