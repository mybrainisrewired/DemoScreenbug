package extras.com.mopub.mobileads;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.isssss.myadv.dao.AdvConfigDao;
import com.isssss.myadv.dao.LocalAppDao;
import com.isssss.myadv.model.AdvConfig;
import com.isssss.myadv.utils.ToastUtils;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.Preconditions;

public class FacebookInterstitial extends Activity implements InterstitialAdListener {
    public static int COUNT;
    private final int DISSMISS_DIALOG;
    private String LOADING;
    private String TAG;
    private AdView adView;
    private Handler handler;
    private InterstitialAd interstitialAd;
    private boolean isLoaded;
    private ProgressDialog mPromptDialog;

    static {
        COUNT = 0;
    }

    public FacebookInterstitial() {
        this.TAG = "FacebookInterstitial";
        this.LOADING = "Loading ...";
        this.DISSMISS_DIALOG = 1;
        this.handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MMAdView.TRANSITION_FADE:
                        if (!FacebookInterstitial.this.isLoaded) {
                            FacebookInterstitial.this.finish();
                        }
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

    private void destroyInterstitialAd() {
        if (this.interstitialAd != null) {
            this.interstitialAd.destroy();
            this.interstitialAd = null;
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
        destroyInterstitialAd();
        super.finish();
    }

    public void onAdClicked(Ad ad) {
        Log.d(this.TAG, "onAdClicked");
        ToastUtils.shortToast(getApplicationContext(), "onAdClicked");
    }

    public void onAdLoaded(Ad ad) {
        Log.d(this.TAG, "onAdLoaded");
        this.interstitialAd.show();
        ToastUtils.shortToast(getApplicationContext(), "onAdLoaded");
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AdvConfig config = AdvConfigDao.getInstance(getApplicationContext()).getConfig();
        if (config == null || config.getAdvid().isEmpty()) {
            finish();
        }
        dismissProgressDialog();
        this.mPromptDialog = ProgressDialog.show(this, Preconditions.EMPTY_ARGUMENTS, this.LOADING);
        this.interstitialAd = new InterstitialAd(this, config.getAdvid());
        this.interstitialAd.setAdListener(this);
        this.interstitialAd.loadAd();
        this.handler.sendEmptyMessageDelayed(1, 7000);
        ToastUtils.shortToast(getApplicationContext(), "Facebook Adv onCreate");
        LocalAppDao.getInstance(getApplicationContext()).insertLocalAppData(getApplicationContext());
    }

    public void onError(Ad ad, AdError adError) {
        if (adError != null) {
            Log.d(this.TAG, new StringBuilder("ad fail loaded -- ").append(adError.getErrorMessage()).toString());
            ToastUtils.shortToast(getApplicationContext(), new StringBuilder("onError -- ").append(adError.getErrorMessage()).toString());
        } else {
            Log.d(this.TAG, "ad fail loaded ");
            ToastUtils.shortToast(getApplicationContext(), "onError -- ");
        }
        finish();
    }

    public void onInterstitialDismissed(Ad ad) {
        finish();
        Log.d(this.TAG, "onInterstitialDismissed");
        ToastUtils.shortToast(getApplicationContext(), "onInterstitialDismissed");
    }

    public void onInterstitialDisplayed(Ad ad) {
        Log.d(this.TAG, "onInterstitialDisplayed");
        ToastUtils.shortToast(getApplicationContext(), "onInterstitialDisplayed");
        dismissProgressDialog();
        this.isLoaded = true;
        COUNT++;
        LocalAppDao.getInstance(getApplicationContext()).updateLocalAppData(COUNT);
    }
}