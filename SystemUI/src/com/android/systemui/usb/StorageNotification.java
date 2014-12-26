package com.android.systemui.usb;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.storage.StorageEventListener;
import android.os.storage.StorageManager;
import android.util.Slog;
import com.android.internal.app.ExternalMediaFormatActivity;
import com.android.systemui.R;

public class StorageNotification extends StorageEventListener {
    private static final boolean POP_UMS_ACTIVITY_ON_CONNECT = true;
    private static final String TAG = "StorageNotification";
    private Handler mAsyncEventHandler;
    private Context mContext;
    private Notification mMediaStorageNotification;
    private StorageManager mStorageManager;
    private boolean mUmsAvailable;
    private Notification mUsbStorageNotification;

    class AnonymousClass_1 implements Runnable {
        final /* synthetic */ boolean val$connected;

        AnonymousClass_1(boolean z) {
            this.val$connected = z;
        }

        public void run() {
            StorageNotification.this.onUsbMassStorageConnectionChangedAsync(this.val$connected);
        }
    }

    class AnonymousClass_2 implements Runnable {
        final /* synthetic */ String val$newState;
        final /* synthetic */ String val$oldState;
        final /* synthetic */ String val$path;

        AnonymousClass_2(String str, String str2, String str3) {
            this.val$path = str;
            this.val$oldState = str2;
            this.val$newState = str3;
        }

        public void run() {
            StorageNotification.this.onStorageStateChangedAsync(this.val$path, this.val$oldState, this.val$newState);
        }
    }

    public StorageNotification(Context context) {
        this.mContext = context;
        this.mStorageManager = (StorageManager) context.getSystemService("storage");
        boolean connected = this.mStorageManager.isUsbMassStorageConnected();
        Slog.d(TAG, String.format("Startup with UMS connection %s (media state %s)", new Object[]{Boolean.valueOf(this.mUmsAvailable), Environment.getExternalStorageState()}));
        HandlerThread thr = new HandlerThread("SystemUI StorageNotification");
        thr.start();
        this.mAsyncEventHandler = new Handler(thr.getLooper());
        onUsbMassStorageConnectionChanged(connected);
    }

    private synchronized boolean getMediaStorageNotificationDismissable() {
        boolean z;
        z = (this.mMediaStorageNotification == null || (this.mMediaStorageNotification.flags & 16) != 16) ? false : POP_UMS_ACTIVITY_ON_CONNECT;
        return z;
    }

