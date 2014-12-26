package com.isssss.myadv.kernel;

import android.content.Context;
import android.util.Log;
import com.google.android.gms.ads.AdListener;
import com.isssss.myadv.utils.SPUtil;
import java.util.Timer;
import java.util.TimerTask;

public class BannerAdListener extends AdListener {
    private Context mContext;
    private Timer timer;

    private class MyTask extends TimerTask {
        private MyTask() {
        }

        public void run() {
            BannerController.removeAdView();
        }
    }

    public BannerAdListener(Context context) {
        this.mContext = context;
    }

    public void onAdClosed() {
        Log.d("BannerAdListener", "onAdClosed");
        BannerController.removeAdView();
    }

    public void onAdFailedToLoad(int errorCode) {
        BannerController.removeAdView();
        Log.d("BannerAdListener", "onAdFailedToLoad");
    }

    public void onAdLeftApplication() {
        Log.d("BannerAdListener", "onAdLeftApplication");
        BannerController.removeAdView();
    }

    public void onAdLoaded() {
        Log.d("BannerAdListener", "onAdLoaded");
        int count = SPUtil.getShowTimes(this.mContext);
        SPUtil.setShowTimes(this.mContext, count + 1);
        Log.d("BannerAdListener", new StringBuilder("SHOW COUNT = ").append(count + 1).toString());
        if (this.timer == null) {
            this.timer = new Timer();
        }
        this.timer.schedule(new MyTask(null), 30000);
    }

    public void onAdOpened() {
        Log.d("BannerAdListener", "onAdOpened");
    }
}