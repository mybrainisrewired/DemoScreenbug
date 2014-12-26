package com.millennialmedia.android;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.support.v4.media.TransportMediator;
import android.support.v4.os.EnvironmentCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.inmobi.monetization.internal.InvalidManifestConfigException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import org.apache.http.conn.util.InetAddressUtils;

public final class MMSDK {
    private static final String BASE_URL_TRACK_EVENT = "http://ads.mp.mydas.mobi/pixel?id=";
    static final int CACHE_REQUEST_TIMEOUT = 30000;
    static final int CLOSE_ACTIVITY_DURATION = 400;
    static String COMMA = null;
    public static final String DEFAULT_APID = "28911";
    public static final String DEFAULT_BANNER_APID = "28913";
    public static final String DEFAULT_RECT_APID = "28914";
    static final String EMPTY = "";
    static final int HANDSHAKE_REQUEST_TIMEOUT = 3000;
    static final String JSON_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss ZZZZ";
    public static final int LOG_LEVEL_DEBUG = 1;
    public static final int LOG_LEVEL_ERROR = 0;
    public static final int LOG_LEVEL_INFO = 2;
    @Deprecated
    public static final int LOG_LEVEL_INTERNAL = 4;
    @Deprecated
    public static final int LOG_LEVEL_PRIVATE_VERBOSE = 5;
    public static final int LOG_LEVEL_VERBOSE = 3;
    static final int OPEN_ACTIVITY_DURATION = 600;
    static final String PREFS_NAME = "MillennialMediaSettings";
    static final int REQUEST_TIMEOUT = 10000;
    public static final String SDKLOG = "MMSDK";
    public static final String VERSION = "5.3.0-c3980670.a";
    @Deprecated
    static boolean disableAdMinRefresh;
    private static String getMMdidValue;
    private static boolean hasSpeechKit;
    private static boolean isBroadcastingEvents;
    static int logLevel;
    static String macId;
    static Handler mainHandler;
    private static int nextDefaultId;

    static class Event {
        public static final String INTENT_CALENDAR_EVENT = "calendar";
        public static final String INTENT_EMAIL = "email";
        public static final String INTENT_EXTERNAL_BROWSER = "browser";
        public static final String INTENT_MAPS = "geo";
        public static final String INTENT_MARKET = "market";
        public static final String INTENT_PHONE_CALL = "tel";
        public static final String INTENT_STREAMING_VIDEO = "streamingVideo";
        public static final String INTENT_TXT_MESSAGE = "sms";
        private static final String KEY_ERROR = "error";
        static final String KEY_INTENT_TYPE = "intentType";
        static final String KEY_INTERNAL_ID = "internalId";
        static final String KEY_PACKAGE_NAME = "packageName";
        protected static final String TAG;

        static class AnonymousClass_1 implements Runnable {
            final /* synthetic */ String val$logString;

            AnonymousClass_1(String str) {
                this.val$logString = str;
            }

            public void run() {
                try {
                    new HttpGetRequest().get(this.val$logString);
                } catch (Exception e) {
                    MMLog.e(TAG, "Error logging event: ", e);
                }
            }
        }

        static class AnonymousClass_2 implements Runnable {
            final /* synthetic */ MMAdImpl val$adImpl;

            AnonymousClass_2(MMAdImpl mMAdImpl) {
                this.val$adImpl = mMAdImpl;
            }

            public void run() {
                if (this.val$adImpl != null && this.val$adImpl.requestListener != null) {
                    try {
                        this.val$adImpl.requestListener.onSingleTap(this.val$adImpl.getCallingAd());
                    } catch (Exception e) {
                        MMLog.e(SDKLOG, "Exception raised in your RequestListener: ", e);
                    }
                }
            }
        }

        static class AnonymousClass_3 implements Runnable {
            final /* synthetic */ MMAdImpl val$adImpl;

            AnonymousClass_3(MMAdImpl mMAdImpl) {
                this.val$adImpl = mMAdImpl;
            }

