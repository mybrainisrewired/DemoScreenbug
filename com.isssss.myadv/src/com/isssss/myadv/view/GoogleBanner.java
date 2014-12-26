package com.isssss.myadv.view;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.isssss.myadv.dao.BannerConfigDao;
import com.isssss.myadv.kernel.BannerAdListener;
import com.isssss.myadv.kernel.BannerController;
import com.isssss.myadv.model.BannerConfig;

public class GoogleBanner extends Activity {
    private static AdView mAdView;
    private static LayoutInflater mInflater;
    public static GoogleBanner mInstance;
    private static LayoutParams params;
    private static WindowManager windowManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInstance = this;
        BannerConfig config = BannerConfigDao.getInstance(getApplicationContext()).getConfig();
        if (!(mAdView == null || windowManager == null)) {
            BannerController.removeAdView();
        }
        if (config.getAdvId() == null) {
            finish();
        }
        mAdView = new AdView(this);
        mAdView.setAdUnitId(config.getAdvId());
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdListener(new BannerAdListener(getApplicationContext()));
        windowManager = (WindowManager) getApplicationContext().getSystemService("window");
        try {
            BannerController.getInstance(getApplicationContext(), windowManager, mAdView).addAdView();
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Handler().postDelayed(new Runnable() {
            public void run() {
                GoogleBanner.this.finish();
            }
        }, 500);
    }
}