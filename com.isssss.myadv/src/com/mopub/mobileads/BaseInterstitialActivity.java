package com.mopub.mobileads;

import android.app.Activity;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.util.Dips;
import com.mopub.common.util.Drawables;
import com.mopub.mobileads.util.Interstitials;

abstract class BaseInterstitialActivity extends Activity {
    private static final float CLOSE_BUTTON_PADDING = 8.0f;
    private static final float CLOSE_BUTTON_SIZE_DP = 50.0f;
    private long mBroadcastIdentifier;
    private int mButtonPadding;
    private int mButtonSize;
    private ImageView mCloseButton;
    private OnClickListener mCloseOnClickListener;
    private RelativeLayout mLayout;

    enum JavaScriptWebViewCallbacks {
        WEB_VIEW_DID_APPEAR("javascript:webviewDidAppear();"),
        WEB_VIEW_DID_CLOSE("javascript:webviewDidClose();");
        private String mUrl;

        static {
            String str = "javascript:webviewDidAppear();";
            WEB_VIEW_DID_APPEAR = new JavaScriptWebViewCallbacks("WEB_VIEW_DID_APPEAR", 0, "javascript:webviewDidAppear();");
            str = "javascript:webviewDidClose();";
            WEB_VIEW_DID_CLOSE = new JavaScriptWebViewCallbacks("WEB_VIEW_DID_CLOSE", 1, "javascript:webviewDidClose();");
            ENUM$VALUES = new JavaScriptWebViewCallbacks[]{WEB_VIEW_DID_APPEAR, WEB_VIEW_DID_CLOSE};
        }

        private JavaScriptWebViewCallbacks(String url) {
            this.mUrl = url;
        }

        protected String getUrl() {
            return this.mUrl;
        }
    }

    BaseInterstitialActivity() {
    }

    private void createInterstitialCloseButton() {
        this.mCloseButton = new ImageButton(this);
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{-16842919}, Drawables.INTERSTITIAL_CLOSE_BUTTON_NORMAL.decodeImage(this));
        states.addState(new int[]{16842919}, Drawables.INTERSTITIAL_CLOSE_BUTTON_PRESSED.decodeImage(this));
        this.mCloseButton.setImageDrawable(states);
        this.mCloseButton.setBackgroundDrawable(null);
        this.mCloseButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                BaseInterstitialActivity.this.finish();
            }
        });
        LayoutParams buttonLayout = new LayoutParams(this.mButtonSize, this.mButtonSize);
        buttonLayout.addRule(ApiEventType.API_MRAID_EXPAND);
        buttonLayout.setMargins(this.mButtonPadding, 0, this.mButtonPadding, 0);
        this.mLayout.addView(this.mCloseButton, buttonLayout);
    }

    void addCloseEventRegion() {
        int buttonSizePixels = Dips.dipsToIntPixels(CLOSE_BUTTON_SIZE_DP, this);
        LayoutParams layoutParams = new LayoutParams(buttonSizePixels, buttonSizePixels);
        layoutParams.addRule(ApiEventType.API_MRAID_EXPAND);
        layoutParams.addRule(ApiEventType.API_MRAID_USE_CUSTOM_CLOSE);
        Interstitials.addCloseEventRegion(this.mLayout, layoutParams, this.mCloseOnClickListener);
    }

    protected AdConfiguration getAdConfiguration() {
        try {
            return (AdConfiguration) getIntent().getSerializableExtra(AdFetcher.AD_CONFIGURATION_KEY);
        } catch (ClassCastException e) {
            return null;
        }
    }

    public abstract View getAdView();

    long getBroadcastIdentifier() {
        return this.mBroadcastIdentifier;
    }

    protected void hideInterstitialCloseButton() {
        this.mCloseButton.setVisibility(MMAdView.TRANSITION_RANDOM);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().addFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
        this.mButtonSize = Dips.asIntPixels(CLOSE_BUTTON_SIZE_DP, this);
        this.mButtonPadding = Dips.asIntPixels(CLOSE_BUTTON_PADDING, this);
        this.mCloseOnClickListener = new OnClickListener() {
            public void onClick(View view) {
                BaseInterstitialActivity.this.finish();
            }
        };
        this.mLayout = new RelativeLayout(this);
        this.mLayout.addView(getAdView(), new LayoutParams(-1, -1));
        setContentView(this.mLayout);
        AdConfiguration adConfiguration = getAdConfiguration();
        if (adConfiguration != null) {
            this.mBroadcastIdentifier = adConfiguration.getBroadcastIdentifier();
        }
        createInterstitialCloseButton();
    }

    protected void onDestroy() {
        this.mLayout.removeAllViews();
        super.onDestroy();
    }

    protected void showInterstitialCloseButton() {
        this.mCloseButton.setVisibility(0);
    }
}