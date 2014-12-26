package com.mopub.nativeads;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.google.android.gms.drive.DriveFile;
import com.isssss.myadv.dao.BannerInfoTable;
import com.mopub.common.DownloadResponse;
import com.mopub.common.HttpClient;
import com.mopub.common.MoPubBrowser;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.IntentUtils;
import com.mopub.common.util.ResponseHeader;
import com.mopub.nativeads.MoPubNative.MoPubNativeEventListener;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NativeResponse {
    private final String mAdUnitId;
    private final Context mContext;
    private boolean mIsClicked;
    private boolean mIsDestroyed;
    private final String mMoPubClickTracker;
    private final Set<String> mMoPubImpressionTrackers;
    private MoPubNativeEventListener mMoPubNativeEventListener;
    private final NativeAdInterface mNativeAd;
    private boolean mRecordedImpression;

    @VisibleForTesting
    class NativeViewClickListener implements OnClickListener {
        NativeViewClickListener() {
        }

        public void onClick(View view) {
            NativeResponse.this.handleClick(view);
        }
    }

    enum Parameter {
        IMPRESSION_TRACKER("imptracker", true),
        CLICK_TRACKER("clktracker", true),
        TITLE(BannerInfoTable.COLUMN_TITLE, false),
        TEXT("text", false),
        MAIN_IMAGE("mainimage", false),
        ICON_IMAGE("iconimage", false),
        CLICK_DESTINATION("clk", false),
        FALLBACK("fallback", false),
        CALL_TO_ACTION("ctatext", false),
        STAR_RATING("starrating", false);
        @VisibleForTesting
        static final Set<String> requiredKeys;
        final String name;
        final boolean required;

        static {
            int i = 0;
            String str = "imptracker";
            IMPRESSION_TRACKER = new Parameter("IMPRESSION_TRACKER", 0, "imptracker", true);
            str = "clktracker";
            CLICK_TRACKER = new Parameter("CLICK_TRACKER", 1, "clktracker", true);
            String str2 = "TITLE";
            str = BannerInfoTable.COLUMN_TITLE;
            TITLE = new Parameter(str2, 2, BannerInfoTable.COLUMN_TITLE, false);
            str = "text";
            TEXT = new Parameter("TEXT", 3, "text", false);
            str = "mainimage";
            MAIN_IMAGE = new Parameter("MAIN_IMAGE", 4, "mainimage", false);
            String str3 = "iconimage";
            ICON_IMAGE = new Parameter("ICON_IMAGE", 5, "iconimage", false);
            str3 = "clk";
            CLICK_DESTINATION = new Parameter("CLICK_DESTINATION", 6, "clk", false);
            str3 = "fallback";
            FALLBACK = new Parameter("FALLBACK", 7, "fallback", false);
            str3 = "ctatext";
            CALL_TO_ACTION = new Parameter("CALL_TO_ACTION", 8, "ctatext", false);
            str3 = "starrating";
            STAR_RATING = new Parameter("STAR_RATING", 9, "starrating", false);
            ENUM$VALUES = new Parameter[]{IMPRESSION_TRACKER, CLICK_TRACKER, TITLE, TEXT, MAIN_IMAGE, ICON_IMAGE, CLICK_DESTINATION, FALLBACK, CALL_TO_ACTION, STAR_RATING};
            requiredKeys = new HashSet();
            Parameter[] values = values();
            int length = values.length;
            while (i < length) {
                Parameter parameter = values[i];
                if (parameter.required) {
                    requiredKeys.add(parameter.name);
                }
                i++;
            }
        }

        private Parameter(String name, boolean required) {
            this.name = name;
            this.required = required;
        }

        static Parameter from(String name) {
            Parameter[] values = values();
            int length = values.length;
            int i = 0;
            while (i < length) {
                Parameter parameter = values[i];
                if (parameter.name.equals(name)) {
                    return parameter;
                }
                i++;
            }
            return null;
        }
    }

    private static class ClickDestinationUrlResolutionListener implements UrlResolutionListener {
        private final Context mContext;
        private final SoftReference<SpinningProgressView> mSpinningProgressView;
        private final Iterator<String> mUrlIterator;

        public ClickDestinationUrlResolutionListener(Context context, Iterator<String> urlIterator, SpinningProgressView spinningProgressView) {
            this.mContext = context.getApplicationContext();
            this.mUrlIterator = urlIterator;
            this.mSpinningProgressView = new SoftReference(spinningProgressView);
        }

        private void removeSpinningProgressView() {
            SpinningProgressView spinningProgressView = (SpinningProgressView) this.mSpinningProgressView.get();
            if (spinningProgressView != null) {
                spinningProgressView.removeFromRoot();
            }
        }

        public void onFailure() {
            MoPubLog.d("Failed to resolve URL for click.");
            removeSpinningProgressView();
        }

        public void onSuccess(String resolvedUrl) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(resolvedUrl));
            intent.addFlags(DriveFile.MODE_READ_ONLY);
            if (IntentUtils.isDeepLink(resolvedUrl) && IntentUtils.deviceCanHandleIntent(this.mContext, intent)) {
                this.mContext.startActivity(intent);
            } else if (this.mUrlIterator.hasNext()) {
                UrlResolutionTask.getResolvedUrl((String) this.mUrlIterator.next(), this);
                return;
            } else {
                MoPubBrowser.open(this.mContext, resolvedUrl);
            }
            removeSpinningProgressView();
        }
    }

    public NativeResponse(Context context, DownloadResponse downloadResponse, String adUnitId, NativeAdInterface nativeAd, MoPubNativeEventListener moPubNativeEventListener) {
        this.mContext = context.getApplicationContext();
        this.mAdUnitId = adUnitId;
        this.mMoPubNativeEventListener = moPubNativeEventListener;
        this.mNativeAd = nativeAd;
        this.mNativeAd.setNativeEventListener(new NativeEventListener() {
            public void onAdClicked() {
                NativeResponse.this.handleClick(null);
            }

            public void onAdImpressed() {
                NativeResponse.this.recordImpression(null);
            }
        });
        this.mMoPubImpressionTrackers = new HashSet();
        this.mMoPubImpressionTrackers.add(downloadResponse.getFirstHeader(ResponseHeader.IMPRESSION_URL));
        this.mMoPubClickTracker = downloadResponse.getFirstHeader(ResponseHeader.CLICKTHROUGH_URL);
    }

    private void loadImageView(String url, ImageView imageView) {
        ImageViewService.loadImageView(url, imageView);
    }

    private void openClickDestinationUrl(View view) {
        if (getClickDestinationUrl() != null) {
            SpinningProgressView spinningProgressView = null;
            if (view != null) {
                spinningProgressView = new SpinningProgressView(this.mContext);
                spinningProgressView.addToRoot(view);
            }
            Iterator<String> urlIterator = Arrays.asList(new String[]{getClickDestinationUrl()}).iterator();
            UrlResolutionTask.getResolvedUrl((String) urlIterator.next(), new ClickDestinationUrlResolutionListener(this.mContext, urlIterator, spinningProgressView));
        }
    }

    private void setOnClickListener(View view, OnClickListener onClickListener) {
        view.setOnClickListener(onClickListener);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int i = 0;
            while (i < viewGroup.getChildCount()) {
                setOnClickListener(viewGroup.getChildAt(i), onClickListener);
                i++;
            }
        }
    }

    public void clear(View view) {
        setOnClickListener(view, null);
        this.mNativeAd.clear(view);
    }

    public void destroy() {
        if (!isDestroyed()) {
            this.mMoPubNativeEventListener = MoPubNative.EMPTY_EVENT_LISTENER;
            this.mNativeAd.destroy();
            this.mIsDestroyed = true;
        }
    }

    public String getAdUnitId() {
        return this.mAdUnitId;
    }

    public String getCallToAction() {
        return this.mNativeAd.getCallToAction();
    }

    public String getClickDestinationUrl() {
        return this.mNativeAd.getClickDestinationUrl();
    }

    public String getClickTracker() {
        return this.mMoPubClickTracker;
    }

    public Object getExtra(String key) {
        return this.mNativeAd.getExtra(key);
    }

    public Map<String, Object> getExtras() {
        return this.mNativeAd.getExtras();
    }

    public String getIconImageUrl() {
        return this.mNativeAd.getIconImageUrl();
    }

    public int getImpressionMinPercentageViewed() {
        return this.mNativeAd.getImpressionMinPercentageViewed();
    }

    public int getImpressionMinTimeViewed() {
        return this.mNativeAd.getImpressionMinTimeViewed();
    }

    public List<String> getImpressionTrackers() {
        Set<String> allImpressionTrackers = new HashSet();
        allImpressionTrackers.addAll(this.mMoPubImpressionTrackers);
        allImpressionTrackers.addAll(this.mNativeAd.getImpressionTrackers());
        return new ArrayList(allImpressionTrackers);
    }

    public String getMainImageUrl() {
        return this.mNativeAd.getMainImageUrl();
    }

    @Deprecated
    @VisibleForTesting
    MoPubNativeEventListener getMoPubNativeEventListener() {
        return this.mMoPubNativeEventListener;
    }

    public boolean getRecordedImpression() {
        return this.mRecordedImpression;
    }

    public Double getStarRating() {
        return this.mNativeAd.getStarRating();
    }

    @Deprecated
    public String getSubtitle() {
        return this.mNativeAd.getText();
    }

    public String getText() {
        return this.mNativeAd.getText();
    }

    public String getTitle() {
        return this.mNativeAd.getTitle();
    }

    public void handleClick(View view) {
        if (!isDestroyed()) {
            if (!isClicked()) {
                HttpClient.makeTrackingHttpRequest(this.mMoPubClickTracker, this.mContext);
            }
            openClickDestinationUrl(view);
            this.mNativeAd.handleClick(view);
            this.mIsClicked = true;
            this.mMoPubNativeEventListener.onNativeClick(view);
        }
    }

    public boolean isClicked() {
        return this.mIsClicked;
    }

    public boolean isDestroyed() {
        return this.mIsDestroyed;
    }

    public boolean isOverridingClickTracker() {
        return this.mNativeAd.isOverridingClickTracker();
    }

    public boolean isOverridingImpressionTracker() {
        return this.mNativeAd.isOverridingImpressionTracker();
    }

    public void loadExtrasImage(String key, ImageView imageView) {
        Object object = getExtra(key);
        if (object != null && object instanceof String) {
            loadImageView((String) object, imageView);
        }
    }

    public void loadIconImage(ImageView imageView) {
        loadImageView(getIconImageUrl(), imageView);
    }

    public void loadMainImage(ImageView imageView) {
        loadImageView(getMainImageUrl(), imageView);
    }

    public void prepare(View view) {
        if (!isDestroyed()) {
            if (!isOverridingClickTracker()) {
                setOnClickListener(view, new NativeViewClickListener());
            }
            this.mNativeAd.prepare(view);
        }
    }

    public void recordImpression(View view) {
        if (!getRecordedImpression() && !isDestroyed()) {
            Iterator it = getImpressionTrackers().iterator();
            while (it.hasNext()) {
                HttpClient.makeTrackingHttpRequest((String) it.next(), this.mContext);
            }
            this.mNativeAd.recordImpression();
            this.mRecordedImpression = true;
            this.mMoPubNativeEventListener.onNativeImpression(view);
        }
    }

    @Deprecated
    @VisibleForTesting
    void setRecordedImpression(boolean recordedImpression) {
        this.mRecordedImpression = recordedImpression;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("\n");
        stringBuilder.append(Parameter.TITLE.name).append(":").append(getTitle()).append("\n");
        stringBuilder.append(Parameter.TEXT.name).append(":").append(getText()).append("\n");
        stringBuilder.append(Parameter.ICON_IMAGE.name).append(":").append(getIconImageUrl()).append("\n");
        stringBuilder.append(Parameter.MAIN_IMAGE.name).append(":").append(getMainImageUrl()).append("\n");
        stringBuilder.append(Parameter.STAR_RATING.name).append(":").append(getStarRating()).append("\n");
        stringBuilder.append(Parameter.IMPRESSION_TRACKER.name).append(":").append(getImpressionTrackers()).append("\n");
        stringBuilder.append(Parameter.CLICK_TRACKER.name).append(":").append(this.mMoPubClickTracker).append("\n");
        stringBuilder.append(Parameter.CLICK_DESTINATION.name).append(":").append(getClickDestinationUrl()).append("\n");
        stringBuilder.append(Parameter.CALL_TO_ACTION.name).append(":").append(getCallToAction()).append("\n");
        stringBuilder.append("recordedImpression").append(":").append(this.mRecordedImpression).append("\n");
        stringBuilder.append("extras").append(":").append(getExtras());
        return stringBuilder.toString();
    }
}