package com.google.android.gms.analytics;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import com.facebook.ads.internal.AdWebViewUtils;
import com.google.android.gms.internal.fq;
import com.mopub.common.Preconditions;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import mobi.vserv.android.ads.VservConstants;

public class Tracker {
    private final TrackerHandler vM;
    private final Map<String, String> vN;
    private ad vO;
    private final h vP;
    private final ae vQ;
    private final g vR;
    private boolean vS;
    private a vT;
    private aj vU;

    private class a implements a {
        private i tg;
        private Timer vV;
        private TimerTask vW;
        private boolean vX;
        private boolean vY;
        private int vZ;
        private long wa;
        private boolean wb;
        private long wc;

        private class a extends TimerTask {
            private a() {
            }

            public void run() {
                a.this.vX = false;
            }
        }

        class AnonymousClass_1 implements i {
            final /* synthetic */ Tracker we;

            AnonymousClass_1(Tracker tracker) {
                this.we = tracker;
            }

            public long currentTimeMillis() {
                return System.currentTimeMillis();
            }
        }

        public a() {
            this.vX = false;
            this.vY = false;
            this.vZ = 0;
            this.wa = -1;
            this.wb = false;
            this.tg = new AnonymousClass_1(Tracker.this);
        }

        private void df() {
            GoogleAnalytics cM = GoogleAnalytics.cM();
            if (cM == null) {
                aa.w("GoogleAnalytics isn't initialized for the Tracker!");
            } else if (this.wa >= 0 || this.vY) {
                cM.a(Tracker.this.vT);
            } else {
                cM.b(Tracker.this.vT);
            }
        }

        private synchronized void dg() {
            if (this.vV != null) {
                this.vV.cancel();
                this.vV = null;
            }
        }

        public long dc() {
            return this.wa;
        }

        public boolean dd() {
            return this.vY;
        }

        public boolean de() {
            boolean z = this.wb;
            this.wb = false;
            return z;
        }

        boolean dh() {
            return this.wa == 0 || (this.wa > 0 && this.tg.currentTimeMillis() > this.wc + this.wa);
        }

        public void enableAutoActivityTracking(boolean enabled) {
            this.vY = enabled;
            df();
        }

        public void f(Activity activity) {
            u.cy().a(com.google.android.gms.analytics.u.a.uQ);
            dg();
            if (!this.vX && this.vZ == 0 && dh()) {
                this.wb = true;
            }
            this.vX = true;
            this.vZ++;
            if (this.vY) {
                Map hashMap = new HashMap();
                hashMap.put("&t", "appview");
                u.cy().t(true);
                Tracker.this.set("&cd", Tracker.this.vU != null ? Tracker.this.vU.h(activity) : activity.getClass().getCanonicalName());
                Tracker.this.send(hashMap);
                u.cy().t(false);
            }
        }

        public void g(Activity activity) {
            u.cy().a(com.google.android.gms.analytics.u.a.uR);
            this.vZ--;
            this.vZ = Math.max(0, this.vZ);
            this.wc = this.tg.currentTimeMillis();
            if (this.vZ == 0) {
                dg();
                this.vW = new a(null);
                this.vV = new Timer("waitForActivityStart");
                this.vV.schedule(this.vW, AdWebViewUtils.DEFAULT_IMPRESSION_DELAY_MS);
            }
        }

        public void setSessionTimeout(long sessionTimeout) {
            this.wa = sessionTimeout;
            df();
        }
    }

    Tracker(String trackingId, TrackerHandler handler) {
        this(trackingId, handler, h.cb(), ae.cZ(), g.ca(), new z("tracking"));
    }

    Tracker(String trackingId, TrackerHandler handler, h clientIdDefaultProvider, ae screenResolutionDefaultProvider, g appFieldsDefaultProvider, ad rateLimiter) {
        this.vN = new HashMap();
        this.vM = handler;
        if (trackingId != null) {
            this.vN.put("&tid", trackingId);
        }
        this.vN.put("useSecure", "1");
        this.vP = clientIdDefaultProvider;
        this.vQ = screenResolutionDefaultProvider;
        this.vR = appFieldsDefaultProvider;
        this.vO = rateLimiter;
        this.vT = new a();
    }

