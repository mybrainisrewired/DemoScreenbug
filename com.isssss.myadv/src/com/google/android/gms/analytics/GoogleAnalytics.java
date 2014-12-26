package com.google.android.gms.analytics;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build.VERSION;
import android.os.Bundle;
import com.inmobi.commons.analytics.iat.impl.AdTrackerConstants;
import com.millennialmedia.android.MMAdView;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class GoogleAnalytics extends TrackerHandler {
    private static boolean uY;
    private static GoogleAnalytics vf;
    private Context mContext;
    private f sH;
    private String so;
    private String sp;
    private boolean uZ;
    private af va;
    private volatile Boolean vb;
    private Logger vc;
    private Set<a> vd;
    private boolean ve;

    static interface a {
        void f(Activity activity);

        void g(Activity activity);
    }

    class b implements ActivityLifecycleCallbacks {
        b() {
        }

        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        public void onActivityDestroyed(Activity activity) {
        }

        public void onActivityPaused(Activity activity) {
        }

        public void onActivityResumed(Activity activity) {
        }

        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        public void onActivityStarted(Activity activity) {
            GoogleAnalytics.this.d(activity);
        }

        public void onActivityStopped(Activity activity) {
            GoogleAnalytics.this.e(activity);
        }
    }

    protected GoogleAnalytics(Context context) {
        this(context, t.q(context), r.ci());
    }

    private GoogleAnalytics(Context context, f thread, af serviceManager) {
        this.vb = Boolean.valueOf(false);
        this.ve = false;
        if (context == null) {
            throw new IllegalArgumentException(AdTrackerConstants.MSG_APP_CONTEXT_NULL);
        }
        this.mContext = context.getApplicationContext();
        this.sH = thread;
        this.va = serviceManager;
        g.n(this.mContext);
        ae.n(this.mContext);
        h.n(this.mContext);
        this.vc = new l();
        this.vd = new HashSet();
        cN();
    }

    private int I(String str) {
        String toLowerCase = str.toLowerCase();
        return "verbose".equals(toLowerCase) ? 0 : "info".equals(toLowerCase) ? 1 : "warning".equals(toLowerCase) ? MMAdView.TRANSITION_UP : "error".equals(toLowerCase) ? MMAdView.TRANSITION_DOWN : -1;
    }

    private Tracker a(Tracker tracker) {
        if (this.so != null) {
            tracker.set("&an", this.so);
        }
        if (this.sp != null) {
            tracker.set("&av", this.sp);
        }
        return tracker;
    }

    static GoogleAnalytics cM() {
        GoogleAnalytics googleAnalytics;
        synchronized (GoogleAnalytics.class) {
            googleAnalytics = vf;
        }
        return googleAnalytics;
    }

    private void cN() {
        if (!uY) {
            ApplicationInfo applicationInfo;
            ApplicationInfo applicationInfo2 = null;
            try {
                applicationInfo = this.mContext.getPackageManager().getApplicationInfo(this.mContext.getPackageName(), 129);
            } catch (NameNotFoundException e) {
                aa.y("PackageManager doesn't know about package: " + e);
                applicationInfo = applicationInfo2;
            }
            if (applicationInfo == null) {
                aa.z("Couldn't get ApplicationInfo to load gloabl config.");
            } else {
                Bundle bundle = applicationInfo.metaData;
                if (bundle != null) {
                    int i = bundle.getInt("com.google.android.gms.analytics.globalConfigResource");
                    if (i > 0) {
                        w wVar = (w) new v(this.mContext).p(i);
                        if (wVar != null) {
                            a(wVar);
                        }
                    }
                }
            }
        }
    }

    private void d(Activity activity) {
        Iterator it = this.vd.iterator();
        while (it.hasNext()) {
            ((a) it.next()).f(activity);
        }
    }

    private void e(Activity activity) {
        Iterator it = this.vd.iterator();
        while (it.hasNext()) {
            ((a) it.next()).g(activity);
        }
    }

    public static GoogleAnalytics getInstance(Context context) {
        GoogleAnalytics googleAnalytics;
        synchronized (GoogleAnalytics.class) {
            if (vf == null) {
                vf = new GoogleAnalytics(context);
            }
            googleAnalytics = vf;
        }
        return googleAnalytics;
    }

    void a(a aVar) {
        this.vd.add(aVar);
    }

    void a(w wVar) {
        aa.y("Loading global config values.");
        if (wVar.cC()) {
            this.so = wVar.cD();
            aa.y("app name loaded: " + this.so);
        }
        if (wVar.cE()) {
            this.sp = wVar.cF();
            aa.y("app version loaded: " + this.sp);
        }
        if (wVar.cG()) {
            int I = I(wVar.cH());
            if (I >= 0) {
                aa.y("log level loaded: " + I);
                getLogger().setLogLevel(I);
            }
        }
        if (wVar.cI()) {
            this.va.setLocalDispatchPeriod(wVar.cJ());
        }
        if (wVar.cK()) {
            setDryRun(wVar.cL());
        }
    }

    void b(a aVar) {
        this.vd.remove(aVar);
    }

    @Deprecated
    public void dispatchLocalHits() {
        this.va.dispatchLocalHits();
    }

    public void enableAutoActivityReports(Application application) {
        if (VERSION.SDK_INT >= 14 && !this.ve) {
            application.registerActivityLifecycleCallbacks(new b());
            this.ve = true;
        }
    }

    public boolean getAppOptOut() {
        u.cy().a(com.google.android.gms.analytics.u.a.uz);
        return this.vb.booleanValue();
    }

    public Logger getLogger() {
        return this.vc;
    }

    public boolean isDryRunEnabled() {
        u.cy().a(com.google.android.gms.analytics.u.a.uL);
        return this.uZ;
    }

    public Tracker newTracker(int configResId) {
        Tracker a;
        synchronized (this) {
            u.cy().a(com.google.android.gms.analytics.u.a.uv);
            Tracker tracker = new Tracker(null, this);
            if (configResId > 0) {
                aj ajVar = (aj) new ai(this.mContext).p(configResId);
                if (ajVar != null) {
                    tracker.a(this.mContext, ajVar);
                }
            }
            a = a(tracker);
        }
        return a;
    }

    public Tracker newTracker(String trackingId) {
        Tracker a;
        synchronized (this) {
            u.cy().a(com.google.android.gms.analytics.u.a.uv);
            a = a(new Tracker(trackingId, this));
        }
        return a;
    }

    void q(Map<String, String> map) {
        synchronized (this) {
            if (map == null) {
                throw new IllegalArgumentException("hit cannot be null");
            }
            ak.a(map, "&ul", ak.a(Locale.getDefault()));
            ak.a(map, "&sr", ae.cZ().getValue("&sr"));
            map.put("&_u", u.cy().cA());
            u.cy().cz();
            this.sH.q(map);
        }
    }

    public void reportActivityStart(Activity activity) {
        if (!this.ve) {
            d(activity);
        }
    }

    public void reportActivityStop(Activity activity) {
        if (!this.ve) {
            e(activity);
        }
    }

    public void setAppOptOut(boolean optOut) {
        u.cy().a(com.google.android.gms.analytics.u.a.uy);
        this.vb = Boolean.valueOf(optOut);
        if (this.vb.booleanValue()) {
            this.sH.bR();
        }
    }

    public void setDryRun(boolean dryRun) {
        u.cy().a(com.google.android.gms.analytics.u.a.uK);
        this.uZ = dryRun;
    }

    @Deprecated
    public void setLocalDispatchPeriod(int dispatchPeriodInSeconds) {
        this.va.setLocalDispatchPeriod(dispatchPeriodInSeconds);
    }

    public void setLogger(Logger logger) {
        u.cy().a(com.google.android.gms.analytics.u.a.uM);
        this.vc = logger;
    }
}