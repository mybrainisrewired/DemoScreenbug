package com.facebook.ads.internal;

import android.content.Context;
import android.webkit.WebView;
import com.facebook.ads.internal.AdHandler.ImpressionHelper;

public class HtmlAdHandler extends AdHandler {
    private volatile boolean adActivated;
    private WebView webView;

    public HtmlAdHandler(WebView webView, ImpressionHelper impressionHelper, long sendImpressionDelay, Context context) {
        super(impressionHelper, sendImpressionDelay, context);
        this.webView = webView;
    }

    public synchronized void activateAd() {
        if (!(this.adActivated || this.adDataModel == null)) {
            HtmlAdDataModel dataModel = (HtmlAdDataModel) this.adDataModel;
            if (!(this.webView == null || StringUtils.isNullOrEmpty(dataModel.getActivationCommand()))) {
                this.webView.loadUrl("javascript:" + dataModel.getActivationCommand());
            }
            scheduleImpressionRetry();
            this.adActivated = true;
        }
    }

    public synchronized void destroy() {
        this.webView = null;
    }

    protected synchronized void sendImpression() {
        HtmlAdDataModel dataModel = (HtmlAdDataModel) this.adDataModel;
        if (!(this.webView == null || StringUtils.isNullOrEmpty(dataModel.getSendImpressionCommand()))) {
            this.webView.loadUrl("javascript:" + dataModel.getSendImpressionCommand());
        }
        if (!StringUtils.isNullOrEmpty(dataModel.getNativeImpressionUrl())) {
            new OpenUrlTask().execute(new String[]{nativeUrl});
            if (this.impressionHelper != null) {
                this.impressionHelper.afterImpressionSent();
            }
        }
    }

    public void setAdDataModel(HtmlAdDataModel adDataModel) {
        super.setAdDataModel(adDataModel);
        this.adActivated = false;
    }
}