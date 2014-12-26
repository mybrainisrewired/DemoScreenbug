package com.wmt.wmtserver;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.Parcel;
import android.os.ServiceManager;
import android.util.Log;

public final class WmtServer {
    public static final int DEV_STATUS_MOUNTED = 1;
    public static final int DEV_STATUS_NA = 0;
    public static final int DEV_STATUS_READY = 3;
    public static final int DEV_STATUS_SYNCING = 2;
    private static final String TAG = "WmtServerJ";
    public static int WMT_AP_CMD = 0;
    public static int WMT_AP_KILLER = 0;
    public static int WMT_DB_REFRESH = 0;
    public static int WMT_DEVICE_REMOVING = 0;
    public static final int WMT_DEV_INTERNAL = 0;
    public static final String WMT_DEV_LOCAL_STR = "/LocalDisk";
    public static final int WMT_DEV_SDCARD = 1;
    public static final String WMT_DEV_SD_STR = "/sdcard";
    public static final int WMT_DEV_UDISK = 2;
    public static final String WMT_DEV_UDISK_STR = "/udisk";
    public static int WMT_DO_SCREENSHOT_IN_MEM = 0;
    public static int WMT_FS_MOUNT = 0;
    public static int WMT_FS_UMOUNT = 0;
    public static int WMT_GET_DEVICE_STATUS = 0;
    public static int WMT_GET_SYNCING_STATE = 0;
    public static int WMT_IMAGE_CAPTURE = 0;
    public static int WMT_ROTATION_INFO = 0;
    private static final String WMT_SERVER_NAME = "wmt.server";
    private static final String WMT_SERVICE_NAME = "com.wmt.IWmtService";
    public static int WMT_SH_CMD;
    private static IBinder s_binder;

    static class AnonymousClass_1 extends Thread {
        int ret;
        final /* synthetic */ com.wmt.wmtserver.WmtServer.OnFinishCallback val$cb;
        final /* synthetic */ String val$fileName;

        AnonymousClass_1(String str, com.wmt.wmtserver.WmtServer.OnFinishCallback onFinishCallback) {
            this.val$fileName = str;
            this.val$cb = onFinishCallback;
            this.ret = -1;
        }

        public void run() {
            IBinder binder = WmtServer.instance();
            if (binder != null) {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                data.writeInterfaceToken(WMT_SERVICE_NAME);
                data.writeString(this.val$fileName);
                try {
                    binder.transact(WMT_SH_CMD, data, reply, WMT_DEV_INTERNAL);
                    this.ret = reply.readInt();
                    if (this.val$cb != null) {
                        this.val$cb.onFinish(this.ret, WMT_SH_CMD);
                    }
                    data.recycle();
                    reply.recycle();
                } catch (Exception e) {
                    try {
                        Log.e(TAG, "wmtShCmd Transact error", e);
                        s_binder = null;
                        if (this.val$cb != null) {
                            this.val$cb.onFinish(this.ret, WMT_SH_CMD);
                        }
                        data.recycle();
                        reply.recycle();
                    } catch (Throwable th) {
                        if (this.val$cb != null) {
                            this.val$cb.onFinish(this.ret, WMT_SH_CMD);
                        }
                    }
                }
            }
        }
    }

    static class AnonymousClass_2 extends Thread {
        final /* synthetic */ int val$pid;

        AnonymousClass_2(int i) {
            this.val$pid = i;
        }

        public void run() {
            IBinder binder = WmtServer.instance();
            if (binder != null) {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                data.writeInterfaceToken(WMT_SERVICE_NAME);
                data.writeInt(this.val$pid);
                try {
                    binder.transact(WMT_AP_KILLER, data, reply, WMT_DEV_INTERNAL);
                    reply.readInt();
                    data.recycle();
                    reply.recycle();
                } catch (Exception e) {
                    Log.e(TAG, "apKiller Transact error", e);
                    s_binder = null;
                    data.recycle();
                    reply.recycle();
                }
            }
        }
    }

    static class AnonymousClass_3 extends Thread {
        int ret;
        final /* synthetic */ com.wmt.wmtserver.WmtServer.OnFinishCallback val$cb;
        final /* synthetic */ String val$option;
        final /* synthetic */ String val$source;
        final /* synthetic */ String val$target;
        final /* synthetic */ String val$type;

        AnonymousClass_3(String str, String str2, String str3, String str4, com.wmt.wmtserver.WmtServer.OnFinishCallback onFinishCallback) {
            this.val$source = str;
            this.val$target = str2;
            this.val$type = str3;
            this.val$option = str4;
            this.val$cb = onFinishCallback;
            this.ret = -1;
        }

