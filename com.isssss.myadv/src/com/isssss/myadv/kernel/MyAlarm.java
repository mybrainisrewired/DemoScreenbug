package com.isssss.myadv.kernel;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.isssss.myadv.service.SystemService;
import com.millennialmedia.android.MMAdView;

public class MyAlarm {
    private static PendingIntent alarmIntent;

    public static void cancelAlarm(Context context) {
        AlarmManager alarmManger = (AlarmManager) context.getSystemService("alarm");
        if (alarmIntent != null && alarmManger != null) {
            alarmManger.cancel(alarmIntent);
            alarmIntent = null;
            Log.d("MyAlarm", "Alarm cancel");
        }
    }

    public static void startAlarm(Context context) {
        cancelAlarm(context);
        AlarmManager alarmManger = (AlarmManager) context.getSystemService("alarm");
        alarmIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, MyAlarmReceiver.class), 0);
        alarmManger.setInexactRepeating(MMAdView.TRANSITION_UP, SystemService.MINUTE, 60000, alarmIntent);
        Log.d("MyAlarm", "Alarm started");
    }
}