    private void onStorageStateChangedAsync(String path, String oldState, String newState) {
        Slog.i(TAG, String.format("Media {%s} state changed from {%s} -> {%s}", new Object[]{path, oldState, newState}));
        Intent intent;
        if (newState.equals("shared")) {
            intent = new Intent();
            intent.setClass(this.mContext, UsbStorageActivity.class);
            setUsbStorageNotification(17040434, 17040435, 17301642, false, POP_UMS_ACTIVITY_ON_CONNECT, PendingIntent.getActivity(this.mContext, 0, intent, 0));
        } else {
            if (newState.equals("checking")) {
                updateUsbMassStorageNotification(false);
            } else {
                String[] list;
                int i;
                if (newState.equals("mounted")) {
                    list = this.mStorageManager.getVolumePaths();
                    if (list != null) {
                        i = 0;
                        while (i < list.length) {
                            if (path.equals(list[i])) {
                                if (path.contains("usb")) {
                                    setMediaStorageNotification(R.string.usb_mounted_title, R.string.usb_mounted_message, 17301675, POP_UMS_ACTIVITY_ON_CONNECT, POP_UMS_ACTIVITY_ON_CONNECT, null);
                                    break;
                                } else {
                                    if (path.contains("extsd")) {
                                        setMediaStorageNotification(R.string.sd_mounted_title, R.string.sd_mounted_message, 17301675, POP_UMS_ACTIVITY_ON_CONNECT, POP_UMS_ACTIVITY_ON_CONNECT, null);
                                    }
                                }
                            } else {
                                i++;
                            }
                        }
                    }
                    updateUsbMassStorageNotification(this.mUmsAvailable);
                } else {
                    if (!newState.equals("unmounted")) {
                        if (newState.equals("nofs")) {
                            intent = new Intent();
                            intent.setClass(this.mContext, ExternalMediaFormatActivity.class);
                            setMediaStorageNotification(17040466, 17040467, 17301627, POP_UMS_ACTIVITY_ON_CONNECT, false, PendingIntent.getActivity(this.mContext, 0, intent, 0));
                            updateUsbMassStorageNotification(this.mUmsAvailable);
                        } else {
                            if (newState.equals("unmountable")) {
                                list = this.mStorageManager.getVolumePaths();
                                if (list != null) {
                                    i = 0;
                                    while (i < list.length) {
                                        if (path.equals(list[i])) {
                                            if (path.contains("extsd")) {
                                                intent = new Intent();
                                                intent.setClass(this.mContext, ExternalMediaFormatActivity.class);
                                                setMediaStorageNotification(17040468, 17040469, 17301627, POP_UMS_ACTIVITY_ON_CONNECT, false, PendingIntent.getActivity(this.mContext, 0, intent, 0));
                                                break;
                                            } else {
                                                if (path.contains("usb")) {
                                                }
                                            }
                                        } else {
                                            i++;
                                        }
                                    }
                                }
                                updateUsbMassStorageNotification(this.mUmsAvailable);
                            } else {
                                if (newState.equals("removed")) {
                                    list = this.mStorageManager.getVolumePaths();
                                    updateUsbMassStorageNotification(false);
                                } else {
                                    if (newState.equals("bad_removal")) {
                                        list = this.mStorageManager.getVolumePaths();
                                        if (list != null) {
                                            i = 0;
                                            while (i < list.length) {
                                                if (path.equals(list[i])) {
                                                    if (path.contains("extsd")) {
                                                        setMediaStorageNotification(17040470, 17040471, 17301642, POP_UMS_ACTIVITY_ON_CONNECT, POP_UMS_ACTIVITY_ON_CONNECT, null);
                                                        break;
                                                    } else {
                                                        if (path.contains("usb")) {
                                                            setMediaStorageNotification(R.string.usb_badremoval_notification_title, R.string.usb_badremoval_notification_message, 17301642, POP_UMS_ACTIVITY_ON_CONNECT, POP_UMS_ACTIVITY_ON_CONNECT, null);
                                                        }
                                                    }
                                                } else {
                                                    i++;
                                                }
                                            }
                                        }
                                        updateUsbMassStorageNotification(false);
                                    } else {
                                        Slog.w(TAG, String.format("Ignoring unknown state {%s}", new Object[]{newState}));
                                    }
                                }
                            }
                        }
                    } else if (this.mStorageManager.isUsbMassStorageEnabled()) {
                        setMediaStorageNotification(0, 0, 0, false, false, null);
                        updateUsbMassStorageNotification(false);
                    } else {
                        if (oldState.equals("shared")) {
                            setMediaStorageNotification(0, 0, 0, false, false, null);
                            updateUsbMassStorageNotification(this.mUmsAvailable);
                        } else {
                            if (Environment.isExternalStorageRemovable()) {
                                setMediaStorageNotification(17040472, 17040473, 17301626, POP_UMS_ACTIVITY_ON_CONNECT, POP_UMS_ACTIVITY_ON_CONNECT, null);
                            } else {
                                setMediaStorageNotification(0, 0, 0, false, false, null);
                            }
                            updateUsbMassStorageNotification(this.mUmsAvailable);
                        }
                    }
                }
            }
        }
    }