        public void run() {
            IBinder binder = WmtServer.instance();
            if (binder != null) {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                data.writeInterfaceToken(WMT_SERVICE_NAME);
                data.writeString(this.val$source);
                data.writeString(this.val$target);
                data.writeString(this.val$type);
                data.writeString(this.val$option);
                try {
                    binder.transact(WMT_FS_MOUNT, data, reply, WMT_DEV_INTERNAL);
                    this.ret = reply.readInt();
                    if (this.val$cb != null) {
                        this.val$cb.onFinish(this.ret, WMT_FS_MOUNT);
                    }
                    data.recycle();
                    reply.recycle();
                } catch (Exception e) {
                    try {
                        Log.e(TAG, "fsMount Transact error", e);
                        s_binder = null;
                        if (this.val$cb != null) {
                            this.val$cb.onFinish(this.ret, WMT_FS_MOUNT);
                        }
                        data.recycle();
                        reply.recycle();
                    } catch (Throwable th) {
                        if (this.val$cb != null) {
                            this.val$cb.onFinish(this.ret, WMT_FS_MOUNT);
                        }
                    }
                }
            }
        }
    }

    private static class DeathCheck implements DeathRecipient {
        private DeathCheck() {
        }

        public void binderDied() {
            s_binder = null;
        }
    }

    public static interface OnFinishCallback {
        void onFinish(int i, int i2);
    }

    public static interface StatusListener {
        void onDbStatus(int i, int i2, int i3);
    }

    static {
        WMT_DB_REFRESH = 1;
        WMT_IMAGE_CAPTURE = 2;
        WMT_AP_KILLER = 3;
        WMT_AP_CMD = 4;
        WMT_SH_CMD = 5;
        WMT_GET_SYNCING_STATE = 6;
        WMT_DEVICE_REMOVING = 7;
        WMT_ROTATION_INFO = 8;
        WMT_DO_SCREENSHOT_IN_MEM = 9;
        WMT_GET_DEVICE_STATUS = 10;
        WMT_FS_MOUNT = 11;
        WMT_FS_UMOUNT = 12;
        s_binder = null;
    }

    @Deprecated
    public static int WmtDbRefresh(String dir) {
        return -1;
    }

    public static int apKiller(int pid) {
        new AnonymousClass_2(pid).start();
        return WMT_DEV_INTERNAL;
    }

    @Deprecated
    public static Bitmap doScreenshot(int w, int h) {
        return null;
    }

    public static int fsMount(String source, String target, String type, String option, OnFinishCallback cb) {
        new AnonymousClass_3(source, target, type, option, cb).start();
        return WMT_DEV_INTERNAL;
    }

    public static int fsUnmount(String mountedPoint) {
        IBinder binder = instance();
        if (binder == null) {
            return -1;
        }
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(WMT_SERVICE_NAME);
        data.writeString(mountedPoint);
        try {
            binder.transact(WMT_FS_UMOUNT, data, reply, WMT_DEV_INTERNAL);
            int ret = reply.readInt();
            data.recycle();
            reply.recycle();
            return ret;
        } catch (Exception e) {
            Log.e(TAG, "fsUmount Transact error", e);
            s_binder = null;
            data.recycle();
            reply.recycle();
            return -1;
        }
    }

    @Deprecated
    public static int getDevStatus(int devType) {
        return -1;
    }

    @Deprecated
    public static int imageCapture(Context context, int delay) {
        return -1;
    }

    @Deprecated
    public static int imageCapture(String dir, int sec, long endTime, int chinese) {
        return -1;
    }

    private static IBinder instance() {
        if (s_binder != null) {
            return s_binder;
        }
        try {
            IBinder binder = ServiceManager.getService(WMT_SERVER_NAME);
            if (binder != null) {
                binder.linkToDeath(new DeathCheck(), WMT_DEV_INTERNAL);
                s_binder = binder;
            }
        } catch (Exception e) {
            Log.e(TAG, "WMTServer::instance error", e);
        }
        return s_binder;
    }

    @Deprecated
    public static boolean regSyncListener(StatusListener sl) {
        return false;
    }

    @Deprecated
    public static void unregSyncListener(StatusListener sl) {
    }

    @Deprecated
    public static Parcel wmtGetDbState() {
        return null;
    }

    @Deprecated
    public static int wmtRotationInfo(int display, int currentOrientation, int requestedOrientation, int flags) {
        return -1;
    }

    public static int wmtShCmd(String fileName, OnFinishCallback cb) {
        new AnonymousClass_1(fileName, cb).start();
        return WMT_DEV_INTERNAL;
    }
}