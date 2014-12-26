package com.mopub.mobileads;

import android.content.Context;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import com.mopub.common.util.VersionCode;
import com.mopub.common.util.Views;
import com.mopub.mobileads.util.WebViews;

public class BaseWebView extends WebView {
    protected boolean mIsDestroyed;

    public BaseWebView(Context context) {
        super(context.getApplicationContext());
        enablePlugins(false);
        WebViews.setDisableJSChromeClient(this);
    }

    public void destroy() {
        this.mIsDestroyed = true;
        Views.removeFromParent(this);
        super.destroy();
    }

    protected void enablePlugins(boolean enabled) {
        if (!VersionCode.currentApiLevel().isAtLeast(VersionCode.JELLY_BEAN_MR2)) {
            if (enabled) {
                getSettings().setPluginState(PluginState.ON);
            } else {
                getSettings().setPluginState(PluginState.OFF);
            }
        }
    }

    @Deprecated
    void setIsDestroyed(boolean isDestroyed) {
        this.mIsDestroyed = isDestroyed;
    }
}