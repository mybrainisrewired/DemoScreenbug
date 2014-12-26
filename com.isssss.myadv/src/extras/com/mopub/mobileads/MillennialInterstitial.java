package extras.com.mopub.mobileads;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.isssss.myadv.dao.AdvConfigDao;
import com.isssss.myadv.dao.LocalAppDao;
import com.isssss.myadv.model.AdvConfig;
import com.isssss.myadv.utils.ToastUtils;
import com.millennialmedia.android.MMAd;
import com.millennialmedia.android.MMAdView;
import com.millennialmedia.android.MMException;
import com.millennialmedia.android.MMInterstitial;
import com.millennialmedia.android.MMRequest;
import com.millennialmedia.android.RequestListener;
import com.mopub.common.Preconditions;

public class MillennialInterstitial extends Activity {
    public static int COUNT;
    private final int DISSMISS_DIALOG;
    private String LOADING;
    private String TAG;
    private Context context;
    private Handler handler;
    private MMInterstitial interstitial;
    private boolean isLoaded;
    private ProgressDialog mPromptDialog;

    static {
        COUNT = 0;
    }

    public MillennialInterstitial() {
        this.TAG = "MillennialInterstitial";
        this.LOADING = "Loading ...";
        this.DISSMISS_DIALOG = 1;
        this.handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MMAdView.TRANSITION_FADE:
                        if (!MillennialInterstitial.this.isLoaded) {
                            MillennialInterstitial.this.finish();
                        }
                        break;
                }
                super.handleMessage(msg);
            }
        };
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
        super.finish();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        AdvConfig config = AdvConfigDao.getInstance(getApplicationContext()).getConfig();
        if (config == null || config.getAdvid().isEmpty()) {
            finish();
        }
        dismissProgressDialog();
        this.mPromptDialog = ProgressDialog.show(this, Preconditions.EMPTY_ARGUMENTS, this.LOADING);
        this.interstitial = new MMInterstitial(this);
        this.interstitial.setMMRequest(new MMRequest());
        this.interstitial.setApid(config.getAdvid());
        LocalAppDao.getInstance(getApplicationContext()).insertLocalAppData(getApplicationContext());
        this.interstitial.setListener(new RequestListener() {
            public void MMAdOverlayClosed(MMAd mmAd) {
                Log.d(MillennialInterstitial.this.TAG, "MMAdOverlayClosed");
                ToastUtils.shortToast(MillennialInterstitial.this.context, "MMAdOverlayClosed");
                MillennialInterstitial.this.finish();
            }

            public void MMAdOverlayLaunched(MMAd mmAd) {
                Log.d(MillennialInterstitial.this.TAG, "MMAdOverlayLaunched");
                MillennialInterstitial.this.dismissProgressDialog();
                MillennialInterstitial.this.isLoaded = true;
                COUNT++;
                LocalAppDao.getInstance(MillennialInterstitial.this.getApplicationContext()).updateLocalAppData(COUNT);
                ToastUtils.shortToast(MillennialInterstitial.this.context, "MMAdOverlayLaunched");
            }

            public void MMAdRequestIsCaching(MMAd mmAd) {
                Log.d(MillennialInterstitial.this.TAG, "MMAdRequestIsCaching");
                ToastUtils.shortToast(MillennialInterstitial.this.context, "MMAdRequestIsCaching");
            }

            public void onSingleTap(MMAd mmAd) {
                Log.d(MillennialInterstitial.this.TAG, "onSingleTap");
                ToastUtils.shortToast(MillennialInterstitial.this.context, "onSingleTap");
            }

            public void requestCompleted(MMAd mmAd) {
                MillennialInterstitial.this.interstitial.display();
                Log.d(MillennialInterstitial.this.TAG, "requestCompleted");
                ToastUtils.shortToast(MillennialInterstitial.this.context, "requestCompleted");
            }

            public void requestFailed(MMAd mmAd, MMException e) {
                ToastUtils.shortToast(MillennialInterstitial.this.context, "requestFailed");
                if (e != null) {
                    Log.d(MillennialInterstitial.this.TAG, new StringBuilder("requestFailed  reason --").append(e.getMessage()).toString());
                } else {
                    Log.d(MillennialInterstitial.this.TAG, "requestFailed");
                }
                MillennialInterstitial.this.finish();
            }
        });
        this.interstitial.fetch();
    }
}