package com.android.systemui.statusbar.policy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.provider.Settings.System;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Slog;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.internal.app.IBatteryStats;
import com.android.internal.telephony.IccCard.State;
import com.android.internal.util.AsyncChannel;
import com.android.server.am.BatteryStatsService;
import com.android.systemui.R;
import com.android.systemui.recent.RecentsCallback;
import com.android.systemui.statusbar.CommandQueue;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

public class NetworkController extends BroadcastReceiver {
    static final boolean CHATTY = false;
    static final boolean DEBUG = false;
    private static final int INET_CONDITION_THRESHOLD = 50;
    static final String TAG = "StatusBar.NetworkController";
    int mAirplaneIconId;
    private boolean mAirplaneMode;
    boolean mAlwaysShowCdmaRssi;
    IBatteryStats mBatteryStats;
    private int mBluetoothTetherIconId;
    private boolean mBluetoothTethered;
    ArrayList<TextView> mCombinedLabelViews;
    ArrayList<ImageView> mCombinedSignalIconViews;
    private boolean mConnected;
    private int mConnectedNetworkType;
    private String mConnectedNetworkTypeName;
    String mContentDescriptionCombinedSignal;
    String mContentDescriptionDataType;
    String mContentDescriptionEthernet;
    String mContentDescriptionPhoneSignal;
    String mContentDescriptionWifi;
    String mContentDescriptionWimax;
    Context mContext;
    boolean mDataActive;
    int mDataActivity;
    boolean mDataAndWifiStacked;
    boolean mDataConnected;
    int mDataDirectionIconId;
    ArrayList<ImageView> mDataDirectionIconViews;
    ArrayList<ImageView> mDataDirectionOverlayIconViews;
    int[] mDataIconList;
    int mDataNetType;
    int mDataSignalIconId;
    int mDataState;
    int mDataTypeIconId;
    ArrayList<ImageView> mDataTypeIconViews;
    int mEthernetActivityIconId;
    boolean mEthernetConnected;
    int mEthernetIconId;
    private boolean mHasMobileDataFeature;
    boolean mHspaDataDistinguishable;
    private int mInetCondition;
    private boolean mIsWimaxEnabled;
    private boolean mLastAirplaneMode;
    String mLastCombinedLabel;
    int mLastCombinedSignalIconId;
    int mLastDataDirectionIconId;
    int mLastDataDirectionOverlayIconId;
    int mLastDataTypeIconId;
    int mLastEthernetIconId;
    int mLastPhoneSignalIconId;
    int mLastSignalLevel;
    int mLastWifiIconId;
    int mLastWimaxIconId;
    int mMobileActivityIconId;
    ArrayList<TextView> mMobileLabelViews;
    String mNetworkName;
    String mNetworkNameDefault;
    String mNetworkNameSeparator;
    final TelephonyManager mPhone;
    int mPhoneSignalIconId;
    ArrayList<ImageView> mPhoneSignalIconViews;
    int mPhoneState;
    PhoneStateListener mPhoneStateListener;
    ServiceState mServiceState;
    boolean mShowAtLeastThreeGees;
    boolean mShowPhoneRSSIForData;
    ArrayList<SignalCluster> mSignalClusters;
    SignalStrength mSignalStrength;
    State mSimState;
    int mWifiActivity;
    int mWifiActivityIconId;
    AsyncChannel mWifiChannel;
    boolean mWifiConnected;
    boolean mWifiEnabled;
    int mWifiIconId;
    ArrayList<ImageView> mWifiIconViews;
    ArrayList<TextView> mWifiLabelViews;
    int mWifiLevel;
    final WifiManager mWifiManager;
    int mWifiRssi;
    String mWifiSsid;
    private boolean mWimaxConnected;
    private int mWimaxExtraState;
    private int mWimaxIconId;
    ArrayList<ImageView> mWimaxIconViews;
    private boolean mWimaxIdle;
    private int mWimaxSignal;
    private int mWimaxState;
    private boolean mWimaxSupported;

    public static interface SignalCluster {
        void setEthernetIndicators(boolean z, int i, int i2, String str);

        void setIsAirplaneMode(boolean z, int i);

        void setMobileDataIndicators(boolean z, int i, int i2, int i3, String str, String str2);

        void setWifiIndicators(boolean z, int i, int i2, String str);
    }

