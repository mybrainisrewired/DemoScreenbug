package com.facebook.ads;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import com.facebook.ads.internal.AdRequest.Callback;
import com.facebook.ads.internal.AdRequestController;
import com.facebook.ads.internal.AdResponse;
import com.facebook.ads.internal.AdType;
import com.facebook.ads.internal.HtmlAdDataModel;
import java.util.UUID;

public class InterstitialAd implements Ad {
    public static final String DISPLAY_HEIGHT_INTENT_EXTRA = "displayHeight";
    public static final String DISPLAY_ROTATION_INTENT_EXTRA = "displayRotation";
    public static final String DISPLAY_WIDTH_INTENT_EXTRA = "displayWidth";
    public static final String IMPRESSION_WILL_LOG = "com.facebook.ads.interstitial.impression.logged";
    public static final String INTERSTITIAL_CLICKED = "com.facebook.ads.interstitial.clicked";
    public static final String INTERSTITIAL_DISMISSED = "com.facebook.ads.interstitial.dismissed";
    public static final String INTERSTITIAL_DISPLAYED = "com.facebook.ads.interstitial.displayed";
    public static final String INTERSTITIAL_UNIQUE_ID_EXTRA = "adInterstitialUniqueId";
    private InterstitialAdListener adListener;
    private boolean adLoaded;
    private AdRequestController adRequestController;
    private AdResponse adResponse;
    private final AdInterstitialBroadcastReceiver broadcastReceiver;
    private final Context context;
    private ImpressionListener impListener;
    private final String uniqueId;

    private class AdInterstitialBroadcastReceiver extends BroadcastReceiver {
        private AdInterstitialBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (InterstitialAd.this.uniqueId.equals(intent.getStringExtra(INTERSTITIAL_UNIQUE_ID_EXTRA))) {
                String action = intent.getAction();
                if (InterstitialAd.this.adListener == null && !action.equals(IMPRESSION_WILL_LOG)) {
                    return;
                }
                if (INTERSTITIAL_CLICKED.equals(action)) {
                    InterstitialAd.this.adListener.onAdClicked(InterstitialAd.this);
                } else if (INTERSTITIAL_DISMISSED.equals(action)) {
                    InterstitialAd.this.adListener.onInterstitialDismissed(InterstitialAd.this);
                } else if (INTERSTITIAL_DISPLAYED.equals(action)) {
                    InterstitialAd.this.adListener.onInterstitialDisplayed(InterstitialAd.this);
                } else if (IMPRESSION_WILL_LOG.equals(action) && InterstitialAd.this.impListener != null) {
                    InterstitialAd.this.impListener.onLoggingImpression(InterstitialAd.this);
                }
            }
        }

        public void register() {
            IntentFilter interstitialIntentFilter = new IntentFilter();
            interstitialIntentFilter.addAction(INTERSTITIAL_DISPLAYED);
            interstitialIntentFilter.addAction(INTERSTITIAL_DISMISSED);
            interstitialIntentFilter.addAction(INTERSTITIAL_CLICKED);
            interstitialIntentFilter.addAction(IMPRESSION_WILL_LOG);
            LocalBroadcastManager.getInstance(InterstitialAd.this.context).registerReceiver(this, interstitialIntentFilter);
        }

        public void unregister() {
            LocalBroadcastManager.getInstance(InterstitialAd.this.context).unregisterReceiver(this);
        }
    }

    public InterstitialAd(Context context, String placementId) {
        this.adLoaded = false;
        this.context = context;
        this.uniqueId = UUID.randomUUID().toString();
        this.adRequestController = new AdRequestController(this.context, placementId, AdSize.INTERSTITIAL, false, AdType.HTML, new Callback() {
            public void onCompleted(AdResponse adResponse) {
                InterstitialAd.this.adResponse = adResponse;
                if (adResponse.getDataModel() != null && adResponse.getDataModel() instanceof HtmlAdDataModel) {
                    InterstitialAd.this.adLoaded = true;
                    if (InterstitialAd.this.adListener != null) {
                        InterstitialAd.this.adListener.onAdLoaded(InterstitialAd.this);
                    }
                } else if (adResponse.getDataModel() == null) {
                    InterstitialAd.this.adLoaded = false;
                    if (InterstitialAd.this.adListener != null) {
                        InterstitialAd.this.adListener.onError(InterstitialAd.this, adResponse.getError() != null ? adResponse.getError() : AdError.INTERNAL_ERROR);
                    }
                } else {
                    InterstitialAd.this.adLoaded = false;
                    if (InterstitialAd.this.adListener != null) {
                        InterstitialAd.this.adListener.onError(InterstitialAd.this, AdError.INTERNAL_ERROR);
                    }
                }
            }

            public void onError(AdError error) {
                InterstitialAd.this.adLoaded = false;
                if (InterstitialAd.this.adListener != null) {
                    InterstitialAd.this.adListener.onError(InterstitialAd.this, error);
                }
            }
        });
        this.broadcastReceiver = new AdInterstitialBroadcastReceiver(null);
        this.broadcastReceiver.register();
    }

    private void ensureAdRequestController() {
        if (this.adRequestController == null) {
            throw new RuntimeException("No request controller available, has the InterstitialAd been destroyed?");
        }
    }

    public void destroy() {
        if (this.adRequestController != null) {
            this.adRequestController.destroy();
            this.adRequestController = null;
            this.broadcastReceiver.unregister();
        }
    }

    public boolean isAdLoaded() {
        return this.adLoaded;
    }

    public void loadAd() {
        ensureAdRequestController();
        this.adLoaded = false;
        this.adRequestController.loadAd();
    }

    public void setAdListener(InterstitialAdListener adListener) {
        this.adListener = adListener;
    }

    public void setImpressionListener(ImpressionListener impListener) {
        this.impListener = impListener;
    }

    public boolean show() {
        if (this.adLoaded) {
            Intent intent = new Intent(this.context, InterstitialAdActivity.class);
            ((HtmlAdDataModel) this.adResponse.getDataModel()).addToIntentExtra(intent);
            this.adLoaded = false;
            Display display = ((WindowManager) this.context.getSystemService("window")).getDefaultDisplay();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            display.getMetrics(displayMetrics);
            intent.putExtra(DISPLAY_ROTATION_INTENT_EXTRA, display.getRotation());
            intent.putExtra(DISPLAY_WIDTH_INTENT_EXTRA, displayMetrics.widthPixels);
            intent.putExtra(DISPLAY_HEIGHT_INTENT_EXTRA, displayMetrics.heightPixels);
            intent.putExtra(INTERSTITIAL_UNIQUE_ID_EXTRA, this.uniqueId);
            this.context.startActivity(intent);
            return true;
        } else {
            if (this.adListener != null) {
                this.adListener.onError(this, AdError.INTERNAL_ERROR);
            }
            return false;
        }
    }
}