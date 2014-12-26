package com.mopub.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.Utils;

public class ClientMetadata {
    private static final String DEVICE_ORIENTATION_LANDSCAPE = "l";
    private static final String DEVICE_ORIENTATION_PORTRAIT = "p";
    private static final String DEVICE_ORIENTATION_SQUARE = "s";
    private static final String DEVICE_ORIENTATION_UNKNOWN = "u";
    private static final String IFA_PREFIX = "ifa:";
    private static final String SHA_PREFIX = "sha:";
    private static final int TYPE_ETHERNET = 9;
    private static final int UNKNOWN_NETWORK = -1;
    private static volatile ClientMetadata sInstance;
    private final String mAppVersion;
    private final ConnectivityManager mConnectivityManager;
    private final Context mContext;
    private final String mDeviceManufacturer;
    private final String mDeviceModel;
    private final String mDeviceProduct;
    private String mIsoCountryCode;
    private String mNetworkOperator;
    private String mNetworkOperatorName;
    private final String mSdkVersion;
    private String mUdid;

    public enum MoPubNetworkType {
        UNKNOWN(0),
        ETHERNET(1),
        WIFI(2),
        MOBILE(3);
        private final int mId;

        static {
            UNKNOWN = new com.mopub.common.ClientMetadata.MoPubNetworkType("UNKNOWN", 0, 0);
            ETHERNET = new com.mopub.common.ClientMetadata.MoPubNetworkType("ETHERNET", 1, 1);
            WIFI = new com.mopub.common.ClientMetadata.MoPubNetworkType("WIFI", 2, 2);
            MOBILE = new com.mopub.common.ClientMetadata.MoPubNetworkType("MOBILE", 3, 3);
            ENUM$VALUES = new com.mopub.common.ClientMetadata.MoPubNetworkType[]{UNKNOWN, ETHERNET, WIFI, MOBILE};
        }

        private MoPubNetworkType(int id) {
            this.mId = id;
        }

        private static com.mopub.common.ClientMetadata.MoPubNetworkType fromAndroidNetworkType(int type) {
            switch (type) {
                case MMAdView.TRANSITION_NONE:
                case MMAdView.TRANSITION_UP:
                case MMAdView.TRANSITION_DOWN:
                case MMAdView.TRANSITION_RANDOM:
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    return MOBILE;
                case MMAdView.TRANSITION_FADE:
                    return WIFI;
                case TYPE_ETHERNET:
                    return ETHERNET;
                default:
                    return UNKNOWN;
            }
        }

        public String toString() {
            return Integer.toString(this.mId);
        }
    }

    private ClientMetadata(Context context) {
        this.mContext = context.getApplicationContext();
        this.mConnectivityManager = (ConnectivityManager) this.mContext.getSystemService("connectivity");
        this.mDeviceManufacturer = Build.MANUFACTURER;
        this.mDeviceModel = Build.MODEL;
        this.mDeviceProduct = Build.PRODUCT;
        this.mSdkVersion = MoPub.SDK_VERSION;
        this.mAppVersion = getAppVersionFromContext(this.mContext);
        TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        this.mNetworkOperator = telephonyManager.getNetworkOperator();
        if (telephonyManager.getPhoneType() == 2 && telephonyManager.getSimState() == 5) {
            this.mNetworkOperator = telephonyManager.getSimOperator();
        }
        this.mIsoCountryCode = telephonyManager.getNetworkCountryIso();
        try {
            this.mNetworkOperatorName = telephonyManager.getNetworkOperatorName();
        } catch (SecurityException e) {
            this.mNetworkOperatorName = null;
        }
        this.mUdid = getUdidFromContext(this.mContext);
    }

    @VisibleForTesting
    public static synchronized void clearForTesting() {
        synchronized (ClientMetadata.class) {
            sInstance = null;
        }
    }

