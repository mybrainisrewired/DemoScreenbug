package com.wmt.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.System;
import android.widget.TextView;
import com.wmt.data.LocalAudioAll;
import com.wmt.res.CommRes;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

public class SuListener extends Thread {
    private static final int COUNT_TIME = 2;
    private static final int SHOW_DIALOG = 1;
    private static final String SOCKET_NAME = "su-wmt";
    private static final String SOCKET_NO = "n";
    private static final String SOCKET_YES = "y";
    private final String TAG;
    private Context mContext;
    private TextView mDialogextView;
    Handler mHandler;
    private AlertDialog mMainDevDialog;
    boolean mQuit;
    private InputStream mReceiveStream;
    private LocalSocket mReceiver;
    private OutputStream mSendStream;
    private Drawable mSuIcon;
    private LocalServerSocket mSuLocalServerSocket;
    private String mSuProcessName;
    private int mTime;
    private Timer mTimer;
    private TimerTask mTimerTask;

    public SuListener(Context context) {
        this.TAG = "SuListener";
        this.mMainDevDialog = null;
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SHOW_DIALOG:
                        SuListener.this.showSuConfirmDialog();
                    case COUNT_TIME:
                        SuListener.this.mDialogextView.setText("" + SuListener.this.mTime + CommRes.getResourceString(SuListener.this.mContext, "R.string.su_time_up"));
                        if (SuListener.this.mTime <= 0) {
                            SuListener.this.prohibitSu();
                            SuListener.this.mMainDevDialog.dismiss();
                        }
                    default:
                        break;
                }
            }
        };
        this.mQuit = false;
        this.mContext = context;
        try {
            this.mSuLocalServerSocket = new LocalServerSocket(SOCKET_NAME);
        } catch (Exception e) {
            Exception e2 = e;
            this.mQuit = true;
            e2.printStackTrace();
        }
    }

    static /* synthetic */ int access$110(SuListener x0) {
        int i = x0.mTime;
        x0.mTime = i - 1;
        return i;
    }

    private void allowSu() {
        try {
            this.mSendStream = this.mReceiver.getOutputStream();
            byte[] sentData = SOCKET_YES.getBytes();
            this.mReceiver.setSendBufferSize(sentData.length);
            this.mReceiver.setReceiveBufferSize(sentData.length);
            this.mSendStream.write(sentData);
            this.mSendStream.flush();
            closeResource();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeResource() throws Exception {
        if (this.mTimer != null) {
            this.mTimer.cancel();
        }
        this.mReceiveStream.close();
        this.mSendStream.close();
        this.mReceiver.close();
    }

    private void getPackInfo(int uid) {
        this.mSuProcessName = null;
        this.mSuIcon = null;
        RunningAppProcessInfo processInfo = null;
        Iterator i$ = ((ActivityManager) this.mContext.getSystemService("activity")).getRunningAppProcesses().iterator();
        while (i$.hasNext()) {
            RunningAppProcessInfo info = (RunningAppProcessInfo) i$.next();
            if (info.uid == uid) {
                processInfo = info;
                break;
            }
        }
        if (processInfo != null) {
            try {
                PackageManager pm = this.mContext.getPackageManager();
                PackageInfo p = pm.getPackageInfo(processInfo.pkgList[0], 0);
                this.mSuProcessName = p.applicationInfo.loadLabel(pm).toString();
                this.mSuIcon = p.applicationInfo.loadIcon(pm);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void prohibitSu() {
        try {
            this.mSendStream = this.mReceiver.getOutputStream();
            byte[] sentData = SOCKET_NO.getBytes();
            this.mReceiver.setSendBufferSize(sentData.length);
            this.mReceiver.setReceiveBufferSize(sentData.length);
            this.mSendStream.write(sentData);
            this.mSendStream.flush();
            closeResource();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSuConfirmDialog() {
        this.mDialogextView = new TextView(this.mContext);
        this.mDialogextView.setTextSize(18.0f);
        this.mDialogextView.setGravity(Opcodes.SIPUSH);
        this.mMainDevDialog = new Builder(this.mContext).setTitle(this.mSuProcessName + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + CommRes.getResourceString(this.mContext, "R.string.su_title")).setPositiveButton(CommRes.getResourceString(this.mContext, "R.string.su_ok"), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SuListener.this.allowSu();
                SuListener.this.mMainDevDialog.dismiss();
            }
        }).setNegativeButton(CommRes.getResourceString(this.mContext, "R.string.su_cancel"), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SuListener.this.prohibitSu();
                SuListener.this.mMainDevDialog.dismiss();
            }
        }).setIcon(this.mSuIcon).setView(this.mDialogextView).setCancelable(false).create();
        this.mMainDevDialog.getWindow().setType(2008);
        this.mMainDevDialog.show();
        this.mTimer = new Timer(true);
        this.mTime = 10;
        this.mTimerTask = new TimerTask() {
            public void run() {
                if (SuListener.this.mTime > 0) {
                    SuListener.access$110(SuListener.this);
                }
                SuListener.this.mHandler.sendMessage(SuListener.this.mHandler.obtainMessage(COUNT_TIME));
            }
        };
        this.mTimer.schedule(this.mTimerTask, 1000, 1000);
    }

    public void close(boolean quit) {
        this.mQuit = true;
    }

    public void run() {
        while (true) {
            try {
                this.mReceiver = this.mSuLocalServerSocket.accept();
                this.mReceiveStream = this.mReceiver.getInputStream();
                getPackInfo(Integer.parseInt(new BufferedReader(new InputStreamReader(this.mReceiveStream)).readLine()));
                switch (System.getInt(this.mContext.getContentResolver(), "su_mode", 0)) {
                    case LocalAudioAll.SORT_BY_TITLE:
                        prohibitSu();
                        break;
                    case SHOW_DIALOG:
                        this.mHandler.sendMessage(this.mHandler.obtainMessage(SHOW_DIALOG));
                        break;
                    case COUNT_TIME:
                        allowSu();
                        break;
                    default:
                        prohibitSu();
                        break;
                }
                if (this.mQuit) {
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}