            public void run() {
                if (this.val$adImpl != null && this.val$adImpl.requestListener != null) {
                    try {
                        this.val$adImpl.requestListener.MMAdRequestIsCaching(this.val$adImpl.getCallingAd());
                    } catch (Exception e) {
                        MMLog.e(SDKLOG, "Exception raised in your RequestListener: ", e);
                    }
                }
            }
        }

        static class AnonymousClass_4 implements Runnable {
            final /* synthetic */ MMAdImpl val$adImpl;

            AnonymousClass_4(MMAdImpl mMAdImpl) {
                this.val$adImpl = mMAdImpl;
            }

            public void run() {
                if (this.val$adImpl != null && this.val$adImpl.requestListener != null) {
                    try {
                        this.val$adImpl.requestListener.MMAdOverlayLaunched(this.val$adImpl.getCallingAd());
                    } catch (Exception e) {
                        MMLog.e(SDKLOG, "Exception raised in your RequestListener: ", e);
                    }
                }
            }
        }

        static class AnonymousClass_5 implements Runnable {
            final /* synthetic */ MMAdImpl val$adImpl;

            AnonymousClass_5(MMAdImpl mMAdImpl) {
                this.val$adImpl = mMAdImpl;
            }

            public void run() {
                if (this.val$adImpl != null && this.val$adImpl.requestListener != null) {
                    try {
                        this.val$adImpl.requestListener.MMAdOverlayClosed(this.val$adImpl.getCallingAd());
                    } catch (Exception e) {
                        MMLog.e(SDKLOG, "Exception raised in your RequestListener: ", e);
                    }
                }
            }
        }

        static class AnonymousClass_6 implements Runnable {
            final /* synthetic */ MMAdImpl val$adImpl;

            AnonymousClass_6(MMAdImpl mMAdImpl) {
                this.val$adImpl = mMAdImpl;
            }

            public void run() {
                if (this.val$adImpl != null && this.val$adImpl.requestListener != null) {
                    try {
                        this.val$adImpl.requestListener.requestCompleted(this.val$adImpl.getCallingAd());
                    } catch (Exception e) {
                        MMLog.e(SDKLOG, "Exception raised in your RequestListener: ", e);
                    }
                }
            }
        }

        static class AnonymousClass_7 implements Runnable {
            final /* synthetic */ MMAdImpl val$adImpl;
            final /* synthetic */ MMException val$error;

            AnonymousClass_7(MMAdImpl mMAdImpl, MMException mMException) {
                this.val$adImpl = mMAdImpl;
                this.val$error = mMException;
            }

            public void run() {
                if (this.val$adImpl != null && this.val$adImpl.requestListener != null) {
                    try {
                        this.val$adImpl.requestListener.requestFailed(this.val$adImpl.getCallingAd(), this.val$error);
                    } catch (Exception e) {
                        MMLog.e(SDKLOG, "Exception raised in your RequestListener: ", e);
                    }
                }
            }
        }

        static {
            TAG = Event.class.getName();
        }

        Event() {
        }

        static void adSingleTap(MMAdImpl adImpl) {
            if (adImpl != null) {
                MMSDK.runOnUiThread(new AnonymousClass_2(adImpl));
                if (isBroadcastingEvents) {
                    sendIntent(adImpl.getContext(), new Intent(MMBroadcastReceiver.ACTION_OVERLAY_TAP), adImpl.internalId);
                    sendIntent(adImpl.getContext(), new Intent(MMBroadcastReceiver.ACTION_AD_SINGLE_TAP), adImpl.internalId);
                }
            }
        }

        static void displayStarted(MMAdImpl adImpl) {
            if (adImpl == null) {
                MMLog.w(SDKLOG, "No Context in the listener: ");
            } else {
                if (isBroadcastingEvents) {
                    sendIntent(adImpl.getContext(), new Intent(MMBroadcastReceiver.ACTION_DISPLAY_STARTED), adImpl.internalId);
                }
                overlayOpened(adImpl);
            }
        }

