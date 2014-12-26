package com.inmobi.commons;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import com.inmobi.commons.analytics.events.AnalyticsEventsWrapper;
import com.inmobi.commons.data.DemogInfo;
import com.inmobi.commons.data.LocationInfo;
import com.inmobi.commons.internal.ApplicationFocusManager.FocusChangedListener;
import com.inmobi.commons.internal.InternalSDKUtil;
import com.inmobi.commons.internal.Log;
import com.inmobi.commons.internal.Log.INTERNAL_LOG_LEVEL;
import com.inmobi.commons.uid.UID;
import com.mopub.common.Preconditions;
import java.util.Calendar;

public final class InMobi {
    public static final int EXCLUDE_FB_ID = 4;
    public static final int EXCLUDE_ODIN1 = 2;
    public static final int EXCLUDE_UM5_ID = 8;
    public static final int ID_DEVICE_NONE = 0;
    public static final int INCLUDE_DEFAULT = 1;
    private static String a;

    public enum LOG_LEVEL {
        NONE(0),
        DEBUG(1),
        VERBOSE(2);
        private final int a;

        static {
            NONE = new com.inmobi.commons.InMobi.LOG_LEVEL("NONE", 0, 0);
            DEBUG = new com.inmobi.commons.InMobi.LOG_LEVEL("DEBUG", 1, 1);
            VERBOSE = new com.inmobi.commons.InMobi.LOG_LEVEL("VERBOSE", 2, 2);
            b = new com.inmobi.commons.InMobi.LOG_LEVEL[]{NONE, DEBUG, VERBOSE};
        }

        private LOG_LEVEL(int i) {
            this.a = i;
        }

        public int getValue() {
            return this.a;
        }
    }

    static class a implements FocusChangedListener {
        a() {
        }

        public void onFocusChanged(boolean z) {
            if (z) {
                UID.getInstance().refresh();
                LocationInfo.collectLocationInfo();
                AnalyticsEventsWrapper.getInstance().startSession(a, null);
            } else {
                AnalyticsEventsWrapper.getInstance().endSession(null);
            }
        }
    }

    static {
        a = null;
    }

