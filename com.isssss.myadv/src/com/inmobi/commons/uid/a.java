package com.inmobi.commons.uid;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.inmobi.commons.IMIDType;
import com.inmobi.commons.data.DemogInfo;
import com.inmobi.commons.data.DeviceInfo;
import com.inmobi.commons.internal.FileOperations;
import com.inmobi.commons.internal.InternalSDKUtil;
import com.inmobi.commons.internal.Log;
import com.inmobi.monetization.internal.imai.db.ClickDatabaseManager;
import com.mopub.common.GpsHelper;
import com.mopub.common.Preconditions;
import java.util.Date;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONObject;

// compiled from: UIDHelper.java
class a {
    private static final Uri a;
    private static AdvertisingId b;

    // compiled from: UIDHelper.java
    static class a implements Runnable {
        a() {
        }

        public void run() {
            try {
                Class forName = Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient");
                Class forName2 = Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient$Info");
                Object invoke = forName.getDeclaredMethod("getAdvertisingIdInfo", new Class[]{Context.class}).invoke(null, new Object[]{InternalSDKUtil.getContext()});
                String str = (String) forName2.getDeclaredMethod("getId", (Class[]) 0).invoke(invoke, (Object[]) 0);
                b.a(str);
                FileOperations.setPreferences(InternalSDKUtil.getContext(), InternalSDKUtil.IM_PREF, "gpid", str);
                Boolean bool = (Boolean) forName2.getDeclaredMethod(GpsHelper.IS_LIMIT_AD_TRACKING_ENABLED_KEY, (Class[]) 0).invoke(invoke, (Object[]) 0);
                b.a(bool.booleanValue());
                FileOperations.setPreferences(InternalSDKUtil.getContext(), InternalSDKUtil.IM_PREF, "limitadtrck", bool.booleanValue());
            } catch (Exception e) {
                Log.internal(InternalSDKUtil.LOGGING_TAG, "Exception getting advertiser id", e);
            }
        }
    }

    static {
        a = Uri.parse("content://com.facebook.katana.provider.AttributionIdProvider");
        b = null;
    }

    private a() {
    }

    static String a() {
        return "1";
    }

    static String a(String str) {
        return InternalSDKUtil.getDigested(str, "SHA-1");
    }

    protected static void a(Context context) {
        String preferences = FileOperations.getPreferences(context, InternalSDKUtil.IM_PREF, UID.KEY_IMID);
        long longPreferences = FileOperations.getLongPreferences(context, InternalSDKUtil.IM_PREF, ClickDatabaseManager.COLUMN_TIMESTAMP);
        if (preferences == null) {
            preferences = UUID.randomUUID().toString();
            FileOperations.setPreferences(context, InternalSDKUtil.IM_PREF, UID.KEY_IMID, preferences);
            FileOperations.setPreferences(context, InternalSDKUtil.IM_PREF, UID.KEY_AID, DeviceInfo.getAid());
            FileOperations.setPreferences(context, InternalSDKUtil.IM_PREF, UID.KEY_APPENDED_ID, DeviceInfo.getAid());
        }
        if (longPreferences == 0) {
            FileOperations.setPreferences(context, InternalSDKUtil.IM_PREF, ClickDatabaseManager.COLUMN_TIMESTAMP, new Date().getTime());
        }
        Intent intent = new Intent();
        intent.setAction(InternalSDKUtil.ACTION_SHARE_INMID);
        intent.putExtra(UID.KEY_IMID, preferences);
        intent.putExtra(UID.KEY_APPENDED_ID, FileOperations.getPreferences(context, InternalSDKUtil.IM_PREF, UID.KEY_APPENDED_ID));
        intent.putExtra(ClickDatabaseManager.COLUMN_TIMESTAMP, FileOperations.getLongPreferences(context, InternalSDKUtil.IM_PREF, ClickDatabaseManager.COLUMN_TIMESTAMP));
        intent.putExtra(UID.KEY_AID, DeviceInfo.getAid());
        context.sendBroadcast(intent);
    }

    static String b() {
        return DemogInfo.getIDType(IMIDType.ID_SESSION);
    }

    static String b(Context context) {
        return FileOperations.getPreferences(context, InternalSDKUtil.IM_PREF, UID.KEY_IMID);
    }

    static String b(String str) {
        return InternalSDKUtil.getDigested(str, "MD5");
    }

    static String c() {
        return DemogInfo.getIDType(IMIDType.ID_LOGIN);
    }

    static String c(Context context) {
        try {
            JSONObject jSONObject = new JSONObject();
            CharSequence preferences = FileOperations.getPreferences(context, InternalSDKUtil.IM_PREF, UID.KEY_AID);
            if (preferences != null) {
                jSONObject.put("p", preferences);
            }
            String preferences2 = FileOperations.getPreferences(context, InternalSDKUtil.IM_PREF, UID.KEY_APPENDED_ID);
            if (preferences2 != null && preferences2.contains(preferences)) {
                preferences2 = preferences2.replace(preferences, Preconditions.EMPTY_ARGUMENTS);
            }
            Object substring;
            if (!(substring == null || substring.trim() == Preconditions.EMPTY_ARGUMENTS)) {
                if (substring.charAt(0) == ',') {
                    substring = substring.substring(1);
                }
                JSONArray jSONArray = new JSONArray();
                jSONArray.put(substring);
                jSONObject.put("s", jSONArray);
            }
            return jSONObject.toString();
        } catch (Exception e) {
            return null;
        }
    }

    static String d() {
        String str = null;
        try {
            Cursor query = InternalSDKUtil.getContext().getContentResolver().query(a, new String[]{"aid"}, null, null, null);
            if (query == null || !query.moveToFirst()) {
                return str;
            }
            String string = query.getString(query.getColumnIndex("aid"));
            return (string == null || string.length() == 0) ? str : string;
        } catch (Exception e) {
            Log.internal(InternalSDKUtil.LOGGING_TAG, "Unable to retrieve Facebook attrib id: " + e);
            return str;
        }
    }

    static String e() {
        try {
            return (String) Class.forName("com.inmobi.commons.uid.PlatformId").getDeclaredMethod("getAndroidId", new Class[]{Context.class}).invoke(null, new Object[]{InternalSDKUtil.getContext()});
        } catch (Exception e) {
            return null;
        }
    }

    static AdvertisingId f() {
        return b;
    }

    static void g() {
        b = new AdvertisingId();
        b.a(FileOperations.getPreferences(InternalSDKUtil.getContext(), InternalSDKUtil.IM_PREF, "gpid"));
        b.a(FileOperations.getBooleanPreferences(InternalSDKUtil.getContext(), InternalSDKUtil.IM_PREF, "limitadtrck"));
        new Thread(new a()).start();
    }

    static boolean h() {
        boolean z = false;
        try {
            return GooglePlayServicesUtil.isGooglePlayServicesAvailable(InternalSDKUtil.getContext()) == 0 ? true : z;
        } catch (NoClassDefFoundError e) {
            return z;
        }
    }

    static boolean i() {
        AdvertisingId f = f();
        return f != null ? f.isLimitAdTracking() : false;
    }
}