package com.mopub.mobileads;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.View;
import com.inmobi.re.configs.Initializer;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.IntentUtils;

public class MraidVideoPlayerActivity extends BaseVideoPlayerActivity implements BaseVideoViewControllerListener {
    private BaseVideoViewController mBaseVideoController;
    private long mBroadcastIdentifier;

    private BaseVideoViewController createVideoViewController() throws IllegalStateException {
        String clazz = getIntent().getStringExtra("video_view_class_name");
        if ("vast".equals(clazz)) {
            return new VastVideoViewController(this, getIntent().getExtras(), this.mBroadcastIdentifier, this);
        }
        if (Initializer.PRODUCT_MRAID.equals(clazz)) {
            return new MraidVideoViewController(this, getIntent().getExtras(), this.mBroadcastIdentifier, this);
        }
        throw new IllegalStateException(new StringBuilder("Unsupported video type: ").append(clazz).toString());
    }

    private AdConfiguration getAdConfiguration() {
        try {
            return (AdConfiguration) getIntent().getSerializableExtra(AdFetcher.AD_CONFIGURATION_KEY);
        } catch (ClassCastException e) {
            return null;
        }
    }

    @Deprecated
    BaseVideoViewController getBaseVideoViewController() {
        return this.mBaseVideoController;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.mBaseVideoController.onActivityResult(requestCode, resultCode, data);
    }

    public void onBackPressed() {
        if (this.mBaseVideoController.backButtonEnabled()) {
            super.onBackPressed();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().addFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
        AdConfiguration adConfiguration = getAdConfiguration();
        if (adConfiguration != null) {
            this.mBroadcastIdentifier = adConfiguration.getBroadcastIdentifier();
        } else {
            MoPubLog.d("Unable to obtain broadcast identifier. Video interactions cannot be tracked.");
        }
        try {
            this.mBaseVideoController = createVideoViewController();
            this.mBaseVideoController.onCreate();
        } catch (IllegalStateException e) {
            EventForwardingBroadcastReceiver.broadcastAction(this, this.mBroadcastIdentifier, "com.mopub.action.interstitial.fail");
            finish();
        }
    }

    protected void onDestroy() {
        this.mBaseVideoController.onDestroy();
        super.onDestroy();
    }

    public void onFinish() {
        finish();
    }

    protected void onPause() {
        this.mBaseVideoController.onPause();
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
        this.mBaseVideoController.onResume();
    }

    public void onSetContentView(View view) {
        setContentView(view);
    }

    public void onSetRequestedOrientation(int requestedOrientation) {
        setRequestedOrientation(requestedOrientation);
    }

    public void onStartActivityForResult(Class<? extends Activity> clazz, int requestCode, Bundle extras) {
        if (clazz != null) {
            try {
                startActivityForResult(IntentUtils.getStartActivityIntent(this, clazz, extras), requestCode);
            } catch (ActivityNotFoundException e) {
                MoPubLog.d(new StringBuilder("Activity ").append(clazz.getName()).append(" not found. Did you declare it in your AndroidManifest.xml?").toString());
            }
        }
    }

    @Deprecated
    void setBaseVideoViewController(BaseVideoViewController baseVideoViewController) {
        this.mBaseVideoController = baseVideoViewController;
    }
}