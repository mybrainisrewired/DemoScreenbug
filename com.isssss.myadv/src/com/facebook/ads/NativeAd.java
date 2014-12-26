package com.facebook.ads;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.facebook.ads.internal.AdClientEvent;
import com.facebook.ads.internal.AdClientEventManager;
import com.facebook.ads.internal.AdHandler.ImpressionHelper;
import com.facebook.ads.internal.AdRequest.Callback;
import com.facebook.ads.internal.AdRequestController;
import com.facebook.ads.internal.AdResponse;
import com.facebook.ads.internal.AdType;
import com.facebook.ads.internal.DownloadImageTask;
import com.facebook.ads.internal.NativeAdDataModel;
import com.facebook.ads.internal.NativeAdHandler;
import com.google.android.gms.plus.PlusShare;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.json.JSONObject;

public class NativeAd implements Ad {
    private static final String TAG;
    private static WeakHashMap<View, WeakReference<NativeAd>> viewMapping;
    private NativeAdDataModel adDataModel;
    private AdListener adListener;
    private boolean adLoaded;
    private AdRequestController adRequestController;
    private View adView;
    private List<View> clickListeners;
    private final Context context;
    private EventHandler eventHandler;
    private NativeAdHandler handler;
    private ImpressionListener impListener;
    private volatile boolean loadRequested;
    private int viewabilityThreshold;

    private class EventHandler implements OnClickListener, OnTouchListener {
        private float adPositionX;
        private float adPositionY;
        private int height;
        private boolean touchDataInitialized;
        private int visibleHeight;
        private int visibleWidth;
        private int width;
        private int xCoord;
        private int yCoord;

        private EventHandler() {
        }

        public Map<String, Object> getData() {
            Map<String, Object> touchData = new HashMap();
            touchData.put("clickX", Integer.valueOf(this.xCoord));
            touchData.put("clickY", Integer.valueOf(this.yCoord));
            touchData.put(MMLayout.KEY_WIDTH, Integer.valueOf(this.width));
            touchData.put(MMLayout.KEY_HEIGHT, Integer.valueOf(this.height));
            touchData.put("adPositionX", Float.valueOf(this.adPositionX));
            touchData.put("adPositionY", Float.valueOf(this.adPositionY));
            touchData.put("visibleWidth", Integer.valueOf(this.visibleWidth));
            touchData.put("visibleHeight", Integer.valueOf(this.visibleHeight));
            return touchData;
        }

        public void onClick(View v) {
            if (NativeAd.this.adListener != null) {
                NativeAd.this.adListener.onAdClicked(NativeAd.this);
            }
            if (!this.touchDataInitialized) {
                Log.e("FBAudienceNetworkLog", "No touch data recorded, please ensure touch events reach the ad View by returning false if you intercept the event.");
            }
            NativeAd.this.adDataModel.handleClick(NativeAd.this.context, getData());
        }

        public boolean onTouch(View v, MotionEvent event) {
            if (!(this.touchDataInitialized || event.getAction() != 0 || NativeAd.this.adView == null)) {
                this.width = NativeAd.this.adView.getWidth();
                this.height = NativeAd.this.adView.getHeight();
                int[] adViewLocation = new int[2];
                NativeAd.this.adView.getLocationInWindow(adViewLocation);
                this.adPositionX = (float) adViewLocation[0];
                this.adPositionY = (float) adViewLocation[1];
                Rect visibleRect = new Rect();
                NativeAd.this.adView.getGlobalVisibleRect(visibleRect);
                this.visibleWidth = visibleRect.width();
                this.visibleHeight = visibleRect.height();
                int[] clickViewLocation = new int[2];
                v.getLocationInWindow(clickViewLocation);
                this.xCoord = ((int) event.getX()) + clickViewLocation[0] - adViewLocation[0];
                this.yCoord = ((int) event.getY()) + clickViewLocation[1] - adViewLocation[1];
                this.touchDataInitialized = true;
            }
            return false;
        }
    }

    public static class Image {
        private final int height;
        private final String url;
        private final int width;

        private Image(String url, int width, int height) {
            this.url = url;
            this.width = width;
            this.height = height;
        }

        public static com.facebook.ads.NativeAd.Image fromJSONObject(JSONObject dataObject) {
            if (dataObject == null) {
                return null;
            }
            String url = dataObject.optString(PlusShare.KEY_CALL_TO_ACTION_URL);
            return url != null ? new com.facebook.ads.NativeAd.Image(url, dataObject.optInt(MMLayout.KEY_WIDTH, 0), dataObject.optInt(MMLayout.KEY_HEIGHT, 0)) : null;
        }

