package com.isssss.myadv.view;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.cast.Cast;
import com.isssss.myadv.dao.AdvConfigDao;
import com.isssss.myadv.dao.LocalAppDao;
import com.isssss.myadv.model.AdvConfig;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.Preconditions;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GoolgeAdvActivity extends Activity {
    public static int COUNT;
    public static boolean isShow;
    public static GoolgeAdvActivity mInstance;
    private InterstitialAd interstitial;
    private boolean isLoaded;
    private String loading;
    private ProgressDialog mPromptDialog;
    private String packageName;
    private MyTimerTask task;
    private Timer timer;

    class MyTimerTask extends TimerTask {
        MyTimerTask() {
        }

        public void run() {
            if (!GoolgeAdvActivity.this.isLoaded) {
                GoolgeAdvActivity.this.finish();
            }
        }
    }

    static {
        mInstance = null;
        isShow = false;
        COUNT = 0;
    }

    public GoolgeAdvActivity() {
        this.loading = "loading ...";
    }

    private void dismissDialog() {
        if (this.mPromptDialog != null) {
            if (this.mPromptDialog.isShowing()) {
                this.mPromptDialog.dismiss();
            }
            this.mPromptDialog = null;
        }
    }

    private List<String> getHomes() {
        List<String> names = new ArrayList();
        PackageManager packageManager = getPackageManager();
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        Iterator it = packageManager.queryIntentActivities(intent, Cast.MAX_MESSAGE_LENGTH).iterator();
        while (it.hasNext()) {
            names.add(((ResolveInfo) it.next()).activityInfo.packageName);
        }
        return names;
    }

    private boolean isHome() {
        return getHomes().contains(((RunningTaskInfo) ((ActivityManager) getSystemService("activity")).getRunningTasks(MMAdView.TRANSITION_UP).get(1)).topActivity.getPackageName());
    }

    private void showAdvertise() {
        AdvConfig config = AdvConfigDao.getInstance(this).getConfig();
        this.interstitial = new InterstitialAd(this);
        this.interstitial.setAdUnitId(config.getAdvid());
        this.interstitial.setAdListener(new AdListener() {
            public void onAdClosed() {
                super.onAdClosed();
                GoolgeAdvActivity.this.finish();
                Log.d("GoolgeAdvActivity", "onAdClosed");
            }

            public void onAdFailedToLoad(int errorCode) {
                GoolgeAdvActivity.this.finish();
                Log.d("GoolgeAdvActivity", "onAdFailedToLoad");
            }

            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                Log.d("GoolgeAdvActivity", "onAdLeftApplication");
            }

            public void onAdLoaded() {
                GoolgeAdvActivity.this.isLoaded = true;
                if (mInstance != null) {
                    GoolgeAdvActivity.this.packageName = ((RunningTaskInfo) ((ActivityManager) GoolgeAdvActivity.this.getSystemService("activity")).getRunningTasks(1).get(0)).topActivity.getPackageName();
                    if (GoolgeAdvActivity.this.packageName.equalsIgnoreCase(GoolgeAdvActivity.this.getPackageName())) {
                        GoolgeAdvActivity.this.interstitial.show();
                        LocalAppDao.getInstance(GoolgeAdvActivity.this.getApplicationContext()).insertLocalAppData(GoolgeAdvActivity.this.getApplicationContext());
                        Log.d("GoolgeAdvActivity", "onAdLoaded");
                    } else {
                        GoolgeAdvActivity.this.finish();
                    }
                }
            }

            public void onAdOpened() {
                super.onAdOpened();
                Log.d("GoolgeAdvActivity", "onAdOpened");
                COUNT++;
                LocalAppDao.getInstance(GoolgeAdvActivity.this.getApplicationContext()).updateLocalAppData(COUNT);
                Log.d("GoolgeAdvActivity", new StringBuilder("shows --").append(COUNT).toString());
            }
        });
        loadInterstitial();
    }

    public void finish() {
        dismissDialog();
        mInstance = null;
        super.finish();
    }

    public void loadInterstitial() {
        this.interstitial.loadAd(new Builder().build());
        if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
        }
        this.timer = new Timer();
        this.timer.schedule(new MyTimerTask(), 7000);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        mInstance = this;
        this.isLoaded = false;
        this.packageName = ((RunningTaskInfo) ((ActivityManager) getSystemService("activity")).getRunningTasks(1).get(0)).topActivity.getPackageName();
        if (!this.packageName.equalsIgnoreCase(getPackageName())) {
            finish();
        } else if (isHome()) {
            finish();
        } else {
            showAdvertise();
            dismissDialog();
            this.mPromptDialog = ProgressDialog.show(mInstance, Preconditions.EMPTY_ARGUMENTS, this.loading);
        }
    }
}