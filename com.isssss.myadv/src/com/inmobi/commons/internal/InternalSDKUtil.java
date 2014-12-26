package com.inmobi.commons.internal;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build.VERSION;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.inmobi.commons.InMobi;
import com.inmobi.commons.cache.CacheController;
import com.inmobi.commons.cache.CacheController.Validator;
import com.inmobi.commons.thirdparty.Base64;
import com.inmobi.commons.uid.UID;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.Preconditions;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import org.json.JSONException;
import org.json.JSONObject;

public class InternalSDKUtil {
    public static final String ACTION_CONNECTIVITY_UPDATE = "android.net.conn.CONNECTIVITY_CHANGE";
    public static final String ACTION_RECEIVER_REFERRER = "com.android.vending.INSTALL_REFERRER";
    public static final String ACTION_SHARE_INMID = "com.inmobi.share.id";
    public static final String BASE_LOG_TAG = "[InMobi]";
    public static final String IM_PREF = "impref";
    public static final String INMOBI_SDK_RELEASE_DATE = "20140905";
    public static final String INMOBI_SDK_RELEASE_VERSION = "4.5.0";
    public static final String KEY_LTVID = "ltvid";
    public static final String LOGGING_TAG = "[InMobi]-4.5.0";
    public static final String PRODUCT_COMMONS = "commons";
    private static CommonsConfig a;
    private static String b;
    private static Context c;
    private static Map<String, String> d;
    private static Validator e;
    private static boolean f;

    @TargetApi(17)
    static class a {
        a() {
        }

        static String a(Context context) {
            return WebSettings.getDefaultUserAgent(context);
        }
    }

    static {
        a = new CommonsConfig();
        c = null;
        d = new HashMap();
        e = new d();
        f = true;
    }

    public static Map<String, Object> JSONToMap(JSONObject jSONObject) {
        try {
            Map<String, Object> hashMap = new HashMap();
            Iterator keys = jSONObject.keys();
            while (keys.hasNext()) {
                String str = (String) keys.next();
                try {
                    hashMap.put(str, jSONObject.get(str));
                } catch (JSONException e) {
                }
            }
            return hashMap;
        } catch (Exception e2) {
            return null;
        }
    }

    private static void a() {
        d = UID.getInstance().getMapForEncryption(null);
    }

