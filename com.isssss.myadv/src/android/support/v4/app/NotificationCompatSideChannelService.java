package android.support.v4.app;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.INotificationSideChannel.Stub;

public abstract class NotificationCompatSideChannelService extends Service {
    private static final int BUILD_VERSION_CODE_KITKAT_WATCH = 20;

    private class NotificationSideChannelStub extends Stub {
        private NotificationSideChannelStub() {
        }

        public void cancel(String packageName, int id, String tag) throws RemoteException {
            NotificationCompatSideChannelService.this.checkPermission(getCallingUid(), packageName);
            long idToken = clearCallingIdentity();
            NotificationCompatSideChannelService.this.cancel(packageName, id, tag);
            restoreCallingIdentity(idToken);
        }

        public void cancelAll(String packageName) {
            NotificationCompatSideChannelService.this.checkPermission(getCallingUid(), packageName);
            long idToken = clearCallingIdentity();
            NotificationCompatSideChannelService.this.cancelAll(packageName);
            restoreCallingIdentity(idToken);
        }

        public void notify(String packageName, int id, String tag, Notification notification) throws RemoteException {
            NotificationCompatSideChannelService.this.checkPermission(getCallingUid(), packageName);
            long idToken = clearCallingIdentity();
            NotificationCompatSideChannelService.this.notify(packageName, id, tag, notification);
            restoreCallingIdentity(idToken);
        }
    }

    private void checkPermission(int callingUid, String packageName) {
        String[] arr$ = getPackageManager().getPackagesForUid(callingUid);
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            if (!arr$[i$].equals(packageName)) {
                i$++;
            } else {
                return;
            }
        }
        throw new SecurityException("NotificationSideChannelService: Uid " + callingUid + " is not authorized for package " + packageName);
    }

    public abstract void cancel(String str, int i, String str2);

    public abstract void cancelAll(String str);

    public abstract void notify(String str, int i, String str2, Notification notification);

    public IBinder onBind(Intent intent) {
        return (!intent.getAction().equals(NotificationManagerCompat.ACTION_BIND_SIDE_CHANNEL) || VERSION.SDK_INT >= 20) ? null : new NotificationSideChannelStub(null);
    }
}