    private void onUsbMassStorageConnectionChangedAsync(boolean connected) {
        this.mUmsAvailable = connected;
        String st = Environment.getExternalStorageState();
        Slog.i(TAG, String.format("UMS connection changed to %s (media state %s)", new Object[]{Boolean.valueOf(connected), st}));
        if (connected) {
            if (st.equals("removed") || st.equals("checking")) {
                connected = false;
            }
        }
        updateUsbMassStorageNotification(connected);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void setMediaStorageNotification(int r12_titleId, int r13_messageId, int r14_icon, boolean r15_visible, boolean r16_dismissable, android.app.PendingIntent r17_pi) {
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.usb.StorageNotification.setMediaStorageNotification(int, int, int, boolean, boolean, android.app.PendingIntent):void");
        /*
        r11 = this;
        monitor-enter(r11);
        if (r15 != 0) goto L_0x0009;
    L_0x0003:
        r8 = r11.mMediaStorageNotification;	 Catch:{ all -> 0x007f }
        if (r8 != 0) goto L_0x0009;
    L_0x0007:
        monitor-exit(r11);
        return;
    L_0x0009:
        r8 = r11.mContext;	 Catch:{ all -> 0x007f }
        r9 = "notification";
        r5 = r8.getSystemService(r9);	 Catch:{ all -> 0x007f }
        r5 = (android.app.NotificationManager) r5;	 Catch:{ all -> 0x007f }
        if (r5 == 0) goto L_0x0007;
    L_0x0015:
        r8 = r11.mMediaStorageNotification;	 Catch:{ all -> 0x007f }
        if (r8 == 0) goto L_0x0022;
    L_0x0019:
        if (r15 == 0) goto L_0x0022;
    L_0x001b:
        r8 = r11.mMediaStorageNotification;	 Catch:{ all -> 0x007f }
        r4 = r8.icon;	 Catch:{ all -> 0x007f }
        r5.cancel(r4);	 Catch:{ all -> 0x007f }
    L_0x0022:
        if (r15 == 0) goto L_0x0073;
    L_0x0024:
        r6 = android.content.res.Resources.getSystem();	 Catch:{ all -> 0x007f }
        r7 = 0;
        r3 = 0;
        r7 = r6.getText(r12);	 Catch:{ Exception -> 0x0082 }
        r3 = r6.getText(r13);	 Catch:{ Exception -> 0x0082 }
    L_0x0032:
        r8 = r11.mMediaStorageNotification;	 Catch:{ all -> 0x007f }
        if (r8 != 0) goto L_0x0043;
    L_0x0036:
        r8 = new android.app.Notification;	 Catch:{ all -> 0x007f }
        r8.<init>();	 Catch:{ all -> 0x007f }
        r11.mMediaStorageNotification = r8;	 Catch:{ all -> 0x007f }
        r8 = r11.mMediaStorageNotification;	 Catch:{ all -> 0x007f }
        r9 = 0;
        r8.when = r9;	 Catch:{ all -> 0x007f }
    L_0x0043:
        r8 = r11.mMediaStorageNotification;	 Catch:{ all -> 0x007f }
        r9 = r8.defaults;	 Catch:{ all -> 0x007f }
        r9 = r9 & -2;
        r8.defaults = r9;	 Catch:{ all -> 0x007f }
        if (r16 == 0) goto L_0x0098;
    L_0x004d:
        r8 = r11.mMediaStorageNotification;	 Catch:{ all -> 0x007f }
        r9 = 16;
        r8.flags = r9;	 Catch:{ all -> 0x007f }
    L_0x0053:
        r8 = r11.mMediaStorageNotification;	 Catch:{ all -> 0x007f }
        r8.tickerText = r7;	 Catch:{ all -> 0x007f }
        if (r17 != 0) goto L_0x0066;
    L_0x0059:
        r2 = new android.content.Intent;	 Catch:{ all -> 0x007f }
        r2.<init>();	 Catch:{ all -> 0x007f }
        r8 = r11.mContext;	 Catch:{ all -> 0x007f }
        r9 = 0;
        r10 = 0;
        r17 = android.app.PendingIntent.getBroadcast(r8, r9, r2, r10);	 Catch:{ all -> 0x007f }
    L_0x0066:
        r8 = r11.mMediaStorageNotification;	 Catch:{ all -> 0x007f }
        r8.icon = r14;	 Catch:{ all -> 0x007f }
        r8 = r11.mMediaStorageNotification;	 Catch:{ all -> 0x007f }
        r9 = r11.mContext;	 Catch:{ all -> 0x007f }
        r0 = r17;
        r8.setLatestEventInfo(r9, r7, r3, r0);	 Catch:{ all -> 0x007f }
    L_0x0073:
        r8 = r11.mMediaStorageNotification;	 Catch:{ all -> 0x007f }
        r4 = r8.icon;	 Catch:{ all -> 0x007f }
        if (r15 == 0) goto L_0x009e;
    L_0x0079:
        r8 = r11.mMediaStorageNotification;	 Catch:{ all -> 0x007f }
        r5.notify(r4, r8);	 Catch:{ all -> 0x007f }
        goto L_0x0007;
    L_0x007f:
        r8 = move-exception;
        monitor-exit(r11);
        throw r8;
    L_0x0082:
        r1 = move-exception;
        r8 = r11.mContext;	 Catch:{ all -> 0x007f }
        r8 = r8.getResources();	 Catch:{ all -> 0x007f }
        r7 = r8.getString(r12);	 Catch:{ all -> 0x007f }
        r8 = r11.mContext;	 Catch:{ all -> 0x007f }
        r8 = r8.getResources();	 Catch:{ all -> 0x007f }
        r3 = r8.getString(r13);	 Catch:{ all -> 0x007f }
        goto L_0x0032;
    L_0x0098:
        r8 = r11.mMediaStorageNotification;	 Catch:{ all -> 0x007f }
        r9 = 2;
        r8.flags = r9;	 Catch:{ all -> 0x007f }
        goto L_0x0053;
    L_0x009e:
        r5.cancel(r4);	 Catch:{ all -> 0x007f }
        goto L_0x0007;
        */
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void setUsbStorageNotification(int r13_titleId, int r14_messageId, int r15_icon, boolean r16_sound, boolean r17_visible, android.app.PendingIntent r18_pi) {
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.usb.StorageNotification.setUsbStorageNotification(int, int, int, boolean, boolean, android.app.PendingIntent):void");
        /*
        r12 = this;
        monitor-enter(r12);
        if (r17 != 0) goto L_0x0009;
    L_0x0003:
        r8 = r12.mUsbStorageNotification;	 Catch:{ all -> 0x0088 }
        if (r8 != 0) goto L_0x0009;
    L_0x0007:
        monitor-exit(r12);
        return;
    L_0x0009:
        r8 = r12.mContext;	 Catch:{ all -> 0x0088 }
        r9 = "notification";
        r5 = r8.getSystemService(r9);	 Catch:{ all -> 0x0088 }
        r5 = (android.app.NotificationManager) r5;	 Catch:{ all -> 0x0088 }
        if (r5 == 0) goto L_0x0007;
    L_0x0015:
        if (r17 == 0) goto L_0x007c;
    L_0x0017:
        r6 = android.content.res.Resources.getSystem();	 Catch:{ all -> 0x0088 }
        r7 = r6.getText(r13);	 Catch:{ all -> 0x0088 }
        r3 = r6.getText(r14);	 Catch:{ all -> 0x0088 }
        r8 = r12.mUsbStorageNotification;	 Catch:{ all -> 0x0088 }
        if (r8 != 0) goto L_0x0038;
    L_0x0027:
        r8 = new android.app.Notification;	 Catch:{ all -> 0x0088 }
        r8.<init>();	 Catch:{ all -> 0x0088 }
        r12.mUsbStorageNotification = r8;	 Catch:{ all -> 0x0088 }
        r8 = r12.mUsbStorageNotification;	 Catch:{ all -> 0x0088 }
        r8.icon = r15;	 Catch:{ all -> 0x0088 }
        r8 = r12.mUsbStorageNotification;	 Catch:{ all -> 0x0088 }
        r9 = 0;
        r8.when = r9;	 Catch:{ all -> 0x0088 }
    L_0x0038:
        if (r16 == 0) goto L_0x008b;
    L_0x003a:
        r8 = r12.mUsbStorageNotification;	 Catch:{ all -> 0x0088 }
        r9 = r8.defaults;	 Catch:{ all -> 0x0088 }
        r9 = r9 | 1;
        r8.defaults = r9;	 Catch:{ all -> 0x0088 }
    L_0x0042:
        r8 = r12.mUsbStorageNotification;	 Catch:{ all -> 0x0088 }
        r9 = 2;
        r8.flags = r9;	 Catch:{ all -> 0x0088 }
        r8 = r12.mUsbStorageNotification;	 Catch:{ all -> 0x0088 }
        r8.tickerText = r7;	 Catch:{ all -> 0x0088 }
        if (r18 != 0) goto L_0x005a;
    L_0x004d:
        r2 = new android.content.Intent;	 Catch:{ all -> 0x0088 }
        r2.<init>();	 Catch:{ all -> 0x0088 }
        r8 = r12.mContext;	 Catch:{ all -> 0x0088 }
        r9 = 0;
        r10 = 0;
        r18 = android.app.PendingIntent.getBroadcast(r8, r9, r2, r10);	 Catch:{ all -> 0x0088 }
    L_0x005a:
        r8 = r12.mUsbStorageNotification;	 Catch:{ all -> 0x0088 }
        r9 = r12.mContext;	 Catch:{ all -> 0x0088 }
        r0 = r18;
        r8.setLatestEventInfo(r9, r7, r3, r0);	 Catch:{ all -> 0x0088 }
        r8 = 1;
        r9 = r12.mContext;	 Catch:{ all -> 0x0088 }
        r9 = r9.getContentResolver();	 Catch:{ all -> 0x0088 }
        r10 = "adb_enabled";
        r11 = 0;
        r9 = android.provider.Settings.Secure.getInt(r9, r10, r11);	 Catch:{ all -> 0x0088 }
        if (r8 != r9) goto L_0x0094;
    L_0x0073:
        r1 = 1;
    L_0x0074:
        if (r1 != 0) goto L_0x007c;
    L_0x0076:
        r8 = r12.mUsbStorageNotification;	 Catch:{ all -> 0x0088 }
        r0 = r18;
        r8.fullScreenIntent = r0;	 Catch:{ all -> 0x0088 }
    L_0x007c:
        r8 = r12.mUsbStorageNotification;	 Catch:{ all -> 0x0088 }
        r4 = r8.icon;	 Catch:{ all -> 0x0088 }
        if (r17 == 0) goto L_0x0096;
    L_0x0082:
        r8 = r12.mUsbStorageNotification;	 Catch:{ all -> 0x0088 }
        r5.notify(r4, r8);	 Catch:{ all -> 0x0088 }
        goto L_0x0007;
    L_0x0088:
        r8 = move-exception;
        monitor-exit(r12);
        throw r8;
    L_0x008b:
        r8 = r12.mUsbStorageNotification;	 Catch:{ all -> 0x0088 }
        r9 = r8.defaults;	 Catch:{ all -> 0x0088 }
        r9 = r9 & -2;
        r8.defaults = r9;	 Catch:{ all -> 0x0088 }
        goto L_0x0042;
    L_0x0094:
        r1 = 0;
        goto L_0x0074;
    L_0x0096:
        r5.cancel(r4);	 Catch:{ all -> 0x0088 }
        goto L_0x0007;
        */
    }

    boolean atLeastOneDeviceMounted() {
        boolean z = false;
        StorageManager manager = (StorageManager) this.mContext.getSystemService("storage");
        String[] arr$ = manager.getVolumePaths();
        try {
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                if (manager.getVolumeState(arr$[i$]).equals("mounted")) {
                    return POP_UMS_ACTIVITY_ON_CONNECT;
                }
                i$++;
            }
            return z;
        } catch (Exception e) {
            return z;
        }
    }

    public void onStorageStateChanged(String path, String oldState, String newState) {
        this.mAsyncEventHandler.post(new AnonymousClass_2(path, oldState, newState));
    }

    public void onUsbMassStorageConnectionChanged(boolean connected) {
        this.mAsyncEventHandler.post(new AnonymousClass_1(connected));
    }

    void updateUsbMassStorageNotification(boolean available) {
        if (available) {
            Intent intent = new Intent();
            intent.setClass(this.mContext, UsbStorageActivity.class);
            intent.setFlags(268435456);
            setUsbStorageNotification(17040432, 17040433, 17302828, false, POP_UMS_ACTIVITY_ON_CONNECT, PendingIntent.getActivity(this.mContext, 0, intent, 0));
        } else if (!this.mUmsAvailable || !atLeastOneDeviceMounted()) {
            setUsbStorageNotification(0, 0, 0, false, false, null);
        }
    }
}