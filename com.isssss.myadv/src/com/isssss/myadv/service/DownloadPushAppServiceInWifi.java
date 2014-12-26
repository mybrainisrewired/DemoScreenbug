package com.isssss.myadv.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import com.google.android.gms.drive.DriveFile;
import com.isssss.myadv.constant.AppConst;
import com.isssss.myadv.kernel.PushAppNotificationReceiever;
import com.isssss.myadv.model.PushAppData;
import com.isssss.myadv.utils.FileUtil;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.Preconditions;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import mobi.vserv.android.ads.R;

public class DownloadPushAppServiceInWifi extends Service {
    private static final int DOWN_ERROR = 0;
    private static final int DOWN_OK = 1;
    private static final int TIMEOUT = 20000;
    private String a_new_message;
    private DownloadThread downloadThread;
    private List<DLPushAppData> mAdvertiseList;
    private Handler mHandler;
    private NotificationManager nNotificationManager;

    class AnonymousClass_1 extends AsyncTask<Void, Void, Bitmap> {
        private Bitmap bitmap;
        private final /* synthetic */ DLPushAppData val$data;

        AnonymousClass_1(DLPushAppData dLPushAppData) {
            this.val$data = dLPushAppData;
        }

        protected Bitmap doInBackground(Void... params) {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(this.val$data.getmIconURL()).openConnection();
                conn.setDoInput(true);
                conn.connect();
                this.bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(DownloadPushAppServiceInWifi.this.getClass().getName(), "\u4e0b\u8f7d\u5e7f\u544aicony\u5f02\u5e38");
            }
            return this.bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            DownloadPushAppServiceInWifi.this.createPushAppNotification(DownloadPushAppServiceInWifi.this.getApplicationContext(), this.val$data, result);
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    public class DownloadThread extends Thread {
        private DLPushAppData mData;

        public DownloadThread(DLPushAppData data) {
            this.mData = data;
        }

        public void run() {
            Message message;
            Bundle bundle;
            int i = DOWN_ERROR;
            try {
                this.mData.setmPath(FileUtil.createFile(DownloadPushAppServiceInWifi.this.getApplicationContext(), AppConst.APP_PUSH_APP_DIR, DownloadPushAppServiceInWifi.this.getApkName(this.mData.getmApkURL())).getPath());
                if (100 == DownloadPushAppServiceInWifi.this.downloadFile(this.mData)) {
                    message = new Message();
                    message.what = 1;
                    bundle = new Bundle();
                    bundle.putSerializable("DLPushAppData", this.mData);
                    message.setData(bundle);
                    DownloadPushAppServiceInWifi.this.mHandler.sendMessage(message);
                } else {
                    message = new Message();
                    message.what = 0;
                    bundle = new Bundle();
                    bundle.putSerializable("DLPushAppData", this.mData);
                    message.setData(bundle);
                    DownloadPushAppServiceInWifi.this.mHandler.sendMessage(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
                message = new Message();
                message.what = i;
                bundle = new Bundle();
                bundle.putSerializable("DLPushAppData", this.mData);
                message.setData(bundle);
                DownloadPushAppServiceInWifi.this.mHandler.sendMessage(message);
            }
        }
    }

    private class MyHandler extends Handler {
        private MyHandler() {
        }

        public void handleMessage(Message msg) {
            DLPushAppData data = (DLPushAppData) msg.getData().getSerializable("DLPushAppData");
            DownloadPushAppServiceInWifi.this.mAdvertiseList.remove(data);
            switch (msg.what) {
                case DOWN_ERROR:
                    File file = new File(data.getmPath());
                    if (file.exists()) {
                        file.delete();
                    }
                    DownloadPushAppServiceInWifi.this.stopSelf();
                case DOWN_OK:
                    if (DownloadPushAppServiceInWifi.this.downloadThread != null && DownloadPushAppServiceInWifi.this.downloadThread.isAlive()) {
                        DownloadPushAppServiceInWifi.this.downloadThread.interrupt();
                        DownloadPushAppServiceInWifi.this.downloadThread = null;
                    }
                    DownloadPushAppServiceInWifi.this.downloadIcon(data);
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private class DLPushAppData extends PushAppData implements Serializable {
        private static final long serialVersionUID = -5322592883010680936L;
        private String mPath;
        private Thread mThread;

        public DLPushAppData(PushAppData data) {
            this.mThread = null;
            this.mPath = Preconditions.EMPTY_ARGUMENTS;
            super.setmApkURL(data.getmApkURL());
            super.setmDescription(data.getmDescription());
            super.setmIconURL(data.getmIconURL());
            super.setmID(data.getmID());
            super.setmOpenType(data.getmOpenType());
            super.setmTitle(data.getmTitle());
        }

        public String getmPath() {
            return this.mPath;
        }

        public void setmPath(String mPath) {
            this.mPath = mPath;
        }
    }

    public DownloadPushAppServiceInWifi() {
        this.a_new_message = "You have a new message ! ";
    }

    private void createPushAppNotification(Context context, DLPushAppData data, Bitmap bitmap) {
        NotificationManager manager = (NotificationManager) context.getSystemService("notification");
        Notification notification = new Builder(context).setContentTitle(data.getmTitle()).setContentText(data.getmDescription()).setLargeIcon(bitmap).setSmallIcon(R.drawable.vserv_green_progress).setTicker(this.a_new_message).setWhen(System.currentTimeMillis()).setDefaults(Encoder.LINE_GROUPS).build();
        notification.flags |= 16;
        Intent intent = new Intent(context, PushAppNotificationReceiever.class);
        Bundle bundle = new Bundle();
        PushAppData pData = new PushAppData();
        pData.setmApkURL(data.getmApkURL());
        pData.setmDescription(data.getmDescription());
        pData.setmIconURL(data.getmIconURL());
        pData.setmID(data.getmID());
        pData.setmOpenType(data.getmOpenType());
        pData.setmTitle(data.getmTitle());
        bundle.putSerializable("advertiseData", pData);
        intent.putExtras(bundle);
        notification.contentIntent = PendingIntent.getBroadcast(context, (int) data.getmID(), intent, DriveFile.MODE_READ_ONLY);
        manager.notify((int) data.getmID(), notification);
        if (this.mAdvertiseList != null && this.mAdvertiseList.size() == 0) {
            stopSelf();
        }
    }

    private void downloadIcon(DLPushAppData data) {
        new AnonymousClass_1(data).execute(new Void[0]);
    }

    private String getApkName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
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
        if (updateFile.exists()) {
            updateFile.delete();
        }
        try {
            updateFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return updateFile;
    }

    public long downloadFile(DLPushAppData data) throws Exception {
        int downloadCount = DOWN_ERROR;
        int updateCount = DOWN_ERROR;
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(data.getmApkURL()).openConnection();
        httpURLConnection.setConnectTimeout(TIMEOUT);
        httpURLConnection.setReadTimeout(TIMEOUT);
        int totalSize = httpURLConnection.getContentLength();
        if (httpURLConnection.getResponseCode() == 404) {
            throw new Exception("fail!");
        }
        InputStream inputStream = httpURLConnection.getInputStream();
        OutputStream outputStream = new FileOutputStream(data.getmPath(), false);
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
        this.mHandler = new MyHandler(null);
        this.mAdvertiseList = new LinkedList();
        this.nNotificationManager = (NotificationManager) getSystemService("notification");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (this.mHandler == null) {
            this.mHandler = new MyHandler(null);
        }
        if (this.mAdvertiseList == null) {
            this.mAdvertiseList = new LinkedList();
        }
        if (this.nNotificationManager == null) {
            this.nNotificationManager = (NotificationManager) getSystemService("notification");
        }
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                DLPushAppData dlData = new DLPushAppData((PushAppData) bundle.getSerializable("push_app_data"));
                this.mAdvertiseList.add(dlData);
                this.downloadThread = new DownloadThread(dlData);
                this.downloadThread.start();
            }
        }
        return MMAdView.TRANSITION_UP;
    }
}