        static void fetchStartedCaching(MMAdImpl adImpl) {
            if (adImpl == null) {
                MMLog.w(SDKLOG, "No Context in the listener: ");
            } else {
                MMSDK.runOnUiThread(new AnonymousClass_3(adImpl));
                if (isBroadcastingEvents) {
                    sendIntent(adImpl.getContext(), new Intent(MMBroadcastReceiver.ACTION_FETCH_STARTED_CACHING), adImpl.internalId);
                }
            }
        }

        static void intentStarted(Context context, String intentType, long adImplId) {
            if (isBroadcastingEvents && intentType != null) {
                sendIntent(context, new Intent(MMBroadcastReceiver.ACTION_INTENT_STARTED).putExtra(KEY_INTENT_TYPE, intentType), adImplId);
            }
        }

        protected static void logEvent(String logString) {
            MMLog.d("Logging event to: %s", logString);
            ThreadUtils.execute(new AnonymousClass_1(logString));
        }

        static void overlayClosed(MMAdImpl adImpl) {
            if (adImpl == null) {
                MMLog.w(SDKLOG, "No Context in the listener: ");
            } else {
                MMSDK.runOnUiThread(new AnonymousClass_5(adImpl));
                if (isBroadcastingEvents && adImpl.getContext() != null) {
                    sendIntent(adImpl.getContext(), new Intent(MMBroadcastReceiver.ACTION_OVERLAY_CLOSED), adImpl.internalId);
                }
            }
        }

        static void overlayOpened(MMAdImpl adImpl) {
            if (adImpl == null) {
                MMLog.w(SDKLOG, "No Context in the listener: ");
            } else {
                MMSDK.runOnUiThread(new AnonymousClass_4(adImpl));
                overlayOpenedBroadCast(adImpl.getContext(), adImpl.internalId);
            }
        }

        static void overlayOpenedBroadCast(Context context, long adImplId) {
            if (isBroadcastingEvents) {
                sendIntent(context, new Intent(MMBroadcastReceiver.ACTION_OVERLAY_OPENED), adImplId);
            }
        }

        static void requestCompleted(MMAdImpl adImpl) {
            if (adImpl == null) {
                MMLog.w(SDKLOG, "No Context in the listener: ");
            } else {
                MMSDK.runOnUiThread(new AnonymousClass_6(adImpl));
                if (isBroadcastingEvents) {
                    sendIntent(adImpl.getContext(), new Intent(adImpl.getRequestCompletedAction()), adImpl.internalId);
                }
            }
        }

        static void requestFailed(MMAdImpl adImpl, MMException error) {
            if (adImpl == null) {
                MMLog.w(SDKLOG, "No Context in the listener: ");
            } else {
                MMSDK.runOnUiThread(new AnonymousClass_7(adImpl, error));
                if (isBroadcastingEvents) {
                    sendIntent(adImpl.getContext(), new Intent(adImpl.getRequestFailedAction()).putExtra(KEY_ERROR, error), adImpl.internalId);
                }
            }
        }

        private static final void sendIntent(Context context, Intent intent, long adImplId) {
            if (context != null) {
                String type;
                intent.addCategory(MMBroadcastReceiver.CATEGORY_SDK);
                if (adImplId != -4) {
                    intent.putExtra(KEY_INTERNAL_ID, adImplId);
                }
                intent.putExtra(KEY_PACKAGE_NAME, context.getPackageName());
                if (TextUtils.isEmpty(intent.getStringExtra(KEY_INTENT_TYPE))) {
                    type = EMPTY;
                } else {
                    type = String.format(" Type[%s]", new Object[]{type});
                }
                MMLog.v(SDKLOG, " @@ Intent: " + intent.getAction() + " " + type + " for " + adImplId);
                context.sendBroadcast(intent);
            }
        }
    }

    static class AnonymousClass_2 extends Iterator {
        final /* synthetic */ Context val$context;

        AnonymousClass_2(Context context) {
            this.val$context = context;
        }

        boolean callback(CachedAd ad) {
            String str = SDKLOG;
            String str2 = "%s %s is %son disk. Is %sexpired.";
            Object[] objArr = new Object[4];
            objArr[0] = ad.getTypeString();
            objArr[1] = ad.getId();
            objArr[2] = ad.isOnDisk(this.val$context) ? EMPTY : "not ";
            objArr[3] = ad.isExpired() ? EMPTY : "not ";
            MMLog.i(str, String.format(str2, objArr));
            return true;
        }
    }

