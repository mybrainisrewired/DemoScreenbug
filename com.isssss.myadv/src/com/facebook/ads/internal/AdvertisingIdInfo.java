package com.facebook.ads.internal;

import android.content.Context;
import android.os.Looper;
import com.mopub.common.GpsHelper;
import java.lang.reflect.Method;

public class AdvertisingIdInfo {
    private static final int CONNECTION_RESULT_SUCCESS = 0;
    private final String id;
    private final boolean limitAdTrackingEnabled;

    private AdvertisingIdInfo(String id, boolean limitAdTrackingEnabled) {
        this.id = id;
        this.limitAdTrackingEnabled = limitAdTrackingEnabled;
    }

    public static AdvertisingIdInfo getAdvertisingIdInfo(Context context, Fb4aData fb4aData) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalStateException("Cannot get advertising info on main thread.");
        } else if (fb4aData != null && !StringUtils.isNullOrEmpty(fb4aData.advertisingId)) {
            return new AdvertisingIdInfo(fb4aData.advertisingId, fb4aData.limitTrackingEnabled);
        } else {
            Method isGooglePlayServicesAvailable = AdUtilities.getMethod("com.google.android.gms.common.GooglePlayServicesUtil", "isGooglePlayServicesAvailable", new Class[]{Context.class});
            if (isGooglePlayServicesAvailable == null) {
                return null;
            }
            Object connectionResult = AdUtilities.invokeMethod(null, isGooglePlayServicesAvailable, new Object[]{context});
            if (connectionResult == null || ((Integer) connectionResult).intValue() != 0) {
                return null;
            }
            Method getAdvertisingIdInfo = AdUtilities.getMethod("com.google.android.gms.ads.identifier.AdvertisingIdClient", "getAdvertisingIdInfo", new Class[]{Context.class});
            if (getAdvertisingIdInfo == null) {
                return null;
            }
            Object advertisingInfo = AdUtilities.invokeMethod(null, getAdvertisingIdInfo, new Object[]{context});
            if (advertisingInfo == null) {
                return null;
            }
            Method getId = AdUtilities.getMethod(advertisingInfo.getClass(), "getId", new Class[0]);
            Method isLimitAdTrackingEnabled = AdUtilities.getMethod(advertisingInfo.getClass(), GpsHelper.IS_LIMIT_AD_TRACKING_ENABLED_KEY, new Class[0]);
            return (getId == null || isLimitAdTrackingEnabled == null) ? null : new AdvertisingIdInfo((String) AdUtilities.invokeMethod(advertisingInfo, getId, new Object[0]), ((Boolean) AdUtilities.invokeMethod(advertisingInfo, isLimitAdTrackingEnabled, new Object[0])).booleanValue());
        }
    }

    public String getId() {
        return this.id;
    }

    public boolean isLimitAdTrackingEnabled() {
        return this.limitAdTrackingEnabled;
    }
}