    static boolean a(Map<String, Object> map) {
        a();
        try {
            CommonsConfig commonsConfig = new CommonsConfig();
            commonsConfig.setFromMap(map);
            a = commonsConfig;
            ApiStatCollector.getLogger().setMetricConfigParams(commonsConfig.getApiStatsConfig());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static byte[] a(byte[] bArr, int i, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException {
        int length;
        byte[] bArr2;
        int i2;
        byte[] bArr3 = new byte[0];
        byte[] bArr4 = new byte[0];
        if (i == 1) {
            length = bArr.length;
            bArr2 = new byte[64];
            bArr3 = bArr4;
            i2 = 0;
        } else {
            length = bArr.length;
            bArr2 = new byte[64];
            bArr3 = bArr4;
            i2 = 0;
        }
        while (i2 < length) {
            if (i2 > 0 && i2 % 64 == 0) {
                int i3;
                bArr3 = a(bArr3, cipher.doFinal(bArr2));
                if (i2 + 64 > length) {
                    i3 = length - i2;
                } else {
                    i3 = 64;
                }
                bArr2 = new byte[i3];
            }
            bArr2[i2 % 64] = bArr[i2];
            i2++;
        }
        return a(bArr3, cipher.doFinal(bArr2));
    }

    private static byte[] a(byte[] bArr, byte[] bArr2) {
        Object obj = new Object[(bArr.length + bArr2.length)];
        System.arraycopy(bArr, 0, obj, 0, bArr.length);
        System.arraycopy(bArr2, 0, obj, bArr.length, bArr2.length);
        return obj;
    }

    public static String byteToHex(byte b) {
        try {
            char[] cArr = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            return new String(new char[]{cArr[(b >> 4) & 15], cArr[b & 15]});
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean checkNetworkAvailibility(Context context) {
        if (context == null) {
            return false;
        }
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            return connectivityManager.getActiveNetworkInfo() == null ? false : connectivityManager.getActiveNetworkInfo().isConnected();
        } catch (Exception e) {
            Log.internal(LOGGING_TAG, "Cannot check network state", e);
            return false;
        }
    }

    public static String convertListToDelimitedString(List<?> list, String str) {
        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        while (i < list.size()) {
            try {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append(str);
                }
                stringBuilder.append(list.get(i));
            } catch (Exception e) {
                Log.internal(LOGGING_TAG, "Exception Converting map to deliminated string " + list.toString(), e);
            }
            i++;
        }
        return stringBuilder.toString();
    }

    public static String encodeMapAndconvertToDelimitedString(Map<String, ? extends Object> map, String str) {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            String str2 = (String) it.next();
            try {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append(str);
                }
                stringBuilder.append(String.format("%s=%s", new Object[]{getURLEncoded(str2), getURLEncoded(map.get(str2).toString())}));
            } catch (Exception e) {
                Log.internal(LOGGING_TAG, "Exception Converting map to deliminated string " + map.toString(), e);
            }
        }
        return stringBuilder.toString();
    }

    public static String encryptRSA(String str) {
        if (str == null || Preconditions.EMPTY_ARGUMENTS.equals(str)) {
            return null;
        }
        try {
            RSAPublicKey rSAPublicKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(new BigInteger("C10F7968CFE2C76AC6F0650C877806D4514DE58FC239592D2385BCE5609A84B2A0FBDAF29B05505EAD1FDFEF3D7209ACBF34B5D0A806DF18147EA9C0337D6B5B", 16), new BigInteger("010001", 16)));
            Cipher instance = Cipher.getInstance("RSA/ECB/nopadding");
            instance.init(1, rSAPublicKey);
            return new String(Base64.encode(a(str.getBytes("UTF-8"), 1, instance), 0));
        } catch (Exception e) {
            Log.debug(LOGGING_TAG, "Exception in encryptRSA", e);
            return null;
        }
    }

    public static boolean getBooleanFromJSON(JSONObject jSONObject, String str, boolean z) {
        try {
            return jSONObject.getBoolean(str);
        } catch (Exception e) {
            try {
                Log.debug(LOGGING_TAG, "JSON with property " + str + " found but has bad datatype(" + jSONObject.get(str).getClass() + "). Reverting to " + z);
                return z;
            } catch (JSONException e2) {
                return z;
            }
        }
    }