    static {
        disableAdMinRefresh = false;
        nextDefaultId = 1897808289;
        COMMA = ",";
        mainHandler = new Handler(Looper.getMainLooper());
        getMMdidValue = null;
        hasSpeechKit = false;
        System.loadLibrary("nmsp_speex");
        hasSpeechKit = true;
    }

    private MMSDK() {
    }

    static String byteArrayToString(byte[] ba) {
        StringBuilder hex = new StringBuilder(ba.length * 2);
        int i = LOG_LEVEL_ERROR;
        while (i < ba.length) {
            hex.append(String.format("%02X", new Object[]{Byte.valueOf(ba[i])}));
            i++;
        }
        return hex.toString();
    }

    static void checkActivity(Context context) {
        try {
            context.getPackageManager().getActivityInfo(new ComponentName(context, "com.millennialmedia.android.MMActivity"), TransportMediator.FLAG_KEY_MEDIA_NEXT);
        } catch (NameNotFoundException e) {
            NameNotFoundException e2 = e;
            MMLog.e(SDKLOG, "Activity MMActivity not declared in AndroidManifest.xml", e2);
            e2.printStackTrace();
            createMissingPermissionDialog(context, "MMActivity class").show();
        }
    }

    static void checkPermissions(Context context) {
        if (context.checkCallingOrSelfPermission("android.permission.INTERNET") == -1) {
            createMissingPermissionDialog(context, "INTERNET permission").show();
        }
        if (context.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") == -1) {
            createMissingPermissionDialog(context, "ACCESS_NETWORK_STATE permission").show();
        }
    }

