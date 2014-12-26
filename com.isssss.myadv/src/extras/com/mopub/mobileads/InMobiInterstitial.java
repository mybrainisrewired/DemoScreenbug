package extras.com.mopub.mobileads;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import com.google.android.gms.cast.CastStatusCodes;
import com.inmobi.commons.InMobi;
import com.inmobi.monetization.IMErrorCode;
import com.inmobi.monetization.IMInterstitial;
import com.inmobi.monetization.IMInterstitial.State;
import com.inmobi.monetization.IMInterstitialListener;
import com.isssss.myadv.dao.AdvConfigDao;
import com.isssss.myadv.dao.LocalAppDao;
import com.isssss.myadv.model.AdvConfig;
import com.isssss.myadv.utils.ToastUtils;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.Preconditions;
import java.util.Map;
import java.util.Timer;

public class InMobiInterstitial extends Activity {
    public static int COUNT;
    private final int DISSMISS_DIALOG;
    private final int FINISH_SELF;
    private final int IS_NEEDED_TURNOFF;
    private String LOADING;
    private final int SHOW_ADV;
    private Handler handler;
    private IMInterstitial interstitial;
    private boolean isLoaded;
    private ProgressDialog mPromptDialog;
    private Timer timer;

    static {
        COUNT = 0;
    }

    public InMobiInterstitial() {
        this.LOADING = "Loading ...";
        this.DISSMISS_DIALOG = 1;
        this.SHOW_ADV = 2;
        this.FINISH_SELF = 3;
        this.IS_NEEDED_TURNOFF = 4;
        this.handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MMAdView.TRANSITION_FADE:
                        InMobiInterstitial.this.dismissDialog();
                        break;
                    case MMAdView.TRANSITION_UP:
                        InMobiInterstitial.this.dismissDialog();
                        break;
                    case MMAdView.TRANSITION_DOWN:
                        InMobiInterstitial.this.dismissDialog();
                        InMobiInterstitial.this.finish();
                        break;
                    case MMAdView.TRANSITION_RANDOM:
                        if (!InMobiInterstitial.this.isLoaded) {
                            InMobiInterstitial.this.dismissDialog();
                            InMobiInterstitial.this.interstitial.stopLoading();
                            InMobiInterstitial.this.finish();
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
            }
            this.mPromptDialog = null;
        }
    }

    public void onAttachedToWindow() {
        getWindow().setType(CastStatusCodes.APPLICATION_NOT_FOUND);
        super.onAttachedToWindow();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        this.isLoaded = false;
        AdvConfig config = AdvConfigDao.getInstance(getApplicationContext()).getConfig();
        InMobi.initialize(this, config.getAdvid());
        this.interstitial = new IMInterstitial(this, config.getAdvid());
        this.interstitial.loadInterstitial();
        Log.d("IMInterActivity", String.valueOf(this.interstitial.getState()));
        dismissDialog();
        this.mPromptDialog = ProgressDialog.show(this, Preconditions.EMPTY_ARGUMENTS, this.LOADING);
        this.handler.sendEmptyMessageDelayed(MMAdView.TRANSITION_RANDOM, 7000);
        this.interstitial.setIMInterstitialListener(new IMInterstitialListener() {
            public void onDismissInterstitialScreen(IMInterstitial mIMInterstitial) {
                Log.d("IMInterActivity", "onDismissInterstitialScreen");
                COUNT++;
                LocalAppDao.getInstance(InMobiInterstitial.this.getApplicationContext()).updateLocalAppData(COUNT);
                Log.d("IMInterActivity", String.valueOf(COUNT));
                ToastUtils.shortToast(InMobiInterstitial.this.getApplicationContext(), "onDismissInterstitialScreen");
                ToastUtils.shortToast(InMobiInterstitial.this.getApplicationContext(), new StringBuilder("Show Count = ").append(COUNT).toString());
                InMobiInterstitial.this.handler.sendEmptyMessage(MMAdView.TRANSITION_DOWN);
            }

            public void onInterstitialFailed(IMInterstitial mIMInterstitial, IMErrorCode mIMErrorCode) {
                Log.d("IMInterActivity", "onInterstitialFailed");
                ToastUtils.shortToast(InMobiInterstitial.this.getApplicationContext(), "onInterstitialFailed");
                InMobiInterstitial.this.handler.sendEmptyMessage(MMAdView.TRANSITION_DOWN);
            }

            public void onInterstitialInteraction(IMInterstitial mIMInterstitial, Map<String, String> map) {
                Log.d("IMInterActivity", "onInterstitialInteraction");
                ToastUtils.shortToast(InMobiInterstitial.this.getApplicationContext(), "onInterstitialInteraction");
            }

            public void onInterstitialLoaded(IMInterstitial mIMInterstitial) {
                Log.d("IMInterActivity", "onInterstitialLoaded");
                ToastUtils.shortToast(InMobiInterstitial.this.getApplicationContext(), "onInterstitialLoaded");
                if (InMobiInterstitial.this.interstitial.getState() == State.READY) {
                    InMobiInterstitial.this.interstitial.show();
                }
            }

            public void onLeaveApplication(IMInterstitial mIMInterstitial) {
                Log.d("IMInterActivity", "onLeaveApplication");
                InMobiInterstitial.this.handler.sendEmptyMessage(MMAdView.TRANSITION_DOWN);
            }

            public void onShowInterstitialScreen(IMInterstitial mIMInterstitial) {
                InMobiInterstitial.this.isLoaded = true;
                Log.d("IMInterActivity", "onShowInterstitialScreen");
                InMobiInterstitial.this.handler.sendEmptyMessage(1);
                ToastUtils.shortToast(InMobiInterstitial.this.getApplicationContext(), "onShowInterstitialScreen");
                LocalAppDao.getInstance(InMobiInterstitial.this.getApplicationContext()).insertLocalAppData(InMobiInterstitial.this.getApplicationContext());
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case MMAdView.TRANSITION_RANDOM:
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    public void onShowAd() {
        if (this.interstitial != null) {
            this.interstitial.show();
        }
        Log.d("IMInterActivity", "SHOW");
    }
}