package com.isssss.myadv.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.plus.PlusShare;
import com.isssss.myadv.constant.AppConst;
import com.isssss.myadv.dao.BannerInfoTable;
import com.isssss.myadv.utils.FileUtil;
import com.millennialmedia.android.MMAdView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class DownloadPushAppService extends Service {
    private static final int DOWN_ERROR = 0;
    private static final int DOWN_OK = 1;
    private static final int TIMEOUT = 10000;
    private String download_success;
    private String downloaderror;
    private String downloading;
    private List<AdvertiseData> mAdvertiseList;
    private Handler mGlobalHandler;
    private NotificationManager nNotificationManager;
    private String startDown;

    class AnonymousClass_1 implements Runnable {
        private final /* synthetic */ AdvertiseData val$data;

        AnonymousClass_1(AdvertiseData advertiseData) {
            this.val$data = advertiseData;
        }

        public void run() {
            Message message = new Message();
            try {
                this.val$data.setmLocalPath(FileUtil.createFile(DownloadPushAppService.this.getApplicationContext(), AppConst.APP_PUSH_APP_DIR, this.val$data.getmAppName()).getPath());
                if (DownloadPushAppService.this.downloadFile(this.val$data) > 0) {
                    message.what = 1;
                    message.arg1 = this.val$data.getmNotifyID();
                    DownloadPushAppService.this.mGlobalHandler.sendMessage(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
                message.what = 0;
                message.arg1 = this.val$data.getmNotifyID();
                DownloadPushAppService.this.mGlobalHandler.sendMessage(message);
            }
        }
    }

    private class AdvertiseData {
        private String mAppName;
        private String mAppUrl;
        private Thread mDLThread;
        private String mLocalPath;
        private Notification mNotification;
        private int mNotifyID;
        private PendingIntent mPendingIntent;
        private String mTitle;

        private AdvertiseData() {
        }

        public String getmAppName() {
            return this.mAppName;
        }

        public String getmAppUrl() {
            return this.mAppUrl;
        }

        public Thread getmDLThread() {
            return this.mDLThread;
        }

        public String getmLocalPath() {
            return this.mLocalPath;
        }

        public Notification getmNotification() {
            return this.mNotification;
        }

        public int getmNotifyID() {
            return this.mNotifyID;
        }

        public PendingIntent getmPendingIntent() {
            return this.mPendingIntent;
        }

        public String getmTitle() {
            return this.mTitle;
        }

        public void setmAppName(String mAppName) {
            this.mAppName = mAppName;
        }

        public void setmAppUrl(String mAppUrl) {
            this.mAppUrl = mAppUrl;
        }

        public void setmDLThread(Thread mDLThread) {
            this.mDLThread = mDLThread;
        }

        public void setmLocalPath(String mLocalPath) {
            this.mLocalPath = mLocalPath;
        }

        public void setmNotification(Notification mNotification) {
            this.mNotification = mNotification;
        }

        public void setmNotifyID(int mNotifyID) {
            this.mNotifyID = mNotifyID;
        }

        public void setmPendingIntent(PendingIntent mPendingIntent) {
            this.mPendingIntent = mPendingIntent;
        }

        public void setmTitle(String mTitle) {
            this.mTitle = mTitle;
        }
    }

    @SuppressLint({"HandlerLeak"})
    private class GlobalHandler extends Handler {
        private GlobalHandler() {
        }

        public void handleMessage(Message msg) {
            AdvertiseData data = DownloadPushAppService.this.getNotify(msg.arg1);
            if (data != null) {
                switch (msg.what) {
                    case DOWN_ERROR:
                        data.getmNotification().setLatestEventInfo(DownloadPushAppService.this.getApplicationContext(), data.getmAppName(), DownloadPushAppService.this.downloaderror, data.getmPendingIntent());
                        DownloadPushAppService.this.nNotificationManager.notify(data.getmNotifyID(), data.getmNotification());
                        DownloadPushAppService.this.stopSelf();
                        return;
                    case DOWN_OK:
                        data.getmNotification().setLatestEventInfo(DownloadPushAppService.this.getApplicationContext(), data.getmAppName(), DownloadPushAppService.this.download_success, data.getmPendingIntent());
                        DownloadPushAppService.this.nNotificationManager.notify(data.getmNotifyID(), data.getmNotification());
                        DownloadPushAppService.this.nNotificationManager.cancel(data.getmNotifyID());
                        Uri uri = Uri.fromFile(new File(data.getmLocalPath()));
                        Intent intent = new Intent("android.intent.action.VIEW");
                        intent.addFlags(DriveFile.MODE_READ_ONLY);
                        intent.setDataAndType(uri, "application/vnd.android.package-archive");
                        DownloadPushAppService.this.getApplicationContext().startActivity(intent);
                        DownloadPushAppService.this.mAdvertiseList.remove(data);
                        if (DownloadPushAppService.this.mAdvertiseList != null && DownloadPushAppService.this.mAdvertiseList.size() == 0) {
                            DownloadPushAppService.this.stopSelf();
                            return;
                        } else {
                            return;
                        }
                }
            }
            super.handleMessage(msg);
        }
    }

    public DownloadPushAppService() {
        this.startDown = "Download\uff1a0%";
        this.downloading = "downloading";
        this.download_success = "Download succeed!";
        this.downloaderror = "Download Failed!";
    }

    private AdvertiseData getNotify(int id) {
        int i = DOWN_ERROR;
        while (i < this.mAdvertiseList.size()) {
            if (((AdvertiseData) this.mAdvertiseList.get(i)).getmNotifyID() == id) {
                return (AdvertiseData) this.mAdvertiseList.get(i);
            }
            i++;
        }
        return null;
    }

    public File createDownloadFile(Context context, String name) {
        File updateDir;
        File updateFile;
        if (FileUtil.isSDCardExist()) {
            updateDir = new File(Environment.getExternalStorageDirectory() + AppConst.APP_PUSH_APP_DIR);
            updateFile = new File(updateDir + "/" + name + ".apk");
        } else {
            updateDir = new File(new StringBuilder(String.valueOf(context.getCacheDir().getAbsolutePath())).append(AppConst.APP_PUSH_APP_DIR).toString());
            updateFile = new File(updateDir + "/" + name + ".apk");
        }
        if (!updateDir.exists()) {
            updateDir.mkdirs();
        }
        if (!updateFile.exists()) {
            try {
                updateFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return updateFile;
    }

    public long downloadFile(AdvertiseData data) throws Exception {
        int downloadCount = DOWN_ERROR;
        int updateCount = DOWN_ERROR;
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(data.getmAppUrl()).openConnection();
        httpURLConnection.setConnectTimeout(TIMEOUT);
        httpURLConnection.setReadTimeout(TIMEOUT);
        int totalSize = httpURLConnection.getContentLength();
        if (httpURLConnection.getResponseCode() == 404) {
            throw new Exception("fail!");
        }
        InputStream inputStream = httpURLConnection.getInputStream();
        OutputStream outputStream = new FileOutputStream(data.getmLocalPath(), false);
        byte[] buffer = new byte[1024];
        while (true) {
            int readsize = inputStream.read(buffer);
            if (readsize == -1) {
                break;
            }
            outputStream.write(buffer, DOWN_ERROR, readsize);
            downloadCount += readsize;
            if (((int) ((((float) downloadCount) / ((float) totalSize)) * 100.0f)) - updateCount >= 1) {
                updateCount = (int) ((((float) downloadCount) / ((float) totalSize)) * 100.0f);
                Log.e("***********", new StringBuilder("\u4e0b\u8f7d\u8fdb\u5ea6\uff1a").append(updateCount).toString());
                data.getmNotification().setLatestEventInfo(this, new StringBuilder(String.valueOf(data.getmTitle())).append(this.downloading).toString(), new StringBuilder(String.valueOf(updateCount)).append("%").toString(), data.getmPendingIntent());
                this.nNotificationManager.notify(data.getmNotifyID(), data.getmNotification());
            }
        }
        if (httpURLConnection != null) {
            httpURLConnection.disconnect();
        }
        inputStream.close();
        outputStream.close();
        return (long) updateCount;
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        this.mGlobalHandler = new GlobalHandler(null);
        this.mAdvertiseList = new LinkedList();
        this.nNotificationManager = (NotificationManager) getSystemService("notification");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (this.mGlobalHandler == null) {
            this.mGlobalHandler = new GlobalHandler(null);
        }
        if (this.mAdvertiseList == null) {
            this.mAdvertiseList = new LinkedList();
        }
        if (this.nNotificationManager == null) {
            this.nNotificationManager = (NotificationManager) getSystemService("notification");
        }
        String appName = intent.getStringExtra("appname");
        String appUrl = intent.getStringExtra(PlusShare.KEY_CALL_TO_ACTION_URL);
        String title = intent.getStringExtra(BannerInfoTable.COLUMN_TITLE);
        if (appName == null || appUrl == null) {
            Log.e("***DownloadAdvertiseService***", new StringBuilder("appName = ").append(appName).append(" appUrl = ").append(appUrl).append("\n").toString());
            return MMAdView.TRANSITION_UP;
        } else {
            AdvertiseData data = new AdvertiseData(null);
            data.setmAppName(appName);
            data.setmAppUrl(appUrl);
            data.setmTitle(title);
            Notification notification = new Builder(getApplicationContext()).setSmallIcon(17301633).setWhen(System.currentTimeMillis()).build();
            PendingIntent pendingIntent = PendingIntent.getActivity(this, DOWN_ERROR, new Intent(), DOWN_ERROR);
            notification.setLatestEventInfo(getApplicationContext(), data.getmAppName(), new StringBuilder(String.valueOf(data.getmTitle())).append(this.startDown).toString(), pendingIntent);
            int notify_id = this.mAdvertiseList.size() + 888;
            this.nNotificationManager.notify(notify_id, notification);
            data.setmNotification(notification);
            data.setmPendingIntent(pendingIntent);
            data.setmNotifyID(notify_id);
            Thread thread = new Thread(new AnonymousClass_1(data));
            thread.start();
            data.setmDLThread(thread);
            this.mAdvertiseList.add(data);
            return MMAdView.TRANSITION_UP;
        }
    }
}