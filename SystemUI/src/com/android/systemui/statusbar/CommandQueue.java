package com.android.systemui.statusbar;

import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import com.android.internal.statusbar.IStatusBar.Stub;
import com.android.internal.statusbar.StatusBarIcon;
import com.android.internal.statusbar.StatusBarIconList;
import com.android.internal.statusbar.StatusBarNotification;

public class CommandQueue extends Stub {
    public static final int FLAG_EXCLUDE_COMPAT_MODE_PANEL = 16;
    public static final int FLAG_EXCLUDE_INPUT_METHODS_PANEL = 8;
    public static final int FLAG_EXCLUDE_NONE = 0;
    public static final int FLAG_EXCLUDE_NOTIFICATION_PANEL = 4;
    public static final int FLAG_EXCLUDE_RECENTS_PANEL = 2;
    public static final int FLAG_EXCLUDE_SEARCH_PANEL = 1;
    private static final int INDEX_MASK = 65535;
    private static final int MSG_ADD_NOTIFICATION = 131072;
    private static final int MSG_CANCEL_PRELOAD_RECENT_APPS = 851968;
    private static final int MSG_DISABLE = 327680;
    private static final int MSG_ICON = 65536;
    private static final int MSG_MASK = -65536;
    private static final int MSG_PRELOAD_RECENT_APPS = 786432;
    private static final int MSG_REMOVE_NOTIFICATION = 262144;
    private static final int MSG_SET_HARD_KEYBOARD_STATUS = 655360;
    private static final int MSG_SET_NAVIGATION_ICON_HINTS = 917504;
    private static final int MSG_SET_SYSTEMUI_VISIBILITY = 458752;
    private static final int MSG_SET_VISIBILITY = 393216;
    private static final int MSG_SHIFT = 16;
    private static final int MSG_SHOW_IME_BUTTON = 589824;
    private static final int MSG_TOGGLE_RECENT_APPS = 720896;
    private static final int MSG_TOP_APP_WINDOW_CHANGED = 524288;
    private static final int MSG_UPDATE_NOTIFICATION = 196608;
    private static final int OP_COLLAPSE = 2;
    private static final int OP_EXPAND = 1;
    private static final int OP_REMOVE_ICON = 2;
    private static final int OP_SET_ICON = 1;
    private static final String TAG = "StatusBar.CommandQueue";
    private Callbacks mCallbacks;
    private Handler mHandler;
    private StatusBarIconList mList;

    public static interface Callbacks {
        void addIcon(String str, int i, int i2, StatusBarIcon statusBarIcon);

        void addNotification(IBinder iBinder, StatusBarNotification statusBarNotification);

        void animateCollapse(int i);

        void animateExpand();

        void cancelPreloadRecentApps();

        void disable(int i);

        void hideSearchPanel();

        void preloadRecentApps();

        void removeIcon(String str, int i, int i2);

        void removeNotification(IBinder iBinder);

        void setHardKeyboardStatus(boolean z, boolean z2);

        void setImeWindowStatus(IBinder iBinder, int i, int i2);

        void setNavigationIconHints(int i);

        void setSystemUiVisibility(int i, int i2);

        void showSearchPanel();

        void toggleRecentApps();

        void topAppWindowChanged(boolean z);

        void updateIcon(String str, int i, int i2, StatusBarIcon statusBarIcon, StatusBarIcon statusBarIcon2);

        void updateNotification(IBinder iBinder, StatusBarNotification statusBarNotification);
    }

    private final class H extends Handler {
        private H() {
        }

