package com.android.systemui.statusbar.phone;

import android.app.StatusBarManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Handler;
import android.os.storage.StorageManager;
import com.android.internal.telephony.IccCard.State;
import com.android.systemui.R;
import com.android.systemui.usb.StorageNotification;

public class PhoneStatusBarPolicy {
    private static final int AM_PM_STYLE = 2;
    private static final int AM_PM_STYLE_GONE = 2;
    private static final int AM_PM_STYLE_NORMAL = 0;
    private static final int AM_PM_STYLE_SMALL = 1;
    private static final int EVENT_BATTERY_CLOSE = 4;
    private static final int INET_CONDITION_THRESHOLD = 50;
    private static final boolean SHOW_SYNC_ICON = false;
    private static final String TAG = "PhoneStatusBarPolicy";
    private static final int[][] sWifiSignalImages;
    private static final int sWifiTemporarilyNotConnectedImage = 2130837675;
    private boolean mBluetoothEnabled;
    private final Context mContext;
    private final Handler mHandler;
    private int mInetCondition;
    private BroadcastReceiver mIntentReceiver;
    private boolean mIsWifiConnected;
    private int mLastWifiSignalLevel;
    private final StatusBarManager mService;
    State mSimState;
    private StorageManager mStorageManager;
    private boolean mVolumeVisible;

    static {
        sWifiSignalImages = new int[][]{new int[]{2130837676, 2130837678, 2130837680, 2130837682}, new int[]{2130837677, 2130837679, 2130837681, 2130837683}};
    }

