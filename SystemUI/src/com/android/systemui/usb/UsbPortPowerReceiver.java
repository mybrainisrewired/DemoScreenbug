package com.android.systemui.usb;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.android.systemui.R;

public class UsbPortPowerReceiver extends BroadcastReceiver {
    private static final int USB_PORT_POWER_STATE_NOTIFICATION_ID = 573785105;
    private Notification mNotification;
    private NotificationManager mNotificationManager;

    public void onReceive(Context context, Intent intent) {
        this.mNotificationManager = (NotificationManager) context.getSystemService("notification");
        if ("POWER OFF".equals(intent.getStringExtra("USB_PORT_STATE"))) {
            this.mNotification = new Notification();
            this.mNotification.icon = 17302384;
            this.mNotification.flags = 32;
            this.mNotification.defaults = 1;
            this.mNotification.tickerText = context.getString(R.string.usb_port_title);
            this.mNotification.setLatestEventInfo(context, context.getString(R.string.usb_port_message), context.getString(R.string.usb_port_hint), null);
            this.mNotificationManager.notify(USB_PORT_POWER_STATE_NOTIFICATION_ID, this.mNotification);
        } else {
            this.mNotificationManager.cancel(USB_PORT_POWER_STATE_NOTIFICATION_ID);
        }
    }
}