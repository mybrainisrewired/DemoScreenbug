package com.facebook.ads;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.net.http.SslError;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.facebook.ads.internal.AdDataModel;
import com.facebook.ads.internal.AdHandler.ImpressionHelper;
import com.facebook.ads.internal.AdRequest.Callback;
import com.facebook.ads.internal.AdRequestController;
import com.facebook.ads.internal.AdResponse;
import com.facebook.ads.internal.AdType;
import com.facebook.ads.internal.AdWebViewInterface;
import com.facebook.ads.internal.AdWebViewUtils;
import com.facebook.ads.internal.HtmlAdDataModel;
import com.facebook.ads.internal.HtmlAdHandler;
import com.facebook.ads.internal.StringUtils;
import com.facebook.ads.internal.action.AdAction;
import com.facebook.ads.internal.action.AdActionFactory;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import java.util.Map;
import java.util.UUID;

public class AdView extends RelativeLayout implements Ad {
    private static final int DEFAULT_ALPHA = 255;
    private static final String DEFAULT_ENCODING = "utf-8";
    private static final String DEFAULT_MIME_TYPE = "text/html";
    private static final int MIN_ALPHA = 229;
    private static final String TAG;
    private HtmlAdHandler adHandler;
    private AdListener adListener;
    private AdRequestController adRequestController;
    private final AdSize adSize;
    private WebView adWebView;
    private int currentAlpha;
    private ImpressionListener impListener;
    private final DisplayMetrics metrics;
    private final String placementId;
    private final ScreenStateReceiver screenStateReceiver;
    private int viewabilityThreshold;

    private class AdWebViewClient extends WebViewClient {
        private AdWebViewClient() {
        }

