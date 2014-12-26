package com.clouds.widget;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import com.clouds.object.NotificationInfo;
import java.util.Iterator;
import java.util.List;

public class ShowNotificationManager {
    private static final int NOTIFICATION_FLAG = 6;
    private static final String TAG;

    static {
        TAG = ShowNotificationManager.class.getSimpleName();
    }

    public static void showNotification(Context context, String url, String title, String text, boolean isCheck) {
        NotificationManager nm = (NotificationManager) context.getSystemService("notification");
        Notification n = new Builder(context).setContentIntent(PendingIntent.getActivity(context, 0, new Intent("android.intent.action.VIEW", Uri.parse(url)), 268435456)).setTicker(title).setSmallIcon(17301586).setWhen(System.currentTimeMillis()).setContentTitle(title).setContentText(text).getNotification();
        n.flags = 48;
        n.defaults |= 1;
        nm.notify(NOTIFICATION_FLAG, n);
    }

    public static void showNotificationList(Context context, List<NotificationInfo> notificationInfos, boolean isCheck) {
        if (notificationInfos != null && notificationInfos.size() > 0) {
            Iterator it = notificationInfos.iterator();
            while (it.hasNext()) {
                NotificationInfo notificationInfo = (NotificationInfo) it.next();
                String url = notificationInfo.getLink();
                String title = notificationInfo.getTitle();
                String text = notificationInfo.getText();
                Log.i(TAG, new StringBuilder("notificationInfo: ").append(notificationInfo.toString()).toString());
                showNotification(context, url, title, text, isCheck);
            }
        }
    }
}