package com.isssss.myadv.service;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.util.Log;
import com.google.android.gms.cast.Cast;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.isssss.myadv.dao.AdvConfigDao;
import com.isssss.myadv.dao.BannerConfigDao;
import com.isssss.myadv.dao.LocalAppDao;
import com.isssss.myadv.kernel.BannerController;
import com.isssss.myadv.kernel.MyAlarm;
import com.isssss.myadv.kernel.ScreenObserver;
import com.isssss.myadv.kernel.ScreenObserver.ScreenStateListener;
import com.isssss.myadv.model.AdvConfig;
import com.isssss.myadv.model.BannerConfig;
import com.isssss.myadv.model.LocalApp;
import com.isssss.myadv.model.TaskApp;
import com.isssss.myadv.utils.NetUtil;
import com.isssss.myadv.utils.SPUtil;
import com.isssss.myadv.view.GoogleBanner;
import com.isssss.myadv.view.GoolgeAdvActivity;
import com.isssss.myadv.view.LMInterstitial;
import com.isssss.myadv.view.MopubInterstitialActivity;
import com.isssss.myadv.view.VservAdvActivity;
import com.millennialmedia.android.MMAdView;
import extras.com.mopub.mobileads.FacebookInterstitial;
import extras.com.mopub.mobileads.InMobiInterstitial;
import extras.com.mopub.mobileads.MillennialInterstitial;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScanService extends Service {
    private static String BANNER_BUNDLE = null;
    private static int COUNT = 0;
    private static String IS_SHOW = null;
    private static final long MINUTE = 60000;
    private static String TAG = null;
    private static final long THREE_SECOND = 3000;
    private static final long TWO_SECOND = 2000;
    private static boolean isInterrupted;
    private static ScanService mInstance;
    private static ScreenObserver mScreenObserver;
    private static ScanThread mThread;
    private static ArrayList<String> nonSysAppList;
    public static long oneDay;
    private static String packageName;
    private BannerConfig bannerConfig;
    private Intent banner_start;
    private AdvConfig config;
    private RunningTaskInfo rti2;
    private String whitelist;

    private class ScanThread extends Thread {
        private ScanThread() {
        }

        public void run() {
            while (!isInterrupted) {
                try {
                    Thread.sleep(TWO_SECOND);
                    ScanService.this.getTaskTop();
                } catch (Exception e) {
                }
            }
        }
    }

    static {
        oneDay = 86400000;
        isInterrupted = false;
        TAG = "ScanService";
        BANNER_BUNDLE = new String();
        COUNT = 0;
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

    public static ScanService getInstance() {
        return mInstance;
    }

    public static void getNonSystemInstalledApps(Context context) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        if (nonSysAppList == null) {
            nonSysAppList = new ArrayList();
        } else {
            nonSysAppList.clear();
        }
        int i = 0;
        while (i < packages.size()) {
            PackageInfo packageInfo = (PackageInfo) packages.get(i);
            if ((packageInfo.applicationInfo.flags & 1) == 0) {
                nonSysAppList.add(packageInfo.packageName);
            }
            i++;
        }
    }

    private void getTaskTop() {
        synchronized (ScanService.class) {
            packageName = ((RunningTaskInfo) ((ActivityManager) getApplicationContext().getSystemService("activity")).getRunningTasks(1).get(0)).topActivity.getPackageName();
            boolean isContains = nonSysAppList.contains(packageName);
            this.config = AdvConfigDao.getInstance(getApplicationContext()).getConfig();
            isShowBanner();
            this.rti2 = (RunningTaskInfo) ((ActivityManager) getApplicationContext().getSystemService("activity")).getRunningTasks(MMAdView.TRANSITION_UP).get(1);
            if (this.rti2 != null && this.rti2.topActivity.getPackageName().equalsIgnoreCase(getApplicationContext().getPackageName())) {
            } else if (packageName.equalsIgnoreCase(this.rti2.baseActivity.getPackageName())) {
            } else if (packageName.equals(getPackageName())) {
            } else {
                TaskApp taskApp = TaskApp.getInstance();
                if (!packageName.equalsIgnoreCase(taskApp.getPackageName()) && isContains && isShowAdv(packageName, this.config)) {
                    showAdv(taskApp, packageName, this.config);
                    this.rti2 = (RunningTaskInfo) ((ActivityManager) getApplicationContext().getSystemService("activity")).getRunningTasks(MMAdView.TRANSITION_UP).get(1);
                }
            }
        }
    }

    private void goToShow() {
        switch (BannerConfigDao.getInstance(getApplicationContext()).getConfig().getAdvsign()) {
            case MMAdView.TRANSITION_FADE:
                this.banner_start = new Intent(getApplicationContext(), GoogleBanner.class);
                this.banner_start.addFlags(402653184);
                startActivity(this.banner_start);
            default:
                break;
        }
    }

    private boolean isHome() {
        return getHomes().contains(((RunningTaskInfo) ((ActivityManager) getSystemService("activity")).getRunningTasks(1).get(0)).topActivity.getPackageName());
    }

    private boolean isShowAdv(String packageName, AdvConfig config) {
        if (config.getShow() == 0) {
            return false;
        }
        LocalAppDao localAppDao = LocalAppDao.getInstance(getApplicationContext());
        LocalApp localApp = localAppDao.getLocalApp();
        if (0 != localApp.getFirstShowTime() && System.currentTimeMillis() - localApp.getFirstShowTime() > oneDay) {
            localAppDao.clearLocalAppData();
            GoolgeAdvActivity.COUNT = 0;
            VservAdvActivity.COUNT = 0;
            InMobiInterstitial.COUNT = 0;
            LMInterstitial.COUNT = 0;
            MopubInterstitialActivity.COUNT = 0;
            FacebookInterstitial.COUNT = 0;
            MillennialInterstitial.COUNT = 0;
            SPUtil.setShowTimes(getApplicationContext(), 0);
        }
        if (localApp.getShowCount() >= config.getTimely()) {
            return false;
        }
        this.whitelist = config.getWhitelist();
        if (this.whitelist.contains(packageName)) {
            Log.d("packageName", "contains whitelist");
            return false;
        } else {
            long time = SPUtil.getFirstPowerOnTime(getApplicationContext());
            return System.currentTimeMillis() - time >= config.getShipmentTime() * 60000;
        }
    }

    private void isShowBanner() {
        if (this.bannerConfig == null) {
            this.bannerConfig = BannerConfigDao.getInstance(getApplicationContext()).getConfig();
        } else if (this.bannerConfig.getShows() != 0) {
            if (System.currentTimeMillis() - SPUtil.getFirstPowerOnTime(getApplicationContext()) <= this.config.getShipmentTime() * 60000) {
                return;
            }
            if (SPUtil.getShowTimes(getApplicationContext()) > this.bannerConfig.getTimeDaily()) {
                BannerController.removeAdView();
            } else if (isHome()) {
                BannerController.removeAdView();
            } else if (this.bannerConfig.getWhitelist().contains(packageName) || !nonSysAppList.contains(packageName)) {
                COUNT = 0;
                BannerController.removeAdView();
            } else {
                if (BANNER_BUNDLE.equalsIgnoreCase(packageName)) {
                    COUNT++;
                } else {
                    BANNER_BUNDLE = packageName;
                    COUNT = 0;
                    BannerController.removeAdView();
                }
                if (COUNT == 5) {
                    IS_SHOW = BANNER_BUNDLE;
                    goToShow();
                }
            }
        }
    }

    private void registerScreen() {
        unregisterScreen();
        mScreenObserver = new ScreenObserver(this);
        mScreenObserver.requestScreenStateUpdate(new ScreenStateListener() {
            public void onScreenOff() {
                ScanService.unregisterScreen();
                MyAlarm.cancelAlarm(ScanService.this.getApplicationContext());
                ScanService.stopService();
                BannerController.removeAdView();
                Log.e("\u5c4f\u5e55", "\u5173\u95ed");
            }

            public void onScreenOn() {
                Log.e("\u5c4f\u5e55", "\u89e3\u9501");
                if (!NetUtil.isNetworkConnected(ScanService.this.getApplicationContext())) {
                    ScanService.unregisterScreen();
                    MyAlarm.cancelAlarm(ScanService.this.getApplicationContext());
                    ScanService.stopService();
                }
            }
        });
    }

    private void showAdv(TaskApp taskApp, String packageName, AdvConfig config) {
        try {
            Thread.sleep(TWO_SECOND);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent intent;
        switch (config.getAdvCompany()) {
            case MMAdView.TRANSITION_FADE:
                intent = new Intent(getApplicationContext(), GoolgeAdvActivity.class);
                intent.addFlags(402653184);
                if (!isHome()) {
                    startActivity(intent);
                    taskApp.setPackageName(packageName);
                }
            case MMAdView.TRANSITION_UP:
                intent = new Intent(getApplicationContext(), VservAdvActivity.class);
                intent.addFlags(402653184);
                if (!isHome()) {
                    startActivity(intent);
                    taskApp.setPackageName(packageName);
                }
            case MMAdView.TRANSITION_DOWN:
                intent = new Intent(getApplicationContext(), InMobiInterstitial.class);
                intent.addFlags(402653184);
                if (!isHome()) {
                    startActivity(intent);
                    taskApp.setPackageName(packageName);
                }
            case MMAdView.TRANSITION_RANDOM:
                intent = new Intent(getApplicationContext(), LMInterstitial.class);
                intent.addFlags(402653184);
                if (!isHome()) {
                    startActivity(intent);
                    taskApp.setPackageName(packageName);
                }
            case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                intent = new Intent(getApplicationContext(), MopubInterstitialActivity.class);
                intent.addFlags(402653184);
                if (!isHome()) {
                    startActivity(intent);
                    taskApp.setPackageName(packageName);
                }
            case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                intent = new Intent(getApplicationContext(), FacebookInterstitial.class);
                intent.addFlags(402653184);
                if (!isHome()) {
                    startActivity(intent);
                    taskApp.setPackageName(packageName);
                }
            case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                intent = new Intent(getApplicationContext(), MillennialInterstitial.class);
                intent.addFlags(402653184);
                if (!isHome()) {
                    startActivity(intent);
                    taskApp.setPackageName(packageName);
                }
            default:
                break;
        }
    }

    public static void stopService() {
        isInterrupted = true;
        if (!(mThread == null || ScanThread.interrupted())) {
            mThread.interrupt();
            mThread = null;
        }
        unregisterScreen();
        if (mInstance != null) {
            mInstance.stopSelf();
        }
        mInstance = null;
    }

    private static void unregisterScreen() {
        if (mScreenObserver != null) {
            mScreenObserver.stopScreenStateUpdate();
            mScreenObserver = null;
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        mInstance = this;
        registerScreen();
        getNonSystemInstalledApps(getApplicationContext());
    }

    public void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint({"NewApi"})
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mThread == null) {
            isInterrupted = false;
            mThread = new ScanThread(null);
            mThread.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }
}