        public void onLoadResource(WebView view, String url) {
            AdView.this.adHandler.activateAd();
        }

        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            String urlPrefix = AdSettings.getUrlPrefix();
            if (StringUtils.isNullOrEmpty(urlPrefix) || !urlPrefix.endsWith(".sb")) {
                handler.cancel();
            } else {
                handler.proceed();
            }
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (AdView.this.adListener != null) {
                AdView.this.adListener.onAdClicked(AdView.this);
            }
            AdAction adAction = AdActionFactory.getAdAction(AdView.this.getContext(), Uri.parse(url));
            Map<String, String> map = ((HtmlAdDataModel) AdView.this.adHandler.getAdDataModel()).getDataModelMap();
            map.put(InterstitialAd.INTERSTITIAL_UNIQUE_ID_EXTRA, UUID.randomUUID().toString());
            if (adAction != null) {
                try {
                    adAction.execute(map);
                } catch (Exception e) {
                    Log.e(TAG, "Error executing action", e);
                }
            }
            return true;
        }
    }

    private class ScreenStateReceiver extends BroadcastReceiver {
        private ScreenStateReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.SCREEN_OFF".equals(intent.getAction())) {
                AdView.this.adHandler.cancelImpressionRetry();
            } else {
                AdView.this.adHandler.scheduleImpressionRetry();
            }
        }
    }

    static {
        TAG = AdView.class.getSimpleName();
    }

    public AdView(Context context, String placementId, AdSize adSize) {
        super(context);
        this.currentAlpha = 255;
        this.adListener = null;
        this.impListener = null;
        if (adSize == null || adSize == AdSize.INTERSTITIAL) {
            throw new IllegalArgumentException("adSize");
        }
        this.placementId = placementId;
        this.adSize = adSize;
        this.metrics = context.getResources().getDisplayMetrics();
        this.screenStateReceiver = new ScreenStateReceiver(null);
        initializeView(context);
        this.adHandler = new HtmlAdHandler(this.adWebView, new ImpressionHelper() {
            public void afterImpressionSent() {
                if (AdView.this.adRequestController != null) {
                    AdView.this.adRequestController.scheduleRefresh("on imp sent");
                }
            }

            public void onLoggingImpression() {
                if (AdView.this.impListener != null) {
                    AdView.this.impListener.onLoggingImpression(AdView.this);
                }
            }

            public boolean shouldSendImpression() {
                return AdView.this.isAdViewVisible();
            }
        }, 1000, context);
        registerScreenStateReceiver();
    }

    private Callback createAdRequestCallback() {
        return new Callback() {
            public void onCompleted(AdResponse adResponse) {
                AdView.this.adHandler.cancelImpressionRetry();
                AdDataModel dataModel = adResponse.getDataModel();
                if (dataModel != null && dataModel instanceof HtmlAdDataModel) {
                    AdView.this.adHandler.setAdDataModel((HtmlAdDataModel) dataModel);
                    AdView.this.updateView((HtmlAdDataModel) dataModel);
                    if (AdView.this.adListener != null) {
                        AdView.this.adListener.onAdLoaded(AdView.this);
                    }
                    AdView.this.viewabilityThreshold = adResponse.getViewabilityThreshold();
                } else if (dataModel == null) {
                    if (AdView.this.adListener != null) {
                        AdView.this.adListener.onError(AdView.this, adResponse.getError() != null ? adResponse.getError() : AdError.INTERNAL_ERROR);
                    }
                    if (AdView.this.adRequestController != null) {
                        AdView.this.adRequestController.scheduleRefresh("on no fill");
                    }
                } else {
                    if (AdView.this.adListener != null) {
                        AdView.this.adListener.onError(AdView.this, AdError.INTERNAL_ERROR);
                    }
                    if (AdView.this.adRequestController != null) {
                        AdView.this.adRequestController.scheduleRefresh("on internal error");
                    }
                }
            }

            public void onError(AdError error) {
                AdView.this.adHandler.cancelImpressionRetry();
                if (AdView.this.adListener != null) {
                    AdView.this.adListener.onError(AdView.this, error);
                }
            }
        };
    }

    private void ensureAdRequestController() {
        if (this.adRequestController == null) {
            throw new RuntimeException("No request controller available, has the AdView been destroyed?");
        }
    }

    private void initializeView(Context context) {
        this.adWebView = new WebView(context);
        this.adWebView.setVisibility(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES);
        AdWebViewUtils.config(this.adWebView, new AdWebViewClient(null), new AdWebViewInterface());
        addView(this.adWebView);
        resizeAdView();
        this.adRequestController = new AdRequestController(getContext(), this.placementId, this.adSize, true, AdType.HTML, createAdRequestCallback());
    }

    private boolean isAdViewVisible() {
        if (getVisibility() != 0 || getParent() == null || this.currentAlpha < 229) {
            return false;
        }
        int[] location = new int[2];
        getLocationOnScreen(location);
        if (location[0] < 0 || this.metrics.widthPixels - location[0] < ((int) Math.ceil((double) (((float) this.adSize.getWidth()) * this.metrics.density)))) {
            return false;
        }
        int adHeight = (int) Math.ceil((double) (((float) this.adSize.getHeight()) * this.metrics.density));
        int verticalInvisibleThreshold = (int) ((((double) adHeight) * (100.0d - ((double) this.viewabilityThreshold))) / 100.0d);
        return (location[1] >= 0 || Math.abs(location[1]) <= verticalInvisibleThreshold) && location[1] + adHeight - this.metrics.heightPixels <= verticalInvisibleThreshold;
    }

    private void registerScreenStateReceiver() {
        IntentFilter filter = new IntentFilter("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.SCREEN_OFF");
        getContext().registerReceiver(this.screenStateReceiver, filter);
    }

    private void resizeAdView() {
        if (this.adWebView != null) {
            LayoutParams layoutParams = new LayoutParams(((int) (((float) this.metrics.widthPixels) / this.metrics.density)) >= this.adSize.getWidth() ? this.metrics.widthPixels : (int) Math.ceil((double) (((float) this.adSize.getWidth()) * this.metrics.density)), (int) Math.ceil((double) (((float) this.adSize.getHeight()) * this.metrics.density)));
            layoutParams.addRule(ApiEventType.API_MRAID_IS_VIEWABLE);
            this.adWebView.setLayoutParams(layoutParams);
        }
    }

    private void unregisterScreenStateReceiver() {
        getContext().unregisterReceiver(this.screenStateReceiver);
    }

    private void updateView(HtmlAdDataModel dataModel) {
        if (this.adWebView != null) {
            this.adWebView.loadUrl("about:blank");
            this.adWebView.clearCache(true);
            this.adWebView.setVisibility(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES);
            this.adWebView.loadDataWithBaseURL(AdWebViewUtils.WEBVIEW_BASE_URL, dataModel.getMarkup(), DEFAULT_MIME_TYPE, DEFAULT_ENCODING, null);
            this.adWebView.setVisibility(0);
        }
    }

    public void destroy() {
        if (this.adRequestController != null) {
            this.adRequestController.destroy();
            this.adRequestController = null;
        }
        this.adHandler.cancelImpressionRetry();
        this.adHandler.destroy();
        if (this.adWebView != null) {
            unregisterScreenStateReceiver();
            try {
                WebView.class.getMethod("onPause", new Class[0]).invoke(this.adWebView, new Object[0]);
            } catch (Exception e) {
            }
            removeView(this.adWebView);
            this.adWebView.destroy();
            this.adWebView = null;
        }
    }

    public void disableAutoRefresh() {
        this.adRequestController.disableRefresh();
    }

    public void loadAd() {
        ensureAdRequestController();
        this.adRequestController.loadAd();
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        resizeAdView();
    }

    protected boolean onSetAlpha(int alpha) {
        this.currentAlpha = alpha;
        return super.onSetAlpha(alpha);
    }

    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (this.adRequestController != null) {
            this.adRequestController.onWindowVisibilityChanged(visibility);
        }
        if (visibility == 0) {
            this.adHandler.scheduleImpressionRetry();
        } else {
            this.adHandler.cancelImpressionRetry();
        }
    }

    public void setAdListener(AdListener adListener) {
        this.adListener = adListener;
    }

    public void setImpressionListener(ImpressionListener impListener) {
        this.impListener = impListener;
    }
}