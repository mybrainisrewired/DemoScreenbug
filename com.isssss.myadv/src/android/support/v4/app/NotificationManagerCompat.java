package android.support.v4.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.provider.Settings.Secure;
import android.support.v4.app.INotificationSideChannel.Stub;
import android.util.Log;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class NotificationManagerCompat {
    public static final String ACTION_BIND_SIDE_CHANNEL = "android.support.BIND_NOTIFICATION_SIDE_CHANNEL";
    public static final String EXTRA_USE_SIDE_CHANNEL = "android.support.useSideChannel";
    private static final Impl IMPL;
    private static final String SETTING_ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final int SIDE_CHANNEL_BIND_FLAGS;
    private static final int SIDE_CHANNEL_RETRY_BASE_INTERVAL_MS = 1000;
    private static final int SIDE_CHANNEL_RETRY_MAX_COUNT = 6;
    private static final String TAG = "NotifManCompat";
    private static Set<String> sEnabledNotificationListenerPackages;
    private static String sEnabledNotificationListeners;
    private static final Object sEnabledNotificationListenersLock;
    private static final Object sLock;
    private static SideChannelManager sSideChannelManager;
    private final Context mContext;
    private final NotificationManager mNotificationManager;

    static interface Impl {
        void cancelNotification(NotificationManager notificationManager, String str, int i);

        int getSideChannelBindFlags();

        void postNotification(NotificationManager notificationManager, String str, int i, Notification notification);
    }

    private static class ServiceConnectedEvent {
        final ComponentName componentName;
        final IBinder iBinder;

        public ServiceConnectedEvent(ComponentName componentName, IBinder iBinder) {
            this.componentName = componentName;
            this.iBinder = iBinder;
        }
    }

    private static class SideChannelManager implements Callback, ServiceConnection {
        private static final String KEY_BINDER = "binder";
        private static final int MSG_QUEUE_TASK = 0;
        private static final int MSG_RETRY_LISTENER_QUEUE = 3;
        private static final int MSG_SERVICE_CONNECTED = 1;
        private static final int MSG_SERVICE_DISCONNECTED = 2;
        private Set<String> mCachedEnabledPackages;
        private final Context mContext;
        private final Handler mHandler;
        private final HandlerThread mHandlerThread;
        private final Map<ComponentName, ListenerRecord> mRecordMap;

        private static class ListenerRecord {
            public boolean bound;
            public final ComponentName componentName;
            public int retryCount;
            public INotificationSideChannel service;
            public LinkedList<Task> taskQueue;

            public ListenerRecord(ComponentName componentName) {
                this.bound = false;
                this.taskQueue = new LinkedList();
                this.retryCount = 0;
                this.componentName = componentName;
            }
        }

        public SideChannelManager(Context context) {
            this.mRecordMap = new HashMap();
            this.mCachedEnabledPackages = new HashSet();
            this.mContext = context;
            this.mHandlerThread = new HandlerThread("NotificationManagerCompat");
            this.mHandlerThread.start();
            this.mHandler = new Handler(this.mHandlerThread.getLooper(), this);
        }

        private boolean ensureServiceBound(ListenerRecord record) {
            if (record.bound) {
                return true;
            }
            record.bound = this.mContext.bindService(new Intent(ACTION_BIND_SIDE_CHANNEL).setComponent(record.componentName), this, SIDE_CHANNEL_BIND_FLAGS);
            if (record.bound) {
                record.retryCount = 0;
            } else {
                Log.w(TAG, "Unable to bind to listener " + record.componentName);
                this.mContext.unbindService(this);
            }
            return record.bound;
        }

        private void ensureServiceUnbound(ListenerRecord record) {
            if (record.bound) {
                this.mContext.unbindService(this);
                record.bound = false;
            }
            record.service = null;
        }

        private void handleQueueTask(Task task) {
            updateListenerMap();
            Iterator i$ = this.mRecordMap.values().iterator();
            while (i$.hasNext()) {
                ListenerRecord record = (ListenerRecord) i$.next();
                record.taskQueue.add(task);
                processListenerQueue(record);
            }
        }

        private void handleRetryListenerQueue(ComponentName componentName) {
            ListenerRecord record = (ListenerRecord) this.mRecordMap.get(componentName);
            if (record != null) {
                processListenerQueue(record);
            }
        }

        private void handleServiceConnected(ComponentName componentName, IBinder iBinder) {
            ListenerRecord record = (ListenerRecord) this.mRecordMap.get(componentName);
            if (record != null) {
                record.service = Stub.asInterface(iBinder);
                record.retryCount = 0;
                processListenerQueue(record);
            }
        }

        private void handleServiceDisconnected(ComponentName componentName) {
            ListenerRecord record = (ListenerRecord) this.mRecordMap.get(componentName);
            if (record != null) {
                ensureServiceUnbound(record);
            }
        }

        private void processListenerQueue(ListenerRecord record) {
            if (Log.isLoggable(TAG, MSG_RETRY_LISTENER_QUEUE)) {
                Log.d(TAG, "Processing component " + record.componentName + ", " + record.taskQueue.size() + " queued tasks");
            }
            if (!record.taskQueue.isEmpty()) {
                if (!ensureServiceBound(record) || record.service == null) {
                    scheduleListenerRetry(record);
                    return;
                }
                while (true) {
                    Task task = record.taskQueue.peek();
                    if (task != null) {
                        try {
                            if (Log.isLoggable(TAG, MSG_RETRY_LISTENER_QUEUE)) {
                                Log.d(TAG, "Sending task " + task);
                            }
                            task.send(record.service);
                            record.taskQueue.remove();
                        } catch (DeadObjectException e) {
                            if (Log.isLoggable(TAG, MSG_RETRY_LISTENER_QUEUE)) {
                                Log.d(TAG, "Remote service has died: " + record.componentName);
                            }
                        } catch (RemoteException e2) {
                            Log.w(TAG, "RemoteException communicating with " + record.componentName, e2);
                        }
                    }
                    if (!record.taskQueue.isEmpty()) {
                        scheduleListenerRetry(record);
                        return;
                    } else {
                        return;
                    }
                }
            }
        }

        private void scheduleListenerRetry(ListenerRecord record) {
            if (!this.mHandler.hasMessages(MSG_RETRY_LISTENER_QUEUE, record.componentName)) {
                record.retryCount++;
                if (record.retryCount > 6) {
                    Log.w(TAG, "Giving up on delivering " + record.taskQueue.size() + " tasks to " + record.componentName + " after " + record.retryCount + " retries");
                    record.taskQueue.clear();
                } else {
                    int delayMs = (1 << (record.retryCount - 1)) * 1000;
                    if (Log.isLoggable(TAG, MSG_RETRY_LISTENER_QUEUE)) {
                        Log.d(TAG, "Scheduling retry for " + delayMs + " ms");
                    }
                    this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(MSG_RETRY_LISTENER_QUEUE, record.componentName), (long) delayMs);
                }
            }
        }

        private void updateListenerMap() {
            Set<String> enabledPackages = NotificationManagerCompat.getEnabledListenerPackages(this.mContext);
            if (!enabledPackages.equals(this.mCachedEnabledPackages)) {
                ComponentName componentName;
                this.mCachedEnabledPackages = enabledPackages;
                List<ResolveInfo> resolveInfos = this.mContext.getPackageManager().queryIntentServices(new Intent().setAction(ACTION_BIND_SIDE_CHANNEL), MMAdView.TRANSITION_RANDOM);
                Set<ComponentName> enabledComponents = new HashSet();
                Iterator i$ = resolveInfos.iterator();
                while (i$.hasNext()) {
                    ResolveInfo resolveInfo = (ResolveInfo) i$.next();
                    if (enabledPackages.contains(resolveInfo.serviceInfo.packageName)) {
                        componentName = new ComponentName(resolveInfo.serviceInfo.packageName, resolveInfo.serviceInfo.name);
                        if (resolveInfo.serviceInfo.permission != null) {
                            Log.w(TAG, "Permission present on component " + componentName + ", not adding listener record.");
                        } else {
                            enabledComponents.add(componentName);
                        }
                    }
                }
                i$ = enabledComponents.iterator();
                while (i$.hasNext()) {
                    componentName = i$.next();
                    if (!this.mRecordMap.containsKey(componentName)) {
                        if (Log.isLoggable(TAG, MSG_RETRY_LISTENER_QUEUE)) {
                            Log.d(TAG, "Adding listener record for " + componentName);
                        }
                        this.mRecordMap.put(componentName, new ListenerRecord(componentName));
                    }
                }
                Iterator<Entry<ComponentName, ListenerRecord>> it = this.mRecordMap.entrySet().iterator();
                while (it.hasNext()) {
                    Entry<ComponentName, ListenerRecord> entry = (Entry) it.next();
                    if (!enabledComponents.contains(entry.getKey())) {
                        if (Log.isLoggable(TAG, MSG_RETRY_LISTENER_QUEUE)) {
                            Log.d(TAG, "Removing listener record for " + entry.getKey());
                        }
                        ensureServiceUnbound((ListenerRecord) entry.getValue());
                        it.remove();
                    }
                }
            }
        }

        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_QUEUE_TASK:
                    handleQueueTask((Task) msg.obj);
                    return true;
                case MSG_SERVICE_CONNECTED:
                    ServiceConnectedEvent event = (ServiceConnectedEvent) msg.obj;
                    handleServiceConnected(event.componentName, event.iBinder);
                    return true;
                case MSG_SERVICE_DISCONNECTED:
                    handleServiceDisconnected((ComponentName) msg.obj);
                    return true;
                case MSG_RETRY_LISTENER_QUEUE:
                    handleRetryListenerQueue((ComponentName) msg.obj);
                    return true;
                default:
                    return false;
            }
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            if (Log.isLoggable(TAG, MSG_RETRY_LISTENER_QUEUE)) {
                Log.d(TAG, "Connected to service " + componentName);
            }
            this.mHandler.obtainMessage(MSG_SERVICE_CONNECTED, new ServiceConnectedEvent(componentName, iBinder)).sendToTarget();
        }

        public void onServiceDisconnected(ComponentName componentName) {
            if (Log.isLoggable(TAG, MSG_RETRY_LISTENER_QUEUE)) {
                Log.d(TAG, "Disconnected from service " + componentName);
            }
            this.mHandler.obtainMessage(MSG_SERVICE_DISCONNECTED, componentName).sendToTarget();
        }

        public void queueTask(Task task) {
            this.mHandler.obtainMessage(MSG_QUEUE_TASK, task).sendToTarget();
        }
    }

    private static interface Task {
        void send(INotificationSideChannel iNotificationSideChannel) throws RemoteException;
    }

    private static class CancelTask implements Task {
        final boolean all;
        final int id;
        final String packageName;
        final String tag;

        public CancelTask(String packageName) {
            this.packageName = packageName;
            this.id = 0;
            this.tag = null;
            this.all = true;
        }

        public CancelTask(String packageName, int id, String tag) {
            this.packageName = packageName;
            this.id = id;
            this.tag = tag;
            this.all = false;
        }

        public void send(INotificationSideChannel service) throws RemoteException {
            if (this.all) {
                service.cancelAll(this.packageName);
            } else {
                service.cancel(this.packageName, this.id, this.tag);
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("CancelTask[");
            sb.append("packageName:").append(this.packageName);
            sb.append(", id:").append(this.id);
            sb.append(", tag:").append(this.tag);
            sb.append(", all:").append(this.all);
            sb.append("]");
            return sb.toString();
        }
    }

    static class ImplBase implements Impl {
        ImplBase() {
        }

        public void cancelNotification(NotificationManager notificationManager, String tag, int id) {
            notificationManager.cancel(id);
        }

        public int getSideChannelBindFlags() {
            return 1;
        }

        public void postNotification(NotificationManager notificationManager, String tag, int id, Notification notification) {
            notificationManager.notify(id, notification);
        }
    }

    private static class NotifyTask implements Task {
        final int id;
        final Notification notif;
        final String packageName;
        final String tag;

        public NotifyTask(String packageName, int id, String tag, Notification notif) {
            this.packageName = packageName;
            this.id = id;
            this.tag = tag;
            this.notif = notif;
        }

        public void send(INotificationSideChannel service) throws RemoteException {
            service.notify(this.packageName, this.id, this.tag, this.notif);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("NotifyTask[");
            sb.append("packageName:").append(this.packageName);
            sb.append(", id:").append(this.id);
            sb.append(", tag:").append(this.tag);
            sb.append("]");
            return sb.toString();
        }
    }

    static class ImplEclair extends ImplBase {
        ImplEclair() {
        }

        public void cancelNotification(NotificationManager notificationManager, String tag, int id) {
            NotificationManagerCompatEclair.cancelNotification(notificationManager, tag, id);
        }

        public void postNotification(NotificationManager notificationManager, String tag, int id, Notification notification) {
            NotificationManagerCompatEclair.postNotification(notificationManager, tag, id, notification);
        }
    }

    static class ImplIceCreamSandwich extends ImplEclair {
        ImplIceCreamSandwich() {
        }

        public int getSideChannelBindFlags() {
            return ApiEventType.API_MRAID_MUTE_AUDIO;
        }
    }

    static {
        sEnabledNotificationListenersLock = new Object();
        sEnabledNotificationListenerPackages = new HashSet();
        sLock = new Object();
        if (VERSION.SDK_INT >= 14) {
            IMPL = new ImplIceCreamSandwich();
        } else if (VERSION.SDK_INT >= 5) {
            IMPL = new ImplEclair();
        } else {
            IMPL = new ImplBase();
        }
        SIDE_CHANNEL_BIND_FLAGS = IMPL.getSideChannelBindFlags();
    }

    private NotificationManagerCompat(Context context) {
        this.mContext = context;
        this.mNotificationManager = (NotificationManager) this.mContext.getSystemService("notification");
    }

    public static NotificationManagerCompat from(Context context) {
        return new NotificationManagerCompat(context);
    }

    public static Set<String> getEnabledListenerPackages(Context context) {
        String enabledNotificationListeners = Secure.getString(context.getContentResolver(), SETTING_ENABLED_NOTIFICATION_LISTENERS);
        if (!(enabledNotificationListeners == null || enabledNotificationListeners.equals(sEnabledNotificationListeners))) {
            String[] components = enabledNotificationListeners.split(":");
            Set<String> packageNames = new HashSet(components.length);
            String[] arr$ = components;
            int len$ = arr$.length;
            int i$ = SIDE_CHANNEL_BIND_FLAGS;
            while (i$ < len$) {
                ComponentName componentName = ComponentName.unflattenFromString(arr$[i$]);
                if (componentName != null) {
                    packageNames.add(componentName.getPackageName());
                }
                i$++;
            }
            synchronized (sEnabledNotificationListenersLock) {
                sEnabledNotificationListenerPackages = packageNames;
                sEnabledNotificationListeners = enabledNotificationListeners;
            }
        }
        return sEnabledNotificationListenerPackages;
    }

    private void pushSideChannelQueue(Task task) {
        synchronized (sLock) {
            if (sSideChannelManager == null) {
                sSideChannelManager = new SideChannelManager(this.mContext.getApplicationContext());
            }
        }
        sSideChannelManager.queueTask(task);
    }

    private static boolean useSideChannelForNotification(Notification notification) {
        Bundle extras = NotificationCompat.getExtras(notification);
        return extras != null && extras.getBoolean(EXTRA_USE_SIDE_CHANNEL);
    }

    public void cancel(int id) {
        cancel(null, id);
    }

    public void cancel(String tag, int id) {
        IMPL.cancelNotification(this.mNotificationManager, tag, id);
        pushSideChannelQueue(new CancelTask(this.mContext.getPackageName(), id, tag));
    }

    public void cancelAll() {
        this.mNotificationManager.cancelAll();
        pushSideChannelQueue(new CancelTask(this.mContext.getPackageName()));
    }

    public void notify(int id, Notification notification) {
        notify(null, id, notification);
    }

    public void notify(String tag, int id, Notification notification) {
        if (useSideChannelForNotification(notification)) {
            pushSideChannelQueue(new NotifyTask(this.mContext.getPackageName(), id, tag, notification));
        } else {
            IMPL.postNotification(this.mNotificationManager, tag, id, notification);
        }
    }
}