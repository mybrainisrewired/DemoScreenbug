package com.isssss.myadv.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.isssss.myadv.dao.AdvConfigDao;
import com.isssss.myadv.dao.LocalAppDao;
import com.isssss.myadv.model.AdvConfig;
import com.isssss.myadv.utils.ToastUtils;
import com.millennialmedia.android.MMAdView;
import mobi.vserv.android.ads.AdLoadCallback;
import mobi.vserv.android.ads.AdPosition;
import mobi.vserv.android.ads.AdType;
import mobi.vserv.android.ads.VservAd;
import mobi.vserv.android.ads.VservManager;

public class VservAdvActivity extends Activity {
    public static int COUNT;
    private final int DISSMISS_DIALOG;
    private Context context;
    private Handler handler;
    private boolean isLoaded;
    private VservManager manager;

    class AnonymousClass_2 implements AdLoadCallback {
        private VservAd adObject;
        private final /* synthetic */ AdvConfig val$config;

        AnonymousClass_2(AdvConfig advConfig) {
            this.val$config = advConfig;
        }

        public void onLoadFailure() {
            VservAdvActivity.this.finish();
        }

        public void onLoadSuccess(VservAd adObj) {
            this.adObject = adObj;
            if (this.adObject != null) {
                VservAdvActivity.this.isLoaded = true;
                this.adObject.setZoneId(this.val$config.getAdvid());
                this.adObject.overlay(VservAdvActivity.this.context, AdType.OVERLAY);
                COUNT++;
                LocalAppDao.getInstance(VservAdvActivity.this.getApplicationContext()).updateLocalAppData(COUNT);
            } else {
                VservAdvActivity.this.finish();
            }
        }

        public void onNoFill() {
            VservAdvActivity.this.finish();
        }
    }

    static {
        COUNT = 0;
    }

    public VservAdvActivity() {
        this.DISSMISS_DIALOG = 1;
        this.handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MMAdView.TRANSITION_FADE:
                        if (!VservAdvActivity.this.isLoaded) {
                            VservAdvActivity.this.finish();
                        }
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

    public void finish() {
        VservManager.getInstance(this.context).release(this.context);
        ToastUtils.shortToast(getApplicationContext(), "finished");
        super.finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        ToastUtils.shortToast(getApplicationContext(), new StringBuilder("requestCode = ").append(requestCode).toString());
        ToastUtils.shortToast(getApplicationContext(), new StringBuilder("resultCode = ").append(resultCode).toString());
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode != 888) {
            return;
        }
        if (intent == null) {
            finish();
        } else if (intent.hasExtra("showAt") && intent.getStringExtra("showAt").equalsIgnoreCase("end")) {
            VservManager.getInstance(this).release(this);
            finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.isLoaded = false;
        this.context = this;
        AdvConfig config = AdvConfigDao.getInstance(getApplicationContext()).getConfig();
        if (config.getAdvid() == null || config.getAdvid().isEmpty()) {
            finish();
        }
        this.handler.sendEmptyMessageDelayed(1, 7000);
        LocalAppDao.getInstance(getApplicationContext()).insertLocalAppData(getApplicationContext());
        this.manager = VservManager.getInstance(this.context);
        this.manager.setShowAt(AdPosition.IN);
        this.manager.getAd(config.getAdvid(), new AnonymousClass_2(config));
    }
}