        public void handleMessage(Message msg) {
            boolean z = true;
            NotificationQueueEntry ne;
            switch (msg.what & -65536) {
                case MSG_ICON:
                    int index = msg.what & 65535;
                    int viewIndex = CommandQueue.this.mList.getViewIndex(index);
                    switch (msg.arg1) {
                        case OP_SET_ICON:
                            StatusBarIcon icon = (StatusBarIcon) msg.obj;
                            StatusBarIcon old = CommandQueue.this.mList.getIcon(index);
                            if (old == null) {
                                CommandQueue.this.mList.setIcon(index, icon);
                                CommandQueue.this.mCallbacks.addIcon(CommandQueue.this.mList.getSlot(index), index, viewIndex, icon);
                            } else {
                                CommandQueue.this.mList.setIcon(index, icon);
                                CommandQueue.this.mCallbacks.updateIcon(CommandQueue.this.mList.getSlot(index), index, viewIndex, old, icon);
                            }
                        case OP_REMOVE_ICON:
                            if (CommandQueue.this.mList.getIcon(index) != null) {
                                CommandQueue.this.mList.removeIcon(index);
                                CommandQueue.this.mCallbacks.removeIcon(CommandQueue.this.mList.getSlot(index), index, viewIndex);
                            }
                        default:
                            break;
                    }
                case MSG_ADD_NOTIFICATION:
                    ne = (NotificationQueueEntry) msg.obj;
                    CommandQueue.this.mCallbacks.addNotification(ne.key, ne.notification);
                case MSG_UPDATE_NOTIFICATION:
                    ne = msg.obj;
                    CommandQueue.this.mCallbacks.updateNotification(ne.key, ne.notification);
                case MSG_REMOVE_NOTIFICATION:
                    CommandQueue.this.mCallbacks.removeNotification((IBinder) msg.obj);
                case MSG_DISABLE:
                    CommandQueue.this.mCallbacks.disable(msg.arg1);
                case MSG_SET_VISIBILITY:
                    if (msg.arg1 == 1) {
                        CommandQueue.this.mCallbacks.animateExpand();
                    } else {
                        CommandQueue.this.mCallbacks.animateCollapse(msg.arg2);
                    }
                case MSG_SET_SYSTEMUI_VISIBILITY:
                    CommandQueue.this.mCallbacks.setSystemUiVisibility(msg.arg1, msg.arg2);
                case MSG_TOP_APP_WINDOW_CHANGED:
                    com.android.systemui.statusbar.CommandQueue.Callbacks access$300 = CommandQueue.this.mCallbacks;
                    if (msg.arg1 == 0) {
                        z = false;
                    }
                    access$300.topAppWindowChanged(z);
                case MSG_SHOW_IME_BUTTON:
                    CommandQueue.this.mCallbacks.setImeWindowStatus((IBinder) msg.obj, msg.arg1, msg.arg2);
                case MSG_SET_HARD_KEYBOARD_STATUS:
                    com.android.systemui.statusbar.CommandQueue.Callbacks access$3002 = CommandQueue.this.mCallbacks;
                    boolean z2 = msg.arg1 != 0;
                    if (msg.arg2 == 0) {
                        z = false;
                    }
                    access$3002.setHardKeyboardStatus(z2, z);
                case MSG_TOGGLE_RECENT_APPS:
                    CommandQueue.this.mCallbacks.toggleRecentApps();
                case MSG_PRELOAD_RECENT_APPS:
                    CommandQueue.this.mCallbacks.preloadRecentApps();
                case MSG_CANCEL_PRELOAD_RECENT_APPS:
                    CommandQueue.this.mCallbacks.cancelPreloadRecentApps();
                case MSG_SET_NAVIGATION_ICON_HINTS:
                    CommandQueue.this.mCallbacks.setNavigationIconHints(msg.arg1);
                default:
                    break;
            }
        }
    }

    private class NotificationQueueEntry {
        IBinder key;
        StatusBarNotification notification;

        private NotificationQueueEntry() {
        }
    }

    public CommandQueue(Callbacks callbacks, StatusBarIconList list) {
        this.mHandler = new H(null);
        this.mCallbacks = callbacks;
        this.mList = list;
    }

    public void addNotification(IBinder key, StatusBarNotification notification) {
        synchronized (this.mList) {
            NotificationQueueEntry ne = new NotificationQueueEntry(null);
            ne.key = key;
            ne.notification = notification;
            this.mHandler.obtainMessage(MSG_ADD_NOTIFICATION, FLAG_EXCLUDE_NONE, FLAG_EXCLUDE_NONE, ne).sendToTarget();
        }
    }

    public void animateCollapse() {
        animateCollapse(FLAG_EXCLUDE_NONE);
    }

    public void animateCollapse(int flags) {
        synchronized (this.mList) {
            this.mHandler.removeMessages(MSG_SET_VISIBILITY);
            this.mHandler.obtainMessage(MSG_SET_VISIBILITY, OP_REMOVE_ICON, flags, null).sendToTarget();
        }
    }

    public void animateExpand() {
        synchronized (this.mList) {
            this.mHandler.removeMessages(MSG_SET_VISIBILITY);
            this.mHandler.obtainMessage(MSG_SET_VISIBILITY, OP_SET_ICON, FLAG_EXCLUDE_NONE, null).sendToTarget();
        }
    }

    public void cancelPreloadRecentApps() {
        synchronized (this.mList) {
            this.mHandler.removeMessages(MSG_CANCEL_PRELOAD_RECENT_APPS);
            this.mHandler.obtainMessage(MSG_CANCEL_PRELOAD_RECENT_APPS, FLAG_EXCLUDE_NONE, FLAG_EXCLUDE_NONE, null).sendToTarget();
        }
    }

