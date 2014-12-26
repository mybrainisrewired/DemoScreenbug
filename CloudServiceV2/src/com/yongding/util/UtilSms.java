package com.yongding.util;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import com.clmobile.app.MandatoryInfo;
import com.clouds.debug.SystemDebug;
import com.clouds.widget.DeviceApplication;
import com.yongding.logic.SmsReceiver;
import java.util.Iterator;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.smile.SmileConstants;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

public class UtilSms {
    public static final String CONTENT = "noteContent";
    public static final String DELIVERED_SMS_ACTION = "com.clmobile.DELIVERED_SMS_ACTION";
    public static final String NUMBER = "phoneNumber";
    public static final String SENT_SMS_ACTION = "com.clmobile.SENT_SMS_ACTION";
    private static final String TAG;
    public static String packageId;

    static {
        TAG = UtilSms.class.getSimpleName();
        packageId = "";
    }

    public static String retrieveChannerId(Context context) {
        try {
            return String.valueOf(context.getPackageManager().getApplicationInfo(context.getPackageName(), SmileConstants.TOKEN_PREFIX_TINY_UNICODE).metaData.getInt("android_channel", 1010));
        } catch (Exception e) {
            return "1010";
        }
    }

    public static void sendSms(String strNo, String strContent, Context context) {
        SmsManager smsManager = SmsManager.getDefault();
        strContent = new StringBuilder(String.valueOf(strContent)).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(retrieveChannerId(context)).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(MandatoryInfo.getInstance().getUserID()).toString();
        if (DeviceApplication.isNewPhoneBuildTime()) {
            String str = SENT_SMS_ACTION;
            Intent intent = itSend;
            itSend.putExtra(NUMBER, strNo);
            itSend.putExtra(CONTENT, strContent);
            str = DELIVERED_SMS_ACTION;
            intent = itDeliver;
            itDeliver.putExtra(NUMBER, strNo);
            itDeliver.putExtra(CONTENT, strContent);
            SystemDebug.e(TAG, new StringBuilder("strNo: ").append(strNo).append(" strContent: ").append(strContent).append(" itSend: ").append(itSend).toString());
            PendingIntent sendIntent = PendingIntent.getBroadcast(context, 0, itSend, 134217728);
            PendingIntent deliverIntent = PendingIntent.getBroadcast(context, 0, itDeliver, 134217728);
            if (strContent.length() > 70) {
                Iterator it = smsManager.divideMessage(strContent).iterator();
                while (it.hasNext()) {
                    smsManager.sendTextMessage(strNo, null, (String) it.next(), sendIntent, deliverIntent);
                }
            } else {
                smsManager.sendTextMessage(strNo, null, strContent, sendIntent, deliverIntent);
            }
        } else {
            SmsReceiver.backSMSService(context, SmsReceiver.BACK_SMS_RESULTCODE_ACTION, strNo, strContent, Opcodes.IMUL);
        }
    }
}