    void a(Context context, aj ajVar) {
        aa.y("Loading Tracker config values.");
        this.vU = ajVar;
        if (this.vU.dj()) {
            String dk = this.vU.dk();
            set("&tid", dk);
            aa.y("[Tracker] trackingId loaded: " + dk);
        }
        if (this.vU.dl()) {
            dk = Double.toString(this.vU.dm());
            set("&sf", dk);
            aa.y("[Tracker] sample frequency loaded: " + dk);
        }
        if (this.vU.dn()) {
            setSessionTimeout((long) this.vU.getSessionTimeout());
            aa.y("[Tracker] session timeout loaded: " + dc());
        }
        if (this.vU.do()) {
            enableAutoActivityTracking(this.vU.dp());
            aa.y("[Tracker] auto activity tracking loaded: " + dd());
        }
        if (this.vU.dq()) {
            if (this.vU.dr()) {
                set("&aip", "1");
                aa.y("[Tracker] anonymize ip loaded: true");
            }
            aa.y("[Tracker] anonymize ip loaded: false");
        }
        this.vS = this.vU.ds();
        if (this.vU.ds()) {
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionReporter(this, Thread.getDefaultUncaughtExceptionHandler(), context));
            aa.y("[Tracker] report uncaught exceptions loaded: " + this.vS);
        }
    }

    long dc() {
        return this.vT.dc();
    }

    boolean dd() {
        return this.vT.dd();
    }

    public void enableAdvertisingIdCollection(boolean enabled) {
        if (enabled) {
            if (this.vN.containsKey("&ate")) {
                this.vN.remove("&ate");
            }
            if (this.vN.containsKey("&adid")) {
                this.vN.remove("&adid");
            }
        } else {
            this.vN.put("&ate", null);
            this.vN.put("&adid", null);
        }
    }

    public void enableAutoActivityTracking(boolean enabled) {
        this.vT.enableAutoActivityTracking(enabled);
    }

    public String get(String key) {
        u.cy().a(com.google.android.gms.analytics.u.a.tR);
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        if (this.vN.containsKey(key)) {
            return (String) this.vN.get(key);
        }
        if (key.equals("&ul")) {
            return ak.a(Locale.getDefault());
        }
        if (this.vP != null && this.vP.C(key)) {
            return this.vP.getValue(key);
        }
        if (this.vQ == null || !this.vQ.C(key)) {
            return (this.vR == null || !this.vR.C(key)) ? null : this.vR.getValue(key);
        } else {
            return this.vQ.getValue(key);
        }
    }

    public void send(Map<String, String> params) {
        u.cy().a(com.google.android.gms.analytics.u.a.tT);
        Map hashMap = new HashMap();
        hashMap.putAll(this.vN);
        if (params != null) {
            hashMap.putAll(params);
        }
        if (TextUtils.isEmpty((CharSequence) hashMap.get("&tid"))) {
            aa.z(String.format("Missing tracking id (%s) parameter.", new Object[]{"&tid"}));
        }
        String str = (String) hashMap.get("&t");
        if (TextUtils.isEmpty(str)) {
            aa.z(String.format("Missing hit type (%s) parameter.", new Object[]{"&t"}));
            str = Preconditions.EMPTY_ARGUMENTS;
        }
        if (this.vT.de()) {
            hashMap.put("&sc", VservConstants.VPLAY0);
        }
        if (str.equals("transaction") || str.equals("item") || this.vO.cS()) {
            this.vM.q(hashMap);
        } else {
            aa.z("Too many hits sent too quickly, rate limiting invoked.");
        }
    }

    public void set(Object key, String value) {
        fq.b(key, (Object)"Key should be non-null");
        u.cy().a(com.google.android.gms.analytics.u.a.tS);
        this.vN.put(key, value);
    }

    public void setAnonymizeIp(boolean anonymize) {
        set("&aip", ak.u(anonymize));
    }

    public void setAppId(String appId) {
        set("&aid", appId);
    }

    public void setAppInstallerId(String appInstallerId) {
        set("&aiid", appInstallerId);
    }

    public void setAppName(String appName) {
        set("&an", appName);
    }

    public void setAppVersion(String appVersion) {
        set("&av", appVersion);
    }

    public void setClientId(String clientId) {
        set("&cid", clientId);
    }

    public void setEncoding(String encoding) {
        set("&de", encoding);
    }

    public void setHostname(String hostname) {
        set("&dh", hostname);
    }

    public void setLanguage(String language) {
        set("&ul", language);
    }

    public void setLocation(String location) {
        set("&dl", location);
    }

    public void setPage(String page) {
        set("&dp", page);
    }

    public void setReferrer(String referrer) {
        set("&dr", referrer);
    }

    public void setSampleRate(double sampleRate) {
        set("&sf", Double.toHexString(sampleRate));
    }

    public void setScreenColors(String screenColors) {
        set("&sd", screenColors);
    }

    public void setScreenName(String screenName) {
        set("&cd", screenName);
    }

    public void setScreenResolution(int width, int height) {
        if (width >= 0 || height >= 0) {
            set("&sr", width + "x" + height);
        } else {
            aa.z("Invalid width or height. The values should be non-negative.");
        }
    }

    public void setSessionTimeout(long sessionTimeout) {
        this.vT.setSessionTimeout(1000 * sessionTimeout);
    }

    public void setTitle(String title) {
        set("&dt", title);
    }

    public void setUseSecure(boolean useSecure) {
        set("useSecure", ak.u(useSecure));
    }

    public void setViewportSize(String viewportSize) {
        set("&vp", viewportSize);
    }
}