    public static boolean getBooleanFromMap(Map<String, Object> map, String str) {
        Object obj = map.get(str);
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue();
        }
        Log.internal(LOGGING_TAG, "Key " + str + " has illegal value");
        throw new IllegalArgumentException();
    }

    public static CommonsConfig getConfig() {
        return a;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getConnectivityType(android.content.Context r6) {
        throw new UnsupportedOperationException("Method not decompiled: com.inmobi.commons.internal.InternalSDKUtil.getConnectivityType(android.content.Context):java.lang.String");
        /*
        r4 = 1;
        r1 = 0;
        r0 = "android.permission.ACCESS_NETWORK_STATE";
        r0 = r6.checkCallingOrSelfPermission(r0);	 Catch:{ Exception -> 0x0041 }
        if (r0 != 0) goto L_0x004f;
    L_0x000a:
        r0 = "connectivity";
        r0 = r6.getSystemService(r0);	 Catch:{ Exception -> 0x0041 }
        r0 = (android.net.ConnectivityManager) r0;	 Catch:{ Exception -> 0x0041 }
        if (r0 == 0) goto L_0x004f;
    L_0x0014:
        r0 = r0.getActiveNetworkInfo();	 Catch:{ Exception -> 0x0041 }
        if (r0 == 0) goto L_0x004f;
    L_0x001a:
        r2 = r0.getType();	 Catch:{ Exception -> 0x0041 }
        r3 = r0.getSubtype();	 Catch:{ Exception -> 0x0041 }
        if (r2 != r4) goto L_0x0027;
    L_0x0024:
        r0 = "wifi";
    L_0x0026:
        return r0;
    L_0x0027:
        if (r2 != 0) goto L_0x004f;
    L_0x0029:
        r0 = "carrier";
        if (r3 != r4) goto L_0x0030;
    L_0x002d:
        r0 = "gprs";
        goto L_0x0026;
    L_0x0030:
        r1 = 2;
        if (r3 != r1) goto L_0x0036;
    L_0x0033:
        r0 = "edge";
        goto L_0x0026;
    L_0x0036:
        r1 = 3;
        if (r3 != r1) goto L_0x003c;
    L_0x0039:
        r0 = "umts";
        goto L_0x0026;
    L_0x003c:
        if (r3 != 0) goto L_0x0026;
    L_0x003e:
        r0 = "carrier";
        goto L_0x0026;
    L_0x0041:
        r0 = move-exception;
        r5 = r0;
        r0 = r1;
        r1 = r5;
    L_0x0045:
        r2 = "[InMobi]-4.5.0";
        r3 = "Error getting the network info";
        com.inmobi.commons.internal.Log.internal(r2, r3, r1);
        goto L_0x0026;
    L_0x004d:
        r1 = move-exception;
        goto L_0x0045;
    L_0x004f:
        r0 = r1;
        goto L_0x0026;
        */
    }

    public static Context getContext() {
        return c;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getDigested(java.lang.String r5, java.lang.String r6) {
        throw new UnsupportedOperationException("Method not decompiled: com.inmobi.commons.internal.InternalSDKUtil.getDigested(java.lang.String, java.lang.String):java.lang.String");
        /*
        if (r5 == 0) goto L_0x000e;
    L_0x0002:
        r0 = "";
        r1 = r5.trim();	 Catch:{ Exception -> 0x0045 }
        r0 = r0.equals(r1);	 Catch:{ Exception -> 0x0045 }
        if (r0 == 0) goto L_0x0011;
    L_0x000e:
        r0 = "TEST_EMULATOR";
    L_0x0010:
        return r0;
    L_0x0011:
        r0 = java.security.MessageDigest.getInstance(r6);	 Catch:{ Exception -> 0x0045 }
        r1 = r5.getBytes();	 Catch:{ Exception -> 0x0045 }
        r0.update(r1);	 Catch:{ Exception -> 0x0045 }
        r1 = r0.digest();	 Catch:{ Exception -> 0x0045 }
        r2 = new java.lang.StringBuffer;	 Catch:{ Exception -> 0x0045 }
        r2.<init>();	 Catch:{ Exception -> 0x0045 }
        r0 = 0;
    L_0x0026:
        r3 = r1.length;	 Catch:{ Exception -> 0x0045 }
        if (r0 >= r3) goto L_0x0040;
    L_0x0029:
        r3 = r1[r0];	 Catch:{ Exception -> 0x0045 }
        r3 = r3 & 255;
        r3 = r3 + 256;
        r4 = 16;
        r3 = java.lang.Integer.toString(r3, r4);	 Catch:{ Exception -> 0x0045 }
        r4 = 1;
        r3 = r3.substring(r4);	 Catch:{ Exception -> 0x0045 }
        r2.append(r3);	 Catch:{ Exception -> 0x0045 }
        r0 = r0 + 1;
        goto L_0x0026;
    L_0x0040:
        r0 = r2.toString();	 Catch:{ Exception -> 0x0045 }
        goto L_0x0010;
    L_0x0045:
        r0 = move-exception;
        r1 = "[InMobi]-4.5.0";
        r2 = "Exception in getting ODIN-1";
        com.inmobi.commons.internal.Log.debug(r1, r2, r0);
        r0 = 0;
        goto L_0x0010;
        */
    }

    public static Map<String, String> getEncodedMap(Map<String, ? extends Object> map) {
        Map<String, String> hashMap = new HashMap();
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            try {
                hashMap.put(getURLEncoded(str), getURLEncoded(map.get(str).toString()));
            } catch (Exception e) {
                Log.internal(LOGGING_TAG, "Exception Map encoding " + map.toString(), e);
            }
        }
        return hashMap;
    }

    public static String getFinalRedirectedUrl(String str) {
        int i = 0;
        String str2 = str;
        while (true) {
            try {
                String str3;
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str2).openConnection();
                httpURLConnection.setRequestProperty("User-Agent", getUserAgent());
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setRequestMethod("GET");
                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode < 300 || responseCode >= 400) {
                    str3 = str2;
                } else {
                    str3 = httpURLConnection.getHeaderField("Location");
                    if (str3 == null) {
                        str3 = str2;
                    } else if (httpURLConnection.getResponseCode() != 200) {
                        int i2 = i + 1;
                        if (i < 5) {
                            i = i2;
                            str2 = str3;
                        }
                    }
                }
                httpURLConnection.disconnect();
                return str3;
            } catch (Exception e) {
                Throwable th = e;
                String str4 = str2;
            }
        }
    }

    public static String getInMobiInternalVersion(String str) {
        String[] split = str.split("[.]");
        StringBuffer stringBuffer = new StringBuffer(Preconditions.EMPTY_ARGUMENTS);
        int i = 0;
        while (i < split.length) {
            try {
                stringBuffer.append("T").append((char) (Integer.valueOf(split[i]).intValue() + 65));
            } catch (NumberFormatException e) {
            }
            i++;
        }
        return stringBuffer.equals(Preconditions.EMPTY_ARGUMENTS) ? Preconditions.EMPTY_ARGUMENTS : stringBuffer.substring(1).toString();
    }

    public static int getIntFromJSON(JSONObject jSONObject, String str, int i) {
        try {
            return jSONObject.getInt(str);
        } catch (Exception e) {
            try {
                Log.debug(LOGGING_TAG, "JSON with property " + str + " found but has bad datatype(" + jSONObject.get(str).getClass() + "). Reverting to " + i);
                return i;
            } catch (JSONException e2) {
                return i;
            }
        }
    }

    public static int getIntFromMap(Map<String, Object> map, String str, int i, long j) {
        Object obj = map.get(str);
        if (obj instanceof Integer) {
            int intValue = ((Integer) obj).intValue();
            if (((long) intValue) <= j && intValue >= i) {
                return intValue;
            }
        }
        Log.internal(LOGGING_TAG, "Key " + str + " has illegal value");
        throw new IllegalArgumentException();
    }

    public static long getLongFromJSON(JSONObject jSONObject, String str, long j) {
        try {
            return jSONObject.getLong(str);
        } catch (Exception e) {
            try {
                Log.debug(LOGGING_TAG, "JSON with property " + str + " found but has bad datatype(" + jSONObject.get(str).getClass() + "). Reverting to " + j);
                return j;
            } catch (JSONException e2) {
                return j;
            }
        }
    }

    public static long getLongFromMap(Map<String, Object> map, String str, long j, long j2) {
        Object obj = map.get(str);
        if (obj instanceof Long) {
            long longValue = ((Long) obj).longValue();
            if (longValue <= j2 && longValue >= j) {
                return longValue;
            }
        }
        if (j < -2147483648L) {
            j = -2147483648L;
        }
        int i = (int) j;
        if (j2 > 2147483647L) {
            j2 = 2147483647L;
        }
        return (long) getIntFromMap(map, str, i, (long) ((int) j2));
    }

    public static String getLtvpSessionId() {
        return getContext().getSharedPreferences("inmobiAppAnalyticsSession", 0).getString("APP_SESSION_ID", null);
    }

    public static Object getObjectFromMap(Map<String, Object> map, String str) {
        Object obj = map.get(str);
        if (obj == null || !obj instanceof Map) {
            Log.internal(LOGGING_TAG, "Key " + str + " has illegal value");
            throw new IllegalArgumentException();
        } else if (!((Map) obj).isEmpty()) {
            return obj;
        } else {
            Log.internal(LOGGING_TAG, "Key " + str + " has empty object as value.");
            throw new IllegalArgumentException();
        }
    }

    public static String getSavedUserAgent() {
        return b;
    }

    public static String getStringFromJSON(JSONObject jSONObject, String str, String str2) {
        try {
            return jSONObject.getString(str);
        } catch (Exception e) {
            try {
                Log.debug(LOGGING_TAG, "JSON with property " + str + " found but has bad datatype(" + jSONObject.get(str).getClass() + "). Reverting to " + str2);
                return str2;
            } catch (JSONException e2) {
                return str2;
            }
        }
    }

    public static String getStringFromMap(Map<String, Object> map, String str) {
        Object obj = map.get(str);
        if (obj instanceof String) {
            return (String) obj;
        }
        Log.internal(LOGGING_TAG, "Key " + str + " has illegal value");
        throw new IllegalArgumentException();
    }

    public static String getURLDecoded(String str, String str2) {
        String str3 = Preconditions.EMPTY_ARGUMENTS;
        try {
            return URLDecoder.decode(str, str2);
        } catch (Exception e) {
            Log.internal(LOGGING_TAG, "Exception URL decoding " + str, e);
            return str3;
        }
    }

    public static String getURLEncoded(String str) {
        String str2 = Preconditions.EMPTY_ARGUMENTS;
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {
            Log.internal(LOGGING_TAG, "Exception URL encoding " + str, e);
            return str2;
        }
    }

    public static String getUserAgent() {
        try {
            if (b == null) {
                if (VERSION.SDK_INT >= 17) {
                    b = a.a(c);
                } else {
                    b = new WebView(c).getSettings().getUserAgentString();
                }
            }
            return b;
        } catch (Exception e) {
            Log.internal(LOGGING_TAG, "Cannot get user agent", e);
            return b;
        }
    }

    public static void initialize(Context context) {
        if (getContext() == null) {
            if (context == null) {
                c.getApplicationContext();
            } else {
                c = context.getApplicationContext();
            }
        }
        if (InMobi.getAppId() != null) {
            if (f) {
                f = false;
                a();
            }
            try {
                a(CacheController.getConfig(PRODUCT_COMMONS, context, d, e).getData());
            } catch (CommonsException e) {
                Log.internal(LOGGING_TAG, "IMCommons config init: IMCommonsException caught. Reason: " + e.toString());
            } catch (Exception e2) {
                Log.internal(LOGGING_TAG, "Exception while getting commons config data.");
            }
        }
    }

    public static boolean isHexString(String str) {
        return str.matches("[0-9A-F]+");
    }

    public static boolean isInitializedSuccessfully() {
        return isInitializedSuccessfully(true);
    }

    public static boolean isInitializedSuccessfully(boolean z) {
        if (getContext() != null && InMobi.getAppId() != null) {
            return true;
        }
        if (z) {
            Log.debug(LOGGING_TAG, "InMobi not initialized. Call InMobi.initialize before using any InMobi API");
        }
        return false;
    }

    public static String mapToJSONs(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        JSONObject jSONObject = new JSONObject();
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            try {
                jSONObject.put(str, map.get(str));
            } catch (JSONException e) {
                Log.internal(LOGGING_TAG, "Unable to convert map to JSON for key " + str);
            }
        }
        return jSONObject.toString();
    }

    public static void populate(Map<String, Object> map, Map<String, Object> map2, boolean z) {
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            if (map2.get(str) == null) {
                map2.put(str, map.get(str));
            }
            Object obj = map.get(str);
            Object obj2 = map2.get(str);
            if (!(obj instanceof Map) || !(obj2 instanceof Map)) {
                map2.put(str, obj);
            } else if (z) {
                populate((Map) obj, (Map) obj2, true);
            } else {
                map2.put(str, obj);
            }
        }
    }

    public static void populate(JSONObject jSONObject, JSONObject jSONObject2, boolean z) throws JSONException {
        Iterator keys = jSONObject.keys();
        while (keys.hasNext()) {
            String str = (String) keys.next();
            try {
                jSONObject2.get(str);
                try {
                    Object obj = jSONObject.get(str);
                    Object obj2 = jSONObject2.get(str);
                } catch (JSONException e) {
                    Log.internal(LOGGING_TAG, "Cannot populate to json exception", e);
                }
                if (!(obj instanceof JSONObject) || !(obj2 instanceof JSONObject)) {
                    jSONObject2.put(str, obj);
                } else if (z) {
                    populate((JSONObject) obj, (JSONObject) obj2, true);
                } else {
                    jSONObject2.put(str, obj);
                }
            } catch (JSONException e2) {
                jSONObject2.put(str, jSONObject.get(str));
            }
        }
    }

    public static JSONObject populateToNewJSON(JSONObject jSONObject, JSONObject jSONObject2, boolean z) throws JSONException {
        JSONObject jSONObject3 = new JSONObject();
        populate(jSONObject2, jSONObject3, false);
        populate(jSONObject, jSONObject3, z);
        return jSONObject3;
    }

    public static Map<String, Object> populateToNewMap(Map map, Map map2, boolean z) {
        Map hashMap = new HashMap();
        populate(map2, hashMap, false);
        populate(map, hashMap, z);
        return hashMap;
    }

    public static void setContext(Context context) {
        if (c == null) {
            c = context.getApplicationContext();
            if (InMobi.getAppId() != null) {
                a();
                try {
                    CacheController.getConfig(PRODUCT_COMMONS, context, d, e);
                } catch (CommonsException e) {
                    Log.internal(PRODUCT_COMMONS, "Unable retrive config for commons product");
                }
            }
        }
    }

    public static String union(String str, String str2) {
        String[] split = str.split(",");
        int i = 0;
        while (i < split.length) {
            if (!str2.contains(split[i])) {
                str2 = str2 + "," + split[i];
            }
            i++;
        }
        return str2;
    }

    public static boolean validateAppId(String str) {
        if (str == null) {
            Log.debug(LOGGING_TAG, "appId is null");
            return false;
        } else if (str.matches("(x)+")) {
            Log.debug(LOGGING_TAG, "appId is all xxxxxxx");
            return false;
        } else if (!Preconditions.EMPTY_ARGUMENTS.equals(str.trim())) {
            return true;
        } else {
            Log.debug(LOGGING_TAG, "appId is all blank");
            return false;
        }
    }

    public static String xorWithKey(String str, String str2) {
        String str3 = Preconditions.EMPTY_ARGUMENTS;
        try {
            byte[] bytes = str.getBytes("UTF-8");
            byte[] bArr = new byte[bytes.length];
            byte[] bytes2 = str2.getBytes("UTF-8");
            int i = 0;
            while (i < bytes.length) {
                bArr[i] = (byte) (bytes[i] ^ bytes2[i % bytes2.length]);
                i++;
            }
            return new String(Base64.encode(bArr, MMAdView.TRANSITION_UP), "UTF-8");
        } catch (Exception e) {
            Log.debug(LOGGING_TAG, "Exception in xor with random integer", e);
            return str3;
        }
    }
}