        public int getHeight() {
            return this.height;
        }

        public String getUrl() {
            return this.url;
        }

        public int getWidth() {
            return this.width;
        }
    }

    public static class Rating {
        private final double scale;
        private final double value;

        private Rating(double value, double scale) {
            this.value = value;
            this.scale = scale;
        }

        public static com.facebook.ads.NativeAd.Rating fromJSONObject(JSONObject dataObject) {
            if (dataObject == null) {
                return null;
            }
            double value = dataObject.optDouble("value", 0.0d);
            double scale = dataObject.optDouble("scale", 0.0d);
            return (value == 0.0d || scale == 0.0d) ? null : new com.facebook.ads.NativeAd.Rating(value, scale);
        }

        public double getScale() {
            return this.scale;
        }

        public double getValue() {
            return this.value;
        }
    }

    static {
        TAG = NativeAd.class.getSimpleName();
        viewMapping = new WeakHashMap();
    }

    public NativeAd(Context context, String placementId) {
        this.context = context;
        this.clickListeners = new ArrayList();
        this.adRequestController = new AdRequestController(this.context, placementId, AdSize.INTERSTITIAL, false, AdType.NATIVE, new Callback() {
            public void onCompleted(AdResponse adResponse) {
                if (adResponse.getDataModel() == null || adResponse.getDataModel() instanceof NativeAdDataModel) {
                    NativeAd.this.adDataModel = (NativeAdDataModel) adResponse.getDataModel();
                    if (NativeAd.this.adDataModel != null && NativeAd.this.adDataModel.isValid()) {
                        NativeAd.this.adLoaded = true;
                        if (NativeAd.this.adListener != null) {
                            NativeAd.this.adListener.onAdLoaded(NativeAd.this);
                        }
                        NativeAd.this.viewabilityThreshold = adResponse.getViewabilityThreshold();
                    } else if (NativeAd.this.adDataModel == null) {
                        NativeAd.this.adLoaded = false;
                        if (NativeAd.this.adListener != null) {
                            NativeAd.this.adListener.onError(NativeAd.this, adResponse.getError() != null ? adResponse.getError() : AdError.INTERNAL_ERROR);
                        }
                    } else {
                        NativeAd.this.adLoaded = false;
                        NativeAd.this.adDataModel = null;
                        if (NativeAd.this.adListener != null) {
                            NativeAd.this.adListener.onError(NativeAd.this, AdError.MISSING_PROPERTIES);
                        }
                    }
                } else if (NativeAd.this.adListener != null) {
                    NativeAd.this.adListener.onError(NativeAd.this, AdError.INTERNAL_ERROR);
                }
            }

            public void onError(AdError error) {
                NativeAd.this.adLoaded = false;
                if (NativeAd.this.adListener != null) {
                    NativeAd.this.adListener.onError(NativeAd.this, error);
                }
            }
        });
    }

    private void addListener(View v) {
        this.clickListeners.add(v);
        v.setOnClickListener(this.eventHandler);
        v.setOnTouchListener(this.eventHandler);
    }

    private void collectAllSubviews(List<View> subviews, View v) {
        subviews.add(v);
        if (v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;
            int i = 0;
            while (i < vg.getChildCount()) {
                collectAllSubviews(subviews, vg.getChildAt(i));
                i++;
            }
        }
    }

    private void detachListeners() {
        Iterator i$ = this.clickListeners.iterator();
        while (i$.hasNext()) {
            View v = (View) i$.next();
            v.setOnClickListener(null);
            v.setOnTouchListener(null);
        }
        this.clickListeners.clear();
    }

    public static void downloadAndDisplayImage(Image image, ImageView imageView) {
        new DownloadImageTask(imageView).execute(new String[]{image.getUrl()});
    }

    private void ensureAdRequestController() {
        if (this.adRequestController == null) {
            RuntimeException ex = new RuntimeException("No request controller available, has the NativeAd been destroyed?");
            AdClientEventManager.addClientEvent(AdClientEvent.newErrorEvent(ex));
            throw ex;
        }
    }

    private boolean isValidAlpha() {
        return VERSION.SDK_INT < 11 || this.adView.getAlpha() > 0.9f;
    }

    private boolean isViewOnScreen() {
        if (this.adView == null || !this.adLoaded || this.adView.getVisibility() != 0 || this.adView.getParent() == null || !isValidAlpha()) {
            return false;
        }
        Rect visibleRect = new Rect();
        if (!this.adView.getGlobalVisibleRect(visibleRect)) {
            return false;
        }
        double visibleArea = (double) (visibleRect.width() * visibleRect.height());
        double totalArea = (double) (this.adView.getWidth() * this.adView.getHeight());
        return visibleArea >= (((double) this.viewabilityThreshold) * totalArea) / 100.0d;
    }

