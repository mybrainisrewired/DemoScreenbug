package com.inmobi.monetization.internal;

import android.app.Activity;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;
import com.google.android.gms.location.LocationRequest;
import com.inmobi.androidsdk.IMBrowserActivity;
import com.inmobi.commons.AnimationType;
import com.inmobi.commons.internal.Log;
import com.inmobi.commons.network.Response;
import com.inmobi.monetization.IMBanner;
import com.inmobi.monetization.internal.configs.Initializer;
import com.inmobi.monetization.internal.configs.NetworkEventType;
import com.inmobi.monetization.internal.imai.IMAIController;
import com.inmobi.re.container.IMWebView;
import com.inmobi.re.container.IMWebView.IMWebViewListener;
import com.inmobi.re.container.IMWebView.ViewState;
import com.inmobi.re.container.mraidimpl.MRAIDInterstitialController;
import com.mopub.common.Preconditions;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class BannerAd extends Ad {
    protected static final String KEY_MANUAL_REFRESH = "u-rt";
    protected static final String KEY_TYPE_OF_ADREQ = "requestactivity";
    protected static final String VALUE_OF_ADREQ = "AdRequest";
    AnimationType a;
    boolean b;
    private Activity d;
    private IMWebView e;
    private IMWebView f;
    private boolean g;
    private int h;
    private long i;
    private int j;
    private a k;
    private Animation l;
    private Animation m;
    public IMWebView mCurrentWebView;
    private long n;
    private boolean o;
    private boolean p;
    private AtomicBoolean q;
    private Response r;
    private boolean s;
    private IMWebViewListener t;
    private b u;
    private AnimationListener v;

    class a implements Runnable {
        a() {
        }

        public void run() {
            BannerAd.this.i();
        }
    }

    static class b extends Handler {
        private final WeakReference<BannerAd> a;

        public b(BannerAd bannerAd) {
            this.a = new WeakReference(bannerAd);
        }

        public void handleMessage(Message message) {
            BannerAd bannerAd = (BannerAd) this.a.get();
            if (bannerAd != null) {
                try {
                    switch (message.what) {
                        case IMBrowserActivity.INTERSTITIAL_ACTIVITY:
                            if (!bannerAd.d.hasWindowFocus()) {
                                Log.debug(InvalidManifestErrorMessages.LOGGING_TAG, InvalidManifestErrorMessages.MSG_AD_FOCUS);
                            } else if (MRAIDInterstitialController.isInterstitialDisplayed.get()) {
                                Log.debug(InvalidManifestErrorMessages.LOGGING_TAG, InvalidManifestErrorMessages.MSG_INTERSTITIAL_AD_DISPLAYED);
                            } else {
                                bannerAd.a(true);
                            }
                            break;
                        case LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY:
                            bannerAd.collectMetrics(bannerAd.r, System.currentTimeMillis() - bannerAd.i, NetworkEventType.RENDER_TIMEOUT);
                            if (bannerAd.e()) {
                                bannerAd.e = null;
                            } else {
                                bannerAd.f = null;
                            }
                            if (bannerAd.mCurrentWebView != null) {
                                bannerAd.mCurrentWebView.cancelLoad();
                                bannerAd.mCurrentWebView.stopLoading();
                                bannerAd.mCurrentWebView.deinit();
                                bannerAd.mCurrentWebView = null;
                            }
                            if (bannerAd.mAdListener != null) {
                                bannerAd.mAdListener.onAdRequestFailed(AdErrorCode.AD_RENDERING_TIMEOUT);
                            }
                            break;
                    }
                } catch (Exception e) {
                    Log.internal(InvalidManifestErrorMessages.LOGGING_TAG, "Exception hanlde message adview", e);
                }
            }
            super.handleMessage(message);
        }
    }

    public BannerAd(Activity activity, IMBanner iMBanner, long j, int i) {
        super(j);
        this.g = true;
        this.h = 15;
        this.i = 0;
        this.a = AnimationType.ROTATE_HORIZONTAL_AXIS;
        this.b = true;
        this.j = Initializer.getConfigParams().getDefaultRefreshRate();
        this.n = 0;
        this.o = false;
        this.p = true;
        this.q = new AtomicBoolean(false);
        this.r = null;
        this.s = false;
        this.t = new d(this);
        this.u = new b(this);
        this.v = new e(this);
        this.h = i;
        this.n = j;
        this.d = activity;
        this.s = initialize();
    }

    public BannerAd(Activity activity, IMBanner iMBanner, String str, int i) {
        super(str);
        this.g = true;
        this.h = 15;
        this.i = 0;
        this.a = AnimationType.ROTATE_HORIZONTAL_AXIS;
        this.b = true;
        this.j = Initializer.getConfigParams().getDefaultRefreshRate();
        this.n = 0;
        this.o = false;
        this.p = true;
        this.q = new AtomicBoolean(false);
        this.r = null;
        this.s = false;
        this.t = new d(this);
        this.u = new b(this);
        this.v = new e(this);
        this.h = i;
        this.d = activity;
        this.s = initialize();
    }

    private void a(boolean z) {
        if (!this.s) {
            this.s = initialize();
        }
        if (this.s) {
            this.o = z;
            if (!g()) {
                super.loadAd();
            } else if (this.mAdListener != null) {
                AdErrorCode adErrorCode = AdErrorCode.INVALID_REQUEST;
                adErrorCode.setMessage("Ad click is in progress.Cannot load new ad");
                Log.debug(Constants.LOG_TAG, "Ad click is in progress.Cannot load new ad");
                this.mAdListener.onAdRequestFailed(adErrorCode);
            }
            this.u.removeMessages(IMBrowserActivity.INTERSTITIAL_ACTIVITY);
            if (this.j > 0) {
                this.u.sendEmptyMessageDelayed(IMBrowserActivity.INTERSTITIAL_ACTIVITY, (long) (this.j * 1000));
            }
        } else {
            Log.debug(Constants.LOG_TAG, "Please check for initialization error on the ad. The activity or appId cannot be null or blank");
        }
    }

    private void b(boolean z) {
        this.g = z;
        if (z) {
            this.e.deinit();
            this.e = null;
        } else {
            this.f.deinit();
            this.f = null;
        }
    }

    private ViewGroup d() {
        ViewGroup viewGroup = (ViewGroup) this.e.getParent();
        return viewGroup == null ? (ViewGroup) this.f.getParent() : viewGroup;
    }

    private boolean e() {
        return this.g;
    }

    private void f() {
        try {
            if (this.f != null) {
                this.f.setBackgroundColor(0);
            }
            if (this.e != null) {
                this.e.setBackgroundColor(0);
            }
        } catch (Exception e) {
            Log.debug(Constants.LOG_TAG, "Error setNormalBGColor", e);
        }
    }

    private boolean g() {
        IMWebView iMWebView;
        if (e()) {
            iMWebView = this.f;
        } else {
            iMWebView = this.e;
        }
        String state = iMWebView.getState();
        Log.debug(Constants.LOG_TAG, "Current Ad State: " + state);
        if (ViewState.EXPANDED.toString().equalsIgnoreCase(state) || ViewState.RESIZED.toString().equalsIgnoreCase(state) || ViewState.RESIZING.toString().equalsIgnoreCase(state) || ViewState.EXPANDING.toString().equalsIgnoreCase(state)) {
            Log.debug(Constants.LOG_TAG, InvalidManifestErrorMessages.MSG_AD_STATE);
            return true;
        } else if (!iMWebView.isBusy()) {
            return this.q.get();
        } else {
            Log.debug(Constants.LOG_TAG, InvalidManifestErrorMessages.MSG_AD_BUSY);
            return true;
        }
    }

    private void h() {
        boolean z = false;
        int i = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
        try {
            ViewGroup d = d();
            if (d != null) {
                LayoutParams layoutParams;
                if (e()) {
                    layoutParams = new RelativeLayout.LayoutParams(-1, -1);
                    this.mCurrentWebView = this.e;
                    d.addView(this.e, layoutParams);
                } else {
                    layoutParams = new RelativeLayout.LayoutParams(-1, -1);
                    this.mCurrentWebView = this.f;
                    d.addView(this.f, layoutParams);
                }
                if (!e()) {
                    z = true;
                }
                b(z);
                setDownloadingNewAd(false);
                f();
            }
            if (this.mAdListener != null) {
                this.mAdListener.onAdRequestSucceeded();
            }
            if (this.u != null) {
                this.u.removeMessages(i);
            }
        } catch (Exception e) {
            th = e;
            try {
                Throwable th2;
                th2.printStackTrace();
                Log.debug(Constants.LOG_TAG, "Error swapping banner ads", th2);
                if (this.mAdListener != null) {
                    this.mAdListener.onAdRequestSucceeded();
                }
                if (this.u != null) {
                    this.u.removeMessages(i);
                }
            } catch (Throwable th3) {
                if (this.mAdListener != null) {
                    this.mAdListener.onAdRequestSucceeded();
                }
                if (this.u != null) {
                    this.u.removeMessages(i);
                }
            }
        }
    }

    private void i() {
        if (this.r != null) {
            String responseBody = this.r.getResponseBody();
            long currentTimeMillis = System.currentTimeMillis() - this.mFetchStartTime;
            if (responseBody != null) {
                responseBody = responseBody.replace("@__imm_aft@", Preconditions.EMPTY_ARGUMENTS + currentTimeMillis);
            }
            if (responseBody != null) {
                if (VERSION.SDK_INT <= 8) {
                    responseBody.replaceAll("%", "%25");
                }
                if (e()) {
                    if (this.e == null) {
                        this.e = new IMWebView(this.d, this.t, false, false);
                        if (!this.p) {
                            this.e.disableHardwareAcceleration();
                        }
                    }
                    this.mCurrentWebView = this.e;
                } else {
                    if (this.f == null) {
                        this.f = new IMWebView(this.d, this.t, false, false);
                        if (!this.p) {
                            this.f.disableHardwareAcceleration();
                        }
                    }
                    this.mCurrentWebView = this.f;
                }
                this.mCurrentWebView.addJavascriptInterface(new IMAIController(this.mCurrentWebView), IMAIController.IMAI_BRIDGE);
                this.i = System.currentTimeMillis();
                this.u.sendEmptyMessageDelayed(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY, (long) Initializer.getConfigParams().getRenderTimeOut());
                this.mCurrentWebView.resetMraid();
                this.mCurrentWebView.loadDataWithBaseURL(Preconditions.EMPTY_ARGUMENTS, responseBody, "text/html", null, null);
                collectMetrics(this.r, System.currentTimeMillis() - this.mFetchStartTime, NetworkEventType.RENDER_COMPLETE);
                if (this.b) {
                    h();
                    this.b = false;
                } else if (this.a == AnimationType.ANIMATION_OFF) {
                    h();
                } else {
                    this.k.a(this.a);
                }
            } else {
                Log.debug(Constants.LOG_TAG, "Cannot load Ad. Invalid Ad Response");
                if (this.mAdListener != null) {
                    this.mAdListener.onAdRequestFailed(AdErrorCode.INTERNAL_ERROR);
                }
            }
        }
    }

    Animation a() {
        return this.l;
    }

    void a(Animation animation) {
        this.l = animation;
    }

    int b() {
        try {
            ViewGroup d = d();
            if (d != null) {
                return d.getWidth();
            }
        } catch (Exception e) {
            Log.internal(Constants.LOG_TAG, "Exception getting width of banner view", e);
        }
        return 0;
    }

    void b(Animation animation) {
        this.m = animation;
    }

    int c() {
        try {
            ViewGroup d = d();
            if (d != null) {
                return d.getHeight();
            }
        } catch (Exception e) {
            Log.internal(Constants.LOG_TAG, "Exception getting height of banner view", e);
        }
        return 0;
    }

    void c(Animation animation) {
        try {
            ViewGroup d = d();
            if (d != null) {
                d.startAnimation(animation);
            }
        } catch (Exception e) {
            Throwable th = e;
            th.printStackTrace();
            Log.internal(Constants.LOG_TAG, "Exception animating  banner view", th);
        }
    }

    public void destroy() {
        super.destroy();
        if (this.s) {
            Log.debug(Constants.LOG_TAG, "onDetatchedFromWindow");
            try {
                if (this.mCurrentWebView != null) {
                    this.mCurrentWebView.deinit();
                    this.mCurrentWebView.destroy();
                }
            } catch (Exception e) {
                Log.debug(Constants.LOG_TAG, "Unable to destroy webview, or it has been destroyed already.");
            }
            this.mCurrentWebView = null;
        } else {
            Log.debug(Constants.LOG_TAG, "Please check for initialization error on the ad. The activity or appId cannot be null or blank");
        }
    }

    public void disableHardwareAcceleration() {
        if (this.s) {
            this.p = false;
            if (this.mCurrentWebView != null) {
                this.mCurrentWebView.disableHardwareAcceleration();
            }
        } else {
            Log.debug(Constants.LOG_TAG, "Please check for initialization error on the ad. The activity or appId cannot be null or blank");
        }
    }

    protected Map<String, String> getAdFormatParams() {
        Map<String, String> hashMap = new HashMap();
        hashMap.put("format", AD_FORMAT.IMAI.toString().toLowerCase(Locale.getDefault()));
        hashMap.put("mk-ads", "1");
        hashMap.put(KEY_TYPE_OF_ADREQ, VALUE_OF_ADREQ);
        if (this.o) {
            hashMap.put(KEY_MANUAL_REFRESH, String.valueOf(1));
        } else {
            hashMap.put(KEY_MANUAL_REFRESH, String.valueOf(0));
        }
        if (this.n > 0) {
            hashMap.put("placement-size", b() + "x" + c());
        }
        hashMap.put("mk-ad-slot", String.valueOf(this.h));
        return hashMap;
    }

    public View getView() {
        return this.mCurrentWebView;
    }

    public void handleResponse(i iVar, Response response) {
        if (response != null) {
            try {
                if (this.d != null) {
                    this.r = response;
                    this.d.runOnUiThread(new a());
                }
            } catch (Exception e) {
                Log.debug(Constants.LOG_TAG, "Failed to render banner ad");
                if (this.mAdListener != null) {
                    this.mAdListener.onAdRequestFailed(AdErrorCode.INTERNAL_ERROR);
                }
            }
        }
    }

    protected boolean initialize() {
        if (this.h < 0) {
            Log.debug(Constants.LOG_TAG, InvalidManifestErrorMessages.MSG_INVALID_AD_SIZE);
            return false;
        } else if (this.d == null) {
            Log.debug(Constants.LOG_TAG, InvalidManifestErrorMessages.MSG_NIL_ACTIVITY);
            return false;
        } else {
            this.d = h.a(this.d);
            if (this.e == null) {
                this.e = new IMWebView(this.d, this.t, false, false);
                if (!this.p) {
                    this.e.disableHardwareAcceleration();
                }
                this.e.addJavascriptInterface(new IMAIController(this.e), IMAIController.IMAI_BRIDGE);
            }
            if (this.f == null) {
                this.f = new IMWebView(this.d, this.t, false, false);
                this.mCurrentWebView = this.f;
                if (!this.p) {
                    this.f.disableHardwareAcceleration();
                }
                this.f.addJavascriptInterface(new IMAIController(this.f), IMAIController.IMAI_BRIDGE);
            }
            this.k = new a(this, this.v);
            return super.initialize();
        }
    }

    public void loadAd() {
        a(false);
    }

    public void refreshAd() {
        this.o = true;
        this.u.removeMessages(IMBrowserActivity.INTERSTITIAL_ACTIVITY);
        if (this.j > 0) {
            this.u.sendEmptyMessageDelayed(IMBrowserActivity.INTERSTITIAL_ACTIVITY, (long) (this.j * 1000));
        }
    }

    public void setAdSize(int i) {
        this.h = i;
    }

    public void setAnimation(AnimationType animationType) {
        this.a = animationType;
    }

    public void setRefreshInterval(int i) {
        if (this.s) {
            int minimumRefreshRate = Initializer.getConfigParams().getMinimumRefreshRate();
            this.u.removeMessages(IMBrowserActivity.INTERSTITIAL_ACTIVITY);
            if (i <= 0) {
                this.j = 0;
            } else {
                if (i < minimumRefreshRate) {
                    Log.debug(InvalidManifestErrorMessages.LOGGING_TAG, "Refresh Interval cannot be less than " + minimumRefreshRate + " seconds. Setting refresh rate to " + minimumRefreshRate);
                    this.j = minimumRefreshRate;
                } else {
                    this.j = i;
                }
                if (this.j != 0) {
                    this.u.sendEmptyMessageDelayed(IMBrowserActivity.INTERSTITIAL_ACTIVITY, (long) (this.j * 1000));
                }
            }
        } else {
            Log.debug(Constants.LOG_TAG, "Please check for initialization error on the ad. The activity or appId cannot be null or blank");
        }
    }

    public void stopLoading() {
        if (this.s) {
            super.stopLoading();
        } else {
            Log.debug(Constants.LOG_TAG, "Please check for initialization error on the ad. The activity or appId cannot be null or blank");
        }
    }

    public void stopRefresh() {
        if (this.s) {
            this.u.removeMessages(IMBrowserActivity.INTERSTITIAL_ACTIVITY);
        } else {
            Log.debug(Constants.LOG_TAG, "Please check for initialization error on the ad. The activity or appId cannot be null or blank");
        }
    }
}