    class WifiHandler extends Handler {
        WifiHandler() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommandQueue.FLAG_EXCLUDE_SEARCH_PANEL:
                    if (msg.arg1 != NetworkController.this.mWifiActivity) {
                        NetworkController.this.mWifiActivity = msg.arg1;
                        NetworkController.this.refreshViews();
                    }
                case 69632:
                    if (msg.arg1 == 0) {
                        NetworkController.this.mWifiChannel.sendMessage(Message.obtain(this, 69633));
                    } else {
                        Slog.e(TAG, "Failed to connect to wifi");
                    }
                default:
                    break;
            }
        }
    }

    public NetworkController(Context context) {
        this.mSimState = State.READY;
        this.mPhoneState = 0;
        this.mDataNetType = 0;
        this.mDataState = 0;
        this.mDataActivity = 0;
        this.mDataIconList = TelephonyIcons.DATA_G[0];
        this.mShowPhoneRSSIForData = false;
        this.mShowAtLeastThreeGees = false;
        this.mAlwaysShowCdmaRssi = false;
        this.mWifiIconId = 0;
        this.mWifiActivityIconId = 0;
        this.mWifiActivity = 0;
        this.mEthernetIconId = 2130837643;
        this.mEthernetActivityIconId = 0;
        this.mBluetoothTethered = false;
        this.mBluetoothTetherIconId = 17302860;
        this.mWimaxSupported = false;
        this.mIsWimaxEnabled = false;
        this.mWimaxConnected = false;
        this.mWimaxIdle = false;
        this.mWimaxIconId = 0;
        this.mWimaxSignal = 0;
        this.mWimaxState = 0;
        this.mWimaxExtraState = 0;
        this.mConnected = false;
        this.mConnectedNetworkType = -1;
        this.mInetCondition = 0;
        this.mAirplaneMode = false;
        this.mLastAirplaneMode = true;
        this.mPhoneSignalIconViews = new ArrayList();
        this.mDataDirectionIconViews = new ArrayList();
        this.mDataDirectionOverlayIconViews = new ArrayList();
        this.mWifiIconViews = new ArrayList();
        this.mWimaxIconViews = new ArrayList();
        this.mCombinedSignalIconViews = new ArrayList();
        this.mDataTypeIconViews = new ArrayList();
        this.mCombinedLabelViews = new ArrayList();
        this.mMobileLabelViews = new ArrayList();
        this.mWifiLabelViews = new ArrayList();
        this.mSignalClusters = new ArrayList();
        this.mLastPhoneSignalIconId = -1;
        this.mLastDataDirectionIconId = -1;
        this.mLastDataDirectionOverlayIconId = -1;
        this.mLastWifiIconId = -1;
        this.mLastWimaxIconId = -1;
        this.mLastEthernetIconId = -1;
        this.mLastCombinedSignalIconId = -1;
        this.mLastDataTypeIconId = -1;
        this.mLastCombinedLabel = "";
        this.mDataAndWifiStacked = false;
        this.mPhoneStateListener = new PhoneStateListener() {
            public void onCallStateChanged(int state, String incomingNumber) {
                if (NetworkController.this.isCdma()) {
                    NetworkController.this.updateTelephonySignalStrength();
                    NetworkController.this.refreshViews();
                }
            }

            public void onDataActivity(int direction) {
                NetworkController.this.mDataActivity = direction;
                NetworkController.this.updateDataIcon();
                NetworkController.this.refreshViews();
            }

            public void onDataConnectionStateChanged(int state, int networkType) {
                NetworkController.this.mDataState = state;
                NetworkController.this.mDataNetType = networkType;
                NetworkController.this.updateDataNetType();
                NetworkController.this.updateDataIcon();
                NetworkController.this.refreshViews();
            }

            public void onServiceStateChanged(ServiceState state) {
                NetworkController.this.mServiceState = state;
                NetworkController.this.updateTelephonySignalStrength();
                NetworkController.this.updateDataNetType();
                NetworkController.this.updateDataIcon();
                NetworkController.this.refreshViews();
            }

            public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                NetworkController.this.mSignalStrength = signalStrength;
                NetworkController.this.updateTelephonySignalStrength();
                NetworkController.this.refreshViews();
            }
        };
        this.mContext = context;
        Resources res = context.getResources();
        this.mHasMobileDataFeature = ((ConnectivityManager) this.mContext.getSystemService("connectivity")).isNetworkSupported(0);
        this.mShowPhoneRSSIForData = res.getBoolean(R.bool.config_showPhoneRSSIForData);
        this.mShowAtLeastThreeGees = res.getBoolean(R.bool.config_showMin3G);
        this.mAlwaysShowCdmaRssi = res.getBoolean(17891378);
        updateWifiIcons();
        updateWimaxIcons();
        this.mPhone = (TelephonyManager) context.getSystemService("phone");
        this.mPhone.listen(this.mPhoneStateListener, 481);
        this.mHspaDataDistinguishable = this.mContext.getResources().getBoolean(R.bool.config_hspa_data_distinguishable);
        this.mNetworkNameSeparator = this.mContext.getString(R.string.status_bar_network_name_separator);
        this.mNetworkNameDefault = this.mContext.getString(17040118);
        this.mNetworkName = this.mNetworkNameDefault;
        this.mWifiManager = (WifiManager) context.getSystemService("wifi");
        Handler handler = new WifiHandler();
        this.mWifiChannel = new AsyncChannel();
        Messenger wifiMessenger = this.mWifiManager.getWifiServiceMessenger();
        if (wifiMessenger != null) {
            this.mWifiChannel.connect(this.mContext, handler, wifiMessenger);
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.wifi.RSSI_CHANGED");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        filter.addAction("android.intent.action.SIM_STATE_CHANGED");
        filter.addAction("android.provider.Telephony.SPN_STRINGS_UPDATED");
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.conn.INET_CONDITION_ACTION");
        filter.addAction("android.intent.action.CONFIGURATION_CHANGED");
        filter.addAction("android.intent.action.AIRPLANE_MODE");
        filter.addAction("android.net.ethernet.ETHERNET_STATE_CHANGED");
        filter.addAction("android.net.ethernet.STATE_CHANGE");
        this.mWimaxSupported = this.mContext.getResources().getBoolean(17891383);
        if (this.mWimaxSupported) {
            filter.addAction("android.net.fourG.wimax.WIMAX_NETWORK_STATE_CHANGED");
            filter.addAction("android.net.wimax.SIGNAL_LEVEL_CHANGED");
            filter.addAction("android.net.fourG.NET_4G_STATE_CHANGED");
        }
        context.registerReceiver(this, filter);
        updateAirplaneMode();
        this.mBatteryStats = BatteryStatsService.getService();
    }

    private String getResourceName(int resId) {
        if (resId == 0) {
            return "(null)";
        }
        try {
            return this.mContext.getResources().getResourceName(resId);
        } catch (NotFoundException e) {
            return "(unknown)";
        }
    }

    private boolean hasService() {
        if (this.mServiceState == null) {
            return DEBUG;
        }
        switch (this.mServiceState.getState()) {
            case CommandQueue.FLAG_EXCLUDE_SEARCH_PANEL:
            case RecentsCallback.SWIPE_DOWN:
                return DEBUG;
            default:
                return true;
        }
    }

    private String huntForSsid(WifiInfo info) {
        String ssid = info.getSSID();
        if (ssid != null) {
            return ssid;
        }
        Iterator i$ = this.mWifiManager.getConfiguredNetworks().iterator();
        while (i$.hasNext()) {
            WifiConfiguration net = (WifiConfiguration) i$.next();
            if (net.networkId == info.getNetworkId()) {
                return net.SSID;
            }
        }
        return null;
    }

    private boolean isCdma() {
        return (this.mSignalStrength == null || this.mSignalStrength.isGsm()) ? DEBUG : true;
    }

    private void updateAirplaneMode() {
        boolean z = true;
        if (System.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) != 1) {
            z = false;
        }
        this.mAirplaneMode = z;
    }

    private void updateConnectivity(Intent intent) {
        boolean z;
        int i = 1;
        NetworkInfo info = ((ConnectivityManager) this.mContext.getSystemService("connectivity")).getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            z = false;
        } else {
            z = true;
        }
        this.mConnected = z;
        if (this.mConnected) {
            this.mConnectedNetworkType = info.getType();
            this.mConnectedNetworkTypeName = info.getTypeName();
        } else {
            this.mConnectedNetworkType = -1;
            this.mConnectedNetworkTypeName = null;
        }
        if (intent.getIntExtra("inetCondition", 0) <= 50) {
            i = 0;
        }
        this.mInetCondition = i;
        if (info == null || info.getType() != 7) {
            this.mBluetoothTethered = false;
        } else {
            this.mBluetoothTethered = info.isConnected();
        }
        updateDataNetType();
        updateWimaxIcons();
        updateDataIcon();
        updateTelephonySignalStrength();
        updateWifiIcons();
        updateEthernetIcons();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void updateDataIcon() {
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.policy.NetworkController.updateDataIcon():void");
        /*
        r10 = this;
        r9 = 3;
        r8 = 1;
        r7 = 0;
        r6 = 2;
        r3 = 1;
        r4 = r10.isCdma();
        if (r4 != 0) goto L_0x005a;
    L_0x000b:
        r4 = r10.mSimState;
        r5 = com.android.internal.telephony.IccCard.State.READY;
        if (r4 == r5) goto L_0x0017;
    L_0x0011:
        r4 = r10.mSimState;
        r5 = com.android.internal.telephony.IccCard.State.UNKNOWN;
        if (r4 != r5) goto L_0x0055;
    L_0x0017:
        r4 = r10.hasService();
        if (r4 == 0) goto L_0x0052;
    L_0x001d:
        r4 = r10.mDataState;
        if (r4 != r6) goto L_0x0052;
    L_0x0021:
        r4 = r10.mDataActivity;
        switch(r4) {
            case 1: goto L_0x0043;
            case 2: goto L_0x0048;
            case 3: goto L_0x004d;
            default: goto L_0x0026;
        };
    L_0x0026:
        r4 = r10.mDataIconList;
        r0 = r4[r7];
    L_0x002a:
        r10.mDataDirectionIconId = r0;
    L_0x002c:
        r1 = android.os.Binder.clearCallingIdentity();
        r4 = r10.mBatteryStats;	 Catch:{ RemoteException -> 0x0080, all -> 0x0085 }
        r5 = r10.mPhone;	 Catch:{ RemoteException -> 0x0080, all -> 0x0085 }
        r5 = r5.getNetworkType();	 Catch:{ RemoteException -> 0x0080, all -> 0x0085 }
        r4.notePhoneDataConnectionState(r5, r3);	 Catch:{ RemoteException -> 0x0080, all -> 0x0085 }
        android.os.Binder.restoreCallingIdentity(r1);
    L_0x003e:
        r10.mDataDirectionIconId = r0;
        r10.mDataConnected = r3;
        return;
    L_0x0043:
        r4 = r10.mDataIconList;
        r0 = r4[r8];
        goto L_0x002a;
    L_0x0048:
        r4 = r10.mDataIconList;
        r0 = r4[r6];
        goto L_0x002a;
    L_0x004d:
        r4 = r10.mDataIconList;
        r0 = r4[r9];
        goto L_0x002a;
    L_0x0052:
        r0 = 0;
        r3 = 0;
        goto L_0x002c;
    L_0x0055:
        r0 = 2130837647; // 0x7f02008f float:1.7280254E38 double:1.0527736782E-314;
        r3 = 0;
        goto L_0x002c;
    L_0x005a:
        r4 = r10.hasService();
        if (r4 == 0) goto L_0x007d;
    L_0x0060:
        r4 = r10.mDataState;
        if (r4 != r6) goto L_0x007d;
    L_0x0064:
        r4 = r10.mDataActivity;
        switch(r4) {
            case 1: goto L_0x006e;
            case 2: goto L_0x0073;
            case 3: goto L_0x0078;
            default: goto L_0x0069;
        };
    L_0x0069:
        r4 = r10.mDataIconList;
        r0 = r4[r7];
        goto L_0x002c;
    L_0x006e:
        r4 = r10.mDataIconList;
        r0 = r4[r8];
        goto L_0x002c;
    L_0x0073:
        r4 = r10.mDataIconList;
        r0 = r4[r6];
        goto L_0x002c;
    L_0x0078:
        r4 = r10.mDataIconList;
        r0 = r4[r9];
        goto L_0x002c;
    L_0x007d:
        r0 = 0;
        r3 = 0;
        goto L_0x002c;
    L_0x0080:
        r4 = move-exception;
        android.os.Binder.restoreCallingIdentity(r1);
        goto L_0x003e;
    L_0x0085:
        r4 = move-exception;
        android.os.Binder.restoreCallingIdentity(r1);
        throw r4;
        */
    }

    private final void updateDataNetType() {
        if (this.mIsWimaxEnabled && this.mWimaxConnected) {
            this.mDataIconList = TelephonyIcons.DATA_4G[this.mInetCondition];
            this.mDataTypeIconId = 2130837622;
            this.mContentDescriptionDataType = this.mContext.getString(R.string.accessibility_data_connection_4g);
        } else {
            switch (this.mDataNetType) {
                case CommandQueue.FLAG_EXCLUDE_NONE:
                    if (!this.mShowAtLeastThreeGees) {
                        this.mDataIconList = TelephonyIcons.DATA_G[this.mInetCondition];
                        this.mDataTypeIconId = 0;
                        this.mContentDescriptionDataType = this.mContext.getString(R.string.accessibility_data_connection_gprs);
                    }
                    if (!this.mShowAtLeastThreeGees) {
                        this.mDataIconList = TelephonyIcons.DATA_E[this.mInetCondition];
                        this.mDataTypeIconId = 2130837623;
                        this.mContentDescriptionDataType = this.mContext.getString(R.string.accessibility_data_connection_edge);
                    }
                    this.mDataIconList = TelephonyIcons.DATA_3G[this.mInetCondition];
                    this.mDataTypeIconId = 2130837621;
                    this.mContentDescriptionDataType = this.mContext.getString(R.string.accessibility_data_connection_3g);
                    break;
                case CommandQueue.FLAG_EXCLUDE_RECENTS_PANEL:
                    if (this.mShowAtLeastThreeGees) {
                        this.mDataIconList = TelephonyIcons.DATA_E[this.mInetCondition];
                        this.mDataTypeIconId = 2130837623;
                        this.mContentDescriptionDataType = this.mContext.getString(R.string.accessibility_data_connection_edge);
                    }
                    this.mDataIconList = TelephonyIcons.DATA_3G[this.mInetCondition];
                    this.mDataTypeIconId = 2130837621;
                    this.mContentDescriptionDataType = this.mContext.getString(R.string.accessibility_data_connection_3g);
                    break;
                case RecentsCallback.SWIPE_DOWN:
                    this.mDataIconList = TelephonyIcons.DATA_3G[this.mInetCondition];
                    this.mDataTypeIconId = 2130837621;
                    this.mContentDescriptionDataType = this.mContext.getString(R.string.accessibility_data_connection_3g);
                    break;
                case CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL:
                    this.mDataIconList = TelephonyIcons.DATA_1X[this.mInetCondition];
                    this.mDataTypeIconId = 2130837620;
                    this.mContentDescriptionDataType = this.mContext.getString(R.string.accessibility_data_connection_cdma);
                    break;
                case 5:
                case 6:
                case 12:
                case 14:
                    this.mDataIconList = TelephonyIcons.DATA_3G[this.mInetCondition];
                    this.mDataTypeIconId = 2130837621;
                    this.mContentDescriptionDataType = this.mContext.getString(R.string.accessibility_data_connection_3g);
                    break;
                case 7:
                    this.mDataIconList = TelephonyIcons.DATA_1X[this.mInetCondition];
                    this.mDataTypeIconId = 2130837620;
                    this.mContentDescriptionDataType = this.mContext.getString(R.string.accessibility_data_connection_cdma);
                    break;
                case CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL:
                case 9:
                case 10:
                case 15:
                    if (this.mHspaDataDistinguishable) {
                        this.mDataIconList = TelephonyIcons.DATA_H[this.mInetCondition];
                        this.mDataTypeIconId = 2130837625;
                        this.mContentDescriptionDataType = this.mContext.getString(R.string.accessibility_data_connection_3_5g);
                    } else {
                        this.mDataIconList = TelephonyIcons.DATA_3G[this.mInetCondition];
                        this.mDataTypeIconId = 2130837621;
                        this.mContentDescriptionDataType = this.mContext.getString(R.string.accessibility_data_connection_3g);
                    }
                    break;
                case 13:
                    this.mDataIconList = TelephonyIcons.DATA_4G[this.mInetCondition];
                    this.mDataTypeIconId = 2130837622;
                    this.mContentDescriptionDataType = this.mContext.getString(R.string.accessibility_data_connection_4g);
                    break;
                default:
                    if (this.mShowAtLeastThreeGees) {
                        this.mDataIconList = TelephonyIcons.DATA_3G[this.mInetCondition];
                        this.mDataTypeIconId = 2130837621;
                        this.mContentDescriptionDataType = this.mContext.getString(R.string.accessibility_data_connection_3g);
                    } else {
                        this.mDataIconList = TelephonyIcons.DATA_G[this.mInetCondition];
                        this.mDataTypeIconId = 2130837624;
                        this.mContentDescriptionDataType = this.mContext.getString(R.string.accessibility_data_connection_gprs);
                    }
                    break;
            }
        }
        if (isCdma()) {
            if (isCdmaEri()) {
                this.mDataTypeIconId = 2130837626;
            }
        } else if (this.mPhone.isNetworkRoaming()) {
            this.mDataTypeIconId = 2130837626;
        }
    }

    private void updateEthernetIcons() {
        if (this.mEthernetConnected) {
            this.mEthernetIconId = 2130837644;
            this.mContentDescriptionEthernet = this.mContext.getString(R.string.ethernet_description);
        } else {
            this.mEthernetIconId = 2130837643;
        }
    }

    private void updateEthernetState(Intent intent) {
        if (intent.getAction().equals("android.net.ethernet.STATE_CHANGE")) {
            NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
            boolean wasConnected = this.mEthernetConnected;
            boolean z = (networkInfo == null || !networkInfo.isConnected()) ? DEBUG : true;
            this.mEthernetConnected = z;
        }
        updateEthernetIcons();
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

    private final void updateTelephonySignalStrength() {
        if (!hasService()) {
            this.mPhoneSignalIconId = 2130837667;
            this.mDataSignalIconId = 2130837667;
            this.mPhoneSignalIconId = 0;
            this.mDataSignalIconId = 0;
        } else if (this.mSignalStrength == null) {
            this.mPhoneSignalIconId = 2130837667;
            this.mDataSignalIconId = 2130837667;
            this.mContentDescriptionPhoneSignal = this.mContext.getString(AccessibilityContentDescriptions.PHONE_SIGNAL_STRENGTH[0]);
        } else {
            int iconLevel;
            int[] iconList;
            if (isCdma() && this.mAlwaysShowCdmaRssi) {
                iconLevel = this.mSignalStrength.getCdmaLevel();
                this.mLastSignalLevel = iconLevel;
            } else {
                iconLevel = this.mSignalStrength.getLevel();
                this.mLastSignalLevel = iconLevel;
            }
            if (isCdma()) {
                if (isCdmaEri()) {
                    iconList = TelephonyIcons.TELEPHONY_SIGNAL_STRENGTH_ROAMING[this.mInetCondition];
                } else {
                    iconList = TelephonyIcons.TELEPHONY_SIGNAL_STRENGTH[this.mInetCondition];
                }
            } else if (this.mPhone.isNetworkRoaming()) {
                iconList = TelephonyIcons.TELEPHONY_SIGNAL_STRENGTH_ROAMING[this.mInetCondition];
            } else {
                iconList = TelephonyIcons.TELEPHONY_SIGNAL_STRENGTH[this.mInetCondition];
            }
            this.mPhoneSignalIconId = iconList[iconLevel];
            this.mContentDescriptionPhoneSignal = this.mContext.getString(AccessibilityContentDescriptions.PHONE_SIGNAL_STRENGTH[iconLevel]);
            this.mDataSignalIconId = TelephonyIcons.DATA_SIGNAL_STRENGTH[this.mInetCondition][iconLevel];
        }
    }

    private void updateWifiIcons() {
        int i = 0;
        if (this.mWifiConnected) {
            this.mWifiIconId = WifiIcons.WIFI_SIGNAL_STRENGTH[this.mInetCondition][this.mWifiLevel];
            this.mContentDescriptionWifi = this.mContext.getString(AccessibilityContentDescriptions.WIFI_CONNECTION_STRENGTH[this.mWifiLevel]);
        } else {
            if (this.mDataAndWifiStacked) {
                this.mWifiIconId = 0;
            } else {
                if (this.mWifiEnabled) {
                    i = R.drawable.stat_sys_wifi_signal_null;
                }
                this.mWifiIconId = i;
            }
            this.mContentDescriptionWifi = this.mContext.getString(R.string.accessibility_no_wifi);
        }
    }

    private void updateWifiState(Intent intent) {
        boolean z = true;
        String action = intent.getAction();
        if (action.equals("android.net.wifi.WIFI_STATE_CHANGED")) {
            if (intent.getIntExtra("wifi_state", CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL) != 3) {
                z = false;
            }
            this.mWifiEnabled = z;
        } else if (action.equals("android.net.wifi.STATE_CHANGE")) {
            NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
            boolean wasConnected = this.mWifiConnected;
            if (networkInfo == null || !networkInfo.isConnected()) {
                z = false;
            }
            this.mWifiConnected = z;
            if (this.mWifiConnected && !wasConnected) {
                WifiInfo info = (WifiInfo) intent.getParcelableExtra("wifiInfo");
                if (info == null) {
                    info = this.mWifiManager.getConnectionInfo();
                }
                if (info != null) {
                    this.mWifiSsid = huntForSsid(info);
                } else {
                    this.mWifiSsid = null;
                }
            } else if (!this.mWifiConnected) {
                this.mWifiSsid = null;
            }
        } else if (action.equals("android.net.wifi.RSSI_CHANGED")) {
            this.mWifiRssi = intent.getIntExtra("newRssi", -200);
            this.mWifiLevel = WifiManager.calculateSignalLevel(this.mWifiRssi, WifiIcons.WIFI_LEVEL_COUNT);
        }
        updateWifiIcons();
    }

    private void updateWimaxIcons() {
        if (!this.mIsWimaxEnabled) {
            this.mWimaxIconId = 0;
        } else if (this.mWimaxConnected) {
            if (this.mWimaxIdle) {
                this.mWimaxIconId = WimaxIcons.WIMAX_IDLE;
            } else {
                this.mWimaxIconId = WimaxIcons.WIMAX_SIGNAL_STRENGTH[this.mInetCondition][this.mWimaxSignal];
            }
            this.mContentDescriptionWimax = this.mContext.getString(AccessibilityContentDescriptions.WIMAX_CONNECTION_STRENGTH[this.mWimaxSignal]);
        } else {
            this.mWimaxIconId = WimaxIcons.WIMAX_DISCONNECTED;
            this.mContentDescriptionWimax = this.mContext.getString(R.string.accessibility_no_wimax);
        }
    }

    private final void updateWimaxState(Intent intent) {
        boolean z = true;
        String action = intent.getAction();
        boolean wasConnected = this.mWimaxConnected;
        if (action.equals("android.net.fourG.NET_4G_STATE_CHANGED")) {
            if (intent.getIntExtra("4g_state", CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL) != 3) {
                z = false;
            }
            this.mIsWimaxEnabled = z;
        } else if (action.equals("android.net.wimax.SIGNAL_LEVEL_CHANGED")) {
            this.mWimaxSignal = intent.getIntExtra("newSignalLevel", 0);
        } else if (action.equals("android.net.fourG.wimax.WIMAX_NETWORK_STATE_CHANGED")) {
            boolean z2;
            this.mWimaxState = intent.getIntExtra("WimaxState", CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL);
            this.mWimaxExtraState = intent.getIntExtra("WimaxStateDetail", CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL);
            if (this.mWimaxState == 7) {
                z2 = true;
            } else {
                z2 = false;
            }
            this.mWimaxConnected = z2;
            if (this.mWimaxExtraState != 6) {
                z = false;
            }
            this.mWimaxIdle = z;
        }
        updateDataNetType();
        updateWimaxIcons();
    }

    public void addCombinedLabelView(TextView v) {
        this.mCombinedLabelViews.add(v);
    }

    public void addCombinedSignalIconView(ImageView v) {
        this.mCombinedSignalIconViews.add(v);
    }

    public void addDataDirectionIconView(ImageView v) {
        this.mDataDirectionIconViews.add(v);
    }

    public void addDataDirectionOverlayIconView(ImageView v) {
        this.mDataDirectionOverlayIconViews.add(v);
    }

    public void addDataTypeIconView(ImageView v) {
        this.mDataTypeIconViews.add(v);
    }

    public void addMobileLabelView(TextView v) {
        this.mMobileLabelViews.add(v);
    }

    public void addPhoneSignalIconView(ImageView v) {
        this.mPhoneSignalIconViews.add(v);
    }

    public void addSignalCluster(SignalCluster cluster) {
        this.mSignalClusters.add(cluster);
        refreshSignalCluster(cluster);
    }

    public void addWifiIconView(ImageView v) {
        this.mWifiIconViews.add(v);
    }

    public void addWifiLabelView(TextView v) {
        this.mWifiLabelViews.add(v);
    }

    public void addWimaxIconView(ImageView v) {
        this.mWimaxIconViews.add(v);
    }

    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        pw.println("NetworkController state:");
        String str = "  %s network type %d (%s)";
        Object[] objArr = new Object[3];
        objArr[0] = this.mConnected ? "CONNECTED" : "DISCONNECTED";
        objArr[1] = Integer.valueOf(this.mConnectedNetworkType);
        objArr[2] = this.mConnectedNetworkTypeName;
        pw.println(String.format(str, objArr));
        pw.println("  - telephony ------");
        pw.print("  hasService()=");
        pw.println(hasService());
        pw.print("  mHspaDataDistinguishable=");
        pw.println(this.mHspaDataDistinguishable);
        pw.print("  mDataConnected=");
        pw.println(this.mDataConnected);
        pw.print("  mSimState=");
        pw.println(this.mSimState);
        pw.print("  mPhoneState=");
        pw.println(this.mPhoneState);
        pw.print("  mDataState=");
        pw.println(this.mDataState);
        pw.print("  mDataActivity=");
        pw.println(this.mDataActivity);
        pw.print("  mDataNetType=");
        pw.print(this.mDataNetType);
        pw.print("/");
        pw.println(TelephonyManager.getNetworkTypeName(this.mDataNetType));
        pw.print("  mServiceState=");
        pw.println(this.mServiceState);
        pw.print("  mSignalStrength=");
        pw.println(this.mSignalStrength);
        pw.print("  mLastSignalLevel=");
        pw.println(this.mLastSignalLevel);
        pw.print("  mNetworkName=");
        pw.println(this.mNetworkName);
        pw.print("  mNetworkNameDefault=");
        pw.println(this.mNetworkNameDefault);
        pw.print("  mNetworkNameSeparator=");
        pw.println(this.mNetworkNameSeparator.replace("\n", "\\n"));
        pw.print("  mPhoneSignalIconId=0x");
        pw.print(Integer.toHexString(this.mPhoneSignalIconId));
        pw.print("/");
        pw.println(getResourceName(this.mPhoneSignalIconId));
        pw.print("  mDataDirectionIconId=");
        pw.print(Integer.toHexString(this.mDataDirectionIconId));
        pw.print("/");
        pw.println(getResourceName(this.mDataDirectionIconId));
        pw.print("  mDataSignalIconId=");
        pw.print(Integer.toHexString(this.mDataSignalIconId));
        pw.print("/");
        pw.println(getResourceName(this.mDataSignalIconId));
        pw.print("  mDataTypeIconId=");
        pw.print(Integer.toHexString(this.mDataTypeIconId));
        pw.print("/");
        pw.println(getResourceName(this.mDataTypeIconId));
        pw.println("  - wifi ------");
        pw.print("  mWifiEnabled=");
        pw.println(this.mWifiEnabled);
        pw.print("  mWifiConnected=");
        pw.println(this.mWifiConnected);
        pw.print("  mWifiRssi=");
        pw.println(this.mWifiRssi);
        pw.print("  mWifiLevel=");
        pw.println(this.mWifiLevel);
        pw.print("  mWifiSsid=");
        pw.println(this.mWifiSsid);
        pw.println(String.format("  mWifiIconId=0x%08x/%s", new Object[]{Integer.valueOf(this.mWifiIconId), getResourceName(this.mWifiIconId)}));
        pw.print("  mWifiActivity=");
        pw.println(this.mWifiActivity);
        if (this.mWimaxSupported) {
            pw.println("  - wimax ------");
            pw.print("  mIsWimaxEnabled=");
            pw.println(this.mIsWimaxEnabled);
            pw.print("  mWimaxConnected=");
            pw.println(this.mWimaxConnected);
            pw.print("  mWimaxIdle=");
            pw.println(this.mWimaxIdle);
            pw.println(String.format("  mWimaxIconId=0x%08x/%s", new Object[]{Integer.valueOf(this.mWimaxIconId), getResourceName(this.mWimaxIconId)}));
            pw.println(String.format("  mWimaxSignal=%d", new Object[]{Integer.valueOf(this.mWimaxSignal)}));
            pw.println(String.format("  mWimaxState=%d", new Object[]{Integer.valueOf(this.mWimaxState)}));
            pw.println(String.format("  mWimaxExtraState=%d", new Object[]{Integer.valueOf(this.mWimaxExtraState)}));
        }
        pw.println("  - Bluetooth ----");
        pw.print("  mBtReverseTethered=");
        pw.println(this.mBluetoothTethered);
        pw.println("  - connectivity ------");
        pw.print("  mInetCondition=");
        pw.println(this.mInetCondition);
        pw.println("  - icons ------");
        pw.print("  mLastPhoneSignalIconId=0x");
        pw.print(Integer.toHexString(this.mLastPhoneSignalIconId));
        pw.print("/");
        pw.println(getResourceName(this.mLastPhoneSignalIconId));
        pw.print("  mLastDataDirectionIconId=0x");
        pw.print(Integer.toHexString(this.mLastDataDirectionIconId));
        pw.print("/");
        pw.println(getResourceName(this.mLastDataDirectionIconId));
        pw.print("  mLastDataDirectionOverlayIconId=0x");
        pw.print(Integer.toHexString(this.mLastDataDirectionOverlayIconId));
        pw.print("/");
        pw.println(getResourceName(this.mLastDataDirectionOverlayIconId));
        pw.print("  mLastWifiIconId=0x");
        pw.print(Integer.toHexString(this.mLastWifiIconId));
        pw.print("/");
        pw.println(getResourceName(this.mLastWifiIconId));
        pw.print("  mLastCombinedSignalIconId=0x");
        pw.print(Integer.toHexString(this.mLastCombinedSignalIconId));
        pw.print("/");
        pw.println(getResourceName(this.mLastCombinedSignalIconId));
        pw.print("  mLastDataTypeIconId=0x");
        pw.print(Integer.toHexString(this.mLastDataTypeIconId));
        pw.print("/");
        pw.println(getResourceName(this.mLastDataTypeIconId));
        pw.print("  mLastCombinedLabel=");
        pw.print(this.mLastCombinedLabel);
        pw.println("");
    }

    public boolean hasMobileDataFeature() {
        return this.mHasMobileDataFeature;
    }

    boolean isCdmaEri() {
        if (!(this.mServiceState == null || this.mServiceState.getCdmaEriIconIndex() == 1)) {
            int iconMode = this.mServiceState.getCdmaEriIconMode();
            if (iconMode == 0 || iconMode == 1) {
                return true;
            }
        }
        return DEBUG;
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals("android.net.wifi.RSSI_CHANGED") || action.equals("android.net.wifi.WIFI_STATE_CHANGED") || action.equals("android.net.wifi.STATE_CHANGE")) {
            updateWifiState(intent);
            refreshViews();
        } else if (action.equals("android.intent.action.SIM_STATE_CHANGED")) {
            updateSimState(intent);
            updateDataIcon();
            refreshViews();
        } else if (action.equals("android.provider.Telephony.SPN_STRINGS_UPDATED")) {
            updateNetworkName(intent.getBooleanExtra("showSpn", DEBUG), intent.getStringExtra("spn"), intent.getBooleanExtra("showPlmn", DEBUG), intent.getStringExtra("plmn"));
            refreshViews();
        } else if (action.equals("android.net.conn.CONNECTIVITY_CHANGE") || action.equals("android.net.conn.INET_CONDITION_ACTION")) {
            updateConnectivity(intent);
            refreshViews();
        } else if (action.equals("android.intent.action.CONFIGURATION_CHANGED")) {
            refreshViews();
        } else if (action.equals("android.intent.action.AIRPLANE_MODE")) {
            updateAirplaneMode();
            refreshViews();
        } else if (action.equals("android.net.fourG.NET_4G_STATE_CHANGED") || action.equals("android.net.wimax.SIGNAL_LEVEL_CHANGED") || action.equals("android.net.fourG.wimax.WIMAX_NETWORK_STATE_CHANGED")) {
            updateWimaxState(intent);
            refreshViews();
        } else if (action.equals("android.net.ethernet.STATE_CHANGE") || action.equals("android.net.ethernet.ETHERNET_STATE_CHANGED")) {
            updateEthernetState(intent);
            refreshViews();
        }
    }

    public void refreshSignalCluster(SignalCluster cluster) {
        boolean z = (!this.mWifiEnabled || (!this.mWifiConnected && this.mHasMobileDataFeature)) ? DEBUG : true;
        cluster.setWifiIndicators(z, this.mWifiIconId, this.mWifiActivityIconId, this.mContentDescriptionWifi);
        cluster.setEthernetIndicators(this.mEthernetConnected, this.mEthernetIconId, this.mEthernetActivityIconId, this.mContentDescriptionEthernet);
        if (this.mIsWimaxEnabled && this.mWimaxConnected) {
            int i;
            if (this.mAlwaysShowCdmaRssi) {
                i = this.mPhoneSignalIconId;
            } else {
                i = this.mWimaxIconId;
            }
            cluster.setMobileDataIndicators(true, i, this.mMobileActivityIconId, this.mDataTypeIconId, this.mContentDescriptionWimax, this.mContentDescriptionDataType);
        } else {
            cluster.setMobileDataIndicators(this.mHasMobileDataFeature, this.mShowPhoneRSSIForData ? this.mPhoneSignalIconId : this.mDataSignalIconId, this.mMobileActivityIconId, this.mDataTypeIconId, this.mContentDescriptionPhoneSignal, this.mContentDescriptionDataType);
        }
        cluster.setIsAirplaneMode(this.mAirplaneMode, this.mAirplaneIconId);
    }

    void refreshViews() {
        int N;
        int i;
        ImageView v;
        Context context = this.mContext;
        int i2 = 0;
        int i3 = 0;
        String str = "";
        String str2 = "";
        String str3 = "";
        if (this.mHasMobileDataFeature) {
            if (this.mDataConnected) {
                str3 = this.mNetworkName;
            } else if (this.mConnected) {
                if (hasService()) {
                    str3 = this.mNetworkName;
                } else {
                    str3 = "";
                }
            } else if (this.mEthernetConnected) {
                str3 = context.getString(R.string.ethernet_link);
            } else {
                str3 = context.getString(R.string.status_bar_settings_signal_meter_disconnected);
            }
            if (this.mDataConnected) {
                i2 = this.mDataSignalIconId;
                switch (this.mDataActivity) {
                    case CommandQueue.FLAG_EXCLUDE_SEARCH_PANEL:
                        this.mMobileActivityIconId = 2130837665;
                        break;
                    case CommandQueue.FLAG_EXCLUDE_RECENTS_PANEL:
                        this.mMobileActivityIconId = 2130837668;
                        break;
                    case RecentsCallback.SWIPE_DOWN:
                        this.mMobileActivityIconId = 2130837666;
                        break;
                    default:
                        this.mMobileActivityIconId = 0;
                        break;
                }
                str = str3;
                i3 = this.mMobileActivityIconId;
                i2 = this.mDataSignalIconId;
                this.mContentDescriptionCombinedSignal = this.mContentDescriptionDataType;
            }
        } else {
            this.mPhoneSignalIconId = 0;
            this.mDataSignalIconId = 0;
            str3 = "";
        }
        if (this.mWifiConnected) {
            if (this.mWifiSsid == null) {
                str2 = context.getString(R.string.status_bar_settings_signal_meter_wifi_nossid);
                this.mWifiActivityIconId = 0;
            } else {
                str2 = this.mWifiSsid;
                switch (this.mWifiActivity) {
                    case CommandQueue.FLAG_EXCLUDE_NONE:
                        this.mWifiActivityIconId = 0;
                        break;
                    case CommandQueue.FLAG_EXCLUDE_SEARCH_PANEL:
                        this.mWifiActivityIconId = 2130837672;
                        break;
                    case CommandQueue.FLAG_EXCLUDE_RECENTS_PANEL:
                        this.mWifiActivityIconId = 2130837674;
                        break;
                    case RecentsCallback.SWIPE_DOWN:
                        this.mWifiActivityIconId = 2130837673;
                        break;
                    default:
                        break;
                }
            }
            i3 = this.mWifiActivityIconId;
            str = str2;
            i2 = this.mWifiIconId;
            this.mContentDescriptionCombinedSignal = this.mContentDescriptionWifi;
        } else if (this.mHasMobileDataFeature) {
            str2 = "";
        } else {
            str2 = context.getString(R.string.status_bar_settings_signal_meter_disconnected);
        }
        if (this.mEthernetConnected) {
            str = this.mContext.getString(R.string.ethernet_link);
            i3 = this.mEthernetIconId;
            i2 = this.mEthernetIconId;
            this.mContentDescriptionCombinedSignal = this.mContentDescriptionEthernet;
        }
        if (this.mBluetoothTethered) {
            str = this.mContext.getString(R.string.bluetooth_tethered);
            i2 = this.mBluetoothTetherIconId;
            this.mContentDescriptionCombinedSignal = this.mContext.getString(R.string.accessibility_bluetooth_tether);
        }
        if (this.mConnectedNetworkType == 9 ? true : DEBUG) {
            str = this.mConnectedNetworkTypeName;
        }
        if (this.mAirplaneMode && (this.mServiceState == null || (!hasService() && !this.mServiceState.isEmergencyOnly()))) {
            this.mContentDescriptionPhoneSignal = this.mContext.getString(R.string.accessibility_airplane_mode);
            this.mAirplaneIconId = 2130837664;
            this.mDataTypeIconId = 0;
            this.mDataSignalIconId = 0;
            this.mPhoneSignalIconId = 0;
            if (this.mWifiConnected) {
                str3 = "";
            } else {
                if (this.mHasMobileDataFeature) {
                    str2 = "";
                } else {
                    str2 = context.getString(R.string.status_bar_settings_signal_meter_disconnected);
                    str = str2;
                }
                this.mContentDescriptionCombinedSignal = this.mContentDescriptionPhoneSignal;
                i2 = this.mDataSignalIconId;
            }
        } else if (!(this.mDataConnected || this.mWifiConnected || this.mBluetoothTethered || this.mWimaxConnected || this.mEthernetConnected)) {
            str = context.getString(R.string.status_bar_settings_signal_meter_disconnected);
            i2 = this.mHasMobileDataFeature ? this.mDataSignalIconId : this.mWifiIconId;
            this.mContentDescriptionCombinedSignal = this.mHasMobileDataFeature ? this.mContentDescriptionDataType : this.mContentDescriptionWifi;
            this.mDataTypeIconId = 0;
            if (isCdma()) {
                if (isCdmaEri()) {
                    this.mDataTypeIconId = 2130837626;
                }
            } else if (this.mPhone.isNetworkRoaming()) {
                this.mDataTypeIconId = 2130837626;
            }
        }
        if (!(this.mLastPhoneSignalIconId == this.mPhoneSignalIconId && this.mLastDataDirectionOverlayIconId == i3 && this.mLastWifiIconId == this.mWifiIconId && this.mLastWimaxIconId == this.mWimaxIconId && this.mLastDataTypeIconId == this.mDataTypeIconId && this.mLastAirplaneMode == this.mAirplaneMode)) {
            Iterator i$ = this.mSignalClusters.iterator();
            while (i$.hasNext()) {
                refreshSignalCluster((SignalCluster) i$.next());
            }
        }
        if (this.mLastAirplaneMode != this.mAirplaneMode) {
            this.mLastAirplaneMode = this.mAirplaneMode;
        }
        if (this.mLastPhoneSignalIconId != this.mPhoneSignalIconId) {
            this.mLastPhoneSignalIconId = this.mPhoneSignalIconId;
            N = this.mPhoneSignalIconViews.size();
            i = 0;
            while (i < N) {
                v = (ImageView) this.mPhoneSignalIconViews.get(i);
                if (this.mPhoneSignalIconId == 0) {
                    v.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
                } else {
                    v.setVisibility(0);
                    v.setImageResource(this.mPhoneSignalIconId);
                    v.setContentDescription(this.mContentDescriptionPhoneSignal);
                }
                i++;
            }
        }
        if (this.mLastDataDirectionIconId != this.mDataDirectionIconId) {
            this.mLastDataDirectionIconId = this.mDataDirectionIconId;
            N = this.mDataDirectionIconViews.size();
            i = 0;
            while (i < N) {
                v = this.mDataDirectionIconViews.get(i);
                v.setImageResource(this.mDataDirectionIconId);
                v.setContentDescription(this.mContentDescriptionDataType);
                i++;
            }
        }
        if (this.mLastWifiIconId != this.mWifiIconId) {
            this.mLastWifiIconId = this.mWifiIconId;
            N = this.mWifiIconViews.size();
            i = 0;
            while (i < N) {
                v = this.mWifiIconViews.get(i);
                if (this.mWifiIconId == 0) {
                    v.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
                } else {
                    v.setVisibility(0);
                    v.setImageResource(this.mWifiIconId);
                    v.setContentDescription(this.mContentDescriptionWifi);
                }
                i++;
            }
        }
        if (this.mLastWimaxIconId != this.mWimaxIconId) {
            this.mLastWimaxIconId = this.mWimaxIconId;
            N = this.mWimaxIconViews.size();
            i = 0;
            while (i < N) {
                v = this.mWimaxIconViews.get(i);
                if (this.mWimaxIconId == 0) {
                    v.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
                } else {
                    v.setVisibility(0);
                    v.setImageResource(this.mWimaxIconId);
                    v.setContentDescription(this.mContentDescriptionWimax);
                }
                i++;
            }
        }
        if (this.mLastCombinedSignalIconId != i2) {
            this.mLastCombinedSignalIconId = i2;
            N = this.mCombinedSignalIconViews.size();
            i = 0;
            while (i < N) {
                v = this.mCombinedSignalIconViews.get(i);
                v.setImageResource(i2);
                v.setContentDescription(this.mContentDescriptionCombinedSignal);
                i++;
            }
        }
        if (this.mLastDataTypeIconId != this.mDataTypeIconId) {
            this.mLastDataTypeIconId = this.mDataTypeIconId;
            N = this.mDataTypeIconViews.size();
            i = 0;
            while (i < N) {
                v = this.mDataTypeIconViews.get(i);
                if (this.mDataTypeIconId == 0) {
                    v.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
                } else {
                    v.setVisibility(0);
                    v.setImageResource(this.mDataTypeIconId);
                    v.setContentDescription(this.mContentDescriptionDataType);
                }
                i++;
            }
        }
        if (this.mLastDataDirectionOverlayIconId != i3) {
            this.mLastDataDirectionOverlayIconId = i3;
            N = this.mDataDirectionOverlayIconViews.size();
            i = 0;
            while (i < N) {
                v = this.mDataDirectionOverlayIconViews.get(i);
                if (i3 == 0) {
                    v.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
                } else {
                    v.setVisibility(0);
                    v.setImageResource(i3);
                    v.setContentDescription(this.mContentDescriptionDataType);
                }
                i++;
            }
        }
        if (!this.mLastCombinedLabel.equals(str)) {
            this.mLastCombinedLabel = str;
            N = this.mCombinedLabelViews.size();
            i = 0;
            while (i < N) {
                ((TextView) this.mCombinedLabelViews.get(i)).setText(str);
                i++;
            }
        }
        N = this.mWifiLabelViews.size();
        i = 0;
        while (i < N) {
            TextView v2 = this.mWifiLabelViews.get(i);
            v2.setText(str2);
            if ("".equals(str2)) {
                v2.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
            } else {
                v2.setVisibility(0);
            }
            i++;
        }
        N = this.mMobileLabelViews.size();
        i = 0;
        while (i < N) {
            v2 = this.mMobileLabelViews.get(i);
            v2.setText(str3);
            if ("".equals(str3)) {
                v2.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
            } else {
                v2.setVisibility(0);
            }
            i++;
        }
    }

    public void setStackedMode(boolean stacked) {
        this.mDataAndWifiStacked = true;
    }

    void updateNetworkName(boolean showSpn, String spn, boolean showPlmn, String plmn) {
        StringBuilder str = new StringBuilder();
        boolean something = DEBUG;
        if (showPlmn && plmn != null) {
            str.append(plmn);
            something = true;
        }
        if (showSpn && spn != null) {
            if (something) {
                str.append(this.mNetworkNameSeparator);
            }
            str.append(spn);
            something = true;
        }
        if (something) {
            this.mNetworkName = str.toString();
        } else {
            this.mNetworkName = this.mNetworkNameDefault;
        }
    }
}