    private static AlertDialog createMissingPermissionDialog(Context context, String permission) {
        AlertDialog dialog = new Builder(context).create();
        dialog.setTitle("Whoops!");
        dialog.setMessage(String.format("The developer has forgot to declare the %s in the manifest file. Please reach out to the developer to remove this error.", new Object[]{permission}));
        dialog.setButton(InvalidManifestConfigException.MISSING_CONFIG_CHANGES, "OK", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
        return dialog;
    }

    static String getAaid(Info adInfo) {
        return adInfo == null ? null : adInfo.getId();
    }

    static Info getAdvertisingInfo(Context context) {
        Info adInfo = null;
        try {
            return AdvertisingIdClient.getAdvertisingIdInfo(context);
        } catch (IOException e) {
            MMLog.e(SDKLOG, "Unrecoverable error connecting to Google Play services (e.g.,the old version of the service doesnt support getting AdvertisingId", e);
            return adInfo;
        } catch (GooglePlayServicesNotAvailableException e2) {
            MMLog.e(SDKLOG, "Google Play services is not available entirely.", e2);
            return adInfo;
        } catch (IllegalStateException e3) {
            MMLog.e(SDKLOG, "IllegalStateException: ", e3);
            return adInfo;
        } catch (GooglePlayServicesRepairableException e4) {
            MMLog.e(SDKLOG, "Google Play Services is not installed, up-to-date, or enabled", e4);
            return adInfo;
        }
    }

    public static boolean getBroadcastEvents() {
        return isBroadcastingEvents;
    }

    static String getCn(Context context) {
        return ((TelephonyManager) context.getSystemService("phone")).getNetworkOperatorName();
    }

    static Configuration getConfiguration(Context context) {
        return context.getResources().getConfiguration();
    }

    static String getConnectionType(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null) {
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
        if (connectivityManager.getActiveNetworkInfo() == null || !connectivityManager.getActiveNetworkInfo().isConnected()) {
            return "offline";
        }
        int type = connectivityManager.getActiveNetworkInfo().getType();
        int subType = connectivityManager.getActiveNetworkInfo().getSubtype();
        if (type == 1) {
            return "wifi";
        }
        if (type != 0) {
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
        switch (subType) {
            case LOG_LEVEL_DEBUG:
                return "gprs";
            case LOG_LEVEL_INFO:
                return "edge";
            case LOG_LEVEL_VERBOSE:
                return "umts";
            case LOG_LEVEL_INTERNAL:
                return "cdma";
            case LOG_LEVEL_PRIVATE_VERBOSE:
                return "evdo_0";
            case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                return "evdo_a";
            case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                return "1xrtt";
            case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                return "hsdpa";
            case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                return "hsupa";
            case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                return "hspa";
            case ApiEventType.API_MRAID_EXPAND:
                return "iden";
            case ApiEventType.API_MRAID_RESIZE:
                return "evdo_b";
            case ApiEventType.API_MRAID_CLOSE:
                return "lte";
            case ApiEventType.API_MRAID_IS_VIEWABLE:
                return "ehrpd";
            case ApiEventType.API_MRAID_GET_PLACEMENT_TYPE:
                return "hspap";
            default:
                return EnvironmentCompat.MEDIA_UNKNOWN;
        }
    }

    public static int getDefaultAdId() {
        int i;
        synchronized (MMSDK.class) {
            i = nextDefaultId + 1;
            nextDefaultId = i;
        }
        return i;
    }

    static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    private static String getDensityString(Context context) {
        return Float.toString(getDensity(context));
    }

    static String getDpiHeight(Context context) {
        return Integer.toString(context.getResources().getDisplayMetrics().heightPixels);
    }

    static String getDpiWidth(Context context) {
        return Integer.toString(context.getResources().getDisplayMetrics().widthPixels);
    }

    static String getIpAddress(Context context) {
        try {
            StringBuilder ips = new StringBuilder();
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                Enumeration<InetAddress> enumIpAddr = ((NetworkInterface) en.nextElement()).getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        if (ips.length() > 0) {
                            ips.append(',');
                        }
                        String inetAddressHost = inetAddress.getHostAddress().toUpperCase();
                        if (InetAddressUtils.isIPv4Address(inetAddressHost)) {
                            ips.append(inetAddressHost);
                        } else {
                            int delim = inetAddressHost.indexOf(ApiEventType.API_MRAID_GET_AUDIO_VOLUME);
                            ips.append(delim < 0 ? inetAddressHost : inetAddressHost.substring(LOG_LEVEL_ERROR, delim));
                        }
                    }
                }
            }
            return ips.toString();
        } catch (Exception e) {
            MMLog.e(SDKLOG, "Exception getting ip information: ", e);
            return EMPTY;
        }
    }

    @Deprecated
    public static int getLogLevel() {
        return MMLog.getLogLevel();
    }

    static synchronized String getMMdid(Context context) {
        String str = null;
        synchronized (MMSDK.class) {
            if (getMMdidValue != null) {
                str = getMMdidValue;
            } else {
                String mmdid = Secure.getString(context.getContentResolver(), "android_id");
                if (mmdid != null) {
                    StringBuilder mmdidBuilder = new StringBuilder("mmh_");
                    try {
                        mmdidBuilder.append(byteArrayToString(MessageDigest.getInstance("MD5").digest(mmdid.getBytes())));
                        mmdidBuilder.append("_");
                        mmdidBuilder.append(byteArrayToString(MessageDigest.getInstance("SHA1").digest(mmdid.getBytes())));
                        str = mmdidBuilder.toString();
                        getMMdidValue = str;
                    } catch (Exception e) {
                        MMLog.e(SDKLOG, "Exception calculating hash: ", e);
                    }
                }
            }
        }
        return str;
    }

    static String getMcc(Context context) {
        Configuration config = getConfiguration(context);
        if (config.mcc == 0) {
            String networkOperator = getNetworkOperator(context);
            if (networkOperator != null && networkOperator.length() >= 6) {
                return networkOperator.substring(LOG_LEVEL_ERROR, LOG_LEVEL_VERBOSE);
            }
        }
        return String.valueOf(config.mcc);
    }

    static int getMediaVolume(Context context) {
        return ((AudioManager) context.getApplicationContext().getSystemService("audio")).getStreamVolume(LOG_LEVEL_VERBOSE);
    }

    static String getMnc(Context context) {
        Configuration config = getConfiguration(context);
        if (config.mnc == 0) {
            String networkOperator = getNetworkOperator(context);
            if (networkOperator != null && networkOperator.length() >= 6) {
                return networkOperator.substring(LOG_LEVEL_VERBOSE);
            }
        }
        return String.valueOf(config.mnc);
    }

    static String getNetworkOperator(Context context) {
        return ((TelephonyManager) context.getSystemService("phone")).getNetworkOperator();
    }

    static String getOrientation(Context context) {
        switch (context.getResources().getConfiguration().orientation) {
            case LOG_LEVEL_DEBUG:
                return "portrait";
            case LOG_LEVEL_INFO:
                return "landscape";
            case LOG_LEVEL_VERBOSE:
                return "square";
            default:
                return "default";
        }
    }

    static final String getOrientationLocked(Context context) {
        return System.getString(context.getContentResolver(), "accelerometer_rotation").equals("1") ? "false" : "true";
    }

    static boolean getSupportsCalendar() {
        return VERSION.SDK_INT >= 14;
    }

    static String getSupportsSms(Context context) {
        return String.valueOf(context.getPackageManager().hasSystemFeature("android.hardware.telephony"));
    }

    static String getSupportsTel(Context context) {
        return String.valueOf(context.getPackageManager().hasSystemFeature("android.hardware.telephony"));
    }

    static boolean hasMicrophone(Context context) {
        return context.getPackageManager().hasSystemFeature("android.hardware.microphone");
    }

    static boolean hasRecordAudioPermission(Context context) {
        return context.checkCallingOrSelfPermission("android.permission.RECORD_AUDIO") == 0;
    }

    static boolean hasSetTranslationMethod() {
        return Integer.parseInt(VERSION.SDK) >= 11;
    }

    private static String hasSpeechKit(Context context) {
        return (hasSpeechKit && hasRecordAudioPermission(context)) ? "true" : "false";
    }

    public static void initialize(Context context) {
        HandShake handShake = HandShake.sharedHandShake(context);
        handShake.sendInitRequest();
        handShake.startSession();
    }

    static void insertUrlCommonValues(Context context, Map<String, String> paramsMap) {
        MMLog.d(SDKLOG, "executing getIDThread");
        paramsMap.put("density", getDensityString(context));
        paramsMap.put("hpx", getDpiHeight(context));
        paramsMap.put("wpx", getDpiWidth(context));
        paramsMap.put("sk", hasSpeechKit(context));
        paramsMap.put("mic", Boolean.toString(hasMicrophone(context)));
        String aaidValue = null;
        String ateValue = "true";
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) == 0) {
            Info info = getAdvertisingInfo(context);
            if (info != null) {
                aaidValue = getAaid(info);
                if (aaidValue != null && info.isLimitAdTrackingEnabled()) {
                    ateValue = "false";
                }
            }
        }
        if (aaidValue != null) {
            paramsMap.put("aaid", aaidValue);
            paramsMap.put("ate", ateValue);
        } else {
            String mmdid = getMMdid(context);
            if (mmdid != null) {
                paramsMap.put("mmdid", mmdid);
            }
        }
        if (isCachedVideoSupportedOnDevice(context)) {
            paramsMap.put("cachedvideo", "true");
        } else {
            paramsMap.put("cachedvideo", "false");
        }
        if (Build.MODEL != null) {
            paramsMap.put("dm", Build.MODEL);
        }
        if (VERSION.RELEASE != null) {
            paramsMap.put("dv", "Android" + VERSION.RELEASE);
        }
        paramsMap.put("sdkversion", VERSION);
        paramsMap.put("mcc", getMcc(context));
        paramsMap.put("mnc", getMnc(context));
        String cn = getCn(context);
        if (!TextUtils.isEmpty(cn)) {
            paramsMap.put("cn", cn);
        }
        Locale locale = Locale.getDefault();
        if (locale != null) {
            paramsMap.put("language", locale.getLanguage());
            paramsMap.put("country", locale.getCountry());
        }
        try {
            String pkid = context.getPackageName();
            paramsMap.put("pkid", pkid);
            PackageManager pm = context.getPackageManager();
            paramsMap.put("pknm", pm.getApplicationLabel(pm.getApplicationInfo(pkid, 0)).toString());
        } catch (Exception e) {
            MMLog.e(SDKLOG, "Can't insert package information", e);
        }
        String schemes = HandShake.sharedHandShake(context).getSchemesList(context);
        if (schemes != null) {
            paramsMap.put("appsids", schemes);
        }
        String vid = AdCache.getCachedVideoList(context);
        if (vid != null) {
            paramsMap.put("vid", vid);
        }
        try {
            String connectionType = getConnectionType(context);
            StatFs statFs;
            String str;
            if (AdCache.isExternalStorageAvailable(context)) {
                statFs = stat;
                str = AdCache.getCacheDirectory(context).getAbsolutePath();
            } else {
                statFs = stat;
                str = context.getFilesDir().getPath();
            }
            String freeSpace = Long.toString(((long) stat.getAvailableBlocks()) * ((long) stat.getBlockSize()));
            String devicePluggedIn = null;
            String deviceBatteryLevel = null;
            Intent intent = context.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
            if (intent != null) {
                devicePluggedIn = intent.getIntExtra("plugged", 0) == 0 ? "false" : "true";
                deviceBatteryLevel = Integer.toString((int) (((float) intent.getIntExtra("level", 0)) * (100.0f / ((float) intent.getIntExtra("scale", 100)))));
            }
            if (deviceBatteryLevel != null && deviceBatteryLevel.length() > 0) {
                paramsMap.put("bl", deviceBatteryLevel);
            }
            if (devicePluggedIn != null && devicePluggedIn.length() > 0) {
                paramsMap.put("plugged", devicePluggedIn);
            }
            if (freeSpace.length() > 0) {
                paramsMap.put("space", freeSpace);
            }
            if (connectionType != null) {
                paramsMap.put("conn", connectionType);
            }
            String ip = URLEncoder.encode(getIpAddress(context), "UTF-8");
            if (!TextUtils.isEmpty(ip)) {
                paramsMap.put("pip", ip);
            }
        } catch (Exception e2) {
            MMLog.e(SDKLOG, "Exception inserting common parameters: ", e2);
        }
        MMRequest.insertLocation(paramsMap);
    }

    static boolean isCachedVideoSupportedOnDevice(Context context) {
        return context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != -1 && (!VERSION.SDK.equalsIgnoreCase("8") || (Environment.getExternalStorageState().equals("mounted") && AdCache.isExternalEnabled));
    }

    static boolean isConnected(Context context) {
        boolean z = LOG_LEVEL_DEBUG;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null) {
            return false;
        }
        if (connectivityManager.getActiveNetworkInfo() == null || !connectivityManager.getActiveNetworkInfo().isConnected()) {
            z = false;
        }
        return z;
    }

    static boolean isUiThread() {
        return mainHandler.getLooper() == Looper.myLooper();
    }

    static void printDiagnostics(MMAdImpl adImpl) {
        if (adImpl != null) {
            Context context = adImpl.getContext();
            MMLog.i(SDKLOG, String.format("MMAd External ID: %d", new Object[]{Integer.valueOf(adImpl.getId())}));
            MMLog.i(SDKLOG, String.format("MMAd Internal ID: %d", new Object[]{Long.valueOf(adImpl.internalId)}));
            MMLog.i(SDKLOG, String.format("APID: %s", new Object[]{adImpl.apid}));
            String str = SDKLOG;
            String str2 = "SD card is %savailable.";
            Object[] objArr = new Object[1];
            objArr[0] = AdCache.isExternalStorageAvailable(context) ? EMPTY : "not ";
            MMLog.i(str, String.format(str2, objArr));
            if (context != null) {
                MMLog.i(SDKLOG, String.format("Package: %s", new Object[]{context.getPackageName()}));
                MMLog.i(SDKLOG, String.format("MMDID: %s", new Object[]{getMMdid(context)}));
                MMLog.i(SDKLOG, "Permissions:");
                str = SDKLOG;
                str2 = "android.permission.ACCESS_NETWORK_STATE is %spresent";
                objArr = new Object[1];
                objArr[0] = context.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") == -1 ? "not " : EMPTY;
                MMLog.i(str, String.format(str2, objArr));
                str = SDKLOG;
                str2 = "android.permission.INTERNET is %spresent";
                objArr = new Object[1];
                objArr[0] = context.checkCallingOrSelfPermission("android.permission.INTERNET") == -1 ? "not " : EMPTY;
                MMLog.i(str, String.format(str2, objArr));
                str = SDKLOG;
                str2 = "android.permission.WRITE_EXTERNAL_STORAGE is %spresent";
                objArr = new Object[1];
                objArr[0] = context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == -1 ? "not " : EMPTY;
                MMLog.i(str, String.format(str2, objArr));
                str = SDKLOG;
                str2 = "android.permission.VIBRATE is %spresent";
                objArr = new Object[1];
                objArr[0] = context.checkCallingOrSelfPermission("android.permission.VIBRATE") == -1 ? "not " : EMPTY;
                MMLog.i(str, String.format(str2, objArr));
                str = SDKLOG;
                str2 = "android.permission.ACCESS_COARSE_LOCATION is %spresent";
                objArr = new Object[1];
                objArr[0] = context.checkCallingOrSelfPermission("android.permission.ACCESS_COARSE_LOCATION") == -1 ? "not " : EMPTY;
                MMLog.i(str, String.format(str2, objArr));
                str = SDKLOG;
                str2 = "android.permission.ACCESS_FINE_LOCATION is %spresent";
                objArr = new Object[1];
                objArr[0] = context.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION") == -1 ? "not " : EMPTY;
                MMLog.i(str, String.format(str2, objArr));
                MMLog.i(SDKLOG, "Cached Ads:");
                AdCache.iterateCachedAds(context, LOG_LEVEL_INFO, new AnonymousClass_2(context));
            }
        }
    }

    static boolean removeAccelForJira1164() {
        return Integer.parseInt(VERSION.SDK) >= 14;
    }

    public static void resetCache(Context context) {
        AdCache.resetCache(context);
    }

    static void runOnUiThread(Runnable action) {
        if (isUiThread()) {
            action.run();
        } else {
            mainHandler.post(action);
        }
    }

    static void runOnUiThreadDelayed(Runnable action, long delayMillis) {
        mainHandler.postDelayed(action, delayMillis);
    }

    public static void setBroadcastEvents(boolean enable) {
        isBroadcastingEvents = enable;
    }

    @Deprecated
    public static void setLogLevel(int level) {
        switch (level) {
            case LOG_LEVEL_ERROR:
                MMLog.setLogLevel(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES);
            case LOG_LEVEL_DEBUG:
                MMLog.setLogLevel(LOG_LEVEL_VERBOSE);
            case LOG_LEVEL_INFO:
                MMLog.setLogLevel(LOG_LEVEL_INTERNAL);
            case LOG_LEVEL_VERBOSE:
                MMLog.setLogLevel(LOG_LEVEL_INFO);
            default:
                MMLog.setLogLevel(LOG_LEVEL_INTERNAL);
        }
    }

    static synchronized void setMMdid(String value) {
        synchronized (MMSDK.class) {
            getMMdidValue = value;
        }
    }

    static boolean supportsFullScreenInline() {
        return Integer.parseInt(VERSION.SDK) >= 11;
    }

    public static void trackConversion(Context context, String goalId) {
        MMConversionTracker.trackConversion(context, goalId, null);
    }

    public static void trackConversion(Context context, String goalId, MMRequest request) {
        MMConversionTracker.trackConversion(context, goalId, request);
    }

    public static void trackEvent(Context context, String eventId) {
        if (!TextUtils.isEmpty(eventId)) {
            String mmdid = getMMdid(context);
            if (!TextUtils.isEmpty(mmdid)) {
                HttpUtils.executeUrl(BASE_URL_TRACK_EVENT + eventId + "&mmdid=" + mmdid);
            }
        }
    }
}