    public void disable(int state) {
        synchronized (this.mList) {
            this.mHandler.removeMessages(MSG_DISABLE);
            this.mHandler.obtainMessage(MSG_DISABLE, state, FLAG_EXCLUDE_NONE, null).sendToTarget();
        }
    }

    public void preloadRecentApps() {
        synchronized (this.mList) {
            this.mHandler.removeMessages(MSG_PRELOAD_RECENT_APPS);
            this.mHandler.obtainMessage(MSG_PRELOAD_RECENT_APPS, FLAG_EXCLUDE_NONE, FLAG_EXCLUDE_NONE, null).sendToTarget();
        }
    }

    public void removeIcon(int index) {
        synchronized (this.mList) {
            int what = 65536 | index;
            this.mHandler.removeMessages(what);
            this.mHandler.obtainMessage(what, OP_REMOVE_ICON, FLAG_EXCLUDE_NONE, null).sendToTarget();
        }
    }

    public void removeNotification(IBinder key) {
        synchronized (this.mList) {
            this.mHandler.obtainMessage(MSG_REMOVE_NOTIFICATION, FLAG_EXCLUDE_NONE, FLAG_EXCLUDE_NONE, key).sendToTarget();
        }
    }

    public void setHardKeyboardStatus(boolean available, boolean enabled) {
        int i = OP_SET_ICON;
        synchronized (this.mList) {
            int i2;
            this.mHandler.removeMessages(MSG_SET_HARD_KEYBOARD_STATUS);
            Handler handler = this.mHandler;
            if (available) {
                i2 = 1;
            } else {
                i2 = 0;
            }
            if (!enabled) {
                i = 0;
            }
            handler.obtainMessage(MSG_SET_HARD_KEYBOARD_STATUS, i2, i).sendToTarget();
        }
    }

    public void setIcon(int index, StatusBarIcon icon) {
        synchronized (this.mList) {
            int what = 65536 | index;
            this.mHandler.removeMessages(what);
            this.mHandler.obtainMessage(what, OP_SET_ICON, FLAG_EXCLUDE_NONE, icon.clone()).sendToTarget();
        }
    }

    public void setImeWindowStatus(IBinder token, int vis, int backDisposition) {
        synchronized (this.mList) {
            this.mHandler.removeMessages(MSG_SHOW_IME_BUTTON);
            this.mHandler.obtainMessage(MSG_SHOW_IME_BUTTON, vis, backDisposition, token).sendToTarget();
        }
    }

    public void setNavigationIconHints(int hints) {
        synchronized (this.mList) {
            this.mHandler.removeMessages(MSG_SET_NAVIGATION_ICON_HINTS);
            this.mHandler.obtainMessage(MSG_SET_NAVIGATION_ICON_HINTS, hints, FLAG_EXCLUDE_NONE, null).sendToTarget();
        }
    }

    public void setSystemUiVisibility(int vis, int mask) {
        synchronized (this.mList) {
            this.mHandler.removeMessages(MSG_SET_SYSTEMUI_VISIBILITY);
            this.mHandler.obtainMessage(MSG_SET_SYSTEMUI_VISIBILITY, vis, mask, null).sendToTarget();
        }
    }

    public void toggleRecentApps() {
        synchronized (this.mList) {
            this.mHandler.removeMessages(MSG_TOGGLE_RECENT_APPS);
            this.mHandler.obtainMessage(MSG_TOGGLE_RECENT_APPS, FLAG_EXCLUDE_NONE, FLAG_EXCLUDE_NONE, null).sendToTarget();
        }
    }

    public void topAppWindowChanged(boolean menuVisible) {
        int i = FLAG_EXCLUDE_NONE;
        synchronized (this.mList) {
            this.mHandler.removeMessages(MSG_TOP_APP_WINDOW_CHANGED);
            Handler handler = this.mHandler;
            if (menuVisible) {
                i = OP_SET_ICON;
            }
            handler.obtainMessage(MSG_TOP_APP_WINDOW_CHANGED, i, FLAG_EXCLUDE_NONE, null).sendToTarget();
        }
    }

    public void updateNotification(IBinder key, StatusBarNotification notification) {
        synchronized (this.mList) {
            NotificationQueueEntry ne = new NotificationQueueEntry(null);
            ne.key = key;
            ne.notification = notification;
            this.mHandler.obtainMessage(MSG_UPDATE_NOTIFICATION, FLAG_EXCLUDE_NONE, FLAG_EXCLUDE_NONE, ne).sendToTarget();
        }
    }
}