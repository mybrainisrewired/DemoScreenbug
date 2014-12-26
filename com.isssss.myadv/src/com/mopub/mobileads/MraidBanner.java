package com.mopub.mobileads;

import android.content.Context;
import android.net.Uri;
import com.mopub.mobileads.CustomEventBanner.CustomEventBannerListener;
import com.mopub.mobileads.MraidView.MraidListener;
import com.mopub.mobileads.MraidView.ViewState;
import com.mopub.mobileads.factories.MraidViewFactory;
import java.util.Map;

class MraidBanner extends CustomEventBanner {
    private CustomEventBannerListener mBannerListener;
    private MraidView mMraidView;

    MraidBanner() {
    }

    private boolean extrasAreValid(Map<String, String> serverExtras) {
        return serverExtras.containsKey(AdFetcher.HTML_RESPONSE_BODY_KEY);
    }

    private void initMraidListener() {
        this.mMraidView.setMraidListener(new MraidListener() {
            public void onClose(MraidView view, ViewState newViewState) {
                MraidBanner.this.onClose();
            }

            public void onExpand(MraidView view) {
                MraidBanner.this.onExpand();
            }

            public void onFailure(MraidView view) {
                MraidBanner.this.onFail();
            }

            public void onOpen(MraidView view) {
                MraidBanner.this.onOpen();
            }

            public void onReady(MraidView view) {
                MraidBanner.this.onReady();
            }
        });
    }

    private void onClose() {
        this.mBannerListener.onBannerCollapsed();
    }

    private void onExpand() {
        this.mBannerListener.onBannerExpanded();
        this.mBannerListener.onBannerClicked();
    }

    private void onFail() {
        this.mBannerListener.onBannerFailed(MoPubErrorCode.MRAID_LOAD_ERROR);
    }

    private void onOpen() {
        this.mBannerListener.onBannerClicked();
    }

    private void onReady() {
        this.mBannerListener.onBannerLoaded(this.mMraidView);
    }

    private void resetMraidListener() {
        this.mMraidView.setMraidListener(null);
    }

    protected void loadBanner(Context context, CustomEventBannerListener customEventBannerListener, Map<String, Object> localExtras, Map<String, String> serverExtras) {
        this.mBannerListener = customEventBannerListener;
        if (extrasAreValid(serverExtras)) {
            String htmlData = Uri.decode((String) serverExtras.get(AdFetcher.HTML_RESPONSE_BODY_KEY));
            this.mMraidView = MraidViewFactory.create(context, AdConfiguration.extractFromMap(localExtras));
            this.mMraidView.loadHtmlData(htmlData);
            initMraidListener();
        } else {
            this.mBannerListener.onBannerFailed(MoPubErrorCode.MRAID_LOAD_ERROR);
        }
    }

    protected void onInvalidate() {
        if (this.mMraidView != null) {
            resetMraidListener();
            this.mMraidView.destroy();
        }
    }
}