    public PhoneStatusBarPolicy(Context context) {
        this.mHandler = new Handler();
        this.mSimState = State.READY;
        this.mBluetoothEnabled = false;
        this.mLastWifiSignalLevel = -1;
        this.mIsWifiConnected = false;
        this.mInetCondition = 0;
        this.mIntentReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals("android.intent.action.ALARM_CHANGED")) {
                    PhoneStatusBarPolicy.this.updateAlarm(intent);
                } else if (action.equals("android.intent.action.SYNC_STATE_CHANGED")) {
                    PhoneStatusBarPolicy.this.updateSyncState(intent);
                } else if (action.equals("android.bluetooth.adapter.action.STATE_CHANGED") || action.equals("android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED")) {
                    PhoneStatusBarPolicy.this.updateBluetooth(intent);
                } else if (action.equals("android.media.RINGER_MODE_CHANGED")) {
                    PhoneStatusBarPolicy.this.updateVolume();
                } else if (action.equals("android.intent.action.SIM_STATE_CHANGED")) {
                    PhoneStatusBarPolicy.this.updateSimState(intent);
                } else if (action.equals("com.android.internal.telephony.cdma.intent.action.TTY_ENABLED_CHANGE")) {
                    PhoneStatusBarPolicy.this.updateTTY(intent);
                }
            }
        };
        this.mContext = context;
        this.mService = (StatusBarManager) context.getSystemService("statusbar");
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.ALARM_CHANGED");
        filter.addAction("android.intent.action.SYNC_STATE_CHANGED");
        filter.addAction("android.media.RINGER_MODE_CHANGED");
        filter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        filter.addAction("android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED");
        filter.addAction("android.intent.action.SIM_STATE_CHANGED");
        filter.addAction("com.android.internal.telephony.cdma.intent.action.TTY_ENABLED_CHANGE");
        this.mContext.registerReceiver(this.mIntentReceiver, filter, null, this.mHandler);
        this.mStorageManager = (StorageManager) context.getSystemService("storage");
        this.mStorageManager.registerListener(new StorageNotification(context));
        this.mService.setIcon("tty", R.drawable.stat_sys_tty_mode, AM_PM_STYLE_NORMAL, null);
        this.mService.setIconVisibility("tty", SHOW_SYNC_ICON);
        this.mService.setIcon("cdma_eri", R.drawable.stat_sys_roaming_cdma_0, AM_PM_STYLE_NORMAL, null);
        this.mService.setIconVisibility("cdma_eri", SHOW_SYNC_ICON);
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        int bluetoothIcon = R.drawable.stat_sys_data_bluetooth;
        if (adapter != null) {
            this.mBluetoothEnabled = adapter.getState() == 12;
            if (adapter.getConnectionState() == 2) {
                bluetoothIcon = R.drawable.stat_sys_data_bluetooth_connected;
            }
        }
        this.mService.setIcon("bluetooth", bluetoothIcon, AM_PM_STYLE_NORMAL, null);
        this.mService.setIconVisibility("bluetooth", this.mBluetoothEnabled);
        this.mService.setIcon("alarm_clock", R.drawable.stat_sys_alarm, AM_PM_STYLE_NORMAL, null);
        this.mService.setIconVisibility("alarm_clock", SHOW_SYNC_ICON);
        this.mService.setIcon("sync_active", R.drawable.stat_sys_sync, AM_PM_STYLE_NORMAL, null);
        this.mService.setIcon("sync_failing", R.drawable.stat_sys_sync_error, AM_PM_STYLE_NORMAL, null);
        this.mService.setIconVisibility("sync_active", SHOW_SYNC_ICON);
        this.mService.setIconVisibility("sync_failing", SHOW_SYNC_ICON);
        this.mService.setIcon("volume", R.drawable.stat_sys_ringer_silent, AM_PM_STYLE_NORMAL, null);
        this.mService.setIconVisibility("volume", SHOW_SYNC_ICON);
        updateVolume();
    }

    private final void updateAlarm(Intent intent) {
        this.mService.setIconVisibility("alarm_clock", intent.getBooleanExtra("alarmSet", SHOW_SYNC_ICON));
    }

    private final void updateBluetooth(Intent intent) {
        int iconId = R.drawable.stat_sys_data_bluetooth;
        String contentDescription = null;
        String action = intent.getAction();
        if (action.equals("android.bluetooth.adapter.action.STATE_CHANGED")) {
            boolean z;
            if (intent.getIntExtra("android.bluetooth.adapter.extra.STATE", Integer.MIN_VALUE) == 12) {
                z = true;
            } else {
                z = false;
            }
            this.mBluetoothEnabled = z;
        } else if (!action.equals("android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED")) {
            return;
        } else {
            if (intent.getIntExtra("android.bluetooth.adapter.extra.CONNECTION_STATE", AM_PM_STYLE_NORMAL) == 2) {
                iconId = R.drawable.stat_sys_data_bluetooth_connected;
                contentDescription = this.mContext.getString(R.string.accessibility_bluetooth_connected);
            } else {
                contentDescription = this.mContext.getString(R.string.accessibility_bluetooth_disconnected);
            }
        }
        this.mService.setIcon("bluetooth", iconId, AM_PM_STYLE_NORMAL, contentDescription);
        this.mService.setIconVisibility("bluetooth", this.mBluetoothEnabled);
    }

    private final void updateSimState(Intent intent) {
        String stateExtra = intent.getStringExtra("ss");
        if ("ABSENT".equals(stateExtra)) {
            this.mSimState = State.ABSENT;
        } else if ("READY".equals(stateExtra)) {
            this.mSimState = State.READY;
        } else if ("LOCKED".equals(stateExtra)) {
            String lockedReason = intent.getStringExtra("reason");
            if ("PIN".equals(lockedReason)) {
                this.mSimState = State.PIN_REQUIRED;
            } else if ("PUK".equals(lockedReason)) {
                this.mSimState = State.PUK_REQUIRED;
            } else {
                this.mSimState = State.NETWORK_LOCKED;
            }
        } else {
            this.mSimState = State.UNKNOWN;
        }
    }

    private final void updateSyncState(Intent intent) {
    }

    private final void updateTTY(Intent intent) {
        String action = intent.getAction();
        if (intent.getBooleanExtra("ttyEnabled", SHOW_SYNC_ICON)) {
            this.mService.setIcon("tty", R.drawable.stat_sys_tty_mode, AM_PM_STYLE_NORMAL, this.mContext.getString(R.string.accessibility_tty_enabled));
            this.mService.setIconVisibility("tty", true);
        } else {
            this.mService.setIconVisibility("tty", SHOW_SYNC_ICON);
        }
    }

    private final void updateVolume() {
        boolean visible;
        int iconId;
        String contentDescription;
        int ringerMode = ((AudioManager) this.mContext.getSystemService("audio")).getRingerMode();
        if (ringerMode == 0 || ringerMode == 1) {
            visible = true;
        } else {
            visible = false;
        }
        if (ringerMode == 1) {
            iconId = R.drawable.stat_sys_ringer_vibrate;
            contentDescription = this.mContext.getString(R.string.accessibility_ringer_vibrate);
        } else {
            iconId = R.drawable.stat_sys_ringer_silent;
            contentDescription = this.mContext.getString(R.string.accessibility_ringer_silent);
        }
        if (visible) {
            this.mService.setIcon("volume", iconId, AM_PM_STYLE_NORMAL, contentDescription);
        }
        if (visible != this.mVolumeVisible) {
            this.mService.setIconVisibility("volume", visible);
            this.mVolumeVisible = visible;
        }
    }
}