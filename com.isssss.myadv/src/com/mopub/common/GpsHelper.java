package com.mopub.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import com.mopub.common.factories.MethodBuilderFactory;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.AsyncTasks;
import com.mopub.common.util.Reflection;
import java.lang.ref.WeakReference;

public class GpsHelper {
    public static final String ADVERTISING_ID_KEY = "advertisingId";
    public static final int GOOGLE_PLAY_SUCCESS_CODE = 0;
    public static final String IS_LIMIT_AD_TRACKING_ENABLED_KEY = "isLimitAdTrackingEnabled";
    private static String sAdvertisingIdClientClassName;
    private static String sPlayServicesUtilClassName;

    private static class FetchAdvertisingInfoTask extends AsyncTask<Void, Void, Void> {
        private WeakReference<Context> mContextWeakReference;
        private WeakReference<com.mopub.common.GpsHelper.GpsHelperListener> mGpsHelperListenerWeakReference;

        public FetchAdvertisingInfoTask(Context context, com.mopub.common.GpsHelper.GpsHelperListener gpsHelperListener) {
            this.mContextWeakReference = new WeakReference(context);
            this.mGpsHelperListenerWeakReference = new WeakReference(gpsHelperListener);
        }

        protected Void doInBackground(Void... voids) {
            try {
                Context context = (Context) this.mContextWeakReference.get();
                if (context != null) {
                    Object adInfo = MethodBuilderFactory.create(null, "getAdvertisingIdInfo").setStatic(Class.forName(sAdvertisingIdClientClassName)).addParam(Context.class, context).execute();
                    if (adInfo != null) {
                        GpsHelper.updateSharedPreferences(context, adInfo);
                    }
                }
            } catch (Exception e) {
                MoPubLog.d("Unable to obtain AdvertisingIdClient.getAdvertisingIdInfo()");
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            com.mopub.common.GpsHelper.GpsHelperListener gpsHelperListener = (com.mopub.common.GpsHelper.GpsHelperListener) this.mGpsHelperListenerWeakReference.get();
            if (gpsHelperListener != null) {
                gpsHelperListener.onFetchAdInfoCompleted();
            }
        }
    }

    public static interface GpsHelperListener {
        void onFetchAdInfoCompleted();
    }

    static {
        sPlayServicesUtilClassName = "com.google.android.gms.common.GooglePlayServicesUtil";
        sAdvertisingIdClientClassName = "com.google.android.gms.ads.identifier.AdvertisingIdClient";
    }

    public static void asyncFetchAdvertisingInfo(Context context) {
        asyncFetchAdvertisingInfo(context, null);
    }

    public static void asyncFetchAdvertisingInfo(Context context, GpsHelperListener gpsHelperListener) {
        if (Reflection.classFound(sAdvertisingIdClientClassName)) {
            try {
                AsyncTasks.safeExecuteOnExecutor(new FetchAdvertisingInfoTask(context, gpsHelperListener), new Void[0]);
            } catch (Exception e) {
                MoPubLog.d("Error executing FetchAdvertisingInfoTask", e);
                if (gpsHelperListener != null) {
                    gpsHelperListener.onFetchAdInfoCompleted();
                }
            }
        } else if (gpsHelperListener != null) {
            gpsHelperListener.onFetchAdInfoCompleted();
        }
    }

    public static void asyncFetchAdvertisingInfoIfNotCached(Context context, GpsHelperListener gpsHelperListener) {
        if (!isGpsAvailable(context) || isSharedPreferencesPopluated(context)) {
            gpsHelperListener.onFetchAdInfoCompleted();
        } else {
            asyncFetchAdvertisingInfo(context, gpsHelperListener);
        }
    }

    static String getAdvertisingId(Context context) {
        return isGpsAvailable(context) ? SharedPreferencesHelper.getSharedPreferences(context).getString(ADVERTISING_ID_KEY, null) : null;
    }

    static boolean isGpsAvailable(Context context) {
        boolean z = false;
        try {
            Object result = MethodBuilderFactory.create(null, "isGooglePlayServicesAvailable").setStatic(Class.forName(sPlayServicesUtilClassName)).addParam(Context.class, context).execute();
            return (result == null || ((Integer) result).intValue() != 0) ? z : true;
        } catch (Exception e) {
            return z;
        }
    }

    public static boolean isLimitAdTrackingEnabled(Context context) {
        return isGpsAvailable(context) ? SharedPreferencesHelper.getSharedPreferences(context).getBoolean(IS_LIMIT_AD_TRACKING_ENABLED_KEY, false) : false;
    }

    static boolean isSharedPreferencesPopluated(Context context) {
        SharedPreferences sharedPreferences = SharedPreferencesHelper.getSharedPreferences(context);
        return sharedPreferences.contains(ADVERTISING_ID_KEY) && sharedPreferences.contains(IS_LIMIT_AD_TRACKING_ENABLED_KEY);
    }

    static String reflectedGetAdvertisingId(Object adInfo, String defaultValue) {
        try {
            return (String) MethodBuilderFactory.create(adInfo, "getId").execute();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    static boolean reflectedIsLimitAdTrackingEnabled(Object adInfo, boolean defaultValue) {
        try {
            Boolean result = (Boolean) MethodBuilderFactory.create(adInfo, IS_LIMIT_AD_TRACKING_ENABLED_KEY).execute();
            return result != null ? result.booleanValue() : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    @Deprecated
    public static void setClassNamesForTesting() {
        String className = "java.lang.Class";
        sPlayServicesUtilClassName = className;
        sAdvertisingIdClientClassName = className;
    }

    static void updateSharedPreferences(Context context, Object adInfo) {
        String advertisingId = reflectedGetAdvertisingId(adInfo, null);
        SharedPreferencesHelper.getSharedPreferences(context).edit().putString(ADVERTISING_ID_KEY, advertisingId).putBoolean(IS_LIMIT_AD_TRACKING_ENABLED_KEY, reflectedIsLimitAdTrackingEnabled(adInfo, false)).commit();
    }
}