    public void destroy() {
        if (this.adRequestController != null) {
            this.adRequestController.destroy();
            this.adRequestController = null;
        }
        detachListeners();
        if (this.handler != null) {
            this.handler.cancelImpressionRetry();
            this.handler = null;
        }
        if (this.adView != null) {
            viewMapping.remove(this.adView);
            this.adView = null;
        }
    }

    public String getAdBody() {
        return !this.adLoaded ? null : this.adDataModel.getBody();
    }

    public String getAdCallToAction() {
        return !this.adLoaded ? null : this.adDataModel.getCallToAction();
    }

    public Image getAdCoverImage() {
        return !this.adLoaded ? null : this.adDataModel.getImage();
    }

    public Image getAdIcon() {
        return !this.adLoaded ? null : this.adDataModel.getIcon();
    }

    public String getAdSocialContext() {
        return !this.adLoaded ? null : this.adDataModel.getSocialContext();
    }

    public Rating getAdStarRating() {
        return !this.adLoaded ? null : this.adDataModel.getStarRating();
    }

    public String getAdTitle() {
        return !this.adLoaded ? null : this.adDataModel.getTitle();
    }

    public boolean isAdLoaded() {
        return this.adLoaded;
    }

    public void loadAd() {
        if (this.loadRequested) {
            IllegalStateException ex = new IllegalStateException("Ad already loaded");
            AdClientEventManager.addClientEvent(AdClientEvent.newErrorEvent(ex));
            throw ex;
        } else {
            this.loadRequested = true;
            ensureAdRequestController();
            this.adRequestController.loadAd();
        }
    }

    public void registerViewForInteraction(View view) {
        List<View> clickableViews = new ArrayList();
        collectAllSubviews(clickableViews, view);
        registerViewForInteraction(view, clickableViews);
    }

    public void registerViewForInteraction(View view, List<View> clickableViews) {
        IllegalArgumentException ex;
        if (view == null) {
            ex = new IllegalArgumentException("Must provide a View");
            AdClientEventManager.addClientEvent(AdClientEvent.newErrorEvent(ex));
            throw ex;
        } else if (clickableViews == null || clickableViews.size() == 0) {
            ex = new IllegalArgumentException("Invalid set of clickable views");
            AdClientEventManager.addClientEvent(AdClientEvent.newErrorEvent(ex));
            throw ex;
        } else if (this.adLoaded) {
            if (this.adView != null) {
                Log.w(TAG, "Native Ad was already registered with a View. Auto unregistering and proceeding.");
                unregisterView();
            }
            if (viewMapping.containsKey(view)) {
                Log.w(TAG, "View already registered with a NativeAd. Auto unregistering and proceeding.");
                ((NativeAd) ((WeakReference) viewMapping.get(view)).get()).unregisterView();
            }
            this.eventHandler = new EventHandler(null);
            this.adView = view;
            Iterator i$ = clickableViews.iterator();
            while (i$.hasNext()) {
                addListener((View) i$.next());
            }
            this.handler = new NativeAdHandler(new ImpressionHelper() {
                public void afterImpressionSent() {
                }

                public void onLoggingImpression() {
                    if (NativeAd.this.impListener != null) {
                        NativeAd.this.impListener.onLoggingImpression(NativeAd.this);
                    }
                }

                public boolean shouldSendImpression() {
                    return NativeAd.this.isViewOnScreen();
                }
            }, 1000, this.adDataModel, this.context);
            this.handler.trySendImpression();
            viewMapping.put(view, new WeakReference(this));
        } else {
            Log.e(TAG, "Ad not loaded");
        }
    }

    public void setAdListener(AdListener adListener) {
        this.adListener = adListener;
    }

    public void setImpressionListener(ImpressionListener impListener) {
        this.impListener = impListener;
    }

    public void unregisterView() {
        if (this.adView != null) {
            if (viewMapping.containsKey(this.adView) && ((WeakReference) viewMapping.get(this.adView)).get() == this) {
                viewMapping.remove(this.adView);
                detachListeners();
                this.handler.cancelImpressionRetry();
                this.handler = null;
                this.adView = null;
            } else {
                IllegalStateException ex = new IllegalStateException("View not registered with this NativeAd");
                AdClientEventManager.addClientEvent(AdClientEvent.newErrorEvent(ex));
                throw ex;
            }
        }
    }
}