    private InMobi() {
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void a(android.content.Context r5, java.lang.String r6) {
        throw new UnsupportedOperationException("Method not decompiled: com.inmobi.commons.InMobi.a(android.content.Context, java.lang.String):void");
        /*
        if (r5 != 0) goto L_0x0011;
    L_0x0002:
        r0 = "[InMobi]-4.5.0";
        r1 = "Application Context NULL";
        com.inmobi.commons.internal.Log.debug(r0, r1);	 Catch:{ Exception -> 0x002e }
        r0 = "[InMobi]-4.5.0";
        r1 = "context cannot be null";
        com.inmobi.commons.internal.Log.debug(r0, r1);	 Catch:{ Exception -> 0x002e }
    L_0x0010:
        return;
    L_0x0011:
        r0 = r5.getApplicationContext();	 Catch:{ Exception -> 0x002e }
        com.inmobi.commons.internal.InternalSDKUtil.setContext(r0);	 Catch:{ Exception -> 0x002e }
        if (r6 == 0) goto L_0x0026;
    L_0x001a:
        r1 = "";
        r2 = r6.trim();	 Catch:{ Exception -> 0x002e }
        r1 = r1.equals(r2);	 Catch:{ Exception -> 0x002e }
        if (r1 == 0) goto L_0x0037;
    L_0x0026:
        r0 = "[InMobi]-4.5.0";
        r1 = "appId cannot be blank";
        com.inmobi.commons.internal.Log.debug(r0, r1);	 Catch:{ Exception -> 0x002e }
        goto L_0x0010;
    L_0x002e:
        r0 = move-exception;
        r1 = "[InMobi]-4.5.0";
        r2 = "Exception in initialize";
        com.inmobi.commons.internal.Log.internal(r1, r2, r0);
        goto L_0x0010;
    L_0x0037:
        r1 = com.inmobi.commons.internal.InternalSDKUtil.isInitializedSuccessfully();	 Catch:{ Exception -> 0x002e }
        if (r1 == 0) goto L_0x0041;
    L_0x003d:
        com.inmobi.commons.internal.ThinICE.start(r5);	 Catch:{ Exception -> 0x002e }
        goto L_0x0010;
    L_0x0041:
        r1 = r6.trim();	 Catch:{ Exception -> 0x002e }
        a = r1;	 Catch:{ Exception -> 0x002e }
        com.inmobi.commons.internal.InternalSDKUtil.getUserAgent();	 Catch:{ Exception -> 0x002e }
        com.inmobi.commons.data.LocationInfo.collectLocationInfo();	 Catch:{ Exception -> 0x002e }
        com.inmobi.commons.data.AppInfo.updateAppInfo();	 Catch:{ Exception -> 0x002e }
        com.inmobi.commons.data.DeviceInfo.updateDeviceInfo();	 Catch:{ Exception -> 0x002e }
        r1 = com.inmobi.commons.uid.UID.getInstance();	 Catch:{ Exception -> 0x002e }
        r1.init();	 Catch:{ Exception -> 0x002e }
        r1 = r5.getApplicationContext();	 Catch:{ Exception -> 0x002e }
        r2 = "impref";
        r3 = "version";
        r1 = com.inmobi.commons.internal.FileOperations.getPreferences(r1, r2, r3);	 Catch:{ Exception -> 0x002e }
        if (r1 == 0) goto L_0x0072;
    L_0x0068:
        r2 = getVersion();	 Catch:{ Exception -> 0x002e }
        r1 = r1.equals(r2);	 Catch:{ Exception -> 0x002e }
        if (r1 != 0) goto L_0x0084;
    L_0x0072:
        r1 = r5.getApplicationContext();	 Catch:{ Exception -> 0x002e }
        r2 = "impref";
        r3 = "version";
        r4 = getVersion();	 Catch:{ Exception -> 0x002e }
        com.inmobi.commons.internal.FileOperations.setPreferences(r1, r2, r3, r4);	 Catch:{ Exception -> 0x002e }
        com.inmobi.commons.cache.LocalCache.reset();	 Catch:{ Exception -> 0x002e }
    L_0x0084:
        com.inmobi.commons.internal.ThinICE.start(r5);	 Catch:{ Exception -> 0x002e }
        r1 = "[InMobi]-4.5.0";
        r2 = "InMobi init successful";
        com.inmobi.commons.internal.Log.debug(r1, r2);	 Catch:{ Exception -> 0x002e }
        r1 = com.inmobi.commons.analytics.androidsdk.IMAdTracker.getInstance();	 Catch:{ Exception -> 0x002e }
        r1.init(r0, r6);	 Catch:{ Exception -> 0x002e }
        r0 = com.inmobi.commons.analytics.androidsdk.IMAdTracker.getInstance();	 Catch:{ Exception -> 0x002e }
        r0.reportAppDownloadGoal();	 Catch:{ Exception -> 0x002e }
        com.inmobi.commons.internal.ApplicationFocusManager.init(r5);	 Catch:{ Exception -> 0x002e }
        r0 = new com.inmobi.commons.InMobi$a;	 Catch:{ Exception -> 0x002e }
        r0.<init>();	 Catch:{ Exception -> 0x002e }
        com.inmobi.commons.internal.ApplicationFocusManager.addFocusChangedListener(r0);	 Catch:{ Exception -> 0x002e }
        r0 = com.inmobi.commons.analytics.events.AnalyticsEventsWrapper.getInstance();	 Catch:{ Exception -> 0x002e }
        r1 = a;	 Catch:{ Exception -> 0x002e }
        r2 = 0;
        r0.startSession(r1, r2);	 Catch:{ Exception -> 0x002e }
        r0 = r5.getApplicationContext();	 Catch:{ Exception -> 0x002e }
        com.inmobi.commons.internal.ActivityRecognitionManager.init(r0);	 Catch:{ Exception -> 0x002e }
        r0 = new android.content.IntentFilter;	 Catch:{ Exception -> 0x002e }
        r0.<init>();	 Catch:{ Exception -> 0x002e }
        r1 = "android.net.conn.CONNECTIVITY_CHANGE";
        r0.addAction(r1);	 Catch:{ Exception -> 0x002e }
        r1 = "com.android.vending.INSTALL_REFERRER";
        r0.addAction(r1);	 Catch:{ Exception -> 0x002e }
        r1 = "com.inmobi.share.id";
        r0.addAction(r1);	 Catch:{ Exception -> 0x002e }
        r1 = r5.getApplicationContext();	 Catch:{ Exception -> 0x002e }
        r2 = new com.inmobi.commons.analytics.androidsdk.IMAdTrackerReceiver;	 Catch:{ Exception -> 0x002e }
        r2.<init>();	 Catch:{ Exception -> 0x002e }
        r1.registerReceiver(r2, r0);	 Catch:{ Exception -> 0x002e }
        goto L_0x0010;
        */
    }

    public static void addIDType(IMIDType iMIDType, String str) {
        DemogInfo.addIDType(iMIDType, str);
    }

    public static String getAppId() {
        return a;
    }

    public static String getVersion() {
        return InternalSDKUtil.INMOBI_SDK_RELEASE_VERSION;
    }

    public static void initialize(Activity activity, String str) {
        a(activity, str);
    }

    public static void initialize(Context context, String str) {
        a(context, str);
    }

    public static void removeIDType(IMIDType iMIDType) {
        DemogInfo.removeIDType(iMIDType);
    }

    public static void setAge(int i) {
        DemogInfo.setAge(Integer.valueOf(i));
    }

    public static void setAreaCode(String str) {
        if (str == null || Preconditions.EMPTY_ARGUMENTS.equals(str.trim())) {
            Log.debug(InternalSDKUtil.LOGGING_TAG, "Area code cannot be null");
        } else {
            DemogInfo.setAreaCode(str);
        }
    }

    public static void setCurrentLocation(Location location) {
        if (location != null) {
            DemogInfo.setCurrentLocation(location);
        } else {
            Log.debug(InternalSDKUtil.LOGGING_TAG, "Location cannot be null");
        }
    }

    public static void setDateOfBirth(Calendar calendar) {
        if (calendar != null) {
            DemogInfo.setDateOfBirth(calendar);
        } else {
            Log.debug(InternalSDKUtil.LOGGING_TAG, "Date Of Birth cannot be null");
        }
    }

    public static void setDeviceIDMask(int i) {
        DemogInfo.setDeviceIDMask(i);
        UID.getInstance().setPublisherDeviceIdMaskMap(i);
    }

    public static void setEducation(EducationType educationType) {
        if (educationType != null) {
            DemogInfo.setEducation(educationType);
        }
    }

    public static void setEthnicity(EthnicityType ethnicityType) {
        if (ethnicityType != null) {
            DemogInfo.setEthnicity(ethnicityType);
        }
    }

    public static void setGender(GenderType genderType) {
        if (genderType != null) {
            DemogInfo.setGender(genderType);
        }
    }

    public static void setHasChildren(HasChildren hasChildren) {
        if (hasChildren != null) {
            DemogInfo.setHasChildren(hasChildren);
        }
    }

    public static void setIncome(int i) {
        DemogInfo.setIncome(Integer.valueOf(i));
    }

    public static void setInterests(String str) {
        if (str == null || Preconditions.EMPTY_ARGUMENTS.equals(str.trim())) {
            Log.debug(InternalSDKUtil.LOGGING_TAG, "Interests cannot be null");
        } else {
            DemogInfo.setInterests(str);
        }
    }

    public static void setLanguage(String str) {
        if (str == null || Preconditions.EMPTY_ARGUMENTS.equals(str.trim())) {
            Log.debug(InternalSDKUtil.LOGGING_TAG, "Language cannot be null");
        } else {
            DemogInfo.setLanguage(str);
        }
    }

    public static void setLocationWithCityStateCountry(String str, String str2, String str3) {
        DemogInfo.setLocationWithCityStateCountry(str, str2, str3);
    }

    public static void setLogLevel(LOG_LEVEL log_level) {
        if (log_level == LOG_LEVEL.NONE) {
            Log.setInternalLogLevel(INTERNAL_LOG_LEVEL.NONE);
        } else if (log_level == LOG_LEVEL.DEBUG) {
            Log.setInternalLogLevel(INTERNAL_LOG_LEVEL.DEBUG);
        } else if (log_level == LOG_LEVEL.VERBOSE) {
            Log.setInternalLogLevel(INTERNAL_LOG_LEVEL.VERBOSE);
        } else {
            Log.setInternalLogLevel(INTERNAL_LOG_LEVEL.INTERNAL);
        }
    }

    public static void setMaritalStatus(MaritalStatus maritalStatus) {
        if (maritalStatus != null) {
            DemogInfo.setMaritalStatus(maritalStatus);
        }
    }

    public static void setPostalCode(String str) {
        if (str == null || Preconditions.EMPTY_ARGUMENTS.equals(str.trim())) {
            Log.debug(InternalSDKUtil.LOGGING_TAG, "Postal Code cannot be null");
        } else {
            DemogInfo.setPostalCode(str);
        }
    }

    public static void setSexualOrientation(SexualOrientation sexualOrientation) {
        if (sexualOrientation != null) {
            DemogInfo.setSexualOrientation(sexualOrientation);
        }
    }
}