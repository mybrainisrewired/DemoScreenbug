package com.isssss.myadv.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.isssss.myadv.dao.AdvConfigDao;
import com.isssss.myadv.dao.LocalAppDao;
import com.isssss.myadv.model.AdvConfig;
import com.isssss.myadv.utils.ToastUtils;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.Preconditions;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;
import com.mopub.mobileads.MoPubInterstitial.InterstitialAdListener;

public class MopubInterstitialActivity extends Activity implements InterstitialAdListener {
    public static int COUNT;
    private final int DISSMISS_DIALOG;
    private String LOADING;
    private String TAG;
    private Handler handler;
    private boolean isLoaded;
    private MoPubInterstitial mMoPubInterstitial;
    private ProgressDialog mPromptDialog;

    static {
        COUNT = 0;
    }

    public MopubInterstitialActivity() {
        this.TAG = "MopubInterstitial";
        this.DISSMISS_DIALOG = 1;
        this.LOADING = "Loading ...";
        this.handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MMAdView.TRANSITION_FADE:
                        if (!MopubInterstitialActivity.this.isLoaded) {
                            MopubInterstitialActivity.this.finish();
                        }
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

    private void destroyInterstitial() {
        if (this.mMoPubInterstitial != null) {
            this.mMoPubInterstitial.destroy();
            this.mMoPubInterstitial = null;
        }
    }

    private void dismissProgressDialog() {
        if (this.mPromptDialog != null) {
            if (this.mPromptDialog.isShowing()) {
                this.mPromptDialog.dismiss();
            }
            this.mPromptDialog = null;
        }
    }

    public void finish() {
        dismissProgressDialog();
        destroyInterstitial();
        super.finish();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.isLoaded = false;
        AdvConfig config = AdvConfigDao.getInstance(getApplicationContext()).getConfig();
        if (config == null || config.getAdvid().isEmpty()) {
            finish();
        }
        this.mPromptDialog = ProgressDialog.show(this, Preconditions.EMPTY_ARGUMENTS, this.LOADING);
        this.mMoPubInterstitial = new MoPubInterstitial(this, config.getAdvid());
        this.mMoPubInterstitial.setInterstitialAdListener(this);
        this.mMoPubInterstitial.load();
        LocalAppDao.getInstance(getApplicationContext()).insertLocalAppData(getApplicationContext());
        this.handler.sendEmptyMessageDelayed(1, 7000);
    }

    public void onInterstitialClicked(MoPubInterstitial arg0) {
        Log.d(this.TAG, "onInterstitialClicked");
        ToastUtils.shortToast(getApplicationContext(), "onInterstitialClicked");
    }

    public void onInterstitialDismissed(MoPubInterstitial arg0) {
        Log.d(this.TAG, "onInterstitialDismissed");
        ToastUtils.shortToast(getApplicationContext(), "onInterstitialDismissed");
        finish();
    }

    public void onInterstitialFailed(MoPubInterstitial arg0, MoPubErrorCode errorCode) {
        if (errorCode != null) {
            Log.d(this.TAG, new StringBuilder("Interstitial failed to load ----").append(errorCode.toString()).toString());
            ToastUtils.shortToast(getApplicationContext(), new StringBuilder("Interstitial failed to load ----").append(errorCode.toString()).toString());
        } else {
            Log.d(this.TAG, "onInterstitialFailed ");
            ToastUtils.shortToast(getApplicationContext(), "onInterstitialFailed");
        }
        finish();
    }

    public void onInterstitialLoaded(MoPubInterstitial arg0) {
        Log.d(this.TAG, "onInterstitialLoaded");
        if (this.mMoPubInterstitial.isReady()) {
            this.mMoPubInterstitial.show();
            ToastUtils.shortToast(getApplicationContext(), "onInterstitialLoaded");
        }
    }

    public void onInterstitialShown(MoPubInterstitial arg0) {
        Log.d(this.TAG, "onInterstitialShown");
        ToastUtils.shortToast(getApplicationContext(), "onInterstitialShown");
        this.isLoaded = true;
        dismissProgressDialog();
        COUNT++;
        LocalAppDao.getInstance(getApplicationContext()).updateLocalAppData(COUNT);
    }
}