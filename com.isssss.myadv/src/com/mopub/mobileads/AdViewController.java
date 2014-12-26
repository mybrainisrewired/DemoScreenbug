package com.mopub.mobileads;

import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;
import com.mopub.common.GpsHelper;
import com.mopub.common.GpsHelper.GpsHelperListener;
import com.mopub.common.LocationService;
import com.mopub.common.MoPub;
import com.mopub.common.Preconditions;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.Dips;
import com.mopub.mobileads.factories.AdFetcherFactory;
import com.mopub.mobileads.factories.HttpClientFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class AdViewController {
    static final int DEFAULT_REFRESH_TIME_MILLISECONDS = 60000;
    static final int MINIMUM_REFRESH_TIME_MILLISECONDS = 10000;
    private static final LayoutParams WRAP_AND_CENTER_LAYOUT_PARAMS;
    private static WeakHashMap<View, Boolean> sViewShouldHonorServerDimensions;
    private AdConfiguration mAdConfiguration;
    private AdFetcher mAdFetcher;
    private boolean mAdWasLoaded;
    private boolean mAutoRefreshEnabled;
    private final Context mContext;
    private GpsHelperListener mGpsHelperListener;
    private Handler mHandler;
    private boolean mIsDestroyed;
    private boolean mIsLoading;
    private boolean mIsTesting;
    private String mKeywords;
    private Map<String, Object> mLocalExtras;
    private Location mLocation;
    private MoPubView mMoPubView;
    private boolean mPreviousAutoRefreshSetting;
    private final Runnable mRefreshRunnable;
    private String mUrl;
    private final WebViewAdUrlGenerator mUrlGenerator;

    class AnonymousClass_4 implements Runnable {
        private final /* synthetic */ View val$view;

        AnonymousClass_4(View view) {
            this.val$view = view;
        }

        public void run() {
            MoPubView moPubView = AdViewController.this.getMoPubView();
            if (moPubView != null) {
                moPubView.removeAllViews();
                moPubView.addView(this.val$view, AdViewController.this.getAdLayoutParams(this.val$view));
            }
        }
    }

    class AdViewControllerGpsHelperListener implements GpsHelperListener {
        AdViewControllerGpsHelperListener() {
        }

        public void onFetchAdInfoCompleted() {
            AdViewController.this.loadNonJavascript(AdViewController.this.generateAdUrl());
        }
    }

    static {
        WRAP_AND_CENTER_LAYOUT_PARAMS = new LayoutParams(-2, -2, 17);
        sViewShouldHonorServerDimensions = new WeakHashMap();
    }

    public AdViewController(Context context, MoPubView view) {
        this.mLocalExtras = new HashMap();
        this.mAutoRefreshEnabled = true;
        this.mPreviousAutoRefreshSetting = true;
        this.mContext = context;
        this.mMoPubView = view;
        this.mUrlGenerator = new WebViewAdUrlGenerator(context);
        this.mAdConfiguration = new AdConfiguration(this.mContext);
        this.mAdFetcher = AdFetcherFactory.create(this, this.mAdConfiguration.getUserAgent());
        this.mGpsHelperListener = new AdViewControllerGpsHelperListener();
        GpsHelper.asyncFetchAdvertisingInfo(this.mContext);
        this.mRefreshRunnable = new Runnable() {
            public void run() {
                AdViewController.this.loadAd();
            }
        };
        this.mHandler = new Handler();
    }

    private void cancelRefreshTimer() {
        this.mHandler.removeCallbacks(this.mRefreshRunnable);
    }

    private LayoutParams getAdLayoutParams(View view) {
        int width = this.mAdConfiguration.getWidth();
        int height = this.mAdConfiguration.getHeight();
        return (!getShouldHonorServerDimensions(view) || width <= 0 || height <= 0) ? WRAP_AND_CENTER_LAYOUT_PARAMS : new LayoutParams(Dips.asIntPixels((float) width, this.mContext), Dips.asIntPixels((float) height, this.mContext), 17);
    }

    private String getServerHostname() {
        return this.mIsTesting ? MoPubView.HOST_FOR_TESTING : MoPubView.HOST;
    }

    private static boolean getShouldHonorServerDimensions(View view) {
        return sViewShouldHonorServerDimensions.get(view) != null;
    }

    private boolean isNetworkAvailable() {
        if (this.mContext.checkCallingPermission("android.permission.ACCESS_NETWORK_STATE") == -1) {
            return true;
        }
        NetworkInfo networkInfo = ((ConnectivityManager) this.mContext.getSystemService("connectivity")).getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void setAutorefreshEnabled(boolean enabled) {
        boolean autorefreshChanged = this.mAdWasLoaded && this.mAutoRefreshEnabled != enabled;
        if (autorefreshChanged) {
            MoPubLog.d(new StringBuilder("Refresh ").append(enabled ? "enabled" : "disabled").append(" for ad unit (").append(this.mAdConfiguration != null ? this.mAdConfiguration.getAdUnitId() : null).append(").").toString());
        }
        this.mAutoRefreshEnabled = enabled;
        if (this.mAdWasLoaded && this.mAutoRefreshEnabled) {
            scheduleRefreshTimerIfEnabled();
        } else if (!this.mAutoRefreshEnabled) {
            cancelRefreshTimer();
        }
    }

    protected static void setShouldHonorServerDimensions(View view) {
        sViewShouldHonorServerDimensions.put(view, Boolean.valueOf(true));
    }

    void adDidFail(MoPubErrorCode errorCode) {
        MoPubLog.i("Ad failed to load.");
        setNotLoading();
        scheduleRefreshTimerIfEnabled();
        getMoPubView().adFailed(errorCode);
    }

    void cleanup() {
        if (!this.mIsDestroyed) {
            setAutorefreshEnabled(false);
            cancelRefreshTimer();
            this.mAdFetcher.cleanup();
            this.mAdFetcher = null;
            this.mAdConfiguration.cleanup();
            this.mMoPubView = null;
            this.mIsDestroyed = true;
        }
    }

    void configureUsingHttpResponse(HttpResponse response) {
        this.mAdConfiguration.addHttpResponse(response);
    }

    @Deprecated
    public void customEventActionWillBegin() {
        registerClick();
    }

    @Deprecated
    public void customEventDidFailToLoadAd() {
        loadFailUrl(MoPubErrorCode.UNSPECIFIED);
    }

    @Deprecated
    public void customEventDidLoadAd() {
        setNotLoading();
        trackImpression();
        scheduleRefreshTimerIfEnabled();
    }

    void fetchAd(String mUrl) {
        if (this.mAdFetcher != null) {
            this.mAdFetcher.fetchAdForUrl(mUrl);
        }
    }

    void forceRefresh() {
        setNotLoading();
        loadAd();
    }

    void forceSetAutorefreshEnabled(boolean enabled) {
        this.mPreviousAutoRefreshSetting = enabled;
        setAutorefreshEnabled(enabled);
    }

    String generateAdUrl() {
        return this.mUrlGenerator.withAdUnitId(this.mAdConfiguration.getAdUnitId()).withKeywords(this.mKeywords).withLocation(this.mLocation).generateUrlString(getServerHostname());
    }

    AdConfiguration getAdConfiguration() {
        return this.mAdConfiguration;
    }

    public int getAdHeight() {
        return this.mAdConfiguration.getHeight();
    }

    Integer getAdTimeoutDelay() {
        return this.mAdConfiguration.getAdTimeoutDelay();
    }

    public String getAdUnitId() {
        return this.mAdConfiguration.getAdUnitId();
    }

    public int getAdWidth() {
        return this.mAdConfiguration.getWidth();
    }

    public boolean getAutorefreshEnabled() {
        return this.mAutoRefreshEnabled;
    }

    public String getClickthroughUrl() {
        return this.mAdConfiguration.getClickthroughUrl();
    }

    public String getKeywords() {
        return this.mKeywords;
    }

    Map<String, Object> getLocalExtras() {
        return this.mLocalExtras != null ? new HashMap(this.mLocalExtras) : new HashMap();
    }

    public Location getLocation() {
        return this.mLocation;
    }

    public MoPubView getMoPubView() {
        return this.mMoPubView;
    }

    public String getRedirectUrl() {
        return this.mAdConfiguration.getRedirectUrl();
    }

    int getRefreshTimeMilliseconds() {
        return this.mAdConfiguration.getRefreshTimeMilliseconds();
    }

    public String getResponseString() {
        return this.mAdConfiguration.getResponseString();
    }

    public boolean getTesting() {
        return this.mIsTesting;
    }

    boolean isDestroyed() {
        return this.mIsDestroyed;
    }

    @Deprecated
    public boolean isFacebookSupported() {
        return false;
    }

    public void loadAd() {
        this.mAdWasLoaded = true;
        if (this.mAdConfiguration.getAdUnitId() == null) {
            MoPubLog.d("Can't load an ad in this ad view because the ad unit ID is null. Did you forget to call setAdUnitId()?");
        } else if (isNetworkAvailable()) {
            if (this.mLocation == null) {
                this.mLocation = LocationService.getLastKnownLocation(this.mContext, MoPub.getLocationPrecision(), MoPub.getLocationAwareness());
            }
            GpsHelper.asyncFetchAdvertisingInfoIfNotCached(this.mContext, this.mGpsHelperListener);
        } else {
            MoPubLog.d("Can't load an ad because there is no network connectivity.");
            scheduleRefreshTimerIfEnabled();
        }
    }

    void loadFailUrl(MoPubErrorCode errorCode) {
        this.mIsLoading = false;
        Log.v("MoPub", new StringBuilder("MoPubErrorCode: ").append(errorCode == null ? Preconditions.EMPTY_ARGUMENTS : errorCode.toString()).toString());
        if (this.mAdConfiguration.getFailUrl() != null) {
            MoPubLog.d(new StringBuilder("Loading failover url: ").append(this.mAdConfiguration.getFailUrl()).toString());
            loadNonJavascript(this.mAdConfiguration.getFailUrl());
        } else {
            adDidFail(MoPubErrorCode.NO_FILL);
        }
    }

    void loadNonJavascript(String url) {
        if (url != null) {
            MoPubLog.d(new StringBuilder("Loading url: ").append(url).toString());
            if (!this.mIsLoading) {
                this.mUrl = url;
                this.mAdConfiguration.setFailUrl(null);
                this.mIsLoading = true;
                fetchAd(this.mUrl);
            } else if (this.mAdConfiguration.getAdUnitId() != null) {
                MoPubLog.i(new StringBuilder("Already loading an ad for ").append(this.mAdConfiguration.getAdUnitId()).append(", wait to finish.").toString());
            }
        }
    }

    void pauseRefresh() {
        this.mPreviousAutoRefreshSetting = this.mAutoRefreshEnabled;
        setAutorefreshEnabled(false);
    }

    void registerClick() {
        new Thread(new Runnable() {
            public void run() {
                if (AdViewController.this.mAdConfiguration.getClickthroughUrl() != null) {
                    DefaultHttpClient httpClient = HttpClientFactory.create();
                    try {
                        MoPubLog.d(new StringBuilder("Tracking click for: ").append(AdViewController.this.mAdConfiguration.getClickthroughUrl()).toString());
                        HttpGet httpget = new HttpGet(AdViewController.this.mAdConfiguration.getClickthroughUrl());
                        httpget.addHeader("User-Agent", AdViewController.this.mAdConfiguration.getUserAgent());
                        httpClient.execute(httpget);
                        httpClient.getConnectionManager().shutdown();
                    } catch (Exception e) {
                        MoPubLog.d(new StringBuilder("Click tracking failed: ").append(AdViewController.this.mAdConfiguration.getClickthroughUrl()).toString(), e);
                        httpClient.getConnectionManager().shutdown();
                    }
                }
            }
        }).start();
    }

    public void reload() {
        MoPubLog.d(new StringBuilder("Reload ad: ").append(this.mUrl).toString());
        loadNonJavascript(this.mUrl);
    }

    void scheduleRefreshTimerIfEnabled() {
        cancelRefreshTimer();
        if (this.mAutoRefreshEnabled && this.mAdConfiguration.getRefreshTimeMilliseconds() > 0) {
            this.mHandler.postDelayed(this.mRefreshRunnable, (long) this.mAdConfiguration.getRefreshTimeMilliseconds());
        }
    }

    void setAdContentView(View view) {
        this.mHandler.post(new AnonymousClass_4(view));
    }

    public void setAdUnitId(String adUnitId) {
        this.mAdConfiguration.setAdUnitId(adUnitId);
    }

    @Deprecated
    public void setClickthroughUrl(String clickthroughUrl) {
        this.mAdConfiguration.setClickthroughUrl(clickthroughUrl);
    }

    @Deprecated
    public void setFacebookSupported(boolean enabled) {
    }

    void setFailUrl(String failUrl) {
        this.mAdConfiguration.setFailUrl(failUrl);
    }

    @Deprecated
    void setGpsHelperListener(GpsHelperListener gpsHelperListener) {
        this.mGpsHelperListener = gpsHelperListener;
    }

    public void setKeywords(String keywords) {
        this.mKeywords = keywords;
    }

    void setLocalExtras(Map<String, Object> localExtras) {
        Map hashMap;
        if (localExtras != null) {
            hashMap = new HashMap(localExtras);
        } else {
            hashMap = new HashMap();
        }
        this.mLocalExtras = hashMap;
    }

    public void setLocation(Location location) {
        this.mLocation = location;
    }

    void setNotLoading() {
        this.mIsLoading = false;
    }

    @Deprecated
    void setRefreshTimeMilliseconds(int refreshTimeMilliseconds) {
        this.mAdConfiguration.setRefreshTimeMilliseconds(refreshTimeMilliseconds);
    }

    public void setTesting(boolean enabled) {
        this.mIsTesting = enabled;
    }

    public void setTimeout(int milliseconds) {
        if (this.mAdFetcher != null) {
            this.mAdFetcher.setTimeout(milliseconds);
        }
    }

    void trackImpression() {
        new Thread(new Runnable() {
            public void run() {
                if (AdViewController.this.mAdConfiguration.getImpressionUrl() != null) {
                    DefaultHttpClient httpClient = HttpClientFactory.create();
                    try {
                        HttpGet httpget = new HttpGet(AdViewController.this.mAdConfiguration.getImpressionUrl());
                        httpget.addHeader("User-Agent", AdViewController.this.mAdConfiguration.getUserAgent());
                        httpClient.execute(httpget);
                        httpClient.getConnectionManager().shutdown();
                    } catch (Exception e) {
                        MoPubLog.d(new StringBuilder("Impression tracking failed : ").append(AdViewController.this.mAdConfiguration.getImpressionUrl()).toString(), e);
                        httpClient.getConnectionManager().shutdown();
                    }
                }
            }
        }).start();
    }

    void unpauseRefresh() {
        setAutorefreshEnabled(this.mPreviousAutoRefreshSetting);
    }
}