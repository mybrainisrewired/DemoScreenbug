package com.facebook.ads.internal;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import com.facebook.ads.AdSettings;
import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class AdUtilities {
    private static final String ADVERTISING_ID_COLUMN_NAME = "androidid";
    private static final String ATTRIBUTION_ID_COLUMN_NAME = "aid";
    private static final Uri ATTRIBUTION_ID_CONTENT_URI;
    private static final String LIMIT_TRACKING_COLUMN_NAME = "limit_tracking";

    protected static class Fb4aData {
        protected String advertisingId;
        protected String attributionId;
        protected boolean limitTrackingEnabled;

        public Fb4aData(String attributionId, String androidId, boolean limitTrackingEnabled) {
            this.attributionId = attributionId;
            this.advertisingId = androidId;
            this.limitTrackingEnabled = limitTrackingEnabled;
        }
    }

    static {
        ATTRIBUTION_ID_CONTENT_URI = Uri.parse("content://com.facebook.katana.provider.AttributionIdProvider");
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    public static void displayDebugMessage(Context context, String message) {
        if (AdSettings.isTestMode(context)) {
            Log.d("FBAudienceNetworkLog", message + " (displayed for test ads only)");
        }
    }

    public static Fb4aData getFb4aData(ContentResolver contentResolver) {
        Fb4aData fb4aData;
        Cursor c = null;
        try {
            ContentResolver contentResolver2 = contentResolver;
            c = contentResolver2.query(ATTRIBUTION_ID_CONTENT_URI, new String[]{ATTRIBUTION_ID_COLUMN_NAME, ADVERTISING_ID_COLUMN_NAME, LIMIT_TRACKING_COLUMN_NAME}, null, null, null);
            if (c == null || !c.moveToFirst()) {
                fb4aData = new Fb4aData(null, null, false);
                if (c != null) {
                    c.close();
                }
                return fb4aData;
            } else {
                fb4aData = new Fb4aData(c.getString(c.getColumnIndex(ATTRIBUTION_ID_COLUMN_NAME)), c.getString(c.getColumnIndex(ADVERTISING_ID_COLUMN_NAME)), Boolean.valueOf(c.getString(c.getColumnIndex(LIMIT_TRACKING_COLUMN_NAME))).booleanValue());
                if (c != null) {
                    c.close();
                }
                return fb4aData;
            }
        } catch (Exception e) {
            try {
                fb4aData = new Fb4aData(null, null, false);
                if (c != null) {
                    c.close();
                }
            } catch (Throwable th) {
                if (c != null) {
                    c.close();
                }
            }
        }
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        try {
            return clazz.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static Method getMethod(String className, String methodName, Class... parameterTypes) {
        try {
            return getMethod(Class.forName(className), methodName, parameterTypes);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static Object getStringPropertyAsJSON(JSONObject jsonObject, String key) throws JSONException {
        Object value = jsonObject.opt(key);
        if (value != null && value instanceof String) {
            value = new JSONTokener((String) value).nextValue();
        }
        if (value == null || value instanceof JSONObject || value instanceof JSONArray) {
            return value;
        }
        throw new IllegalArgumentException(key);
    }

    public static Object invokeMethod(Object receiver, Method method, Object... args) {
        try {
            return method.invoke(receiver, args);
        } catch (Exception e) {
            return null;
        }
    }

    public static String jsonEncode(Map<String, Object> analogInfo) {
        JSONObject jsonObject = new JSONObject();
        Iterator i$ = analogInfo.entrySet().iterator();
        while (i$.hasNext()) {
            Entry<String, Object> entry = (Entry) i$.next();
            try {
                jsonObject.put((String) entry.getKey(), entry.getValue());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject.toString();
    }

    public static String readStreamToString(InputStream inputStream) throws IOException {
        Throwable th;
        BufferedInputStream bufferedInputStream = null;
        InputStreamReader reader = null;
        try {
            BufferedInputStream bufferedInputStream2 = new BufferedInputStream(inputStream);
            try {
                InputStreamReader reader2 = new InputStreamReader(bufferedInputStream2);
                try {
                    StringBuilder stringBuilder = new StringBuilder();
                    char[] buffer = new char[2048];
                    while (true) {
                        int n = reader2.read(buffer);
                        if (n != -1) {
                            stringBuilder.append(buffer, 0, n);
                        } else {
                            String toString = stringBuilder.toString();
                            closeQuietly(bufferedInputStream2);
                            closeQuietly(reader2);
                            return toString;
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    reader = reader2;
                    bufferedInputStream = bufferedInputStream2;
                    closeQuietly(bufferedInputStream);
                    closeQuietly(reader);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                bufferedInputStream = bufferedInputStream2;
                closeQuietly(bufferedInputStream);
                closeQuietly(reader);
                throw th;
            }
        } catch (Throwable th4) {
            th = th4;
            closeQuietly(bufferedInputStream);
            closeQuietly(reader);
            throw th;
        }
    }
}