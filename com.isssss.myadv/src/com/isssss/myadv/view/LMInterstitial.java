package com.isssss.myadv.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.isssss.myadv.dao.AdvConfigDao;
import com.isssss.myadv.dao.LocalAppDao;
import com.isssss.myadv.utils.ToastUtils;
import com.loopme.LoopMeError;
import com.loopme.LoopMeExitPopup;
import com.loopme.LoopMeInterstitial;
import com.loopme.LoopMeInterstitialListener;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.Preconditions;

public class LMInterstitial extends Activity implements LoopMeInterstitialListener {
    public static int COUNT;
    private static String TAG;
    private String APP_KEY;
    private final int DISSMISS_DIALOG;
    private String LOADING;
    private Handler handler;
    private boolean isLoaded;
    private LoopMeInterstitial mInterstitial;
    private ProgressDialog mPromptDialog;

    static {
        TAG = "LMInterstitialActivity";
        COUNT = 0;
    }

    public LMInterstitial() {
        this.LOADING = "Loading ...";
        this.DISSMISS_DIALOG = 1;
        this.handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MMAdView.TRANSITION_FADE:
                        if (!LMInterstitial.this.isLoaded) {
                            LMInterstitial.this.finish();
                        }
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

    private void dismissDialog() {
        if (this.mPromptDialog != null) {
            if (this.mPromptDialog.isShowing()) {
                this.mPromptDialog.dismiss();
                Log.d(TAG, "Dialog Dismiss");
            }
            this.mPromptDialog = null;
        }
    }

    public void finish() {
        dismissDialog();
        super.finish();
    }

    @SuppressLint({"NewApi"})
    public void onBackPressed() {
        if (this.mInterstitial == null || !this.mInterstitial.isReady()) {
            super.onBackPressed();
        } else {
            LoopMeExitPopup exitPopup = new LoopMeExitPopup(this, this.mInterstitial);
            ToastUtils.shortToast(getApplicationContext(), "onBackPressed");
            exitPopup.show();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.isLoaded = false;
        dismissDialog();
        this.mPromptDialog = ProgressDialog.show(this, Preconditions.EMPTY_ARGUMENTS, this.LOADING);
        this.APP_KEY = AdvConfigDao.getInstance(getApplicationContext()).getConfig().getAdvid();
        this.mInterstitial = new LoopMeInterstitial(this, this.APP_KEY);
        this.mInterstitial.addListener(this);
        this.mInterstitial.load();
        this.handler.sendEmptyMessageDelayed(1, 7000);
        LocalAppDao.getInstance(getApplicationContext()).insertLocalAppData(getApplicationContext());
    }

    public void onLoopMeExitNoClicked(LoopMeInterstitial arg0) {
        Log.d(TAG, "onLoopMeExitNoClicked");
        ToastUtils.shortToast(getApplicationContext(), "onLoopMeExitNoClicked");
    }

    public void onLoopMeExitYesClicked(LoopMeInterstitial arg0) {
        Log.d(TAG, "onLoopMeExitYesClicked");
        dismissDialog();
        ToastUtils.shortToast(getApplicationContext(), "onLoopMeExitYesClicked");
        finish();
    }

    public void onLoopMeInterstitialClicked(LoopMeInterstitial arg0) {
        Log.d(TAG, "onLoopMeInterstitialClicked");
        ToastUtils.shortToast(getApplicationContext(), "onLoopMeInterstitialClicked");
    }

    public void onLoopMeInterstitialExpired(LoopMeInterstitial arg0) {
        Log.d(TAG, "onLoopMeInterstitialExpired");
        dismissDialog();
        ToastUtils.shortToast(getApplicationContext(), "onLoopMeInterstitialExpired");
        finish();
    }

    public void onLoopMeInterstitialHide(LoopMeInterstitial arg0) {
        Log.d(TAG, "onLoopMeInterstitialHide");
        ToastUtils.shortToast(getApplicationContext(), "onLoopMeInterstitialHide");
        dismissDialog();
    }

    public void onLoopMeInterstitialLeaveApp(LoopMeInterstitial arg0) {
        Log.d(TAG, "onLoopMeInterstitialLeaveApp");
        ToastUtils.shortToast(getApplicationContext(), "onLoopMeInterstitialLeaveApp");
        finish();
    }

    public void onLoopMeInterstitialLoadFail(LoopMeInterstitial arg0, LoopMeError arg1) {
        Log.d(TAG, "onLoopMeInterstitialLoadFail");
        ToastUtils.shortToast(getApplicationContext(), "onLoopMeInterstitialLoadFail");
        dismissDialog();
        finish();
    }

    public void onLoopMeInterstitialLoadSuccess(LoopMeInterstitial arg0) {
        Log.d(TAG, "onLoopMeInterstitialLoadSuccess");
        if (this.mInterstitial != null && this.mInterstitial.getAppKey().equalsIgnoreCase(this.APP_KEY)) {
            this.mInterstitial.show();
            ToastUtils.shortToast(getApplicationContext(), "onLoopMeInterstitialLoadSuccess");
        }
    }

    public void onLoopMeInterstitialShow(LoopMeInterstitial arg0) {
        Log.d(TAG, "onLoopMeInterstitialShow");
        dismissDialog();
        this.isLoaded = false;
        COUNT++;
        LocalAppDao.getInstance(getApplicationContext()).updateLocalAppData(COUNT);
        ToastUtils.shortToast(getApplicationContext(), "onLoopMeInterstitialShow");
    }
}