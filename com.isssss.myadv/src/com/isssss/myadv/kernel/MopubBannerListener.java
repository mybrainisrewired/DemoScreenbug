package com.isssss.myadv.kernel;

import android.content.Context;
import com.isssss.myadv.utils.ToastUtils;
import com.mopub.common.Preconditions;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;
import com.mopub.mobileads.MoPubView.BannerAdListener;

public class MopubBannerListener implements BannerAdListener {
    private Context context;

    public MopubBannerListener(Context ctx) {
        this.context = ctx;
    }

    public void onBannerClicked(MoPubView banner) {
        ToastUtils.shortToast(this.context, "onBannerClicked");
    }

    public void onBannerCollapsed(MoPubView banner) {
        ToastUtils.shortToast(this.context, "onBannerCollapsed");
    }

    public void onBannerExpanded(MoPubView banner) {
        ToastUtils.shortToast(this.context, "onBannerExpanded");
    }

    public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {
        ToastUtils.shortToast(this.context, new StringBuilder("Banner failed to load: ").append(errorCode != null ? errorCode.toString() : Preconditions.EMPTY_ARGUMENTS).toString());
    }

    public void onBannerLoaded(MoPubView banner) {
        ToastUtils.shortToast(this.context, "onBannerLoaded");
    }
}