    private static String getAppVersionFromContext(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            MoPubLog.d("Failed to retrieve PackageInfo#versionName.");
            return null;
        }
    }

    public static ClientMetadata getInstance() {
        ClientMetadata result = sInstance;
        if (result == null) {
            synchronized (ClientMetadata.class) {
                result = sInstance;
            }
        }
        return result;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.mopub.common.ClientMetadata getInstance(android.content.Context r4_context) {
        throw new UnsupportedOperationException("Method not decompiled: com.mopub.common.ClientMetadata.getInstance(android.content.Context):com.mopub.common.ClientMetadata");
        /*
        r0 = sInstance;
        if (r0 != 0) goto L_0x0014;
    L_0x0004:
        r3 = com.mopub.common.ClientMetadata.class;
        monitor-enter(r3);
        r0 = sInstance;	 Catch:{ all -> 0x0015 }
        if (r0 != 0) goto L_0x0013;
    L_0x000b:
        r1 = new com.mopub.common.ClientMetadata;	 Catch:{ all -> 0x0015 }
        r1.<init>(r4);	 Catch:{ all -> 0x0015 }
        sInstance = r1;	 Catch:{ all -> 0x0018 }
        r0 = r1;
    L_0x0013:
        monitor-exit(r3);	 Catch:{ all -> 0x0015 }
    L_0x0014:
        return r0;
    L_0x0015:
        r2 = move-exception;
    L_0x0016:
        monitor-exit(r3);	 Catch:{ all -> 0x0015 }
        throw r2;
    L_0x0018:
        r2 = move-exception;
        r0 = r1;
        goto L_0x0016;
        */
    }

    private static String getUdidFromContext(Context context) {
        String androidId = GpsHelper.getAdvertisingId(context);
        if (androidId != null) {
            return new StringBuilder(IFA_PREFIX).append(androidId).toString();
        }
        String deviceId = Secure.getString(context.getContentResolver(), "android_id");
        return new StringBuilder(SHA_PREFIX).append(deviceId == null ? Preconditions.EMPTY_ARGUMENTS : Utils.sha1(deviceId)).toString();
    }

    public MoPubNetworkType getActiveNetworkType() {
        int networkType = UNKNOWN_NETWORK;
        if (this.mContext.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") == 0) {
            NetworkInfo activeNetworkInfo = this.mConnectivityManager.getActiveNetworkInfo();
            networkType = activeNetworkInfo != null ? activeNetworkInfo.getType() : UNKNOWN_NETWORK;
        }
        return MoPubNetworkType.access$2(networkType);
    }

    public String getAppVersion() {
        return this.mAppVersion;
    }

    public float getDensity() {
        return this.mContext.getResources().getDisplayMetrics().density;
    }

    public String getDeviceManufacturer() {
        return this.mDeviceManufacturer;
    }

    public String getDeviceModel() {
        return this.mDeviceModel;
    }

    public String getDeviceProduct() {
        return this.mDeviceProduct;
    }

    public boolean getDoNoTrack() {
        return GpsHelper.isLimitAdTrackingEnabled(this.mContext);
    }

    public String getIsoCountryCode() {
        return this.mIsoCountryCode;
    }

    public String getNetworkOperator() {
        return this.mNetworkOperator;
    }

    public String getNetworkOperatorName() {
        return this.mNetworkOperatorName;
    }

    public String getOrientationString() {
        int orientationInt = this.mContext.getResources().getConfiguration().orientation;
        String orientation = DEVICE_ORIENTATION_UNKNOWN;
        if (orientationInt == 1) {
            return DEVICE_ORIENTATION_PORTRAIT;
        }
        if (orientationInt == 2) {
            return DEVICE_ORIENTATION_LANDSCAPE;
        }
        return orientationInt == 3 ? DEVICE_ORIENTATION_SQUARE : orientation;
    }

    public String getSdkVersion() {
        return this.mSdkVersion;
    }

    public String getUdid() {
        return this.mUdid;
    }
}